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

import java.util.Collection;

import de.bsvrz.dav.daf.main.config.Aspect;

/**
 * Schnittstelle f&uuml;r Onlinedatens&auml;tze.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public interface OnlineDatensatz<T extends Datum> extends Datensatz<T> {

	/**
	 * Meldet eine eventuell vorhandene Anmeldung als Sender oder Quelle wieder
	 * ab.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 */
	void abmeldenSender(Aspect asp);

	/**
	 * Registriert einen Listener.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param l
	 *            ein interessierte Listener.
	 */
	void addUpdateListener(Aspect asp, DatensatzUpdateListener l);

	/**
	 * Meldet den Datensatz als Sender oder Quelle am Datenverteiler an.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @throws AnmeldeException
	 *             wenn die Anmeldung nicht erfolgreich war.
	 */
	void anmeldenSender(Aspect asp) throws AnmeldeException;

	/**
	 * Gibt die verf&uuml;gbaren Aspekte zur&uuml;ck.
	 *
	 * @return die Menge der verf&uuml;gbaren Aspekte.
	 */
	Collection<Aspect> getAspekte();

	/**
	 * Gibt die aktuellen Daten des Datensatzes zur&uuml;ck.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return ein Datum, welches die Daten des Datensatzes kapselt.
	 */
	T getDatum(Aspect asp);

	/**
	 * Fragt, ob der Datensatz als Sender oder Quelle Daten senden darf.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Sender oder Quelle Daten
	 *         senden darf.
	 */
	Status getStatusSendesteuerung(Aspect asp);

	/**
	 * Fragt, ob der Datensatz als Sender oder Quelle angemeldet ist.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Sender oder Quelle
	 *         angemeldet ist.
	 */
	boolean isAngemeldetSender(Aspect asp);

	/**
	 * Liest das Flag {@code autoUpdate}.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz neue Daten automatisch vom
	 *         Datenverteiler empf&auml;ngt.
	 */
	boolean isAutoUpdate(Aspect asp);

	/**
	 * Gibt das Flag {@code quelle} zur&uuml;ck.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Quelle und {@code false},
	 *         wenn er als Sender angemeldet werden soll.
	 */
	boolean isQuelle(Aspect asp);

	/**
	 * Gibt das Flag {@code senke} zur&uuml;ck.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @return {@code true}, wenn der Datensatz als Senke und {@code false},
	 *         wenn er als Empf&auml;nger angemeldet werden soll.
	 */
	boolean isSenke(Aspect asp);

	/**
	 * Deregistriert einen Listener.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param l
	 *            ein nicht mehr interessierten Listener.
	 */
	void removeUpdateListener(Aspect asp, DatensatzUpdateListener l);

	/**
	 * Veranlasst den Datensatz ein Datum an den Datenverteiler zusenden. Ist
	 * der Zeitstempel des Datums nicht gesetzt oder gleich 0, wird automatisch
	 * der aktuelle Zeitstempel beim Versand verwendet.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param datum
	 *            das zu sendende Datum.
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 * @see #erzeugeDatum()
	 */
	void sendeDaten(Aspect asp, T datum) throws DatensendeException;

	/**
	 * Veranlasst den Datensatz ein Datum an den Datenverteiler zusenden. Ist
	 * der Zeitstempel des Datums nicht gesetzt oder gleich 0, wird automatisch
	 * der aktuelle Zeitstempel beim Versand verwendet.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param datum
	 *            das zu sendende Datum.
	 * @param timeout
	 *            die Zeit in der der Datensatz gesendet werden muss.
	 * @throws DatensendeException
	 *             wenn die Daten nicht gesendet werden konnten. Der Sendecache
	 *             wird in dem Fall nicht geleert.
	 * @see #erzeugeDatum()
	 */
	void sendeDaten(Aspect asp, T datum, long timeout)
			throws DatensendeException;

	/**
	 * Legt fest, ob Anmeldungen als Quelle durchgef&uuml;hrt werden sollen.
	 * Eine bereits bestehende Anmeldung wird dadurch nicht beeinflusst.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param quelle
	 *            {@code true}, wenn die Anmeldung als Quelle erfolgen soll,
	 *            ansonsten erfolgt sie als Sender.
	 */
	void setQuelle(Aspect asp, boolean quelle);

	/**
	 * Legt fest, ob Anmeldungen als Senke durchgef&uuml;hrt werden sollen. Eine
	 * bereits bestehende Anmeldung wird dadurch nicht beeinflusst.
	 *
	 * @param asp
	 *            der betroffene Aspekt.
	 * @param senke
	 *            {@code true}, wenn die Anmeldung als Senke erfolgen soll,
	 *            ansonsten erfolgt sie als Empf&auml;nger.
	 */
	void setSenke(Aspect asp, boolean senke);

}
