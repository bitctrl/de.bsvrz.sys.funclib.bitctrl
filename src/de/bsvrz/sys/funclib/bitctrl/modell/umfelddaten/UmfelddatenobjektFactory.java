/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fabrikmethode f&uuml;r gekapselte Systemobjekte aus dem Umfelddatenmodell.
 * Jedes gekapselte Objekt wird als Singleton behandelt und zwischengespeichert.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public final class UmfelddatenobjektFactory implements ModellObjektFactory {

	/** Globaler Cache f&uuml;r Systemobjekte. */
	private static Map<SystemObject, SystemObjekt> objekte;

	/**
	 * {@inheritDoc}
	 */
	public List<SystemObjekt> getInstanzen() {
		return new ArrayList<SystemObjekt>(objekte.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjekt getInstanz(SystemObject objekt) {
		if (objekte == null) {
			objekte = new HashMap<SystemObject, SystemObjekt>();
		}

		// Gesuchtes Objekt im Cache?
		if (objekte.containsKey(objekt)) {
			return objekte.get(objekt);
		}

		// Objekt neu anlegen
		SystemObjekt obj = null;
		if (objekt.isOfType(UDS_NIEDERSCHLAGSINTENSITAET.getPid())) {
			obj = new UDSNiederschlagsintensitaet(objekt);
		} else if (objekt.isOfType(UDS_WINDRICHTUNG.getPid())) {
			obj = new UDSWindrichtung(objekt);
		} else if (objekt.isOfType(UDS_WINDGESCHWINDIGKEIT.getPid())) {
			obj = new UDSWindgeschwindigkeit(objekt);
		} else if (objekt.isOfType(UDS_SICHTWEITE.getPid())) {
			obj = new UDSSichtweite(objekt);
		} else if (objekt.isOfType(UDS_HELLIGKEIT.getPid())) {
			obj = new UDSHelligkeit(objekt);
		} else if (objekt.isOfType(UMFELDDATENMESSSTELLE.getPid())) {
			obj = new UmfelddatenMessstelle(objekt);
		}

		if (obj != null) {
			// Nur konkrete Objekte d¸rfen in den Cache
			objekte.put(objekt, obj);
		}

		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp[] getTypen() {
		return UmfelddatenModelTypen.values();
	}

}
