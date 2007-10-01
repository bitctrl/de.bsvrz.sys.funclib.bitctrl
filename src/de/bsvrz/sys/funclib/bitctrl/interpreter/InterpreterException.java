/*
 * Interpreter, allgemeine Struktur zum Auswerten von Ausdrücken
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
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Basis-Implementierung f&uuml;r alle Runtime-Exceptions, die vom Interpreter
 * aktiv geworfen werden k&ouml;nnen.
 *
 * @author Peuker
 * @version $Id: InterpreterException.java 164 2007-02-26 10:36:54Z Schumann $
 */
public class InterpreterException extends RuntimeException {

	/**
	 * Versions-ID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standardkonstruktor.
	 */
	public InterpreterException() {
		super();
	}

	/**
	 * Konstruktor mit Angabe der urspr&uuml;nglichen Exception, die diese
	 * ausgel&ouml;st hat und des Meldungstextes, der die Exception n&auml;her
	 * beschreibt.
	 *
	 * @param meldung
	 *            der Text der Meldung
	 * @param cause
	 *            die Ursache
	 */
	public InterpreterException(String meldung, Throwable cause) {
		super(meldung, cause);
	}

	/**
	 * Konstruktor mit Angabe der urspr&uuml;nglichen Exception, die diese
	 * ausgel&ouml;st hat.
	 *
	 * @param cause
	 *            die Ursache
	 */
	public InterpreterException(Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor mit Angabe der Meldung, die die Exception n&auml;her
	 * beschreibt.
	 *
	 * @param meldung
	 *            der Meldungstext
	 */
	public InterpreterException(String meldung) {
		super(meldung);
	}

}
