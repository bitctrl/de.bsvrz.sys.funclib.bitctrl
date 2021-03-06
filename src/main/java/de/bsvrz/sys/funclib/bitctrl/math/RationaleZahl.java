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

package de.bsvrz.sys.funclib.bitctrl.math;

import java.util.Objects;

/**
 * Repr&auml;sentiert eine rationale Zahl.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class RationaleZahl extends Number implements Comparable<RationaleZahl> {

	/** Repr&auml;sentiert 0 als rationale Zahl. */
	public static final RationaleZahl NULL = new RationaleZahl(0);

	/** Repr&auml;sentiert 1 als rationale Zahl. */
	public static final RationaleZahl EINS = new RationaleZahl(1);

	/** Serialisierungs-ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Addiert zwei ganze Zahlen.
	 *
	 * @param a
	 *            Erste ganze Zahl
	 * @param b
	 *            Zweite ganze Zahl
	 * @return Das Ergebnis der Addition
	 */
	public static RationaleZahl addiere(final long a, final long b) {
		return addiere(new RationaleZahl(a), new RationaleZahl(b));
	}

	/**
	 * Addiert eine rationale mit einer ganzen Zahl.
	 *
	 * @param a
	 *            Eine rationale Zahl
	 * @param b
	 *            Eine ganze Zahl
	 * @return Das Ergebnis der Addition
	 */
	public static RationaleZahl addiere(final RationaleZahl a, final long b) {
		return addiere(a, new RationaleZahl(b));
	}

	/**
	 * Addiert zwei rationale Zahlen.
	 *
	 * @param a
	 *            Erste rationale Zahl
	 * @param b
	 *            Zweite rationale Zahl
	 * @return Das Ergebnis der Addition
	 */
	public static RationaleZahl addiere(final RationaleZahl a,
			final RationaleZahl b) {
		final long z, n;

		z = (a.zaehler * b.nenner) + (b.zaehler * a.nenner);
		n = a.nenner * b.nenner;

		return kuerze(new RationaleZahl(z, n));
	}

	/**
	 * Dividiert zwei ganze Zahlen.
	 *
	 * @param a
	 *            Erste ganze Zahl
	 * @param b
	 *            Zweite ganze Zahl
	 * @return Das Ergebnis der Division
	 */
	public static RationaleZahl dividiere(final long a, final long b) {
		return dividiere(new RationaleZahl(a), new RationaleZahl(b));
	}

	/**
	 * Dividiert eine ganze durch eine rationale Zahl.
	 *
	 * @param a
	 *            Eine ganze Zahl
	 * @param b
	 *            Eine rationale Zahl
	 * @return Das Ergebnis der Division
	 */
	public static RationaleZahl dividiere(final long a, final RationaleZahl b) {
		return dividiere(new RationaleZahl(a), b);
	}

	/**
	 * Dividiert eine rationale durch eine ganze Zahl.
	 *
	 * @param a
	 *            Eine rationale Zahl
	 * @param b
	 *            Eine ganze Zahl
	 * @return Das Ergebnis der Division
	 */
	public static RationaleZahl dividiere(final RationaleZahl a, final long b) {
		return dividiere(a, new RationaleZahl(b));
	}

	/**
	 * Dividiert zwei rationale Zahlen.
	 *
	 * @param a
	 *            Erste rationale Zahl
	 * @param b
	 *            Zweite rationale Zahl
	 * @return Das Ergebnis der Division
	 */
	public static RationaleZahl dividiere(final RationaleZahl a,
			final RationaleZahl b) {
		return multipliziere(a, b.kehrwert());
	}

	/**
	 * Bestimmt den gr&ouml;&szlig;ten gemeinsamen Teiler zweier ganzer Zahlen.
	 *
	 * @param a
	 *            Erste ganze Zahl
	 * @param b
	 *            Zweite ganze Zahl
	 * @return Der gr&ouml;&szlig;te gemeinsame Teiler beider Zahlen
	 */
	public static long ggT(final long a, final long b) {
		if (b == 0) {
			return a;
		}

		return ggT(b, a % b);
	}

	/**
	 * Bestimmt das kleinste gemeinsame Vielfache zweier ganzer Zahlen.
	 *
	 * @param a
	 *            Erste ganze Zahl
	 * @param b
	 *            Zweite ganze Zahl
	 * @return Das kleinste gemeinsame Vielfache beider Zahlen
	 */
	public static long kgV(final long a, final long b) {
		return Math.abs(a * b) / ggT(a, b);
	}

	/**
	 * K&uuml;rzt einen Bruch.
	 *
	 * @param a
	 *            Ein Bruch als rationale Zahl
	 * @return Der gek&uuml;rzte Bruch
	 */
	public static RationaleZahl kuerze(final RationaleZahl a) {
		final long ggT;

		ggT = ggT(a.zaehler, a.nenner);

		return new RationaleZahl(a.zaehler / ggT, a.nenner / ggT);
	}

	/**
	 * Multipliziert zwei ganze Zahlen.
	 *
	 * @param a
	 *            Erste ganze Zahl
	 * @param b
	 *            Zweite ganze Zahl
	 * @return Das Ergebnis der Multiplikation
	 */
	public static RationaleZahl multipliziere(final long a, final long b) {
		return multipliziere(new RationaleZahl(a), new RationaleZahl(b));
	}

	/**
	 * Multipliziert eine rationale Zahlen mit einer ganzen Zahl.
	 *
	 * @param a
	 *            Eine rationale Zahl
	 * @param b
	 *            Eine ganze Zahl
	 * @return Das Ergebnis der Multiplikation
	 */
	public static RationaleZahl multipliziere(final RationaleZahl a,
			final long b) {
		return multipliziere(a, new RationaleZahl(b));
	}

	/**
	 * Multipliziert zwei rationale Zahlen.
	 *
	 * @param a
	 *            Erste rationale Zahl
	 * @param b
	 *            Zweite rationale Zahl
	 * @return Das Ergebnis der Multiplikation
	 */
	public static RationaleZahl multipliziere(final RationaleZahl a,
			final RationaleZahl b) {
		final long z, n;

		z = a.zaehler * b.zaehler;
		n = a.nenner * b.nenner;

		return kuerze(new RationaleZahl(z, n));
	}

	/**
	 * Berechnet die Potenz einer rationalen Basis mit einem ganzzahligen
	 * Exponenten.
	 *
	 * @param basis
	 *            Die Basis
	 * @param exponent
	 *            Der Exponent
	 * @return Die Potenz
	 */
	public static RationaleZahl potenz(final RationaleZahl basis,
			final int exponent) {
		RationaleZahl potenz;

		potenz = new RationaleZahl(basis);
		for (int i = 1; i < exponent; i++) {
			potenz = RationaleZahl.multipliziere(potenz, basis);
		}

		return potenz;
	}

	/**
	 * Subtrahiert zwei ganze Zahlen.
	 *
	 * @param a
	 *            Erste ganze Zahl
	 * @param b
	 *            Zweite ganze Zahl
	 * @return Das Ergebnis der Subtraktion
	 */
	public static RationaleZahl subtrahiere(final long a, final long b) {
		return subtrahiere(new RationaleZahl(a), new RationaleZahl(b));
	}

	/**
	 * Subtrahiert eine rationale von einer ganzen Zahl.
	 *
	 * @param a
	 *            Eine ganze Zahl
	 * @param b
	 *            Eine rationale Zahl
	 * @return Das Ergebnis der Subtraktion
	 */
	public static RationaleZahl subtrahiere(final long a,
			final RationaleZahl b) {
		return subtrahiere(new RationaleZahl(a), b);
	}

	/**
	 * Subtrahiert eine ganze von einer rationalen Zahl.
	 *
	 * @param a
	 *            Eine rationale Zahl
	 * @param b
	 *            Eine ganze Zahl
	 * @return Das Ergebnis der Subtraktion
	 */
	public static RationaleZahl subtrahiere(final RationaleZahl a,
			final long b) {
		return subtrahiere(a, new RationaleZahl(b));
	}

	/**
	 * Subtrahiert zwei rationale Zahlen.
	 *
	 * @param a
	 *            Erste rationale Zahl
	 * @param b
	 *            Zweite rationale Zahl
	 * @return Das Ergebnis der Subtraktion
	 */
	public static RationaleZahl subtrahiere(final RationaleZahl a,
			final RationaleZahl b) {
		final long z, n;

		z = (a.zaehler * b.nenner) - (b.zaehler * a.nenner);
		n = a.nenner * b.nenner;

		return kuerze(new RationaleZahl(z, n));
	}

	/** Z&auml;hler der rationalen Zahl. */
	private final long zaehler;

	/** Nenner der rationalen Zahl. */
	private final long nenner;

	/**
	 * Erzeugt aus einer endlichen reelen Zahl eine rationale Zahl. Da alle
	 * Double-Werte endlich sind, gibt es kein Problem.
	 *
	 * @param wert
	 *            eine Zahl, deren String-Repräsentation kein "E" enthalten
	 *            darf.
	 */
	public RationaleZahl(final double wert) {
		final String s, s1, s2;

		s = Double.valueOf(wert).toString();
		if (s.contains("E")) {
			throw new IllegalArgumentException(
					"Der Wert kann nicht als rationale Zahl dargestellt werden.");
		}

		s1 = s.substring(0, s.indexOf('.'));
		s2 = s.substring(s.indexOf('.') + 1);
		zaehler = Long.parseLong(s1 + s2);
		nenner = (long) Math.pow(10, Long.valueOf(s2.length()));
	}

	/**
	 * Konstruiert eine rationale Zahl als ganze Zahl. Der Nenner ist hier 1.
	 *
	 * @param zaehler
	 *            Der Z&auml;hler
	 */
	public RationaleZahl(final long zaehler) {
		this(zaehler, 1);
	}

	/**
	 * Konstruiert eine rationale Zahl als Quotient.
	 *
	 * @param zaehler
	 *            Der Z&auml;hler
	 * @param nenner
	 *            Der Nenner
	 */
	public RationaleZahl(final long zaehler, final long nenner) {
		if (nenner == 0) {
			throw new ArithmeticException("Null als Nenner ist nicht erlaubt.");
		}

		if (((zaehler > 0) && (nenner < 0))
				|| ((zaehler < 0) && (nenner < 0))) {
			this.zaehler = -zaehler;
			this.nenner = -nenner;
		} else {
			this.zaehler = zaehler;
			this.nenner = nenner;
		}
	}

	/**
	 * Konstruiert eine rationale Zahl aus einer anderen.
	 *
	 * @param zahl
	 *            Eine rationale Zahl
	 */
	public RationaleZahl(final RationaleZahl zahl) {
		this(zahl.zaehler, zahl.nenner);
	}

	@Override
	public int compareTo(final RationaleZahl zahl) {
		final long kgv;
		final Long a, b;

		kgv = kgV(nenner, zahl.nenner);
		a = zaehler * kgv;
		b = zahl.zaehler * kgv;

		return a.compareTo(b);
	}

	@Override
	public double doubleValue() {
		final double a, b;

		a = zaehler;
		b = nenner;

		return a / b;
	}

	@Override
	public boolean equals(final Object o) {
		if (o instanceof RationaleZahl) {
			final RationaleZahl r1, r2;

			r1 = kuerze(this);
			r2 = kuerze((RationaleZahl) o);
			return (r1.zaehler == r2.zaehler) && (r1.nenner == r2.nenner);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(zaehler, nenner);
	}

	@Override
	public float floatValue() {
		final float a, b;

		a = zaehler;
		b = nenner;

		return a / b;
	}

	/**
	 * Gibt den Nenner der rationalen Zahl zur&uuml;ck.
	 *
	 * @return Der Nenner
	 */
	public long getNenner() {
		return nenner;
	}

	/**
	 * Gibt den Z&auml;hler der rationalen Zahl zur&uuml;ck.
	 *
	 * @return Der Z&auml;hler
	 */
	public long getZaehler() {
		return zaehler;
	}

	@Override
	public int intValue() {
		return Math.round(floatValue());
	}

	/**
	 * Bildet den Kehrwert der rationalen Zahl. Es werden Z&auml;hler und Nenner
	 * vertauscht.
	 *
	 * @return Der Kehrwert der rationalen Zahl
	 */
	public RationaleZahl kehrwert() {
		return new RationaleZahl(nenner, zaehler);
	}

	@Override
	public long longValue() {
		return Math.round(doubleValue());
	}

	@Override
	public String toString() {
		if (nenner == 1) {
			return String.valueOf(zaehler);
		}

		return zaehler + "/" + nenner;
	}
}
