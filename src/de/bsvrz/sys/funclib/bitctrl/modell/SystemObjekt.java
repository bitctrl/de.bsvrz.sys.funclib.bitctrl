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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Schnittstelle f&uuml;r alle modellierten Systemobjekte. Am wichtigsten ist
 * die Methode {@link #getSystemObject()}, die das gekapselte Systemobjekt des
 * Datenverteilsers zur&uuml;ck gibt. Die anderen sollen lediglich die
 * Schreibweise verk&uuml;rzen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface SystemObjekt {

	/**
	 * Gibt die ID des Systemobjekts zur&uuml;ck.
	 * 
	 * @see SystemObject#getId()
	 * @return Die ID
	 */
	long getId();

	/**
	 * Gibt den Namen des Systemobjekts zur&uuml;ck.
	 * 
	 * @see SystemObject#getName()
	 * @return Der Systemobjektname
	 */
	String getName();

	/**
	 * Gibt die PID des Systemobjekts zur&uuml;ck.
	 * 
	 * @see SystemObject#getPid()
	 * @return Die PID als String
	 */
	String getPid();

	/**
	 * Gibt das gekapselte Systemobjekt des Datenverteilers zur&uuml;ck.
	 * 
	 * @see SystemObject#getId()
	 * @return Das Datenverteilersystemobjekt
	 */
	SystemObject getSystemObject();

	/**
	 * Gibt den Typ des Systemobjekts zur&uuml;ck.
	 * 
	 * @return den Typ
	 */
	SystemObjektTyp getTyp();

}
