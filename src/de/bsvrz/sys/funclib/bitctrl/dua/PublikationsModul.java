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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.adapter.AbstraktBearbeitungsKnotenAdapter;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DFSKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * Dieses Modul funktioniert wie ein normaler Bearbeitungsknoten mit
 * folgenden Unterschieden: <br>
 * 1.) Es werden keine Daten plausibilisiert<br>
 * 2.) Die Publikation ist standardmäßig angeschaltet
 * und kann nicht ausgeschaltet werden<br>
 * 3.) Für das selbe Systemobjekt darf nicht zweimal hintereinander die 
 * Kennung <code>keine Daten</code> versendet werden
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class PublikationsModul
extends AbstraktBearbeitungsKnotenAdapter{

	/**
	 * der Typ des Moduls, für den dieser Bearbeitungsknoten
	 * publizieren soll
	 */
	private ModulTyp modulTyp = null;
	
	/**
	 * Parameter zur Datenflusssteuerung für diese
	 * SWE und dieses Modul
	 */
	private IDatenFlussSteuerungFuerModul iDfsMod = DFSKonstanten.STANDARD;
	
	/**
	 * Zustand <code>keine Daten</code> jedes Objektes
	 */
	private Map<SystemObject, Boolean> keineDaten = new HashMap<SystemObject, Boolean>(); 
	
	
	/**
	 * Standardkonstruktor
	 * 
	 * @param stdAspekte Informationen zu den Standardpublikationsaspekten für dieses
	 * Modul
	 * @param modulTyp der Typ des Moduls, für den dieser Bearbeitungsknoten
	 * publizieren soll oder <code>null</code>, wenn die Publikation hier nicht
	 * dynamisch sein soll (sich also nicht an der Datenflusssteuerung für dieses
	 * Modul orientieren soll)
	 */
	public PublikationsModul(final IStandardAspekte stdAspekte,
							 final ModulTyp modulTyp){
		this.standardAspekte = stdAspekte;
		this.modulTyp = modulTyp;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initialisiere(IVerwaltung dieVerwaltung)
	throws DUAInitialisierungsException {
		super.initialisiere(dieVerwaltung);
		this.publikationsAnmeldungen.modifiziereObjektAnmeldung(this.standardAspekte.
				getStandardAnmeldungen(this.verwaltung.getSystemObjekte()));
	}		


	/**
	 * {@inheritDoc}
	 */
	public void aktualisiereDaten(ResultData[] resultate) {
		if(resultate != null){
			for(ResultData resultat:resultate){
				if(resultat != null){
					ResultData publikationsDatum = null;
					
					if(this.modulTyp != null){
						publikationsDatum = 
							iDfsMod.getPublikationsDatum(resultat,
									resultat.getData(), standardAspekte.getStandardAspekt(resultat));
					}else{
						publikationsDatum = new ResultData(resultat.getObject(),
								new DataDescription(resultat.getDataDescription().getAttributeGroup(), 
										standardAspekte.getStandardAspekt(resultat), (short)0),
										resultat.getDataTime(), resultat.getData());
						
					}
					
					if(publikationsDatum != null){
						if(publikationsDatum.getData() == null){
							Boolean objektStehtAktuellAufKeineDaten = this.keineDaten.get(publikationsDatum.getObject());
							if(objektStehtAktuellAufKeineDaten != null && !objektStehtAktuellAufKeineDaten){
								this.publikationsAnmeldungen.sende(publikationsDatum);
							}
						}else{
							this.publikationsAnmeldungen.sende(publikationsDatum);
						}
						
						this.keineDaten.put(publikationsDatum.getObject(), publikationsDatum.getData() == null);
					}			
				}
			}
			
			if(this.knoten != null){
				this.knoten.aktualisiereDaten(resultate);
			}
		}
	}

	
	/**
	 * {@inheritDoc} 
	 */
	public ModulTyp getModulTyp() {
		return this.modulTyp;
	}

	
	/**
	 * {@inheritDoc} 
	 */
	public void aktualisierePublikation(IDatenFlussSteuerung iDfs) {
		if(this.modulTyp != null){
			this.iDfsMod = iDfs.getDFSFuerModul(this.verwaltung.getSWETyp(),
					this.getModulTyp());
			
			Collection<DAVObjektAnmeldung> anmeldungenStd =
				new ArrayList<DAVObjektAnmeldung>();

			if(this.standardAspekte != null){
				anmeldungenStd = this.standardAspekte.
				getStandardAnmeldungen(this.verwaltung.getSystemObjekte());
			}

			Collection<DAVObjektAnmeldung> anmeldungen = 
				this.iDfsMod.getDatenAnmeldungen(this.verwaltung.getSystemObjekte(), 
						anmeldungenStd);

			synchronized(this){
				this.publikationsAnmeldungen.modifiziereObjektAnmeldung(anmeldungen);
			}
		}
	}
}
