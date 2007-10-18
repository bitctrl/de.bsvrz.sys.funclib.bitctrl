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

/**
 * Kapselt die Daten eines Datensatzes.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface Datum {

	/**
	 * Klont das Objekt, in dem der Zeitstempel und alle Daten hart kopiert
	 * werden.
	 * 
	 * @return ein Klon des Datum.
	 */
	Datum clone();

	/**
	 * Gibt den Zeitstempel des Datums als lesbaren Text zur&uuml;ck.
	 * 
	 * @return der Zeitpunkt.
	 */
	String getZeitpunkt();

	/**
	 * Gibt den Zeitstempel des Datum zur&uuml;ck.
	 * 
	 * @return den Zeitstempel oder 0, wenn er nicht bekannt ist.
	 */
	long getZeitstempel();

	/**
	 * Liste das Flag {@code valid}.
	 * 
	 * @return {@code false}, wenn der Datensatz ung&uuml;ltig ist (z.&nbsp;B.
	 *         "keine Daten" oder "keine Quelle").
	 */
	boolean isValid();

	/**
	 * Legt den Zeitstempel des Datums fest.
	 * 
	 * @param zeitstempel
	 *            der neue Zeitstempel.
	 */
	void setZeitstempel(long zeitstempel);

}
