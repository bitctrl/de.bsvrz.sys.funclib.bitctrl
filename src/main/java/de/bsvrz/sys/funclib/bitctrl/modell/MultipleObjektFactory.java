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

import java.util.HashMap;
import java.util.Map;

/**
 * Erlaubt die Verwendung mehrerer Objektfabriken in einer Applikation. Die
 * Fabriken werden über den ihnen zugewiesenen Namen verwaltet.
 *
 * @author BitCtrl Systems GmbH, Falko
 */
public class MultipleObjektFactory extends ObjektFactory {

	private static MultipleObjektFactory singleton;

	/**
	 * Gibt die einzige Instanz dieser Klasse zurück.
	 *
	 * @return das Singleton.
	 */
	public static MultipleObjektFactory getInstanz() {
		if (singleton == null) {
			singleton = new MultipleObjektFactory();
		}
		return singleton;
	}

	private final Map<String, ObjektFactory> objektFactories;

	/**
	 * Öffentlichen Konstruktor verstecken.
	 */
	protected MultipleObjektFactory() {
		objektFactories = new HashMap<>();
	}

	/**
	 * Gibt die Objektfabrik mit dem angegebenen Namen zurück. Existiert diese
	 * noch nicht, wird sie angelegt. Eine neu angelegt Fabrik ist noch nicht
	 * initialisiert.
	 *
	 * @param verbindungsName
	 *            der Name der gesuchten Datenverteilerverbindung.
	 * @return die Datenverteilerverbindung.
	 * @see ObjektFactory#setVerbindung(de.bsvrz.dav.daf.main.ClientDavInterface)
	 * @see ObjektFactory#registerStandardFactories()
	 * @see ObjektFactory#registerFactory(ModellObjektFactory...)
	 */
	public synchronized ObjektFactory getDav(final String verbindungsName) {
		if (!objektFactories.containsKey(verbindungsName)) {
			objektFactories.put(verbindungsName, new ObjektFactory());
		}
		return objektFactories.get(verbindungsName);
	}

}
