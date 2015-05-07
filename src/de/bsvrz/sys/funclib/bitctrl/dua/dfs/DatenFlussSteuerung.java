/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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

import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerung;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.schnittstellen.IDatenFlussSteuerungFuerModul;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.ModulTyp;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;

/**
 * Diese Klasse repr�sentiert die Attributgruppe
 * <code>atg.datenFlussSteuerung</code> des Typs
 * <code>typ.datenFlussSteuerung</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DatenFlussSteuerung implements IDatenFlussSteuerung {

	/**
	 * Liste aller Parameters�tze innerhalb der Attributgruppe.
	 */
	private final List<ParameterSatz> parameterSaetze = new ArrayList<>();

	/**
	 * F�gt diesem Objekt einen Parametersatz hinzu.
	 *
	 * @param ps
	 *            der neue Parametersatz
	 */
	protected final void add(final ParameterSatz ps) {
		parameterSaetze.add(ps);
	}

	/**
	 * Erfragt den Parametersatz f�r eine bestimmte SWE<br>
	 * <b>Achtung: Es wird innerhalb dieser Klasse immer nur ein
	 * ParameterSatz-Objekt pro SWE instanziiert werden, auch wenn mehrere
	 * parametriert sind (die Informationen werden zusammengefasst). Sollten
	 * widerspr�chliche Informationen innerhalb der Parameters�tze enthalten
	 * sein, so werden alle Parameters�tze, die diesen Widerspruch enthalten
	 * ignoriert.</b>
	 *
	 * @param swe
	 *            die SWE
	 * @return der Parametersatz der Datenflusssteuerung f�r die �bergebene SWE
	 *         oder <code>null</code>, wenn f�r die SWE kein Parametersatz
	 *         vorliegt
	 */
	protected final ParameterSatz getParameterSatzFuerSWE(final SWETyp swe) {
		ParameterSatz ps = null;

		for (final ParameterSatz psDummy : parameterSaetze) {
			if (psDummy.getSwe().equals(swe)) {
				ps = psDummy;
				break;
			}
		}

		return ps;
	}

	@Override
	public IDatenFlussSteuerungFuerModul getDFSFuerModul(final SWETyp swe,
			final ModulTyp modulTyp) {
		final DatenFlussSteuerungFuerModul dfsModul = new DatenFlussSteuerungFuerModul();
		final ParameterSatz ps = getParameterSatzFuerSWE(swe);

		if (ps != null) {
			for (final PublikationsZuordung pzFuerModul : ps.getPubZuordnung()) {
				if (pzFuerModul.getModulTyp().equals(modulTyp)) {
					dfsModul.add(pzFuerModul);
				}
			}
		}

		return dfsModul;
	}

	@Override
	public String toString() {
		String s = "\nDatenflusssteuerung:\n"; //$NON-NLS-1$

		for (int i = 0; i < parameterSaetze.size(); i++) {
			s += "ParamaterSatz: " + i + "\n" + parameterSaetze.get(i); //$NON-NLS-1$//$NON-NLS-2$
		}

		return s;
	}
}
