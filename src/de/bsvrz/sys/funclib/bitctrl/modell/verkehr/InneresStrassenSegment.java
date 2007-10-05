package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;

public class InneresStrassenSegment extends StrassenSegment {

	/** PID des Typs eines Inneren Stra&szlig;ensegments. */
	@SuppressWarnings("hiding")
	public final static String PID_TYP = "typ.inneresStra�enSegment";

	private final SystemObject vonSegmentObj;
	private final SystemObject nachSegmentObj;

	public InneresStrassenSegment(SystemObject obj) {
		super(obj);

		AttributeGroup atg = obj.getDataModel().getAttributeGroup(
				"atg.inneresStra�enSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);

		Data daten = obj.getConfigurationData(atg);
		vonSegmentObj = daten.getReferenceValue("vonStra�enSegment")
				.getSystemObject();
		nachSegmentObj = daten.getReferenceValue("nachStra�enSegment")
				.getSystemObject();
	}

	public AeusseresStrassenSegment getNachSegment() {
		AeusseresStrassenSegment result = null;
		if (nachSegmentObj != null) {
			result = (AeusseresStrassenSegment) ObjektFactory.getInstanz()
					.getModellobjekt(nachSegmentObj);
		}
		return result;
	}

	public AeusseresStrassenSegment getVonSegment() {
		AeusseresStrassenSegment result = null;
		if (vonSegmentObj != null) {
			result = (AeusseresStrassenSegment) ObjektFactory.getInstanz()
					.getModellobjekt(vonSegmentObj);
		}
		return result;
	}
}
