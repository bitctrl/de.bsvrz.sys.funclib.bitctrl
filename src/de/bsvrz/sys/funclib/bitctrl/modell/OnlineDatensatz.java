package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.config.Aspect;

public interface OnlineDatensatz extends Datensatz {
	Aspect getEmpfangsAspekt();

	Aspect getSendeAspekt();

	boolean isQuelle();

	boolean isSenke();
}
