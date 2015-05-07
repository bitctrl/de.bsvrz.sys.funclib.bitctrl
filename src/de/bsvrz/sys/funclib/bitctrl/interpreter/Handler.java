/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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

import java.util.List;

/**
 * Schnittstelle f&uuml;r alle Klassen die Operationen des Interpreters
 * implementieren. Statt jeder Operation eine eigene Klasse zu geben, werden mit
 * Hilfe des Musters <em>Besucher</em> alle Operationen in einer Klasse
 * geb&uuml;ndelt. Neue Operationen k&ouml;nnen durch implementieren dieser
 * Schnittstelle definiert werden. Siehe auch {@link AbstractHandler}
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public interface Handler {

	/**
	 * Gibt die Liste der Operatoren zur&uuml;ck, die der Handler verarbeiten
	 * kann.
	 *
	 * @return Liste von Operatoren
	 */
	Operator[] getHandledOperators();

	/**
	 * F&uuml;hrt eine Operation mit der Liste der Operanden aus. Die Liste der
	 * Operanden wird entsprechend der Ordung des Iterators abgearbeitet.
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Liste der Operanden
	 * @return Wert der Operation, abh&auml;ngig von Operator und Operanden
	 */
	Object perform(Operator operator, List<Object> operanden);

	/**
	 * F&uuml;hrt eine Operation mit dem Operanden aus.
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Operandenliste
	 * @return Wert der Operation, abh&auml;ngig von Operator und Operand
	 */
	Object perform(Operator operator, Object... operanden);

	/**
	 * Pr&uuml;ft ob der Handler die angegebene Operation auf Liste der
	 * Operanden anwenden kann.
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Liste von Operanden
	 * @return das Ergebnis der &Uuml;berpr&uuml;fung
	 */
	HandlerValidation validiereHandler(Operator operator,
			List<? extends Object> operanden);

	/**
	 * Pr&uuml;ft ob der Handler die angegebene Operation auf dem Operanden
	 * anwenden kann.
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Operandenliste
	 * @return das Ergebnis der &Uuml;berpr&uuml;fung
	 */
	HandlerValidation validiereHandler(Operator operator, Object... operanden);

}
