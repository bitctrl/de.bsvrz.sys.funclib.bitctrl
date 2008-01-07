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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * Ein Parameterdatensatz, der die Daten des Verantwortlichen für die Baustelle beinhaltet. 
 * Der Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.baustellenVerantwortlicher".
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id$
 */
public class PdBaustellenVerantwortlicher extends
		AbstractParameterDatensatz<PdBaustellenVerantwortlicher.Daten> {

	/**
	 * Repr&auml;sentation der Daten des Baustellenverantwortlichen-Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Gieseler
	 * @version $Id$
	 */
	public static class Daten extends AbstractDatum {

		/** Name der verantwortlichen Firma. */
		private String _firma;
		
		/** Telefonnummer der verantwortlichen Firma. */
		private String _nameBaustellenVerantwortlicher;
		
		/** Name des Baustellenverantwortlichen. */
		private String _telefonBaustellenVerantwortlicher;
		
		/** Telefonnummer des Baustellenverantwortlichen. */
		private String _telefonFirma;
		
		/** Mobiltelefonnummer des Baustellenverantwortlichen. */
		private String _telefonMobilBaustellenVerantwortlicher;

		/**
		 * markiert die G&uuml;ltigkeit des Datensatzes.
		 */
		private boolean valid;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein leeres Datum.
		 */
		public Daten() {
			_firma = "";
			_nameBaustellenVerantwortlicher = "";
			_telefonBaustellenVerantwortlicher = "";
			_telefonFirma = "";
			_telefonMobilBaustellenVerantwortlicher = "";
			valid = false;
			setZeitstempel(0);
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion erzeugt ein Datum als Kopie des &uuml;bergebenen Datums.
		 * 
		 * @param daten
		 *            die Daten die kopiert werden sollen
		 */
		public Daten(Daten daten) {
			this._firma = daten._firma;
			this._nameBaustellenVerantwortlicher = daten._nameBaustellenVerantwortlicher;
			this._telefonBaustellenVerantwortlicher = daten._telefonBaustellenVerantwortlicher;
			this._telefonFirma = daten._telefonFirma;
			this._telefonMobilBaustellenVerantwortlicher = daten._telefonMobilBaustellenVerantwortlicher;
			
			this.valid = daten.valid;
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
		public Daten(ResultData result) {
			setZeitstempel(result.getDataTime());
			Data daten = result.getData();
			if (daten == null) {
				valid = false;
				_firma = "";
				_nameBaustellenVerantwortlicher = "";
				_telefonBaustellenVerantwortlicher = "";
				_telefonFirma = "";
				_telefonMobilBaustellenVerantwortlicher = "";
			} else {
				_firma = daten.getTextValue("Firma").getText();
				_nameBaustellenVerantwortlicher = daten.getTextValue("NameBaustellenVerantwortlicher").getText();
				_telefonFirma = daten.getTextValue("TelefonFirma").getText();
				_telefonBaustellenVerantwortlicher = daten.getTextValue("TelefonBaustellenVerantwortlicher").getText();
				_telefonMobilBaustellenVerantwortlicher = daten.getTextValue("TelefonMobilBaustellenVerantwortlicher").getText();
				valid = true;
			}
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum#clone()
		 */
		@Override
		public Datum clone() {
			return new Daten(this);
		}


		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#isValid()
		 */
		public boolean isValid() {
			return valid;
		}

		/**
		 *  liefert den Namen der Firma des Baustellenverantwortlichen
		 *  
		 * @return die Firma
		 */
		protected String getFirma() {
			return _firma;
		}

		/**
		 * Setzt den Namen der Firma des Baustellenverantwortlichen
		 * 
		 * @param firma zu setzende Firma
		 */
		protected void setFirma(String firma) {
			this._firma = firma;
		}

		/**
		 * liefert den Namen des Baustellenverantwortlichen
		 * 
		 * @return Name des BaustellenVerantwortlichen
		 */
		protected String getNameBaustellenVerantwortlicher() {
			return _nameBaustellenVerantwortlicher;
		}

		/**
		 *  setzt den Namen des Baustellenverantwortlichen
		 *  
		 * @param nameBaustellenVerantwortlicher zu setzender Baustellenverantwortlicher 
		 */
		protected void setNameBaustellenVerantwortlicher(
				String nameBaustellenVerantwortlicher) {
			this._nameBaustellenVerantwortlicher = nameBaustellenVerantwortlicher;
		}

		/**
		 * liefert die Telefonnummer des Baustellenverantwortlichen
		 * 
		 * @return Telefonnummer Baustellenverantwortlicher
		 */
		protected String getTelefonBaustellenVerantwortlicher() {
			return _telefonBaustellenVerantwortlicher;
		}

		/**
		 * setzt die Telefonnummer des Baustellenverantwortlichen
		 * 
		 * @param telefonBaustellenVerantwortlicher zu setzende Telefonnummer des Baustellenverantwortlichen
		 */
		protected void setTelefonBaustellenVerantwortlicher(
				String telefonBaustellenVerantwortlicher) {
			this._telefonBaustellenVerantwortlicher = telefonBaustellenVerantwortlicher;
		}

		/**
		 * liefert die Telefonnummer der Firma des Baustellenverantwortlichen
		 * 
		 * @return Telefonnummer der Firma des Baustellenverantwortlichen
		 */
		protected String getTelefonFirma() {
			return _telefonFirma;
		}

		/**
		 * setzt die Telefonnummer der Firma des Baustellenverantwortlichen
		 * 
		 * @param telefonFirma zu setzende Telefonnummer
		 */
		protected void setTelefonFirma(String telefonFirma) {
			this._telefonFirma = telefonFirma;
		}

		/**
		 * liefert die Mobil-Telefonnummer des Baustellenverantwortlichen
		 * 
		 * @return Mobil-Telefonnummer Baustellenverantwortlicher
		 */
		protected String getTelefonMobilBaustellenVerantwortlicher() {
			return _telefonMobilBaustellenVerantwortlicher;
		}

		/**
		 * setzt die Mobil-Telefonnummer desBaustellenverantwortlichen
		 * 
		 * @param telefonMobilBaustellenVerantwortlicher zu setzende Mobil-Telefonnummer Baustellenverantwortlicher
		 */
		protected void setTelefonMobilBaustellenVerantwortlicher(
				String telefonMobilBaustellenVerantwortlicher) {
			this._telefonMobilBaustellenVerantwortlicher = telefonMobilBaustellenVerantwortlicher;
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
	public PdBaustellenVerantwortlicher(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenVerantwortlicher");
		}
	}


	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#erzeugeDatum()
	 */
	public Daten erzeugeDatum() {
		return new Daten((ResultData) null);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getAttributGruppe()
	 */
	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
	}

	
	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten datum) {
		Data daten = erzeugeSendeCache();

		daten.getTextValue("Firma").setText(datum.getFirma());
		daten.getTextValue("TelefonFirma").setText(datum.getTelefonFirma());
		daten.getTextValue("NameBaustellenVerantwortlicher").setText(datum.getNameBaustellenVerantwortlicher());
		daten.getTextValue("TelefonBaustellenVerantwortlicher").setText(datum.getTelefonBaustellenVerantwortlicher());
		daten.getTextValue("TelefonMobilBaustellenVerantwortlicher").setText(datum.getTelefonMobilBaustellenVerantwortlicher());
		
		return daten;	
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.Data)
	 */
	public void setDaten(ResultData daten) {
		check(daten);
		setDatum(new Daten(daten));
	}
}
