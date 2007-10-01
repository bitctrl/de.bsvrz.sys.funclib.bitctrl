package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fasst alle Objekttypen im Umfelddatenmodell zusammen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum UmfelddatenModelTypen implements SystemObjektTyp {

	/** Umfelddatensensor. */
	UMFELDDATENSENSOR("typ.umfeldDatenSensor", UmfelddatenSensor.class),

	/** Umfelddatensensor f&uuml;r die Helligkeit. */
	UDS_HELLIGKEIT("typ.ufdsHelligkeit", UDSHelligkeit.class),

	/** Umfelddatensensor f&uuml;r die Niederschlag. */
	UDS_NIEDERSCHLAGSINTENSITAET("typ.ufdsNiederschlagsIntensität",
			UDSNiederschlagsintensitaet.class),

	/** Umfelddatensensor f&uuml;r die Sichtweite. */
	UDS_SICHTWEITE("typ.ufdsSichtWeite", UDSSichtweite.class),

	/** Umfelddatensensor f&uuml;r die Windgeschwindigkeit. */
	UDS_WINDGESCHWINDIGKEIT("typ.ufdsWindGeschwindigkeitMittelWert",
			UDSWindgeschwindigkeit.class),

	/** Umfelddatensensor f&uuml;r die Windrichtung. */
	UDS_WINDRICHTUNG("typ.ufdsWindRichtung", UDSWindrichtung.class),

	/** Umfelddatenmessstelle. */
	UMFELDDATENMESSSTELLE("typ.umfeldDatenMessStelle",
			UmfelddatenMessstelle.class);

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
	private UmfelddatenModelTypen(String pid,
			Class<? extends SystemObjekt> klasse) {
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
