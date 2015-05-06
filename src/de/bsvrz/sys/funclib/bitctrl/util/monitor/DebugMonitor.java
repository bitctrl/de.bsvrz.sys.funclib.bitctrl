/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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

package de.bsvrz.sys.funclib.bitctrl.util.monitor;

import com.bitctrl.util.monitor.AbstractMonitor;

import de.bsvrz.sys.funclib.debug.Debug;

public class DebugMonitor extends AbstractMonitor {

	private final Debug log = Debug.getLogger();
	private String name;
	private boolean canceled;
	private int leftWork;
	private double totalWork;
	private double allWork = 0.0;

	public void beginTask(final String name, final int totalWork) {
		this.name = name;
		log.info("DebugMonitor : " + name + " : beginTask(" + totalWork + ")");
		leftWork = totalWork;
		this.totalWork = totalWork;
	}

	public void done() {
		log.info("DebugMonitor : " + name + " : done");
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(final boolean canceled) {
		this.canceled = canceled;
		log.info("DebugMonitor : " + name + " : setCanceled(" + canceled + ")");
	}

	public void setTaskName(final String name) {
		log.info(
				"DebugMonitor : " + this.name + " : setTaskName(" + name + ")");
		this.name = name;

	}

	public void subTask(final String name) {
		log.fine("DebugMonitor : " + this.name + " : subTask(" + name + ")");
	}

	public void worked(final int work) {
		leftWork -= work;
		log.fine("DebugMonitor : " + name + " : worked(" + work + ") : "
				+ leftWork + " left");
		if (totalWork > 0) {
			allWork += work;
			notifyMonitorListeners(allWork / totalWork);
		}
	}

}
