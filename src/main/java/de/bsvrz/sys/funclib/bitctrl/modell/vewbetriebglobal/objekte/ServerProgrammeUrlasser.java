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
/* Copyright by BitCtrl Systems Leipzig */
/* BitCtrl Systems Leipzig */
/* Weisenfelser Str. 67 */
/* 04229 Leipzig */
/* Tel.: +49 341 49067 - 0 */
/* Fax.: +49 341 49067 - 15 */
/* mailto:info@bitctrl.de */
/* http://www.bitctrl.de */
/*---------------------------------------------------------------*/
package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte;

import de.bsvrz.sys.funclib.bitctrl.benutzer.Benutzerverwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.attributlisten.Urlasser;

/**
 * Ein Singleton-Objekt, welches ein korrekt initialisiertes {@link Urlasser}
 * -Objekt bereitstellt, welches man zum Versenden von Betriebsmeldungen auf
 * Serverseite verwenden kann.
 *
 * @author BitCtrl Systems GmbH, uhlmann
 */
public final class ServerProgrammeUrlasser {

	/**
	 * das Singleton.
	 */
	private static ServerProgrammeUrlasser instance;

	/**
	 * Der Login-Name des Benutzers für die Serverprogramme.
	 */
	private static final String SERVER_NUTZER_LOGIN_NAME = "Server";

	/**
	 * das bereitgestellt {@link Urlasser}-Objekt.
	 */
	private final Urlasser urlasser;

	/**
	 * Die Hauptmethode, die das {@link Urlasser}-Objekt bereitstellt und es mit
	 * der aktuellen Ursache versieht.
	 *
	 * @param ursache
	 *            die aktuell zu verwendende Ursache.
	 * @return der Urlasser
	 */
	public static Urlasser getUrlasser(final String ursache) {
		if (instance == null) {
			instance = new ServerProgrammeUrlasser();
		}
		if (ursache != null) {
			instance.getUrlasser().setUrsache(ursache);
		}
		return instance.getUrlasser();
	}

	/**
	 * Der Konstruktor ist privat, da es keine öffentlichen Instanzen gibt.
	 */
	private ServerProgrammeUrlasser() {
		urlasser = new Urlasser();
		final Benutzer benutzer = Benutzerverwaltung.getInstanz()
				.getBenutzer(SERVER_NUTZER_LOGIN_NAME);
		if (benutzer == null) {
			throw new RuntimeException("Der Benutzer mit dem Login-Namen '"
					+ SERVER_NUTZER_LOGIN_NAME + "' ist nicht definiert");
		}
		urlasser.setBenutzer(benutzer);
		urlasser.setVeranlasser(benutzer.getName());
	}

	/**
	 * Liefert das zugrunde liegende {@link Urlasser}-Objekt.
	 *
	 * @return der Urlasser
	 */
	public Urlasser getUrlasser() {
		return urlasser;
	}
}
