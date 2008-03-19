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
package de.bsvrz.sys.funclib.bitctrl.dua.intpuf;

/**
 * Schnittstelle zu einem Datum, das in einem Intervallpuffer 
 * gespeichert werden soll
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public interface IIntervallPufferElement<T extends IIntervallDatum<T>>{

	/**
	 * Erfragt den Anfang des Intervalls, fuer das dieses Datum gilt
	 * 
	 * @return den Anfang des Intervalls, fuer das dieses Datum gilt
	 */
	public long getIntervallStart();
	
	
	/**
	 * Erfragt das Ende des Intervalls, fuer das dieses Datum gilt
	 * 
	 * @return das Ende des Intervalls, fuer das dieses Datum gilt
	 */
	public long getIntervallEnde();
	
	
	/**
	 * Erfragt den Inhalt
	 * 
	 * @return der Inhalt
	 */
	public T getInhalt();
	
}
