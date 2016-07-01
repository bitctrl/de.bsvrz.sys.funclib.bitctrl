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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.KalenderImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag;

/**
 * Fasst alle Objekttypen im Kalendermodell zusammen.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public enum KalenderModellTypen implements SystemObjektTyp {

	/** Ein Kalender. */
	KALENDER("typ.kalender", KalenderImpl.class),

	/** Ein Ereignistyp. */
	EREIGNISTYP("typ.ereignisTyp", EreignisTyp.class),

	/** Ein Ereignis. */
	EREIGNIS("typ.ereignis", Ereignis.class),

	/** Ein Systemkalendereintrag. */
	SYSTEM_KALENDER_EINTRAG("typ.systemKalenderEintrag",
			SystemKalenderEintrag.class);

	/** PID des Objekttyps im Datenverteiler. */
	private final String pid;

	/** Klasse des Systemobjekts im Modell. */
	private final Class<? extends SystemObjekt> klasse;

	/**
	 * Privater Konstruktor.
	 *
	 * @param pid
	 *            Die PID des Typs
	 * @param klasse
	 *            Die Klasse des Modellobjekts
	 */
	KalenderModellTypen(final String pid,
			final Class<? extends SystemObjekt> klasse) {
		this.pid = pid;
		this.klasse = klasse;
	}

	@Override
	public Class<? extends SystemObjekt> getKlasse() {
		return klasse;
	}

	@Override
	public String getPid() {
		return pid;
	}

}
