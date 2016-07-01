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
 * Klasse zur Repr&auml;sentation einer Koordinate in WGS84.
 *
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */
public class WGS84Koordinate {

	/**
	 * kleinster zul&auml;ssiger Wert für die L&auml;nge.
	 */
	public static final double MIN_LAENGE = -180;

	/**
	 * gr&ouml;ßter zul&auml;ssiger Wert für die Lä&auml;nge.
	 */
	public static final double MAX_LAENGE = 180;

	/**
	 * kleinster zul&auml;ssiger Wert für die Breite.
	 */
	public static final double MIN_BREITE = -90;

	/**
	 * gr&ouml;ßter zul&auml;ssiger Wert für die Breite.
	 */
	public static final double MAX_BREITE = 90;

	/**
	 * L&auml;nge.
	 */
	private final double laenge;

	/**
	 * Breite.
	 */
	private final double breite;

	/**
	 * Konstruktor f&uuml;r eine WGS84-Koordinate.
	 *
	 * @param laenge
	 *            geographische L&auml;nge in Dezimalgrad
	 * @param breite
	 *            geographische Breite in Dezimalgrad
	 */
	public WGS84Koordinate(final double laenge, final double breite) {
		if (testBreite(breite)) {
			throw new IllegalArgumentException(
					"Der Wert für die Breite ist ungültig!");
		}

		if (testLaenge(laenge)) {
			throw new IllegalArgumentException(
					"Der Wert für die Länge ist ungültig!");
		}

		this.laenge = laenge;
		this.breite = breite;
	}

	/**
	 * Gibt die geographische Breite zur&uuml;ck.
	 *
	 * @return geographische Breite in Dezimalgrad
	 */
	public double getBreite() {
		return breite;
	}

	/**
	 * Gibt die geographische L&auml;nge zur&uuml;ck.
	 *
	 * @return geographische L&auml;nge in Dezimalgrad
	 */
	public double getLaenge() {
		return laenge;
	}

	/**
	 * Testet die Breite auf G&uuml;ltigkeit.
	 *
	 * @param tbreite
	 *            Breite
	 * @return true, wenn ok sonst false
	 */
	private boolean testBreite(final double tbreite) {
		return ((tbreite < MIN_BREITE) || (tbreite > MAX_BREITE));
	}

	/**
	 * Testet die L&auml;nge auf G&uuml;ltigkeit.
	 *
	 * @param tlaenge
	 *            L&auml;nge
	 * @return true, wenn ok sonst false
	 */
	private boolean testLaenge(final double tlaenge) {
		return ((tlaenge < MIN_LAENGE) || (tlaenge > MAX_LAENGE));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(breite);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(laenge);
		result = (prime * result) + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof WGS84Koordinate)) {
			return false;
		}
		final WGS84Koordinate other = (WGS84Koordinate) obj;
		if (Double.doubleToLongBits(breite) != Double.doubleToLongBits(other.breite)) {
			return false;
		}
		if (Double.doubleToLongBits(laenge) != Double.doubleToLongBits(other.laenge)) {
			return false;
		}
		return true;
	}
}
