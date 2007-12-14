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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Polygonzug in WGS84-Koordinaten
 * 
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version
 *
 */
public class WGS84Polygon {

	/**
	 * Koordinatenliste
	 */
	private ArrayList<WGS84Punkt> _punkte;
	
	
	/**
	 * Konstruktor für Polygon mit WGS84-Koordinaten in Dezimalnotation.
	 *  
	 * Beispiel +4.354551 +50.839402 bedeutet 4°. 354551 O 50°. 839402 N
	 * 
	 * @param laenge
	 * @param breite
	 */
	public WGS84Polygon(double laenge[], double breite[]) {
		
		if( laenge.length != breite.length )
			throw new IllegalArgumentException ("Die Anzahl der Koordinaten für Länge und Breite muss übereinstimmen"); //$NON-NLS-1$

		_punkte = new ArrayList<WGS84Punkt>(laenge.length);
		
		for( int i=0; i< laenge.length; i++ ) {
			WGS84Punkt p = new WGS84Punkt( laenge[i], breite[i]);
			_punkte.add(p);
		}
	}

	/**
	 * Konstruktor für Polygon aus Liste von Punkten
	 *  
	 * @param punktliste 
	 */
	public WGS84Polygon(List<WGS84Punkt> punktliste) {
		
		_punkte = new ArrayList<WGS84Punkt>(punktliste);
	}


	/**
	 * Test, ob das Polygon gleich einem anderen Polygon ist, wobei eine bestimmte
	 * Abweichung der Koordinaten nicht &uuml;berschritten werden darf.
	 * 
	 * @param testpolygon Das zu testende Polgon
	 * @param maxabweichungGrad maximal zul&auml;ssige Abweichung in Grad
	 * @return true, wenn die Polygone im o.g. Sinne identisch sind, sonst false
	 */
	public boolean istIdentisch(WGS84Polygon testpolygon, double maxabweichungGrad) {
		double abstand_meter = 1;
		for( WGS84Punkt punkt : testpolygon._punkte ) {
			if(!liegtAufPolygon(punkt, abstand_meter) )
				return false;
		}	
		
		return true;
	}

	/**
	 * Test, ob ein Punkt auf dem Polygon liegt.
	 * 
	 * @param punkt
	 * @return true, wenn der Punkt auf dem Polygonzug liegt, sonst false
	 */
	public boolean liegtAufPolygon(WGS84Punkt punkt) {
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			if( punktLiegtAufStrecke(_punkte.get(i), _punkte.get(i+1), punkt))
				return true;
		}	
		
		return false;
	}
	
	/**
	 * Test, ob ein Punkt auf dem Polygon liegt.
	 * 
	 * @param punkt
	 * @param maxAbweichung maximal zul&auml;ssige Abweichung in m
	 * @return true, wenn der Punkt auf dem Polygonzug liegt, sonst false
	 */
	public boolean liegtAufPolygon(WGS84Punkt punkt, double maxAbweichung) {
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			if( punktLiegtAufStrecke(_punkte.get(i), _punkte.get(i+1), punkt, maxAbweichung))
				return true;
		}	
		
		return false;
	}
		
	/**
	 *  Test, ob ein Punkt auf einer Strecke liegt.
	 *  
	 * @param l1 Startpunkt der Strecke
	 * @param l2 Endpunkt der Strecke
	 * @param punkt Punkt
	 * @return true, wenn der Punkt auf der Strecke liegt, sonst false.
	 */
	public static boolean punktLiegtAufStrecke(WGS84Punkt l1, WGS84Punkt l2, WGS84Punkt punkt) {
		return punktAbstandStrecke(l1, l2, punkt) == 0.0;
	}
	
	/**
	 *  Test, ob ein Punkt mit einer zul&auml;ssigen Abweichung auf einer Strecke liegt.
	 *  
	 * @param l1 Startpunkt der Strecke
	 * @param l2 Endpunkt der Strecke
	 * @param punkt Punkt
	 * @param maxAbweichung maximal zul&auml;ssige Abweichung in m
	 * @return true, wenn der Punkt auf der Strecke liegt, sonst false.
	 */
	public static boolean punktLiegtAufStrecke(WGS84Punkt l1, WGS84Punkt l2, WGS84Punkt punkt, 
			double maxAbweichung) {
		return punktAbstandStrecke(l1, l2, punkt) <= maxAbweichung;
	}
	
	/**
	 * Bestimmt den Abstand eines Punktes von einer Strecke.
	 * 
	 * @param l1 Startpunkt der Strecke
	 * @param l2 Endpunkt der Strecke
	 * @param punkt Punkt
	 * @return Abstand des Punktes von der Strecke in Meter
	 */
	public static double punktAbstandStrecke(WGS84Punkt l1, WGS84Punkt l2, WGS84Punkt punkt) {
		
		Line2D.Double l2d = new Line2D.Double(l1.get_utm_x(), l1.get_utm_y(), l2.get_utm_x(), l2.get_utm_y());
		double abstand = l2d.ptSegDist(punkt.get_utm_x(), punkt.get_utm_y());
		
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(abstand * 1000.0)/1000.0;	
	}
	
	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laenge_exakt() {
		double laenge = 0;
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			laenge += WGS84Punkt.AbstandExakt(_punkte.get(i), _punkte.get(i+1));
			//laenge += WGS84Punkt.Abstand(_punkte.get(i), _punkte.get(i+1));
		}
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(laenge * 1000.0)/1000.0;	
	}
	
	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laenge_kartesisch() {
		double laenge = 0;
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			laenge += WGS84Punkt.AbstandKartesisch(_punkte.get(i), _punkte.get(i+1));
		}
		
		return laenge;
	}
	
	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laenge_appr() {
		double laenge = 0;
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			laenge += WGS84Punkt.Abstand(_punkte.get(i), _punkte.get(i+1));
		}
		
		return laenge;
	}
	

	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laenge() {
		return laenge_exakt();
	}
	
	
	/**
	 * Berechnet die Koordinaten der Abbildung eines Punktes auf eine Strecke. Die Strecke ist
	 * definiert durch einen Anfangs- und Endpunkt. Wenn der Punkt nicht auf der Strecke liegt, 
	 * wird die Bildpunkt-Koordinate durch das Lot vom Punkt auf die Strecke berechnet. Kann 
	 * kein Lot gef&auml;llt werden, wird als Ergebnis der Punkt der Linie zur&uuml;ckgeliefert,
	 * welcher dem Punkt am n&auml;chsten liegt, also entweder der Anfangs- oder der Endpunkt.
	 * 
	 * @param s1 der Anfangspunkt der Strecke
	 * @param s2 der Endpunkt der Strecke
	 * @param punkt der abzubildende Punkt
	 * 
	 * @return die Koordinaten des Bildpunktes
	 */
	public static WGS84Punkt bildPunktAufStrecke(WGS84Punkt s1, WGS84Punkt s2, WGS84Punkt punkt) {
		
		if(punktLiegtAufStrecke(s1, s2, punkt))
			return punkt;
		
		// TODO: was passiert bei Zonenwechsel???
		
		//	alle Berechnungen auf den kartesischen Koordinaten
		Point2D.Double ergebnis;
		Line2D.Double line = new Line2D.Double(s1.get_utm_x(), s1.get_utm_y(), s2.get_utm_x(), s2.get_utm_y());
		Point2D.Double point = new Point2D.Double(punkt.get_utm_x(), punkt.get_utm_y());

		if(istAbbildbar(line, point) == false) {
			// kann nicht abgebildet werden, benutze Anfangspunkt, der am naechsten liegt
			if(WGS84Punkt.Abstand(s1, punkt) <= WGS84Punkt.Abstand(s2, punkt))
				return new WGS84Punkt(s1);
			
			return new WGS84Punkt(s2);
		}
		
		if (line.x1 == line.x2) {
			//	die Strecke liegt auf der X-Koordinate, wir sparen uns die numerischen Ungenauigkeiten
			ergebnis = new Point2D.Double(line.x1, point.y);
		} else {
			// Hilfsdreieck
			double alpha = Math.atan((line.y2 - line.y1) /(line.x2 - line.x1));
			double gegenKathede = line.ptSegDist(point);
			double hypo = Math.sqrt(Math.pow(line.x1 - point.x, 2.0) + Math.pow(line.y1 - point.y, 2.0));
			
			// Laenge auf der Strecke bis zum Punkt
			double anKathede = Math.sqrt(Math.pow(hypo, 2.0) - Math.pow(gegenKathede, 2.0));
			
			ergebnis = berecheneBildPunkt( line, alpha, anKathede);
			
			// Ueberpruefung der Richtung
			if( !richtungOK(line, ergebnis) ) { 
				anKathede *=-1;
				ergebnis = berecheneBildPunkt( line, alpha, anKathede);
			}
		}
			
		// Ruecktransformation in Winkelkoordinaten
		// die Zone des ersten Streckenpunktes wird benutzt
		WGS84Koordinate w = GeoTransformation.UTM_nach_WGS84Punkt(
				new UTMKoordinate(ergebnis.x, ergebnis.y, s1.get_utm_zone()));
	
		return new WGS84Punkt(w);
		
	}

	
	
	/**
	 * Berechnet die Koordinaten des Punktes auf der Strecke, der einen gegebenen Offset 
	 * vom Anfangspunkt der Strecke entfernt ist.
	 * Die Strecke ist definiert durch einen Anfangs- und Endpunkt. Wenn der gegebene
	 * Offset gr&ouml;&szlig;er als die L&auml;nge der Strecke ist, wird eine 
	 * IllegalArgumentException geworfen. 
	 * 
	 * @param s1 der Anfangspunkt der Strecke
	 * @param s2 der Endpunkt der Strecke
	 * @param offset der Offset beginnend vom Anfang der Strecke, bei dem der Punkt liegen soll
	 * 
	 * @return Punkt bzw. IllegalArgumentException
	 */
	public static WGS84Punkt bildPunktAufStrecke(WGS84Punkt s1, WGS84Punkt s2, double offset) {
		if(WGS84Punkt.AbstandExakt(s1, s2) < offset)
			throw new IllegalArgumentException ("Der Offset ("+ offset + 
					") ist größer als die Streckenlänge (" + WGS84Punkt.AbstandExakt(s1, s2) +")."); //$NON-NLS-1$
	
		if(offset < 0.0)
			throw new IllegalArgumentException ("Der Offset muss positiv sein."); //$NON-NLS-1$
		
		//	alle Berechnungen auf den kartesischen Koordinaten
		Point2D.Double ergebnis;
		Line2D.Double line = new Line2D.Double(s1.get_utm_x(), s1.get_utm_y(), s2.get_utm_x(), s2.get_utm_y());
		double alpha = Math.atan((line.y2 - line.y1) /(line.x2 - line.x1));
		
		ergebnis = berecheneBildPunkt( line, alpha, offset);

		// Ueberpruefung der Richtung
		if( !richtungOK(line, ergebnis) ) { 
			ergebnis = berecheneBildPunkt( line, alpha, offset * -1);
		}
			
		// Ruecktransformation in Winkelkoordinaten
		// die Zone des ersten Streckenpunktes wird benutzt
		WGS84Koordinate w = GeoTransformation.UTM_nach_WGS84Punkt(
				new UTMKoordinate(ergebnis.x, ergebnis.y, s1.get_utm_zone()));
	
		return new WGS84Punkt(w);
		
	}
	
	/**
	 * Berechnet die Koordinaten des Punktes auf dem Polygonzug, der einen gegebenen 
	 * Offset vom Anfangspunkt entfernt ist.
	 * Wenn der gegebene Offset gr&ouml;&szlig;er als die L&auml;nge des Polygones ist, 
	 * wird eine IllegalArgumentException geworfen. 
	 * 
	 * @param offset der Offset beginnend vom Anfang des Polygones, bei dem der Punkt 
	 * liegen soll
	 * 
	 * @return Punkt bzw. IllegalArgumentException
	 */
	public WGS84Punkt bildPunkt(double offset) {
		if(laenge() < offset)
			throw new IllegalArgumentException ("Der Offset ist größer als die Polygonlänge"); //$NON-NLS-1$
		
		double laenge = 0.0;
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			double slaenge = WGS84Punkt.AbstandExakt(_punkte.get(i), _punkte.get(i+1));
			double tmplaenge = laenge+slaenge;
			tmplaenge = Math.round(tmplaenge*1000.0)/1000.0;
			if(tmplaenge >= offset)
				return WGS84Polygon.bildPunktAufStrecke(_punkte.get(i), _punkte.get(i+1), 
						Math.round((offset-laenge) * 1000.0)/1000.0);
			
			laenge += slaenge;
		}
		
		// this should not happen!!!
		throw new IllegalStateException ("Der Offset kann nicht auf das Polygon abgebildet werden"); //$NON-NLS-1$
	}
	
	/**
	 * Berechnet die Koordinaten der Abbildung eines Punktes auf das Polygon. Wenn der 
	 * Punkt nicht auf dem Polygon liegt, wird die Bildpunkt-Koordinate durch das Lot vom 
	 * Punkt auf den Streckenteil des Polygones mit dem kleinsten Abstand zum Punkt berechnet. 
	 * Kann kein Lot gef&auml;llt werden, wird als Ergebnis der Punkt der Linie zur&uuml;ckgeliefert,
	 * welcher dem Punkt am n&auml;chsten liegt, also entweder der Anfangs- oder der Endpunkt.
	 * 
	 * @param punkt der abzubildende Punkt 
	 * 
	 * @return Punkt bzw. IllegalArgumentException
	 */
	public WGS84Punkt bildPunkt(WGS84Punkt punkt) {
		if(liegtAufPolygon(punkt))
			return punkt;
		
		WGS84Polygon strecke = findeTeilstreckeKleinsterAbstand(punkt);
		
		if(strecke != null) {
			WGS84Punkt bp = punkt;
			int iterationen = 0;
			// iteriere um numerische Fehler bei grossem Abstand zu eliminieren
			do {
				bp = WGS84Polygon.bildPunktAufStrecke(strecke._punkte.get(0), strecke._punkte.get(1), bp);
				
				if(++iterationen > 3)
					throw new IllegalArgumentException ("Der Bildpunkt kann nicht genau bestimmt werden"); //$NON-NLS-1$
			}
			while(!liegtAufPolygon(bp));
			
			return bp;
		}
		
		throw new IllegalArgumentException ("Der Bildpunkt kann nicht bestimmt werden"); //$NON-NLS-1$
	}
	
	/**
	 * Berechnet den Offset eines Punktes auf dem Polygon. 
	 * 
	 * @param punkt Punkt, f&uuml;r den der Offset berechnet werden soll
	 * 
	 * @return Offset (in m) bzw. IllegalArgumentException
	 */
	public double berecheneOffset(WGS84Punkt punkt) {
		if(!liegtAufPolygon(punkt))
			throw new IllegalArgumentException ("Der Offset kann nicht bestimmt werden"); //$NON-NLS-1$

		double offset = 0.0;

		for( int i=0; i< _punkte.size()-1; i++ ) {
			if( WGS84Polygon.punktLiegtAufStrecke(_punkte.get(i), _punkte.get(i+1), punkt) ) {
				offset += WGS84Punkt.Abstand(punkt, _punkte.get(i));
				break;
			}

			offset += WGS84Punkt.Abstand(_punkte.get(i+1), _punkte.get(i));
		}
		
		return offset;
	}

	/**
	 * Schneidet den Anfangsteil des Polygones bis zur L&auml;nge des angegebenen Offsets ab
	 * und gibt diesen Teil zurück. Das Polygon wird um den entsprechenden Teil gekürzt. 
	 * Wenn der gegebene Offset gr&ouml;&szlig;er als die L&auml;nge des Polygones ist, 
	 * wird eine IllegalArgumentException geworfen. 
	 * 
	 * @param offset der Offset beginnend vom Anfang des Polygones, bei dem der Schnitt- 
	 * punkt liegen soll
	 * 
	 * @return Teil des Polygones bis zum Offset-Punkt oder IllegalArgumentException
	 */
	public WGS84Polygon anfangAbschneiden(double offset) {
		if(laenge() < offset)
			throw new IllegalArgumentException ("Der Offset ist größer als die Polygonlänge"); //$NON-NLS-1$
		
		if(laenge() == offset) {
			WGS84Polygon ret = new WGS84Polygon(this._punkte);
			this._punkte.clear();
			return ret;
		}
		
		WGS84Koordinate bk = bildPunkt(offset);
		WGS84Punkt bp = new WGS84Punkt(bk.get_laenge(), bk.get_breite());
		List<WGS84Punkt> apunkte = new ArrayList<WGS84Punkt>(); 

		int found = -1;
		for( int i=0; i< _punkte.size()-1; i++ ) {
			apunkte.add(_punkte.get(i));
			if( punktLiegtAufStrecke(_punkte.get(i), _punkte.get(i+1), bp, 0.001)) {
				found = i;
				break;
			}
		}
		
		if(found == -1)
			return null;

		// entferne Anfang von this
		for( WGS84Punkt p: apunkte ) {
			_punkte.remove(p);
		}
		// Bildpunkt an den Anfang
		_punkte.add(0, bp);

		// Bildpunkt an das Ende der Ergebnisliste
		apunkte.add(bp);
		
		// return Anfangsteil
		return new WGS84Polygon(apunkte);
	}
	
	/**
	 * Schneidet den Anfangsteil des Polygones bis zu einem gegebenen Punkt  ab
	 * und gibt diesen Teil zurück. Das Polygon wird um den entsprechenden Teil gekürzt. 
	 * Wenn der gegebene Punkt nicht auf dem Polygon liegt, wird eine 
	 * IllegalArgumentException geworfen. 
	 * 
	 * @param punkt  Schnittpunkt
	 * @return Teil des Polygones bis zum Offset-Punkt oder IllegalArgumentException
	 */
	public WGS84Polygon anfangAbschneiden(WGS84Punkt punkt) {
		if(! liegtAufPolygon(punkt, 0.001 ) )
			throw new IllegalArgumentException ("Der Punkt liegt nicht auf dem Polygon"); //$NON-NLS-1$
		
		List<WGS84Punkt> apunkte = new ArrayList<WGS84Punkt>(); 

		int found = -1;
		for( int i=0; i< _punkte.size()-1; i++ ) {
			apunkte.add(_punkte.get(i));
			if( punktLiegtAufStrecke(_punkte.get(i), _punkte.get(i+1), punkt, 0.001)) {
				found = i;
				break;
			}
		}
		
		if(found == -1)
			throw new IllegalArgumentException ("Der Punkt liegt nicht auf dem Polygon"); //$NON-NLS-1$


		// entferne Anfang von this
		for( WGS84Punkt p: apunkte ) {
			_punkte.remove(p);
		}
		// Bildpunkt an den Anfang
		_punkte.add(0, punkt);

		// Bildpunkt an das Ende der Ergebnisliste
		apunkte.add(punkt);
		
		// return Anfangsteil
		return new WGS84Polygon(apunkte);
	}
		
	/**
	 * sortiert das Polygon
	 */
	public void sort() {
		Collections.sort(_punkte, new Comparator<WGS84Punkt>() {
			public int compare(WGS84Punkt o1, WGS84Punkt o2) {
				return o1.compareTo(o2);
			}
		} );
	}
	
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		String ret = "WGS84-Polygon: [";
		for(WGS84Punkt p : _punkte)
			ret += "(" + p.get_laenge() + ", " + p.get_breite() + ") ";
		
		ret += "]";
		return ret;
	}

	/**
	 * Gibt die Koordinaten des Polygons als Punktliste zur&uuml;ck.
	 * 
	 * @return Punktkoordinaten
	 */
	public ArrayList<WGS84Punkt> getKoordinaten() {
		return _punkte;
	}
	
	/**
	 * Bestimmt den kleinsten Abstand eines Punktes vom Polygon
	 * 
	 * @param punkt Der Punkt, f&uuml;r den der Abstand bestimmt werden soll
	 * @return der kleinste Abstand des Punktes vom Polygon (in m)
	 */
	public double kleinsterPunktAbstand(WGS84Punkt punkt) {
		WGS84Polygon strecke = findeTeilstreckeKleinsterAbstand(punkt);
		
		if(strecke != null)
			return WGS84Polygon.punktAbstandStrecke(strecke._punkte.get(0), strecke._punkte.get(1), punkt);
		
		throw new IllegalArgumentException ("Der Abstand des Punktes kann nicht bestimmt werden"); //$NON-NLS-1$
	}
	
	
	/**
	 * Berechnet die Teilstrecke des Polygons, f&uuml;r die der Abstand eines gegebenen
	 * Punktes von dieser Strecke minimal ist.
	 * 
	 * @param punkt Punkt 
	 * @return gefundene Teilstrecke als Polygon oder null
	 */
	public WGS84Polygon findeTeilstreckeKleinsterAbstand(WGS84Punkt punkt) {
		double abstand = Double.MAX_VALUE;
		WGS84Punkt p1 = null, p2 = null;
		WGS84Polygon strecke = null;
		
		for( int i=0; i< _punkte.size()-1; i++ ) {
			double sabstand = WGS84Polygon.punktAbstandStrecke(_punkte.get(i), _punkte.get(i+1), punkt);
			if(sabstand < abstand) {
				abstand = sabstand;
				p1 = _punkte.get(i);
				p2 = _punkte.get(i+1);
			}
		}
		
		if( p1!=null && p2!=null) {
			WGS84Punkt punkte[] = {p1, p2};
			strecke = new WGS84Polygon(Arrays.asList(punkte));
		}
		
		return strecke;
	}

	/**
	 * Bestimmt, ob der Punkt in der korrekten Richtung erzeugt wurde.
	 * 
	 * @param line die Linie, auf die der Punkt abgebildet werden soll
	 * @param punkt der zu testende Punkt 
	 * @return ja/nein
	 */
	private static boolean richtungOK(Line2D.Double line, Point2D.Double punkt) {
		Rectangle2D r = line.getBounds2D();
		
		return r.contains(punkt);
	}

	/**
	 * Bestimmt, ob der Punkt auf die Strecke abbildbar ist, d.h. ob das Lot vom Punkt auf
	 * die Strecke zwischen den beiden Streckenpunkten liegt.
	 * 
	 * @param line
	 * @param point
	 * @return abbildbar ja/nein
	 */
	private static boolean istAbbildbar(Line2D.Double line, Point2D.Double point) {
		double ld = line.ptLineDist(point);
		double l = line.ptSegDist(point);
		
		// Bei den obigen Berechnungen treten numerische Fehler auf. Hier wird deshalb ein
		// zusaetzliches Kriterium definiert.
		double max_diff = 0.000001;
		double rel_fehler = (ld-l) / ld;
		
		return (line.ptLineDist(point) == line.ptSegDist(point)) || (rel_fehler < max_diff);
	}

	/**
	 * Berechnet die Koordinaten eines Punktes auf einer Linie mit einem Offset vom
	 * Anfangspunkt.
	 * 
	 * @param line Line
	 * @param alpha Anstiegswinkel
	 * @param laenge Offset des Punktes auf der Linie
	 * @return Punktkoordinaten
	 */
	private static Point2D.Double berecheneBildPunkt( Line2D.Double line, double alpha, double laenge) {
		double yl = Math.sin(alpha) * laenge;
		double xl = ((float) Math.cos(alpha)) * laenge;
	
		return new Point2D.Double(line.x1 + xl, line.y1 + yl);
	}
}
