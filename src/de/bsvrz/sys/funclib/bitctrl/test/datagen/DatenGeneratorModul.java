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
 * Abstrakte Klasse zur Definition eines Moduls des Datengenerators.<br>
 * Die Klasse verwaltet die verwendete Datenverteilerverbindung und die
 * Aufrufparameter des Moduls.
 *
 * @author peuker
 */
abstract class DatenGeneratorModul {

	/**
	 * die Aufrufargumente.
	 */
	private final Map<String, String> argumente;

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private final ClientDavInterface connection;

	/**
	 * erzeugt ein Modul f�r die �bergebene Datenverteilerverbindung.
	 *
	 * @param connection
	 *            die verwendete Datenverteilerverbindung
	 * @param argumente
	 *            die Argumente f�r das Modul
	 */
	DatenGeneratorModul(final ClientDavInterface connection,
			final Map<String, String> argumente) {
		this.connection = connection;
		this.argumente = argumente;
	}

	/**
	 * f�hrt das entsprechende Modul aus.
	 *
	 * @return das Ergbnis der Ausf�hrung (EXIT-Code der Anwendung)
	 */
	protected abstract int ausfuehren();

	/**
	 * liefert die Argumente des Moduls.
	 *
	 * @return argumente die Argumente
	 */
	protected Map<String, String> getArgumente() {
		return argumente;
	}

	/**
	 * liefert die verwendete Datenverteilerverbindung.
	 *
	 * @return connection die Verbindung
	 */
	protected ClientDavInterface getConnection() {
		return connection;
	}
}
