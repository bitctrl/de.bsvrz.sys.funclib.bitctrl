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
package de.bsvrz.sys.funclib.bitctrl.dua.lve;

/**
 * Messquerschnittbestandteil eines virtuellen Messquerschnittes
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class MQBestandteil {
	
	/**
	 * ein Messquerschnitt
	 */
	private MessQuerschnitt mq = null;
	
	/**
	 * der Anteil, mit dem dieser Messquerschnitt in
	 * den virtuellen Messquerschnitt eingeht
	 */
	private double anteil = 1.0;
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param mq ein Messquerschnitt
	 * @param anteil der Anteil, mit dem dieser Messquerschnitt in
	 * den virtuellen Messquerschnitt eingeht
	 */
	public MQBestandteil(MessQuerschnitt mq, final double anteil){
		this.mq = mq;
		this.anteil = anteil;
	}
	

	/**
	 * Erfragt den Anteil, mit dem dieser Messquerschnitt in
	 * den virtuellen Messquerschnitt eingeht
	 * 
	 * @return anteil der Anteil, mit dem dieser Messquerschnitt in
	 * den virtuellen Messquerschnitt eingeht
	 */
	public final double getAnteil() {
		return anteil;
	}

	
	/**
	 * Erfragt den Messquerschnitt
	 * 
	 * @return mq der Messquerschnitt
	 */
	public final MessQuerschnitt getMq() {
		return mq;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.mq + ", Anteil: " + this.anteil; //$NON-NLS-1$
	}
	
}
