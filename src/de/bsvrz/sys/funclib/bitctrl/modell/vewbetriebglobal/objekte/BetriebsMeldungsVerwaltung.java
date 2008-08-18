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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.VeWBetriebGlobalTypen;

/**
 * Kapselt ein Systemobjekt, welches eine Betriebsmeldungsverwaltung darstellt.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class BetriebsMeldungsVerwaltung extends AbstractSystemObjekt {

	/**
	 * Erzeugt eine Betriebsmeldungsverwaltung aus einem Systemobjekt.
	 * 
	 * @param objekt
	 *            Ein Systemobjekt, welches eine Betriebsmeldungsverwaltung sein
	 *            muss
	 */
	public BetriebsMeldungsVerwaltung(final SystemObject objekt) {
		super(objekt);

		if (!objekt.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine Betriebsmeldungsverwaltung.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return VeWBetriebGlobalTypen.BetriebsMeldungsVerwaltung;
	}

}
