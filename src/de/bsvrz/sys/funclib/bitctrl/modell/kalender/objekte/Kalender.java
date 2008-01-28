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
 * Wei�enfelser Stra�e 67
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
	 * F�gt ein Ereignis in die Ereignismenge des Kalenders ein.
	 * 
	 * @param ereignis
	 *            das Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das Einf�gen unzul�ssig ist.
	 */
	void add(Ereignis ereignis) throws ConfigurationChangeException;

	/**
	 * F�gt einen Ereignistyp in die Ereignistypmenge des Kalenders ein.
	 * 
	 * @param ereignisTyp
	 *            der Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Einf�gen unzul�ssig ist.
	 */
	void add(EreignisTyp ereignisTyp) throws ConfigurationChangeException;

	/**
	 * F�gt ein Systemkalendereintrag in die Systemkalendereintragsmenge des
	 * Kalenders ein.
	 * 
	 * @param eintrag
	 *            das Systemkalendereintrag.
	 * @throws ConfigurationChangeException
	 *             wenn das Einf�gen unzul�ssig ist.
	 */
	void add(SystemKalenderEintrag eintrag) throws ConfigurationChangeException;

	/**
	 * Registriert einen Listener f�r die �nderung der Ereignismenge.
	 * 
	 * @param l
	 *            der neue Listener.
	 */
	void addEreignisListener(EreignisListener l);

	/**
	 * Registriert einen Listener f�r die �nderung der Ereignistypmenge.
	 * 
	 * @param l
	 *            der neue Listener.
	 */
	void addEreignisTypListener(EreignisTypListener l);

	/**
	 * Registriert einen Listener f�r die �nderung der Menge
	 * Systemkalendereintr�ge.
	 * 
	 * @param l
	 *            der neue Listener.
	 */
	void addSystemKalenderEintragListener(SystemKalenderEintragListener l);

	/**
	 * Gibt die aktuelle Menge der Ereignisse zur�ck.
	 * 
	 * @return die Ereignismenge.
	 */
	Set<Ereignis> getEreignisse();

	/**
	 * Gibt die aktuelle Menge der Ereignistypen zur�ck.
	 * 
	 * @return die Ereignistypmenge.
	 */
	Set<EreignisTyp> getEreignisTypen();

	/**
	 * Gibt die aktuelle Menge der Systemkalendereintr�ge zur�ck.
	 * 
	 * @return die Menge der Systemkalendereintr�ge.
	 */
	Set<SystemKalenderEintrag> getSystemKalenderEintraege();

	/**
	 * Entfernt ein Ereignis aus der Ereignismenge des Kalenders.
	 * 
	 * @param ereignis
	 *            das Ereignis.
	 * @throws ConfigurationChangeException
	 *             wenn das Entfernen unzul�ssig ist.
	 */
	void remove(Ereignis ereignis) throws ConfigurationChangeException;

	/**
	 * Entfernt einen Ereignistyp aus der Ereignistypmenge des Kalenders.
	 * 
	 * @param ereignisTyp
	 *            der Ereignistyp.
	 * @throws ConfigurationChangeException
	 *             wenn das Entfernen unzul�ssig ist.
	 */
	void remove(EreignisTyp ereignisTyp) throws ConfigurationChangeException;

	/**
	 * Entfernt einen Systemkalendereintrag aus der Systemkalendereintragsmenge
	 * des Kalenders.
	 * 
	 * @param eintrag
	 *            das Systemkalendereintrag.
	 * @throws ConfigurationChangeException
	 *             wenn das Einf�gen unzul�ssig ist.
	 */
	void remove(SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException;

	/**
	 * Entfernet einen Listener, der auf die �nderung der Ereignismenge lauscht.
	 * 
	 * @param l
	 *            der zu entfernende Listener.
	 */
	void removeEreignisListener(EreignisListener l);

	/**
	 * Entfernet einen Listener, der auf die �nderung der Ereignistypmenge
	 * lauscht.
	 * 
	 * @param l
	 *            der zu entfernende Listener.
	 */
	void removeEreignisTypListener(EreignisTypListener l);

	/**
	 * Entfernet einen Listener, der auf die �nderung der Menge
	 * Systemkalendereintr�ge lauscht.
	 * 
	 * @param l
	 *            der zu entfernende Listener.
	 */
	void removeSystemKalenderEintragListener(SystemKalenderEintragListener l);

}
