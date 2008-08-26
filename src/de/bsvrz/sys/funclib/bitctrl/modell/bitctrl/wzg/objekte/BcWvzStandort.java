/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.wzg.BcWzgTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.objekte.Uz;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class BcWvzStandort extends AbstractSystemObjekt {

	private String beschreibung;
	private Uz unterzentrale;
	private BcSchaltProgrammTyp programmTyp;
	private BcGrundProgrammGruppe grundProgramme;
	private BcSonderProgrammGruppe sonderProgramme;

	/**
	 * @param obj
	 */
	public BcWvzStandort(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein BcWvzStandort.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return BcWzgTypen.BcWvzStandort;
	}

}
