/*---------------------------------------------------------------*/
/* Copyright by BitCtrl Systems Leipzig */
/* BitCtrl Systems Leipzig */
/* Weisenfelser Str. 67 */
/* 04229 Leipzig */
/* Tel.: +49 341 49067 - 0 */
/* Fax.: +49 341 49067 - 15 */
/* mailto:info@bitctrl.de */
/* http://www.bitctrl.de */
/*---------------------------------------------------------------*/
package de.bsvrz.sys.funclib.bitctrl.testdaten;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
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
 */
public class KzdTestSender extends TimerTask implements StandardApplication,
		ClientSenderInterface {

	private static double VLKW_MAX = 100.0;

	private static double QKFZ_MAX = 80.0;

	private static double VPKW_MAX = 140.0;

	/**
	 * der Logger
	 */
	protected final Logger LOGGER = Logger.getLogger(KzdTestSender.class
			.getName());

	/**
	 * Liste aller Fahrstreifen mit ihrem aktuellen Verbindungszustand
	 */
	protected final Collection<SystemObject> fahrStreifenListe = new ArrayList<SystemObject>();

	/**
	 * 
	 */
	private DataDescription descKurzzeitDaten;

	/**
	 * 
	 */
	Timer checkTimer = new Timer("KzdCheckTimer", true); //$NON-NLS-1$

	/**
	 * 
	 */
	Random dataSource = new Random();

	/**
	 * 
	 */
	private long startZeit;

	/**
	 * index
	 */
	private int idx_data = 0;

	/**
	 * 
	 */
	private final ArrayList<Integer[]> fileData = new ArrayList<Integer[]>();

	/**
	 * true, wenn Testdaten aus einer Datei erzeugt werden sollen.
	 */
	private boolean useFileData = false;

	/**
	 * intervall, in dem neue Daten erzeugt werden
	 */
	private long delay = 60000L;

	/**
	 * Die Liste aller DE im System
	 */
	private SystemObject[] alleDeLve;

	private DataDescription deFehlerBeschreibung;

	private ClientDavInterface con;

	private boolean aktiv;

	private final Map<SystemObject, ResultData> latestResultsDeFehler = new LinkedHashMap<SystemObject, ResultData>();

	private final Map<SystemObject, ResultData> latestResultsKzd = new LinkedHashMap<SystemObject, ResultData>();

	/**
	 * Standardkonstruktor
	 */
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
			startZeit = (now / delay) * delay;
		}

		LOGGER.fine("Sende Daten für " + fahrStreifenListe.size() + " Fahrstreifen"); //$NON-NLS-1$

		int loop = 0;
		while ((loop < 20) && (startZeit < now)) {
			for (final SystemObject fahrStreifen : fahrStreifenListe) {
				try {
					Data data;
					if (useFileData) {
						data = getTestDatenFromFile();

					} else {
						data = getVerkehrsDaten();
					}
					final ResultData result = new ResultData(fahrStreifen,
							descKurzzeitDaten, startZeit - delay, data);
					latestResultsKzd.put(fahrStreifen, result);
					if (aktiv) {
						con.sendData(result);
						LOGGER.info("Kurzzeitdaten versendet: " + result);
					}
				} catch (final DataNotSubscribedException e) {
					LOGGER.warning(e.getLocalizedMessage());
				} catch (final SendSubscriptionNotConfirmed e) {
					LOGGER.warning(e.getLocalizedMessage());
				} catch (final Exception e) {
					LOGGER.severe(e.getLocalizedMessage());
				}
			}
			startZeit += (1L * delay);
			loop++;
		}
	}

	/**
	 * @param dav
	 * @see sys.funclib.application.StandardApplication#initialize(stauma.dav.clientside.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface dav) throws Exception {
		con = dav;

		// als Quelle fuer alle DeLVE anmelden, damit der globaleDeFehler
		// auf OK gesetzt wird
		alleDeLve = dav.getDataModel().getType("typ.deLve").getElements()
				.toArray(new SystemObject[0]);
		deFehlerBeschreibung = new DataDescription(dav.getDataModel()
				.getAttributeGroup("atg.tlsGloDeFehler"), dav.getDataModel()
				.getAspect("asp.tlsAntwort"));

		final AttributeGroup atg = dav.getDataModel().getAttributeGroup(
				"atg.verkehrsDatenKurzZeitIntervall"); //$NON-NLS-1$
		final Aspect asp = dav.getDataModel().getAspect("asp.externeErfassung"); //$NON-NLS-1$
		descKurzzeitDaten = new DataDescription(atg, asp);

		fahrStreifenListe.addAll(dav.getDataModel().getType("typ.fahrStreifen")
				.getElements());

		synchronized (fahrStreifenListe) {
			try {
				for (final SystemObject obj : alleDeLve) {
					final ResultData initialData = latestResultsDeFehler
							.get(obj);
					if (initialData != null) {
						con.subscribeSource(this, initialData);
					} else {
						con.subscribeSender(this, obj, deFehlerBeschreibung,
								SenderRole.source());
					}
				}
			} catch (final OneSubscriptionPerSendData e) {
				LOGGER.warning(e.getLocalizedMessage());
			}
			try {
				for (final SystemObject obj : fahrStreifenListe) {
					final ResultData initialData = latestResultsKzd.get(obj);
					if (initialData != null) {
						con.subscribeSource(this, initialData);
					} else {
						con.subscribeSender(this, obj, descKurzzeitDaten,
								SenderRole.source());
					}
				}
			} catch (final OneSubscriptionPerSendData e) {
				LOGGER.severe(e.getLocalizedMessage());
			} catch (final Exception e) {
				LOGGER.severe(e.getLocalizedMessage());
			}
			aktiv = true;

			// TODO: Zeitpunkt mit dem Datenintervall synchronisieren.
			checkTimer.scheduleAtFixedRate(this, 1000L, delay);
		}
	}

	/**
	 * @param argumentList
	 * @throws Exception
	 * @see sys.funclib.application.StandardApplication#parseArguments(sys.funclib.ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		final String startStr = argumentList
				.fetchArgument("-startDatum=").asString(); //$NON-NLS-1$
		if ((startStr != null) && (startStr.length() > 0)) {
			startZeit = DateFormat.getDateInstance().parse(startStr).getTime();
		}

		if (argumentList.hasArgument("-delay")) {
			delay = argumentList.fetchArgument("-delay=60000").longValue();
		}

		if (argumentList.hasArgument("-file")) {
			final String dataFile = argumentList.fetchArgument("-file=")
					.asString();
			if ((dataFile != null) && (dataFile.length() > 0)) {
				initWithFile(dataFile);
			}
		}

		KzdTestSender.QKFZ_MAX = argumentList.fetchArgument("-qKfzMax=80.0")
				.doubleValue();
		KzdTestSender.VPKW_MAX = argumentList.fetchArgument("-vPkwMax=140.0")
				.doubleValue();
		KzdTestSender.VLKW_MAX = argumentList.fetchArgument("-vLkwMax=100.0")
				.doubleValue();

	}

	private void initWithFile(final String file) throws Exception {

		final Properties properties = new Properties();
		properties.load(new FileInputStream(file));

		if (properties.containsKey("delay")) {
			delay = Long.parseLong(properties.get("delay").toString());
		}

		final ArrayList<Integer> values = new ArrayList<Integer>();
		for (final String currval : properties.get("qKfz").toString()
				.split(",")) {
			values.add(Integer.parseInt(currval));
		}

		final Integer[] qKfz = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final String currval : properties.get("qLkw").toString()
				.split(",")) {
			values.add(Integer.parseInt(currval));
		}
		final Integer[] qLkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final String currval : properties.get("vPkw").toString()
				.split(",")) {
			values.add(Integer.parseInt(currval));
		}
		final Integer[] vPkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (final String currval : properties.get("vLkw").toString()
				.split(",")) {
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

		idx_data = fileData.size() - 1;
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

	/**
	 * {@inheritDoc}.
	 * 
	 * @see stauma.dav.clientside.ClientSenderInterface#dataRequest(stauma.dav.configuration.interfaces.SystemObject,
	 *      stauma.dav.clientside.DataDescription, byte)
	 */
	public void dataRequest(final SystemObject object,
			final DataDescription dataDescription, final byte state) {
		if (state == ClientSenderInterface.START_SENDING
				&& dataDescription.equals(deFehlerBeschreibung)) {

			versendeDeFehlerZustandOk();
		}

	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see stauma.dav.clientside.ClientSenderInterface#isRequestSupported(stauma.dav.configuration.interfaces.SystemObject,
	 *      stauma.dav.clientside.DataDescription)
	 */
	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
		boolean result = false;
		if (dataDescription.equals(deFehlerBeschreibung)
				|| dataDescription.equals(descKurzzeitDaten)) {
			result = true;
		} else {
			LOGGER.warning("Unerwarteter Request: Attributgruppe "
					+ dataDescription.getAttributeGroup().getName()
					+ " Aspekt " + dataDescription.getAspect().getName()
					+ " Objekt " + object.getPid());
		}
		return result;
	}

	/**
	 * @return die Daten
	 * @throws Exception
	 */
	private Data getVerkehrsDaten() throws Exception {

		int _qKfz_; // qKfz-Wert aus Telegramm
		int _qLkw_; // qLkw-Wert aus Telegramm
		int _vPkw_; // vPkwÄ-Wert aus Telegramm
		int _vLkw_; // vLkwÄ-Wert aus Telegramm
		int _b_; // b-Wert (Belegung)

		_qKfz_ = (int) (dataSource.nextDouble() * KzdTestSender.QKFZ_MAX); // 0
		// <=
		// qKfz
		// <
		// 80
		// Fz/Intervall
		final double anteilLkw = dataSource.nextDouble() * .4; // Zwischen 0%
		// und 40%
		// Lkw
		_qLkw_ = (int) (_qKfz_ * anteilLkw);
		if (_qKfz_ <= 0) {
			_vPkw_ = -1; // Geschw. ist nicht ermittelbar, wenn keine gefahren
			// sind!
		} else {
			_vPkw_ = (int) (dataSource.nextDouble() * KzdTestSender.VPKW_MAX);
		}
		if (_qLkw_ <= 0) {
			_vLkw_ = -1; // Geschw. ist nicht ermittelbar, wenn keine gefahren
			// sind!
		} else {
			_vLkw_ = (int) (dataSource.nextDouble() * KzdTestSender.VLKW_MAX);
		}
		_b_ = (int) (dataSource.nextDouble() * 40.);

		return buildData(_qKfz_, _qLkw_, _vPkw_, _vLkw_, _b_);
	}

	/**
	 * @return die Daten
	 * @throws Exception
	 */
	private Data getTestDatenFromFile() throws Exception {

		int _qKfz_; // qKfz-Wert aus Telegramm
		int _qLkw_; // qLkw-Wert aus Telegramm
		int _vPkw_; // vPkwÄ-Wert aus Telegramm
		int _vLkw_; // vLkwÄ-Wert aus Telegramm

		if (idx_data >= fileData.size()) {
			idx_data = 0;
		}
		_qKfz_ = fileData.get(idx_data)[0];
		_qLkw_ = fileData.get(idx_data)[1];
		_vPkw_ = fileData.get(idx_data)[2];
		_vLkw_ = fileData.get(idx_data)[3];

		idx_data++;

		return buildData(_qKfz_, _qLkw_, _vPkw_, _vLkw_, 0);
	}

	/**
	 * Methode um einen Datensatz zu erzeugen.
	 * 
	 * @param _qKfz_
	 *            die Anzahl der Kfz
	 * @param _qLkw_
	 *            die Anzahl der Lkw
	 * @param _vPkw_
	 *            die Geschwindigkeit der Pkw
	 * @param _vLkw_
	 *            die Geschwindigkeit der Lkw
	 * @param _b_
	 *            die Belegung
	 * @return einen neuen Verkehrsdatensatz
	 */
	private Data buildData(final int _qKfz_, final int _qLkw_,
			final int _vPkw_, final int _vLkw_, final int _b_) {

		final Data data = con.createData(descKurzzeitDaten.getAttributeGroup());

		// Intervalllänge aus IntervalldatenHeader
		// ---------------------------------------------------------------------
		data.getTimeValue("T").setMillis(delay);//etSeconds(60); //$NON-NLS-1$

		// Art der Mittelwertbildung am DE aus Betriebsparametern
		// ------------------------------------------------------
		data.getUnscaledValue("ArtMittelwertbildung").setText("gleitende Mittelwertbildung"); //$NON-NLS-1$ //$NON-NLS-2$

		final String[] valStrings = {
				"qKfz", "vKfz", "qLkw", "vLkw", "qPkw", "vPkw", "b", "tNetto", "sKfz", "vgKfz" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

		for (final String valString : valStrings) {
			data.getItem(valString)
					.getUnscaledValue("Wert").setText("nicht ermittelbar"); //$NON-NLS-1$ //$NON-NLS-2$
			data.getItem(valString)
					.getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMaxLogisch").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMinLogisch").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Interpoliert").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valString)
					.getItem("Güte").getUnscaledValue("Index").set(-1); //$NON-NLS-1$ //$NON-NLS-2$
			data.getItem(valString)
					.getItem("Güte").getUnscaledValue("Verfahren").set(0); //$NON-NLS-1$ //$NON-NLS-2$
		}

		data.getItem("qKfz").getUnscaledValue("Wert").set(_qKfz_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("qKfz").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("qLkw").getUnscaledValue("Wert").set(_qLkw_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("qLkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("vPkw").getUnscaledValue("Wert").set(_vPkw_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vPkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("vLkw").getUnscaledValue("Wert").set(_vLkw_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vLkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		data.getItem("b").getUnscaledValue("Wert").set(_b_);
		data.getItem("b").getItem("Güte").getUnscaledValue("Index").set(10);

		int _qPkw_;
		int _vKfz_;
		// Nicht erfasste Werte qPkw und vKfz berechnen
		_qPkw_ = (_qKfz_ - _qLkw_) >= 0 ? (_qKfz_ - _qLkw_) : -1;
		data.getItem("qPkw").getUnscaledValue("Wert").set(_qPkw_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("qPkw").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		data.getItem("qPkw").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Ja"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		_vKfz_ = (_qLkw_ + _qPkw_) > 0 ? ((_qLkw_ * _vLkw_) + (_qPkw_ * _vPkw_))
				/ (_qLkw_ + _qPkw_)
				: -1;
		data.getItem("vKfz").getUnscaledValue("Wert").set(_vKfz_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vKfz").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		data.getItem("vKfz").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Ja"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		return data;

	}

	/**
	 * Versendet einen DaV-Datensatz an ein oder mehrere Systemobjekte
	 * (normalerweise eine DE-Liste, ansonsten vermutlich DaV-Exception)
	 * 
	 * @param deList
	 *            die Liste der Systemobjekte
	 * @param data
	 *            der Datensatz
	 */
	private void versendeDeFehlerZuMehrerenDE(final SystemObject[] deList,
			final Data data) {
		final ResultData[] results = new ResultData[deList.length];
		int loop = 0;
		for (final SystemObject obj : deList) {
			final long timeStamp = System.currentTimeMillis();
			final ResultData result = new ResultData(obj, deFehlerBeschreibung,
					timeStamp, data);
			latestResultsDeFehler.put(obj, result);
			results[loop] = result;
			++loop;
		}
		if (aktiv) {
			try {
				con.sendData(results);
			} catch (final DataNotSubscribedException e) {
				LOGGER.warning("Fehler beim versenden des Globalen-DE-Fehlers: "
						+ e.getLocalizedMessage());
			} catch (final SendSubscriptionNotConfirmed e) {
				LOGGER.warning("Fehler beim versenden des Globalen-DE-Fehlers: "
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Versendet an alle WZG-DE den globalen DE-Fehlerzustand OK
	 */
	private void versendeDeFehlerZustandOk() {
		final Data data = con.createData(con.getDataModel().getAttributeGroup(
				"atg.tlsGloDeFehler"));

		data.getUnscaledValue("DEFehlerStatus").set(0);
		data.getUnscaledValue("DEProjektierungsStatus").set(0);
		data.getUnscaledValue("DEKanalStatus").set(0);
		data.getUnscaledValue("HerstellerDefinierterCode").set(0);
		data.getUnscaledValue("Hersteller").set(0);

		versendeDeFehlerZuMehrerenDE(alleDeLve, data);
	}

}
