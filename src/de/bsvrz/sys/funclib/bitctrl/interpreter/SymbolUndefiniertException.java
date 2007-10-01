/*
 * Interpreter, allgemeine Struktur zum Auswerten von Ausdr�cken
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Wird ausgel&ouml;st, wenn die Variable zu einem Symbolnamen im Kontext nicht
 * existiert
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class SymbolUndefiniertException extends InterpreterException {

	/**
	 * Versions-ID f&uuml;r die Serialisierung
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Standardkonstruktor
	 */
	public SymbolUndefiniertException() {
		super();
	}

	/**
	 * Konstruktor mit Angabe der Meldung, die die Exception n&auml;her
	 * beschreibt.
	 * 
	 * @param meldung
	 *            Der Meldungstext
	 */
	public SymbolUndefiniertException(String meldung) {
		super(meldung);
	}

	/**
	 * Konstruktor mit Angabe der urspr&uuml;nglichen Exception, die diese
	 * ausgel&ouml;st hat und des Meldungstextes, der die Exception n&auml;her
	 * beschreibt.
	 * 
	 * @param meldung
	 *            Der Text der Meldung
	 * @param ursache
	 *            Die Ursache
	 */
	public SymbolUndefiniertException(String meldung, Throwable ursache) {
		super(meldung, ursache);
	}

	/**
	 * Konstruktor mit Angabe der urspr&uuml;nglichen Exception, die diese
	 * ausgel&ouml;st hat.
	 * 
	 * @param ursache
	 *            Die Ursache
	 */
	public SymbolUndefiniertException(Throwable ursache) {
		super(ursache);
	}

}
