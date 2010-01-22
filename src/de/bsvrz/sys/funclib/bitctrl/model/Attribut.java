/*
 * Segment BW-Übergangsvisualisierung und -bedienung, SWE BW-Übergangsvisualisierung und -bedienung
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

package de.bsvrz.sys.funclib.bitctrl.model;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.Attribute;
import de.bsvrz.dav.daf.main.config.AttributeListDefinition;

/**
 * Kapselt ein Attribut und erweitert es um nützliche Funktionen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: Attribut.java 14414 2008-12-02 13:27:00Z Schumann $
 */
public class Attribut {

	private final Attribut elter;
	private final Attribute attribut;
	private final String pfad;
	private int index = -1;
	private List<Attribut> kinder;

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param elter
	 *            das übergeordnete Attribut.
	 * @param attribut
	 *            das gekapselte Attribut.
	 */
	public Attribut(final Attribut elter, final Attribute attribut) {
		String txt;

		this.elter = elter;
		this.attribut = attribut;
		if (isFeld()) {
			index = 1;
		}

		if (elter == null) {
			txt = getName();
		} else {
			txt = elter.getPfad() + "." + attribut.getNameOrPidOrId();
		}
		pfad = txt;
	}

	/**
	 * Gibt den Namen des Attributs zurück.
	 * 
	 * @return der Attributname.
	 */
	public String getName() {
		return attribut.getNameOrPidOrId();
	}

	/**
	 * Gibt das gekapselte Datenverteilerattribut zurück.
	 * 
	 * @return das gekapselte Datenverteilerattribut.
	 */
	public Attribute getAttribut() {
		return attribut;
	}

	/**
	 * Gibt das übergeordnete Attribut zurück.
	 * 
	 * @return das übergeordnete Attribut.
	 */
	public Attribut getElter() {
		return elter;
	}

	/**
	 * Gibt die Liste der Subattribute zurück.
	 * 
	 * @return die Subattribute.
	 */
	public List<Attribut> getKinder() {
		if (kinder == null) {
			kinder = new ArrayList<Attribut>();
			for (final Attribute att : ((AttributeListDefinition) attribut
					.getAttributeType()).getAttributes()) {
				kinder.add(new Attribut(this, att));
			}
		}

		return kinder;
	}

	/**
	 * Gibt den Pfad des Attributs zurück.
	 * 
	 * @return der Attributpfad.
	 */
	public String getPfad() {
		return index < 0 ? pfad : pfad + "." + index;
	}

	/**
	 * Gibt den Pfad ohne eventuellen Index zurück. Nützlich, um bei
	 * Feldelementen den Pfad des Felds zu bestimmen.
	 * 
	 * @return der Pfad ohne Feldindex.
	 */
	public String getPfadOhneIndex() {
		return pfad;
	}

	/**
	 * Testet, ob das Attribut eine Attributliste darstellt.
	 * 
	 * @return {@code true} wenn das Attribut eine Attributliste ist.
	 */
	public boolean isAttributliste() {
		return attribut.getAttributeType() instanceof AttributeListDefinition;
	}

	/**
	 * Testet, ob das Attribut ein Feld ist.
	 * 
	 * @return {@code true}, wenn das Attribut ein Feld ist.
	 */
	public boolean isFeld() {
		return attribut.isArray();
	}

	/**
	 * Testet ob dies ein einfaches Attribut ist, also weder Attributliste noch
	 * Feld.
	 * 
	 * @return {@code true}, wenn dies ein einfaches Attribut ist.
	 */
	public boolean isAttribut() {
		return !isFeld() && !isAttributliste();
	}

	/**
	 * Gibt den Feldindex des Attributs zurück.
	 * 
	 * @return der Feldindex oder {@code -1}, wenn das Attribut kein Feld ist.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Legt den Feldindex des Attributs fest.
	 * 
	 * @param index
	 *            der Feldindex oder {@code -1}, wenn das Attribut kein Feld
	 *            ist.
	 */
	public void setIndex(final int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return index < 0 ? getName() : getName() + "." + index;
	}

	/**
	 * Zwei Attribute sind gleich, wenn sie den gleichen Pfad haben, ohne
	 * Berücksichtung von Feldindizes.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Attribut) {
			final Attribut anderes = (Attribut) obj;
			final String pfad1 = pfad.replaceAll("\\\\.?", "");
			final String pfad2 = anderes.pfad.replaceAll("\\\\.?", "");
	
			return pfad1.equals(pfad2);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return attribut.hashCode();
	}

	@Override
	public Attribut clone() {
		final Attribut klon = new Attribut(elter, attribut);
	
		klon.index = index;
		return klon;
	}

}
