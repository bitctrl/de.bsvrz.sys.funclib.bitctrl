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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Berechtigungsklasse;

/**
 * Kapselt die Attributgruppe
 * {@value PdBenutzerParameter#ATG_BENUTZER_PARAMETER}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class PdBenutzerParameter
extends AbstractParameterDatensatz<PdBenutzerParameter.Daten> {

	/**
	 * Repräsentation der Daten des Parameters.
	 */
	public static class Daten extends AbstractDatum {

		/** Der Initialzustand des Datenstatus ist UNDEFINIERT. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/** Die Berechtigungsklasse des Nutzers. */
		private Berechtigungsklasse berechtigungsklasse;

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Legt den aktuellen Datensatzstatus fest.
		 *
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * Gibt die Berechtigungsklasse des Nutzers zurück.
		 *
		 * @return die Berechtigungsklasse des Nutzers.
		 */
		public Berechtigungsklasse getBerechtigungsklasse() {
			return berechtigungsklasse;
		}

		/**
		 * Legt die Berechtigungsklasse des Nutzers fest.
		 *
		 * @param berechtigungsklasse
		 *            die Berechtigungsklasse des Nutzers.
		 */
		public void setBerechtigungsklasse(
				final Berechtigungsklasse berechtigungsklasse) {
			this.berechtigungsklasse = berechtigungsklasse;
		}

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;

			return klon;
		}

		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;
			s += ", berechtigungsklasse=" + berechtigungsklasse;

			return s + "]";
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_BENUTZER_PARAMETER = "atg.benutzerParameter";

	/** Die Attributgruppe. */
	private static AttributeGroup atg;

	/**
	 * Konstruktor.
	 *
	 * @param objekt
	 *            ein Systemobjekt, welches einen Benutzer darstellt.
	 */
	public PdBenutzerParameter(final SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_BENUTZER_PARAMETER);
			assert atg != null;
		}
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();
		daten.getReferenceValue("berechtigungsklasse").setSystemObject(
				datum.getBerechtigungsklasse().getSystemObject());

		return daten;
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	@Override
	public void setDaten(final ResultData datensatz) {
		check(datensatz);

		final Daten datum = new Daten();
		if (datensatz.hasData()) {
			final ObjektFactory factory;
			final Data daten;

			factory = ObjektFactory.getInstanz();
			daten = datensatz.getData();

			datum.setBerechtigungsklasse(
					(Berechtigungsklasse) factory.getModellobjekt(
							daten.getReferenceValue("berechtigungsklasse")
							.getSystemObject()));
		}

		datum.setZeitstempel(datensatz.getDataTime());
		datum.setDatenStatus(Datum.Status.getStatus(datensatz.getDataState()));
		setDatum(datensatz.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(datensatz.getDataDescription().getAspect(),
				datum.clone());
	}

}
