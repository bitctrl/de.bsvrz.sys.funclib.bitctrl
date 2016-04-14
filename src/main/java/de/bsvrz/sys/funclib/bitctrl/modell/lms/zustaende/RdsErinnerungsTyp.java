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
 * Definitionen f�r das Attribut Erinnerungstyp innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum RdsErinnerungsTyp implements Zustand<Integer> {

	/** Keine Erinnerung. ErinnerungsZeit wird ignoriert, Wert 0. */
	KEINE("Keine Erinnerung. ErinnerungsZeit wird ignoriert.", 0),

	/**
	 * ErinnerungsZeitOffset enth�lt den Offset seit AktivierungsZeit.
	 * ErinnerungsZeit = AktivierungsZeit + ErinnerungsZeitOffset, Wert 1.
	 */
	PLUS_OFFSET(
			"ErinnerungsZeitOffset enth�lt den Offset seit AktivierungsZeit. ErinnerungsZeit = AktivierungsZeit + ErinnerungsZeitOffset.",
			1),

	/**
	 * ErinnerungsZeitOffset enth�lt den Offset bis AblaufZeit. ErinnerungsZeit
	 * = AblaufZeit - ErinnerungsZeitOffset, Wert 2.
	 */
	MINUS_OFFSET(
			"ErinnerungsZeitOffset enth�lt den Offset bis AblaufZeit. ErinnerungsZeit = AblaufZeit - ErinnerungsZeitOffset.",
			2),

	/**
	 * ErinnerungsZeit enth�lt den absoluten Zeitpunkt. ErinnerungsZeitOffset
	 * wird ignoriert, Wert 3.
	 */
	ABSOLUT("ErinnerungsZeit enth�lt den absoluten Zeitpunkt. ErinnerungsZeitOffset wird ignoriert.",
			3);

	/**
	 * liefert den Rds-Erinnerungstyp mit dem �bergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine der Status UNBEKANNT geliefert.
	 */
	public static RdsErinnerungsTyp getStatus(final int gesuchterCode) {
		RdsErinnerungsTyp result = RdsErinnerungsTyp.KEINE;
		for (final RdsErinnerungsTyp status : values()) {
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
	RdsErinnerungsTyp(final String name, final int code) {
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
