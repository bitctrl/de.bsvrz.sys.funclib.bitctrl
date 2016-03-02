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
package de.bsvrz.sys.funclib.bitctrl.daf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationAuthority;
import de.bsvrz.dav.daf.main.config.ObjectTimeSpecification;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;

/**
 * Allgemeine Funktionen zur Arbeit mit Konfigurationsbereichen.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public final class Konfigurationsbereich {

	private Konfigurationsbereich() {
		// es gibt keine Instanzen der Klasse.
	}

	/**
	 * Gibt alle Systemobjekte aus einem Konfigurationsbereich zur&uuml;ck.
	 *
	 * @param kb
	 *            Ein Konfigurationsbereich
	 * @return Liste der gesuchten Systemobjekten
	 */
	public static List<SystemObject> getObjekte(final ConfigurationArea kb) {
		final List<SystemObject> liste = new ArrayList<SystemObject>();

		liste.addAll(kb.getObjects(null, ObjectTimeSpecification.valid()));

		return liste;
	}

	/**
	 * Gibt alle Systemobjekte aus einem Konfigurationsbereich zur&uuml;ck. Mit
	 * dem zweitem Parameter kann der Typ der gesuchten Objekte
	 * eingeschr&auml;nkt werden.
	 *
	 * @param kb
	 *            Ein Konfigurationsbereich
	 * @param typen
	 *            Optional ein Feld von Systemobjekttypen
	 * @return Liste der gesuchten Systemobjekten
	 */
	public static List<SystemObject> getObjekte(final ConfigurationArea kb,
			final SystemObjectType... typen) {
		final List<SystemObject> liste = new ArrayList<SystemObject>();

		if (typen == null) {
			liste.addAll(kb.getObjects(null, ObjectTimeSpecification.valid()));
		} else {
			liste.addAll(kb.getObjects(Arrays.asList(typen),
					ObjectTimeSpecification.valid()));
		}

		return liste;
	}

	/**
	 * Gibt alle Systemobjekte aus einem Konfigurationsbereich zur&uuml;ck. Mit
	 * dem zweitem Parameter kann der Typ der gesuchten Objekte
	 * eingeschr&auml;nkt werden.
	 *
	 * @param kb
	 *            Ein Konfigurationsbereich
	 * @param typen
	 *            Optional ein Feld von Systemobjekttypen
	 * @return Liste der gesuchten Systemobjekten
	 */
	public static List<SystemObject> getObjekte(final ConfigurationArea kb,
			final List<String> typen) {
		return getObjekte(kb, typen.toArray(new String[typen.size()]));
	}

	/**
	 * Gibt alle Systemobjekte aus einem Konfigurationsbereich zur&uuml;ck. Mit
	 * dem zweitem Parameter kann der Typ der gesuchten Objekte
	 * eingeschr&auml;nkt werden.
	 *
	 * @param kb
	 *            Ein Konfigurationsbereich
	 * @param typen
	 *            Optional ein Feld von Systemobjekttypen
	 * @return Liste der gesuchten Systemobjekten
	 */
	public static List<SystemObject> getObjekte(final ConfigurationArea kb,
			final String... typen) {
		final List<SystemObject> liste = new ArrayList<SystemObject>();
		final List<SystemObjectType> objekttypen = new ArrayList<SystemObjectType>();

		if (typen == null) {
			liste.addAll(kb.getObjects(null, ObjectTimeSpecification.valid()));
		} else {
			for (final String typ : typen) {
				objekttypen.add(kb.getDataModel().getType(typ));
			}

			liste.addAll(kb.getObjects(objekttypen,
					ObjectTimeSpecification.valid()));
		}

		return liste;
	}

	/**
	 * liefert den Standardkonfigurationsbereich für &uuml;bergebene
	 * Datenverteilerverbindung.<br>
	 * Der Standardkonfigurationsbereich wird verwendet um dynamische Objekte
	 * abzulegen, sofern kein expliziter Zielbereich f&uuml;r diesen Zweck
	 * definiert wurde.
	 *
	 * @param verbindung
	 *            die Verbindung, deren Standardbereich ermittelt werden soll
	 * @return den ermittelten Bereich
	 * @throws InvalidArgumentException
	 *             für die übergebene Verbindung konnte kein entsprechender
	 *             Konfigurationsbereich ermittelt werden.
	 */
	public static ConfigurationArea getStandardKonfigurationsBereich(
			final ClientDavInterface verbindung)
					throws InvalidArgumentException {

		ConfigurationArea result = null;

		if (verbindung != null) {
			final ConfigurationAuthority authority = verbindung
					.getLocalApplicationObject().getConfigurationArea()
					.getConfigurationAuthority();
			final Data daten = authority.getConfigurationData(
					authority.getDataModel().getAttributeGroup(
							"atg.konfigurationsVerantwortlicherEigenschaften"));
			final String bereichsPid = daten.getTextArray("defaultBereich")
					.getText(0);
			final SystemObject objekt = authority.getDataModel()
					.getObject(bereichsPid);
			if ((objekt != null) && (objekt instanceof ConfigurationArea)) {
				result = (ConfigurationArea) objekt;
			}
		}

		if (result == null) {
			throw new InvalidArgumentException(
					"Für die übergebene Datenverteilerverbindung konnte kein Defaultbereich ermittelt werden.");
		}

		return result;
	}
}
