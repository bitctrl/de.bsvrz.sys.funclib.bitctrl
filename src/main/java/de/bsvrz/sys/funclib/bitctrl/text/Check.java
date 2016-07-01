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

package de.bsvrz.sys.funclib.bitctrl.text;

/**
 * Stellt Methoden zum pr&uuml;fen von Bedingungen an Textvariablen bereit.
 *
 * @author BitCtrl Systems GmbH, Schumann
 * @deprecated Nach {@link com.bitctrl.text.CheckText} ausgelagert.
 */
@Deprecated
public final class Check {

	private Check() {
		// es gibt keine Instanzen der Klasse
	}

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
	public static boolean enthaeltLeerzeichen(final String s) {
		if (s == null) {
			throw new NullPointerException();
		}

		for (final char z : s.toCharArray()) {
			if (Character.isWhitespace(z)) {
				return true;
			}
		}

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
	public static boolean enthaeltZeilenumbruch(final String s) {
		if (s == null) {
			throw new NullPointerException();
		}

		for (final char z : s.toCharArray()) {
			if ((z == '\n') || (z == '\r')) {
				return true;
			}
		}

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
	public static boolean isDruckbar(final String s) {
		if (s == null) {
			return false;
		}

		final String t = s.trim();

		if (t.isEmpty()) {
			return false;
		}

		return true;
	}

}
