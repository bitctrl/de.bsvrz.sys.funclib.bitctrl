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
package de.bsvrz.sys.funclib.bitctrl.dua;

import de.bsvrz.sys.funclib.bitctrl.daf.AbstractDavZustand;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;

/**
 * Korrespondiert mit den Eigenschaften einer ggf. skalierbaren DAV-Ganzzahl
 * (mit Zustaenden)
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class GanzZahl
implements Comparable<GanzZahl> {
	
	/**
	 * der Wert an sich
	 */
	private long wert;
		
	/**
	 * der Skalierungsfaktor
	 */
	private double skalierungsFaktor = 1.0;
	
	/**
	 * Menge der Zustaende dieser Ganzzahl
	 */
	private AbstractDavZustand[] zustaende = null;
	
	/**
	 * der aktuelle Zustand
	 */
	private AbstractDavZustand aktuellerZustand = null;
		
	
	/**********************************************************
	 * 														  *
	 *     statische Methoden zum Anlegen von Variablen       *
	 *            											  *
	 **********************************************************/
	
	/**
	 * Erfragt eine Instanz einer normalen Messwertzahl (unskaliert und mit
	 * den drei Zuständen <code>fehlerhaft</code>, <code>nicht ermittelbar</code> und
	 * <code>nicht ermittelbar/fehlerhaft</code>)
	 * 
	 * @return eine Instanz einer normalen Messwertzahl
	 */
	public static final GanzZahl getMWZahl(){
		return new GanzZahl( new AbstractDavZustand[] {MesswertZustand.FEHLERHAFT, 
													   MesswertZustand.NICHT_ERMITTELBAR,
													   MesswertZustand.FEHLERHAFT_BZW_NICHT_ERMITTELBAR});
	}
	
	
	/**
	 * Erfragt eine Instanz eines Gueteindizes (skaliert mit 0,0001 und mit
	 * den drei Zustaenden <code>fehlerhaft</code>, <code>nicht ermittelbar</code> und
	 * <code>nicht ermittelbar/fehlerhaft</code>)<br>
	 * <b>Achtung:</b> Dieser Wert ist standardmaessig mit 1.0 initialisiert
	 * 
	 * @return eine Instanz eines Gueteindizes
	 */
	public static final GanzZahl getGueteIndex(){
		GanzZahl gueteIndex = new GanzZahl( 0.0001, new AbstractDavZustand[] {MesswertZustand.FEHLERHAFT, 
																			  MesswertZustand.NICHT_ERMITTELBAR,
																			  MesswertZustand.FEHLERHAFT_BZW_NICHT_ERMITTELBAR});
		gueteIndex.setWert(10000);
		return gueteIndex;
	}
	
			
	
	/**
	 * Standardkonstruktor für Skalierung 1.0 und keine Zustaende
	 */
	public GanzZahl(){
		//
	}
	
	
	/**
	 * Standardkonstruktor mit Skalierungsfaktor
	 * 
	 * @param skalierungsFaktor der Skalierungsfaktor
	 */
	public GanzZahl(final double skalierungsFaktor){
		this.skalierungsFaktor = skalierungsFaktor;
	}
	

	/**
	 * Standardkonstruktor mit Zustandsmenge
	 * 
	 * @param zustaende Menge von Zustaenden
	 */
	public GanzZahl(final AbstractDavZustand[] zustaende){
		this.zustaende = zustaende;
		this.setWert(0);
	}

	
	/**
	 * Standardkonstruktor mit Skalierungsfaktor und Zustandsmenge
	 * 
	 * @param skalierungsFaktor der Skalierungsfaktor
	 * @param zustaende Menge von Zustaenden
	 */
	public GanzZahl(final double skalierungsFaktor,
					final AbstractDavZustand[] zustaende){
		this.skalierungsFaktor = skalierungsFaktor;
		this.zustaende = zustaende;
		this.setWert(0);
	}

	
	/**
	 * Kopierkonstruktor
	 * 
	 * @param vorlage das zu kopierende <code>GanzZahl</code>-Objekt
	 */
	public GanzZahl(final GanzZahl vorlage){
		this.wert = vorlage.wert;
		this.skalierungsFaktor = vorlage.skalierungsFaktor;
		this.zustaende = new AbstractDavZustand[vorlage.zustaende.length];
		for(int i = 0; i<vorlage.zustaende.length; i++){
			this.zustaende[i] = vorlage.zustaende[i];
		}
		this.aktuellerZustand = vorlage.aktuellerZustand;
	}

	
	/**
	 * Erfragt den Wert
	 * 
	 * @return der Wert
	 */
	public final long getWert() {
		return wert;
	}


	/**
	 * Setzt den Wert
	 * 
	 * @param wert festzulegender Wert
	 */
	public final void setWert(long wert) {
		this.wert = wert;
		
		if(this.zustaende != null){
			this.aktuellerZustand = null;
			for(AbstractDavZustand zustand:this.zustaende){
				if(zustand.getCode() == this.wert){
					this.aktuellerZustand = zustand;
					break;
				}
			}
		}
	}
	
	
	/**
	 * Setzt den (skalierten) Wert
	 * 
	 * @param wert festzulegender (skalierter) Wert
	 */
	public final void setSkaliertenWert(double wert) {
		double skalierung = this.skalierungsFaktor;
		this.setWert( Math.round(wert / skalierung) );
	}
	
	
	/**
	 * Erfragt den (skalierten) Wert
	 * 
	 * @return der (skalierte) Wert
	 */
	public final double getSkaliertenWert() {
		return this.getWert() * this.skalierungsFaktor;
	}
	
	/**
	 * Setzt den aktuellen Zustand dieses Wertes
	 * 
	 * @param zustand der aktuelle Zustand dieses Wertes
	 */
	public final void setZustand(final AbstractDavZustand zustand) {
		assert( zustand != null );
		this.wert = zustand.getCode();
		this.aktuellerZustand = zustand;
	}

	
	/**
	 * Erfragt den aktuellen Zustand dieses Wertes
	 * 
	 * @return der aktuelle Zustand dieses Wertes
	 */
	public final AbstractDavZustand getZustand(){
		return this.aktuellerZustand;
	}
	
	
	/**
	 * Erfragt, ob dieser Wert zur Zeit einen Zustand angenommen hat
	 * 
	 * @return ob dieser Wert zur Zeit einen Zustand angenommen hat
	 */
	public final boolean isZustand(){
		return this.aktuellerZustand != null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = Konstante.LEERSTRING;
		
		s +=   "Wert (unskaliert): " + this.getWert(); //$NON-NLS-1$
		s += "\nWert (skaliert): " + this.getSkaliertenWert() + ", (F: " + this.skalierungsFaktor + ")";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		s += "\nZustaende: ";  //$NON-NLS-1$
		if(this.zustaende == null || this.zustaende.length == 0){
			s += "keine";  //$NON-NLS-1$
		}else{
			for(AbstractDavZustand zustand:this.zustaende){
				s += "\n" + zustand.toString() + " (" + zustand.getCode() + ")";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
			
			s += "\nAktueller Zustand: "; //$NON-NLS-1$
			if(this.isZustand()){
				s += this.getZustand().toString() + " (" + this.getZustand().getCode() + ")";  //$NON-NLS-1$ //$NON-NLS-2$
			}else{
				s += "keiner"; //$NON-NLS-1$
			}
		}
		
		return s;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean gleich = false;
		
		if(obj != null && obj instanceof GanzZahl){
			GanzZahl that = (GanzZahl)obj;
			gleich = this.getWert() == that.getWert();
		}
		
		return gleich;
	}


	/**
	 * {@inheritDoc}
	 */
	public int compareTo(GanzZahl that) {
		if(that == null){
			throw new NullPointerException("Vergleichswert ist <<null>>"); //$NON-NLS-1$
		}
		return new Long(this.getWert()).compareTo(that.getWert());
	}
	
}
