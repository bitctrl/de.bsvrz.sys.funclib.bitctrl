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
 * Repräsentation des Attributs RdsLocationRichtungTextID innerhalb einer
 * RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsLocationRichtungTextID implements Zustand<Integer> {

	/** unbekannt, Wert 0. */
	UNBEKANNT("unbekannt", 0),

	/** stadteinwärts, Wert 1. */
	STADTEINWAERTS("stadteinwärts", 1),

	/** stadtauswärts, Wert 2. */
	STADTAUSWAERTS("stadtauswärts", 2),

	/** Nord, Wert 3. */
	NORD("Nord", 3),

	/** Süd, Wert 4. */
	SUED("Süd", 4),

	/** Ost, Wert 5. */
	OST("Ost", 5),

	/** West, Wert 6. */
	WEST("West", 6),

	/** Nordost, Wert 7. */
	NORDOST("Nordost", 7),

	/** Nordwest, Wert 8. */
	NORDWEST("Nordwest", 8),

	/** Südost, Wert 9. */
	SUEDOST("Südost", 9),

	/** Südwest, Wert 10. */
	SUEDWEST("Südwest", 10);

	/**
	 * liefert den Rds-Erinnerungstyp mit dem übergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status UNBEKANNT geliefert.
	 */
	public static RdsLocationRichtungTextID getStatus(final int gesuchterCode) {
		RdsLocationRichtungTextID result = RdsLocationRichtungTextID.UNBEKANNT;
		for (final RdsLocationRichtungTextID status : values()) {
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
	 * Die Funktion einen eine neue Instanz für einen RDS-Erinerungstyp mit dem
	 * übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zustände zur Verfügung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	RdsLocationRichtungTextID(final String name, final int code) {
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
