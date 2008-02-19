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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definitionen f�r die unterst�tzten Sprachen einer RDS-Meldung.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public enum RdsNachrichtenSprache implements Zustand<Integer> {

	/** baskisch, Wert 1069. */
	BASKISCH("baskisch", 1069),

	/** belorussisch, Wert 1059. */
	BELORUSSISCH("belorussisch", 1059),

	/** bulgarisch, Wert 1026. */
	BULGARISCH("bulgarisch", 1026),

	/** katalanisch, Wert 1027. */
	KATALANISCH("katalanisch", 1027),

	/** kroatisch, Wert 1050. */
	KROATISCH("kroatisch", 1050),

	/** tschechisch, Wert 1029. */
	TSCHECHISCH("tschechisch", 1029),

	/** d�nisch, Wert 1030. */
	DAENISCH("d�nisch", 1030),

	/** niederl�ndisch (Niederlande), Wert 1043. */
	NIEDERLAENDISCH("niederl�ndisch (Niederlande)", 1043),

	/** niederl�ndisch (Belgien), Wert 2067. */
	NIEDERLAENDISCH_BELGIEN("niederl�ndisch (Belgien)", 2067),

	/** englisch (United States), Wert 1033. */
	ENGLISCH_US("englisch (United States)", 1033),

	/** englisch (United Kingdom), Wert 2057. */
	ENGLISCH_UK("englisch (United Kingdom)", 2057),

	/** englisch (Irland), Wert 6153. */
	ENGLISCH_IR("englisch (Irland)", 6153),

	/** englisch (Europa), Wert 9225. */
	ENGLISCH_EUROPA("englisch (Europa)", 9225),

	/** estnisch, Wert 1061. */
	ESTNISCH("estnisch", 1061),

	/** finnisch, Wert 1035. */
	FINNISCH("finnisch", 1035),

	/** franz�sisch (Standard), Wert 1036. */
	FRANZOESISCH("franz�sisch (Standard)", 1036),

	/** franz�sisch (Belgien), Wert 2060. */
	FRANZOESISCH_BELGIEN("franz�sisch (Belgien)", 2060),

	/** franz�sisch (Schweiz), Wert 4108. */
	FRANZOESISCH_SCHWEIZ("franz�sisch (Schweiz)", 4108),

	/** franz�sisch (Luxemburg), Wert 5132. */
	FRANZOESISCH_LUXEMBURG("franz�sisch (Luxemburg)", 5132),

	/** franz�sisch (Monaco), Wert 6156. */
	FRANZOESISCH_MONACO("franz�sisch (Monaco)", 6156),

	/** deutsch (Standard), Wert 1031. */
	DEUTSCH("deutsch (Standard)", 1031),

	/** deutsch (Schweiz), Wert 2055. */
	DEUTSCH_SCHWEIZ("deutsch (Schweiz)", 2055),

	/** deutsch (�sterreich), Wert 3079. */
	DEUTSCH_OESTERREICH("deutsch (�sterreich)", 3079),

	/** deutsch (Luxemburg), Wert 4103. */
	DEUTSCH_LUXEMBURG("deutsch (Luxemburg)", 4103),

	/** deutsch (Liechtenstein), Wert 5127. */
	DEUTSCH_LIECHTENSTEIN("deutsch (Liechtenstein)", 5127),

	/** griechisch, Wert 1032. */
	GRIECHISCH("griechisch", 1032),

	/** ungarisch, Wert 1038. */
	UNGARISCH("ungarisch", 1038),

	/** italienisch (Standard), Wert 1040. */
	ITALIENISCH("italienisch (Standard)", 1040),

	/** italienisch (Schweiz), Wert 2064. */
	ITALIENISCH_SCHWEIZ("italienisch (Schweiz)", 2064),

	/** norwegisch (Bokmal), Wert 1044. */
	NORWEGISCH_BOKMAL("norwegisch (Bokmal)", 1044),

	/** norwegisch (Nynorsk), Wert 2068. */
	NORWEGISCH_NYNORSK("norwegisch (Nynorsk)", 2068),

	/** polnisch", Wert 1045. */
	POLNISCH("polnisch", 1045),

	/** portugisisch (Brasilien), Wert 1046. */
	PORTUGISISCH_BRASILIEN("portugisisch (Brasilien)", 1046),

	/** portugisisch (Standard), Wert 2070. */
	PORTUGISISCH("portugisisch (Standard)", 2070),

	/** rum�nisch, Wert 1048. */
	RUMAENISCH("rum�nisch", 1048),

	/** russisch, Wert 1049. */
	RUSSISCH("russisch", 1049),

	/** "serbisch (russisch), Wert 3098. */
	SERBISCH_RUSSISCH("serbisch (russisch)", 3098),

	/** serbisch (latein), Wert 2074. */
	SERBISCH_LATEIN("serbisch (latein)", 2074),

	/** slowakisch, Wert 1051. */
	SLOWAKISCH("slowakisch", 1051),

	/** slowenisch, Wert 1060. */
	SLOWENISCH("slowenisch", 1060),

	/** spanisch (traditionell), Wert 1034. */
	SPANISCH_TRADITIONELL("spanisch (traditionell)", 1034),

	/** spanisch (modern), Wert 3082. */
	SPANISCH_MODERN("spanisch (modern)", 3082),

	/** schwedisch, Wert 1053. */
	SCHWEDISCH("schwedisch", 1053),

	/** schwedisch (Finnland), Wert 2077. */
	SCHWEDISCH_FINNLAND("schwedisch (Finnland)", 2077),

	/** t�rkisch, Wert 1055. */
	TUERKISCH("t�rkisch", 1055),

	/** ukrainisch, Wert 1058. */
	UKRAINISCH("ukrainisch", 1058);

	/**
	 * liefert die Rds-Nachrichtensprache mit dem �bergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
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
	 * Die Funktion einen eine neue Instanz f�r eine RDS-Nachrichtensprache mit
	 * dem �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zust�nde zur Verf�gung gestellt.
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
	public Integer getCode() {
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
