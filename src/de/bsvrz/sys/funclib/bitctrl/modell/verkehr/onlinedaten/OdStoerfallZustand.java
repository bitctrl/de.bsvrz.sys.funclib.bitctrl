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
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.fachmodellglobal.GueteVerfahren;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallIndikator;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallSituation;

/**
 * Kapselt die Attributgruppe {@code atg.st&ouml;rfallZustand}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdStoerfallZustand extends
		AbstractOnlineDatensatz<OdStoerfallZustand.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenStandard}. */
		StoerfallVerfahrenStandard("asp.st�rfallVerfahrenStandard"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenMARZ}. */
		StoerfallVerfahrenMARZ("asp.st�rfallVerfahrenMARZ"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenNRW}. */
		StoerfallVerfahrenNRW("asp.st�rfallVerfahrenNRW"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenRDS}. */
		StoerfallVerfahrenRDS("asp.st�rfallVerfahrenRDS"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenFD}. */
		StoerfallVerfahrenFD("asp.st�rfallVerfahrenFD"),

		/** Der Aspekt {@code asp.st&ouml;rfallVerfahrenVKDiffKfz}. */
		StoerfallVerfahrenVKDiffKfz("asp.st�rfallVerfahrenVKDiffKfz"),

		/** Der Aspekt {@code asp.st�rfallVerfahrenConstraint}. */
		StoerfallVerfahrenConstraint("asp.st�rfallVerfahrenConstraint"),

		/** Der Aspekt {@code asp.st�rfallVerfahrenFuzzy}. */
		StoerfallVerfahrenFuzzy("asp.st�rfallVerfahrenFuzzy"),

		/** Der Aspekt {@code asp.st�rfallVerfahrenMOBINET}. */
		StoerfallVerfahrenMOBINET("asp.st�rfallVerfahrenMOBINET");

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
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
		private boolean valid;

		/** Intervalldauer, mit dem die Werte erfasst wurden. */
		private long t;

		/** Verkehrssituation. */
		private StoerfallSituation situation;

		/** Prognosehorizont (0 entspricht Analysewert). */
		private long horizont;

		/** Die G&uuml;te des betrachteten Wertes. */
		private double gueteIndex;

		/** Berechnungsverfahren, mit dem die G&uuml;te ermittelt wurde. */
		private GueteVerfahren gueteVerfahren;

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			klon.setGueteIndex(gueteIndex);
			klon.setGueteVerfahren(gueteVerfahren);
			klon.setHorizont(horizont);
			klon.setSituation(situation);
			klon.setT(t);
			klon.setZeitstempel(getZeitstempel());
			klon.valid = valid;
			return klon;
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
		 * {@inheritDoc}
		 */
		public boolean isValid() {
			return valid;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code gueteIndex} fest.
		 * 
		 * @param gueteIndex
		 *            der neue Wert von {@code gueteIndex}.
		 */
		public void setGueteIndex(double gueteIndex) {
			this.gueteIndex = gueteIndex;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code gueteVerfahren} fest.
		 * 
		 * @param gueteVerfahren
		 *            der neue Wert von {@code gueteVerfahren}.
		 */
		public void setGueteVerfahren(GueteVerfahren gueteVerfahren) {
			this.gueteVerfahren = gueteVerfahren;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code horizont} fest.
		 * 
		 * @param horizont
		 *            der neue Wert von {@code horizont}.
		 */
		public void setHorizont(long horizont) {
			this.horizont = horizont;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code situation} fest.
		 * 
		 * @param situation
		 *            der neue Wert von {@code situation}.
		 */
		public void setSituation(StoerfallSituation situation) {
			this.situation = situation;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code t} fest.
		 * 
		 * @param t
		 *            der neue Wert von {@code t}.
		 */
		public void setT(long t) {
			this.t = t;
		}

		/**
		 * Setzt das Flag {@code valid} des Datum.
		 * 
		 * @param valid
		 *            der neue Wert des Flags.
		 */
		protected void setValid(boolean valid) {
			this.valid = valid;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
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
	private static final String ATG_STOERFALL_ZUSTAND = "atg.st�rfallZustand";
	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Datensatz.
	 * 
	 * @param si
	 *            ein St&ouml;rfallindikator.
	 */
	public OdStoerfallZustand(StoerfallIndikator si) {
		super(si);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_STOERFALL_ZUSTAND);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#abrufenDatum(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public OdStoerfallZustand.Daten abrufenDatum(Aspect asp) {
		return super.abrufenDatum(asp);
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
	 * liefert den St�rfallzustand, der mit dem �bergebenen Aspekt berechnet
	 * wurde. Wenn keine daten f�r den Aspekt verf�gbar sind wird der Zustand
	 * UNBEKANNT geliefert.
	 * 
	 * @param aspekt
	 *            der Aspekt
	 * @return der Zustand
	 */
	public StoerfallSituation getSituation(Aspect aspekt) {
		StoerfallSituation result = StoerfallSituation.UNBEKANNT;
		OdStoerfallZustand.Daten daten = getDatum(aspekt);
		if ((daten != null) && daten.isValid()) {
			result = daten.getSituation();
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten datum) {
		Data daten = erzeugeSendeCache();

		daten.getItem("G�te").getUnscaledValue("Index").set(
				datum.getGueteIndex());
		daten.getItem("G�te").getUnscaledValue("Verfahren").set(
				datum.getGueteVerfahren().getCode());
		daten.getTimeValue("Horizont").setMillis(datum.getHorizont());
		daten.getTimeValue("T").setMillis(datum.getT());
		daten.getUnscaledValue("Situation").set(datum.getSituation().getCode());

		return daten;
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
			Data daten = result.getData();

			datum.setGueteIndex(daten.getItem("G�te").getUnscaledValue("Index")
					.doubleValue());
			datum.setGueteVerfahren(GueteVerfahren.getGueteVerfahren(daten
					.getItem("G�te").getUnscaledValue("Verfahren").intValue()));
			datum.setHorizont(daten.getTimeValue("Horizont").getMillis());
			datum.setT(daten.getTimeValue("T").getMillis());
			datum.setSituation(StoerfallSituation.getSituation(daten
					.getUnscaledValue("Situation").intValue()));

			datum.setValid(true);
		} else {
			datum.setValid(false);
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
