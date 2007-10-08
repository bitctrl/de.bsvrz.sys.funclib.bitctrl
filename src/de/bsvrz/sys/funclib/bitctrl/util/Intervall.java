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
 * Weiﬂenfelser Straﬂe 67
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
public class Intervall {

	/** Startzeitpunkt des Intervall. */
	public final long start;

	/** Endzeitpunkt des Intervall. */
	public final long ende;

	/** Die Breite des Intervall. */
	public final long breite;

	/** Flag, ob das Intervall einen Zeitraum beschreibt. */
	private final boolean zeitstempel;

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
	 * Kopierkonstruktor.
	 * 
	 * @param intervall
	 *            ein zu kopierendes Intervall.
	 */
	public Intervall(Intervall intervall) {
		this(intervall.start, intervall.ende, false);
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
	 * Gibt das Ende des Intervalls zur&uuml;ck.
	 * 
	 * @return Zeitstempel
	 */
	public long getEnde() {
		return ende;
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
		return (start < i.start && i.start < ende)
				|| (start < i.ende && i.ende < ende)
				|| (start > i.start && ende < i.ende);
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
	 * Die Hashfunktion verkn&uuml;pft den Start- und Endwert per XOR.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Long.valueOf(start).hashCode() ^ Long.valueOf(ende).hashCode();
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

	/**
	 * Stellt dieses Intervall einen Zeitraum dar.
	 * 
	 * @return {@code true}, wenn das Intervall einen Zeitraum beschreibt.
	 */
	public boolean isZeitstempel() {
		return zeitstempel;
	}

}
