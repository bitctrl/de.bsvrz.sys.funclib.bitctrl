/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
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
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.NonMutableSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;sentiert eine Umfelddatenmessstelle.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UmfelddatenMessstelle extends AbstractSystemObjekt {

	/** Liste der Umfelddatensensoren dieser Umfelddatenmessstelle. */
	private final List<UmfelddatenSensor> umfelddatensensoren;

	/**
	 * Erzeugt eine Umfelddatenmessstelle aus einem Systemobjekt.
	 * 
	 * @param so
	 *            Ein Systemobjekt, welches eine Umfelddatenmessstelle sein muss
	 * @throws IllegalArgumentException
	 */
	public UmfelddatenMessstelle(SystemObject so) {
		super(so);

		ConfigurationObject co;
		NonMutableSet menge;

		umfelddatensensoren = new ArrayList<UmfelddatenSensor>();

		co = (ConfigurationObject) so;
		menge = co.getNonMutableSet("UmfeldDatenSensoren");
		for (SystemObject obj : menge.getElements()) {
			umfelddatensensoren.add((UmfelddatenSensor) ObjektFactory
					.getModellobjekt(obj));
		}
	}

	/**
	 * Pr&uuml;ft ob ein bestimmter Umfelddatensensor zur Umfelddatenmessstelle
	 * geh&ouml;rt.
	 * 
	 * @param uds
	 *            Ein Umfelddatensensor
	 * @return {@code true}, wenn der Umfelddatensensor zur
	 *         Umfelddatenmessstelle geh&ouml;rt
	 */
	public boolean besitzt(UmfelddatenSensor uds) {
		return umfelddatensensoren.contains(uds);
	}

	/**
	 * Gibt einen Iterator der Umfelddatensensoren dieser Umfelddatenmessstelle
	 * zur&uuml;ck.
	 * 
	 * @return Ein Umfelddatensensoriterator
	 */
	public List<UmfelddatenSensor> getUmfelddatensensoren() {
		return new ArrayList<UmfelddatenSensor>(umfelddatensensoren);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return UmfelddatenModelTypen.UMFELDDATENMESSSTELLE;
	}

}
