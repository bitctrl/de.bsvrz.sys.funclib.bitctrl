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

import java.text.Collator;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.bsvrz.dav.daf.main.config.SystemObject;

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
	 * Sortiert eine Liste von Systemobjekten nach deren Namen. Beim Sortieren
	 * werden deutsche Umlaute ber&uuml;cksichtigt.
	 * <p>
	 * <em>Hinweis:</em> Das Ergebnis wird im Parameter abgelegt.
	 * 
	 * @param objekte
	 *            die zu sortierende Liste.
	 */
	public static void sortiere(List<? extends SystemObject> objekte) {
		Collections.sort(objekte, new Comparator<SystemObject>() {

			public int compare(SystemObject so1, SystemObject so2) {
				Collator de = Collator.getInstance(Locale.GERMANY);
				return de.compare(so1.toString(), so2.toString());
			}

		});
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
	 * werden nur echte Simulationen, d.h. Simulationsvarianten &gt; 0
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
