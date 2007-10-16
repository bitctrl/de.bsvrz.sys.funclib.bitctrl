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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bsvrz.dav.daf.main.ClientDavInterface;
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

	/**
	 * Enth&auml;lt die Datens&auml;tze die einem bestimmten Systemobjekt von
	 * der Factory initial hinzugef&uuml;gt werden.
	 */
	private static final Map<Class<? extends SystemObjekt>, Set<Class<? extends Datensatz>>> DEFAULT = new HashMap<Class<? extends SystemObjekt>, Set<Class<? extends Datensatz>>>();

	/**
	 * Veranlasst die Factory f&uuml;r ein bestimmtes Systemobjekt initial
	 * beliebige Datens&auml;tze hinzuzuf&uuml;gen.
	 * <p>
	 * <em>Hinweis:</em> Alle erforderlichen Datens&auml;tze m&uuml;ssen vor
	 * dem ersten Abrufen eines Objekts mit der Factory mit dieser Methode
	 * registriert werden.
	 * 
	 * @param klasse
	 *            die Klasse eines Systemobjekts.
	 * @param datensatz
	 *            die Klasse eines passenden Parameters oder Onlinedatensatzes.
	 */
	public static void addParameter(Class<? extends SystemObjekt> klasse,
			Class<? extends Datensatz> datensatz) {
		if (DEFAULT.get(klasse) == null) {
			DEFAULT.put(klasse, new HashSet<Class<? extends Datensatz>>());
		}
		DEFAULT.get(klasse).add(datensatz);
	}

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
	 * Die Datenverteilerverbindung der Objektfactory, muss explizit gesetzt
	 * werden und wird von den Autoupdatern der Datensätze benötigt.
	 */
	private ClientDavInterface verbindung;

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
	 * @param pids
	 *            PIDs der zu &uuml;bersetzenden Systemobjekte
	 * @return Tabelle von IDs und Modellobjekten
	 */
	public List<SystemObjekt> bestimmeModellobjekte(String... pids) {
		DataModel dm = verbindung.getDataModel();
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

		// Falls noch nicht geschehen gewünschte Standarddatensätze ergänzen
		if (so != null) {
			for (Class<? extends SystemObjekt> co : DEFAULT.keySet()) {
				if (co.isInstance(so)) {
					for (Class<? extends Datensatz> cd : DEFAULT.get(co)) {
						if (cd.isAssignableFrom(OnlineDatensatz.class)) {
							so
									.getOnlineDatensatz((Class<? extends OnlineDatensatz>) cd);
						} else {
							assert cd
									.isAssignableFrom(ParameterDatensatz.class);
							so
									.getParameterDatensatz((Class<? extends ParameterDatensatz>) cd);
						}
					}
				}
			}
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
	 * liefert die der Objektfactory zugeordnete Datenverteilerverbindung. Wenn
	 * versucht wird, auf die Verbindung zuzugreifen, obwohl keine gesetzt
	 * wurde, wird eine {@link IllegalStateException} geworfen.
	 * 
	 * @return die Verbindung
	 */
	public ClientDavInterface getVerbindung() {
		if (verbindung == null) {
			throw new IllegalStateException(
					"Der Factory wurde keine Datenverteilerverbindung zugewiesen");
		}
		return verbindung;
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

	/**
	 * ordnet der Factory eine Datenverteilerverbindung zu.
	 * 
	 * @param verbindung
	 *            die Verbindung
	 */
	public void setVerbindung(ClientDavInterface verbindung) {
		this.verbindung = verbindung;
	}
}
