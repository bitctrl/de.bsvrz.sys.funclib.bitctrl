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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messStellenGruppe</code>
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class MessStellenGruppe
extends AbstractSystemObjekt{

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Mapt alle MessStelleGruppe-Systemobjekte auf Objekte dieser Klasse  
	 */
	private static Map<SystemObject, MessStellenGruppe> SYS_OBJ_MSG_OBJ_MAP = 
											new HashMap<SystemObject, MessStellenGruppe>();
	
	/**
	 * Datenverteiler-Verbindung
	 */
	private static ClientDavInterface DAV = null;
	
	/**
	 * Messstellen dieser Gruppe
	 */
	private Collection<MessStelle> messStellen = new HashSet<MessStelle>();
	
	/**
	 * Legt fest, ob die Ermittlung systematischer Detektorfehler für diese MessStellenGruppe
	 * durchgeführt werden soll
	 */
	private boolean systematischeDetektorfehler = false;

	
	/**
	 * Standardkontruktor
	 * 
	 * @param msgObjekt ein Systemobjekt vom Typ <code>typ.messStellenGruppe</code>
	 * @throws DUAInitialisierungsException wenn die Messstellengruppe nicht 
	 * initialisiert werden konnte
	 */
	@SuppressWarnings("unused")
	private MessStellenGruppe(final SystemObject msgObjekt)
	throws DUAInitialisierungsException{
		super(msgObjekt);
		
		ConfigurationObject konfigObjekt = (ConfigurationObject)msgObjekt;

		AttributeGroup atgEigenschaften = DAV.getDataModel().getAttributeGroup(DUAKonstanten.ATG_MESS_STELLEN_GRUPPE);
		Data eigenschaften = msgObjekt.getConfigurationData(atgEigenschaften);
		
		if(eigenschaften == null){
			LOGGER.warning("Eigenschaften von Messstellengruppe " + msgObjekt + //$NON-NLS-1$
					" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		}else{
			if(eigenschaften.getReferenceArray("MessStellen") != null){ //$NON-NLS-1$
				for(int i = 0; i < eigenschaften.getReferenceArray("MessStellen").getLength(); i++){ //$NON-NLS-1$
					if(eigenschaften.getReferenceArray("MessStellen").getReferenceValue(i) != null && //$NON-NLS-1$
						eigenschaften.getReferenceArray("MessStellen").getReferenceValue(i).getSystemObject() != null){ //$NON-NLS-1$
							this.messStellen.add(
									MessStelle.getInstanz(
											eigenschaften.getReferenceArray("MessStellen"). //$NON-NLS-1$
											getReferenceValue(i).getSystemObject()));
					}
				}
			}

			this.systematischeDetektorfehler = 
				eigenschaften.getUnscaledValue("SystematischeDetektorfehler").intValue() == DUAKonstanten.JA; //$NON-NLS-1$
		}
	}

	
	/**
	 * Erfragt alle statischen Instanzen dieser Klasse
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public final static Collection<MessStellenGruppe> getInstanzen(){
		return SYS_OBJ_MSG_OBJ_MAP.values();
	}
	
	
	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ <code>typ.messStelleGruppe</code>
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
		
		for(SystemObject msObj:DAV.getDataModel().getType(DUAKonstanten.TYP_MESS_STELLEN_GRUPPE).getElements()){
			SYS_OBJ_MSG_OBJ_MAP.put(msObj, new MessStellenGruppe(msObj));
		}
	}
	

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse 
	 * 
	 * @param msgObjekt ein MessStellenGruppe-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische Instanz
	 * dieser Klasse oder <code>null</code>, wenn diese Instanz nicht ermittelt werden
	 * konnte
	 */
	public static MessStellenGruppe getInstanz(final SystemObject msgObjekt){
		if(DAV == null){
			throw new RuntimeException("MessStellen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		MessStellenGruppe ergebnis = null;
		
		if(msgObjekt != null){
			ergebnis = SYS_OBJ_MSG_OBJ_MAP.get(msgObjekt);
		}
		
		return ergebnis;
	}

	
	/**
	 * Erfragt die Menge der Messstellen dieser Messstellengruppe
	 * 
	 * @return ggf. leere Menge der Messstellen dieser Messstellengruppe
	 */
	public final Collection<MessStelle> getMessStellen(){	
		return this.messStellen;
	}
	
	
	/**
	 * Erfragt, ob die Ermittlung systematischer Detektorfehler für diese MessStellenGruppe
	 * durchgeführt werden soll
	 * 
	 * @return die Ermittlung systematischer Detektorfehler für diese MessStellenGruppe
	 * durchgeführt werden soll
	 */
	public final boolean isSystematischeDetektorfehler(){
		return this.systematischeDetektorfehler;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp(){

			public Class<? extends SystemObjekt> getKlasse() {
				return MessStellenGruppe.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}
			
		};
	}
	
}
