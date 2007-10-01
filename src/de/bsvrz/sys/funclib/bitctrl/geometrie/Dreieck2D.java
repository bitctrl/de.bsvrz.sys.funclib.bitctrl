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

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Ergänzt die Java2D-API um ein Dreieck
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public abstract class Dreieck2D implements Shape, Cloneable {

	/**
	 * Ein Dreieck, dessen Punkte mit doppelter Genauigkeit gesichert werden
	 * 
	 * @author BitCtrl, Schumann
	 * @version $Id$
	 */
	public static class Double extends Dreieck2D {

		/** X-Koordinate des ersten Punkts */
		public double x1;

		/** Y-Koordinate des ersten Punkts */
		public double y1;

		/** X-Koordinate des zweiten Punkts */
		public double x2;

		/** Y-Koordinate des zweiten Punkts */
		public double y2;

		/** X-Koordinate des dritten Punkts */
		public double x3;

		/** Y-Koordinate des dritten Punkts */
		public double y3;

		/**
		 * Erzeugt ein Dreieck, welches als Punkt entartet ist p1 = p2 = p3 =
		 * (0, 0)
		 * 
		 */
		public Double() {
			// nix zu tun
		}

		/**
		 * Erzeugt ein Dreieck mit dem angegebenen Koordianten
		 * 
		 * @param x1
		 *            X-Koordinate des ersten Punkts
		 * @param y1
		 *            Y-Koordinate des ersten Punkts
		 * @param x2
		 *            X-Koordinate des zweiten Punkts
		 * @param y2
		 *            Y-Koordinate des zweiten Punkts
		 * @param x3
		 *            X-Koordinate des dritten Punkts
		 * @param y3
		 *            Y-Koordinate des dritten Punkts
		 */
		public Double(double x1, double y1, double x2, double y2, double x3,
				double y3) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.x3 = x3;
			this.y3 = y3;
		}

		/**
		 * Erzeugt ein Dreieck mit dem angegebenen Punkten
		 * 
		 * @param p1
		 *            Erster Punkt
		 * @param p2
		 *            Zeiter Punkt
		 * @param p3
		 *            Dritter Punkt
		 */
		public Double(Point2D p1, Point2D p2, Point2D p3) {
			x1 = p1.getX();
			y1 = p1.getY();
			x2 = p2.getX();
			y2 = p2.getY();
			x3 = p3.getX();
			y3 = p3.getY();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX1() {
			return x1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX2() {
			return x2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX3() {
			return x3;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY1() {
			return y1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY2() {
			return y2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY3() {
			return y3;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2D getP1() {
			return new Point2D.Double(x1, y1);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2D getP2() {
			return new Point2D.Double(x2, y2);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2D getP3() {
			return new Point2D.Double(x3, y3);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDreieck(double x1, double y1, double x2, double y2,
				double x3, double y3) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.x3 = x3;
			this.y3 = y3;
		}

		/**
		 * @see java.awt.Shape#getBounds2D()
		 */
		public Rectangle2D getBounds2D() {
			double xmin, xmax, ymin, ymax;

			xmin = Math.min(x1, x2);
			xmin = Math.min(xmin, x3);

			xmax = Math.max(x1, x2);
			xmax = Math.max(xmax, x3);

			ymin = Math.min(y1, y2);
			ymin = Math.min(ymin, y3);

			ymax = Math.max(y1, y2);
			ymax = Math.max(ymax, y3);

			return new Rectangle2D.Double(xmin, ymin, xmax - xmin, ymax - ymin);
		}

	}

	/**
	 * Ein Dreieck, dessen Punkte mit einfacher Genauigkeit gesichert werden
	 * 
	 * @author BitCtrl, Schumann
	 * @version $Id$
	 */
	public static class Float extends Dreieck2D {

		/** X-Koordinate des ersten Punkts */
		public float x1;

		/** Y-Koordinate des ersten Punkts */
		public float y1;

		/** X-Koordinate des zweiten Punkts */
		public float x2;

		/** Y-Koordinate des zweiten Punkts */
		public float y2;

		/** X-Koordinate des dritten Punkts */
		public float x3;

		/** Y-Koordinate des dritten Punkts */
		public float y3;

		/**
		 * Erzeugt ein Dreieck, welches als Punkt entartet ist p1 = p2 = p3 =
		 * (0, 0)
		 * 
		 */
		public Float() {
			// nix zu tun
		}

		/**
		 * Erzeugt ein Dreieck mit dem angegebenen Koordianten
		 * 
		 * @param x1
		 *            X-Koordinate des ersten Punkts
		 * @param y1
		 *            Y-Koordinate des ersten Punkts
		 * @param x2
		 *            X-Koordinate des zweiten Punkts
		 * @param y2
		 *            Y-Koordinate des zweiten Punkts
		 * @param x3
		 *            X-Koordinate des dritten Punkts
		 * @param y3
		 *            Y-Koordinate des dritten Punkts
		 */
		public Float(float x1, float y1, float x2, float y2, float x3, float y3) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.x3 = x3;
			this.y3 = y3;
		}

		/**
		 * Erzeugt ein Dreieck mit dem angegebenen Punkten
		 * 
		 * @param p1
		 *            Erster Punkt
		 * @param p2
		 *            Zeiter Punkt
		 * @param p3
		 *            Dritter Punkt
		 */
		public Float(Point2D p1, Point2D p2, Point2D p3) {
			x1 = (float) p1.getX();
			y1 = (float) p1.getY();
			x2 = (float) p2.getX();
			y2 = (float) p2.getY();
			x3 = (float) p3.getX();
			y3 = (float) p3.getY();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX1() {
			return x1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX2() {
			return x2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX3() {
			return x3;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY1() {
			return y1;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY2() {
			return y2;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY3() {
			return y3;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2D getP1() {
			return new Point2D.Float(x1, y1);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2D getP2() {
			return new Point2D.Float(x2, y2);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Point2D getP3() {
			return new Point2D.Float(x3, y3);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setDreieck(double x1, double y1, double x2, double y2,
				double x3, double y3) {
			this.x1 = (float) x1;
			this.y1 = (float) y1;
			this.x2 = (float) x2;
			this.y2 = (float) y2;
			this.x3 = (float) x3;
			this.y3 = (float) y3;
		}

		/**
		 * @see java.awt.Shape#getBounds2D()
		 */
		public Rectangle2D getBounds2D() {
			float xmin, xmax, ymin, ymax;

			xmin = Math.min(x1, x2);
			xmin = Math.min(xmin, x3);

			xmax = Math.max(x1, x2);
			xmax = Math.max(xmax, x3);

			ymin = Math.min(y1, y2);
			ymin = Math.min(ymin, y3);

			ymax = Math.max(y1, y2);
			ymax = Math.max(ymax, y3);

			return new Rectangle2D.Float(xmin, ymin, xmax - xmin, ymax - ymin);
		}

	}

	/**
	 * Gibt den ersten Punkt des Dreiecks zurück
	 * 
	 * @return Ein Punkt
	 */
	public abstract Point2D getP1();

	/**
	 * Gibt den zweiten Punkt des Dreiecks zurück
	 * 
	 * @return Ein Punkt
	 */
	public abstract Point2D getP2();

	/**
	 * Gibt den dritten Punkt des Dreiecks zurück
	 * 
	 * @return Ein Punkt
	 */
	public abstract Point2D getP3();

	/**
	 * Gibt die x-Koordinate des ersten Punkts zurück
	 * 
	 * @return x-Koordinate
	 */
	public abstract double getX1();

	/**
	 * Gibt die x-Koordinate des zweiten Punkts zurück
	 * 
	 * @return x-Koordinate
	 */
	public abstract double getX2();

	/**
	 * Gibt die x-Koordinate des dritten Punkts zurück
	 * 
	 * @return x-Koordinate
	 */
	public abstract double getX3();

	/**
	 * Gibt die y-Koordinate des ersten Punkts zurück
	 * 
	 * @return y-Koordinate
	 */
	public abstract double getY1();

	/**
	 * Gibt die y-Koordinate des zweiten Punkts zurück
	 * 
	 * @return y-Koordinate
	 */
	public abstract double getY2();

	/**
	 * Gibt die y-Koordinate des dritten Punkts zurück
	 * 
	 * @return y-Koordinate
	 */
	public abstract double getY3();

	/**
	 * Legt die Koordianten des Dreiecks fest
	 * 
	 * @param x1
	 *            X-Koordinate des ersten Punkts
	 * @param y1
	 *            Y-Koordinate des ersten Punkts
	 * @param x2
	 *            X-Koordinate des zweiten Punkts
	 * @param y2
	 *            Y-Koordinate des zweiten Punkts
	 * @param x3
	 *            X-Koordinate des dritten Punkts
	 * @param y3
	 *            Y-Koordinate des dritten Punkts
	 */
	public abstract void setDreieck(double x1, double y1, double x2, double y2,
			double x3, double y3);

	/**
	 * Legt die drei Punkte des Dreiecks fest
	 * 
	 * @param p1
	 *            Erster Punkt
	 * @param p2
	 *            Zeiter Punkt
	 * @param p3
	 *            Dritter Punkt
	 */
	public void setDreieck(Point2D p1, Point2D p2, Point2D p3) {
		setDreieck(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3
				.getY());
	}

	/**
	 * @see java.awt.Shape#contains(java.awt.geom.Point2D)
	 */
	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	/**
	 * @see java.awt.Shape#contains(java.awt.geom.Rectangle2D)
	 */
	public boolean contains(Rectangle2D r) {
		return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Shape#contains(double, double)
	 */
	public boolean contains(double x, double y) {
		return Line2D.relativeCCW(getX1(), getY1(), getX2(), getY2(), x, y) <= 0
				&& Line2D.relativeCCW(getX2(), getY2(), getX3(), getY3(), x, y) <= 0
				&& Line2D.relativeCCW(getX3(), getY3(), getX1(), getY1(), x, y) <= 0;
	}

	/**
	 * Alle Eckpunkte des Rechtecks müssen im Inneren des Dreiecks liegen
	 * 
	 * @see java.awt.Shape#contains(double, double, double, double)
	 */
	public boolean contains(double x, double y, double w, double h) {
		double x2, y2, x3, y3, x4, y4; // Eckpunkte des Rechtecks

		x2 = x + w;
		y2 = y;

		x3 = x + w;
		y3 = y + h;

		x4 = x;
		y4 = y + h;

		return contains(x, y) && contains(x2, y2) && contains(x3, y3)
				&& contains(x4, y4);
	}

	/**
	 * @see java.awt.Shape#getBounds()
	 */
	public Rectangle getBounds() {
		return getBounds2D().getBounds();
	}

	/**
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
	 */
	public PathIterator getPathIterator(AffineTransform at) {
		return new DreieckIterator(this, at);
	}

	/**
	 * Der zweite Parameter wird ignoriert, da die Strecken des Pfads nicht
	 * approximiert werden müssen
	 * 
	 * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform,
	 *      double)
	 */
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return new DreieckIterator(this, at);
	}

	/**
	 * @see java.awt.Shape#intersects(java.awt.geom.Rectangle2D)
	 */
	public boolean intersects(Rectangle2D r) {
		return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
	}

	/**
	 * Einer von drei Fällen muss zutreffen.
	 * <ul>
	 * <li>Das Dreieck liegt im Rechteck,</li>
	 * <li>Das Rechteck liegt im Dreieck oder</li>
	 * <li>Eine Dreieckskante schneidet mindestens eine Rechteckkante</li>
	 * </ul>
	 * 
	 * @see java.awt.Shape#intersects(double, double, double, double)
	 */
	public boolean intersects(double x, double y, double w, double h) {
		Rectangle2D r = new Rectangle2D.Double(x, y, w, h);
		boolean imRechteck;
		boolean schneidet = false;

		// Befindet sich das Dreieck im Rechteck?
		imRechteck = r.contains(getP1()) && r.contains(getP2())
				&& r.contains(getP3());

		// Schneidet sich eine Kante des Dreiecks mit einer des Rechtecks?
		List<Line2D> dreieck = new ArrayList<Line2D>();
		List<Line2D> rechteck = new ArrayList<Line2D>();
		dreieck.add(new Line2D.Double(getP1(), getP2()));
		dreieck.add(new Line2D.Double(getP2(), getP3()));
		dreieck.add(new Line2D.Double(getP3(), getP1()));
		rechteck.add(new Line2D.Double(x, y, x + w, y));
		rechteck.add(new Line2D.Double(x + w, y, x + w, y + h));
		rechteck.add(new Line2D.Double(x + w, y + h, x, y + h));
		rechteck.add(new Line2D.Double(x, y + h, x, y));
		for (Line2D kanteRechteck : rechteck)
			for (Line2D kanteDreieck : dreieck)
				if (kanteDreieck.intersectsLine(kanteRechteck))
					schneidet = true;

		return contains(x, y, w, h) || imRechteck || schneidet;
	}
}
