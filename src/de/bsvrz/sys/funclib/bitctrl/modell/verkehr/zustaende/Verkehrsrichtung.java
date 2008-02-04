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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definitionen für die Verkehrsrichtung eines Asb-Stationierung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public enum Verkehrsrichtung implements Zustand {
	/**
	 * Richtungsbezug unbekannt.
	 */
	UNBEKANNT("unbekannt", 0),
	/**
	 * Richtungsbezug in Stationierungsrichtung.
	 */
	IN_STATIONIERUNGSRICHTUNG("inStationierungsRichtung", 1),
	/**
	 * Richtungsbezug gegen Stationierungsrichtung.
	 */
	GEGEN_STATIONIERUNGSRICHTUNG("gegenStationierungsRichtung", 2);

	/**
	 * liefert den Richtungsbezug für den übergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static Verkehrsrichtung getVerkehrsRichtung(int gesuchterCode) {
		Verkehrsrichtung result = Verkehrsrichtung.UNBEKANNT;
		for (Verkehrsrichtung richtung : values()) {
			if (richtung.getCode() == gesuchterCode) {
				result = richtung;
				break;
			}
		}
		return result;

	}

	/**
	 * der Code des Zustandes.
	 */
	private int code;

	/**
	 * der Name des Zustandes.
	 */
	private String name;

	/**
	 * Konstruktor.<br>
	 * Die Funktion einen eine neue Instanz für einen Richtungsbezug mit dem
	 * übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zustände zur Verfügung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private Verkehrsrichtung(String name, int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 * 
	 * @return den Code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * liefert den Namen des Zustandes.
	 * 
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

}
