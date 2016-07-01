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

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.io.Serializable;

/**
 * @author peuker
 *
 */
public abstract class AbstractDavZustand
		implements Serializable, Comparable<AbstractDavZustand> {

	/**
	 *
	 */
	private final int code;

	/**
	 *
	 */
	private final String name;

	/**
	 * @param code
	 *            der Code
	 * @param name
	 *            die Bezeichnung
	 */
	public AbstractDavZustand(final int code, final String name) {
		super();
		this.code = code;
		this.name = name;
	}

	/**
	 * @return code
	 */
	public final int getCode() {
		return code;
	}

	/**
	 * @return name
	 */
	@Override
	public final String toString() {
		return name;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean result = false;

		if ((obj != null) && (obj instanceof AbstractDavZustand)) {
			final AbstractDavZustand that = (AbstractDavZustand) obj;
			result = getCode() == that.getCode();
		}

		return result;
	}

	@Override
	public int compareTo(final AbstractDavZustand o) {
		return ((Integer) code).compareTo(o.getCode());
	}
}
