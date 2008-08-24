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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;

/**
 * Fasst die Urlasserinformationen eines Datensatzes zusammen.
 * 
 * @author BitCtrl Systems GmbH, Falko
 * @version $Id$
 */
public class Urlasser implements Cloneable {

	/** Der Benutzer, der den Datensatz verschickt. */
	private Benutzer benutzer;

	/** Die Ursache für das Verschicken dieses Datensatzes. */
	private String ursache = "";

	/** Der Veranlasser. */
	private String veranlasser = "";

	/**
	 * Gibt den Benutzer zurück, der den Datensatz schickt.
	 * 
	 * @return der Benutzer.
	 */
	public Benutzer getBenutzer() {
		return benutzer;
	}

	/**
	 * Legt den Benutzer fest, der den Datensatz schickt.
	 * 
	 * @param benutzer
	 *            der Benutzer.
	 */
	public void setBenutzer(final Benutzer benutzer) {
		this.benutzer = benutzer;
	}

	/**
	 * Gibt die Ursache für das Verschicken des Datensatzes zurück.
	 * 
	 * @return die Ursache.
	 */
	public String getUrsache() {
		return ursache;
	}

	/**
	 * Legt die Ursache für das Verschicken des Datensatzes fest.
	 * 
	 * @param ursache
	 *            die Ursache.
	 */
	public void setUrsache(final String ursache) {
		this.ursache = ursache;
	}

	/**
	 * Gibt den Veranlasser zurück.
	 * 
	 * @return der Veranlasser.
	 */
	public String getVeranlasser() {
		return veranlasser;
	}

	/**
	 * Legt den Veranlasser fest.
	 * 
	 * @param veranlasser
	 *            der Veranlasser.
	 */
	public void setVeranlasser(final String veranlasser) {
		this.veranlasser = veranlasser;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Urlasser) {
			final Urlasser o = (Urlasser) obj;
			boolean result;

			if (benutzer == null || o.benutzer == null) {
				result = benutzer == o.benutzer;
			} else if (benutzer != null) {
				result = benutzer.equals(o);
			} else {
				result = o.benutzer.equals(benutzer);
			}

			result &= ursache.equals(o.ursache);
			result &= veranlasser.equals(o.veranlasser);

			return result;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Urlasser clone() {
		final Urlasser klon = new Urlasser();

		klon.benutzer = benutzer;
		klon.ursache = ursache;
		klon.veranlasser = veranlasser;

		return klon;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s;

		s = getClass().getName() + "[";
		s += "benutzer=" + benutzer;
		s += ", ursache=" + ursache;
		s += ", veranlasser=" + veranlasser;
		s += "]";

		return s;
	}

}
