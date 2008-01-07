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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis;

/**
 * Kapselt die Attributgruppe {@code atg.ereignisKalenderAntwort}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdEreignisKalenderAntwort extends
		AbstractOnlineDatensatz<OdEreignisKalenderAntwort.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.anfrage}. */
		Antwort("asp.antwort");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 * 
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		private Aspekte(String pid) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Aspekt#getAspekt()
		 */
		public Aspect getAspekt() {
			return aspekt;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Aspekt#getName()
		 */
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Repr&auml;sentation der Daten des Ereignistypparameters.
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Beschreibt einen Zustandswechsel in der Kalenderantwort.
		 * 
		 * @author BitCtrl Systems GmbH, Falko Schumann
		 * @version $Id$
		 */
		public static class Zustandswechsel {

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
			public Zustandswechsel(long zeitstempel, Ereignis ereignis,
					boolean zeitlichGueltig, boolean verkehrlichGueltig) {
				this.zeitstempel = zeitstempel;
				this.ereignis = ereignis;
				this.zeitlichGueltig = zeitlichGueltig;
				this.verkehrlichGueltig = verkehrlichGueltig;
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
			 * Gibt den Zeitstempel der Zustands&auml;nderung zur&uuml;ck.
			 * 
			 * @return ein Zeitstempel.
			 */
			public long getZeitstempel() {
				return zeitstempel;
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
			 * Flag f&uuml;r die neue zeitliche G&uuml;ltigkeit.
			 * 
			 * @return der Zustand der neuen zeitlichen G&uuml;ltigkeit.
			 */
			public boolean isZeitlichGueltig() {
				return zeitlichGueltig;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public String toString() {
				return getClass().getName()
						+ "[zeitstempel="
						+ DateFormat.getDateTimeInstance().format(
								new Date(zeitstempel)) + ", ereignis="
						+ ereignis + ", zeitlichGueltig=" + zeitlichGueltig
						+ ", verkehrlichGueltig=" + verkehrlichGueltig + "]";
			}

		}

		/** Die Liste der Ereignisse und deren Zustandswechsel. */
		private final List<Zustandswechsel> zustandswechsel = new ArrayList<Zustandswechsel>();

		/** Das Absenderzeichen des Anfragers. */
		private String absenderZeichen;

		/** Signalisiert das Event eine &Auml;nderung? */
		private boolean aenderung;

		/** Flag ob der Datensatz g&uuml;ltige Daten enth&auml;lt. */
		private boolean valid;

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.valid = valid;
			klon.absenderZeichen = absenderZeichen;
			klon.aenderung = aenderung;
			klon.zustandswechsel.addAll(zustandswechsel);

			return klon;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code absenderZeichen} wieder.
		 * 
		 * @return {@code absenderZeichen}.
		 */
		public String getAbsenderZeichen() {
			return absenderZeichen;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code zustandswechsel} wieder.
		 * 
		 * @return {@code zustandswechsel}.
		 */
		public List<Zustandswechsel> getZustandswechsel() {
			return zustandswechsel;
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
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#isValid()
		 */
		public boolean isValid() {
			return valid;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code absenderZeichen} fest.
		 * 
		 * @param absenderZeichen
		 *            der neue Wert von {@code absenderZeichen}.
		 */
		public void setAbsenderZeichen(String absenderZeichen) {
			this.absenderZeichen = absenderZeichen;
		}

		/**
		 * Legt fest, ob das Event eine Aktualisierung darstellt.
		 * 
		 * @param aenderung
		 *            {@code true}, wenn es eine Aktualissierung ist.
		 */
		public void setAenderung(boolean aenderung) {
			this.aenderung = aenderung;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", valid=" + valid;
			s += ", absenderZeichen=" + absenderZeichen;
			s += ", aenderung=" + aenderung;
			s += ", zustandswechsel=" + zustandswechsel;

			return s + "]";
		}

		/**
		 * Setzt das Flag f&uuml;r g&uuml;ltige Daten.
		 * 
		 * @param valid
		 *            {@code true}, wenn das Datum g&uuml;ltige Daten
		 *            enth&auml;lt.
		 */
		protected void setValid(boolean valid) {
			this.valid = valid;
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_EREIGNIS_KALENDER_ANTWORT = "atg.ereignisKalenderAntwort";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param objekt
	 *            die Applikation f&uuml;r die die Antwort bestimmt ist.
	 */
	public OdEreignisKalenderAntwort(SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_EREIGNIS_KALENDER_ANTWORT);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#erzeugeDatum()
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getAspekte()
	 */
	@Override
	public Collection<Aspect> getAspekte() {
		Set<Aspect> aspekte = new HashSet<Aspect>();
		for (Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getAttributGruppe()
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.ResultData)
	 */
	public void setDaten(ResultData result) {
		check(result);

		Daten datum = new Daten();
		if (result.hasData()) {
			Data daten;
			Array feld;

			daten = result.getData();

			datum.setAbsenderZeichen(daten.getTextValue("absenderZeichen")
					.getText());
			datum
					.setAenderung(daten.getUnscaledValue("‰nderung").intValue() == 1);

			feld = daten.getArray("Ereignis");
			for (int i = 0; i < feld.getLength(); i++) {
				long zeitstempel;
				SystemObject ereignisSO;
				Ereignis ereignis;
				boolean zeitlichGueltig;
				boolean verkehrlichGueltig;

				zeitstempel = feld.getItem(i).getTimeValue("Zeitpunkt")
						.getMillis();
				ereignisSO = feld.getItem(i).getReferenceValue(
						"EreignisReferenz").getSystemObject();
				ereignis = (Ereignis) ObjektFactory.getInstanz()
						.getModellobjekt(ereignisSO);
				zeitlichGueltig = feld.getItem(i).getUnscaledValue(
						"zeitlichG¸ltig").intValue() != 0;
				verkehrlichGueltig = feld.getItem(i).getUnscaledValue(
						"verkehrlichG¸ltig").intValue() != 0;
				datum.getZustandswechsel().add(
						new Daten.Zustandswechsel(zeitstempel, ereignis,
								zeitlichGueltig, verkehrlichGueltig));
			}

			datum.setValid(true);
		} else {
			datum.setValid(false);
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten datum) {
		Data daten;
		Array feld;
		int i;

		daten = erzeugeSendeCache();
		daten.getTextValue("absenderZeichen").setText(
				datum.getAbsenderZeichen());
		if (datum.isAenderung()) {
			daten.getUnscaledValue("‰nderung").set(1);
		} else {
			daten.getUnscaledValue("‰nderung").set(0);
		}

		feld = daten.getArray("Ereignis");
		feld.setLength(datum.getZustandswechsel().size());
		i = 0;
		for (Daten.Zustandswechsel z : datum.getZustandswechsel()) {
			feld.getItem(i++).getTimeValue("Zeitpunkt").setMillis(
					z.getZeitstempel());
			feld.getItem(i++).getReferenceValue("").setSystemObject(
					z.getEreignis().getSystemObject());
			if (z.isZeitlichGueltig()) {
				feld.getItem(i++).getUnscaledValue("zeitlichG¸ltig").set(1);
			} else {
				feld.getItem(i++).getUnscaledValue("zeitlichG¸ltig").set(0);
			}
			if (z.isVerkehrlichGueltig()) {
				feld.getItem(i++).getUnscaledValue("verkehrlichG¸ltig").set(1);
			} else {
				feld.getItem(i++).getUnscaledValue("verkehrlichG¸ltig").set(0);
			}
		}

		return daten;
	}

}
