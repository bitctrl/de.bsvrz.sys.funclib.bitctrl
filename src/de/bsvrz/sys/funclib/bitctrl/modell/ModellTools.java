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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Fasst allgemeine Funktionen des Modells zusammen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public final class ModellTools {

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
	public static List<? extends SystemObjekt> sortiere(
			List<? extends SystemObjekt> objekte) {
		Collections.sort(objekte, new Comparator<SystemObjekt>() {

			public int compare(SystemObjekt so1, SystemObjekt so2) {
				Collator de = Collator.getInstance(Locale.GERMANY);
				return de.compare(so1.toString(), so2.toString());
			}

		});
		return objekte;
	}

	/**
	 * Konstruktor verstecken.
	 */
	private ModellTools() {
		// nix
	}

}
