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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.EventListener;

/**
 * Schnittstelle für einen Listener, der Benachrichtigt wird, wenn eine
 * Baustelle in einem VerkehrsmodellNetz angelegt oder entfernt wurde.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public interface BaustellenListener extends EventListener {
	/**
	 * eine neue Baustelle wurde angelegt.
	 *
	 * @param netz
	 *            das Netz in dem die Baustelle angelegt wurde.
	 * @param baustelle
	 *            die Baustelle
	 */
	void baustelleAngelegt(VerkehrModellNetz netz, Baustelle baustelle);

	/**
	 * eine neue Baustelle wurde entfernt.
	 *
	 * @param netz
	 *            das Netz in dem die Baustelle angelegt wurde.
	 * @param baustelle
	 *            die Baustelle
	 */
	void baustelleEntfernt(VerkehrModellNetz netz, Baustelle baustelle);
}
