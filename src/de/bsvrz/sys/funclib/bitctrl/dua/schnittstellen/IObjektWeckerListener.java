/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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
 * Schnittstelle zu einem Objekt, das von einer Instanz der Klasse 
 * <code>ObjektWecker</code> alarmiert werden k�nnen soll.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id: IObjektWeckerListener.java 8054 2008-04-09 15:11:59Z tfelder $
 */
public interface IObjektWeckerListener {

	/**
	 * Alarmiert ein Objekt.
	 */
	void alarm();
	
}
