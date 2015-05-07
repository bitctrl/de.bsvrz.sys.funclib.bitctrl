/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.geolib;

/**
 * Klasse zur Repr&auml;sentation einer Koordinate in WGS84.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */
public class WGS84Koordinate {

	/**
	 * Kleinster zul&auml;ssiger Wert f�r die L&auml;nge.
	 */
	public static final double MIN_LAENGE = -180;

	/**
	 * Gr&ouml;�ter zul&auml;ssiger Wert f�r die L�&auml;nge.
	 */
	public static final double MAX_LAENGE = 180;

	/**
	 * Kleinster zul&auml;ssiger Wert f�r die Breite.
	 */
	public static final double MIN_BREITE = -90;

	/**
	 * Gr&ouml;�ter zul&auml;ssiger Wert f�r die Breite.
	 */
	public static final double MAX_BREITE = 90;

	/**
	 * L&auml;nge.
	 */
	private double laenge;

	/**
	 * Breite.
	 */
	private double breite;

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
					"Der Wert f�r die Breite ist ung�ltig!");
		}

		if (testLaenge(laenge)) {
			throw new IllegalArgumentException(
					"Der Wert f�r die L�nge ist ung�ltig!");
		}

		this.laenge = laenge;
		this.breite = breite;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final WGS84Koordinate other = (WGS84Koordinate) obj;
		if (Double.doubleToLongBits(breite) != Double
				.doubleToLongBits(other.breite)) {
			return false;
		}
		if (Double.doubleToLongBits(laenge) != Double
				.doubleToLongBits(other.laenge)) {
			return false;
		}
		return true;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(breite);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(laenge);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Setzt die geographische Breite.
	 *
	 * @param breite
	 *            neue geographische Breite in Dezimalgrad
	 * @deprecated Die Breite wird im Konstruktor gesetzt und sollte nicht mehr
	 *             ver�ndert werden. Andernfalls kann die Klassenkapselung
	 *             verletzt werden (Java �bergibt Parameter immer per
	 *             Referenz!).
	 */
	@Deprecated
	public void setBreite(final double breite) {
		if (testBreite(breite)) {
			throw new IllegalArgumentException(
					"Der Wert f�r die Breite ist ung�ltig!");
		}

		this.breite = breite;
	}

	/**
	 * Setzt die geographische L&auml;nge.
	 *
	 * @param laenge
	 *            neue geographische L&auml;nge in Dezimalgrad
	 * @deprecated Die L�nge wird im Konstruktor gesetzt und sollte nicht mehr
	 *             ver�ndert werden. Andernfalls kann die Klassenkapselung
	 *             verletzt werden (Java �bergibt Parameter immer per
	 *             Referenz!).
	 */
	@Deprecated
	public void setLaenge(final double laenge) {
		if (testLaenge(laenge)) {
			throw new IllegalArgumentException(
					"Der Wert f�r die L�nge ist ung�ltig!");
		}

		this.laenge = laenge;
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
	public String toString() {
		String result = "WGS84-Koordinate";
		result += "[";
		result += "laenge=" + laenge;
		result += ", breite=" + breite;
		result += "]";
		return result;
	}

}
