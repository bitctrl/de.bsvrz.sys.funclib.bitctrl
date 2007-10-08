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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.ObjectTimeSpecification;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.daf.Konfigurationsbereich;

/**
 * Eine "Super-Factory" f&uuml;r Modellobjekte. Dient dem Erzeugen von Objekten
 * des logischen Modells aus dem Datenmodell des Datenverteilers. Es k&ouml;nnen
 * beliebige verschiedene Modelle benutzt werden. Sie &uuml;ssen aber vor der
 * Verwendung mit {@link #registerFactory(ModellObjektFactory[])} bekannt
 * gemacht werden.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class ObjektFactory implements ModellObjektFactory {

	/** Das Singleton der Factory. */
	private static ObjektFactory singleton;

	/** Die Singleton der benannten Factories. */
	private static Map<String, ObjektFactory> singletons = new HashMap<String, ObjektFactory>();

	/**
	 * Gibt das einzige Objekt der Super-Factory zur&uuml;ck.
	 * 
	 * @return das Singleton der Super-Factory.
	 */
	public static ObjektFactory getInstanz() {
		if (singleton == null) {
			singleton = new ObjektFactory();
		}
		return singleton;
	}

	/**
	 * Werden in der selben Applikation mehrere verschiedene Fabriken
	 * ben&ouml;tigt, kann man hiermit die Fabriken anhand ihres eineutigen
	 * Namens verwalten.
	 * 
	 * @param id
	 *            der Name der gesuchten Fabrik. Existiert sie nicht, wird sie
	 *            angelegt.
	 * @return die Fabrik mit dem angegebenen Namen.
	 */
	public static ObjektFactory getInstanz(String id) {
		if (singletons.get(id) == null) {
			singletons.put(id, new ObjektFactory());
		}
		return singletons.get(id);
	}

	/**
	 * Cacht die erstellten Objekten und stellt sicher, dass es jedes Objekt nur
	 * genau einmal gubt.
	 */
	private final Map<Long, SystemObjekt> cache;

	/** Merkt sich die Klassen aller registrierten Fabriken. */
	private final Map<Class<? extends ModellObjektFactory>, ModellObjektFactory> factories;

	/**
	 * Konstruktor verstecken, da Klasse nur statische Objekte besitzt.
	 */
	private ObjektFactory() {
		factories = new HashMap<Class<? extends ModellObjektFactory>, ModellObjektFactory>();
		cache = new HashMap<Long, SystemObjekt>();
	}

	/**
	 * Bestimmt eine Liste der Modellobjekte zu vorgegebenen PIDs von
	 * Systemobjekten. Ist eine PID ein Konfigurationsbereich, werden alle
	 * Objekte des Konfigurationsbereichs bestimmt. Ist eine PID ein Typ, werden
	 * alle Objekte des Typs bestimmt.
	 * 
	 * @param dm
	 *            Das Datenmodell des Datenverteilers
	 * @param pids
	 *            PIDs der zu &uuml;bersetzenden Systemobjekte
	 * @return Tabelle von IDs und Modellobjekten
	 */
	public List<SystemObjekt> bestimmeModellobjekte(DataModel dm,
			String... pids) {
		List<SystemObjekt> objekte = new ArrayList<SystemObjekt>();

		for (int i = 0; i < pids.length; i++) {
			SystemObject obj = dm.getObject(pids[i]);

			if (obj != null) {
				if (obj instanceof ConfigurationArea) {
					// Alle Objekte des Konfigurationsbereich suchen
					ConfigurationArea kb = (ConfigurationArea) obj;
					List<String> typen;

					typen = new ArrayList<String>();
					for (ModellObjektFactory f : factories.values()) {
						for (SystemObjektTyp typ : f.getTypen()) {
							typen.add(typ.getPid());
						}
					}

					for (SystemObject objekt : Konfigurationsbereich
							.getObjekte(kb, typen)) {
						SystemObjekt fuzzyObjekt = getModellobjekt(objekt);
						if (fuzzyObjekt != null) {
							objekte.add(fuzzyObjekt);
						}
					}
				} else if (obj instanceof SystemObjectType) {
					// Alle Objekte des Typs suchen
					List<SystemObjectType> liste;

					liste = new ArrayList<SystemObjectType>();
					liste.add((SystemObjectType) obj);
					for (SystemObject so : dm.getObjects(null, liste,
							ObjectTimeSpecification.valid())) {
						SystemObjekt objekt = getModellobjekt(so);
						if (objekt != null) {
							objekte.add(objekt);
						}
					}
				} else {
					// Das Objekt selber suchen
					SystemObjekt so = getModellobjekt(obj);
					if (so != null) {
						objekte.add(so);
					}
				}
			}
		}

		return objekte;
	}

	/**
	 * Sammelt alle gecachten Systemobjekte der registrierten Objektfabriken.
	 * 
	 * @return Liste aller derzeit bekannten Systemobjekte
	 */
	public Collection<SystemObjekt> getInstanzen() {
		return cache.values();
	}

	/**
	 * Versucht mit Hilfe der registrierten Fabriken ein Systemobjekt in ein
	 * Modellobjekt zu &uumnl;berf&uuml;hren. Gibt es mehrere Fabriken, die dazu
	 * in der Lage sind, wird die Fabrik benutzt, die zuerst registriert wurde.
	 * 
	 * {@inheritDoc}
	 * 
	 * @param obj
	 *            Ein Systemobjekt
	 * @return Das korrespondierende Modellobjekt oder {@code null}, wenn das
	 *         Systemobjekt nicht in ein Modellobjekt &uuml;berf&uuml;hrt werden
	 *         kann
	 */
	public SystemObjekt getModellobjekt(SystemObject obj) {
		if (obj == null) {
			throw new IllegalArgumentException("Argument darf nicht null sein.");
		}

		SystemObjekt so = null;

		// Liegt Objekt bereits im Cache?
		if (cache.containsKey(obj.getId())) {
			return cache.get(obj.getId());
		}

		// Objekt muss erzeugt werden
		for (ModellObjektFactory f : factories.values()) {
			so = f.getModellobjekt(obj);
			if (so != null) {
				// Wir haben eine passende Fabrik gefunden
				break;
			}
		}

		// Objekt im Cache ablegen, falls es erstellt werden konnte
		if (so != null) {
			cache.put(obj.getId(), so);
		}

		return so;
	}

	/**
	 * Sammelt alle erzeugbaren Objekttypen der registrierten Factories.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ModellObjektFactory#getTypen()
	 */
	public Collection<SystemObjektTyp> getTypen() {
		List<SystemObjektTyp> typen;

		typen = new ArrayList<SystemObjektTyp>();
		for (ModellObjektFactory f : factories.values()) {
			typen.addAll(f.getTypen());
		}

		return typen;
	}

	/**
	 * Registriert eine neuen Modellobjektfabrik.
	 * 
	 * @param factory
	 *            Modellobjektfabrik
	 */
	public void registerFactory(ModellObjektFactory... factory) {
		for (ModellObjektFactory f : factory) {
			factories.put(f.getClass(), f);
		}
	}

}
