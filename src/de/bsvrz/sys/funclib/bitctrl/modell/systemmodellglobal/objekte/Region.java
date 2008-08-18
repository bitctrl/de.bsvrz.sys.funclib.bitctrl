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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen;

/**
 * Repräsentiert eine Region des Datenverteilers im Sinne der Nutzerrechte.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class Region extends AbstractSystemObjekt {

	/** Die Liste der betroffenen Objekte. */
	private List<SystemObjekt> objekte;

	/** Die Liste der betroffenen Zusammmenstellungen. */
	private List<SystemObject> zusammmenstellungen;

	/**
	 * Konstruktor zum Anlegen eines Systemobjekt, das ein Applikationsobjekt
	 * {@link SystemModellGlobalTypen#REGION} in der
	 * Datenverteiler-Konfiguration repräsentiert.
	 * 
	 * @param obj
	 *            das Objekt in der Konfiguration des Datenverteilers
	 */
	public Region(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return SystemModellGlobalTypen.Region;
	}

	/**
	 * Gibt eine Read-Only-Liste der Aktivitäten zurück, die diese Rolle
	 * umfasst.
	 * 
	 * @return die Liste der betroffenen Aspekte.
	 */
	public List<SystemObjekt> getObjekte() {
		if (objekte == null) {
			objekte = new ArrayList<SystemObjekt>();
			final ConfigurationObject co = (ConfigurationObject) getSystemObject();
			for (final SystemObject so : co.getObjectSet("Objekte")
					.getElements()) {
				objekte.add(ObjektFactory.getInstanz().getModellobjekt(so));
			}
		}

		return Collections.unmodifiableList(objekte);
	}

	/**
	 * Gibt eine Read-Only-Liste der Aktivitäten zurück, die diese Rolle
	 * umfasst.
	 * 
	 * @return die Liste der betroffenen Aspekte.
	 */
	public List<SystemObject> getZusammmenstellungen() {
		if (zusammmenstellungen == null) {
			zusammmenstellungen = new ArrayList<SystemObject>();
			final ConfigurationObject co = (ConfigurationObject) getSystemObject();
			for (final SystemObject so : co.getObjectSet("Zusammmenstellungen")
					.getElements()) {
				zusammmenstellungen.add(so);
			}
		}

		return Collections.unmodifiableList(zusammmenstellungen);
	}

}
