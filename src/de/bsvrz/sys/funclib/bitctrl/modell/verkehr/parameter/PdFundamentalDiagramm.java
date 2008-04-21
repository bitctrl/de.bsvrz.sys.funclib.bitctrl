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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StoerfallIndikator;

/**
 * Ein Parameterdatensatz, die Parameterdaten des Fundamentaldiagramms eines
 * Messquerschnitts beinhaltet "atg.fundamentalDiagramm".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdFundamentalDiagramm extends
		AbstractParameterDatensatz<PdFundamentalDiagramm.Daten> {

	/**
	 * Die Repräsentation der Daten des Situationseigenschaften-Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Peuker
	 * @version $Id: PdSituationsEigenschaften.java 4508 2007-10-18 05:30:18Z
	 *          peuker $
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Q0 Verkehrsmenge des Fundamentaldiagramms.
		 */
		private Integer q0;
		/**
		 * Maximale Dichte des Fundamentaldiagramms.
		 */
		private Short k0;
		/**
		 * V0-Geschwindigkeit des Fundamentaldiagramms.
		 */
		private Short v0;
		/**
		 * Freie Geschwindigkeit des Fundamentaldiagramms.
		 */
		private Short vFrei;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standard-Konstruktor zum Erstellen eines leeren Datensatzes.
		 * 
		 */
		Daten() {
			setZeitstempel(0);
			q0 = null;
			k0 = null;
			v0 = null;
			vFrei = null;
		}

		/**
		 * Konstruktor zu Erstellen einer Kopie des übergebenen Datums.
		 * 
		 * @param daten
		 *            das zu kopierende Datum
		 */
		Daten(final Daten daten) {
			this.datenStatus = daten.datenStatus;
			q0 = daten.q0;
			k0 = daten.k0;
			v0 = daten.v0;
			vFrei = daten.vFrei;
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion erzeugt eine Instnz des Datums und füllt dieses mit dem
		 * Inhalt des übergebenen Datenverteiler-Datensatzes.
		 * 
		 * @param result
		 *            die vom Datenverteiler empfangenen Dtaen
		 */
		Daten(final ResultData result) {
			setZeitstempel(result.getDataTime());
			Data daten = result.getData();
			if (daten != null) {
				q0 = daten.getUnscaledValue("Q0").intValue();
				k0 = daten.getUnscaledValue("K0").shortValue();
				v0 = daten.getUnscaledValue("V0").shortValue();
				vFrei = daten.getUnscaledValue("VFrei").shortValue();
			} else {
				q0 = null;
				k0 = null;
				v0 = null;
				vFrei = null;
			}
			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum#clone()
		 */
		@Override
		public Daten clone() {
			return new Daten(this);
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#getDatenStatus()
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert den K0-Wert.
		 * 
		 * @return the k0
		 */
		public Short getK0() {
			return k0;
		}

		/**
		 * liefert den Q0-Wert.
		 * 
		 * @return the q0
		 */
		public Integer getQ0() {
			return q0;
		}

		/**
		 * liefert den V0-Wert.
		 * 
		 * @return the v0
		 */
		public Short getV0() {
			return v0;
		}

		/**
		 * liefert den VFrei-Wert.
		 * 
		 * @return the vFrei
		 */
		public Short getVFrei() {
			return vFrei;
		}

		/**
		 * setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(Status neuerStatus) {
			this.datenStatus = neuerStatus;
		}

		/**
		 * setzt den K0-Wert.
		 * 
		 * @param k0
		 *            the k0 to set
		 */
		public void setK0(Short k0) {
			this.k0 = k0;
		}

		/**
		 * setzt den Q0-Wert.
		 * 
		 * @param q0
		 *            the q0 to set
		 */
		public void setQ0(Integer q0) {
			this.q0 = q0;
		}

		/**
		 * setzt den V0-Wert.
		 * 
		 * @param v0
		 *            the v0 to set
		 */
		public void setV0(Short v0) {
			this.v0 = v0;
		}

		/**
		 * setzt den VFrei-Wert.
		 * 
		 * @param frei
		 *            the vFrei to set
		 */
		public void setVFrei(Short frei) {
			vFrei = frei;
		}
	}

	/**
	 * die Attributgruppe für den Zugriff auf die Parameter.
	 */
	private static AttributeGroup attributGruppe;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz des Parameterdatensatzes auf der Basis
	 * des übergebenen Störfallindikators.
	 * 
	 * @param indikator
	 *            das Systemobjekt
	 */
	public PdFundamentalDiagramm(final StoerfallIndikator indikator) {
		super(indikator);
		if (attributGruppe == null) {
			attributGruppe = indikator.getSystemObject().getDataModel()
					.getAttributeGroup("atg.fundamentalDiagramm");
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#erzeugeDatum()
	 */
	public Daten erzeugeDatum() {
		return new Daten();
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
	protected Data konvertiere(final Daten datum) {
		Data result = erzeugeSendeCache();
		result.getUnscaledValue("Q0").set(datum.getQ0());
		result.getUnscaledValue("K0").set(datum.getK0());
		result.getUnscaledValue("V0").set(datum.getV0());
		result.getUnscaledValue("VFrei").set(datum.getVFrei());

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.ResultData)
	 */
	public void setDaten(final ResultData result) {
		check(result);
		Daten daten = new Daten(result);
		setDatum(daten);
		daten.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		fireDatensatzAktualisiert(daten.clone());

	}

}
