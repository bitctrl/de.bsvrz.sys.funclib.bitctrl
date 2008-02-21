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

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Korrespondiert mit <code>att.stundenIntervallAnteile12h</code>
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class StundenIntervallAnteil12h
extends AbstractDavZustand{
	
	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, StundenIntervallAnteil12h> WERTE_BEREICH = 
						new HashMap<Integer, StundenIntervallAnteil12h>();

	/**
	 * Alle wirklichen Enumerationswerte
	 */	
	public static final StundenIntervallAnteil12h STUNDEN_1 = 
		new StundenIntervallAnteil12h("1 Stunde", 1, Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	public static final StundenIntervallAnteil12h STUNDEN_2 = 
		new StundenIntervallAnteil12h("2 Stunden", 2, 2* Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	public static final StundenIntervallAnteil12h STUNDEN_3 = 
		new StundenIntervallAnteil12h("3 Stunden", 3, 3 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	public static final StundenIntervallAnteil12h STUNDEN_4 = 
		new StundenIntervallAnteil12h("4 Stunden", 4, 4 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	public static final StundenIntervallAnteil12h STUNDEN_6 = 
		new StundenIntervallAnteil12h("6 Stunden", 6, 6 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	public static final StundenIntervallAnteil12h STUNDEN_8 = 
		new StundenIntervallAnteil12h("8 Stunden", 8, 8 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	public static final StundenIntervallAnteil12h STUNDEN_12 = 
		new StundenIntervallAnteil12h("12 Stunden", 12, 12 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$

	/**
	 * der hier definierte Zeitbereich in Millisekunden
	 */
	private long millis = -1;
	
	
	/**
	 * Interner Konstruktor
	 * 
	 * @param name der Name des Zustandes
	 * @param code der Kode
	 * @param millis der hier definierte Zeitbereich in Millisekunden
	 */
	private StundenIntervallAnteil12h(String name, int code, long millis){
		super(code, name);
		this.millis = millis;
		WERTE_BEREICH.put(code, this);
	}
	
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final StundenIntervallAnteil12h getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
	
	
	/**
	 * Erfragt den hier definierten Zeitbereich in Millisekunden
	 * 
	 * @return der hier definierte Zeitbereich in Millisekunden
	 */
	public final long getMillis(){
		return this.millis;
	}

}
