/**
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
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.konstante;

/**
 * @author peuker
 *
 */
/**
 * @author peuker
 *
 */
public final class Konstante {

	/**
	 *
	 */
	public static final long SEKUNDE_IN_MS = 1000L;
	
	/**
	 *
	 */
	public static final long MINUTE_IN_MS = 60L * 1000L;

	/**
	 *
	 */
	public static final long STUNDE_IN_MS = 60L * 60L * 1000L;
	
	/**
	 *
	 */
	public static final long TAG_24_IN_MS = 24L * 60L * 60L * 1000L;

	/**
	 *
	 */
	public static final long PROZENT_FAKTOR = 100L;
	
	/**
	 *
	 */
	public static final String LEERSTRING = ""; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String DAV_TYP_VERKEHRS_MODELL_NETZ = "typ.verkehrsModellNetz"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String DAV_ASP_PARAMETER_SOLL = "asp.parameterSoll"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String DAV_ASP_PARAMETER_VORGABE = "asp.parameterVorgabe"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String DAV_TYP_TYP = "typ.typ"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String DAV_TYP_ATG = "typ.attributgruppe"; //$NON-NLS-1$

	/**
	 *
	 */
	public static final String STRING_UNBEKANNT = "unbekannt"; //$NON-NLS-1$

	/**
	 * Systemabhängiges Zeichen für einen Zeilenumbruch.
	 */
	public static final String NEWLINE = System.getProperty("line.separator"); //$NON-NLS-1$

	/**
	 * Primzahlkonstante zur Berechnung von Hash-Werten.
	 */
	public static final int PRIME = 31;

}
