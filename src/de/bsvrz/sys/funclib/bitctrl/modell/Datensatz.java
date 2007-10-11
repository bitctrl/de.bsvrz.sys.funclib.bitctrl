package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;

public interface Datensatz {
	void addUpdateListener(DatensatzUpdateListener l);

	AttributeGroup getAttributGruppe();

	SystemObjekt getObjekt();

	boolean hasDaten();

	boolean isAutoUpdate();

	void removeUpdateListener(DatensatzUpdateListener l);

	void sendeDaten();

	void setAutoUpdate(boolean ein);

	void setDaten(Data daten);
}
