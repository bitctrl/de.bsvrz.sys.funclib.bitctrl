/*
 * Interpreter von logischen Ausdrücken
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

package de.bsvrz.sys.funclib.bitctrl.interpreter.logik;

import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.interpreter.Ausdruck;
import de.bsvrz.sys.funclib.bitctrl.interpreter.Kontext;
import de.bsvrz.sys.funclib.bitctrl.interpreter.Literal;

/**
 * Ein boolesches Terminalsymbol (Literal)
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class LogischesLiteral implements Literal {

	/**
	 * Repr&auml;sentiert den logischen Wert des Literals
	 */
	private final LogischerWert wert;

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Wert
	 * 
	 * @param wert
	 *            Wert
	 */
	public LogischesLiteral(boolean wert) {
		this.wert = new LogischerWert(wert);
	}

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Wert
	 * 
	 * @param wert
	 *            Wert
	 */
	public LogischesLiteral(float wert) {
		this.wert = new LogischerWert(wert);
	}

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Wert
	 * 
	 * @param wert
	 *            Wert
	 */
	public LogischesLiteral(LogischerWert wert) {
		if (wert.isBoolWert()) {
			this.wert = new LogischerWert(wert.getBoolWert());
		} else {
			this.wert = new LogischerWert(wert.getZugehoerigkeit());
		}
	}

	/**
	 * Nennt den Wert des Terminalsymbols
	 * 
	 * @return Wert des Literal
	 */
	public LogischerWert getWert() {
		return wert;
	}

	/**
	 * Gibt immer {@code null} zur&uuml;ck, da dies ein Terminalsymbol ist.
	 * <p>
	 * {@inheritDoc}
	 */
	public List<Ausdruck> getNachfolger() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
	public Object interpret(Kontext kontext) {
		return wert;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getWert().toString();
	}

}
