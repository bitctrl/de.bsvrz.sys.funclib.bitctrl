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

import de.bsvrz.sys.funclib.application.StandardApplication;

/**
 * Implementiert nichts aus der Schnittstelle {@link StandardApplication},
 * f&uuml;gt aber n&uuml;tzliche Methoden der Applikation hinzu.
 *
 * @author BitCtrl Systems GmbH, Schumann
 * @deprecated Für die Ausgabe beim Starten und dem Auslesen Applikationsdaten
 *             kann nun
 *             {@link com.bitctrl.util.jar.JarTools#printVersionInfo(Class)} und
 *             {@link com.bitctrl.VersionInfo} verwendet werden.
 */
@Deprecated
public abstract class AbstractStandardApplication
		implements StandardApplication {

	/**
	 * Gibt den Copyright-Hinweis laut GPL beim Starten aus.
	 */
	protected AbstractStandardApplication() {
		System.out.println(getApplikationName());
		System.out.println("Version " + getVersion());
		System.out.println(
				"--------------------------------------------------------------------------------");
		System.out.println(getCopyright());
		System.out.println();
		System.out.println(
				"This Application comes with ABSOLUTELY NO WARRANTY. This is free software, and");
		System.out.println(
				"you are welcome to redistribute it under certain conditions. See the GNU General");
		System.out.println("Public License version 2 for more details.");
	}

	/**
	 * Gibt den Namen der Applikation zur&uuml;ck.
	 *
	 * @return Vollständiger Name der Applikation
	 */
	protected abstract String getApplikationName();

	/**
	 * Gibt den Copyrighthinweis der Applikation zur&uuml;ck.
	 * <p>
	 * Muster: Copyright (C) {Jahr} {Firma}
	 *
	 * @return Copyrighthinweis
	 */
	protected abstract String getCopyright();

	/**
	 * Gibt die Version der Applikation zur&uuml;ck.
	 *
	 * @return Versionsnummer bzw. Kennzeichen der Version
	 */
	protected abstract String getVersion();

}
