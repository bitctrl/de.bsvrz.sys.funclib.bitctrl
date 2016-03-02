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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.util;

/**
 * Enth&auml;lt allgemeine Konstanten.
 *
 * @author BitCtrl Systems GmbH, Schumann
 * @deprecated ‹berschneidet sich sich mit
 *             {@link de.bsvrz.sys.funclib.bitctrl.util.Konstanten} und
 *             {@link com.bitctrl.Constants}.
 */
@Deprecated
public final class Konstanten {

	private Konstanten() {
		// es gibt keine Instanzen der Klasse
	}

	/** Anzahl Millisekunden pro Tag. */
	public static final long MILLIS_PER_TAG = 24 * 60 * 60 * 1000;

	/** Anzahl Millisekunden pro Stunde. */
	public static final long MILLIS_PER_STUNDE = 60 * 60 * 1000;

	/** Anzahl Millisekunden pro Minute. */
	public static final long MILLIS_PER_MINUTE = 60 * 1000;

	/** Anzahl Millisekunden pro Sekunde. */
	public static final long MILLIS_PER_SEKUNDE = 1000;

	/** Der systemabh&auml;ngige Zeilenumbruch. */
	public static final String NL = System.getProperty("line.separator");

}
