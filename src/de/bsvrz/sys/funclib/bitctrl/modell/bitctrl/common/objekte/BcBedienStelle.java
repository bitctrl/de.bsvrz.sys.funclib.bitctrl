/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.BcCommonTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class BcBedienStelle extends Applikation {

	/**
	 * @param obj
	 */
	public BcBedienStelle(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine BitCtrl-Bedienstelle.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return BcCommonTypen.BcBedienStelle;
	}

}
