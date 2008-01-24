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

package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Implementierung eines Fläschenobjekts ("typ.flächeXY"). Instanzen dieser
 * Klasse werden normalerweise nur über eine ObjektFactory angelegt und sollten
 * über das Interface {@link FlaecheXY} verwendet werden.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class FlaecheXYImpl extends AbstractSystemObjekt implements FlaecheXY {

	/**
	 * die Koordinaten der Fläche.
	 */
	private List<Punkt> koordinaten;

	/**
	 * Standardkonstruktor. Es wird ein Systemobjekt erzeugt, das in der
	 * Datenverteilerkonfiguration durch das übergebene Objekt repräsentiert
	 * wird.
	 * 
	 * @param obj
	 *            das zu Grunde liegende Objekt aus der
	 *            Datenverteilerkonfiguration
	 */
	public FlaecheXYImpl(SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.FlaecheXY#getKoordinaten()
	 */
	public List<Punkt> getKoordinaten() {
		if (koordinaten == null) {
			koordinaten = new ArrayList<Punkt>();
			AttributeGroup atg = getSystemObject().getDataModel()
					.getAttributeGroup("atg.flächenKoordinaten");

			DataCache.cacheData(getSystemObject().getType(), atg);
			Data datum = getSystemObject().getConfigurationData(atg);
			if (datum != null) {
				Data.Array xArray = datum.getArray("x");
				Data.Array yArray = datum.getArray("y");
				if ((xArray != null) && (yArray != null)) {
					int size = Math.max(xArray.getLength(), yArray.getLength());
					for (int idx = 0; idx < size; idx++) {
						koordinaten.add(new Punkt(xArray.getItem(idx)
								.asScaledValue().doubleValue(), yArray.getItem(
								idx).asScaledValue().doubleValue()));
					}
				}
			}
		}

		return koordinaten;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.FLAECHE_XY;
	}
}
