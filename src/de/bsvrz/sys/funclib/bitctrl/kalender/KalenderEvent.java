/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.1 Ganglinienprognose
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.ClientApplication;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.Ereignis;

/**
 * Das Event wird vom Ereigniskalender ausgel&ouml;st.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
@SuppressWarnings("serial")
public class KalenderEvent extends EventObject {

	/**
	 * Struktur f&uuml;r die Zustandswechsel der Ereignisse.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	public static class Zustand {

		/** Zeitpunkt des Zustandswechsel. */
		private final long zeitstempel;

		/** Das Ereignis dessen Zustand sich &auml;ndert. */
		private final Ereignis ereignis;

		/** Ist das Ereignis nun zeitlich g&uuml;ltig. */
		private final boolean zeitlichGueltig;

		/** Ist das Ereignis nun verkehrlich g&uuml;ltig. */
		private final boolean verkehrlichGueltig;

		/**
		 * Konstruiert den Zustand.
		 * <p>
		 * <em>Hinweis:</em> Der Konstruktior ist nicht Teil der
		 * &ouml;ffentlichen API und sollte nicht verwendet werden.
		 * 
		 * @param zeitstempel
		 *            der Zeitpunkt des Zustandwechsels.
		 * @param ereignis
		 *            das sich &anuml;ndernde Ereignis.
		 * @param zeitlichGueltig
		 *            die neue zeitliche G&uuml;ltigkeit.
		 * @param verkehrlichGueltig
		 *            die neue verkehrliche G&uuml;ltigkeit.
		 */
		public Zustand(long zeitstempel, Ereignis ereignis,
				boolean zeitlichGueltig, boolean verkehrlichGueltig) {
			this.zeitstempel = zeitstempel;
			this.ereignis = ereignis;
			this.zeitlichGueltig = zeitlichGueltig;
			this.verkehrlichGueltig = verkehrlichGueltig;
		}

		/**
		 * Gibt den Zeitstempel der Zustands&auml;nderung zur&uuml;ck.
		 * 
		 * @return ein Zeitstempel.
		 */
		public long getZeitstempel() {
			return zeitstempel;
		}

		/**
		 * Gibt das Ereignis zur&uuml;ck, dessen Zustand sich &auml;ndert.
		 * 
		 * @return ein Ereignis.
		 */
		public Ereignis getEreignis() {
			return ereignis;
		}

		/**
		 * Flag f&uuml;r die neue zeitliche G&uuml;ltigkeit.
		 * 
		 * @return der Zustand der neuen zeitlichen G&uuml;ltigkeit.
		 */
		public boolean isZeitlichGueltig() {
			return zeitlichGueltig;
		}

		/**
		 * Flag f&uuml;r die neue verkehrliche G&uuml;ltigkeit.
		 * 
		 * @return der Zustand der neuen verkehrlichen G&uuml;ltigkeit.
		 */
		public boolean isVerkehrlichGueltig() {
			return verkehrlichGueltig;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return getClass().getName()
					+ "[zeitstempel="
					+ DateFormat.getDateTimeInstance().format(
							new Date(zeitstempel)) + ", ereignis=" + ereignis
					+ ", zeitlichGueltig=" + zeitlichGueltig
					+ ", verkehrlichGueltig=" + verkehrlichGueltig + "]";
		}

	}

	/** Die anfragende Applikation. */
	private final ClientApplication anfrager;

	/** Das Absenderzeichen des Anfragers. */
	private String absenderZeichen;

	/** Signalisiert das Event eine &Auml;nderung? */
	private boolean aenderung;

	/** Die Liste der Ereignisse und deren Zustandswechsel. */
	private List<Zustand> zustandswechsel;

	/**
	 * Konstruiert das Event.
	 * 
	 * @param source
	 *            die Quelle des Events.
	 * @param anfrager
	 *            die anfragende Applikation.
	 */
	public KalenderEvent(Object source, ClientApplication anfrager) {
		super(source);
		this.anfrager = anfrager;
		zustandswechsel = new ArrayList<Zustand>();
	}

	/**
	 * Baut aus den Informationen der Anfrage ein Datum.
	 * <p>
	 * Hinweis: Das Ergebnis wird auch im Parameter abgelegt!
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param daten
	 *            ein Datum, welches eine (leere) Anfrage darstellt.
	 * @return das ausgef&uuml;llte Datum.
	 */
	public Data getDaten(Data daten) {
		Array feld;
		int i;

		daten.getTextValue("absenderZeichen").setText(absenderZeichen);
		if (aenderung) {
			daten.getUnscaledValue("änderung").set(1);
		} else {
			daten.getUnscaledValue("änderung").set(0);
		}

		feld = daten.getArray("Ereignis");
		feld.setLength(zustandswechsel.size());
		i = 0;
		for (Zustand z : zustandswechsel) {
			feld.getItem(i++).getTimeValue("Zeitpunkt").setMillis(
					z.getZeitstempel());
			feld.getItem(i++).getReferenceValue("").setSystemObject(
					z.getEreignis().getSystemObject());
			if (z.isZeitlichGueltig()) {
				feld.getItem(i++).getUnscaledValue("zeitlichGültig").set(1);
			} else {
				feld.getItem(i++).getUnscaledValue("zeitlichGültig").set(0);
			}
			if (z.isVerkehrlichGueltig()) {
				feld.getItem(i++).getUnscaledValue("verkehrlichGültig").set(1);
			} else {
				feld.getItem(i++).getUnscaledValue("verkehrlichGültig").set(0);
			}
		}

		return daten;
	}

	/**
	 * &Uuml;bernimmt die Informationen aus dem Datum als inneren Zustand.
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param daten
	 *            ein Datum, welches eine Anfrage darstellt.
	 */
	public void setDaten(Data daten) {
		Array feld;

		absenderZeichen = daten.getTextValue("absenderZeichen").getText();
		aenderung = daten.getUnscaledValue("änderung").intValue() != 0;

		zustandswechsel.clear();
		feld = daten.getArray("Ereignis");
		for (int i = 0; i < feld.getLength(); i++) {
			long zeitstempel;
			SystemObject ereignisSO;
			Ereignis ereignis;
			boolean zeitlichGueltig;
			boolean verkehrlichGueltig;

			zeitstempel = feld.getItem(i).getTimeValue("Zeitpunkt").getMillis();
			ereignisSO = feld.getItem(i).getReferenceValue("EreignisReferenz")
					.getSystemObject();
			ereignis = (Ereignis) ObjektFactory.getModellobjekt(ereignisSO);
			zeitlichGueltig = feld.getItem(i)
					.getUnscaledValue("zeitlichGültig").intValue() != 0;
			verkehrlichGueltig = feld.getItem(i).getUnscaledValue(
					"verkehrlichGültig").intValue() != 0;
			zustandswechsel.add(new Zustand(zeitstempel, ereignis,
					zeitlichGueltig, verkehrlichGueltig));
		}
	}

	/**
	 * Gibt das Absenderzeichen zur&uuml;ck.
	 * 
	 * @return ein beliebiger String.
	 */
	public String getAbsenderZeichen() {
		return absenderZeichen;
	}

	/**
	 * Legt das Absenderzeichen fest.
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param absenderZeichen
	 *            ein beliebiger String.
	 */
	public void setAbsenderZeichen(String absenderZeichen) {
		this.absenderZeichen = absenderZeichen;
	}

	/**
	 * Flag, ob es sich um die erste Antwort handelt oder ob es sich um eine
	 * Aktualisierung handelt.
	 * 
	 * @return {@code true}, wenn das Event eine Aktualisierung darstellt.
	 */
	public boolean isAenderung() {
		return aenderung;
	}

	/**
	 * Legt fest, ob das Event eine Aktualisierung darstellt.
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param aenderung
	 *            {@code true}, wenn es eine Aktualissierung ist.
	 */
	public void setAenderung(boolean aenderung) {
		this.aenderung = aenderung;
	}

	/**
	 * Gibt die anfragende Applikation zur&uuml;ck.
	 * 
	 * @return die Applikation.
	 */
	public ClientApplication getAnfrager() {
		return anfrager;
	}

	/**
	 * Gibt die Liste der Zustandswechsel zur&uuml;ck.
	 * 
	 * @return die nach Zeitstempel sortierte Liste der Zustandswechsel.
	 */
	public List<Zustand> getZustandswechsel() {
		return new ArrayList<Zustand>(zustandswechsel);
	}

	/**
	 * F&uuml;gt der Liste der Zustandswechsel einen neuen hinzu.
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nicht Teil der öffentlichen API und
	 * sollte nicht außerhalb der Ganglinie-API verwendet werden.
	 * 
	 * @param zustand
	 *            ein Zustandswechsel.
	 */
	public void add(Zustand zustand) {
		if (zustand == null) {
			throw new IllegalArgumentException();
		}
		zustandswechsel.add(zustand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[anfrager=" + anfrager
				+ ", absenderZeichen=" + absenderZeichen + ", aenderung="
				+ aenderung + ", zustandswechsel=" + zustandswechsel + "]";
	}

}
