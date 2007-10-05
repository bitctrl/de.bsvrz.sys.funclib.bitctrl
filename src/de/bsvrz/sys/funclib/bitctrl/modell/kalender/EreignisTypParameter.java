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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.dav.daf.main.Data;

/**
 * Ereignistypparameter, der die Priorit&auml;t des Ereignistyps angibt.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface EreignisTypParameter {

	/** Die PID der Parameterattributgruppe. */
	String ATG_PARAMETER = "atg.ereignisTypParameter";

	/**
	 * Getter der Eigenschaft "Priorit&auml;t".
	 * 
	 * @return die Priorit&auml;t als ganze Zahl gr&ouml;&szlig;er oder gleich
	 *         Null.
	 */
	long getPrioritaet();

	/**
	 * Setzt den inneren Zustand anhand des angegebenen Datums.
	 * 
	 * @param daten
	 *            ein g&uuml;ltiges Datum.
	 */
	void setDaten(Data daten);

	/**
	 * Setter der Eigenschaft "Priorit&auml;t".
	 * 
	 * @param prioritaet
	 *            die Priorit&auml;t als ganze Zahl gr&ouml;&szlig;er oder
	 *            gleich Null.
	 */
	void setPrioritaet(long prioritaet);

}
