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
 * Definitionen für die Klassen der Informationsbestandteile einer Nachricht
 * innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsNachrichtenKlasse implements Zustand<Integer> {

	/** Straßennummer, Wert 0. */
	STRASSEN_NUMMER("Straßennummer", 0),

	/** Segment, Wert 1. */
	SEGMENT("Segment", 1),

	/** Inhalt, Wert 2. */
	INHALT("Inhalt", 2),

	/** freier Text, Wert 3. */
	FREIER_TEXT("freier Text", 3),

	/** Ergänzung, Wert 4. */
	ERGAENZUNG("Ergänzung", 4),

	/** Segment von, Wert 20. */
	SEGMENT_VON("Segment von", 20),

	/** Segment nach, Wert 21. */
	SEGMENT_NACH("Segment nach", 21),

	/** Bundesland, Wert 22. */
	BUNDESLAND("Bundesland", 22),

	/** Stadt, Wert 23). */
	STADT("Stadt", 23),

	/** Name, Wert 40. */
	NAME("Name", 40),

	/** Straßenname, Wert 41. */
	STRASSEN_NAME("Straßenname", 41),

	/** 2. Name, Wert 42. */
	ZWEITER_NAME("2. Name", 42),

	/** Gebietsname, Wert 43. */
	GEBIETS_NAME("Gebietsname", 43),

	/** linearer erster Name, Wert 44. */
	LINEARER_ERSTER_NAME("linearer erster Name", 44),

	/** linearer zweiter Name, Wert 45. */
	LINEARER_ZWEITER_NAME("linearer zweiter Name", 45),

	/** Kreis, Wert 46. */
	KREIS("Kreis", 46),

	/** PLZ, Wert 47. */
	PLZ("PLZ", 47),

	/** Hausnummer, Wert 48. */
	HAUSNUMMER("Hausnummer", 48);

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
		for (final RdsNachrichtenKlasse status : values()) {
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
