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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.EventObject;
import java.util.Set;

/**
 * Dieses Event kapselt die Änderung der Ereignistypmenge.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class EreignisTypenAktualisiertEvent extends EventObject {

	/** Die Eigenschaft {@code serialVersionUID}. */
	private static final long serialVersionUID = 1L;

	/** Die Menge der hinzugefügten Ereignistypen. */
	private final Set<EreignisTyp> hinzugefuegt;

	/** Die Menge der entfernten Ereignistypen. */
	private final Set<EreignisTyp> entfernt;

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param source
	 *            die Quelle des Events.
	 * @param hinzugefuegt
	 *            die Menge der hinzugefügten Ereignistypen.
	 * @param entfernt
	 *            die entfernten Ereignistypen.
	 */
	public EreignisTypenAktualisiertEvent(KalenderImpl source,
			Set<EreignisTyp> hinzugefuegt, Set<EreignisTyp> entfernt) {
		super(source);
		this.hinzugefuegt = hinzugefuegt;
		this.entfernt = entfernt;
	}

	/**
	 * Gibt die Menge der entfernten Ereignistypen zurück.
	 * 
	 * @return die Menge kann leer sein ist aber nie {@code null}.
	 */
	public Set<EreignisTyp> getEntfernt() {
		assert entfernt != null;
		return entfernt;
	}

	/**
	 * Gibt die Menge der hinzugefügten Ereignistypen zurück.
	 * 
	 * @return die Menge kann leer sein ist aber nie {@code null}.
	 */
	public Set<EreignisTyp> getHinzugefuegt() {
		assert hinzugefuegt != null;
		return hinzugefuegt;
	}

	/**
	 * Gibt den Kalender zurück, der das Event geschickt hat.
	 * 
	 * @return der Kalender.
	 */
	public KalenderImpl getKalender() {
		return (KalenderImpl) source;
	}

}
