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

package de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen;

/**
 * Schnittstelle zu einem Kontrollprozess.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IKontrollProzessListener<T> {

	/**
	 * Wird von einem assoziierten Kontrollprozess zur Kontrollzeit
	 * aufgerufen
	 *
	 * @param information ein Objekt mit einer bestimmten Information
	 * zu diesem Ereignis. Dieses Objekt muss dem zugeh�rigen Kontrollprozess
	 * vorher �bergeben worden sein
	 */
	public void trigger(T information);
	
}
