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

import de.bsvrz.dav.daf.main.config.Aspect;

/**
 * Schnittstelle f&uuml;r Onlinedatens&auml;tze.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public interface OnlineDatensatz<T extends Datum> extends Datensatz<T> {

	/**
	 * Gibt den Aspekt zur&uuml;ck, mit dem Daten empfangen werden.
	 * 
	 * @return der Empfangsaspekt.
	 */
	Aspect getEmpfangsAspekt();

	/**
	 * Gibt den Aspekt zur&uuml;ck, mit dem Daten gesendet werden.
	 * 
	 * @return der Sendeaspekt.
	 */
	Aspect getSendeAspekt();

	/**
	 * Gibt das Flag {@code quelle} zur&uuml;ck.
	 * 
	 * @return {@code true}, wenn der Datensatz als Quelle und {@code false},
	 *         wenn er als Sender angemeldet werden soll.
	 */
	boolean isQuelle();

	/**
	 * Gibt das Flag {@code senke} zur&uuml;ck.
	 * 
	 * @return {@code true}, wenn der Datensatz als Senke und {@code false},
	 *         wenn er als Empf&auml;nger angemeldet werden soll.
	 */
	boolean isSenke();

	/**
	 * Legt fest, ob Anmeldungen als Quelle durchgef&uuml;hrt werden sollen.
	 * Eine bereits bestehende Anmeldung wird dadurch nicht beeinflusst.
	 * 
	 * @param quelle
	 *            {@code true}, wenn die Anmeldung als Quelle erfolgen soll,
	 *            ansonsten erfolgt sie als Sender.
	 */
	void setQuelle(boolean quelle);

	/**
	 * Legt fest, ob Anmeldungen als Senke durchgef&uuml;hrt werden sollen. Eine
	 * bereits bestehende Anmeldung wird dadurch nicht beeinflusst.
	 * 
	 * @param senke
	 *            {@code true}, wenn die Anmeldung als Senke erfolgen soll,
	 *            ansonsten erfolgt sie als Empf&auml;nger.
	 */
	void setSenke(boolean senke);

}
