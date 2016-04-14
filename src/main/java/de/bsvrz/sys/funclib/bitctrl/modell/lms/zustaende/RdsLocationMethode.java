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
 * Wei�enfelser Stra�e 67
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
	 * erste Feldposition ist prim�re Location und letzte Feldposition ist
	 * sekund�re Location, Wert 0.
	 */
	METHODE0(
			"erste Feldposition ist prim�re Location und letzte Feldposition ist sekund�re Location",
			0),

	/** Prim�re Location und Extent sind g�ltig, Wert 1. */
	METHODE1("Prim�re Location und Extent sind g�ltig", 1),

	/** Prim�re Location und Sekund�re Location sind g�ltig, Wert 2. */
	METHODE2("Prim�re Location und Sekund�re Location sind g�ltig", 2),

	/** Prim�re Location, Extent und prim�re Entfernung sind g�ltig, Wert 3. */
	METHODE3("Prim�re Location, Extent und prim�re Entfernung sind g�ltig", 3),

	/**
	 * Prim�re Location, sekund�re Location, prim�re Entfernung und sekund�re
	 * Entfernung sind g�ltig, Wert 4.
	 */
	METHODE4(
			"Prim�re Location, sekund�re Location, prim�re Entfernung und sekund�re Entfernung sind g�ltig.",
			4),

	/**
	 * Strassennummer, Ausfahrtnummer und Nachrichtentext f�r Nachrichtenklasse
	 * = 40 der prim�ren und sekund�ren Location sind g�ltig, Wert 5.
	 */
	METHODE5(
			"Strassennummer, Ausfahrtnummer und Nachrichtentext f�r Nachrichtenklasse = 40 der prim�ren und sekund�ren Location sind g�ltig.",
			5),

	/**
	 * Strassennummer, LocationKilometrierung der prim�ren und sekund�ren
	 * Location sind g�ltig, Wert 6.
	 */
	METHODE6(
			"Strassennummer, LocationKilometrierung der prim�ren und sekund�ren Location sind g�ltig.",
			6);

	/**
	 * liefert die Rds-Locationmethode mit dem �bergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
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
	 * Die Funktion einen eine neue Instanz f�r eine RDS-Locationmethode mit dem
	 * �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zust�nde zur Verf�gung gestellt.
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
