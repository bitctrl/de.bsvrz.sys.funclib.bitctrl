package de.bsvrz.sys.funclib.bitctrl.test;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.util.dav.Umrechung;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;

public class KzdSender implements StandardApplication {

	private class Gui extends JFrame {

		private static final String suffixVerkehrsstaerke = " Fahrzeuge";

		private static final String suffixGeschwindigkeit = " km/h";

		private static final String suffixBemessungsdichte = " Fahrzeuge/km";

		JLabel lblQKfz;

		JLabel lblQLkw;

		JLabel lblVPkw;

		JLabel lblVLkw;

		JLabel lblSKfz;

		JLabel lblKB;

		Gui() {
			super("Generator für Verkehrskurzzeitdaten am Messquerschnitt");

			Container contentPane;
			JSlider slider;

			contentPane = new JPanel(new GridLayout(6, 3));

			contentPane.add(new JLabel("Maximaler Wert für QKfz"));
			slider = new JSlider(0, 10000, maxQKfz);
			lblQKfz = new JLabel(String.valueOf(maxQKfz)
					+ suffixVerkehrsstaerke);
			contentPane.add(slider);
			contentPane.add(lblQKfz);
			slider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					maxQKfz = ((JSlider) e.getSource()).getValue();
					lblQKfz.setText(String.valueOf(maxQKfz)
							+ suffixVerkehrsstaerke);
				}

			});

			contentPane.add(new JLabel("Maximaler Wert für QLkw"));
			slider = new JSlider(0, 10000, maxQKfz);
			lblQLkw = new JLabel(String.valueOf(maxQLkw)
					+ suffixVerkehrsstaerke);
			contentPane.add(slider);
			contentPane.add(lblQLkw);
			slider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					maxQLkw = ((JSlider) e.getSource()).getValue();
					lblQLkw.setText(String.valueOf(maxQLkw)
							+ suffixVerkehrsstaerke);
				}

			});

			contentPane.add(new JLabel("Maximaler Wert für VPkw"));
			slider = new JSlider(0, 255, maxVPkw);
			lblVPkw = new JLabel(String.valueOf(maxVPkw)
					+ suffixGeschwindigkeit);
			contentPane.add(slider);
			contentPane.add(lblVPkw);
			slider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					maxVPkw = ((JSlider) e.getSource()).getValue();
					lblVPkw.setText(String.valueOf(maxVPkw)
							+ suffixGeschwindigkeit);
				}

			});

			contentPane.add(new JLabel("Maximaler Wert für VLkw"));
			slider = new JSlider(0, 255, maxVLkw);
			lblVLkw = new JLabel(String.valueOf(maxVLkw)
					+ suffixGeschwindigkeit);
			contentPane.add(slider);
			contentPane.add(lblVLkw);
			slider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					maxVLkw = ((JSlider) e.getSource()).getValue();
					lblVLkw.setText(String.valueOf(maxVLkw)
							+ suffixGeschwindigkeit);
				}

			});

			contentPane.add(new JLabel("Maximaler Wert für SKfz"));
			slider = new JSlider(0, 255, maxSKfz);
			lblSKfz = new JLabel(String.valueOf(maxSKfz)
					+ suffixGeschwindigkeit);
			contentPane.add(slider);
			contentPane.add(lblSKfz);
			slider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					maxSKfz = ((JSlider) e.getSource()).getValue();
					lblSKfz.setText(String.valueOf(maxSKfz)
							+ suffixGeschwindigkeit);
				}

			});

			contentPane.add(new JLabel("Maximaler Wert für KB"));
			slider = new JSlider(0, 1000, maxKB);
			lblKB = new JLabel(String.valueOf(maxKB) + suffixBemessungsdichte);
			contentPane.add(slider);
			contentPane.add(lblKB);
			slider.addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent e) {
					maxKB = ((JSlider) e.getSource()).getValue();
					lblKB.setText(String.valueOf(maxKB)
							+ suffixBemessungsdichte);
				}

			});

			setContentPane(contentPane);
			pack();

			addWindowListener(new WindowListener() {

				@SuppressWarnings("unused")
				public void windowActivated(WindowEvent e) {
					// Nichts zu tun
				}

				@SuppressWarnings("unused")
				public void windowClosed(WindowEvent e) {
					// Nichts zu tun
				}

				@SuppressWarnings("unused")
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

				@SuppressWarnings("unused")
				public void windowDeactivated(WindowEvent e) {
					// Nichts zu tun
				}

				@SuppressWarnings("unused")
				public void windowDeiconified(WindowEvent e) {
					// Nichts zu tun
				}

				@SuppressWarnings("unused")
				public void windowIconified(WindowEvent e) {
					// Nichts zu tun
				}

				@SuppressWarnings("unused")
				public void windowOpened(WindowEvent e) {
					// Nichts zu tun
				}

			});
		}

	}

	private class Sender extends TimerTask implements ClientSenderInterface {

		private final DataDescription dbs;

		private final Random dataSource = new Random();

		public Sender(int intervall) {
			Timer timer;
			AttributeGroup atg;
			Aspect asp;

			atg = modell.getAttributeGroup("atg.verkehrsDatenKurzZeitMq");
			asp = modell.getAspect("asp.analyse");
			dbs = new DataDescription(atg, asp);

			try {
				verbindung.subscribeSender(this, objekte, dbs, SenderRole
						.source());
			} catch (OneSubscriptionPerSendData e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
			logger.config("Anmeldung für " + objekte.size()
					+ " Messquerschnitte durchgeführt.");

			timer = new Timer(false);
			timer.scheduleAtFixedRate(this, 5000, intervall * 1000);

			new Gui().setVisible(true);
		}

		/**
		 * {@inheritDoc}
		 */
		public void dataRequest(SystemObject object,
				DataDescription dataDescription, byte state) {
			// TODO Auto-generated method stub

		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
			// TODO Auto-generated method stub
			return false;
		}

		public void run() {
			long zeitstempel;

			zeitstempel = System.currentTimeMillis();
			for (SystemObject so : objekte) {
				Data data;

				data = getVerkehrsDaten();
				try {
					verbindung.sendData(new ResultData(so, dbs, zeitstempel,
							data));
				} catch (DataNotSubscribedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1);
				} catch (SendSubscriptionNotConfirmed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1);
				}
			}
			logger.info("Daten gesendet.");
		}

		private Data getVerkehrsDaten() {

			int qKfz, qLkw, vPkw, vLkw, sKfz, kb;

			qKfz = (int) (dataSource.nextDouble() * maxQKfz);
			qLkw = (int) (dataSource.nextDouble() * Math.min(qKfz, maxQLkw));
			vPkw = (int) (dataSource.nextDouble() * maxVPkw);
			vLkw = (int) (dataSource.nextDouble() * maxVLkw);
			sKfz = (int) (dataSource.nextDouble() * maxSKfz);
			kb = (int) (dataSource.nextDouble() * maxKB);

			return buildData(qKfz, qLkw, vPkw, vLkw, sKfz, kb);
		}

		private Data buildData(int qKfz, int qLkw, int vPkw, int vLkw,
				int sKfz, int kb) {
			Data data;

			logger.fine("Eingangswerte: QKfz=" + qKfz + ", QLkw=" + qLkw
					+ ", VPkw=" + vPkw + ", VLkw=" + vLkw + ", SKfz=" + sKfz
					+ ", KB=" + kb);

			data = verbindung.createData(dbs.getAttributeGroup());

			final String[] valStrings = { "QKfz", "VKfz", "QLkw", "VLkw",
					"QPkw", "VPkw", "B", "SKfz", "BMax", "VgKfz", "ALkw",
					"KKfz", "KPkw", "KLkw", "QB", "KB", "VDelta" };

			for (int idx = 0; idx < valStrings.length; idx++) {
				data.getItem(valStrings[idx]).getUnscaledValue("Wert").setText(
						"nicht ermittelbar");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"Erfassung").getUnscaledValue("NichtErfasst").setText(
						"Nein");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"PlFormal").getUnscaledValue("WertMax").setText("Nein");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"PlFormal").getUnscaledValue("WertMin").setText("Nein");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"PlLogisch").getUnscaledValue("WertMaxLogisch")
						.setText("Nein");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"PlLogisch").getUnscaledValue("WertMinLogisch")
						.setText("Nein");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"MessWertErsetzung").getUnscaledValue("Implausibel")
						.setText("Nein");
				data.getItem(valStrings[idx]).getItem("Status").getItem(
						"MessWertErsetzung").getUnscaledValue("Interpoliert")
						.setText("Nein");
				data.getItem(valStrings[idx]).getItem("Güte").getUnscaledValue(
						"Index").set(-1);
				data.getItem(valStrings[idx]).getItem("Güte").getUnscaledValue(
						"Verfahren").set(0);
			}

			data.getItem("QKfz").getUnscaledValue("Wert").set(qKfz);
			data.getItem("QKfz").getItem("Güte").getUnscaledValue("Index").set(
					10);

			data.getItem("QLkw").getUnscaledValue("Wert").set(qLkw);
			data.getItem("QLkw").getItem("Güte").getUnscaledValue("Index").set(
					10);

			data.getItem("VPkw").getUnscaledValue("Wert").set(vPkw);
			data.getItem("VPkw").getItem("Güte").getUnscaledValue("Index").set(
					10);

			data.getItem("VLkw").getUnscaledValue("Wert").set(vLkw);
			data.getItem("VLkw").getItem("Güte").getUnscaledValue("Index").set(
					10);

			data.getItem("SKfz").getUnscaledValue("Wert").set(sKfz);
			data.getItem("SKfz").getItem("Güte").getUnscaledValue("Index").set(
					10);

			data.getItem("KB").getUnscaledValue("Wert").set(kb);
			data.getItem("KB").getItem("Güte").getUnscaledValue("Index")
					.set(10);

			// Nicht erfasste Werte qPkw, vKfz und qb berechnen
			Integer qPkw, vKfz, qb, aLkw;

			aLkw = Umrechung.getALkw(qLkw, qKfz);
			data.getItem("ALkw").getUnscaledValue("Wert").set(aLkw);
			data.getItem("ALkw").getItem("Güte").getUnscaledValue("Index").set(
					10);

			qPkw = Umrechung.getQPkw(qKfz, qLkw);
			if (qPkw != null) {
				data.getItem("QPkw").getUnscaledValue("Wert").set(qPkw);
				data.getItem("QPkw").getItem("Güte").getUnscaledValue("Index")
						.set(10);
				data.getItem("QPkw").getItem("Status").getItem("Erfassung")
						.getUnscaledValue("NichtErfasst").setText("Ja");
			}

			vKfz = Umrechung.getVKfz(qLkw, qKfz, vPkw, vLkw);
			if (vKfz != null) {
				data.getItem("VKfz").getUnscaledValue("Wert").set(vKfz);
				data.getItem("VKfz").getItem("Güte").getUnscaledValue("Index")
						.set(10);
				data.getItem("VKfz").getItem("Status").getItem("Erfassung")
						.getUnscaledValue("NichtErfasst").setText("Ja");
			}

			qb = Umrechung.getQB(qLkw, qKfz, vPkw, vLkw, 0.5f, 1);
			if (qb != null) {
				data.getItem("QB").getUnscaledValue("Wert").set(qb);
				data.getItem("QB").getItem("Güte").getUnscaledValue("Index")
						.set(10);
				data.getItem("QB").getItem("Status").getItem("Erfassung")
						.getUnscaledValue("NichtErfasst").setText("Ja");
			}

			logger.fine("Berechnete Werte: QPkw=" + qPkw + ", VKfz=" + vKfz
					+ ", QB=" + qb + ", ALkw=" + aLkw);

			return data;
		}

	}

	int maxQKfz = 1000;

	int maxQLkw = 200;

	int maxVPkw = 130;

	int maxVLkw = 80;

	int maxSKfz = 30;

	int maxKB = 100;

	Debug logger;

	ClientDavInterface verbindung;

	DataModel modell;

	List<SystemObject> objekte;

	Sender sender;

	private String typPid;

	private int intervall;

	/**
	 * {@inheritDoc}
	 */
	public void initialize(ClientDavInterface connection) throws Exception {

		logger = Debug.getLogger();
		verbindung = connection;
		modell = verbindung.getDataModel();

		objekte = new ArrayList<SystemObject>();
		for (SystemObject fs : verbindung.getDataModel().getType(typPid)
				.getElements()) {
			objekte.add(fs);
		}

		sender = new Sender(intervall);

		// Speicher freigeben
		typPid = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void parseArguments(ArgumentList argumentList) throws Exception {
		typPid = argumentList
				.fetchArgument("-typ=typ.messQuerschnittAllgemein").asString();

		// Intervall in Sekunden
		intervall = Integer.valueOf(argumentList.fetchArgument("-intervall=10")
				.asString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StandardApplicationRunner.run(new KzdSender(), args);

	}

}
