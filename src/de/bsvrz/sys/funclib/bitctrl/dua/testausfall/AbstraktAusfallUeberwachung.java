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

package de.bsvrz.sys.funclib.bitctrl.dua.testausfall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.KontrollProzess;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IKontrollProzessListener;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Abstrakte Ausfallüberwachung für zyklisch gesendete Daten
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktAusfallUeberwachung
extends AbstraktBearbeitungsKnotenAdapter 
implements IKontrollProzessListener<Long>{
	
	/**
	 * Debug-Logger
	 */
	protected static final Debug LOGGER = Debug.getLogger();

	/**
	 * Mapt alle betrachteten Systemobjekte auf den aktuell für sie
	 * erlaubten maximalen Zeitverzug
	 */
	protected Map<SystemObject, Long> objektWertErfassungVerzug =
					Collections.synchronizedMap(new TreeMap<SystemObject, Long>());	

	/**
	 * Eine Map mit allen aktuellen Kontrollzeitpunkten und den zu diesen
	 * Kontrollzeitpunkten zu überprüfenden Systemobjekten
	 */
	private SortedMap<Long, Collection<ObjektResultat>> kontrollZeitpunkte =
					Collections.synchronizedSortedMap(new TreeMap<Long, Collection<ObjektResultat>>());
	
	/**
	 * interner Kontrollprozess
	 */
	private KontrollProzess<Long> kontrollProzess = null;
	
	/**
	 * speichert pro Systemobjekt die letzte empfangene Datenzeit
	 */
	private Map<SystemObject, Long> letzteEmpfangeneDatenZeitProObj = new HashMap<SystemObject, Long>(); 	
	
		
	/**
	 * Erfragt die Intervalllänge T eines Datums
	 * 
	 * @param resultat ein Datum
	 * @return die im übergebenen Datum enthaltene Intervalllänge T
	 */
	protected abstract long getTVon(final ResultData resultat);
	
	
	/**
	 * Erfragt das ausgefallene Datum, dass sich aus dem übergebenen Datum
	 * ergibt
	 * 
	 * @param originalResultat ein Datum
	 * @return das ausgefallene Datum, dass sich aus dem übergebenen Datum
	 * ergibt
	 */
	protected abstract ResultData getAusfallDatumVon(final ResultData originalResultat);
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialisiere(IVerwaltung dieVerwaltung)
	throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		this.kontrollProzess = new KontrollProzess<Long>();
		this.kontrollProzess.addListener(this);
				
		for(SystemObject objekt:verwaltung.getSystemObjekte()){
			this.letzteEmpfangeneDatenZeitProObj.put(objekt, new Long(-1));
		}
	}


	/**
	 * {@inheritDoc}
	 */
	public synchronized void aktualisiereDaten(ResultData[] resultate) {
		if(resultate != null){
			List<ResultData> weiterzuleitendeResultate = new ArrayList<ResultData>();
			
			for(ResultData resultat:resultate){
				if(resultat != null){
					
					/**
					 * Hier werden die Daten herausgefiltert, die von der Ausfallkontrolle
					 * quasi zu unrecht generiert wurden, da das Datum nur minimal zu spät kam.
					 */
					if(this.letzteEmpfangeneDatenZeitProObj.get(resultat.getObject()) < resultat.getDataTime()){
						/**
						 * Zeitstempel ist echt neu!
						 */
						weiterzuleitendeResultate.add(resultat);
					}
					this.letzteEmpfangeneDatenZeitProObj.put(resultat.getObject(), resultat.getDataTime());
					
					
					if(resultat.getData() != null){
											
						this.bereinigeKontrollZeitpunkte(resultat);
						
						long kontrollZeitpunkt = this.getKontrollZeitpunktVon(resultat);
						if(kontrollZeitpunkt > 0){
							Collection<ObjektResultat> kontrollObjekte =
									this.kontrollZeitpunkte.get(kontrollZeitpunkt);
								
							/**
							 * Kontrolldatum bestimmten
							 */
							ObjektResultat neuesKontrollObjekt = new ObjektResultat(resultat);							
							if(kontrollObjekte != null){
								kontrollObjekte.add(neuesKontrollObjekt);
							}else{
								kontrollObjekte = new TreeSet<ObjektResultat>();
								kontrollObjekte.add(neuesKontrollObjekt);
								this.kontrollZeitpunkte.put(new Long(kontrollZeitpunkt), kontrollObjekte);
							}
						}
	
						long fruehesterKontrollZeitpunkt = -1;
												
						if(!this.kontrollZeitpunkte.isEmpty()){
							fruehesterKontrollZeitpunkt = this.kontrollZeitpunkte.firstKey().longValue();
							
							if(fruehesterKontrollZeitpunkt > 0){
								this.kontrollProzess.setNaechstenAufrufZeitpunkt(fruehesterKontrollZeitpunkt,
																				 new Long(fruehesterKontrollZeitpunkt));
							}else{
								LOGGER.warning("Der momentan aktuellste Kontrollzeitpunkt ist <= 0"); //$NON-NLS-1$
							}
						}else{
							LOGGER.warning("Die Menge der Kontrollzeitpunkte ist leer"); //$NON-NLS-1$
						}
					}
				}
			}
			
			if(this.knoten != null && !weiterzuleitendeResultate.isEmpty()){
				this.knoten.aktualisiereDaten(weiterzuleitendeResultate.toArray(new ResultData[0]));
			}
		}
	}
	
	
	/**
	 * Erfragt den maximalen Zeitverzug für ein Systemobjekt
	 * 
	 * @param obj ein Systemobjekt
	 * @return der maximale Zeitverzug für das Systemobjekt oder
	 * -1, wenn dieser nicht ermittelt werden konnte
	 */
	protected long getMaxZeitVerzug(SystemObject obj) {
		long maxZeitVerzug = -1;

		if(obj != null){
			synchronized (this.objektWertErfassungVerzug) {
				Long dummy = this.objektWertErfassungVerzug.get(obj);
				if(dummy != null && dummy > 0){
					maxZeitVerzug = dummy;
				}
			}
		}

		return maxZeitVerzug;
	}

	
	/**
	 * Bereinigt die Liste der Kontrollzeitpunkte anhand des gerade eingetroffenen
	 * Datums. Dabei wird zunächst der momentan noch erwartete Kontrollzeitpunkt dieses
	 * Datums berechnet und dieser dann aus der Liste der Kontrollzeitpunkte entfernt
	 * 
	 * @param resultat ein Roh-Datum eines Systemobjekts
	 */
	@SuppressWarnings("null")
	private final void bereinigeKontrollZeitpunkte(final ResultData resultat){
		
		/**
		 * Berechne den wahrscheinlichsten Zeitpunkt, für den hier noch auf 
		 * ein Datum dieses Objektes gewartet wird 
		 */
		final Long letzterErwarteterZeitpunkt = resultat.getDataTime() + 
												this.getTVon(resultat) +
												this.getMaxZeitVerzug(resultat.getObject());
		
		Collection<ObjektResultat> kontrollObjekte = 
			this.kontrollZeitpunkte.get(letzterErwarteterZeitpunkt);
		
		ObjektResultat datum = new ObjektResultat(resultat);

		/**
		 * Gibt es einen Kontrollzeitpunkt, für den das Objekt, des empfangenen Datums
		 * eingeplant sein müsste
		 */
		if(kontrollObjekte != null){
			if(kontrollObjekte.remove(datum)){
				if(kontrollObjekte.isEmpty()){
					this.kontrollZeitpunkte.remove(letzterErwarteterZeitpunkt);
				}
			}else{
				kontrollObjekte = null;
			}
		}

		if(kontrollObjekte == null){
			Long gefundenInKontrollZeitpunkt = new Long(-1);
			for(Long kontrollZeitpunkt:this.kontrollZeitpunkte.keySet()){
				kontrollObjekte = this.kontrollZeitpunkte.get(kontrollZeitpunkt);
				if(kontrollObjekte.remove(datum)){
					gefundenInKontrollZeitpunkt = kontrollZeitpunkt;
					break;
				}
			}

			if(gefundenInKontrollZeitpunkt >= 0){
				if(kontrollObjekte.isEmpty()){
					this.kontrollZeitpunkte.remove(gefundenInKontrollZeitpunkt);
				}					
			}else{
				LOGGER.info("Datum " + datum + " konnte nicht aus" + //$NON-NLS-1$ //$NON-NLS-2$
				" Kontrollwarteschlange gelöscht werden"); //$NON-NLS-1$
			}
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void trigger(Long kontrollZeitpunkt) {
		List<ResultData> zuSendendeAusfallDatenMenge = new ArrayList<ResultData>();
		
		synchronized (this.kontrollZeitpunkte) {
			Collection<ObjektResultat> ausfallDaten = this.kontrollZeitpunkte.get(kontrollZeitpunkt);
			if(ausfallDaten != null){
				for(ObjektResultat ausfallDatum:ausfallDaten){
					zuSendendeAusfallDatenMenge.add(this.getAusfallDatumVon(ausfallDatum.getDatum()));
				}				
			}else{
				LOGGER.warning("Der Kontrollzeitpunkt " +  //$NON-NLS-1$
						DUAKonstanten.ZEIT_FORMAT_GENAU.format(new Date(kontrollZeitpunkt)) + 
						" wurde inzwischen entfernt"); //$NON-NLS-1$
			}
		}
		
		/**
		 * Sende die ausgefallenen Daten an mich selbst, um
		 * die Liste der Ausfallzeitpunkte zu aktualisieren
		 */
		if(zuSendendeAusfallDatenMenge.size() > 0){
			this.aktualisiereDaten(zuSendendeAusfallDatenMenge.toArray(new ResultData[0]));
		}		
	}

	
	/**
	 * Erfragt den Zeitpunkt, zu dem von dem Objekt, das mit diesem 
	 * Datensatz assoziiert ist, ein neuer Datensatz (spätestens) erwartet wird
	 * 
	 * @param empfangenesResultat ein empfangener Datensatz  
	 * @return der späteste Zeitpunkt des nächsten Datensatzes oder -1, 
	 * wenn dieser nicht sinnvoll bestimmt werden konnte (wenn z.B. keine
	 * Parameter vorliegen)
	 */
	private final long getKontrollZeitpunktVon(final ResultData empfangenesResultat){
		long kontrollZeitpunkt = -1;

		long maxZeitVerzug = this.getMaxZeitVerzug(empfangenesResultat.getObject());

		if(maxZeitVerzug >= 0){
			kontrollZeitpunkt = empfangenesResultat.getDataTime() + 2 * this.getTVon(empfangenesResultat) + maxZeitVerzug;
		}else{
			LOGGER.fine("Es wurden noch keine (sinnvollen) Parameter empfangen: " //$NON-NLS-1$ 
					+ empfangenesResultat.getObject());
		}

		return kontrollZeitpunkt;
	}


	/**
	 * {@inheritDoc}
	 */
	public ModulTyp getModulTyp() {
		return null;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void aktualisierePublikation(IDatenFlussSteuerung dfs) {
		// hier wird nicht publiziert
	}
}
