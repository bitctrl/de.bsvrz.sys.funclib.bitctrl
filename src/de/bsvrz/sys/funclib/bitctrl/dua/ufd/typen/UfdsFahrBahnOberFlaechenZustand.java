/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x
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
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
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
 **/
public class UfdsFahrBahnOberFlaechenZustand
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, UfdsFahrBahnOberFlaechenZustand> WERTE_BEREICH = 
		new HashMap<Integer, UfdsFahrBahnOberFlaechenZustand>();
	
	/**
	 * Wert <code>nicht ermittelbar/fehlerhaft</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand NICHT_ERMITTELBAR_FEHLERHAFT = 
		new UfdsFahrBahnOberFlaechenZustand("nicht ermittelbar/fehlerhaft", -3); //$NON-NLS-1$

	/**
	 * Wert <code>fehlerhaft</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand FEHLERHAFT = 
		new UfdsFahrBahnOberFlaechenZustand("fehlerhaft", -2); //$NON-NLS-1$

	/**
	 * Wert <code>nicht ermittelbar</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand NICHT_ERMITTELBAR = 
		new UfdsFahrBahnOberFlaechenZustand("nicht ermittelbar", -1); //$NON-NLS-1$
	
	/**
	 * Wert <code>trocken</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand TROCKEN = 
		new UfdsFahrBahnOberFlaechenZustand("trocken", 0); //$NON-NLS-1$
	
	/**
	 * Wert <code>feucht</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand FEUCHT = 
		new UfdsFahrBahnOberFlaechenZustand("feucht", 1); //$NON-NLS-1$

	/**
	 * Wert <code>nass</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand NASS = 
		new UfdsFahrBahnOberFlaechenZustand("nass", 32); //$NON-NLS-1$

	/**
	 * Wert <code>gefrorenes Wasser</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand GEFRORENES_WASSER = 
		new UfdsFahrBahnOberFlaechenZustand("gefrorenes Wasser", 64); //$NON-NLS-1$

	/**
	 * Wert <code>Schnee/Schneematsch</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand SCHNEE_SCHNEEMATSCH = 
		new UfdsFahrBahnOberFlaechenZustand("Schnee/Schneematsch", 65); //$NON-NLS-1$

	/**
	 * Wert <code>Eis</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand EIS = 
		new UfdsFahrBahnOberFlaechenZustand("Eis", 66); //$NON-NLS-1$	
	
	/**
	 * Wert <code>Raureif</code>
	 */	
	public static final UfdsFahrBahnOberFlaechenZustand RAUREIF = 
		new UfdsFahrBahnOberFlaechenZustand("Raureif", 67); //$NON-NLS-1$
	
	
	/**
	 * Interner Konstruktor
	 * 
	 * @param name der Name des Typen
	 * @param code dessen Kode
	 */
	private UfdsFahrBahnOberFlaechenZustand(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}


	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final UfdsFahrBahnOberFlaechenZustand getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
	
}

