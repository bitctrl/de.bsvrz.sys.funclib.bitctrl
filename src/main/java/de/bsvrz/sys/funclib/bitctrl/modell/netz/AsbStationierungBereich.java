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

import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.ASBStationierungsRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.AsbStationierung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.Verkehrsrichtung;

/**
 * Rep&auml;sentiert einen ASB-Stationierungsbereich, d.h. den kleinsten und
 * gr&ouml;ssten Stationierungswert.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */
public class AsbStationierungBereich extends AsbStationierungOrtsReferenz {

	/** max. Stationierung (in Metern) auf dem ASB Abschnitt. */
	private long maxStationierung;

	/** min. Stationierung (in Metern) auf dem ASB Abschnitt. */
	private long minStationierung;

	public void setMinStationierung(final long minStationierung) {
		this.minStationierung = minStationierung;
	}

	public void setMaxStationierung(final long maxStationierung) {
		this.maxStationierung = maxStationierung;
	}

	public long getMaxStationierung() {
		return maxStationierung;
	}

	public long getMinStationierung() {
		return getStationierung();
	}

	/**
	 * Erzeugt eine Instanz eines ASB-Stationierungsbereiches.
	 *
	 * @param anfangsKnoten
	 *            Anfangsknoten der ASB Stationierung
	 * @param endKnoten
	 *            Endknoten der ASB Stationierung
	 * @param richtung
	 *            Stationierungsrichtung für den ASB Abschnitt
	 * @param minStationierung
	 *            kleinste Stationierung (in Metern) auf dem ASB Abschnitt
	 * @param maxStationierung
	 *            gr&ouml;sste Stationierung (in Metern) auf dem ASB Abschnitt
	 */
	public AsbStationierungBereich(final String anfangsKnoten,
			final String endKnoten, final ASBStationierungsRichtung richtung,
			final long minStationierung, final long maxStationierung) {
		super(anfangsKnoten, endKnoten, richtung, minStationierung);
		this.maxStationierung = maxStationierung;
		this.minStationierung = minStationierung;
	}

	public AsbStationierungBereich(final AsbStationierung asbStationierung) {
		this(asbStationierung.getAnfangsKnoten(),
				asbStationierung.getEndKnoten(),
				asbStationierung
						.getVerkehrsRichtung() == Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG
								? NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG
								: NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG,
				asbStationierung.getAnfang(), asbStationierung.getEnde());
	}
}
