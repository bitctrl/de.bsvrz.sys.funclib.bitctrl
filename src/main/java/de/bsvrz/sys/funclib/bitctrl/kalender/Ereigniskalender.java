/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

import java.util.HashMap;
import java.util.Map;

import javax.swing.event.EventListenerList;

import com.bitctrl.util.Interval;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.Datensatz.Status;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAnfrage;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAntwort;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdEreignisParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdEreignisTypParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdSystemKalenderEintrag;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse stellt Methoden zur Verf&uuml;gung um ein einfacher Art und
 * Weise Anfragen an den Ereigniskalender zustellen.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public final class Ereigniskalender implements DatensatzUpdateListener {

	/** Sichert die Liste des Singletons pro Datenverteilerverbindung. */
	private static Ereigniskalender singleton;

	/**
	 * Gibt einen Kalender als Singleton zur&uuml;ck.
	 *
	 * @return der Kalender als Singleton.
	 */
	public static synchronized Ereigniskalender getInstanz() {
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

		// Anmelden als Empfänger der Kalenderantworten
		klient = (Applikation) factory.getModellobjekt(
				factory.getVerbindung().getLocalApplicationObject());
		odAntwort = klient.getOnlineDatensatz(OdEreignisKalenderAntwort.class);
		aspAntwort = OdEreignisKalenderAntwort.Aspekte.Antwort.getAspekt();
		odAntwort.setSenke(aspAntwort, true);
		odAntwort.addUpdateListener(aspAntwort, this);
		try {
			odAnfrage.anmeldenSender(aspAnfrage);
		} catch (final AnmeldeException ex) {
			log.error(
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
	public void addKalenderListener(final KalenderListener listener) {
		listeners.add(KalenderListener.class, listener);
	}

	/**
	 * Legt ein Ereignis an und fügt ihn der entsprechenden Menge am Kalender
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
	 *            das Intervall in dem das Ereignis gültig sein soll. Das
	 *            Intervall bezieht sich auf die zeitliche und verkehrliche
	 *            Gültigkeit.
	 * @param quelle
	 *            die Quelle.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws AnmeldeException
	 *             wenn das Anmelden nicht möglich war.
	 * @throws DatensendeException
	 *             wenn das Sendes Parameters mit dem Intervall nicht möglich
	 *             war.
	 */
	public Ereignis anlegenEreignis(final String pid, final String name,
			final String beschreibung, final EreignisTyp typ,
			final Interval intervall, final String quelle)
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
		datum.getVerkehrlicheGueltigkeit()
				.add(new PdEreignisParameter.Daten.VerkehrlicheGueltigkeit());
		datum.setQuelle(quelle);
		param.sendeDaten(datum);

		return erg;
	}

	/**
	 * Legt ein Ereignis an und fügt ihn der entsprechenden Menge am Kalender
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
	 *            ein Systemkalendereintrag, der die Gültigkeit des Ereignisses
	 *            beschreibt. Die zeitliche und verkehrliche Gültigkeit wird als
	 *            identisch angenommen.
	 * @param quelle
	 *            die Quelle.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws AnmeldeException
	 *             wenn das Anmelden nicht möglich war.
	 * @throws DatensendeException
	 *             wenn das Sendes Parameters mit dem Intervall nicht möglich
	 *             war.
	 */
	public Ereignis anlegenEreignis(final String pid, final String name,
			final String beschreibung, final EreignisTyp typ,
			final SystemKalenderEintrag ske, final String quelle)
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
		datum.getVerkehrlicheGueltigkeit()
				.add(new PdEreignisParameter.Daten.VerkehrlicheGueltigkeit());
		datum.setQuelle(quelle);
		param.sendeDaten(datum);

		return erg;
	}

	/**
	 * Legt einen Ereignistyp an und fügt ihn der entsprechenden Menge am
	 * Kalender hinzu.
	 *
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param prioritaet
	 *            die Priorität des Ereignistyps.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws DatensendeException
	 *             wenn der Parameter mit der Priorität nicht gesendet werden
	 *             konnte.
	 * @throws AnmeldeException
	 *             wenn der Parameter mit der Priorität nicht zum Senden
	 *             angemeldet werden konnte.
	 */
	public EreignisTyp anlegenEreignisTyp(final String pid, final String name,
			final int prioritaet) throws ConfigurationChangeException,
					DatensendeException, AnmeldeException {
		return anlegenEreignisTyp(pid, name, prioritaet,
				new HashMap<String, String>());
	}

	/**
	 * Legt einen Ereignistyp an und fügt ihn der entsprechenden Menge am
	 * Kalender hinzu.
	 *
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param prioritaet
	 *            die Priorität des Ereignistyps.
	 * @param attribute
	 *            zusätzliche Attribute in Form einer Liste von
	 *            Schlüssel/Wert-Paaren.
	 * @return der angeleget Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen fehlerhaft verlief.
	 * @throws DatensendeException
	 *             wenn der Parameter mit der Priorität nicht gesendet werden
	 *             konnte.
	 * @throws AnmeldeException
	 *             wenn der Parameter mit der Priorität nicht zum Senden
	 *             angemeldet werden konnte.
	 */
	public EreignisTyp anlegenEreignisTyp(final String pid, final String name,
			final int prioritaet, final Map<String, String> attribute)
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
		param.sendeDaten(datum);

		return typ;
	}

	/**
	 * Legt einen Systemkalendereintrag an und fügt ihn der entsprechenden Menge
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
	 *             wenn das Anmelden zum Datensenden nicht möglich ist.
	 * @throws DatensendeException
	 *             wenn der Parameter mit der Definition nicht gesendet werden
	 *             konnte.
	 */
	public SystemKalenderEintrag anlegenSystemKalenderEintrag(final String pid,
			final String name, final String definition)
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
		param.sendeDaten(datum);

		return ske;
	}

	@Override
	public void datensatzAktualisiert(final DatensatzUpdateEvent event) {
		if (event.getDatum().isValid()) {
			fireAntwort((OdEreignisKalenderAntwort.Daten) event.getDatum());
		}
	}

	/**
	 * Fragt, ob der Kalender Anfragen entgegennimmt. Wenn die Sendesteuerung
	 * noch nicht geantwortet hat, wartet die Methode maximal 30 Sekunden. Hat
	 * die Sendesteuerung schon geantwortet, entsteht keine Verzögerung.
	 *
	 * @return {@code true}, wenn der Kalender verwendet werden kann.
	 */
	public boolean isBereit() {
		for (int i = 0; i < 300; i++) {
			if (odAnfrage.getStatusSendesteuerung(aspAnfrage) != null) {
				break;
			}
			ObjektFactory.getInstanz().getVerbindung().sleep(100);
		}
		return odAnfrage.getStatusSendesteuerung(aspAnfrage) == Status.START;
	}

	/**
	 * Entfernt das Ereignis aus der entsprechenden Menge am Kalender und
	 * "löscht" das Ereignis anschließend, in dem er invalidiert wird.
	 *
	 * @param erg
	 *            das zu löschende Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das Löschen schief ging.
	 */
	public void loeschen(final Ereignis erg)
			throws ConfigurationChangeException {
		kalender.remove(erg);
		erg.entfernen();
	}

	/**
	 * Entfernt den Ereignistyp aus der entsprechenden Menge am Kalender und
	 * "löscht" den Typ anschließend, in dem er invalidiert wird.
	 *
	 * @param typ
	 *            der zu löschende Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Löschen schief ging.
	 */
	public void loeschen(final EreignisTyp typ)
			throws ConfigurationChangeException {
		kalender.remove(typ);
		typ.entfernen();
	}

	/**
	 * Entfernt den Ereignistyp aus der entsprechenden Menge am Kalender und
	 * "löscht" den Typ anschließend, in dem er invalidiert wird.
	 *
	 * @param typ
	 *            der zu löschende Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Löschen schief ging.
	 */
	public void loeschen(final SystemKalenderEintrag typ)
			throws ConfigurationChangeException {
		kalender.remove(typ);
		typ.entfernen();
	}

	/**
	 * Entfernt einen Listener wieder aus der Liste registrierter Listener.
	 *
	 * @param listener
	 *            Listener der abgemeldet werden soll
	 */
	public void removeKalenderListener(final KalenderListener listener) {
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
	public void sendeAnfrage(final String absenderZeichen,
			final KalenderAnfrage anfrage) throws DatensendeException {
		OdEreignisKalenderAnfrage.Daten datum;
		ObjektFactory factory;
		ClientApplication klient;

		factory = ObjektFactory.getInstanz();
		klient = factory.getVerbindung().getLocalApplicationObject();

		datum = odAnfrage.erzeugeDatum();
		datum.setAbsender((Applikation) factory.getModellobjekt(klient));
		datum.setAbsenderZeichen(absenderZeichen);
		datum.setEreignisTypenOption(anfrage.getEreignisTypenOption());
		datum.setIntervall(anfrage.getIntervall());
		datum.getRaeumlicheGueltigkeit()
				.addAll(anfrage.getRaeumlicheGueltigkeit());
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
			final OdEreignisKalenderAntwort.Daten datum) {
		final KalenderEvent e = new KalenderEvent(this,
				datum.getAbsenderZeichen(), datum.isAenderung(),
				datum.getZustandswechsel());

		for (final KalenderListener l : listeners
				.getListeners(KalenderListener.class)) {
			l.antwortEingetroffen(e);
		}

		log.fine("Kalenderantwort wurde verteilt: " + e);
	}

}
