package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fasst alle Objekttypen im Kalendermodell zusammen.
 *
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum KalenderModellTypen implements SystemObjektTyp {

	/** Ein Ereignistyp. */
	EREIGNISTYP("typ.ereignisTyp", EreignisTyp.class),
	
	/** Ein Ereignis. */
	EREIGNIS("typ.ereignis", Ereignis.class);

	/** PID des Objekttyps im Datenverteiler. */
	private final String pid;

	/** Klasse des Systemobjekts im Modell. */
	private final Class<? extends SystemObjekt> klasse;

	/**
	 * @param pid
	 *            Die PID des Typs
	 * @param klasse
	 *            Die Klasse des Modellobjekts
	 */
	private KalenderModellTypen(String pid, Class<? extends SystemObjekt> klasse) {
		this.pid = pid;
		this.klasse = klasse;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends SystemObjekt> getKlasse() {
		return klasse;
	}

}
