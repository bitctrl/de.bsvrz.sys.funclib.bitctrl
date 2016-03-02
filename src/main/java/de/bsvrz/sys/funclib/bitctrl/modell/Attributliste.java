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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.Data;

/**
 * Schnittstelle für Attributlisten. Anstelle von zig Setter und Getter für ein
 * Datensatzdatum sollten die Attributlisten auch auf Objekte abgebildet werden.
 * Diese Schnittstelle erlaubt die Konvertierung der Daten zwischen einer
 * Dav-Attributliste und einem Java-Objekt.
 * <p>
 * Eine Attributliste sollte als JavaBean implementiert werden, also in der
 * Regel nur über die üblichen getXXX- und setXXX-Methoden verfügen.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public interface Attributliste extends Cloneable {

	/**
	 * Schreibt die Daten des Java-Objekts in die übergebenen Attributliste.
	 *
	 * @param daten
	 *            die zum Objekt korrespondierende Attributliste.
	 */
	void bean2Atl(final Data daten);

	/**
	 * Liest die Daten aus der übergebenen Attributliste und übernimmt sie in
	 * das Java-Objekt.
	 *
	 * @param daten
	 *            die zum Objekt korrespondierende Attributliste.
	 */
	void atl2Bean(final Data daten);

	/**
	 * Erzeugt eine Kopie der Attributliste.
	 *
	 * @return die Kopie.
	 * @see Object#clone()
	 */
	Attributliste clone();

}
