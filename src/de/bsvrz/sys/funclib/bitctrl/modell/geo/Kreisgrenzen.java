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

package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Implementierung eines Objekts vom Typ "typ.KreisGrenzen", mit dem die Grenzen
 * eines Landkreises innerhalb der Datenverteiler-Konfiguration abgelegt sind.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class Kreisgrenzen extends KomplexXYImpl {

	/**
	 * Standardkonstruktor.
	 * 
	 * @param obj
	 *            das Objekt mit dem der Kreis innerhalb der
	 *            Datenverteiler-Konfiguration repr‰sentiert ist
	 */
	protected Kreisgrenzen(SystemObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.KomplexXYImpl#getTyp()
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.KREISGRENZEN;
	}

}
