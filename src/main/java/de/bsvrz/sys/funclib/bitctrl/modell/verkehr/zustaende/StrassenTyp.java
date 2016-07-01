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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Typen einer Straße.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public enum StrassenTyp implements Zustand<Integer> {
	/**
	 * Allgemeiner nicht näher definierte Straße.
	 */
	SONSTIGE("SonstigeStraße", 0), /**
	 * eine Autobahn.
	 */
	AUTOBAHN("Autobahn", 1), /**
	 * eine Bundesstraße.
	 */
	BUNDESSSTRASSE("Bundesstraße", 2), /**
	 * eine Landstraße.
	 */
	LANDSTRASSE("Landstraße", 3), /**
	 * eine Kreisstraße.
	 */
	KREISSTRASSE("Kreisstraße", 4), /**
	 * eine Stadtstraße.
	 */
	STADTSTRASSE("Stadtstraße", 5), /**
	 * eine Hauptverkehrsstraße.
	 */
	HAUPTVERKEHRSSTRASSE("Hauptverkehrsstraße", 6), /**
	 * eine Sammelstraße.
	 */
	SAMMELSTRASSE("Sammelstraße", 7), /**
	 * eine Anliegerstraße.
	 */
	ANLIEGERSTRASSE("Anliegerstraße", 8);

	/**
	 * liefert den Straßenknotentyp, der dem übergebenen Code entspricht.
	 *
	 * @param gesuchterCode
	 *            der Code, für den ein Straßenknoten
	 * @return den gefundenen Typ, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static StrassenTyp getTyp(final int gesuchterCode) {
		for (final StrassenTyp typ : values()) {
			if (typ.getCode() == gesuchterCode) {
				return typ;
			}
		}

		throw new IllegalArgumentException(
				"Ungültiger Typ mit Code: " + gesuchterCode);
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
	 * Die Funktion erzeugt einen Straßentyp mit dem gegebenen Code und der
	 * zugehörigen Bezeichnung. Die Funktion wird nur innerhalb der Klasse
	 * verwendet um eine Mange an Straßentypen zur Verfügung zu stellen.
	 *
	 * @param name
	 *            der Name des Typs
	 * @param code
	 *            der zugeordnete Code
	 */
	StrassenTyp(final String name, final int code) {
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
