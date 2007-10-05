package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;

public class InneresStrassenSegment extends StrassenSegment {

	/** PID des Typs eines Inneren Stra&szlig;ensegments. */
	@SuppressWarnings("hiding")
	public final static String PID_TYP = "typ.inneresStraﬂenSegment";

	private final SystemObject vonSegmentObj;
	private final SystemObject nachSegmentObj;

	public InneresStrassenSegment(SystemObject obj) {
		super(obj);

		AttributeGroup atg = obj.getDataModel().getAttributeGroup(
				"atg.inneresStraﬂenSegment");
		DataCache.cacheData(getSystemObject().getType(), atg);

		Data daten = obj.getConfigurationData(atg);
		vonSegmentObj = daten.getReferenceValue("vonStraﬂenSegment")
				.getSystemObject();
		nachSegmentObj = daten.getReferenceValue("nachStraﬂenSegment")
				.getSystemObject();
	}

	public AeusseresStrassenSegment getNachSegment() {
		return (AeusseresStrassenSegment) ObjektFactory.getInstanz()
				.getModellobjekt(nachSegmentObj);
	}

	public AeusseresStrassenSegment getVonSegment() {
		return (AeusseresStrassenSegment) ObjektFactory.getInstanz()
				.getModellobjekt(vonSegmentObj);
	}
}
