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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.text.Collator;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Allgemeine Funktionen im Zusammenhang mit
 * Datenverteiler-Applikationsfunktionen.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class DavTools {

	/**
	 * Konvertiert einen Zeitstempel in eine lesbare absolute Zeit.
	 * 
	 * @param zeitstempel
	 *            ein Zeitstempel.
	 * @return die entsprechende Zeit als lesbaren String.
	 */
	public static String absoluteZeit(long zeitstempel) {
		return DateFormat.getDateTimeInstance().format(new Date(zeitstempel));
	}

	/**
	 * Generiert aus einem Objektnamen eine g�ltige PID. Es wird jedes Zeichen
	 * vor einem Leerzeichen in einen Gro�buchstaben verwandelt, danach alle
	 * Leerzeichen entfernt und der erste Buchstabe des Namens in einen
	 * Kleinbuchstaben umgewandelt.
	 * 
	 * @param name
	 *            der Objektname.
	 * @param praefix
	 *            der Pr�fix f�r die PID (mit Punkt abgeschlossen).
	 * @return die g�ltige PID zum Objektnamen.
	 */
	public static String generierePID(String name, String praefix) {
		String pid, regex;
		Matcher matcher;

		regex = "(\\S)+(\\s)";
		matcher = Pattern.compile(regex).matcher(name);
		if (matcher.find()) {
			pid = matcher.group(0).trim();

			regex = "(\\s)+(\\S)+";
			matcher = Pattern.compile(regex).matcher(name);
			while (matcher.find()) {
				String s = matcher.group(0).trim();
				pid += s.substring(0, 1).toUpperCase();
				pid += s.substring(1);
			}
		} else {
			pid = name;
		}
		pid = pid.substring(0, 1).toLowerCase() + pid.substring(1);

		return praefix + pid;
	}

	/**
	 * liefert einen Integerwert, der als Boolean-Ersatz f�r JaNein-Werte
	 * innerhalb einer Datenverteiler-Attributgruppe verschicht werden kann.
	 * 
	 * @param wert
	 *            der Boolwert
	 * @return der Integerwert
	 */
	public static int intWertVonBoolean(boolean wert) {
		int ergebnis = 0;
		if (wert) {
			ergebnis = 1;
		}

		return ergebnis;
	}

	/**
	 * Sortiert eine Liste von Systemobjekten nach deren Namen. Beim Sortieren
	 * werden deutsche Umlaute ber&uuml;cksichtigt.
	 * <p>
	 * <em>Hinweis:</em> Das Ergebnis wird auch im Parameter abgelegt.
	 * 
	 * @param objekte
	 *            die zu sortierende Liste.
	 * @return die sortierte Liste.
	 */
	public static List<? extends SystemObject> sortiere(
			List<? extends SystemObject> objekte) {
		Collections.sort(objekte, new Comparator<SystemObject>() {

			public int compare(SystemObject so1, SystemObject so2) {
				Collator de = Collator.getInstance(Locale.GERMANY);
				return de.compare(so1.toString(), so2.toString());
			}

		});
		return objekte;
	}

	/**
	 * �berpr�ft die G�ltigkeit der �bergebenen Simulationsvariante.<br>
	 * Liegt die Simulationsvariante nicht im Bereich 0 ... 999, wird eine
	 * RuntimeException augel�st.
	 * 
	 * @param sim
	 *            die Simulationsvariante
	 */
	public static void validiereSimulationsVariante(short sim) {
		try {
			validiereSimulationsVariante(sim, false);
		} catch (NoSimulationException e) {
			// Alle g�ltigen Simulationsvarianten werden akzeptiert
		}
	}

	/**
	 * �berpr�ft die G�ltigkeit der �bergebenen Simulationsvariante.<br>
	 * Liegt die Simulationsvariante nicht im Bereich 0 ... 999, wird eine
	 * RuntimeException augel�st.<br>
	 * Wird der Parameter <i>simulation</i> auf den Wert <i>true</i> gesetzt,
	 * werden nur echte Simulationen, d.h. Simulationsvarianten &gt; 0
	 * zugelassen.
	 * 
	 * @param sim
	 *            die Simulationsvariante
	 * @param simulation
	 *            nur echte Simulation ?
	 * @throws NoSimulationException
	 *             wenn die Simulationsvariante 0 ist.
	 */
	public static void validiereSimulationsVariante(short sim,
			boolean simulation) throws NoSimulationException {

		if ((sim < 0) || (sim > 999)) {
			throw new RuntimeException(
					"Die Verwendung der ung�ltigen Simulationsvariante: \""
							+ sim + "\" ist nicht erlaubt");
		}

		if (simulation) {
			if (sim == 0) {
				throw new NoSimulationException();
			}
		}

	}

	/**
	 * Defaultkonstruktor verstecken.
	 */
	private DavTools() {
		// nix
	}

}
