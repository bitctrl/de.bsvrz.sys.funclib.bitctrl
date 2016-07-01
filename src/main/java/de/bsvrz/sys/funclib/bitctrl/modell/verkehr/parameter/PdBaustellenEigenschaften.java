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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.BaustellenStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.BaustellenVeranlasser;

/**
 * Ein Parameterdatensatz, die Eigenschaften einer Baustelle beinhaltet. Der
 * Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.baustellenEigenschaften".
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class PdBaustellenEigenschaften
extends AbstractParameterDatensatz<PdBaustellenEigenschaften.Daten> {

	/**
	 * Repräsentation der Daten des Baustelleneigenschaften-Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Restkapazität während der Gültigkeitsdauer der Baustelle.
		 * ("RestKapazität")
		 */
		private long restKapazitaet;
		/**
		 * Zustand der Baustelle. ("Status")
		 */
		private BaustellenStatus status = BaustellenStatus.ENTWORFEN;
		/**
		 * Veranlasser der Baustelle (BIS-System oder VRZ). ("Veranlasser")
		 */
		private BaustellenVeranlasser veranlasser;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein en leeres Datum.
		 */
		public Daten() {
			status = BaustellenStatus.ENTWORFEN;
			veranlasser = BaustellenVeranlasser.UNDEFINIERT;
			restKapazitaet = 0;
			setZeitstempel(0);
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion erzeugt ein Datum als Kopie des übergebenen Datums.
		 *
		 * @param daten
		 *            die Daten die kopiert werden sollen
		 */
		public Daten(final Daten daten) {
			restKapazitaet = daten.restKapazitaet;
			status = daten.status;
			veranlasser = daten.veranlasser;
			datenStatus = daten.datenStatus;
			setZeitstempel(daten.getZeitstempel());
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion wertet den vom Datenverteiler empfangenen Datensatz aus
		 * und füllt die Daten entsprechend.
		 *
		 * @param result
		 *            der übergebene Datensatz
		 */
		public Daten(final ResultData result) {
			setZeitstempel(result.getDataTime());
			final Data daten = result.getData();
			if (daten == null) {
				status = BaustellenStatus.ENTWORFEN;
				veranlasser = BaustellenVeranlasser.UNDEFINIERT;
				restKapazitaet = 0;
			} else {
				status = BaustellenStatus
						.getStatus(daten.getUnscaledValue("Status").intValue());
				restKapazitaet = daten.getScaledValue("RestKapazität")
						.longValue();
				veranlasser = BaustellenVeranlasser.getVeranlasser(
						daten.getUnscaledValue("Veranlasser").intValue());
			}
			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		@Override
		public Daten clone() {
			return new Daten(this);
		}

		/**
		 * liefert den Status der Baustelle.
		 *
		 * @return der Status
		 */
		public BaustellenStatus getBaustellenStatus() {
			return status;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert die Restkapazität der Baustelle.
		 *
		 * @return die Restkapazität
		 */
		public long getRestKapazitaet() {
			return restKapazitaet;
		}

		/**
		 * liefert den Veranlassser der Baustelle.
		 *
		 * @return den Veranlasser
		 */
		public BaustellenVeranlasser getVeranlasser() {
			return veranlasser;
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
		 * setzt die Restkapazität der Baustelle.
		 *
		 * @param restKapazitaet
		 *            die zu setzende Restkapazit&auml;t
		 *
		 */
		public void setRestKapazitaet(final long restKapazitaet) {
			this.restKapazitaet = restKapazitaet;
		}

		/**
		 * setzt den Status der Baustelle.
		 *
		 * @param status
		 *            zu setzender Status
		 *
		 */
		public void setStatus(final BaustellenStatus status) {
			this.status = status;
		}

		/**
		 * setzt den Veranlasser der Baustelle.
		 *
		 * @param veranlasser
		 *            zu setzender Veranlasser
		 *
		 */
		public void setVeranlasser(final BaustellenVeranlasser veranlasser) {
			this.veranlasser = veranlasser;
		}
	}

	/**
	 * die Attributgruppe, in der die Eigenschaften enthalten sind.
	 */
	private static AttributeGroup attributGruppe;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeigt eine Instanz eines
	 * Baustellen-Eigenschaften-Parameterdatensatzes.
	 *
	 * @param objekt
	 *            das der Baustelle zugrundliegende Systemobjekt.
	 */
	public PdBaustellenEigenschaften(final SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenEigenschaften");
		}
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		daten.getUnscaledValue("Status")
		.set(datum.getBaustellenStatus().ordinal());
		daten.getScaledValue("RestKapazität").set(datum.getRestKapazitaet());
		daten.getUnscaledValue("Veranlasser")
		.set(datum.getVeranlasser().getCode());

		return daten;
	}

	@Override
	public void setDaten(final ResultData daten) {
		check(daten);
		final Daten datum = new Daten(daten);
		setDatum(datum);
		datum.setDatenStatus(Datum.Status.getStatus(daten.getDataState()));
		fireDatensatzAktualisiert(datum.clone());

	}
}
