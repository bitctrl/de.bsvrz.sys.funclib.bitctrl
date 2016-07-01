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

package de.bsvrz.sys.funclib.bitctrl.modell;

/**
 * Ausnahme die von einem Datensender geworfen wird, wenn Daten gesendet werden
 * konnten.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class AnmeldeException extends Exception {

	/** Versions-ID der Serialisierung. */
	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor.
	 */
	public AnmeldeException() {
		// nix
	}

	/**
	 * Konstruktor.
	 *
	 * @param message
	 *            eine Fehlernachricht.
	 */
	public AnmeldeException(final String message) {
		super(message);
	}

	/**
	 * Konstruktor.
	 *
	 * @param message
	 *            eine Fehlernachricht.
	 * @param cause
	 *            ein Grund.
	 */
	public AnmeldeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Konstruktor.
	 *
	 * @param cause
	 *            ein Grund.
	 */
	public AnmeldeException(final Throwable cause) {
		super(cause);
	}

}
