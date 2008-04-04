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

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.objekte;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.Konfigurationsbereich;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXY;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModellTypen;

/**
 * Implementtiert die gemeinsame Logik von Umfelddatensensoren.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractUmfeldDatenSensor extends AbstractSystemObjekt
		implements UmfeldDatenSensor {

	/**
	 * Objekt, das die Punkt-Eigenschaften des Objekts repr‰sentiert.
	 */
	private final PunktXY punkt;

	/** Die Liste der Umfelddatenmessstellen denen der Sensor zugeordnet ist. */
	private List<UmfeldDatenMessStelle> listeUDMS;

	/**
	 * Ruft den Superkonstruktor auf.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, was ein Umfelddatensensor darstellt
	 */
	public AbstractUmfeldDatenSensor(final SystemObject obj) {
		super(obj);
		punkt = new PunktXYImpl(obj);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXY#getKoordinate()
	 */
	public Punkt getKoordinate() {
		return punkt.getKoordinate();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<UmfeldDatenMessStelle> getUmfelddatenMessstellen() {
		if (listeUDMS == null) {
			List<SystemObject> listeSO;

			listeUDMS = new ArrayList<UmfeldDatenMessStelle>();
			listeSO = Konfigurationsbereich.getObjekte(objekt
					.getConfigurationArea(),
					UmfelddatenModellTypen.UMFELDDATENMESSSTELLE.getPid());

			for (final SystemObject so : listeSO) {
				final UmfeldDatenMessStelle udms = (UmfeldDatenMessStelle) ObjektFactory
						.getInstanz().getModellobjekt(so);
				if (udms.besitzt(this)) {
					listeUDMS.add(udms);
				}
			}
		}

		return listeUDMS;
	}

}
