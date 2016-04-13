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
package de.bsvrz.sys.funclib.bitctrl.test;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Test-Applikation zur Erzeugung einigermaßen realistischer Kurzzeitdaten.
 *
 * @author BitCtrl Systems GmbH, anonymous
 */
public final class KzdTestSender extends TimerTask implements StandardApplication, ClientSenderInterface {

	/** der Logger. */
	private static final Logger LOGGER = Logger.getLogger(KzdTestSender.class.getName());

	/** Liste aller Fahrstreifen mit ihrem aktuellen Verbindungszustand. */
	private final Collection<SystemObject> objektListe = new ArrayList<SystemObject>();

	private DataDescription descKurzzeitDaten;
	private final Timer checkTimer = new Timer("KzdCheckTimer", true);
	private final Random dataSource = new Random();
	private long startZeit;
	private int idxData;
	private final ArrayList<Integer[]> fileData = new ArrayList<Integer[]>();

	/** true, wenn Testdaten aus einer Datei erzeugt werden sollen. */
	private boolean useFileData;

	/** Intervall, in dem neue Daten erzeugt werden. */
	private long delay = 60000L;

	/** verwendete Datenverteilerverbindung. */
	private ClientDavInterface verbindung;

	/** die PID des verwendeten Datentyps. */
	private String typPid;

	/** Standardkonstruktor. */
	private KzdTestSender() {
		super();
	}

	/**
	 * Prüft die Verfügbarkeit von Kurzzeitdaten aller Fahrstreifen<br>
	 * und sendet für die Stationen, die länger als X Minuten keine KZD
	 * empfangen haben eine Warnung an die BMV.
	 */
	@Override
	public void run() {
		final long now = System.currentTimeMillis();
		if (startZeit == 0) {
			startZeit = (now / 60000L) * 60000L;
		}

		System.err.printf("Sende Daten für %d \n", objektListe.size()); //$NON-NLS-1$

		int loop = 0;
		while ((loop < 20) && (startZeit < now)) {
			for (final SystemObject fahrStreifen : objektListe) {
				try {
					final Data data;
					if (useFileData) {
						data = getTestDatenFromFile();

					} else {
						data = getVerkehrsDaten();
					}
					verbindung.sendData(new ResultData(fahrStreifen, descKurzzeitDaten, startZeit, data));
				} catch (final DataNotSubscribedException e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				} catch (final SendSubscriptionNotConfirmed e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				} catch (final Exception e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				}
			}
			startZeit += (1L * delay);
			loop++;
		}
	}

	@Override
	public void initialize(final ClientDavInterface dav) {
		verbindung = dav;
		final AttributeGroup atg = dav.getDataModel().getAttributeGroup("atg.verkehrsDatenKurzZeitIntervall"); //$NON-NLS-1$
		final Aspect asp = dav.getDataModel().getAspect("asp.externeErfassung"); //$NON-NLS-1$
		descKurzzeitDaten = new DataDescription(atg, asp);

		for (final SystemObject fs : verbindung.getDataModel().getType(typPid).getElements()) {
			objektListe.add(fs);
		}

		try {
			dav.subscribeSender(this, objektListe.toArray(new SystemObject[0]), descKurzzeitDaten, SenderRole.source());
		} catch (final OneSubscriptionPerSendData e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace();
		}

		checkTimer.scheduleAtFixedRate(this, 1000L, delay);
	}

	@Override
	public void parseArguments(final ArgumentList argumentList) throws Exception {
		final String startStr = argumentList.fetchArgument("-startDatum=").asString(); //$NON-NLS-1$
		if ((startStr != null) && (startStr.length() > 0)) {
			startZeit = DateFormat.getDateInstance().parse(startStr).getTime();
		}

		if (argumentList.hasArgument("-file")) {
			final String dataFile = argumentList.fetchArgument("-file=").asString();
			if ((dataFile != null) && (dataFile.length() > 0)) {
				initWithFile(dataFile);
			}
		}

		if (argumentList.hasArgument("-typ")) {
			typPid = argumentList.fetchArgument("-typ=typ.messQuerschnitt").asString();
		}

	}

	/**
	 * Initialisiert den KzdSender mit Daten aus dem übergebenen File.
	 *
	 * @param file
	 *            der Name des Files
	 * @throws Exception
	 *             Initialisierung fehlgeschlagen
	 */
	private void initWithFile(final String file) throws Exception {

		final Properties properties = new Properties();
		properties.load(new FileInputStream(file));

		if (properties.containsKey("delay")) {
			delay = Long.parseLong(properties.get("delay").toString());
		}

		final ArrayList<Integer> values = new ArrayList<Integer>();
		for (final String currval : properties.get("qKfz").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}

		final Integer[] qKfz = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final String currval : properties.get("qLkw").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}
		final Integer[] qLkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final String currval : properties.get("vPkw").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}
		final Integer[] vPkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final String currval : properties.get("vLkw").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}
		final Integer[] vLkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final Integer element : qKfz) {
			for (final Integer element2 : qLkw) {
				for (final Integer element3 : vPkw) {
					for (final Integer element4 : vLkw) {
						final Integer[] array = new Integer[4];
						array[0] = element;
						array[1] = element2;
						array[2] = element3;
						array[3] = element4;
						fileData.add(array);

					}
				}
			}
		}

		idxData = fileData.size() - 1;
		useFileData = true;

	}

	/**
	 * Die Hauptfunktion des Observers.
	 *
	 * @param args
	 *            Programm-Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new KzdTestSender(), args);
	}

	@Override
	public void dataRequest(final SystemObject object, final DataDescription dataDescription, final byte state) {
		// TODO Automatisch erstellter Methoden-Stub

	}

	@Override
	public boolean isRequestSupported(final SystemObject object, final DataDescription dataDescription) {
		// TODO Automatisch erstellter Methoden-Stub
		return false;
	}

	/**
	 * liefert einen Satz Verkehrsdaten.
	 *
	 * @return die Daten
	 * @throws Exception
	 *             es konnten keine Daten ermittelt werden
	 */
	private Data getVerkehrsDaten() throws Exception {

		final int qKfz; // qKfz-Wert aus Telegramm
		final int qLkw; // qLkw-Wert aus Telegramm
		final int vPkw; // vPkwÄ-Wert aus Telegramm
		final int vLkw; // vLkwÄ-Wert aus Telegramm
		final int belegung; // b-Wert (Belegung)

		qKfz = (int) (dataSource.nextDouble() * 100.0);
		qLkw = (int) (dataSource.nextDouble() * 100.0);
		vPkw = (int) (dataSource.nextDouble() * 140.0);
		vLkw = (int) (dataSource.nextDouble() * 140.0);
		belegung = (int) (dataSource.nextDouble() * 100);

		return buildData(qKfz, qLkw, vPkw, vLkw, belegung);
	}

	/**
	 * liefert die Testdaten aus einer Datei.
	 *
	 * @return die Daten
	 * @throws Exception
	 */
	private Data getTestDatenFromFile() throws Exception {

		final int qKfz; // qKfz-Wert aus Telegramm
		final int qLkw; // qLkw-Wert aus Telegramm
		final int vPkw; // vPkwÄ-Wert aus Telegramm
		final int vLkw; // vLkwÄ-Wert aus Telegramm

		if (idxData >= fileData.size()) {
			idxData = 0;
		}
		qKfz = fileData.get(idxData)[0];
		qLkw = fileData.get(idxData)[1];
		vPkw = fileData.get(idxData)[2];
		vLkw = fileData.get(idxData)[3];

		idxData++;

		return buildData(qKfz, qLkw, vPkw, vLkw, 0);
	}

	/**
	 * Methode um einen Datensatz zu erzeugen.
	 *
	 * @param qKfz
	 *            die Anzahl der Kfz
	 * @param qLkw
	 *            die Anzahl der Lkw
	 * @param vPkw
	 *            die Geschwindigkeit der Pkw
	 * @param vLkw
	 *            die Geschwindigkeit der Lkw
	 * @param belegung
	 *            die Belegung
	 * @return einen neuen Verkehrsdatensatz
	 */
	private Data buildData(final int qKfz, final int qLkw, final int vPkw, final int vLkw, final int belegung) {

		final Data data = verbindung.createData(descKurzzeitDaten.getAttributeGroup());

		// Intervalllänge aus IntervalldatenHeader
		// ---------------------------------------------------------------------
		data.getTimeValue("T").setSeconds(60); //$NON-NLS-1$

		// Art der Mittelwertbildung am DE aus Betriebsparametern
		// ------------------------------------------------------
		data.getUnscaledValue("ArtMittelwertbildung").setText("gleitende Mittelwertbildung"); //$NON-NLS-1$ //$NON-NLS-2$

		final String[] valStrings = { "qKfz", "vKfz", "qLkw", "vLkw", "qPkw", "vPkw", "b", "tNetto", "sKfz", "vgKfz" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

		for (final String valString : valStrings) {
			data.getItem(valString).getUnscaledValue("Wert").setText("nicht ermittelbar"); //$NON-NLS-1$ //$NON-NLS-2$
			data.getItem(valString).getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			.setText("Nein"); //$NON-NLS-1$
			data.getItem(valString).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString).getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMaxLogisch") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			.setText("Nein"); //$NON-NLS-1$
			data.getItem(valString).getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMinLogisch") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			.setText("Nein"); //$NON-NLS-1$
			data.getItem(valString).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			.setText("Nein"); //$NON-NLS-1$
			data.getItem(valString).getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Interpoliert") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			.setText("Nein"); //$NON-NLS-1$
			data.getItem(valString).getItem("Güte").getUnscaledValue("Index").set(-1); //$NON-NLS-1$ //$NON-NLS-2$
			data.getItem(valString).getItem("Güte").getUnscaledValue("Verfahren").set(0); //$NON-NLS-1$ //$NON-NLS-2$
		}

		data.getItem("qKfz").getUnscaledValue("Wert").set(qKfz); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("qKfz").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("qLkw").getUnscaledValue("Wert").set(qLkw); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("qLkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("vPkw").getUnscaledValue("Wert").set(vPkw); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vPkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("vLkw").getUnscaledValue("Wert").set(vLkw); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vLkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("b").getUnscaledValue("Wert").set(belegung);
		data.getItem("b").getItem("Güte").getUnscaledValue("Index").set(10);

		final int qPkw;
		final int vKfz;
		// Nicht erfasste Werte qPkw und vKfz berechnen
		qPkw = (qKfz - qLkw) >= 0 ? (qKfz - qLkw) : -1;
		data.getItem("qPkw").getUnscaledValue("Wert").set(qPkw); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("qPkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		data.getItem("qPkw").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Ja"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		vKfz = (qLkw + qPkw) > 0 ? ((qLkw * vLkw) + (qPkw * vPkw)) / (qLkw + qPkw) : -1;
		data.getItem("vKfz").getUnscaledValue("Wert").set(vKfz); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vKfz").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		data.getItem("vKfz").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Ja"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		return data;

	}
}
