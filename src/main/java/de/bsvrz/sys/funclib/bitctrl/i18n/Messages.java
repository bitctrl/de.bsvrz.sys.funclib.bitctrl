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

package de.bsvrz.sys.funclib.bitctrl.i18n;

import java.text.MessageFormat;

/**
 * Liefert lokalisierte Meldungen.
 *
 * @author BitCtrl Systems GmbH, Schumann
 * @deprecated Die Klasse wurde nach {@link com.bitctrl.i18n.Messages}
 *             ausgelagert.
 */
@Deprecated
public final class Messages {

	/**
	 * Gibt eine lokalisierte Meldung zur&uuml;ck.
	 *
	 * @param key
	 *            Typ der Meldung
	 * @return Lokalisierte Meldung
	 */

	public static String get(final MessageHandler key) {
		return key.getResourceBundle().getString(key.toString());
	}

	/**
	 * Ersetzt in einer Meldung die enthaltenen Variablen und gibt sie
	 * lokalisiert zur&uuml;ck. Eine Meldung mit Variablen hat die Form
	 * <code>text {0} text {1}"</code>. <code>{...}</code> wird durch den Inhalt
	 * der &uuml;bergebenen Argumente ersetzt.
	 *
	 * @param key
	 *            Typ der Meldung
	 * @param arguments
	 *            Argumente, mit denen die Variaben in der Meldung ersetzt
	 *            werden sollen
	 * @return Lokalisierte Meldung
	 */
	public static String get(final MessageHandler key,
			final Object... arguments) {
		return MessageFormat.format(get(key), arguments);
	}

	/**
	 * privater versteckter Konstruktor.
	 */
	private Messages() {
		// wird nie ausgeführt
	}
}
