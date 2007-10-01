package de.bsvrz.sys.funclib.bitctrl.app;

import de.bsvrz.sys.funclib.application.StandardApplication;

/**
 * Implementiert nichts aus der Schnittstelle {@link StandardApplication},
 * f&uuml;gt aber n&uuml;tzliche Methoden der Applikation hinzu.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractStandardApplication implements
		StandardApplication {

	/**
	 * Gibt den Copyright-Hinweis laut GPL beim Starten aus.
	 */
	protected AbstractStandardApplication() {
		System.out.println(getApplikationName());
		System.out.println("Version " + getVersion());
		System.out
				.println("--------------------------------------------------------------------------------");
		System.out.println(getCopyright());
		System.out.println();
		System.out
				.println("This Application comes with ABSOLUTELY NO WARRANTY. This is free software, and");
		System.out
				.println("you are welcome to redistribute it under certain conditions. See the GNU General");
		System.out.println("Public License version 2 for more details.");
	}

	/**
	 * Gibt den Namen der Applikation zur&uuml;ck.
	 * 
	 * @return Vollständiger Name der Applikation
	 */
	protected abstract String getApplikationName();

	/**
	 * Gibt die Version der Applikation zur&uuml;ck.
	 * 
	 * @return Versionsnummer bzw. Kennzeichen der Version
	 */
	protected abstract String getVersion();

	/**
	 * Gibt den Copyrighthinweis der Applikation zur&uuml;ck.
	 * <p>
	 * Muster: Copyright (C) {Jahr} {Firma}
	 * 
	 * @return Copyrighthinweis
	 */
	protected abstract String getCopyright();

}
