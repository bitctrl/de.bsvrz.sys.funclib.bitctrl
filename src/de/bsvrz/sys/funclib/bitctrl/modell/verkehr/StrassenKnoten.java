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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Die Repr‰sentation eines Strﬂenknotens innerhalb der Modellabbildung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class StrassenKnoten extends StoerfallIndikator {

	/**
	 * der Typ des Straﬂenknotens.
	 */
	private StrassenKnotenTyp knotenTyp = StrassenKnotenTyp.SONSTIG;

	/**
	 * die Liste der inneren Straﬂensegmente des Knotens.
	 */
	private final Set<InneresStrassenSegment> innereSegmente = new HashSet<InneresStrassenSegment>();

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt einen Straﬂenknoten auf der Basis des ¸bergebenen
	 * Systemobjekts.
	 * 
	 * @param obj
	 *            das Systemobjekt
	 */
	StrassenKnoten(SystemObject obj) {
		super(obj);
		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Straﬂenknoten.");
		}

		DataModel modell = objekt.getDataModel();
		AttributeGroup atg = modell.getAttributeGroup("atg.straﬂenKnoten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data daten = obj.getConfigurationData(atg);
		if (daten != null) {
			knotenTyp = StrassenKnotenTyp.getTyp(daten.getUnscaledValue("Typ")
					.intValue());
		}

		for (SystemObject o : ((ConfigurationObject) obj).getObjectSet(
				"InnereStraﬂenSegmente").getElements()) {
			innereSegmente.add((InneresStrassenSegment) ObjektFactory
					.getInstanz().getModellobjekt(o));
		}
	}

	/**
	 * liefert eine Kopie der Liste der inneren Straﬂensegmente.
	 * 
	 * @return die Liste der Segmente
	 */
	public Collection<InneresStrassenSegment> getInnereSegmente() {
		return new ArrayList<InneresStrassenSegment>(innereSegmente);
	}

	/**
	 * liefert das innere Straﬂensegment, das ein ‰uﬂeres Straﬂensegment des
	 * Knotens in Fahrtrichtung mit einem anderen ‰uﬂeren Straﬂensegment des
	 * Knotens auf der gleichen Straﬂe verbindet. Wird kein solches gefunden,
	 * wird das innere Straﬂensegment geliefert, das an dem ‰uﬂeren Segment
	 * h‰ngt. Existiert auch dieses nicht, wird <b><i>null</i></b> geliefert.
	 * 
	 * @param segment
	 *            das ‰uﬂere Straﬂensegment
	 * @return das ermittelte innere Straﬂensegment
	 */
	public InneresStrassenSegment getInnereVerbindungDanach(
			AeusseresStrassenSegment segment) {
		InneresStrassenSegment result = null;
		InneresStrassenSegment candidate = null;
		Strasse strasse = segment.getStrasse();
		if ((segment != null) && (strasse != null)) {
			for (InneresStrassenSegment innen : innereSegmente) {
				if (segment.equals(innen.getVonSegment())) {
					AeusseresStrassenSegment ass = innen.getNachSegment();
					if ((ass != null) && strasse.equals(ass.getStrasse())) {
						if (ass.getTmcRichtung() == segment.getTmcRichtung()) {
							result = innen;
							break;
						}
					}

					if ((ass == null) && strasse.equals(innen.getStrasse())) {
						candidate = innen;
					}
				}
			}
		}

		if (result == null) {
			result = candidate;
		}

		return result;
	}

	/**
	 * liefert das innere Straﬂensegment, das ein ‰uﬂeres Straﬂensegment des
	 * Knotens entgengesetzt der Fahrtrichtung mit einem anderen ‰uﬂeren
	 * Straﬂensegment des Knotens auf der gleichen Straﬂe verbindet. Wird kein
	 * solches gefunden, wird das innere Straﬂensegment geliefert, das an dem
	 * ‰uﬂeren Segment h‰ngt. Existiert auch dieses nicht, wird <b><i>null</i></b>
	 * geliefert.
	 * 
	 * @param segment
	 *            das ‰uﬂere Straﬂensegment
	 * @return das ermittelte innere Straﬂensegment
	 */
	public InneresStrassenSegment getInnereVerbindungDavor(
			AeusseresStrassenSegment segment) {
		InneresStrassenSegment result = null;
		InneresStrassenSegment candidate = null;
		Strasse strasse = segment.getStrasse();
		if ((segment != null) && (strasse != null)) {
			for (InneresStrassenSegment innen : innereSegmente) {
				if (segment.equals(innen.getNachSegment())) {
					AeusseresStrassenSegment ass = innen.getVonSegment();
					if ((ass != null) && strasse.equals(ass.getStrasse())) {
						if (ass.getTmcRichtung() == segment.getTmcRichtung()) {
							result = innen;
							break;
						}
					}

					if ((ass == null) && strasse.equals(innen.getStrasse())) {
						candidate = innen;
					}
				}
			}
		}

		if (result == null) {
			result = candidate;
		}

		return result;
	}

	/**
	 * liefert den Typ des Straﬂenknotens.
	 * 
	 * @return den Typ
	 */
	public StrassenKnotenTyp getKnotenTyp() {
		return knotenTyp;
	}

	/**
	 * liefert die Lage des Straﬂenknotens.
	 * 
	 * @return die Lage als Punkt oder {@code null}, wenn keine ermittelt
	 *         werden konnte
	 */
	public Punkt getLocation() {
		Punkt result = null;
		double sumX = 0;
		double sumY = 0;
		int count = 0;

		for (InneresStrassenSegment segment : innereSegmente) {
			Punkt pkt = segment.getAnfangsPunkt();
			if (pkt != null) {
				sumX += pkt.getX();
				sumY += pkt.getY();
				count++;
			}

			pkt = segment.getEndPunkt();
			if (pkt != null) {
				sumX += pkt.getX();
				sumY += pkt.getY();
				count++;
			}

			if (segment.getNachSegment() != null) {
				pkt = segment.getNachSegment().getAnfangsPunkt();
				if (pkt != null) {
					sumX += pkt.getX();
					sumY += pkt.getY();
					count++;
				}
			}

			if (segment.getVonSegment() != null) {
				pkt = segment.getVonSegment().getEndPunkt();
				if (pkt != null) {
					sumX += pkt.getX();
					sumY += pkt.getY();
					count++;
				}
			}
		}

		if (count > 0) {
			result = new Punkt(sumX / count, sumY / count);
		}

		return result;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallIndikator#getTyp()
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSENKNOTEN;
	}
}
