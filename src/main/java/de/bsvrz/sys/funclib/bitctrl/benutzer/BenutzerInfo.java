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

package de.bsvrz.sys.funclib.bitctrl.benutzer;

/**
 * Beschreibt einen Benutzer.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class BenutzerInfo {

	/** Der Loginname. */
	private String loginname;

	/** Das Anmeldekennwort. */
	private String passwort;

	/** Der Nachname. */
	private String nachname;

	/** Der Vorname. */
	private String vorname;

	/** Der zweite Vorname, falls vorhanden. */
	private String zweiterVorname;

	/** Die Organisation (Firma, Behörde, ...). */
	private String organisation;

	/** Die E-Mail-Adresse. */
	private String emailAdresse;

	/**
	 * Gibt den Loginname zurück.
	 *
	 * @return der Loginname.
	 */
	public String getLoginname() {
		return loginname;
	}

	/**
	 * Legt den Loginname fest.
	 *
	 * @param loginname
	 *            der Loginname.
	 */
	public void setLoginname(final String loginname) {
		this.loginname = loginname;
	}

	/**
	 * Gibt das Anmeldekennwort zurück.
	 *
	 * @return das Anmeldekennwort.
	 */
	public String getPasswort() {
		return passwort;
	}

	/**
	 * Legt das Anmeldekennwort fest.
	 *
	 * @param passwort
	 *            das Anmeldekennwort.
	 */
	public void setPasswort(final String passwort) {
		this.passwort = passwort;
	}

	/**
	 * Gibt den Nachnamen zurück.
	 *
	 * @return der Nachname.
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * Legt den Nachnamen fest.
	 *
	 * @param nachname
	 *            der Nachname.
	 */
	public void setNachname(final String nachname) {
		this.nachname = nachname;
	}

	/**
	 * Gibt den Vornamen zurück.
	 *
	 * @return der Vorname.
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Legt den Vornamen fest.
	 *
	 * @param vorname
	 *            der Vorname.
	 */
	public void setVorname(final String vorname) {
		this.vorname = vorname;
	}

	/**
	 * Gibt den zweiten Vornamen zurück.
	 *
	 * @return der zweiten Vorname.
	 */
	public String getZweiterVorname() {
		return zweiterVorname;
	}

	/**
	 * Legt den zweiten Vornamen fest.
	 *
	 * @param zweiterVorname
	 *            der zweiten Vorname.
	 */
	public void setZweiterVorname(final String zweiterVorname) {
		this.zweiterVorname = zweiterVorname;
	}

	/**
	 * Gibt die Organisation (Firma, Behörde, ...) zurück.
	 *
	 * @return die Organisation.
	 */
	public String getOrganisation() {
		return organisation;
	}

	/**
	 * Legt die Organisation (Firma, Behörde, ...) fest.
	 *
	 * @param organisation
	 *            die Organisation.
	 */
	public void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	/**
	 * Gibt die E-Mail-Adresse zurück.
	 *
	 * @return die E-Mail-Adresse.
	 */
	public String getEmailAdresse() {
		return emailAdresse;
	}

	/**
	 * Legt die E-Mail-Adresse fest.
	 *
	 * @param emailAdresse
	 *            die E-Mail-Adresse.
	 */
	public void setEmailAdresse(final String emailAdresse) {
		this.emailAdresse = emailAdresse;
	}

	@Override
	public String toString() {
		String s;

		s = getClass().getName() + "[";
		s += "loginname=" + loginname;
		s += ", nachname=" + nachname;
		s += ", vorname=" + vorname;
		s += ", zweiterVorname=" + zweiterVorname;
		s += ", organisation=" + organisation;
		s += ", emailAdresse=" + emailAdresse;
		s += "]";

		return s;
	}

}
