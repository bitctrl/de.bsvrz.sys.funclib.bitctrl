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
 * Beschreibt Anforderungen die an die Sicherheit eines Passworts gestellt
 * werden.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class PasswortInfo {

	/** Die minimale Länge eines Passworts. */
	private long minLaenge = 1;

	/** Die Gültigkeitsdauer eines Passworts. */
	private long gueltiggeitsdauer;

	/**
	 * Soll bei der Neueingabe eines Passworts auf Ähnlichkeiten zu den
	 * Nutzerdaten geprüft werden?
	 */
	private boolean vergleicheBenutzerdaten;

	/**
	 * Sollen mindestens zwei Arten von Zeichen (Buchstaben, Zahlen,
	 * Sonderzeichen) enthalten sein?
	 */
	private boolean gemischteZeichen;

	/**
	 * Soll bei der Neueingabe eines Passworts auf Unterschiede zum zuletzt
	 * vergebenen geprüft werden?
	 */
	private boolean vergleichePasswort;

	/**
	 * Die maximale Anzahl fehlerhafter Anmeldeversuche bevor ein Terminal
	 * gesperrt wird.
	 */
	private long maxAnmeldeversuche;

	/**
	 * Die Blockierungszeit eines Terminals nach der Blockierung durch eine zu
	 * große Anzahl von fehlerhaften Anmeldeversuchen.
	 */
	private long blockierungszeit = 10;

	/**
	 * Die maximale Zeitdauer ohne Nutzeraktivität, bevor eine erneute
	 * Überprüfung der Nutzeranmeldung erfolgt.
	 */
	private long maxInaktivitaet = 10000;

	/**
	 * @return the minLaenge
	 */
	public long getMinLaenge() {
		return minLaenge;
	}

	/**
	 * @param minLaenge
	 *            the minLaenge to set
	 */
	public void setMinLaenge(final long minLaenge) {
		this.minLaenge = minLaenge;
	}

	/**
	 * @return the gueltiggeitsdauer
	 */
	public long getGueltiggeitsdauer() {
		return gueltiggeitsdauer;
	}

	/**
	 * @param gueltiggeitsdauer
	 *            the gueltiggeitsdauer to set
	 */
	public void setGueltiggeitsdauer(final long gueltiggeitsdauer) {
		this.gueltiggeitsdauer = gueltiggeitsdauer;
	}

	/**
	 * @return the vergleicheBenutzerdaten
	 */
	public boolean isVergleicheBenutzerdaten() {
		return vergleicheBenutzerdaten;
	}

	/**
	 * @param vergleicheBenutzerdaten
	 *            the vergleicheBenutzerdaten to set
	 */
	public void setVergleicheBenutzerdaten(
			final boolean vergleicheBenutzerdaten) {
		this.vergleicheBenutzerdaten = vergleicheBenutzerdaten;
	}

	/**
	 * @return the gemischteZeichen
	 */
	public boolean isGemischteZeichen() {
		return gemischteZeichen;
	}

	/**
	 * @param gemischteZeichen
	 *            the gemischteZeichen to set
	 */
	public void setGemischteZeichen(final boolean gemischteZeichen) {
		this.gemischteZeichen = gemischteZeichen;
	}

	/**
	 * @return the vergleichePasswort
	 */
	public boolean isVergleichePasswort() {
		return vergleichePasswort;
	}

	/**
	 * @param vergleichePasswort
	 *            the vergleichePasswort to set
	 */
	public void setVergleichePasswort(final boolean vergleichePasswort) {
		this.vergleichePasswort = vergleichePasswort;
	}

	/**
	 * @return the maxAnmeldeversuche
	 */
	public long getMaxAnmeldeversuche() {
		return maxAnmeldeversuche;
	}

	/**
	 * @param maxAnmeldeversuche
	 *            the maxAnmeldeversuche to set
	 */
	public void setMaxAnmeldeversuche(final long maxAnmeldeversuche) {
		this.maxAnmeldeversuche = maxAnmeldeversuche;
	}

	/**
	 * @return the blockierungszeit
	 */
	public long getBlockierungszeit() {
		return blockierungszeit;
	}

	/**
	 * @param blockierungszeit
	 *            the blockierungszeit to set
	 */
	public void setBlockierungszeit(final long blockierungszeit) {
		this.blockierungszeit = blockierungszeit;
	}

	/**
	 * @return the maxInaktivitaet
	 */
	public long getMaxInaktivitaet() {
		return maxInaktivitaet;
	}

	/**
	 * @param maxInaktivitaet
	 *            the maxInaktivitaet to set
	 */
	public void setMaxInaktivitaet(final long maxInaktivitaet) {
		this.maxInaktivitaet = maxInaktivitaet;
	}

}
