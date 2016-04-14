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
import java.util.Map.Entry;
import java.util.Properties;

import de.bsvrz.sys.funclib.application.StandardApplicationRunner;

/**
 * Kleiner Manager zum Starten und Beenden von Datenverteilerapplikationen.
 * <p>
 * TODO StundardApplikationRunner ersetzen, da dieser ständig die Log-Handler
 * erneut registriert
 *</p>
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
@SuppressWarnings({ "serial", "nls" })
public class ApplikationsManager extends Frame {

	/**
	 * Suffix für den Startbefehl.
	 */
	private static final String START = " starten";

	/**
	 * Suffix für das Warten auf das Starten.
	 */
	private static final String STARTING = " wird gestartet ...";

	/**
	 * Suffix für den Stopbefehl.
	 */
	private static final String STOP = " beenden";

	/**
	 * Suffix für das Warten auf das Beenden.
	 */
	private static final String STOPPING = " wird beendet ...";

	/**
	 * Standardargumente aller Applikationen.
	 */
	private String[] stdArgumente;

	/**
	 * Hash-Tabelle aller Applikationsdaten. Der Schlüssel ist der Name der
	 * Applikation. Der Wert ist ein Feld, dessen erster Eintrag die zu
	 * startende Klasse und die restlichen Aufrufparameter darstellen.
	 */
	private Map<String, String[]> eigenschaften;

	/**
	 * Hash-Tabelle aller laufenden Applikationen.
	 */
	private Map<String, StandardApplikation> applikationen;

	/**
	 * Liest die Properties-Datei ein und baut den Dialog.
	 *
	 * @param file
	 *            Pfad zur Properties-Datei
	 */
	public ApplikationsManager(final String file) {
		super();

		initialize();

		final Properties prop = new Properties();
		try (FileInputStream inStream = new FileInputStream(file)) {
			prop.load(inStream);
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		eigenschaften = new HashMap<>();
		applikationen = new HashMap<>();
		for (final Object obj : prop.keySet()) {
			final String name = (String) obj;
			if ("Argumente".equals(name)) {
				stdArgumente = prop.getProperty(name).split(" ");
			} else {
				eigenschaften.put(name, prop.getProperty(name).split(" "));
			}
		}

		// Buttons anlegen
		for (final Entry<String, String[]> tmpEntry : eigenschaften
				.entrySet()) {
			final Button button = new Button();
			final Entry<String, String[]> entry = tmpEntry;

			// Name des Button = Name der Applikation
			button.setLabel(entry.getKey() + START);
			button.setName(entry.getKey());
			try {
				Class.forName(entry.getValue()[0]);
			} catch (final ClassNotFoundException ex) {
				button.setLabel(entry.getKey() + ": Klasse nicht gefunden");
				button.setEnabled(false);
			}

			button.addActionListener(new java.awt.event.ActionListener() {

				@Override
				public void actionPerformed(
						final java.awt.event.ActionEvent event) {
					final Button src = (Button) event.getSource();
					src.setEnabled(false);

					if (src.getLabel().endsWith(START)) {
						// Applikation starten
						src.setLabel(src.getName() + STARTING);

						// Standardaufrupparameter holen
						final List<String> args = new ArrayList<>();
						args.addAll(Arrays.asList(stdArgumente));

						// Klasse und zusätzliche Argumente bestimmen
						final String[] app = eigenschaften.get(src.getName());
						for (int i = 1; i < app.length; i++) {
							args.add(app[i]);
						}

						// Applikationsklasse instanziieren
						StandardApplikation applikation = null;
						try {
							applikation = (StandardApplikation) Class
									.forName(app[0]).newInstance();
						} catch (final InstantiationException ex) {
							ex.printStackTrace();
							return;
						} catch (final IllegalAccessException ex) {
							ex.printStackTrace();
							return;
						} catch (final ClassNotFoundException ex) {
							ex.printStackTrace();
							return;
						}

						// Applikation in Liste "läuft" eintragen und starten
						applikationen.put(src.getName(), applikation);
						StandardApplicationRunner.run(applikation,
								args.toArray(new String[0]));

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
	 * Startmethode.
	 *
	 * @param args
	 *            wird nicht benötigt
	 */
	public static void main(final String[] args) {
		String file = "applikationen.properties";

		if (args.length == 1) {
			file = args[0];
		}

		new ApplikationsManager(file);
	}

	/**
	 * This method initializes this.
	 */
	private void initialize() {
		setLayout(new GridLayout(0, 1));
		this.setSize(300, 300);
		setTitle("Applikationsmanager");

		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(final java.awt.event.WindowEvent e) {
				for (final StandardApplikation app : applikationen.values()) {
					app.exit();
				}

				System.exit(0);
			}
		});
	}

} // @jve:decl-index=0:visual-constraint="10,10"
