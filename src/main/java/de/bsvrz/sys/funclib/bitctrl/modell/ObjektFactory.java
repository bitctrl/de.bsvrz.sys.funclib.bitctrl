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
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.daf.Konfigurationsbereich;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.BcCommonObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.GeoModellFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.KalenderobjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.LmsObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.mif.MifModellFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellaoe.SystemModellAoeObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellaoe.objekte.AutarkeOrganisationsEinheit;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Datenverteiler;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenobjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsobjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.VeWBetriebGlobalObjektFactory;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Eine "Super-Factory" f&uuml;r Modellobjekte. Dient dem Erzeugen von Objekten
 * des logischen Modells aus dem Datenmodell des Datenverteilers. Es k&ouml;nnen
 * beliebige verschiedene Modelle benutzt werden. Sie &uuml;ssen aber vor der
 * Verwendung mit {@link #registerFactory(ModellObjektFactory[])} bekannt
 * gemacht werden.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class ObjektFactory implements ModellObjektFactory {

	/** Das Singleton der Factory. */
	private static ObjektFactory singleton;

	/**
	 * Enth&auml;lt die Datens&auml;tze die einem bestimmten Systemobjekt von
	 * der Factory initial hinzugef&uuml;gt werden.
	 */
	private static final Map<Class<? extends SystemObjekt>, Set<Class<? extends Datensatz<?>>>> DEFAULT = new HashMap<Class<? extends SystemObjekt>, Set<Class<? extends Datensatz<?>>>>();

	/**
	 * Veranlasst die Factory f&uuml;r ein bestimmtes Systemobjekt initial
	 * beliebige Datens&auml;tze hinzuzuf&uuml;gen.
	 * <p>
	 * <em>Hinweis:</em> Alle erforderlichen Datens&auml;tze m&uuml;ssen vor dem
	 * ersten Abrufen eines Objekts mit der Factory mit dieser Methode
	 * registriert werden.
	 *
	 * @param klasse
	 *            die Klasse eines Systemobjekts.
	 * @param datensatz
	 *            die Klasse eines passenden Parameters oder Onlinedatensatzes.
	 */
	public static void addParameter(final Class<? extends SystemObjekt> klasse,
			final Class<? extends Datensatz<?>> datensatz) {
		if (DEFAULT.get(klasse) == null) {
			DEFAULT.put(klasse, new HashSet<Class<? extends Datensatz<?>>>());
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

	/** Der Logger der Klasse. */
	private final Debug log = Debug.getLogger();

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
	protected ObjektFactory() {
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
	public List<SystemObjekt> bestimmeModellobjekte(final String... pids) {
		final DataModel dm = getVerbindung().getDataModel();
		final List<SystemObjekt> objekte = new ArrayList<SystemObjekt>();

		for (final String pid : pids) {
			final SystemObject obj = dm.getObject(pid);

			if (obj != null) {
				if (obj instanceof ConfigurationArea) {
					// Alle Objekte des Konfigurationsbereich suchen
					final ConfigurationArea kb = (ConfigurationArea) obj;
					final List<String> typen;

					typen = new ArrayList<String>();
					for (final ModellObjektFactory f : factories.values()) {
						for (final SystemObjektTyp typ : f.getTypen()) {
							typen.add(typ.getPid());
						}
					}

					for (final SystemObject objekt : Konfigurationsbereich
							.getObjekte(kb, typen)) {
						final SystemObjekt fuzzyObjekt = getModellobjekt(
								objekt);
						if (fuzzyObjekt != null) {
							objekte.add(fuzzyObjekt);
						}
					}
				} else if (obj instanceof SystemObjectType) {
					// Alle Objekte des Typs suchen
					final SystemObjectType typ = (SystemObjectType) obj;
					// List<SystemObjectType> liste;
					//
					// liste = new ArrayList<SystemObjectType>();
					// liste.add((SystemObjectType) obj);
					// for (SystemObject so : dm.getObjects(null, liste,
					// ObjectTimeSpecification.valid())) {
					for (final SystemObject so : typ.getElements()) {
						final SystemObjekt objekt = getModellobjekt(so);
						if (objekt != null) {
							objekte.add(objekt);
						}
					}
				} else {
					// Das Objekt selber suchen
					final SystemObjekt so = getModellobjekt(obj);
					if (so != null) {
						objekte.add(so);
					}
				}
			}
		}

		assert!objekte.isEmpty();
		return objekte;
	}

	/**
	 * Gibt die autarke Organisationseinheit zurück.
	 *
	 * @return die AOE.
	 */
	public AutarkeOrganisationsEinheit getAOE() {
		return (AutarkeOrganisationsEinheit) getModellobjekt(
				getVerbindung().getLocalConfigurationAuthority());
	}

	/**
	 * Gibt die lokale Klientapplikation zurück.
	 *
	 * @return die lokale Applikation.
	 */
	public Applikation getApplikation() {
		return (Applikation) getModellobjekt(
				getVerbindung().getLocalApplicationObject());
	}

	/**
	 * Gibt den Datenverteiler zurück, mit dem die lokale Applikation verbunden
	 * ist.
	 *
	 * @return der Datenverteiler.
	 */
	public Datenverteiler getDatenverteiler() {
		return (Datenverteiler) getModellobjekt(getVerbindung().getLocalDav());
	}

	/**
	 * Gibt den angemeldeten Benutzer für die lokale Applikation zurück.
	 *
	 * @return der lokale Benutzer.
	 */
	public Benutzer getBenutzer() {
		return (Benutzer) getModellobjekt(getVerbindung().getLocalUser());
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
	 * Bestimmt das Modellobjekt zu einer PID.
	 *
	 * @param pid
	 *            die PID des gesuchten Systemobjekts.
	 * @return das Systemobjekt oder {@code null}, wenn keines existiert oder es
	 *         nicht als Modellobjekt darstellbar ist.
	 */
	public SystemObjekt getModellobjekt(final String pid) {
		final DataModel modell = getVerbindung().getDataModel();
		final SystemObject so = modell.getObject(pid);
		if (so != null) {
			return getModellobjekt(so);
		}
		return null;
	}

	/**
	 * Sucht mit Hilfe der registrierten Fabriken nach einem Systemobjekt,
	 * dessen Id bekannt ist.
	 *
	 * @param id
	 *            die Id eines Systemobjekts.
	 * @return das korrespondierende Modellobjekt oder {@code null}, wenn es
	 *         keines mit der Id gibt.
	 */
	public SystemObjekt getModellobjekt(final long id) {
		final DataModel modell = getVerbindung().getDataModel();
		final SystemObject so = modell.getObject(id);
		if (so != null) {
			return getModellobjekt(so);
		}
		return null;
	}

	/**
	 * Versucht mit Hilfe der registrierten Fabriken ein Systemobjekt in ein
	 * Modellobjekt zu überführen. Gibt es mehrere Fabriken, die dazu in der
	 * Lage sind, wird die Fabrik benutzt, die zuerst registriert wurde.
	 * Existiert keine passende Fabrik, wird generisches {@link SystemObjekt}
	 * zurückgegeben.
	 *
	 * @param obj
	 *            Ein Systemobjekt
	 * @return das korrespondierende Modellobjekt, niemals {@code null}.
	 */
	@Override
	public SystemObjekt getModellobjekt(final SystemObject obj) {
		if (obj == null) {
			throw new IllegalArgumentException(
					"Argument darf nicht null sein.");
		}

		SystemObjekt so = null;

		// Liegt Objekt bereits im Cache?
		if (cache.containsKey(obj.getId())) {
			return cache.get(obj.getId());
		}

		// Objekt muss erzeugt werden, dazu wird über die Typhierachie des
		// übergebenen SystemObjects eine passende Factory gesucht.
		Collection<SystemObjectType> objTypen = new ArrayList<SystemObjectType>();
		objTypen.add(obj.getType());
		do {
			for (final ModellObjektFactory f : factories.values()) {
				boolean factoryGefunden = false;
				final Collection<? extends SystemObjektTyp> unterstuetzteTypen = f
						.getTypen();
				for (final SystemObjectType type : objTypen) {
					for (final SystemObjektTyp factoryTyp : unterstuetzteTypen) {
						if (type.getPid().equals(factoryTyp.getPid())) {
							factoryGefunden = true;
						}
					}
					if (factoryGefunden) {
						break;
					}
				}
				if (factoryGefunden) {
					so = f.getModellobjekt(obj);
					if (so != null) {
						// Wir haben eine passende Fabrik gefunden
						break;
					}
				}
			}
			if (so == null) {
				final Collection<SystemObjectType> basisTypen = new ArrayList<SystemObjectType>();
				for (final SystemObjectType type : objTypen) {
					basisTypen.addAll(type.getSuperTypes());
				}
				objTypen = basisTypen;
			}
		} while ((objTypen.size() > 0) && (so == null));

		// Objekt im Cache ablegen, falls es erstellt werden konnte
		if (so != null) {
			cache.put(obj.getId(), so);
		}

		// Falls noch nicht geschehen gewünschte Standarddatensätze ergänzen
		if (so != null) {
			for (final Class<? extends SystemObjekt> co : DEFAULT.keySet()) {
				if (co.isInstance(so)) {
					for (final Class<? extends Datensatz<?>> cd : DEFAULT
							.get(co)) {
						if (cd.isAssignableFrom(OnlineDatensatz.class)) {
							so.getOnlineDatensatz(
									(Class<? extends OnlineDatensatz<?>>) cd);
						} else {
							assert cd
							.isAssignableFrom(ParameterDatensatz.class);
							so.getParameterDatensatz(
									(Class<? extends ParameterDatensatz<?>>) cd);
						}
					}
				}
			}
		}

		if (so == null) {
			log.fine("Es existiert kein passendes Modellobjekt für " + obj
					+ " vom Typ " + obj.getType() + ".");
			@SuppressWarnings("deprecation")
			final SystemObjekt deprecatedObj = new SystemObjektImpl(obj);
			so = deprecatedObj;
		}

		assert so != null;
		return so;
	}

	/**
	 * Sammelt alle erzeugbaren Objekttypen der registrierten Factories.
	 */
	@Override
	public Collection<SystemObjektTyp> getTypen() {
		final List<SystemObjektTyp> typen;

		typen = new ArrayList<SystemObjektTyp>();
		for (final ModellObjektFactory f : factories.values()) {
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
	public void registerFactory(final ModellObjektFactory... factory) {
		for (final ModellObjektFactory f : factory) {
			factories.put(f.getClass(), f);
		}
	}

	/**
	 * Registriert alle Factories, die sich in dieser Bibliothek befinden
	 * befinden. Folgende Factories werden registriert:
	 * <ul>
	 * <li>{@link GeoModellFactory}</li>
	 * <li>{@link KalenderobjektFactory}</li>
	 * <li>{@link SystemModellAoeObjektFactory}</li>
	 * <li>{@link SystemModellGlobalObjektFactory}</li>
	 * <li>{@link UmfelddatenobjektFactory}</li>
	 * <li>{@link VerkehrsobjektFactory}</li>
	 * <li>{@link MifModellFactory}</li>
	 * <li>{@link LmsObjektFactory}</li>
	 * <li>{@link VeWBetriebGlobalObjektFactory}</li>
	 * </ul>
	 */
	public void registerStandardFactories() {
		registerFactory(new GeoModellFactory());
		registerFactory(new KalenderobjektFactory());
		registerFactory(new LmsObjektFactory());
		registerFactory(new MifModellFactory());
		registerFactory(new SystemModellAoeObjektFactory());
		registerFactory(new SystemModellGlobalObjektFactory());
		registerFactory(new UmfelddatenobjektFactory());
		registerFactory(new VerkehrsobjektFactory());
		registerFactory(new VeWBetriebGlobalObjektFactory());
	}

	/**
	 * Registriert alle Factories üfr BitCtrl-spezifische Objekte. Folgende
	 * Factories werden registriert:
	 * <ul>
	 * <li>{@link BcCommonObjektFactory}</li>
	 * </ul>
	 */
	public void registerBitCtrlFactories() {
		registerFactory(new BcCommonObjektFactory());
	}

	/**
	 * Ordnet der Factory eine Datenverteilerverbindung zu und löscht den
	 * Systemobjektcache.
	 *
	 * @param verbindung
	 *            die Verbindung
	 */
	public void setVerbindung(final ClientDavInterface verbindung) {
		this.verbindung = verbindung;
		cache.clear();
	}

}
