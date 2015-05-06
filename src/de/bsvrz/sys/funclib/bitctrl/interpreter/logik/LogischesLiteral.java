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
 * Weiﬂenfelser Straﬂe 67
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
 * Ein boolesches Terminalsymbol (Literal).
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: LogischesLiteral.java 5262 2007-12-18 09:33:29Z Schumann $
 */
public class LogischesLiteral implements Literal {

	/**
	 * Repr&auml;sentiert den logischen Wert des Literals.
	 */
	private final LogischerWert wert;

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Wert.
	 *
	 * @param wert
	 *            Wert
	 */
	public LogischesLiteral(final boolean wert) {
		this.wert = new LogischerWert(wert);
	}

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Wert.
	 *
	 * @param wert
	 *            Wert
	 */
	public LogischesLiteral(final Float wert) {
		this.wert = new LogischerWert(wert);
	}

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Wert.
	 *
	 * @param wert
	 *            Wert
	 */
	public LogischesLiteral(final LogischerWert wert) {
		if (wert.isBoolWert()) {
			this.wert = new LogischerWert(wert.getBoolWert());
		} else {
			this.wert = new LogischerWert(wert.getZugehoerigkeit());
		}
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
	 * Nennt den Wert des Terminalsymbols.
	 *
	 * @return Wert des Literal
	 */
	public LogischerWert getWert() {
		return wert;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object interpret(final Kontext kontext) {
		return wert;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getWert().toString();
	}

}
