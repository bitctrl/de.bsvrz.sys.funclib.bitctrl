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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.ufdsFahrBahnOberFlächenZustand</code> beschriebenen Werte zur
 * Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 **/
public final class UfdsFahrBahnOberFlaechenZustand extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, UfdsFahrBahnOberFlaechenZustand> werteBereich = new HashMap<Integer, UfdsFahrBahnOberFlaechenZustand>();

	/**
	 * Wert <code>nicht ermittelbar/fehlerhaft</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand NICHT_ERMITTELBAR_FEHLERHAFT = new UfdsFahrBahnOberFlaechenZustand(
			"nicht ermittelbar/fehlerhaft", -3); //$NON-NLS-1$

	/**
	 * Wert <code>fehlerhaft</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand FEHLERHAFT = new UfdsFahrBahnOberFlaechenZustand(
			"fehlerhaft", -2); //$NON-NLS-1$

	/**
	 * Wert <code>nicht ermittelbar</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand NICHT_ERMITTELBAR = new UfdsFahrBahnOberFlaechenZustand(
			"nicht ermittelbar", -1); //$NON-NLS-1$

	/**
	 * Wert <code>trocken</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand TROCKEN = new UfdsFahrBahnOberFlaechenZustand(
			"trocken", 0); //$NON-NLS-1$

	/**
	 * Wert <code>feucht</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand FEUCHT = new UfdsFahrBahnOberFlaechenZustand(
			"feucht", 1); //$NON-NLS-1$

	/**
	 * Wert <code>nass</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand NASS = new UfdsFahrBahnOberFlaechenZustand(
			"nass", 32); //$NON-NLS-1$

	/**
	 * Wert <code>gefrorenes Wasser</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand GEFRORENES_WASSER = new UfdsFahrBahnOberFlaechenZustand(
			"gefrorenes Wasser", 64); //$NON-NLS-1$

	/**
	 * Wert <code>Schnee/Schneematsch</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand SCHNEE_SCHNEEMATSCH = new UfdsFahrBahnOberFlaechenZustand(
			"Schnee/Schneematsch", 65); //$NON-NLS-1$

	/**
	 * Wert <code>Eis</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand EIS = new UfdsFahrBahnOberFlaechenZustand(
			"Eis", 66); //$NON-NLS-1$	

	/**
	 * Wert <code>Raureif</code>.
	 */
	public static final UfdsFahrBahnOberFlaechenZustand RAUREIF = new UfdsFahrBahnOberFlaechenZustand(
			"Raureif", 67); //$NON-NLS-1$

	/**
	 * Interner Konstruktor.
	 * 
	 * @param name der Name des Typen
	 * @param code dessen Kode
	 */
	private UfdsFahrBahnOberFlaechenZustand(String name, int code) {
		super(code, name);
		werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code.
	 *
	 * @param code der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code.
	 */
	public static UfdsFahrBahnOberFlaechenZustand getZustand(int code) {
		return werteBereich.get(code);
	}

}
