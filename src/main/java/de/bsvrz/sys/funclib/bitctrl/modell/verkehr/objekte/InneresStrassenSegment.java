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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;

/**
 * Repr‰sentation eines inneren Straﬂensegment in der Datenmodellabbildung.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class InneresStrassenSegment extends StrassenSegment {

	/** PID des Typs eines Inneren Stra&szlig;ensegments. */
	public static final String PID_TYP = "typ.inneresStraﬂenSegment";

	/**
	 * das ‰uﬂere Straﬂensegment am Beginn des Segments.
	 */
	private final SystemObject vonSegmentObj;

	/**
	 * das ‰uﬂere Straﬂensegment am Ende des Segments.
	 */
	private final SystemObject nachSegmentObj;

	/** markiert, ob der Straﬂenknoten bereits ermittelt wurde. */
	private boolean knotenGesucht;

	/** der Straﬂenknoten zu dem das Segment gehˆrt. */
	private StrassenKnoten strassenKnoten;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt ein InneresStraﬂensegment auf der Basis des
	 * ¸bergebenen Systemsobjekts.
	 *
	 * @param obj
	 *            das Systemobjekt
	 */
	public InneresStrassenSegment(final SystemObject obj) {
		super(obj);

		final AttributeGroup atg = obj.getDataModel()
				.getAttributeGroup("atg.inneresStraﬂenSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);

		final Data daten = obj.getConfigurationData(atg);
		vonSegmentObj = daten.getReferenceValue("vonStraﬂenSegment")
				.getSystemObject();
		nachSegmentObj = daten.getReferenceValue("nachStraﬂenSegment")
				.getSystemObject();
	}

	/**
	 * liefert das ‰uﬂere Straﬂensegment, das nach dem inneren Straﬂensegment
	 * folgt.
	 *
	 * @return das Segment oder <code>null</code>, wenn keines konfiguriert ist
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
	 * ermittelt den Straﬂenknoten, zu dem des Segment gehˆrt.
	 *
	 * @return der Knoten oder <code>null</code>
	 */
	public StrassenKnoten getStrassenKnoten() {
		if (!knotenGesucht) {
			final List<SystemObjekt> listeSO;

			listeSO = ObjektFactory.getInstanz().bestimmeModellobjekte(
					VerkehrsModellTypen.STRASSENKNOTEN.getPid());

			for (final SystemObjekt so : listeSO) {
				final StrassenKnoten knoten = (StrassenKnoten) so;
				if (knoten.getInnereSegmente().contains(this)) {
					strassenKnoten = knoten;
					break;
				}
			}
			knotenGesucht = true;
		}

		return strassenKnoten;
	}

	/**
	 * liefert das ‰uﬂere Straﬂensegment, an dem das Segment beginnt.
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
