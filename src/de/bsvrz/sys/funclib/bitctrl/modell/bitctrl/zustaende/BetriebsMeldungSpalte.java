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

package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Steht jeweils für eine Spalte in einer Betriebsmeldung.
 * 
 * @author BitCtrl Systems GmbH, Falko
 * @version $Id$
 */
public enum BetriebsMeldungSpalte implements Zustand<Integer> {

	Zeit("Zeit", 0),

	ApplikationsID("ApplikationsID", 1),

	LaufendeNummer("LaufendeNummer", 2),

	ApplikationsTyp("ApplikationsTyp", 3),

	ApplikationsKennung("ApplikationsKennung", 4),

	ID("ID", 5),

	MeldungsTyp("MeldungsTyp", 6),

	MeldungsTypZusatz("MeldungsTypZusatz", 7),

	MeldungsKlasse("MeldungsKlasse", 8),

	Referenz("Referenz", 9),

	Status("Status", 10),

	UrlasserBenutzer("Urlasserbenutzer", 11),

	UrlasserUrsache("Urlasserursache", 12),

	UrlasserVeranlasser("Urlasserveranlasser", 13),

	MeldungsText("MeldungsText", 14);

	/**
	 * Liefert zu einem Code den dazugehörigen Meldungstyp.
	 * 
	 * @param code
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static BetriebsMeldungSpalte valueOf(final int code) {
		for (final BetriebsMeldungSpalte spalte : values()) {
			if (spalte.getCode() == code) {
				return spalte;
			}
		}

		throw new IllegalArgumentException(
				"Ungültige Betriebsmeldungsspalte mit Code: " + code);
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
	private BetriebsMeldungSpalte(final String name, final int code) {
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
