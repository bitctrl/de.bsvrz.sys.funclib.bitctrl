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
import java.util.Map;

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
		private final Map<Aspect, Boolean> angemeldet;

		/**
		 * Konstruiert den Sender.
		 */
		public AsynchronerReceiver() {
			dav = ObjektFactory.getInstanz().getVerbindung();
			angemeldet = new HashMap<Aspect, Boolean>();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 */
		public void abmelden(Aspect asp) {
			if (angemeldet.get(asp) != null && angemeldet.get(asp)) {
				DataDescription dbs = new DataDescription(getAttributGruppe(),
						asp);
				dav.unsubscribeReceiver(this, getObjekt().getSystemObject(),
						dbs);
				angemeldet.put(asp, false);
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 */
		public void anmelden(Aspect asp) {
			abmelden(asp);
			DataDescription dbs = new DataDescription(getAttributGruppe(), asp);
			if (isSenke(asp)) {
				dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
						ReceiveOptions.normal(), ReceiverRole.drain());
			} else {
				dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
						ReceiveOptions.normal(), ReceiverRole.receiver());
			}
			angemeldet.put(asp, true);
		}

		/**
		 * ermittelt, ob der Receiver f&uuml;r den Empfang der Daten des
		 * Datensatzes angemeldet ist.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 * @return <code>true</code>, wenn die Anmeldung erfolgt ist.
		 */
		public boolean isAngemeldet(Aspect asp) {
			return angemeldet.get(asp);
		}

		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("synthetic-access")
		public void update(ResultData[] results) {
			for (ResultData result : results) {
				setDaten(result);
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
		private final Map<Aspect, Boolean> sendenErlaubt;

		/** Flag ob der Sender aktuell angemeldet ist. */
		private final Map<Aspect, Boolean> angemeldet;

		/**
		 * Konstruiert den Sender.
		 */
		public SynchronerSender() {
			dav = ObjektFactory.getInstanz().getVerbindung();
			sendenErlaubt = new HashMap<Aspect, Boolean>();
			angemeldet = new HashMap<Aspect, Boolean>();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 */
		public void abmelden(Aspect asp) {
			if (angemeldet.get(asp) != null && angemeldet.get(asp)) {
				DataDescription dbs = new DataDescription(getAttributGruppe(),
						asp);
				dav.unsubscribeSender(this, getObjekt().getSystemObject(), dbs);
				angemeldet.put(asp, false);
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
		public void anmelden(Aspect asp) throws AnmeldeException {
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
				angemeldet.put(asp, true);
			} catch (OneSubscriptionPerSendData ex) {
				angemeldet.put(asp, false);
				throw new AnmeldeException(ex);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.ClientSenderInterface#dataRequest(de.bsvrz.dav.daf.main.config.SystemObject,
		 *      de.bsvrz.dav.daf.main.DataDescription, byte)
		 */
		public void dataRequest(SystemObject object,
				DataDescription dataDescription, byte state) {
			if (isRequestSupported(object, dataDescription)
					&& state == ClientSenderInterface.START_SENDING) {
				sendenErlaubt.put(dataDescription.getAspect(), true);
			} else {
				sendenErlaubt.put(dataDescription.getAspect(), false);
			}
		}

		/**
		 * Gibt den Wert des Flags {@code angemeldet} zur&uuml;ck.
		 * 
		 * @param asp
		 *            der betroffene Aspekt.
		 * @return der Wert.
		 */
		public boolean isAngemeldet(Aspect asp) {
			return angemeldet.get(asp);
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
		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
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
		public void sende(Data d, Aspect asp, long zeitstempel)
				throws DatensendeException {
			if (!angemeldet.get(asp)) {
				throw new DatensendeException(
						"Der Datensatz wurde noch nicht zum Senden angemeldet.");
			}

			if (isQuelle(asp) || sendenErlaubt.get(asp)) {
				long z = zeitstempel > 0 ? zeitstempel : dav.getTime();
				DataDescription dbs = new DataDescription(getAttributGruppe(),
						asp);
				ResultData datensatz = new ResultData(getObjekt()
						.getSystemObject(), dbs, z, d);
				try {
					dav.sendData(datensatz);
				} catch (DataNotSubscribedException ex) {
					throw new DatensendeException(ex);
				} catch (SendSubscriptionNotConfirmed ex) {
					throw new DatensendeException(ex);
				}
			} else {
				throw new DatensendeException(
						"Die Sendesteuerung hat das Senden verboten.");
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
	public AbstractDatensatz(SystemObjekt objekt) {
		super();
		this.objekt = objekt;
		receiver = new AsynchronerReceiver();
		sender = new SynchronerSender();
		listeners = new HashMap<Aspect, EventListenerList>();
		daten = new HashMap<Aspect, T>();
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
	public boolean equals(Object obj) {
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
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getObjekt()
	 */
	public SystemObjekt getObjekt() {
		return objekt;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAttributGruppe() + "[objekt=" + getObjekt() + ", daten="
				+ daten + "]";
	}

	/**
	 * Meldet eine eventuell vorhandene Anmeldung als Sender oder Quelle wieder
	 * ab.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 */
	protected void abmeldenSender(Aspect asp) {
		sender.abmelden(asp);
	}

	/**
	 * Registriert einen Listener.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param listener
	 *            ein interessierte Listener.
	 */
	protected void addUpdateListener(Aspect asp,
			DatensatzUpdateListener listener) {
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
	protected void anmeldenSender(Aspect asp) throws AnmeldeException {
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
	protected void check(ResultData result) {
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
	protected synchronized void fireDatensatzAktualisiert(Aspect asp, T datum) {
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
	protected T getDatum(Aspect asp) {
		return daten.get(asp);
	}

	/**
	 * Fragt, ob der Datensatz als Sender oder Quelle angemeldet ist.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Sender oder Quelle
	 *         angemeldet ist.
	 */
	protected boolean isAngemeldetSender(Aspect asp) {
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
	protected boolean isAutoUpdate(Aspect asp) {
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
	 * Deregistriert einen Listener.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param listener
	 *            ein nicht mehr interessierten Listener.
	 */
	protected void removeUpdateListener(Aspect asp,
			DatensatzUpdateListener listener) {
		listeners.get(asp).remove(DatensatzUpdateListener.class, listener);
		if (listeners.get(asp).getListenerCount(DatensatzUpdateListener.class) <= 0) {
			receiver.abmelden(asp);
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
	protected void sendeDaten(Aspect asp, T datum) throws DatensendeException {
		sender.sende(konvertiere(datum), asp, datum.getZeitstempel());
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
	protected void setDatum(Aspect asp, T datum) {
		daten.put(asp, datum);
	}

	/**
	 * Ruft die aktuellen Daten ab und setzt die internen Daten.
	 * 
	 * @param asp
	 *            der betroffene Aspekt.
	 */
	protected void update(Aspect asp) {
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
