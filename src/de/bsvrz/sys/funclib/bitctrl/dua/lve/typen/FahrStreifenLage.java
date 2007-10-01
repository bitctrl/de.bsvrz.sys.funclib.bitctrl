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
 * <code>att.fahrStreifenLage</code> beschriebenen Werte zur
 * Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class FahrStreifenLage  
extends AbstractDavZustand{

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen
	 */
	private static Map<Integer, FahrStreifenLage> WERTE_BEREICH = 
						new HashMap<Integer, FahrStreifenLage>();

	/**
	 * Wert <code>HFS</code>
	 */	
	public static final FahrStreifenLage HFS = 
		new FahrStreifenLage("HFS", 0); //$NON-NLS-1$
	
	/**
	 * Wert <code>1ÜFS</code>
	 */
	public static final FahrStreifenLage UFS1 = 
		new FahrStreifenLage("1ÜFS", 1); //$NON-NLS-1$

	/**
	 * Wert <code>2ÜFS</code>
	 */
	public static final FahrStreifenLage UFS2 = 
		new FahrStreifenLage("2ÜFS", 2); //$NON-NLS-1$
	
	/**
	 * Wert <code>3ÜFS</code>
	 */
	public static final FahrStreifenLage UFS3 = 
		new FahrStreifenLage("3ÜFS", 3); //$NON-NLS-1$
	
	/**
	 * Wert <code>4ÜFS</code>
	 */
	public static final FahrStreifenLage UFS4 = 
		new FahrStreifenLage("4ÜFS", 4); //$NON-NLS-1$
	
	/**
	 * Wert <code>5ÜFS</code>
	 */
	public static final FahrStreifenLage UFS5 = 
		new FahrStreifenLage("5ÜFS", 5); //$NON-NLS-1$
		
	/**
	 * Wert <code>6ÜFS</code>
	 */
	public static final FahrStreifenLage UFS6 = 
		new FahrStreifenLage("6ÜFS", 6); //$NON-NLS-1$
	
	
	/**
	 * Interner Konstruktor
	 * 
	 * @param name der Name des Typen
	 * @param code dessen Kode
	 */
	private FahrStreifenLage(String name, int code){
		super(code, name);
		WERTE_BEREICH.put(code, this);
	}
	
	
	/**
	 * Erfragt die Lage des Fahrtreifens links vom Fahstreifen mit dieser Lage
	 * (in Fahrtrichtung)
	 * 
	 * @return die Lage des Fahrtreifens links vom Fahstreifen mit dieser Lage
	 * oder <code>null</code>, wenn die Fahrbahn dort zu Ende ist
	 */
	public final FahrStreifenLage getLinksVonHier(){
		FahrStreifenLage ergebnis = null;
		
		if(this.equals(HFS)){
			ergebnis = UFS1;
		}else
		if(this.equals(UFS1)){
			ergebnis = UFS2;
		}else
		if(this.equals(UFS2)){
			ergebnis = UFS3;
		}else
		if(this.equals(UFS3)){
			ergebnis = UFS4;
		}else
		if(this.equals(UFS4)){
			ergebnis = UFS5;	
		}else
		if(this.equals(UFS5)){
			ergebnis = UFS6;
		}
		
		return ergebnis;
	}
	
	
	/**
	 * Erfragt die Lage des Fahrtreifens rechts vom Fahstreifen mit dieser Lage
	 * (in Fahrtrichtung)
	 * 
	 * @return die Lage des Fahrtreifens rechts vom Fahstreifen mit dieser Lage
	 * oder <code>null</code>, wenn die Fahrbahn dort zu Ende ist
	 */
	public final FahrStreifenLage getRechtsVonHier(){
		FahrStreifenLage ergebnis = null;
		
		if(this.equals(UFS1)){
			ergebnis = HFS;
		}else
		if(this.equals(UFS2)){
			ergebnis = UFS1;
		}else
		if(this.equals(UFS3)){
			ergebnis = UFS2;
		}else
		if(this.equals(UFS4)){
			ergebnis = UFS3;	
		}else
		if(this.equals(UFS5)){
			ergebnis = UFS4;
		}else			
		if(this.equals(UFS6)){
			ergebnis = UFS5;
		}
		
		return ergebnis;
	}
	
	
	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen 
	 * mit dem übergebenen Code
	 *
	 * @param code der Code des Enumerations-Wertes
	 */
	public static final FahrStreifenLage getZustand(int code){
		return WERTE_BEREICH.get(code);
	}
}

