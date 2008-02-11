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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.Collections;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataAndATGUsageInformation;
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
 * Repr&auml;sentiert ein Ereignis.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class Ereignis extends AbstractSystemObjekt {

	/** Der Standardpr�fix f�r die PID eines neuen Ereignisses. */
	public static final String PRAEFIX_PID = "ergeignis.";

	/**
	 * Legt ein neues Ereignis an.
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param beschreibung
	 *            die Ereignisbeschreibung.
	 * @param ereignisTyp
	 *            der Typ des Ereignisses.
	 * @return das angelegte Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen unzul�ssig ist.
	 */
	public static Ereignis anlegen(String pid, String name,
			String beschreibung, EreignisTyp ereignisTyp)
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

		factory = ObjektFactory.getInstanz();
		dav = factory.getVerbindung();
		modell = dav.getDataModel();
		typ = (DynamicObjectType) modell.getType(KalenderModellTypen.EREIGNIS
				.getPid());
		kb = dav.getLocalConfigurationAuthority().getConfigurationArea();
		atg = modell.getAttributeGroup("atg.ereignisEigenschaften");
		asp = modell.getAspect("asp.eigenschaften");
		atgVerwendung = atg.getAttributeGroupUsage(asp);
		daten = dav.createData(atg);
		daten.getTextValue("Ereignisbeschreibung").setText(beschreibung);
		daten.getReferenceValue("EreignisTypReferenz").setSystemObject(
				ereignisTyp.getSystemObject());
		datenUndVerwendung = new DataAndATGUsageInformation(atgVerwendung,
				daten);

		so = kb.createDynamicObject(typ, pid, name, Collections
				.singleton(datenUndVerwendung));

		return (Ereignis) factory.getModellobjekt(so);
	}

	/** Ein beschreibender Text des Ereignisses. */
	private String beschreibung;

	/** Der Typ des Ereignisses. */
	private EreignisTyp ereignisTyp;

	/**
	 * Konstruiert ein Ereignis aus einem {@code SystemObject}.
	 * 
	 * @param obj
	 *            ein {@code SystemObject}, welches ein Ereignis sein muss.
	 */
	public Ereignis(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Ereignis.");
		}
	}

	/**
	 * L�scht das Objekt in dem es auf "ung�ltig" gesetzt wird.
	 * 
	 * @throws ConfigurationChangeException
	 *             wenn das L�schen nicht zul�ssig ist.
	 */
	public void entfernen() throws ConfigurationChangeException {
		((DynamicObject) getSystemObject()).invalidate();
	}

	/**
	 * Getter der konfigurierten Ereignisbeschreibung.
	 * 
	 * @return der Beschreibungstext.
	 */
	public String getBeschreibung() {
		leseKonfigEigenschaften();
		return beschreibung;
	}

	/**
	 * Getter des konfigurierten Ereignistyps.
	 * 
	 * @return der Typ dieses Ereignisses.
	 */
	public EreignisTyp getEreignisTyp() {
		leseKonfigEigenschaften();
		return ereignisTyp;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.EREIGNIS;
	}

	/**
	 * Liest die konfigurierenden Daten des Objekts bei Bedarf.
	 */
	private void leseKonfigEigenschaften() {
		if (ereignisTyp != null) {
			// Konfiurationsdaten wurden bereits gelesen
			return;
		}

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		modell = objekt.getDataModel();
		atg = modell.getAttributeGroup("atg.ereignisEigenschaften");
		datum = objekt.getConfigurationData(atg);

		if (datum != null) {
			SystemObject so;

			beschreibung = datum.getTextValue("Ereignisbeschreibung").getText();
			so = datum.getReferenceValue("EreignisTypReferenz")
					.getSystemObject();
			ereignisTyp = (EreignisTyp) ObjektFactory.getInstanz()
					.getModellobjekt(so);
		} else {
			beschreibung = null;
			ereignisTyp = null;
		}
	}

}
