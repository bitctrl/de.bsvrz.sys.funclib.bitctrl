/*
 * Hilfsfunktionen zur Bearbeitungen von Strings und Texten
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

package de.bsvrz.sys.funclib.bitctrl.text;

/**
 * Stellt Methoden zum pr&uuml;fen von Bedingungen an Textvariablen bereit
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class Check {

	/**
	 * Pr&uuml;ft ob im String Leerzeichen enthalten sind. Als Leerzeichen gilt
	 * neben dem eigentlichen Leerzeichen auch Zeilenumbr&uuml; und Tabulatoren.
	 * 
	 * @param s
	 *            Zu testender Text
	 * @return {@code true}, wenn Leerzeichen enthalten sind
	 * @throws NullPointerException
	 *             Wenn der &uuml;bergebene String {@code null} ist
	 * @see java.lang.Character#isWhitespace(char)
	 */
	public static boolean enthaeltLeerzeichen(String s) {
		if (s == null)
			throw new NullPointerException();

		for (char z : s.toCharArray())
			if (Character.isWhitespace(z))
				return true;

		return false;
	}

	/**
	 * Pr&uuml;ft ob im String ein Zeilenumbruch enthalten sind. Zeilenumbruch:
	 * \n und \r
	 * 
	 * @param s
	 *            Zu testender Text
	 * @return {@code true}, wenn ein Zeilenumbruch enthalten sind
	 * @throws NullPointerException
	 *             Wenn der &uuml;bergebene String {@code null} ist
	 */
	public static boolean enthaeltZeilenumbruch(String s) {
		if (s == null)
			throw new NullPointerException();

		for (Character z : s.toCharArray())
			if (z.equals('\n') || z.equals('\r'))
				return true;

		return false;
	}

	/**
	 * Testet ob der String {@code null} oder ein Leerstring ist. Strings am
	 * Anfang und Ende des Strings werden vor dem Test entfernt.
	 * 
	 * @param s
	 *            Ein String
	 * @return {@code true}, wenn der String druckbar bzw. anzeigbar ist
	 */
	public static boolean isDruckbar(String s) {
		if (s == null)
			return false;

		String t = s.trim();

		if (t.equals("")) //$NON-NLS-1$
			return false;

		return true;
	}

}
