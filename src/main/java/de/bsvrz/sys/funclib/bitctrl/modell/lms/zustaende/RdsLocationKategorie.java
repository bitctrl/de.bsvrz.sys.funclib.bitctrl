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
 * Die Definition des Attributs LocationKategorie innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsLocationKategorie implements Zustand<Integer> {

	/** Nicht benutzt, Wert 0. */
	NICHT_BENUTZT("Nicht benutzt", 0),

	/** Gebiet, Wert 1. */
	GEBIET("Gebiet", 1),

	/** bedeutender Punkt, Wert 2. */
	PUNKT("bedeutender Punkt", 2),

	/** Linie, Wert 3. */
	LINIE("Linie", 3),

	/** Segment, Wert 4. */
	SEGMENT("Segment", 4);

	/**
	 * liefert die Rds-Locationkategorie mit dem übergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status PUNKT geliefert.
	 */
	public static RdsLocationKategorie getStatus(final int gesuchterCode) {
		RdsLocationKategorie result = RdsLocationKategorie.NICHT_BENUTZT;
		for (final RdsLocationKategorie status : values()) {
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
	 * Die Funktion einen eine neue Instanz für eine RDS-Locationkategorie mit
	 * dem übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zustände zur Verfügung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	RdsLocationKategorie(final String name, final int code) {
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
