package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.EventObject;

public class BaustellenUpdateEvent extends EventObject {

	public BaustellenUpdateEvent(Object source) {
		super(source);
	}

	Baustelle getBaustelle() {
		return (Baustelle) getSource();
	}
}
