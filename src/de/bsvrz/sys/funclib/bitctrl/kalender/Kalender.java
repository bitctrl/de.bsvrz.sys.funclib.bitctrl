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

package de.bsvrz.sys.funclib.bitctrl.kalender;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.Datensatz.Status;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.KalenderobjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAnfrage;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAntwort;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalObjektFactory;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse stellt Methoden zur Verf&uuml;gung um ein einfacher Art und
 * Weise Anfragen an den Ereigniskalender zustellen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public final class Kalender implements DatensatzUpdateListener {

	/** Sichert die Liste des Singletons pro Datenverteilerverbindung. */
	private static Kalender singleton;

	/**
	 * Gibt einen Kalender als Singleton zur&uuml;ck.
	 * 
	 * @return der Kalender als Singleton.
	 */
	public static Kalender getInstanz() {
		if (singleton == null) {
			singleton = new Kalender();
		}
		return singleton;
	}

	/** Der Logger. */
	private final Debug log = Debug.getLogger();

	/** Angemeldete Listener. */
	private final EventListenerList listeners = new EventListenerList();

	/** Der Anfragedatensatz. */
	private final OdEreignisKalenderAnfrage odAnfrage;

	/** Der Aspekt zum Senden der Anfrage. */
	private final Aspect aspAnfrage;

	/**
	 * Initialisert die Anfrageschnittstelle.
	 */
	private Kalender() {
		ObjektFactory factory;
		OdEreignisKalenderAntwort odAntwort;
		Aspect aspAntwort;
		de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender kalender;
		Applikation klient;

		// Modellobjektfactory initialisieren
		factory = ObjektFactory.getInstanz();
		factory.registerFactory(new KalenderobjektFactory(),
				new SystemModellGlobalObjektFactory());

		// Anfragedatensatz bestimmen
		kalender = (de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender) factory
				.getModellobjekt(factory.getVerbindung()
						.getLocalConfigurationAuthority());
		aspAnfrage = OdEreignisKalenderAnfrage.Aspekte.Anfrage.getAspekt();
		odAnfrage = kalender
				.getOnlineDatensatz(OdEreignisKalenderAnfrage.class);

		// Anmelden als Empfänger der Kalenderantworten
		klient = (Applikation) factory.getModellobjekt(factory.getVerbindung()
				.getLocalApplicationObject());
		odAntwort = klient.getOnlineDatensatz(OdEreignisKalenderAntwort.class);
		aspAntwort = OdEreignisKalenderAntwort.Aspekte.Antwort.getAspekt();
		odAntwort.setSenke(aspAntwort, true);
		odAntwort.addUpdateListener(aspAntwort, this);
		try {
			odAnfrage.anmeldenSender(aspAnfrage);
		} catch (AnmeldeException ex) {
			log
					.error(
							"Anmeldung zum Senden von Anfragen an die Ganglinienprognose konnte nicht durchgeführt werden",
							ex);
		}

		log.info("Schnittstelle zum Kalender initialisiert.");
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
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener#datensatzAktualisiert(de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent)
	 */
	public void datensatzAktualisiert(DatensatzUpdateEvent event) {
		if (event.getDatum().isValid()) {
			fireAntwort((OdEreignisKalenderAntwort.Daten) event.getDatum());
		}
	}

	/**
	 * Fragt, ob der Kalender Anfragen entgegennimmt.
	 * 
	 * @return {@code true}, wenn der Kalender verwendet werden kann.
	 */
	public boolean isBereit() {
		return odAnfrage.getStatusSendesteuerung(aspAnfrage) == Status.START;
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
	 * @param absenderZeichen
	 *            ein beliebiger Text.
	 * @param anfrage
	 *            die Anfragen.
	 * @throws DatensendeException
	 *             wenn beim Senden ein Fehler passiert ist.
	 */
	public void sendeAnfrage(String absenderZeichen, KalenderAnfrage anfrage)
			throws DatensendeException {
		OdEreignisKalenderAnfrage.Daten datum;
		ObjektFactory factory;
		ClientApplication klient;

		if (!isBereit()) {
			throw new DatensendeException(
					"Ereigniskalender (noch) nicht bereit.");
		}
		factory = ObjektFactory.getInstanz();
		factory.registerFactory(new KalenderobjektFactory(),
				new SystemModellGlobalObjektFactory());
		factory.registerFactory(new KalenderobjektFactory());
		klient = factory.getVerbindung().getLocalApplicationObject();

		datum = odAnfrage.erzeugeDatum();
		datum.setAbsender((Applikation) factory.getModellobjekt(klient));
		datum.setAbsenderZeichen(absenderZeichen);
		datum.setEreignisTypenOption(anfrage.getEreignisTypenOption());
		datum.setIntervall(anfrage.getIntervall());
		datum.getRaeumlicheGueltigkeit().addAll(
				anfrage.getRaeumlicheGueltigkeit());
		datum.getEreignisTypen().addAll(anfrage.getEreignisTypen());

		odAnfrage.sendeDaten(aspAnfrage, datum);

		log.fine("Anfrage \"" + absenderZeichen + "\" wurde gesendet");
	}

	/**
	 * Informiert alle registrierten Listener &uuml;ber eine Antwort.
	 * 
	 * @param datum
	 *            das Datum mit der Antwort.
	 */
	protected synchronized void fireAntwort(
			OdEreignisKalenderAntwort.Daten datum) {
		KalenderEvent e = new KalenderEvent(this, datum.getAbsenderZeichen(),
				datum.isAenderung(), datum.getZustandswechsel());

		for (KalenderListener l : listeners
				.getListeners(KalenderListener.class)) {
			l.antwortEingetroffen(e);
		}

		log.fine("Kalenderantwort wurde verteilt: " + e);
	}

}
