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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Typen einer Straﬂe.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public enum StrassenTyp implements Zustand<Integer> {
	/**
	 * Allgemeiner nicht n‰her definierte Straﬂe.
	 */
	SONSTIGE("SonstigeStraﬂe", 0),
	/**
	 * eine Autobahn.
	 */
	AUTOBAHN("Autobahn", 1),
	/**
	 * eine Bundesstraﬂe.
	 */
	BUNDESSSTRASSE("Bundesstraﬂe", 2),
	/**
	 * eine Landstraﬂe.
	 */
	LANDSTRASSE("Landstraﬂe", 3),
	/**
	 * eine Kreisstraﬂe.
	 */
	KREISSTRASSE("Kreisstraﬂe", 4),
	/**
	 * eine Stadtstraﬂe.
	 */
	STADTSTRASSE("Stadtstraﬂe", 5),
	/**
	 * eine Hauptverkehrsstraﬂe.
	 */
	HAUPTVERKEHRSSTRASSE("Hauptverkehrsstraﬂe", 6),
	/**
	 * eine Sammelstraﬂe.
	 */
	SAMMELSTRASSE("Sammelstraﬂe", 7),
	/**
	 * eine Anliegerstraﬂe.
	 */
	ANLIEGERSTRASSE("Anliegerstraﬂe", 8);

	/**
	 * liefert den Straﬂenknotentyp, der dem ¸bergebenen Code entspricht.
	 * 
	 * @param gesuchterCode
	 *            der Code, f¸r den ein Straﬂenknoten
	 * @return den gefundenen Typ, wenn ein ung¸ltiger Code ¸bergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static StrassenTyp getTyp(int gesuchterCode) {
		for (StrassenTyp typ : values()) {
			if (typ.getCode() == gesuchterCode) {
				return typ;
			}
		}

		throw new IllegalArgumentException("Ung¸ltiger Typ mit Code: "
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
	 * Die Funktion erzeugt einen Straﬂentyp mit dem gegebenen Code und der
	 * zugehˆrigen Bezeichnung. Die Funktion wird nur innerhalb der Klasse
	 * verwendet um eine Mange an Straﬂentypen zur Verf¸gung zu stellen.
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
