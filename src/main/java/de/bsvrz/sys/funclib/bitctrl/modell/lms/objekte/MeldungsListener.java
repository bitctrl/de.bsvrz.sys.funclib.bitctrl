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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte;

import java.util.EventListener;

/**
 * Schnittstelle f�r einen Listener, der benachrichtigt wird, wenn eine
 * RDS-Meldung angelegt bzw. entfernt wird.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public interface MeldungsListener extends EventListener {

	/**
	 * f�r die �bergebene Landesmeldestelle wurde die neue Meldung in die Liste
	 * der Meldungen aingetragen.
	 *
	 * @param lms
	 *            die Landesmeldestelle
	 * @param meldung
	 *            die Meldung
	 */
	void meldungAngelegt(LandesMeldeStelle lms, RdsMeldung meldung);

	/**
	 * f�r die �bergebene Landesmeldestelle wurde die �bergebene Meldung aus der
	 * Liste der Meldungen entfernt.
	 *
	 * @param lms
	 *            die Landesmeldestelle
	 * @param meldung
	 *            die Meldung
	 */
	void meldungEntfernt(LandesMeldeStelle lms, RdsMeldung meldung);

}
