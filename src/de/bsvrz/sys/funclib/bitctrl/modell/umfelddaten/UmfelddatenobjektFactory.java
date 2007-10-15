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

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import static de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen.UDS_HELLIGKEIT;
import static de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen.UDS_NIEDERSCHLAGSINTENSITAET;
import static de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen.UDS_SICHTWEITE;
import static de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen.UDS_WINDGESCHWINDIGKEIT;
import static de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen.UDS_WINDRICHTUNG;
import static de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen.UMFELDDATENMESSSTELLE;

import java.util.Arrays;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fabrikmethode f&uuml;r gekapselte Systemobjekte aus dem Umfelddatenmodell.
 * Jedes gekapselte Objekt wird als Singleton behandelt und zwischengespeichert.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class UmfelddatenobjektFactory implements ModellObjektFactory {

	/**
	 * {@inheritDoc}
	 */
	public SystemObjekt getModellobjekt(SystemObject objekt) {
		if (objekt == null) {
			throw new IllegalArgumentException("Argument darf nicht null sein.");
		}

		SystemObjekt obj = null;
		if (objekt.isOfType(UDS_NIEDERSCHLAGSINTENSITAET.getPid())) {
			obj = new UfdsNiederschlagsintensitaet(objekt);
		} else if (objekt.isOfType(UDS_WINDRICHTUNG.getPid())) {
			obj = new UfdsWindrichtung(objekt);
		} else if (objekt.isOfType(UDS_WINDGESCHWINDIGKEIT.getPid())) {
			obj = new UfdsWindgeschwindigkeit(objekt);
		} else if (objekt.isOfType(UDS_SICHTWEITE.getPid())) {
			obj = new UfdsSichtweite(objekt);
		} else if (objekt.isOfType(UDS_HELLIGKEIT.getPid())) {
			obj = new UfdsHelligkeit(objekt);
		} else if (objekt.isOfType(UMFELDDATENMESSSTELLE.getPid())) {
			obj = new elddatenMessstelle(objekt);
		}

		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<? extends SystemObjektTyp> getTypen() {
		return Arrays.asList(UmfelddatenModelTypen.values());
	}

}
