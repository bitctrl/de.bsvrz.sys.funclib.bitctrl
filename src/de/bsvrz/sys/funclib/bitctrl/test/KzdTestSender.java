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
 */
public class KzdTestSender extends TimerTask implements StandardApplication,
		ClientSenderInterface {

	/**
	 * der Logger
	 */
	protected final Logger LOGGER = Logger.getLogger(KzdTestSender.class
			.getName());

	/**
	 * Liste aller Fahrstreifen mit ihrem aktuellen Verbindungszustand
	 */
	protected Collection<SystemObject> objektListe = new ArrayList<SystemObject>();

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
	private ArrayList<Integer[]> fileData = new ArrayList<Integer[]>();

	/**
	 * true, wenn Testdaten aus einer Datei erzeugt werden sollen.
	 */
	private boolean useFileData = false;

	/**
	 * intervall, in dem neue Daten erzeugt werden
	 */
	private long delay = 60000L;

	/**
	 * verwendete Datenverteilerverbindung.
	 */
	private ClientDavInterface verbindung;

	/**
	 * die PID des verwendeten Datentyps.
	 */
	private String typPid;

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
		long now = System.currentTimeMillis();
		if (startZeit == 0) {
			startZeit = (now / 60000L) * 60000L;
		}

		System.err.printf("Sende Daten für %d \n", objektListe.size()); //$NON-NLS-1$

		int loop = 0;
		while ((loop < 20) && (startZeit < now)) {
			for (SystemObject fahrStreifen : objektListe) {
				try {
					Data data;
					if (useFileData) {
						data = getTestDatenFromFile();

					} else {
						data = getVerkehrsDaten();
					}
					verbindung.sendData(new ResultData(fahrStreifen,
							descKurzzeitDaten, startZeit, data));
				} catch (DataNotSubscribedException e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				} catch (SendSubscriptionNotConfirmed e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Automatisch erstellter Catch-Block
					e.printStackTrace();
				}
			}
			startZeit += (1L * delay);
			loop++;
		}
	}

	/**
	 * @param dav
	 * @see StandardApplication#initialize(ClientDavInterface)
	 */
	public void initialize(ClientDavInterface dav) {
		this.verbindung = dav;
		AttributeGroup atg = dav.getDataModel().getAttributeGroup(
				"atg.verkehrsDatenKurzZeitIntervall"); //$NON-NLS-1$
		Aspect asp = dav.getDataModel().getAspect("asp.externeErfassung"); //$NON-NLS-1$
		descKurzzeitDaten = new DataDescription(atg, asp);

		for (SystemObject fs : verbindung.getDataModel().getType(typPid)
				.getElements()) {
			objektListe.add(fs);
		}

		try {
			dav.subscribeSender(this, objektListe.toArray(new SystemObject[0]),
					descKurzzeitDaten, SenderRole.source());
		} catch (OneSubscriptionPerSendData e) {
			// TODO Automatisch erstellter Catch-Block
			e.printStackTrace();
		}

		checkTimer.scheduleAtFixedRate(this, 1000L, delay);
	}

	/**
	 * @param argumentList
	 * @throws Exception
	 * @see StandardApplication#parseArguments(ArgumentList)
	 */
	public void parseArguments(ArgumentList argumentList) throws Exception {
		String startStr = argumentList.fetchArgument("-startDatum=").asString(); //$NON-NLS-1$
		if ((startStr != null) && (startStr.length() > 0)) {
			startZeit = DateFormat.getDateInstance().parse(startStr).getTime();
		}

		if (argumentList.hasArgument("-file")) {
			String dataFile = argumentList.fetchArgument("-file=").asString();
			if ((dataFile != null) && (dataFile.length() > 0)) {
				initWithFile(dataFile);
			}
		}

		if (argumentList.hasArgument("-typ")) {
			typPid = argumentList.fetchArgument("-typ=typ.messQuerschnitt")
					.asString();
		}

	}

	/**
	 * @param file
	 * @throws Exception
	 */
	private void initWithFile(String file) throws Exception {

		Properties properties = new Properties();
		properties.load(new FileInputStream(file));

		if (properties.containsKey("delay")) {
			delay = Long.parseLong(properties.get("delay").toString());
		}

		ArrayList<Integer> values = new ArrayList<Integer>();
		for (String currval : properties.get("qKfz").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}

		Integer[] qKfz = values.toArray(new Integer[values.size()]);
		values.clear();

		for (String currval : properties.get("qLkw").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}
		Integer[] qLkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (String currval : properties.get("vPkw").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}
		Integer[] vPkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (String currval : properties.get("vLkw").toString().split(",")) {
			values.add(Integer.parseInt(currval));
		}
		Integer[] vLkw = values.toArray(new Integer[values.size()]);
		values.clear();

		for (int i = 0; i < qKfz.length; i++) {
			for (int j = 0; j < qLkw.length; j++) {
				for (int k = 0; k < vPkw.length; k++) {
					for (int l = 0; l < vLkw.length; l++) {
						Integer[] array = new Integer[4];
						array[0] = qKfz[i];
						array[1] = qLkw[j];
						array[2] = vPkw[k];
						array[3] = vLkw[l];
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
	 * @see ClientSenderInterface#dataRequest(SystemObject, DataDescription,
	 *      byte)
	 */
	public void dataRequest(SystemObject object,
			DataDescription dataDescription, byte state) {
		// TODO Automatisch erstellter Methoden-Stub

	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see ClientSenderInterface#isRequestSupported(SystemObject,
	 *      DataDescription)
	 */
	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		// TODO Automatisch erstellter Methoden-Stub
		return false;
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

		_qKfz_ = (int) (dataSource.nextDouble() * 100.0);
		_qLkw_ = (int) (dataSource.nextDouble() * 100.0);
		_vPkw_ = (int) (dataSource.nextDouble() * 140.0);
		_vLkw_ = (int) (dataSource.nextDouble() * 140.0);
		_b_ = (int) (dataSource.nextDouble() * 100);

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

		final Data data = verbindung.createData(descKurzzeitDaten
				.getAttributeGroup());

		// Intervalllänge aus IntervalldatenHeader
		// ---------------------------------------------------------------------
		data.getTimeValue("T").setSeconds(60); //$NON-NLS-1$

		// Art der Mittelwertbildung am DE aus Betriebsparametern
		// ------------------------------------------------------
		data
				.getUnscaledValue("ArtMittelwertbildung").setText("gleitende Mittelwertbildung"); //$NON-NLS-1$ //$NON-NLS-2$

		final String[] valStrings = {
				"qKfz", "vKfz", "qLkw", "vLkw", "qPkw", "vPkw", "b", "tNetto", "sKfz", "vgKfz" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$

		for (int idx = 0; idx < valStrings.length; idx++) {
			data.getItem(valStrings[idx])
					.getUnscaledValue("Wert").setText("nicht ermittelbar"); //$NON-NLS-1$ //$NON-NLS-2$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("PlFormal").getUnscaledValue("WertMax").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("PlFormal").getUnscaledValue("WertMin").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMaxLogisch").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("PlLogisch").getUnscaledValue("WertMinLogisch").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Implausibel").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data
					.getItem(valStrings[idx])
					.getItem("Status").getItem("MessWertErsetzung").getUnscaledValue("Interpoliert").setText("Nein"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			data.getItem(valStrings[idx])
					.getItem("Güte").getUnscaledValue("Index").set(-1); //$NON-NLS-1$ //$NON-NLS-2$
			data.getItem(valStrings[idx])
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
		data
				.getItem("qPkw").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Ja"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		_vKfz_ = (_qLkw_ + _qPkw_) > 0 ? ((_qLkw_ * _vLkw_) + (_qPkw_ * _vPkw_))
				/ (_qLkw_ + _qPkw_)
				: -1;
		data.getItem("vKfz").getUnscaledValue("Wert").set(_vKfz_); //$NON-NLS-1$ //$NON-NLS-2$
		data.getItem("vKfz").getItem("Güte").getUnscaledValue("Index").set(10); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		data
				.getItem("vKfz").getItem("Status").getItem("Erfassung").getUnscaledValue("NichtErfasst").setText("Ja"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		return data;

	}
}
