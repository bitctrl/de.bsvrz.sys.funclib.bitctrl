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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.ReferenceArray;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.BestehtAusLinienobjekten;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.Linie;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;

/**
 * Repräsentation eines Routenstücks innerhalb der Datenmodellabbildung.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class RoutenStueck extends StoerfallIndikator
implements BestehtAusLinienobjekten {

	/**
	 * die Liste der Straßensegmente, die das Routenstück bilden.
	 */
	private Set<AeusseresStrassenSegment> strassenSegmente;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz eines Routenstücks auf der Basis des
	 * übergebenen Systemobjekts.
	 *
	 * @param obj
	 *            das Systemobjekt
	 */
	public RoutenStueck(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Routenstück.");
		}

		// Straßenteilsegmente bestimmen
		final DataModel modell = obj.getDataModel();
		final AttributeGroup atg = modell
				.getAttributeGroup("atg.bestehtAusLinienObjekten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		final Data datum = getSystemObject().getConfigurationData(atg);
		if (datum != null) {
			final ReferenceArray ref;
			final SystemObject[] objekte;

			strassenSegmente = new HashSet<>();
			ref = datum.getReferenceArray("LinienReferenz");
			objekte = ref.getSystemObjectArray();
			for (final SystemObject so : objekte) {
				strassenSegmente.add((AeusseresStrassenSegment) ObjektFactory
						.getInstanz().getModellobjekt(so));
			}
		}
	}

	@Override
	public List<Linie> getLinien() {
		return new ArrayList<Linie>(getStrassenSegmente());
	}

	/**
	 * liefert die Liste der Straßensegmente, die das Routenstück bilden.
	 *
	 * @return die ermittelte Liste der Segmente
	 */
	public Collection<AeusseresStrassenSegment> getStrassenSegmente() {
		final Collection<AeusseresStrassenSegment> segmente = new ArrayList<>();
		if (strassenSegmente != null) {
			segmente.addAll(strassenSegmente);
		}
		return segmente;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.ROUTENSTUECK;
	}
}
