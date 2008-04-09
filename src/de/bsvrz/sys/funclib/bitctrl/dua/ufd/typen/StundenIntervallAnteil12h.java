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

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Korrespondiert mit <code>att.stundenIntervallAnteile12h</code>.
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public final class StundenIntervallAnteil12h
extends AbstractDavZustand {
	
	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, StundenIntervallAnteil12h> werteBereich = 
						new HashMap<Integer, StundenIntervallAnteil12h>();

	/**
	 * Alle wirklichen Enumerationswerte
	 * 
	 * Wert: STUNDEN_1.
	 */	
	public static final StundenIntervallAnteil12h STUNDEN_1 = 
		new StundenIntervallAnteil12h("1 Stunde", 1, Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	/**
	 * Wert: STUNDEN_2.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_2 = 
		new StundenIntervallAnteil12h("2 Stunden", 2, 2 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$

	/**
	 * Wert: STUNDEN_3.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_3 = 
		new StundenIntervallAnteil12h("3 Stunden", 3, 3 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	/**
	 * Wert: STUNDEN_4.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_4 = 
		new StundenIntervallAnteil12h("4 Stunden", 4, 4 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	/**
	 * Wert: STUNDEN_6.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_6 = 
		new StundenIntervallAnteil12h("6 Stunden", 6, 6 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	/**
	 * Wert: STUNDEN_8.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_8 = 
		new StundenIntervallAnteil12h("8 Stunden", 8, 8 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$
	
	/**
	 * Wert: STUNDEN_12.
	 */
	public static final StundenIntervallAnteil12h STUNDEN_12 = 
		new StundenIntervallAnteil12h("12 Stunden", 12, 12 * Constants.MILLIS_PER_HOUR); //$NON-NLS-1$

	
	/**
	 * der hier definierte Zeitbereich in Millisekunden.
	 */
	private long millis = -1;
	
	
	/**
	 * Interner Konstruktor.
	 * 
	 * @param name der Name des Zustandes
	 * @param code der Kode
	 * @param millis der hier definierte Zeitbereich in Millisekunden
	 */
	private StundenIntervallAnteil12h(String name, int code, long millis) {
		super(code, name);
		this.millis = millis;
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
	public static StundenIntervallAnteil12h getZustand(int code) {
		return werteBereich.get(code);
	}
	
	
	/**
	 * Erfragt den hier definierten Zeitbereich in Millisekunden.
	 * 
	 * @return der hier definierte Zeitbereich in Millisekunden
	 */
	public long getMillis() {
		return this.millis;
	}

}
