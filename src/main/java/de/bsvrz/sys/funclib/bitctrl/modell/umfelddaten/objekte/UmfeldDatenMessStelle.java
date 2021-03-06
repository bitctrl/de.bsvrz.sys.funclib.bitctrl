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

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.objekte;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.NonMutableSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXY;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModellTypen;

/**
 * Repr&auml;sentiert eine Umfelddatenmessstelle.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class UmfeldDatenMessStelle extends AbstractSystemObjekt implements PunktXY {

	/** Liste der Umfelddatensensoren dieser Umfelddatenmessstelle. */
	private final List<UmfeldDatenSensor> umfelddatensensoren;

	/**
	 * das Objekt mit dem die Punkteigenschaften des Objekts repräsentiert
	 * werden.
	 */
	private final PunktXY punkt;

	/**
	 * Erzeugt eine Umfelddatenmessstelle aus einem Systemobjekt.
	 *
	 * @param so
	 *            Ein Systemobjekt, welches eine Umfelddatenmessstelle sein muss
	 * @throws IllegalArgumentException
	 *             das übergebene Objekt ist keine Umfelddatenmessstelle
	 */
	public UmfeldDatenMessStelle(final SystemObject so) {
		super(so);

		punkt = new PunktXYImpl(so);

		final ConfigurationObject co;
		final NonMutableSet menge;

		umfelddatensensoren = new ArrayList<>();

		co = (ConfigurationObject) so;
		menge = co.getNonMutableSet("UmfeldDatenSensoren");
		for (final SystemObject obj : menge.getElements()) {
			umfelddatensensoren.add((UmfeldDatenSensor) ObjektFactory.getInstanz().getModellobjekt(obj));
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
	public boolean besitzt(final UmfeldDatenSensor uds) {
		return umfelddatensensoren.contains(uds);
	}

	/**
	 * liefert die konfigurierten Koordinaten, an denen sich die Messstelle
	 * befindet.
	 *
	 * @return die Position oder <code>null</code>, wenn keine konfiguriert
	 *         wurde
	 */
	@Override
	public Punkt getKoordinate() {
		return punkt.getKoordinate();
	}

	@Override
	public SystemObjektTyp getTyp() {
		return UmfelddatenModellTypen.UMFELDDATENMESSSTELLE;
	}

	/**
	 * Gibt einen Iterator der Umfelddatensensoren dieser Umfelddatenmessstelle
	 * zur&uuml;ck.
	 *
	 * @return Ein Umfelddatensensoriterator
	 */
	public List<UmfeldDatenSensor> getUmfelddatensensoren() {
		return new ArrayList<>(umfelddatensensoren);
	}

}
