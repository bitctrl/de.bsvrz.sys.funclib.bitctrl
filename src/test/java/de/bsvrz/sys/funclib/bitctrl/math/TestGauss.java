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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.math;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bsvrz.sys.funclib.bitctrl.math.algebra.Gauss;
import de.bsvrz.sys.funclib.bitctrl.math.algebra.Matrix;
import de.bsvrz.sys.funclib.bitctrl.math.algebra.Vektor;

/**
 * Testet alle relevanten Methoden der Klasse {@link Gauss}.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class TestGauss {

	/**
	 * Testmethode f&uuml;r {@link Gauss#bestimmeLRZerlegung(Matrix)}.
	 */
	@Test
	public void testBestimmeLRZerlegungA() {
		final Matrix a, lr, l, r;

		a = new Matrix(4, 4);
		a.set(0, 0, 1);
		a.set(0, 1, 2);
		a.set(0, 2, -1);
		a.set(0, 3, 6);
		a.set(1, 0, 2);
		a.set(1, 1, -4);
		a.set(1, 2, 2);
		a.set(1, 3, -2);
		a.set(2, 0, -1);
		a.set(2, 1, 4);
		a.set(2, 2, 1);
		a.set(2, 3, 4);
		a.set(3, 0, 3);
		a.set(3, 1, -2);
		a.set(3, 2, 3);
		a.set(3, 3, 1);

		lr = Gauss.bestimmeLRZerlegung(a);
		r = Gauss.extrahiereMatrixR(lr);
		l = Gauss.extrahiereMatrixL(lr);
		assertEquals(a, Matrix.multipliziere(l, r));
	}

	/**
	 * Testmethode f&uuml;r {@link Gauss#bestimmeLRZerlegung(Matrix)}.
	 */
	@Test
	public void testBestimmeLRZerlegungB() {
		final Matrix a, lr, l, r;

		a = new Matrix(3, 3);
		a.set(0, 0, 2);
		a.set(0, 1, 3);
		a.set(0, 2, 5);
		a.set(1, 0, 6);
		a.set(1, 1, 10);
		a.set(1, 2, 17);
		a.set(2, 0, 8);
		a.set(2, 1, 14);
		a.set(2, 2, 28);

		lr = Gauss.bestimmeLRZerlegung(a);
		r = Gauss.extrahiereMatrixR(lr);
		l = Gauss.extrahiereMatrixL(lr);
		assertEquals(a, Matrix.multipliziere(l, r));
	}

	/**
	 * Testmethode f&uuml;r {@link Gauss#loeseLGS(Matrix, Vektor)}.
	 */
	@Test
	public void testLoeseLGSA() {
		final Matrix a;
		final Vektor b, x;

		a = new Matrix(2, 2);
		a.set(0, 0, 8);
		a.set(0, 1, 3);
		a.set(1, 0, 5);
		a.set(1, 1, 2);

		b = new Vektor(30, 19);

		x = Gauss.loeseLGS(a, b);
		assertEquals(b, Matrix.multipliziere(a, x).getVektor());
	}

	/**
	 * Testmethode f&uuml;r {@link Gauss#loeseLGS(Matrix, Vektor)}.
	 */
	@Test
	public void testLoeseLGSB() {
		final Matrix a;
		final Vektor b, x;

		a = new Matrix(4, 4);
		a.set(0, 0, 1);
		a.set(0, 1, 2);
		a.set(0, 2, -1);
		a.set(0, 3, 6);
		a.set(1, 0, 2);
		a.set(1, 1, -4);
		a.set(1, 2, 2);
		a.set(1, 3, -2);
		a.set(2, 0, -1);
		a.set(2, 1, 4);
		a.set(2, 2, 1);
		a.set(2, 3, 4);
		a.set(3, 0, 3);
		a.set(3, 1, -2);
		a.set(3, 2, 3);
		a.set(3, 3, 1);

		b = new Vektor(33, -6, 13, 11);

		x = Gauss.loeseLGS(a, b);
		assertEquals(b, Matrix.multipliziere(a, x).getVektor());
	}

	/**
	 * Testmethode f&uuml;r {@link Gauss#loeseLGS(Matrix, Vektor)}.
	 */
	@Test
	public void testLoeseLGSC() {
		final Matrix a;
		final Vektor b, x;

		a = new Matrix(3, 3);
		a.set(0, 0, 2);
		a.set(0, 1, 3);
		a.set(0, 2, 5);
		a.set(1, 0, 6);
		a.set(1, 1, 10);
		a.set(1, 2, 17);
		a.set(2, 0, 8);
		a.set(2, 1, 14);
		a.set(2, 2, 28);

		b = new Vektor(1, 2, 3);

		x = Gauss.loeseLGS(a, b);
		assertEquals(b, Matrix.multipliziere(a, x).getVektor());
	}

}
