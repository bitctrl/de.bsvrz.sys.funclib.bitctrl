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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.HashMap;
import java.util.Map;

import sun.jdbc.odbc.OdbcDef;

import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdStoerfallZustand;

/**
 * Repr&auml;ssentiert einen Störfallindikator innerhalb der
 * Modellobjektverwaltung.
 * 
 * @author BitCtrl Systems GmbH, Schumann, Peuker
 * @version $Id$
 */
public class StoerfallIndikator extends AbstractSystemObjekt {

	/**
	 * Attributname für die Speicherung des aktuellen Störfallzustands des
	 * Indikators.
	 */
	public static final String ATT_NAME_SITUATION = "Situation"; //$NON-NLS-1$

	/**
	 * die PID der Attributgruppe, unter der der Störfallzustand des Indikators
	 * veröffentlicht wird.
	 */
	private static final String PID_ATG_STOERFALL_ZUSTAND = "atg.störfallZustand"; //$NON-NLS-1$

	/**
	 * die Attributgruppe, unter der der Störfallzustand des Indikators
	 * veröffentlicht wird.
	 */
	private static AttributeGroup situationsAtg = null;

	/**
	 * liefert die Attributgruppe unter der der Störfallzustand eines Indikators
	 * innerhald des Datenverteilers publiziert wird.
	 * 
	 * @return die Attributgruppe
	 */
	public static AttributeGroup getSituationsAtg() {
		return situationsAtg;
	}

	/** die aktuelle Situation des Indikators. */
	private final Map<Aspect, StoerfallSituation> situationen = new HashMap<Aspect, StoerfallSituation>();

	/**
	 * Erzeugt einen Störfallindikator aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein StörfallIndikator sein muss
	 * @throws IllegalArgumentException
	 */
	StoerfallIndikator(final SystemObject obj) {
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
	 * entfernt alle Verfahren (Aspekte) aus der Situationsliste, die nicht in
	 * der angegebenen Liste enthalten sind.
	 * 
	 * @param aspekte
	 *            die Liste der unterstützten Verfahren.
	 */
	public void bereinigeSituationsListe(final Aspect... aspekte) {
		Map<Aspect, StoerfallSituation> alteSituationen = new HashMap<Aspect, StoerfallSituation>(
				situationen);
		situationen.clear();
		for (Aspect aspekt : aspekte) {
			if (alteSituationen.containsKey(aspekt)) {
				situationen.put(aspekt, alteSituationen.get(aspekt));
			}
		}
	}

	/**
	 * liefert den aktuellen Zustand des Indikators.<br>
	 * Es wird das Maximum aus allen mit den verschiedenen Aspekten ermittelten
	 * Zustandswerten ermittelt und geliefert.
	 * 
	 * @return der aktuelle Störfallzustand des Indikators.
	 */
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
	 * {@inheritDoc}.
	 */
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STOERFALLINDIKATOR;
	}

	/**
	 * setzt den Situationswert für den angegebenen Aspekt.
	 * 
	 * @param situation
	 *            der neue Störfallzustand des Indikators.
	 * @param aspekt
	 *            der Aspekt, unter dem der Zustand ermittelt wurde
	 */
	public void setSituation(final StoerfallSituation situation,
			final Aspect aspekt) {
		situationen.put(aspekt, situation);
	}
}
