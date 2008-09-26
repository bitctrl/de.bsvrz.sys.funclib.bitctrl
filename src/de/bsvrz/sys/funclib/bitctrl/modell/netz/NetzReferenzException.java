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
 * Wei&szlig;enfelser Stra&szlig;e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

/**
 * Exception, die bei Ausnahmen und Fehlern der Umwandlung der Netzreferenzen
 * ausgel&ouml;st wird.
 * 
 * @author BitCtrl Systems GmbH, Steffen Gieseler
 * @version  $Id: $
 */

public class NetzReferenzException extends Exception {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception des BisInterface.
	 */
	public NetzReferenzException() {
		super(); //$NON-NLS-1$
	}

	/**
	 * Exception des BisInterface.
	 * 
	 * @param message
	 */
	public NetzReferenzException(final String message) {
		super(message);
	}

	/**
	 * Exception des BisInterface.
	 * 
	 * @param message
	 * @param cause
	 */
	public NetzReferenzException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Exception des BisInterface.
	 * 
	 * @param cause
	 */
	public NetzReferenzException(Throwable cause) {
		super(cause);
	}

}
