package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public class MessQuerschnittVirtuell extends MessQuerschnittAllgemein {

	public MessQuerschnittVirtuell(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein virtueller Messquerschnitt.");
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.MESSQUERSCHNITTVIRTUELL;
	}
}
