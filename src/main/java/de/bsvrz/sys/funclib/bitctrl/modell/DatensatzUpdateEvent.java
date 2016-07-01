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

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.EventObject;

import de.bsvrz.dav.daf.main.config.Aspect;

/**
 * Das Ereignis tritt ein, wenn ein Datensatz ge&auml;ndert wurde.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class DatensatzUpdateEvent extends EventObject {

	/** Die Versions-ID der Serialialisierung. */
	private static final long serialVersionUID = 1L;

	/** Der betroffene Aspekt. */
	private final Aspect aspekt;

	/** Das Datum des Datensatzes zum Zeitpunkt des Events. */
	private final Datum datum;

	/**
	 * Der Konstruktor des Ereignisses.
	 *
	 * @param datensatz
	 *            der Datensatz, der sich ge&auml;ndert hat.
	 * @param aspekt
	 *            der betroffene Aspekt.
	 * @param datum
	 *            die Daten des Datensatzes zum Zeitpunkt des Events.
	 */
	public DatensatzUpdateEvent(final Datensatz<?> datensatz,
			final Aspect aspekt, final Datum datum) {
		super(datensatz);
		this.aspekt = aspekt;
		this.datum = datum;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code aspekt} wieder.
	 *
	 * @return {@code aspekt}.
	 */
	public Aspect getAspekt() {
		return aspekt;
	}

	/**
	 * Gibt den ge&auml;nderten Datensatz zur&uuml;ck.
	 *
	 * @return der ge&auml;nderte Datensatz.
	 */
	public Datensatz<?> getDatensatz() {
		return (Datensatz<?>) source;
	}

	/**
	 * Gibt das zum Zeitpunktz des Events g&uuml;ltige Datum des ge&auml;nderten
	 * Datensatzes zur&uuml;ck.
	 *
	 * @return das Datum.
	 */
	public Datum getDatum() {
		return datum;
	}

	/**
	 * Gibt das Systemobjekt zur&uuml;ck, dessen Datensatz sich ge&auml;ndert
	 * hat.
	 *
	 * @return das Systemobjekt.
	 */
	public SystemObjekt getObjekt() {
		return getDatensatz().getObjekt();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[source=" + source + ", datum="
				+ datum + "]";
	}

}
