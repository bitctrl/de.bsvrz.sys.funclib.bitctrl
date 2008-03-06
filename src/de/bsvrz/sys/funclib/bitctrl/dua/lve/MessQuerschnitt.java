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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.FahrStreifenLage;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnitt</code>
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class MessQuerschnitt
extends MessQuerschnittAllgemein{
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Mapt alle Messquerschnitt-Systemobjekte auf Objekte dieser Klasse  
	 */
	protected static Map<SystemObject, MessQuerschnitt> SYS_OBJ_MQ_OBJ_MAP = 
											new HashMap<SystemObject, MessQuerschnitt>();
	
	/**
	 * Datenverteiler-Verbindung
	 */
	protected static ClientDavInterface DAV = null;
			
	/**
	 * Menge der an diesem Messquerschnitt definierten Fahstreifen
	 */
	private List<FahrStreifen> fahrStreifen = new ArrayList<FahrStreifen>();

	
	/**
	 * Standardkontruktor
	 * 
	 * @param mqObjekt ein Systemobjekt vom Typ <code>typ.messQuerschnitt</code>
	 */
	protected MessQuerschnitt(final SystemObject mqObjekt){
		super(DAV, mqObjekt);

		if(mqObjekt == null){
			throw new NullPointerException("Übergebenes Messquerschnitt-Systemobjekt ist <<null>>"); //$NON-NLS-1$
		}

		ConfigurationObject konfigObjekt = (ConfigurationObject)mqObjekt;
		ObjectSet fsMenge = konfigObjekt.getNonMutableSet("FahrStreifen"); //$NON-NLS-1$
		for(SystemObject fsObj:fsMenge.getElements()){
			if(fsObj.isValid()){
				FahrStreifen fs = FahrStreifen.getInstanz(fsObj);
				if(fs != null){
					this.fahrStreifen.add(fs);
				}else{
					LOGGER.warning("Fahrstreifen " + fsObj + " an " + mqObjekt +   //$NON-NLS-1$//$NON-NLS-2$
							" konnte nicht identifiziert werden"); //$NON-NLS-1$
				}
			}
		}
	}

	
	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ <code>typ.messQuerschnitt</code>
	 * statische Instanzen dieser Klasse angelegt werden
	 * 
	 * @param dav Datenverteiler-Verbindung
	 */
	protected static void initialisiere(final ClientDavInterface dav){
		if(dav == null){
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>"); //$NON-NLS-1$
		}
		
		if(DAV != null){
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden"); //$NON-NLS-1$
		}		
		DAV = dav;
		
		for(SystemObject mqObjekt:DAV.getDataModel().getType(DUAKonstanten.TYP_MQ).getElements()){
			if(mqObjekt.isValid()){
				SYS_OBJ_MQ_OBJ_MAP.put(mqObjekt, new MessQuerschnitt(mqObjekt));				
			}
		}
	}
	
	
	/**
	 * Erfragt alle statischen Instanzen dieser Klasse
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessQuerschnitt> getInstanzen(){
		if(DAV == null){
			throw new RuntimeException("Messquerschnitt-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		return SYS_OBJ_MQ_OBJ_MAP.values();
	}


	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse 
	 * 
	 * @param mqObjekt ein Messquerschnitt-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse oder <code>null</code>, wenn diese Instanz nicht ermittelt werden
	 * konnte
	 */
	public static MessQuerschnitt getInstanz(final SystemObject mqObjekt){
		if(DAV == null){
			throw new RuntimeException("Messquerschnitt-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		MessQuerschnitt ergebnis = null;
		
		if(mqObjekt != null){
			ergebnis = SYS_OBJ_MQ_OBJ_MAP.get(mqObjekt);
		}
		
		return ergebnis;
	}
	
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public final List<FahrStreifen> getFahrStreifen(){
		return this.fahrStreifen;
	}
		
	
	/**
	 * Erfragt den Nachbarfahrstreifen des übergebenen Fahrstreifens<br>
	 * Der Nachbarfahrstreifen ist immer der in Fahrtrichtung linke Fahrstreifen
	 * bzw. der rechte, wenn es links keinen Fahrstreifen gibt.
	 * 
	 * @param fs ein Fahrstreifen
	 * @return der Nachbarfahrstreifen des übergebenen Fahrstreifens oder
	 * <code>null</code> wenn dieser Fahrstreifen keinen Nachbarfahrstreifen 
	 * hat
	 */
	protected final FahrStreifen getNachbarVon(FahrStreifen fs){
		FahrStreifen nachbar = null;
		
		FahrStreifenLage lageLinksVonHier = fs.getLage().getLinksVonHier();
		FahrStreifenLage lageRechtsVonHier = fs.getLage().getRechtsVonHier();
		
		FahrStreifen linkerNachbar = null;
		FahrStreifen rechterNachbar = null;
		
		for(FahrStreifen fs1:this.fahrStreifen){
			if(lageLinksVonHier != null){
				if(fs1.getLage().equals(lageLinksVonHier)){
					linkerNachbar = fs1;
					break;
				}
			}
			if(lageRechtsVonHier != null){
				if(fs1.getLage().equals(lageRechtsVonHier)){
					rechterNachbar = fs1;
				}				
			}
		}

		if(linkerNachbar != null){
			nachbar = linkerNachbar;
		}else{
			nachbar = rechterNachbar;
		}
		
		return nachbar;
	}	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp(){

			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnitt.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}
			
		};
	}
}
