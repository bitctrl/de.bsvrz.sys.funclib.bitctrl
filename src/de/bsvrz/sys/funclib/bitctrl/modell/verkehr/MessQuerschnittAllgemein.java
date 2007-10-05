package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public abstract class MessQuerschnittAllgemein extends StoerfallIndikator {

	/**
	 * Das Stra&szlig;ensegment auf dem der Messquerschnitt liegt.
	 */
	private final StrassenSegment strassenSegment;
	/**
	 * Der Offset auf dem Stra&szlig;ensegment.
	 */
	private final float offset;

	private Punkt position;

	public MessQuerschnittAllgemein(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein MessquerschnittAllgemein.");
		}

		// Straßensegment und Offset bestimmen
		DataModel modell = objekt.getDataModel();
		AttributeGroup atg = modell
				.getAttributeGroup("atg.punktLiegtAufLinienObjekt");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			SystemObject so;

			so = datum.getReferenceValue("LinienReferenz").getSystemObject();
			strassenSegment = (StrassenSegment) ObjektFactory.getInstanz()
					.getModellobjekt(so);
			offset = datum.getScaledValue("Offset").floatValue();
		} else {
			strassenSegment = null;
			offset = 0;
		}

	}

	public Punkt getLocation() {
		DataModel model = getSystemObject().getDataModel();
		AttributeGroup atg = model.getAttributeGroup("atg.punktKoordinaten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			double x = datum.getScaledValue("x").doubleValue();
			double y = datum.getScaledValue("y").doubleValue();
			position = new Punkt(x, y);
		} else {
			position = null;
		}

		return position;
	}

	/**
	 * Gibt das Stra&szlig;ensegment zur&uuml;ck, auf dem der Messquerschnitt
	 * liegt.
	 * 
	 * @return Ein Stra&szlig;ensegment
	 */
	public StrassenSegment getStrassenSegment() {
		return strassenSegment;
	}

	/**
	 * Gibt die Position des Messquerschnitts auf dem Stra&szlig;ensegment als
	 * Offset zur L&auml;nge des Stra&szlig;ensegments zur&uuml;ck.
	 * 
	 * @return Der Offset
	 */
	public float getStrassenSegmentOffset() {
		return offset;
	}

	/**
	 * Gibt das Stra&szlig;enteilsegment zur&uuml;ck auf dem sich der
	 * Messquerschnitt befindet.
	 * 
	 * @return Das Stra&szlig;enteilsegment
	 */
	public StrassenTeilSegment getStrassenTeilSegment() {
		float offsetSS = 0;
		StrassenTeilSegment sts = null;

		// Das richtige STS ist letzte für das gilt offset(MQ) < offsetSS
		for (StrassenTeilSegment s : strassenSegment.getStrassenTeilSegmente()) {
			if (offsetSS < offset && offset < offsetSS + s.getLaenge()) {
				sts = s;
				break;
			}
			offsetSS += s.getLaenge();
		}
		assert sts != null;

		return sts;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.MESSQUERSCHNITTALLGEMEIN;
	}
}
