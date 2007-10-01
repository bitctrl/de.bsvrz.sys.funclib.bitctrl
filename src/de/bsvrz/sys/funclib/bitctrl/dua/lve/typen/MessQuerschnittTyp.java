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
package de.bsvrz.sys.funclib.bitctrl.dua.lve.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.messQuerschnittTyp</code> beschriebenen Werte zur
 * Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class MessQuerschnittTyp
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, MessQuerschnittTyp> WERTE_BEREICH = 
						new HashMap<Integer, MessQuerschnittTyp>();

	/**
	 * Wert <code>SonstigeFahrbahn</code>
	 */	
	public static final MessQuerschnittTyp SONSTIGE = 
		new MessQuerschnittTyp("SonstigeFahrbahn", 0); //$NON-NLS-1$

	/**
	 * Wert <code>HauptFahrbahn</code>
	 */	
	public static final MessQuerschnittTyp HAUPT = 
		new MessQuerschnittTyp("HauptFahrbahn", 1); //$NON-NLS-1$
	
	/**
	 * Wert <code>NebenFahrbahn</code>
	 */	
	public static final MessQuerschnittTyp NEBEN = 
		new MessQuerschnittTyp("NebenFahrbahn", 2); //$NON-NLS-1$
	
	/**
	 * Wert <code>Einfahrt</code>
	 */	
	public static final MessQuerschnittTyp EINFAHRT = 
		new MessQuerschnittTyp("Einfahrt", 3); //$NON-NLS-1$

	/**
	 * Wert <code>Ausfahrt</code>
	 */	
	public static final MessQuerschnittTyp AUSFAHRT = 
		new MessQuerschnittTyp("Ausfahrt", 4); //$NON-NLS-1$

	
	/**
	 * Interner Konstruktor
	 * 
	 * @param name der Name des Typen
	 * @param code dessen Kode
	 */
	private MessQuerschnittTyp(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final MessQuerschnittTyp getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
}

