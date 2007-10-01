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

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

/**
 * @author BitCtrl, Schumann
 * @version $Id$
 */
class DreieckIterator implements PathIterator {

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

	/** Affine Transformation der Dreieckspunkte */
	private final AffineTransform affine;

	/** Index des aktuellen Punkts der Iteration */
	private int index = 0;

	/**
	 * Erzeugt eine Iteration über die Punkte des Dreiecks
	 * 
	 * @param dreieck
	 *            Ein Dreieck
	 * @param affine
	 *            Eine affine Transformation
	 */
	DreieckIterator(Dreieck2D dreieck, AffineTransform affine) {
		x1 = dreieck.getX1();
		y1 = dreieck.getY1();
		x2 = dreieck.getX2();
		y2 = dreieck.getY2();
		x3 = dreieck.getX3();
		y3 = dreieck.getY3();
		this.affine = affine;
	}

	/**
	 * @see java.awt.geom.PathIterator#currentSegment(float[])
	 */
	public int currentSegment(float[] coords) {
		switch (index) {
		case 0:
			coords[0] = (float) x1;
			coords[1] = (float) y1;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_MOVETO;
		case 1:
			coords[0] = (float) x2;
			coords[1] = (float) y2;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_LINETO;
		case 2:
			coords[0] = (float) x3;
			coords[1] = (float) y3;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_LINETO;
		case 3:
			coords[0] = (float) x1;
			coords[1] = (float) y1;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_LINETO;
		case 4:
			return SEG_CLOSE;
		default:
			throw new NoSuchElementException("triangle iterator out of bounds");
		}
	}

	/**
	 * @see java.awt.geom.PathIterator#currentSegment(double[])
	 */
	public int currentSegment(double[] coords) {
		switch (index) {
		case 0:
			coords[0] = x1;
			coords[1] = y1;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_MOVETO;
		case 1:
			coords[0] = x2;
			coords[1] = y2;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_LINETO;
		case 2:
			coords[0] = x3;
			coords[1] = y3;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_LINETO;
		case 3:
			coords[0] = x1;
			coords[1] = y1;
			if (affine != null) {
				affine.transform(coords, 0, coords, 0, 1);
			}
			return SEG_LINETO;
		case 4:
			return SEG_CLOSE;
		default:
			throw new NoSuchElementException("triangle iterator out of bounds");
		}
	}

	/**
	 * @see java.awt.geom.PathIterator#getWindingRule()
	 */
	public int getWindingRule() {
		return WIND_NON_ZERO;
	}

	/**
	 * @see java.awt.geom.PathIterator#isDone()
	 */
	public boolean isDone() {
		return index > 4;
	}

	/**
	 * @see java.awt.geom.PathIterator#next()
	 */
	public void next() {
		index++;
	}

}
