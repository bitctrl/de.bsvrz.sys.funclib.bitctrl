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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.Arrays;
import java.util.Collection;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Fabrikmethode f&uuml;r gekapselte Systemobjekte aus dem Verkehrsmodell. Jedes
 * gekapselte Objekt wird als Singleton behandelt und zwischengespeichert.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class VerkehrsobjektFactory implements ModellObjektFactory {

	/**
	 * {@inheritDoc}
	 */
	public SystemObjekt getModellobjekt(final SystemObject objekt) {
		if (objekt == null) {
			throw new IllegalArgumentException("Argument darf nicht null sein.");
		}

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
		} else if (objekt.isOfType(VerkehrsModellTypen.BAUSTELLE.getPid())) {
			obj = new Baustelle(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.STAU.getPid())) {
			obj = new Stau(objekt);
		} else if (objekt.isOfType(VerkehrsModellTypen.FAHRSTREIFEN.getPid())) {
			obj = new FahrStreifen(objekt);
		}

		return obj;
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<? extends SystemObjektTyp> getTypen() {
		return Arrays.asList(VerkehrsModellTypen.values());
	}
}
