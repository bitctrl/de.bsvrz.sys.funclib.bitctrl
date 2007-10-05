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

import de.bsvrz.sys.funclib.bitctrl.i18n.Messages;

/**
 * Die Klasse repräsentiert den Nichtwert "undefiniert", den jedes Element in
 * Uda annehmen kann. Ein Nichtwert hat keinen Typ. Er kann jeder Variable
 * zugewiesen werden bzw. jeder Variable, die den Nichtwert hat kann ein
 * beliebiges Datenobjekt zugewiesen werden.
 * 
 * @author Peuker
 * @version $Id$
 */
public final class NichtWert {

	/**
	 * die statische Instanz des Wertes.
	 */
	public final static NichtWert NICHTWERT = new NichtWert();

	/**
	 * privater Konstruktor, verhindert das Anlagen zusätzlicher
	 * Nichtwert-Instanzen.
	 */
	private NichtWert() {
		super();
	}

	/**
	 * liefert die Repräsentation des Nichtwertes als Zeichenkette.
	 * 
	 * {@inheritDoc}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (Messages.get(InterpreterMessages.Undefined));
	}
}
