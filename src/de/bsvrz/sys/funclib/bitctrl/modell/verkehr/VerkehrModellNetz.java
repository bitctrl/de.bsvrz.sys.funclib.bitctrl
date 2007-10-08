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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Repr&auml;sentiert ein Stra&szlig;ensegment.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @version $Id$
 */
public class VerkehrModellNetz extends Netz {

	/**
	 * Name der Menge, in der die Staus des VerkehrsmodellNetz abgelegt werden.
	 */
	public static final String MENGENNAME_STAUS = "Staus"; //$NON-NLS-1$

	/**
	 * Logger für Fehlerausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/** PID des Typs eines VerkehrsModellNetz. */
	@SuppressWarnings("hiding")
	public static final String PID_TYP = "typ.verkehrsModellNetz"; //$NON-NLS-1$

	/**
	 * Konstruiert aus einem Systemobjekt ein Netz.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Netz darstellt
	 * @throws IllegalArgumentException
	 */
	public VerkehrModellNetz(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(PID_TYP)) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein gültiges VerkehrsModellNetz."); //$NON-NLS-1$
		}
	}

	/**
	 * liefert die Liste der äußeren Straßensegmente, die das Netz bilden und
	 * zur übergebenen Straße gehören. Die Liste enthält alle äußeren
	 * Straßensegmente, die innerhalb des Netzes selbst konfiguriert sind und
	 * zusätzlich die Segmente aus den Listen der Unternetze.
	 * 
	 * @param strasse
	 *            die Straße, für die die äußeren Straßensegemente gesucht
	 *            werden
	 * @return die Liste der ermittelten Straßensegmente
	 */
	public List<AeusseresStrassenSegment> getAssListe(Strasse strasse) {
		List<AeusseresStrassenSegment> result = new ArrayList<AeusseresStrassenSegment>();
		for (StrassenSegment segment : getNetzSegmentListe()) {
			if (segment instanceof AeusseresStrassenSegment) {
				if ((strasse == null) || strasse.equals(segment.getStrasse())) {
					result.add((AeusseresStrassenSegment) segment);
				}
			}
		}
		return result;
	}

	/**
	 * fügt den Netz ein Stauobjekt mit dem übergeben Systemobjekt hinzu. Das
	 * Objekt wird in die Menge der Staus des VerkehrsmodellNetz eingetragen.
	 * 
	 * @param obj
	 *            das neue Stauobjekt
	 */
	public void stauHinzufuegen(SystemObject obj) {
		ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);
		if (!set.getElements().contains(obj)) {
			try {
				set.add(obj);
			} catch (ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
			}
		}

	}

	/**
	 * entfernt ein Stauobjekt mit dem übergeben Systemobjekt vom Netz. Das
	 * Objekt wird in die Menge der Staus des VerkehrsmodellNetz ausgetragen.
	 * 
	 * @param obj
	 *            das zu entfernende Stauobjekt
	 */
	public void stauEntfernen(SystemObject obj) {
		ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);
		if (set.getElements().contains(obj)) {
			try {
				set.remove(obj);
			} catch (ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}
}
