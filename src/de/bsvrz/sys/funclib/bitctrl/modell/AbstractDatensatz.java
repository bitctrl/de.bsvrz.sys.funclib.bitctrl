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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

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
 */
public abstract class AbstractDatensatz implements Datensatz {

	/**
	 * Der Empf&auml;nger wird in einer internen Klasse vor dem Anwender
	 * versteckt.
	 */
	private class AsynchronerReceiver implements ClientReceiverInterface {

		/** Die Datenverteilerverbindung. */
		private final ClientDavInterface dav;

		/** Flag ob der Sender aktuell angemeldet ist. */
		private boolean angemeldet;

		/** Die Datenbeschreibung mit der altuell Daten empfangen werden. */
		private DataDescription dbs;

		/**
		 * Konstruiert den Sender.
		 */
		public AsynchronerReceiver() {
			dav = ObjektFactory.getInstanz().getVerbindung();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 */
		public void abmelden() {
			if (angemeldet) {
				dav.unsubscribeReceiver(this, getObjekt().getSystemObject(),
						dbs);
				angemeldet = false;
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 */
		public void anmelden() {
			abmelden();
			dbs = new DataDescription(getAttributGruppe(), getEmpfangsAspekt());
			if (isSenke()) {
				dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
						ReceiveOptions.normal(), ReceiverRole.drain());
			} else {
				dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
						ReceiveOptions.normal(), ReceiverRole.receiver());
			}
			angemeldet = true;
		}

		/**
		 * ermittelt, ob der Receiver f&uuml;r den Empfang der Daten des
		 * Datensatzes angemeldet ist.
		 * 
		 * @return <code>true</code>, wenn die Anmeldung erfolgt ist.
		 */
		public boolean isAngemeldet() {
			return angemeldet;
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
		private boolean sendenErlaubt;

		/** Flag ob der Sender aktuell angemeldet ist. */
		private boolean angemeldet;

		/** Die Datenbeschreibung mit der altuell Daten gesendet werden. */
		private DataDescription dbs;

		/**
		 * Konstruiert den Sender.
		 */
		public SynchronerSender() {
			dav = ObjektFactory.getInstanz().getVerbindung();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 */
		public void abmelden() {
			if (angemeldet) {
				dav.unsubscribeSender(this, getObjekt().getSystemObject(), dbs);
				angemeldet = false;
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 * 
		 * @throws AnmeldeException
		 *             wenn die Anmeldung schief ging.
		 */
		public void anmelden() throws AnmeldeException {
			abmelden();
			dbs = new DataDescription(getAttributGruppe(), getSendeAspekt());
			try {
				if (isQuelle()) {
					dav.subscribeSender(this, getObjekt().getSystemObject(),
							dbs, SenderRole.source());
				} else {
					dav.subscribeSender(this, getObjekt().getSystemObject(),
							dbs, SenderRole.sender());
				}
				angemeldet = true;
			} catch (OneSubscriptionPerSendData ex) {
				angemeldet = false;
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
				sendenErlaubt = true;
			} else {
				sendenErlaubt = false;
			}
		}

		/**
		 * Gibt den Wert des Flags {@code angemeldet} zur&uuml;ck.
		 * 
		 * @return der Wert.
		 */
		public boolean isAngemeldet() {
			return angemeldet;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.ClientSenderInterface#isRequestSupported(de.bsvrz.dav.daf.main.config.SystemObject,
		 *      de.bsvrz.dav.daf.main.DataDescription)
		 */
		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
			if (object.equals(getObjekt().getSystemObject())
					&& dataDescription.equals(dbs)) {
				return true;
			}
			return false;
		}

		/**
		 * F&uuml;gt ein Datum der Warteschlange des Senders hinzu.
		 * 
		 * @param daten
		 *            ein zu sendentes Datum.
		 * @throws DatensendeException
		 *             wenn die Daten nicht gesendet werden konnten.
		 */
		public void sende(Data daten) throws DatensendeException {
			if (!angemeldet) {
				throw new DatensendeException(AbstractDatensatz.this,
						"Datensatz ist zum Senden nicht angemeldet.");
			}
			if (isQuelle() || sendenErlaubt) {
				// Quelle darf immer senden!
				ResultData datensatz = new ResultData(getObjekt()
						.getSystemObject(), dbs, dav.getTime(), daten);
				try {
					dav.sendData(datensatz);
				} catch (DataNotSubscribedException ex) {
					throw new DatensendeException(ex);
				} catch (SendSubscriptionNotConfirmed ex) {
					throw new DatensendeException(ex);
				}
			} else {
				throw new DatensendeException(AbstractDatensatz.this,
						"Die Sendesteuerung hat das Senden verboten.");
			}
		}

		/**
		 * F&uuml;gt ein Datum der Warteschlange des Senders hinzu.
		 * 
		 * @param daten
		 *            ein zu sendentes Datum.
		 * @param zeitstempel
		 *            der Zeitstempel, mit dem die Datengesendet werden.
		 * @throws DatensendeException
		 *             wenn die Daten nicht gesendet werden konnten.
		 */
		public void sende(Data daten, long zeitstempel)
				throws DatensendeException {
			if (!angemeldet) {
				throw new DatensendeException(
						"Der Datensatz wurde noch nicht zum Senden angemeldet.");
			}
			if (sendenErlaubt) {
				ResultData datensatz = new ResultData(getObjekt()
						.getSystemObject(), dbs, zeitstempel, daten);
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

	/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
	private boolean valid;

	/** Das Systemobjekt. */
	private final SystemObjekt objekt;

	/** Liste der registrierten Listener. */
	private final EventListenerList listeners = new EventListenerList();

	/** Kapselt die aktuellen Daten des Datensatzes. */
	private Datum datum;

	/** Der Sendecache. */
	private Data sendeCache;

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
	}

	/**
	 * {@inheritDoc}
	 */
	public void abmeldenSender() {
		sender.abmelden();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void addUpdateListener(DatensatzUpdateListener listener) {
		boolean anmelden;

		anmelden = listeners.getListenerCount(DatensatzUpdateListener.class) == 0;
		listeners.add(DatensatzUpdateListener.class, listener);

		if (anmelden) {
			receiver.anmelden();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void anmeldenSender() throws AnmeldeException {
		sender.anmelden();
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof AbstractDatensatz) {
			AbstractDatensatz ds = (AbstractDatensatz) obj;
			result = getObjekt().equals(ds.getObjekt())
					&& (getAttributGruppe().equals(ds.getAttributGruppe()));
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getDatum()
	 */
	public Datum getDatum() {
		return datum;
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
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#isAngemeldetSender()
	 */
	public boolean isAngemeldetSender() {
		return sender.isAngemeldet();
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isAutoUpdate() {
		return receiver.isAngemeldet();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void removeUpdateListener(
			DatensatzUpdateListener listener) {
		listeners.remove(DatensatzUpdateListener.class, listener);
		if (listeners.getListenerCount(DatensatzUpdateListener.class) <= 0) {
			receiver.abmelden();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendeDaten() throws DatensendeException {
		sender.sende(getSendeCache());
		sendeCache = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendeDaten(long zeitstempel) throws DatensendeException {
		sender.sende(getSendeCache(), zeitstempel);
		sendeCache = null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAttributGruppe() + "[objekt=" + getObjekt() + ", datum="
				+ getDatum() + "]";
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#update()
	 */
	public void update() {
		if (!receiver.isAngemeldet()) {
			ClientDavInterface dav;
			ResultData datensatz;
			DataDescription dbs;

			dav = ObjektFactory.getInstanz().getVerbindung();
			dbs = new DataDescription(getAttributGruppe(), getEmpfangsAspekt());
			datensatz = dav.getData(getObjekt().getSystemObject(), dbs, 0);
			setDaten(datensatz);
		}
	}

	/**
	 * Benachricht registrierte Listener &uuml;ber &Auml;nderungen am Datensatz.
	 * Muss von abgeleiteten Klassen aufgerufen werden, wenn das Datum
	 * ge&auml;ndert wurde.
	 * 
	 * @param neu
	 *            das Datum zum Zeitpunkt des Events.
	 */
	protected synchronized void fireDatensatzAktualisiert(Datum neu) {
		DatensatzUpdateEvent event = new DatensatzUpdateEvent(this, neu);
		for (DatensatzUpdateListener listener : listeners
				.getListeners(DatensatzUpdateListener.class)) {
			listener.datensatzAktualisiert(event);
		}
	}

	/**
	 * Gibt den Aspekt zur&uuml;ck, mit dem Daten empfangen werden.
	 * 
	 * @return der Empfangsaspekt.
	 */
	protected abstract Aspect getEmpfangsAspekt();

	/**
	 * Gibt den Aspekt zur&uuml;ck, mit dem Daten gesendet werden.
	 * 
	 * @return der Sendeaspekt.
	 */
	protected abstract Aspect getSendeAspekt();

	/**
	 * Gibt den Sendecache zur&uuml;ck. Ist der Cache leer (z.&nbsp;B. nach dem
	 * Senden), wird ein neues Datum angelegt. Datensatz&auml;nderungen werden
	 * am Cache durchgef&uuml;hrt und anschlie&szlig;end mit
	 * {@link #sendeDaten()} gesammelt gesendet.
	 * 
	 * @return der Sendecache.
	 * @see #sendeDaten()
	 */
	protected Data getSendeCache() {
		if (sendeCache == null) {
			sendeCache = ObjektFactory.getInstanz().getVerbindung().createData(
					getAttributGruppe());
		}
		return sendeCache;
	}

	/**
	 * Gibt an, ob der Datensatz als Quelle oder Sender angemeldet werden soll.
	 * 
	 * @return {@code true}, wenn die Anmeldung als Quelle erfolgen soll.
	 */
	protected abstract boolean isQuelle();

	/**
	 * Gibt an, ob der Datensatz als Senke oder Empf&auml;ngher angemeldet
	 * werden soll.
	 * 
	 * @return {@code true}, wenn die Anmeldung als Senke erfolgen soll.
	 */
	protected abstract boolean isSenke();

	/**
	 * Legt die aktuellen Daten fest.
	 * 
	 * @param datum
	 *            das neuen Datum.
	 */
	protected void setDatum(Datum datum) {
		this.datum = datum;
	}

	/**
	 * Setzt den Zustand des Datensatzes. Muss von abgeleiteten Klassen
	 * aufgerufen werden, nachdem das Datum ge&auml;ndert wurde.
	 * 
	 * @param valid
	 *            {@code true}, wenn der Datensatz ein g&uuml;ltiges Datum
	 *            enth&auml;lt.
	 */
	protected void setValid(boolean valid) {
		this.valid = valid;
	}

}
