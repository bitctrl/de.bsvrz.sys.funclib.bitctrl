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

package de.bsvrz.sys.funclib.bitctrl.konstante;

/**
 * Definition von allgemeinen Konstanten.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @deprecated Überschneidet sich sich mit
 *             {@link de.bsvrz.sys.funclib.bitctrl.util.Konstanten} und
 *             {@link com.bitctrl.Constants}.
 */
@Deprecated
public final class Konstante {

	private Konstante() {
		// es gibt keine Instanzen der Klasse
	}

	/** eine Sekunde in Millisekunden. */
	public static final long SEKUNDE_IN_MS = 1000L;

	/** eine Minute in Millisekunden. */
	public static final long MINUTE_IN_MS = 60L * 1000L;

	/** eine Stunde in Millisekunden. */
	public static final long STUNDE_IN_MS = 60L * 60L * 1000L;

	/** ein Tag in Millisekunden. */
	public static final long TAG_24_IN_MS = 24L * 60L * 60L * 1000L;

	/** Faktor für Prozentrechnung. */
	public static final int PROZENT_FAKTOR = 100;

	/** leere Zeichenkette. */
	public static final String LEERSTRING = ""; //$NON-NLS-1$

	/** Zeichenkette "unbekannt". */
	public static final String STRING_UNBEKANNT = "unbekannt"; //$NON-NLS-1$

	/** systemabhängiges Zeichen für einen Zeilenumbruch. */
	public static final String NEWLINE = System.getProperty("line.separator"); //$NON-NLS-1$
}
