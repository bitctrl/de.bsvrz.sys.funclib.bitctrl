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

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Werte für den Zustand der Meldungsklasse einer
 * Betriebsmeldung.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public enum MeldungsKlasse implements Zustand<Integer> {

	/** Die Meldungsklasse Fatal. */
	Fatal("Fatal", 0),

	/** Die Meldungsklasse Fehler. */
	Fehler("Fehler", 1),

	/** Die Meldungsklasse Warnung. */
	Warnung("Warnung", 2),

	/** Die Meldungsklasse Information. */
	Information("Information", 3);

	/**
	 * Liefert zu einem Code die dazugehörige Meldungsklasse.
	 * 
	 * @param code
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird {@code null} zurückgegegeben.
	 */
	public static MeldungsKlasse getMeldungsKlasse(final int code) {
		for (final MeldungsKlasse situation : values()) {
			if (situation.getCode() == code) {
				return situation;
			}
		}

		return null;
	}

	/** Der Code des Zustandes. */
	private int code;

	/** Der Name des Zustandes. */
	private String name;

	/**
	 * Konstruktor.
	 * 
	 * @param name
	 *            der Name des Zustands.
	 * @param code
	 *            der verwendete Code.
	 */
	private MeldungsKlasse(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name + " (" + code + ")";
	}

}
