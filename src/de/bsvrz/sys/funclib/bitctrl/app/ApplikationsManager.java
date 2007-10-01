package de.bsvrz.sys.funclib.bitctrl.app;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import de.bsvrz.sys.funclib.application.StandardApplicationRunner;

/**
 * Kleiner Manager zum Starten und Beenden von Datenverteilerapplikationen
 * <p>
 * TODO StundardApplikationRunner ersetzen, da dieser ständig die Log-Handler erneut registriert
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
@SuppressWarnings( { "serial", "nls" })
public class ApplikationsManager extends Frame {

	/**
	 * Suffix für den Startbefehl
	 */
	private static final String START = " starten";

	/**
	 * Suffix für das Warten auf das Starten
	 */
	private static final String STARTING = " wird gestartet ...";

	/**
	 * Suffix für den Stopbefehl
	 */
	private static final String STOP = " beenden";

	/**
	 * Suffix für das Warten auf das Beenden
	 */
	private static final String STOPPING = " wird beendet ...";

	/**
	 * Standardargumente aller Applikationen
	 */
	String[] stdArgumente;

	/**
	 * Hash-Tabelle aller Applikationsdaten. Der Schlüssel ist der Name der
	 * Applikation. Der Wert ist ein Feld, dessen erster Eintrag die zu
	 * startende Klasse und die restlichen Aufrufparameter darstellen.
	 */
	Map<String, String[]> eigenschaften;

	/**
	 * Hash-Tabelle aller laufenden Applikationen
	 */
	Map<String, StandardApplikation> applikationen;

	/**
	 * Liest die Properties-Datei ein und baut den Dialog
	 * 
	 * @param file
	 *            Pfad zur Properties-Datei
	 */
	public ApplikationsManager(String file) {
		super();

		initialize();

		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(file));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		eigenschaften = new HashMap<String, String[]>();
		applikationen = new HashMap<String, StandardApplikation>();
		for (Object obj : prop.keySet()) {
			String name = (String) obj;
			if (name.equals("Argumente")) {
				stdArgumente = prop.getProperty(name).split(" "); //$NON-NLS-1$
			} else
				eigenschaften.put(name, prop.getProperty(name).split(" ")); //$NON-NLS-1$
		}

		// Buttons anlegen
		for (Entry<String, String[]> tmpEntry : eigenschaften.entrySet()) {
			Button button = new Button();
			final Entry<String, String[]> entry = tmpEntry;

			// Name des Button = Name der Applikation
			button.setLabel(entry.getKey() + START);
			button.setName(entry.getKey());
			try {
				Class.forName(entry.getValue()[0]);
			} catch (ClassNotFoundException ex) {
				button.setLabel(entry.getKey() + ": Klasse nicht gefunden");
				button.setEnabled(false);
			}

			button.addActionListener(new java.awt.event.ActionListener() {

				public void actionPerformed(java.awt.event.ActionEvent event) {
					Button src = (Button) event.getSource();
					src.setEnabled(false);

					if (src.getLabel().endsWith(START)) {
						// Applikation starten
						src.setLabel(src.getName() + STARTING);

						// Standardaufrupparameter holen
						List<String> args = new ArrayList<String>();
						args.addAll(Arrays.asList(stdArgumente));

						// Klasse und zusätzliche Argumente bestimmen
						String[] app = eigenschaften.get(src.getName());
						for (int i = 1; i < app.length; i++)
							args.add(app[i]);

						// Applikationsklasse instanziieren
						StandardApplikation applikation = null;
						try {
							applikation = (StandardApplikation) Class.forName(
									app[0]).newInstance();
						} catch (InstantiationException ex) {
							ex.printStackTrace();
							return;
						} catch (IllegalAccessException ex) {
							ex.printStackTrace();
							return;
						} catch (ClassNotFoundException ex) {
							ex.printStackTrace();
							return;
						}

						// Applikation in Liste "läuft" eintragen und starten
						applikationen.put(src.getName(), applikation);
						StandardApplicationRunner.run(applikation, args
								.toArray(new String[0]));

						src.setLabel(src.getName() + STOP);
					} else {
						// Applikation beenden
						src.setLabel(src.getName() + STOPPING);

						// Applikation stoppen und aus Liste "läuft" entfernen
						applikationen.get(src.getName()).exit();
						applikationen.remove(src.getName());

						src.setLabel(src.getName() + START);
					}
					src.setEnabled(true);
				}

			});

			add(button);
		}

		pack();
		setSize(getSize().width + 100, getSize().height);
		setVisible(true);
	}

	/**
	 * Startmethode
	 * 
	 * @param args
	 *            wird nicht benötigt
	 */
	public static void main(String args[]) {
		String file = "applikationen.properties";
		
		if (args.length == 1)
			file = args[0];
		
		new ApplikationsManager(file);
	}

	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setLayout(new GridLayout(0, 1));
		this.setSize(300, 300);
		this.setTitle("Applikationsmanager");

		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				for (StandardApplikation app : applikationen.values())
					app.exit();
				
				System.exit(0);
			}
		});
	}

} // @jve:decl-index=0:visual-constraint="10,10"
