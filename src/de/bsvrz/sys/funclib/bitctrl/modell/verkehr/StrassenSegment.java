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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.ReferenceArray;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.BestehtAusLinienobjekten;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.Linie;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittAllgemein.MessQuerschnittComparator;

/**
 * Repr&auml;sentiert ein Stra&szlig;ensegment.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class StrassenSegment extends StoerfallIndikator implements
		BestehtAusLinienobjekten {

	/** Die sortierte Liste der enthaltenen Stra&szlig;enteilsegmente. */
	private List<StrassenTeilSegment> strassenTeilSegmente;

	/** Die L&auml;nge des Stra&szlig;ensegments. */
	private float laenge;

	/** Die Stra&szlig;e, zur der das Stra&zslig;ensegment gehört. */
	private Strasse strasse;

	/**
	 * Nach Offset sortierte Liste der Messquerschnitt auf dem Segement. Die
	 * Liste besteht zwar aus konfigurierenden Daten, diese m&uuml;ssen aber
	 * aufwendig zusammengesucht werden, weswegen die Liste nur bei Bedarf
	 * erstellt wird.
	 */
	private List<MessQuerschnittAllgemein> messQuerschnitte;

	/**
	 * Konstruiert aus einem Systemobjekt ein Stra&szlig;ensegment.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Stra&szlig;ensegment darstellt
	 * @throws IllegalArgumentException
	 */
	StrassenSegment(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Straßensegment.");
		}
	}

	/**
	 * Gibt die Anzahl der enthaltenen Stra&szlig;enteilsegmente zur&uuml;ck.
	 * 
	 * @return Anzahl
	 */
	public int anzahlStrassenTeilSegmente() {
		return getStrassenTeilSegmente().size();
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
		return getStrassenTeilSegmente().contains(sts);
	}

	/**
	 * liefert den Punkt, an dem das Straßensegment beginnt.
	 * 
	 * @return den Punkt oder {@code null}, wenn keiner ermittelt werden konnte
	 */
	public Punkt getAnfangsPunkt() {
		Punkt result = null;
		if (getStrassenTeilSegmente().size() > 0) {
			StrassenTeilSegment segment = getStrassenTeilSegmente().get(0);
			List<Punkt> punkte = segment.getKoordinaten();
			if (punkte.size() > 0) {
				result = punkte.get(0);
			}
		}
		return result;
	}

	/**
	 * liefert den Punkt, an dem das Straßensegment endet.
	 * 
	 * @return den Punkt oder {@code null}, wenn keiner ermittelt werden konnte
	 */
	public Punkt getEndPunkt() {
		Punkt result = null;
		if (getStrassenTeilSegmente().size() > 0) {
			StrassenTeilSegment segment = getStrassenTeilSegmente().get(
					getStrassenTeilSegmente().size() - 1);
			List<Punkt> punkte = segment.getKoordinaten();
			if (punkte.size() > 0) {
				result = punkte.get(punkte.size() - 1);
			}
		}
		return result;
	}

	/**
	 * Gibt die L&auml;nge des Stra&szlig;ensegments zur&uuml;ck.
	 * 
	 * @return Die L&auml;nge
	 */
	public float getLaenge() {
		leseKonfigDaten();
		return laenge;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.BestehtAusLinienobjekten#getLinien()
	 */
	public List<Linie> getLinien() {
		return new ArrayList<Linie>(getStrassenTeilSegmente());
	}

	/**
	 * Sucht alle Messquerschnitte der Stra&szlig;enteilsegmente dieses
	 * Stra&szlig;ensegments zusammen.
	 * 
	 * @return Menge aller Messquerschnitte des Stra&szlig;ensegments. Die Liste
	 *         kann leer sein.
	 */
	public List<MessQuerschnittAllgemein> getMessquerschnitte() {
		if (messQuerschnitte == null) {
			List<SystemObjekt> listeSO;

			messQuerschnitte = new ArrayList<MessQuerschnittAllgemein>();
			listeSO = ObjektFactory.getInstanz().bestimmeModellobjekte(
					VerkehrsModellTypen.MESSQUERSCHNITTALLGEMEIN.getPid());

			for (SystemObjekt so : listeSO) {
				MessQuerschnittAllgemein mq = (MessQuerschnittAllgemein) so;
				if (this.equals(mq.getStrassenSegment())) {
					messQuerschnitte.add(mq);
				}
			}

			Collections.sort(messQuerschnitte, new MessQuerschnittComparator());
		}

		return Collections.unmodifiableList(messQuerschnitte);
	}

	/**
	 * liefert die Stra&szlig;e, zu der das Segment geh&ouml;rt oder <i>null</i>,
	 * wenn keine Stra&szlig;e konfiguriert wurde.
	 * 
	 * @return die Stra&szlig;e oder <i>null</i>.
	 */
	public Strasse getStrasse() {
		leseKonfigDaten();
		return strasse;
	}

	/**
	 * Gibt die Liste der Stra&szlig;enteilsegmente zur&uuml;ck.
	 * 
	 * @return Liste von Stra&szlig;enteilsegmenten. Die Liste kann leer sein.
	 */
	public List<StrassenTeilSegment> getStrassenTeilSegmente() {
		if (strassenTeilSegmente == null) {
			DataModel modell;
			AttributeGroup atg;
			Data datum;

			modell = objekt.getDataModel();
			strassenTeilSegmente = new ArrayList<StrassenTeilSegment>();
			atg = modell.getAttributeGroup("atg.bestehtAusLinienObjekten");
			DataCache.cacheData(getSystemObject().getType(), atg);
			datum = objekt.getConfigurationData(atg);
			if (datum != null) {
				ReferenceArray ref;
				SystemObject[] objekte;

				ref = datum.getReferenceArray("LinienReferenz");
				objekte = ref.getSystemObjectArray();
				for (SystemObject so : objekte) {
					strassenTeilSegmente
							.add((StrassenTeilSegment) ObjektFactory
									.getInstanz().getModellobjekt(so));
				}
			}
		}
		return Collections.unmodifiableList(strassenTeilSegmente);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSENSEGMENT;
	}

	/**
	 * Liest die konfigurierten Eigenschaften des Objekts.
	 */
	private void leseKonfigDaten() {
		if (messQuerschnitte == null) {
			DataModel modell;
			AttributeGroup atg;
			Data datum;

			modell = objekt.getDataModel();

			// Länge bestimmen
			atg = modell.getAttributeGroup("atg.straßenSegment");
			DataCache.cacheData(getSystemObject().getType(), atg);
			datum = objekt.getConfigurationData(atg);
			if (datum != null) {
				laenge = datum.getScaledValue("Länge").floatValue();
				SystemObject strassenObjekt = datum.getReferenceValue(
						"gehörtZuStraße").getSystemObject();
				if (strassenObjekt != null) {
					strasse = (Strasse) ObjektFactory.getInstanz()
							.getModellobjekt(strassenObjekt);
				} else {
					strasse = null;
				}
			} else {
				laenge = 0;
				strasse = null;
			}
		}
	}

}
