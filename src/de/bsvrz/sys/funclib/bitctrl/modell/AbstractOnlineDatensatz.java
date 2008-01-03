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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.Aspect;

/**
 * Implementiert gemeinsame Funktionen von Onlinedatens&auml;tzen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public abstract class AbstractOnlineDatensatz<T extends Datum> extends
		AbstractDatensatz<T> implements OnlineDatensatz<T> {

	/** Flag ob dieser Datensatz als Quelle angemeldet werden soll. */
	private final Set<Aspect> quellen;

	/** Flag ob dieser Datensatz als Senke angemeldet werden soll. */
	private final Set<Aspect> senken;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            das Objekt dem der Datensatz zugeordnet ist.
	 */
	public AbstractOnlineDatensatz(SystemObjekt objekt) {
		super(objekt);
		quellen = new HashSet<Aspect>();
		senken = new HashSet<Aspect>();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#abmeldenSender(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public void abmeldenSender(Aspect asp) {
		super.abmeldenSender(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#addUpdateListener(de.bsvrz.dav.daf.main.config.Aspect,
	 *      de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener)
	 */
	@Override
	public void addUpdateListener(Aspect asp, DatensatzUpdateListener listener) {
		super.addUpdateListener(asp, listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#anmeldenSender(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public void anmeldenSender(Aspect asp) throws AnmeldeException {
		super.anmeldenSender(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getDatum(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public T getDatum(Aspect asp) {
		return super.getDatum(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getStatusSendesteuerung(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public Status getStatusSendesteuerung(Aspect asp) {
		return super.getStatusSendesteuerung(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#isAngemeldetSender(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public boolean isAngemeldetSender(Aspect asp) {
		return super.isAngemeldetSender(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#isAutoUpdate(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public boolean isAutoUpdate(Aspect asp) {
		return super.isAutoUpdate(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#isQuelle(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public boolean isQuelle(Aspect asp) {
		return quellen.contains(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#isSenke(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public boolean isSenke(Aspect asp) {
		return senken.contains(asp);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#removeUpdateListener(de.bsvrz.dav.daf.main.config.Aspect,
	 *      de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener)
	 */
	@Override
	public void removeUpdateListener(Aspect asp,
			DatensatzUpdateListener listener) {
		super.removeUpdateListener(asp, listener);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#sendeDaten(de.bsvrz.dav.daf.main.config.Aspect,
	 *      de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	public void sendeDaten(Aspect asp, T datum) throws DatensendeException {
		super.sendeDaten(asp, datum);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#sendeDaten(de.bsvrz.dav.daf.main.config.Aspect,
	 *      de.bsvrz.sys.funclib.bitctrl.modell.Datum, long)
	 */
	@Override
	public void sendeDaten(Aspect asp, T datum, long timeout)
			throws DatensendeException {
		super.sendeDaten(asp, datum, timeout);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.OnlineDatensatz#setQuelle(de.bsvrz.dav.daf.main.config.Aspect,
	 *      boolean)
	 */
	public void setQuelle(Aspect asp, boolean quelle) {
		if (quelle) {
			quellen.add(asp);
		} else {
			quellen.remove(asp);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.OnlineDatensatz#setSenke(de.bsvrz.dav.daf.main.config.Aspect,
	 *      boolean)
	 */
	public void setSenke(Aspect asp, boolean senke) {
		if (senke) {
			senken.add(asp);
		} else {
			senken.remove(asp);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#update(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public void update(Aspect asp) {
		super.update(asp);
	}

}
