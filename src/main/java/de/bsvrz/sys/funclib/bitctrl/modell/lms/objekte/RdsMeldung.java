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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.LmsModellTypen;

/**
 * Repr&auml;sentiert eine RDS-Meldung.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class RdsMeldung extends AbstractSystemObjekt {

	/**
	 * die Menge der Landesmeldestellen in denen die meldung referenziert wird.
	 */
	private final Set<LandesMeldeStelle> lmsListe = new HashSet<LandesMeldeStelle>();

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz einer RdsMeldung auf der Basis des
	 * übergebenen Systemobjekts.
	 *
	 * @param obj
	 *            das Systemobjekt, das die Baustelle definiert
	 */
	public RdsMeldung(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine RdsMeldung.");
		}
	}

	/**
	 * fügt der Meldung eine Referenz auf eine Landesmeldestelle hinzu.
	 *
	 * @param lms
	 *            die Landesmeldestelle, die als Referenz hinzugefügt werden
	 *            soll
	 */
	public void addLmsReferenz(final LandesMeldeStelle lms) {
		lmsListe.add(lms);
	}

	/**
	 * liefert die Menge der Landesmeldestellen in denen die meldung refernziert
	 * wird.
	 *
	 * @return die Menge der Netze
	 */
	public Set<LandesMeldeStelle> getLms() {
		return lmsListe;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return LmsModellTypen.RDSMELDUNG;
	}

	/**
	 * entfernt eine Landesmeldestellenreferenz von der Meldung.
	 *
	 * @param lms
	 *            die Landesmeldestelle, deren Referenz entfernt werden soll
	 */
	public void removeLmsReferenz(final LandesMeldeStelle lms) {
		lmsListe.remove(lms);
	}
}
