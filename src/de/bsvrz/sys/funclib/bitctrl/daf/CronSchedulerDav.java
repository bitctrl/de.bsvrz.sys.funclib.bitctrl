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
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;

/**
 * Erweitert den allgemeinen Scheduler für die Zusammenarbeit mit dem
 * Datenverteiler.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class CronSchedulerDav extends CronScheduler {

	/** Die zu verwendende Datenverteilerverbindung. */
	private ClientDavInterface verbindung;

	/**
	 * Erzeugt einen Scheduler. Entspricht {@code new CronSchedulerDav(false)}.
	 * 
	 * @see #CronSchedulerDav(boolean)
	 */
	public CronSchedulerDav() {
		this(false);
	}

	/**
	 * Erzeugt einen Scheduler.
	 * 
	 * @param daemon
	 *            {@code true}, wenn der Scheduler-Thread als Daemon laufen
	 *            soll.
	 */
	public CronSchedulerDav(final boolean daemon) {
		super(daemon);
	}

	/**
	 * Verwendet {@link ClientDavInterface#getTime()}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public long getTime() {
		if (verbindung != null) {
			return verbindung.getTime();
		}

		return ObjektFactory.getInstanz().getVerbindung().getTime();
	}

	/**
	 * Legt die zu verwendende Datenverteilerverbindung fest. Wird hier keine
	 * festgelegt, wird die von {@link ObjektFactory#getVerbindung()} verwendet.
	 * 
	 * @param verbindung
	 *            eine Datenverteilerverbindung.
	 */
	public void setVerbindung(final ClientDavInterface verbindung) {
		if (verbindung != null) {
			throw new IllegalStateException(
					"Die Verbindung wurde bereits festgelegt.");
		}

		this.verbindung = verbindung;
	}

	/**
	 * Verwendet {@link ClientDavInterface#sleep(long)}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void sleep(final long millis) throws InterruptedException {
		if (verbindung != null) {
			verbindung.sleep(millis);
		} else {
			ObjektFactory.getInstanz().getVerbindung().sleep(millis);
		}
	}

}
