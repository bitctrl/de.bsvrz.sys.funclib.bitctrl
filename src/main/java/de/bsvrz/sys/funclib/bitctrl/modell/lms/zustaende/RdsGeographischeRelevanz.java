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
 * Definitionen für das Attribut Geografische Relevanz innerhalb einer
 * RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsGeographischeRelevanz implements Zustand<Integer> {

	/** Lokal, Wert 0. */
	LOKAL("Lokal", 0),

	/** Regional, Wert 1. */
	REGIONAL("Regional", 1),

	/** Überregional, Wert 2. */
	UEBERREGIONAL("Überregional", 2),

	/** National, Wert 3. */
	NATIONAL("National", 3),

	/** Ausland, Wert 4. */
	AUSLAND("Ausland", 4),

	/** Lokal und angrenzendes Ausland, Wert 5. */
	LOKAL_UND_ANGRENZENDES_AUSLAND("Lokal und angrenzendes Ausland", 5),

	/** Regional und angrenzendes Ausland, Wert 6. */
	REGIONAL_UND_ANGRENZENDES_AUSLAND("Regional und angrenzendes Ausland", 6),

	/** Überregional und angrenzendes Ausland, Wert 7. */
	UEBERREGIONAL_UND_ANGRENZENDES_AUSLAND(
			"Überregional und angrenzendes Ausland", 7),

	/** Land und angrenzendes Ausland, Wert 8. */
	LAND_UND_ANGRENZENDES_AUSLAND("Land und angrenzendes Ausland", 8);

	/**
	 * liefert die geograpische Relevanz mit dem übergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status NATIONAL geliefert.
	 */
	public static RdsGeographischeRelevanz getStatus(final int gesuchterCode) {
		RdsGeographischeRelevanz result = RdsGeographischeRelevanz.NATIONAL;
		for (final RdsGeographischeRelevanz status : values()) {
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
	 * Die Funktion einen eine neue Instanz für eine geografische Relevanz mit
	 * dem übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zustände zur Verfügung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsGeographischeRelevanz(final String name, final int code) {
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
