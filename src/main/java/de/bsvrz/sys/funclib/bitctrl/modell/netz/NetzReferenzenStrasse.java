/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.tmc.zustaende.TmcRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.AeusseresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.InneresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenKnoten;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.BetriebsKilometer;

/**
 * Klasse zur Umrechnung von stra&szlig;enbezogenen Referenzen.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public class NetzReferenzenStrasse extends Strasse {
	/**
	 * Repr&auml;sentiert eine Betriebskilometerangabe der Stra&szlig;e.
	 *
	 * @author BitCtrl Systems GmbH, Gieseler
	 *
	 */
	public class BetriebsKilometerStrasse {

		/**
		 * Betriebskilometerwert.
		 */
		long betriebsKilometer;

		/**
		 * Blocknummer.
		 */
		int blockNummer;

		/**
		 * Konstruktor.
		 *
		 * @param betriebsKilometer
		 *            Betriebskilometerwert
		 * @param blockNummer
		 *            Blocknummer
		 */
		public BetriebsKilometerStrasse(final long betriebsKilometer,
				final int blockNummer) {
			super();
			this.betriebsKilometer = betriebsKilometer;
			this.blockNummer = blockNummer;
		}

		/**
		 * Gibt den Betriebskilometer zur&uuml;ck.
		 *
		 * @return Betriebskilometer
		 */
		public long getBetriebsKilometer() {
			return betriebsKilometer;
		}

		/**
		 * Gibt die Blocknummer zur&uuml;ck.
		 *
		 * @return Blocknummer
		 */
		public int getBlockNummer() {
			return blockNummer;
		}

	}

	/**
	 * Die Fahrtrichtung auf der Str&szlig;e.
	 */
	private final TmcRichtung fahrtRichtung;

	/**
	 * Die Liste der Teilsegmente der Str&szlig;e.
	 */
	private final ArrayList<StrassenTeilSegment> teilSegmente = new ArrayList<StrassenTeilSegment>();

	/**
	 * Konstruktor.
	 *
	 * @param systemObjekt
	 *            Systemobjekt der Stra&szlig;e
	 * @param richtung
	 *            TMC-Richtung
	 * @throws NetzReferenzException
	 *             bei Ausnahmen
	 */
	public NetzReferenzenStrasse(final SystemObject systemObjekt,
			final TmcRichtung richtung) throws NetzReferenzException {
		super(systemObjekt);
		fahrtRichtung = richtung;
		init();
	}

	/**
	 * Sucht eine Betriebskilometerangabe zu einem Stra&szlig;enelement und
	 * Offset auf der Stra&szlig;e.
	 *
	 * @param referenz
	 *            StrassenSegmentUndOffsetOrtsReferenz
	 * @return Betriebskilometer-Angabe
	 */
	public BetriebsKilometerStrasse findeBetriebsKilometerZuSegmentUndOffset(
			final StrassenSegmentUndOffsetOrtsReferenzInterface referenz) {

		final StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
				.getModelSegment();

		final StrassenTeilSegment teilsegment = segment
				.getStrassenTeilSegment(referenz.getStartOffset());

		for (int i = 0; i < teilSegmente.size(); i++) {
			final StrassenTeilSegment ts = teilSegmente.get(i);

			if (ts == teilsegment) {
				final double restoffset = referenz.getStartOffset()
						- segment.getTeilSegmentOffset(ts);
				if (ts.getBetriebsKilometer().size() > 0) {
					final BetriebsKilometer bk = ts.getBetriebsKilometer()
							.get(0);
					final double betriebkilometerkorrektur = restoffset
							- bk.getOffset();
					return new BetriebsKilometerStrasse(
							(long) (bk.getWert() + betriebkilometerkorrektur),
							Integer.parseInt(bk.getBlockNummer()));
				}
			}
		}

		return null;
	}

	/**
	 * Bildet die Liste der vorhergehenden Stra&szlig;ensegmente ausgehend von
	 * einem Segment.
	 *
	 * @param segment
	 *            Startsegmenet
	 * @return Liste der vorhergehenden Stra&szlig;ensegmente
	 */
	private LinkedList<StrassenSegment> getSegmentListeRueckwaerts(
			final AeusseresStrassenSegment segment) {
		final LinkedList<StrassenSegment> liste = new LinkedList<StrassenSegment>();

		if (!segment.getStrasse().equals(this)) {
			return liste;
		}

		final StrassenKnoten knoten = segment.getVonKnoten();
		if (knoten != null) {
			for (final InneresStrassenSegment iss : knoten
					.getInnereSegmente()) {
				if (segment.equals(iss.getNachSegment())) {
					final AeusseresStrassenSegment next = iss.getVonSegment();
					if ((next != null)
							&& next.getTmcRichtung()
									.equals(segment.getTmcRichtung())
							&& next.getStrasse().equals(this)) {
						liste.add(next);
						liste.addAll(getSegmentListeRueckwaerts(next));
						break;
					}
				}
			}
		}

		return liste;
	}

	/**
	 * Bildet die Liste der folgenden Stra&szlig;ensegmente ausgehend von einem
	 * Segment.
	 *
	 * @param segment
	 *            Startsegmenet
	 * @return Liste der folgenden Stra&szlig;ensegmente
	 */
	private List<StrassenSegment> getSegmentListeVorwaerts(
			final StrassenSegment segment) {
		final List<StrassenSegment> liste = new ArrayList<StrassenSegment>();
		if (segment == null) {
			return liste;
		}

		if (!segment.getStrasse().equals(this)) {
			return liste;
		}

		if (segment instanceof AeusseresStrassenSegment) {
			final AeusseresStrassenSegment ass = (AeusseresStrassenSegment) segment;
			final StrassenKnoten knoten = ((AeusseresStrassenSegment) segment)
					.getNachKnoten();
			if (knoten != null) {
				for (final InneresStrassenSegment iss : knoten
						.getInnereSegmente()) {
					if (segment.equals(iss.getVonSegment())) {
						final AeusseresStrassenSegment next = iss
								.getNachSegment();
						if ((iss.getNachSegment() != null)
								&& iss.getNachSegment().getTmcRichtung()
										.equals(ass.getTmcRichtung())
								&& next.getStrasse().equals(this)) {
							liste.add(next);
							liste.addAll(getSegmentListeVorwaerts(next));
							break;
						}
					}
				}
			}
		}

		return liste;
	}

	/**
	 * Initialisiert die Strassenreferenz.
	 *
	 * @throws NetzReferenzException
	 *             bei Ausnahmen
	 */
	private void init() throws NetzReferenzException {
		final List<AeusseresStrassenSegment> ass = (List<AeusseresStrassenSegment>) getAuessereStrassensegmente();

		final List<AeusseresStrassenSegment> assr = new ArrayList<AeusseresStrassenSegment>();

		for (final AeusseresStrassenSegment segment : ass) {
			if (segment.getTmcRichtung().equals(fahrtRichtung)) {
				assr.add(segment);
			}
		}

		if (assr.size() == 0) {
			throw new NetzReferenzException("Die Strasse kann in Richtung "
					+ fahrtRichtung + " nicht initialisiert werden");
		}

		LinkedList<StrassenSegment> segmente = new LinkedList<StrassenSegment>();
		// Strassensegmente ordnen
		final AeusseresStrassenSegment start = assr.get(0);
		segmente = getSegmentListeRueckwaerts(start);

		Collections.reverse(segmente);
		segmente.add(start);
		segmente.addAll(getSegmentListeVorwaerts(start));

		for (final StrassenSegment segment : segmente) {
			teilSegmente.addAll(segment.getStrassenTeilSegmente());
		}
	}
}
