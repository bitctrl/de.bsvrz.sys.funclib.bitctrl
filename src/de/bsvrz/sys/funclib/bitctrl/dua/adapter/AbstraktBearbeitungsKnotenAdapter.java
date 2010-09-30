/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
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
 * Adapterklasse für einen Bearbeitungsknoten.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: AbstraktBearbeitungsKnotenAdapter.java 8054 2008-04-09 15:11:59Z tfelder $
 */
public abstract class AbstraktBearbeitungsKnotenAdapter
implements IBearbeitungsKnoten {
	
	/**
	 * nächster Bearbeitungsknoten.
	 */
	protected IBearbeitungsKnoten knoten = null;
	
	/**
	 * <b>FLAG</b>: Soll publiziert werden?
	 */
	protected boolean publizieren = false;
	
	/**
	 * Verbindung zum Verwaltungsmodul.
	 */
	protected IVerwaltung verwaltung = null;
	
	/**
	 * Schnittstelle zu den Informationen über die
	 * Standardpublikationsaspekte.
	 */
	protected IStandardAspekte standardAspekte = null;

	/**
	 * Anmeldungen zum Publizieren von verarbeiteten
	 * Daten.
	 */
	protected DAVSendeAnmeldungsVerwaltung 
						publikationsAnmeldungen = null;
	
	
	/**
	 * {@inheritDoc}.
	 */
	public void setPublikation(boolean publizieren1) {
		this.publizieren = publizieren1;	
	}

	
	/**
	 * {@inheritDoc}.
	 */
	public void setNaechstenBearbeitungsKnoten(IBearbeitungsKnoten knoten1) {
		this.knoten = knoten1;
	}
	

	/**
	 * {@inheritDoc}.
	 */
	public void initialisiere(IVerwaltung dieVerwaltung) 
	throws DUAInitialisierungsException {
		if (dieVerwaltung == null || dieVerwaltung.getVerbindung() == null) {
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
		return "Modul-Typ: " + (this.getModulTyp() != null ? this.getModulTyp() : "unbekannt") + //$NON-NLS-1$//$NON-NLS-2$
				" in SWE " + this.verwaltung.getSWETyp();  //$NON-NLS-1$
	}	
}
