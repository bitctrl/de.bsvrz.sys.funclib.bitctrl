/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.av;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Abstrakte Verwaltungsklasse für Datenanmeldungen.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class DAVAnmeldungsVerwaltung {
	
	/**
	 * produziert ausfuehrlichere Log-Meldungen
	 */
	protected static final boolean DEBUG = false;
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Baum der Datenanmeldungen, die im Moment aktuell sind
	 * (ggf. mit ihrem Status der Sendesteuerung)
	 */
	protected Map<DAVObjektAnmeldung, Byte> aktuelleObjektAnmeldungen =
								new TreeMap<DAVObjektAnmeldung, Byte>();

	/**
	 * Datenverteilerverbindung
	 */
	protected ClientDavInterface dav = null;


	/**
	 * Standardkonstruktor
	 *
	 * @param dav Datenverteilerverbindung
	 */
	protected DAVAnmeldungsVerwaltung(final ClientDavInterface dav){
		this.dav = dav;
	}

	/**
	 * Modifiziert die hier verwalteten Objektanmeldungen
	 * dergestalt, dass nur die innerhalb der übergebenen
	 * Liste beschriebenen Anmeldungen bestehen bleiben.<br>
	 * D.h. insbesondere, dass eine übergebene leere Liste
	 * alle bereits durchgeführten Anmeldungen wieder
	 * rückgängig macht.
	 *
	 * @param neueObjektAnmeldungen die neue Liste mit
	 * Objektanmeldungen
	 */
	public final void modifiziereObjektAnmeldung(final
							Collection<DAVObjektAnmeldung> neueObjektAnmeldungen){

//		Debug Anfang
		String info = Constants.EMPTY_STRING;
		System.out.println("------" + LOGGER.debugInfo());
		if(DEBUG){
			info = "Verlangte Anmeldungen (" + this.getInfo() + "): "; //$NON-NLS-1$ //$NON-NLS-2$
			if(neueObjektAnmeldungen.size() == 0){
				info += "keine\n"; //$NON-NLS-1$
			}else{
				info += "\n"; //$NON-NLS-1$
			}
			for(DAVObjektAnmeldung neueObjektAnmeldung:neueObjektAnmeldungen){
				info += neueObjektAnmeldung;
			}
			info += "Bisherige Anmeldungen (" + this.getInfo() + "): "; //$NON-NLS-1$ //$NON-NLS-2$
			if(aktuelleObjektAnmeldungen.size() == 0){
				info += "keine\n"; //$NON-NLS-1$
			}else{
				info += "\n"; //$NON-NLS-1$
			}
			for(DAVObjektAnmeldung aktuelleObjektAnmeldung:aktuelleObjektAnmeldungen.keySet()){
				info += aktuelleObjektAnmeldung;
			}
		}
//		Debug Ende

		LOGGER.error("----------1.0");
		synchronized (this) {
			LOGGER.error("----------1");
			Collection<DAVObjektAnmeldung> diffObjekteAnmeldungen =
				new TreeSet<DAVObjektAnmeldung>();
			LOGGER.error("----------2");
			for(DAVObjektAnmeldung neueAnmeldung:neueObjektAnmeldungen){
				if(!aktuelleObjektAnmeldungen.containsKey(neueAnmeldung)){
					diffObjekteAnmeldungen.add(neueAnmeldung);
				}
			}
			LOGGER.error("----------3");

			Collection<DAVObjektAnmeldung> diffObjekteAbmeldungen =
				new TreeSet<DAVObjektAnmeldung>();
			LOGGER.error("----------4");
			for(DAVObjektAnmeldung aktuelleAnmeldung:aktuelleObjektAnmeldungen.keySet()){
				if(!neueObjektAnmeldungen.contains(aktuelleAnmeldung)){
					diffObjekteAbmeldungen.add(aktuelleAnmeldung);
				}
			}
			LOGGER.error("----------5");

			if(DEBUG){
				info += "--------\nABmeldungen: "; //$NON-NLS-1$
				info += abmelden(diffObjekteAbmeldungen);
				info += "ANmeldungen: "; //$NON-NLS-1$
				info += anmelden(diffObjekteAnmeldungen);
				LOGGER.config(info);
			}else{
				abmelden(diffObjekteAbmeldungen);
				anmelden(diffObjekteAnmeldungen);				
			}
		}
		LOGGER.error("----------5.0");
	}

	/**
	 * Führt alle übergebenen Daten<b>ab</b>meldungen durch
	 *
	 * @param abmeldungen durchzuführende Daten<b>ab</b>meldungen
	 * @return eine Liste aller <b>ab</b>gemeldeten Einzel-Anmeldungen
	 * als Zeichenkette
	 */
	protected abstract String abmelden(final
								Collection<DAVObjektAnmeldung> abmeldungen);

	/**
	 * Führt alle übergebenen Daten<b>an</b>meldungen durch.
	 *
	 * @param anmeldungen durchzuführende Daten<b>an</b>meldungen
	 * @return eine Liste aller neu <b>an</b>gemeldeten Einzel-Anmeldungen
	 * als Zeichenkette
	 */
	protected abstract String anmelden(final
								Collection<DAVObjektAnmeldung> anmeldungen);

	/**
	 * Erfragt Informationen zum Anmeldungsverhalten
	 *
	 * @return Informationen zum Anmeldungsverhalten
	 */
	protected abstract String getInfo();

}
