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

package de.bsvrz.sys.funclib.bitctrl.dua.lve.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.fahrStreifenLage</code> beschriebenen Werte zur Verfügung gestellt.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 **/
public final class FahrStreifenLage extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, FahrStreifenLage> werteBereich = new HashMap<Integer, FahrStreifenLage>();

	/**
	 * Wert <code>HFS</code>.
	 */
	public static final FahrStreifenLage HFS = new FahrStreifenLage("HFS", 0); //$NON-NLS-1$

	/**
	 * Wert <code>1ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS1 = new FahrStreifenLage("1ÜFS", 1); //$NON-NLS-1$

	/**
	 * Wert <code>2ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS2 = new FahrStreifenLage("2ÜFS", 2); //$NON-NLS-1$

	/**
	 * Wert <code>3ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS3 = new FahrStreifenLage("3ÜFS", 3); //$NON-NLS-1$

	/**
	 * Wert <code>4ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS4 = new FahrStreifenLage("4ÜFS", 4); //$NON-NLS-1$

	/**
	 * Wert <code>5ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS5 = new FahrStreifenLage("5ÜFS", 5); //$NON-NLS-1$

	/**
	 * Wert <code>6ÜFS</code>.
	 */
	public static final FahrStreifenLage UFS6 = new FahrStreifenLage("6ÜFS", 6); //$NON-NLS-1$

	/**
	 * Interner Konstruktor.
	 *
	 * @param name
	 *            der Name des Typen
	 * @param code
	 *            dessen Kode
	 */
	private FahrStreifenLage(final String name, final int code) {
		super(code, name);
		werteBereich.put(code, this);
	}

	/**
	 * Erfragt die Lage des Fahrtreifens links vom Fahstreifen mit dieser Lage
	 * (in Fahrtrichtung).
	 *
	 * @return die Lage des Fahrtreifens links vom Fahstreifen mit dieser Lage
	 *         oder <code>null</code>, wenn die Fahrbahn dort zu Ende ist
	 */
	public FahrStreifenLage getLinksVonHier() {
		FahrStreifenLage ergebnis = null;

		if (equals(HFS)) {
			ergebnis = UFS1;
		} else if (equals(UFS1)) {
			ergebnis = UFS2;
		} else if (equals(UFS2)) {
			ergebnis = UFS3;
		} else if (equals(UFS3)) {
			ergebnis = UFS4;
		} else if (equals(UFS4)) {
			ergebnis = UFS5;
		} else if (equals(UFS5)) {
			ergebnis = UFS6;
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Lage des Fahrtreifens rechts vom Fahstreifen mit dieser Lage
	 * (in Fahrtrichtung).
	 *
	 * @return die Lage des Fahrtreifens rechts vom Fahstreifen mit dieser Lage
	 *         oder <code>null</code>, wenn die Fahrbahn dort zu Ende ist
	 */
	public FahrStreifenLage getRechtsVonHier() {
		FahrStreifenLage ergebnis = null;

		if (equals(UFS1)) {
			ergebnis = HFS;
		} else if (equals(UFS2)) {
			ergebnis = UFS1;
		} else if (equals(UFS3)) {
			ergebnis = UFS2;
		} else if (equals(UFS4)) {
			ergebnis = UFS3;
		} else if (equals(UFS5)) {
			ergebnis = UFS4;
		} else if (equals(UFS6)) {
			ergebnis = UFS5;
		}

		return ergebnis;
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 *
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code
	 */
	public static FahrStreifenLage getZustand(final int code) {
		return werteBereich.get(code);
	}
}
