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

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;

/**
 * Schnittstelle f&uum;r den Inhalt einer Attributgruppen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface Datensatz {

	/**
	 * Meldet eine eventuell vorhandene Anmeldung als Sender oder Quelle wieder
	 * ab.
	 */
	void abmeldenSender();

	/**
	 * Registriert einen Listener.
	 * 
	 * @param l
	 *            ein interessierte Listener.
	 */
	void addUpdateListener(DatensatzUpdateListener l);

	/**
	 * Gibt die Attributgruppe zur&uuml;ck die diesem Datensatz entpricht.
	 * 
	 * @return die Attributgruppe die dem Datensatz entspricht.
	 */
	AttributeGroup getAttributGruppe();

	/**
	 * Gibt das Systemobjekt zur&uuml;ck, zu dem der Datensatz geh&ouml;rt.
	 * 
	 * @return das Objekt, zu dem der Datensatz geh&ouml;rt.
	 */
	SystemObjekt getObjekt();

	/**
	 * Liest das Flag {@code autoUpdate}.
	 * 
	 * @return {@code true}, wenn der Datensatz neue Daten automatisch vom
	 *         Datenverteiler empf&auml;ngt.
	 */
	boolean isAutoUpdate();

	/**
	 * Liste das Flag {@code valid}.
	 * 
	 * @return {@code false}, wenn der Datensatz ung&uuml;ltig ist (z.&nbsp;B.
	 *         "keine Daten" oder "keine Quelle").
	 */
	boolean isValid();

	/**
	 * Deregistriert einen Listener.
	 * 
	 * @param l
	 *            ein nicht mehr interessierten Listener.
	 */
	void removeUpdateListener(DatensatzUpdateListener l);

	/**
	 * Veranlasst den Datensatz seinen Inhalt an den Datenverteiler zu
	 * &uuml;bermitteln.
	 */
	void sendeDaten();

	/**
	 * Setzt das Flag {@code autoUpdate}.
	 * 
	 * @param ein
	 *            {@code true}, wenn der Datensatz neue Daten automatisch vom
	 *            Datenverteiler empfangen soll.
	 */
	void setAutoUpdate(boolean ein);

	/**
	 * Liest das Datum aus und setzt dessen Inhalt als internen Zustand.
	 * 
	 * @param daten
	 *            ein passendes Datum.
	 */
	void setDaten(Data daten);

	/**
	 * ruft die aktuellen Daten ab und setzt die internen Daten.
	 */
	void update();
}
