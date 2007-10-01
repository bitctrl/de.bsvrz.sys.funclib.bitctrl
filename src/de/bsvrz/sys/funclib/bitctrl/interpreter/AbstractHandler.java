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

import java.util.Arrays;
import java.util.List;

/**
 * Abstrakte Implementierung eines Handlers zur Ausf&uuml;hrung von Operationen.
 * Die Klasse implementiert einige Funktionen der Schnittstelle Handler um die
 * Implementierung konkreter Handler-Klassen zu erleichtern.
 * 
 * @author BitCtrl, Uwe
 * @author BitCtrl, Schumann
 * @version $Id: AbstractHandler.java 164 2007-02-26 10:36:54Z Schumann $
 */
public abstract class AbstractHandler implements Handler {

	/**
	 * {@inheritDoc}
	 */
	public HandlerValidation validiereHandler(Operator operator, Object... operanden) {
		return validiereHandler(operator, Arrays.asList(operanden));
	}

	/**
	 * {@inheritDoc}
	 */
	public Object perform(Operator operator, Object... operanden) {
		return perform(operator, Arrays.asList(operanden));
	}

	/**
	 * Liefert den n-ten Operanden aus einer Liste von Operanden.<br>
	 * Wenn die Liste weniger Operanden enth&auml;lt, wird {@code null}
	 * geliefert.
	 * 
	 * @param operanden
	 *            Die Liste der Operanden
	 * @param index
	 *            Der Index des gesuchten Operanden
	 * @return Den Operand oder {@code null}
	 */
	public static Object getOperand(List<? extends Object> operanden, int index) {
		Object ergebnis = null;
		
		if ((operanden != null) && (operanden.size() > index)) {
			ergebnis = operanden.get(index);
		}
		return ergebnis;
	}
}
