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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.tmc.zustaende.TmcRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;

/**
 * Die Repr‰sentation eines ‰uﬂeren Straﬂensegments innerhalb der
 * Modellobjektverwaltung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class AeusseresStrassenSegment extends StrassenSegment implements
		NetzBestandTeil {

	/**
	 * die Liste aller definierten ‰uﬂeren Straﬂensegmente.
	 */
	private static Set<AeusseresStrassenSegment> assListe;

	/**
	 * liefert die Liste aller im System deifnierten ‰uﬂeren Straﬂensegmente.<br>
	 * Die Liste wird nur einmalig abgefragt und initialisiert, da
	 * Straﬂensegmente nicht dynamisch angelegt werden kˆnnen.
	 * 
	 * @param model
	 *            das Datenmodell, aus dem die Straﬂensegmente ermittelt werden
	 * @return die Liste der ermittelten Straﬂensegmente
	 */
	public static Collection<AeusseresStrassenSegment> getSegmentListe(
			final DataModel model) {
		Collection<AeusseresStrassenSegment> result = new ArrayList<AeusseresStrassenSegment>();

		if (assListe == null) {
			List<SystemObject> listeSO;
			listeSO = model.getType(
					VerkehrsModellTypen.AUESSERES_STRASSENSEGMENT.getPid())
					.getElements();

			assListe = new HashSet<AeusseresStrassenSegment>();
			for (SystemObject so : listeSO) {
				AeusseresStrassenSegment ass = (AeusseresStrassenSegment) ObjektFactory
						.getInstanz().getModellobjekt(so);
				if (ass != null) {
					assListe.add(ass);
				}
			}
		}

		result.addAll(assListe);
		return result;
	}

	/**
	 * der Straﬂenknoten, an dem das Segment beginnt.
	 */
	private StrassenKnoten vonKnoten;

	/**
	 * der Straﬂenknoten, an dem das Segment endet.
	 */
	private StrassenKnoten nachKnoten;

	/**
	 * die Menge der Routenst¸cke, zu denen das Straﬂensegment gehˆrt.
	 */
	private Set<RoutenStueck> routenStuecke;

	/**
	 * die Richtung des Straﬂensegments.
	 */
	private TmcRichtung tmcRichtung = TmcRichtung.UNDEFINIERT;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz des ‰uﬂeren Teilsegments auf Basis des
	 * ¸bergebenen Systemsobjekts.
	 * 
	 * @param obj
	 *            das zu Grunde liegende Systemobjekt
	 */
	public AeusseresStrassenSegment(SystemObject obj) {
		super(obj);

		DataModel model = obj.getDataModel();

		// Straﬂenknoten ermitteln
		AttributeGroup atg = model
				.getAttributeGroup("atg.‰uﬂeresStraﬂenSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data daten = getSystemObject().getConfigurationData(atg);
		if (daten != null) {
			SystemObject knotenObj = daten.getReferenceValue("vonKnoten")
					.getSystemObject();
			if (knotenObj != null) {
				vonKnoten = (StrassenKnoten) ObjektFactory.getInstanz()
						.getModellobjekt(knotenObj);
			}
			knotenObj = daten.getReferenceValue("nachKnoten").getSystemObject();
			if (knotenObj != null) {
				nachKnoten = (StrassenKnoten) ObjektFactory.getInstanz()
						.getModellobjekt(knotenObj);
			}
			tmcRichtung = TmcRichtung.getTyp(daten.getUnscaledValue(
					"TmcRichtung").intValue());
		}
	}

	/**
	 * liefert den Knoten, zu dem das Straﬂensegment hinf¸hrt.
	 * 
	 * @return den Knoten
	 */
	public StrassenKnoten getNachKnoten() {
		return nachKnoten;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.NetzBestandTeil#getSegmentListe()
	 */
	public Collection<? extends StrassenSegment> getNetzSegmentListe() {
		Set<StrassenSegment> liste = new HashSet<StrassenSegment>();
		liste.add(this);
		if (vonKnoten != null) {
			for (InneresStrassenSegment segment : vonKnoten.getInnereSegmente()) {
				if (this.equals(segment.getNachSegment())) {
					liste.add(segment);
				}
			}
		}
		if (nachKnoten != null) {
			for (InneresStrassenSegment segment : nachKnoten
					.getInnereSegmente()) {
				if (this.equals(segment.getVonSegment())) {
					liste.add(segment);
				}
			}
		}
		return liste;
	}

	/**
	 * liefert die Liste der Routenst¸cke, zu denen das Straﬂensegment gehˆrt.
	 * 
	 * @return die Liste der ermittelten Routenst¸cke
	 */
	public Collection<RoutenStueck> getRoutenStuecke() {
		if (routenStuecke == null) {
			routenStuecke = new HashSet<RoutenStueck>();

			for (SystemObject obj : getSystemObject().getDataModel().getType(
					VerkehrsModellTypen.ROUTENSTUECK.getPid()).getElements()) {
				RoutenStueck rs = (RoutenStueck) ObjektFactory.getInstanz()
						.getModellobjekt(obj);
				if (rs.getStrassenSegmente().contains(this)) {
					routenStuecke.add(rs);
				}
			}
		}

		return routenStuecke;
	}

	/**
	 * liefert die f¸r das Straﬂensegment definierte TMC-Richtung.
	 * 
	 * @return die Richtung
	 */
	public TmcRichtung getTmcRichtung() {
		return tmcRichtung;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.AUESSERES_STRASSENSEGMENT;
	}

	/**
	 * liefert den Knoten, an dem das Straﬂensegment beginnt.
	 * 
	 * @return vonKnoten
	 */
	public StrassenKnoten getVonKnoten() {
		return vonKnoten;
	}
}
