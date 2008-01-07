package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender;

public enum GeoModellTypen implements SystemObjektTyp {
	KOMPLEX_XY("typ.komplexXY", KomplexXY.class),
	/** Eine Fläche. */
	FLAECHE_XY("typ.flächeXY", FlaecheXYImpl.class),
	/** Ein Punkt. */
	PUNKT_XY("typ.punktXY", PunktXYImpl.class),
	/** Eine Linie. */
	LINIE_XY("typ.linieXY", LinieXYImpl.class),

	/** die Grenzen eines Kreise. */
	KREISGRENZEN("typ.Kreis", Kreisgrenzen.class),
	/** die Grenzen eines Ortes. */
	ORTSLAGE("typ.Ortslage", Ortslage.class),
	/** Eine Ortsbezeichung. */
	ORTSNAME("typ.Ortsname", Ortsname.class);

	/** PID des Objekttyps im Datenverteiler. */
	private final String pid;

	/** Klasse des Systemobjekts im Modell. */
	private final Class<? extends SystemObjekt> klasse;

	/**
	 * Privater Konstruktor.
	 * 
	 * @param pid
	 *            Die PID des Typs
	 * @param klasse
	 *            Die Klasse des Modellobjekts
	 */
	private GeoModellTypen(String pid, Class<? extends SystemObjekt> klasse) {
		this.pid = pid;
		this.klasse = klasse;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends SystemObjekt> getKlasse() {
		return klasse;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return pid;
	}
}
