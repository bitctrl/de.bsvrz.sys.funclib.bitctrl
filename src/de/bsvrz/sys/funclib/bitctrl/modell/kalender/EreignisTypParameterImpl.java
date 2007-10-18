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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.dav.daf.main.Data;

/**
 * Implementiert die Schnittstelle.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class EreignisTypParameterImpl implements EreignisTypParameter {

	/** Die Priori&auml;t des Ereignistyps. */
	private long prioritaet;

	/**
	 * {@inheritDoc}
	 */
	public long getPrioritaet() {
		return prioritaet;
	}

	/**
	 * Setzt den inneren Zustand anhand des angegebenen Datums.
	 * 
	 * @param daten
	 *            ein g&uuml;ltiges Datum.
	 */
	public void setDaten(Data daten) {
		if (daten != null) {
			prioritaet = daten.getUnscaledValue("EreignisTypPriorit�t")
					.longValue();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPrioritaet(long prioritaet) {
		this.prioritaet = prioritaet;
	}

}
