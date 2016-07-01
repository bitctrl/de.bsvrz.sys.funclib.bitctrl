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

package de.bsvrz.sys.funclib.bitctrl.benutzer;

import java.util.EventObject;

import com.bitctrl.util.Timestamp;

import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;

/**
 * Beschreibt ein Anmeldeevent. Dies kann das Anmelden oder Abmelden eines
 * Benutzers sein.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class LoginEvent extends EventObject {

	/** Der betroffene Benutzer. */
	private final Benutzer benutzer;

	/** Die betroffene Applikation. */
	private final Applikation applikation;

	/** Der Zeitpunkt der Anmeldung. */
	private final Timestamp anmeldezeit;

	/**
	 * Initialisierung.
	 *
	 * @param source
	 *            die Quelle des Events.
	 * @param benutzer
	 *            der betroffene Benutzer.
	 * @param applikation
	 *            die betroffene Applikation.
	 * @param anmeldezeit
	 *            der Zeitpunkt der Anmeldung.
	 */
	public LoginEvent(final Object source, final Benutzer benutzer,
			final Applikation applikation, final Timestamp anmeldezeit) {
		super(source);

		this.benutzer = benutzer;
		this.applikation = applikation;
		this.anmeldezeit = anmeldezeit;
	}

	/**
	 * Gibt den betroffenen Benutzer zurück.
	 *
	 * @return der Benutzer.
	 */
	public Benutzer getBenutzer() {
		return benutzer;
	}

	/**
	 * Gibt die betroffene Applikation zurück.
	 *
	 * @return die betroffene Applikation.
	 */
	public Applikation getApplikation() {
		return applikation;
	}

	/**
	 * Gibt den Anmeldezeitpunkt laut Datenverteiler zurück.
	 *
	 * @return der Zeitpunkt der Anmeldung.
	 */
	public Timestamp getAnmeldezeit() {
		return anmeldezeit;
	}

	@Override
	public String toString() {
		String s;

		s = getClass().getName() + "[";
		s += "source=" + getSource();
		s += ", applikation=" + applikation;
		s += ", benutzer=" + benutzer;
		s += ", anmeldezeit=" + anmeldezeit;
		s += "]";

		return s;
	}

}
