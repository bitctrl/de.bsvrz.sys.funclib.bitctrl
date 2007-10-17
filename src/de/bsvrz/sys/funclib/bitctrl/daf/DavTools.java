/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Wei&szlig;enfelser Stra&szlig;e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.text.DateFormat;
import java.util.Date;

/**
 * Allgemeine Funktionen im Zusammenhang mit
 * Datenverteiler-Applikationsfunktionen.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class DavTools {

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
	 * liefert einen Integerwert, der als Boolean-Ersatz für JaNein-Werte
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
	 * überprüft die Gültigkeit der übergebenen Simulationsvariante.<br>
	 * Liegt die Simulationsvariante nicht im Bereich 0 ... 999, wird eine
	 * RuntimeException augelöst.
	 * 
	 * @param sim
	 *            die Simulationsvariante
	 */
	public static void validiereSimulationsVariante(short sim) {
		try {
			validiereSimulationsVariante(sim, false);
		} catch (NoSimulationException e) {
			// Alle gültigen Simulationsvarianten werden akzeptiert
		}
	}

	/**
	 * überprüft die Gültigkeit der übergebenen Simulationsvariante.<br>
	 * Liegt die Simulationsvariante nicht im Bereich 0 ... 999, wird eine
	 * RuntimeException augelöst.<br>
	 * Wird der Parameter <i>simulation</i> auf den Wert <i>true</i> gesetzt,
	 * werdedn nur echte Simulationen, d.h. Simulationsvarianten &gt; 0
	 * zugelassen.
	 * 
	 * @param sim
	 *            die Simulationsvariante
	 * @param simulation
	 *            nur echte Simulation ?
	 * @throws NoSimulationException
	 */
	public static void validiereSimulationsVariante(short sim,
			boolean simulation) throws NoSimulationException {

		if ((sim < 0) || (sim > 999)) {
			throw new RuntimeException(
					"Die Verwendung der ungültigen Simulationsvariante: \""
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
	protected DavTools() {
		// nix
	}

}
