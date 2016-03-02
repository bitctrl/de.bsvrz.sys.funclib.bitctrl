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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */
package de.bsvrz.sys.funclib.bitctrl.modell.netz;

/**
 * Exception, die ausgel&ouml;st wird, wenn bei der Umrechnung von
 * ASB-Ortsreferenzen Stationierungswert im Netz der VRZ nicht konfiguriert ist.
 *
 * @author BitCtrl Systems GmbH, Dipl.-Ing. Steffen Gieseler
 */
public class NetzReferenzAsbStationierungUnbekanntException extends NetzReferenzException {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	private final long stationierung;

	public long getStationierung() {
		return stationierung;
	}

	private static final String FEHLERTEXT1 = "Die Stationierung [";
	private static final String FEHLERTEXT2 = "] ist in der angegebenen Fahrtrichtung nicht bekannt";

	public NetzReferenzAsbStationierungUnbekanntException(final long l) {
		super(FEHLERTEXT1 + l + FEHLERTEXT2);
		stationierung = l;
	}

}
