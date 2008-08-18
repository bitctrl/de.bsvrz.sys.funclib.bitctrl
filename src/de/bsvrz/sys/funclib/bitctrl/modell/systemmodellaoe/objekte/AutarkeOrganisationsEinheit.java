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
 * Repr‰sentiert die autarke Organisationseinheit (AOE).
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
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

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#add(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis)
	 */
	public void add(final Ereignis ereignis)
			throws ConfigurationChangeException {
		kalender.add(ereignis);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#add(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp)
	 */
	public void add(final EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		kalender.add(ereignisTyp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#add(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag)
	 */
	public void add(final SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException {
		kalender.add(eintrag);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#addEreignisListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisListener)
	 */
	public void addEreignisListener(final EreignisListener l) {
		kalender.addEreignisListener(l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#addEreignisTypListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTypListener)
	 */
	public void addEreignisTypListener(final EreignisTypListener l) {
		kalender.addEreignisTypListener(l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#addSystemKalenderEintragListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintragListener)
	 */
	public void addSystemKalenderEintragListener(
			final SystemKalenderEintragListener l) {
		kalender.addSystemKalenderEintragListener(l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#getEreignisse()
	 */
	public Set<Ereignis> getEreignisse() {
		return kalender.getEreignisse();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#getEreignisTypen()
	 */
	public Set<EreignisTyp> getEreignisTypen() {
		return kalender.getEreignisTypen();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#getSystemKalenderEintraege()
	 */
	public Set<SystemKalenderEintrag> getSystemKalenderEintraege() {
		return kalender.getSystemKalenderEintraege();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return SystemModellAoeTypen.AUTARKE_ORGANISATIONS_EINHEIT;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#remove(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis)
	 */
	public void remove(final Ereignis ereignis)
			throws ConfigurationChangeException {
		kalender.remove(ereignis);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#remove(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp)
	 */
	public void remove(final EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		kalender.remove(ereignisTyp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#remove(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag)
	 */
	public void remove(final SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException {
		kalender.remove(eintrag);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#removeEreignisListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisListener)
	 */
	public void removeEreignisListener(final EreignisListener l) {
		kalender.removeEreignisListener(l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#removeEreignisTypListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTypListener)
	 */
	public void removeEreignisTypListener(final EreignisTypListener l) {
		kalender.removeEreignisTypListener(l);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#removeSystemKalenderEintragListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintragListener)
	 */
	public void removeSystemKalenderEintragListener(
			final SystemKalenderEintragListener l) {
		kalender.removeSystemKalenderEintragListener(l);
	}

}
