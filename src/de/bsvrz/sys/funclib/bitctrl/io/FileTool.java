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

package de.bsvrz.sys.funclib.bitctrl.io;

import java.io.File;

/**
 * Allgemeine Funktionen.
 *
 * @author peuker
 */
public final class FileTool {

	/**
	 * loescht die angegebene Datei/Verzeichnis rekursiv.
	 *
	 * @param verzeichnis
	 *            das Verzeichnis
	 * @return <i>true</i>, wenn der Löschvorgang erfolgreich ausgeführt werden
	 *         konnte
	 */
	public static boolean loescheVerzeichnis(final File verzeichnis) {

		boolean result = true;

		if (verzeichnis.isDirectory()) {
			for (File file : verzeichnis.listFiles()) {
				if (file.isDirectory()) {
					if (!loescheVerzeichnis(file)) {
						result = false;
						break;
					}
				} else {
					result = file.delete();
				}
			}
			if (result) {
				result = verzeichnis.delete();
			}
		} else {
			result = verzeichnis.delete();
		}

		return result;
	}
}
