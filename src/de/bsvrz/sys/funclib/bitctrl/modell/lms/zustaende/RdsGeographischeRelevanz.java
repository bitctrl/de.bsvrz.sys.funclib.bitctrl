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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum RdsGeographischeRelevanz implements Zustand<Integer> {

	LOKAL("Lokal", 0), REGIONAL("Regional", 1), UEBERREGIONAL("�berregional", 2), NATIONAL(
			"National", 3), AUSLAND("Ausland", 4), LOKAL_UND_ANGRENZENDES_AUSLAND(
			"Lokal und angrenzendes Ausland", 5), REGIONAL_UND_ANGRENZENDES_AUSLAND(
			"Regional und angrenzendes Ausland", 6), UEBERREGIONAL_UND_ANGRENZENDES_AUSLAND(
			"�berregional und angrenzendes Ausland", 7), LAND_UND_ANGRENZENDES_AUSLAND(
			"Land und angrenzendes Ausland", 8);

	/**
	 * liefert die geograpische Relevanz mit dem �bergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine der Status NATIONAL geliefert.
	 */
	public static RdsGeographischeRelevanz getStatus(final int gesuchterCode) {
		RdsGeographischeRelevanz result = RdsGeographischeRelevanz.NATIONAL;
		for (RdsGeographischeRelevanz status : values()) {
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
	 * Die Funktion einen eine neue Instanz f�r eine geografische Relevanz mit
	 * dem �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zust�nde zur Verf�gung gestellt.
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
	public Integer getCode() {
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
