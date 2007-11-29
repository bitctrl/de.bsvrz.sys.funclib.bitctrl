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
package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Korrespondiert mit dem Zustandsraum eines Messwertes
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class MesswertZustand
extends AbstractDavZustand{
	
	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, MesswertZustand> WERTE_BEREICH = 
						new HashMap<Integer, MesswertZustand>();
	
	/**
	 * Daten sind nicht ermittelbar (ist KEIN Fehler). Wird gesetzt, wenn der entsprechende Wert
	 * nicht ermittelbar ist und kein Interpolation sinnvoll möglich ist (z.B. ist die
	 * Geschwindigkeit nicht ermittelbar, wenn kein Fahrzeug erfasst wurde).
	 **/
	public static final MesswertZustand NICHT_ERMITTELBAR = 
												new MesswertZustand("nicht ermittelbar", -1); //$NON-NLS-1$

	/**
	 * Daten sind fehlerhaft.
	 * Wird gesetzt, wenn die Daten als fehlerhaft erkannt wurden
	 */
	public static final MesswertZustand FEHLERHAFT = 
												new MesswertZustand("fehlerhaft", -2); //$NON-NLS-1$
	
	/**
	 * Daten nicht ermittelbar, da bereits Basiswerte fehlerhaft. Wird gesetzt, wenn Daten,
	 * die zur Berechnung dieses Werts notwendig sind, bereits als fehlerhaft gekennzeichnet
	 * sind, oder wenn die Berechnung aus anderen Gründen (z.B. Nenner = 0 in der Berechnungsformel)
	 * nicht möglich war.
	 */
	public static final MesswertZustand FEHLERHAFT_BZW_NICHT_ERMITTELBAR = 
												new MesswertZustand("nicht ermittelbar/fehlerhaft", -3); //$NON-NLS-1$
		

	/**
	 * Interner Konstruktor
	 * 
	 * @param code
	 *            der Code
	 * @param name
	 *            die Bezeichnung
	 */
	private MesswertZustand(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final MesswertZustand getZustand(int code){
		return WERTE_BEREICH.get(code);
	}

}
