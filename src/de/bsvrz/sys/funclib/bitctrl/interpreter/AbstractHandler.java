/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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

import java.util.Arrays;
import java.util.List;

/**
 * Abstrakte Implementierung eines Handlers zur Ausf&uuml;hrung von Operationen.
 * Die Klasse implementiert einige Funktionen der Schnittstelle Handler um die
 * Implementierung konkreter Handler-Klassen zu erleichtern.
 *
 * @author BitCtrl Systems GmbH, Peuker, Schumann
 * @version $Id: AbstractHandler.java 6835 2008-02-21 13:04:58Z peuker $
 */
public abstract class AbstractHandler implements Handler {

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
	public static Object getOperand(final List<? extends Object> operanden,
			final int index) {
		Object ergebnis = null;

		if ((operanden != null) && (operanden.size() > index)) {
			ergebnis = operanden.get(index);
		}
		return ergebnis;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object perform(final Operator operator, final Object... operanden) {
		return perform(operator, Arrays.asList(operanden));
	}

	/**
	 * {@inheritDoc}
	 */
	public HandlerValidation validiereHandler(final Operator operator,
			final Object... operanden) {
		return validiereHandler(operator, Arrays.asList(operanden));
	}
}
