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

package de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.GeoModellTypen;

/**
 * Instanz eines Punktes mit Koordinaten, der als Systemobjekt in der
 * Konfiguration des Datenverteilers definiert ist.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class PunktXYImpl extends AbstractSystemObjekt implements PunktXY {

	/**
	 * Konstruktor.
	 *
	 * @param obj
	 *            das Objekt, mit dem der Punkt innerhalb der
	 *            Datenverteiler-Konfiguration repr‰sentiert ist.
	 */
	public PunktXYImpl(final SystemObject obj) {
		super(obj);
	}

	@Override
	public Punkt getKoordinate() {
		Punkt position = null;
		final DataModel model = getSystemObject().getDataModel();
		final AttributeGroup atg = model
				.getAttributeGroup("atg.punktKoordinaten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		final Data datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			final double x = datum.getScaledValue("x").doubleValue();
			final double y = datum.getScaledValue("y").doubleValue();
			position = new Punkt(x, y);
		}

		return position;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.PUNKT_XY;
	}
}
