package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.Collection;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

public interface NetzBestandTeil extends SystemObjekt {
	
	Collection<? extends StrassenSegment> getSegmentListe();
	
}
