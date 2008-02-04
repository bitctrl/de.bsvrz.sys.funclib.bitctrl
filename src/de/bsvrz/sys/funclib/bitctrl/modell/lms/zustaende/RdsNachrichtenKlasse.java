/*
 * Segment 14 (ÜVi), SWE 14.BW-Übergangsvisualisierung 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
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

public enum RdsNachrichtenKlasse implements Zustand {
	STRASSEN_NUMMER("Straßennummer", 0), SEGMENT("Segment", 1), INHALT(
			"Inhalt", 2), FREIER_TEXT("freier Text", 3), ERGAENZUNG(
			"Ergänzung", 4), SEGMENT_VON("Segment von", 20), SEGMENT_NACH(
			"Segment nach", 21), BUNDESLAND("Bundesland", 22), STADT("Stadt",
			23), NAME("Name", 40), STRASSEN_NAME("Straßenname", 41), ZWEITER_NAME(
			"2. Name", 42), GEBIETS_NAME("Gebietsname", 43), LINEARER_ERSTER_NAME(
			"linearer erster Name", 44), LINEARER_ZWEITER_NAME(
			"linearer zweiter Name", 45), KREIS("Kreis", 46), PLZ("PLZ", 47), HAUSNUMMER(
			"Hausnummer", 48);

	/**
	 * liefert den Rds-Nachrichtenklasse mit dem übergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status FREIER_TEXT geliefert.
	 */
	public static RdsNachrichtenKlasse getStatus(final int gesuchterCode) {
		RdsNachrichtenKlasse result = RdsNachrichtenKlasse.FREIER_TEXT;
		for (RdsNachrichtenKlasse status : values()) {
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
	 * Die Funktion einen eine neue Instanz für eine RDS-Nachrichtenklasse mit
	 * dem übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zustände zur Verfügung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsNachrichtenKlasse(final String name, final int code) {
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
