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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueState;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Definitionen für das Attribut EmpfehlungsCoe innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public final class RdsEmpfehlungsCode implements Zustand<Integer> {

	/** Name des Attributtyps der den Empfehlungscode definiert. */
	private static final String ATT_RDS_EMPEHLUNGS_CODE = "att.rdsEmpfehlungsCode";

	/** der Attributtyp der den Empfehlungscode definiert. */
	private static IntegerAttributeType attribut;

	/** der Menge der Locationtabellen geordnet nach dem Code. */
	private static final Map<Integer, RdsEmpfehlungsCode> TABELLE_PER_CODE = new HashMap<Integer, RdsEmpfehlungsCode>();

	/** der Menge der Locationtabellen geordnet nach dem Name. */
	private static final Map<String, RdsEmpfehlungsCode> TABELLE_PER_NAME = new HashMap<String, RdsEmpfehlungsCode>();

	/**
	 * liefert den Empfehlungscode mit dem übergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code für den eine Empfehlungscode gesucht wird.
	 * @return der ermittelte Empfehlungscode, wenn ein ungültiger Code
	 *         übergeben wurde, wird eine {@link IllegalArgumentException}
	 *         geworfen
	 */
	public static RdsEmpfehlungsCode getEmpfehlungsCode(
			final int gesuchterCode) {

		RdsEmpfehlungsCode result = TABELLE_PER_CODE.get(gesuchterCode);

		if (result == null) {
			if (attribut == null) {
				attribut = (IntegerAttributeType) ObjektFactory.getInstanz()
						.getVerbindung().getDataModel()
						.getAttributeType(ATT_RDS_EMPEHLUNGS_CODE);
			}

			if (attribut != null) {
				for (final IntegerValueState state : attribut.getStates()) {
					if (state.getValue() == gesuchterCode) {
						result = new RdsEmpfehlungsCode(state.getName(),
								(int) state.getValue());
						TABELLE_PER_CODE.put((int) state.getValue(), result);
						TABELLE_PER_NAME.put(state.getName(), result);
						break;
					}
				}
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("Für den Code \"" + gesuchterCode
					+ "\" ist kein Empfehlungscode im Datenkatalog definiert");
		}

		return result;
	}

	/**
	 * liefert den Empfehlungscode mit dem übergebenen Name.
	 *
	 * @param gesuchterName
	 *            der Name für den ein Empfehlungscode gesucht wird.
	 * @return der ermittelte Empfehlungscode, wenn ein ungültiger Name
	 *         übergeben wurde, wird eine {@link IllegalArgumentException}
	 *         geworfen.
	 */
	public static RdsEmpfehlungsCode getEmpfehlungsCode(
			final String gesuchterName) {

		RdsEmpfehlungsCode result = TABELLE_PER_NAME.get(gesuchterName);

		if (result == null) {
			if (attribut == null) {
				attribut = (IntegerAttributeType) ObjektFactory.getInstanz()
						.getVerbindung().getDataModel()
						.getAttributeType(ATT_RDS_EMPEHLUNGS_CODE);
			}

			if (attribut != null) {
				for (final IntegerValueState state : attribut.getStates()) {
					if (state.getName().equals(gesuchterName)) {
						result = new RdsEmpfehlungsCode(state.getName(),
								(int) state.getValue());
						TABELLE_PER_CODE.put((int) state.getValue(), result);
						TABELLE_PER_NAME.put(state.getName(), result);
						break;
					}
				}
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("Für den Name \"" + gesuchterName
					+ "\" ist kein Empfehlungscode im Datenkatalog definiert");
		}

		return result;
	}

	/**
	 * der Code des Zustandes.
	 */
	private final int code;

	/**
	 * der Name des Zustandes.
	 */
	private final String name;

	/**
	 * Konstruktor.<br>
	 * Die Funktion einen eine neue Instanz für eine RDS-Nachrichtensprache mit
	 * dem übergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zustände zur Verfügung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsEmpfehlungsCode(final String name, final int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 *
	 * @return den Code.
	 */
	@Override
	public Integer getCode() {
		return code;
	}

	/**
	 * liefert den Namen des Zustandes.
	 *
	 * @return der Name
	 */
	@Override
	public String getName() {
		return name;
	}

}
