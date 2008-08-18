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
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen;

/**
 * Repräsentiert einen Benutzer des Datenverteilers.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class Benutzer extends AbstractSystemObjekt {

	/** Der Standardpräfix für die PID eines neuen Benutzer. */
	public static final String PRAEFIX_PID = "benutzer.";

	/**
	 * Legt einen Benutzer an. Es wird nur das Objekt angelegt! Der Nutzer wird
	 * nicht im System registriert!
	 * 
	 * @param pid
	 *            die PID.
	 * @param name
	 *            der Name.
	 * @param vorname
	 *            der Vorname des Benutzers.
	 * @param zweiterVorname
	 *            der zweite Vorname des Benutzers oder {@code null}, wenn es
	 *            keinen gibt.
	 * @param nachname
	 *            der Nachname des Benutzers.
	 * @param organisation
	 *            der Name der Verwaltung oder Firma zu der der Benutzer gehört.
	 * @param emailAdresse
	 *            die E-Mail-Adresse des Benutzers.
	 * @return der angelegte Benutzer.
	 * @throws ConfigurationChangeException
	 *             wenn das Anlegen unzulässig ist.
	 * @see de.bsvrz.dav.daf.main.config.management.UserAdministration
	 * @see de.bsvrz.dav.daf.main.config.DataModel#getUserAdministration()
	 */
	public static Benutzer anlegen(final String pid, final String name,
			final String vorname, final String zweiterVorname,
			final String nachname, final String organisation,
			final String emailAdresse) throws ConfigurationChangeException {
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
		typ = (DynamicObjectType) modell
				.getType(SystemModellGlobalTypen.Benutzer.getPid());
		kb = dav.getLocalConfigurationAuthority().getConfigurationArea();
		atg = modell.getAttributeGroup("atg.benutzerEigenschaften");
		asp = modell.getAspect("asp.eigenschaften");
		atgVerwendung = atg.getAttributeGroupUsage(asp);
		daten = dav.createData(atg);
		daten.getTextValue("vorname").setText(vorname);
		daten.getTextValue("zweiterVorname").setText(zweiterVorname);
		daten.getTextValue("nachname").setText(nachname);
		daten.getTextValue("organisation").setText(organisation);
		daten.getTextValue("emailAdresse").setText(emailAdresse);
		datenUndVerwendung = new DataAndATGUsageInformation(atgVerwendung,
				daten);
		so = kb.createDynamicObject(typ, pid, name, Collections
				.singleton(datenUndVerwendung));

		return (Benutzer) factory.getModellobjekt(so);
	}

	/** Der Vorname des Benutzers. */
	private String vorname;

	/** Der zweite Vorname des Benutzers oder {@code null}, wenn es keinen gibt. */
	private String zweiterVorname;

	/** Der Nachname des Benutzers. */
	private String nachname;

	/** Der Name der Verwaltung oder Firma zu der der Benutzer gehört. */
	private String organisation;

	/** Die E-Mail-Adresse des Benutzers. */
	private String emailAdresse;

	/** Flag, ob die Konfigurationsdaten bereits gelesen wurden. */
	private boolean konfigDatenGelesen = false;

	/**
	 * Konstruktor zum Anlegen eines Systemobjekt, das ein Applikationsobjekt
	 * {@link SystemModellGlobalTypen#BENUTZER} in der
	 * Datenverteiler-Konfiguration repräsentiert.
	 * 
	 * @param obj
	 *            das Objekt in der Konfiguration des Datenverteilers
	 */
	public Benutzer(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return SystemModellGlobalTypen.Benutzer;
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
	 * Gibt den Vornamen des Benutzers zurück.
	 * 
	 * @return der Vorname des Benutzers.
	 */
	public String getVorname() {
		leseKonfigEigenschaften();
		return vorname;
	}

	/**
	 * Gibt den zwieten Vornamen des Benutzers zurück.
	 * 
	 * @return der zweite Vorname des Benutzers oder {@code null}, wenn es
	 *         keinen gibt.
	 */
	public String getZweiterVorname() {
		leseKonfigEigenschaften();
		return zweiterVorname;
	}

	/**
	 * Gibt den Nachnamen des Benutzers zurück.
	 * 
	 * @return der Nachname des Benutzers.
	 */
	public String getNachname() {
		leseKonfigEigenschaften();
		return nachname;
	}

	/**
	 * Gibt den Namen der Verwaltung oder Firma, zu der der Benutzer gehört,
	 * zurück.
	 * 
	 * @return der Name der Verwaltung oder Firma zu der der Benutzer gehört.
	 */
	public String getOrganisation() {
		leseKonfigEigenschaften();
		return organisation;
	}

	/**
	 * Gibt die E-Mail-Adresse des Benutzers zurück.
	 * 
	 * @return die E-Mail-Adresse des Benutzers.
	 */
	public String getEmailAdresse() {
		leseKonfigEigenschaften();
		return emailAdresse;
	}

	/**
	 * Liest die konfigurierenden Daten des Objekts bei Bedarf.
	 */
	private void leseKonfigEigenschaften() {
		if (!konfigDatenGelesen) {
			return;
		}

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		modell = objekt.getDataModel();
		atg = modell.getAttributeGroup("atg.benutzerEigenschaften");
		datum = objekt.getConfigurationData(atg);

		if (datum != null) {
			vorname = datum.getTextValue("vorname").getText();
			zweiterVorname = datum.getTextValue("zweiterVorname").getText();
			nachname = datum.getTextValue("nachname").getText();
			organisation = datum.getTextValue("organisation").getText();
			emailAdresse = datum.getTextValue("emailAdresse").getText();
		} else {
			vorname = null;
			zweiterVorname = null;
			nachname = null;
			organisation = null;
			emailAdresse = null;
		}

		konfigDatenGelesen = true;
	}

}
