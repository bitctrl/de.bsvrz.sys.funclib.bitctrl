/*
 * Copyright 2004 by Kappich+Kniß Systemberatung Aachen (K2S)
 *
 * This file is part of K2S-Kernsoftware-Bibliothek.
 *
 * K2S-Kernsoftware-Bibliothek is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * K2S-Kernsoftware-Bibliothek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with K2S-Kernsoftware-Bibliothek; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package de.bsvrz.sys.funclib.parameter.dataIdentificationSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.Attribute;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.AttributeListDefinition;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationException;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.Pid;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectCollection;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.asyncReceiver.AsyncReceiver;
import de.bsvrz.sys.funclib.dataIdentificationSettings.DataIdentification;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Diese Klasse dient zur Verwaltung von Parametersätzen mit Einstellungen die
 * sich auf Datenidentifikationen beziehen. Derartige Parameterdatensätze werden
 * z.B. zur Steuerung des Archivverhaltens (atg.archiv) und der Parametrierung
 * (atg.parametrierung) eingesetzt. Über die Parameterdatensätze können in
 * einzelnen Einträgen mit Hilfe von Aufzählungen und Wildcards Einstellungen
 * für viele Datenidentifikation auf einmal eingeben werden.
 *
 * @author Kappich+Kniß Systemberatung Aachen (K2S)
 * @author Roland Schmitz (rs)
 * @version $Revision: 3054 $ / $Date: 2006-03-07 18:44:05 +0100 (Di, 07 Mrz
 *          2006) $ / ($Author: homeyer $)
 */
public class SettingsManager {

	private static final Debug _debug = Debug.getLogger();

	private final ClientDavInterface _connection;

	private final DataIdentification _parameterIdentification;

	private final EventListenerList listenerList = new EventListenerList();

	// private final LinkedList _updateListeners = new LinkedList();
	// private final LinkedList _endOfSettingsListener = new LinkedList();
	private ClientReceiverInterface _receiver = null;

	private Map _settingsTable;

	private final boolean _aspectUsed;

	private final boolean _simulationsVarianteUsed;

	private final boolean _einstellungenUsed;

	private String parameterSatzName = "ParameterSatz";

	private String eintragsName = "DatenSpezifikation";

	/**
	 * Dieses Set speichert alle Attributgruppen, die über den SettingsManager
	 * nicht angemeldet werden dürfen. Sonst gibt es ein Senke - Empfänger -
	 * Konflikt.
	 */
	private final Set<AttributeGroup> _excludedAttributeGroups = new HashSet<AttributeGroup>();

	/**
	 * Erzeugt ein neues Verwaltungsobjekt.
	 *
	 * @param connection
	 * @param parameterIdentification
	 */
	public SettingsManager(ClientDavInterface connection,
			DataIdentification parameterIdentification) {
		_connection = connection;
		_parameterIdentification = parameterIdentification;
		AttributeListDefinition atl1;
		AttributeListDefinition atl2;
		try {
			atl1 = getListType(_parameterIdentification.getDataDescription()
					.getAttributeGroup());
			atl2 = getItemType(atl1);
			// (AttributeListDefinition)
			// atl1.getAttribute("DatenSpezifikation").getAttributeType();
			_aspectUsed = atl2.getAttribute("Aspekt") != null;
			_debug.finest("_aspectUsed = " + _aspectUsed);
			_simulationsVarianteUsed = atl2.getAttribute("SimulationsVariante") != null;
			_debug.finest("_simulationsVarianteUsed = "
					+ _simulationsVarianteUsed);
			_einstellungenUsed = atl1.getAttribute("Einstellungen") != null;
			_debug.finest("_einstellungenUsed = " + _einstellungenUsed);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Attributgruppe hat nicht den erwarteten Aufbau", e);
		}

		// Hier werden die Attributgruppen bestimmt, die nicht für Anmeldungen
		// publiziert werden dürfen! Werden sie dennoch verwendet,
		// so gibt es ein Senke - Empfänger - Konflikt.
		try {
			final DataModel configuration = connection.getDataModel();
			AttributeGroup atg = configuration
					.getAttributeGroup("atg.konfigurationsAnfrageSchnittstelle");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration
					.getAttributeGroup("atg.konfigurationsSchreibAntwort");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration
					.getAttributeGroup("atg.konfigurationsSchreibAnfrage");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration.getAttributeGroup("atg.konfigurationsAntwort");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration.getAttributeGroup("atg.konfigurationsAnfrage");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration
					.getAttributeGroup("atg.archivAnfrageSchnittstelle");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration.getAttributeGroup("atg.archivAntwort");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration.getAttributeGroup("atg.archivAnfrage");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
			atg = configuration.getAttributeGroup("atg.parameterSchnittstelle");
			if (atg != null)
				_excludedAttributeGroups.add(atg);
		} catch (ConfigurationException ex) {
			_debug.error("Fehler ", ex);
		}
	}

	private AttributeListDefinition getItemType(AttributeListDefinition atl1) {
		Attribute attribute = atl1.getAttribute(eintragsName);

		if (attribute == null) {
			eintragsName = "Parameter";
			attribute = atl1.getAttribute(eintragsName);
		}

		if (attribute == null) {
			throw new RuntimeException("Attributlistentyp: " + atl1
					+ " wird vom SettingsManager nicht unterstützt");
		}

		return (AttributeListDefinition) attribute.getAttributeType();
	}

	private AttributeListDefinition getListType(AttributeGroup attributeGroup) {
		Attribute attribute = attributeGroup.getAttribute(parameterSatzName);
		if (attribute == null) {
			parameterSatzName = "Simulationsdaten";
			attribute = attributeGroup.getAttribute(parameterSatzName);
		}

		if (attribute == null) {
			throw new RuntimeException("Attributgruppe: " + attributeGroup
					+ " wird vom SettingsManager nicht unterstützt");
		}

		return (AttributeListDefinition) attribute.getAttributeType();
	}

	/**
	 * Ergänzt die Liste der Beobachter, die bei Änderung des Parameters zu
	 * informieren sind, um einen weiteren Eintrag
	 *
	 * @param listener
	 *            Neuer Beobachter.
	 */
	public void addUpdateListener(UpdateListener listener) {
		listenerList.add(UpdateListener.class, listener);
	}

	/**
	 * Löscht einen Beobachter aus der Liste der Beobachter, die bei Änderung
	 * des Parameters zu informieren sind.
	 *
	 * @param listener
	 *            Zu löschender Beobachter.
	 */
	public void removeUpdateListener(UpdateListener listener) {
		listenerList.remove(UpdateListener.class, listener);
	}

	/**
	 * Ergänzt die Liste der Beobachter, die informiert werden wollen, sobald
	 * alle Einstellungen abgearbeitet wurden.
	 *
	 * @param listener
	 *            neuer Beobachter
	 */
	public void addEndOfSettingsListener(EndOfSettingsListener listener) {
		synchronized (listenerList) {
			listenerList.add(EndOfSettingsListener.class, listener);
		}
	}

	/**
	 * Löscht einen Beobachter aus der Liste der Beobachter, die informiert
	 * werden wollen, sobald alle Einstellungen abgearbeitet wurden.
	 *
	 * @param listener
	 *            zu löschender Beobachter
	 */
	public void removeEndOfSettingsListener(EndOfSettingsListener listener) {
		synchronized (listenerList) {
			listenerList.remove(EndOfSettingsListener.class, listener);
		}
	}

	/**
	 * Ergänzt die Liste der Beobachter, die informiert werden wollen, sobald
	 * die Übertragung der Einstellungen beginnt.
	 *
	 * @param listener
	 *            neuer Beobachter
	 */
	public void addStartOfSettingsListener(StartOfSettingsListener listener) {
		synchronized (listenerList) {
			listenerList.add(StartOfSettingsListener.class, listener);
		}
	}

	/**
	 * Löscht einen Beobachter aus der Liste der Beobachter, der informiert
	 * werden wollte, sobald die Übertragung der Einstellungen beginnt.
	 *
	 * @param listener
	 *            zu löschender Beobachter
	 */
	public void removeStartOfSettingsListener(StartOfSettingsListener listener) {
		synchronized (listenerList) {
			listenerList.remove(StartOfSettingsListener.class, listener);
		}
	}

	/**
	 * Meldet die im Konstruktor übergebene Datenidentifikation an und startet
	 * damit auch die Verarbeitung und Weitergabe der alten und neuen
	 * Einstellungen pro Datenidentifikation aus erhaltenen Parameterdatensätzen
	 * an die angemeldeten Beobachter.
	 *
	 * @throws IllegalStateException
	 *             Wenn der Manager bereits gestartet wurde.
	 */
	public void start() {
		if (_receiver != null)
			throw new IllegalStateException("Ist bereits gestartet");
		ClientReceiverInterface receiver = new AsyncReceiver(new Receiver());
		_settingsTable = new HashMap();
		try {
			_connection.subscribeReceiver(receiver, _parameterIdentification
					.getObject(),
					_parameterIdentification.getDataDescription(),
					ReceiveOptions.normal(), ReceiverRole.receiver());
			_receiver = receiver;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Meldet die im Konstruktor übergebene Datenidentifikation wieder ab und
	 * beendet damit auch die Verarbeitung und Weitergabe der alten und neuen
	 * Einstellungen pro Datenidentifikation aus erhaltenen Parameterdatensätzen
	 * an die angemeldeten Beobachter.
	 */
	public void stop() {
		if (_receiver == null)
			return;
		try {
			_connection
					.unsubscribeReceiver(_receiver, _parameterIdentification
							.getObject(), _parameterIdentification
							.getDataDescription());
			_receiver = null;
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	/**
	 * Aktualisiert die Tabelle mit den Einstellungen je Datenidentifikation mit
	 * den jeweiligen Einstellungen aus dem übergebenen Datensatz
	 */
	private void extractSettings(ResultData result, Map newSettings) {
		try {
			DataModel configuration = _connection.getDataModel();
			Aspect parameterAspect = null;
			if (!_aspectUsed)
				parameterAspect = configuration
						.getAspect("asp.parameterVorgabe");
			Data data = result.getData();
			_debug.finer("Parametrierte Daten = " + data);
			if (data == null)
				return;
			Data parameterArray = data.getItem(parameterSatzName);
			// Schleife über die einzelnen Elemente des Arrays ParameterSatz
			for (Iterator iterator = parameterArray.iterator(); iterator
					.hasNext();) {
				Data parameter = (Data) iterator.next();
				Data settings = null;
				if (_einstellungenUsed) {
					settings = parameter.getItem("Einstellungen");
				}
				_debug.finer("parameter = " + parameter);
				_debug.finer("settings = " + settings);
				_debug.finer("parameter.getArray(\"Bereich\").getLength() = "
						+ parameter.getArray("Bereich").getLength());
				SystemObject[] specifiedConfigAreas = parameter
						.getReferenceArray("Bereich").getSystemObjectArray();
				specifiedConfigAreas = removeNullReferences(specifiedConfigAreas);
				for (Iterator dataSpecificationIterator = parameter.getItem(
						eintragsName).iterator(); dataSpecificationIterator
						.hasNext();) {
					Data dataSpec = (Data) dataSpecificationIterator.next();
					SystemObject[] specifiedObjects = dataSpec
							.getReferenceArray("Objekt").getSystemObjectArray();
					specifiedObjects = removeNullReferences(specifiedObjects);
					SystemObject[] specifiedAtgs = dataSpec.getReferenceArray(
							"AttributGruppe").getSystemObjectArray();
					specifiedAtgs = removeNullReferences(specifiedAtgs);
					// Sortierung ist notwendig für das später binäre Suchen in
					// diesem Array s.u.
					Arrays.sort(specifiedAtgs);
					SystemObject[] specifiedAspects;
					if (_aspectUsed) {
						specifiedAspects = dataSpec.getReferenceArray("Aspekt")
								.getSystemObjectArray();
						// Sortierung ist notwendig für das später binäre Suchen
						// in diesem Array s.u.
						specifiedAspects = removeNullReferences(specifiedAspects);
						Arrays.sort(specifiedAspects);
					} else {
						specifiedAspects = new SystemObject[1];
						specifiedAspects[0] = parameterAspect;
					}

					short simulationVariant = DataDescription.NO_SIMULATION_VARIANT_SET;
					if (_simulationsVarianteUsed) {
						simulationVariant = dataSpec.getScaledValue(
								"SimulationsVariante").shortValue();
					}

					List objectList;
					if (specifiedObjects.length == 0) {
						// keine Objekte sind alle Objekte
						// d.h. alle dynamischen Objekte und alle
						// Konfigurationsobjekte

						List dynamicObjects = configuration.getType(
								Pid.Type.DYNAMIC_OBJECT).getObjects();
						List configurationObjects = configuration.getType(
								Pid.Type.CONFIGURATION_OBJECT).getObjects();
						objectList = new ArrayList(dynamicObjects.size()
								+ configurationObjects.size());
						objectList.addAll(dynamicObjects);
						objectList.addAll(configurationObjects);
					} else {
						objectList = new ArrayList(specifiedObjects.length);
						for (int i = 0; i < specifiedObjects.length; i++) {
							SystemObject object = specifiedObjects[i];
							if (object instanceof SystemObjectCollection) {
								SystemObjectCollection collection = (SystemObjectCollection) object;
								// Typen und Mengen werden durch die jeweils
								// enthaltenen Elemente ersetzt
								objectList.addAll(collection.getElements());
							} else {
								objectList.add(object);
							}
						}
					}

					// Einschränkung auf ausgewählte Konfigurationsbereiche
					if (specifiedConfigAreas.length > 0) {
						_debug.finest("specifiedConfigAreas.length = "
								+ specifiedConfigAreas.length);
						List specifiedConfigAreaList = Arrays
								.asList(specifiedConfigAreas);
						List newObjectList = new LinkedList();
						for (Iterator objectIterator = objectList.iterator(); objectIterator
								.hasNext();) {
							SystemObject systemObject = (SystemObject) objectIterator
									.next();
							ConfigurationArea configurationArea = systemObject
									.getConfigurationArea();
							if (specifiedConfigAreaList
									.contains(configurationArea)) {
								newObjectList.add(systemObject);
							} // alle anderen werden aus der Objektliste
								// gelöscht
						}
						objectList.clear();
						objectList.addAll(newObjectList);
					}

					// Folgende Map Enthält als Schlüssel die zu betrachtenden
					// Typen und als Wert jeweils ein Set mit den zu
					// betrachtenden Objekten des Typs:
					Map type2objectSetMap = new TreeMap();

					// Erzeugen der Map, d.h. sortieren nach Typen und
					// sicherstellen, dass jedes Objekt nur einmal enthalten
					// ist.
					for (Iterator objectIterator = objectList.iterator(); objectIterator
							.hasNext();) {
						SystemObject object = (SystemObject) objectIterator
								.next();
						SystemObjectType type = object.getType();
						Collection objectCollection = (Collection) type2objectSetMap
								.get(type);
						if (objectCollection == null) {
							objectCollection = new LinkedList();
							type2objectSetMap.put(type, objectCollection);
						}
						objectCollection.add(object);
					}

					objectList.clear();
					objectList = null;

					for (Iterator mapIterator = type2objectSetMap.entrySet()
							.iterator(); mapIterator.hasNext();) {
						Map.Entry entry = (Map.Entry) mapIterator.next();
						SystemObjectType type = (SystemObjectType) entry
								.getKey();
						Collection objectCollection = (Collection) entry
								.getValue();
						// Im folgenden werden alle Attributgruppen des Typs
						// betrachtet und diese eventuell weiter
						// eingeschränkt, dadurch werden unsinnige Spezifikation
						// ignoriert.
						List typeAtgs = type.getAttributeGroups();
						for (Iterator atgIterator = typeAtgs.iterator(); atgIterator
								.hasNext();) {
							AttributeGroup typeAtg = (AttributeGroup) atgIterator
									.next();
							// Konfigurierende Attributgruppen werden ignoriert
							if (typeAtg.isConfigurating())
								continue;

							// Wenn keine Attributgruppen spezifiziert wurden,
							// dann werden alle des Typs weiter betrachtet,
							// ansonsten werden nur die Attributgruppen des Typs
							// weiter betrachtet, die auch explizit
							// spezifiziert wurden.
							if (specifiedAtgs.length > 0
									&& Arrays.binarySearch(specifiedAtgs,
											typeAtg) < 0)
								continue;

							// Wenn die betrachtete Attributgruppe in der Liste
							// der nicht erlaubten Attributgruppen ist, wird
							// diese nicht weiter betrachtet.
							if (_excludedAttributeGroups.contains(typeAtg))
								continue;

							Collection atgAspects;

							atgAspects = typeAtg.getAspects();

							for (Iterator aspectIterator = atgAspects
									.iterator(); aspectIterator.hasNext();) {
								Aspect atgAspect = (Aspect) aspectIterator
										.next();
								// Wenn kein Aspekt spezifiziert wurde, dann
								// werden alle Aspekte der Attributgruppe weiter
								// betrachtet,
								// ansonsten werden nur die Aspekte weiter
								// betrachtet, die auch explizit
								// spezifiziert wurden.
								if (specifiedAspects.length > 0
										&& Arrays.binarySearch(
												specifiedAspects, atgAspect) < 0)
									continue;

								// Für alle spezifizierten Objekte des
								// betrachteten Typs die spezifizierte
								// Einstellung merken
								for (Iterator objectIterator = objectCollection
										.iterator(); objectIterator.hasNext();) {
									SystemObject object = (SystemObject) objectIterator
											.next();
									DataIdentification key = new DataIdentification(
											object, new DataDescription(
													typeAtg, atgAspect,
													simulationVariant));
									newSettings.put(key, settings);

								}
							}
						}
					}

				}
			}
		} catch (ConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private SystemObject[] removeNullReferences(SystemObject[] objects) {
		int numberOfNonNullObjects = 0;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null)
				numberOfNonNullObjects++;
		}
		if (numberOfNonNullObjects != objects.length) {
			SystemObject[] trimmedObjects = new SystemObject[numberOfNonNullObjects];
			int trimmedIndex = 0;
			for (int i = 0; i < objects.length; i++) {
				if (objects[i] != null)
					trimmedObjects[trimmedIndex++] = objects[i];
			}
			return trimmedObjects;
		} else {
			return objects;
		}
	}

	/**
	 * Iteriert über die Tabelle mit den Einstellungen je Datenidentifikation
	 * und informiert die angemeldeten Beobachter über Änderungen an den
	 * Einstellungen je Datenidentifikation. Die übergebenen Einstellungen
	 * werden als aktuelle Einstellungen übernommen.
	 */
	private final void activateSettings(Map newSettingsTable) {
		notifyStartOfSettings();
		// Schleife über Zeilen der neuen Tabelle
		// entsprechender Eintrag in alter Tabelle wird gelöscht
		// Benachrichtigung an alle Beobachter
		long startTime = System.currentTimeMillis();
		_debug.fine("+++aktiviere " + newSettingsTable.size()
				+ " neue Einstellungen");
		for (Iterator iterator = newSettingsTable.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			DataIdentification dataIdentification = (DataIdentification) entry
					.getKey();
			Data newSettings = (Data) entry.getValue();
			Data oldSettings = (Data) _settingsTable.remove(dataIdentification);
			notifySettings(dataIdentification, oldSettings, newSettings);
		}
		long duration = System.currentTimeMillis() - startTime;
		_debug
				.finer("+++Ende Aktivierung "
						+ newSettingsTable.size()
						+ " neue Einstellungen (Zeit: "
						+ duration
						+ " ms [pro Parametersatz "
						+ ((double) duration / (double) ((newSettingsTable
								.size() == 0) ? 1 : (newSettingsTable.size())))
						+ " ms]");

		_debug.fine("---deaktiviere verbleibende " + _settingsTable.size()
				+ " alte Einstellungen");
		startTime = System.currentTimeMillis();
		// Schleife über die verbleibenden Einträge in der alten Tabelle, das
		// sind die Einträge, zu denen es keine
		// neuen Einträge mehr gibt. Jeweils entsprechende Benachrichtigung der
		// angemeldeten Beobachter.
		for (Iterator iterator = _settingsTable.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			DataIdentification dataIdentification = (DataIdentification) entry
					.getKey();
			Data oldSettings = (Data) _settingsTable.get(dataIdentification);
			if ( oldSettings != null ) {
				notifySettings(dataIdentification, oldSettings, null);
			}
		}
		duration = System.currentTimeMillis() - startTime;
		_debug
				.finer("+++Ende Deaktivierung "
						+ _settingsTable.size()
						+ " alte Einstellungen (Zeit: "
						+ duration
						+ "ms [pro Parametersatz "
						+ ((double) duration / (double) ((_settingsTable.size() == 0) ? 1
								: (_settingsTable.size()))) + " ms]");
		// Neue Einstellungstabelle wird als aktuelle übernommen
		_settingsTable.clear();
		_settingsTable = newSettingsTable;
		notifyEndOfSettings();
	}

	/**
	 * Iteriert über alle Beobachter und gibt die Datenidentifikation mit alten
	 * und neuen Einstellungen an die {@link UpdateListener#update update(...)}
	 * Methode weiter.
	 *
	 * @param dataIdentification
	 *            Betroffene Datenidentifikation.
	 * @param oldSettings
	 *            Zur Datenidentifikation gehörende Einstellung vor der Änderung
	 *            oder <code>null</code> wenn es vor der Änderung keinen
	 *            spezifischen Eintrag gab.
	 * @param newSettings
	 *            Zur Datenidentifikation gehörende Einstellung nach der
	 *            Änderung oder <code>null</code>
	 */
	private final void notifySettings(DataIdentification dataIdentification,
			Data oldSettings, Data newSettings) {
		// Schleife über alle Beobachter und jeweils Aufruf der update-Methode
		for (UpdateListener listener : listenerList
				.getListeners(UpdateListener.class)) {
			listener.update(dataIdentification, oldSettings, newSettings);
		}
	}

	/**
	 * Iteriert über alle Beobachter, die informiert werden wollen, sobald
	 * begonnen wird die Einstellungen an den {@link UpdateListener} zu
	 * schicken.
	 */
	private final void notifyStartOfSettings() {
		for (StartOfSettingsListener listener : listenerList
				.getListeners(StartOfSettingsListener.class)) {
			listener.settingsGestartet();
		}
	}

	/**
	 * Iteriert über alle Beobachter, die informiert werden wollen, sobald alle
	 * Einstellungen an den {@link UpdateListener} geschickt wurden.
	 */
	private final void notifyEndOfSettings() {
		for (EndOfSettingsListener listener : listenerList
				.getListeners(EndOfSettingsListener.class)) {
			listener.inform();
		}
	}

	/**
	 * Klasse, die zur Entgegennahme der Parameterdatensätze vom Datenverteiler
	 * die entsprechende Update-Methode implementiert.
	 */
	private class Receiver implements ClientReceiverInterface {

		/**
		 * Flag, dass dafür sorgt, dass initiale leere Datensätze ignoriert
		 * werden
		 */
		private boolean _seenNonEmptyData = false;

		/**
		 * Der Datensatzindex des zuletzt verarbeiteten Parameterdatensatz wird
		 * als Workaround benutzt um doppelte Datensätze zu erkennen und zu
		 * ignorieren.
		 *
		 * Doppelte Datensätze können bei veränderten AnmeldeOptionen einer
		 * zweiten Anmeldung auf diese Datenidentifikation auftreten.
		 */
		private long _lastDataIndex = 0;

		/**
		 * Aktualisierungsmethode, die nach Empfang eines angemeldeten
		 * Datensatzes von den Datenverteiler-Applikationsfunktionen aufgerufen
		 * wird. Diese Methode muss von der Applikation zur Verarbeitung der
		 * empfangenen Datensätze implementiert werden.
		 *
		 * @param results
		 *            Feld mit den empfangenen Ergebnisdatensätzen.
		 */
		public void update(ResultData results[]) {
			Map newSettingsTable = new HashMap();
			boolean gotNewData = false;
			for (int i = 0; i < results.length; i++) {
				ResultData result = results[i];
				if (result.hasData())
					_seenNonEmptyData = true;
				long resultDataIndex = result.getDataIndex();
				if (resultDataIndex != _lastDataIndex) {
					_lastDataIndex = resultDataIndex;
					gotNewData = true;
					extractSettings(result, newSettingsTable);
				}
			}
			if (_seenNonEmptyData && gotNewData)
				activateSettings(newSettingsTable);
		}
	}
}
