/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.BcWzgTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class BcWvzServer extends Applikation {

	/**
	 * @param obj
	 */
	public BcWvzServer(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return BcWzgTypen.BcWvzServer;
	}

}
