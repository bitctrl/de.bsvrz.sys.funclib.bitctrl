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

package de.bsvrz.sys.funclib.bitctrl.interpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Basisklasse aller Symbole des Interpeters.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker, Schumann
 */
public interface Ausdruck {

	/**
	 * Behelfsklasse, um ein paar n&uuml;tzliche statische Methoden im Interface
	 * unterzubringen.
	 */
	final class Info {

		/**
		 * Bestimmt Rekursiv die Menge der Termsymbole im Ausdruck, die
		 * Variablen darstellen.
		 *
		 * @param ausdruck
		 *            Ein beliebiger Ausdruck
		 * @return Menge der Variablennamen
		 */
		public static Set<String> getVariablen(final Ausdruck ausdruck) {
			final Set<String> variablen;

			variablen = new HashSet<String>();
			if (ausdruck instanceof Variable) {
				// Wir sind an einem Termsymbol angekommen, was Variable ist
				variablen.add(((Variable) ausdruck).getName());
			} else if (ausdruck.getNachfolger() != null) {
				// Ausdruck ist kein Terminalsymbol
				for (final Ausdruck a : ausdruck.getNachfolger()) {
					// Rekursion
					variablen.addAll(getVariablen(a));
				}
			}

			return variablen;
		}

		/**
		 * Konstruktor.
		 */
		private Info() {
			// privater Standardkonstruktor, wird nie verwendet
		}
	}

	/**
	 * Gibt eine Liste der verschachtelten Ausdr&uuml;cke zur&uuml;ck.
	 * <p>
	 * <em>Hinweis:</em> Terminalsymbole liefern keine leere Liste sondern
	 * {@code null} zur&uuml;ck. Eine leere Liste ist demnach ein Hinweis auf
	 * einen unvollst&auml;ndigen Syntaxbaum.
	 *
	 * @return Liste der Ausdr&uuml;cke <em>direkt</em> unter diesen Ausdruck.
	 *         Die Methode arbeiten im Gegensatz zu {@link #interpret(Kontext)}
	 *         nicht rekursiv.
	 */
	List<Ausdruck> getNachfolger();

	/**
	 * Interpretiert den Ausdruck im gegebenen Kontext.
	 *
	 * @param kontext
	 *            Kontext, indem der Ausdruck ausgewertet wird
	 * @return Wert des interpretierten Ausdrucks
	 */
	Object interpret(Kontext kontext);

}
