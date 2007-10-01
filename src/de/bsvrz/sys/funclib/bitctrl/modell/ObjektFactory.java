/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse
 * Copyright (C) 2007 BitCtrl Systems GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
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
 * Dient dem Erzeugen von Objekten des logischen Modells aus dem Datenmodell des
 * Datenverteilers. Es k&ouml;nnen beliebige verschiedene Modelle benutzt
 * werden. Sie &uuml;ssen aber vor der Verwendung mit
 * {@link #registerFactory(ModellObjektFactory[])} bekannt gemacht werden.
 * 
 * @author BitCtrl, Schumann
 * @version $Id: ObjektFactory.java 1410 2007-05-29 13:16:10Z Schumann $
 */
public final class ObjektFactory {

	/** Merkt sich die Klassen aller registrierten Fabriken. */
	private static Map<Class<ModellObjektFactory>, ModellObjektFactory> factories;

	/**
	 * Registriert eine neuen Modellobjektfabrik.
	 * 
	 * @param factory
	 *            Modellobjektfabrik
	 */
	public static void registerFactory(ModellObjektFactory... factory) {
		if (factories == null) {
			factories = new HashMap<Class<ModellObjektFactory>, ModellObjektFactory>();
		}

		for (ModellObjektFactory f : factory) {
			factories.put((Class<ModellObjektFactory>) f.getClass(), f);
		}
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
	public static List<SystemObjekt> bestimmeModellobjekte(DataModel dm,
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
	 * Versucht mit Hilfe der registrierten Fabriken ein Systemobjekt in ein
	 * Modellobjekt zu &uumnl;berf&uuml;hren. Gibt es mehrere Fabriken, die dazu
	 * in der Lage sind, wird die Fabrik benutzt, die zuerst registriert wurde.
	 * 
	 * @param obj
	 *            Ein Systemobjekt
	 * @return Das korrespondierende Modellobjekt oder {@code null}, wenn das
	 *         Systemobjekt nicht in ein Modellobjekt &uuml;berf&uuml;hrt werden
	 *         kann
	 */
	public static SystemObjekt getModellobjekt(SystemObject obj) {
		SystemObjekt so = null;

		for (ModellObjektFactory f : factories.values()) {
			so = f.getInstanz(obj);
			if (so != null) {
				// Wir haben eine passende Fabrik gefunden
				break;
			}
		}

		return so;
	}

	/**
	 * Sammelt alle gecachten Systemobjekte der registrierten Objektfabriken.
	 * 
	 * @return Liste aller derzeit bekannten Systemobjekte
	 */
	public static List<SystemObjekt> getModellobjekte() {
		List<SystemObjekt> objekte;

		objekte = null;
		for (ModellObjektFactory f : factories.values()) {
			if (objekte == null) {
				objekte = f.getInstanzen();
			} else {
				objekte.addAll(f.getInstanzen());
			}
		}

		return objekte;
	}

	/**
	 * Konstruktor verstecken, da Klasse nur statische Objekte besitzt.
	 */
	private ObjektFactory() {
		// Nix zu tun
	}

}
