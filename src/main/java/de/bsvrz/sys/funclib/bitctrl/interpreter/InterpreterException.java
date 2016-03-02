/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Basis-Implementierung f&uuml;r alle Runtime-Exceptions, die vom Interpreter
 * aktiv geworfen werden k&ouml;nnen.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
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
	 * Konstruktor mit Angabe der Meldung, die die Exception n&auml;her
	 * beschreibt.
	 *
	 * @param meldung
	 *            der Meldungstext
	 */
	public InterpreterException(final String meldung) {
		super(meldung);
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
	public InterpreterException(final String meldung, final Throwable cause) {
		super(meldung, cause);
	}

	/**
	 * Konstruktor mit Angabe der urspr&uuml;nglichen Exception, die diese
	 * ausgel&ouml;st hat.
	 *
	 * @param cause
	 *            die Ursache
	 */
	public InterpreterException(final Throwable cause) {
		super(cause);
	}

}
