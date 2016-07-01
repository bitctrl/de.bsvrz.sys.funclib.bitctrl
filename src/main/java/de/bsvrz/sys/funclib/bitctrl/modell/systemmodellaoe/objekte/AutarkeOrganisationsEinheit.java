/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellaoe.objekte;

import java.util.Set;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.ganglinien.objekte.ApplikationGanglinienPrognose;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisListener;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTypListener;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.KalenderImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintragListener;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellaoe.SystemModellAoeTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte.BetriebsMeldungsVerwaltung;

/**
 * Repräsentiert die autarke Organisationseinheit (AOE).
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class AutarkeOrganisationsEinheit extends AbstractSystemObjekt implements
Kalender, ApplikationGanglinienPrognose, BetriebsMeldungsVerwaltung {

	/** Die Eigenschaft {@code kalender}. */
	private final Kalender kalender;

	/**
	 * Initialisiert das Objekt.
	 *
	 * @param obj
	 *            ein Systemobjekt, welches eine AOE sein muss.
	 */
	public AutarkeOrganisationsEinheit(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine autarke Organisationseinheit.");
		}

		// Test auf Ganglinienprognose, das wir nur das Interface nutzen ohne
		// ein konkretes Objekt.
		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine Ganglinienprognose.");
		}

		kalender = new KalenderImpl(obj);
	}

	@Override
	public void add(final Ereignis ereignis)
			throws ConfigurationChangeException {
		kalender.add(ereignis);
	}

	@Override
	public void add(final EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		kalender.add(ereignisTyp);
	}

	@Override
	public void add(final SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException {
		kalender.add(eintrag);
	}

	@Override
	public void addEreignisListener(final EreignisListener l) {
		kalender.addEreignisListener(l);
	}

	@Override
	public void addEreignisTypListener(final EreignisTypListener l) {
		kalender.addEreignisTypListener(l);
	}

	@Override
	public void addSystemKalenderEintragListener(
			final SystemKalenderEintragListener l) {
		kalender.addSystemKalenderEintragListener(l);
	}

	@Override
	public Set<Ereignis> getEreignisse() {
		return kalender.getEreignisse();
	}

	@Override
	public Set<EreignisTyp> getEreignisTypen() {
		return kalender.getEreignisTypen();
	}

	@Override
	public Set<SystemKalenderEintrag> getSystemKalenderEintraege() {
		return kalender.getSystemKalenderEintraege();
	}

	@Override
	public SystemObjektTyp getTyp() {
		return SystemModellAoeTypen.AUTARKE_ORGANISATIONS_EINHEIT;
	}

	@Override
	public void remove(final Ereignis ereignis)
			throws ConfigurationChangeException {
		kalender.remove(ereignis);
	}

	@Override
	public void remove(final EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		kalender.remove(ereignisTyp);
	}

	@Override
	public void remove(final SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException {
		kalender.remove(eintrag);
	}

	@Override
	public void removeEreignisListener(final EreignisListener l) {
		kalender.removeEreignisListener(l);
	}

	@Override
	public void removeEreignisTypListener(final EreignisTypListener l) {
		kalender.removeEreignisTypListener(l);
	}

	@Override
	public void removeSystemKalenderEintragListener(
			final SystemKalenderEintragListener l) {
		kalender.removeSystemKalenderEintragListener(l);
	}
}
