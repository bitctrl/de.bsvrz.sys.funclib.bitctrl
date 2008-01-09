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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObjectType;

/**
 * Klasse zur Verwaltung der Informationen, für welche
 * Objekttyp-Attributgruppe-Kombinationen bereits die Konfigurationsdaten
 * komplett abgerufen wurden, um sie per lokalem Datenverteiler-Cache
 * bereitzustellen.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public final class DataCache {

	/**
	 * Globale Instanz der Klasse.
	 */
	private static final DataCache INSTANCE = new DataCache();

	/**
	 * Lesen und Zwsichenspeiechern aller Daten der entsprechenden
	 * Attributgruppe für den übergebenen Typ. Die Funktion ermittelt den Typ,
	 * in dem die übergebene Attributgruppe ursprünglich definiert ist,
	 * ermittelt alle Objekte dieses Typs und lieset die mit der Attributgruppe
	 * beschriebenen konfigurierenden Daten aus. Die Operation wird nur einmalig
	 * ausgeführt, d.h. beim wiederholten Aufruf mit identischen Parametern
	 * erfolgt keine Aktion.
	 * 
	 * @param objType
	 *            der Typ
	 * @param atg
	 *            die Attributgruppe
	 */
	public static void cacheData(final SystemObjectType objType,
			final AttributeGroup atg) {
		boolean cache = false;
		SystemObjectType type = sucheAtgDefinitionsTyp(objType, atg);
		Set<AttributeGroup> set = INSTANCE.cachedData.get(type);
		if (set == null) {
			set = new HashSet<AttributeGroup>();
			INSTANCE.cachedData.put(type, set);
			set.add(atg);
			cache = true;
		} else {
			if (!set.contains(atg)) {
				set.add(atg);
				cache = true;
			}
		}

		if (cache) {
			type.getDataModel().getConfigurationData(type.getElements(), atg);
		}
	}

	/**
	 * ermitteln des Objekttyps, in dem die übergebene Attributgruppe
	 * ursprünglich angelegt wurde.
	 * 
	 * @param objType
	 *            der Typ
	 * @param atg
	 *            die Attributgruppe
	 * @return der ermittelte Basistyp der übergebenen
	 */
	private static SystemObjectType sucheAtgDefinitionsTyp(
			final SystemObjectType objType, final AttributeGroup atg) {
		SystemObjectType result = objType;
		for (SystemObjectType superType : objType.getSuperTypes()) {
			if (superType.getAttributeGroups().contains(atg)) {
				result = sucheAtgDefinitionsTyp(superType, atg);
				break;
			}
		}
		return result;
	}

	/**
	 * Verwaltungsliste für abgerufene Kombinationen aus Objekttyp und
	 * Attributgruppe.
	 */
	private final Map<SystemObjectType, Set<AttributeGroup>> cachedData = new HashMap<SystemObjectType, Set<AttributeGroup>>();

	/**
	 * Konstruktor ist privat, damit keine Objekte des Typs angelegt werden
	 * können.
	 */
	private DataCache() {
		super();
	}
}
