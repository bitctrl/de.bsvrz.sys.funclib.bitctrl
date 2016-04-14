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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition des Attributs LocationMethode innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsLocationMethode implements Zustand<Integer> {

	/**
	 * erste Feldposition ist primäre Location und letzte Feldposition ist
	 * sekundäre Location, Wert 0.
	 */
	METHODE0(
			"erste Feldposition ist primäre Location und letzte Feldposition ist sekundäre Location",
			0),

	/** Primäre Location und Extent sind gültig, Wert 1. */
	METHODE1("Primäre Location und Extent sind gültig", 1),

	/** Primäre Location und Sekundäre Location sind gültig, Wert 2. */
	METHODE2("Primäre Location und Sekundäre Location sind gültig", 2),

	/** Primäre Location, Extent und primäre Entfernung sind gültig, Wert 3. */
	METHODE3("Primäre Location, Extent und primäre Entfernung sind gültig", 3),

	/**
	 * Primäre Location, sekundäre Location, primäre Entfernung und sekundäre
	 * Entfernung sind gültig, Wert 4.
	 */
	METHODE4(
			"Primäre Location, sekundäre Location, primäre Entfernung und sekundäre Entfernung sind gültig.",
			4),

	/**
	 * Strassennummer, Ausfahrtnummer und Nachrichtentext für Nachrichtenklasse
	 * = 40 der primären und sekundären Location sind gültig, Wert 5.
	 */
	METHODE5(
			"Strassennummer, Ausfahrtnummer und Nachrichtentext für Nachrichtenklasse = 40 der primären und sekundären Location sind gültig.",
			5),

	/**
	 * Strassennummer, LocationKilometrierung der primären und sekundären
	 * Location sind gültig, Wert 6.
	 */
	METHODE6(
			"Strassennummer, LocationKilometrierung der primären und sekundären Location sind gültig.",
			6);

	/**
	 * liefert die Rds-Locationmethode mit dem übergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status PUNKT geliefert.
	 */
	public static RdsLocationMethode getStatus(final int gesuchterCode) {
		RdsLocationMethode result = RdsLocationMethode.METHODE0;
		for (final RdsLocationMethode status : values()) {
			if (status.getCode() == gesuchterCode) {
				result = status;
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
	 * Die Funktion einen eine neue Instanz für eine RDS-Locationmethode mit dem
	 * übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zustände zur Verfügung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	RdsLocationMethode(final String name, final int code) {
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

	/**
	 * liefert den Namen des Zustandes.
	 *
	 * @return der Name
	 */
	@Override
	public String getName() {
		return name;
	}

}
