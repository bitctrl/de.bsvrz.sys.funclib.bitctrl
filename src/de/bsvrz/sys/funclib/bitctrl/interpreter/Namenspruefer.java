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
 * Schnittstelle, welchen einen Mechanismus zum Testen eines Strings gegen eine
 * Bedingung besreitstellt. Durch unterschiedliche Implementationen kann die
 * Bedingung variiert werden.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public interface Namenspruefer {

	/**
	 * Pr&uuml;ft einen String gegen eine Bedingung
	 * 
	 * @param name
	 *            Text
	 * @return {@code true}, wenn der Text die Bedingung erf&uuml;llt
	 */
	boolean pruefe(String name);

}
