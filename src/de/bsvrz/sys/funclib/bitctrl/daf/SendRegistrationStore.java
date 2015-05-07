/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */
package de.bsvrz.sys.funclib.bitctrl.daf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Globale Verwaltung aller Sendeanmeldungen, da der Datenverteilungen
 * Sendeanmeldungen f�r eine Objekt-Datenbeschreibung-Kombination anwendungsweit
 * nur einmalig erlaubt.
 *
 * @author BitCtrl Systems GmbH, anonymous
 */

public final class SendRegistrationStore implements ClientSenderInterface {

	/**
	 * Wrapper - Klasse f�r eine "Sendeanmeldung", d.h. {@link SystemObject} +
	 * {@link DataDescription}.
	 *
	 * @author BitCtrl Systems GmbH, Hoesel
	 */
	private class SystemObjectDataDescription {
		private final SystemObject obj;
		private final DataDescription desc;

		public SystemObjectDataDescription(final SystemObject sysObj,
				final DataDescription desc) {
			obj = sysObj;
			this.desc = desc;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return obj.getPid() + ";" + desc.toString();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object arg0) {
			boolean result = super.equals(arg0);
			if (!result && (arg0 instanceof SystemObjectDataDescription)) {
				final SystemObjectDataDescription that = (SystemObjectDataDescription) arg0;
				if (obj != null
						&& desc != null
						&& obj.equals(that.obj)
						&& desc.getAspect().equals(that.desc.getAspect())
						&& desc.getAttributeGroup().equals(
								that.desc.getAttributeGroup())) {
					result = true;
				}

			}
			return result;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int result = obj.hashCode() + desc.hashCode();
			return result;
		}
	}

	/**
	 * Standard-Timeout beim Warten auf eine Sendebest�tigung.
	 */
	public static final long DEFAULT_TIMEOUT = 60 * Constants.MILLIS_PER_SECOND;

	/**
	 * die globale Instanz dieser Klasse.
	 */
	private static SendRegistrationStore instance = new SendRegistrationStore();

	/**
	 * die Collection zur Verwaltung der Anmeldeeintr�ge.
	 */
	private static final Map<SystemObjectDataDescription, Integer> STORAGE = new HashMap<>();

	/**
	 * Menge aller {@link SystemObjectDataDescription}s, f�r die eine
	 * Sendeerlaubnis empfangen wurde.
	 */
	private static final Set<SystemObjectDataDescription> SENDE_ERLAUBNISSE = new HashSet<>();

	private static final Map<SystemObjectDataDescription, EventListenerList> DATAREQUEST_LISTENERS = new HashMap<>();

	/**
	 * Privater Standardkonstruktor.
	 */
	private SendRegistrationStore() {
		super();
	}

	/**
	 * erzeigen einer Instanz der Klasse.<br>
	 * Da nur eine Instanz existieren kann, wird immer diese geliefert.
	 *
	 * @return die Instanz der Klasse
	 */
	public static SendRegistrationStore getInstance() {
		if (instance == null) {
			instance = new SendRegistrationStore();
		}

		return instance;
	}

	/**
	 * Pr�fen, ob Daten f�r eine Objekt-Datenbeschreibungskombination versendet
	 * werden k�nnen.<br>
	 *
	 * @param object
	 *            das Objekt
	 * @param desc
	 *            die Datenbeschreibung
	 * @return Daten k�nnen versendet werden ?
	 */
	public boolean isRegistered(final SystemObject object,
			final DataDescription desc) {
		boolean result = false;
		if (SENDE_ERLAUBNISSE.contains(new SystemObjectDataDescription(object,
				desc))) {
			result = true;
		}
		return result;
	}

	/**
	 * die Funktion r�ft, ob Daten f�r die gegebene
	 * Objekt-Datenbeschreibungskombination versendet werden k�nnen.<br>
	 * Wird die Sendem�glichkeit innerhalb der vorgegebenen Zeit nicht gemeldet,
	 * liefert die Funktion eine {@link OperationTimedOutException}.
	 *
	 * @param object
	 *            das Objekt
	 * @param desc
	 *            die Datenbeschreibung
	 * @param msec
	 *            die maximale Wartezeit in Millisekunden
	 * @throws OperationTimedOutException
	 *             die Bereitschaft wurde innerhalb der vorgegebenen Zeit nicht
	 *             erreicht
	 */
	public static void waitForRegistration(final SystemObject object,
			final DataDescription desc, final long msec)
					throws OperationTimedOutException {
		final long startTime = System.currentTimeMillis();
		while (!instance.isRegistered(object, desc)) {
			if (System.currentTimeMillis() - startTime > msec) {
				throw new OperationTimedOutException();
			}
			try {
				Thread.sleep(50);
			} catch (final InterruptedException e) {
				//
			}
		}
	}

	/**
	 * wartet, die {@link #DEFAULT_TIMEOUT}, auf die Best�tigung der
	 * Sendeanmeldung f�r die gegebene Kombination aus der Liste der Objekte und
	 * der Datenverteiler-Datensatzbeschreibung.
	 *
	 * @param objects
	 *            die Liste der Objekte
	 * @param desc
	 *            die Datensatzbeschreibung
	 * @throws OperationTimedOutException
	 *             die Sendebest�tigung erfolgte nicht innerhalb der erwarteten
	 *             Zeitspanne
	 */
	public static void waitForRegistration(final SystemObject[] objects,
			final DataDescription desc) throws OperationTimedOutException {
		final long startTime = System.currentTimeMillis();

		boolean found = false;
		while (!found) {
			if (System.currentTimeMillis() - startTime > DEFAULT_TIMEOUT) {
				throw new OperationTimedOutException();
			}
			for (final SystemObject obj : objects) {
				found = instance.isRegistered(obj, desc);
				if (!found) {
					try {
						Thread.sleep(50);
					} catch (final InterruptedException e) {
						//
					}
					break;
				}
			}
		}
	}

	/**
	 * wartet, die {@link #DEFAULT_TIMEOUT}, auf die Best�tigung der
	 * Sendeanmeldung f�r die gegebene Kombination aus dem Objekte und der
	 * Datenverteiler-Datensatzbeschreibung.
	 *
	 * @param object
	 *            das Objekt
	 * @param desc
	 *            die Datensatzbeschreibung
	 * @throws OperationTimedOutException
	 *             die Sendebest�tigung erfolgte nicht innerhalb der erwarteten
	 *             Zeitspanne
	 */
	public static void waitForRegistration(final SystemObject object,
			final DataDescription desc) throws OperationTimedOutException {
		waitForRegistration(object, desc, DEFAULT_TIMEOUT);
	}

	/**
	 * F&uuml;gt einen {@link SendRegistrationStoreDataRequestListener} hinzu.
	 *
	 * @param object
	 *            das Objekt
	 * @param dbs
	 *            die Datensatzbeschreibung
	 * @param listener
	 *            der Listener
	 */
	public static void addSendRegistrationStoreDataRequestListener(
			final SystemObject object, final DataDescription dbs,
			final SendRegistrationStoreDataRequestListener listener) {
		synchronized (DATAREQUEST_LISTENERS) {
			final SystemObjectDataDescription sods = getInstance().new SystemObjectDataDescription(
					object, dbs);
			// Falls notwendig Initialisierung
			if (!DATAREQUEST_LISTENERS.containsKey(sods)) {
				DATAREQUEST_LISTENERS.put(sods, new EventListenerList());
			}
			DATAREQUEST_LISTENERS.get(sods).add(
					SendRegistrationStoreDataRequestListener.class, listener);
		}
	}

	/**
	 * Entfernt einen {@link SendRegistrationStoreDataRequestListener} hinzu.
	 *
	 * @param object
	 *            das Objekt
	 * @param dbs
	 *            die Datensatzbeschreibung
	 * @param listener
	 *            der Listener
	 */
	public static void removeSendRegistrationStoreDataRequestListener(
			final SystemObject object, final DataDescription dbs,
			final SendRegistrationStoreDataRequestListener listener) {
		synchronized (DATAREQUEST_LISTENERS) {
			final SystemObjectDataDescription sods = getInstance().new SystemObjectDataDescription(
					object, dbs);

			final EventListenerList listenerListe = DATAREQUEST_LISTENERS
					.get(sods);
			if (listenerListe != null) {
				listenerListe.remove(
						SendRegistrationStoreDataRequestListener.class,
						listener);
			}
		}
	}

	/**
	 * R�ckmeldung der Sendesteuerung f�r die gegebene Kombination aus Objekt
	 * und Datenverteiler-Datensatzbeschreibung.
	 *
	 * @param obj
	 *            das Objekt
	 * @param desc
	 *            die Datensatzbeschreibung
	 * @param state
	 *            der Sendestatus
	 */
	@Override
	public void dataRequest(final SystemObject obj, final DataDescription desc,
			final byte state) {
		if (state == ClientSenderInterface.START_SENDING) {
			SENDE_ERLAUBNISSE.add(new SystemObjectDataDescription(obj, desc));
		} else {
			SENDE_ERLAUBNISSE.remove(new SystemObjectDataDescription(obj, desc));
		}

		notifyListener(obj, desc, state);
	}

	private void notifyListener(final SystemObject obj,
			final DataDescription desc, final byte state) {
		synchronized (DATAREQUEST_LISTENERS) {
			final SystemObjectDataDescription sods = getInstance().new SystemObjectDataDescription(
					obj, desc);

			final EventListenerList listenerListe = DATAREQUEST_LISTENERS
					.get(sods);

			if (listenerListe != null) {
				for (final SendRegistrationStoreDataRequestListener listener : listenerListe
						.getListeners(SendRegistrationStoreDataRequestListener.class)) {
					listener.registrationStoreDataRequest(obj, desc, state);
				}
			}
		}
	}

	/**
	 * Die Funktion meldet die �bergebene
	 * Objekt-Datensatzbeschreibung-Kombination beim Datenverteiler als Sender
	 * an.
	 *
	 * @param dav
	 *            die Datenverteilerverbindung
	 * @param object
	 *            das Objekt
	 * @param dataDesc
	 *            die Datensatzbeschreibung
	 */
	public static synchronized void subscribeSender(
			final ClientDavInterface dav, final SystemObject object,
			final DataDescription dataDesc) {

		final SystemObjectDataDescription sysDesc = instance.new SystemObjectDataDescription(
				object, dataDesc);
		Integer anzahlAnmeldungen = STORAGE.get(sysDesc);
		if (anzahlAnmeldungen == null || anzahlAnmeldungen == 0) {
			STORAGE.put(sysDesc, 1);
			try {
				dav.subscribeSender(instance, object, dataDesc,
						SenderRole.sender());
				Debug.getLogger().fine(
						"Anmeldung als Sender f�r " + object + "; " + dataDesc
						+ " erfolgreich.");
			} catch (final OneSubscriptionPerSendData e) {
				Debug.getLogger()
				.warning(
						"Mehr als eine Anmeldung als Sender! Es mu� der SendRegistrationStore verwendet werden.",
						e);
			}
		} else {
			STORAGE.put(sysDesc, ++anzahlAnmeldungen);
		}
	}

	/**
	 * Die Funktion meldet die �bergebene Kombination aus dem Array der
	 * �bergebenen Objekte und Datensatzbeschreibung beim Datenverteiler als
	 * Sender an.
	 *
	 * @param dav
	 *            die Datenverteilerverbindung
	 * @param object
	 *            die Liste der Objekte
	 * @param dataDesc
	 *            die Datensatzbeschreibung
	 */
	public static void subscribeSender(final ClientDavInterface dav,
			final SystemObject[] object, final DataDescription dataDesc) {
		subscribeSender(dav, Arrays.asList(object), dataDesc);
	}

	/**
	 * Die Funktion meldet die �bergebene Kombination aus dem �bergebenen Objekt
	 * und Datensatzbeschreibung beim Datenverteiler als Sender ab.
	 *
	 * @param dav
	 *            die Datenverteilerverbindung
	 * @param object
	 *            das Objekt
	 * @param desc
	 *            die Datensatzbeschreibung
	 */
	public static synchronized void unsubscribeSender(
			final ClientDavInterface dav, final SystemObject object,
			final DataDescription desc) {
		final SystemObjectDataDescription sysDesc = instance.new SystemObjectDataDescription(
				object, desc);
		Integer anzahlAnmeldungen = SendRegistrationStore.STORAGE.get(sysDesc);
		if (anzahlAnmeldungen != null && anzahlAnmeldungen > 1) {
			SendRegistrationStore.STORAGE.put(sysDesc, --anzahlAnmeldungen);
		} else {
			dav.unsubscribeSender(instance, object, desc);
			Debug.getLogger().fine(
					"Abmeldung als Sender f�r " + object + "; " + desc
					+ " erfolgreich.");
			STORAGE.remove(sysDesc);
			SENDE_ERLAUBNISSE.remove(sysDesc);
		}
	}

	/**
	 * Die Funktion meldet die �bergebene Kombination aus der Liste der
	 * �bergebenen Objekte und Datensatzbeschreibung beim Datenverteiler als
	 * Sender an.
	 *
	 * @param dav
	 *            die Datenverteilerverbindung
	 * @param object
	 *            die Liste der Objekte
	 * @param dataDesc
	 *            die Datensatzbeschreibung
	 */
	public static synchronized void subscribeSender(
			final ClientDavInterface dav, final List<SystemObject> object,
			final DataDescription dataDesc) {

		final List<SystemObject> nochNichtAngemeldet = new ArrayList<>();
		for (final SystemObject obj : object) {
			final SystemObjectDataDescription sysDesc = instance.new SystemObjectDataDescription(
					obj, dataDesc);
			Integer anzahlAnmeldungen = SendRegistrationStore.STORAGE
					.get(sysDesc);
			if (anzahlAnmeldungen == null || anzahlAnmeldungen == 0) {
				nochNichtAngemeldet.add(obj);
				SendRegistrationStore.STORAGE.put(sysDesc, 1);
			} else {
				SendRegistrationStore.STORAGE.put(sysDesc, ++anzahlAnmeldungen);
			}

		}
		if (!nochNichtAngemeldet.isEmpty()) {
			try {
				dav.subscribeSender(instance, nochNichtAngemeldet, dataDesc,
						SenderRole.sender());
				Debug.getLogger().fine(
						"Anmeldung als Sender f�r " + nochNichtAngemeldet
						+ "; " + dataDesc + " erfolgreich.");
			} catch (final OneSubscriptionPerSendData ex) {
				Debug.getLogger()
				.warning(
						"Mehr als eine Anmeldung als Sender! Es mu� der SendRegistrationStore verwendet werden.",
						ex);
			}
		}
	}

	@Override
	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
		return true;
	}
}
