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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.EventObject;

/**
 * Das Ereignis tritt ein, wenn ein Datensatz ge&auml;ndert wurde.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class DatensatzUpdateEvent extends EventObject {

	/** Die Versions-ID der Serialialisierung. */
	private static final long serialVersionUID = 1L;

	/** Der Datensatz, der sich ge&auml;ndert hat. */
	private final Datensatz datensatz;

	/**
	 * Der Konstruktor des Ereignisses.
	 * 
	 * @param source
	 *            die Quelle des Ereignis, in dem Fall das betreffende
	 *            Systemobjekt.
	 * @param datensatz
	 *            der Datensatz, der sich ge&auml;ndert hat.
	 */
	public DatensatzUpdateEvent(SystemObjekt source, Datensatz datensatz) {
		super(source);
		this.datensatz = datensatz;
	}

	/**
	 * Gibt den ge&auml;nderten Datensatz zur&uuml;ck.
	 * 
	 * @return der ge&auml;nderte Datensatz.
	 */
	public Datensatz getDatensatz() {
		return datensatz;
	}

	/**
	 * Gibt das Systemobjekt zur&uuml;ck, dessen Datensatz sich ge&auml;ndert
	 * hat.
	 * 
	 * @return das Systemobjekt.
	 */
	public SystemObjekt getObjekt() {
		return (SystemObjekt) source;
	}

}
