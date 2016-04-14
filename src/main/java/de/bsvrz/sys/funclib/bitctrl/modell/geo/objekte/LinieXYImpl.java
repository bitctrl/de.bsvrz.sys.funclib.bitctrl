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

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.GeoModellTypen;

/**
 * Instanz einer Linie mit Koordinaten, die als Systemobjekt in der
 * Konfiguration des Datenverteilers definiert ist..
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class LinieXYImpl extends AbstractSystemObjekt implements LinieXY {

	/**
	 * die Liste der Koordinaten der Linie.
	 */
	private List<Punkt> koordinaten;

	/**
	 * Konstruktor.
	 *
	 * @param obj
	 *            das Objekt, mit dem die Linie innerhalb der
	 *            Datenverteiler-Konfiguration repr‰sentiert ist.
	 */
	public LinieXYImpl(final SystemObject obj) {
		super(obj);
	}

	@Override
	public List<Punkt> getKoordinaten() {
		if (koordinaten == null) {
			koordinaten = new ArrayList<>();
			final AttributeGroup atg = getSystemObject().getDataModel()
					.getAttributeGroup("atg.linienKoordinaten");

			DataCache.cacheData(getSystemObject().getType(), atg);
			final Data datum = getSystemObject().getConfigurationData(atg);

			if (datum != null) {
				final Data.Array xArray = datum.getArray("x");
				final Data.Array yArray = datum.getArray("y");
				if ((xArray != null) && (yArray != null)) {
					final int size = Math.max(xArray.getLength(),
							yArray.getLength());
					for (int idx = 0; idx < size; idx++) {
						koordinaten.add(new Punkt(
								xArray.getItem(idx).asScaledValue()
										.doubleValue(),
								yArray.getItem(idx).asScaledValue()
										.doubleValue()));
					}
				}
			}
		}

		return koordinaten;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.LINIE_XY;
	}
}
