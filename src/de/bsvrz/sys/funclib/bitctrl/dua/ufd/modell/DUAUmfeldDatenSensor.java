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

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Korrespondiert mit einem Objekt vom Typ <code>typ.umfeldDatenSensor</code>
 * und stellt alle Konfigurationsdaten, sowie die Parameter der Messwertersetzung
 * zur Verf�gung
 *  
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class DUAUmfeldDatenSensor
implements ClientReceiverInterface{
	
	/**
	 * statische Instanzen dieser Klasse
	 */
	private static Map<SystemObject, DUAUmfeldDatenSensor> INSTANZEN = 
							new HashMap<SystemObject, DUAUmfeldDatenSensor>(); 
	
	/**
	 * das Systemobjekt
	 */
	private SystemObject objekt = null;
	
	/**
	 * Maximaler Zeitbereich, �ber den eine Messwertersetzung f�r diesen Sensor durchgef�hrt wird
	 */	
	private long maxZeitMessWertErsetzung = -1;

	/**
	 * Maximaler Zeitbereich, �ber den eine Messwertfortschreibung bei implausiblen Werten stattfindet
	 */
	/**
	 * 
	 * 
	 * TODO: wieder zuruecksetzten 
	 * 
	 * 
	 * 
	 */
//	private long maxZeitMessWertFortschreibung = -1;
	private long maxZeitMessWertFortschreibung = Konstante.SEKUNDE_IN_MS * 1;
	
	/**
	 * Die Umfelddatenmessstelle vorher
	 */
	private SystemObject vorgaenger = null;

	/**
	 * Die Umfelddatenmessstelle nachher
	 */
	private SystemObject nachfolger = null;

	/**
	 * Ersatzsensor dieses Umfelddatensensors f�r die Messwertersetzung
	 */
	private SystemObject ersatzSensor = null;

	/**
	 * Zeigt an, ob dieser Sensor der Hauptsensor f�r diesen Sensortyp an der Umfelddatenmessstelle,
	 * oder ein(er von mehreren) Nebensensoren f�r diesen Sensortyp an der Umfelddatenmessstelle
	 * ist
	 */
	private boolean hauptSensor = false;
	
	/**
	 * Die Umfelddatenart dieses Sensors
	 */
	private UmfeldDatenArt datenArt = null;
	
	
	/**
	 * Erfragt die statische Instanz dieser Klasse, die mit dem uebergebenen
	 * Systemobjekt assoziiert ist (nicht vorhandene werden ggf. angelegt)
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param objekt ein Systemobjekt eines Umfelddatensensors
	 * @return die statische Instanz dieser Klasse, die mit dem uebergebenen
	 * Systemobjekt assoziiert ist
	 */
	static final DUAUmfeldDatenSensor getInstanz(final ClientDavInterface dav,
												 final SystemObject objekt){
		if(objekt == null){
			throw new NullPointerException("Umfelddatensensor mit Systemobjekt <<null>> existiert nicht"); //$NON-NLS-1$
		}
		
		DUAUmfeldDatenSensor instanz = INSTANZEN.get(objekt);
		
		if(instanz == null){
			instanz = new DUAUmfeldDatenSensor(dav, objekt);
			INSTANZEN.put(objekt, instanz);
		}
		
		return instanz;
	}
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param dav Datenverteiler-Verbindung
	 * @param objekt das Systemobjekt des Umfelddatensensors
	 */
	private DUAUmfeldDatenSensor(final ClientDavInterface dav, final SystemObject objekt){
		if(this.objekt != null){
			throw new NullPointerException("Als Umfelddatensensor wurde <<null>> uebergeben"); //$NON-NLS-1$
		}
		this.objekt = objekt;
		
		this.datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);
		if(this.datenArt == null) {
			throw new RuntimeException("Datenart von Umfelddatensensor " + this.objekt + //$NON-NLS-1$ 
					" konnte nicht identifiziert werden"); //$NON-NLS-1$
		}
		
		ConfigurationObject konfigObjekt = (ConfigurationObject)objekt;
		Data konfigDaten = konfigObjekt.getConfigurationData(
					dav.getDataModel().getAttributeGroup("atg.umfeldDatenSensor")); //$NON-NLS-1$
	
		if(konfigDaten != null){
			if(konfigDaten.getReferenceValue("Vorg�nger") != null){ //$NON-NLS-1$
				this.vorgaenger = konfigDaten.getReferenceValue("Vorg�nger").getSystemObject(); //$NON-NLS-1$	
			}			
			if(konfigDaten.getReferenceValue("Nachfolger") != null){ //$NON-NLS-1$
				this.nachfolger = konfigDaten.getReferenceValue("Nachfolger").getSystemObject(); //$NON-NLS-1$	
			}			
			
			if(konfigDaten.getReferenceValue("ErsatzSensor") != null){ //$NON-NLS-1$
				this.ersatzSensor = konfigDaten.getReferenceValue("ErsatzSensor").getSystemObject(); //$NON-NLS-1$	
			}			
			this.hauptSensor = konfigDaten.getUnscaledValue("Typ").intValue() == 0; //$NON-NLS-1$
		}
		
		DataDescription parameterBeschreibung = new DataDescription(
				dav.getDataModel().getAttributeGroup("atg.ufdsMessWertErsetzung"), //$NON-NLS-1$
				dav.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL),
				(short)0);
		dav.subscribeReceiver(this, objekt, parameterBeschreibung, ReceiveOptions.normal(), ReceiverRole.receiver());
	}
	
	
	/**
	 * Erfragt die Umfelddatenart dieses Sensors
	 * 
	 * @return die Umfelddatenart dieses Sensors
	 */
	public final UmfeldDatenArt getDatenArt(){
		return this.datenArt;
	}
	

	/**
	 * Erfragt die Umfelddatenmessstelle vorher
	 * 
	 * @return die Umfelddatenmessstelle vorher oder <code>null</code>,
	 * wenn diese nicht konfiguriert ist
	 */
	public final SystemObject getVorgaenger() {
		return vorgaenger;
	}


	/**
	 * Ergagt die Umfelddatenmessstelle nachher
	 * 
	 * @return die Umfelddatenmessstelle nachher oder <code>null</code>,
	 * wenn diese nicht konfiguriert ist
	 */
	public final SystemObject getNachfolger() {
		return nachfolger;
	}


	/**
	 * Erfragt den Ersatzsensor dieses Umfelddatensensors f�r die Messwertersetzung
	 * 
	 * @return der Ersatzsensor dieses Umfelddatensensors f�r die Messwertersetzung
	 * oder <code>null</code>, wenn dieser nicht konfiguriert ist
	 */
	public final SystemObject getErsatzSensor() {
		return ersatzSensor;
	}


	/**
	 * Erfragt, ob dieser Sensor der Hauptsensor f�r diesen Sensortyp an der Umfelddatenmessstelle,
	 * oder ein(er von mehreren) Nebensensoren f�r diesen Sensortyp an der Umfelddatenmessstelle
	 * ist
	 * 
	 * @return ob dieser Sensor der Hauptsensor ist
	 */
	public final boolean isHauptSensor() {
		return hauptSensor;
	}


	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] resultate) {
		if(resultate != null){
			for(ResultData resultat:resultate){
				if(resultat != null && resultat.getData() != null){
					Data ufdsMessWertErsetzungData = resultat.getData();
					this.maxZeitMessWertErsetzung = 
						ufdsMessWertErsetzungData.getTimeValue("maxZeitMessWertErsetzung").getMillis(); //$NON-NLS-1$
					
					/**
					 * 
					 * 
					 * 
					 * TODO: Fortschreibeung auslesen
					 * 
					 * 
					 * 
					 */
//					this.maxZeitMessWertFortschreibung = 
//						ufdsMessWertErsetzungData.getTimeValue("maxZeitMessWertFortschreibung").getMillis(); //$NON-NLS-1$
				}
			}
		}
	}
	
	
	/**
	 * Erfragt den maximalen Zeitbereich, �ber den eine Messwertersetzung f�r diesen
	 * Sensor durchgef�hrt wird
	 * 
	 * @return maximaler Zeitbereich, �ber den eine Messwertersetzung f�r diesen Sensor
	 * durchgef�hrt wird
	 */	
	public final long getMaxZeitMessWertErsetzung() {
		return this.maxZeitMessWertErsetzung;
	}


	/**
	 * Erfragt den maximalen Zeitbereich, �ber den eine Messwertfortschreibung bei
	 * implausiblen Werten stattfindet
	 * 
	 * @return maximaler Zeitbereich, �ber den eine Messwertfortschreibung bei implausiblen
	 * Werten stattfindet
	 */
	public final long getMaxZeitMessWertFortschreibung() {
		return this.maxZeitMessWertFortschreibung;
	}


	/**
	 * Erfragt das Systemobjekt
	 * 
	 * @return das Systemobjekt
	 */
	public final SystemObject getObjekt(){
		return this.objekt;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean ergebnis = false;
		
		if(obj != null && obj instanceof DUAUmfeldDatenSensor){
			DUAUmfeldDatenSensor that = (DUAUmfeldDatenSensor)obj;
			ergebnis = this.objekt.equals(that.objekt);
		}
			
		return ergebnis;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.objekt.toString();
	}

}
