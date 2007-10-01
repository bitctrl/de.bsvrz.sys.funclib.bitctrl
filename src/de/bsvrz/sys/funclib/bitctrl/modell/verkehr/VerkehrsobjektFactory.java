/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Stra�ensubsegmentanalyse
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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fabrikmethode f&uuml;r gekapselte Systemobjekte aus dem Verkehrsmodell. Jedes
 * gekapselte Objekt wird als Singleton behandelt und zwischengespeichert.
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public class VerkehrsobjektFactory implements ModellObjektFactory {

	/** Globaler Cache f&uuml;r Systemobjekte des Verkehrmodells. */
	private static Map<SystemObject, SystemObjekt> objekte;

	/**
	 * {@inheritDoc}
	 */
	public SystemObjekt getInstanz(SystemObject objekt) {
		if (objekt == null) {
			return null;
		}

		if (objekte == null) {
			objekte = new HashMap<SystemObject, SystemObjekt>();
		}

		// Gesuchtes Objekt im Cache?
		if (objekte.containsKey(objekt)) {
			return objekte.get(objekt);
		}

		// Objekt neu anlegen
		SystemObjekt obj = null;
		if (objekt.isOfType(VerkehrsModellTypen.VERKEHRSMODELLNETZ.getPid())) {
			obj = new VerkehrModellNetz(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.NETZ.getPid())) {
			obj = new Netz(objekt);
		} else if (objekt
				.isOfType(VerkehrsModellTypen.MESSQUERSCHNITT.getPid())) {
			obj = new MessQuerschnitt(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.MESSQUERSCHNITTVIRTUELL
				.getPid())) {
			obj = new MessQuerschnittVirtuell(objekt);
		} else if (objekt
				.isOfType(VerkehrsModellTypen.AUESSERES_STRASSENSEGMENT
						.getPid())) {
			obj = new AeusseresStrassenSegment(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.INNERES_STRASSENSEGMENT
				.getPid())) {
			obj = new InneresStrassenSegment(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.STRASSENTEILSEGMENT
				.getPid())) {
			obj = new StrassenTeilSegment(objekt);
		} else if (objekt
				.isOfType(VerkehrsModellTypen.STRASSENSEGMENT.getPid())) {
			obj = new StrassenSegment(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.STRASSENKNOTEN.getPid())) {
			obj = new StrassenKnoten(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.STRASSE.getPid())) {
			obj = new Strasse(objekt);
		}

		if (obj != null) {
			// Nur konkrete Objekte d�rfen in den Cache
			objekte.put(objekt, obj);
		}

		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SystemObjekt> getInstanzen() {
		return new ArrayList<SystemObjekt>(objekte.values());
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp[] getTypen() {
		return VerkehrsModellTypen.values();
	}
}
