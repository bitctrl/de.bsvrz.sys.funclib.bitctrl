/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.List;

/**
 * Schnittstelle f&uuml;r einen Onlinedatensatz, der Messwerte beinhaltet.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface MesswertDatum extends Datum {

	/**
	 * Gibt den Zahlenwert eines Messwerts zur&uuml;ck.
	 * 
	 * @param name
	 *            der Name des gesuchten Messwert.
	 * @return der Zahlenwert.
	 */
	Number getWert(String name);

	/**
	 * Gibt alle Messwerte zur&uuml;ck, die der Datensatz kennt.
	 * 
	 * @return die Liste bekannter Messwerte.
	 */
	List<String> getWerte();

	/**
	 * Legt den Zahlenwert eines Messwerts fest.
	 * 
	 * @param name
	 *            der Name des Messwerts.
	 * @param wert
	 *            der neue Zahlenwert.
	 */
	void setWert(String name, Number wert);

}
