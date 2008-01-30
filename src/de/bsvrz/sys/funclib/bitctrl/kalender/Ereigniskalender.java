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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.Datensatz.Status;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAnfrage;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAntwort;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdEreignisParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdEreignisTypParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdSystemKalenderEintrag;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.Applikation;
import de.bsvrz.sys.funclib.bitctrl.util.Intervall;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse stellt Methoden zur Verf&uuml;gung um ein einfacher Art und
 * Weise Anfragen an den Ereigniskalender zustellen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public final class Ereigniskalender implements DatensatzUpdateListener {

	/** Timeout von einer Minute f�r das Senden von Daten. */
	private static final long TIMEOUT = 60 * 1000;

	/** Sichert die Liste des Singletons pro Datenverteilerverbindung. */
	private static Ereigniskalender singleton;

	/**
	 * Gibt einen Kalender als Singleton zur&uuml;ck.
	 * 
	 * @return der Kalender als Singleton.
	 */
	public static Ereigniskalender getInstanz() {
		if (singleton == null) {
			singleton = new Ereigniskalender();
		}
		return singleton;
	}

	/** Der Logger. */
	private final Debug log = Debug.getLogger();

	/** Angemeldete Listener. */
	private final EventListenerList listeners = new EventListenerList();

	/** Die Eigenschaft {@code kalender}. */
	private final Kalender kalender;

	/** Der Anfragedatensatz. */
	private final OdEreignisKalenderAnfrage odAnfrage;

	/** Der Aspekt zum Senden der Anfrage. */
	private final Aspect aspAnfrage;

	/**
	 * Initialisert die Anfrageschnittstelle.
	 */
	private Ereigniskalender() {
		ObjektFactory factory;
		OdEreignisKalenderAntwort odAntwort;
		Aspect aspAntwort;
		Applikation klient;

		// Modellobjektfactory initialisieren
		factory = ObjektFactory.getInstanz();
		factory.registerStandardFactories();

		// Anfragedatensatz bestimmen
		kalender = (de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender) factory
				.getModellobjekt(factory.getVerbindung()
						.getLocalConfigurationAuthority());
		aspAnfrage = OdEreignisKalenderAnfrage.Aspekte.Anfrage.getAspekt();
		odAnfrage = kalender
				.getOnlineDatensatz(OdEreignisKalenderAnfrage.class);

		// Anmelden als Empf�nger der Kalenderantworten
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
							"Anmeldung zum Senden von Anfragen an die Ganglinienprognose konnte nicht durchgef�hrt werden",
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
	 * Legt ein Ereignis an und f�gt ihn der entsprechenden Menge am Kalender
	 * hinzu.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param beschreibung
	 *            die Beschreibung.
	 * @param typ
	 *            der Ereignistyp.
	 * @param intervall
	 *            das Intervall in dem das Ereignis g�ltig sein soll. Das
	 *            Intervall bezieht sich auf die zeitliche und verkehrliche
	 *            G�ltigkeit.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws AnmeldeException
	 *             wenn das Anmelden nicht m�glich war.
	 * @throws DatensendeException
	 *             wenn das Sendes Parameters mit dem Intervall nicht m�glich
	 *             war.
	 */
	public Ereignis anlegenEreignis(String pid, String name,
			String beschreibung, EreignisTyp typ, Intervall intervall)
			throws ConfigurationChangeException, AnmeldeException,
			DatensendeException {
		Ereignis erg;
		PdEreignisParameter param;
		PdEreignisParameter.Daten datum;

		erg = Ereignis.anlegen(pid, name, beschreibung, typ);
		kalender.add(erg);

		param = erg.getParameterDatensatz(PdEreignisParameter.class);
		param.anmeldenSender();
		datum = param.erzeugeDatum();
		datum.setZeitlicheGueltigkeit(intervall);
		datum.getVerkehrlicheGueltigkeit().add(
				new PdEreignisParameter.Daten.VerkehrlicheGueltigkeit());
		param.sendeDaten(datum, TIMEOUT);

		return erg;
	}

	/**
	 * Legt ein Ereignis an und f�gt ihn der entsprechenden Menge am Kalender
	 * hinzu.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param beschreibung
	 *            die Beschreibung.
	 * @param typ
	 *            der Ereignistyp.
	 * @param ske
	 *            ein Systemkalendereintrag, der die G�ltigkeit des Ereignisses
	 *            beschreibt. Die zeitliche und verkehrliche G�ltigkeit wird als
	 *            identisch angenommen.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws AnmeldeException
	 *             wenn das Anmelden nicht m�glich war.
	 * @throws DatensendeException
	 *             wenn das Sendes Parameters mit dem Intervall nicht m�glich
	 *             war.
	 */
	public Ereignis anlegenEreignis(String pid, String name,
			String beschreibung, EreignisTyp typ, SystemKalenderEintrag ske)
			throws ConfigurationChangeException, AnmeldeException,
			DatensendeException {
		Ereignis erg;
		PdEreignisParameter param;
		PdEreignisParameter.Daten datum;

		erg = Ereignis.anlegen(pid, name, beschreibung, typ);
		kalender.add(erg);

		param = erg.getParameterDatensatz(PdEreignisParameter.class);
		param.anmeldenSender();
		datum = param.erzeugeDatum();
		datum.setSystemKalenderEintrag(ske);
		datum.getVerkehrlicheGueltigkeit().add(
				new PdEreignisParameter.Daten.VerkehrlicheGueltigkeit());
		param.sendeDaten(datum, TIMEOUT);

		return erg;
	}

	/**
	 * Legt einen Ereignistyp an und f�gt ihn der entsprechenden Menge am
	 * Kalender hinzu.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param prioritaet
	 *            die Priorit�t des Ereignistyps.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws DatensendeException
	 *             wenn der Parameter mit der Priorit�t nicht gesendet werden
	 *             konnte.
	 * @throws AnmeldeException
	 *             wenn der Parameter mit der Priorit�t nicht zum Senden
	 *             angemeldet werden konnte.
	 */
	public EreignisTyp anlegenEreignisTyp(String pid, String name,
			int prioritaet) throws ConfigurationChangeException,
			DatensendeException, AnmeldeException {
		return anlegenEreignisTyp(pid, name, prioritaet,
				new HashMap<String, String>());
	}

	/**
	 * Legt einen Ereignistyp an und f�gt ihn der entsprechenden Menge am
	 * Kalender hinzu.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param prioritaet
	 *            die Priorit�t des Ereignistyps.
	 * @param attribute
	 *            zus�tzliche Attribute in Form einer Liste von
	 *            Schl�ssel/Wert-Paaren.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws DatensendeException
	 *             wenn der Parameter mit der Priorit�t nicht gesendet werden
	 *             konnte.
	 * @throws AnmeldeException
	 *             wenn der Parameter mit der Priorit�t nicht zum Senden
	 *             angemeldet werden konnte.
	 */
	public EreignisTyp anlegenEreignisTyp(String pid, String name,
			int prioritaet, Map<String, String> attribute)
			throws ConfigurationChangeException, DatensendeException,
			AnmeldeException {
		EreignisTyp typ;
		PdEreignisTypParameter param;
		PdEreignisTypParameter.Daten datum;

		typ = EreignisTyp.anlegen(pid, name, attribute);
		kalender.add(typ);

		param = typ.getParameterDatensatz(PdEreignisTypParameter.class);
		param.anmeldenSender();
		datum = param.erzeugeDatum();
		datum.setPrioritaet(prioritaet);
		param.sendeDaten(datum, TIMEOUT);

		return typ;
	}

	/**
	 * Legt einen Systemkalendereintrag an und f�gt ihn der entsprechenden Menge
	 * am Kalender hinzu.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param definition
	 *            die Definition des Systemkalendereintrags.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws AnmeldeException
	 *             wenn das Anmelden zum Datensenden nicht m�glich ist.
	 * @throws DatensendeException
	 *             wenn der Parameter mit der Definition nicht gesendet werden
	 *             konnte.
	 */
	public SystemKalenderEintrag anlegenSystemKalenderEintrag(String pid,
			String name, String definition)
			throws ConfigurationChangeException, AnmeldeException,
			DatensendeException {
		SystemKalenderEintrag ske;
		PdSystemKalenderEintrag param;
		PdSystemKalenderEintrag.Daten datum;

		ske = SystemKalenderEintrag.anlegen(pid, name);
		kalender.add(ske);

		param = ske.getParameterDatensatz(PdSystemKalenderEintrag.class);
		param.anmeldenSender();
		datum = param.erzeugeDatum();
		datum.setDefinition(definition);
		param.sendeDaten(datum, TIMEOUT);

		return ske;
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
	 * Entfernt das Ereignis aus der entsprechenden Menge am Kalender und
	 * "l�scht" das Ereignis anschlie�end, in dem er invalidiert wird.
	 * 
	 * @param erg
	 *            das zu l�schende Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das L�schen schief ging.
	 */
	public void loeschen(Ereignis erg) throws ConfigurationChangeException {
		kalender.add(erg);
		erg.entfernen();
	}

	/**
	 * Entfernt den Ereignistyp aus der entsprechenden Menge am Kalender und
	 * "l�scht" den Typ anschlie�end, in dem er invalidiert wird.
	 * 
	 * @param typ
	 *            der zu l�schende Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das L�schen schief ging.
	 */
	public void loeschen(EreignisTyp typ) throws ConfigurationChangeException {
		kalender.add(typ);
		typ.entfernen();
	}

	/**
	 * Entfernt den Ereignistyp aus der entsprechenden Menge am Kalender und
	 * "l�scht" den Typ anschlie�end, in dem er invalidiert wird.
	 * 
	 * @param typ
	 *            der zu l�schende Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das L�schen schief ging.
	 */
	public void loeschen(SystemKalenderEintrag typ)
			throws ConfigurationChangeException {
		kalender.add(typ);
		typ.entfernen();
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