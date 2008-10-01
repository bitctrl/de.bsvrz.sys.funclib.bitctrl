/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.BcCommonTypen;

/**
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class BcBedienStelle extends AbstractSystemObjekt {

	/** Der Standardpräfix für den Namen einer Bedienstelle. */
	public static final String PRAEFIX_NAME = "bcBedienStelle.";

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
	public SystemObjektTyp getTyp() {
		return BcCommonTypen.BcBedienStelle;
	}

}
