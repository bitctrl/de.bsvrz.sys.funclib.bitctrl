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

package de.bsvrz.sys.funclib.bitctrl.benutzer;

import java.util.EventListener;

/**
 * Listener f�r das Anlegen, L�schen und �ndern eines Benutzers.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public interface BenutzerListener extends EventListener {

	/**
	 * Wird aufgerufen, wenn ein neuer Benutzer angelegt wurde.
	 *
	 * @param e
	 *            das entsprechende Event.
	 */
	void benutzerAdded(BenutzerEvent e);

	/**
	 * Wird aufgerufen, wenn ein Benutzer gel�scht wurde.
	 *
	 * @param e
	 *            das entsprechende Event.
	 */
	void benutzerRemoved(BenutzerEvent e);

	/**
	 * Wird aufgerufen, wenn die Berechtigungsklasse eines Benutzers ge�ndert
	 * wurde.
	 *
	 * @param e
	 *            das entsprechende Event.
	 */
	void benutzerChanged(BenutzerEvent e);

}
