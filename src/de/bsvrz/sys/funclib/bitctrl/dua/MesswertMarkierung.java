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

/**
 * Klasse, die alle Markierungen eines Messwertes speichert
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class MesswertMarkierung 
implements Cloneable{

	/**
	 * der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	protected boolean nichtErfasst = false;

	/**
	 * der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	protected boolean implausibel = false;

	/**
	 * der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	protected boolean interpoliert = false;

	/**
	 * der Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	protected boolean formalMax = false; 

	/**
	 * der Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	protected boolean formalMin = false; 

	/**
	 * der Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	protected boolean logischMax = false; 
	
	/**
	 * der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	protected boolean logischMin = false; 
	
	/**
	 * zeigt an, ob eine der Setter-Methoden benutzt wurde
	 */
	protected boolean veraendert = false;

	
	/**
	 * Erfragt den Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 *  
	 * @return der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final boolean isInterpoliert(){
		return this.interpoliert;
	}
	
	
	/**
	 * Setzt den Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 *  
	 * @param interpoliert der Wert von <code>*.Status.MessWertErsetzung.Interpoliert</code>
	 */
	public final void setInterpoliert(boolean interpoliert){
		this.veraendert = true;
		this.interpoliert = interpoliert;
	}
	
	
	/**
	 * Erfragt den Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 *  
	 * @return der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	public final boolean isImplausibel(){
		return this.implausibel;
	}
	
	
	/**
	 * Setzt den Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 *  
	 * @param implausibel der Wert von <code>*.Status.MessWertErsetzung.Implausibel</code>
	 */
	public final void setImplausibel(boolean implausibel){
		this.veraendert = true;
		this.implausibel = nichtErfasst;
	}
	
	
	/**
	 * Erfragt den Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 *  
	 * @return der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	public final boolean isNichtErfasst(){
		return this.nichtErfasst;
	}
	
	
	/**
	 * Setzt den Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 *  
	 * @param nichtErfasst der Wert von <code>*.Status.Erfassung.NichtErfasst</code>
	 */
	public final void setNichtErfasst(boolean nichtErfasst){
		this.veraendert = true;
		this.nichtErfasst = nichtErfasst;
	}
	

	/**
	 * Erfragt den Wert von <code>*.Status.PlFormal.WertMax</code>
	 * 
	 * @return den Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	public final boolean isFormalMax() {
		return formalMax;
	}


	/**
	 * Setzt den Wert von <code>*.Status.PlFormal.WertMax</code>
	 * 
	 * @param formalMax der Wert von <code>*.Status.PlFormal.WertMax</code>
	 */
	public final void setFormalMax(boolean formalMax) {
		this.veraendert = true;
		this.formalMax = formalMax;
	}


	/**
	 * Erfragt den Wert von <code>*.Status.PlFormal.WertMin</code>
	 * 
	 * @return den Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	public final boolean isFormalMin() {
		return formalMin;
	}


	/**
	 * Setzt den Wert von <code>*.Status.PlFormal.WertMin</code>
	 * 
	 * @param formalMin der Wert von <code>*.Status.PlFormal.WertMin</code>
	 */
	public final void setFormalMin(boolean formalMin) {
		this.veraendert = true;
		this.formalMin = formalMin;
	}


	/**
	 * Erfragt den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 * 
	 * @return den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	public final boolean isLogischMax() {
		return logischMax;
	}


	/**
	 * Setzt den Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 * 
	 * @param logischMax der Wert von <code>*.Status.PlLogisch.WertMaxLogisch</code>
	 */
	public final void setLogischMax(boolean logischMax) {
		this.veraendert = true;
		this.logischMax = logischMax;
	}


	/**
	 * Erfragt den Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 * 
	 * @return der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	public final boolean isLogischMin() {
		return logischMin;
	}


	/**
	 * Setzt den Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 * 
	 * @param logischMin der Wert von <code>*.Status.PlLogisch.WertMinLogisch</code>
	 */
	public final void setLogischMin(boolean logischMin) {
		this.veraendert = true;
		this.logischMin = logischMin;
	}

		
	/**
	 * Erfragt, ob dieser Wert veraendert wurde
	 * 
	 * @return ob dieser Wert veraendert wurde
	 */
	public final boolean isVeraendert(){
		return this.veraendert;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return   "Nicht Erfasst:    " + (this.nichtErfasst?"Ja":"Nein") +  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			   "\nFormal.Max:       " + (this.formalMax?"Ja":"Nein") +  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			   "\nFormal.Min:       " + (this.formalMin?"Ja":"Nein") +  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			   "\nLogisch.Max:      " + (this.logischMax?"Ja":"Nein") +  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			   "\nLogisch.Min:      " + (this.logischMin?"Ja":"Nein") +  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			   "\nMwe.Implausibel:  " + (this.implausibel?"Ja":"Nein") +  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
			   "\nMwe.Interpoliert: " + (this.interpoliert?"Ja":"Nein");  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
	}
}
