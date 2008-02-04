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

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.util.HashSet;
import java.util.Set;

import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.zustaende.EreignisTypenOption;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.NetzBestandTeil;
import de.bsvrz.sys.funclib.bitctrl.util.Intervall;

/**
 * Repr&auml;sentiert eine Anfrage an den Ereigniskalender.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class KalenderAnfrage {

	/**
	 * Menge von Ereignistypen, die entsprechend der gesetzten Auswahloption
	 * ber&uuml;cksichtigt werden.
	 * 
	 * @see #ereignisTypenOption
	 */
	private final Set<EreignisTyp> ereignisTypen = new HashSet<EreignisTyp>();

	/** Menge der Netzbestandteile, dessen Ereignisse angefragt werden. */
	private final Set<NetzBestandTeil> raeumlicheGueltigkeit = new HashSet<NetzBestandTeil>();

	/** Das Zeitintervall, indem Ereignisse angefragt werden. */
	private Intervall intervall;

	/**
	 * Auswahloption f&uuml;r die Liste der Ereignistypen. Standard ist
	 * {@link EreignisTypenOption#ALLE}.
	 * 
	 * @see #ereignisTypen
	 */
	private EreignisTypenOption ereignisTypenOption = EreignisTypenOption.ALLE;

	/**
	 * Gibt den Wert der Eigenschaft {@code ereignisTypen} wieder.
	 * 
	 * @return {@code ereignisTypen}.
	 */
	public Set<EreignisTyp> getEreignisTypen() {
		return ereignisTypen;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code ereignisTypenOption} wieder.
	 * 
	 * @return {@code ereignisTypenOption}.
	 */
	public EreignisTypenOption getEreignisTypenOption() {
		return ereignisTypenOption;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code intervall} wieder.
	 * 
	 * @return {@code intervall}.
	 */
	public Intervall getIntervall() {
		return intervall;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code raeumlicheGueltigkeit} wieder.
	 * 
	 * @return {@code raeumlicheGueltigkeit}.
	 */
	public Set<NetzBestandTeil> getRaeumlicheGueltigkeit() {
		return raeumlicheGueltigkeit;
	}

	/**
	 * Legt den Wert der Eigenschaft {@code ereignisTypenOption} fest.
	 * 
	 * @param ereignisTypenOption
	 *            der neue Wert von {@code ereignisTypenOption}.
	 */
	public void setEreignisTypenOption(EreignisTypenOption ereignisTypenOption) {
		this.ereignisTypenOption = ereignisTypenOption;
	}

	/**
	 * Legt den Wert der Eigenschaft {@code intervall} fest.
	 * 
	 * @param intervall
	 *            der neue Wert von {@code intervall}.
	 */
	public void setIntervall(Intervall intervall) {
		this.intervall = intervall;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = getClass().getSimpleName() + "[";

		s += "intervall=" + intervall;
		s += ", ereignisTypenOption=" + ereignisTypenOption;
		s += ", ereignisTypen=" + ereignisTypen;
		s += ", raeumlicheGueltigkeit=" + raeumlicheGueltigkeit;

		return s + "]";
	}

}
