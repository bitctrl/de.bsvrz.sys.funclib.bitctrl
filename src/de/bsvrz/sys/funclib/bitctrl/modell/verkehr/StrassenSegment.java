/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
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
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.ReferenceArray;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnitt.MessQuerschnittComparator;

/**
 * Repr&auml;sentiert ein Stra&szlig;ensegment.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class StrassenSegment extends StoerfallIndikator {

	/** Die sortierte Liste der enthaltenen Stra&szlig;enteilsegmente. */
	private final List<StrassenTeilSegment> strassenTeilSegment;

	/** Die L&auml;nge des Stra&szlig;ensegments. */
	private final float laenge;

	/** Die Stra&szlig;e, zur der das Stra&zslig;ensegment gehˆrt. */
	private final Strasse strasse;

	/**
	 * Konstruiert aus einem Systemobjekt ein Stra&szlig;ensegment.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Stra&szlig;ensegment darstellt
	 * @throws IllegalArgumentException
	 */
	public StrassenSegment(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Straﬂensegment.");
		}

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		modell = objekt.getDataModel();

		// L‰nge bestimmen
		atg = modell.getAttributeGroup("atg.straﬂenSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);
		datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			laenge = datum.getScaledValue("L‰nge").floatValue();
			SystemObject strassenObjekt = datum.getReferenceValue(
					"gehˆrtZuStraﬂe").getSystemObject();
			if (strassenObjekt != null) {
				strasse = (Strasse) ObjektFactory
						.getModellobjekt(strassenObjekt);
			} else {
				strasse = null;
			}
		} else {
			laenge = 0;
			strasse = null;
		}

		// Straﬂenteilsegmente bestimmen
		strassenTeilSegment = new ArrayList<StrassenTeilSegment>();
		atg = modell.getAttributeGroup("atg.bestehtAusLinienObjekten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			ReferenceArray ref;
			SystemObject[] objekte;

			ref = datum.getReferenceArray("LinienReferenz");
			objekte = ref.getSystemObjectArray();
			for (SystemObject so : objekte) {
				strassenTeilSegment.add((StrassenTeilSegment) ObjektFactory
						.getModellobjekt(so));
			}
		}
	}

	/**
	 * Gibt die L&auml;nge des Stra&szlig;ensegments zur&uuml;ck.
	 * 
	 * @return Die L&auml;nge
	 */
	public float getLaenge() {
		return laenge;
	}

	/**
	 * Pr&uuml;ft ob ein Stra&szlig;enteilsegment zu diesem Stra&szlig;ensegment
	 * geh&ouml;rt.
	 * 
	 * @param sts
	 *            Ein Stra&szlig;enteilsegment
	 * @return {@code true}, wenn das Stra&szlig;enteilsegment dazugeh&ouml;rt
	 */
	public boolean contains(StrassenTeilSegment sts) {
		return strassenTeilSegment.contains(sts);
	}

	/**
	 * Gibt die Anzahl der enthaltenen Stra&szlig;enteilsegmente zur&uuml;ck.
	 * 
	 * @return Anzahl
	 */
	public int anzahlStrassenTeilSegmente() {
		return strassenTeilSegment.size();
	}

	/**
	 * Gibt die Liste der Stra&szlig;enteilsegmente zur&uuml;ck.
	 * 
	 * @return Liste von Stra&szlig;enteilsegmenten
	 */
	public List<StrassenTeilSegment> getStrassenTeilSegmente() {
		return new ArrayList<StrassenTeilSegment>(strassenTeilSegment);
	}

	/**
	 * TODO: Ergebnis zwischenspeichern, da konfigurierende Daten
	 * <p>
	 * Sucht alle Messquerschnitte der Stra&szlig;enteilsegmente dieses
	 * Stra&szlig;ensegments zusammen.
	 * 
	 * @return Menge aller Messquerschnitte des Stra&szlig;ensegments
	 */
	public List<MessQuerschnitt> getMessquerschnitte() {
		List<MessQuerschnitt> mengeMQ = new ArrayList<MessQuerschnitt>();

		for (MessQuerschnitt mq : MessQuerschnitt.getMqListe(objekt
				.getDataModel())) {
			if (this.equals(mq.getStrassenSegment())) {
				mengeMQ.add(mq);
			}
		}

		Collections.sort(mengeMQ, new MessQuerschnittComparator());
		return mengeMQ;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSENSEGMENT;
	}

	/**
	 * liefert die Stra&szlig;e, zu der das Segment geh&ouml;rt oder <i>null</i>,
	 * wenn keine Stra&szlig;e konfiguriert wurde.
	 * 
	 * @return die Stra&szlig;e oder <i>null</i>.
	 */
	public Strasse getStrasse() {
		return strasse;
	}

	/**
	 * liefert den Punkt, an dem das Straﬂensegment beginnt.
	 * 
	 * @return den Punkt oder {@code null}, wenn keiner ermittelt werden konnte
	 */
	public Punkt getAnfangsPunkt() {
		Punkt result = null;
		if (strassenTeilSegment.size() > 0) {
			StrassenTeilSegment segment = strassenTeilSegment.get(0);
			List<Punkt> punkte = segment.getKoordinaten();
			if (punkte.size() > 0) {
				result = punkte.get(0);
			}
		}
		return result;
	}

	/**
	 * liefert den Punkt, an dem das Straﬂensegment endet.
	 * 
	 * @return den Punkt oder {@code null}, wenn keiner ermittelt werden konnte
	 */
	public Punkt getEndPunkt() {
		Punkt result = null;
		if (strassenTeilSegment.size() > 0) {
			StrassenTeilSegment segment = strassenTeilSegment
					.get(strassenTeilSegment.size() - 1);
			List<Punkt> punkte = segment.getKoordinaten();
			if (punkte.size() > 0) {
				result = punkte.get(punkte.size() - 1);
			}
		}
		return result;
	}
}
