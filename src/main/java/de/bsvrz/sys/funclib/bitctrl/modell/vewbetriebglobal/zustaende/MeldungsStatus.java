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

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Werte für den Zustand des Status einer Betriebsmeldung.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public enum MeldungsStatus implements Zustand<Integer> {

	/**
	 * Meldung, die nicht mit einem der anderen Status bezeichnet werden kann.
	 * Diese Meldung wird von Applikationen benutzt, die den für die
	 * Unterscheidung der drei Fälle "Neue Meldung", "Wiederholungsmeldung" und
	 * "Änderungsmeldung" notwendigen Verwaltungsaufwand nicht selbst vornehmen
	 * können oder möchten.
	 */
	Meldung("Meldung", 0),

	/**
	 * Meldung, die zu einer zuvor gesandten Meldung gehört und deren Inhalt
	 * wieder aufhebt.
	 */
	Gutmeldung("Gutmeldung", 1),

	/** Eine Meldung, die zum ersten Mal rausgeschickt wird. */
	NeueMeldung("Neue Meldung", 2),

	/** Eine Meldung, die zu einer bereits zuvor gesendeten Meldung gehört. */
	Wiederholungsmeldung("Wiederholungsmeldung", 3),

	/**
	 * Meldung, die zu einer zuvor gesendeten Meldung gehört und deren Inhalt
	 * modifiziert.
	 */
	Aenderungsmeldung("Änderungsmeldung", 4);

	/**
	 * Liefert zu einem Code den dazugehörigen Status.
	 *
	 * @param code
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird {@code null} zurückgegeben.
	 */
	public static MeldungsStatus getMeldungsStatus(final int code) {
		for (final MeldungsStatus situation : values()) {
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
	MeldungsStatus(final String name, final int code) {
		this.name = name;
		this.code = code;
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

}
