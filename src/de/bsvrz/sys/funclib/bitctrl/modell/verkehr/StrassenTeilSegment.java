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
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.Konfigurationsbereich;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnitt.MessQuerschnittComparator;

/**
 * Repr&auml;sentiert ein Stra&szlig;enteilsegment. Als Schl&uuml;ssel f&uuml;r
 * die Mess- und Fuzzy-Werte, wird der selbe wie am {@link MessQuerschnitt}
 * benutzt.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class StrassenTeilSegment extends StoerfallIndikator {

	/** Die L&auml;nge des Stra&szlig;enteilsegments. */
	private final float laenge;

	/**
	 * die Anzahl der Fahrstreifen des Stra&szlig;enteilsegments
	 */
	private int anzahlFahrStreifen;

	/**
	 * Erzeugt ein Stra&szlig;enteilsegment aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Stra&szlig;enteilsegment sein
	 *            muss
	 * @throws IllegalArgumentException
	 */
	public StrassenTeilSegment(SystemObject obj) {
		super(obj);

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Straﬂenteilsegment.");
		}

		// L‰nge bestimmen
		modell = objekt.getDataModel();
		atg = modell.getAttributeGroup("atg.straﬂenTeilSegment");

		DataCache.cacheData(getSystemObject().getType(), atg);

		datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			laenge = datum.getScaledValue("L‰nge").floatValue();
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
	 * TODO: Ergebnis zwischenspeichern, da konfigurierende Daten
	 * <p>
	 * Gibt die Menge der Messquerschnitte dieses Stra&szlig;enteilsegment
	 * zur&uuml;ck. Das Stra&szlig;enteilsegment und seine Messquerschnitte
	 * m&uuml;ssen im selben Konfigurationsbereich definiert sein.
	 * 
	 * @return Menge von Messquerschnitten
	 */
	public List<MessQuerschnitt> getMessQuerschnitte() {
		List<MessQuerschnitt> mengeMQ;
		List<SystemObject> listeSO;

		mengeMQ = new ArrayList<MessQuerschnitt>();
		listeSO = Konfigurationsbereich.getObjekte(objekt
				.getConfigurationArea(), VerkehrsModellTypen.MESSQUERSCHNITT
				.getPid());

		for (SystemObject so : listeSO) {
			MessQuerschnitt mq = (MessQuerschnitt) ObjektFactory.getInstanz()
					.getModellobjekt(so);
			StrassenTeilSegment sts = mq.getStrassenTeilSegment();
			if (this.equals(sts)) {
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
		return VerkehrsModellTypen.STRASSENTEILSEGMENT;
	}
}
