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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

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
import de.bsvrz.sys.funclib.bitctrl.modell.geo.BestehtAusLinienobjekten;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.Linie;

/**
 * Repr�sentation eines Routenst�cks innerhalb der Datenmodellabbildung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class RoutenStueck extends StoerfallIndikator implements
		BestehtAusLinienobjekten {

	/**
	 * die Liste der Stra�ensegmente, die das Routenst�ck bilden.
	 */
	private Set<AeusseresStrassenSegment> strassenSegmente;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz eines Routenst�cks auf der Basis des
	 * �bergebenen Systemobjekts.
	 * 
	 * @param obj
	 *            das Systemobjekt
	 */
	RoutenStueck(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Routenst�ck.");
		}

		// Stra�enteilsegmente bestimmen
		DataModel modell = obj.getDataModel();
		AttributeGroup atg = modell
				.getAttributeGroup("atg.bestehtAusLinienObjekten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			ReferenceArray ref;
			SystemObject[] objekte;

			strassenSegmente = new HashSet<AeusseresStrassenSegment>();
			ref = datum.getReferenceArray("LinienReferenz");
			objekte = ref.getSystemObjectArray();
			for (SystemObject so : objekte) {
				strassenSegmente.add((AeusseresStrassenSegment) ObjektFactory
						.getInstanz().getModellobjekt(so));
			}
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.BestehtAusLinienobjekten#getLinien()
	 */
	public List<Linie> getLinien() {
		return new ArrayList<Linie>(getStrassenSegmente());
	}

	/**
	 * liefert die Liste der Stra�ensegmente, die das Routenst�ck bilden.
	 * 
	 * @return die ermittelte Liste der Segmente
	 */
	public Collection<AeusseresStrassenSegment> getStrassenSegmente() {
		Collection<AeusseresStrassenSegment> segmente = new ArrayList<AeusseresStrassenSegment>();
		if (strassenSegmente != null) {
			segmente.addAll(strassenSegmente);
		}
		return segmente;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallIndikator#getTyp()
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.ROUTENSTUECK;
	}
}
