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

package de.bsvrz.sys.funclib.bitctrl.modell.mif;

import java.util.Arrays;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.FlaecheXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.KomplexXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.LinieXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Gewaesser;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Kreisgrenzen;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Ortslage;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.objekte.Ortsname;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Factory zum Erzeugen von Objekten, aus dem Mif-Import-Modell.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class MifModellFactory implements ModellObjektFactory {

	/**
	 * Logger für Debug-Ausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory#getModellobjekt(de.bsvrz.dav.daf.main.config.SystemObject)
	 */
	public SystemObjekt getModellobjekt(SystemObject objekt) {
		if (objekt == null) {
			throw new IllegalArgumentException("Argument darf nicht null sein.");
		}

		SystemObjekt obj = null;
		if (objekt.isOfType(MifModellTypen.KREISGRENZEN.getPid())) {
			obj = new Kreisgrenzen(objekt);
		} else if (objekt.isOfType(MifModellTypen.ORTSLAGE.getPid())) {
			obj = new Ortslage(objekt);
		} else if (objekt.isOfType(MifModellTypen.GEWAESSER.getPid())) {
			obj = new Gewaesser(objekt);
		} else if (objekt.isOfType(MifModellTypen.ORTSNAME.getPid())) {
			obj = new Ortsname(objekt);
		}

		if (obj == null) {
			LOGGER.error("Für das Ohbjekt: " + objekt
					+ " konnte kein Systemobjekt angelegt werden");
		}

		return obj;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory#getTypen()
	 */
	public Collection<? extends SystemObjektTyp> getTypen() {
		return Arrays.asList(MifModellTypen.values());
	}

}
