package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.daf.Konfigurationsbereich;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten.UDSHelligkeitSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten.UDSNiederschlagsintensitaetSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten.UDSSichtweiteSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten.UDSWindgeschwindigkeitSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten.UDSWindrichtungSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten.UmfelddatenmessstelleSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.verkehr.VerkehrskurzzeitdatenSender;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;

public class TestDatenGenerator extends JFrame implements StandardApplication,
		ChangeListener {

	private ClientDavInterface verbindung;

	private Debug logger;

	private List<SystemObject> objekte;

	private JSpinner spnIntervall;

	private static final int INTERVALL = 60;

	private final JPanel messwertOptionen;

	private final JPanel messwerte;

	private String konfigurationsbereich;

	private Map<Class<? extends MesswertSender>, MesswertSender> senderListe;

	public static void main(String[] args) {
		StandardApplicationRunner.run(new TestDatenGenerator(), args);

	}

	public TestDatenGenerator() {
		super("Generischer Testdatengenerator");

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Kann theoretisch nicht eintreten
			e.printStackTrace();
		}
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		messwertOptionen = new JPanel(new FlowLayout());
		add(messwertOptionen, BorderLayout.CENTER);

		messwerte = new JPanel(new GridLayout(0, 1));
		add(messwerte, BorderLayout.WEST);
	}

	public void initialize(ClientDavInterface verbindung) throws Exception {
		ConfigurationArea kb;
		Integer intervalle[];
		JPanel allgemeineOptionen;
		JLabel lblIntervall;

		logger = Debug.getLogger();
		this.verbindung = verbindung;

		// Gui bauen
		allgemeineOptionen = new JPanel(new GridLayout(1, 2));
		lblIntervall = new JLabel("Intervall generierter Werte in Sekunden:");
		allgemeineOptionen.add(lblIntervall);
		intervalle = new Integer[] { 10, 30, 60, 300, 600 };
		spnIntervall = new JSpinner(new SpinnerListModel(intervalle));
		spnIntervall.setValue(INTERVALL);
		spnIntervall.addChangeListener(this);
		allgemeineOptionen.add(spnIntervall);
		add(allgemeineOptionen, BorderLayout.NORTH);

		// Systemobjekte bestimmmen
		objekte = new ArrayList<SystemObject>();
		kb = verbindung.getDataModel().getConfigurationArea(
				konfigurationsbereich);
		if (kb == null) {
			throw new IllegalStateException("Der Konfigurationsbereich "
					+ konfigurationsbereich + " existiert nicht.");
		}
		objekte.addAll(Konfigurationsbereich.getObjekte(kb));

		// Sender starten
		// TODO: Aufzählung durch Aufrufparameter ersetzen
		add(VerkehrskurzzeitdatenSender.class);
		add(UDSHelligkeitSender.class);
		add(UDSNiederschlagsintensitaetSender.class);
		add(UDSSichtweiteSender.class);
		add(UDSWindgeschwindigkeitSender.class);
		add(UDSWindrichtungSender.class);
		add(UmfelddatenmessstelleSender.class);

		pack();
		setVisible(true);
	}

	public void parseArguments(ArgumentList argumentList) throws Exception {
		konfigurationsbereich = argumentList.fetchArgument("-kb=")
				.asNonEmptyString();
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == spnIntervall) {
			for (MesswertSender sender : senderListe.values()) {
				sender.setTimer((Integer) spnIntervall.getValue());
			}
		}
	}

	private void add(Class<? extends MesswertSender> senderKlasse) {
		Constructor<? extends MesswertSender> konstruktor;
		MesswertSender sender = null;

		try {
			konstruktor = senderKlasse.getConstructor(ClientDavInterface.class,
					Collection.class);
			sender = konstruktor.newInstance(verbindung, objekte);
		} catch (Exception ex) {
			logger.error("Messwertsender konnte nicht geladen werden",
					new Object[] { senderKlasse, ex.getLocalizedMessage() });
			System.exit(-1);
		}

		for (MesswertOption mw : sender.getMesswerte()) {
			messwertOptionen.add(mw);
		}

		for (Anzeige anz : sender.getAnzeigen()) {
			messwerte.add(anz);
		}

		sender.setTimer((Integer) spnIntervall.getValue());
		if (senderListe == null) {
			senderListe = new HashMap<Class<? extends MesswertSender>, MesswertSender>();
		}

		if (!senderListe.containsKey(senderKlasse)) {
			senderListe.put(senderKlasse, sender);
		}
	}

}
