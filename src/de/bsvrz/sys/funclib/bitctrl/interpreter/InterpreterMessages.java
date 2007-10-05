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

import java.util.ResourceBundle;

import de.bsvrz.sys.funclib.bitctrl.i18n.MessageHandler;

/**
 * Versorgt das Package de.bwl.rpt.ref95.common.interpreter, samt Subpackages,
 * mit lokalisierten Meldungen
 *
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public enum InterpreterMessages implements MessageHandler {

	/**
	 * Der Nichtwert bzw. "undefiniert"
	 */
	Undefined,

	/**
	 * Wert ist nicht vom Typ boolean
	 */
	NoBooleanValue,

	/**
	 * Zugeh&ouml;rigkeit muss zwischen 0 und 1 liegen
	 */
	BadMembership,

	/**
	 * Kein Handler f&uuml;r Operator gefunden, Argumente: Operator
	 */
	HandlerNotFound,

	/**
	 * Variablenname ist ungültig
	 */
	BadVariableName,

	/**
	 * Variable mit Name/Typ nicht im Kontext gefunden, Argumente:
	 * Variablenname, Typname
	 */
	NoVariableWithNameAndTyp,

	/**
	 * Variable mit Name nicht im Kontext gefunden, Argumente: Variablenname
	 */
	NoVariableWithName,

	/**
	 * {@code null} als Variablenwert ist unzulässig
	 */
	// TODO Wert fehlt im Properties-File
	BadValueNull,

	/**
	 * Operationssymbol muss lesbar sein (nicht null usw.)
	 */
	BadSymbol,

	/**
	 * {@code null} als Handler ist nicht zugelassen
	 */
	BadHandlerNull,

	/**
	 * Die Angabe eines Typs darf nicht null sein
	 */
	BadTypNull;

	/**
	 * Name des Bundles
	 */
	private static final String BUNDLE_NAME = InterpreterMessages.class
			.getCanonicalName();

	/**
	 * Das Resourcenbundle
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * {@inheritDoc}
	 */
	public ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

}
