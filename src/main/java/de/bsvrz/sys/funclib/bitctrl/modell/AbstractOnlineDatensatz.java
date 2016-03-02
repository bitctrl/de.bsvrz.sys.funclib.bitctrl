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
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public abstract class AbstractOnlineDatensatz<T extends Datum>
extends AbstractDatensatz<T>implements OnlineDatensatz<T> {

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
	public AbstractOnlineDatensatz(final SystemObjekt objekt) {
		super(objekt);
		quellen = new HashSet<Aspect>();
		senken = new HashSet<Aspect>();
	}

	@Override
	public void abmeldenSender(final Aspect asp) {
		super.abmeldenSender(asp);
	}

	@Override
	public T abrufenDatum(final Aspect asp) {
		return super.abrufenDatum(asp);
	}

	@Override
	public void addUpdateListener(final Aspect asp,
			final DatensatzUpdateListener listener) {
		super.addUpdateListener(asp, listener);
	}

	@Override
	public void anmeldenSender(final Aspect asp) throws AnmeldeException {
		super.anmeldenSender(asp);
	}

	@Override
	public T getDatum(final Aspect asp) {
		return super.getDatum(asp);
	}

	@Override
	public Status getStatusSendesteuerung(final Aspect asp) {
		return super.getStatusSendesteuerung(asp);
	}

	@Override
	public boolean isAngemeldetSender(final Aspect asp) {
		return super.isAngemeldetSender(asp);
	}

	@Override
	public boolean isAutoUpdate(final Aspect asp) {
		return super.isAutoUpdate(asp);
	}

	@Override
	public boolean isQuelle(final Aspect asp) {
		return quellen.contains(asp);
	}

	@Override
	public boolean isSenke(final Aspect asp) {
		return senken.contains(asp);
	}

	@Override
	public void removeUpdateListener(final Aspect asp,
			final DatensatzUpdateListener listener) {
		super.removeUpdateListener(asp, listener);
	}

	@Override
	public void sendeDaten(final Aspect asp, final T datum)
			throws DatensendeException {
		super.sendeDaten(asp, datum);
	}

	@Override
	public void sendeDaten(final Aspect asp, final T datum, final long timeout)
			throws DatensendeException {
		super.sendeDaten(asp, datum, timeout);
	}

	@Override
	public void setQuelle(final Aspect asp, final boolean quelle) {
		if (quelle) {
			quellen.add(asp);
		} else {
			quellen.remove(asp);
		}
	}

	@Override
	public void setSenke(final Aspect asp, final boolean senke) {
		if (senke) {
			senken.add(asp);
		} else {
			senken.remove(asp);
		}
	}

}
