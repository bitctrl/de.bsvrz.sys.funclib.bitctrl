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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
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
 * @author BitCtrl Systems GmbH, Peuker
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
