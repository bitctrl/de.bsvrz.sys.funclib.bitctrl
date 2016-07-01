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

/**
 * Ein Parameterdatensatz, der die Daten des Verantwortlichen für die Baustelle
 * beinhaltet. Der Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.baustellenVerantwortlicher".
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */
public class PdBaustellenVerantwortlicher
extends AbstractParameterDatensatz<PdBaustellenVerantwortlicher.Daten> {

	/**
	 * Repr&auml;sentation der Daten des Baustellenverantwortlichen-Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/** Name der verantwortlichen Firma. */
		private String firma;

		/** Telefonnummer der verantwortlichen Firma. */
		private String nameBaustellenVerantwortlicher;

		/** Name des Baustellenverantwortlichen. */
		private String telefonBaustellenVerantwortlicher;

		/** Telefonnummer des Baustellenverantwortlichen. */
		private String telefonFirma;

		/** Mobiltelefonnummer des Baustellenverantwortlichen. */
		private String telefonMobilBaustellenVerantwortlicher;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein leeres Datum.
		 */
		public Daten() {
			firma = "";
			nameBaustellenVerantwortlicher = "";
			telefonBaustellenVerantwortlicher = "";
			telefonFirma = "";
			telefonMobilBaustellenVerantwortlicher = "";
			setZeitstempel(0);
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion erzeugt ein Datum als Kopie des &uuml;bergebenen Datums.
		 *
		 * @param daten
		 *            die Daten die kopiert werden sollen
		 */
		public Daten(final Daten daten) {
			firma = daten.firma;
			nameBaustellenVerantwortlicher = daten.nameBaustellenVerantwortlicher;
			telefonBaustellenVerantwortlicher = daten.telefonBaustellenVerantwortlicher;
			telefonFirma = daten.telefonFirma;
			telefonMobilBaustellenVerantwortlicher = daten.telefonMobilBaustellenVerantwortlicher;

			datenStatus = daten.datenStatus;
			setZeitstempel(daten.getZeitstempel());
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion wertet den vom Datenverteiler empfangenen Datensatz aus
		 * und f&uuml;llt die Daten entsprechend.
		 *
		 * @param result
		 *            der &uuml;bergebene Datensatz
		 */
		public Daten(final ResultData result) {
			setZeitstempel(result.getDataTime());
			final Data daten = result.getData();
			if (daten == null) {
				firma = "";
				nameBaustellenVerantwortlicher = "";
				telefonBaustellenVerantwortlicher = "";
				telefonFirma = "";
				telefonMobilBaustellenVerantwortlicher = "";
			} else {
				firma = daten.getTextValue("Firma").getText();
				nameBaustellenVerantwortlicher = daten
						.getTextValue("NameBaustellenVerantwortlicher")
						.getText();
				telefonFirma = daten.getTextValue("TelefonFirma").getText();
				telefonBaustellenVerantwortlicher = daten
						.getTextValue("TelefonBaustellenVerantwortlicher")
						.getText();
				telefonMobilBaustellenVerantwortlicher = daten
						.getTextValue("TelefonMobilBaustellenVerantwortlicher")
						.getText();
			}

			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		@Override
		public Daten clone() {
			return new Daten(this);
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert den Namen der Firma des Baustellenverantwortlichen.
		 *
		 * @return die Firma
		 */
		public String getFirma() {
			return firma;
		}

		/**
		 * liefert den Namen des Baustellenverantwortlichen.
		 *
		 * @return Name des BaustellenVerantwortlichen
		 */
		public String getNameBaustellenVerantwortlicher() {
			return nameBaustellenVerantwortlicher;
		}

		/**
		 * liefert die Telefonnummer des Baustellenverantwortlichen.
		 *
		 * @return Telefonnummer Baustellenverantwortlicher
		 */
		public String getTelefonBaustellenVerantwortlicher() {
			return telefonBaustellenVerantwortlicher;
		}

		/**
		 * liefert die Telefonnummer der Firma des Baustellenverantwortlichen.
		 *
		 * @return Telefonnummer der Firma des Baustellenverantwortlichen
		 */
		public String getTelefonFirma() {
			return telefonFirma;
		}

		/**
		 * liefert die Mobil-Telefonnummer des Baustellenverantwortlichen.
		 *
		 * @return Mobil-Telefonnummer Baustellenverantwortlicher
		 */
		public String getTelefonMobilBaustellenVerantwortlicher() {
			return telefonMobilBaustellenVerantwortlicher;
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
		 * Setzt den Namen der Firma des Baustellenverantwortlichen.
		 *
		 * @param firma
		 *            zu setzende Firma
		 */
		public void setFirma(final String firma) {
			this.firma = firma;
		}

		/**
		 * setzt den Namen des Baustellenverantwortlichen.
		 *
		 * @param nameBaustellenVerantwortlicher
		 *            zu setzender Baustellenverantwortlicher
		 */
		public void setNameBaustellenVerantwortlicher(
				final String nameBaustellenVerantwortlicher) {
			this.nameBaustellenVerantwortlicher = nameBaustellenVerantwortlicher;
		}

		/**
		 * setzt die Telefonnummer des Baustellenverantwortlichen.
		 *
		 * @param telefonBaustellenVerantwortlicher
		 *            zu setzende Telefonnummer des Baustellenverantwortlichen
		 */
		public void setTelefonBaustellenVerantwortlicher(
				final String telefonBaustellenVerantwortlicher) {
			this.telefonBaustellenVerantwortlicher = telefonBaustellenVerantwortlicher;
		}

		/**
		 * setzt die Telefonnummer der Firma des Baustellenverantwortlichen.
		 *
		 * @param telefonFirma
		 *            zu setzende Telefonnummer
		 */
		public void setTelefonFirma(final String telefonFirma) {
			this.telefonFirma = telefonFirma;
		}

		/**
		 * setzt die Mobil-Telefonnummer desBaustellenverantwortlichen.
		 *
		 * @param telefonMobilBaustellenVerantwortlicher
		 *            zu setzende Mobil-Telefonnummer Baustellenverantwortlicher
		 */
		public void setTelefonMobilBaustellenVerantwortlicher(
				final String telefonMobilBaustellenVerantwortlicher) {
			this.telefonMobilBaustellenVerantwortlicher = telefonMobilBaustellenVerantwortlicher;
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
	public PdBaustellenVerantwortlicher(final SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenVerantwortlicher");
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

		daten.getTextValue("Firma").setText(datum.getFirma());
		daten.getTextValue("TelefonFirma").setText(datum.getTelefonFirma());
		daten.getTextValue("NameBaustellenVerantwortlicher")
		.setText(datum.getNameBaustellenVerantwortlicher());
		daten.getTextValue("TelefonBaustellenVerantwortlicher")
		.setText(datum.getTelefonBaustellenVerantwortlicher());
		daten.getTextValue("TelefonMobilBaustellenVerantwortlicher")
		.setText(datum.getTelefonMobilBaustellenVerantwortlicher());

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
