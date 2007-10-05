package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

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

public class AeusseresStrassenSegment extends StrassenSegment implements
		NetzBestandTeil {

	static private Set<AeusseresStrassenSegment> assListe;

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

	private StrassenKnoten vonKnoten;
	private StrassenKnoten nachKnoten;
	private Set<RoutenStueck> routenStuecke;

	private TmcRichtung tmcRichtung = TmcRichtung.UNDEFINIERT;

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
	 * @return nachKnoten
	 */
	public StrassenKnoten getNachKnoten() {
		return nachKnoten;
	}

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

	public Collection<? extends StrassenSegment> getSegmentListe() {
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
	 * @return vonKnoten
	 */
	public StrassenKnoten getVonKnoten() {
		return vonKnoten;
	}
}
