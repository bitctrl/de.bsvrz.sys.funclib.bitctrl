package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.ReferenceArray;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public class RoutenStueck extends StoerfallIndikator {

	private Set<AeusseresStrassenSegment> strassenSegmente;

	public RoutenStueck(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Routenstück.");
		}

		// Straßenteilsegmente bestimmen
		DataModel modell = obj.getDataModel();
		AttributeGroup atg = modell
				.getAttributeGroup("atg.bestehtAusLinienObjekten");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			ReferenceArray ref;
			SystemObject[] objekte;

			strassenSegmente = new HashSet<AeusseresStrassenSegment>();
			ref = datum.getReferenceArray("LinienReferenz");
			objekte = ref.getSystemObjectArray();
			for (SystemObject so : objekte) {
				strassenSegmente.add((AeusseresStrassenSegment) ObjektFactory
						.getModellobjekt(so));
			}
		}
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.ROUTENSTUECK;
	}

	public Collection<AeusseresStrassenSegment> getStrassenSegmente() {
		Collection<AeusseresStrassenSegment> segmente = new ArrayList<AeusseresStrassenSegment>();
		if (strassenSegmente != null) {
			segmente.addAll(strassenSegmente);
		}
		return segmente;
	}
}
