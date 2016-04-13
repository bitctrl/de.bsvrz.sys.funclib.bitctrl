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

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.bitctrl.util.logging.LoggerTools;

import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Hilfsklasse zum Loggen mittels des Datenverteiler-Loggers.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public final class LogTools {

	/**
	 * definiert, ob die ID für eine Meldung gegebenenfalls explizit berechnet
	 * werden soll.
	 */
	private static boolean calculateId = true;

	/** das Tool zum Bestimmen der ID für eine Betriebsmeldung. */
	private static BetriebsmeldungIdKonverter konverter = new DefaultBetriebsMeldungsIdKonverter();

	/**
	 * bestimmt, ob die ID einer Meldung berechnet werden soll.
	 *
	 * @param calculateId
	 *            der Status
	 */
	public static void setCalculateId(final boolean calculateId) {
		LogTools.calculateId = calculateId;
	}

	/**
	 * setzt das Modul zum Bestimmen der ID einer Meldung.
	 *
	 * @param konverter
	 *            das Modul
	 */
	public static void setKonverter(
			final BetriebsmeldungIdKonverter konverter) {
		LogTools.konverter = konverter;
		if (LogTools.konverter == null) {
			LogTools.konverter = new DefaultBetriebsMeldungsIdKonverter();
		}
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
	 * @deprecated neu: {@link #isLogbar(Debug, Level)}
	 */
	@Deprecated
	public static boolean isLogbar(final Class<?> klasse, final Level level) {
		final Logger logger;
		final LogRecord logRecord;
		boolean result;

		logger = Logger.getLogger(klasse.getSimpleName() + "."
				+ klasse.getPackage().getName() + "." + klasse.getSimpleName());
		logRecord = new LogRecord(level, "");
		result = false;

		for (final Handler h : Logger.getLogger(klasse.getSimpleName())
				.getHandlers()) {
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
	 * @deprecated neu: {@link #isLogbar(Debug, Level, Class)}
	 */
	@Deprecated
	public static boolean isLogbarAufConsole(final Class<?> klasse,
			final Level level) {
		final Logger logger;
		final LogRecord logRecord;
		boolean result;

		logger = Logger.getLogger(klasse.getSimpleName() + "."
				+ klasse.getPackage().getName() + "." + klasse.getSimpleName());
		logRecord = new LogRecord(level, "");
		result = false;

		for (final Handler h : Logger.getLogger(klasse.getSimpleName())
				.getHandlers()) {
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
	 * @deprecated neu: {@link #isLogbar(Debug, Level, Class)}
	 */
	@Deprecated
	public static boolean isLogbarInFile(final Class<?> klasse,
			final Level level) {
		final Logger logger;
		final LogRecord logRecord;
		boolean result;

		logger = Logger.getLogger(klasse.getSimpleName() + "."
				+ klasse.getPackage().getName() + "." + klasse.getSimpleName());
		logRecord = new LogRecord(level, "");
		result = false;

		for (final Handler h : Logger.getLogger(klasse.getSimpleName())
				.getHandlers()) {
			if (h instanceof FileHandler) {
				result = logger.isLoggable(level) && h.isLoggable(logRecord);
				break;
			}
		}

		return result;
	}

	/**
	 * Testet ob auf einem Logger mit einem bestimmten Level geloggt wird.
	 *
	 * @param debug
	 *            ein Logger.
	 * @param level
	 *            der zu prüfende Level.
	 * @return <code>true</code>, wenn der Logger auf dem angegebenen Level
	 *         Ausgaben macht.
	 */
	public static boolean isLogbar(final Debug debug, final Level level) {
		return isLogbar(debug, level, null);
	}

	/**
	 * Testet ob auf einem Logger mit einem bestimmten Level geloggt wird.
	 * Zusätzlich kann auf einen bestimmten Handler z.&nbsp;B.
	 * <code>ConsoleHandler</code> oder <code>FileHandler</code> geprüft werden.
	 *
	 * @param debug
	 *            ein Logger.
	 * @param level
	 *            der zu prüfende Level.
	 * @param handlerClazz
	 *            die Klasse eines Log-Handlers.
	 * @return <code>true</code>, wenn der Logger auf dem angegebenen Handler
	 *         und Level Ausgaben macht. Wenn der Handler <code>null</code> ist,
	 *         wird <code>true</code> zurückgegeben, wenn der Logger auf dem
	 *         angegebenen Level Ausgaben macht, der Handler wird dann
	 *         ignoriert.
	 */
	public static boolean isLogbar(final Debug debug, final Level level,
			final Class<? extends Handler> handlerClazz) {
		try {
			final Field field = Debug.class.getDeclaredField("_logger");
			field.setAccessible(true);
			final Logger logger = (Logger) field.get(debug);

			return LoggerTools.isLogable(logger, level, handlerClazz);
		} catch (final SecurityException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		} catch (final NoSuchFieldException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		} catch (final IllegalArgumentException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		} catch (final IllegalAccessException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			throw new IllegalStateException(ex);
		}
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
	public static void log(final Debug log, final LogNachricht nachricht,
			final Object... arguments) {
		if (isLogbar(log, nachricht.getLogLevel())) {
			final String txt;
			if (arguments != null) {
				txt = MessageFormat.format(nachricht.toString(), arguments);
			} else {
				txt = nachricht.toString();
			}

			// Ausgabe Logger
			if (log != null) {
				final Level logLevel = nachricht.getLogLevel();
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
		}

		sendeBetriebsmeldung(null, nachricht, arguments);
	}

	/**
	 * Gibt die Meldung auf dem Logger aus. Wenn es die Nachricht verlangt, wird
	 * ebenfalls eine Betriebsmeldung versandt. Wenn der Logger {@code null}
	 * ist, dann wird nur eine Betriebsmeldung versandt.
	 *
	 * @param log
	 *            der Logger.
	 * @param daten
	 *            die Daten für den Versand als Betriebsmeldung
	 * @param nachricht
	 *            die Nachricht.
	 * @param arguments
	 *            optional eine beliebige Anzahl Argumente, falls Platzhalter in
	 *            der Nachricht vorkommen.
	 */
	public static void log(final Debug log, final BetriebsmeldungDaten daten,
			final LogNachricht nachricht, final Object... arguments) {
		if (isLogbar(log, nachricht.getLogLevel())) {
			final String txt;
			if (arguments != null) {
				txt = MessageFormat.format(nachricht.toString(), arguments);
			} else {
				txt = nachricht.toString();
			}

			// Ausgabe Logger
			if (log != null) {
				final Level logLevel = nachricht.getLogLevel();
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
		}

		sendeBetriebsmeldung(daten, nachricht, arguments);
	}

	/**
	 * Gibt eine Nachricht als Betriebsmeldung aus. Der Level wird aus der
	 * Nachricht gelesen.
	 *
	 * @param daten
	 *            die Daten für die Betriebsmeldung
	 * @param nachricht
	 *            die Nachricht.
	 * @param arguments
	 *            optional eine beliebige Anzahl Argumente, falls Platzhalter in
	 *            der Nachricht vorkommen.
	 */
	public static void sendeBetriebsmeldung(final BetriebsmeldungDaten daten,
			final LogNachricht nachricht, final Object... arguments) {
		final MessageGrade bmvLevel = nachricht.getBmvLevel();
		if (bmvLevel != null) {
			String txt;
			if (arguments != null) {
				txt = MessageFormat.format(nachricht.toString(), arguments);
			} else {
				txt = nachricht.toString();
			}

			// Ausgabe Betriebsmeldung
			// TODO Textlänge muss gekürzt werden, da writeUTF die Länge
			// begrenzt. (UTFDataFormatException)
			if (txt.length() > 20000) {
				txt = txt.substring(0, 20000);
			}

			String sendId = null;
			if (calculateId) {
				sendId = konverter.konvertiere(daten, nachricht, arguments);
			} else if (daten != null) {
				sendId = daten.getId();
			}

			if (daten == null) {
				MessageSender.getInstance().sendMessage(sendId,
						MessageType.APPLICATION_DOMAIN, null, bmvLevel,
						MessageState.MESSAGE, txt);

			} else {
				MessageSender.getInstance().sendMessage(sendId, daten.getType(),
						null, bmvLevel, daten.getReference(), daten.getState(),
						daten.getCauser(), txt);
			}
		}
	}

	/**
	 * Konstruktor verstecken.
	 */
	private LogTools() {
		// nix
	}
}
