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

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Exceptions, die geworfen wird, wenn für die Ausführung einer Operation kein
 * gültiger Handler gefunden werden konnte.
 *
 * @author peuker
 */
public class HandlerNotFoundException extends InterpreterException {

	/**
	 * Versions-ID für die Serialisierung des Objekts.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 *
	 * @param string
	 *            der Meldungstext
	 */
	public HandlerNotFoundException(String string) {
		super(string);
	}
}
