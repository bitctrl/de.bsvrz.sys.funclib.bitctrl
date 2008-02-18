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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.util.logging.Level;

import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;

/**
 * Schnittstelle für Nachrichten die geloggt werden sollen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface LogNachricht {

	/**
	 * Gibt den Level der Nachricht zurück, der für die
	 * Betriebsmeldungsverwaltung genutzt werden soll.
	 * 
	 * @return der Level oder {@code null}, wenn keine Betriebsmeldung versandt
	 *         werden soll.
	 */
	MessageGrade getBmvLevel();

	/**
	 * Gibt den Log-Level der Nachricht zurück.
	 * 
	 * @return der Log-Level.
	 */
	Level getLogLevel();

	/**
	 * Gibt den Namen der Nachricht zurück.
	 * 
	 * @return der Nachrichtenname.
	 */
	String name();

	/**
	 * Gibt die Nachricht als Text zurück. Eventuell vorhandene Platzhalter
	 * werden nicht ersetzt und bleiben erhalten.
	 * 
	 * @return die Nachricht.
	 */
	String toString();

}
