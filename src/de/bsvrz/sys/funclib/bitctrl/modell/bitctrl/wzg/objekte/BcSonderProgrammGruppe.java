/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.BcWzgTypen;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class BcSonderProgrammGruppe extends AbstractSystemObjekt {

	/**
	 * @param obj
	 */
	public BcSonderProgrammGruppe(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return BcWzgTypen.BcSonderProgrammGruppe;
	}

}
