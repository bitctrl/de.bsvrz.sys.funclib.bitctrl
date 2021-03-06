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

import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Berechtigungsklasse;

/**
 * Dieses Event wird getriggert, wenn ein Benutzer angelegt, geändert oder
 * gelöscht wurde.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class BenutzerEvent extends EventObject {

	/** Der betroffene Benutzer. */
	private final Benutzer benutzer;

	/** Die Berechtigungsklasse des Benutzers. */
	private final Berechtigungsklasse berechtigungsklasse;

	/**
	 * Initialisierung.
	 *
	 * @param source
	 *            die Quelle des Events.
	 * @param benutzer
	 *            der betroffene Benutzer.
	 * @param berechtigungsklasse
	 *            die Berechtigungsklasse des Benutzers.
	 */
	public BenutzerEvent(final Object source, final Benutzer benutzer,
			final Berechtigungsklasse berechtigungsklasse) {
		super(source);

		this.benutzer = benutzer;
		this.berechtigungsklasse = berechtigungsklasse;
	}

	/**
	 * Gibt den betroffenen Benutzer zurück.
	 *
	 * @return der betroffene Benutzer.
	 */
	public Benutzer getBenutzer() {
		return benutzer;
	}

	/**
	 * Gibt die Berechtigungsklasse des Benutzers zurück.
	 * <p>
	 * <em>Hinweis:</em> Gibt {@code null} zurück, wenn der Benutzer angelegt
	 * oder gelöscht wurde.
	 *
	 * @return die Berechtigungsklasse des Benutzers.
	 */
	public Berechtigungsklasse getBerechtigungsklasse() {
		return berechtigungsklasse;
	}

}
