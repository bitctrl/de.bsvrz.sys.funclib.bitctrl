/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.KExTlsGlobalTypen;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class DeWzg extends De {

	/**
	 * @param obj
	 */
	public DeWzg(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException("Systemobjekt ist keine DeWzg.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return KExTlsGlobalTypen.DeWzg;
	}

}