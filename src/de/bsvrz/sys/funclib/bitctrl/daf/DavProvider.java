/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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

import java.beans.PropertyChangeListener;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Stellt eine Verbindung zum Datenverteiler zur Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface DavProvider {

	/** Name der Property, die den Namen der Verbindung hält. */
	String PROP_NAME = "name";

	/** Name der Property, die die Datenverteilerverbindung hält. */
	String PROP_DAV = "dav";

	/** Name der Property, die das Flag <em>verbunden</em> hält. */
	String PROP_VERBUNDEN = "verbunden";

	/** Der Defaultname für die Nutzerverbindung. */
	String NUTZVERVERBINDUNG = "Nutzerverbindung";

	/** Der Defaultname für die Urlasserverbindung. */
	String URLASSERVERBINDUNG = "Urlasserverbindung";

	/**
	 * Gibt den Namen der Verbindung zurück.
	 * 
	 * @return der Verbindungsname.
	 */
	String getName();

	/**
	 * Gibt die Verbindung zum Datenverteiler zurück.
	 * 
	 * @return die Verbindung.
	 */
	ClientDavInterface getDav();

	/**
	 * Flag ob eine Datenverteilerverbindung besteht.
	 * 
	 * @return <code>true</code>, wenn eine aktuell eine Verbindung besteht,
	 *         sonst <code>false</code>.
	 */
	boolean isVerbunden();

	/**
	 * Registriert einen Listener auf eine Property der Klasse.
	 * 
	 * @param listener
	 *            der Listener.
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Registriert einen Listener auf eine Property der Klasse.
	 * 
	 * @param propertyName
	 *            der Name der zu beobachtenden Propertery.
	 * @param listener
	 *            der Listener.
	 */
	void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

	/**
	 * Meldet einen Listener auf eine Property der Klasse wieder ab.
	 * 
	 * @param listener
	 *            der Listener.
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Meldet einen Listener auf eine Property der Klasse wieder ab.
	 * 
	 * @param propertyName
	 *            der Name der Property die nicht mehr beobachtet werden soll.
	 * @param listener
	 *            der Listener.
	 */
	void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

}
