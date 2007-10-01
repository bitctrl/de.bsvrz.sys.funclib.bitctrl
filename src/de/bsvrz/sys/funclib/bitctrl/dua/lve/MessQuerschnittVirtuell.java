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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnittVirtuell</code>
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class MessQuerschnittVirtuell
extends MessQuerschnittAllgemein{
	
	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Mapt alle MessQuerschnittVirtuell-Systemobjekte auf Objekte dieser Klasse  
	 */
	private static Map<SystemObject, MessQuerschnittVirtuell> SYS_OBJ_MQV_OBJ_MAP = 
											new HashMap<SystemObject, MessQuerschnittVirtuell>();
	
	/**
	 * Datenverteiler-Verbindung
	 */
	private static ClientDavInterface DAV = null;
	
	/**
	 * Messquerschnitt VOR der Anschlussstelle
	 */
	private MessQuerschnitt mqVorObj;
	
	/**
	 * Messquerschnitt NACH der Anschlussstelle
	 */
	private MessQuerschnitt mqNachObj;
	
	/**
	 * Messquerschnitt MITTE der Anschlussstelle
	 */
	private MessQuerschnitt mqMitteObj;
	
	/**
	 * Messquerschnitt AUSFAHRT der Anschlussstelle
	 */
	private MessQuerschnitt mqAusfahrtObj;
	
	/**
	 * Messquerschnitt EINFAHRT der Anschlussstelle
	 */
	private MessQuerschnitt mqEinfahrtObj;
				
	/**
	 * Menge der an diesem virtuellen Messquerschnitt
	 * zusammengefassten Messquerschnitte
	 */
	private Set<MQBestandteil> mqBestandteile = new HashSet<MQBestandteil>();

	
	/**
	 * Standardkontruktor
	 * 
	 * @param mqvObjekt ein Systemobjekt vom Typ <code>typ.messQuerschnittVirtuell</code>
	 * @throws DUAInitialisierungsException wenn der virtuelle Messquerschnitt nicht 
	 * initialisiert werden konnte
	 */
	private MessQuerschnittVirtuell(final SystemObject mqvObjekt)
	throws DUAInitialisierungsException{
		super(DAV, mqvObjekt);

		if(mqvObjekt == null){
			throw new NullPointerException("Übergebenes MessQuerschnittVirtuell-Systemobjekt ist <<null>>"); //$NON-NLS-1$
		}

		AttributeGroup atgEigenschaftenSTD = DAV.getDataModel().getAttributeGroup(DUAKonstanten.ATG_MQ_VIRTUELL_STANDARD);
		Data eigenschaftenSTD = mqvObjekt.getConfigurationData(atgEigenschaftenSTD);
		
		if(eigenschaftenSTD == null){
			LOGGER.warning("Standardeigenschaften von MessQuerschnittVirtuell-Objekt " + mqvObjekt + //$NON-NLS-1$
					" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		}else{	
			if(eigenschaftenSTD.getReferenceValue("MessQuerschnittVor") != null){ //$NON-NLS-1$
				this.mqVorObj = MessQuerschnitt.getInstanz(eigenschaftenSTD.
						getReferenceValue("MessQuerschnittVor").getSystemObject()); //$NON-NLS-1$
			}
			if(eigenschaftenSTD.getReferenceValue("MessQuerschnittNach") != null){ //$NON-NLS-1$
				this.mqNachObj = MessQuerschnitt.getInstanz(eigenschaftenSTD.
						getReferenceValue("MessQuerschnittNach").getSystemObject()); //$NON-NLS-1$
			}
			if(eigenschaftenSTD.getReferenceValue("MessQuerschnittMitte") != null){ //$NON-NLS-1$
				this.mqMitteObj = MessQuerschnitt.getInstanz(eigenschaftenSTD.
						getReferenceValue("MessQuerschnittMitte").getSystemObject()); //$NON-NLS-1$
			}
			if(eigenschaftenSTD.getReferenceValue("MessQuerschnittAusfahrt") != null){ //$NON-NLS-1$
				this.mqAusfahrtObj = MessQuerschnitt.getInstanz(eigenschaftenSTD.
						getReferenceValue("MessQuerschnittAusfahrt").getSystemObject()); //$NON-NLS-1$
			}
			if(eigenschaftenSTD.getReferenceValue("MessQuerschnittEinfahrt") != null){ //$NON-NLS-1$
				this.mqEinfahrtObj = MessQuerschnitt.getInstanz(eigenschaftenSTD.
						getReferenceValue("MessQuerschnittEinfahrt").getSystemObject()); //$NON-NLS-1$
			}
		}		
		
		AttributeGroup atgEigenschaften = DAV.getDataModel().getAttributeGroup(DUAKonstanten.ATG_MQ_VIRTUELL);
		Data eigenschaften = mqvObjekt.getConfigurationData(atgEigenschaften);
		
		if(eigenschaften == null){
			throw new DUAInitialisierungsException("Eigenschaften von MessQuerschnittVirtuell-Objekt " + mqvObjekt + //$NON-NLS-1$
					" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		}
		
		Array mqBestandTeile = eigenschaften.getArray("MessQuerSchnittBestandTeile"); //$NON-NLS-1$
		for(int i = 0; i<mqBestandTeile.getLength(); i++){
			if(mqBestandTeile.getItem(i).getReferenceValue("MessQuerschnittReferenz") != null){ //$NON-NLS-1$
				SystemObject mqObj = mqBestandTeile.getItem(i).getReferenceValue(
									 "MessQuerschnittReferenz").getSystemObject(); //$NON-NLS-1$
				
				if(mqObj != null){
					if(MessQuerschnitt.getInstanz(mqObj) != null){
						MQBestandteil bestandTeil = new MQBestandteil(
								MessQuerschnitt.getInstanz(mqObj), 
								mqBestandTeile.getItem(i).getScaledValue("Anteil").doubleValue()); //$NON-NLS-1$
						this.mqBestandteile.add(bestandTeil);
					}else{
						throw new DUAInitialisierungsException("Messquerschnitt " + mqObj + //$NON-NLS-1$
								" an " + mqvObjekt + " konnte nicht identifiziert werden"); //$NON-NLS-1$ //$NON-NLS-2$						
					}
				}
			}
		}		
	}

	
	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ <code>typ.messQuerschnittVirtuell</code>
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
		
		for(SystemObject mqvObjekt:DAV.getDataModel().getType(DUAKonstanten.TYP_MQ_VIRTUELL).getElements()){
			SYS_OBJ_MQV_OBJ_MAP.put(mqvObjekt, new MessQuerschnittVirtuell(mqvObjekt));
		}
	}
	
	
	/**
	 * Erfragt alle statischen Instanzen dieser Klasse
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessQuerschnittVirtuell> getInstanzen(){
		if(DAV == null){
			throw new RuntimeException("MessQuerschnittVirtuell-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		return SYS_OBJ_MQV_OBJ_MAP.values();
	}

	
	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse 
	 * 
	 * @param mqvObjekt ein MessQuerschnittVirtuell-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse oder <code>null</code>, wenn diese Instanz nicht ermittelt werden
	 * konnte
	 */
	public static MessQuerschnittVirtuell getInstanz(final SystemObject mqvObjekt){
		if(DAV == null){
			throw new RuntimeException("MessQuerschnittVirtuell-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		MessQuerschnittVirtuell ergebnis = null;
		
		if(mqvObjekt != null){
			ergebnis = SYS_OBJ_MQV_OBJ_MAP.get(mqvObjekt);
		}
		
		return ergebnis;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<FahrStreifen> getFahrStreifen() {
		Collection<FahrStreifen> fahrStreifenMenge = new HashSet<FahrStreifen>();
		
		for(MQBestandteil mqBestandteil:this.getMQBestandteile()){
			if(mqBestandteil.getMq() != null){
				for(FahrStreifen fs:mqBestandteil.getMq().getFahrStreifen()){
					fahrStreifenMenge.add(fs);
				}
			}
		}
		
		return fahrStreifenMenge;
	}

	
	/**
	 * Erfragt Messquerschnitt VOR der Anschlussstelle
	 * 
	 * @return Messquerschnitt VOR der Anschlussstelle
	 */
	public final MessQuerschnitt getMQVor(){
		return this.mqVorObj;
	}

	
	/**
	 * Erfragt Messquerschnitt NACH der Anschlussstelle
	 * 
	 * @return Messquerschnitt NACH der Anschlussstelle
	 */
	public final MessQuerschnitt getMQNach(){
		return this.mqNachObj;
	}

	
	/**
	 * Erfragt Messquerschnitt MITTE der Anschlussstelle
	 * 
	 * @return Messquerschnitt MITTE der Anschlussstelle
	 */
	public final MessQuerschnitt getMQMitte(){
		return this.mqMitteObj;
	}

	
	/**
	 * Erfragt Messquerschnitt AUSFAHRT der Anschlussstelle
	 * 
	 * @return Messquerschnitt AUSFAHRT der Anschlussstelle
	 */
	public final MessQuerschnitt getMQAusfahrt(){
		return this.mqAusfahrtObj;
	}

	
	/**
	 * Erfragt Messquerschnitt EINFAHRT der Anschlussstelle
	 * 
	 * @return Messquerschnitt EINFAHRT der Anschlussstelle
	 */
	public final MessQuerschnitt getMQEinfahrt(){
		return this.mqEinfahrtObj;
	}
	
	
	/**
	 * Erfragt die Menge der an diesem virtuellen Messquerschnitt
	 * zusammengefassten Messquerschnitte
	 * 
	 * @return ggf. leere Menge der an diesem virtuellen Messquerschnitt
	 * zusammengefassten Messquerschnitte
	 */
	public final Collection<MQBestandteil> getMQBestandteile(){	
		return this.mqBestandteile;
	}
		
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp(){

			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnittVirtuell.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}
			
		};
	}
}
