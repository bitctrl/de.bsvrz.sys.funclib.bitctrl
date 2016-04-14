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
 * Wei�enfelser Stra�e 67
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
 * Definitionen f�r das Attribut Ereigniscode innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public final class RdsEreignisCode implements Zustand<Integer> {

	/** Name des Attributtyps der den Ereigniscode definiert. */
	private static final String ATT_RDS_EREIGNIS_CODE = "att.rdsEreignisCode";

	/** der Attributtyp der den Ereigniscode definiert. */
	private static IntegerAttributeType attribut;

	/** der Menge der Locationtabellen geordnet nach dem Code. */
	private static final Map<Integer, RdsEreignisCode> TABELLE_PER_CODE = new HashMap<>();

	/** der Menge der Locationtabellen geordnet nach dem Name. */
	private static final Map<String, RdsEreignisCode> TABELLE_PER_NAME = new HashMap<>();

	/**
	 * liefert den Ereigniscode mit dem �bergebenen Code.
	 *
	 * @param gesuchterCode
	 *            der Code f�r den eine Ereigniscode gesucht wird.
	 * @return der ermittelte Ereigniscode, wenn ein ung�ltiger Code �bergeben
	 *         wurde, wird eine {@link IllegalArgumentException} geworfen
	 */
	public static RdsEreignisCode getEreignisCode(final int gesuchterCode) {

		RdsEreignisCode result = TABELLE_PER_CODE.get(gesuchterCode);

		if (result == null) {
			if (attribut == null) {
				attribut = (IntegerAttributeType) ObjektFactory.getInstanz()
						.getVerbindung().getDataModel()
						.getAttributeType(ATT_RDS_EREIGNIS_CODE);
			}

			if (attribut != null) {
				for (final IntegerValueState state : attribut.getStates()) {
					if (state.getValue() == gesuchterCode) {
						result = new RdsEreignisCode(state.getName(),
								(int) state.getValue());
						TABELLE_PER_CODE.put((int) state.getValue(), result);
						TABELLE_PER_NAME.put(state.getName(), result);
						break;
					}
				}
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("F�r den Code \"" + gesuchterCode
					+ "\" ist kein Ereigniscode im Datenkatalog definiert");
		}

		return result;
	}

	/**
	 * liefert die Ereigniscode mit dem �bergebenen Name.
	 *
	 * @param gesuchterName
	 *            der Name f�r den ein Ereigniscode gesucht wird.
	 * @return der ermittelte Ereigniscode, wenn ein ung�ltiger Name �bergeben
	 *         wurde, wird eine {@link IllegalArgumentException} geworfen.
	 */
	public static RdsEreignisCode getEreignisCode(final String gesuchterName) {

		RdsEreignisCode result = TABELLE_PER_NAME.get(gesuchterName);

		if (result == null) {
			if (attribut == null) {
				attribut = (IntegerAttributeType) ObjektFactory.getInstanz()
						.getVerbindung().getDataModel()
						.getAttributeType(ATT_RDS_EREIGNIS_CODE);
			}

			if (attribut != null) {
				for (final IntegerValueState state : attribut.getStates()) {
					if (state.getName().equals(gesuchterName)) {
						result = new RdsEreignisCode(state.getName(),
								(int) state.getValue());
						TABELLE_PER_CODE.put((int) state.getValue(), result);
						TABELLE_PER_NAME.put(state.getName(), result);
						break;
					}
				}
			}
		}

		if (result == null) {
			throw new IllegalArgumentException("F�r den Name \"" + gesuchterName
					+ "\" ist kein Ereigniscode im Datenkatalog definiert");
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
	 * Die Funktion einen eine neue Instanz f�r eine RDS-Nachrichtensprache mit
	 * dem �bergebenem Code und der entsprechenden Bezeichnung. Der Konstruktor
	 * wird nur innerhalb der Klasse verwendet. Es wird eine Menge
	 * vordefinierter Zust�nde zur Verf�gung gestellt.
	 *
	 * @param name
	 *            der Name des zustands
	 * @param code
	 *            der verwendete Code
	 */
	private RdsEreignisCode(final String name, final int code) {
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
