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
 * Definitionen f�r den Status einer RDS-Meldung.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public enum RdsStatus implements Zustand<Integer> {

	/** Status der Meldung ist nicht bekannt, Wert 0. */
	UNBEKANNT("Unbekannt", 0),

	/** Meldung wurde ver�ndert, Wert 2. */
	MODIFIKATION("Modifikation", 2),

	/** Meldung wurde aufgehoben, Wert 4. */
	AUFHEBUNG("Aufhebung", 4),

	/** Meldung wurde gel�scht, Wert 5. */
	LOESCHUNG("L�schung", 5),

	/** Meldung wurde erweitert, Wert 6. */
	ERWEITERUNG("Erweiterung", 6);

	/**
	 * liefert den Rds-Status mit dem �bergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine der Status UNBEKANNT geliefert.
	 */
	public static RdsStatus getStatus(final int gesuchterCode) {
		RdsStatus result = RdsStatus.UNBEKANNT;
		for (RdsStatus status : values()) {
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
	 * Die Funktion einen eine neue Instanz f�r einen RDS-Status mit dem
	 * �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zust�nde zur Verf�gung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsStatus(final String name, final int code) {
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
