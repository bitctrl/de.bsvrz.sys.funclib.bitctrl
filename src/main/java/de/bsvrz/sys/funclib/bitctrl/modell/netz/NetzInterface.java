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

/**
 * Definitionen zur Netzreferenzumrechnung.
 *
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public interface NetzInterface {
	/**
	 * Aufz&auml;hlungstyp f&uuml;r die Stationierungsrichtung auf einem ASB
	 * Abschnitt.
	 */
	enum ASBStationierungsRichtung {
		/**
		 * ASB Abschnitt wird in Stationierungsrichtung betrachtet.
		 */
		IN_STATIONIERUNGSRICHTUNG,

		/**
		 * ASB Abschnitt wird entgegen der Stationierungsrichtung betrachtet.
		 */
		GEGEN_STATIONIERUNGSRICHTUNG
	}

	/**
	 * Aufz&auml;hlungstyp f&uuml;r die Fahrtrichtung auf einer Stra&szlig;e.
	 */
	enum FahrtRichtung {
		/**
		 * Stra&szlig;e wird wie angegeben durchfahren.
		 */
		IN_RICHTUNG,

		/**
		 * Stra&szlig;e wird entgegen der Angaben durchfahren.
		 */
		GEGEN_RICHTUNG
	}

}
