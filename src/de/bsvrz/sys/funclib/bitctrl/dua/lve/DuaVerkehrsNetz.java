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
package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Ein Verkehrsnetz im Sinne der DUA
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class DuaVerkehrsNetz {
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Flag: Wurde das statische DUA-Verkehrsnetz bereits initialisiert? 
	 */
	private static boolean INITIALISIERT = false;
	
	
	/**
	 * Initialisiert das gesamte Verkehrs-Netz aus Sicht der DUA<br>
	 * Nach Aufruf dieser Methode sind insbesondere die Fahrtreifen mit den
	 * Informationen zu ihren Ersatz und Nachbarfahrstreifen initialisiert
	 * 
	 * @param dav Verbindung zum Datenverteiler
	 * @throws DUAInitialisierungsException wenn es Probleme geben sollte, die
	 * die Initialisierung des Netzes (im Sinne der DUA) nicht möglich machen
	 */
	public static final synchronized void initialisiere(final ClientDavInterface dav)
	throws DUAInitialisierungsException{
		if(INITIALISIERT){
			LOGGER.warning("Das DUA-Verkehrsnetz wurde bereits initialisiert"); //$NON-NLS-1$
		}else{
			INITIALISIERT = true;
			FahrStreifen.initialisiere(dav);
			MessQuerschnitt.initialisiere(dav);
			MessQuerschnittVirtuell.initialisiere(dav);
			MessStelle.initialisiere(dav);
			ermittleErsatzUndNachbarFS();
			MessStellenGruppe.initialisiere(dav);
		}
	}
	
	
	/**
	 * Ermittelt für alle Fahrstreifen die Nachbar- bzw. Ersatzfahrstreifen,
	 * so diese nicht explizit versorgt sind und trägt sie an den entsprechenden
	 * Fahrtreifen ein
	 */
	private static final void ermittleErsatzUndNachbarFS(){

		for(MessQuerschnitt mq:MessQuerschnitt.getInstanzen()){
			for(FahrStreifen fs:mq.getFahrStreifen()){
				if(fs.getNachbarFahrStreifen() == null){
					FahrStreifen nachbar = mq.getNachbarVon(fs);
					if(nachbar != null){
						fs.setNachbarFahrStreifen(nachbar.getSystemObject());
					}
				}

				if(fs.getNachbarFahrStreifen() == null){
					LOGGER.warning("Für Fahrstreifen " + fs + " kann " +  //$NON-NLS-1$//$NON-NLS-2$
							"kein Nachbarfahrstreifen ermittelt werden"); //$NON-NLS-1$
				}

				if(fs.getErsatzFahrStreifen() == null){
					/**
					 * Ersatzfahrstreifen ist Fahrstreifen desselben Typs 
					 * an Ersatzmessstelle
					 */
					MessQuerschnittAllgemein ersatzQuerschnitt = mq.getErsatzMessquerSchnitt();
					if(ersatzQuerschnitt != null){
						List<FahrStreifen> ersatzFahstreifen = new ArrayList<FahrStreifen>();
						for(FahrStreifen fsAnErsatzQuerschnitt:ersatzQuerschnitt.getFahrStreifen()){
							if(fsAnErsatzQuerschnitt.getLage().equals(fs.getLage())){
								ersatzFahstreifen.add(fsAnErsatzQuerschnitt);
							}
						}
						
						if(ersatzFahstreifen.size() > 0){
							if(ersatzFahstreifen.size() > 1){
								LOGGER.warning("Für Fahrstreifen " + fs + " sind mehrere" + //$NON-NLS-1$ //$NON-NLS-2$
										" Ersatzfahrstreifen ermittelbar." +  //$NON-NLS-1$
										" Wähle: " + ersatzFahstreifen.get(0));  //$NON-NLS-1$
							}
							fs.setErsatzFahrStreifen(ersatzFahstreifen.get(0).getSystemObject());
						}
					}
				}
				
				if(fs.getErsatzFahrStreifen() == null){
					LOGGER.warning("Für Fahrstreifen " + fs + " kann " +  //$NON-NLS-1$//$NON-NLS-2$
							"kein Ersatzfahrstreifen ermittelt werden"); //$NON-NLS-1$
				}
			}
		}
	}
}
