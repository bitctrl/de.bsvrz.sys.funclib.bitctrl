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

package de.bsvrz.sys.funclib.bitctrl.modell;

import com.bitctrl.util.Timestamp;

/**
 * Implementiert die Gemeinsamkeiten der Schnittstelle {@code Datum}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public abstract class AbstractDatum implements Datum {

	/** Der Zeitstempel des Datums. */
	private long zeitstempel;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract Datum clone();

	/**
	 * {@inheritDoc}
	 */
	public String getZeitpunkt() {
		return Timestamp.absoluteTime(zeitstempel);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getZeitstempel() {
		return zeitstempel;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean isValid() {
		return getDatenStatus() == Datum.Status.DATEN;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setZeitstempel(final long zeitstempel) {
		this.zeitstempel = zeitstempel;
	}

}
