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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.BaustellenStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.BaustellenVeranlasser;

/**
 * Ein Parameterdatensatz, die Eigenschaften einer Baustelle beinhaltet. Der
 * Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.baustellenEigenschaften".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdBaustellenEigenschaften extends
		AbstractParameterDatensatz<PdBaustellenEigenschaften.Daten> {

	/**
	 * Repräsentation der Daten des Baustelleneigenschaften-Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Peuker
	 * @version $Id: PdBaustellenEigenschaften.java 4508 2007-10-18 05:30:18Z
	 *          peuker $
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
		private BaustellenStatus status;
		/**
		 * Veranlasser der Baustelle (BIS-System oder VRZ). ("Veranlasser")
		 */
		private BaustellenVeranlasser veranlasser;

		/**
		 * markiert die Gültigkeit des Datensatzes.
		 */
		private boolean valid;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein en leeres Datum.
		 */
		public Daten() {
			status = BaustellenStatus.ENTWORFEN;
			veranlasser = BaustellenVeranlasser.UNDEFINIERT;
			restKapazitaet = 0;
			valid = false;
			setZeitstempel(0);
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion erzeugt ein Datum als Kopie des übergebenen Datums.
		 * 
		 * @param daten
		 *            die Daten die kopiert werden sollen
		 */
		public Daten(Daten daten) {
			this.restKapazitaet = daten.restKapazitaet;
			this.status = daten.status;
			this.veranlasser = daten.veranlasser;
			this.valid = daten.valid;
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
		public Daten(ResultData result) {
			setZeitstempel(result.getDataTime());
			Data daten = result.getData();
			if (daten == null) {
				valid = false;
				status = BaustellenStatus.ENTWORFEN;
				veranlasser = BaustellenVeranlasser.UNDEFINIERT;
				restKapazitaet = 0;
			} else {
				status = BaustellenStatus.getStatus(daten.getUnscaledValue(
						"Status").intValue());
				restKapazitaet = daten.getScaledValue("RestKapazität")
						.longValue();
				veranlasser = BaustellenVeranlasser.getVeranlasser(daten
						.getUnscaledValue("Veranlasser").intValue());
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
		 * liefert den Status der Baustelle.
		 * 
		 * @return der Status
		 */
		public BaustellenStatus getStatus() {
			return status;
		}


		/**
		 * setzt den Status der Baustelle.
		 * @param _status neuer Status
		 * 
		 */
		public void setStatus(BaustellenStatus _status) {
			status = _status;
		}

		/**
		 * setzt den Veranlasser der Baustelle.
		 * @param _veranlasser neuer Veranlasser
		 * 
		 */
		public void setVeranlasser(BaustellenVeranlasser _veranlasser) {
			veranlasser = _veranlasser;
		}

		/**
		 * liefert die Restkapazität der Baustelle.
		 * @param _restKapazitaet die neue Restkapazit&auml;t
		 * 
		 */
		public void setRestKapazitaet(long _restKapazitaet) {
			restKapazitaet = _restKapazitaet;
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#isValid()
		 */
		public boolean isValid() {
			return valid;
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
	public PdBaustellenEigenschaften(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenEigenschaften");
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

		daten.getUnscaledValue("Status").set(datum.getStatus().ordinal());
		daten.getScaledValue("RestKapazität").set(datum.getRestKapazitaet());
		daten.getUnscaledValue("Veranlasser").set(datum.getVeranlasser().ordinal());
		
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
