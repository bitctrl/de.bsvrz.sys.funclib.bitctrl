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
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.GeoModellTypen;

/**
 * Instanz eines PunktLiegtAufLinieObjekts mit dem Verweis auf die Linie und dem
 * Offset des Punktes auf der Linie.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class PunktLiegtAufLinienObjektmpl extends AbstractSystemObjekt
		implements PunktLiegtAufLinienObjekt {

	/**
	 * die Linie auf der der Punkt liegt.
	 */
	private Linie linie;

	/**
	 * die Offset des Punktes auf der Linie.
	 */
	private float offset;

	/**
	 * markiert, ob die Konfigurationsdaten bereits eingelesen wurden.
	 */
	private boolean gelesen;

	/**
	 * Konstruktor.
	 *
	 * @param obj
	 *            das Objekt, mit dem die Linie innerhalb der
	 *            Datenverteiler-Konfiguration repr‰sentiert ist.
	 */
	public PunktLiegtAufLinienObjektmpl(final SystemObject obj) {
		super(obj);
	}

	@Override
	public Linie getLinie() {
		liesKonfiguration();
		return linie;
	}

	@Override
	public float getOffset() {
		liesKonfiguration();
		return offset;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.PUNKT_LIEGT_AUF_LINIEN_OBJEKT;
	}

	/**
	 * liest die konfigurierenden Daten des Objekts ein.
	 */
	private void liesKonfiguration() {
		if (!gelesen) {
			final AttributeGroup atg = getSystemObject().getDataModel()
					.getAttributeGroup("atg.punktLiegtAufLinienObjekt");

			DataCache.cacheData(getSystemObject().getType(), atg);
			final Data datum = getSystemObject().getConfigurationData(atg);
			if (datum != null) {
				SystemObject so;

				so = datum.getReferenceValue("LinienReferenz")
						.getSystemObject();
				linie = (Linie) ObjektFactory.getInstanz().getModellobjekt(so);
				offset = datum.getScaledValue("Offset").floatValue();
			}

			gelesen = true;
		}
	}
}
