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

package de.bsvrz.sys.funclib.bitctrl.geolib;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Polygonzug in WGS84-Koordinaten.
 * 
 * Alle Längenangaben für Offsets u.&nbsp;ä. werden, wenn nicht anders
 * angegeben, in Meter notiert.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id: WGS84Polygon.java 9032 2008-05-21 08:32:43Z gieseler $
 */
public class WGS84Polygon {

	/**
	 * Berechnet die Koordinaten eines Punktes auf einer Linie mit einem Offset
	 * vom Anfangspunkt.
	 * 
	 * @param line
	 *            Line
	 * @param alpha
	 *            Anstiegswinkel
	 * @param laenge
	 *            Offset des Punktes auf der Linie in Meter.
	 * @return Punktkoordinaten
	 */
	private static strictfp Point2D.Double berecheneBildPunkt(
			final Line2D.Double line, final double alpha, final double laenge) {
		final double yl = Math.sin(alpha) * laenge;
		final double xl = ((float) Math.cos(alpha)) * laenge;

		// XXX Auskommentierten Quelltext löschen?
		// Test
		// yl = Math.round(yl * 100000000.0)/100000000.0;
		// xl = Math.round(xl * 100000000.0)/100000000.0;

		return new Point2D.Double(line.x1 + xl, line.y1 + yl);
	}

	/**
	 * Berechnet die Koordinaten des Punktes auf der Strecke, der einen
	 * gegebenen Offset vom Anfangspunkt der Strecke entfernt ist. Die Strecke
	 * ist definiert durch einen Anfangs- und Endpunkt. Wenn der gegebene Offset
	 * gr&ouml;&szlig;er als die L&auml;nge der Strecke ist, wird eine
	 * IllegalArgumentException geworfen.
	 * 
	 * @param s1
	 *            der Anfangspunkt der Strecke
	 * @param s2
	 *            der Endpunkt der Strecke
	 * @param offset
	 *            der Offset (in Meter) beginnend vom Anfang der Strecke, bei
	 *            dem der Punkt liegen soll
	 * 
	 * @return der Punkt.
	 * @throws IllegalArgumentException
	 */
	public static WGS84Punkt bildPunktAufStrecke(final WGS84Punkt s1,
			final WGS84Punkt s2, final double offset) {
		if (WGS84Punkt.abstandExakt(s1, s2) < offset) {
			throw new IllegalArgumentException("Der Offset (" + offset
					+ ") ist größer als die Streckenlänge ("
					+ WGS84Punkt.abstandExakt(s1, s2) + ").");
		}

		if (offset < 0.0) {
			throw new IllegalArgumentException("Der Offset muss positiv sein.");
		}

		// alle Berechnungen auf den kartesischen Koordinaten
		Point2D.Double ergebnis;
		final UTMKoordinate utm1 = s1.toUTMKoordinate();
		final UTMKoordinate utm2 = s2.toUTMKoordinate();
		final Line2D.Double line = new Line2D.Double(utm1.getX(), utm1.getY(),
				utm2.getX(), utm2.getY());
		final double alpha = Math.atan((line.y2 - line.y1)
				/ (line.x2 - line.x1));

		ergebnis = berecheneBildPunkt(line, alpha, offset);

		// Ueberpruefung der Richtung
		if (!richtungOK(line, ergebnis)) {
			ergebnis = berecheneBildPunkt(line, alpha, offset * -1);
		}

		// Ruecktransformation in Winkelkoordinaten
		// die Zone des ersten Streckenpunktes wird benutzt
		final WGS84Koordinate w = GeoTransformation
				.uTMnachWGS84Punkt(new UTMKoordinate(ergebnis.x, ergebnis.y, s1
						.toUTMKoordinate().getZone()));

		return new WGS84Punkt(w);

	}

	/**
	 * Berechnet die Koordinaten der Abbildung eines Punktes auf eine Strecke.
	 * Die Strecke ist definiert durch einen Anfangs- und Endpunkt. Wenn der
	 * Punkt nicht auf der Strecke liegt, wird die Bildpunkt-Koordinate durch
	 * das Lot vom Punkt auf die Strecke berechnet. Kann kein Lot gef&auml;llt
	 * werden, wird als Ergebnis der Punkt der Linie zur&uuml;ckgeliefert,
	 * welcher dem Punkt am n&auml;chsten liegt, also entweder der Anfangs- oder
	 * der Endpunkt.
	 * 
	 * @param s1
	 *            der Anfangspunkt der Strecke
	 * @param s2
	 *            der Endpunkt der Strecke
	 * @param punkt
	 *            der abzubildende Punkt
	 * 
	 * @return die Koordinaten des Bildpunktes
	 */
	public static WGS84Punkt bildPunktAufStrecke(final WGS84Punkt s1,
			final WGS84Punkt s2, final WGS84Punkt punkt) {

		if (punktLiegtAufStrecke(s1, s2, punkt)) {
			return punkt;
		}

		// TODO: was passiert bei Zonenwechsel???

		if (istAbbildbar(s1, s2, punkt)) {
			// kann nicht abgebildet werden, benutze Anfangspunkt, der am
			// naechsten liegt
			if (WGS84Punkt.abstand(s1, punkt) <= WGS84Punkt.abstand(s2, punkt)) {
				return new WGS84Punkt(s1);
			}

			return new WGS84Punkt(s2);
		}

		// alle Berechnungen auf den kartesischen Koordinaten
		Point2D.Double ergebnis;
		final UTMKoordinate utm1 = s1.toUTMKoordinate();
		final UTMKoordinate utm2 = s2.toUTMKoordinate();
		final UTMKoordinate utmPunkt = punkt.toUTMKoordinate();
		final Line2D.Double line = new Line2D.Double(utm1.getX(), utm1.getY(),
				utm2.getX(), utm2.getY());
		final Point2D.Double point = new Point2D.Double(utmPunkt.getX(),
				utmPunkt.getY());

		if (line.x1 == line.x2) {
			// die Strecke liegt auf der X-Koordinate, wir sparen uns die
			// numerischen Ungenauigkeiten
			ergebnis = new Point2D.Double(line.x1, point.y);
			// Ruecktransformation in Winkelkoordinaten
			// die Zone des ersten Streckenpunktes wird benutzt
			final WGS84Koordinate w = GeoTransformation
					.uTMnachWGS84Punkt(new UTMKoordinate(ergebnis.x,
							ergebnis.y, utm1.getZone()));

			return new WGS84Punkt(w);
		}

		final double hoehe = line.ptLineDist(point);
		final double hypo = WGS84Punkt.abstandExakt(s1, punkt);

		// Laenge auf der Strecke bis zum Punkt
		final double fusspunktlaenge = Math.sqrt(Math.pow(hypo, 2.0)
				- Math.pow(hoehe, 2.0));

		return WGS84Polygon.bildPunktAufStrecke(s1, s2, fusspunktlaenge);
	}

	/**
	 * Berechnet die Koordinaten der Abbildung eines Punktes auf eine Strecke.
	 * Die Strecke ist definiert durch einen Anfangs- und Endpunkt. Wenn der
	 * Punkt nicht auf der Strecke liegt, wird die Bildpunkt-Koordinate durch
	 * das Lot vom Punkt auf die Strecke berechnet. Kann kein Lot gef&auml;llt
	 * werden, wird als Ergebnis der Punkt der Linie zur&uuml;ckgeliefert,
	 * welcher dem Punkt am n&auml;chsten liegt, also entweder der Anfangs- oder
	 * der Endpunkt.
	 * 
	 * @param s1
	 *            der Anfangspunkt der Strecke
	 * @param s2
	 *            der Endpunkt der Strecke
	 * @param punkt
	 *            der abzubildende Punkt
	 * 
	 * @return die Koordinaten des Bildpunktes
	 */
	// XXX Auskommentierten Quelltext löschen?
	// public static WGS84Punkt bildPunktAufStrecke_old(WGS84Punkt s1,
	// WGS84Punkt s2, WGS84Punkt punkt) {
	//
	// if (punktLiegtAufStrecke(s1, s2, punkt)) {
	// return punkt;
	// }
	//
	// // TODO: was passiert bei Zonenwechsel???
	//
	// // alle Berechnungen auf den kartesischen Koordinaten
	// Point2D.Double ergebnis;
	// Line2D.Double line = new Line2D.Double(s1.get_utm_x(), s1.get_utm_y(),
	// s2.get_utm_x(), s2.get_utm_y());
	// Point2D.Double point = new Point2D.Double(punkt.get_utm_x(), punkt
	// .get_utm_y());
	//
	// if (istAbbildbar(line, point) == false) {
	// // kann nicht abgebildet werden, benutze Anfangspunkt, der am
	// // naechsten liegt
	// if (WGS84Punkt.Abstand(s1, punkt) <= WGS84Punkt.Abstand(s2, punkt)) {
	// return new WGS84Punkt(s1);
	// }
	//
	// return new WGS84Punkt(s2);
	// }
	//
	// if (line.x1 == line.x2) {
	// // die Strecke liegt auf der X-Koordinate, wir sparen uns die
	// // numerischen Ungenauigkeiten
	// ergebnis = new Point2D.Double(line.x1, point.y);
	// } else {
	// // Hilfsdreieck
	// double alpha = Math.atan((line.y2 - line.y1) / (line.x2 - line.x1));
	// double gegenKathede = line.ptSegDist(point);
	// double hypo = Math.sqrt(Math.pow(line.x1 - point.x, 2.0)
	// + Math.pow(line.y1 - point.y, 2.0));
	//
	// // Laenge auf der Strecke bis zum Punkt
	// double anKathede = Math.sqrt(Math.pow(hypo, 2.0)
	// - Math.pow(gegenKathede, 2.0));
	//
	// ergebnis = berecheneBildPunkt(line, alpha, anKathede);
	//
	// // Ueberpruefung der Richtung
	// if (!richtungOK(line, ergebnis)) {
	// anKathede *= -1;
	// ergebnis = berecheneBildPunkt(line, alpha, anKathede);
	// }
	// }
	//
	// // Ruecktransformation in Winkelkoordinaten
	// // die Zone des ersten Streckenpunktes wird benutzt
	// WGS84Koordinate w = GeoTransformation
	// .uTMnachWGS84Punkt(new UTMKoordinate(ergebnis.x, ergebnis.y, s1
	// .get_utm_zone()));
	//
	// return new WGS84Punkt(w);
	//
	// }
	/**
	 * Bestimmt, ob der Punkt auf die Strecke abbildbar ist, d.h. ob das Lot vom
	 * Punkt auf die Strecke zwischen den beiden Streckenpunkten liegt.
	 * 
	 * @param line
	 *            Linie
	 * @param point
	 *            Punkt
	 * @return abbildbar ja/nein
	 */
	private static boolean istAbbildbar(final Line2D.Double line,
			final Point2D.Double point) {
		final double ld = line.ptLineDistSq(point);
		final double l = line.ptSegDistSq(point);

		// Bei den obigen Berechnungen treten numerische Fehler auf. Hier wird
		// deshalb ein zusaetzliches Kriterium definiert.
		final double maxdiff = 0.000001;
		final double relfehler = (ld - l) / ld;

		return (line.ptLineDist(point) == line.ptSegDist(point))
				|| (relfehler < maxdiff);
	}

	/**
	 * Bestimmt, ob der Punkt auf die Strecke abbildbar ist, d.h. ob das Lot vom
	 * Punkt auf die Strecke zwischen den beiden Streckenpunkten liegt.
	 * 
	 * @param s1
	 *            Anfangspunkt der Strecke
	 * @param s2
	 *            Endpunkt der Strecke
	 * @param punkt
	 *            abzubildender Punkt
	 * 
	 * @return abbildbar ja/nein
	 */
	private static boolean istAbbildbar(final WGS84Punkt s1,
			final WGS84Punkt s2, final WGS84Punkt punkt) {
		final UTMKoordinate utm1 = s1.toUTMKoordinate();
		final UTMKoordinate utm2 = s2.toUTMKoordinate();
		final UTMKoordinate utmPunkt = punkt.toUTMKoordinate();
		final Line2D.Double line = new Line2D.Double(utm1.getX(), utm1.getY(),
				utm2.getX(), utm2.getY());
		final Point2D.Double point = new Point2D.Double(utmPunkt.getX(),
				utmPunkt.getY());

		final double hoehe = line.ptLineDist(point);
		final double hypo = WGS84Punkt.abstandExakt(s1, punkt);

		// Laenge auf der Strecke bis zum Punkt
		final double fusspunktlaenge = Math.sqrt(Math.pow(hypo, 2.0)
				- Math.pow(hoehe, 2.0));

		final boolean abbildbar = fusspunktlaenge <= WGS84Punkt.abstandExakt(
				s1, s2);

		return abbildbar;
	}

	/**
	 * Bestimmt den Abstand eines Punktes von einer Strecke.
	 * 
	 * @param l1
	 *            Startpunkt der Strecke
	 * @param l2
	 *            Endpunkt der Strecke
	 * @param punkt
	 *            Punkt
	 * @return Abstand des Punktes von der Strecke in Meter
	 */
	public static double punktAbstandStrecke(final WGS84Punkt l1,
			final WGS84Punkt l2, final WGS84Punkt punkt) {
		final UTMKoordinate utm1 = l1.toUTMKoordinate();
		final UTMKoordinate utm2 = l2.toUTMKoordinate();
		final UTMKoordinate utmPunkt = punkt.toUTMKoordinate();
		final Line2D.Double l2d = new Line2D.Double(utm1.getX(), utm1.getY(),
				utm2.getX(), utm2.getY());
		final double abstand = l2d.ptSegDist(utmPunkt.getX(), utmPunkt.getY());

		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(abstand * 1000.0) / 1000.0;
	}

	/**
	 * Test, ob ein Punkt auf einer Strecke liegt.
	 * 
	 * @param l1
	 *            Startpunkt der Strecke
	 * @param l2
	 *            Endpunkt der Strecke
	 * @param punkt
	 *            Punkt
	 * @return true, wenn der Punkt auf der Strecke liegt, sonst false.
	 */
	public static boolean punktLiegtAufStrecke(final WGS84Punkt l1,
			final WGS84Punkt l2, final WGS84Punkt punkt) {
		return punktAbstandStrecke(l1, l2, punkt) == 0.0;
	}

	/**
	 * Test, ob ein Punkt mit einer zul&auml;ssigen Abweichung auf einer Strecke
	 * liegt.
	 * 
	 * @param l1
	 *            Startpunkt der Strecke
	 * @param l2
	 *            Endpunkt der Strecke
	 * @param punkt
	 *            Punkt
	 * @param maxAbweichungMeter
	 *            maximal zul&auml;ssige Abweichung in m
	 * @return true, wenn der Punkt auf der Strecke liegt, sonst false.
	 */
	public static boolean punktLiegtAufStrecke(final WGS84Punkt l1,
			final WGS84Punkt l2, final WGS84Punkt punkt,
			final double maxAbweichungMeter) {
		return punktAbstandStrecke(l1, l2, punkt) <= maxAbweichungMeter;
	}

	/**
	 * Bestimmt, ob der Punkt in der korrekten Richtung erzeugt wurde.
	 * 
	 * @param line
	 *            die Linie, auf die der Punkt abgebildet werden soll
	 * @param punkt
	 *            der zu testende Punkt
	 * @return ja/nein
	 */
	private static boolean richtungOK(final Line2D.Double line,
			final Point2D.Double punkt) {
		final Rectangle2D r = line.getBounds2D();

		return r.contains(punkt);
	}

	/**
	 * Koordinatenliste.
	 */
	private final ArrayList<WGS84Punkt> punkte;

	/**
	 * Konstruktor für Polygon mit WGS84-Koordinaten in Dezimalnotation.
	 * 
	 * Beispiel +4.354551 +50.839402 bedeutet 4°. 354551 O 50°. 839402 N
	 * 
	 * @param laenge
	 *            L&auml;nge
	 * @param breite
	 *            Breite
	 * @throws IllegalArgumentException
	 *             wenn die beiden Felder eine unterschliedliche Länge besitzen.
	 */
	public WGS84Polygon(final double[] laenge, final double[] breite) {
		if (laenge.length != breite.length) {
			throw new IllegalArgumentException(
					"Die Anzahl der Koordinaten für Länge und Breite muss übereinstimmen");
		}

		punkte = new ArrayList<WGS84Punkt>(laenge.length);

		for (int i = 0; i < laenge.length; i++) {
			final WGS84Punkt p = new WGS84Punkt(laenge[i], breite[i]);
			punkte.add(p);
		}
	}

	/**
	 * Konstruktor für Polygon aus Liste von Punkten.
	 * 
	 * @param punktliste
	 *            Punktliste
	 */
	public WGS84Polygon(final List<WGS84Punkt> punktliste) {
		punkte = new ArrayList<WGS84Punkt>(punktliste);
	}

	/**
	 * Schneidet den Anfangsteil des Polygones bis zur L&auml;nge des
	 * angegebenen Offsets ab und gibt diesen Teil zurück. Das Polygon wird um
	 * den entsprechenden Teil gekürzt. Wenn der gegebene Offset
	 * gr&ouml;&szlig;er als die L&auml;nge des Polygones ist, wird eine
	 * IllegalArgumentException geworfen.
	 * 
	 * @param offset
	 *            der Offset (in Meter) beginnend vom Anfang des Polygones, bei
	 *            dem der Schnittpunkt liegen soll
	 * @return Teil des Polygones bis zum Offset-Punkt.
	 * @throws IllegalArgumentException
	 */
	public WGS84Polygon anfangAbschneiden(final double offset) {
		if (laenge() < offset) {
			throw new IllegalArgumentException(
					"Der Offset ist größer als die Polygonlänge");
		}

		if (laenge() == offset) {
			final WGS84Polygon ret = new WGS84Polygon(punkte);
			punkte.clear();
			return ret;
		}

		final WGS84Koordinate bk = bildPunkt(offset);
		final WGS84Punkt bp = new WGS84Punkt(bk.getLaenge(), bk.getBreite());
		final List<WGS84Punkt> apunkte = new ArrayList<WGS84Punkt>();

		int found = -1;
		for (int i = 0; i < punkte.size() - 1; i++) {
			apunkte.add(punkte.get(i));

			if (punktLiegtAufStrecke(punkte.get(i), punkte.get(i + 1), bp,
					0.001)) {
				found = i;
				break;
			}
		}

		if (found == -1) {
			return null;
		}

		// entferne Anfang von this
		for (final WGS84Punkt p : apunkte) {
			punkte.remove(p);
		}
		// Bildpunkt an den Anfang
		punkte.add(0, bp);

		// Bildpunkt an das Ende der Ergebnisliste
		apunkte.add(bp);

		// return Anfangsteil
		return new WGS84Polygon(apunkte);
	}

	/**
	 * Schneidet den Anfangsteil des Polygones bis zu einem gegebenen Punkt ab
	 * und gibt diesen Teil zurück. Das Polygon wird um den entsprechenden Teil
	 * gekürzt. Wenn der gegebene Punkt nicht auf dem Polygon liegt, wird eine
	 * IllegalArgumentException geworfen.
	 * 
	 * @param punkt
	 *            Schnittpunkt
	 * @return Teil des Polygones bis zum Offset-Punkt.
	 * @throws IllegalArgumentException
	 */
	public WGS84Polygon anfangAbschneiden(final WGS84Punkt punkt) {
		if (!liegtAufPolygon(punkt, 0.001)) {
			throw new IllegalArgumentException(
					"Der Punkt liegt nicht auf dem Polygon");
		}

		final List<WGS84Punkt> apunkte = new ArrayList<WGS84Punkt>();

		int found = -1;
		for (int i = 0; i < punkte.size() - 1; i++) {
			apunkte.add(punkte.get(i));
			if (punktLiegtAufStrecke(punkte.get(i), punkte.get(i + 1), punkt,
					0.001)) {
				found = i;
				break;
			}
		}

		if (found == -1) {
			throw new IllegalArgumentException(
					"Der Punkt liegt nicht auf dem Polygon");
		}

		// entferne Anfang von this
		for (final WGS84Punkt p : apunkte) {
			punkte.remove(p);
		}
		// Bildpunkt an den Anfang
		punkte.add(0, punkt);

		// Bildpunkt an das Ende der Ergebnisliste
		apunkte.add(punkt);

		// return Anfangsteil
		return new WGS84Polygon(apunkte);
	}

	/**
	 * Berechnet den Offset eines Punktes auf dem Polygon.
	 * 
	 * @param punkt
	 *            Punkt, f&uuml;r den der Offset berechnet werden soll
	 * 
	 * @return Offset (in m).
	 * @throws IllegalArgumentException
	 *             wenn der Punkt nicht auf dem Polygon liegt.
	 */
	public double berecheneOffset(final WGS84Punkt punkt) {
		if (!liegtAufPolygon(punkt)) {
			throw new IllegalArgumentException(
					"Der Offset kann nicht bestimmt werden");
		}

		double offset = 0.0;

		for (int i = 0; i < punkte.size() - 1; i++) {
			if (WGS84Polygon.punktLiegtAufStrecke(punkte.get(i), punkte
					.get(i + 1), punkt)) {
				offset += WGS84Punkt.abstand(punkt, punkte.get(i));
				break;
			}

			offset += WGS84Punkt.abstand(punkte.get(i + 1), punkte.get(i));
		}

		return offset;
	}

	/**
	 * Berechnet die Koordinaten des Punktes auf dem Polygonzug, der einen
	 * gegebenen Offset vom Anfangspunkt entfernt ist. Wenn der gegebene Offset
	 * gr&ouml;&szlig;er als die L&auml;nge des Polygones ist, wird eine
	 * IllegalArgumentException geworfen.
	 * 
	 * @param offset
	 *            der Offset (in Meter) beginnend vom Anfang des Polygones, bei
	 *            dem der Punkt liegen soll
	 * 
	 * @return der berechnete Punkt.
	 * @throws IllegalArgumentException
	 *             wenn der Offset länger als das Polygon ist.
	 */
	public strictfp WGS84Punkt bildPunkt(final double offset) {
		if (laenge() < offset) {
			throw new IllegalArgumentException(
					"Der Offset ist größer als die Polygonlänge");
		}

		double laenge = 0.0;

		for (int i = 0; i < punkte.size() - 1; i++) {
			final double slaenge = WGS84Punkt.abstandExakt(punkte.get(i),
					punkte.get(i + 1));
			double tmplaenge = laenge + slaenge;
			tmplaenge = Math.round(tmplaenge * 1000.0) / 1000.0;
			if (tmplaenge >= offset) {
				return WGS84Polygon.bildPunktAufStrecke(punkte.get(i), punkte
						.get(i + 1),
						Math.round((offset - laenge) * 1000.0) / 1000.0);
			}

			laenge += slaenge;
		}

		// this should not happen!!!
		throw new IllegalStateException(
				"Der Offset kann nicht auf das Polygon abgebildet werden");
	}

	/**
	 * Berechnet die Koordinaten der Abbildung eines Punktes auf das Polygon.
	 * Wenn der Punkt nicht auf dem Polygon liegt, wird die Bildpunkt-Koordinate
	 * durch das Lot vom Punkt auf den Streckenteil des Polygones mit dem
	 * kleinsten Abstand zum Punkt berechnet. Kann kein Lot gef&auml;llt werden,
	 * wird als Ergebnis der Punkt der Linie zur&uuml;ckgeliefert, welcher dem
	 * Punkt am n&auml;chsten liegt, also entweder der Anfangs- oder der
	 * Endpunkt.
	 * 
	 * @param punkt
	 *            der abzubildende Punkt
	 * 
	 * @return Punkt.
	 * @throws IllegalArgumentException
	 */
	public WGS84Punkt bildPunkt(final WGS84Punkt punkt) {
		if (liegtAufPolygon(punkt)) {
			return punkt;
		}

		final WGS84Polygon strecke = findeTeilstreckeKleinsterAbstand(punkt);
		int iterationen = 0;

		if (strecke != null) {
			WGS84Punkt bp = punkt;

			// iteriere um numerische Fehler bei grossem Abstand zu eliminieren
			do {
				bp = WGS84Polygon.bildPunktAufStrecke(strecke.punkte.get(0),
						strecke.punkte.get(1), bp);

				if (++iterationen > 10) {
					throw new IllegalArgumentException(
							"Der Bildpunkt kann nicht genau bestimmt werden");
				}
			} while (!liegtAufPolygon(bp));

			return bp;
		}

		throw new IllegalArgumentException(
				"Der Bildpunkt kann nicht bestimmt werden");
	}

	/**
	 * Berechnet die Koordinaten der Abbildung eines Punktes auf das Polygon.
	 * Wenn der Punkt nicht auf dem Polygon liegt, wird die Bildpunkt-Koordinate
	 * durch das Lot vom Punkt auf den Streckenteil des Polygones mit dem
	 * kleinsten Abstand zum Punkt berechnet. Kann kein Lot gef&auml;llt werden,
	 * wird als Ergebnis der Punkt der Linie zur&uuml;ckgeliefert, welcher dem
	 * Punkt am n&auml;chsten liegt, also entweder der Anfangs- oder der
	 * Endpunkt.
	 * 
	 * @param punkt
	 *            der abzubildende Punkt
	 * 
	 * @return der Punkt.
	 * @throws IllegalArgumentException
	 */
	public WGS84Punkt bildPunktTest(final WGS84Punkt punkt) {
		if (liegtAufPolygon(punkt)) {
			return punkt;
		}

		final WGS84Polygon strecke = findeTeilstreckeKleinsterAbstand(punkt);
		int iterationen = 0;

		if (strecke != null) {
			WGS84Punkt bp = punkt;

			// iteriere um numerische Fehler bei grossem Abstand zu eliminieren
			do {
				final WGS84Punkt sp1 = strecke.punkte.get(0);
				final WGS84Punkt sp2 = strecke.punkte.get(1);

				System.out.println("Iterationen: " + iterationen);
				System.out.println("Abstand: "
						+ punktAbstandStrecke(sp1, sp2, bp));

				// XXX Auskommentierten Quelltext löschen?
				// if(punktAbstandStrecke(sp1, sp2, bp) < 10) {
				// System.out.println("WGS");
				// bp = WGS84Polygon.bildPunktAufStreckeTest(sp1, sp2, bp);
				// }
				// else {
				System.out.println("UTM");
				bp = WGS84Polygon.bildPunktAufStrecke(sp1, sp2, bp);
				// }

				if (++iterationen > 10000) {
					throw new IllegalArgumentException(
							"Der Bildpunkt kann nicht genau bestimmt werden");
				}
			} while (!liegtAufPolygon(bp));

			return bp;
		}

		throw new IllegalArgumentException(
				"Der Bildpunkt kann nicht bestimmt werden");
	}

	/**
	 * Berechnet die Teilstrecke des Polygons, f&uuml;r die der Abstand eines
	 * gegebenen Punktes von dieser Strecke minimal ist.
	 * 
	 * @param punkt
	 *            Punkt
	 * @return gefundene Teilstrecke als Polygon oder null
	 */
	public WGS84Polygon findeTeilstreckeKleinsterAbstand(final WGS84Punkt punkt) {
		double abstand = Double.MAX_VALUE;
		WGS84Punkt p1 = null, p2 = null;
		WGS84Polygon strecke = null;

		for (int i = 0; i < punkte.size() - 1; i++) {
			final double sabstand = WGS84Polygon.punktAbstandStrecke(punkte
					.get(i), punkte.get(i + 1), punkt);
			if (sabstand > abstand) {
				continue;
			}

			// XXX Auskommentierten Quelltext löschen?
			// if(sabstand == abstand) {
			// // die beiden aufeinanderfolgenden Strecken haben den gleichen
			// Abstand, da sie
			// // einen gemeinsamen Punkt haben
			// if(!istAbbildbar(_punkte.get(i), _punkte.get(i+1), punkt))
			// continue;
			// }

			abstand = sabstand;
			p1 = punkte.get(i);
			p2 = punkte.get(i + 1);
		}

		if (p1 != null && p2 != null) {
			final WGS84Punkt[] spunkte = { p1, p2 };
			strecke = new WGS84Polygon(Arrays.asList(spunkte));
		}

		return strecke;
	}

	/**
	 * Gibt die Koordinaten des Polygons als Punktliste zur&uuml;ck.
	 * 
	 * @return Punktkoordinaten
	 */
	public ArrayList<WGS84Punkt> getKoordinaten() {
		return punkte;
	}

	/**
	 * Test, ob ein Punkt der Anfangs- oder Endpunkt des Polygon ist.
	 * 
	 * @param punkt
	 *            zu testender Punkt
	 * @return true, wenn der Punkt der Anfangs- oder Endpunkt des Polygons ist,
	 *         sonst false
	 */
	public boolean istAnfangsOderEndPunkt(final WGS84Punkt punkt) {
		return istAnfangsPunkt(punkt) || istEndPunkt(punkt);
	}

	/**
	 * Test, ob ein Punkt der Anfangs- oder Endpunkt des Polygon ist oder in der
	 * N&auml;he dieser liegt.
	 * 
	 * @param punkt
	 *            zu testender Punkt
	 * @param maxAbstandMeter
	 *            max. zul&auml;ssiger Abstand in Meter
	 * @return true, wenn der Punkt der Anfangs- oder Endpunkt des Polygons ist
	 *         oder maximal <code>maxAbstandMeter</code> vom Anfangs- oder
	 *         Endpunkt entfernt ist, sonst false
	 */
	public boolean istAnfangsOderEndPunkt(final WGS84Punkt punkt,
			final double maxAbstandMeter) {
		return istAnfangsPunkt(punkt, maxAbstandMeter)
				|| istEndPunkt(punkt, maxAbstandMeter);
	}

	/**
	 * Test, ob ein Punkt der Anfangspunkt des Polygon ist.
	 * 
	 * @param punkt
	 *            zu testender Punkt
	 * @return true, wenn der Punkt der Anfangspunkt des Polygons ist, sonst
	 *         false
	 */
	public boolean istAnfangsPunkt(final WGS84Punkt punkt) {
		if (punkte.size() == 0) {
			return false;
		}

		return punkte.get(0).equals(punkt);
	}

	/**
	 * Test, ob ein Punkt der Anfangspunkt des Polygon ist oder in dessen
	 * N&auml;he liegt.
	 * 
	 * @param punkt
	 *            zu testender Punkt
	 * @param maxAbstandMeter
	 *            max. zul&auml;ssiger Abstand in Meter
	 * @return true, wenn der Punkt der Anfangpunkt des Polygons ist oder
	 *         maximal <code>maxAbstandMeter</code> vom Anfangspunkt entfernt
	 *         ist, sonst false
	 */
	public boolean istAnfangsPunkt(final WGS84Punkt punkt,
			final double maxAbstandMeter) {
		if (punkte.size() == 0) {
			return false;
		}

		return istAnfangsPunkt(punkt)
				|| (WGS84Punkt.abstand(punkte.get(0), punkt) <= maxAbstandMeter);
	}

	/**
	 * Test, ob ein Punkt der Endpunkt des Polygon ist.
	 * 
	 * @param punkt
	 *            zu testender Punkt
	 * @return true, wenn der Punkt der Endpunkt des Polygons ist, sonst false
	 */
	public boolean istEndPunkt(final WGS84Punkt punkt) {
		if (punkte.size() == 0) {
			return false;
		}

		return punkte.get(punkte.size() - 1).equals(punkt);
	}

	/**
	 * Test, ob ein Punkt der Endpunkt des Polygon ist oder in dessen N&auml;he
	 * liegt..
	 * 
	 * @param punkt
	 *            zu testender Punkt
	 * @param maxAbstandMeter
	 *            max. zul&auml;ssiger Abstand in Meter
	 * @return true, wenn der Punkt der Endpunkt des Polygons ist oder maximal
	 *         <code>maxAbstandMeter</code> vom Endpunkt entfernt ist, sonst
	 *         false
	 */
	public boolean istEndPunkt(final WGS84Punkt punkt,
			final double maxAbstandMeter) {
		if (punkte.size() == 0) {
			return false;
		}

		return istEndPunkt(punkt)
				|| (WGS84Punkt.abstand(punkte.get(punkte.size() - 1), punkt) <= maxAbstandMeter);
	}

	/**
	 * Test, ob das Polygon gleich einem anderen Polygon ist, wobei eine
	 * bestimmte Abweichung der Koordinaten nicht &uuml;berschritten werden
	 * darf.
	 * 
	 * @param testpolygon
	 *            Das zu testende Polgon
	 * @param maxabweichungGrad
	 *            maximal zul&auml;ssige Abweichung in Grad
	 * @return true, wenn die Polygone im o.g. Sinne identisch sind, sonst false
	 */
	public boolean istIdentisch(final WGS84Polygon testpolygon,
			final double maxabweichungGrad) {
		// XXX Auskommentierten Quelltext löschen?

		// double abstandmeter = GeoTransformation
		// .winkelInMeter(maxabweichungGrad);

		if (punkte.size() != testpolygon.punkte.size()) {
			return false;
		}

		for (int i = 0; i < punkte.size(); i++) {
			// if(WGS84Punkt.abstand(punkte.get(i), testpolygon.punkte.get(i)) >
			// abstandmeter)
			// boolean b = punkte.get(i).equals(testpolygon.punkte.get(i),
			// maxabweichungGrad);
			if (!punkte.get(i).equals(testpolygon.punkte.get(i),
					maxabweichungGrad)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Bestimmt den kleinsten Abstand eines Punktes vom Polygon.
	 * 
	 * @param punkt
	 *            Der Punkt, f&uuml;r den der Abstand bestimmt werden soll
	 * @return der kleinste Abstand des Punktes vom Polygon (in m)
	 */
	public double kleinsterPunktAbstand(final WGS84Punkt punkt) {
		final WGS84Polygon strecke = findeTeilstreckeKleinsterAbstand(punkt);

		if (strecke != null) {
			return WGS84Polygon.punktAbstandStrecke(strecke.punkte.get(0),
					strecke.punkte.get(1), punkt);
		}

		throw new IllegalArgumentException(
				"Der Abstand des Punktes kann nicht bestimmt werden");
	}

	/**
	 * Bestimmt den gr&ouml;ssten Abstand eines Punktes vom Polygon.
	 * 
	 * @param punkt
	 *            Der Punkt, f&uuml;r den der Abstand bestimmt werden soll
	 * @return der gr&ouml;sste Abstand des Punktes vom Polygon (in m)
	 */
	public double groessterPunktAbstand(final WGS84Punkt punkt) {
		double punktabstand = 0;

		for (int i = 0; i < punkte.size() - 1; i++) {
			final double abstand = WGS84Polygon.punktAbstandStrecke(punkte
					.get(i), punkte.get(i + 1), punkt);
			if (abstand > punktabstand) {
				punktabstand = abstand;
			}
		}

		return punktabstand;
	}

	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laenge() {
		return laengeExakt();
	}

	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laengeAppr() {
		double laenge = 0;

		for (int i = 0; i < punkte.size() - 1; i++) {
			laenge += WGS84Punkt.abstand(punkte.get(i), punkte.get(i + 1));
		}

		return laenge;
	}

	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laengeExakt() {
		double laenge = 0;

		for (int i = 0; i < punkte.size() - 1; i++) {
			laenge += WGS84Punkt.abstandExakt(punkte.get(i), punkte.get(i + 1));
			// XXX Auskommentierten Quelltext löschen?
			// laenge += WGS84Punkt.Abstand(_punkte.get(i), _punkte.get(i+1));
		}
		// zur Vermeidung von numerischen Problemen mit 3 Nachkommastellen
		return Math.round(laenge * 1000.0) / 1000.0;
	}

	/**
	 * Berechnet die Länge des Polygonzuges in m.
	 * 
	 * @return L&auml;nge in Meter.
	 */
	public double laengeKartesisch() {
		double laenge = 0;

		for (int i = 0; i < punkte.size() - 1; i++) {
			laenge += WGS84Punkt.abstandKartesisch(punkte.get(i), punkte
					.get(i + 1));
		}

		return laenge;
	}

	/**
	 * Test, ob ein Punkt auf dem Polygon liegt.
	 * 
	 * @param punkt
	 *            Punkt
	 * @return true, wenn der Punkt auf dem Polygonzug liegt, sonst false
	 */
	public boolean liegtAufPolygon(final WGS84Punkt punkt) {
		for (int i = 0; i < punkte.size() - 1; i++) {
			if (punktLiegtAufStrecke(punkte.get(i), punkte.get(i + 1), punkt)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Test, ob ein Punkt auf dem Polygon liegt.
	 * 
	 * @param punkt
	 *            Punkt
	 * @param maxAbweichungMeter
	 *            maximal zul&auml;ssige Abweichung in m
	 * @return true, wenn der Punkt auf dem Polygonzug liegt, sonst false
	 */
	public boolean liegtAufPolygon(final WGS84Punkt punkt,
			final double maxAbweichungMeter) {

		for (int i = 0; i < punkte.size() - 1; i++) {
			if (punktLiegtAufStrecke(punkte.get(i), punkte.get(i + 1), punkt,
					maxAbweichungMeter)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Sortiert das Polygon.
	 */
	public void sort() {
		Collections.sort(punkte, new Comparator<WGS84Punkt>() {
			public int compare(final WGS84Punkt o1, final WGS84Punkt o2) {
				return o1.compareTo(o2);
			}
		});
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof WGS84Polygon) {
			final WGS84Polygon other = (WGS84Polygon) obj;
			return punkte.equals(other.punkte);
		}
		return false;
	}

	@Override
	public String toString() {
		String result = "WGS84-Polygon";
		result += "[";
		final Iterator<WGS84Punkt> iterator = punkte.iterator();
		while (iterator.hasNext()) {
			final WGS84Punkt p = iterator.next();
			result += "(" + p.getLaenge() + ", " + p.getBreite() + ")";
			if (iterator.hasNext()) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}

	@Override
	public WGS84Polygon clone() {
		return new WGS84Polygon(punkte);
	}

}
