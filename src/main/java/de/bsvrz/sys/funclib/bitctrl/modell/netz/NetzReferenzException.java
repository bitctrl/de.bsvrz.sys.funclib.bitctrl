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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

/**
 * Exception, die bei Ausnahmen und Fehlern der Umwandlung der Netzreferenzen
 * ausgel&ouml;st wird.
 *
 * @author BitCtrl Systems GmbH, Steffen Gieseler
 */

public class NetzReferenzException extends Exception {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Exception des BisInterface.
	 */
	public NetzReferenzException() {
		super();
	}

	/**
	 * Exception des BisInterface.
	 *
	 * @param message
	 *            Text
	 */
	public NetzReferenzException(final String message) {
		super(message);
	}

	/**
	 * Exception des BisInterface.
	 *
	 * @param message
	 *            Text
	 * @param cause
	 *            Throwable
	 */
	public NetzReferenzException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Exception des BisInterface.
	 *
	 * @param cause
	 *            Throwable
	 */
	public NetzReferenzException(final Throwable cause) {
		super(cause);
	}

}
