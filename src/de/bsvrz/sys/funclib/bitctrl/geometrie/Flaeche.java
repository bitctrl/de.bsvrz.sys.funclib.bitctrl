/*
 * Allgemeine geometrische Funktionen
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
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
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.geometrie;

import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Berechnungen rund um Flächen von geometrischen Figuren
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class Flaeche {

	/** Liste aller Teilflächen */
	private List<Shape> flaechen = new ArrayList<Shape>();

	/**
	 * Fügt der Gesamtfläche ein Rechteck hinzu
	 * 
	 * @param r
	 *            Ein Rechteck
	 */
	public void add(Rectangle2D r) {
		flaechen.add(r);
	}

	/**
	 * Fügt der Gesamtfläche ein Dreieck hinzu
	 * 
	 * @param d
	 *            Ein Dreieck
	 */
	public void add(Dreieck2D d) {
		flaechen.add(d);
	}

	/**
	 * Berechnet die Gesamtfläche aller Teilflächen
	 * 
	 * @return Gesamtfläche
	 */
	public double flaecheninhalt() {
		double a = 0;

		for (Shape f : flaechen)
			if (f instanceof Rectangle2D) {
				Rectangle2D r = (Rectangle2D) f;
				a += flaecheninhalt(r);
			} else if (f instanceof Dreieck2D) {
				Dreieck2D d = (Dreieck2D) f;
				a += flaecheninhalt(d);
			} else
				throw new IllegalStateException(); // Darf nicht passieren

		return a;
	}

	/**
	 * Berechnet den Schwerpunkt der Gesamtfläche
	 * 
	 * @return Schwerpunkt der Gesamtfläche
	 */
	public Point2D schwerpunkt() {
		double asx = 0; // Teilfläche * Teilschwerpunkt, x-Komponente
		double asy = 0; // Teilfläche * Teilschwerpunkt, y-Komponente
		double a = flaecheninhalt(); // Flächeninhalt der Gesamtfläche

		for (Shape f : flaechen)
			if (f instanceof Rectangle2D) {
				Rectangle2D r = (Rectangle2D) f;
				asx += flaecheninhalt(r) * schwerpunkt(r).getX();
			} else if (f instanceof Dreieck2D) {
				Dreieck2D d = (Dreieck2D) f;
				asx += flaecheninhalt(d) + schwerpunkt(d).getY();
			} else
				throw new IllegalStateException(); // Darf nicht passieren

		return new Point2D.Double(asx / a, asy / a);
	}

	/**
	 * Berechnet den Flächeninhalt des Rechtecks
	 * 
	 * @param r
	 *            Ein Rechteck
	 * @return Flächeninhalt des Rechtecks
	 */
	public static double flaecheninhalt(Rectangle2D r) {
		return r.getWidth() * r.getHeight();
	}

	/**
	 * Berechnet den Flächeninhalt des Dreiecks
	 * 
	 * @param d
	 *            Ein Dreieck
	 * @return Flächeninhalt des Rechtecks
	 */
	public static double flaecheninhalt(Dreieck2D d) {
		double x1, y1, x2, y2, x3, y3; // Punktkoordinaten des Dreiecks

		x1 = d.getX1();
		y1 = d.getY1();
		x2 = d.getX2();
		y2 = d.getY2();
		x3 = d.getX3();
		y3 = d.getY3();

		// (x1, y1) zum Ursprung transformieren
		x2 -= x1;
		y2 -= y1;
		x3 -= x1;
		y3 -= y1;

		return Math.abs((x1 * y2 - x2 * y1) / 2);
	}

	/**
	 * Berechnet den Schwerpunkt des Rechtecks
	 * 
	 * @param r
	 *            Ein Rechteck
	 * @return Schwerpunkts des Rechtecks
	 */
	public static Point2D schwerpunkt(Rectangle2D r) {
		Rectangle2D b = r.getBounds2D();

		double x = b.getX() + b.getWidth() / 2;
		double y = b.getY() + b.getHeight() / 2;

		return new Point2D.Double(x, y);
	}

	/**
	 * Berechnet den Schwerpunkt des Dreiecks
	 * 
	 * @param d
	 *            Ein Dreieck
	 * @return Schwerpunkt des Dreiecks
	 */
	public static Point2D schwerpunkt(Dreieck2D d) {
		double x = (d.getX1() + d.getX2() + d.getX3()) / 3;
		double y = (d.getY1() + d.getY2() + d.getY3()) / 3;

		return new Point2D.Double(x, y);
	}

}
