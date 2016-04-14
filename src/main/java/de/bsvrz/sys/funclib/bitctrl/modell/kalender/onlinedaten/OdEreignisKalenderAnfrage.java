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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.onlinedaten;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.bitctrl.util.Interval;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.zustaende.EreignisTypenOption;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.NetzBestandTeil;

/**
 * Kapselt die Attributgruppe {@code atg.ereignisKalenderAnfrage}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class OdEreignisKalenderAnfrage
extends AbstractOnlineDatensatz<OdEreignisKalenderAnfrage.Daten> {

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
	 * Repr&auml;sentation der Daten des Ereignistypparameters.
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Menge von Ereignistypen, die entsprechend der gesetzten Auswahloption
		 * ber&uuml;cksichtigt werden.
		 *
		 * @see #ereignisTypenOption
		 */
		private final Set<EreignisTyp> ereignisTypen = new HashSet<>();

		/** Menge der Netzbestandteile, dessen Ereignisse angefragt werden. */
		private final Set<NetzBestandTeil> raeumlicheGueltigkeit = new HashSet<>();

		/** Der Absender der Anfrage. */
		private Applikation absender;

		/** Das Absenderzeichen des Anfragers. */
		private String absenderZeichen;

		/** Das Zeitintervall, indem Ereignisse angefragt werden. */
		private Interval intervall;

		/**
		 * Auswahloption f&uuml;r die Liste der Ereignistypen. Standard ist
		 * {@link EreignisTypenOption#ALLE}.
		 *
		 * @see #ereignisTypen
		 */
		private EreignisTypenOption ereignisTypenOption = EreignisTypenOption.ALLE;

		/** Flag ob der Datensatz g&uuml;ltige Daten enth&auml;lt. */
		private boolean valid;

		/**
		 * aktueller Datenstatus.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Erzeugt eine flache Kopie.
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.valid = valid;
			klon.absender = absender;
			klon.absenderZeichen = absenderZeichen;
			klon.ereignisTypen.addAll(ereignisTypen);
			klon.ereignisTypenOption = ereignisTypenOption;
			klon.intervall = intervall.clone();
			klon.raeumlicheGueltigkeit.addAll(raeumlicheGueltigkeit);

			return klon;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code absender} wieder.
		 *
		 * @return {@code absender}.
		 */
		public Applikation getAbsender() {
			return absender;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code absenderZeichen} wieder.
		 *
		 * @return {@code absenderZeichen}.
		 */
		public String getAbsenderZeichen() {
			return absenderZeichen;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
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
		public EreignisTypenOption getEreignisTypenOption() {
			return ereignisTypenOption;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code intervall} wieder.
		 *
		 * @return {@code intervall}.
		 */
		public Interval getIntervall() {
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
		 * Legt den Wert der Eigenschaft {@code absender} fest.
		 *
		 * @param absender
		 *            der neue Wert von {@code absender}.
		 */
		public void setAbsender(final Applikation absender) {
			this.absender = absender;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code absenderZeichen} fest.
		 *
		 * @param absenderZeichen
		 *            der neue Wert von {@code absenderZeichen}.
		 */
		public void setAbsenderZeichen(final String absenderZeichen) {
			this.absenderZeichen = absenderZeichen;
		}

		/**
		 * setzt den aktuellen Datenstatus.
		 *
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code ereignisTypenOption} fest.
		 *
		 * @param ereignisTypenOption
		 *            der neue Wert von {@code ereignisTypenOption}.
		 */
		public void setEreignisTypenOption(
				final EreignisTypenOption ereignisTypenOption) {
			this.ereignisTypenOption = ereignisTypenOption;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code intervall} fest.
		 *
		 * @param intervall
		 *            der neue Wert von {@code intervall}.
		 */
		public void setIntervall(final Interval intervall) {
			this.intervall = intervall;
		}

		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", valid=" + valid;
			s += ", absenderId=" + absender;
			s += ", absenderZeichen=" + absenderZeichen;
			s += ", intervall=" + intervall;
			s += ", ereignisTypenOption=" + ereignisTypenOption;
			s += ", ereignisTypen=" + ereignisTypen;
			s += ", raeumlicheGueltigkeit=" + raeumlicheGueltigkeit;

			return s + "]";
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
	public OdEreignisKalenderAnfrage(final SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_EREIGNIS_KALENDER_ANFRAGE);
			assert atg != null;
		}
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

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten;
		Array feld;
		int i;

		daten = erzeugeSendeCache();
		daten.getReferenceValue("absenderId")
		.setSystemObject(datum.getAbsender().getSystemObject());
		daten.getTextValue("absenderZeichen")
		.setText(datum.getAbsenderZeichen());
		daten.getTimeValue("Anfangszeitpunkt")
		.setMillis(datum.getIntervall().getStart());
		daten.getTimeValue("Endzeitpunkt")
		.setMillis(datum.getIntervall().getEnd());
		daten.getUnscaledValue("EreignisTypenOption")
		.set(datum.getEreignisTypenOption().getCode());

		feld = daten.getArray("RäumlicheGültigkeit");
		feld.setLength(datum.getRaeumlicheGueltigkeit().size());
		i = 0;
		for (final NetzBestandTeil nbt : datum.getRaeumlicheGueltigkeit()) {
			feld.getItem(i++).asReferenceValue()
			.setSystemObject(nbt.getSystemObject());
		}

		feld = daten.getArray("EreignisTypReferenz");
		feld.setLength(datum.getEreignisTypen().size());
		i = 0;
		for (final EreignisTyp typ : datum.getEreignisTypen()) {
			feld.getItem(i++).asReferenceValue()
			.setSystemObject(typ.getSystemObject());
		}

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final ObjektFactory factory;
			final Data daten;
			Array feld;

			factory = ObjektFactory.getInstanz();

			daten = result.getData();

			datum.setAbsender((Applikation) factory.getModellobjekt(
					daten.getReferenceValue("absenderId").getSystemObject()));
			datum.setAbsenderZeichen(
					daten.getTextValue("absenderZeichen").getText());
			datum.setIntervall(new Interval(
					daten.getTimeValue("Anfangszeitpunkt").getMillis(),
					daten.getTimeValue("Endzeitpunkt").getMillis()));
			datum.setEreignisTypenOption(EreignisTypenOption.getTyp(
					daten.getUnscaledValue("EreignisTypenOption").intValue()));

			feld = daten.getArray("RäumlicheGültigkeit");
			for (int i = 0; i < feld.getLength(); i++) {
				datum.getRaeumlicheGueltigkeit()
				.add((NetzBestandTeil) factory
						.getModellobjekt(feld.getItem(i++)
								.asReferenceValue().getSystemObject()));
			}

			feld = daten.getArray("EreignisTypReferenz");
			feld.setLength(datum.getEreignisTypen().size());
			for (int i = 0; i < feld.getLength(); i++) {
				datum.getEreignisTypen()
				.add((EreignisTyp) factory
						.getModellobjekt(feld.getItem(i++)
								.asReferenceValue().getSystemObject()));
			}

		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
