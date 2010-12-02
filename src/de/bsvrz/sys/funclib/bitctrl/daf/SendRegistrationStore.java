package de.bsvrz.sys.funclib.bitctrl.daf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * Sendeanmeldungen für eine Objekt-Datenbeschreibung-Kombination anwendungsweit
 * nur einmalig erlaubt.
 */
public final class SendRegistrationStore implements ClientSenderInterface {

	/**
	 * Wrapper - Klasse für eine "Sendeanmeldung", d.h. {@link SystemObject} +
	 * {@link DataDescription}.
	 * 
	 * @author BitCtrl Systems GmbH, Hoesel
	 * @version $Id$
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
	 * Standard-Timeout beim Warten auf eine Sendebestätigung.
	 */
	public static final long DEFAULT_TIMEOUT = 60 * Constants.MILLIS_PER_SECOND;

	/**
	 * die globale Instanz dieser Klasse.
	 */
	private static SendRegistrationStore instance = new SendRegistrationStore();

	/**
	 * die Collection zur Verwaltung der Anmeldeeinträge.
	 */
	private static final Map<SystemObjectDataDescription, Integer> storage = new HashMap<SystemObjectDataDescription, Integer>();

	/**
	 * Menge aller {@link SystemObjectDataDescription}s, für die eine
	 * Sendeerlaubnis empfangen wurde.
	 */
	private static final Set<SystemObjectDataDescription> sendeerlaubnise = new HashSet<SendRegistrationStore.SystemObjectDataDescription>();

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
	 * Prüfen, ob Daten für eine Objekt-Datenbeschreibungskombination versendet
	 * werden können.<br>
	 * 
	 * @param object
	 *            das Objekt
	 * @param desc
	 *            die Datenbeschreibung
	 * @return Daten können versendet werden ?
	 */
	public boolean isRegistered(final SystemObject object,
			final DataDescription desc) {
		boolean result = false;
		if (sendeerlaubnise.contains(new SystemObjectDataDescription(object,
				desc))) {
			result = true;
		}
		return result;
	}

	/**
	 * die Funktion rüft, ob Daten für die gegebene
	 * Objekt-Datenbeschreibungskombination versendet werden können.<br>
	 * Wird die Sendemöglichkeit innerhalb der vorgegebenen Zeit nicht gemeldet,
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
	 * wartet, die {@link #DEFAULT_TIMEOUT}, auf die Bestätigung der
	 * Sendeanmeldung für die gegebene Kombination aus der Liste der Objekte und
	 * der Datenverteiler-Datensatzbeschreibung.
	 * 
	 * @param objects
	 *            die Liste der Objekte
	 * @param desc
	 *            die Datensatzbeschreibung
	 * @throws OperationTimedOutException
	 *             die Sendebestätigung erfolgte nicht innerhalb der erwarteten
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
	 * wartet, die {@link #DEFAULT_TIMEOUT}, auf die Bestätigung der
	 * Sendeanmeldung für die gegebene Kombination aus dem Objekte und der
	 * Datenverteiler-Datensatzbeschreibung.
	 * 
	 * @param object
	 *            das Objekt
	 * @param desc
	 *            die Datensatzbeschreibung
	 * @throws OperationTimedOutException
	 *             die Sendebestätigung erfolgte nicht innerhalb der erwarteten
	 *             Zeitspanne
	 */
	public static void waitForRegistration(final SystemObject object,
			final DataDescription desc) throws OperationTimedOutException {
		waitForRegistration(object, desc, DEFAULT_TIMEOUT);
	}

	/**
	 * Rückmeldung der Sendesteuerung für die gegebene Kombination aus Objekt
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
			sendeerlaubnise.add(new SystemObjectDataDescription(obj, desc));
		} else {
			sendeerlaubnise.remove(new SystemObjectDataDescription(obj, desc));
		}

	}

	/**
	 * Die Funktion meldet die übergebene
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
	public synchronized static void subscribeSender(
			final ClientDavInterface dav, final SystemObject object,
			final DataDescription dataDesc) {

		final SystemObjectDataDescription sysDesc = instance.new SystemObjectDataDescription(
				object, dataDesc);
		Integer anzahlAnmeldungen = storage.get(sysDesc);
		if (anzahlAnmeldungen == null || anzahlAnmeldungen == 0) {
			storage.put(sysDesc, 1);
			try {
				dav.subscribeSender(instance, object, dataDesc,
						SenderRole.sender());
				Debug.getLogger().info(
						"Anmeldung als Sender für " + object + "; " + dataDesc
								+ " erfolgreich.");
			} catch (final OneSubscriptionPerSendData e) {
				// TODO irgendwas von sich geben, wenns nicht klappt
				// da hat wohl jemand das SendRegistrationStore nocht verwendet.
				// e.printStackTrace();
			}
		} else {
			storage.put(sysDesc, ++anzahlAnmeldungen);
		}
	}

	/**
	 * Die Funktion meldet die übergebene Kombination aus dem Array der
	 * übergebenen Objekte und Datensatzbeschreibung beim Datenverteiler als
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

	public synchronized static void unsubscribeSender(
			final ClientDavInterface dav, final SystemObject object,
			final DataDescription desc) {
		final SystemObjectDataDescription sysDesc = instance.new SystemObjectDataDescription(
				object, desc);
		Integer anzahlAnmeldungen = SendRegistrationStore.storage.get(sysDesc);
		if (anzahlAnmeldungen != null && anzahlAnmeldungen > 1) {
			SendRegistrationStore.storage.put(sysDesc, --anzahlAnmeldungen);
		} else {
			dav.unsubscribeSender(instance, object, desc);
			Debug.getLogger().info(
					"Abmeldung als Sender für " + object + "; " + desc
							+ " erfolgreich.");
			storage.remove(sysDesc);
			sendeerlaubnise.remove(sysDesc);
		}
	}

	/**
	 * Die Funktion meldet die übergebene Kombination aus der Liste der
	 * übergebenen Objekte und Datensatzbeschreibung beim Datenverteiler als
	 * Sender an.
	 * 
	 * @param dav
	 *            die Datenverteilerverbindung
	 * @param object
	 *            die Liste der Objekte
	 * @param dataDesc
	 *            die Datensatzbeschreibung
	 */
	public synchronized static void subscribeSender(
			final ClientDavInterface dav, final List<SystemObject> object,
			final DataDescription dataDesc) {

		final List<SystemObject> nochNichtAngemeldet = new ArrayList<SystemObject>();
		for (final SystemObject obj : object) {
			final SystemObjectDataDescription sysDesc = instance.new SystemObjectDataDescription(
					obj, dataDesc);
			Integer anzahlAnmeldungen = SendRegistrationStore.storage
					.get(sysDesc);
			if (anzahlAnmeldungen == null || anzahlAnmeldungen == 0) {
				nochNichtAngemeldet.add(obj);
				SendRegistrationStore.storage.put(sysDesc, 1);
			} else {
				SendRegistrationStore.storage.put(sysDesc, ++anzahlAnmeldungen);
			}

		}
		if (!nochNichtAngemeldet.isEmpty()) {
			try {
				dav.subscribeSender(instance, nochNichtAngemeldet, dataDesc,
						SenderRole.sender());
				Debug.getLogger().info(
						"Anmeldung als Sender für " + nochNichtAngemeldet
								+ "; " + dataDesc + " erfolgreich.");
			} catch (final OneSubscriptionPerSendData ex) {
				// da hat wohl jemand das SendRegistrationStore nocht verwendet.
				// ex.printStackTrace();
			}
		}
	}

	/**
	 * die Funktion ermittelt, ob für die gegebene Kombination ais Objekt und
	 * Datensatzbeschreibung eine Sendesteuerung unterstützt wird.<br>
	 * Da die Klasse zur Verwaltung von Datenverteiler-"Sendern" verwendet wird,
	 * wird immer true geliefert.
	 * 
	 * @param object
	 *            das Objekt
	 * @param dataDescription
	 *            die Datensatzbeschreibung
	 * @return die Sendesteuerung wird unterstützt ?
	 * @see stauma.dav.clientside.ClientSenderInterface#isRequestSupported(stauma.dav.configuration.interfaces.SystemObject,
	 *      stauma.dav.clientside.DataDescription)
	 */
	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
		return true;
	}

}
