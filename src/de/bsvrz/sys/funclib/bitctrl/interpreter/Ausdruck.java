/*
 * Interpreter, allgemeine Struktur zum Auswerten von Ausdrücken
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
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
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Basisklasse aller Symbole des Interpeters
 * 
 * @author BitCtrl, Schumann
 * @version $Id: Ausdruck.java 157 2007-02-23 12:37:29Z Schumann $
 */
public interface Ausdruck {

	/**
	 * Behelfsklasse, um ein paar n&uuml;tzliche statische Methoden im Interface
	 * unterzubringen.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	class Info {

		/**
		 * Bestimmt Rekursiv die Menge der Termsymbole im Ausdruck, die
		 * Variablen darstellen
		 * 
		 * @param ausdruck
		 *            Ein beliebiger Ausdruck
		 * @return Menge der Variablennamen
		 */
		public static Set<String> getVariablen(Ausdruck ausdruck) {
			Set<String> variablen;

			variablen = new HashSet<String>();
			if (ausdruck instanceof Variable) {
				// Wir sind an einem Termsymbol angekommen, was Variable ist
				variablen.add(((Variable) ausdruck).getName());
			} else if (ausdruck.getNachfolger() != null) {
				// Ausdruck ist kein Terminalsymbol
				for (Ausdruck a : ausdruck.getNachfolger()) {
					// Rekursion
					variablen.addAll(getVariablen(a));
				}
			}

			return variablen;
		}
	}

	/**
	 * Gibt eine Liste der verschachtelten Ausdr&uumlck;cke zur&uuml;ck.
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
	 * Interpretiert den Ausdruck im gegebenen Kontext
	 * 
	 * @param kontext
	 *            Kontext, indem der Ausdruck ausgewertet wird
	 * @return Wert des interpretierten Ausdrucks
	 */
	Object interpret(Kontext kontext);

}
