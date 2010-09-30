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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;

/**
 * Über diese Klasse werden alle im DAV-Enumerationstyp <code>att.sweTyp</code>
 * beschriebenen Werte zur Verfügung gestellt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: SWETyp.java 8054 2008-04-09 15:11:59Z tfelder $
 */
public final class SWETyp extends AbstractDavZustand {

	/**
	 * Der Wertebereich dieses DAV-Enumerationstypen.
	 */
	private static Map<Integer, SWETyp> werteBereich = new HashMap<Integer, SWETyp>();

	/**
	 * Wert <code>SWE_PL_Prüfung_formal</code>.
	 */
	public static final SWETyp PL_PRUEFUNG_FORMAL = new SWETyp(
			"SWE_PL_Prüfung_formal", 1); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_PL_Prüfung_logisch_LVE</code>.
	 */
	public static final SWETyp PL_PRUEFUNG_LOGISCH_LVE = new SWETyp(
			"SWE_PL_Prüfung_logisch_LVE", 2); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_PL_Prüfung_logisch_UFD</code>.
	 */
	public static final SWETyp SWE_PL_PRUEFUNG_LOGISCH_UFD = new SWETyp(
			"SWE_PL_Prüfung_logisch_UFD", 3); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_PL_Prüfung_logisch_WZG</code>.
	 */
	public static final SWETyp SWE_PL_PRUEFUNG_LOGISCH_WZG = new SWETyp(
			"SWE_PL_Prüfung_logisch_WZG", 4); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Messwertersetzung_LVE</code>.
	 */
	public static final SWETyp SWE_MESSWERTERSETZUNG_LVE = new SWETyp(
			"SWE_Messwertersetzung_LVE", 5); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Abfrage_Pufferdaten</code>.
	 */
	public static final SWETyp SWE_ABFRAGE_PUFFERDATEN = new SWETyp(
			"SWE_Abfrage_Pufferdaten", 6); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Datenaufbereitung_LVE</code>.
	 */
	public static final SWETyp SWE_DATENAUFBEREITUNG_LVE = new SWETyp(
			"SWE_Datenaufbereitung_LVE", 7); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Datenaufbereitung_UFD</code>.
	 */
	public static final SWETyp SWE_DATENAUFBEREITUNG_UFD = new SWETyp(
			"SWE_Datenaufbereitung_UFD", 8); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Aggregation_LVE</code>.
	 */
	public static final SWETyp SWE_AGGREGATION_LVE = new SWETyp(
			"SWE_Aggregation_LVE", 9); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Ergänzung_BASt</code>.
	 */
	public static final SWETyp SWE_ERGAENZUNG_BAST = new SWETyp(
			"SWE_Ergänzung_BASt", 10); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Güteberechnung</code>.
	 */
	public static final SWETyp SWE_GUETEBERECHNUNG = new SWETyp(
			"SWE_Güteberechnung", 11); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_Messwertersetzung_UFD</code>.
	 */
	public static final SWETyp SWE_MESSWERTERSETZUNG_UFD = new SWETyp(
			"SWE_Messwertersetzung_UFD", 12); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_PL_Prüfung_Langzeit_UFD</code>.
	 */
	public static final SWETyp SWE_PL_PRUEFUNG_LANGZEIT_UFD = new SWETyp(
			"SWE_PL_Prüfung_Langzeit_UFD", 13); //$NON-NLS-1$

	/**
	 * Wert <code>SWE_DuA_Glättewarnung_und_Prognose</code>.
	 */
	public static final SWETyp SWE_GLAETTEWARNUNG_UND_PROGNOSE = new SWETyp(
			"SWE_DuA_Glättewarnung_und_Prognose", 14); //$NON-NLS-1$

	/**
	 * Interner Konstruktor.
	 * 
	 * @param name
	 *            der Name des Zustandes
	 * @param code
	 *            der Kode
	 */
	private SWETyp(String name, int code) {
		super(code, name);
		werteBereich.put(code, this);
	}

	/**
	 * Erfragt den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 * 
	 * @param code
	 *            der Code des Enumerations-Wertes
	 * @return den Wert dieses DAV-Enumerationstypen mit dem übergebenen Code.
	 */
	public static SWETyp getZustand(int code) {
		return werteBereich.get(code);
	}

}
