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

package de.bsvrz.sys.funclib.bitctrl.modell.mif;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Gewaesser;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Kreisgrenzen;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Ortslage;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Ortsname;

/**
 * Definition der Typen, die von {@link MifModellFactory} angelegt werden
 * können.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public enum MifModellTypen implements SystemObjektTyp {

	/** die Grenzen eines Kreise. */
	KREISGRENZEN("typ.Kreis", Kreisgrenzen.class),

	/** die Grenzen eines Ortes. */
	ORTSLAGE("typ.Ortslage", Ortslage.class),

	/** die Grenzen eines Gewässers. */
	GEWAESSER("typ.gewässer", Gewaesser.class),

	/** Eine Ortsbezeichung. */
	ORTSNAME("typ.Ortsname", Ortsname.class);

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
	MifModellTypen(final String pid,
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
