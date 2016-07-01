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

import java.util.List;

/**
 * <p>
 * Repr&auml;sentiert eine Ortsreferenz nach dem ASB-Stationierungssystem.
 * </p>
 * <p>
 * Bei dem das ASB-Stationierungssystem wird die Ortsrefererenz &uuml;ber
 * Anfangs- und Endpunkt (Netzknoten bzw. Nullpunkt) sowie der Stationierung im
 * Wertebereich von 0 bis zugewiesene L&auml;nge sowie der Richtung festgelegt.
 * </p>
 * <p>
 * Kurzbeschreibung des ASB-Stationierungssystems:
 * </p>
 * Die Kreuzungen (Netzknoten) des klassifizierten Stra&szlig;ennetzes erhalten
 * innerhalb eines Kartenblattes einer Topographischen Karte eine eindeutige
 * Nummer. Die Beschreibung eines Stra&szlig;enabschnitts ergibt sich nun aus
 * der Nennung von Anfangsknoten und Endknoten. Diesem Stra&szlig;enabschnitt
 * wird die reale L&auml;nge (Keine Berechnung aufgrund von Koordinaten!)
 * zugewiesen. Innerhalb eines Netzknotens k&ouml;nnen &Auml;ste definiert
 * werden. Diese &Auml;ste starten und Enden an so genannten Nullpunkten, die im
 * Bezug zum Netzknoten eindeutig indiziert sind. Den &Auml;sten wird ebenfalls
 * die reale L&auml;nge zugewiesen. Die Lagebeschreibung von Punkte entlang
 * eines Abschnitts oder eines Astes kann man nun eindeutig durch den Anfangs-
 * und Endpunkt (Netzknoten bzw. Nullpunkt) sowie der Stationierung im
 * Wertebereich von 0 bis zugewiesene L&auml;nge sowie der Richtung (ergibt sich
 * aus der Reihenfolge der Netzknoten) erfolgen. Bei dem &Uuml;bergang auf die
 * Darstellung von Verkehrsbeziehungen werden die Nullpunkte zu
 * Verbindungspunkte. Zwischen den Verbindungspunkten sind Stra&szlig;enelemente
 * definiert. Dabei besitzen die Stra&szlig;enelemente nach Definition ASB eine
 * Referenz auf das oben beschriebene ASB Stationierungssystem. Durch diese
 * Referenzierung der Stra&szlig;enelemente auf die Stra&szlig;enabschnitte
 * lassen sich alle Informationen von Abschnitten und &Auml;sten &uuml;ber das
 * Stationierungssystem auf die Stra&szlig;enelemente &uuml;bertragen.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public interface AsbStationierungOrtsReferenzInterface {
	// ------------------------------------------------------------------------
	// SONSTIGE METHODEN
	// ------------------------------------------------------------------------

	/**
	 * Rechnet Ortsreferenzen vom ASB-Stationierungssystem in Angaben mit
	 * Stra&szlig;enSegment und den Offset vom Anfang des Stra&szlig;enSegments
	 * um. Da potentiell mehrere Stra&szlig;en &uuml;ber einen Abschnitt
	 * verlaufen k&ouml;nnen, wird eine Liste aller gefundenen Referenzen
	 * gebildet.
	 *
	 * @return Liste von Ortsreferenzen, bei denen die Ortsangabe &uuml;ber ein
	 *         Stra&szlig;enSegment und den Offset vom Anfang des
	 *         Stra&szlig;enSegments dargestellt wird.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	List<StrassenSegmentUndOffsetOrtsReferenzInterface> ermittleOrtsReferenzStrassenSegmentUndOffset()
			throws NetzReferenzException;

	/**
	 * Rechnet Ortsreferenzen vom ASB-Stationierungssystem in Angaben &uuml;ber
	 * eine Stra&szlig;e und den Betriebskilometers um.
	 *
	 * @return Ortsreferenz, bei dem die Ortsangabe &uuml;ber eine Stra&szlig;e
	 *         und den Betriebskilometer dargestellt wird.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist
	 */
	List<StrasseUndBetriebsKilometerOrtsReferenzInterface> ermittleOrtsReferenzStrasseUndBetriebsKilometer()
			throws NetzReferenzException;

	/**
	 * Gibt den Anfangsknoten der ASB Stationierung zur&uuml;ck.
	 *
	 * @return Anfangsknoten der ASB Stationierung. Anfangsknoten mit
	 *         eindeutiger Kennung (Anfangsnullpunkt) des Teilabschnittes oder
	 *         Astes. Die Nullpunktbezeichung hat die Form TTTTnnnB, wobei TTTT
	 *         die vierstellige TK25-Blattnummer und nnn die dreistellige
	 *         laufende Nummer ist, die zusammen die bundesweit eindeutige
	 *         Netzknotennummer darstellen. Durch die Kennung B (ein Zeichen)
	 *         wird zus&auml;tzlich der Nullpunkt des Abschnitts oder Astes
	 *         eindeutig festgelegt. Eine nicht vorhandene Kennung wird als
	 *         o(hne) eingetragen.
	 */
	String getAnfangsKnoten();

	/**
	 * Gibt die Stationierungsrichtung f&uuml;r den ASB Abschnitt zur&uuml;ck.
	 *
	 * @return Stationierungsrichtung f&uuml;r den ASB Abschnitt.
	 */
	NetzInterface.ASBStationierungsRichtung getAsbStationierungsRichtung();

	/**
	 * Gibt den Endknoten der ASB Stationierung zur&uuml;ck.
	 *
	 * @return Endknoten der ASB Stationierung. Endknoten mit eindeutiger
	 *         Kennung (Endnullpunkt) des Teilabschnittes oder Astes. Die
	 *         Nullpunktbezeichung hat die Form TTTTnnnB, wobei TTTT die
	 *         vierstellige TK25-Blattnummer und nnn die dreistellige laufende
	 *         Nummer ist, die zusammen die bundesweit eindeutige
	 *         Netzknotennummer darstellen. Durch die Kennung B (ein Zeichen)
	 *         wird zus&auml;tzlich der Nullpunkt des Abschnitts oder Astes
	 *         eindeutig festgelegt. Eine nicht vorhandene Kennung wird als
	 *         o(hne) eingetragen.
	 */
	String getEndKnoten();

	/**
	 * Gibt die Stationierung (in Metern) auf dem ASB Abschnitt zur&uuml;ck.
	 *
	 * @return Stationierung (in Metern) auf dem ASB Abschnitt.
	 */
	long getStationierung();
}
