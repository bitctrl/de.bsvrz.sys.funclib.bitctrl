/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal;

import static de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen.APPLIKATION;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * @author Schumann
 * 
 */
public class Applikation extends AbstractSystemObjekt {

	/**
	 * @param obj
	 */
	public Applikation(SystemObject obj) {
		super(obj);
	}

	/**
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return APPLIKATION;
	}

}
