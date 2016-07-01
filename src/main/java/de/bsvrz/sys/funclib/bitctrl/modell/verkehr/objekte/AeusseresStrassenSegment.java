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
 * Weißenfelser Straße 67
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
 * Die Repräsentation eines äußeren Straßensegments innerhalb der
 * Modellobjektverwaltung.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class AeusseresStrassenSegment extends StrassenSegment
implements NetzBestandTeil {

	/**
	 * die Liste aller definierten äußeren Straßensegmente.
	 */
	private static Set<AeusseresStrassenSegment> assListe;

	/**
	 * liefert die Liste aller im System deifnierten äußeren Straßensegmente.
	 * <br>
	 * Die Liste wird nur einmalig abgefragt und initialisiert, da
	 * Straßensegmente nicht dynamisch angelegt werden können.
	 *
	 * @param model
	 *            das Datenmodell, aus dem die Straßensegmente ermittelt werden
	 * @return die Liste der ermittelten Straßensegmente
	 */
	public static Collection<AeusseresStrassenSegment> getSegmentListe(
			final DataModel model) {
		final Collection<AeusseresStrassenSegment> result = new ArrayList<>();

		if (assListe == null) {
			final List<SystemObject> listeSO;
			listeSO = model.getType(
					VerkehrsModellTypen.AUESSERES_STRASSENSEGMENT.getPid())
					.getElements();

			assListe = new HashSet<>();
			for (final SystemObject so : listeSO) {
				final AeusseresStrassenSegment ass = (AeusseresStrassenSegment) ObjektFactory
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
	 * der Straßenknoten, an dem das Segment beginnt.
	 */
	private StrassenKnoten vonKnoten;

	/**
	 * der Straßenknoten, an dem das Segment endet.
	 */
	private StrassenKnoten nachKnoten;

	/**
	 * die Menge der Routenstücke, zu denen das Straßensegment gehört.
	 */
	private Set<RoutenStueck> routenStuecke;

	/**
	 * die Richtung des Straßensegments.
	 */
	private TmcRichtung tmcRichtung = TmcRichtung.UNDEFINIERT;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz des äußeren Teilsegments auf Basis des
	 * übergebenen Systemsobjekts.
	 *
	 * @param obj
	 *            das zu Grunde liegende Systemobjekt
	 */
	public AeusseresStrassenSegment(final SystemObject obj) {
		super(obj);

		final DataModel model = obj.getDataModel();

		// Straßenknoten ermitteln
		final AttributeGroup atg = model
				.getAttributeGroup("atg.äußeresStraßenSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);
		final Data daten = getSystemObject().getConfigurationData(atg);
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
			tmcRichtung = TmcRichtung
					.getTyp(daten.getUnscaledValue("TmcRichtung").intValue());
		}
	}

	/**
	 * liefert den Knoten, zu dem das Straßensegment hinführt.
	 *
	 * @return den Knoten
	 */
	public StrassenKnoten getNachKnoten() {
		return nachKnoten;
	}

	@Override
	public Collection<? extends StrassenSegment> getNetzSegmentListe() {
		final Set<StrassenSegment> liste = new HashSet<>();
		liste.add(this);
		if (vonKnoten != null) {
			for (final InneresStrassenSegment segment : vonKnoten
					.getInnereSegmente()) {
				if (equals(segment.getNachSegment())) {
					liste.add(segment);
				}
			}
		}
		if (nachKnoten != null) {
			for (final InneresStrassenSegment segment : nachKnoten
					.getInnereSegmente()) {
				if (equals(segment.getVonSegment())) {
					liste.add(segment);
				}
			}
		}
		return liste;
	}

	/**
	 * liefert die Liste der Routenstücke, zu denen das Straßensegment gehört.
	 *
	 * @return die Liste der ermittelten Routenstücke
	 */
	public Collection<RoutenStueck> getRoutenStuecke() {
		if (routenStuecke == null) {
			routenStuecke = new HashSet<>();

			for (final SystemObject obj : getSystemObject().getDataModel()
					.getType(VerkehrsModellTypen.ROUTENSTUECK.getPid())
					.getElements()) {
				final RoutenStueck rs = (RoutenStueck) ObjektFactory
						.getInstanz().getModellobjekt(obj);
				if (rs.getStrassenSegmente().contains(this)) {
					routenStuecke.add(rs);
				}
			}
		}

		return routenStuecke;
	}

	/**
	 * liefert die für das Straßensegment definierte TMC-Richtung.
	 *
	 * @return die Richtung
	 */
	public TmcRichtung getTmcRichtung() {
		return tmcRichtung;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.AUESSERES_STRASSENSEGMENT;
	}

	/**
	 * liefert den Knoten, an dem das Straßensegment beginnt.
	 *
	 * @return vonKnoten
	 */
	public StrassenKnoten getVonKnoten() {
		return vonKnoten;
	}
}
