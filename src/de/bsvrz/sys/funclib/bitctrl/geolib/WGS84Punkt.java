/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Wei&szlig;enfelser Stra&szlig;e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.geolib;

/**
 * geographischer Punkt in WGS84-Koordinaten
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version
 *
 */
public class WGS84Punkt  extends WGS84Koordinate implements Comparable<WGS84Punkt> {


	/** $Auml;quatorradius der Erde (6378,137 km)	*/
	public static final double ERD_RADIUS_KM = 6378.137;

	private UTMKoordinate _utmPunkt;

	/**
	 * Konstruktor für Punkt mit WGS84-Koordinaten in Dezimalnotation.
	 *  
	 * Beispiel +4.354551 +50.839402 bedeutet 4°. 354551 O 50°. 839402 N
	 * 
	 * @param laenge
	 * @param breite
	 */
	public WGS84Punkt(double laenge, double breite) {
		super( laenge, breite);
		_utmPunkt = GeoTransformation.WGS84_nach_UTM(laenge, breite);
	}
	
	
	/**
	 * @param w
	 */
	public WGS84Punkt(WGS84Koordinate w) {
		super( w.get_laenge(), w.get_breite());
		_utmPunkt = GeoTransformation.WGS84_nach_UTM(w.get_laenge(), w.get_breite());
	}


	/**
	 * Abstandsberechnung zwischen 2 Punkten mit Näherungsformel (idealisierte Erdkugel)
	 * 
	 * Formel: d=arccos(sin(X2)*sin(X1)+cos(X2)*cos(X1)*cos(Y2 - Y1)) * Erdradius
	 * 
	 * @param p1 erster Punkt
	 * @param p2 zweiter Punkt
	 * @return Abstand der Punkte in m
	 */
	public static double Abstand( WGS84Punkt p1, WGS84Punkt p2 ) {
		
		double d=Math.acos(
				Math.sin(Math.toRadians(p1.get_breite()))*Math.sin(Math.toRadians(p2.get_breite()))
				+Math.cos(Math.toRadians(p1.get_breite()))*Math.cos(Math.toRadians(p2.get_breite()))
				*Math.cos(Math.toRadians(p2.get_laenge()) - Math.toRadians(p1.get_laenge())));

		// in m
		d *= ERD_RADIUS_KM * 1000;
		
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(d * 1000.0)/1000.0;	
	}

	/**
	 * Abstandsberechnung zwischen 2 Punkten auf der Erdoberfläche mit exakter Formel
	 * 
	 * (Algorithmus: http://de.wikipedia.org/wiki/Orthodrome)
	 * 
	 *  b1 := Geografische Breite von Standort 1
	 *  l1 := Geografische Länge von Standort 1
	 *  b2 := Geografische Breite von Standort 2
	 *  l2 := Geografische Länge von Standort 2
	 *  
	 *  f := Abplattung der Erde (1/298,257223563)
	 *  a := Äquatorradius der Erde (6378,14 km)
	 *  F := (b1+b2)/2
	 *  G := (b1-b2)/2
	 *  l := (l1-l2)/2
	 *  S := sin²(G)cos²(l) + cos²(F)sin²(l)
	 *  C := cos²(G)cos²(l) + sin²(F)sin²(l)
	 *  w := arctan(sqrt(S/C)) in rad
	 *  R := sqrt(S*C)/w
	 *  D := 2*w*a
	 *  H1 := (3R-1)/(2C)
	 *  H2 := (3R+1)/(2S)
	 *  
	 *  Abstand:
	 *  s := D(1 + f*H1*sin²(F)cos²(G) - f*H2*cos²(F)sin²(G))
	 *
	 * @param p1 erster Punkt
	 * @param p2 zweiter Punkt
	 * @return Abstand der Punkte in m
	 */
	public static double AbstandExakt( WGS84Punkt p1, WGS84Punkt p2 ) {
		
		double F = Math.toRadians((p1.get_breite()+p2.get_breite())/2);
		double G = Math.toRadians((p1.get_breite()-p2.get_breite())/2);
		double l = Math.toRadians((p1.get_laenge()-p2.get_laenge())/2);
		
		double S = Math.pow( Math.sin(G), 2) * Math.pow( Math.cos(l), 2) + Math.pow( Math.cos(F), 2) * Math.pow( Math.sin(l), 2);
		double C = Math.pow( Math.cos(G), 2) * Math.pow( Math.cos(l), 2) + Math.pow( Math.sin(F), 2) * Math.pow( Math.sin(l), 2);
		double w = Math.atan(Math.sqrt(S/C));
		double R = Math.sqrt(S*C)/w;
		double D = 2*w*ERD_RADIUS_KM;
		double H1 = (3*R-1)/(2*C);
		double H2 = (3*R+1)/(2*S);
		
		 double f = 1/298.257223563;
		
		double s = D * (1 + ( f * H1 * Math.pow(Math.sin(F), 2) * Math.pow(Math.cos(G), 2)) - (f * H2 * Math.pow(Math.cos(F), 2) * Math.pow(Math.sin(G), 2) ) );
		
		// in m
		s *= 1000;
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(s * 1000.0)/1000.0;
	}

	
	/**
	 * Berechnet der Abstand der Punkte auf der Basis der transformierten 
	 * kartesischen Koordinaten
	 *
	 * @param p1 erster Punkt
	 * @param p2 zweiter Punkt
	 * @return Abstand der Punkte in m
	 */
	public static double AbstandKartesisch( WGS84Punkt p1, WGS84Punkt p2 ) {
		
		double a = Math.sqrt(
				( Math.pow( p2.get_utm_x() - p1.get_utm_x(), 2) ) + 
				( Math.pow(p2.get_utm_y() - p1.get_utm_y(), 2))  );
		
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(a * 1000.0)/1000.0;
	}

	
	/**
	 * Gibt die nach UTM transformierte y-Koordinate (Hochwert) zur&uuml;ck.
	 * 
	 * @return kartesische y-Koordinate des Punktes
	 */
	public double get_utm_y() {
		return _utmPunkt.get_y();
	}


	/**
	 * Gibt die nach UTM transformierte x-Koordinate (Rechtswert) zur&uuml;ck.
	 * 
	 * @return kartesische x-Koordinate des Punktes
	 */
	public double get_utm_x() {
		return _utmPunkt.get_x();
	}

	
	/**
	 * Gibt die Zone der UTM-Transformation an.
	 * 
	 * @return Zonen-Nummer
	 */
	public int get_utm_zone() {
		return _utmPunkt.get_zone();
	}
	
	
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		return "Punkt in WGS84-Koordinaten: Länge: " + get_laenge() + ", Breite: " + get_breite(); 
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WGS84Punkt) {
			WGS84Punkt p = (WGS84Punkt) obj;
			return ( ( get_laenge() == p.get_laenge() ) && ( get_breite() == p.get_breite() ) );
		}
		
		return false;
	}
	
	/**
	 * Testet auf gleiche Punkte mit einer maximalen Abweichung.
	 * 
	 * @param obj
	 * @param maxAbweichung
	 * @return
	 */
	public boolean equals(Object obj, double maxAbweichung) {
		if (obj instanceof WGS84Punkt) {
			WGS84Punkt p = (WGS84Punkt) obj;
			return ( ( ( get_laenge() - p.get_laenge() ) < maxAbweichung ) && 
						( ( get_breite() - p.get_breite() ) < maxAbweichung ) );
		}
		
		return false;
	}
	
	/* (Kein Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(WGS84Punkt p) {
		return ( get_laenge() > p.get_laenge() ?  1 :
					get_laenge() < p.get_laenge() ?  -1 :
						get_breite() > p.get_breite() ?  1 :
						get_breite() < p.get_breite() ?  1 :
							0 );
	}

}
