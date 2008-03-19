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
package de.bsvrz.sys.funclib.bitctrl.dua.intpuf;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Temporaerer Puffer fuer grosse Datenmengen, deren einzelne Bestandteile 
 * (Datensaetze) chronologisch aufeinanderfolgen und deren Eigenschaften sich
 * relativ selten aendern<br>
 * <b>Achtung:<b> Puffer funktioniert nur fuer chronologisch einlaufende Daten
 * TODO: Mal optimieren und nicht immer ueber den ganzen Puffer iterieren!!!
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <T> die Objektart, ueber die sich die Gleichheit bzw. Ungleichheit
 */
public class IntervallPuffer<T extends IIntervallDatum<T>> {

	/**
	 * Speichert die Daten
	 */
	protected SortedMap<Long, Intervall<T>> puffer = new TreeMap<Long, Intervall<T>>();
	
	
	/**
	 * Loescht alle Daten aus dem Puffer, die aelter als der uebergebene
	 * Zeitstempel sind
	 * 
	 * @param startIntervall der neue Intervallbegin dieses Puffers
	 * @throws IntervallPufferException wenn das obere Ende des Intervalls vor
	 * dem neuen unteren Ende liegt
	 */
	public final void loescheAllesUnterhalbVon(final long startIntervall)
	throws IntervallPufferException{
		List<Long> loeschListe = new ArrayList<Long>();
		for(long start:this.puffer.keySet()){
			Intervall<T> intervall = this.puffer.get(start);
			if(intervall.getIntervallStart() < startIntervall &&
			   intervall.getIntervallEnde() <= startIntervall){
				loeschListe.add(start);
			}else
			if(intervall.getIntervallStart() < startIntervall &&
			   intervall.getIntervallEnde() > startIntervall){
				long restImIntervall = intervall.getIntervallEnde() - startIntervall;
				long restIntervallTeile = restImIntervall / intervall.getGranularitaet();
				if(restIntervallTeile > 0){
					intervall.setStart(intervall.getIntervallEnde() - (restIntervallTeile * intervall.getGranularitaet())); 
				}else{
					loeschListe.add(start);
				}
			}
		}
		
		for(Long zuLoeschenderSchluessel:loeschListe){
			this.puffer.remove(zuLoeschenderSchluessel);
		}
	}
	
	
	/**
	 * Fuegt diesem Puffer ein neues Element hinzu
	 * 
	 * @param element eine neues Pufferelement
	 * @throws IntervallPufferException wenn das obere Ende des Intervalls vor
	 * dem unteren Ende liegt 
	 */
	public final void add(final IIntervallPufferElement<T> element)
	throws IntervallPufferException{
		Intervall<T> letztesIntervall = null;
		
		if( !this.puffer.keySet().isEmpty() ){
			letztesIntervall = puffer.get(puffer.lastKey());
		}
		
		if(letztesIntervall == null){
			letztesIntervall = new Intervall<T>(element);
			this.puffer.put(element.getIntervallStart(), letztesIntervall);
		}else{
			if(letztesIntervall.isKompatibel(element)){
				letztesIntervall.add(element);
			}else{
				Intervall<T> neuesIntervall = new Intervall<T>(element);
				this.puffer.put(element.getIntervallStart(), neuesIntervall);
			}
		}
				
	}
	
	
	/**
	 * Erfragt die Anzahl der im Puffer gespeicherten Elemente
	 * 
	 * @return die Anzahl der im Puffer gespeicherten Elemente
	 */
	public final long getSpeicherAuslastung(){
		return this.puffer.keySet().size();
	}
	
		
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		long start = Long.MAX_VALUE;
		long ende = Long.MIN_VALUE;
		
		for(Intervall<T> intervall:this.puffer.values()){
			if(intervall.getIntervallStart() < start){
				start = intervall.getIntervallStart(); 
			}
			if(intervall.getIntervallEnde() > ende){
				ende = intervall.getIntervallEnde();
			}
		}
		
		return this.puffer.isEmpty()?"leer":"[" + start + //$NON-NLS-1$ //$NON-NLS-2$
				", " + ende + "] Mem: " + this.puffer.size(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	
	/**
	 * Speichert jeweils kompatible Daten innerhalb eines Intervalls
	 *  
	 * @author BitCtrl Systems GmbH, Thierfelder
	 * 
	 **/
	protected class Intervall<T1 extends IIntervallDatum<T>>
	extends IntervallPufferElementAdapter<T>{
		
		/**
		 * die Granularitaet dieses Intervalls
		 */
		private long granularitaet;
		
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param element ein erstes Element dieses Intervalls
		 * @throws IntervallPufferException wenn das obere Ende des Intervalls vor
		 * dem unteren Ende liegt 
		 */
		protected Intervall(final IIntervallPufferElement<T> element)
		throws IntervallPufferException{
			super(element.getIntervallStart(), element.getIntervallEnde());
			this.inhalt = element.getInhalt();
			this.granularitaet = this.getIntervallEnde() - this.getIntervallStart();
			if(this.granularitaet < 0){
				throw new IllegalArgumentException("Intervallende (" + this.getIntervallEnde() +  //$NON-NLS-1$
						") liegt vor Intervallanfang (" + this.getIntervallStart() +	//$NON-NLS-1$
						"):\n" + this.inhalt);  //$NON-NLS-1$
			}
		}
		
		
		/**
		 * Setzt das untere Ende des Intervalls fest
		 * 
		 * @param start das neue untere Ende des Intervalls
		 * @throws IntervallPufferException wenn das obere Ende des Intervalls vor
		 * dem neuen unteren Ende liegt 
		 */
		protected final void setStart(final long start)
		throws IntervallPufferException{
			if(this.getIntervallEnde() - start < 0){
				throw new IllegalArgumentException("Intervallende (" + this.getIntervallEnde() +  //$NON-NLS-1$
						") liegt vor Intervallanfang (" + this.getIntervallStart() +	//$NON-NLS-1$
						"):\n" + this.inhalt);  //$NON-NLS-1$
			}
			this.intervallStart = start;
		}
		
		
		/**
		 * Ueberprueft, ob das uebergebene Datum mit den bisher
		 * gespeicherten Daten im Puffer kompatibel ist
		 * 
		 * @param element ein in den Puffer zu integrierendes Element
		 * @return ob das uebergebene Datum mit den bisher
		 * gespeicherten Daten im Puffer kompatibel ist
		 */
		protected final boolean isKompatibel(final IIntervallPufferElement<T> element){
			return element.getInhalt().istGleich(this.inhalt) && 
			   	   element.getIntervallEnde() - element.getIntervallStart() == this.granularitaet &&
			   	   this.intervallEnde == element.getIntervallStart();
		}
		
		
		/**
		 * Fuegt diesem Intervall ein neues Element hinzu
		 * 
		 * @param element ein neues, logisch zu diesem Intervall passendes
		 * Element
		 * @throws IntervallPufferException wenn das einzufuegende Datum nicht mit
		 * den bisher gespeicherten Daten kompatibel ist
		 */
		protected final void add(final IIntervallPufferElement<T> element)
		throws IntervallPufferException{
			if( isKompatibel(element) ){
				this.intervallEnde += granularitaet;
			}else{
				throw new IntervallPufferException("Versuch inkompatibles Datum\n" + element + //$NON-NLS-1$
						"einzufuegen in Puffer:\n" + this); //$NON-NLS-1$
			}
		}

		
		/**
		 * Erfragt die Granularitaet dieses Intervalls
		 * 
		 * @return die Granularitaet dieses Intervalls
		 */
		protected final long getGranularitaet(){
			return this.granularitaet;
		}
		
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "[" + this.intervallStart + ", " + this.intervallEnde +   //$NON-NLS-1$//$NON-NLS-2$
				"] Granularitaet: "	+ this.granularitaet + "\n" + this.inhalt; //$NON-NLS-1$//$NON-NLS-2$
		}
		
	}
	
}
