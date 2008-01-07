package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public class PunktXYImpl extends AbstractSystemObjekt implements PunktXY {

	protected PunktXYImpl(SystemObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	public SystemObjektTyp getTyp() {
		// TODO Auto-generated method stub
		return GeoModellTypen.PUNKT_XY;
	}
}
