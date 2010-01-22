/*
 * Segment BW-‹bergangsvisualisierung und -bedienung, SWE BW-‹bergangsvisualisierung und -bedienung
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

package de.bsvrz.sys.funclib.bitctrl.model;

import java.util.ArrayList;

import de.bsvrz.dav.daf.main.config.AttributeGroup;

/**
 * Repr‰sentiert einen Attributfilter, der aus einer Attributgruppe eine
 * Teilmenge von Attribute herausgreift.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: AttributFilter.java 5135 2007-12-06 13:48:54Z Schumann $
 */
public class AttributFilter extends ArrayList<Attribut> {

	private static final long serialVersionUID = 1L;

	private Attributgruppe atg;
	private String name;
	private String info;

	/**
	 * Erh‰lt den Standardkonstruktor.
	 */
	public AttributFilter() {
		// tut nix
	}

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param atg
	 *            die zu kapselnde Attritrubgruppe.
	 */
	public AttributFilter(final AttributeGroup atg) {
		this.atg = new Attributgruppe(atg);
	}

	/**
	 * Zwei Attributfilter sind gleich, wenn sie den gleichen Namen haben und
	 * auf die selbe Attributgruppe angewandt werden.
	 */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof AttributFilter) {
			final AttributFilter a = (AttributFilter) o;

			return name.equals(a.name) && (atg == a.atg || atg.equals(a.atg));
		}
		return false;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code atg} wieder.
	 * 
	 * @return {@code atg}.
	 */
	public Attributgruppe getAtg() {
		return atg;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code info} wieder.
	 * 
	 * @return {@code info}.
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code name} wieder.
	 * 
	 * @return {@code name}.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Legt den Wert der Eigenschaft {@code atg} fest.
	 * 
	 * @param atg
	 *            der neue Wert von {@code atg}.
	 */
	public void setAtg(final Attributgruppe atg) {
		this.atg = atg;
	}

	/**
	 * Legt den Wert der Eigenschaft {@code info} fest.
	 * 
	 * @param info
	 *            der neue Wert von {@code info}.
	 */
	public void setInfo(final String info) {
		this.info = info;
	}

	/**
	 * Legt den Wert der Eigenschaft {@code name} fest.
	 * 
	 * @param name
	 *            der neue Wert von {@code name}.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.AbstractCollection#toString()
	 */
	@Override
	public String toString() {
		return name + " (" + atg + ")";
	}

}
