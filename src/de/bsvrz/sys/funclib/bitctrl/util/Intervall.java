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

package de.bsvrz.sys.funclib.bitctrl.util;

import java.text.DateFormat;
import java.util.Date;

/**
 * Repr&auml;sentiert ein Intervall f&uuml;r <code>long</code>-Werte. Wird
 * f&uuml;r Zeitintervalle genutzt, die mit Zeitstempeln arbeiten.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class Intervall implements Cloneable {

	/** Startzeitpunkt des Intervall. */
	public final long start;

	/** Endzeitpunkt des Intervall. */
	public final long ende;

	/** Die Breite des Intervall. */
	public final long breite;

	/** Flag, ob das Intervall einen Zeitraum beschreibt. */
	private final boolean zeitstempel;

	/**
	 * Kopierkonstruktor.
	 * 
	 * @param intervall
	 *            ein zu kopierendes Intervall.
	 */
	public Intervall(Intervall intervall) {
		this(intervall.start, intervall.ende, false);
	}

	/**
	 * Kopierkonstruktor.
	 * 
	 * @param intervall
	 *            ein zu kopierendes Intervall.
	 * @param zeitstempel
	 *            handelt es sich um ein zeitliches Intervall?
	 */
	public Intervall(Intervall intervall, boolean zeitstempel) {
		this(intervall.start, intervall.ende, zeitstempel);
	}

	/**
	 * Konstruiert das Intervall mit dem angegebenen Grenzen.
	 * 
	 * @param start
	 *            Start des Intervalls
	 * @param ende
	 *            Ende des Intervalls
	 */
	public Intervall(long start, long ende) {
		this(start, ende, true);
	}

	/**
	 * Konstruiert das Intervall mit dem angegebenen Grenzen.
	 * 
	 * @param start
	 *            Start des Intervalls
	 * @param ende
	 *            Ende des Intervalls
	 * @param zeitstempel
	 *            handelt es sich um ein zeitliches Intervall?
	 */
	public Intervall(long start, long ende, boolean zeitstempel) {
		if (start > ende) {
			throw new IllegalArgumentException(
					"Das Ende des Intervalls darf nicht vor dessen Start liegen (start="
							+ start + ", ende=" + ende + ").");
		}

		this.start = start;
		this.ende = ende;
		this.zeitstempel = zeitstempel;
		this.breite = ende - start;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Zwei Intervalle sind gleich, wenn sie den selben Start- und Endwert
	 * besitzen.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Intervall) {
			Intervall intervall = (Intervall) o;

			if (start == intervall.start && ende == intervall.ende) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gibt die Breite des Intervalls zur&uuml;ck.
	 * 
	 * @return Intervallbreite
	 */
	public long getBreite() {
		return breite;
	}

	/**
	 * Gibt das Ende des Intervalls zur&uuml;ck.
	 * 
	 * @return Zeitstempel
	 */
	public long getEnde() {
		return ende;
	}

	/**
	 * Gibt den Anfang des Intervalls zur&uuml;ck.
	 * 
	 * @return Zeitstempel
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Prüftft ob ein anderes Intervall in diesem Intervall enhalten ist.
	 * 
	 * @param intervall
	 *            ein Intervall.
	 * @return {@code true}, wenn das andere Intervall innerhalb dieses
	 *         Intervalls liegt oder mit ihm identisch ist.
	 */
	public boolean isEnthalten(Intervall intervall) {
		return start <= intervall.start && intervall.ende <= ende;
	}

	/**
	 * Pr&uuml;ft ob ein Wert im Intervall enhalten ist.
	 * 
	 * @param wert
	 *            Ein Wert
	 * @return {@code true}, wenn der Wert innerhalb des Intervalls oder auf
	 *         einer der Intervallgrenzen liegt
	 */
	public boolean isEnthalten(long wert) {
		return start <= wert && wert <= ende;
	}

	/**
	 * Prüft ob sich ein anderes Intervall innerhalb der Intervallgrenzen dieses
	 * Intervalls befindet.
	 * 
	 * @param intervall
	 *            ein Intervall.
	 * @return {@code true}, wenn das andere Intervall innerhalb dieses
	 *         Intervalls liegt, aber nicht mit ihm identisch ist.
	 */
	public boolean isInnerhalb(Intervall intervall) {
		return start < intervall.start && intervall.ende < ende;
	}

	/**
	 * Pr&uuml;ft ob sich ein Wert innerhalb der Intervallgrenzen befindet.
	 * 
	 * @param wert
	 *            Ein Wert
	 * @return {@code true}, wenn der Wert innerhalb des Intervalls, aber nicht
	 *         auf einer der Intervallgrenzen liegt
	 */
	public boolean isInnerhalb(long wert) {
		return start < wert && wert < ende;
	}

	/**
	 * Stellt dieses Intervall einen Zeitraum dar.
	 * 
	 * @return {@code true}, wenn das Intervall einen Zeitraum beschreibt.
	 */
	public boolean isZeitstempel() {
		return zeitstempel;
	}

	/**
	 * Pr&uuml;ft ob sich zwei Intervalle schneiden. Zwei Intervalle schneiden
	 * sich, wenn sie mindestens einen Punkt gemeinsam haben (exklusive den
	 * Intervallgrenzen).
	 * 
	 * @param i
	 *            Ein anderes Intervall
	 * @return {@code true}, wenn sich dieses Intervall mit dem anderen
	 *         schneidet
	 */
	public boolean schneidet(Intervall i) {
		return i.isInnerhalb(start) || i.isInnerhalb(ende)
				|| (start == i.start && ende == i.ende);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (zeitstempel) {
			DateFormat formatter;

			formatter = DateFormat.getDateTimeInstance();
			return "[" + formatter.format(new Date(start)) + ", "
					+ formatter.format(new Date(ende)) + "]";
		}
		return "[" + start + ", " + ende + "]";
	}

}
