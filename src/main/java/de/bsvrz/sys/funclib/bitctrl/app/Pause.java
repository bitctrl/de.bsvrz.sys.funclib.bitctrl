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

package de.bsvrz.sys.funclib.bitctrl.app;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Allgemeine Klasse zum Pausieren eines Threads.<br>
 * Der Thread wird für die angegebene Zeit suspendiert. Wird die gewünschte
 * Pausenzeit unterbrochen, wird dies ignoriert, d.h. die Dauer der Pause ist
 * nicht sichergestellt.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 *
 * @deprecated Kann durch Datenverteilerfunktionen
 *             {@link ClientDavInterface#sleep(long)} und
 *             {@link ClientDavInterface#sleepUntil(long)} ersetzt werden.
 */
@Deprecated
public final class Pause {

	/**
	 * die Funktion wird verlassen, bis der angebenen Zeitpunkt erreicht ist.
	 * Die Angabe des Zeitpunkts erfolgt absolut in Millisekunden. Die Auflösung
	 * beträgt 1 Sekunde.
	 *
	 * TODO Auflösung verbessern
	 *
	 * @param endzeitpunkt
	 *            der Zeitpunkt
	 */
	public static void bis(final long endzeitpunkt) {
		do {
			final long now = System.currentTimeMillis();
			if (now >= endzeitpunkt) {
				break;
			}
			warte(Constants.MILLIS_PER_SECOND);
		} while (true);
	}

	/**
	 * die Funktion suspendiert die Ausführung des aufrufenden Threads für die
	 * gegebene Zeit oder kürzer.
	 *
	 * @param millisekunden
	 *            die Dauer in Millisekunden
	 */
	public static void warte(final long millisekunden) {
		try {
			Thread.sleep(millisekunden);
		} catch (final InterruptedException e) {
			// wird vernachlässigt
		}
	}

	/** privater Konstruktor. */
	private Pause() {
		super();
	}
}
