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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.KalenderModellTypen;

/**
 * Repr‰sentiert einen Systemkalendereintrag.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class SystemKalenderEintrag extends AbstractSystemObjekt {

	/**
	 * Legt einen neuen Systemkalendereintrag an.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @return der angelegte Systemkalendereintrag.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen unzul‰ssig ist.
	 */
	public static SystemKalenderEintrag anlegen(String pid, String name)
			throws ConfigurationChangeException {
		ObjektFactory factory;
		ClientDavInterface dav;
		DataModel modell;
		ConfigurationArea kb;
		DynamicObjectType typ;
		SystemObject so;

		factory = ObjektFactory.getInstanz();
		dav = factory.getVerbindung();
		modell = dav.getDataModel();
		typ = (DynamicObjectType) modell
				.getType(KalenderModellTypen.SYSTEM_KALENDER_EINTRAG.getPid());
		kb = dav.getLocalConfigurationAuthority().getConfigurationArea();

		so = kb.createDynamicObject(typ, pid, name);

		return (SystemKalenderEintrag) factory.getModellobjekt(so);
	}

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param obj
	 *            ein Systemkalendereintrag.
	 */
	public SystemKalenderEintrag(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Systemkalendereintrag.");
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.SYSTEM_KALENDER_EINTRAG;
	}

}
