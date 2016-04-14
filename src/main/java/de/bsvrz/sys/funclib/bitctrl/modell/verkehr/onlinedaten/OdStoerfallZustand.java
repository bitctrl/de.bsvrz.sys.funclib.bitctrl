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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.fachmodellglobal.zustaende.GueteVerfahren;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StoerfallIndikator;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.StoerfallSituation;

/**
 * Kapselt die Attributgruppe {@code atg.st&ouml;rfallZustand}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class OdStoerfallZustand
extends AbstractOnlineDatensatz<OdStoerfallZustand.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenStandard}. */
		StoerfallVerfahrenStandard("asp.störfallVerfahrenStandard"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenMARZ}. */
		StoerfallVerfahrenMARZ("asp.störfallVerfahrenMARZ"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenNRW}. */
		StoerfallVerfahrenNRW("asp.störfallVerfahrenNRW"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenRDS}. */
		StoerfallVerfahrenRDS("asp.störfallVerfahrenRDS"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenFD}. */
		StoerfallVerfahrenFD("asp.störfallVerfahrenFD"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenVKDiffKfz}. */
		StoerfallVerfahrenVKDiffKfz("asp.störfallVerfahrenVKDiffKfz"),

		/** Der Aspekt {@code asp.störfallVerfahrenConstraint}. */
		StoerfallVerfahrenConstraint("asp.störfallVerfahrenConstraint"),

		/** Der Aspekt {@code asp.störfallVerfahrenFuzzy}. */
		StoerfallVerfahrenFuzzy("asp.störfallVerfahrenFuzzy"),

		/** Der Aspekt {@code asp.störfallVerfahrenMOBINET}. */
		StoerfallVerfahrenMOBINET("asp.störfallVerfahrenMOBINET");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 *
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		Aspekte(final String pid) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		@Override
		public Aspect getAspekt() {
			return aspekt;
		}

		@Override
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/** Intervalldauer, mit dem die Werte erfasst wurden. */
		private long t;

		/** Verkehrssituation. */
		private StoerfallSituation situation = StoerfallSituation.KEINE_AUSSAGE;

		/** Prognosehorizont (0 entspricht Analysewert). */
		private long horizont;

		/** Die G&uuml;te des betrachteten Wertes. */
		private double gueteIndex = 1.0;

		/** Berechnungsverfahren, mit dem die G&uuml;te ermittelt wurde. */
		private GueteVerfahren gueteVerfahren = GueteVerfahren.Standard;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setGueteIndex(gueteIndex);
			klon.setGueteVerfahren(gueteVerfahren);
			klon.setHorizont(horizont);
			klon.setSituation(situation);
			klon.setT(t);
			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code gueteIndex} wieder.
		 *
		 * @return {@code gueteIndex}.
		 */
		public double getGueteIndex() {
			return gueteIndex;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code gueteVerfahren} wieder.
		 *
		 * @return {@code gueteVerfahren}.
		 */
		public GueteVerfahren getGueteVerfahren() {
			return gueteVerfahren;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code horizont} wieder.
		 *
		 * @return {@code horizont}.
		 */
		public long getHorizont() {
			return horizont;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code situation} wieder.
		 *
		 * @return {@code situation}.
		 */
		public StoerfallSituation getSituation() {
			return situation;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code t} wieder.
		 *
		 * @return {@code t}.
		 */
		public long getT() {
			return t;
		}

		/**
		 * setzt den aktuellen Status des Datensatzes.
		 *
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code gueteIndex} fest.
		 *
		 * @param gueteIndex
		 *            der neue Wert von {@code gueteIndex}.
		 */
		public void setGueteIndex(final double gueteIndex) {
			this.gueteIndex = gueteIndex;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code gueteVerfahren} fest.
		 *
		 * @param gueteVerfahren
		 *            der neue Wert von {@code gueteVerfahren}.
		 */
		public void setGueteVerfahren(final GueteVerfahren gueteVerfahren) {
			this.gueteVerfahren = gueteVerfahren;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code horizont} fest.
		 *
		 * @param horizont
		 *            der neue Wert von {@code horizont}.
		 */
		public void setHorizont(final long horizont) {
			this.horizont = horizont;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code situation} fest.
		 *
		 * @param situation
		 *            der neue Wert von {@code situation}.
		 */
		public void setSituation(final StoerfallSituation situation) {
			this.situation = situation;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code t} fest.
		 *
		 * @param t
		 *            der neue Wert von {@code t}.
		 */
		public void setT(final long t) {
			this.t = t;
		}

		@Override
		public String toString() {
			String s = "Daten[";

			s += "[zeitpunkt=" + getZeitpunkt();
			s += ", valid=" + isValid();
			s += " , t=" + t;
			s += ", horizont=" + horizont;
			s += ", situation=" + situation;
			s += ", gueteIndex=" + gueteIndex;
			s += ", gueteVerfahren=" + gueteVerfahren;

			return s + "]";
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_STOERFALL_ZUSTAND = "atg.störfallZustand";
	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Datensatz.
	 *
	 * @param si
	 *            ein St&ouml;rfallindikator.
	 */
	public OdStoerfallZustand(final StoerfallIndikator si) {
		super(si);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_STOERFALL_ZUSTAND);
			assert atg != null;
		}
	}

	@Override
	public OdStoerfallZustand.Daten abrufenDatum(final Aspect asp) {
		return super.abrufenDatum(asp);
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * liefert den Störfallzustand, der mit dem übergebenen Aspekt berechnet
	 * wurde. Wenn keine daten für den Aspekt verfügbar sind wird der Zustand
	 * UNBEKANNT geliefert.
	 *
	 * @param aspekt
	 *            der Aspekt
	 * @return der Zustand
	 */
	public StoerfallSituation getSituation(final Aspect aspekt) {
		StoerfallSituation result = StoerfallSituation.UNBEKANNT;
		final OdStoerfallZustand.Daten daten = getDatum(aspekt);
		if ((daten != null) && daten.isValid()) {
			result = daten.getSituation();
		}

		return result;
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		final double gIndex = datum.getGueteIndex();
		if (gIndex < 0) {
			daten.getItem("Güte").getUnscaledValue("Index").set((int) gIndex);
		} else {
			daten.getItem("Güte").getScaledValue("Index").set(gIndex);
		}

		daten.getItem("Güte").getUnscaledValue("Verfahren")
		.set(datum.getGueteVerfahren().getCode());
		daten.getTimeValue("Horizont").setMillis(datum.getHorizont());
		daten.getTimeValue("T").setMillis(datum.getT());
		daten.getUnscaledValue("Situation").set(datum.getSituation().getCode());

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();

			if (daten.getItem("Güte").getUnscaledValue("Index").isState()) {
				datum.setGueteIndex(daten.getItem("Güte")
						.getUnscaledValue("Index").intValue());
			} else {
				datum.setGueteIndex(daten.getItem("Güte")
						.getScaledValue("Index").doubleValue());
			}
			datum.setGueteVerfahren(GueteVerfahren.getGueteVerfahren(daten
					.getItem("Güte").getUnscaledValue("Verfahren").intValue()));
			datum.setHorizont(daten.getTimeValue("Horizont").getMillis());
			datum.setT(daten.getTimeValue("T").getMillis());
			datum.setSituation(StoerfallSituation.getSituation(
					daten.getUnscaledValue("Situation").intValue()));
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
