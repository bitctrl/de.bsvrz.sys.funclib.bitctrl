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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bitctrl.util.CollectionUtilities;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;

/**
 * Allgemeine Funktionen im Zusammenhang mit
 * Datenverteiler-Applikationsfunktionen.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class DavTools {

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
	 * vor einem Leerzeichen in einen Großbuchstaben verwandelt, danach alle
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
	 * liefert einen Integerwert, der als Boolean-Ersatz für JaNein-Werte
	 * innerhalb einer Datenverteiler-Attributgruppe verschicht werden kann.
	 * 
	 * @param wert
	 *            der Boolwert
	 * @return der Integerwert
	 */
	public static int intWertVonBoolean(final boolean wert) {
		int ergebnis = 0;
		if (wert) {
			ergebnis = 1;
		}

		return ergebnis;
	}

	/**
	 * Sortiert eine Liste von Systemobjekten nach deren Namen. Beim Sortieren
	 * werden deutsche Umlaute ber&uuml;cksichtigt.
	 * <p>
	 * <em>Hinweis:</em> Das Ergebnis wird auch im Parameter abgelegt.
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

	public static SystemObjectType getSuperType(
			final Collection<SystemObject> objects) {
		Set<SystemObjectType> basis = null;

		for (final SystemObject so : objects) {
			if (basis == null) {
				basis = getSuperTypes(so.getType());
			} else {
				basis = CollectionUtilities.intersection(basis,
						getSuperTypes(so.getType()));
			}
		}

		if (basis.isEmpty()) {
			return null;
		}

		while (basis.size() > 1) {
			final Iterator<SystemObjectType> iterator = basis.iterator();
			final SystemObjectType type1 = iterator.next();
			final Set<SystemObjectType> remove = new HashSet<SystemObjectType>();

			while (iterator.hasNext()) {
				final SystemObjectType type2 = iterator.next();
				if (type1.inheritsFrom(type2)) {
					remove.add(type2);
				}
				if (type2.inheritsFrom(type1)) {
					remove.add(type1);
				}
			}

			basis.removeAll(remove);
		}

		return basis.iterator().next();
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
		final Set<SystemObjectType> superTypes = new HashSet<SystemObjectType>(
				objectType.getSuperTypes());

		for (final SystemObjectType type : superTypes) {
			superTypes.addAll(getSuperTypes(type));
		}

		return superTypes;
	}

	/**
	 * überprüft die Gültigkeit der übergebenen Simulationsvariante.<br>
	 * Liegt die Simulationsvariante nicht im Bereich 0 ... 999, wird eine
	 * RuntimeException augelöst.
	 * 
	 * @param sim
	 *            die Simulationsvariante
	 */
	public static void validiereSimulationsVariante(final short sim) {
		try {
			validiereSimulationsVariante(sim, false);
		} catch (final NoSimulationException e) {
			// Alle gültigen Simulationsvarianten werden akzeptiert
		}
	}

	/**
	 * überprüft die Gültigkeit der übergebenen Simulationsvariante.<br>
	 * Liegt die Simulationsvariante nicht im Bereich 0 ... 999, wird eine
	 * RuntimeException augelöst.<br>
	 * Wird der Parameter <i>simulation</i> auf den Wert <i>true</i> gesetzt,
	 * werden nur echte Simulationen, d.h. Simulationsvarianten &gt; 0
	 * zugelassen.
	 * 
	 * @param sim
	 *            die Simulationsvariante
	 * @param simulation
	 *            nur echte Simulation ?
	 * @throws NoSimulationException
	 *             wenn die Simulationsvariante 0 ist.
	 */
	public static void validiereSimulationsVariante(final short sim,
			final boolean simulation) throws NoSimulationException {

		if (sim < 0 || sim > 999) {
			throw new RuntimeException(
					"Die Verwendung der ungültigen Simulationsvariante: \""
							+ sim + "\" ist nicht erlaubt");
		}

		if (simulation) {
			if (sim == 0) {
				throw new NoSimulationException();
			}
		}

	}

	/**
	 * Defaultkonstruktor verstecken.
	 */
	private DavTools() {
		// nix
	}

}
