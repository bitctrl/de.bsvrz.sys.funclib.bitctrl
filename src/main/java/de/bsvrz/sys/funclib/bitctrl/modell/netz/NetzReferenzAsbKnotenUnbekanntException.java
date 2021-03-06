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
package de.bsvrz.sys.funclib.bitctrl.modell.netz;

/**
 * Exception, die ausgel&ouml;st wird, wenn bei der Umrechnung von
 * ASB-Ortsreferenzen der Anfangs- oder Endknoten im Netz der VRZ nicht
 * konfiguriert ist.
 *
 * @author BitCtrl Systems GmbH, Dipl.-Ing. Steffen Gieseler
 */
public class NetzReferenzAsbKnotenUnbekanntException
extends NetzReferenzException {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Der unbekannte Knoten.
	 */
	private final String asbKnoten;

	/**
	 * Gibt den unbekannten ASB-Knoten zur&uuml;ck.
	 *
	 * @return ASB-Knoten
	 */
	public String getAsbKnoten() {
		return asbKnoten;
	}

	/**
	 * Fehlertexte.
	 */
	private static final String FEHLERTEXT1 = "Der Netzknoten [";
	private static final String FEHLERTEXT2 = "] ist in der angegebenen Fahrtrichtung nicht bekannt";

	/**
	 * Konstruktor.
	 *
	 * @param asbKnoten
	 *            unbekannter ASB-Knoten
	 */
	public NetzReferenzAsbKnotenUnbekanntException(final String asbKnoten) {
		super(FEHLERTEXT1 + asbKnoten + FEHLERTEXT2);
		this.asbKnoten = asbKnoten;
	}

}
