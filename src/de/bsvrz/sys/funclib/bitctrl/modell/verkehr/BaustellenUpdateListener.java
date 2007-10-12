package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.EventListener;

public interface BaustellenUpdateListener extends EventListener {
	void baustelleAktualisiert(BaustellenUpdateEvent event);
}
