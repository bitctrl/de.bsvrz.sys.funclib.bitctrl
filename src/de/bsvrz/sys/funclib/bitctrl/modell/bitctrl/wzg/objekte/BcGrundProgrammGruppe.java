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
public class BcGrundProgrammGruppe extends AbstractSystemObjekt {

	/**
	 * @param obj
	 */
	public BcGrundProgrammGruppe(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein BcGrundProgrammGruppe.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return BcWzgTypen.BcGrundProgrammGruppe;
	}

}
