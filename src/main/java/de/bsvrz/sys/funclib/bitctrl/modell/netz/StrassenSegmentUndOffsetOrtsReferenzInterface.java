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

import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;

/**
 * Repr&auml;sentiert eine Ortsrefererenz &uuml;ber die Angabe eines
 * Stra&szlig;enSegments und dem Offset vom Anfang des Stra&szlig;enSegments.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public interface StrassenSegmentUndOffsetOrtsReferenzInterface {
	// ------------------------------------------------------------------------
	// SONSTIGE METHODEN
	// ------------------------------------------------------------------------

	/**
	 * Rechnet Ortsreferenzen mit Stra&szlig;enSegment und den Offset in
	 * Ortsangabe &uuml;ber das ASB-Stationierungssystem um (Anfangs- und
	 * Endpunkt (Netzknoten bzw. Nullpunkt), Stationierung im Wertebereich von 0
	 * bis zugewiesene L&auml;nge sowie Angabe der Richtung).
	 *
	 * @return Ortsreferenz nach dem ASB-Stationierungssystem.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung()
			throws NetzReferenzException;

	/**
	 * Rechnet Ortsreferenzen mit Stra&szlig;enSegment und den Offset in Angaben
	 * &uuml;ber eine Stra&szlig;e und den Betriebskilometers um.
	 *
	 * @return Ortsreferenz, bei dem die Ortsangabe &uuml;ber eine Stra&szlig;e
	 *         und den Betriebskilometer dargestellt wird.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */

	StrasseUndBetriebsKilometerOrtsReferenzInterface ermittleOrtsReferenzStrasseUndBetriebsKilometer()
			throws NetzReferenzException;

	/**
	 * Gibt die L&auml;ngsneigung f&uuml;r das Stra&szlig;ensegment am
	 * betrachteten Offset zur&uuml;ck.
	 *
	 * @return L&auml;ngsneigung f&uuml;r das Stra&szlig;ensegment am
	 *         betrachteten Offset.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	Integer getLaengsNeigung() throws NetzReferenzException;

	/**
	 * Gibt den Offset auf dem Stra&szlig;enSegment zur&uuml;ck.
	 *
	 * @return Offset auf dem Stra&szlig;enSegment in Metern.
	 */
	long getStartOffset();

	/**
	 * Gibt das referenzierte Stra&szlig;enSegment zur&uuml;ck.
	 *
	 * @return Referenziertes Stra&szlig;enSegment.
	 */
	StrassenSegment getStrassenSegment();

}
