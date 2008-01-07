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

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.util.Collections;
import java.util.EventObject;
import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAntwort;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten.OdEreignisKalenderAntwort.Daten.Zustandswechsel;

/**
 * Das Event wird vom Ereigniskalender ausgel&ouml;st.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class KalenderEvent extends EventObject {

	/** Die Eigenschaft {@code serialVersionUID}. */
	private static final long serialVersionUID = 1L;

	/** Kapselt die Daten des Events. */
	private final OdEreignisKalenderAntwort.Daten datum;

	/**
	 * Konstruiert das Event.
	 * 
	 * @param source
	 *            die Quelle des Events.
	 * @param absenderZeichen
	 *            das Zeichen des Absenders.
	 * @param aenderung
	 *            ist die Antwort eine Aktualisierung?
	 * @param zustandswechsel
	 *            die Liste der Zustandswechsel.
	 */
	public KalenderEvent(
			Object source,
			String absenderZeichen,
			boolean aenderung,
			List<OdEreignisKalenderAntwort.Daten.Zustandswechsel> zustandswechsel) {
		super(source);

		datum = new OdEreignisKalenderAntwort.Daten();
		datum.setAbsenderZeichen(absenderZeichen);
		datum.setAenderung(aenderung);
		datum.getZustandswechsel().addAll(zustandswechsel);
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code absenderZeichen} wieder.
	 * 
	 * @return {@code absenderZeichen}.
	 */
	public String getAbsenderZeichen() {
		return datum.getAbsenderZeichen();
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code zustandswechsel} wieder.
	 * 
	 * @return {@code zustandswechsel}.
	 */
	public List<Zustandswechsel> getZustandswechsel() {
		return Collections.unmodifiableList(datum.getZustandswechsel());
	}

	/**
	 * Flag, ob es sich um die erste Antwort handelt oder ob es sich um eine
	 * Aktualisierung handelt.
	 * 
	 * @return {@code true}, wenn das Event eine Aktualisierung darstellt.
	 */
	public boolean isAenderung() {
		return datum.isAenderung();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = getClass().getName() + "[";

		s = "absenderZeichen=" + datum.getAbsenderZeichen();
		s += ", zustandswechsel=" + datum.getZustandswechsel() + "]";

		return s + "]";
	}

}
