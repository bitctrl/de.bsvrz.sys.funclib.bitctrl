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

import de.bsvrz.dav.daf.main.ResultData;
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
	 * Meldet den Datensatz als Sender oder Quelle am Datenverteiler an.
	 * 
	 * @throws AnmeldeException
	 *             wenn die Anmeldung nicht erfolgreich war.
	 */
	void anmeldenSender() throws AnmeldeException;

	/**
	 * Gibt die Attributgruppe zur&uuml;ck die diesem Datensatz entpricht.
	 * 
	 * @return die Attributgruppe die dem Datensatz entspricht.
	 */
	AttributeGroup getAttributGruppe();

	/**
	 * Gibt die aktuellen Daten des Datensatzes zur&uuml;ck.
	 * 
	 * @return ein Datum, welches die Daten des Datensatzes kapselt.
	 */
	Datum getDatum();

	/**
	 * Gibt das Systemobjekt zur&uuml;ck, zu dem der Datensatz geh&ouml;rt.
	 * 
	 * @return das Objekt, zu dem der Datensatz geh&ouml;rt.
	 */
	SystemObjekt getObjekt();

	/**
	 * Fragt, ob der Datensatz als Sender oder Quelle angemeldet ist.
	 * 
	 * @return {@code true}, wenn der Datensatz als Sender oder Quelle
	 *         angemeldet ist.
	 */
	boolean isAngemeldetSender();

	/**
	 * Liest das Flag {@code autoUpdate}.
	 * 
	 * @return {@code true}, wenn der Datensatz neue Daten automatisch vom
	 *         Datenverteiler empf&auml;ngt.
	 */
	boolean isAutoUpdate();

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
	 * 
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 */
	void sendeDaten() throws DatensendeException;

	/**
	 * Veranlasst den Datensatz seinen Inhalt an den Datenverteiler zu
	 * &uuml;bermitteln.
	 * 
	 * @param zeitstempel
	 *            der Zeitstempel, mit dem die Daten versandt werden.
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 */
	void sendeDaten(long zeitstempel) throws DatensendeException;

	/**
	 * Liest das Datum aus und setzt dessen Inhalt als internen Zustand.
	 * 
	 * @param daten
	 *            ein passender Datenverteilerdatensatz.
	 */
	void setDaten(ResultData daten);

	/**
	 * Ruft die aktuellen Daten ab und setzt die internen Daten.
	 */
	void update();
}
