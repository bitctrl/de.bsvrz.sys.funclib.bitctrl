package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.BcWzgTypen;

public class BcSchaltProgrammTyp extends AbstractSystemObjekt {

	public BcSchaltProgrammTyp(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return BcWzgTypen.BcSchaltProgrammTyp;
	}

}
