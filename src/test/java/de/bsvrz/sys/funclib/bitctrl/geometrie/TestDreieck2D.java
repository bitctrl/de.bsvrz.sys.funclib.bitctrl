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

package de.bsvrz.sys.funclib.bitctrl.geometrie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;

import org.junit.Before;
import org.junit.Test;

/**
 * Testfall für die Klasse.
 * {@link de.bsvrz.sys.funclib.bitctrl.geometrie.Dreieck2D}
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
@SuppressWarnings("nls")
public class TestDreieck2D {

	/** Dreieck für die Tests. */
	private Dreieck2D dreieck;

	/**
	 * Initialisiert das Testdreieck für alle Testfälle mit identischen Daten.
	 */
	@Before
	public void before() {
		dreieck = new Dreieck2D.Double(0, 0, 5, 0, 0, 5);
	}

	/**
	 * Punkt in Dreieck enthalten.
	 */
	@Test
	public final void testContainsPoint2D() {
		// Eckpunkte
		assertTrue(dreieck.contains(0, 0));
		assertTrue(dreieck.contains(0, 5));
		assertTrue(dreieck.contains(5, 0));

		// Innere Punkte
		assertTrue(dreieck.contains(1, 2));
		assertTrue(dreieck.contains(2, 2));
		assertTrue(dreieck.contains(3, 1));

		// Äußere Punkte
		assertFalse(dreieck.contains(-1, 2));
		assertFalse(dreieck.contains(3, -5));
		assertFalse(dreieck.contains(2, 4));
		assertFalse(dreieck.contains(4, 3));
	}

	/**
	 * Umfasst Dreieck ein Rechteck.
	 */
	@Test
	public final void testContainsRectangle2D() {
		// Innere Rechtecke
		assertTrue(dreieck.contains(0, 0, 2, 2));
		assertTrue(dreieck.contains(1, 1, 1, 1));
		assertTrue(dreieck.contains(0, 2, 1, 1));

		// Schneidene Rechtecke
		assertFalse(dreieck.contains(-1, 2, 3, 3));
		assertFalse(dreieck.contains(2, 0, 5, 2));
		assertFalse(dreieck.contains(1, -5, 3, 10));

		// Äußere Rechtecke
		assertFalse(dreieck.contains(-5, 2, 3, 3));
		assertFalse(dreieck.contains(1, 7, 5, 1));
		assertFalse(dreieck.contains(10, 12, 5, 4));
	}

	/**
	 * Umgebenes Rechteck bestimmen.
	 */
	@Test
	public final void testGetBounds2D() {
		final Rectangle2D bounds = new Rectangle2D.Double(0, 0, 5, 5);

		assertEquals(bounds, dreieck.getBounds2D());
	}

	/**
	 * Pfad-Iterator.
	 */
	@Test
	public final void testGetPathIteratorAffineTransform() {
		final PathIterator path = dreieck.getPathIterator(null);

		for (int i = 0; i < 5; i++) {
			assertFalse(path.isDone());

			final double[] coords = new double[6];
			final int typ = path.currentSegment(coords);

			switch (i) {
			case 0:
				assertEquals(PathIterator.SEG_MOVETO, typ);
				assertEquals(0.0, coords[0], 0);
				assertEquals(0.0, coords[1], 0);
				break;
			case 1:
				assertEquals(PathIterator.SEG_LINETO, typ);
				assertEquals(5.0, coords[0], 0);
				assertEquals(0.0, coords[1], 0);
				break;
			case 2:
				assertEquals(PathIterator.SEG_LINETO, typ);
				assertEquals(0.0, coords[0], 0);
				assertEquals(5.0, coords[1], 0);
				break;
			case 3:
				assertEquals(PathIterator.SEG_LINETO, typ);
				assertEquals(0.0, coords[0], 0);
				assertEquals(0.0, coords[1], 0);
				break;
			case 4:
				assertEquals(PathIterator.SEG_CLOSE, typ);
				break;
			default:
				fail("Unerwarteter Switch: " + i);
				break;
			}

			path.next();
		}
		assertTrue(path.isDone());
	}

	/**
	 * Schneidet Dreieck ein Rechteck.
	 */
	@Test
	public final void testIntersectsDoubleDoubleDoubleDouble() {
		// Innere Rechtecke
		assertTrue(dreieck.intersects(0, 0, 2, 2));
		assertTrue(dreieck.intersects(1, 1, 1, 1));
		assertTrue(dreieck.intersects(0, 2, 1, 1));

		// Schneidene Rechtecke
		assertTrue(dreieck.intersects(-1, 2, 3, 3));
		assertTrue(dreieck.intersects(2, 0, 5, 2));
		assertTrue(dreieck.intersects(1, -5, 3, 10));

		// Äußere Rechtecke
		assertFalse(dreieck.intersects(-5, 2, 3, 3));
		assertFalse(dreieck.intersects(1, 7, 5, 1));
		assertFalse(dreieck.intersects(10, 12, 5, 4));
	}

}
