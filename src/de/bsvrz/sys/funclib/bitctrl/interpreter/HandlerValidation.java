/**
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
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Wei&szlig;enfelser Stra&szlig;e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;

/**
 * Repr&auml;sentation des Ergebnisses einer Handler-Validierung.<br>
 *
 * @author peuker
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
	public HandlerValidation(boolean richtigeAnzahl, boolean richtigerTyp) {
		this.richtigeAnzahl = richtigeAnzahl;
		this.richtigerTyp = richtigerTyp;
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

	/**
	 * ermittelt, ob das Ergebnis die korrekte Anzahl von Operanden bescheinigt.
	 *
	 * @return <i>true</i>, wenn die Anzahl der gepr&uuml;ften Operanden
	 *         korrekt war
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
}
