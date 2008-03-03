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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.text.MessageFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Hilfsklasse zum Loggen mittels des Datenverteiler-Loggers.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class LogTools {

	/**
	 * Prüft, ob für einen Logger auf einem bestimmten Level eine Ausgabe
	 * mittels dem {@link ConsoleHandler} erfolgt.
	 * 
	 * @param klasse
	 *            die Klasse, die den Logger angelegt hat.
	 * @param level
	 *            der Loglevel.
	 * @return {@code true}, wenn eine Ausgabe auf der Konsole erfolgt.
	 */
	public static boolean isLogbar(Class<?> klasse, Level level) {
		Logger logger;
		LogRecord logRecord;
		boolean result;

		logger = Logger.getLogger(klasse.getSimpleName() + "."
				+ klasse.getPackage().getName() + "." + klasse.getSimpleName());
		logRecord = new LogRecord(level, "");
		result = false;

		for (Handler h : Logger.getLogger(klasse.getSimpleName()).getHandlers()) {
			result = logger.isLoggable(level) && h.isLoggable(logRecord);
			break;
		}

		return result;
	}

	/**
	 * Prüft, ob für einen Logger auf einem bestimmten Level eine Ausgabe
	 * mittels dem {@link ConsoleHandler} erfolgt.
	 * 
	 * @param klasse
	 *            die Klasse, die den Logger angelegt hat.
	 * @param level
	 *            der Loglevel.
	 * @return {@code true}, wenn eine Ausgabe auf der Konsole erfolgt.
	 */
	public static boolean isLogbarAufConsole(Class<?> klasse, Level level) {
		Logger logger;
		LogRecord logRecord;
		boolean result;

		logger = Logger.getLogger(klasse.getSimpleName() + "."
				+ klasse.getPackage().getName() + "." + klasse.getSimpleName());
		logRecord = new LogRecord(level, "");
		result = false;

		for (Handler h : Logger.getLogger(klasse.getSimpleName()).getHandlers()) {
			if (h instanceof ConsoleHandler) {
				result = logger.isLoggable(level) && h.isLoggable(logRecord);
				break;
			}
		}

		return result;
	}

	/**
	 * Prüft, ob für einen Logger auf einem bestimmten Level eine Ausgabe
	 * mittels dem {@link ConsoleHandler} erfolgt.
	 * 
	 * @param klasse
	 *            die Klasse, die den Logger angelegt hat.
	 * @param level
	 *            der Loglevel.
	 * @return {@code true}, wenn eine Ausgabe auf der Konsole erfolgt.
	 */
	public static boolean isLogbarInFile(Class<?> klasse, Level level) {
		Logger logger;
		LogRecord logRecord;
		boolean result;

		logger = Logger.getLogger(klasse.getSimpleName() + "."
				+ klasse.getPackage().getName() + "." + klasse.getSimpleName());
		logRecord = new LogRecord(level, "");
		result = false;

		for (Handler h : Logger.getLogger(klasse.getSimpleName()).getHandlers()) {
			if (h instanceof FileHandler) {
				result = logger.isLoggable(level) && h.isLoggable(logRecord);
				break;
			}
		}

		return result;
	}

	/**
	 * Gibt die Meldung auf dem Logger aus. Wenn es die Nachricht verlangt, wird
	 * ebenfalls eine Betriebsmeldung versandt. Wenn der Logger {@code null}
	 * ist, dann wird nur eine Betriebsmeldung versandt.
	 * 
	 * @param log
	 *            der Logger.
	 * @param nachricht
	 *            die Nachricht.
	 * @param arguments
	 *            optional eine beliebige Anzahl Argumente, falls Platzhalter in
	 *            der Nachricht vorkommen.
	 */
	public static void log(Debug log, LogNachricht nachricht,
			Object... arguments) {
		final MessageSender msg = MessageSender.getInstance();
		final String txt;
		final Level logLevel;
		final MessageGrade bmvLevel;

		if (arguments != null) {
			txt = MessageFormat.format(nachricht.toString(), arguments);
		} else {
			txt = nachricht.toString();
		}

		// Ausgabe Logger
		if (log != null) {
			logLevel = nachricht.getLogLevel();
			if (logLevel.equals(Debug.ERROR)) {
				log.error(txt);
			} else if (logLevel.equals(Debug.WARNING)) {
				log.warning(txt);
			} else if (logLevel.equals(Debug.INFO)) {
				log.info(txt);
			} else if (logLevel.equals(Debug.CONFIG)) {
				log.config(txt);
			} else if (logLevel.equals(Debug.FINE)) {
				log.fine(txt);
			} else if (logLevel.equals(Debug.FINER)) {
				log.finer(txt);
			} else if (logLevel.equals(Debug.FINEST)) {
				log.finest(txt);
			}
		}

		// Ausgabe Betriebsmeldung
		bmvLevel = nachricht.getBmvLevel();
		if (bmvLevel != null) {
			msg.sendMessage(MessageType.APPLICATION_DOMAIN, bmvLevel, txt);
		}
	}

	/**
	 * Konstruktor verstecken.
	 */
	private LogTools() {
		// nix
	}

}
