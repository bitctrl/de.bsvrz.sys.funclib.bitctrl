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
import de.bsvrz.sys.funclib.bitctrl.modell.geo.LinieXY;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.LinieXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittAllgemein.MessQuerschnittComparator;

/**
 * Repr&auml;sentiert ein Stra&szlig;enteilsegment. Als Schl&uuml;ssel f&uuml;r
 * die Mess- und Fuzzy-Werte, wird der selbe wie am {@link MessQuerschnitt}
 * benutzt.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class StrassenTeilSegment extends StoerfallIndikator implements LinieXY {

	/**
	 * das Objekt, mit dem die Linieneigenschaften des Straﬂenteilsegments
	 * repr‰sentiert werden.
	 */
	private LinieXY linie;

	/** Die L&auml;nge des Stra&szlig;enteilsegments. */
	private float laenge;

	/** Die Anzahl der Fahrstreifen des Stra&szlig;enteilsegments. */
	private int anzahlFahrStreifen = -1;

	/** Steigung (positiv) oder Gef&auml;lle (negativ) des Segments. */
	private double steigungGefaelle = -200;	// undefiniert


	/**
	 * Nach Offset sortierte Liste der Messquerschnitt auf dem Teilsegement. Die
	 * Liste besteht zwar aus konfigurierenden Daten, diese m&uuml;ssen aber
	 * aufwendig zusammengesucht werden, weswegen die Liste nur bei Bedarf
	 * erstellt wird.
	 */
	private List<MessQuerschnittAllgemein> messQuerschnitte;

	/** Das Stra&szlig;ensegment auf dem das Stra&szlig;enteilsegment liegt. */
	private StrassenSegment strassenSegment;

	/**
	 * Erzeugt ein Stra&szlig;enteilsegment aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Stra&szlig;enteilsegment sein
	 *            muss
	 * @throws IllegalArgumentException
	 */
	public StrassenTeilSegment(final SystemObject obj) {
		super(obj);

		linie = new LinieXYImpl(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Straﬂenteilsegment.");
		}
	}

	/**
	 * liefert die Anzahl der Fahrstreifen des Strassenteilsegments.
	 * 
	 * @return die Anzahl
	 */
	public int getAnzahlFahrstreifen() {
		leseKonfigDaten();
		return anzahlFahrStreifen;
	}

	/**
	 * liefert die Steigung/Gef&auml;lle des Strassenteilsegments.
	 * 
	 * @return Steigung (positiv) oder Gef&auml;lle (negativ) des Segments.
	 */
	public double getSteigungGefaelle() {
		leseKonfigDaten();
		return steigungGefaelle;
	}

	/**
	 * liefert die konfigurierten Koordinaten des Straﬂenteilsegments.
	 * 
	 * @return die Koordinaten
	 */
	public List<Punkt> getKoordinaten() {
		return linie.getKoordinaten();
	}

	/**
	 * Gibt die L&auml;nge des Stra&szlig;enteilsegments zur&uuml;ck.
	 * 
	 * @return Die L&auml;nge
	 */
	public float getLaenge() {
		leseKonfigDaten();
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
	 * Gibt das Stra&szlig;ensegment zur&uuml;ck, auf das
	 * Stra&szlig;enteilsegment liegt.
	 * 
	 * @return das Stra&szlig;ensegment.
	 */
	public StrassenSegment getStrassenSegment() {
		if (strassenSegment == null) {
			List<SystemObjekt> listeSO;

			listeSO = ObjektFactory.getInstanz().bestimmeModellobjekte(
					VerkehrsModellTypen.STRASSENSEGMENT.getPid());

			for (SystemObjekt so : listeSO) {
				StrassenSegment ss = (StrassenSegment) so;
				if (ss.getStrassenTeilSegmente().contains(this)) {
					strassenSegment = ss;
					break;
				}
			}
		}
		return strassenSegment;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSENTEILSEGMENT;
	}

	/**
	 * Ruft konfigurierende Daten vom Datenverteiler ab.
	 */
	private void leseKonfigDaten() {
		if (anzahlFahrStreifen == -1) {
			DataModel modell;
			AttributeGroup atg;
			Data datum;

			modell = objekt.getDataModel();
			atg = modell.getAttributeGroup("atg.straﬂenTeilSegment");

			DataCache.cacheData(getSystemObject().getType(), atg);

			datum = objekt.getConfigurationData(atg);
			if (datum != null) {
				laenge = datum.getScaledValue("L‰nge").floatValue();
				anzahlFahrStreifen = datum.getUnscaledValue(
						"AnzahlFahrStreifen").intValue();
				steigungGefaelle = datum.getScaledValue(
						"SteigungGef‰lle").doubleValue();
			} else {
				laenge = 0;
				anzahlFahrStreifen = 0;
				steigungGefaelle = 0;
			}
		}
	}
}
