package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.EventListener;

public interface DatensatzUpdateListener extends EventListener {
	void datensatzAktualisiert(DatensatzUpdateEvent event);
}
