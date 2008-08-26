/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.KExTlsGlobalTypen;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class Uz extends AbstractSystemObjekt {

	/**
	 * @param obj
	 */
	public Uz(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine Unterzentrale.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return KExTlsGlobalTypen.Uz;
	}

}
