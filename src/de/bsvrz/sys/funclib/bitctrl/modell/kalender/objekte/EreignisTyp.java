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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataAndATGUsageInformation;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.AttributeGroupUsage;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.KalenderModellTypen;

/**
 * Repr&auml;ssentiert den Typ eines Ereignisses.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class EreignisTyp extends AbstractSystemObjekt {

	/** Der Standardpräfix ({@value}) für die PID eines neuen Ereignistyps. */
	public static final String PRAEFIX_PID = "ereignisTyp.";

	/**
	 * Legt einen neuen Ereignistyp an.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @return der angelegte Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen unzulässig ist.
	 */
	public static EreignisTyp anlegen(final String pid, final String name)
			throws ConfigurationChangeException {
		return anlegen(pid, name, new HashMap<String, String>());
	}

	/**
	 * Legt einen neuen Ereignistyp an.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param attribute
	 *            eine Liste von zusätzlichen Attributname/Attributwert-Paaren.
	 * @return der angelegte Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen unzulässig ist.
	 */
	public static EreignisTyp anlegen(final String pid, final String name,
			final Map<String, String> attribute)
			throws ConfigurationChangeException {
		ObjektFactory factory;
		ClientDavInterface dav;
		DataModel modell;
		ConfigurationArea kb;
		DynamicObjectType typ;
		SystemObject so;
		DataAndATGUsageInformation datenUndVerwendung;
		AttributeGroupUsage atgVerwendung;
		Data daten;
		AttributeGroup atg;
		Aspect asp;
		Array feld;
		int i;

		factory = ObjektFactory.getInstanz();
		dav = factory.getVerbindung();
		modell = dav.getDataModel();
		typ = (DynamicObjectType) modell
				.getType(KalenderModellTypen.EREIGNISTYP.getPid());
		kb = dav.getLocalConfigurationAuthority().getConfigurationArea();
		atg = modell.getAttributeGroup("atg.ereignisTypEigenschaften");
		asp = modell.getAspect("asp.eigenschaften");
		atgVerwendung = atg.getAttributeGroupUsage(asp);

		daten = dav.createData(atg);
		feld = daten.getArray("ZusätzlicheAttribute");
		feld.setLength(attribute.size());
		i = 0;
		for (final Entry<String, String> entry : attribute.entrySet()) {
			feld.getItem(i).getTextValue("Attributname")
					.setText(entry.getKey());
			feld.getItem(i).getTextValue("Attributwert").setText(
					entry.getValue());
			++i;
		}

		datenUndVerwendung = new DataAndATGUsageInformation(atgVerwendung,
				daten);
		so = kb.createDynamicObject(typ, pid, name, Collections
				.singleton(datenUndVerwendung));

		return (EreignisTyp) factory.getModellobjekt(so);
	}

	/**
	 * Erzeugt einen Messquerschnitt aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Messquerschnitt sein muss
	 * @throws IllegalArgumentException
	 */
	public EreignisTyp(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Ereignistyp.");
		}
	}

	/**
	 * Löscht das Objekt in dem es auf "ungültig" gesetzt wird.
	 * 
	 * @throws ConfigurationChangeException
	 *             wenn das Löschen nicht zulässig ist.
	 */
	public void entfernen() throws ConfigurationChangeException {
		((DynamicObject) getSystemObject()).invalidate();
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.EREIGNISTYP;
	}

}
