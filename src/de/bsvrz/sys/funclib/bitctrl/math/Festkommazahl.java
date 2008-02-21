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

package de.bsvrz.sys.funclib.bitctrl.math;

/**
 * Repr&auml;sentiert eine Zahl mit einer festen Anzahl Kommastellen.
 * <p>
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class Festkommazahl extends Number implements Comparable<Festkommazahl> {

	/** Repr&auml;sentiert 0 als Festkommazahl. */
	public static final Festkommazahl NULL = new Festkommazahl(0);

	/** Repr&auml;sentiert 1 als Festkommazahl. */
	public static final Festkommazahl EINS = new Festkommazahl(1);

	/** Serialisierungs-ID. */
	private static final long serialVersionUID = 1L;

	/** Sichert den Zahlenwert unskaliert. */
	private final long wert;

	/** Der Faktor um den der Wert skaliert wird. */
	private final float skalierung;

	/**
	 * Konstruiert eine Festkommazahl mit dem angegebenen Wert und dem
	 * Skalierungsfaktor 1.
	 * 
	 * @param wert
	 *            Der skalierte Wert der neuen Festkommzahl
	 */
	public Festkommazahl(double wert) {
		this(wert, 1);
	}

	/**
	 * Konstruiert eine Festkommazahl mit dem angegebenen Wert und
	 * Skalierungsfaktor.
	 * 
	 * @param wert
	 *            Der skalierte Wert der neuen Festkommzahl
	 * @param skalierung
	 *            Faktor mit dem der Wert skaliert wird
	 */
	public Festkommazahl(double wert, float skalierung) {
		this.wert = Math.round(wert / skalierung);
		this.skalierung = skalierung;
	}

	/**
	 * Konstruiert eine Festkommazahl mit dem angegebenen Wert und der genannten
	 * Anzahl Dezimalstellen.
	 * 
	 * @param wert
	 *            Der skalierte Wert der neuen Festkommzahl
	 * @param stellen
	 *            Anzahl von Dezimalstellen um die der Wert skaliert werden
	 *            soll. Eine Zahl kleiner 0 entspricht dabei Kommastellen und
	 *            eine Zahl grˆﬂer 0 entspricht Zehnerpotenzen.
	 */
	public Festkommazahl(double wert, int stellen) {
		this(wert, (float) Math.pow(10, stellen));
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(Festkommazahl zahl) {
		Long a, b;

		a = wert;
		b = zahl.wert;
		return a.compareTo(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double doubleValue() {
		return wert * skalierung;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float floatValue() {
		return wert * skalierung;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int intValue() {
		return Math.round(wert * skalierung);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long longValue() {
		return Math.round(wert * skalierung);
	}

}
