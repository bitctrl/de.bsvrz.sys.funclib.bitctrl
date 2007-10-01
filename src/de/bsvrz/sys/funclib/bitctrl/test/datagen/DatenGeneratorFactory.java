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

package de.bsvrz.sys.funclib.bitctrl.test.datagen;

import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Die Klasse erzeugt das jeweils geforderte Modul des Datengenerators.
 *
 * @author peuker
 */
class DatenGeneratorFactory {

	/**
	 * erzeugt das geforderte Programmmodul auf Basis der übergebenen
	 * Datenverteilerverbindung, dem Modulname und der Liste der
	 * Aufrufargumente.
	 *
	 * @param connection
	 *            die Verbindung zum Datenverteiler
	 * @param modStr
	 *            der Name des zu erzeugenden Moduls
	 * @param argumente
	 *            die Liste der Argumente für das Modul
	 * @return das Modul
	 */
	static DatenGeneratorModul getModul(
			final ClientDavInterface connection, final String modStr,
			final Map<String, String> argumente) {
		DatenGeneratorModul modul = null;
		if ("CONFIG".equals(modStr.toUpperCase())) {
			modul = new GeneriereTemplateModul(connection, argumente);
		} else if ("CREATE".equals(modStr.toUpperCase())) {
			modul = new GeneriereAusgabeDateiModul(connection, argumente);
		} else {
			throw new IllegalArgumentException("Unbekannter Modus: " + modStr);
		}

		return modul;
	}
}
