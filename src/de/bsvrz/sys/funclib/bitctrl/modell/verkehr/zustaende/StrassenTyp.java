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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Typen einer Stra�e.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public enum StrassenTyp implements Zustand<Integer> {
	/**
	 * Allgemeiner nicht n�her definierte Stra�e.
	 */
	SONSTIGE("SonstigeStra�e", 0),
	/**
	 * eine Autobahn.
	 */
	AUTOBAHN("Autobahn", 1),
	/**
	 * eine Bundesstra�e.
	 */
	BUNDESSSTRASSE("Bundesstra�e", 2),
	/**
	 * eine Landstra�e.
	 */
	LANDSTRASSE("Landstra�e", 3),
	/**
	 * eine Kreisstra�e.
	 */
	KREISSTRASSE("Kreisstra�e", 4),
	/**
	 * eine Stadtstra�e.
	 */
	STADTSTRASSE("Stadtstra�e", 5),
	/**
	 * eine Hauptverkehrsstra�e.
	 */
	HAUPTVERKEHRSSTRASSE("Hauptverkehrsstra�e", 6),
	/**
	 * eine Sammelstra�e.
	 */
	SAMMELSTRASSE("Sammelstra�e", 7),
	/**
	 * eine Anliegerstra�e.
	 */
	ANLIEGERSTRASSE("Anliegerstra�e", 8);

	/**
	 * liefert den Stra�enknotentyp, der dem �bergebenen Code entspricht.
	 * 
	 * @param gesuchterCode
	 *            der Code, f�r den ein Stra�enknoten
	 * @return den gefundenen Typ, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static StrassenTyp getTyp(int gesuchterCode) {
		for (StrassenTyp typ : values()) {
			if (typ.getCode() == gesuchterCode) {
				return typ;
			}
		}

		throw new IllegalArgumentException("Ung�ltiger Typ mit Code: "
				+ gesuchterCode);
	}

	/**
	 * der Code des Zustandes.
	 */
	private int code;

	/**
	 * die Bezeichnung des Zustandes.
	 */
	private String name;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt einen Stra�entyp mit dem gegebenen Code und der
	 * zugeh�rigen Bezeichnung. Die Funktion wird nur innerhalb der Klasse
	 * verwendet um eine Mange an Stra�entypen zur Verf�gung zu stellen.
	 * 
	 * @param name
	 *            der Name des Typs
	 * @param code
	 *            der zugeordnete Code
	 */
	private StrassenTyp(String name, int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 * 
	 * @return den Code.
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Zustand#getName()
	 */
	public String getName() {
		return name;
	}

}
