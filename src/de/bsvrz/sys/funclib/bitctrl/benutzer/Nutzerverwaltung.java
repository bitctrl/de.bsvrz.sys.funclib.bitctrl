/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
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

package de.bsvrz.sys.funclib.bitctrl.benutzer;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationTaskException;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.management.UserAdministration;
import de.bsvrz.sys.funclib.bitctrl.daf.DavTools;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Berechtigungsklasse;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Region;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Rolle;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdBenutzerParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdRollenRegionenPaareParameter;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdRollenRegionenPaareParameter.Daten.RolleRegionPaar;

/**
 * Verwaltet die Benutzer des Datenverteilers.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class Nutzerverwaltung {

	/** PID der Berechtigungsklasse ohne jede Zugriffsrechte. */
	public static final String PID_KEIN_ZUGRIFF = "berechtigungsklasse.keinZugriff";

	/** PID der Berechtigungsklasse mit allen Zugriffsrechten. */
	public static final String PID_ADMINISTRATOR = "berechtigungsklasse.administrator";

	/** PID der Zugriffsregion für alle Objekte. */
	public static final String PID_ROLLE_ALLES = "region.alles";

	/** Das Singleton der Klasse. */
	private static Nutzerverwaltung singleton;

	/**
	 * Gibt die einzige Instanz der Klasse zurück.
	 * 
	 * @return die Nutzerverwaltung als Singleton.
	 */
	public static Nutzerverwaltung getInstanz() {
		if (singleton == null) {
			singleton = new Nutzerverwaltung();
		}
		return singleton;
	}

	/** Der Loginname der als Administrator verwendet wird. */
	private String adminLoginname;

	/** Das Anmeldekennwort des Administrator. */
	private String adminPasswort;

	/**
	 * Konstruktor verstecken.
	 */
	private Nutzerverwaltung() {
		// nix zu tun
	}

	/**
	 * Legt den Benutzer fest, der als Administrator verwendet werden soll.
	 * 
	 * @param loginname
	 *            der Loginname der als Administrator verwendet wird.
	 * @param passwort
	 *            das Anmeldekennwort des Administrator.
	 * @see #clearAdmin()
	 */
	public void setAdmin(final String loginname, final String passwort) {
		adminLoginname = loginname;
		adminPasswort = passwort;
	}

	/**
	 * "Vergisst" die Anmeldung als Administrator.
	 * 
	 * @see #setAdmin(String, String)
	 */
	public void clearAdmin() {
		adminLoginname = null;
		adminPasswort = null;
	}

	/**
	 * Prüft, ob ein Bennutzer für Administratoraufgaben festgelegt wurde und
	 * dieser über ausreichend Rechte verfügt.
	 * 
	 * @return {@code true}, wenn ein Bennutzer für Administratoraufgaben
	 *         angegeben ist.
	 * @see #setAdmin(String, String)
	 */
	public boolean checkAdmin() {
		if (adminLoginname == null || adminPasswort == null) {
			return false;
		}

		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Benutzer benutzer = (Benutzer) factory.getModellobjekt(DavTools
				.generierePID(adminLoginname, Benutzer.PRAEFIX_PID));
		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.abrufenDatum();

		if (datum.isValid()) {
			return datum.getBerechtigungsklasse().getPid().equals(
					PID_ADMINISTRATOR);
		}

		return false;
	}

	/**
	 * Prüft ob ein bestimmter Benutzer zu einer bestimmten Berechtigungsklasse
	 * gehört.
	 * 
	 * @param benutzer
	 *            ein Benutzer.
	 * @param klasse
	 *            eine Berechtuigungsklasse.
	 * @return {@code true}, wenn der Benutzer zu der Berechtigungsklasse
	 *         gehört.
	 * @todo Diese Aktion muss der Dav ausführen, wenn er mit Rechteprüfung
	 *       gestartet wird.
	 */
	public boolean isBerechtigungsklasse(final Benutzer benutzer,
			final Berechtigungsklasse klasse) {
		if (benutzer == null) {
			throw new IllegalArgumentException(
					"Der Benutzer darf nicht null sein.");
		}
		if (klasse == null) {
			throw new IllegalArgumentException(
					"Die Berechtigungsklasse darf nicht null sein.");
		}

		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.abrufenDatum();

		if (datum.isValid()) {
			return datum.getBerechtigungsklasse().equals(klasse);
		}

		return false;
	}

	/**
	 * Prüft ob ein bestimmter Benutzer eine bestimmte Rolle in einer Region
	 * hat.
	 * 
	 * @param benutzer
	 *            ein Benutzer.
	 * @param rolle
	 *            eine Zugriffsrolle.
	 * @param region
	 *            eine Zugriffsregion.
	 * @return {@code true}, wenn der Benutzer in der Region die angegebene
	 *         Rolle hat.
	 */
	public boolean isRolleUndRegion(final Benutzer benutzer, final Rolle rolle,
			final Region region) {
		final PdBenutzerParameter paramBenutzer = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datumBenutzer = paramBenutzer
				.abrufenDatum();
		final Berechtigungsklasse klasse = datumBenutzer
				.getBerechtigungsklasse();
		final PdRollenRegionenPaareParameter paramPaare = klasse
				.getParameterDatensatz(PdRollenRegionenPaareParameter.class);
		final PdRollenRegionenPaareParameter.Daten datumPaare = paramPaare
				.abrufenDatum();

		for (final RolleRegionPaar paar : datumPaare) {
			if (region.equals(paar.getRegion())
					&& rolle.equals(paar.getRolle())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Suchte eine bestimmte Berechtigungsklasse.
	 * 
	 * @param pid
	 *            die PID einer Berechtigungsklasse.
	 * @return die Berechtigungsklasse oder {@code null}, wenn zu der PID keine
	 *         existiert.
	 */
	public Berechtigungsklasse sucheBerechtigungsklasse(final String pid) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Berechtigungsklasse) factory.getModellobjekt(pid);
	}

	/**
	 * Suchte eine bestimmte Zugriffsrolle.
	 * 
	 * @param pid
	 *            die PID einer Zugriffsrolle.
	 * @return die Zugriffsrolle oder {@code null}, wenn zu der PID keine
	 *         existiert.
	 */
	public Rolle sucheRolle(final String pid) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Rolle) factory.getModellobjekt(pid);
	}

	/**
	 * Suchte eine bestimmte Zugriffsregion.
	 * 
	 * @param pid
	 *            die PID einer Zugriffsregion.
	 * @return die Zugriffsregion oder {@code null}, wenn zu der PID keine
	 *         existiert.
	 */
	public Region sucheRegion(final String pid) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		return (Region) factory.getModellobjekt(pid);
	}

	/**
	 * Prüft ob ein bestimmter Benutzer existiert und gibt ihn zurück.
	 * 
	 * @param loginname
	 *            der eindeutige Benutzername (Loginname).
	 * @return der Benutzer oder {@code null}, wenn kein Benutzer mit dem
	 *         angegebenen Namen existiert.
	 */
	public Benutzer sucheBenutzer(final String loginname) {
		final ObjektFactory factory = ObjektFactory.getInstanz();

		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.BENUTZER
						.getPid())) {
			final Benutzer benutzer = (Benutzer) so;

			if (benutzer.getName().equals(loginname)) {
				return benutzer;
			}
		}

		return null;
	}

	/**
	 * Sucht alle Benutzer auf die bestimmte Kriterien zutreffen. Die
	 * Suchkriterien dürfen auch {@code null} sein, dies wird als Wildcard
	 * "alle" gedeutet. Die einzelnen Kriterien werden "oder"-verknüpft.
	 * 
	 * @param nachname
	 *            der Nachname der gesuchten Benutzer.
	 * @param vorname
	 *            der Vorname der gesuchten Benutzer.
	 * @param zweiterVorname
	 *            der zweite Vorname der gesuchten Benutzer.
	 * @param organisation
	 *            die Organisation der gesuchten Benutzer.
	 * @param email
	 *            die E-Mail-Adresse der gesuchten Benutzer.
	 * @return die Liste der gefundenen Benutzer, niemals {@code null}.
	 */
	public List<Benutzer> sucheBenutzer(final String nachname,
			final String vorname, final String zweiterVorname,
			final String organisation, final String email) {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final List<Benutzer> benutzer = new ArrayList<Benutzer>();

		for (final SystemObjekt so : factory
				.bestimmeModellobjekte(SystemModellGlobalTypen.BENUTZER
						.getPid())) {
			final Benutzer b = (Benutzer) so;
			boolean ok = false;

			if (nachname == null || nachname.equals(b.getNachname())) {
				ok = true;
			} else if (vorname == null || vorname.equals(b.getVorname())) {
				ok = true;
			} else if (organisation == null
					|| zweiterVorname.equals(b.getZweiterVorname())) {
				ok = true;
			} else if (organisation == null
					|| organisation.equals(b.getOrganisation())) {
				ok = true;
			} else if (email == null || email.equals(b.getEmailAdresse())) {
				ok = true;
			}

			if (ok) {
				benutzer.add(b);
			}
		}

		return benutzer;
	}

	/**
	 * Legt einen neuen Benutzer an.
	 * <p>
	 * <em>Hinweis:</em> Vor dem Aufruf dieser Methode muss mit
	 * {@link #setAdmin(String, String)} ein berechtigter Administrator
	 * festgelegt werden. Am Ende der Methode wird die Anmeldung als
	 * Administrator wieder aufgehoben.
	 * 
	 * @param loginname
	 *            der Loginname.
	 * @param passwort
	 *            das Anmeldekennwort.
	 * @param nachname
	 *            der Nachname des neuen Benutzers.
	 * @param vorname
	 *            der Vorname des neuen Benutzers.
	 * @param zweiterVorname
	 *            der zweite Vorname des neuen Benutzers.
	 * @param organisation
	 *            die Organsiation oder Firma des neuen Benutzers.
	 * @param emailAdresse
	 *            die E-Mail-Adresse des neuen Benutzers.
	 * @return der neue Benutzer
	 * @throws KeineRechteException
	 *             wenn kein Administrator festgelegt wurde oder dieser nicht
	 *             über genügend Rechte verfügt.
	 * @see #setAdmin(String, String)
	 * @todo Fehler weiterleiten: Nutzer existiert bereits, keine Rechte usw.
	 */
	public Benutzer anlegenBenutzer(final String loginname,
			final String passwort, final String nachname, final String vorname,
			final String zweiterVorname, final String organisation,
			final String emailAdresse) throws KeineRechteException {
		if (!checkAdmin()) {
			throw new KeineRechteException(
					"Zum Anlegen eines neuen Benutzer, müssen Sie sich als Administrator anmelden und über ausreichend Rechte verfügen.");
		}

		final String pid;
		Benutzer benutzer;

		pid = DavTools.generierePID(loginname, Benutzer.PRAEFIX_PID);
		try {
			benutzer = Benutzer.anlegen(pid, loginname, vorname,
					zweiterVorname, nachname, organisation, emailAdresse);

			final ObjektFactory factory = ObjektFactory.getInstanz();
			final DataModel modell = factory.getVerbindung().getDataModel();
			final UserAdministration userAdmin = modell.getUserAdministration();
			userAdmin.createNewUser(adminLoginname, adminPasswort, loginname,
					pid, passwort, false, factory.getVerbindung()
							.getLocalConfigurationAuthority()
							.getConfigurationArea().getPid());
		} catch (final ConfigurationChangeException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			benutzer = null;
		} catch (final ConfigurationTaskException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			benutzer = null;
		} finally {
			clearAdmin();
		}

		return benutzer;
	}

	/**
	 * Deaktiviert einen Benutzer. Seine Berechtigungsklasse wird auf "Kein
	 * Zugriff" gesetzt.
	 * 
	 * @param benutzer
	 *            der zu deaktivierende Benutzers.
	 * @param klasse
	 *            die zu setzende Berechtigungsklasse.
	 * @throws KeineRechteException
	 *             wenn kein Administrator festgelegt wurde oder dieser nicht
	 *             über genügend Rechte verfügt.
	 */
	public void setBerechtigungsklasse(final Benutzer benutzer,
			final Berechtigungsklasse klasse) throws KeineRechteException {
		if (benutzer == null) {
			throw new IllegalArgumentException(
					"Der Benutzer darf nicht null sein.");
		}

		if (!checkAdmin()) {
			throw new KeineRechteException(
					"Zum Anlegen eines neuen Benutzer, müssen Sie sich als Administrator anmelden und überausreichend Rechte verfügen.");
		}

		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.erzeugeDatum();
		datum.setBerechtigungsklasse(klasse);
		try {
			parameter.anmeldenSender();
			parameter.sendeDaten(datum);
		} catch (final AnmeldeException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (final DatensendeException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} finally {
			parameter.abmeldenSender();
		}

		clearAdmin();
	}

	/**
	 * Deaktiviert einen Benutzer. Seine Berechtigungsklasse wird auf "Kein
	 * Zugriff" gesetzt.
	 * 
	 * @param benutzer
	 *            der zu deaktivierende Benutzers.
	 * @throws KeineRechteException
	 *             wenn kein Administrator festgelegt wurde oder dieser nicht
	 *             über genügend Rechte verfügt.
	 */
	public void deaktiviereBenutzer(final Benutzer benutzer)
			throws KeineRechteException {
		if (benutzer == null) {
			throw new IllegalArgumentException(
					"Der Benutzer darf nicht null sein.");
		}

		if (!checkAdmin()) {
			throw new KeineRechteException(
					"Zum Anlegen eines neuen Benutzer, müssen Sie sich als Administrator anmelden und überausreichend Rechte verfügen.");
		}

		final ObjektFactory factory = ObjektFactory.getInstanz();
		final Berechtigungsklasse klasse = (Berechtigungsklasse) factory
				.getModellobjekt(PID_KEIN_ZUGRIFF);
		final PdBenutzerParameter parameter = benutzer
				.getParameterDatensatz(PdBenutzerParameter.class);
		final PdBenutzerParameter.Daten datum = parameter.erzeugeDatum();
		datum.setBerechtigungsklasse(klasse);
		try {
			parameter.anmeldenSender();
			parameter.sendeDaten(datum);
		} catch (final AnmeldeException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (final DatensendeException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} finally {
			parameter.abmeldenSender();
		}

		clearAdmin();
	}

}
