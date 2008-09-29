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

import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;


/**
 * Repr&auml;sentiert eine Ortsrefererenz &uuml;ber die Angabe einer
 * Stra&szlig;e und des Betriebskilometers.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id:$
 * 
 */

public interface StrasseUndBetriebsKilometerOrtsReferenzInterface {
	// ------------------------------------------------------------------------
	// SONSTIGE METHODEN
	// ------------------------------------------------------------------------

	/**
	 * Rechnet Ortsreferenzen mit Stra&szlig;e und Betriebskilometer in
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
	 * Rechnet Ortsreferenzen mit Stra&szlig;e und Betriebskilometer in Angaben
	 * mit Stra&szlig;enSegment und den Offset vom Anfang des
	 * Stra&szlig;enSegments um.
	 * 
	 * @return Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *         Stra&szlig;enSegment und den Offset vom Anfang des
	 *         Stra&szlig;enSegments dargestellt wird.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	StrassenSegmentUndOffsetOrtsReferenzInterface ermittleOrtsReferenzStrassenSegmentUndOffset()
			throws NetzReferenzException;

	/**
	 * Gibt den Betriebskilometer auf der Stra&szlig;e zur&uuml;ck.
	 * 
	 * @return Betriebskilometer auf der Stra&szlig;e in Metern.
	 */
	long getBetriebsKilometer();

	/**
	 * Gibt die Blocknummer des Betriebskilometers auf der Stra&szlig;e
	 * zur&uuml;ck.
	 * 
	 * @return Blocknummer des Betriebskilometers auf der Stra&szlig;e.
	 */
	int getBlockNummer();

	/**
	 * Gibt die Richtung, in der die Stra&szlig;e durchlaufen wird, zur&uuml;ck.
	 * 
	 * @return Richtung auf dem Stra&szlig;enSegment.
	 */
	NetzInterface.FahrtRichtung getFahrtRichtung();

	/**
	 * Gibt die referenzierte Stra&szlig;e zur&uuml;ck.
	 * 
	 * @return Referenzierte Stra&szlig;e.
	 */
	Strasse getStrasse();
}
