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

import java.util.List;

import com.bitctrl.i18n.Messages;

import de.bsvrz.sys.funclib.bitctrl.interpreter.AbstractHandler;
import de.bsvrz.sys.funclib.bitctrl.interpreter.HandlerValidation;
import de.bsvrz.sys.funclib.bitctrl.interpreter.InterpreterException;
import de.bsvrz.sys.funclib.bitctrl.interpreter.InterpreterMessages;
import de.bsvrz.sys.funclib.bitctrl.interpreter.Operator;

/**
 * Handler f&uuml;r (fuzzy-)logische Ausdr&uuml;cke. Abgebildet sind die
 * Basisoperatoren, alle anderen lassen auf diese zur&uuml;ckf&uuml;hren.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class LogikHandler extends AbstractHandler {

	/** Konjunktion bzw. logisches "und" */
	public static final Operator UND = Operator.getOperator("und");

	/** Disjunktion bzw. logisches "oder" */
	public static final Operator ODER = Operator.getOperator("oder");

	/** Negation bzw. logisches "nicht" */
	public static final Operator NICHT = Operator.getOperator("nicht");

	/** Logische Implikation. */
	public static final Operator IMPLIKATION = Operator.getOperator("->");

	/**
	 * Liste der vom Handler unterst&uuml;tzten Operatoren.
	 */
	private static Operator[] operatoren = { UND, ODER, NICHT, IMPLIKATION };

	@Override
	public Operator[] getHandledOperators() {
		return operatoren;
	}

	@Override
	public Object perform(final Operator operator, final List<Object> operanden) {
		if ((operator == null) || !validiereHandler(operator, operanden).isValid()) {
			throw new InterpreterException(Messages.get(InterpreterMessages.HandlerNotFound));
		}

		LogischerWert result = null;

		if (operator == UND) {
			result = minimum(operanden.toArray());
		} else if (operator == ODER) {
			result = maximum(operanden.toArray());
		} else if (operator == NICHT) {
			result = komplement(operanden.get(0));
		} else if (operator == IMPLIKATION) {
			result = implikation(operanden.toArray());
		}

		return result;
	}

	@Override
	public HandlerValidation validiereHandler(final Operator operator, final List<? extends Object> operanden) {
		assert operanden != null : "Liste der Operanden darf nicht null sein.";

		boolean anzahlOk = false;
		boolean typOk = true;

		// Anzahl der Operanden und Operation prüfen
		switch (operanden.size()) {
		case 0:
			anzahlOk = false;
			break;
		case 1:
			if (NICHT.equals(operator)) {
				anzahlOk = true;
			}
			break;
		case 2:
			if (IMPLIKATION.equals(operator)) {
				anzahlOk = true;
			}
			break;
		default:
		}

		if (UND.equals(operator) || ODER.equals(operator)) {
			if (operanden.size() >= 2) {
				anzahlOk = true;
			}
		}

		// Alle Operanden müssen ein logischer Wert sein
		for (final Object obj : operanden) {
			if (obj instanceof LogischerWert) {
				final LogischerWert lw = (LogischerWert) obj;
				if (!lw.isBoolWert()) {
					typOk = false;
					break;
				}
			} else {
				typOk = false;
				break;
			}
		}

		return new HandlerValidation(anzahlOk, typOk);
	}

	/**
	 * Bestimmt das Ergebnis der Implikation: nicht a oder b.
	 *
	 * @param operanden
	 *            Operandenliste mit genau zwei Operanden: a und b
	 * @return Logischer Wert mit berechneter Zugeh&ouml;rigkeit oder booleschen
	 *         Wert, wenn alle Operanden boolesche Werte haben
	 */
	protected LogischerWert implikation(final Object[] operanden) {
		assert operanden != null : "Argument operanden darf nicht null sein.";
		assert operanden.length == 2 : "Anzahl der Operanden muss gleich 2 sein.";
		assert operanden[0] instanceof LogischerWert : "Operanden müssen logische Werte sein.";
		assert operanden[1] instanceof LogischerWert : "Operanden müssen logische Werte sein.";

		final LogischerWert na = komplement(operanden[0]);
		final LogischerWert[] ab = { na, (LogischerWert) operanden[1] };
		return maximum(ab);
	}

	/**
	 * Berechnet das Komplement: 1 - a. Entspricht dem logischen "nicht".
	 *
	 * @param operand
	 *            Operand
	 * @return Logischer Wert mit berechneter Zugeh&ouml;rigkeit oder booleschen
	 *         Wert, wenn alle Operanden boolesche Werte haben
	 */
	protected LogischerWert komplement(final Object operand) {
		assert operand != null : "Argument operand darf nicht null sein.";
		assert operand instanceof LogischerWert : "Operand muss ein logische Werte sein.";

		final LogischerWert wert = (LogischerWert) operand;

		if (wert.isBoolWert()) {
			return new LogischerWert(!wert.getBoolWert());
		}

		if (wert.getZugehoerigkeit() == null) {
			return new LogischerWert(null);
		}

		final Double d = Math.abs(1.0 - wert.getZugehoerigkeit());
		return new LogischerWert(d.floatValue());
	}

	/**
	 * Bestimmt das Maximum: max(a, b, ...). Entspricht dem logischen "oder".
	 *
	 * @param operanden
	 *            Operandenliste mit mindestens einem Operanden
	 * @return Logischer Wert mit berechneter Zugeh&ouml;rigkeit oder booleschen
	 *         Wert, wenn alle Operanden boolesche Werte haben
	 */
	protected LogischerWert maximum(final Object[] operanden) {
		assert operanden != null : "Argument operanden darf nicht null sein.";
		assert operanden.length > 0 : "Anzahl der Operanden muss größer 0 sein.";

		Float wert = null;
		boolean boolWert = true;

		for (final Object obj : operanden) {
			assert obj instanceof LogischerWert : "Operanden müssen logische Werte sein.";

		LogischerWert operand;

		operand = (LogischerWert) obj;
		if (!operand.isBoolWert()) {
			boolWert = false;
			if (operand.getZugehoerigkeit() == null) {
				// Einer der Operanden ist null => Ergebnis = null
				return new LogischerWert(null);
			}
		}
		if (wert == null) {
			// Erster Operand
			wert = operand.getZugehoerigkeit();
		} else {
			if (operand.getZugehoerigkeit() > wert) {
				wert = operand.getZugehoerigkeit();
			}
		}
		}

		if (boolWert) {
			assert (wert == 1) || (wert == 0);

			if (wert == 1) {
				return new LogischerWert(true);
			}

			return new LogischerWert(false);
		}

		return new LogischerWert(wert);
	}

	/**
	 * Bestimmt das Minimum: min(a, b, ...). Entspricht dem logischen "und".
	 *
	 * @param operanden
	 *            Operandenliste mit mindestens einem Operanden
	 * @return Logischer Wert mit berechneter Zugeh&ouml;rigkeit oder booleschen
	 *         Wert, wenn alle Operanden boolesche Werte haben
	 */
	protected LogischerWert minimum(final Object[] operanden) {
		assert operanden != null : "Argument operanden darf nicht null sein.";
		assert operanden.length > 0 : "Anzahl der Operanden muss größer 0 sein.";

		Float wert = null;
		boolean boolWert = true;

		for (final Object obj : operanden) {
			assert obj instanceof LogischerWert : "Operanden müssen logische Werte sein.";

		LogischerWert operand;

		operand = (LogischerWert) obj;
		if (!operand.isBoolWert()) {
			boolWert = false;
			if (operand.getZugehoerigkeit() == null) {
				// Einer der Operanden ist null => Ergebnis = null
				return new LogischerWert(null);
			}
		}
		if (wert == null) {
			// Erster Operand
			wert = operand.getZugehoerigkeit();
		} else {
			if (operand.getZugehoerigkeit() == null) {
				wert = null;
				break;
			}

			if (operand.getZugehoerigkeit() < wert) {
				wert = operand.getZugehoerigkeit();
			}
		}
		}

		if (boolWert) {
			assert (wert == 1) || (wert == 0);

			if (wert == 1) {
				return new LogischerWert(true);
			}

			return new LogischerWert(false);
		}

		return new LogischerWert(wert);
	}

}
