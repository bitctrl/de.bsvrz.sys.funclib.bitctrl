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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.testausfall;

import de.bsvrz.dav.daf.main.ResultData;

/**
 * Objekte dieser Klasse wrappen ein <code>ResultData</code> dergestalt, 
 * dass innerhalb von geordneten Mengen nur <b>eine</b> Instanz dieser
 * Klasse mit dem selben Systemobjekt gespeichert werden kann.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public class ObjektResultat implements Comparable<ObjektResultat> {

	/**
	 * ein Datum eines Systemobjekts.
	 */
	private ResultData resultat = null;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param resultat ein Datum eines Systemobjekts.<br><b>ACHTUNG:</b>
	 * Das Datum darf nicht <code>null</code> sein und muss Daten besitzen<br>
	 */
	public ObjektResultat(final ResultData resultat) {
		if (resultat == null) {
			throw new NullPointerException("Roh-Datum ist <<null>>"); //$NON-NLS-1$
		}
		if (resultat.getData() == null) {
			throw new NullPointerException("Roh-Datum hat keine Daten"); //$NON-NLS-1$
		}
		this.resultat = resultat;
	}

	/**
	 * Erfragt das Roh-Datum eines Systemobjekts.
	 * 
	 * @return das Roh-Datum eines Systemobjekts
	 */
	public final ResultData getDatum() {
		return this.resultat;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		boolean ergebnis = false;

		if (obj != null && obj instanceof ObjektResultat) {
			ObjektResultat that = (ObjektResultat) obj;
			ergebnis = this.getDatum().getObject().equals(
					that.getDatum().getObject());
		}

		return ergebnis;
	}

	/**
	 * {@inheritDoc}
	 */
	public int compareTo(ObjektResultat that) {
		return new Long(this.getDatum().getObject().getId()).compareTo(that
				.getDatum().getObject().getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.resultat.toString();
	}

}
