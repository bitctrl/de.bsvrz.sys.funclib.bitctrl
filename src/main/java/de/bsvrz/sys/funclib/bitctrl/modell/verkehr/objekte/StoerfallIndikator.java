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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;

/**
 * Repr&auml;ssentiert einen St�rfallindikator innerhalb der
 * Modellobjektverwaltung.
 *
 * @author BitCtrl Systems GmbH, Schumann, Peuker
 */
public class StoerfallIndikator extends AbstractSystemObjekt {

	/**
	 * Attributname f�r die Speicherung des aktuellen St�rfallzustands des
	 * Indikators.
	 */
	public static final String ATT_NAME_SITUATION = "Situation"; //$NON-NLS-1$

	/**
	 * Erzeugt einen St�rfallindikator aus einem Systemobjekt.
	 *
	 * @param obj
	 *            Ein Systemobjekt, welches ein St�rfallIndikator sein muss
	 * @throws IllegalArgumentException
	 */
	public StoerfallIndikator(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein St�rfallindikator."); //$NON-NLS-1$
		}
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STOERFALLINDIKATOR;
	}
}