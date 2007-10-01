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

import java.util.List;

/**
 * Schnittstelle f&uuml;r alle Klassen die Operationen des Interpreters
 * implementieren. Statt jeder Operation eine eigene Klasse zu geben, werden mit
 * Hilfe des Musters <em>Besucher</em> alle Operationen in einer Klasse
 * geb&uuml;ndelt. Neue Operationen k&ouml;nnen durch implementieren dieser
 * Schnittstelle definiert werden. Siehe auch {@link AbstractHandler}
 *
 * @author BitCtrl, Schumann
 * @version $Id: Handler.java 158 2007-02-23 12:52:55Z Schumann $
 */
public interface Handler {

	/**
	 * F&uuml;hrt eine Operation mit dem Operanden aus
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Operandenliste
	 * @return Wert der Operation, abh&auml;ngig von Operator und Operand
	 */
	public abstract Object perform(Operator operator, Object... operanden);

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
	public Object perform(Operator operator, List<Object> operanden);

	/**
	 * Pr&uuml;ft ob der Handler die angegebene Operation auf dem Operanden
	 * anwenden kann
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Operandenliste
	 * @return das Ergebnis der &Uuml;berpr&uuml;fung
	 */
	public abstract HandlerValidation validiereHandler(Operator operator, Object... operanden);

	/**
	 * Pr&uuml;ft ob der Handler die angegebene Operation auf Liste der
	 * Operanden anwenden kann
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Liste von Operanden
	 * @return das Ergebnis der &Uuml;berpr&uuml;fung
	 */
	public HandlerValidation validiereHandler(Operator operator, List<? extends Object> operanden);

	/**
	 * Gibt die Liste der Operatoren zur&uuml;ck, die der Handler verarbeiten
	 * kann
	 *
	 * @return Liste von Operatoren
	 */
	public Operator[] getHandledOperators();

}
