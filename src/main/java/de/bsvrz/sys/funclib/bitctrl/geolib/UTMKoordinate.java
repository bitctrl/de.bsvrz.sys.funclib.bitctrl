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

package de.bsvrz.sys.funclib.bitctrl.geolib;

/**
 * Klasse zur Repr&auml;sentation einer Koordinate im UTM (Universal Transverse
 * Mercator) Koordinatensystem.
 *
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */
public class UTMKoordinate {

	/**
	 * Konstantendefinition f&uuml;r die Hemisph&auml;re.
	 */
	public static final class UTMHEMI {

		/**
		 * Konstante für die n&ouml;rdliche Hemisph&auml;re.
		 */
		public static final UTMHEMI HORDHALBKUGEL = new UTMHEMI(0);

		/**
		 * Konstante für die s&uuml;dliche Hemisph&auml;re.
		 */
		public static final UTMHEMI SUEDHALBKUGEL = new UTMHEMI(1);

		/**
		 * Hemisph&auml;re.
		 */
		@SuppressWarnings("unused")
		private final int hemi;

		/**
		 * Nicht &ouml;ffentlicher Konstruktor der zum Erzeugen der
		 * vordefinierten Werte benutzt wird.
		 *
		 * @param wert
		 *            Wert
		 */
		private UTMHEMI(final int wert) {
			hemi = wert;
		}
	}

	/**
	 * Konstante f&uuml;r die kleinste erlaubte Zone.
	 */
	private static final int UTM_ZONE_MIN = 1;

	/**
	 * Konstante f&uuml;r die gr&ouml;&szlig;te erlaubte Zone.
	 */
	private static final int UTM_ZONE_MAX = 60;

	/**
	 * der Rechtswert (X-Koordinate).
	 */
	private final double xwert;

	/**
	 * der Hochwert (Y-Koordinate).
	 */
	private final double ywert;

	/**
	 * die Zone (1-60).
	 */
	private final int utmzone;

	/**
	 * die Hemisphäre.
	 */
	private UTMHEMI utmhemisphaere = UTMHEMI.HORDHALBKUGEL;

	/**
	 * Konstruktor f&uuml;r eine UTM-Koordinate auf der n&ouml;rdlichen
	 * Erdhalbkugel.
	 *
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param zone
	 *            Zone
	 */
	public UTMKoordinate(final double x, final double y, final int zone) {
		if (testZone(zone)) {
			throw new IllegalArgumentException(
					"Der Wert für die Zone ist ungültig!");
		}

		xwert = x;
		ywert = y;
		utmzone = zone;
		utmhemisphaere = UTMHEMI.HORDHALBKUGEL;
	}

	/**
	 * Konstruktor f&uuml;r eine UTM-Koordinate.
	 *
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 * @param zone
	 *            Zone
	 * @param hemisphaere
	 *            die Erdhalbkugel
	 */
	public UTMKoordinate(final double x, final double y, final int zone,
			final UTMHEMI hemisphaere) {
		if (testZone(zone)) {
			throw new IllegalArgumentException(
					"Der Wert für die Zone ist ungültig!");
		}

		xwert = x;
		ywert = y;
		utmzone = zone;
		utmhemisphaere = hemisphaere;
	}

	/**
	 * Gibt die Hemisph&auml;re zur&uuml;ck.
	 *
	 * @return Hemisph&auml;re.
	 */
	public UTMHEMI getHemisphaere() {
		return utmhemisphaere;
	}

	/**
	 * Gibt die X-Koordinate (Rechtswert) zur&uuml;ck.
	 *
	 * @return X-Koordinate
	 */
	public double getX() {
		return xwert;
	}

	/**
	 * Gibt die Y-Koordinate (Rechtswert) zur&uuml;ck.
	 *
	 * @return Y-Koordinate
	 */
	public double getY() {
		return ywert;
	}

	/**
	 * Gibt die Zone zur&uuml;ck.
	 *
	 * @return Zone
	 */
	public int getZone() {
		return utmzone;
	}

	/**
	 * Setzt die Hemisph&auml;re.
	 *
	 * @param hemisphaere
	 *            Hemisph&auml;re
	 */
	public void setHemisphaere(final UTMHEMI hemisphaere) {
		utmhemisphaere = hemisphaere;
	}

	/**
	 * Testfunktion f&uuml;r die Zone.
	 *
	 * @param zone
	 *            Zone
	 * @return true wenn ok sonst false
	 */
	private boolean testZone(final int zone) {
		return ((zone < UTM_ZONE_MIN) || (zone > UTM_ZONE_MAX));
	}

}
