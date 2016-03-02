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

import com.bitctrl.commands.Command;

import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.OdBetriebsMeldung;

/**
 * Beschreibt einen Befehl beim Eintreffen bestimmter Betriebsmeldungen
 * ausgeführt werden soll.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public interface BetriebsmeldungCommand extends Command {

	/**
	 * Prüft ob eine bestimmte Betriebsmeldung diesen Befehl auslöst.
	 *
	 * @param meldung
	 *            eine Betriebsmeldung.
	 * @return {@code true}, wenn der Befehl ausgeführt werden soll.
	 */
	boolean isTrigger(OdBetriebsMeldung.Daten meldung);

}
