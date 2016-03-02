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

package de.bsvrz.sys.funclib.bitctrl.bmv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.OdBetriebsMeldung;

/**
 * Beschreibt die Änderung der Betriebsmeldungsliste.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class BetriebsmeldungEvent extends EventObject {

	/** Die neu hinzugekommenen Meldungen. */
	private final List<OdBetriebsMeldung.Daten> neueMeldungen;

	/** Die entfernten Meldungen. */
	private final List<OdBetriebsMeldung.Daten> entfernteMeldungen;

	/**
	 * Eventkonstruktor.
	 *
	 * @param source
	 *            die Quelle des Events.
	 * @param neueMeldungen
	 *            die Liste der neuen Meldungen.
	 * @param entfernteMeldungen
	 *            die Liste der entfernten Meldungen
	 */
	public BetriebsmeldungEvent(final Object source,
			final List<OdBetriebsMeldung.Daten> neueMeldungen,
			final List<OdBetriebsMeldung.Daten> entfernteMeldungen) {
		super(source);

		this.neueMeldungen = neueMeldungen != null ? neueMeldungen
				: new ArrayList<OdBetriebsMeldung.Daten>();
		this.entfernteMeldungen = entfernteMeldungen != null
				? entfernteMeldungen : new ArrayList<OdBetriebsMeldung.Daten>();
	}

	/**
	 * Gibt die Liste der neu hinzugekommenen Betriebsmeldungen zurück.
	 *
	 * @return eine unveränderliche Liste der neuen Meldungen.
	 */
	public List<OdBetriebsMeldung.Daten> getNeueMeldungen() {
		return Collections.unmodifiableList(neueMeldungen);
	}

	/**
	 * Gibt die Liste der entfernten Betriebsmeldungen zurück.
	 *
	 * @return eine unveränderliche Liste der entfernten Meldungen.
	 */
	public List<OdBetriebsMeldung.Daten> getEntfernteMeldungen() {
		return Collections.unmodifiableList(entfernteMeldungen);
	}

}
