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

package de.bsvrz.sys.funclib.bitctrl.test.datagen;

import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Die Klasse erzeugt das jeweils geforderte Modul des Datengenerators.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
final class DatenGeneratorFactory {

	/**
	 * Privater Standardkonstruktor, verhindert das Anlegen von Instanzen dieser
	 * Klasse.
	 */
	private DatenGeneratorFactory() {
		super();
	}

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
	static DatenGeneratorModul getModul(final ClientDavInterface connection,
			final String modStr, final Map<String, String> argumente) {
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
