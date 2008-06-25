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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObjectType;

/**
 * Klasse zur Verwaltung der Informationen, f�r welche
 * Objekttyp-Attributgruppe-Kombinationen bereits die Konfigurationsdaten
 * komplett abgerufen wurden, um sie per lokalem Datenverteiler-Cache
 * bereitzustellen.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public final class DataCache {

	/**
	 * die Art, wie Daten blockweise von der Kondifguration ermittelt werden.
	 * 
	 * @author BitCtrl Systems GmbH, Uwe Peuker
	 * @version $Id$
	 */
	public enum CacheMode {
		/** Konfigurationsdaten werden einzeln von der Konfiguration ermittelt. */
		NICHT,

		/**
		 * Konfigurationsdaten werden f�r einen Typ von der Konfiguration
		 * ermittelt.
		 */
		FLACH,

		/**
		 * Konfigurationsdaten werden f�r einen Typ und alle Basistypen von der
		 * Konfiguration ermittelt.
		 */
		TIEF
	}

	/** Globale Instanz der Klasse. */
	private static final DataCache INSTANCE = new DataCache();

	/**
	 * Lesen und Zwsichenspeiechern aller Daten der entsprechenden
	 * Attributgruppe f�r den �bergebenen Typ. Die Funktion ermittelt den Typ,
	 * in dem die �bergebene Attributgruppe urspr�nglich definiert ist,
	 * ermittelt alle Objekte dieses Typs und lieset die mit der Attributgruppe
	 * beschriebenen konfigurierenden Daten aus. Die Operation wird nur einmalig
	 * ausgef�hrt, d.h. beim wiederholten Aufruf mit identischen Parametern
	 * erfolgt keine Aktion.
	 * 
	 * @param objType
	 *            der Typ
	 * @param atg
	 *            die Attributgruppe
	 */
	public static void cacheData(final SystemObjectType objType,
			final AttributeGroup atg) {
		if (INSTANCE.caching != CacheMode.NICHT) {
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
				type.getDataModel().getConfigurationData(type.getElements(),
						atg);
			}
		}
	}

	/**
	 * liefert den Modus, in dem Daten blockweise von der Konfiguration
	 * abgefragt werden.
	 * 
	 * @return der Cachemodus
	 */
	public static CacheMode getCaching() {
		return INSTANCE.caching;
	}

	/**
	 * setzt den Modus zur blocckweisen Abfrage von Konfigurationsdaten.
	 * 
	 * @param caching
	 *            der Modus
	 */
	public static void setCaching(CacheMode caching) {
		INSTANCE.caching = caching;
	}

	/**
	 * ermitteln des Objekttyps, in dem die �bergebene Attributgruppe
	 * urspr�nglich angelegt wurde.
	 * 
	 * @param objType
	 *            der Typ
	 * @param atg
	 *            die Attributgruppe
	 * @return der ermittelte Basistyp der �bergebenen
	 */
	private static SystemObjectType sucheAtgDefinitionsTyp(
			final SystemObjectType objType, final AttributeGroup atg) {
		SystemObjectType result = objType;
		if (INSTANCE.caching == CacheMode.TIEF) {
			for (SystemObjectType superType : objType.getSuperTypes()) {
				if (superType.getAttributeGroups().contains(atg)) {
					result = sucheAtgDefinitionsTyp(superType, atg);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * legt fest, wie die Konfigurationsdaten blockweise von der Konfiguration
	 * ermittelt werden sollen.
	 */
	private CacheMode caching = CacheMode.NICHT;

	/**
	 * Verwaltungsliste f�r abgerufene Kombinationen aus Objekttyp und
	 * Attributgruppe.
	 */
	private final Map<SystemObjectType, Set<AttributeGroup>> cachedData = new HashMap<SystemObjectType, Set<AttributeGroup>>();

	/**
	 * Konstruktor ist privat, damit keine Objekte des Typs angelegt werden
	 * k�nnen.
	 */
	private DataCache() {
		super();
	}
}
