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

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Exceptions, die geworfen wird, wenn f�r die Ausf�hrung einer Operation kein
 * g�ltiger Handler gefunden werden konnte.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id: HandlerNotFoundException.java 6835 2008-02-21 13:04:58Z peuker
 *          $
 */
public class HandlerNotFoundException extends InterpreterException {

	/**
	 * Versions-ID f�r die Serialisierung des Objekts.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 *
	 * @param string
	 *            der Meldungstext
	 */
	public HandlerNotFoundException(final String string) {
		super(string);
	}
}
