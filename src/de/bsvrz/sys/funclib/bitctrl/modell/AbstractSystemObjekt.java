/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse 
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
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Implementierung der gemeinsamen Methoden der Systemobjektschnittstelle.
 * 
 * @author Schumann, BitCtrl
 * @version $Id$
 */
public abstract class AbstractSystemObjekt implements SystemObjekt {

	/** Das gekapselte Systemobjekt des Datenverteilers. */
	protected final SystemObject objekt;

	/**
	 * Weist lediglich das Systemobjekt zu.
	 * 
	 * @param obj
	 *            Das zu kapselnde Systemobjekt
	 */
	protected AbstractSystemObjekt(SystemObject obj) {
		objekt = obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getId() {
		return objekt.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return objekt.getPid();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return objekt.getNameOrPidOrId();
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObject getSystemObject() {
		return objekt;
	}

	/**
	 * &Uuml;bernimmt die Methode von {@link SystemObject}.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SystemObjekt) {
			SystemObjekt obj = (SystemObjekt) o;
			return getSystemObject().equals(obj.getSystemObject());
		}

		return false;
	}

	/**
	 * Ruft die Hashfunktion von {@link SystemObject} auf.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return getSystemObject().hashCode();
	}

	/**
	 * &UUml;bernimmt die Methode von {@link SystemObject}.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return objekt.toString();
	}

}
