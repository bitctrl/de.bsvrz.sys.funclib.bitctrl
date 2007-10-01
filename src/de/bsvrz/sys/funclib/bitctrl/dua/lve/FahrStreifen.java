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
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.FahrStreifenLage;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.fahrStreifen</code>
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class FahrStreifen
extends AbstractSystemObjekt{
	
	/**
	 * Mapt alle Fahrstreifen-Systemobjekte auf Objekte der Klasse <code>FahrStreifen</code>  
	 */
	private static Map<SystemObject, FahrStreifen> SYS_OBJ_FS_OBJ_MAP = new HashMap<SystemObject, FahrStreifen>();
	
	/**
	 * Datenverteiler-Verbindung
	 */
	private static ClientDavInterface DAV = null;
			
	/**
	 * die Lage dieses Fahrtreifens
	 */
	private FahrStreifenLage lage = null;
	
	/**
	 * Systemobjekt des Ersatzfahrstreifens dieses Fahrstreifens
	 */
	private SystemObject ersatzFahrstreifenObj = null;
	
	/**
	 * Systemobjekt des Nachbarfahrstreifens dieses Fahrstreifens
	 */
	private SystemObject nachbarFahrstreifenObj = null;

	
	
	/**
	 * Standardkontruktor
	 * 
	 * @param fsObjekt ein Systemobjekt vom Typ <code>typ.fahrStreifen</code>
	 * @throws DUAInitialisierungsException wenn der Fahrstreifen nicht 
	 * initialisiert werden konnte
	 */
	private FahrStreifen(final SystemObject fsObjekt)
	throws DUAInitialisierungsException{
		super(fsObjekt);

		if(fsObjekt == null){
			throw new NullPointerException("Übergebenes Fahrstreifenobjekt ist <<null>>"); //$NON-NLS-1$
		}
		
		AttributeGroup atgEigenschaften = DAV.getDataModel().getAttributeGroup(DUAKonstanten.ATG_FAHRSTREIFEN);
		Data eigenschaften = fsObjekt.getConfigurationData(atgEigenschaften);
		
		if(eigenschaften == null){
			throw new DUAInitialisierungsException("Eigenschaften von Fahrstreifenobjekt " + fsObjekt + //$NON-NLS-1$
					" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		}
		
		this.lage = FahrStreifenLage.getZustand(eigenschaften.getUnscaledValue("Lage").intValue()); //$NON-NLS-1$
		if(eigenschaften.getReferenceValue("ErsatzFahrStreifen") != null){ //$NON-NLS-1$
			this.ersatzFahrstreifenObj = eigenschaften.getReferenceValue("ErsatzFahrStreifen").getSystemObject(); //$NON-NLS-1$
		}
	}

	
	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ <code>typ.fahrStreifen</code>
	 * statische Instanzen dieser Klasse angelegt werden
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException wenn eines der Objekte nicht 
	 * initialisiert werden konnte
	 */
	protected static final void initialisiere(final ClientDavInterface dav)
	throws DUAInitialisierungsException{
		if(dav == null){
			throw new NullPointerException("Datenverteiler-Verbindung ist <<null>>"); //$NON-NLS-1$
		}
		
		if(DAV != null){
			throw new RuntimeException("Objekt darf nur einmal initialisiert werden"); //$NON-NLS-1$
		}		
		DAV = dav;
		 
		for(SystemObject fsObjekt:DAV.getDataModel().getType(DUAKonstanten.TYP_FAHRSTREIFEN).getElements()){
			SYS_OBJ_FS_OBJ_MAP.put(fsObjekt, new FahrStreifen(fsObjekt));
		}
	}
	
	
	/**
	 * Erfragt alle statischen Instanzen dieser Klasse
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<FahrStreifen> getInstanzen(){
		if(DAV == null){
			throw new RuntimeException("FahrStreifen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		return SYS_OBJ_FS_OBJ_MAP.values();
	}
	

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse 
	 * 
	 * @param fsObjekt ein Fahrstreifen-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse oder <code>null</code>, wenn diese Instanz nicht ermittelt werden
	 * konnte
	 */
	public static final FahrStreifen getInstanz(final SystemObject fsObjekt){
		if(DAV == null){
			throw new RuntimeException("Fahrstreifen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		FahrStreifen ergebnis = null;
		
		if(fsObjekt != null){
			ergebnis = SYS_OBJ_FS_OBJ_MAP.get(fsObjekt);
		}
		
		return ergebnis; 
	}
	

	/**
	 * Erfragt die Lage dieses Fahrtreifens innerhalb eines Messquerschnitts
	 * 
	 * @return die Lage dieses Fahrtreifens innerhalb eines Messquerschnitts
	 */
	public FahrStreifenLage getLage(){
		return this.lage;
	}
	

	/**
	 * Erfragt den Ersatzfahrstreifen dieses Fahrstreifens
	 * 
	 * @return den Ersatzfahrstreifen dieses Fahrstreifens oder <code>null</code>,
	 * wenn dieser nicht ermittelt werden konnte
	 */
	public final FahrStreifen getErsatzFahrStreifen(){
		return FahrStreifen.getInstanz(this.ersatzFahrstreifenObj);			
	}
	
	
	/**
	 * Setzt den Ersatzfahrstreifen dieses Fahrstreifens
	 * 
	 * @param ersatzFahrstreifenObj den Ersatzfahrstreifen dieses Fahrstreifens
	 */
	protected final void setErsatzFahrStreifen(final SystemObject ersatzFahrstreifenObj){
		this.ersatzFahrstreifenObj = ersatzFahrstreifenObj;
	}
	
	
	/**
	 * Erfragt den Nachbarfahrstreifen dieses Fahrstreifens
	 * 
	 * @return den Nachbarfahrstreifen dieses Fahrstreifens oder <code>null</code>,
	 * wenn dieser Fahrstreifen keinen Nachbarfahrstreifen hat
	 */
	public final FahrStreifen getNachbarFahrStreifen(){
		return FahrStreifen.getInstanz(this.nachbarFahrstreifenObj);
	}
	
	
	/**
	 * Setzt den Nachbarfahrstreifen dieses Fahrstreifens
	 * 
	 * @param nachbarFahrstreifenObj den Nachbarfahrstreifen dieses Fahrstreifens
	 */
	protected final void setNachbarFahrStreifen(final SystemObject nachbarFahrstreifenObj){
		this.nachbarFahrstreifenObj = nachbarFahrstreifenObj;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp(){

			public Class<? extends SystemObjekt> getKlasse() {
				return FahrStreifen.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}
			
		};
	}

}
