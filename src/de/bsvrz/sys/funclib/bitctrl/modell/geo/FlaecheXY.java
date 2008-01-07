package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import java.util.Collection;

import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;

// Wird von TMCGebiet erweitert

public interface FlaecheXY extends Flaeche {

	Collection<Punkt> getKoordinaten();
}
