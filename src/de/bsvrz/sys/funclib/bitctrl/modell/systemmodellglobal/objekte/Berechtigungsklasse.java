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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen;

/**
 * Repräsentiert die Berechtigungsklasse eines Benutzers des Datenverteilers.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class Berechtigungsklasse extends AbstractSystemObjekt {

	/**
	 * Konstruktor zum Anlegen eines Systemobjekt, das ein Applikationsobjekt
	 * {@link SystemModellGlobalTypen#BERECHTIGUNGSKLASSE} in der
	 * Datenverteiler-Konfiguration repräsentiert.
	 * 
	 * @param obj
	 *            das Objekt in der Konfiguration des Datenverteilers
	 */
	public Berechtigungsklasse(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return SystemModellGlobalTypen.Berechtigungsklasse;
	}

}
