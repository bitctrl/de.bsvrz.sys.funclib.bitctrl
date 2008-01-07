package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public class Kreisgrenzen extends KomplexXY {

	protected Kreisgrenzen(SystemObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.KREISGRENZEN;
	}

}
