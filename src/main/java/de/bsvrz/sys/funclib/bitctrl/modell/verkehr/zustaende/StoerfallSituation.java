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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definition der Werte für den Zustand eines Störfallindikators.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public enum StoerfallSituation implements Zustand<Integer> {
	/**
	 * der Datenverteiler hat keine Daten zum Indikator geliefert.
	 */
	UNBEKANNT("Unbekannt", -1), /**
	 * der Störfallindikator ist gestört.
	 */
	STOERUNG("Störung", 0), /**
	 * der Indikator kann keine Aussage zum aktuellen
	 * Verkehrszustand liefern.
	 */
	KEINE_AUSSAGE("keine Aussage", 1), /**
	 * es herrscht freier Verkehr.
	 */
	FREIER_VERKEHR("freier Verkehr", 2), /**
	 * es herrscht lebhafter Verkehr.
	 */
	LEBHAFTER_VERKEHR("lebhafter Verkehr", 3), /**
	 * es herrscht dichter Verkehr.
	 */
	DICHTER_VERKEHR("dichter Verkehr",
			4), /**
			 * es herrscht zähfliessender Verkehr.
			 */
	ZAEHER_VERKEHR("zähfließender Verkehr",
			5), /**
			 * es herrscht stockender Verkehr.
			 */
	STOCKENDER_VERKEHR("stockender Verkehr", 6), /**
	 * es ist Stau.
	 */
	STAU("Stau", 7);

	/**
	 * liefert den Störfallzustand mit dem übergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static StoerfallSituation getSituation(final int gesuchterCode) {
		for (final StoerfallSituation situation : values()) {
			if (situation.getCode() == gesuchterCode) {
				return situation;
			}
		}

		throw new IllegalArgumentException(
				"Ungültiger Situation mit Code: " + gesuchterCode);
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
	 * Die Funktion einen eine neue Instanz für einen Störfallzustand mit dem
	 * übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zustände zur Verfügung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private StoerfallSituation(final String name, final int code) {
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

	@Override
	public String toString() {
		return name + " (" + code + ")"; //$NON-NLS-1$//$NON-NLS-2$
	}

}
