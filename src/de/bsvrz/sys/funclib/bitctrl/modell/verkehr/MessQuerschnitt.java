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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;ssentiert einen Messquerschnitt.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class MessQuerschnitt extends MessQuerschnittAllgemein {

	/** Die Liste der Fahrstreifen am MQ. */
	private List<FahrStreifen> fahrStreifen;

	/**
	 * Erzeugt einen Messquerschnitt aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Messquerschnitt sein muss
	 */
	MessQuerschnitt(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Messquerschnitt.");
		}
	}

	/**
	 * Gibt eine Read-Only-Liste der Fahrstreifen des Messquerschnitts
	 * zur&uuml;ck.
	 * 
	 * @return die Liste der Fahrstreifen.
	 */
	public List<FahrStreifen> getFahrStreifen() {

		if (fahrStreifen == null) {
			fahrStreifen = new ArrayList<FahrStreifen>();
			ConfigurationObject co = (ConfigurationObject) getSystemObject();
			for (SystemObject so : co.getObjectSet("FahrStreifen")
					.getElements()) {
				fahrStreifen.add((FahrStreifen) ObjektFactory.getInstanz()
						.getModellobjekt(so));
			}
		}

		return Collections.unmodifiableList(fahrStreifen);
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.MESSQUERSCHNITT;
	}

}
