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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs;

import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;

/**
 * Konstanten, die für die Datenflusssteuerung benötigt werden.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public final class DFSKonstanten {

	/**
	 * Pid <code>typ.datenflussSteuerung</code>.
	 */
	public static final String TYP = "typ.datenflussSteuerung"; //$NON-NLS-1$

	/**
	 * Pid <code>atg.datenflussSteuerung</code>.
	 */
	public static final String ATG = "atg.datenflussSteuerung"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>ParameterSatz</code>.
	 */
	public static final String ATL_PARA_SATZ = "ParameterSatz"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>Objekt</code>.
	 */
	public static final String ATT_OBJ = "Objekt"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>AttributGruppe</code>.
	 */
	public static final String ATT_ATG = "AttributGruppe"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>PublikationsAspekt</code>.
	 */
	public static final String ATT_ASP = "PublikationsAspekt"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>SWE</code>.
	 */
	public static final String ATT_SWE = "SWE"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>ModulTyp</code>.
	 */
	public static final String ATT_MODUL_TYP = "ModulTyp"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>PublikationsZuordnung</code>.
	 */
	public static final String ATT_PUB_ZUORDNUNG = "PublikationsZuordnung"; //$NON-NLS-1$

	/**
	 * Attribut-Name <code>Publizieren</code>.
	 */
	public static final String ATT_PUBLIZIEREN = "Publizieren"; //$NON-NLS-1$

	/**
	 * Standarddatenflusssteuerung.
	 */
	public static final IDatenFlussSteuerungFuerModul STANDARD = new DatenFlussSteuerungFuerModul();

	/**
	 * Standardkonstruktor.
	 */
	private DFSKonstanten() {

	}

}
