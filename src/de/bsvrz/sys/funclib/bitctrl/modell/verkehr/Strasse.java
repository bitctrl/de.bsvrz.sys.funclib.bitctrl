package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public class Strasse extends AbstractSystemObjekt {

	public Strasse(SystemObject obj) {
		super(obj);
	}

	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSE;
	}

	public Collection<AeusseresStrassenSegment> getAuessereStrassensegmente() {
		Collection<AeusseresStrassenSegment> result = new ArrayList<AeusseresStrassenSegment>();
		for (AeusseresStrassenSegment segment : AeusseresStrassenSegment
				.getSegmentListe(getSystemObject().getDataModel())) {
			if (this.equals(segment.getStrasse())) {
				result.add(segment);
			}
		}
		return result;
	}
}
