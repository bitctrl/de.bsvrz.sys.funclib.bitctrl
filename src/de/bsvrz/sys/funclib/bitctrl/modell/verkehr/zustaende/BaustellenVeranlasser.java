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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definitionen f�r den Veranlasser einer Baustelle gem�� Datenkatalog.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public enum BaustellenVeranlasser implements Zustand {
	/**
	 * der Veranlasser ist nicht definiert.
	 */
	ALLE("Alle", -1),
	/**
	 * der Veranlasser ist nicht definiert.
	 */
	UNDEFINIERT("Undefiniert", 0),
	/**
	 * Baustelle wurde durch externes BIS-System angelegt.
	 */
	BIS("BIS-extern", 1),
	/**
	 * Baustelle wurde intern von der VRZ angelegt.
	 */
	VRZ("VRZ-intern", 2);

	/**
	 * liefert den St�rfallzustand mit dem �bergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Code, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static BaustellenVeranlasser getVeranlasser(int gesuchterCode) {
		BaustellenVeranlasser result = BaustellenVeranlasser.UNDEFINIERT;
		for (BaustellenVeranlasser veranlasser : values()) {
			if (veranlasser.getCode() == gesuchterCode) {
				result = veranlasser;
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
	 * Die Funktion einen eine neue Instanz f�r einen St�rfallzustand mit dem
	 * �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zust�nde zur Verf�gung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private BaustellenVeranlasser(String name, int code) {
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