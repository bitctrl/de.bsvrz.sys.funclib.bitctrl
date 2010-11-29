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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen;

/**
 * Schnittstelle zu einem Kontrollprozess.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <T> eine Information zur Uebergabe an den Listener
 * 
 * @version $Id: IKontrollProzessListener.java 8054 2008-04-09 15:11:59Z tfelder $
 */
public interface IKontrollProzessListener<T> {

	/**
	 * Wird von einem assoziierten Kontrollprozess zur Kontrollzeit
	 * aufgerufen.
	 *
	 * @param information ein Objekt mit einer bestimmten Information
	 * zu diesem Ereignis. Dieses Objekt muss dem zugeh�rigen Kontrollprozess
	 * vorher �bergeben worden sein
	 */
	void trigger(T information);

}