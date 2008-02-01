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
 * 
 * Eintrag für die Konfiguration der Betriebskilometer (eines
 * Straßenteilsegments).
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class BetriebsKilometer {

	/**
	 * Abstand des Betriebskilometer-Punktes vom Anfang des Linienobjekts.
	 */
	private final long offset;
	/**
	 * Der Wert des Betriebskilometers.
	 */
	private final long wert;
	/**
	 * Blocknummer für diesen Betriebskilometer. Für eine Straße ist die
	 * Kombinaiton aus Betriebskilometer und Blocknummer eindeutig.
	 */
	private final String blockNummer;

	/**
	 * Konstruktor, erzeugt einen leeren Datensatz und füllt ihn mit den
	 * Informationen aus den übergebenen Datenverteilerinformnationen.
	 * 
	 * @param daten
	 *            die Daten für die Initialisierung
	 */
	public BetriebsKilometer(Data daten) {
		if (daten != null) {
			offset = daten.getUnscaledValue("Offset").longValue();
			wert = daten.getUnscaledValue("Wert").longValue();
			blockNummer = daten.getTextValue("BlockNummer").getText();
		} else {
			offset = 0;
			wert = 0;
			blockNummer = Konstante.LEERSTRING;
		}
	}

	/**
	 * liefert die Blocknummer.
	 * 
	 * @return die Nummer
	 */
	public String getBlockNummer() {
		return blockNummer;
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
	 * liefert den Wert des betriebskilometers.
	 * 
	 * @return den Wert
	 */
	public long getWert() {
		return wert;
	}
}
