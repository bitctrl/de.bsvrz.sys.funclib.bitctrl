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

import java.lang.reflect.Method;

import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Allgemeine Klasse für die Beschreibung von Objekten, die <b>nur</b> Daten halten,
 * auf welche über Getter-Methoden (<b>ohne Argumente</b>) zugegriffen werden kann.
 * (z.B. Attributgruppeninhalte)
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class AllgemeinerDatenContainer {
	
	
	/**
	 * Vergleicht dieses Objekt mit dem übergebenen Objekt. Die beiden Objekte
	 * sind dann gleich, wenn sie vom selben Typ sind und wenn alle Getter-Methoden
	 * die gleichen Werte zurückliefern.
	 * 
	 * @param that ein anderes Objekt
	 * @return ob die beiden Objekte inhaltlich gleich sind
	 */
	@Override
	public boolean equals(Object that) {
		if(that != null){
			if(that.getClass().equals(this.getClass())){
				for(Method method:this.getClass().getMethods()){
					if(method.getName().startsWith("get")){  //$NON-NLS-1$
						try {
							Object thisInhalt = method.invoke(this, new Object[0]);
							Object thatInhalt = method.invoke(that, new Object[0]);
							if(!thisInhalt.equals(thatInhalt)){
								return false;
							}
						} catch (Exception ex){
							return false;
						}
					}
				}
				return true;
			}
		}
				
		return false;
	}
	

	/**
	 * Erfragt eine Zeichenkette, welche die aktuellen Werte aller über Getter-Methoden
	 * zugänglichen Member-Variable enthält
	 * 
	 * @return eine Inhaltsangabe dieses Objektes
	 */
	@Override
	public String toString() {
		String s = Konstante.LEERSTRING;
		
		for(Method methode:this.getClass().getMethods()){
			if(methode.getName().startsWith("get") &&  //$NON-NLS-1$
			   methode.getDeclaringClass().equals(this.getClass())){
				s += methode.getName().substring(3) + " = ";  //$NON-NLS-1$
				try {
					s += methode.invoke(this, new Object[0]);
				} catch (Exception ex){
					s += "unbekannt";   //$NON-NLS-1$
				}
				s += "\n";  //$NON-NLS-1$
			}else
			if(methode.getName().startsWith("is") &&  //$NON-NLS-1$
			   methode.getDeclaringClass().equals(this.getClass())){
				s += methode.getName().substring(2) + " = ";  //$NON-NLS-1$
				try {
					s += methode.invoke(this, new Object[0]);
				} catch (Exception ex){
					s += "unbekannt";   //$NON-NLS-1$
				}
				s += "\n";  //$NON-NLS-1${
			}
		}
		
		return s;
	}
	
}
