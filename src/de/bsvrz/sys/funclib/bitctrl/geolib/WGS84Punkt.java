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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.geolib;

/**
 * geographischer Punkt in WGS84-Koordinaten.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id$
 * 
 */
public class WGS84Punkt extends WGS84Koordinate implements
		Comparable<WGS84Punkt> {

	/** $Auml;quatorradius der Erde (6378,137 km). */
	public static final double ERD_RADIUS_KM = 6378.137;

	/** Genauigkeit, mit der der Koordinaten verglichen werden. */
	public static final double GENAUIGKEIT_KOORDINATEN = 1000000.0;

	/**
	 * Abstandsberechnung zwischen 2 Punkten mit N‰herungsformel (idealisierte
	 * Erdkugel).
	 * 
	 * Formel: d=arccos(sin(X2)*sin(X1)+cos(X2)*cos(X1)*cos(Y2 - Y1)) *
	 * Erdradius
	 * 
	 * @param p1
	 *            erster Punkt
	 * @param p2
	 *            zweiter Punkt
	 * @return Abstand der Punkte in m
	 */
	public static double abstand(WGS84Punkt p1, WGS84Punkt p2) {

		double d = Math.acos(Math.sin(Math.toRadians(p1.getBreite()))
				* Math.sin(Math.toRadians(p2.getBreite()))
				+ Math.cos(Math.toRadians(p1.getBreite()))
				* Math.cos(Math.toRadians(p2.getBreite()))
				* Math.cos(Math.toRadians(p2.getLaenge())
						- Math.toRadians(p1.getLaenge())));

		// in m
		d *= ERD_RADIUS_KM * 1000;

		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(d * 1000.0) / 1000.0;
	}

	/**
	 * Abstandsberechnung zwischen 2 Punkten auf der Erdoberfl‰che mit exakter
	 * Formel.
	 * 
	 * (Algorithmus: http://de.wikipedia.org/wiki/Orthodrome)
	 * 
	 * b1 := Geografische Breite von Standort 1 l1 := Geografische L‰nge von
	 * Standort 1 b2 := Geografische Breite von Standort 2 l2 := Geografische
	 * L‰nge von Standort 2
	 * 
	 * f := Abplattung der Erde (1/298,257223563) a := ƒquatorradius der Erde
	 * (6378,14 km) F := (b1+b2)/2 G := (b1-b2)/2 l := (l1-l2)/2 S :=
	 * sin≤(G)cos≤(l) + cos≤(F)sin≤(l) C := cos≤(G)cos≤(l) + sin≤(F)sin≤(l) w :=
	 * arctan(sqrt(S/C)) in rad R := sqrt(S*C)/w D := 2*w*a H1 := (3R-1)/(2C) H2 :=
	 * (3R+1)/(2S)
	 * 
	 * Abstand: s := D(1 + f*H1*sin≤(F)cos≤(G) - f*H2*cos≤(F)sin≤(G))
	 * 
	 * @param p1
	 *            erster Punkt
	 * @param p2
	 *            zweiter Punkt
	 * @return Abstand der Punkte in m
	 */
	public static double abstandExakt(WGS84Punkt p1, WGS84Punkt p2) {

		double mF = Math.toRadians((p1.getBreite() + p2.getBreite()) / 2);
		double mG = Math.toRadians((p1.getBreite() - p2.getBreite()) / 2);
		double l = Math.toRadians((p1.getLaenge() - p2.getLaenge()) / 2);

		double mS = Math.pow(Math.sin(mG), 2) * Math.pow(Math.cos(l), 2)
				+ Math.pow(Math.cos(mF), 2) * Math.pow(Math.sin(l), 2);
		double mC = Math.pow(Math.cos(mG), 2) * Math.pow(Math.cos(l), 2)
				+ Math.pow(Math.sin(mF), 2) * Math.pow(Math.sin(l), 2);
		double w = Math.atan(Math.sqrt(mS / mC));
		double mR = Math.sqrt(mS * mC) / w;
		double mD = 2 * w * ERD_RADIUS_KM;
		double mH1 = (3 * mR - 1) / (2 * mC);
		double mH2 = (3 * mR + 1) / (2 * mS);

		double f = 1 / 298.257223563;

		double s = mD
				* (1 + (f * mH1 * Math.pow(Math.sin(mF), 2) * Math.pow(Math
						.cos(mG), 2)) - (f * mH2 * Math.pow(Math.cos(mF), 2) * Math
						.pow(Math.sin(mG), 2)));

		// in m
		s *= 1000;
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(s * 1000.0) / 1000.0;
	}

	/**
	 * Berechnet der Abstand der Punkte auf der Basis der transformierten
	 * kartesischen Koordinaten.
	 * 
	 * @param p1
	 *            erster Punkt
	 * @param p2
	 *            zweiter Punkt
	 * @return Abstand der Punkte in m
	 */
	public static double abstandKartesisch(WGS84Punkt p1, WGS84Punkt p2) {

		double a = Math.sqrt((Math.pow(p2.getUtmX() - p1.getUtmX(), 2))
				+ (Math.pow(p2.getUtmY() - p1.getUtmY(), 2)));

		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(a * 1000.0) / 1000.0;
	}

	/**
	 * liefert einen gerundeten Koordinatenwert.
	 * 
	 * @param wert
	 *            der Wert der gerundet werden soll
	 * @return der gerundete Wert
	 */
	public static double koordinateRunden(double wert) {
		long unscaledValue;

		// unscaledValue = Math.round(wert * (1 / conversionFactor));
		unscaledValue = Math.round(wert * GENAUIGKEIT_KOORDINATEN);

		// return Math.round(wert * GENAUIGKEIT_KOORDINATEN)
		// / GENAUIGKEIT_KOORDINATEN;

		return (unscaledValue / GENAUIGKEIT_KOORDINATEN);
	}

	/**
	 * der Punkt in UTM-Koordinaten.
	 */
	private final UTMKoordinate utmPunkt;

	/**
	 * Konstruktor f¸r Punkt mit WGS84-Koordinaten in Dezimalnotation.
	 * 
	 * Beispiel +4.354551 +50.839402 bedeutet 4∞. 354551 O 50∞. 839402 N
	 * 
	 * @param laenge
	 *            L&auml;nge
	 * @param breite
	 *            Breite
	 */
	public WGS84Punkt(double laenge, double breite) {
		// super(koordinateRunden(laenge), koordinateRunden(breite));
		super(laenge, breite);
		utmPunkt = GeoTransformation.wGS84nachUTM(laenge, breite);
	}

	/**
	 * Konstruktor.
	 * 
	 * @param w
	 *            Koordinate
	 */
	public WGS84Punkt(WGS84Koordinate w) {
		// super(koordinateRunden(w.getLaenge()),
		// koordinateRunden(w.getBreite()));
		super(w.getLaenge(), w.getBreite());
		utmPunkt = GeoTransformation.wGS84nachUTM(w.getLaenge(), w.getBreite());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(WGS84Punkt p) {
		return (getLaenge() > p.getLaenge() ? 1
				: getLaenge() < p.getLaenge() ? -1 : getBreite() > p
						.getBreite() ? 1 : getBreite() < p.getBreite() ? 1 : 0);
	}

	/**
	 * Testet auf gleiche Punkte mit einer maximalen Abweichung.
	 * 
	 * @param obj
	 *            Objekt
	 * @param maxAbweichung
	 *            max. zul. Abweichung
	 * @return true wenn gleich sonst false
	 */
	public boolean equals(Object obj, double maxAbweichung) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		if (obj instanceof WGS84Punkt) {
			WGS84Punkt p = (WGS84Punkt) obj;
			return ((Math
					.abs((koordinateRunden(getLaenge()) - koordinateRunden(p
							.getLaenge()))) < maxAbweichung) && (Math
					.abs((koordinateRunden(getBreite()) - koordinateRunden(p
							.getBreite()))) < maxAbweichung));
		}

		return false;
	}

	/**
	 * Gibt die nach UTM transformierte x-Koordinate (Rechtswert) zur&uuml;ck.
	 * 
	 * @return kartesische x-Koordinate des Punktes
	 */
	public double getUtmX() {
		return utmPunkt.getX();
	}

	/**
	 * Gibt die nach UTM transformierte y-Koordinate (Hochwert) zur&uuml;ck.
	 * 
	 * @return kartesische y-Koordinate des Punktes
	 */
	public double getUtmY() {
		return utmPunkt.getY();
	}

	/**
	 * Gibt die Zone der UTM-Transformation an.
	 * 
	 * @return Zonen-Nummer
	 */
	public int getUTMZone() {
		return utmPunkt.getZone();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "Punkt in WGS84-Koordinaten: L‰nge: " + getLaenge()
				+ ", Breite: " + getBreite();
	}
}
