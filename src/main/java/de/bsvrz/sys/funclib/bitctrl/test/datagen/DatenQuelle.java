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

package de.bsvrz.sys.funclib.bitctrl.test.datagen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Die Realisierung einer einzelnen Datenquelle zur Erstellung der Eingabedatei
 * für den Datenverteiler-Datengenerator.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
class DatenQuelle {

	/**
	 * Konstanten zur Definition des aktuell eingelesenen Bereichs der
	 * Konfigurationsdatei.
	 *
	 * @author peuker
	 *
	 */
	private enum Bereich {
		/**
		 * Bereich für die allgemeinen Konfigurationsdaten der Datenquelle.
		 */
		CONFIG, /**
		 * Bereich für die Standardwerte für die Attribute eines
		 * Datensatzes.
		 */
		DEFAULT, /**
		 * Bereich für die variablen Werte eines Datensatzes.
		 */
		DATEN;
	}

	/**
	 * Logger für Debugausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die Menge der Systemobjekte, für die Daten versendet werden sollen.
	 */
	private final Set<SystemObject> objekte = new HashSet<>();

	/**
	 * der Aspekt unter den die Daten versendet werden sollen.
	 */
	private Aspect asp;

	/**
	 * die Attributgruppe zur Beschreibung eines Datensatzes.
	 */
	private AttributeGroup atg;

	/**
	 * die Simulationsvariante unter die Daten versendet werden sollen.
	 */
	private short simulationsVariante;

	/**
	 * die Definition der Standardwerte für die Attribute des Datensatzes.
	 */
	private final Map<String, String> defaultWerte = new HashMap<>();

	/**
	 * die Spaltenbezeichungen des Datenabschnittes.
	 */
	private List<String> csvHeaders;

	/**
	 * die Daten für die zu versendenden Datensätze als CSV-EInträge
	 * (Trennzeichen ';').
	 */
	private final SortedMap<Long, List<String>> csvdaten = new TreeMap<>();

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private final ClientDavInterface connection;

	/**
	 * das Datemnmodell.
	 */
	private final DataModel model;

	/**
	 * die Rolle, unter der der Datenversand erfolgen soll (Quelle oder Sender).
	 */
	private String rolle;

	/**
	 * erzeugt eine Datenquelle.
	 *
	 * @param connection
	 *            die verwendete Datenverteilerverbindung.
	 * @param string
	 *            der Name der Konfigurationsdatei, die die Quelle beschreibt.
	 */
	DatenQuelle(final ClientDavInterface connection, final String string) {

		this.connection = connection;
		model = connection.getDataModel();

		final File file = new File(string);
		Bereich aktuellerBereich = null;

		try (final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.defaultCharset()))) {

			String line = null;
			do {
				line = reader.readLine();
				if (line != null) {
					line = line.trim();
					if ((line.length() <= 0) || line.startsWith("#") || (line.startsWith("//"))) {
						// Kommentar wird vernachlässigt
						LOGGER.finest("Ignoriere Kommentarzeile: " + line);
					} else if (line.toUpperCase().startsWith("[CONFIG]")) {
						aktuellerBereich = Bereich.CONFIG;
					} else if (line.toUpperCase().startsWith("[DEFAULT]")) {
						aktuellerBereich = Bereich.DEFAULT;
					} else if (line.toUpperCase().startsWith("[DATEN]")) {
						aktuellerBereich = Bereich.DATEN;
					} else {
						switch (aktuellerBereich) {
						case CONFIG:
							addConfigurationData(line);
							break;
						case DEFAULT:
							addDefaultData(line);
							break;
						case DATEN:
							addData(line);
							break;
						default:
							System.err.println(line);
							break;
						}
					}
				}
			} while (line != null);
		} catch (final IOException e) {
			LOGGER.error("Fehler beim einlesen der Datenquelle: " + e.getMessage());
		}

	}

	/**
	 * verarbeitet einen Eintrag aus dem Konfigurationsbereich der
	 * Konfigurationsdatei.
	 *
	 * @param line
	 *            die Zeile aus der Konfigurationsdatei
	 */
	private void addConfigurationData(final String line) {
		final int dataIdx = line.indexOf('=');
		String daten = "";
		if (dataIdx >= 0) {
			daten = line.substring(dataIdx + 1).trim();
		}

		if (line.startsWith("objekt")) {
			final StringTokenizer tokenizer = new StringTokenizer(daten, ",");
			while (tokenizer.hasMoreTokens()) {
				final String pid = tokenizer.nextToken();
				final SystemObject object = model.getObject(pid);
				if (object instanceof SystemObjectType) {
					objekte.addAll(((SystemObjectType) object).getElements());
				} else {
					objekte.add(object);
				}
			}
		} else if (line.startsWith("atg")) {
			atg = model.getAttributeGroup(daten);
		} else if (line.startsWith("asp")) {
			asp = model.getAspect(daten);
		} else if (line.startsWith("simulationsVariante")) {
			simulationsVariante = Short.valueOf(daten);
		} else if (line.startsWith("rolle")) {
			rolle = daten;
		}

	}

	/**
	 * fügt einen Eintrag aus dem Abschnitt für die variablen Daten hinzu.
	 *
	 * @param line
	 *            die Zeile aus der Konfigurationsdatei
	 */
	private void addData(final String line) {
		if (csvHeaders == null) {
			csvHeaders = new ArrayList<>();
			final StringTokenizer tokenizer = new StringTokenizer(line, ";");
			while (tokenizer.hasMoreTokens()) {
				csvHeaders.add(tokenizer.nextToken().trim());
			}
			if (!csvHeaders.get(0).equals("Zeit")) {
				throw new RuntimeException("Ungültige Datenkonfiguration: Erste Spalte muss Zeit sein");
			}
			csvHeaders.remove(0);
		} else {
			final StringTokenizer tokenizer = new StringTokenizer(line, ";");
			final Long offset = Long.valueOf(tokenizer.nextToken().trim());
			final List<String> datenSatz = new ArrayList<>();
			csvdaten.put(offset, datenSatz);
			while (tokenizer.hasMoreTokens()) {
				datenSatz.add(tokenizer.nextToken());
			}
		}
	}

	/**
	 * fügt einen Eintrag für den Standardwert eines Attributes des Datensatzes
	 * hinzu.
	 *
	 * @param line
	 *            die Zeile aus der Konfigurationsdatei
	 */
	private void addDefaultData(final String line) {
		final StringTokenizer tokenizer = new StringTokenizer(line, "=");
		String attr = null;
		String wert = null;
		if (tokenizer.hasMoreTokens()) {
			attr = tokenizer.nextToken().trim();
		}
		if (tokenizer.hasMoreTokens()) {
			wert = tokenizer.nextToken().trim();
		} else {
			wert = "";
		}

		if (attr != null) {
			defaultWerte.put(attr, wert);
		}
	}

	/**
	 * füllt den Datenverteilerdatensatz mit den konfigurierten Werten.
	 *
	 * @param daten
	 *            der Zieldatensatz
	 * @param offset
	 *            der Offset für den Ausführungszeitpunkt
	 */
	private void fuelleDatenSatz(final Data daten, final long offset) {
		for (final Entry<String, String> entry : defaultWerte.entrySet()) {
			setzeAttribut(daten, entry.getKey(), entry.getValue());
		}

		final List<String> datenListe = csvdaten.get(offset);
		for (int idx = 0; idx < Math.min(csvHeaders.size(), datenListe.size()); idx++) {
			setzeAttribut(daten, csvHeaders.get(idx), datenListe.get(idx));
		}

	}

	/**
	 * liefert den Arrayindex aus einem Atttributnamen.
	 *
	 * @param attName
	 *            der auszuwertende Name
	 * @return der Index oder -1, wenn es sich nicht um ein Feldattribut handelt
	 */
	private int getArrayIndex(final String attName) {
		int arrayIndex = -1;
		final int index = attName.indexOf('[');
		if (index >= 0) {
			arrayIndex = Integer.parseInt(attName.substring(index + 1, attName.indexOf(']')));
		}
		return arrayIndex;
	}

	/**
	 * liefert die den zu versendeden Datensätze für den übergebenen
	 * Startzeitpunkt.
	 *
	 * @param startZeit
	 *            der absolute Startzeotpunkt für die Gesamtdatei.
	 * @param offset
	 *            der Offset für den aktuellen Datensatz
	 * @return die Liste der zu versendeten Datensätze.
	 */
	Collection<ResultData> getAusgabeDaten(final long startZeit, final long offset) {

		final Collection<ResultData> resultData = new ArrayList<>();

		final Data daten = connection.createData(atg);

		fuelleDatenSatz(daten, offset);

		for (final SystemObject object : objekte) {
			resultData.add(new ResultData(object, new DataDescription(atg, asp, simulationsVariante),
					startZeit + (offset * TimeUnit.SECONDS.toMillis(1)), daten));
		}

		return resultData;
	}

	/**
	 * liefert die Beschreibung des Datensatzes in der Form
	 * &lt;atg&gt;:&lt;aspekt&gt;:&lt;simulationsvariante&gt;.
	 *
	 * @return die Repräsentation der Datenbeschreibung
	 */
	String getDatenBeschreibung() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(atg.getPid());
		buffer.append(':');
		buffer.append(asp.getPid());
		buffer.append(':');
		buffer.append(simulationsVariante);
		return buffer.toString();
	}

	/**
	 * liefert den "reinen" Namen des Attributs, d.h. eventuelle Feldindizes
	 * werden eliminiert.
	 *
	 * @param attName
	 *            der Name des Attributs
	 * @return den bereinigten Namen
	 */
	private String getItemName(final String attName) {
		final int index = attName.indexOf('[');
		if (index >= 0) {
			return (attName.substring(0, index));
		}
		return attName;
	}

	/**
	 * liefert den auf den übergebenen Zeitpunkt nächstfolgenden Zeitpunkt für
	 * den Versand eines Datensatzes. Die Zeitpunkte ergeben sich aus den im
	 * Datenberecih definierten Einträgen für die relativen Zeitstempel.
	 *
	 * @param wert
	 *            der Zeuitpunkt nach dem der nächste Start gesucht ist.
	 * @return den nächstfolgenden Zeitpunkt oder -1, wenn es keinen weiteren
	 *         gibt.
	 */
	long getNextStart(final long wert) {
		long result = -1;
		final SortedMap<Long, List<String>> sub = csvdaten.tailMap(wert + 1);
		if ((sub != null) && (sub.size() > 0)) {
			result = sub.firstKey();
		}
		return result;
	}

	/**
	 * liefert die Liste der Systemobjekte, für die Daten versendet werden
	 * sollen.
	 *
	 * @return die Liste der Objekte
	 */
	Collection<SystemObject> getObjekte() {
		return objekte;
	}

	/**
	 * liefert die definierte Rolle für den Datenversand.
	 *
	 * @return die Rolle
	 */
	String getRolle() {
		return rolle;
	}

	/**
	 * setzt den mit dem Namen definierten Dateneintrag auf den übergebenen
	 * Wert.
	 *
	 * @param daten
	 *            der Datensatz
	 * @param name
	 *            der Name des Attributs
	 * @param wert
	 *            der zu setzende Wert
	 */
	private void setzeAttribut(final Data daten, final String name, final String wert) {
		final StringTokenizer attributListe = new StringTokenizer(name, ".");
		final List<String> attNamen = new ArrayList<>();
		while (attributListe.hasMoreElements()) {
			attNamen.add(attributListe.nextToken());
		}

		Data data = daten;
		while (attNamen.size() > 0) {
			final String attName = attNamen.get(0);
			attNamen.remove(0);

			final int arrayIndex = getArrayIndex(attName);
			final String dataName = getItemName(attName);
			if (arrayIndex >= 0) {
				final Data.Array array = data.getArray(dataName);
				array.setLength(Math.max(data.getArray(dataName).getLength(), arrayIndex + 1));
				data = array.getItem(arrayIndex);
			} else {
				data = data.getItem(getItemName(dataName));
			}
		}

		if (data.getAttributeType() instanceof IntegerAttributeType) {
			if (((IntegerAttributeType) data.getAttributeType()).getRange() != null) {
				try {
					data.asScaledValue().set(Long.parseLong(wert));
				} catch (final NumberFormatException e) {
					data.asTextValue().setText(wert);
				}
			} else {
				try {
					data.asUnscaledValue().set(Long.parseLong(wert));
				} catch (final NumberFormatException e) {
					data.asTextValue().setText(wert);
				}
			}
		} else {
			data.asTextValue().setText(wert);
		}
	}
}
