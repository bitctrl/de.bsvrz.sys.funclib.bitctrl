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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.ASBStationierungsRichtung;

/**
 * Implementation des {@link AsbStationierungOrtsReferenzInterface}.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id: $
 */
public class AsbStationierungOrtsReferenz implements
		AsbStationierungOrtsReferenzInterface {

	/** Anfangsknoten der ASB Stationierung. */
	private final String anfangsKnoten;

	/** Endknoten der ASB Stationierung. */
	private final String endKnoten;

	/** Stationierung (in Metern) auf dem ASB Abschnitt. */
	private final long stationierung;

	/** Stationierungsrichtung für den ASB Abschnitt. */
	private final ASBStationierungsRichtung richtung;

	/**
	 * Erzeugt eine Instanz einer Ortsreferenzen vom ASB-Stationierungssystem.
	 * 
	 * @param anfangsKnoten
	 *            Anfangsknoten der ASB Stationierung
	 * @param endKnoten Endknoten der ASB Stationierung
	 * @param richtung
	 *            Stationierungsrichtung für den ASB Abschnitt
	 * @param stationierung
	 *            Stationierung (in Metern) auf dem ASB Abschnitt
	 */
	public AsbStationierungOrtsReferenz(String anfangsKnoten, String endKnoten,
			ASBStationierungsRichtung richtung, long stationierung) {

		if ((anfangsKnoten == null) || (anfangsKnoten.length() == 0)) {
			throw new IllegalArgumentException(
					"Der Anfangsknoten darf nicht 'null' oder leer sein");
		}

		if ((endKnoten == null) || (endKnoten.length() == 0)) {
			throw new IllegalArgumentException(
					"Der Endknoten darf nicht 'null' oder leer sein");
		}

		this.anfangsKnoten = anfangsKnoten;
		this.endKnoten = endKnoten;
		this.stationierung = stationierung;
		this.richtung = richtung;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#ermittleOrtsReferenzStrassenSegmentUndOffset()
	 */
	public List<StrassenSegmentUndOffsetOrtsReferenzInterface> ermittleOrtsReferenzStrassenSegmentUndOffset()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzStrassenSegmentUndOffset(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#ermittleOrtsReferenzStrasseUndBetriebsKilometer()
	 */
	public List<StrasseUndBetriebsKilometerOrtsReferenzInterface> ermittleOrtsReferenzStrasseUndBetriebsKilometer()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzStrasseUndBetriebsKilometer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#getAnfangsKnoten()
	 */
	public String getAnfangsKnoten() {
		return anfangsKnoten;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#getAsbStationierungsRichtung()
	 */
	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#getAsbStationierungsRichtung()
	 */
	public ASBStationierungsRichtung getAsbStationierungsRichtung() {
		return richtung;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#getEndKnoten()
	 */
	public String getEndKnoten() {
		return endKnoten;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.AsbStationierungOrtsReferenzInterface#getStationierung()
	 */
	public long getStationierung() {
		return stationierung;
	}

}
