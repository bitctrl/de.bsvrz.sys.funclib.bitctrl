/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

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

	/** Ein &auml;u&szlig;eres Stra&szlig;ensegment. */
	AUESSERES_STRASSENSEGMENT("typ.‰uﬂeresStraﬂenSegment",
			AeusseresStrassenSegment.class),

	/** Ein inneres Stra&szlig;ensegment. */
	INNERES_STRASSENSEGMENT("typ.inneresStraﬂenSegment",
			InneresStrassenSegment.class),

	/** Ein Messquerschnitt. */
	STOERFALLINDIKATOR("typ.stˆrfallIndikator", StoerfallIndikator.class),

	/** Ein allgemeiner Messquerschnitt. */
	MESSQUERSCHNITTALLGEMEIN("typ.messQuerschnittAllgemein",
			MessQuerschnittAllgemein.class),

	/** Ein Messquerschnitt. */
	MESSQUERSCHNITT("typ.messQuerschnitt", MessQuerschnitt.class),

	/** Ein virtueller Messquerschnitt. */
	MESSQUERSCHNITTVIRTUELL("typ.messQuerschnittVirtuell",
			MessQuerschnittVirtuell.class),

	/** Ein Netz. */
	NETZ("typ.netz", Netz.class),

	/** Ein Stra&szlig;enknoten. */
	STRASSENKNOTEN("typ.straﬂenKnoten", StrassenKnoten.class),

	/** Ein Stra&szlig;ensegment. */
	STRASSENSEGMENT("typ.straﬂenSegment", StrassenSegment.class),

	/** Ein Stra&szlig;enteilsegment. */
	STRASSENTEILSEGMENT("typ.straﬂenTeilSegment", StrassenTeilSegment.class),

	/** Ein Verkehrsmodellnetz. */
	VERKEHRSMODELLNETZ("typ.verkehrsModellNetz", VerkehrModellNetz.class),

	/** Eine Stra&szlig;e. */
	STRASSE("typ.straﬂe", Strasse.class),

	/** Ein Routenst&uuml;ck. */
	ROUTENSTUECK("typ.routenSt¸ck", RoutenStueck.class);

	/** PID des Objekttyps im Datenverteiler. */
	private final String pid;

	/** Klasse des Systemobjekts im Modell. */
	private final Class<? extends SystemObjekt> klasse;

	/**
	 * Konstruktor.
	 * 
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
