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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * Ein Parameterdatensatz, die Eigenschaften eines Ereignistyps beinhaltet. Der
 * Datensatz repr‰sentiert die Daten einer Attributgruppe
 * "atg.ereignisTypParameter".
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class PdEreignisTypParameter
		extends AbstractParameterDatensatz<PdEreignisTypParameter.Daten> {

	/**
	 * Repr&auml;sentation der Daten des Ereignistypparameters.
	 */
	public static class Daten extends AbstractDatum {

		/** Die Priorit&auml;t des Ereignistyps. */
		private long prioritaet;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.prioritaet = prioritaet;
			klon.datenStatus = datenStatus;
			klon.setZeitstempel(getZeitstempel());

			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Gibt die Priorit&auml;t des Ereignistyps wieder.
		 *
		 * @return die Ereignistyppriorit&auml;t.
		 */
		public long getPrioritaet() {
			return prioritaet;
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
		 * Legt die Priorit&auml;t des Ereignistyps fest.
		 *
		 * @param prioritaet
		 *            die neue Priorit&auml;t.
		 */
		public void setPrioritaet(final long prioritaet) {
			this.prioritaet = prioritaet;
		}

		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", valid=" + isValid();
			s += ", prioritaet=" + prioritaet;

			return s + "]";
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_EREIGNIS_TYP_PARAMETER = "atg.ereignisTypParameter";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Konstruktor.
	 *
	 * @param objekt
	 *            der Ereignistyp.
	 */
	public PdEreignisTypParameter(final SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_EREIGNIS_TYP_PARAMETER);
			assert atg != null;
		}
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
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		daten.getUnscaledValue("EreignisTypPriorit‰t")
				.set(datum.getPrioritaet());

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();

			datum.setPrioritaet(
					daten.getUnscaledValue("EreignisTypPriorit‰t").longValue());

		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
