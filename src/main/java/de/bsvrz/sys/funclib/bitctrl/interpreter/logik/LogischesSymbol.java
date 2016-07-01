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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter.logik;

import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.interpreter.Ausdruck;
import de.bsvrz.sys.funclib.bitctrl.interpreter.Kontext;
import de.bsvrz.sys.funclib.bitctrl.interpreter.Variable;

/**
 * Ein boolesches Terminalsymbol (Variable).
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class LogischesSymbol implements Variable {

	/**
	 * Der Name der Variable im Kontext.
	 */
	private final String name;

	/**
	 * Konstruiert ein Terminalsymbol mit dem angegebenen Namen.
	 *
	 * @param name
	 *            Name der Variable im Kontext
	 */
	public LogischesSymbol(final String name) {
		Kontext.pruefeName(name);
		this.name = name;
	}

	/**
	 * Gibt immer {@code null} zur&uuml;ck, da dies ein Terminalsymbol ist.
	 */
	@Override
	public List<Ausdruck> getNachfolger() {
		return null;
	}

	/**
	 * Nennt den Namen des Terminalsymbols.
	 *
	 * @return Variablenname
	 */
	@Override
	public String getName() {
		assert(name != null) && (name != "");

		return name;
	}

	@Override
	public Object interpret(final Kontext kontext) {
		if (kontext.enthaelt(getName())) {
			return kontext.get(getName(), LogischerWert.class);
		}

		return null;
	}

	/**
	 * Die Funktion gibt den Namen des Symbols aus.
	 */
	@Override
	public String toString() {
		return getName();
	}

}
