/*
 * Segment 14 (ÜVi), SWE 14.BW-Übergangsvisualisierung 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum RdsNachrichtenSprache implements Zustand {

	BASKISCH("baskisch", 1069), BELORUSSISCH("belorussisch", 1059), BULGARISCH(
			"bulgarisch", 1026), KATALANISCH("katalanisch", 1027), KROATISCH(
			"kroatisch", 1050), TSCHECHISCH("tschechisch", 1029), DAENISCH(
			"dänisch", 1030), NIEDERLAENDISCH("niederländisch (Niederlande)",
			1043), NIEDERLAENDISCH_BELGIEN("niederländisch (Belgien)", 2067), ENGLISCH_US(
			"englisch (United States)", 1033), ENGLISCH_UK(
			"englisch (United Kingdom)", 2057), ENGLISCH_IR(
			"englisch (Irland)", 6153), ENGLISCH_EUROPA("englisch (Europa)",
			9225), ESTNISCH("estnisch", 1061), FINNISCH("finnisch", 1035), FRANZOESISCH(
			"französisch (Standard)", 1036), FRANZOESISCH_BELGIEN(
			"französisch (Belgien)", 2060), FRANZOESISCH_SCHWEIZ(
			"französisch (Schweiz)", 4108), FRANZOESISCH_LUXEMBURG(
			"französisch (Luxemburg)", 5132), FRANZOESISCH_MONACO(
			"französisch (Monaco)", 6156), DEUTSCH("deutsch (Standard)", 1031), DEUTSCH_SCHWEIZ(
			"deutsch (Schweiz)", 2055), DEUTSCH_OESTERREICH(
			"deutsch (Österreich)", 3079), DEUTSCH_LUXEMBURG(
			"deutsch (Luxemburg)", 4103), DEUTSCH_LIECHTENSTEIN(
			"deutsch (Liechtenstein)", 5127), GRIECHISCH("griechisch", 1032), UNGARISCH(
			"ungarisch", 1038), ITALIENISCH("italienisch (Standard)", 1040), ITALIENISCH_SCHWEIZ(
			"italienisch (Schweiz)", 2064), NORWEGISCH_BOKMAL(
			"norwegisch (Bokmal)", 1044), NORWEGISCH_NYNORSK(
			"norwegisch (Nynorsk)", 2068), POLNISCH("polnisch", 1045), PORTUGISISCH_BRASILIEN(
			"portugisisch (Brasilien)", 1046), PORTUGISISCH(
			"portugisisch (Standard)", 2070), RUMAENISCH("rumänisch", 1048), RUSSISCH(
			"russisch", 1049), SERBISCH_RUSSISCH("serbisch (russisch)", 3098), SERBISCH_LATEIN(
			"serbisch (latein)", 2074), SLOWAKISCH("slowakisch", 1051), SLOWENISCH(
			"slowenisch", 1060), SPANISCH_TRADITIONELL(
			"spanisch (traditionell)", 1034), SPANISCH_MODERN(
			"spanisch (modern)", 3082), SCHWEDISCH("schwedisch", 1053), SCHWEDISCH_FINNLAND(
			"schwedisch (Finnland)", 2077), TUERKISCH("türkisch", 1055), UKRAINISCH(
			"ukrainisch", 1058);

	/**
	 * liefert die Rds-Nachrichtensprache mit dem übergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ungültiger Code übergeben wurde,
	 *         wird eine der Status DEUTSCH geliefert.
	 */
	public static RdsNachrichtenSprache getStatus(final int gesuchterCode) {
		RdsNachrichtenSprache result = RdsNachrichtenSprache.DEUTSCH;
		for (RdsNachrichtenSprache status : values()) {
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
	 * Die Funktion einen eine neue Instanz für eine RDS-Nachrichtensprache mit
	 * dem übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zustände zur Verfügung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsNachrichtenSprache(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 * 
	 * @return den Code.
	 */
	public int getCode() {
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
