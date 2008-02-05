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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.lms;

import java.util.Arrays;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte.LandesMeldeStelle;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte.RdsMeldung;

/**
 * Fabrikmethode f&uuml;r gekapselte Systemobjekte aus dem LMS-Modell. Jedes
 * gekapselte Objekt wird als Singleton behandelt und zwischengespeichert.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class LmsObjektFactory implements ModellObjektFactory {

	/**
	 * {@inheritDoc}
	 */
	public SystemObjekt getModellobjekt(final SystemObject objekt) {
		if (objekt == null) {
			throw new IllegalArgumentException("Argument darf nicht null sein.");
		}

		SystemObjekt obj = null;
		if (objekt.isOfType(LmsModellTypen.LANDESMELDESTELLE.getPid())) {
			obj = new LandesMeldeStelle(objekt);
		} else if (objekt.isOfType(LmsModellTypen.RDSMELDUNG.getPid())) {
			obj = new RdsMeldung(objekt);
		}

		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<? extends SystemObjektTyp> getTypen() {
		return Arrays.asList(LmsModellTypen.values());
	}
}