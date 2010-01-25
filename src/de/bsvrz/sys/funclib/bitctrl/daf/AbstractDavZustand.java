/**
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
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weiﬂenfelser Straﬂe 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.io.Serializable;

/**
 * @author peuker
 *
 */
public abstract class AbstractDavZustand implements Serializable,
		Comparable<AbstractDavZustand> {

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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		if (obj != null && obj instanceof AbstractDavZustand) {
			AbstractDavZustand that = (AbstractDavZustand) obj;
			result = this.getCode() == that.getCode();
		}

		return result;
	}

	/**
	 * {@inheritDoc}.
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(final AbstractDavZustand o) {
		return ((Integer) code).compareTo(o.getCode());
	}
}
