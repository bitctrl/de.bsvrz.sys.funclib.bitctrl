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

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;

/**
 * Schnittstelle f&uum;r den Inhalt einer Attributgruppen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public interface Datensatz<T extends Datum> {

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
	 * Erzeugt ein leeres oder mit Standardwerten ausgef&uuml;lltes Datum des
	 * Datensatzes. Dieses Datum kann nach dem Ausf&uuml;llen an den
	 * Datenverteiler versandt werden.
	 * 
	 * @return das Datum.
	 * 
	 * @see #sendeDaten(Datum)
	 */
	T erzeugeDatum();

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
	T getDatum();

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
	 * Veranlasst den Datensatz ein Datum an den Datenverteiler zusenden. Ist
	 * der Zeitstempel des Datums nicht gesetzt oder gleich 0, wird automatisch
	 * der aktuelle Zeitstempel beim Versand verwendet.
	 * 
	 * @param datum
	 *            das zu sendende Datum.
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 * @see #erzeugeDatum()
	 */
	void sendeDaten(T datum) throws DatensendeException;

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
