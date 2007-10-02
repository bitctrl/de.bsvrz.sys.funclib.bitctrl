/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straßensubsegmentanalyse
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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;ssentiert einen Messquerschnitt.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class StoerfallIndikator extends AbstractSystemObjekt {

	private static final String PID_ATG_STOERFALL_ZUSTAND = "atg.störfallZustand"; //$NON-NLS-1$

	/**
	 * Attributname für die Speicherung des aktuellen Störfallzustands des
	 * Indikators.
	 */
	public static final String ATT_NAME_SITUATION = "Situation"; //$NON-NLS-1$

	private static AttributeGroup situationsAtg = null;

	/** die aktuelle Situation des Indikators. */
	private final Map<Aspect, StoerfallSituation> situationen = new HashMap<Aspect, StoerfallSituation>();

	/**
	 * Erzeugt einen Störfallindikator aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein StörfallIndikator sein muss
	 * @throws IllegalArgumentException
	 */
	public StoerfallIndikator(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Störfallindikator."); //$NON-NLS-1$
		}

		if (situationsAtg == null) {
			situationsAtg = obj.getDataModel().getAttributeGroup(
					PID_ATG_STOERFALL_ZUSTAND);
		}
	}

	/**
	 * {@inheritDoc}.
	 */
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STOERFALLINDIKATOR;
	}

	/**
	 * setzt den Situationswert für den angegebenen Aspekt.
	 * 
	 * @param situation
	 * @param aspekt
	 */
	public void setSituation(final StoerfallSituation situation,
			final Aspect aspekt) {
		situationen.put(aspekt, situation);
	}

	public StoerfallSituation getSituation() {
		StoerfallSituation situation = StoerfallSituation.STOERUNG;
		for (StoerfallSituation s : situationen.values()) {
			if (s.getCode() > situation.getCode()) {
				situation = s;
			}
		}
		return situation;
	}

	/**
	 * entfernt alle Verfahren (Aspekte) aus der Situationsliste, die nicht in
	 * der angegebenen Liste enthalten sind.
	 * 
	 * @param aspekte
	 *            die Liste der unterstützten Verfahren.
	 */
	public void bereinigeSituationsListe(Aspect... aspekte) {
		Map<Aspect, StoerfallSituation> alteSituationen = new HashMap<Aspect, StoerfallSituation>(
				situationen);
		situationen.clear();
		for (Aspect aspekt : aspekte) {
			if (alteSituationen.containsKey(aspekt)) {
				situationen.put(aspekt, alteSituationen.get(aspekt));
			}
		}
	}

	public static AttributeGroup getSituationsAtg() {
		return situationsAtg;
	}
}
