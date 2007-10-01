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

package de.bsvrz.sys.funclib.bitctrl.dua.adapter;

import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltungMitGuete;

/**
 * Adapterklasse für Verwaltungsmodule, die eine Messwertersetzung
 * durchführen und dabei die Guete der Messwerte manipulieren.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktVerwaltungsAdapterMitGuete
extends	AbstraktVerwaltungsAdapter
implements IVerwaltungMitGuete{
	
	/**
	 * benutzter Guetefaktor
	 */
	private double gueteFaktor = -1;

	
	/**
	 * {@inheritDoc}<br>
	 */
	@Override
	protected void initialisiere()
	throws DUAInitialisierungsException {
		final String gueteFaktorStr = DUAUtensilien.getArgument("gueteFaktor", komArgumente); //$NON-NLS-1$
		if(gueteFaktorStr != null){
			double gueteFaktorDummy = -1;
			try{
				gueteFaktorDummy = Double.parseDouble(gueteFaktorStr);
			}catch(Exception e){
				LOGGER.warning("Der Guetefaktor konnte nicht ausgelesen werden. Standard: " + //$NON-NLS-1$
						this.getStandardGueteFaktor(), e);
			}
			
			if(gueteFaktorDummy <= 1.0 && gueteFaktorDummy >= 0.0){
				this.gueteFaktor = gueteFaktorDummy;
			}else{
				LOGGER.warning("Der uebergebene Guetefaktor ist ausserhalb des gueltigen Bereichs. Standard: " + //$NON-NLS-1$
						this.getStandardGueteFaktor());
			}
		}
		
		if(this.gueteFaktor < 0.0){
			this.gueteFaktor = this.getStandardGueteFaktor();
		}		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public final double getGueteFaktor(){
		return this.gueteFaktor;
	}
	

	/**
	 * Erfragt den Standard-Guetefaktor, welcher in dem Verwaltungsmodul
	 * verwendet wird, wenn kein Guetefaktor über die Kommandozeilenargumente
	 * der Applikation selbst uebergeben wurde
	 * 
	 * @return der Standard-Guetefaktor
	 */
	public abstract double getStandardGueteFaktor();
	
}
