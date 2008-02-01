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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Eintrag für die Konfiguration der AsbStationierung (eines
 * Straßenteilsegments).
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class AsbStationierung {

	/**
	 * Abstand des ASB-Stationierungs-Punktes vom Anfang des Linienobjekts.
	 */
	private final long offset;

	/**
	 * Anfangsknoten mit eindeutiger Kennung (Anfangsnullpunkt) des
	 * Teilabschnittes oder Astes. Die Nullpunktbezeichung hat die Form
	 * TTTTnnnB, wobei TTTT die vierstellige TK25-Blattnummer und nnn die
	 * dreistellige laufende Nummer ist, die zusammen die bundesweit eindeutige
	 * Netzknotennummer darstellen. Durch die Kennung B (ein Zeichen) wird
	 * zusätzlich der Nullpunkt des Abschnitts oder Astes eindeutig festgelegt.
	 * Eine nicht vorhandene Kennung wird als o(hne) eingetragen.
	 */
	private final String anfangsKnoten;

	/**
	 * Endknoten mit eindeutiger Kennung (Endnullpunkt) des Teilabschnittes oder
	 * Astes. Die Nullpunktbezeichung hat die Form TTTTnnnB, wobei TTTT die
	 * vierstellige TK25-Blattnummer und nnn die dreistellige laufende Nummer
	 * ist, die zusammen die bundesweit eindeutige Netzknotennummer darstellen.
	 * Durch die Kennung B (ein Zeichen) wird zusätzlich der Nullpunkt des
	 * Abschnitts oder Astes eindeutig festgelegt. Eine nicht vorhandene Kennung
	 * wird als o(hne) eingetragen.
	 */
	private final String endKnoten;
	/**
	 * Anfangsstationierung. Stationierungsangabe in Metern relativ zum
	 * Abschnitt oder Ast.
	 */
	private final long anfang;
	/**
	 * Endstationierung. Stationierungsangabe in Metern relativ zum Abschnitt
	 * oder Ast.
	 */
	private final long ende;

	/**
	 * Angabe des Richtungsbezugs der ASB-Stationierung relativ zur Richtung des
	 * Straßensegments.
	 */
	private final Verkehrsrichtung verkehrsRichtung;

	/**
	 * Konstruktor, erzeugt einen leeren Datensatz und füllt ihn mit den
	 * Informationen aus den übergebenen Datenverteilerinformnationen.
	 * 
	 * @param daten
	 *            die Daten für die Initialisierung
	 */
	public AsbStationierung(Data daten) {
		if (daten != null) {
			anfang = daten.getUnscaledValue("Anfang").longValue();
			anfangsKnoten = daten.getTextValue("AnfangsKnoten").getText();
			ende = daten.getUnscaledValue("Ende").longValue();
			endKnoten = daten.getTextValue("EndKnoten").getText();
			offset = daten.getUnscaledValue("Offset").longValue();
			verkehrsRichtung = Verkehrsrichtung.getVerkehrsRichtung(daten
					.getUnscaledValue("VerkehrsRichtung").shortValue());
		} else {
			anfang = 0;
			anfangsKnoten = Konstante.LEERSTRING;
			ende = 0;
			endKnoten = Konstante.LEERSTRING;
			offset = 0;
			verkehrsRichtung = Verkehrsrichtung.UNBEKANNT;
		}
	}

	/**
	 * liefert den Anfangswert.
	 * 
	 * @return den Wert
	 */
	public long getAnfang() {
		return anfang;
	}

	/**
	 * liefert den Anfangsknoten.
	 * 
	 * @return den Knoten
	 */
	public String getAnfangsKnoten() {
		return anfangsKnoten;
	}

	/**
	 * liefert den Endwert.
	 * 
	 * @return den Wert
	 */
	public long getEnde() {
		return ende;
	}

	/**
	 * liefert den Endknoten.
	 * 
	 * @return den Knoten
	 */
	public String getEndKnoten() {
		return endKnoten;
	}

	/**
	 * liefert den Offset.
	 * 
	 * @return den Offset
	 */
	public long getOffset() {
		return offset;
	}

	/**
	 * liefert die Verkehrsrichtung.
	 * 
	 * @return die Richtung
	 */
	public Verkehrsrichtung getVerkehrsRichtung() {
		return verkehrsRichtung;
	}
}
