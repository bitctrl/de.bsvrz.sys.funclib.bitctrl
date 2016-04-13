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

package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.util.List;
import java.util.TimerTask;

import de.bsvrz.sys.funclib.bitctrl.modell.OnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class Zufallsdaten extends TimerTask {

	/** Die Eigenschaft {@code objekte}. */
	private final List<SystemObjekt> objekte;

	/** Die Eigenschaft {@code generator}. */
	private final TestDatenGenerator generator;

	/** Die Eigenschaft {@code datensatzTyp}. */
	private final Class<OnlineDatensatz> datensatzTyp;

	/**
	 * Initialisiert das Objekt.
	 *
	 * @param objekte
	 *            die Liste der Objekte für die Daten generiert werden sollen.
	 * @param generator
	 *            der Generator der Zufallsdaten.
	 */
	public Zufallsdaten(final List<SystemObjekt> objekte,
			final TestDatenGenerator generator,
			final Class<OnlineDatensatz> datensatzTyp) {
		this.objekte = objekte;
		this.generator = generator;
		this.datensatzTyp = datensatzTyp;
	}

	@Override
	public void run() {
		for (final SystemObjekt obj : objekte) {
			final OnlineDatensatz datensatz;

			datensatz = obj.getOnlineDatensatz(datensatzTyp);
		}
	}
}
