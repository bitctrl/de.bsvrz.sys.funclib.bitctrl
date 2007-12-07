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
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittAllgemein.MessQuerschnittComparator;

/**
 * Repr&auml;sentiert ein Stra&szlig;enteilsegment. Als Schl&uuml;ssel f&uuml;r
 * die Mess- und Fuzzy-Werte, wird der selbe wie am {@link MessQuerschnitt}
 * benutzt.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class StrassenTeilSegment extends StoerfallIndikator {

	/** Die L&auml;nge des Stra&szlig;enteilsegments. */
	private final float laenge;

	/** Die Anzahl der Fahrstreifen des Stra&szlig;enteilsegments. */
	private final int anzahlFahrStreifen;

	/**
	 * Nach Offset sortierte Liste der Messquerschnitt auf dem Teilsegement. Die
	 * Liste besteht zwar aus konfigurierenden Daten, diese m&uuml;ssen aber
	 * aufwendig zusammengesucht werden, weswegen die Liste nur bei Bedarf
	 * erstellt wird.
	 */
	private List<MessQuerschnittAllgemein> messQuerschnitte;

	/**
	 * Erzeugt ein Stra&szlig;enteilsegment aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Stra&szlig;enteilsegment sein
	 *            muss
	 * @throws IllegalArgumentException
	 */
	StrassenTeilSegment(final SystemObject obj) {
		super(obj);

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Straßenteilsegment.");
		}

		// Länge bestimmen
		modell = objekt.getDataModel();
		atg = modell.getAttributeGroup("atg.straßenTeilSegment");

		DataCache.cacheData(getSystemObject().getType(), atg);

		datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			laenge = datum.getScaledValue("Länge").floatValue();
			anzahlFahrStreifen = datum.getUnscaledValue("AnzahlFahrStreifen")
					.intValue();
		} else {
			laenge = 0;
			anzahlFahrStreifen = 0;
		}
	}

	/**
	 * liefert die Anzahl der Fahrstreifen des Strassenteilsegments.
	 * 
	 * @return die Anzahl
	 */
	public int getAnzahlFahrstreifen() {
		return anzahlFahrStreifen;
	}

	/**
	 * liefert die konfigurierten Koordinaten des Straßenteilsegments.
	 * 
	 * @return die Koordinaten
	 */
	public List<Punkt> getKoordinaten() {
		List<Punkt> result = new ArrayList<Punkt>();
		AttributeGroup atg = getSystemObject().getDataModel()
				.getAttributeGroup("atg.linienKoordinaten");

		DataCache.cacheData(getSystemObject().getType(), atg);

		Data datum = getSystemObject().getConfigurationData(atg);
		if (datum != null) {
			Data.Array xArray = datum.getArray("x");
			Data.Array yArray = datum.getArray("y");
			if ((xArray != null) && (yArray != null)) {
				int size = Math.max(xArray.getLength(), yArray.getLength());
				for (int idx = 0; idx < size; idx++) {
					result.add(new Punkt(xArray.getItem(idx).asScaledValue()
							.doubleValue(), yArray.getItem(idx).asScaledValue()
							.doubleValue()));
				}
			}
		}

		return result;
	}

	/**
	 * Gibt die L&auml;nge des Stra&szlig;enteilsegments zur&uuml;ck.
	 * 
	 * @return Die L&auml;nge
	 */
	public float getLaenge() {
		return laenge;
	}

	/**
	 * Gibt die Menge der Messquerschnitte dieses Stra&szlig;enteilsegment
	 * zur&uuml;ck. Das Stra&szlig;enteilsegment und seine Messquerschnitte
	 * m&uuml;ssen im selben Konfigurationsbereich definiert sein.
	 * 
	 * @return Menge von Messquerschnitten
	 */
	public List<MessQuerschnittAllgemein> getMessQuerschnitte() {
		if (messQuerschnitte != null) {
			return messQuerschnitte;
		}

		List<MessQuerschnittAllgemein> listeMQ;
		List<SystemObjekt> listeSO;

		listeMQ = new ArrayList<MessQuerschnittAllgemein>();
		listeSO = ObjektFactory.getInstanz().bestimmeModellobjekte(
				VerkehrsModellTypen.MESSQUERSCHNITTALLGEMEIN.getPid());

		for (SystemObjekt so : listeSO) {
			MessQuerschnittAllgemein mq = (MessQuerschnittAllgemein) ObjektFactory
					.getInstanz().getModellobjekt(so.getSystemObject());
			StrassenTeilSegment sts = mq.getStrassenTeilSegment();
			if (this.equals(sts)) {
				listeMQ.add(mq);
			}
		}

		Collections.sort(listeMQ, new MessQuerschnittComparator());
		messQuerschnitte = listeMQ;
		return listeMQ;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSENTEILSEGMENT;
	}
}
