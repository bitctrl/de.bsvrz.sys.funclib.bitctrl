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

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Standardimplementation des Namenspr&uuml;fers.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class StandardNamenspruefer implements Namenspruefer {

	/**
	 * Pr&uuml;ft ob der Name nicht {@code null} ist und nicht nur aus
	 * Leerzeichen besteht. Leerzeichen am Anfang und Ende werden ignoriert.
	 */
	@Override
	public boolean pruefe(final String name) {
		if (name == null) {
			return false;
		}

		final String s = name.trim();
		if (s.length() == 0) {
			return false;
			// for (char z : s.toCharArray())
			// if (Character.isWhitespace(z))
			// return false;
		}

		return true;
	}

}
