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

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Werte f�r den Zustand eines Meldungstyps einer
 * Betriebsmeldung.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public enum MeldungsTyp implements Zustand<Integer> {

	/** Meldungstyp f�r eine Meldung eines systemtechnischen Zustands. */
	System("System", 0),

	/** Meldungstyp f�r eine Meldung eines fachlichen Zustands. */
	Fach("Fach", 1);

	/**
	 * Liefert zu einem Code den dazugeh�rigen Meldungstyp.
	 * 
	 * @param code
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird {@code null} zur�ckgegeben.
	 */
	public static MeldungsTyp getMeldungsTyp(final int code) {
		for (final MeldungsTyp situation : values()) {
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
	private MeldungsTyp(final String name, final int code) {
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
