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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.app;

import com.bitctrl.util.logging.LoggerTools;

import de.bsvrz.dav.daf.main.ClientDavConnection;
import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientDavParameters;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;

/**
 * Eine Alternative f¸r den
 * {@link de.bsvrz.sys.funclib.application.StandardApplicationRunner} mit mehr
 * Optionen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class BcStandardApplicationRunner {

	/**
	 * Die Methode baut mit Hilfe der Kommandozeilenargumente eine Verbindung
	 * zum Datenverteiler auf und initialisiert anschlieﬂend die Applikation.
	 * <p>
	 * Bei der Initialisierung werden nacheinander die beiden Methoden
	 * {@link StandardApplication#parseArguments(ArgumentList)} und
	 * {@link StandardApplication#initialize(ClientDavInterface)} aufgerufen.
	 * 
	 * @param application
	 *            die zu initialisierende Applikation.
	 * @param args
	 *            die Kommandozeilenargumente der Applikation.
	 * @param appTypePID
	 *            die PID die der Applikation als Typ anstelle von
	 *            <em>typ.applikation</em> gesetzt werden soll. Wenn
	 *            {@code null}, wird der Applikationstyp nicht ge‰ndert.
	 * @param autoReadyMsg
	 *            dieses Flag sagt aus, ob nach der Initialisierung der
	 *            Anwendung automatisch das Readysignal gesendet werden soll.
	 *            Ist das Flag auf {@code false} gesetzt, muss die Applikation
	 *            selber das Ready-Signal mit
	 *            {@link ClientDavInterface#sendApplicationReadyMessage()}
	 *            senden.
	 * @param uncaughtExceptionHandler
	 *            dieses Flag sagt aus, ob ein UncaughtExceptionHandler
	 *            installiert werden soll. Ist das Flag {@code true}, wird ein
	 *            Handler inistalliert, der alle Runtime Exceptions f‰ngt, loggt
	 *            und anschlieﬂend die Applikation beendet.
	 */
	public static void run(final StandardApplication application,
			final String[] args, final String appTypePID,
			final boolean autoReadyMsg, final boolean uncaughtExceptionHandler) {
		final ArgumentList argumentList = new ArgumentList(args);

		final StringBuilder applicationLabel = new StringBuilder();
		for (final String arg : args) {
			applicationLabel.append(arg);
		}

		// Debuglogger initialisieren
		final String[] classNameParts = application.getClass().getName().split(
				"[.]");
		final int lastPartIndex = classNameParts.length - 1;
		final String applicationName;
		if (lastPartIndex < 0) {
			applicationName = "StandardApplication";
		} else {
			applicationName = classNameParts[lastPartIndex];
		}
		applicationLabel.append(applicationName);
		Debug.init(applicationName, argumentList);
		final Debug debug = Debug.getLogger();

		// UncaughtExceptionHandler installieren, falls gew¸nscht.
		if (uncaughtExceptionHandler) {
			Thread
					.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

						public void uncaughtException(final Thread t,
								final Throwable ex) {
							Debug.getLogger().error(
									"Applikation wird unerwartet beendet",
									LoggerTools.getStackTrace(ex));
							System.exit(-1);
						}

					});
		}

		try {
			// Kommandozeilenargumente verarbeiten
			final ClientDavParameters parameters = new ClientDavParameters(
					argumentList);
			parameters.setApplicationName(applicationName);
			application.parseArguments(argumentList);
			argumentList.ensureAllArgumentsUsed();

			// Verbindung zum Datenverteiler herstellen
			final ClientDavInterface connection = new ClientDavConnection(
					parameters);
			if (appTypePID != null) {
				connection.getClientDavParameters().setApplicationTypePid(
						appTypePID);
			}
			connection.enableExplicitApplicationReadyMessage();
			connection.connect();
			connection.login();

			// Betriebsmeldungsverwaltung initialieren
			applicationLabel.append(connection.getLocalConfigurationAuthority()
					.getPid());
			MessageSender.getInstance().init(connection, applicationName,
					applicationLabel.toString());

			// Applikation initialisieren
			application.initialize(connection);

			// Fertigmeldung senden
			if (autoReadyMsg) {
				connection.sendApplicationReadyMessage();
			}
		} catch (final Exception ex) {
			debug.error("Die Applikation konnte nicht initialisiert werden",
					LoggerTools.getStackTrace(ex));
			System.exit(1);
		}
	}

	/**
	 * Identischt mit {@code run(application, args, null, true, true)}.
	 * 
	 * @param application
	 *            die zu initialisierende Applikation.
	 * @param args
	 *            die Kommandozeilenargumente der Applikation.
	 * @see #run(StandardApplication, String[], String, boolean, boolean)
	 */
	public static void run(final StandardApplication application,
			final String[] args) {
		run(application, args, null, true, true);
	}

	/**
	 * Konstruktor verstecken. Der Standardkonstruktor tut nichts.
	 */
	protected BcStandardApplicationRunner() {
		// nix
	}

}
