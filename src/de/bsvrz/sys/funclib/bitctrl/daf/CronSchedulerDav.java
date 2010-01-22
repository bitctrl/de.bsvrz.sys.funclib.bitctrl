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

package de.bsvrz.sys.funclib.bitctrl.daf;

import com.bitctrl.util.CronScheduler;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Erweitert den allgemeinen Scheduler für die Zusammenarbeit mit dem
 * Datenverteiler.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class CronSchedulerDav extends CronScheduler {

	/** Die zu verwendende Datenverteilerverbindung. */
	private final ClientDavInterface verbindung;

	/**
	 * Erzeugt einen Scheduler, der kein Daemon ist.
	 * 
	 * @param verbindung
	 *            die zu verwendende Datenverteilerverbindung.
	 */
	public CronSchedulerDav(final ClientDavInterface verbindung) {
		this(verbindung, false);
	}

	/**
	 * Erzeugt einen Scheduler.
	 * 
	 * @param verbindung
	 *            die zu verwendende Datenverteilerverbindung.
	 * @param daemon
	 *            {@code true}, wenn der Scheduler-Thread als Daemon laufen
	 *            soll.
	 */
	public CronSchedulerDav(final ClientDavInterface verbindung,
			final boolean daemon) {
		super(daemon);
		if (verbindung == null) {
			throw new NullPointerException("Verbindung darf nicht null sein.");
		}
		this.verbindung = verbindung;
	}

	/**
	 * Verwendet {@link ClientDavInterface#getTime()}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public long getTime() {
		return verbindung.getTime();
	}

	/**
	 * Verwendet {@link ClientDavInterface#sleep(long)}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void sleep(final long millis) throws InterruptedException {
		verbindung.sleep(millis);
	}

}
