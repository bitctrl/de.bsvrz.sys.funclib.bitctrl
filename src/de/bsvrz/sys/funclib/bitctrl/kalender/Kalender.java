/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.1 Ganglinienprognose
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
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
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse stellt Methoden zur Verf&uuml;gung um ein einfacher Art und
 * Weise Anfragen an den Ereigniskalender zustellen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class Kalender {

	/**
	 * Wickelt die Kommunikation mit dem Datenverteiler ab. L&auml;ft als
	 * eigenst&auml;ndiger Thread.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	private class Kommunikation implements ClientSenderInterface,
			ClientReceiverInterface {

		/** Der Logger. */
		private final Debug kommLogger;

		/** Die zu verwendende Datenverteilerverbindung. */
		private final ClientDavInterface verbindung;

		/** Cached die gestellte Anfragen. */
		private final List<KalenderAnfrage> anfragen;

		/** Das Systemobjekt, an dass die Anfragen geschickt werden. */
		private final SystemObject soKalender;

		/** Datenbeschreibung, mit der Anfragen gestellt werden. */
		private final DataDescription dbsAnfrage;

		/** Datenbeschreibung, mit der Antworten empfangen werden. */
		private final DataDescription dbsAntwort;

		/** Verwaltet die Anmeldungen als Senke der Antworten. */
		private final List<SystemObject> anmeldungen = new ArrayList<SystemObject>();

		/** D&uuml;rfen Anfragen gesendet werden? */
		private boolean sendenErlaubt;

		/**
		 * Initialisiert die Kommunikationsverbindung.
		 * 
		 * @param verbindung
		 *            die f&uuml;r Anfragen zu verwendende
		 *            Datenverteilerverbindung.
		 */
		Kommunikation(ClientDavInterface verbindung) {
			DataModel modell;
			AttributeGroup atg;
			Aspect asp;

			this.verbindung = verbindung;
			anfragen = Collections
					.synchronizedList(new ArrayList<KalenderAnfrage>());
			kommLogger = Debug.getLogger();

			modell = verbindung.getDataModel();

			soKalender = modell.getConfigurationAuthority();
			atg = modell.getAttributeGroup("atg.ereignisKalenderAnfrage");
			asp = modell.getAspect("asp.anfrage");
			dbsAnfrage = new DataDescription(atg, asp);

			atg = modell.getAttributeGroup("atg.ereignisKalenderAntwort");
			asp = modell.getAspect("asp.antwort");
			dbsAntwort = new DataDescription(atg, asp);

			try {
				verbindung.subscribeSender(this, soKalender, dbsAnfrage,
						SenderRole.sender());
			} catch (OneSubscriptionPerSendData ex) {
				// throw new IllegalStateException(ex.getLocalizedMessage());
				kommLogger.error("Datum ist bereits angemeldet.");
			}

			kommLogger.config("Kommunikationschnittstelle bereit.");
		}

		/**
		 * Sendet eine Anfrage an den Kalender.
		 * 
		 * @param anfrage
		 *            die Nachricht mit den Anfragen.
		 */
		public void sendeAnfrage(KalenderAnfrage anfrage) {
			anfragen.add(anfrage);
			sendeAnfragen();
		}

		/**
		 * Sendet alle gecachten Anfragen, solange es erlaubt. Erfolgreich
		 * gesendete Anfragen werden aus dem Cache entfernt.
		 */
		private void sendeAnfragen() {
			ListIterator<KalenderAnfrage> iterator;

			iterator = anfragen.listIterator();
			while (sendenErlaubt && iterator.hasNext()) {
				Data daten;
				ResultData datensatz;
				SystemObject so;
				KalenderAnfrage anfrage;

				anfrage = iterator.next();

				// Als Empfänger der Antwort anmelden
				so = anfrage.getAbsender();
				synchronized (anmeldungen) {
					if (!anmeldungen.contains(so)) {
						verbindung.subscribeReceiver(this, so, dbsAntwort,
								ReceiveOptions.normal(), ReceiverRole.drain());
					}
					anmeldungen.add(so);
				}
				kommLogger.finer("Als Empfänger der Antwort angemeldet für "
						+ "die Anfrage von", so);

				// Anfrage senden
				daten = verbindung.createData(dbsAnfrage.getAttributeGroup());
				anfrage.getDaten(daten);
				datensatz = new ResultData(soKalender, dbsAnfrage, System
						.currentTimeMillis(), daten);
				try {
					verbindung.sendData(datensatz);
					iterator.remove(); // Anfrage erfolgreich gesendet
				} catch (DataNotSubscribedException e) {
					sendenErlaubt = false;
					System.out.println(e.getMessage());
					continue;
				} catch (SendSubscriptionNotConfirmed e) {
					sendenErlaubt = false;
					System.out.println(e.getMessage());
					continue;
				}

				kommLogger.finer("Anfrage wurde gesendet", anfrage);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void dataRequest(SystemObject object,
				DataDescription dataDescription, byte state) {
			if (object.equals(soKalender) && dataDescription.equals(dbsAnfrage)) {
				if (state == ClientSenderInterface.START_SENDING) {
					sendenErlaubt = true;
					sendeAnfragen();
				} else {
					sendenErlaubt = false;
				}
			}
		}

		/**
		 * Sendesteuerung wird verwendet.
		 * <p>
		 * {@inheritDoc}
		 */
		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
			if (object.equals(soKalender) && dataDescription.equals(dbsAnfrage)) {
				return true;
			}
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		public void update(ResultData[] results) {
			for (ResultData datensatz : results) {
				if (datensatz.getDataDescription().equals(dbsAntwort)
						&& datensatz.hasData()) {
					SystemObject so;

					so = datensatz.getObject();
					kommLogger.finer("Kalenderantwort erhalten für die "
							+ "Anfrage von", so);
					fireAntwort((ClientApplication) so, datensatz.getData());
					synchronized (anmeldungen) {
						anmeldungen.remove(so);
						if (!anmeldungen.contains(so)) {
							verbindung
									.unsubscribeReceiver(this, so, dbsAntwort);
						}
					}
				}
			}
		}

		/**
		 * Gibt den Wert der Eigenschaft sendenErlaubt wieder.
		 * 
		 * @return the sendenErlaubt
		 */
		boolean isSendenErlaubt() {
			return sendenErlaubt;
		}

	}

	/** Der Logger. */
	private final Debug logger = Debug.getLogger();

	/** Angemeldete Listener. */
	private final EventListenerList listeners;

	/** Die Kommunikationsinstanz. */
	private final Kommunikation kommunikation;

	/** Sichert die Liste des Singletons pro Datenverteilerverbindung. */
	private static Map<ClientDavInterface, Kalender> singleton;

	/**
	 * Gibt einen Kalender als Singleton pro Datenverteilerverbindung
	 * zur&uuml;ck.
	 * 
	 * @param verbindung
	 *            eine Datenverteilerverbindung.
	 * @return der Kalender als Singleton.
	 */
	public static Kalender getInstanz(ClientDavInterface verbindung) {
		if (singleton == null) {
			singleton = new HashMap<ClientDavInterface, Kalender>();
		}
		if (!singleton.containsKey(verbindung)) {
			singleton.put(verbindung, new Kalender(verbindung));
		}
		return singleton.get(verbindung);
	}

	/**
	 * Initialisert die Anfrageschnittstelle.
	 * 
	 * @param verbindung
	 *            die f&uuml;r Anfragen zu verwendende Datenverteilerverbindung.
	 */
	private Kalender(ClientDavInterface verbindung) {
		kommunikation = new Kommunikation(verbindung);
		listeners = new EventListenerList();

		logger.info("Schnittstelle zum Kalender bereit.");
	}

	/**
	 * Registriert einen Listener.
	 * 
	 * @param listener
	 *            Der neue Listener
	 */
	public void addKalenderListener(KalenderListener listener) {
		listeners.add(KalenderListener.class, listener);
	}

	/**
	 * Entfernt einen Listener wieder aus der Liste registrierter Listener.
	 * 
	 * @param listener
	 *            Listener der abgemeldet werden soll
	 */
	public void removeKalenderListener(KalenderListener listener) {
		listeners.remove(KalenderListener.class, listener);
	}

	/**
	 * Sendet eine Anfrage an den Kalender. Die anfragende Applikation wird
	 * &uuml;ber ein Event &uuml;ber die eingetroffene Antwort informiert.
	 * 
	 * @param anfrage
	 *            die Nachricht mit den Anfragen.
	 */
	public void sendeAnfrage(KalenderAnfrage anfrage) {
		logger.fine("Neue Anfrage entgegengenommen", anfrage);
		kommunikation.sendeAnfrage(anfrage);
	}

	/**
	 * Mit dieser Methode kann getestet werden, ob im Augenblick der
	 * Ereigniskalender Anfragen entgegennimmt.
	 * 
	 * @return {@code true}, wennn der Kalender Anfragen entgegennimmt.
	 */
	public boolean isKalenderBereit() {
		return kommunikation.isSendenErlaubt();
	}

	/**
	 * Informiert alle registrierten Listener &uuml;ber eine Antwort.
	 * 
	 * @param anfrager
	 *            die anfragende Applikation.
	 * @param daten
	 *            ein Datum mit der Antwort auf eine Kalenderanfrage.
	 */
	protected synchronized void fireAntwort(ClientApplication anfrager,
			Data daten) {
		KalenderEvent e = new KalenderEvent(this, anfrager);
		e.setDaten(daten);

		for (KalenderListener l : listeners
				.getListeners(KalenderListener.class)) {
			l.antwortEingetroffen(e);
		}

		logger.fine("Kalenderantwort wurde verteilt: " + e);
	}

}
