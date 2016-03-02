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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.tmc.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der konfigurierbaren Zust�nde f�r die TMC-Richtung.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public enum TmcRichtung implements Zustand<Integer> {
	/**
	 * die Richtung ist nicht definiert.
	 */
	UNDEFINIERT("Undefiniert", Integer.MAX_VALUE), /**
	 * die Richtung ist Positiv.
	 */
	POSITIV("positiv", 1), /**
	 * es ist keine Richtung festgelegt.
	 */
	OHNE("ohne Richtung", 0), /**
	 * die Richtung ist Positiv.
	 */
	NEGATIV("negativ", -1);

	/**
	 * liefert den Typ, der dem �bergebenen Code entspricht.
	 *
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird
	 * @return den gefundenen Zustand, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static TmcRichtung getTyp(final int gesuchterCode) {
		for (final TmcRichtung typ : values()) {
			if (typ.getCode() == gesuchterCode) {
				return typ;
			}
		}

		throw new IllegalArgumentException(
				"Ung�ltiger Typ mit Code: " + gesuchterCode);
	}

	/**
	 * der Code des Zustandes.
	 */
	private int code;

	/**
	 * die zugeordnete Bezeichnung.
	 */
	private String name;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz der TmcRichtung mit dem �bergebenen
	 * Code und der zugeordneten Bezeichnung. Die Funktion wird nur innerhalb
	 * der Klasse aufgerufen und erzeugt die �ffentlich zur Verf�gung gestellten
	 * Zust�nde.
	 *
	 * @param name
	 *            der Name
	 * @param code
	 *            der zugeordnete Code
	 */
	private TmcRichtung(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 *
	 * @return den Code.
	 */
	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

}
