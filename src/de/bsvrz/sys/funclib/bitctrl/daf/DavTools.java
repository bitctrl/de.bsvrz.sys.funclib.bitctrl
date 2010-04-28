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

package de.bsvrz.sys.funclib.bitctrl.daf;

import java.text.Collator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationAuthority;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.ObjectTimeSpecification;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;

/**
 * Allgemeine Funktionen im Zusammenhang mit
 * Datenverteiler-Applikationsfunktionen.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class DavTools {

	/** Der Typ Typ. */
	public static final String TYP_TYP = "typ.typ";

	/** Der Typ Attributgruppe. */
	public static final String TYP_ATTRIBUTGRUPPE = "typ.attributgruppe";

	/** Der Typ Aspekt. */
	public static final String TYP_ASPEKT = "typ.aspekt";

	/** Standardaspekt für Konfigurationsdaten. */
	public static final String ASP_EIGENSCHAFTEN = "asp.eigenschaften";

	/** Aspekt für Soll-Angaben eines Parameter. */
	public static final String ASP_PARAMETER_SOLL = "asp.parameterSoll";

	/** Aspekt für das Einstellen eines Parameter. */
	public static final String ASP_PARAMETER_VORGABE = "asp.parameterVorgabe";

	/** Aspekt für den Ist-Zustand eines Parameters. */
	public static final String ASP_PARAMETER_IST = "asp.parameterIst";

	/** Aspekt für den Standardwert eines Parameters. */
	public static final String ASP_PARAMETER_DEFAULT = "asp.parameterDefault";

	/**
	 * Konvertiert einen Zeitstempel in eine lesbare absolute Zeit.
	 * 
	 * @param zeitstempel
	 *            ein Zeitstempel.
	 * @return die entsprechende Zeit als lesbaren String.
	 */
	public static String absoluteZeit(final long zeitstempel) {
		return DateFormat.getDateTimeInstance().format(new Date(zeitstempel));
	}

	/**
	 * Generiert aus einem Objektnamen eine gültige PID. Es wird jedes Zeichen
	 * nach einem Leerzeichen in einen Großbuchstaben verwandelt, danach alle
	 * Leerzeichen entfernt und der erste Buchstabe des Namens in einen
	 * Kleinbuchstaben umgewandelt.
	 * 
	 * @param name
	 *            der Objektname.
	 * @param praefix
	 *            der Präfix für die PID (mit Punkt abgeschlossen).
	 * @return die gültige PID zum Objektnamen.
	 */
	public static String generierePID(final String name, final String praefix) {
		String pid, regex;
		Matcher matcher;

		regex = "(\\S)+(\\s)";
		matcher = Pattern.compile(regex).matcher(name);
		if (matcher.find()) {
			pid = matcher.group(0).trim();

			regex = "(\\s)+(\\S)+";
			matcher = Pattern.compile(regex).matcher(name);
			while (matcher.find()) {
				final String s = matcher.group(0).trim();
				pid += s.substring(0, 1).toUpperCase();
				pid += s.substring(1);
			}
		} else {
			pid = name;
		}
		pid = pid.substring(0, 1).toLowerCase() + pid.substring(1);

		return praefix + pid;
	}

	/**
	 * Liefert einen int-Wert, der als Boolean-Ersatz für JaNein-Werte innerhalb
	 * einer Datenverteiler-Attributgruppe verschickt werden kann.
	 * 
	 * <p>
	 * <em>Hinweis:</em> Setzt Verwendung des vorhandenen Standardattribitts
	 * att.jaNein voraus, in dem <code>false</code> durch 0 repräsentiert wird.
	 * 
	 * @param wert
	 *            ein boolean-Wert.
	 * @return der int-Wert.
	 */
	public static int int2Bool(final boolean wert) {
		return wert ? 1 : 0;
	}

	/**
	 * Liefert einen boolean-Wert, der als int-Ersatz für JaNein-Werte innerhalb
	 * einer Datenverteiler-Attributgruppe verschickt werden kann.
	 * 
	 * <p>
	 * <em>Hinweis:</em> Setzt Verwendung des vorhandenen Standardattribitts
	 * att.jaNein voraus, in dem <code>false</code> durch 0 repräsentiert wird.
	 * 
	 * @param wert
	 *            der int-Wert.
	 * @return der boolean-wert.
	 */
	public static boolean bool2Int(final int wert) {
		return wert == 0 ? false : true;
	}

	/**
	 * Sortiert eine Liste von Systemobjekten nach deren Namen. Beim Sortieren
	 * werden deutsche Umlaute berücksichtigt.
	 * <p>
	 * <em>Hinweis:</em> Das Ergebnis wird im Parameter abgelegt, der
	 * Rückgabewert ist identisch und für chaining-Aufrufe gedacht.
	 * 
	 * @param objekte
	 *            die zu sortierende Liste.
	 * @return die sortierte Liste.
	 */
	public static List<? extends SystemObject> sortiere(
			final List<? extends SystemObject> objekte) {
		Collections.sort(objekte, new Comparator<SystemObject>() {

			public int compare(final SystemObject so1, final SystemObject so2) {
				final Collator de = Collator.getInstance(Locale.GERMANY);
				return de.compare(so1.toString(), so2.toString());
			}

		});
		return objekte;
	}

	/**
	 * Bestimmt rekursiv alle Supertypen eines Systemobjekttyps.
	 * 
	 * @param objectType
	 *            ein Systemobjekttyp.
	 * @return alle Typen, die der übergebene Typ direkt oder indirekt
	 *         erweitert.
	 */
	public static Set<SystemObjectType> getSuperTypes(
			final SystemObjectType objectType) {
		final Set<SystemObjectType> superTypes = new HashSet<SystemObjectType>();

		superTypes.addAll(objectType.getSuperTypes());
		for (final SystemObjectType type : superTypes) {
			superTypes.addAll(getSuperTypes(type));
		}

		return superTypes;
	}

	/**
	 * Überprüft ob der übergebene Wert eine gültige Simulationsvariante ist.
	 * Liegt die Simulationsvariante nicht im Bereich 0..999, wird eine
	 * {@link NoSimulationException} augelöst.
	 * 
	 * @param sim
	 *            die zu prüfende Simulationsvariante.
	 * @throws NoSimulationException
	 *             wenn der Wert keine echte Simulationsvariante ist.
	 */
	public static void validiereSimulationsVariante(final short sim)
			throws NoSimulationException {
		if (sim < 0 || sim > 999) {
			throw new RuntimeException(
					"Wert ist keine gültige Simulationsvariante: " + sim);
		}
	}

	/**
	 * Überprüft ob der übergebene Wert eine echte Simulationsvariante ist.
	 * Liegt die Simulationsvariante nicht im Bereich 1..999, wird eine
	 * {@link NoSimulationException} augelöst.
	 * 
	 * @param sim
	 *            die zu prüfende Simulationsvariante.
	 * @throws NoSimulationException
	 *             wenn der Wert keine echte Simulationsvariante ist.
	 */
	public static void validiereEchteSimulationsVariante(final short sim)
			throws NoSimulationException {
		if (sim < 1 || sim > 999) {
			throw new RuntimeException(
					"Wert ist keine echte Simulationsvariante: " + sim);
		}
	}

	/**
	 * Liefert den Standardkonfigurationsbereich für übergebene
	 * Datenverteilerverbindung. Der Standardkonfigurationsbereich wird
	 * verwendet um dynamische Objekte abzulegen, sofern kein expliziter
	 * Zielbereich für diesen Zweck definiert wurde.
	 * 
	 * @param verbindung
	 *            die Verbindung, deren Standardbereich ermittelt werden soll
	 * @return den ermittelten Konfigurationsbereich.
	 * @throws InvalidArgumentException
	 *             wenn für die übergebene Verbindung kein entsprechender
	 *             Konfigurationsbereich ermittelt werden.
	 */
	public static ConfigurationArea getDefaultKonfigurationsBereich(
			final ClientDavInterface verbindung)
			throws InvalidArgumentException {
		if (verbindung == null) {
			throw new NullPointerException(
					"Parameter für Datenverteilerverbindung darf nicht null sein.");
		}

		final ConfigurationAuthority authority = verbindung
				.getLocalApplicationObject().getConfigurationArea()
				.getConfigurationAuthority();
		final DataModel model = authority.getDataModel();
		final AttributeGroup atg = model
				.getAttributeGroup("atg.konfigurationsVerantwortlicherEigenschaften");
		final Data daten = authority.getConfigurationData(atg);
		final String bereichsPid = daten.getTextArray("defaultBereich")
				.getText(0);
		final SystemObject objekt = model.getObject(bereichsPid);
		if (objekt instanceof ConfigurationArea) {
			return (ConfigurationArea) objekt;
		}

		throw new InvalidArgumentException(
				"Für die übergebene Datenverteilerverbindung konnte kein Defaultbereich ermittelt werden.");
	}

	/**
	 * Sucht zu einer Menge von PIDs die dazugehörigen Systemobjekte. Kann in
	 * für die PID eines Konfigurationsbereichs die enthalten Systemobjekte und
	 * für die PID eines Typs alle dazugehörigen Instanzen ermitteln. Praktische
	 * Funktion für die Auswertung von PIDs bei Kommandozeilenargumenten.
	 * 
	 * @param dav
	 *            die Datenverteilerverbindung, die zu Abfrage genutzt werden
	 *            soll.
	 * @param typenAufloesen
	 *            <code>true</code>, wenn zu Objekttypen alle Objekte dieser
	 *            Typen bestimmt werden sollen. Anderfalls wird der Typ selbst
	 *            ins Ergebnis aufgenommen.
	 * @param bereicheAufloesen
	 *            <code>true</code>, wenn zu Konfigurationsbereichen alle
	 *            Objekte, die in den jeweiligen Bereich enthalten sind,
	 *            bestimmt werden sollen. Anderfalls wird der
	 *            Konfigurationsbereich selbst ins Ergebnis aufgenommen. Typen
	 *            in Konfigurationsbereichen werden <em>nicht</em> rekursiv
	 *            aufgelöst.
	 * @param pids
	 *            eine Liste von PIDs von Systemobjekttypen.
	 * @return die Liste der gesuchten Systemobjekte.
	 */
	public static Collection<SystemObject> getObjekte(
			final ClientDavInterface dav, final boolean typenAufloesen,
			final boolean bereicheAufloesen, final String... pids) {
		final List<SystemObject> result = new ArrayList<SystemObject>();
		final DataModel model = dav.getDataModel();

		for (final String pid : pids) {
			final SystemObject so = model.getObject(pid);
			if (so instanceof SystemObjectType && typenAufloesen) {
				final SystemObjectType type = (SystemObjectType) so;
				result.addAll(type.getObjects());
			} else if (so instanceof ConfigurationArea && bereicheAufloesen) {
				final ConfigurationArea area = (ConfigurationArea) so;
				final Collection<SystemObject> bereich = area.getObjects(null,
						ObjectTimeSpecification.valid());
				result.addAll(bereich);
			} else {
				result.add(so);
			}
		}
		return result;
	}

	/**
	 * Gibt alle Systemobjekte aus einem Konfigurationsbereich zurück. Mit dem
	 * zweitem Parameter kann der Typ der gesuchten Objekte eingeschränkt
	 * werden. Praktische Funktion für die Auswertung von PIDs bei
	 * Kommandozeilenargumenten.
	 * 
	 * @param kb
	 *            ein Konfigurationsbereich.
	 * @param pidTypen
	 *            ein optionales Feld von PIDs von Systemobjekttypen.
	 * @return die Liste der gesuchten Systemobjekte.
	 */
	public static Collection<SystemObject> getObjekte(
			final ConfigurationArea kb, final String... pidTypen) {
		if (pidTypen == null) {
			return kb.getObjects(null, ObjectTimeSpecification.valid());
		}

		final List<SystemObjectType> objekttypen = new ArrayList<SystemObjectType>();
		for (final String typ : pidTypen) {
			objekttypen.add(kb.getDataModel().getType(typ));
		}
		return kb.getObjects(objekttypen, ObjectTimeSpecification.valid());
	}

	private DavTools() {
		// Keine Instanzen von Utilities-Klassen erlaubt.
	}

}
