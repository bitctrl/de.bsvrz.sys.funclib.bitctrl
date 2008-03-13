package de.bsvrz.sys.funclib.bitctrl.dua.intpuf;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <b>Achtung:<b> Puffer funktioniert nur fuer chronologisch einlaufende Daten
 * TODO: Mal optimieren und nicht immer über den ganzen Puffer iterieren!!!
 * 
 * @author Thierfelder
 *
 * @param <T>
 */
public class IntervallPuffer<T extends IIntervallDatum<T>> {

	protected SortedMap<Long, Intervall> puffer = new TreeMap<Long, Intervall>();
	
	
	public final void loescheAllesUnterhalbVon(final long startIntervall)
	throws IntervallPufferException{
		List<Long> loeschListe = new ArrayList<Long>();
		for(long start:this.puffer.keySet()){
			Intervall intervall = this.puffer.get(start);
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
	
	
	public final void add(final IIntervallPufferElement<T> element)
	throws IntervallPufferException{
		Intervall letztesIntervall = null;
		
		if( !this.puffer.keySet().isEmpty() ){
			letztesIntervall = puffer.get(puffer.lastKey());
		}
		
		if(letztesIntervall == null){
			letztesIntervall = new Intervall(element);
			this.puffer.put(element.getIntervallStart(), letztesIntervall);
		}else{
			if(letztesIntervall.isKompatibel(element)){
				letztesIntervall.add(element);
			}else{
				Intervall neuesIntervall = new Intervall(element);
				this.puffer.put(element.getIntervallStart(), neuesIntervall);
			}
		}
				
	}
	
	
	public final long getSpeicherAuslastung(){
		return this.puffer.keySet().size();
	}
	
		
	@Override
	public String toString() {
		long start = Long.MAX_VALUE;
		long ende = Long.MIN_VALUE;
		
		for(Intervall intervall:this.puffer.values()){
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


	protected class Intervall{

		private T inhalt;
		
		private long start;
		
		private long ende;
		
		private long granularitaet;
		
		
		protected Intervall(final IIntervallPufferElement<T> element)
		throws IntervallPufferException{
			this.start = element.getIntervallStart();
			this.ende = element.getIntervallEnde();
			this.inhalt = element.getInhalt();
			this.granularitaet = this.ende - this.start;
			if(this.granularitaet < 0){
				throw new IllegalArgumentException("Intervallende (" + this.ende +  //$NON-NLS-1$
						") liegt vor Intervallanfang (" + this.start +	//$NON-NLS-1$
						"):\n" + this.inhalt);  //$NON-NLS-1$
			}
		}
		
		
		protected final void setStart(final long start)
		throws IntervallPufferException{
			if(this.ende - start < 0){
				throw new IllegalArgumentException("Intervallende (" + this.ende +  //$NON-NLS-1$
						") liegt vor Intervallanfang (" + this.start +	//$NON-NLS-1$
						"):\n" + this.inhalt);  //$NON-NLS-1$
			}
			this.start = start;
		}
		
		protected final boolean isKompatibel(final IIntervallPufferElement<T> element){
			return element.getInhalt().istGleich(this.inhalt) && 
			   	   element.getIntervallEnde() - element.getIntervallStart() == this.granularitaet &&
			   	   this.ende == element.getIntervallStart();
		}
		
		protected final void add(final IIntervallPufferElement<T> element)
		throws IntervallPufferException{
			if( isKompatibel(element) ){
				this.ende += granularitaet;
			}else{
				throw new IntervallPufferException("Versuch inkompatibles Datum\n" + element + //$NON-NLS-1$
						"einzufuegen in Puffer:\n" + this); //$NON-NLS-1$
			}
		}

		protected final long getGranularitaet(){
			return this.granularitaet;
		}

		public T getInhalt() {
			return this.inhalt;
		}

		public long getIntervallEnde() {
			return this.ende;
		}

		public long getIntervallStart() {
			return this.start;
		}
		
		@Override
		public String toString() {
			return "[" + this.start + ", " + this.ende +   //$NON-NLS-1$//$NON-NLS-2$
				"] Granularitaet: "	+ this.granularitaet + "\n" + this.inhalt; //$NON-NLS-1$//$NON-NLS-2$
		}
		
	}
	
}
