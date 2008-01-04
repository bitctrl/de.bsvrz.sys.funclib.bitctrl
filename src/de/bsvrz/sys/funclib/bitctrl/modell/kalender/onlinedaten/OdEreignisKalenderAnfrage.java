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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.Kalender;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.NetzBestandTeil;
import de.bsvrz.sys.funclib.bitctrl.util.Intervall;

/**
 * Kapselt die Attributgruppe {@code atg.ereignisKalenderAnfrage}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdEreignisKalenderAnfrage extends
		AbstractOnlineDatensatz<OdEreignisKalenderAnfrage.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.anfrage}. */
		Anfrage("asp.anfrage");

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

		/** Es werden alle Ereignistypen ber&uuml;cksichtigt. */
		public static final int OPT_ALLE = 0;

		/**
		 * Es werden nur die Ereignistypen ber&uuml;cksichtigt, die angeben
		 * sind.
		 */
		public static final int OPT_NUR = 1;

		/**
		 * Es werden alle Ereignistypen ber&uuml;cksichtigt au&szlig;er denen,
		 * die angeben sind.
		 */
		public static final int OPT_NICHT = 2;

		/** Das Absenderzeichen des Anfragers. */
		private String absenderZeichen;

		/** Das Zeitintervall, indem Ereignisse angefragt werden. */
		private Intervall intervall;

		/**
		 * Menge von Ereignistypen, die entsprechend der gesetzten Auswahloption
		 * ber&uuml;cksichtigt werden.
		 * 
		 * @see #ereignisTypenOption
		 */
		private final Set<EreignisTyp> ereignisTypen = new HashSet<EreignisTyp>();

		/**
		 * Auswahloption f&uuml;r die Liste der Ereignistypen. Standard ist
		 * {@link #OPT_ALLE}.
		 * 
		 * @see #ereignisTypen
		 */
		private int ereignisTypenOption = OPT_ALLE;

		/** Menge der Netzbestandteile, dessen Ereignisse angefragt werden. */
		private final Set<NetzBestandTeil> raeumlicheGueltigkeit = new HashSet<NetzBestandTeil>();

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
			// TODO

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
		public int getEreignisTypenOption() {
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
		 * Legt den Wert der Eigenschaft {@code ereignisTypenOption} fest.
		 * 
		 * @param ereignisTypenOption
		 *            der neue Wert von {@code ereignisTypenOption}.
		 */
		public void setEreignisTypenOption(int ereignisTypenOption) {
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

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", valid=" + valid;

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
	public static final String ATG_EREIGNIS_KALENDER_ANFRAGE = "atg.ereignisKalenderAnfrage";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param objekt
	 *            der Kalender.
	 */
	public OdEreignisKalenderAnfrage(Kalender objekt) {
		super(objekt);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_EREIGNIS_KALENDER_ANFRAGE);
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
			Data daten = result.getData();
			Array feld;

			datum.setAbsenderZeichen(daten.getTextValue("absenderZeichen")
					.getText());
			datum.setIntervall(new Intervall(daten.getTimeValue(
					"Anfangszeitpunkt").getMillis(), daten.getTimeValue(
					"Endzeitpunkt").getMillis()));

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
		Data daten = erzeugeSendeCache();

		daten.getItem("Güte").getUnscaledValue("Index").set(
				datum.getGueteIndex());
		daten.getItem("Güte").getUnscaledValue("Verfahren").set(
				datum.getGueteVerfahren().getCode());
		daten.getTimeValue("Horizont").setMillis(datum.getHorizont());
		daten.getTimeValue("T").setMillis(datum.getT());
		daten.getUnscaledValue("Situation").set(datum.getSituation().getCode());

		return daten;
	}

}
