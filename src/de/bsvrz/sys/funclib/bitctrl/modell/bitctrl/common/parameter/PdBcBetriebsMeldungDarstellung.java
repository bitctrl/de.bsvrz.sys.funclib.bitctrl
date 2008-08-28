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

package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.objekte.BcBetriebsMeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.parameter.PdBcBetriebsMeldungDarstellung.Daten.Darstellung;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.zustaende.BetriebsMeldungSpalte;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsKlasse;

/**
 * Parameter, der die Darstellung von Betriebsmeldungen in einer graphischen
 * Oberfläche charakterisiert.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class PdBcBetriebsMeldungDarstellung extends
		AbstractParameterDatensatz<PdBcBetriebsMeldungDarstellung.Daten> {

	/**
	 * Repräsentation der Daten des Parameters.
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Beschreibt die Darstellung von Betriebsmeldungen.
		 */
		public static class Darstellung {

			private MeldungsKlasse klasse;
			private long vordergrundfarbe;
			private long hintergrundfarbe;
			private String schriftart;
			private boolean fettdruck;
			private boolean kursivdruck;

			/**
			 * @return the klasse
			 */
			public MeldungsKlasse getKlasse() {
				return klasse;
			}

			/**
			 * @param klasse
			 *            the klasse to set
			 */
			public void setKlasse(final MeldungsKlasse klasse) {
				this.klasse = klasse;
			}

			/**
			 * @return the vordergrundfarbe
			 */
			public long getVordergrundfarbe() {
				return vordergrundfarbe;
			}

			/**
			 * @param vordergrundfarbe
			 *            the vordergrundfarbe to set
			 */
			public void setVordergrundfarbe(final long vordergrundfarbe) {
				this.vordergrundfarbe = vordergrundfarbe;
			}

			/**
			 * @return the hintergrundfarbe
			 */
			public long getHintergrundfarbe() {
				return hintergrundfarbe;
			}

			/**
			 * @param hintergrundfarbe
			 *            the hintergrundfarbe to set
			 */
			public void setHintergrundfarbe(final long hintergrundfarbe) {
				this.hintergrundfarbe = hintergrundfarbe;
			}

			/**
			 * @return the schriftart
			 */
			public String getSchriftart() {
				return schriftart;
			}

			/**
			 * @param schriftart
			 *            the schriftart to set
			 */
			public void setSchriftart(final String schriftart) {
				this.schriftart = schriftart;
			}

			/**
			 * @return the fettdruck
			 */
			public boolean isFettdruck() {
				return fettdruck;
			}

			/**
			 * @param fettdruck
			 *            the fettdruck to set
			 */
			public void setFettdruck(final boolean fettdruck) {
				this.fettdruck = fettdruck;
			}

			/**
			 * @return the kursivdruck
			 */
			public boolean isKursivdruck() {
				return kursivdruck;
			}

			/**
			 * @param kursivdruck
			 *            the kursivdruck to set
			 */
			public void setKursivdruck(final boolean kursivdruck) {
				this.kursivdruck = kursivdruck;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public String toString() {
				String s;

				s = getClass().getName() + "[";
				s += "klasse=" + klasse;
				s += ", vordergrundfarbe=" + vordergrundfarbe;
				s += ", hintergrundfarbe=" + hintergrundfarbe;
				s += ", schriftart=" + schriftart;
				s += ", fettdruck=" + fettdruck;
				s += ", kursivdruck=" + kursivdruck;
				s += "]";

				return s;
			}

		}

		/** Der aktuelle Status des Datensatzes. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Die Zeit in die Vergangenheit, für die Meldungen initial gecacht
		 * werden sollen.
		 */
		private long maxHistory;

		/** Die maximale Anzahl gecachter Meldungen. */
		private int maxAnzahl;

		/** Die Liste der anzuzeigenden Spalten einer Meldung. */
		private List<BetriebsMeldungSpalte> anzuzeigendeSpalten = new ArrayList<BetriebsMeldungSpalte>(
				Arrays.asList(BetriebsMeldungSpalte.values()));

		/** Die Liste der Darstellungsbeschreibungen. */
		private List<Darstellung> darstellung = new ArrayList<Darstellung>();

		/**
		 * Gibt die Zeit in die Vergangenheit zurück, für die initial
		 * archivierte Meldungen gelesen und gecacht werden.
		 * 
		 * @return das maximale Alter von archivierten Betriebsmeldungen.
		 */
		public long getMaxHistory() {
			return maxHistory;
		}

		/**
		 * Legt die Zeit in die Vergangenheit fest, für die initial archivierte
		 * Meldungen gelesen und gecacht werden.
		 * 
		 * @param maxHistory
		 *            das maximale Alter von archivierten Betriebsmeldungen.
		 */
		public void setMaxHistory(final long maxHistory) {
			// Nur der Absolutwert wird übernommen.
			if (maxHistory >= 0) {
				this.maxHistory = maxHistory;
			} else {
				this.maxHistory = maxHistory * -1;
			}
		}

		/**
		 * Gibt die maximale Anzahl gecachter Meldungen zurück.
		 * 
		 * @return die Maximalanzahl gecachter Meldungen.
		 */
		public int getMaxAnzahl() {
			return maxAnzahl;
		}

		/**
		 * Legt die maximale Anzahl gecachter Meldungen fest.
		 * 
		 * @param maxAnzahl
		 *            die Maximalanzahl gecachter Meldungen.
		 */
		public void setMaxAnzahl(final int maxAnzahl) {
			if (maxAnzahl <= 0) {
				throw new IllegalArgumentException(
						"Die maximale Anzahl gecachter Betriebsmeldungen muss größer 0 sein.");
			}
			this.maxAnzahl = maxAnzahl;
		}

		/**
		 * @return
		 */
		public List<BetriebsMeldungSpalte> getAnzuzeigendeSpalten() {
			return anzuzeigendeSpalten;
		}

		/**
		 * @param anzuzeigendeSpalten
		 */
		public void setAnzuzeigendeSpalten(
				final List<BetriebsMeldungSpalte> anzuzeigendeSpalten) {
			this.anzuzeigendeSpalten = anzuzeigendeSpalten;
		}

		/**
		 * @return
		 */
		public List<Darstellung> getDarstellung() {
			return darstellung;
		}

		/**
		 * @param darstellung
		 */
		public void setDarstellung(final List<Darstellung> darstellung) {
			this.darstellung = darstellung;
		}

		/**
		 * Erzeugt eine flache Kopie.
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			klon.anzuzeigendeSpalten = new ArrayList<BetriebsMeldungSpalte>(
					anzuzeigendeSpalten);
			klon.darstellung = new ArrayList<Darstellung>(darstellung);
			klon.maxAnzahl = maxAnzahl;
			klon.maxHistory = maxHistory;

			return klon;
		}

		/**
		 * {@inheritDoc}
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * setzt den aktuellen Datensatzstatus.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s;

			s = getClass().getName() + "[";
			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;
			s += ", maxAnzahl" + maxAnzahl;
			s += ", maxHistory" + maxHistory;
			s += ", anzuzeigendeSpalten" + anzuzeigendeSpalten;
			s += ", darstellung" + darstellung;
			s += "]";

			return s;
		}

		/**
		 * Sucht in der Liste der Darstellungsoptionen nach der mit einer
		 * bestimmten Meldungsklasse.
		 * 
		 * @param klasse
		 *            eine Meldungsklasse.
		 * @return die passenden Darstellungsparameter oder {@code null}, wenn
		 *         die Liste keine passenden enthält.
		 */
		public Darstellung getDarstellung(final MeldungsKlasse klasse) {
			for (final Darstellung d : getDarstellung()) {
				if (klasse.equals(d.getKlasse())) {
					return d;
				}
			}
			return null;
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_BC_BMV_MESSAGE_DISPLAY = "atg.bcBetriebsmeldungDarstellung";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            ein Ereignis.
	 */
	public PdBcBetriebsMeldungDarstellung(
			final BcBetriebsMeldungsVerwaltung objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_BC_BMV_MESSAGE_DISPLAY);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();
		final Array darstellung, anzuzeigendeSpalten;

		darstellung = daten.getArray("Darstellung");
		darstellung.setLength(datum.getDarstellung().size());
		for (int i = 0; i < darstellung.getLength(); ++i) {
			final Data item = darstellung.getItem(i);
			final Darstellung d = datum.getDarstellung().get(i);
			item.getUnscaledValue("Klasse").set(d.getKlasse().getCode());
			item.getUnscaledValue("Vordergrundfarbe").set(
					d.getVordergrundfarbe());
			item.getUnscaledValue("Hintergrundfarbe").set(
					d.getHintergrundfarbe());
			item.getTextValue("Schriftart").setText(d.getSchriftart());
			item.getUnscaledValue("Fettdruck").setText(
					d.isFettdruck() ? "Ja" : "Nein");
			item.getUnscaledValue("Kursivdruck").setText(
					d.isKursivdruck() ? "Ja" : "Nein");
		}

		daten.getUnscaledValue("MaxAnzahl").set(datum.getMaxAnzahl());
		daten.getUnscaledValue("MaxHistory").set(
				datum.getMaxHistory() / Constants.MILLIS_PER_DAY);

		anzuzeigendeSpalten = daten.getArray("AnzuzeigendeSpalten");
		anzuzeigendeSpalten.setLength(datum.getAnzuzeigendeSpalten().size());
		for (int i = 0; i < anzuzeigendeSpalten.getLength(); ++i) {
			anzuzeigendeSpalten.getItem(i).asUnscaledValue().set(
					datum.getAnzuzeigendeSpalten().get(i).getCode());
		}

		return daten;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			final Array darstellung, anzuzeigendeSpalten;

			darstellung = daten.getArray("Darstellung");
			for (int i = 0; i < darstellung.getLength(); ++i) {
				final Data item = darstellung.getItem(i);
				final Darstellung d = new Darstellung();

				d.setKlasse(MeldungsKlasse.getMeldungsKlasse(item
						.getUnscaledValue("Klasse").intValue()));
				d.setVordergrundfarbe(item.getUnscaledValue("Vordergrundfarbe")
						.longValue());
				d.setHintergrundfarbe(item.getUnscaledValue("Hintergrundfarbe")
						.longValue());
				d.setSchriftart(item.getTextValue("Schriftart").getText());
				d.setFettdruck(item.getUnscaledValue("Fettdruck").getText()
						.equals("Ja"));
				d.setKursivdruck(item.getUnscaledValue("Kursivdruck").getText()
						.equals("Ja"));

				datum.getDarstellung().add(d);
			}

			datum.setMaxAnzahl(daten.getUnscaledValue("MaxAnzahl").intValue());
			datum.setMaxHistory(Constants.MILLIS_PER_DAY
					* daten.getUnscaledValue("MaxHistory").intValue());

			anzuzeigendeSpalten = daten.getArray("AnzuzeigendeSpalten");
			datum.getAnzuzeigendeSpalten().clear();
			for (int i = 0; i < anzuzeigendeSpalten.getLength(); ++i) {
				final Data item = anzuzeigendeSpalten.getItem(i);
				datum.getAnzuzeigendeSpalten().add(
						BetriebsMeldungSpalte.valueOf(item.asUnscaledValue()
								.intValue()));
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
