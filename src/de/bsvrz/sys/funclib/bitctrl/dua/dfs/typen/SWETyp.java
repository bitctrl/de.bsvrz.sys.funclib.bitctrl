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
 * <code>att.sweTyp</code> beschriebenen Werte zur
 * Verf�gung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class SWETyp
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, SWETyp> WERTE_BEREICH = 
						new HashMap<Integer, SWETyp>();

	/**
	 * Alle Werte
	 */
	public static final SWETyp PL_PRUEFUNG_FORMAL = 
		new SWETyp("SWE_PL_Pr�fung_formal", 1); //$NON-NLS-1$
	
	public static final SWETyp PL_PRUEFUNG_LOGISCH_LVE = 
		new SWETyp("SWE_PL_Pr�fung_logisch_LVE", 2); //$NON-NLS-1$
	
	public static final SWETyp SWE_PL_PRUEFUNG_LOGISCH_UFD = 
		new SWETyp("SWE_PL_Pr�fung_logisch_UFD", 3); //$NON-NLS-1$
	
	public static final SWETyp SWE_PL_PRUEFUNG_LOGISCH_WZG = 
		new SWETyp("SWE_PL_Pr�fung_logisch_WZG", 4); //$NON-NLS-1$
	
	public static final SWETyp SWE_MESSWERTERSETZUNG_LVE = 
		new SWETyp("SWE_Messwertersetzung_LVE", 5); //$NON-NLS-1$
	
	public static final SWETyp SWE_ABFRAGE_PUFFERDATEN = 
		new SWETyp("SWE_Abfrage_Pufferdaten", 6); //$NON-NLS-1$
	
	public static final SWETyp SWE_DATENAUFBEREITUNG_LVE = 
		new SWETyp("SWE_Datenaufbereitung_LVE", 7); //$NON-NLS-1$
	
	public static final SWETyp SWE_DATENAUFBEREITUNG_UFD = 
		new SWETyp("SWE_Datenaufbereitung_UFD", 8); //$NON-NLS-1$
	
	public static final SWETyp SWE_AGGREGATION_LVE = 
		new SWETyp("SWE_Aggregation_LVE", 9); //$NON-NLS-1$
	
	public static final SWETyp SWE_ERGAENZUNG_BASt = 
		new SWETyp("SWE_Erg�nzung_BASt", 10); //$NON-NLS-1$
	
	public static final SWETyp SWE_GUETEBERECHNUNG = 
		new SWETyp("SWE_G�teberechnung", 11); //$NON-NLS-1$
	
	public static final SWETyp SWE_MESSWERTERSETZUNG_UFD = 
		new SWETyp("SWE_Messwertersetzung_UFD", 12); //$NON-NLS-1$
	
	public static final SWETyp SWE_PL_PRUEFUNG_LANGZEIT_UFD = 
		new SWETyp("SWE_PL_Pr�fung_Langzeit_UFD", 13); //$NON-NLS-1$
	
	public static final SWETyp SWE_GLAETTEWARNUNG_UND_PROGNOSE = 
		new SWETyp("SWE_DuA_Gl�ttewarnung_und_Prognose", 14); //$NON-NLS-1$
	
	
	/**
	 * Interner Konstruktor
	 * 
	 * @param name der Name des Zustandes
	 * @param code der Kode
	 */
	private SWETyp(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem �bergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final SWETyp getZustand(int code){
		return WERTE_BEREICH.get(code);
	}

}
