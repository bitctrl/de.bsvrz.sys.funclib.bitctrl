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

import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.FahrtRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;


/**
 * Rep&auml;sentiert eine Ortsreferenz, bei der die Ortsangabe ¸ber eine
 * Stra&szlige und den Betriebskilometer dargestellt wird.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id: OrtsReferenzStrasseUndBetriebsKilometer.java 9715 2008-06-16 14:12:50Z gieseler $
 * 
 */

public class StrasseUndBetriebsKilometerOrtsReferenz implements
		StrasseUndBetriebsKilometerOrtsReferenzInterface {

	/** Betriebskilometer auf der Stra&szlig;e in Metern. */
	long betriebsKilometer;

	/** Blocknummer des Betriebskilometers auf der Straﬂe. */
	int blockNummer;

	/** Richtung auf dem Stra&szlig;enSegment. */
	NetzInterface.FahrtRichtung fahrtRichtung;

	/** Referenzierte Stra&szlig;e. */
	Strasse strasse;

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
	public StrasseUndBetriebsKilometerOrtsReferenz(Strasse strasse,
			FahrtRichtung fahrtRichtung, long betriebsKilometer, int blockNummer) {

		if (strasse == null) {
			throw new IllegalArgumentException(
					"Die Strasse darf nicht 'null' sein");
		}

		this.betriebsKilometer = betriebsKilometer;
		this.blockNummer = blockNummer;
		this.fahrtRichtung = fahrtRichtung;
		this.strasse = strasse;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrasseUndBetriebsKilometerOrtsReferenzInterface#ermittleOrtsReferenzAsbStationierung()
	 */
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzAsbStationierung(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrasseUndBetriebsKilometerOrtsReferenzInterface#ermittleOrtsReferenzStrassenSegmentUndOffset()
	 */
	public StrassenSegmentUndOffsetOrtsReferenzInterface ermittleOrtsReferenzStrassenSegmentUndOffset()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzStrassenSegmentUndOffset(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrasseUndBetriebsKilometerOrtsReferenzInterface#getBetriebsKilometer()
	 */
	public long getBetriebsKilometer() {
		return betriebsKilometer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrasseUndBetriebsKilometerOrtsReferenzInterface#getBlockNummer()
	 */
	public int getBlockNummer() {
		return blockNummer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrasseUndBetriebsKilometerOrtsReferenzInterface#getFahrtRichtung()
	 */
	public FahrtRichtung getFahrtRichtung() {
		return fahrtRichtung;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrasseUndBetriebsKilometerOrtsReferenzInterface#getStrasse()
	 */
	public Strasse getStrasse() {
		return strasse;
	}

}
