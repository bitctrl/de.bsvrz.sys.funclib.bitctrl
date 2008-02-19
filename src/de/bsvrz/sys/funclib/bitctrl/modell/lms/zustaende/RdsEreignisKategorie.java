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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Kategorien für Ereignisse in RDS-Meldungen.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public enum RdsEreignisKategorie implements Zustand<Integer> {

	/** Verkehrslage. */
	VERKEHRSLAGE("Verkehrslage", 1),

	/** erwartete Verkehrslage. */
	ERWARTETE_VERKEHRSLAGE("erwartete Verkehrslage", 2),

	/** Unfälle. */
	UNFAELLE("Unfälle", 3),

	/** Vorfälle. */
	VORFAELLE("Vorfälle", 4),

	/** Straßen- und Fahrbahnsperrungen. */
	SPERRUNGEN("Straßen- und Fahrbahnsperrungen", 5),

	/** Fahrbahnbeschränkungen. */
	FAHRBAHN_BESCHRAENKUNGEN("Fahrbahnbeschränkungen", 6),

	/** Beschränkungen der Ausfahrt. */
	AUSFAHRT_BESCHRAENKUNGEN("Beschränkungen der Ausfahrt", 7),

	/** Beschränkungen der Einfahrt. */
	EINFAHRT_BESCHRAENKUNGEN("Beschränkungen der Einfahrt", 8),

	/** Verkehrsbeschränkungen. */
	VERKEHRS_BESCHRAENKUNGEN("Verkehrsbeschränkungen", 9),

	/** Informationen für Fahrgemeinschaften. */
	FAHRGEMEINSCHAFT_INFO("Informationen für Fahrgemeinschaften", 10),

	/** Bauarbeiten. */
	BAUARBEITEN("Bauarbeiten", 11),

	/** Behinderungen auf der Fahrbahn. */
	FAHRBAHN_BEHINDERUNGEN("Behinderungen auf der Fahrbahn", 12),

	/** Gefährliche Situationen. */
	GEFAEHRLICHE_SITUATION("Gefährliche Situationen", 13),

	/** Straßenzustand. */
	STRASSENZUSTAND("Straßenzustand", 14),

	/** Temperaturen. */
	TEMPERATUREN("Temperaturen", 15),

	/** Niederschlag und Sichtbehinderungen. */
	NIEDERSCHLAG_UND_SICHT("Niederschlag und Sichtbehinderungen", 16),

	/** Wind und Luftqualität. */
	WIND_UND_LUFT("Wind und Luftqualität", 17),

	/** Veranstaltungen. */
	VERANSTALTUNG("Veranstaltungen", 18),

	/** Sicherheitsvorfälle. */
	SICHERHEIT("Sicherheitsvorfälle", 19),

	/** Zeitverluste. */
	ZEITVERLUST("Zeitverluste", 20),

	/** Ausfälle. */
	AUSFAELLE("Ausfälle", 21),

	/** Reiseinformation. */
	REISEINFORMATIONEN("Reiseinformation", 22),

	/** Gefährliche Fahrzeuge. */
	GEFAEHRLICHE_FAHRZEUGE("Gefährliche Fahrzeuge", 23),

	/** Außergewöhnliche Ladungen und Fahrzeuge. */
	AUSSERGEWÖHNLICHE_FAHRZEUGE("Außergewöhnliche Ladungen und Fahrzeuge", 24),

	/** Störungen an Lichtsignalanlagen und sonstigen Straßenausrüstungen. */
	LSA_STOERUNG(
			"Störungen an Lichtsignalanlagen und sonstigen Straßenausrüstungen",
			25),

	/** Beschränkungen der Fahrzeugmaße und -gewichte. */
	MASSE_UND_GEWICHTE("Beschränkungen der Fahrzeugmaße und -gewichte", 26),

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
	 * liefert die Rds-Ereigniskategorie mit dem übergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
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
	 * Die Funktion einen eine neue Instanz für eine RDS-Ereigniskategorie mit
	 * dem übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zustände zur Verfügung gestellt.
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
