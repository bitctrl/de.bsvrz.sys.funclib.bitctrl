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

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal;

import java.util.Arrays;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte.BetriebsMeldungsVerwaltungImpl;

/**
 * Fabrikmethode für gekapselte Systemobjekte aus dem Teilmodell
 * VeWBetriebGlobal. Jedes gekapselte Objekt wird als Singleton behandelt und
 * zwischengespeichert.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class VeWBetriebGlobalObjektFactory implements ModellObjektFactory {

	@Override
	public SystemObjekt getModellobjekt(final SystemObject objekt) {
		if (objekt == null) {
			throw new IllegalArgumentException(
					"Argument darf nicht null sein.");
		}

		SystemObjekt obj = null;
		if (objekt.isOfType(
				VeWBetriebGlobalTypen.BetriebsMeldungsVerwaltung.getPid())) {
			obj = new BetriebsMeldungsVerwaltungImpl(objekt);
		}

		return obj;
	}

	@Override
	public Collection<? extends SystemObjektTyp> getTypen() {
		return Arrays.asList(VeWBetriebGlobalTypen.values());
	}
}
