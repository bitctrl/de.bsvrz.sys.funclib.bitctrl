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

package de.bsvrz.sys.funclib.bitctrl.dua.dfs;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse enth�lt alle Parameter, die innerhalb eines Datensatzes
 * <code>ParameterSatz</code> der Attributgruppe
 * <code>atg.datenflussSteuerung</code> vorkommen. Pro SWE wird nur ein
 * Parametersatz vorgehalten. Sollten also innerhalb dieser Attributgruppe
 * mehrere Parameters�tze f�r die gleiche SWE vorkommen, so werden diese
 * (sp�ter) gemischt.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class ParameterSatz {

	/**
	 * Debug-Logger.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die SWE, deren Publikationsparameter in dieser Klasse stehen.
	 */
	private SWETyp swe = null;

	/**
	 * alle Publikationszuordnungen dieses Parametersatzes.
	 */
	private List<PublikationsZuordung> pubZuordnungen = new ArrayList<PublikationsZuordung>();

	/**
	 * Erfragt die SWE, f�r die Publikationsparameter in dieser Klasse stehen.
	 * 
	 * @return die SWE
	 */
	public final SWETyp getSwe() {
		return swe;
	}

	/**
	 * Setzt die SWE, f�r die Publikationsparameter in dieser Klasse stehen.
	 * 
	 * @param swe
	 *            die SWE
	 */
	public final void setSwe(final SWETyp swe) {
		this.swe = swe;
	}

	/**
	 * Erfragt eine Liste mit allen Publikationszuordnungen dieses
	 * Parametersatzes.
	 * 
	 * @return alle Publikationszuordnungen dieses Parametersatzes (oder eine
	 *         leere Liste)
	 */
	public final List<PublikationsZuordung> getPubZuordnung() {
		return pubZuordnungen;
	}

	/**
	 * F�gt der Liste aller Publikationszuordnungen eine neue
	 * Publikationszuordnung hinzu. Bevor dies geschieht, werden alle schon
	 * vorhandenen Publikationszuordnungen auf Konsistenz mit der neuen
	 * Publikationszuordnung getestet. F�llt dieser Test negativ aus, so wird
	 * die neue Publikationszuordnung ignoriert und eine den Fehler
	 * dokumentierende Warnung ausgegeben.
	 * 
	 * @param pubZuordnung
	 *            neue Publikationszuordnung
	 */
	public final void add(final PublikationsZuordung pubZuordnung) {
		boolean addErlaubt = true;

		for (PublikationsZuordung altePz : this.pubZuordnungen) {
			String fehler = altePz.isKompatibelMit(pubZuordnung);
			if (fehler != null) {
				LOGGER.warning(fehler);
				addErlaubt = false;
				break;
			}
		}

		if (addErlaubt) {
			this.pubZuordnungen.add(pubZuordnung);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = "SWE: " + swe + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

		for (PublikationsZuordung pz : pubZuordnungen) {
			s += pz + "\n"; //$NON-NLS-1$
		}

		return s;
	}

}
