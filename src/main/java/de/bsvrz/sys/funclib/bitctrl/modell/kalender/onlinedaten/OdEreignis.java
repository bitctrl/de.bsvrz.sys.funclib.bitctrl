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
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * Kapselt die Attributgruppe {@code atg.ereignis}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class OdEreignis extends AbstractOnlineDatensatz<OdEreignis.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.zustand}. */
		Zustand("asp.zustand");

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
	 * Repr&auml;sentation der Daten des Ereigniszustandes.
	 */
	public static class Daten extends AbstractDatum {

		/** Flag ob der Datensatz g&uuml;ltige Daten enth&auml;lt. */
		private boolean valid;

		/** Die Eigenschaft {@code zeitlichGueltig}. */
		private boolean zeitlichGueltig;

		/** Die Eigenschaft {@code verkehrlichGueltig}. */
		private boolean verkehrlichGueltig;

		/** Die Eigenschaft {@code attributAenderung}. */
		private boolean attributAenderung;

		/** Die Eigenschaft {@code naechsterWechsel}. */
		private long naechsterWechsel;

		/**
		 * aktueller Zustand des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.valid = valid;
			klon.attributAenderung = attributAenderung;
			klon.naechsterWechsel = naechsterWechsel;
			klon.verkehrlichGueltig = verkehrlichGueltig;
			klon.zeitlichGueltig = zeitlichGueltig;

			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code naechsterWechsel} wieder.
		 *
		 * @return {@code naechsterWechsel}.
		 */
		public long getNaechsterWechsel() {
			return naechsterWechsel;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code attributAenderung} wieder.
		 *
		 * @return {@code attributAenderung}.
		 */
		public boolean isAttributAenderung() {
			return attributAenderung;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code verkehrlichGueltig} wieder.
		 *
		 * @return {@code verkehrlichGueltig}.
		 */
		public boolean isVerkehrlichGueltig() {
			return verkehrlichGueltig;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code zeitlichGueltig} wieder.
		 *
		 * @return {@code zeitlichGueltig}.
		 */
		public boolean isZeitlichGueltig() {
			return zeitlichGueltig;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code attributAenderung} fest.
		 *
		 * @param attributAenderung
		 *            der neue Wert von {@code attributAenderung}.
		 */
		public void setAttributAenderung(final boolean attributAenderung) {
			this.attributAenderung = attributAenderung;
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
		 * Legt den Wert der Eigenschaft {@code naechsterWechsel} fest.
		 *
		 * @param naechsterWechsel
		 *            der neue Wert von {@code naechsterWechsel}.
		 */
		public void setNaechsterWechsel(final long naechsterWechsel) {
			this.naechsterWechsel = naechsterWechsel;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code verkehrlichGueltig} fest.
		 *
		 * @param verkehrlichGueltig
		 *            der neue Wert von {@code verkehrlichGueltig}.
		 */
		public void setVerkehrlichGueltig(final boolean verkehrlichGueltig) {
			this.verkehrlichGueltig = verkehrlichGueltig;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code zeitlichGueltig} fest.
		 *
		 * @param zeitlichGueltig
		 *            der neue Wert von {@code zeitlichGueltig}.
		 */
		public void setZeitlichGueltig(final boolean zeitlichGueltig) {
			this.zeitlichGueltig = zeitlichGueltig;
		}

		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", valid=" + valid;
			s += ", zeitlichGueltig=" + zeitlichGueltig;
			s += ", verkehrlichGueltig=" + verkehrlichGueltig;
			s += ", attributAenderung=" + attributAenderung;
			s += ", naechsterWechsel=" + naechsterWechsel;

			return s + "]";
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_EREIGNIS = "atg.ereignis";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert das Objekt.
	 *
	 * @param objekt
	 *            ein Ereignis.
	 */
	public OdEreignis(final SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_EREIGNIS);
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

		daten = erzeugeSendeCache();
		daten.getUnscaledValue("zeitlichGültig")
		.set(datum.isZeitlichGueltig() ? 1 : 0);
		daten.getUnscaledValue("verkehrlichGültig")
		.set(datum.isVerkehrlichGueltig() ? 1 : 0);
		daten.getUnscaledValue("AttributÄnderung")
		.set(datum.isAttributAenderung() ? 1 : 0);
		daten.getTimeValue("Zeitpunkt").setMillis(datum.getNaechsterWechsel());

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten;

			daten = result.getData();
			datum.setZeitlichGueltig(
					daten.getUnscaledValue("zeitlichGültig").intValue() == 1);
			datum.setVerkehrlichGueltig(daten
					.getUnscaledValue("verkehrlichGültig").intValue() == 1);
			datum.setAttributAenderung(
					daten.getUnscaledValue("AttributÄnderung").intValue() == 1);
			datum.setNaechsterWechsel(
					daten.getTimeValue("Zeitpunkt").getMillis());

		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
