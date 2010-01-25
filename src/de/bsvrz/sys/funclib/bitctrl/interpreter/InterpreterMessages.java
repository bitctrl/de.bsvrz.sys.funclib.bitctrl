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

import java.util.ResourceBundle;

import com.bitctrl.i18n.MessageHandler;

/**
 * Versorgt das Package de.bwl.rpt.ref95.common.interpreter, samt Subpackages,
 * mit lokalisierten Meldungen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: InterpreterMessages.java 6716 2008-02-18 17:32:51Z Schumann $
 */
public enum InterpreterMessages implements MessageHandler {

	/** Keine Parameter. */
	Undefined,

	/** Keine Parameter. */
	NoBooleanValue,

	/** Parameter: 1) Zugehörigkeit. */
	BadMembership,

	/** Parameter: 1) Operator. */
	HandlerNotFound,

	/** Parameter: 1) Variablenname. */
	BadVariableName,

	/** Parameter: 1) Variablenname, 2) Variablentyp. */
	NoVariableWithNameAndTyp,

	/** Parameter: 1) Variablenname. */
	NoVariableWithName,

	/** Keine Parameter. */
	BadValueNull,

	/** Keine Parameter. */
	BadSymbol,

	/** Keine Parameter. */
	BadHandlerNull,

	/** Keine Parameter. */
	BadTypNull;

	/** Die Eigenschaft {@code BUNDLE_NAME}. */
	private static final String BUNDLE_NAME = InterpreterMessages.class
			.getCanonicalName();

	/** Die Eigenschaft {@code RESOURCE_BUNDLE}. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * {@inheritDoc}
	 */
	public ResourceBundle getResourceBundle() {
		return RESOURCE_BUNDLE;
	}

	/**
	 * Gibt den Text der Nachricht zurück.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getResourceBundle().getString(name());
	}

}
