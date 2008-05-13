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

/**
 * Ein Parameterdatensatz, der die an einem VerkehrsmodellNetz festgelegten
 * Parameter für die Baustellensimulation enthält. Der Datensatz repräsentiert
 * die Daten einer Attributgruppe "atg.baustellenSimulationModell".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdBaustellenSimulationModell extends
		AbstractParameterDatensatz<PdBaustellenSimulationModell.Daten> {

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

		/** Attributname für die Iterationsschrittweite. */
		public static final String ITERATIONS_SCHRITTWEITE = "IterationsSchrittweite";

		/** Attributname für den Faktor Q0. */
		public static final String FAKTOR_Q0 = "FaktorQ0";

		/** Attributname für die Länge eines Pkw. */
		public static final String LAENGE_PKW = "LängePkw";

		/** Attributname für die Länge des Staubeginns. */
		public static final String LAENGE_STAUBEGINN = "LängeStauBeginn";

		/** Attributname für die Länge der Stauaufhebung. */
		public static final String LAENGE_STAUAUFHEBUNG = "LängeStauAufhebung";

		/** privater Konstruktor. */
		private Att() {
			super();
		}
	}

	/**
	 * Repräsentation der Daten des Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Peuker
	 * @version $Id$
	 */
	public static class Daten extends AbstractDatum {

		/** Prognosehorizont in Tagen. */
		private long progoseHorizont;

		/** Aktualisierungsintervall in Tagen. */
		private long aktualisierungsIntervall;

		/** Iterationsschrittweite in Millisekunden. */
		private long iterationsSchrittweite;

		/** Faktor Q0 als Prozent. */
		private double faktorQ0;

		/** Länge eines Pkw in Metern. */
		private long laengePkw;

		/** Länge des Staubeginns in Metern. */
		private long stauBeginn;

		/** Länge der Stauaufhebung in Metern. */
		private long stauAufhebung;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein en leeres Datum.
		 */
		public Daten() {
			progoseHorizont = 0;
			aktualisierungsIntervall = 0;
			iterationsSchrittweite = 0;
			faktorQ0 = 0;
			laengePkw = 0;
			stauBeginn = 0;
			stauAufhebung = 0;
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
				progoseHorizont = daten.progoseHorizont;
				aktualisierungsIntervall = daten.aktualisierungsIntervall;
				iterationsSchrittweite = daten.iterationsSchrittweite;
				faktorQ0 = daten.faktorQ0;
				laengePkw = daten.laengePkw;
				stauBeginn = daten.stauBeginn;
				stauAufhebung = daten.stauAufhebung;
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
				progoseHorizont = daten.getUnscaledValue(Att.PROGNOSE_HORIZONT)
						.longValue();
				aktualisierungsIntervall = daten.getUnscaledValue(
						Att.AKTUALISIERUNGS_INTERVALL).longValue();
				iterationsSchrittweite = daten.getTimeValue(
						Att.ITERATIONS_SCHRITTWEITE).getMillis();
				faktorQ0 = daten.getScaledValue(Att.FAKTOR_Q0).doubleValue();
				laengePkw = daten.getUnscaledValue(Att.LAENGE_PKW).longValue();
				stauBeginn = daten.getUnscaledValue(Att.LAENGE_STAUBEGINN)
						.longValue();
				stauAufhebung = daten
						.getUnscaledValue(Att.LAENGE_STAUAUFHEBUNG).longValue();
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

		public long getAktualisierungsIntervall() {
			return aktualisierungsIntervall;
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

		public long getIterationsSchrittweite() {
			return iterationsSchrittweite;
		}

		public long getLaengePkw() {
			return laengePkw;
		}

		public long getProgoseHorizont() {
			return progoseHorizont;
		}

		public long getStauAufhebung() {
			return stauAufhebung;
		}

		public long getStauBeginn() {
			return stauBeginn;
		}

		public void setAktualisierungsIntervall(long aktualisierungsIntervall) {
			this.aktualisierungsIntervall = aktualisierungsIntervall;
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

		public void setIterationsSchrittweite(long iterationsSchrittweite) {
			this.iterationsSchrittweite = iterationsSchrittweite;
		}

		public void setLaengePkw(long laengePkw) {
			this.laengePkw = laengePkw;
		}

		public void setProgoseHorizont(long progoseHorizont) {
			this.progoseHorizont = progoseHorizont;
		}

		public void setStauAufhebung(long stauAufhebung) {
			this.stauAufhebung = stauAufhebung;
		}

		public void setStauBeginn(long stauBeginn) {
			this.stauBeginn = stauBeginn;
		}
	}

	/**
	 * die Attributgruppe, in der die Eigenschaften enthalten sind.
	 */
	private static AttributeGroup attributGruppe;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeigt eine Instanz des Parameterdatensatzes.
	 * 
	 * @param objekt
	 *            das der Baustelle zugrundliegende Systemobjekt.
	 */
	public PdBaustellenSimulationModell(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenSimulationModell");
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

		daten.getUnscaledValue(Att.PROGNOSE_HORIZONT).set(
				datum.getProgoseHorizont());
		daten.getUnscaledValue(Att.AKTUALISIERUNGS_INTERVALL).set(
				datum.getAktualisierungsIntervall());
		daten.getTimeValue(Att.ITERATIONS_SCHRITTWEITE).setMillis(
				datum.getIterationsSchrittweite());
		daten.getScaledValue(Att.FAKTOR_Q0).set(datum.getFaktorQ0());
		daten.getUnscaledValue(Att.LAENGE_PKW).set(datum.getLaengePkw());
		daten.getUnscaledValue(Att.LAENGE_STAUBEGINN)
				.set(datum.getStauBeginn());
		daten.getUnscaledValue(Att.LAENGE_STAUAUFHEBUNG).set(
				datum.getStauAufhebung());

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
