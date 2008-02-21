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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.interpreter;

import static com.bitctrl.text.CheckText.isPrintable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.bitctrl.i18n.Messages;

/**
 * Implementiert jedes Operatorsymbol als Entwurfsmuster Singleton. Jedes
 * Operatorsymbol kann abh&auml;ngig vom Kontext im Ausdruck eine andere
 * Operation darstellen. Deshalb f&uuml;hrt jeder Operator eine Liste von
 * {@link Handler} die ihn behandeln können.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public final class Operator {
	/**
	 * Statische Menge aller Operatoren.
	 */
	private static HashMap<String, Operator> operatorMenge = new HashMap<String, Operator>();

	/**
	 * ermiitelt, ob in der Menge der Operatoren ein Operator mit dem gegebenen
	 * Namen existiert..
	 * 
	 * @param symbol
	 *            Operationsymbol
	 * @return true, wenn der Operator existiert
	 */
	public static boolean enthaelt(String symbol) {
		String sym = symbol.trim();

		return operatorMenge.containsKey(sym);
	}

	/**
	 * Gibt den Operator zu einem Symbol zur&uuml;ck. Der Operator wird neu
	 * erzeugt, wenn das Symbol noch unbekannt ist.
	 * 
	 * @param symbol
	 *            Operationsymbol
	 * @return Operator
	 */
	public static Operator getOperator(String symbol) {
		if (!isPrintable(symbol)) {
			throw new InterpreterException(Messages
					.get(InterpreterMessages.BadSymbol));
		}

		String sym = symbol.trim();

		if (operatorMenge.containsKey(sym)) {
			return operatorMenge.get(sym);
		}

		return new Operator(sym);
	}

	/**
	 * Registriert einen Handler. Der Handler wird in die jeweiligen Listen der
	 * von ihm unterst&uuml;tzten Operatoren eingetragen.
	 * 
	 * @param handler
	 *            Handler
	 */
	public static void registerHandler(Handler handler) {
		if (handler == null) {
			throw new InterpreterException(Messages
					.get(InterpreterMessages.BadHandlerNull));
		}

		for (Operator o : handler.getHandledOperators()) {
			o.addHandler(handler);
		}
	}

	/**
	 * Das Symbol des Operators.
	 */
	private final String symbol;

	/**
	 * Menge aller Handler dieses Operators.
	 */
	private List<Handler> handler = new ArrayList<Handler>();

	/**
	 * Konstruktor verstecken.
	 * 
	 * @param symbol
	 *            Die Zeichenkette, die das Operatorsymbol darstellt
	 */
	private Operator(String symbol) {
		this.symbol = symbol;
		operatorMenge.put(symbol, this);
	}

	/**
	 * Liste der Handler dieses Operators erg&auml;nzen.
	 * 
	 * @param h
	 *            Ein neuer Handler
	 */
	private void addHandler(Handler h) {
		assert h != null;
		Handler replace = null;
		for (Handler item : handler) {
			if ((h.getClass().equals(item.getClass()))
					|| (h.getClass().isInstance(item))) {
				return;
			}
			if (item.getClass().isInstance(h)) {
				replace = item;
				break;
			}
		}
		if (replace != null) {
			handler.remove(replace);
		}
		handler.add(h);
	}

	/**
	 * {@inheritDoc}.<br>
	 * Zwei Operatoren sind gleich, wenn sie das selbe Symbol darstellen.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Operator) {
			Operator op = (Operator) obj;

			if (this.getSymbol() == op.getSymbol()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Wendet den Operator auf die Menge der Operanden an. Die Operanden werden
	 * von links nach rechts bzw. in der Reihenfolge der Iteration abgearbeitet.
	 * Wenn kein passender Handler gefunden wurde, wird eine
	 * {@link InterpreterException} geworfen.
	 * 
	 * @param werte
	 *            Menge von Operanden
	 * @return Ergebnis der Operation
	 */
	public Object execute(List<Object> werte) {
		if (handler.size() == 0) {
			throw new InterpreterException(Messages.get(
					InterpreterMessages.HandlerNotFound, getSymbol()));
		}

		for (Handler h : handler) {
			if (h.validiereHandler(this, werte).isValid()) {
				return h.perform(this, werte);
			}
		}

		throw new HandlerNotFoundException(Messages.get(
				InterpreterMessages.HandlerNotFound, getAufrufString(werte)));
	}

	/**
	 * Wendet den Operator auf die Liste der Operanden an. Die Operanden werden
	 * von links nach rechts bzw. in der Reihenfolge der Iteration abgearbeitet.
	 * Wenn kein passender Handler für die Operation gefunden wird, wird eine
	 * {@link InterpreterException} geworfen.
	 * 
	 * @param werte
	 *            Menge von Operanden
	 * @return Ergebnis der Operation
	 */
	public Object execute(Object... werte) {
		return execute(Arrays.asList(werte));
	}

	/**
	 * Liefert den Name des Symbols mit den Typen der Operanden.
	 * 
	 * @param werte
	 *            Die Operanden
	 * @return Den Text
	 */
	@SuppressWarnings("nls")
	private String getAufrufString(List<Object> werte) {
		StringBuffer meldung = new StringBuffer("(");
		for (Object wert : werte) {
			if (meldung.length() > 1) {
				meldung.append(',');
			}
			if (wert != null) {
				meldung.append(wert.getClass().getSimpleName());
			} else {
				meldung.append(wert);
			}
		}
		meldung.append(')');
		meldung.insert(0, getSymbol());

		return meldung.toString();
	}

	/**
	 * Gibt das Symbol des Operators zur&uuml;ck.
	 * 
	 * @return Operatorsymbol
	 */
	public String getSymbol() {
		assert symbol != null;

		return symbol;
	}

	/**
	 * {@inheritDoc}.<br>
	 * Gibt das Symbol des Operators zur&uuml;ck
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		assert symbol != null;

		return getSymbol();
	}

}
