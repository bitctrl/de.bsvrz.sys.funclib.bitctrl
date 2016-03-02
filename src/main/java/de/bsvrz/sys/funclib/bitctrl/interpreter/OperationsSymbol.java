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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Ein Nicht-Terminal-Symbol des Interpreters, sprich ein Operatorsymbol bzw
 * eine Operation.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class OperationsSymbol implements Ausdruck {

	/**
	 * Der Operator dieser Operation.
	 */
	private final Operator operator;

	/**
	 * Die Liste der Operanden.
	 */
	private final List<Ausdruck> operanden;

	/**
	 * Konstruiert ein Symbol mit den gegebenen Operator und Operanden.
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Liste mit Operanden
	 * @throws NullPointerException
	 *             Wenn ein Funktionsparameter {@code null} ist
	 */
	public OperationsSymbol(final Operator operator,
			final Ausdruck... operanden) {
		if ((operator == null) || (operanden == null)) {
			throw new NullPointerException();
		}

		this.operator = operator;
		this.operanden = new ArrayList<Ausdruck>();
		if (operanden.length > 0) {
			this.operanden.addAll(Arrays.asList(operanden));
		}
	}

	/**
	 * Konstruiert ein Symbol mit den gegebenen Operator und Operanden.
	 *
	 * @param operator
	 *            Operator
	 * @param operanden
	 *            Liste mit Operanden
	 * @throws NullPointerException
	 *             Wenn ein Funktionsparameter {@code null} ist
	 */
	public OperationsSymbol(final Operator operator,
			final List<? extends Ausdruck> operanden) {
		this(operator, operanden.toArray(new Ausdruck[0]));
	}

	/**
	 * Konstruiert ein Symbol mit den gegebenen Operatorsymbol und Operanden.
	 *
	 * @param operatorSymbol
	 *            Operator
	 * @param operanden
	 *            Liste mit Operanden
	 * @throws NullPointerException
	 *             Wenn ein Funktionsparameter {@code null} ist
	 */
	public OperationsSymbol(final String operatorSymbol,
			final Ausdruck... operanden) {
		this(Operator.getOperator(operatorSymbol), operanden);
	}

	/**
	 * Konstruiert ein Symbol mit den gegebenen Operatorsymbol und Operanden.
	 *
	 * @param operatorSymbol
	 *            Operator
	 * @param operanden
	 *            Liste mit Operanden
	 * @throws NullPointerException
	 *             Wenn ein Funktionsparameter {@code null} ist
	 */
	public OperationsSymbol(final String operatorSymbol,
			final List<? extends Ausdruck> operanden) {
		this(operatorSymbol, operanden.toArray(new Ausdruck[0]));
	}

	@Override
	public List<Ausdruck> getNachfolger() {
		return new ArrayList<Ausdruck>(operanden);
	}

	/**
	 * Gibt die Operanden der Operation zur&uuml;ck.
	 *
	 * @return Die Liste der Operanden
	 */
	public List<? extends Ausdruck> getOperanden() {
		return operanden;
	}

	/**
	 * Gibt den Operator dieser Operation zur&uuml;ck.
	 *
	 * @return Operator
	 */
	public Operator getOperator() {
		assert operator != null;

		return operator;
	}

	@Override
	public Object interpret(final Kontext kontext) {
		assert operator != null;

		final List<Object> values = new ArrayList<Object>();
		for (final Ausdruck a : operanden) {
			if (a != null) {
				values.add(a.interpret(kontext));
			} else {
				values.add(null);
			}
		}

		return operator.execute(values);
	}

	@Override
	public String toString() {
		assert operator != null;

		String result = "(";

		if (operanden.size() == 1) {
			// Es gibt nur einen Operanden
			result += operator + " " + operanden.get(0);
		} else {
			// Es gibt mehr als einen Operanden
			final Iterator<Ausdruck> i = operanden.iterator();
			Ausdruck a = i.next();

			result += a.toString(); // erster Operand
			while (i.hasNext()) {
				a = i.next();
				result += " " + operator + " " + a; // restliche Operanden
			}
		}
		result += ")";

		return result;
	}

}
