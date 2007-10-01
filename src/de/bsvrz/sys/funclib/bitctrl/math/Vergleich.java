/*
 * Hilfsfunktionen zur Bearbeitung von Zahlen und Mathematik
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
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
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.math;

/**
 * Hilfsklasse f&auml;r den vergleich von Zahlen
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class Vergleich {

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
		if (Math.abs(a - b) < abweichung)
			return true;

		return false;
	}

}
