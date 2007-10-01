package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;ssentiert den Typ eines Ereignisses.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class EreignisTyp extends AbstractSystemObjekt {

	/**
	 * Erzeugt einen Messquerschnitt aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Messquerschnitt sein muss
	 * @throws IllegalArgumentException
	 */
	public EreignisTyp(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Ereignistyp.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.EREIGNISTYP;
	}

}
