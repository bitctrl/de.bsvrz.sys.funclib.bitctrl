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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Generischer Wrapper f�r unbekannte Systemobjekttypen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 * @deprecated Diese Klasse ist per Definition deprecated, Referenzen sollten
 *             m�glichst durch echte Wrapper ersetzt werden.
 */
@Deprecated
public class SystemObjektTypImpl extends AbstractSystemObjekt implements
		SystemObjektTyp {

	/**
	 * Initialisierung.
	 * 
	 * @param obj
	 *            ein beliebiges Systemobjekt, welches einen Systemobjekttyp
	 *            darstellt.
	 */
	public SystemObjektTypImpl(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends SystemObjekt> getKlasse() {
		return SystemObjekt.class;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return new SystemObjektTypImpl(getSystemObject().getType());
	}

}
