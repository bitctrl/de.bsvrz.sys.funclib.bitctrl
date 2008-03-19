/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x 
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */
package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messStelle</code>
 * TODO: Parameter auslesen
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class MessStelle 
extends AbstractSystemObjekt{
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Mapt alle MessStelle-Systemobjekte auf Objekte dieser Klasse  
	 */
	protected static Map<SystemObject, MessStelle> SYS_OBJ_MS_OBJ_MAP = 
											new HashMap<SystemObject, MessStelle>();
	
	/**
	 * Datenverteiler-Verbindung
	 */
	protected static ClientDavInterface DAV = null;
	
	/**
	 * Zufahrten zu dieser Messstelle
	 */
	private Collection<MessQuerschnittAllgemein> zufahrten = new HashSet<MessQuerschnittAllgemein>();

	/**
	 * Abfahrten von dieser Messstelle
	 */
	private Collection<MessQuerschnittAllgemein> abfahrten = new HashSet<MessQuerschnittAllgemein>();
	
	/**
	 * Referenz auf den MessQuerschnitt, der zu pr�fen ist
	 */
	private SystemObject pruefling = null;
	

	
	/**
	 * Standardkontruktor
	 * 
	 * @param msObjekt ein Systemobjekt vom Typ <code>typ.messStelle</code>
	 * @throws DUAInitialisierungsException wenn die Messstelle nicht 
	 * initialisiert werden konnte
	 */
	@SuppressWarnings("unused")
	protected MessStelle(final SystemObject msObjekt)
	throws DUAInitialisierungsException{
		super(msObjekt);
		
		ConfigurationObject konfigObjekt = (ConfigurationObject)msObjekt;
		ObjectSet mqMengeAbfahrten = konfigObjekt.getNonMutableSet("Abfahrten"); //$NON-NLS-1$
		for(SystemObject mqObj:mqMengeAbfahrten.getElements()){
			if(mqObj.isValid()){
				MessQuerschnittAllgemein mqa = MessQuerschnittAllgemein.getInstanz(mqObj);
				if(mqa != null){
					this.abfahrten.add(mqa);
				}else{
					LOGGER.warning("Abfahrt " + mqObj + " an " + msObjekt +   //$NON-NLS-1$//$NON-NLS-2$
							" konnte nicht identifiziert werden"); //$NON-NLS-1$
				}
			}
		}
		
		ObjectSet mqMengeZufahrten = konfigObjekt.getNonMutableSet("Zufahrten"); //$NON-NLS-1$
		for(SystemObject mqObj:mqMengeZufahrten.getElements()){
			if(mqObj.isValid()){
				MessQuerschnittAllgemein mqa = MessQuerschnittAllgemein.getInstanz(mqObj);
				if(mqa != null){
					this.zufahrten.add(mqa);
				}else{
					LOGGER.warning("Zufahrt " + mqObj + " an " + msObjekt +   //$NON-NLS-1$//$NON-NLS-2$
							" konnte nicht identifiziert werden"); //$NON-NLS-1$
				}
			}
		}
		

		AttributeGroup atgEigenschaften = DAV.getDataModel().getAttributeGroup(DUAKonstanten.ATG_MESS_STELLE);
		Data eigenschaften = msObjekt.getConfigurationData(atgEigenschaften);
		
		if(eigenschaften == null){
			LOGGER.warning("Eigenschaften von Messstelle " + msObjekt + //$NON-NLS-1$
					" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		}else{
			if(eigenschaften.getReferenceValue("Pr�fling") != null){ //$NON-NLS-1$
				this.pruefling = eigenschaften.getReferenceValue("Pr�fling").getSystemObject(); //$NON-NLS-1$
			}
		}
	}

	
	/**
	 * Initialisiert diese Klasse, indem f�r alle Systemobjekte vom Typ <code>typ.messStelle</code>
	 * statische Instanzen dieser Klasse angelegt werden
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException wenn eines der Objekte nicht 
	 * initialisiert werden konnte
	 */
	protected static void initialisiere(final ClientDavInterface dav)
	throws DUAInitialisierungsException{
		if(dav == null){
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>"); //$NON-NLS-1$
		}
		
		if(DAV != null){
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden"); //$NON-NLS-1$
		}		
		DAV = dav;
		
		for(SystemObject msObj:DAV.getDataModel().getType(DUAKonstanten.TYP_MESS_STELLE).getElements()){
			if(msObj.isValid()){
				SYS_OBJ_MS_OBJ_MAP.put(msObj, new MessStelle(msObj));
			}
		}
	}
	

	/**
	 * Erfragt eine mit dem �bergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse 
	 * 
	 * @param msObjekt ein MessStellen-Systemobjekt
	 * @return eine mit dem �bergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse oder <code>null</code>, wenn diese Instanz nicht ermittelt werden
	 * konnte
	 */
	public static MessStelle getInstanz(final SystemObject msObjekt){
		if(DAV == null){
			throw new RuntimeException("MessStellen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		MessStelle ergebnis = null;
		
		if(msObjekt != null){
			ergebnis = SYS_OBJ_MS_OBJ_MAP.get(msObjekt);
		}
		
		return ergebnis;
	}
	

	/**
	 * Erfragt die Menge der Messquerschnitte der Zufahrten dieser Messstelle
	 * 
	 * @return ggf. leere Menge der Messquerschnitte der Zufahrten
	 * dieser Messstelle
	 */
	public final Collection<MessQuerschnittAllgemein> getZufahrten(){	
		return this.zufahrten;
	}
	
	
	/**
	 * Erfragt die Menge der Messquerschnitte der Abfahrten dieser Messstelle
	 * 
	 * @return ggf. leere Menge der Messquerschnitte der Abfahrten
	 * dieser Messstelle
	 */
	public final Collection<MessQuerschnittAllgemein> getAbfahrten(){	
		return this.abfahrten;
	}
	
	
	/**
	 * Erfragt Referenz auf den MessQuerschnitt, der zu pr�fen ist 
	 * 
	 * @return Referenz auf den MessQuerschnitt, der zu pr�fen ist
	 */
	public final MessQuerschnittAllgemein getPruefling(){
		return MessQuerschnittAllgemein.getInstanz(this.pruefling);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp(){

			public Class<? extends SystemObjekt> getKlasse() {
				return MessStelle.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}
			
		};
	}
}
