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
 * Die Definitionen zur Abbildung des Authorisirungszustands einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsAuthorisierungsErgebnis implements Zustand<Integer> {

	/** Meldung wurde noch nicht authorisiert. */
	NOCH_NICHT_AUTHORISIERT("(Noch) nicht authorisiert",
			0), /** Meldung wurde authorisiert. */
	AUTHORISIERT("authorisiert", 1), /** Meldung wurde zur�ckgewiesen. */
	ZURUECKGEWIESEN("zur�ckgewiesen", 2), /**
	 * Meldung und die ihr folgenden
	 * Meldungen wurden zur�ckgewiesen.
	 */
	ZURUECKGEWIESEN_UND_FOLGENDE("zur�ckgewiesen und Zur�ckweisung folgender",
			3);

	/**
	 * liefert das Authorisierungsergebnis mit dem �bergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine der Status
	 *         {@link RdsAuthorisierungsErgebnis#NOCH_NICHT_AUTHORISIERT}
	 *         geliefert.
	 */
	public static RdsAuthorisierungsErgebnis getStatus(
			final int gesuchterCode) {
		RdsAuthorisierungsErgebnis result = RdsAuthorisierungsErgebnis.NOCH_NICHT_AUTHORISIERT;
		for (final RdsAuthorisierungsErgebnis status : values()) {
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
	 * Die Funktion einen eine neue Instanz f�r ein RDS-Authorisierungsergebnis
	 * mit dem �bergebenem Code und der entsprechenden Bezeichnung. Der
	 * Konstruktor wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zust�nde zur Verf�gung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	RdsAuthorisierungsErgebnis(final String name, final int code) {
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
