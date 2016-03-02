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

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Repr&auml;sentation des Ergebnisses einer Handler-Validierung.<br>
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class HandlerValidation {

	/**
	 * der Handler kann mit der Anzahl der übergebenen Operanden umgehen.
	 */
	private final boolean richtigeAnzahl;

	/**
	 * alle Operanden, die mit dem Handler verarbeitet werden sollen, haben den
	 * passenden Typ.
	 */
	private final boolean richtigerTyp;

	/**
	 * Konstruktor.
	 *
	 * @param richtigeAnzahl
	 *            die Anzahl der Operanden ist korrekt
	 * @param richtigerTyp
	 *            die Operanden haben passende Typen
	 */
	public HandlerValidation(final boolean richtigeAnzahl,
			final boolean richtigerTyp) {
		this.richtigeAnzahl = richtigeAnzahl;
		this.richtigerTyp = richtigerTyp;
	}

	/**
	 * ermittelt, ob das Ergebnis die korrekte Anzahl von Operanden bescheinigt.
	 *
	 * @return <i>true</i>, wenn die Anzahl der gepr&uuml;ften Operanden korrekt
	 *         war
	 */
	public boolean isRichtigeAnzahl() {
		return richtigeAnzahl;
	}

	/**
	 * ermittelt, ob das Ergebnis die passenden Operanden bescheinigt.
	 *
	 * @return <i>true</i>, wenn die gepr&uuml;ften Operanden passend sind
	 */
	public boolean isRichtigerTyp() {
		return richtigerTyp;
	}

	/**
	 * ermittelt, ob das Ergebnis von der Validierung eines passenden Handler
	 * generiert wurde.
	 *
	 * @return <i>true</i>, wenn der Handler, der das Ergebnis geliefert hat,
	 *         passend ist
	 */
	public boolean isValid() {
		return richtigeAnzahl && richtigerTyp;
	}
}
