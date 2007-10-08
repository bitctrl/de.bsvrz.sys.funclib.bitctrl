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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

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

/**
 * Repr&auml;sentiert ein Stra&szlig;ensegment.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class Netz extends StoerfallIndikator implements NetzBestandTeil {

	/** Die sortierte Liste der enthaltenen Netzbestandteile. */
	private final List<NetzBestandTeil> bestandteile = new ArrayList<NetzBestandTeil>();

	/**
	 * die Liste der Straﬂen, die das Netz bilden.
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
					"Systemobjekt ist kein g¸ltiges Netz.");
		}

		// Netzbestandzeile ermitteln
		ObjectSet menge = ((ConfigurationObject) getSystemObject())
				.getObjectSet("NetzBestandTeile");
		for (SystemObject mengenObj : menge.getElements()) {
			bestandteile.add((NetzBestandTeil) ObjektFactory.getInstanz()
					.getModellobjekt(mengenObj));
		}
	}

	/**
	 * liefert die Netzbestandteile, die das Netz gem‰ﬂ Konfiguration bilden.
	 * 
	 * @return bestandteile die Liste der konfigurierten Bestandteile
	 */
	public List<NetzBestandTeil> getBestandteile() {
		return new ArrayList<NetzBestandTeil>(bestandteile);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.verkehr.NetzBestandTeil#getNetzSegmentListe()
	 */
	public Collection<? extends StrassenSegment> getNetzSegmentListe() {
		Set<StrassenSegment> liste = new HashSet<StrassenSegment>();
		for (NetzBestandTeil bestandTeil : getBestandteile()) {
			liste.addAll(bestandTeil.getNetzSegmentListe());
		}
		return liste;
	}

	/**
	 * liefert die Straﬂen, die an der Bildung des Netzes beteiligt sind.
	 * 
	 * @return die Liste der Straﬂen
	 */
	public Collection<Strasse> getStrassen() {
		if (strassenListe == null) {
			strassenListe = new HashSet<Strasse>();
			for (NetzBestandTeil bestandTeil : bestandteile) {
				if (bestandTeil instanceof AeusseresStrassenSegment) {
					Strasse str = ((AeusseresStrassenSegment) bestandTeil)
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

	/**
	 * {@inheritDoc}.
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.NETZ;
	}

}
