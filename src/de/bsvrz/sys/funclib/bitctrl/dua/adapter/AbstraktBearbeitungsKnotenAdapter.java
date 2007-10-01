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

package de.bsvrz.sys.funclib.bitctrl.dua.adapter;

import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVSendeAnmeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.DatenFlussSteuerungsVersorger;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IBearbeitungsKnoten;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;

/**
 * Adapterklasse f�r einen Bearbeitungsknoten.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public abstract class AbstraktBearbeitungsKnotenAdapter
implements IBearbeitungsKnoten {
	
	/**
	 * n�chster Bearbeitungsknoten
	 */
	protected IBearbeitungsKnoten knoten = null;
	
	/**
	 * <b>FLAG</b>: Soll publiziert werden?
	 */
	protected boolean publizieren = false;
	
	/**
	 * Verbindung zum Verwaltungsmodul
	 */
	protected IVerwaltung verwaltung = null;
	
	/**
	 * Schnittstelle zu den Informationen �ber die
	 * Standardpublikationsaspekte
	 */
	protected IStandardAspekte standardAspekte = null;

	/**
	 * Anmeldungen zum Publizieren von verarbeiteten
	 * Daten
	 */
	protected DAVSendeAnmeldungsVerwaltung 
						publikationsAnmeldungen = null;
	
	
	/**
	 * {@inheritDoc}
	 */
	public void setPublikation(boolean publizieren) {
		this.publizieren = publizieren;	
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void setNaechstenBearbeitungsKnoten(IBearbeitungsKnoten knoten) {
		this.knoten = knoten;
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void initialisiere(IVerwaltung dieVerwaltung) 
	throws DUAInitialisierungsException {
		if(dieVerwaltung == null || dieVerwaltung.getVerbindung() == null){
			throw new DUAInitialisierungsException("Es konnte keine Verbindung" + //$NON-NLS-1$
					" zum Verwaltungsmodul (bzw. zum Datenverteiler" + //$NON-NLS-1$
					") hergestellt werden"); //$NON-NLS-1$
		}
		this.verwaltung = dieVerwaltung;
		this.publikationsAnmeldungen = new DAVSendeAnmeldungsVerwaltung(
				this.verwaltung.getVerbindung(),
				SenderRole.source());
		DatenFlussSteuerungsVersorger.getInstanz(verwaltung).addListener(this);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "Modul-Typ: " + (this.getModulTyp() != null?this.getModulTyp():"unbekannt")+ //$NON-NLS-1$ //$NON-NLS-2$
				" in SWE " + this.verwaltung.getSWETyp();  //$NON-NLS-1$
	}	
}
