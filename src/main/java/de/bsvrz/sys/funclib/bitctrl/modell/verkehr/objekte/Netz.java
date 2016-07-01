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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;

/**
 * Repr&auml;sentiert ein Stra&szlig;ensegment.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class Netz extends StoerfallIndikator implements NetzBestandTeil {

	/** Die sortierte Liste der enthaltenen Netzbestandteile. */
	private final List<NetzBestandTeil> bestandteile = new ArrayList<>();

	/**
	 * die Liste der Straßen, die das Netz bilden.
	 */
	private HashSet<Strasse> strassenListe;

	/**
	 * Konstruiert aus einem Systemobjekt ein Netz.
	 *
	 * @param obj
	 *            Ein Systemobjekt, welches ein Netz darstellt
	 * @throws IllegalArgumentException
	 */
	public Netz(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein gültiges Netz.");
		}

		// Netzbestandzeile ermitteln
		final ObjectSet menge = ((ConfigurationObject) getSystemObject())
				.getObjectSet("NetzBestandTeile");
		for (final SystemObject mengenObj : menge.getElements()) {
			bestandteile.add((NetzBestandTeil) ObjektFactory.getInstanz()
					.getModellobjekt(mengenObj));
		}
	}

	/**
	 * liefert die Netzbestandteile, die das Netz gemäß Konfiguration bilden.
	 *
	 * @return bestandteile die Liste der konfigurierten Bestandteile
	 */
	public List<NetzBestandTeil> getBestandteile() {
		return new ArrayList<>(bestandteile);
	}

	@Override
	public Collection<? extends StrassenSegment> getNetzSegmentListe() {
		final Set<StrassenSegment> liste = new HashSet<>();
		for (final NetzBestandTeil bestandTeil : getBestandteile()) {
			liste.addAll(bestandTeil.getNetzSegmentListe());
		}
		return liste;
	}

	/**
	 * liefert die Straßen, die an der Bildung des Netzes beteiligt sind.
	 *
	 * @return die Liste der Straßen
	 */
	public Collection<Strasse> getStrassen() {
		if (strassenListe == null) {
			strassenListe = new HashSet<>();
			for (final NetzBestandTeil bestandTeil : bestandteile) {
				if (bestandTeil instanceof AeusseresStrassenSegment) {
					final Strasse str = ((AeusseresStrassenSegment) bestandTeil)
							.getStrasse();
					if (str != null) {
						strassenListe.add(str);
					}
				} else if (bestandTeil instanceof Netz) {
					strassenListe.addAll(((Netz) bestandTeil).getStrassen());
				}
			}
		}
		return strassenListe;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.NETZ;
	}

}
