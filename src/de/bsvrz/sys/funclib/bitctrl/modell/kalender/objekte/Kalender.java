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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.Set;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * Repr&auml;sentiert den Ereigniskalender.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface Kalender extends SystemObjekt {

	/**
	 * Fügt ein Ereignis in die Ereignismenge des Kalenders ein.
	 * 
	 * @param ereignis
	 *            das Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das Einfügen unzulässig ist.
	 */
	void add(Ereignis ereignis) throws ConfigurationChangeException;

	/**
	 * Fügt einen Ereignistyp in die Ereignistypmenge des Kalenders ein.
	 * 
	 * @param ereignisTyp
	 *            der Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Einfügen unzulässig ist.
	 */
	void add(EreignisTyp ereignisTyp) throws ConfigurationChangeException;

	/**
	 * Fügt ein Systemkalendereintrag in die Systemkalendereintragsmenge des
	 * Kalenders ein.
	 * 
	 * @param eintrag
	 *            das Systemkalendereintrag.
	 * @throws ConfigurationChangeException
	 *             wenn das Einfügen unzulässig ist.
	 */
	void add(SystemKalenderEintrag eintrag) throws ConfigurationChangeException;

	/**
	 * Registriert einen Listener für die Änderung der Ereignismenge.
	 * 
	 * @param l
	 *            der neue Listener.
	 */
	void addEreignisListener(EreignisListener l);

	/**
	 * Registriert einen Listener für die Änderung der Ereignistypmenge.
	 * 
	 * @param l
	 *            der neue Listener.
	 */
	void addEreignisTypListener(EreignisTypListener l);

	/**
	 * Registriert einen Listener für die Änderung der Menge
	 * Systemkalendereinträge.
	 * 
	 * @param l
	 *            der neue Listener.
	 */
	void addSystemKalenderEintragListener(SystemKalenderEintragListener l);

	/**
	 * Gibt die aktuelle Menge der Ereignisse zurück.
	 * 
	 * @return die Ereignismenge.
	 */
	Set<Ereignis> getEreignisse();

	/**
	 * Gibt die aktuelle Menge der Ereignistypen zurück.
	 * 
	 * @return die Ereignistypmenge.
	 */
	Set<EreignisTyp> getEreignisTypen();

	/**
	 * Gibt die aktuelle Menge der Systemkalendereinträge zurück.
	 * 
	 * @return die Menge der Systemkalendereinträge.
	 */
	Set<SystemKalenderEintrag> getSystemKalenderEintraege();

	/**
	 * Entfernt ein Ereignis aus der Ereignismenge des Kalenders.
	 * 
	 * @param ereignis
	 *            das Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das Entfernen unzulässig ist.
	 */
	void remove(Ereignis ereignis) throws ConfigurationChangeException;

	/**
	 * Entfernt einen Ereignistyp aus der Ereignistypmenge des Kalenders.
	 * 
	 * @param ereignisTyp
	 *            der Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Entfernen unzulässig ist.
	 */
	void remove(EreignisTyp ereignisTyp) throws ConfigurationChangeException;

	/**
	 * Entfernt einen Systemkalendereintrag aus der Systemkalendereintragsmenge
	 * des Kalenders.
	 * 
	 * @param eintrag
	 *            das Systemkalendereintrag.
	 * @throws ConfigurationChangeException
	 *             wenn das Einfügen unzulässig ist.
	 */
	void remove(SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException;

	/**
	 * Entfernet einen Listener, der auf die Änderung der Ereignismenge lauscht.
	 * 
	 * @param l
	 *            der zu entfernende Listener.
	 */
	void removeEreignisListener(EreignisListener l);

	/**
	 * Entfernet einen Listener, der auf die Änderung der Ereignistypmenge
	 * lauscht.
	 * 
	 * @param l
	 *            der zu entfernende Listener.
	 */
	void removeEreignisTypListener(EreignisTypListener l);

	/**
	 * Entfernet einen Listener, der auf die Änderung der Menge
	 * Systemkalendereinträge lauscht.
	 * 
	 * @param l
	 *            der zu entfernende Listener.
	 */
	void removeSystemKalenderEintragListener(SystemKalenderEintragListener l);

}
