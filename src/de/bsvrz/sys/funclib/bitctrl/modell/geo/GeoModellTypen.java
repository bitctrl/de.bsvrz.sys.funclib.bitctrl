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

package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Definition der Typen, die von {@link GeoModellFactory} angelegt werden
 * können.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public enum GeoModellTypen implements SystemObjektTyp {

	/** komplexes Objekt. */
	KOMPLEX_XY("typ.komplexXY", KomplexXYImpl.class),

	/** Eine Fläche. */
	FLAECHE_XY("typ.flächeXY", FlaecheXYImpl.class),

	/** Ein Punkt. */
	PUNKT_XY("typ.punktXY", PunktXYImpl.class),

	/** Eine Linie. */
	LINIE_XY("typ.linieXY", LinieXYImpl.class),

	/** die Grenzen eines Kreise. */
	KREISGRENZEN("typ.Kreis", Kreisgrenzen.class),

	/** die Grenzen eines Ortes. */
	ORTSLAGE("typ.Ortslage", Ortslage.class),

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
	private GeoModellTypen(String pid, Class<? extends SystemObjekt> klasse) {
		this.pid = pid;
		this.klasse = klasse;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends SystemObjekt> getKlasse() {
		return klasse;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return pid;
	}
}
