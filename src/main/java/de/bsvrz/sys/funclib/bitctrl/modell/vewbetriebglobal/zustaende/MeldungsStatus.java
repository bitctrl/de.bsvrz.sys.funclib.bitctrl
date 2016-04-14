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

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Werte f�r den Zustand des Status einer Betriebsmeldung.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public enum MeldungsStatus implements Zustand<Integer> {

	/**
	 * Meldung, die nicht mit einem der anderen Status bezeichnet werden kann.
	 * Diese Meldung wird von Applikationen benutzt, die den f�r die
	 * Unterscheidung der drei F�lle "Neue Meldung", "Wiederholungsmeldung" und
	 * "�nderungsmeldung" notwendigen Verwaltungsaufwand nicht selbst vornehmen
	 * k�nnen oder m�chten.
	 */
	Meldung("Meldung", 0),

	/**
	 * Meldung, die zu einer zuvor gesandten Meldung geh�rt und deren Inhalt
	 * wieder aufhebt.
	 */
	Gutmeldung("Gutmeldung", 1),

	/** Eine Meldung, die zum ersten Mal rausgeschickt wird. */
	NeueMeldung("Neue Meldung", 2),

	/** Eine Meldung, die zu einer bereits zuvor gesendeten Meldung geh�rt. */
	Wiederholungsmeldung("Wiederholungsmeldung", 3),

	/**
	 * Meldung, die zu einer zuvor gesendeten Meldung geh�rt und deren Inhalt
	 * modifiziert.
	 */
	Aenderungsmeldung("�nderungsmeldung", 4);

	/**
	 * Liefert zu einem Code den dazugeh�rigen Status.
	 *
	 * @param code
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird {@code null} zur�ckgegeben.
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
