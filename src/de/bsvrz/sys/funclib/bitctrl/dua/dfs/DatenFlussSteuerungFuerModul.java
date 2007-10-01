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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;

/**
 * Diese Klasse stellt �ber die Schnittstelle <code>IDatenFlussSteuerungFuerModul</code>
 * alle Informationen �ber die Datenflusssteuerung einer bestimmten SWE in Zusammenhang
 * mit einem bestimmten Modul-Typ zur Verf�gung.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 **/
public class DatenFlussSteuerungFuerModul
implements IDatenFlussSteuerungFuerModul {
		
	/**
	 * Liste aller Publikationszuordnungen innerhalb der Attributgruppe
	 */
	private Collection<PublikationsZuordung> publikationsZuordnungen = 
			new ArrayList <PublikationsZuordung>();
	
	/**
	 * Eine Map von einer Objekt-Attributgruppe-Kombination auf
	 * die Information ob, und unter welchem Aspekt publiziert
	 * werden soll
	 */
	private Map<PublikationObjAtg, PublikationFuerDatum> publikationsMap =
				new TreeMap<PublikationObjAtg, PublikationFuerDatum>();
	

	/**
	 * F�gt diesem Objekt eine Publikationszuordung hinzu
	 * 
	 * @param pz
	 *            die neue Publikationszuordung
	 */
	public final void add(final PublikationsZuordung pz) {
		for(DAVObjektAnmeldung anmeldung:pz.getObjektAnmeldungen()){
			PublikationObjAtg pubObjAtg = new PublikationObjAtg(
					anmeldung.getObjekt(), anmeldung.getDatenBeschreibung().
					getAttributeGroup());
			PublikationFuerDatum pub = new PublikationFuerDatum(pz.isPublizieren(), 
											pz.getAspekt());
			
			publikationsMap.put(pubObjAtg, pub);
		}
		publikationsZuordnungen.add(pz);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<DAVObjektAnmeldung> getDatenAnmeldungen(
			final SystemObject[] filterObjekte,
			final Collection<DAVObjektAnmeldung> standardAnmeldungen) {
		Collection<DAVObjektAnmeldung> alleAnmeldungen = new TreeSet<DAVObjektAnmeldung>();
		Collection<DAVObjektAnmeldung> stdAnmeldungen = new TreeSet<DAVObjektAnmeldung>();
		stdAnmeldungen.addAll(standardAnmeldungen);

		for (PublikationsZuordung pz:publikationsZuordnungen) {
			if (pz.isPublizieren()) {
				Collection<SystemObject> pzAnzumeldendeObjekte = 
											new HashSet<SystemObject>();

				if (filterObjekte != null && filterObjekte.length > 0) {
					for (SystemObject obj:pz.getObjekte()) {
						for (SystemObject filterObj:filterObjekte) {
							if (obj.equals(filterObj)){
								pzAnzumeldendeObjekte.add(obj);
								break;
							}
						}
					}
				} else {
					pzAnzumeldendeObjekte.addAll(pz.getObjekte());
				}

				for(DAVObjektAnmeldung pzAnmeldung:pz.getObjektAnmeldungen()){
					if( pzAnzumeldendeObjekte.contains(pzAnmeldung.getObjekt()) ){
						alleAnmeldungen.add(pzAnmeldung);
					}
				}
			}else{
				stdAnmeldungen.removeAll(pz.getObjektAnmeldungen());
			}
		}
		alleAnmeldungen.addAll(stdAnmeldungen);

		return alleAnmeldungen;
	}

	/**
	 * {@inheritDoc}
	 */
	public final ResultData getPublikationsDatum(
							final ResultData originalDatum,
							final Data plausibilisiertesDatum,
							final Aspect standardAspekt) {
		ResultData ergebnis = null;
		Aspect publikationsAspect = null;

		PublikationObjAtg pubObjAtg = new PublikationObjAtg(
				originalDatum.getObject(), originalDatum.getDataDescription().
				getAttributeGroup());
		PublikationFuerDatum pubDatum = this.publikationsMap.get(pubObjAtg);
		
		if(pubDatum != null){
			if(pubDatum.publizieren){
				publikationsAspect = pubDatum.asp;
			}else{
				if(pubDatum.asp != standardAspekt){
					publikationsAspect = standardAspekt;
				}
			}
		}else{
			publikationsAspect = standardAspekt;
		}
		
		if(publikationsAspect != null){
			DataDescription dd = new DataDescription(originalDatum
					.getDataDescription().getAttributeGroup(),
					publikationsAspect, (short) 0);
			ergebnis = new ResultData(originalDatum.getObject(),
					dd, originalDatum.getDataTime(),
					plausibilisiertesDatum);			
		}

		return ergebnis;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "\nDatenflusssteuerung f�r Modul:\n"; //$NON-NLS-1$

		int i = 0;
		for (PublikationsZuordung pz : publikationsZuordnungen) {
			s += "Publikationszuordnung: " + (i++) + "\n" + pz; //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
	
	
	/**
	 * Diese Klasse wird nur als Schl�ssel-Objekt innerhalb der internen
	 * Struktur <code>publikationsMap</code> ben�tigt. Sie speichert
	 * ein (finales) Systemobjekt zusammen mit einer Attributgruppe.
	 * Die Klasse ist so designed, dass sie effektiv als Schl�ssel
	 * innerhalb von <code>TreeMap</code>-Objekten eingesetzt werden
	 * kann.
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class PublikationObjAtg
	implements Comparable<PublikationObjAtg>{
		
		/**
		 * ein Systemobjekt
		 */
		protected SystemObject obj;
		
		/**
		 * eine Attributgruppe
		 */
		protected AttributeGroup atg;
		
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param obj ein Objekt
		 * @param atg eine Attributgruppe
		 */
		protected PublikationObjAtg(final SystemObject obj,
									final AttributeGroup atg){
			this.obj = obj;
			this.atg = atg;			
		}

		/**
		 * {@inheritDoc}
		 */
		public int compareTo(PublikationObjAtg that) {
			int result = Long.valueOf(this.obj.getId())
								.compareTo(that.obj.getId());

			if(result == 0){
				result = Long.valueOf(this.atg.getId()).compareTo(that.atg.getId());
			}

			return result;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * Diese Methode muss implementiert werden, da nach der
		 * Exploration des Baums �ber <code>compareTo(..)</code>
		 * (bspw. beim Aufruf von <code>contains()</code>) nochmals
		 * mit <code>equals(..)</code> explizit auf Gleichheit
		 * getestet wird.
		 */
		@Override
		public boolean equals(Object obj1) {
			boolean result = false;
			
			if(obj1 instanceof PublikationObjAtg){
				PublikationObjAtg that = (PublikationObjAtg)obj1;
				result = this.obj.equals(that.obj) &&
						 that.atg.equals(that.atg);
			}
			
			return result;
		}
		
	}
	
	
	/**
	 * Diese Klasse wird nur als Wert-Objekt zu einem Schl�ssel vom
	 * Typ <code>PublikationObjAtg</code> innerhalb der internen
	 * Struktur <code>publikationsMap</code> ben�tigt. Sie speichert
	 * die Information, ob und unter welchem Aspekt publiziert werden
	 * soll.
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	protected class PublikationFuerDatum{
		
		/**
		 * Soll publiziert werden?
		 */
		protected boolean publizieren = false;
		
		/**
		 * Unter welchem Aspekt soll ein Datum publiziert werden
		 */
		protected Aspect asp = null;
		
		/**
		 * Standardkonstruktor
		 * 
		 * @param publizieren soll publiziert werden?
		 * @param asp unter welchem Aspekt soll ein Datum
		 * publiziert werden
		 */
		protected PublikationFuerDatum(final boolean publizieren,
									   final Aspect asp){
			this.asp = asp;
			this.publizieren = publizieren;
		}
	}
	
}
