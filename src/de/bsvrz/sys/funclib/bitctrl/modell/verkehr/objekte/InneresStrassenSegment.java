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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;

/**
 * Repr�sentation eines inneren Stra�ensegment in der Datenmodellabbildung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class InneresStrassenSegment extends StrassenSegment {

	/** PID des Typs eines Inneren Stra&szlig;ensegments. */
	@SuppressWarnings("hiding")
	public static final String PID_TYP = "typ.inneresStra�enSegment";

	/**
	 * das �u�ere Stra�ensegment am Beginn des Segments.
	 */
	private final SystemObject vonSegmentObj;

	/**
	 * das �u�ere Stra�ensegment am Ende des Segments.
	 */
	private final SystemObject nachSegmentObj;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt ein InneresStra�ensegment auf der Basis des
	 * �bergebenen Systemsobjekts.
	 * 
	 * @param obj
	 *            das Systemobjekt
	 */
	public InneresStrassenSegment(SystemObject obj) {
		super(obj);

		AttributeGroup atg = obj.getDataModel().getAttributeGroup(
				"atg.inneresStra�enSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);

		Data daten = obj.getConfigurationData(atg);
		vonSegmentObj = daten.getReferenceValue("vonStra�enSegment")
				.getSystemObject();
		nachSegmentObj = daten.getReferenceValue("nachStra�enSegment")
				.getSystemObject();
	}

	/**
	 * liefert das �u�ere Stra�ensegment, das nach dem inneren Stra�ensegment
	 * folgt.
	 * 
	 * @return das Segment oder <code>null</code>, wenn keines konfiguriert
	 *         ist
	 */
	public AeusseresStrassenSegment getNachSegment() {
		AeusseresStrassenSegment result = null;
		if (nachSegmentObj != null) {
			result = (AeusseresStrassenSegment) ObjektFactory.getInstanz()
					.getModellobjekt(nachSegmentObj);
		}
		return result;
	}

	/**
	 * liefert das �u�ere Stra�ensegment, an dem das Segment beginnt.
	 * 
	 * @return das Segment oder <code>null</code>, wenn keines konfiguriert
	 *         wurde.
	 */
	public AeusseresStrassenSegment getVonSegment() {
		AeusseresStrassenSegment result = null;
		if (vonSegmentObj != null) {
			result = (AeusseresStrassenSegment) ObjektFactory.getInstanz()
					.getModellobjekt(vonSegmentObj);
		}
		return result;
	}
}