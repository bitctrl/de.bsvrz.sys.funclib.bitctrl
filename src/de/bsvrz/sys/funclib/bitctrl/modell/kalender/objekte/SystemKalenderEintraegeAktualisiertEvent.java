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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.EventObject;
import java.util.Set;

/**
 * Dieses Event kapselt die �nderung der Systemkalendereintragsmenge.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class SystemKalenderEintraegeAktualisiertEvent extends EventObject {

	/** Die Eigenschaft {@code serialVersionUID}. */
	private static final long serialVersionUID = 1L;

	/** Die Menge der hinzugef�gten Systemkalendereintr�ge. */
	private final Set<SystemKalenderEintrag> hinzugefuegt;

	/** Die Menge der entfernten Systemkalendereintr�ge. */
	private final Set<SystemKalenderEintrag> entfernt;

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param source
	 *            die Quelle des Events.
	 * @param hinzugefuegt
	 *            die Menge der hinzugef�gten Systemkalendereintr�ge.
	 * @param entfernt
	 *            die entfernten Systemkalendereintr�ge.
	 */
	public SystemKalenderEintraegeAktualisiertEvent(Kalender source,
			Set<SystemKalenderEintrag> hinzugefuegt,
			Set<SystemKalenderEintrag> entfernt) {
		super(source);

		assert hinzugefuegt != null;
		assert entfernt != null;

		this.hinzugefuegt = hinzugefuegt;
		this.entfernt = entfernt;
	}

	/**
	 * Gibt die Menge der entfernten Systemkalendereintr�ge zur�ck.
	 * 
	 * @return die Menge kann leer sein ist aber nie {@code null}.
	 */
	public Set<SystemKalenderEintrag> getEntfernt() {
		assert entfernt != null;
		return entfernt;
	}

	/**
	 * Gibt die Menge der hinzugef�gten Systemkalendereintr�ge zur�ck.
	 * 
	 * @return die Menge kann leer sein ist aber nie {@code null}.
	 */
	public Set<SystemKalenderEintrag> getHinzugefuegt() {
		assert hinzugefuegt != null;
		return hinzugefuegt;
	}

	/**
	 * Gibt den Kalender zur�ck, der das Event geschickt hat.
	 * 
	 * @return der Kalender.
	 */
	public Kalender getKalender() {
		return (Kalender) source;
	}

}
