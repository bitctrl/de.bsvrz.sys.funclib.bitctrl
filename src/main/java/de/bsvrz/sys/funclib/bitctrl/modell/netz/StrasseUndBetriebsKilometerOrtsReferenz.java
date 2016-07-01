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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.FahrtRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;

/**
 * Rep&auml;sentiert eine Ortsreferenz, bei der die Ortsangabe über eine
 * Stra&szlig;e und den Betriebskilometer dargestellt wird.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public class StrasseUndBetriebsKilometerOrtsReferenz implements StrasseUndBetriebsKilometerOrtsReferenzInterface {

	/** Betriebskilometer auf der Stra&szlig;e in Metern. */
	private final long betriebsKilometer;

	/** Blocknummer des Betriebskilometers auf der Straße. */
	private final int blockNummer;

	/** Richtung auf dem Stra&szlig;enSegment. */
	private final NetzInterface.FahrtRichtung fahrtRichtung;

	/** Referenzierte Stra&szlig;e. */
	private final Strasse strasse;

	/**
	 * Erzeugt eine Ortsreferenz, bei der die Ortsangabe &uuml;ber eine
	 * Stra&szlig;e und den Betriebskilometer dargestellt wird.
	 *
	 * @param strasse
	 *            Referenzierte Stra&szlig;e
	 * @param fahrtRichtung
	 *            Richtung auf dem Stra&szlig;enSegment
	 * @param betriebsKilometer
	 *            Betriebskilometer auf der Stra&szlig;e in Metern
	 * @param blockNummer
	 *            Blocknummer des Betriebskilometers auf der Stra&szlig;e
	 */
	public StrasseUndBetriebsKilometerOrtsReferenz(final Strasse strasse, final FahrtRichtung fahrtRichtung,
			final long betriebsKilometer, final int blockNummer) {

		if (strasse == null) {
			throw new IllegalArgumentException("Die Strasse darf nicht 'null' sein");
		}

		this.betriebsKilometer = betriebsKilometer;
		this.blockNummer = blockNummer;
		this.fahrtRichtung = fahrtRichtung;
		this.strasse = strasse;
	}

	@Override
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung() throws NetzReferenzException {
		return NetzReferenzen.getInstanz().ermittleOrtsReferenzAsbStationierung(this);
	}

	@Override
	public StrassenSegmentUndOffsetOrtsReferenzInterface ermittleOrtsReferenzStrassenSegmentUndOffset()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz().ermittleOrtsReferenzStrassenSegmentUndOffset(this);
	}

	@Override
	public long getBetriebsKilometer() {
		return betriebsKilometer;
	}

	@Override
	public int getBlockNummer() {
		return blockNummer;
	}

	@Override
	public FahrtRichtung getFahrtRichtung() {
		return fahrtRichtung;
	}

	@Override
	public Strasse getStrasse() {
		return strasse;
	}
}
