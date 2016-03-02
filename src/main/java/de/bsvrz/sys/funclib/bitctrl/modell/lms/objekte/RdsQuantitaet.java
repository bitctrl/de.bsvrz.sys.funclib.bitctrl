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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.LmsModellTypen;

/**
 * Repr‰sentation einer Quantit‰t innerhalb einer RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class RdsQuantitaet extends AbstractSystemObjekt {

	/** die Kennung. */
	private String kennung;

	/** die Beschreibung. */
	private String beschreibung;

	/** die Einheit. */
	private String einheit;

	/**
	 * Konstruktor.
	 *
	 * @param obj
	 *            das Systemobjekt, mit dem die Quantit‰t in der
	 *            Datenverteiler-Konfiguration repr‰sentiert ist
	 */
	public RdsQuantitaet(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine RdsMeldung.");
		}

		final Data daten = obj.getConfigurationData(
				obj.getDataModel().getAttributeGroup("atg.rdsQuantit‰t"));
		if (daten != null) {
			kennung = daten.getTextValue("Kennung").getText();
			beschreibung = daten.getTextValue("Beschreibung").getText();
			einheit = daten.getTextValue("Einheit").getText();
		}
	}

	/**
	 * liefert den Beschreibungstext. Wenn keiner definiert wurde, wird der Wert
	 * <code>null</code> geliefert.
	 *
	 * @return den Text oder <code>null</code>
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * liefert die Einheit. Wenn keine definiert wurde, wird der Wert
	 * <code>null</code> geliefert.
	 *
	 * @return den Text oder <code>null</code>
	 */
	public String getEinheit() {
		return einheit;
	}

	/**
	 * liefert die Kennung. Wenn keine definiert wurde, wird der Wert
	 * <code>null</code> geliefert.
	 *
	 * @return den Text oder <code>null</code>
	 */
	public String getKennung() {
		return kennung;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return LmsModellTypen.RDSQUANTITAET;
	}
}
