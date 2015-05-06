/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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

package de.bsvrz.sys.funclib.bitctrl.daf;

/**
 * Standardumsetzer für die Ermittlung einer ID aus den Daten für eine
 * Betriebsmeldung.
 *
 * Die ID der Betriebsmeldung ist identisch mit der PID bzw. der ID eines mit
 * der Betriebsmeldung assoziierten Systemobjekts. Eine bereits explizit
 * gesetzte ID wird nicht überschrieben.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class DefaultBetriebsMeldungsIdKonverter
		implements BetriebsmeldungIdKonverter {

	public String konvertiere(final BetriebsmeldungDaten daten,
			final LogNachricht nachricht, final Object... arguments) {
		String result = null;
		if (daten != null) {
			result = daten.getId();
			if ((result == null) || result.isEmpty()) {
				if (daten.getReference() != null) {
					result = daten.getReference().getPidOrId();
				}
			}
		}

		if (result == null) {
			result = "";
		}
		return result;
	}
}
