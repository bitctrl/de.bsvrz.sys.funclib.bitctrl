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

package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Steht jeweils für eine Spalte in einer Betriebsmeldung.
 *
 * @author BitCtrl Systems GmbH, Falko
 */
public enum BetriebsMeldungSpalte implements Zustand<Integer> {

	Zeit("Zeit", 0, 150),

	ApplikationsID("ApplikationsID", 1, 150),

	LaufendeNummer("LaufendeNummer", 2, 150),

	ApplikationsTyp("ApplikationsTyp", 3, 100),

	ApplikationsKennung("ApplikationsKennung", 4, 100),

	ID("ID", 5, 100),

	MeldungsTyp("MeldungsTyp", 6, 100),

	MeldungsTypZusatz("MeldungsTypZusatz", 7, 100),

	MeldungsKlasse("MeldungsKlasse", 8, 100),

	Referenz("Referenz", 9, 100),

	Status("Status", 10, 100),

	UrlasserBenutzer("Urlasserbenutzer", 11, 100),

	UrlasserUrsache("Urlasserursache", 12, 100),

	UrlasserVeranlasser("Urlasserveranlasser", 13, 100),

	MeldungsText("MeldungsText", 14, 250);

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
	private final int code;

	/** Der Name des Zustandes. */
	private final String name;

	/** Die empfohlene Spaltenbreite. */
	private final int breite;

	/**
	 * Konstruktor.
	 *
	 * @param name
	 *            der Name des Zustands.
	 * @param code
	 *            der verwendete Code.
	 * @param breite
	 *            die empfohlene Spaltenbreite.
	 */
	BetriebsMeldungSpalte(final String name, final int code,
			final int breite) {
		this.name = name;
		this.code = code;
		this.breite = breite;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name + " (" + code + ")";
	}

	/**
	 * Gibt die empfohlene Spaltenbreite zurück.
	 *
	 * @return
	 */
	public int getBreite() {
		return breite;
	}

}
