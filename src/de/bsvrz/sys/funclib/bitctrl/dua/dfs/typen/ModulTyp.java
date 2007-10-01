/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x 
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * �ber diese Klasse werden alle im DAV-Enumerationstyp
 * <code>att.modulTyp</code> beschriebenen Werte zur
 * Verf�gung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class ModulTyp
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, ModulTyp> WERTE_BEREICH = 
						new HashMap<Integer, ModulTyp>();

	/**
	 * Alle wirklichen Enumerationswerte
	 */	
	public static final ModulTyp PL_PRUEFUNG_FORMAL = 
		new ModulTyp("PlPr�fungFormal", 1); //$NON-NLS-1$
	
	public static final ModulTyp PL_PRUEFUNG_LOGISCH_UFD = 
		new ModulTyp("PlPr�fungLogischUFD", 2); //$NON-NLS-1$

	public static final ModulTyp PL_PRUEFUNG_LOGISCH_WZG = 
		new ModulTyp("PlPr�fungLogischWZG", 3); //$NON-NLS-1$
	
	public static final ModulTyp MESSWERTERSETZUNG_LVE = 
		new ModulTyp("PlPr�fungMesswertErsetzungLVE", 4); //$NON-NLS-1$

	public static final ModulTyp MESSWERTERSETZUNG_UFD = 
		new ModulTyp("PlPr�fungMesswertErsetzungUFD", 5); //$NON-NLS-1$

	public static final ModulTyp PL_PRUEFUNG_LANGZEIT_UFD = 
		new ModulTyp("PlPr�fungLangZeitUFD", 6); //$NON-NLS-1$

	
	/**
	 * Interner Konstruktor
	 * 
	 * @param name der Name des Zustandes
	 * @param code der Kode
	 */
	private ModulTyp(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem �bergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final ModulTyp getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
}

