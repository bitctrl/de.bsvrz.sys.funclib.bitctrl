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

package de.bsvrz.sys.funclib.bitctrl.modell.fachmodellglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Werte die ein G&uuml;teverfahren benennen.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public enum GueteVerfahren implements Zustand<Integer> {

	/** Standardverfahren. */
	Standard("Standard", 0);

	/**
	 * Sucht das G&uuml;teverfahren anhand eines Code.
	 *
	 * @param code
	 *            der Code.
	 * @return das G&uuml;teverfahren zum Code.
	 */
	public static GueteVerfahren getGueteVerfahren(final int code) {
		for (final GueteVerfahren verfahren : values()) {
			if (verfahren.getCode() == code) {
				return verfahren;
			}
		}

		throw new IllegalArgumentException(
				"Zum Code " + code + " existiert kein Güteverfahren.");
	}

	/** Der Code des Zustandes. */
	private int code;

	/** Der Name des Zustandes. */
	private String name;

	/**
	 * Initialisierung.
	 *
	 * @param name
	 *            der Name des Zustands
	 * @param code
	 *            der verwendete Code
	 */
	GueteVerfahren(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

}
