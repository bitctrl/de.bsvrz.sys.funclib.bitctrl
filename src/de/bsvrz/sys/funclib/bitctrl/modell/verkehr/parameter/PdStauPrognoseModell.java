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

import java.util.ArrayList;
import java.util.Collection;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter.PdStauBestimmungModell.Daten;

/**
 * Ein Parameterdatensatz, der die an einem VerkehrsmodellNetz festgelegten
 * Parameter für die Stauprognose enthält. Der Datensatz repräsentiert die Daten
 * einer Attributgruppe "atg.stauPrognoseModell".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdStauPrognoseModell extends
		AbstractParameterDatensatz<PdStauPrognoseModell.Daten> {

	/**
	 * Definition der Attributnamen für den Zugriff auf den Parameterdatensatz.
	 * 
	 * @author BitCtrl Systems GmbH, Uwe Peuker
	 * @version $Id$
	 */
	private static final class Att {
		/** Attributname für den Prognosehorizont. */
		public static final String PROGNOSE_HORIZONT = "PrognoseHorizont";

		/** Attributname für das Aktualisierungsintervall. */
		public static final String AKTUALISIERUNGS_INTERVALL = "AktualisierungsIntervall";

		/** Attributname für den FaktorQ0. */
		public static final String FAKTOR_Q0 = "FaktorQ0";

		/** Attributname für die Länge eines Pkw. */
		public static final String LAENGE_PKW = "LängePkw";

		/** Attributname für die Dämpfungszeit. */
		public static final String DAEMPFUNGSZEIT = "DämpfungsZeit";

		/** privater Konstruktor. */
		private Att() {
			super();
		}
	}

	/**
	 * Repräsentation der Daten des Baustelleneigenschaften-Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Peuker
	 * @version $Id: PdBaustellenEigenschaften.java 4508 2007-10-18 05:30:18Z
	 *          peuker $
	 */
	public static class Daten extends AbstractDatum {

		/** Prognosehorizont der Stauverlaufsprognose. */
		private long prognoseHorizont;

		/** Anzahl der Prognoseiterationen je Zyklus der Stauobjektbestimmung. */
		private long aktualisierungsIntervall;

		/** Faktor für die Anpassung von Q0 aus dem Fundamentaldiagramm. */
		private double faktorQ0;

		/** Strecke, die ein Pkw im Stau beansprucht. */
		private long laengePkw;

		/**
		 * Zeit, die bei der Bestimmung der Verkehrsstärke von Anschlussstellen
		 * im Stau fü die lineare Dämpfung vom aktuellen Messwert zur
		 * Prognoseganglinie verwendet wird.
		 */
		private long daempfungsZeit;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein en leeres Datum.
		 */
		public Daten() {
			prognoseHorizont = 0;
			aktualisierungsIntervall = 0;
			faktorQ0 = 0.0;
			laengePkw = 0;
			daempfungsZeit = 0;
			datenStatus = Datum.Status.UNDEFINIERT;
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
			this();
			if (daten != null) {
				prognoseHorizont = daten.prognoseHorizont;
				aktualisierungsIntervall = daten.aktualisierungsIntervall;
				faktorQ0 = daten.faktorQ0;
				laengePkw = daten.laengePkw;
				daempfungsZeit = daten.daempfungsZeit;
				this.datenStatus = daten.datenStatus;
				setZeitstempel(daten.getZeitstempel());
			}
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
			this();
			setZeitstempel(result.getDataTime());
			Data daten = result.getData();

			if (daten != null) {
				prognoseHorizont = daten.getTimeValue(Att.PROGNOSE_HORIZONT)
						.getMillis();
				aktualisierungsIntervall = daten.getUnscaledValue(
						Att.AKTUALISIERUNGS_INTERVALL).longValue();
				faktorQ0 = daten.getScaledValue(Att.FAKTOR_Q0).doubleValue();
				laengePkw = daten.getUnscaledValue(Att.LAENGE_PKW).longValue();
				daempfungsZeit = daten.getTimeValue(Att.DAEMPFUNGSZEIT)
						.getMillis();
			}

			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		/** {@inheritDoc} */
		@Override
		public Daten clone() {
			return new Daten(this);
		}

		public long getAktualisierungsIntervall() {
			return aktualisierungsIntervall;
		}

		public long getDaempfungsZeit() {
			return daempfungsZeit;
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#getDatenStatus()
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		public double getFaktorQ0() {
			return faktorQ0;
		}

		public long getLaengePkw() {
			return laengePkw;
		}

		public long getPrognoseHorizont() {
			return prognoseHorizont;
		}

		public void setAktualisierungsIntervall(long aktualisierungsIntervall) {
			this.aktualisierungsIntervall = aktualisierungsIntervall;
		}

		public void setDaempfungsZeit(long daempfungsZeit) {
			this.daempfungsZeit = daempfungsZeit;
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

		public void setFaktorQ0(double faktorQ0) {
			this.faktorQ0 = faktorQ0;
		}

		public void setLaengePkw(long laengePkw) {
			this.laengePkw = laengePkw;
		}

		public void setPrognoseHorizont(long prognoseHorizont) {
			this.prognoseHorizont = prognoseHorizont;
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
	public PdStauPrognoseModell(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.stauPrognoseModell");
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
	protected Data konvertiere(Daten datum) {
		Data daten = erzeugeSendeCache();

		daten.getTimeValue(Att.PROGNOSE_HORIZONT).setMillis(
				datum.getPrognoseHorizont());
		daten.getUnscaledValue(Att.AKTUALISIERUNGS_INTERVALL).set(
				datum.getAktualisierungsIntervall());
		daten.getScaledValue(Att.FAKTOR_Q0).set(datum.getFaktorQ0());
		daten.getUnscaledValue(Att.LAENGE_PKW).set(datum.getLaengePkw());
		daten.getTimeValue(Att.DAEMPFUNGSZEIT).setMillis(
				datum.getDaempfungsZeit());

		return daten;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(ResultData)
	 */
	public void setDaten(ResultData daten) {
		check(daten);
		Daten datum = new Daten(daten);
		setDatum(datum);
		datum.setDatenStatus(Datum.Status.getStatus(daten.getDataState()));
		fireDatensatzAktualisiert(datum.clone());

	}
}
