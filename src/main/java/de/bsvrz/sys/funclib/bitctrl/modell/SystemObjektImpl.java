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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Generischer Wrapper für unbekannte Systemobjekte. Diese Klasse dient nur als
 * Rückfallebene bis die entsprechenden Typen im Modell implementiert sind.
 *
 * @author BitCtrl Systems GmbH, Schumann
 * @deprecated Diese Klasse ist per Definition deprecated, Referenzen sollten
 *             möglichst durch echte Wrapper ersetzt werden.
 */
@Deprecated
public class SystemObjektImpl extends AbstractSystemObjekt {

	/**
	 * Initialisierung.
	 *
	 * @param obj
	 *            ein beliebiges Systemobjekt.
	 */
	public SystemObjektImpl(final SystemObject obj) {
		super(obj);
	}

	/**
	 * Gibt nur rudimentäre Typinformation zurück, also ob es sich um ein
	 * Konfigurationsobjekt oder ein dynamisches handelt.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return SystemObjekt.class;
			}

			@Override
			public String getPid() {
				if (getSystemObject().isOfType("typ.konfigurationsObjekt")) {
					return "typ.konfigurationsObjekt";
				} else
					if (getSystemObject().isOfType("typ.dynamischesObjekt")) {
						return "typ.dynamischesObjekt";
					}
				return getSystemObject().getType().getPid();
			}

		};
	}

}
