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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueState;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definitionen für das Attribut Vorhersagecode innerhalb einer RDS-Meldung.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public final class RdsVorhersageCode implements Zustand<Integer> {

	/** Name des Attributtyps der den Vorhersagecode definiert. */
	private static final String ATT_RDS_VORHERSAGE_CODE = "att.rdsVorhersageCode";

	/** der Attributtyp der den Vorhersagecode definiert. */
	private static IntegerAttributeType attribut = null;

	/** der Menge der Locationtabellen geordnet nach dem Code. */
	private static final Map<Integer, RdsVorhersageCode> TABELLE_PER_CODE = new HashMap<Integer, RdsVorhersageCode>();

	/** der Menge der Locationtabellen geordnet nach dem Name. */
	private static final Map<String, RdsVorhersageCode> TABELLE_PER_NAME = new HashMap<String, RdsVorhersageCode>();

	/**
	 * liefert den Vorhersagecode mit dem übergebenen Code.
	 * 
	 * @param gesuchterCode
	 *            der Code für den eine Vorhersagecode gesucht wird.
	 * @return den ermittelten Vorhersagecode, wenn ein ungültiger Code
	 *         übergeben wurde, wird eine {@link IllegalArgumentException}
	 *         geworfen
	 */
	public static RdsVorhersageCode getVorhersagecode(final int gesuchterCode) {

		RdsVorhersageCode result = TABELLE_PER_CODE.get(gesuchterCode);

		if (result == null) {
			if (attribut == null) {
				attribut = (IntegerAttributeType) ObjektFactory.getInstanz()
						.getVerbindung().getDataModel().getAttributeType(
								ATT_RDS_VORHERSAGE_CODE);
			}

			if (attribut != null) {
				for (IntegerValueState state : attribut.getStates()) {
					if (state.getValue() == gesuchterCode) {
						result = new RdsVorhersageCode(state.getName(),
								(int) state.getValue());
						TABELLE_PER_CODE.put((int) state.getValue(), result);
						TABELLE_PER_NAME.put(state.getName(), result);
						break;
					}
				}
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("Für den Code \""
					+ gesuchterCode
					+ "\" ist kein Vorhersagecode im Datenkatalog definiert");
		}

		return result;
	}

	/**
	 * liefert die Vorhersagecode mit dem übergebenen Name.
	 * 
	 * @param gesuchterName
	 *            der Name für den ein Vorhersagecode gesucht wird.
	 * @return der ermittelte Vorhersagecode, wenn ein ungültiger Name übergeben
	 *         wurde, wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static RdsVorhersageCode getVorhersagecode(final String gesuchterName) {

		RdsVorhersageCode result = TABELLE_PER_NAME.get(gesuchterName);

		if (result == null) {
			if (attribut == null) {
				attribut = (IntegerAttributeType) ObjektFactory.getInstanz()
						.getVerbindung().getDataModel().getAttributeType(
								ATT_RDS_VORHERSAGE_CODE);
			}

			if (attribut != null) {
				for (IntegerValueState state : attribut.getStates()) {
					if (state.getName().equals(gesuchterName)) {
						result = new RdsVorhersageCode(state.getName(),
								(int) state.getValue());
						TABELLE_PER_CODE.put((int) state.getValue(), result);
						TABELLE_PER_NAME.put(state.getName(), result);
						break;
					}
				}
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("Für den Name \""
					+ gesuchterName
					+ "\" ist kein Vorhersagecode im Datenkatalog definiert");
		}

		return result;
	}

	/**
	 * der Code des Zustandes.
	 */
	private int code;

	/**
	 * der Name des Zustandes.
	 */
	private String name;

	/**
	 * Konstruktor.<br>
	 * Die Funktion einen eine neue Instanz für einen Vorhersagecode mit dem
	 * übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor wird
	 * nur innerhalb der Klasse verwendet. Es wird eine Menge vordefinierter
	 * Zustände zur Verfügung gestellt.
	 * 
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsVorhersageCode(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 * 
	 * @return den Code.
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * liefert den Namen des Zustandes.
	 * 
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

}
