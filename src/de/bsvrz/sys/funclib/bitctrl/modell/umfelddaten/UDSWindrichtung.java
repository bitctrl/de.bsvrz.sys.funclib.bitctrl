/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straßensubsegmentanalyse
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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Umfelddatensensor, der die Windrichtung misst.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id: Windrichtung.java 1531 2007-06-04 09:27:42Z Schumann $
 */
public class UDSWindrichtung extends AbstractUmfelddatensensor {

	/**
	 * Ruft den Superkonstruktor auf.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, was ein Helligkeitssensor darstellt
	 */
	public UDSWindrichtung(SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return UmfelddatenModelTypen.UDS_WINDRICHTUNG;
	}

}
