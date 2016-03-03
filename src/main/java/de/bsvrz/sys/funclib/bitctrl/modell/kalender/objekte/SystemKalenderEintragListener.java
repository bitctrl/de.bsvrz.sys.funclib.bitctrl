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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.EventListener;

/**
 * Diese Schnittstelle muss implementiert werden, wenn man �ber �nderungen an
 * der Menge der Systemkalendereintr�ge informiert werden will.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public interface SystemKalenderEintragListener extends EventListener {

	/**
	 * Teilt dem Listener die �nderung mit.
	 *
	 * @param e
	 *            das Event, welches die Mengen�nderung kapselt.
	 */
	void systemKalenderEintraegeAktualisiert(
			SystemKalenderEintraegeAktualisiertEvent e);

}