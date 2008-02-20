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
 * Repr�sentation des Attributs RdsLocationRichtungTextID innerhalb einer
 * RDS-Meldung.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public enum RdsLocationRichtungTextID implements Zustand<Integer> {

	/** unbekannt, Wert 0. */
	UNBEKANNT("unbekannt", 0),

	/** stadteinw�rts, Wert 1. */
	STADTEINWAERTS("stadteinw�rts", 1),

	/** stadtausw�rts, Wert 2. */
	STADTAUSWAERTS("stadtausw�rts", 2),

	/** Nord, Wert 3. */
	NORD("Nord", 3),

	/** S�d, Wert 4. */
	SUED("S�d", 4),

	/** Ost, Wert 5. */
	OST("Ost", 5),

	/** West, Wert 6. */
	WEST("West", 6),

	/** Nordost, Wert 7. */
	NORDOST("Nordost", 7),

	/** Nordwest, Wert 8. */
	NORDWEST("Nordwest", 8),

	/** S�dost, Wert 9. */
	SUEDOST("S�dost", 9),

	/** S�dwest, Wert 10. */
	SUEDWEST("S�dwest", 10);

	/**
	 * liefert den Rds-Erinnerungstyp mit dem �bergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine der Status UNBEKANNT geliefert.
	 */
	public static RdsLocationRichtungTextID getStatus(final int gesuchterCode) {
		RdsLocationRichtungTextID result = RdsLocationRichtungTextID.UNBEKANNT;
		for (RdsLocationRichtungTextID status : values()) {
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
	 * Die Funktion einen eine neue Instanz f�r einen RDS-Erinerungstyp mit dem
	 * �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zust�nde zur Verf�gung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsLocationRichtungTextID(final String name, final int code) {
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
