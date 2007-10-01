/*
 * Internationalisierung, Paket für mehrsprachige Dialoge
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
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
 * Liefert lokalisierte Meldungen
 *
 * @author BitCtrl, Schumann
 * @version $Id:Messages.java 559 2007-04-02 12:25:14Z peuker $
 */
public class Messages {

	/**
	 * Gibt eine lokalisierte Meldung zur&uuml;ck
	 *
	 * @param key
	 *            Typ der Meldung
	 * @return Lokalisierte Meldung
	 */

	public static String get(MessageHandler key) {
		return key.getResourceBundle().getString(key.toString());
	}

	/**
	 * Ersetzt in einer Meldung die enthaltenen Variablen und gibt sie
	 * lokalisiert zur&uuml;ck. Eine Meldung mit Variablen hat die Form
	 * <code>text {0} text {1}"</code>. <code>{...}</code> wird durch den
	 * Inhalt der &uuml;bergebenen Argumente ersetzt.
	 *
	 * @param key
	 *            Typ der Meldung
	 * @param arguments
	 *            Argumente, mit denen die Variaben in der Meldung ersetzt
	 *            werden sollen
	 * @return Lokalisierte Meldung
	 */
	public static String get(MessageHandler key, Object... arguments) {
		return MessageFormat.format(get(key), arguments);
	}
}
