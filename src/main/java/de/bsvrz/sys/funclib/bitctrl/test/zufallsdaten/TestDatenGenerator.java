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

package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import de.bsvrz.sys.funclib.bitctrl.modell.Datum;

/**
 * Schnittstelle f&uuml;r Zufallsdatengenerator.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @param <T>
 *            der Typ der Testdaten
 */
public interface TestDatenGenerator<T extends Datum> {

	/**
	 * Generiert ein zuf&auml;lliges Datum. Das Datum sollte in sich logisch
	 * korrekt sein. Zum Beispiel sollten Beziehungen zwischen den Werten im
	 * Datum erf&uuml;llt sein.
	 *
	 * @return das generierte Datum.
	 */
	T generiere();

	/**
	 * Gibt den aktuellen maximal generierten Wert eines Teils des Datums
	 * zur&uuml;ck.
	 *
	 * @param name
	 *            der Name des Datumteils.
	 * @return der Maximalwert.
	 */
	Number getMaxWert(String name);

	/**
	 * Gibt den aktuellen minimal generierten Wert eines Teils des Datums
	 * zur&uuml;ck.
	 *
	 * @param name
	 *            der Name des Datumteils.
	 * @return der Minimalwert.
	 */
	Number getMinWert(String name);

	/**
	 * Legt den maximal generierten Wert eines Teils des Datums fest.
	 *
	 * @param name
	 *            der Name des Datumteils.
	 * @param max
	 *            der neue Maximalwert.
	 */
	void setMaxWert(String name, Number max);

	/**
	 * Legt den minximal generierten Wert eines Teils des Datums fest.
	 *
	 * @param name
	 *            der Name des Datumteils.
	 * @param min
	 *            der neue Minimalwert.
	 */
	void setMinWert(String name, Number min);

}
