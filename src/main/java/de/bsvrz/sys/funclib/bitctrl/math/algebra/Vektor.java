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

package de.bsvrz.sys.funclib.bitctrl.math.algebra;

import de.bsvrz.sys.funclib.bitctrl.math.RationaleZahl;

/**
 * Repr&auml;sentiert einen Zahlenvektor.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class Vektor implements Cloneable {

	/**
	 * Addiert zwei Vektoren.
	 *
	 * @param a
	 *            Erster Vektor
	 * @param b
	 *            Zweiter Vektor
	 * @return Das Ergebnis der Vektoraddition
	 * @throws IllegalArgumentException
	 *             Wenn die beiden Vektoren nicht die gleiche Komponentenanzahl
	 *             aufweisen
	 */
	public static Vektor addiere(final Vektor a, final Vektor b) {
		if (a.anzahlKomponenten() != b.anzahlKomponenten()) {
			throw new IllegalArgumentException(
					"Die beiden Vektoren haben nicht die selbe Komponentenanzahl.");
		}

		Vektor v;

		v = new Vektor(a.anzahlKomponenten());
		for (int i = 0; i < a.anzahlKomponenten(); i++) {
			v.set(i, RationaleZahl.addiere(a.get(i), b.get(i)));
		}

		return v;
	}

	/**
	 * Dividiert einen Vektor mit einem Skalar.
	 *
	 * @param a
	 *            Ein Vektor
	 * @param s
	 *            Ein Skalar
	 * @return Das Vielfache des Vektors
	 */
	public static Vektor dividiere(final Vektor a, final long s) {
		return dividiere(a, new RationaleZahl(s));
	}

	/**
	 * Dividiert einen Vektor mit einem Skalar.
	 *
	 * @param a
	 *            Ein Vektor
	 * @param s
	 *            Ein Skalar
	 * @return Das Vielfache des Vektors
	 */
	public static Vektor dividiere(final Vektor a, final RationaleZahl s) {
		return multipliziere(a, s.kehrwert());
	}

	/**
	 * Multipliziert einen Vektor mit einem Skalar.
	 *
	 * @param a
	 *            Ein Vektor
	 * @param s
	 *            Ein Skalar
	 * @return Das Vielfache des Vektors
	 */
	public static Vektor multipliziere(final Vektor a, final long s) {
		return multipliziere(a, new RationaleZahl(s));
	}

	/**
	 * Multipliziert einen Vektor mit einem Skalar.
	 *
	 * @param a
	 *            Ein Vektor
	 * @param s
	 *            Ein Skalar
	 * @return Das Vielfache des Vektors
	 */
	public static Vektor multipliziere(final Vektor a, final RationaleZahl s) {
		Vektor v;

		v = new Vektor(a.anzahlKomponenten());
		for (int i = 0; i < a.anzahlKomponenten(); i++) {
			v.set(i, RationaleZahl.multipliziere(a.get(i), s));
		}

		return v;
	}

	/**
	 * Berechnet das Skalarprodukt zweier Vektoren.
	 *
	 * @param a
	 *            Erster Vektor
	 * @param b
	 *            Zweiter Vektor
	 * @return Das Skalarprodukt
	 * @throws IllegalArgumentException
	 *             Wenn die beiden Vektoren nicht die gleiche Komponentenanzahl
	 *             von 2 oder 3 aufweisen
	 */
	public static RationaleZahl skalarprodukt(final Vektor a, final Vektor b) {
		if (a.anzahlKomponenten() != b.anzahlKomponenten()) {
			throw new IllegalArgumentException(
					"Die beiden Vektoren haben nicht die selbe Komponentenanzahl.");
		}

		RationaleZahl s;

		s = new RationaleZahl(0);
		for (int i = 0; i < a.anzahlKomponenten(); i++) {
			s = RationaleZahl.addiere(s,
					RationaleZahl.multipliziere(a.get(i), b.get(i)));
		}

		return s;

	}

	/**
	 * Subtrahiert zwei Vektoren.
	 *
	 * @param a
	 *            Erster Vektor
	 * @param b
	 *            Zweiter Vektor
	 * @return Das Ergebnis der Vektorsubtraktion
	 * @throws IllegalArgumentException
	 *             Wenn die beiden Vektoren nicht die gleiche Komponentenanzahl
	 *             aufweisen
	 */
	public static Vektor subtrahiere(final Vektor a, final Vektor b) {
		if (a.anzahlKomponenten() != b.anzahlKomponenten()) {
			throw new IllegalArgumentException(
					"Die beiden Vektoren haben nicht die selbe Komponentenanzahl.");
		}

		Vektor v;

		v = new Vektor(a.anzahlKomponenten());
		for (int i = 0; i < a.anzahlKomponenten(); i++) {
			v.set(i, RationaleZahl.subtrahiere(a.get(i), b.get(i)));
		}

		return v;
	}

	/**
	 * Berechnet das Vektorprodukt zweier Vektoren.
	 *
	 * @param a
	 *            Erster Vektor
	 * @param b
	 *            Zweiter Vektor
	 * @return Das Vektorprodukt
	 * @throws IllegalArgumentException
	 *             Wenn die beiden Vektoren nicht die gleiche Komponentenanzahl
	 *             von 3 aufweisen
	 */
	public static Vektor vektorprodukt(final Vektor a, final Vektor b) {
		if (a.anzahlKomponenten() != b.anzahlKomponenten()) {
			throw new IllegalArgumentException(
					"Die beiden Vektoren haben nicht die selbe Komponentenanzahl.");
		}

		if (a.anzahlKomponenten() != 3) {
			throw new IllegalArgumentException(
					"Die Komponentenanzahl entspricht nicht 3.");
		}

		Vektor v;

		v = new Vektor(a.anzahlKomponenten());
		v.set(0, RationaleZahl.subtrahiere(
				RationaleZahl.multipliziere(a.get(1), b.get(2)),
				RationaleZahl.multipliziere(a.get(2), b.get(1))));
		v.set(1, RationaleZahl.subtrahiere(
				RationaleZahl.multipliziere(a.get(2), b.get(0)),
				RationaleZahl.multipliziere(a.get(0), b.get(2))));
		v.set(2, RationaleZahl.subtrahiere(
				RationaleZahl.multipliziere(a.get(0), b.get(1)),
				RationaleZahl.multipliziere(a.get(1), b.get(0))));

		return v;

	}

	/** Interner Speicher der Vektorkomponenten. */
	private final RationaleZahl[] vektor;

	/**
	 * Konstruiert einen leeren Vektor.
	 *
	 * @param n
	 *            Gew&uuml;nschte Gr&ouml;&szlig;e des Vektors
	 * @throws IllegalArgumentException
	 *             Wenn die Gr&ouml;&szlig;e kleiner als 1 ist
	 */
	public Vektor(final int n) {
		if (n < 1) {
			throw new IllegalArgumentException(
					"Anzahl der Komponenten muss grˆﬂer oder gleich 1 sein.");
		}

		vektor = new RationaleZahl[n];
		for (int i = 0; i < n; i++) {
			vektor[i] = RationaleZahl.NULL;
		}
	}

	/**
	 * Konstruiert einen Vektor aus einem Feld.
	 *
	 * @param vektor
	 *            Ein Feld
	 * @throws IllegalArgumentException
	 *             Wenn die L&auml;nge des Felds kleiner als 1 ist
	 */
	public Vektor(final long... vektor) {
		this(vektor.length);
		for (int i = 0; i < vektor.length; i++) {
			set(i, vektor[i]);
		}
	}

	/**
	 * Konstruiert einen Vektor aus einem Feld.
	 *
	 * @param vektor
	 *            Ein Feld
	 * @throws IllegalArgumentException
	 *             Wenn die L&auml;nge des Felds kleiner als 1 ist
	 */
	public Vektor(final RationaleZahl... vektor) {
		this(vektor.length);
		for (int i = 0; i < vektor.length; i++) {
			set(i, new RationaleZahl(vektor[i]));
		}
	}

	/**
	 * Konstruiert einen Vektor aus einem bestehenden Vektor.
	 *
	 * @param vektor
	 *            Ein Vektor
	 */
	public Vektor(final Vektor vektor) {
		this(vektor.anzahlKomponenten());
		for (int i = 0; i < vektor.anzahlKomponenten(); i++) {
			set(i, new RationaleZahl(vektor.get(i)));
		}
	}

	/**
	 * Gibt die Anzahl der Komponenten des Vektors zur&uuml;ck.
	 *
	 * @return Komponentenanzahl
	 */
	public int anzahlKomponenten() {
		return vektor.length;
	}

	/**
	 * Gibt den quadrierten Betrag (=L&auml;nge) des Vektors zur&uuml;ck. Das
	 * Quadrieren hat den Vorteil, dass der Wert eine ganze Zahl ist.
	 *
	 * @return Der Betrag des Vektors zum Quadrat
	 */
	public RationaleZahl betragQuadrat() {
		RationaleZahl bq;

		bq = new RationaleZahl(0);
		for (int i = 0; i < anzahlKomponenten(); i++) {
			bq = RationaleZahl.addiere(bq,
					RationaleZahl.multipliziere(vektor[i], vektor[i]));
		}

		return bq;
	}

	/**
	 * Zwei Vektoren sind gleich, wenn sie die gleiche Anzahl Komponenten
	 * besitzen und in allen Komponenten &uuml;bereinstimmen.
	 */
	@Override
	public boolean equals(final Object o) {
		if (o instanceof Vektor) {
			Vektor v;
			boolean gleich;

			v = (Vektor) o;

			if (anzahlKomponenten() != v.anzahlKomponenten()) {
				return false;
			}

			gleich = true;
			for (int i = 0; i < anzahlKomponenten(); i++) {
				if (!vektor[i].equals(v.vektor[i])) {
					gleich = false;
					break;
				}
			}
			return gleich;
		}

		return false;
	}

	/**
	 * Gibt eine bestimmte Komponente des Vektors zur&uuml;ck.
	 *
	 * @param i
	 *            Index der gesuchten Komponente
	 * @return Wert der gesuchten Komponente
	 */
	public RationaleZahl get(final int i) {
		return vektor[i];
	}

	/**
	 * Setzt den Wert einer Vektorkomponente.
	 *
	 * @param i
	 *            Index der Komponente
	 * @param wert
	 *            Neuer Wert der Vektorkomponenten
	 */
	public void set(final int i, final long wert) {
		set(i, new RationaleZahl(wert));
	}

	/**
	 * Setzt den Wert einer Vektorkomponente.
	 *
	 * @param i
	 *            Index der Komponente
	 * @param wert
	 *            Neuer Wert der Vektorkomponenten
	 */
	public void set(final int i, final RationaleZahl wert) {
		vektor[i] = new RationaleZahl(wert);
	}

	@Override
	public String toString() {
		String s;

		s = "(";
		for (int i = 0; i < anzahlKomponenten(); i++) {
			s += get(i);
			if (i < (anzahlKomponenten() - 1)) {
				s += ", ";
			}
		}
		s += ")";

		return s;
	}

}
