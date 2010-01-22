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

package de.bsvrz.sys.funclib.bitctrl.math;

/**
 * Hilfsklasse für den vergleich von Zahlen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: Vergleich.java 6726 2008-02-19 12:27:55Z Schumann $
 * @deprecated verschoben nach {@link com.bitctrl.math.MathTools}.
 */
@Deprecated
public final class Vergleich {

	/**
	 * Testet ob die zwei Werte ungef&auml;hr gleich sind. Die maximal erlaubte
	 * Abweichung wird als dritter Parameter angegeben.
	 * 
	 * @param a
	 *            Ein Wert
	 * @param b
	 *            Ein zweiter Wert
	 * @param abweichung
	 *            Maximal erlaubte Abweichung
	 * @return {@code true}, wenn die beiden Werte ungef&auml;hr gleich sind
	 */
	public static boolean ungefaehr(double a, double b, double abweichung) {
		if (Math.abs(a - b) < abweichung) {
			return true;
		}

		return false;
	}

	/**
	 * Konstruktor verstecken.
	 */
	private Vergleich() {
		// nix
	}

}
