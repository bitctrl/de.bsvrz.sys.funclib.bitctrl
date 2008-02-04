/*
 * Segment 14 (�Vi), SWE 14.BW-�bergangsvisualisierung 
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum RdsEreignisKategorie implements Zustand {

	VERKEHRSLAGE("Verkehrslage", 1), ERWARTETE_VERKEHRSLAGE(
			"erwartete Verkehrslage", 2), UNFAELLE("Unf�lle", 3), VORFAELLE(
			"Vorf�lle", 4), SPERRUNGEN("Stra�en- und Fahrbahnsperrungen", 5), FAHRBAHN_BESCHRAENKUNGEN(
			"Fahrbahnbeschr�nkungen", 6), AUSFAHRT_BESCHRAENKUNGEN(
			"Beschr�nkungen der Ausfahrt", 7), EINFAHRT_BESCHRAENKUNGEN(
			"Beschr�nkungen der Einfahrt", 8), VERKEHRS_BESCHRAENKUNGEN(
			"Verkehrsbeschr�nkungen", 9), FAHRGEMEINSCHAFT_INFO(
			"Informationen f�r Fahrgemeinschaften", 10), BAUARBEITEN(
			"Bauarbeiten", 11), FAHRBAHN_BEHINDERUNGEN(
			"Behinderungen auf der Fahrbahn", 12), GEFAEHRLICHE_SITUATION(
			"Gef�hrliche Situationen", 13), STRASSENZUSTAND("Stra�enzustand",
			14), TEMPERATUREN("Temperaturen", 15), NIEDERSCHLAG_UND_SICHT(
			"Niederschlag und Sichtbehinderungen", 16), WIND_UND_LUFT(
			"Wind und Luftqualit�t", 17), VERANSTALTUNG("Veranstaltungen", 18), SICHERHEIT(
			"Sicherheitsvorf�lle", 19), ZEITVERLUST("Zeitverluste", 20), AUSFAELLE(
			"Ausf�lle", 21), REISEINFORMATIONEN("Reiseinformation", 22), GEFAEHRLICHE_FAHRZEUGE(
			"Gef�hrliche Fahrzeuge", 23), AUSSERGEW�HNLICHE_FAHRZEUGE(
			"Au�ergew�hnliche Ladungen und Fahrzeuge", 24), LSA_STOERUNG(
			"St�rungen an Lichtsignalanlagen und sonstigen Stra�enausr�stungen",
			25), MASSE_UND_GEWICHTE(
			"Beschr�nkungen der Fahrzeugma�e und -gewichte", 26), PARKREGELUNG(
			"Parkregelungen", 27), PARKEN("Parken", 28), INFORMATION(
			"Information", 29), SERVICE("Service-Meldung", 30), SPEZIELL(
			"spezielle Meldung", 31), EMPFEHLUNG("Empfehlung", 50);

	/**
	 * liefert die Rds-Ereigniskategorie mit dem �bergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine der Status VERKEHRSLAGE geliefert.
	 */
	public static RdsEreignisKategorie getStatus(final int gesuchterCode) {
		RdsEreignisKategorie result = RdsEreignisKategorie.VERKEHRSLAGE;
		for (RdsEreignisKategorie status : values()) {
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
	 * Die Funktion einen eine neue Instanz f�r eine RDS-Ereigniskategorie mit
	 * dem �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zust�nde zur Verf�gung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsEreignisKategorie(final String name, final int code) {
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
