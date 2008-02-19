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

/**
 * Definition der Kategorien f�r Ereignisse in RDS-Meldungen.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public enum RdsEreignisKategorie implements Zustand<Integer> {

	/** Verkehrslage. */
	VERKEHRSLAGE("Verkehrslage", 1),

	/** erwartete Verkehrslage. */
	ERWARTETE_VERKEHRSLAGE("erwartete Verkehrslage", 2),

	/** Unf�lle. */
	UNFAELLE("Unf�lle", 3),

	/** Vorf�lle. */
	VORFAELLE("Vorf�lle", 4),

	/** Stra�en- und Fahrbahnsperrungen. */
	SPERRUNGEN("Stra�en- und Fahrbahnsperrungen", 5),

	/** Fahrbahnbeschr�nkungen. */
	FAHRBAHN_BESCHRAENKUNGEN("Fahrbahnbeschr�nkungen", 6),

	/** Beschr�nkungen der Ausfahrt. */
	AUSFAHRT_BESCHRAENKUNGEN("Beschr�nkungen der Ausfahrt", 7),

	/** Beschr�nkungen der Einfahrt. */
	EINFAHRT_BESCHRAENKUNGEN("Beschr�nkungen der Einfahrt", 8),

	/** Verkehrsbeschr�nkungen. */
	VERKEHRS_BESCHRAENKUNGEN("Verkehrsbeschr�nkungen", 9),

	/** Informationen f�r Fahrgemeinschaften. */
	FAHRGEMEINSCHAFT_INFO("Informationen f�r Fahrgemeinschaften", 10),

	/** Bauarbeiten. */
	BAUARBEITEN("Bauarbeiten", 11),

	/** Behinderungen auf der Fahrbahn. */
	FAHRBAHN_BEHINDERUNGEN("Behinderungen auf der Fahrbahn", 12),

	/** Gef�hrliche Situationen. */
	GEFAEHRLICHE_SITUATION("Gef�hrliche Situationen", 13),

	/** Stra�enzustand. */
	STRASSENZUSTAND("Stra�enzustand", 14),

	/** Temperaturen. */
	TEMPERATUREN("Temperaturen", 15),

	/** Niederschlag und Sichtbehinderungen. */
	NIEDERSCHLAG_UND_SICHT("Niederschlag und Sichtbehinderungen", 16),

	/** Wind und Luftqualit�t. */
	WIND_UND_LUFT("Wind und Luftqualit�t", 17),

	/** Veranstaltungen. */
	VERANSTALTUNG("Veranstaltungen", 18),

	/** Sicherheitsvorf�lle. */
	SICHERHEIT("Sicherheitsvorf�lle", 19),

	/** Zeitverluste. */
	ZEITVERLUST("Zeitverluste", 20),

	/** Ausf�lle. */
	AUSFAELLE("Ausf�lle", 21),

	/** Reiseinformation. */
	REISEINFORMATIONEN("Reiseinformation", 22),

	/** Gef�hrliche Fahrzeuge. */
	GEFAEHRLICHE_FAHRZEUGE("Gef�hrliche Fahrzeuge", 23),

	/** Au�ergew�hnliche Ladungen und Fahrzeuge. */
	AUSSERGEW�HNLICHE_FAHRZEUGE("Au�ergew�hnliche Ladungen und Fahrzeuge", 24),

	/** St�rungen an Lichtsignalanlagen und sonstigen Stra�enausr�stungen. */
	LSA_STOERUNG(
			"St�rungen an Lichtsignalanlagen und sonstigen Stra�enausr�stungen",
			25),

	/** Beschr�nkungen der Fahrzeugma�e und -gewichte. */
	MASSE_UND_GEWICHTE("Beschr�nkungen der Fahrzeugma�e und -gewichte", 26),

	/** Parkregelungen. */
	PARKREGELUNG("Parkregelungen", 27),

	/** Parken. */
	PARKEN("Parken", 28),

	/** Information. */
	INFORMATION("Information", 29),

	/** Service-Meldung. */
	SERVICE("Service-Meldung", 30),

	/** spezielle Meldung. */
	SPEZIELL("spezielle Meldung", 31),

	/** Empfehlung. */
	EMPFEHLUNG("Empfehlung", 50);

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
