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
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;


/**
 * Repräsentation einer Variable innerhalb eines Uda-Skripts. Eine Variable
 * stellt einen Namen dar, über den auf Daten innerhalb des aktuellen Kontextes
 * der Ausführung eines Uda-Skriptes zugegriffen werden kann.
 *
 * @author Peuker
 *
 */
public interface Variable extends Ausdruck {


	/**
	 * liefert den Name der Variable, die der Ausdruck definiert.
	 *
	 * @return den Name
	 */
	String getName();
}
