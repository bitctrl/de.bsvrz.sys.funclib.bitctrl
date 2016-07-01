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

package de.bsvrz.sys.funclib.bitctrl.interpreter.logik;

import com.bitctrl.i18n.Messages;

import de.bsvrz.sys.funclib.bitctrl.interpreter.InterpreterException;
import de.bsvrz.sys.funclib.bitctrl.interpreter.InterpreterMessages;

/**
 * Der Wert eines LogischenAsudrucks, der f&uuml;r Boolesche Logik und
 * Fuzzy-Logik verwendbar ist. Boolsche Logik wird intern durch die
 * Zugeh&ouml;rigkeitswerte "1" und "0" repr&auml;sentiert.
 *
 * @author BitCtrl Systems GmbH, Peuker
 * @author BitCtrl Systems GmbH, Schumann
 */
public class LogischerWert {

	/**
	 * Eine globale Instanz f&uuml;r den boolschen Wert <i>true</i>.
	 */
	public static final LogischerWert WAHR = new LogischerWert(true);

	/**
	 * Eine globale Instanz f&uuml;r den boolschen Wert <i>false</i>.
	 */
	public static final LogischerWert FALSCH = new LogischerWert(false);

	/**
	 * liefert die statische Instanz eines logischen Wertes f&uuml;r die
	 * Boolschen Werte WAHR und FALSCH.
	 *
	 * @param wert
	 *            der boolsche Wert.
	 * @return die Instanz.
	 */
	public static final LogischerWert valueOf(final boolean wert) {
		if (wert) {
			return WAHR;
		}
		return FALSCH;
	}

	/**
	 * Die Zugeh&ouml;rigkeit.
	 */
	private Float zugehoerigkeit;

	/**
	 * Gibt an, ob der Wert gerade ein logischer oder ein
	 * Zugeh&ouml;rigkeitswert ist.
	 */
	private boolean boolWert;

	/**
	 * Der Konstruktor erzeugt einen logischen Wert mit der Zugeh&ouml;rigkeit
	 * "1" f&uuml;r <i>true</i> bzw "0" f&uuml;r <i>false</i>.
	 *
	 * @param wert
	 *            Der boolsche Wert, den der logische Wert repr&auml;sentieren
	 *            soll
	 */
	public LogischerWert(final boolean wert) {
		set(wert);
	}

	/**
	 * Der Konstruktor erzeugt einen logischen Wert mit der &uuml;bergebenen
	 * Zugeh&ouml;rigkeit.
	 *
	 * @param wert
	 *            Der Zugeh&ouml;rigkeitswert
	 */
	public LogischerWert(final Float wert) {
		set(wert);
	}

	/**
	 * Zwei logische Werte sind gleich, wenn sie beide den selben Typ (logischer
	 * Wert oder Zugeh&ouml;rigkeit) und Wert besitzen.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof LogischerWert) {
			final LogischerWert lw = (LogischerWert) obj;
			final Float f1, f2;
			f1 = zugehoerigkeit;
			f2 = lw.zugehoerigkeit;
			if (!(boolWert ^ lw.boolWert) && f1.equals(f2)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gibt den aktuellen Wert zur&uuml;ck.
	 *
	 * @return Wert vom Typ {@code Float} oder {@code Boolean}
	 */
	public Object get() {
		if (!boolWert) {
			return zugehoerigkeit;
		}

		assert(zugehoerigkeit == 0) || (zugehoerigkeit == 1);

		if (getZugehoerigkeit() == 1) {
			return true;
		}

		return false;
	}

	/**
	 * Liefert einen booleschen Wert entsprechend der Zugeh&ouml;rigkeit des
	 * logischen Wertes. Die Zugeh&ouml;rigkeit "1" entspricht dem booleschen
	 * Wert <i>true</i>, die Zugeh&ouml;rigkeit "0" entspricht dem booleschen
	 * Wert <i>false</i>. Hat die Zugeh&ouml;rigkeit einen anderen Wert wird
	 * eine <b>InterpreterException</b> geworfen.
	 *
	 * @return Den Wert
	 */
	public boolean getBoolWert() {
		if (!boolWert) {
			throw new InterpreterException(
					Messages.get(InterpreterMessages.NoBooleanValue));
		}

		assert(zugehoerigkeit == 0) || (zugehoerigkeit == 1);

		if (getZugehoerigkeit() == 1) {
			return true;
		}

		return false;
	}

	/**
	 * Liefert den Zugeh&ouml;rigkeitswert, der durch den logischen Wert
	 * repr&auml;sentiert wird.
	 *
	 * @return Wert
	 */
	public Float getZugehoerigkeit() {
		return zugehoerigkeit;
	}

	/**
	 * Pr&uuml;ft ob der logische Wert ein boolescher Wert ist.
	 *
	 * @return {@code true}, wenn der Wert ein boolescher Wert ist, sonst
	 *         {@code false}
	 */
	public boolean isBoolWert() {
		return boolWert;
	}

	/**
	 * Setzt den Wert auf den angegebenen booleschen Wert.
	 *
	 * @param wert
	 *            boolescher Wert
	 */
	public void set(final boolean wert) {
		if (wert) {
			set(1f);
		} else {
			set(0f);
		}
		boolWert = true;
	}

	/**
	 * Setzt den Wert auf die angegebene Zugeh&ouml;rigkeit.
	 *
	 * @param z
	 *            Zugeh&ouml;rigkeit.
	 */
	public void set(final Float z) {
		if ((z != null) && ((z < 0) || (z > 1))) {
			throw new InterpreterException(
					Messages.get(InterpreterMessages.BadMembership, z));
		}

		zugehoerigkeit = z;
		boolWert = false;
	}

	/**
	 * Wenn der logische Wert ein boolescher Wert ist, wird "wahr" oder "falsch"
	 * zur&uuml;ckgegeben, sonst der Zahlenwert der Zugeh&ouml;rigkeit.
	 */
	@Override
	public String toString() {
		final String result;

		if (boolWert) {
			if (getBoolWert()) {
				result = "wahr";
			} else {
				result = "falsch";
			}
		} else {
			result = String.valueOf(getZugehoerigkeit());
		}

		return result;
	}

}
