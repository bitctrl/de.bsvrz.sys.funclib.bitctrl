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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.FahrtRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.BetriebsKilometer;

/**
 * Betriebskilometerangaben eines Stra&szlig:ensegmentes.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id:$
 * 
 */

public class SegmentBetriebsKilometer {
	/**
	 * Eine Betriebskilometerangabe auf dem Segment.
	 * 
	 * @author BitCtrl Systems GmbH, Gieseler
	 * @version $Id: SegmentBetriebsKilometer.java 9722 2008-06-16 16:34:21Z
	 *          gieseler $
	 * 
	 */
	public class BetriebsKilometerSegment {

		/**
		 * Betriebskilometerangabe.
		 */
		private final BetriebsKilometer betriebsKilometer;

		/**
		 * zugeh&ouml;riges Teilsegment.
		 */
		private final StrassenTeilSegment teilsegment;

		/**
		 * zugeh&ouml;riges Segment.
		 */
		private final StrassenSegment segment;

		/**
		 * Konstruktor.
		 * 
		 * @param betriebskilometer
		 *            Betriebskilometerangabe
		 * @param teilsegment
		 *            Teilsegment
		 * @param segment
		 *            Stra&szlig;ensegment
		 */
		public BetriebsKilometerSegment(
				final BetriebsKilometer betriebskilometer,
				final StrassenTeilSegment teilsegment, StrassenSegment segment) {
			super();
			this.betriebsKilometer = betriebskilometer;
			this.teilsegment = teilsegment;
			this.segment = segment;
		}

		/**
		 * Gibt den Wert des Betriebskilometers zur&uuml;ck.
		 * 
		 * @return Betriebskilometer
		 */
		public long getBetriebsKilometer() {
			return betriebsKilometer.getWert();
		}

		/**
		 * Gibt die Blocknummer zur&uuml;ck.
		 * 
		 * @return Blocknummer
		 */
		public int getBlockNummer() {
			return Integer.parseInt(betriebsKilometer.getBlockNummer());
		}

		/**
		 * Gibt den Offset der Betriebskilometerangabe auf dem Segment
		 * zur&uuml;ck.
		 * 
		 * @return Offset auf dem Segment
		 */
		public double getSegmentOffset() {
			return segment.getTeilSegmentOffset(teilsegment)
					+ betriebsKilometer.getOffset();
		}

	}

	/**
	 * Die zugeh&ouml;rige Stra&szlig;e.
	 */
	private final Strasse strasse;

	/**
	 * Liste der Betriebskilometerangaben auf dem Segment.
	 */
	private final List<BetriebsKilometerSegment> bk = new ArrayList<BetriebsKilometerSegment>();

	/**
	 * Fahrtrichtung auf der Stra&szlig;e.
	 */
	private final FahrtRichtung fahrtRichtung;

	/**
	 * Konstruktor.
	 * 
	 * @param strasse
	 *            Stra&szlig;e
	 * @param fahrtRichtung
	 *            Fahrtrichtung auf der Stra&szlig;e
	 * @param segment
	 *            Stra&szlig;ensegment
	 * @throws NetzReferenzException
	 *             wenn weniger als 2 Betriebskilometerangaben auf dem Segment
	 *             existieren
	 */
	public SegmentBetriebsKilometer(Strasse strasse,
			FahrtRichtung fahrtRichtung, StrassenSegment segment)
			throws NetzReferenzException {
		super();
		this.strasse = strasse;
		this.fahrtRichtung = fahrtRichtung;

		for (StrassenTeilSegment teilsegment : segment
				.getStrassenTeilSegmente()) {
			for (BetriebsKilometer betriebskilometer : teilsegment
					.getBetriebsKilometer()) {
				bk.add(new BetriebsKilometerSegment(betriebskilometer,
						teilsegment, segment));
			}
		}

		if (bk.size() < 2) {
			throw new NetzReferenzException(
					"Zu wenige Betriebskilometerangaben zur Umrechnung");
		}
	}

	/**
	 * Findet eine Betriebskilometerangabe f&uuml;r einen bestimmten Offset
	 * innerhalb eines Stra&szlig;enteilsegmentes.
	 * 
	 * @param offset
	 *            Offset auf dem Stra&szlig;ensegment
	 * @return OrtsReferenzStrasseUndBetriebsKilometer
	 * @throws NetzReferenzException
	 *             wenn keine passende Betriebskilometerangabe bestimmt werden
	 *             konnte
	 */
	public StrasseUndBetriebsKilometerOrtsReferenz findeBetriebsKilometerAmOffset(
			final double offset) throws NetzReferenzException {

		if (bk.size() < 2) {
			throw new NetzReferenzException(
					"Zu wenige Betriebskilometerangaben zur Umrechnung");
		}

		BetriebsKilometerSegment vor = null;
		BetriebsKilometerSegment nach = null;

		for (BetriebsKilometerSegment bkr : bk) {
			if (bkr.getSegmentOffset() == offset) {
				return new StrasseUndBetriebsKilometerOrtsReferenz(strasse,
						fahrtRichtung, bkr.getBetriebsKilometer(), bkr
								.getBlockNummer());
			}

			if (bkr.getSegmentOffset() < offset) {
				vor = bkr;
			}

			if (bkr.getSegmentOffset() > offset) {
				nach = bkr;
				break;
			}
		}

		if (vor == null || nach == null
				|| (vor.getBlockNummer() != nach.getBlockNummer())) {
			throw new NetzReferenzException(
					"Die Blocknummer kann nicht bestimmt werden");
		}

		return new StrasseUndBetriebsKilometerOrtsReferenz(strasse,
				fahrtRichtung,
				(long) (vor.getBetriebsKilometer() + offset - vor
						.getSegmentOffset()), vor.getBlockNummer());
	}
}
