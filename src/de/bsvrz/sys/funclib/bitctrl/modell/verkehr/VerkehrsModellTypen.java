package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fasst alle Objekttypen im Verkehrsmodell zusammen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum VerkehrsModellTypen implements SystemObjektTyp {

	/** Ein &auml;u&szlig;eres Stra&szlig;ensegment */
	AUESSERES_STRASSENSEGMENT("typ.‰uﬂeresStraﬂenSegment",
			AeusseresStrassenSegment.class),

	/** Ein inneres Stra&szlig;ensegment */
	INNERES_STRASSENSEGMENT("typ.inneresStraﬂenSegment",
			InneresStrassenSegment.class),

	/** Ein Messquerschnitt */
	STOERFALLINDIKATOR("typ.stˆrfallIndikator", StoerfallIndikator.class),

	/** Ein allgemeiner Messquerschnitt */
	MESSQUERSCHNITTALLGEMEIN("typ.messQuerschnittAllgemein",
			MessQuerschnittAllgemein.class),

	/** Ein Messquerschnitt */
	MESSQUERSCHNITT("typ.messQuerschnitt", MessQuerschnitt.class),

	/** Ein virtueller Messquerschnitt */
	MESSQUERSCHNITTVIRTUELL("typ.messQuerschnittVirtuell",
			MessQuerschnittVirtuell.class),

	/** Ein Netz */
	NETZ("typ.netz", Netz.class),

	/** Ein Stra&szlig;enknoten */
	STRASSENKNOTEN("typ.straﬂenKnoten", StrassenKnoten.class),

	/** Ein Stra&szlig;ensegment */
	STRASSENSEGMENT("typ.straﬂenSegment", StrassenSegment.class),

	/** Ein Stra&szlig;enteilsegment */
	STRASSENTEILSEGMENT("typ.straﬂenTeilSegment", StrassenTeilSegment.class),

	/** Ein Verkehrsmodellnetz */
	VERKEHRSMODELLNETZ("typ.verkehrsModellNetz", VerkehrModellNetz.class),

	/** Eine Stra&szlig;e */
	STRASSE("typ.straﬂe", Strasse.class),

	/** Ein Routenst&uuml;ck */
	ROUTENSTUECK("typ.routenSt¸ck", RoutenStueck.class);

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
	private VerkehrsModellTypen(String pid, Class<? extends SystemObjekt> klasse) {
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
