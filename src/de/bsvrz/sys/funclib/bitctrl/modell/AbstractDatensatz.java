/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Implementiert gemeinsame Funktionen der Datens&auml;tze.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public abstract class AbstractDatensatz<T extends Datum> implements
		Datensatz<T> {

	/**
	 * Der Empf&auml;nger wird in einer internen Klasse vor dem Anwender
	 * versteckt.
	 */
	private class AsynchronerReceiver implements ClientReceiverInterface {

		/** Die Datenverteilerverbindung. */
		private final ClientDavInterface dav;

		/** Flag ob der Sender aktuell angemeldet ist. */
		private final Set<Aspect> angemeldet;

		/**
		 * Konstruiert den Sender.
		 */
		public AsynchronerReceiver() {
			dav = ObjektFactory.getInstanz().getVerbindung();
			angemeldet = new HashSet<Aspect>();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 */
		public void abmelden(final Aspect asp) {
			if (angemeldet.contains(asp)) {
				DataDescription dbs = new DataDescription(getAttributGruppe(),
						asp);
				dav.unsubscribeReceiver(this, getObjekt().getSystemObject(),
						dbs);
				angemeldet.remove(asp);
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 */
		public void anmelden(final Aspect asp) {
			abmelden(asp);
			DataDescription dbs = new DataDescription(getAttributGruppe(), asp);
			if (isSenke(asp)) {
				dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
						ReceiveOptions.normal(), ReceiverRole.drain());
			} else {
				dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
						ReceiveOptions.normal(), ReceiverRole.receiver());
			}
			angemeldet.add(asp);
		}

		/**
		 * ermittelt, ob der Receiver f&uuml;r den Empfang der Daten des
		 * Datensatzes angemeldet ist.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 * @return <code>true</code>, wenn die Anmeldung erfolgt ist.
		 */
		public boolean isAngemeldet(final Aspect asp) {
			return angemeldet.contains(asp);
		}

		/**
		 * {@inheritDoc}
		 */
		public void update(final ResultData[] results) {
			synchronized (AbstractDatensatz.this) {
				for (ResultData result : results) {
					setDaten(result);
				}
			}
		}

	}

	/**
	 * Versteckt die Sendelogik des Datenverteilers vor dem Anwender.
	 */
	private class SynchronerSender implements ClientSenderInterface {

		/** Die Datenverteilerverbindung. */
		private final ClientDavInterface dav;

		/** Der Zustand der Sendesteuerung. */
		private final Map<Aspect, Status> sendesteuerung;

		/** Flag ob der Sender aktuell angemeldet ist. */
		private final Set<Aspect> angemeldet;

		/**
		 * Konstruiert den Sender.
		 */
		public SynchronerSender() {
			dav = ObjektFactory.getInstanz().getVerbindung();
			sendesteuerung = new HashMap<Aspect, Status>();
			angemeldet = new HashSet<Aspect>();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts. Noch nicht gesendet Datens&auml;tze
		 * werden aus dem Sendepuffer entfernt.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 */
		public void abmelden(final Aspect asp) {
			if (angemeldet.contains(asp)) {
				DataDescription dbs = new DataDescription(getAttributGruppe(),
						asp);
				dav.unsubscribeSender(this, getObjekt().getSystemObject(), dbs);
				angemeldet.remove(asp);
				sendesteuerung.remove(asp);
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 * @throws AnmeldeException
		 *             wenn die Anmeldung schief ging.
		 */
		public void anmelden(final Aspect asp) throws AnmeldeException {
			abmelden(asp);
			DataDescription dbs = new DataDescription(getAttributGruppe(), asp);
			try {
				if (isQuelle(asp)) {
					dav.subscribeSender(this, getObjekt().getSystemObject(),
							dbs, SenderRole.source());
				} else {
					dav.subscribeSender(this, getObjekt().getSystemObject(),
							dbs, SenderRole.sender());
				}
				angemeldet.add(asp);
			} catch (OneSubscriptionPerSendData ex) {
				angemeldet.remove(asp);
				throw new AnmeldeException(ex);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.ClientSenderInterface#dataRequest(de.bsvrz.dav.daf.main.config.SystemObject,
		 *      de.bsvrz.dav.daf.main.DataDescription, byte)
		 */
		public void dataRequest(final SystemObject object,
				final DataDescription dataDescription, final byte state) {
			synchronized (AbstractDatensatz.this) {
				sendesteuerung.put(dataDescription.getAspect(), Status
						.getStatus(state));
				AbstractDatensatz.this.notify();
			}
		}

		/**
		 * Gibt den Staus der Sendesteuerung zur&uuml;ck.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 * @return der Wert.
		 */
		public Status getStatus(final Aspect asp) {
			return sendesteuerung.get(asp);
		}

		/**
		 * Gibt den Wert des Flags {@code angemeldet} zur&uuml;ck.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 * @return der Wert.
		 */
		public boolean isAngemeldet(final Aspect asp) {
			return angemeldet.contains(asp);
		}

		/**
		 * Wenn Systemobjekt und Attributgruppe &uuml;bereinstimmen, dann wird
		 * {@code true} zur&uuml;gegeben.
		 * <p>
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.ClientSenderInterface#isRequestSupported(de.bsvrz.dav.daf.main.config.SystemObject,
		 *      de.bsvrz.dav.daf.main.DataDescription)
		 */
		public boolean isRequestSupported(final SystemObject object,
				final DataDescription dataDescription) {
			if (object.equals(getObjekt().getSystemObject())
					&& dataDescription.getAttributeGroup().equals(
							getAttributGruppe())) {
				return true;
			}
			return false;
		}

		/**
		 * F&uuml;gt ein Datum der Warteschlange des Senders hinzu.
		 * 
		 * @param d
		 *            ein zu sendentes Datum.
		 * @param asp
		 *            der betroffene Aspekt.
		 * @param zeitstempel
		 *            der Zeitstempel, mit dem die Datengesendet werden.
		 * @throws DatensendeException
		 *             wenn die Daten nicht gesendet werden konnten.
		 */
		public void sende(final Data d, final Aspect asp, final long zeitstempel)
				throws DatensendeException {
			Status status;
			long z;
			DataDescription dbs;
			ResultData datensatz;

			if (!isAngemeldet(asp)) {
				throw new DatensendeException(
						"Der Datensatz wurde noch nicht zum Senden angemeldet.");
			}

			if (sendesteuerung.get(asp) == null) {
				if (isQuelle(asp)) {
					status = Status.START;
				} else {
					throw new DatensendeException(
							"Es liegt noch keine Sendeerlaubnis vor.");
				}
			} else {
				status = sendesteuerung.get(asp);
			}

			switch (status) {
			case STOP:
				if (!isQuelle(asp)) {
					throw new DatensendeException(
							"Die Sendesteuerung hat das Senden verboten.");
				}
				break;
			case KEINE_RECHTE:
				throw new DatensendeException(
						"Die Sendesteuerung hat das Senden verboten, weil keine ausreichenden Rechte vorhanden sind.");
			case ANMELDUNG_UNGUELTIG:
				throw new DatensendeException(
						"Die Sendesteuerung hat das Senden verboten, weil die Anmeldung ungültig ist (doppelte Quelle?).");
			default:
				// Sendesteuerung ist positiv
			}

			z = zeitstempel > 0 ? zeitstempel : dav.getTime();
			dbs = new DataDescription(getAttributGruppe(), asp);
			datensatz = new ResultData(getObjekt().getSystemObject(), dbs, z, d);

			try {
				dav.sendData(datensatz);
			} catch (DataNotSubscribedException ex) {
				throw new DatensendeException(ex);
			} catch (SendSubscriptionNotConfirmed ex) {
				throw new DatensendeException(ex);
			}
		}

	}

	/** Der Empf&auml;nger dieses Datensatzes. */
	private final AsynchronerReceiver receiver;

	/** Der Sender dieses Datensatzes. */
	private final SynchronerSender sender;

	/** Das Systemobjekt. */
	private final SystemObjekt objekt;

	/** Liste der registrierten Listener. */
	private final Map<Aspect, EventListenerList> listeners;

	/** Kapselt die aktuellen Daten des Datensatzes. */
	private final Map<Aspect, T> daten;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            das Systemobjekt, dem der Datensatz zugeordnet ist.
	 */
	public AbstractDatensatz(final SystemObjekt objekt) {
		super();
		this.objekt = objekt;
		receiver = new AsynchronerReceiver();
		sender = new SynchronerSender();
		listeners = new HashMap<Aspect, EventListenerList>();
		daten = new HashMap<Aspect, T>();
	}

	/**
	 * Meldet eine eventuell vorhandene Anmeldung als Sender oder Quelle wieder
	 * ab. Noch nicht gesendet Datens&auml;tze werden aus dem Sendepuffer
	 * entfernt.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 */
	protected void abmeldenSender(final Aspect asp) {
		sender.abmelden(asp);
	}

	/**
	 * liefert die aktuellen Daten des Datensatzes. Es wird der für den Aspekt
	 * gespeicherte Datensatz geliefert, falls keiner existiert werden die Daten
	 * mittels
	 * {@link ClientDavInterface#getData(SystemObject, DataDescription, long)}
	 * abgerufen, gespeichert und geliefert.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return ein Datum, welches die Daten des Datensatzes kapselt.
	 */
	protected T abrufenDatum(final Aspect asp) {
		synchronized (this) {
			T result = daten.get(asp);
			if ((!isAngemeldetSender(asp)) || (result == null)) {
				setDaten(ObjektFactory.getInstanz().getVerbindung().getData(
						getObjekt().getSystemObject(),
						new DataDescription(getAttributGruppe(), asp), 0));
				result = daten.get(asp);
			}
			return result;
		}
	}

	/**
	 * Registriert einen Listener.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param listener
	 *            ein interessierte Listener.
	 */
	protected void addUpdateListener(final Aspect asp,
			final DatensatzUpdateListener listener) {
		boolean anmelden;

		// Falls notwenig Initialisierung
		if (!listeners.containsKey(asp)) {
			listeners.put(asp, new EventListenerList());
		}

		// Listener für Aspekt registrieren
		anmelden = listeners.get(asp).getListenerCount(
				DatensatzUpdateListener.class) == 0;
		listeners.get(asp).add(DatensatzUpdateListener.class, listener);

		// Unter Aspekt als Empfänger anmelden
		if (anmelden) {
			receiver.anmelden(asp);
		}
	}

	/**
	 * Meldet den Datensatz als Sender oder Quelle am Datenverteiler an.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @throws AnmeldeException
	 *             wenn die Anmeldung nicht erfolgreich war.
	 */
	protected void anmeldenSender(final Aspect asp) throws AnmeldeException {
		sender.anmelden(asp);
	}

	/**
	 * Pr&uuml;ft, ob das {@code ResultData} zum Datensatz geh&ouml;rt. Es wird
	 * die Attributgruppe aus der Datenbeschreibung des {@code ResultData} mit
	 * der Attributgruppe des Datensatzes. Au&szlig;erdem wird gepr&uuml;ft, ob
	 * der Aspekt des {@code ResultData} bekannt ist.
	 * <p>
	 * Geh&ouml;hrt das {@code ResultData} nicht zum Datensatz wird eine
	 * {@link IllegalArgumentException} geworfen.
	 * 
	 * @param result
	 *            ein {@code ResultSet}.
	 * @see Datensatz#getAttributGruppe()
	 */
	protected void check(final ResultData result) {
		if (!result.getDataDescription().getAttributeGroup().equals(
				getAttributGruppe())) {
			throw new IllegalArgumentException(
					"Das Datum muss zur Attributgruppe " + getAttributGruppe()
							+ " gehören (gefunden: "
							+ result.getDataDescription().getAttributeGroup()
							+ ").");
		}
		if (!getAspekte().contains(result.getDataDescription().getAspect())) {
			throw new IllegalArgumentException(
					"Unbekanner Aspekt im Datum gefunden (gefunden: "
							+ result.getDataDescription().getAspect()
							+ ", bekannt: " + getAspekte() + ")");
		}
	}

	/**
	 * Zwei Datens&auml;tze sind gleich, wenn sie die selbe Attributgruppe am
	 * gleichen Systemobjekt abbilden.
	 * <p>
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Datensatz) {
			Datensatz<?> ds = (Datensatz<?>) obj;
			return getObjekt().equals(ds.getObjekt())
					&& getAttributGruppe().equals(ds.getAttributGruppe());
		}
		return false;
	}

	/**
	 * Gibt einen leeren Sendecache zur&uuml;ck.
	 * 
	 * @return ein leeres {@code Data}.
	 * @see #konvertiere(Datum)
	 */
	protected Data erzeugeSendeCache() {
		return ObjektFactory.getInstanz().getVerbindung().createData(
				getAttributGruppe());
	}

	/**
	 * Benachricht registrierte Listener &uuml;ber &Auml;nderungen am Datensatz.
	 * Muss von abgeleiteten Klassen aufgerufen werden, wenn das Datum
	 * ge&auml;ndert wurde. Muss von {@link Datensatz#setDaten(ResultData)}
	 * aufgerufen, nachdem das Datum des Datensatzes aktuallisiert wurde.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param datum
	 *            das Datum zum Zeitpunkt des Events.
	 * @see Datensatz#setDaten(ResultData)
	 * @see #setDatum(Aspect, Datum)
	 */
	protected synchronized void fireDatensatzAktualisiert(final Aspect asp,
			final T datum) {
		DatensatzUpdateEvent event = new DatensatzUpdateEvent(this, asp, datum);
		for (DatensatzUpdateListener listener : listeners.get(asp)
				.getListeners(DatensatzUpdateListener.class)) {
			listener.datensatzAktualisiert(event);
		}
	}

	/**
	 * Gibt die verf&uuml;gbaren Aspekte zur&uuml;ck.
	 * 
	 * @return die Menge der verf&uuml;gbaren Aspekte.
	 */
	protected abstract Collection<Aspect> getAspekte();

	/**
	 * Gibt die aktuellen Daten des Datensatzes zur&uuml;ck.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return ein Datum, welches die Daten des Datensatzes kapselt.
	 */
	protected T getDatum(final Aspect asp) {
		return daten.get(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getObjekt()
	 */
	public SystemObjekt getObjekt() {
		return objekt;
	}

	/**
	 * Fragt, ob der Datensatz als Sender oder Quelle Daten senden darf.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Sender oder Quelle Daten
	 *         senden darf.
	 */
	protected Status getStatusSendesteuerung(final Aspect asp) {
		return sender.getStatus(asp);
	}

	/**
	 * Fragt, ob der Datensatz als Sender oder Quelle angemeldet ist.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Sender oder Quelle
	 *         angemeldet ist.
	 */
	protected boolean isAngemeldetSender(final Aspect asp) {
		return sender.isAngemeldet(asp);
	}

	/**
	 * Liest das Flag {@code autoUpdate}.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz neue Daten automatisch vom
	 *         Datenverteiler empf&auml;ngt.
	 */
	protected boolean isAutoUpdate(final Aspect asp) {
		return receiver.isAngemeldet(asp);
	}

	/**
	 * Gibt an, ob der Datensatz als Quelle oder Sender angemeldet werden soll.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn die Anmeldung als Quelle erfolgen soll.
	 */
	protected abstract boolean isQuelle(Aspect asp);

	/**
	 * Gibt an, ob der Datensatz als Senke oder Empf&auml;ngher angemeldet
	 * werden soll.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn die Anmeldung als Senke erfolgen soll.
	 */
	protected abstract boolean isSenke(Aspect asp);

	/**
	 * Erzeugt aus dem Datum ein f&uuml;r den Datenverteiler verst&auml;ndliches
	 * Objekt.
	 * 
	 * @param datum
	 *            ein Datum, welches konvertiert werden soll.
	 * @return der Sendecache.
	 */
	protected abstract Data konvertiere(T datum);

	/**
	 * Deregistriert einen Listener. Beim Versuch einen nicht registrierten
	 * Listener zu entfernen, wird keine Aktion ausgeführt.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param listener
	 *            ein nicht mehr interessierten Listener.
	 */
	protected void removeUpdateListener(final Aspect asp,
			final DatensatzUpdateListener listener) {
		EventListenerList listenerListe = listeners.get(asp);
		if (listenerListe != null) {
			listenerListe.remove(DatensatzUpdateListener.class, listener);
			if (listenerListe.getListenerCount(DatensatzUpdateListener.class) <= 0) {
				receiver.abmelden(asp);
			}
		}
	}

	/**
	 * Veranlasst den Datensatz ein Datum an den Datenverteiler zusenden. Ist
	 * der Zeitstempel des Datums nicht gesetzt oder gleich 0, wird automatisch
	 * der aktuelle Zeitstempel beim Versand verwendet.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param datum
	 *            das zu sendende Datum.
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 * @see #erzeugeDatum()
	 */
	protected void sendeDaten(final Aspect asp, final T datum)
			throws DatensendeException {
		sender.sende(konvertiere(datum), asp, datum.getZeitstempel());
	}

	/**
	 * Veranlasst den Datensatz ein Datum an den Datenverteiler zusenden. Ist
	 * der Zeitstempel des Datums nicht gesetzt oder gleich 0, wird automatisch
	 * der aktuelle Zeitstempel beim Versand verwendet.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param datum
	 *            das zu sendende Datum.
	 * @param timeout
	 *            die Zeitspanne in der die Daten gesendet werden m&uuml;ssen.
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 * @see #erzeugeDatum()
	 */
	protected void sendeDaten(final Aspect asp, final T datum,
			final long timeout) throws DatensendeException {
		synchronized (this) {
			if (getStatusSendesteuerung(asp) != Datensatz.Status.START) {
				try {
					wait(timeout);
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}
			if (getStatusSendesteuerung(asp) != Datensatz.Status.START) {
				throw new DatensendeException("Timeout (" + this + ")");
			}
			sender.sende(konvertiere(datum), asp, datum.getZeitstempel());
		}
	}

	/**
	 * Legt die aktuellen Daten fest. Muss von
	 * {@link Datensatz#setDaten(ResultData)} aufgerufen werden.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param datum
	 *            das neuen Datum.
	 * @see Datensatz#setDaten(ResultData)
	 * @see #fireDatensatzAktualisiert(Aspect, Datum)
	 */
	protected void setDatum(final Aspect asp, final T datum) {
		daten.put(asp, datum);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[objekt=" + getObjekt() + ", daten="
				+ daten + "]";
	}

	/**
	 * Ruft die aktuellen Daten ab und setzt die internen Daten.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 */
	protected void update(final Aspect asp) {
		if (!receiver.isAngemeldet(asp)) {
			ClientDavInterface dav;
			ResultData datensatz;
			DataDescription dbs;

			dav = ObjektFactory.getInstanz().getVerbindung();
			dbs = new DataDescription(getAttributGruppe(), asp);
			datensatz = dav.getData(getObjekt().getSystemObject(), dbs, 0);
			setDaten(datensatz);
		}
	}

}
