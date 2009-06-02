/*---------------------------------------------------------------*/
/* Copyright by BitCtrl Systems Leipzig */
/* BitCtrl Systems Leipzig */
/* Weisenfelser Str. 67 */
/* 04229 Leipzig */
/* Tel.: +49 341 49067 - 0 */
/* Fax.: +49 341 49067 - 15 */
/* mailto:info@bitctrl.de */
/* http://www.bitctrl.de */
/*---------------------------------------------------------------*/
package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte;

import de.bsvrz.sys.funclib.bitctrl.benutzer.Benutzerverwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.attributlisten.Urlasser;

/**
 * Ein Singleton-Objekt, welches ein korrekt initialisiertes {@link Urlasser}-Objekt
 * bereitstellt, welches man zum Versenden von Betriebsmeldungen auf Serverseite
 * verwenden kann.
 * 
 * @author BitCtrl Systems GmbH, uhlmann
 * @version $Id$
 */
public final class ServerProgrammeUrlasser {
	
	/**
	 * das Singleton.
	 */
	private static ServerProgrammeUrlasser instance;

	/**
	 * Der Login-Name des Benutzers für die Serverprogramme.
	 */
	private static final String SERVER_NUTZER_LOGIN_NAME = "Server";

	/**
	 * das bereitgestellt {@link Urlasser}-Objekt.
	 */
	private final Urlasser urlasser;

	/**
	 * Die Hauptmethode, die das {@link Urlasser}-Objekt bereitstellt und es
	 * mit der aktuellen Ursache versieht.
	 * 
	 * @param ursache die aktuell zu verwendende Ursache.
	 * @return der Urlasser
	 */
	public static Urlasser getUrlasser(final String ursache) {
		if (instance == null) {
			instance = new ServerProgrammeUrlasser();
		}
		if (ursache != null) {
			instance.getUrlasser().setUrsache(ursache);
		}
		return instance.getUrlasser();
	}

	/**
	 * Der Konstruktor ist privat, da es keine öffentlichen Instanzen gibt.
	 */
	private ServerProgrammeUrlasser() {
		urlasser = new Urlasser();
		Benutzer benutzer = Benutzerverwaltung.getInstanz().getBenutzer(SERVER_NUTZER_LOGIN_NAME);
		if (benutzer == null) {
			throw new RuntimeException("Der Benutzer mit dem Login-Namen '" + SERVER_NUTZER_LOGIN_NAME + "' ist nicht definiert");
		}
		urlasser.setBenutzer(benutzer);
		urlasser.setVeranlasser(benutzer.getName());
	}

	/**
	 * Liefert das zugrunde liegende {@link Urlasser}-Objekt.
	 * 
	 * @return der Urlasser
	 */
	public Urlasser getUrlasser() {
		return urlasser;
	}
}
