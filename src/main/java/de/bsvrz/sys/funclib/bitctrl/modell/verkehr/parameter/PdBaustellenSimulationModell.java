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
 * Ein Parameterdatensatz, der die an einem VerkehrsmodellNetz festgelegten
 * Parameter für die Baustellensimulation enthält. Der Datensatz repräsentiert
 * die Daten einer Attributgruppe "atg.baustellenSimulationModell".
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class PdBaustellenSimulationModell
extends AbstractParameterDatensatz<PdBaustellenSimulationModell.Daten> {

	/**
	 * Definition der Attributnamen für den Zugriff auf den Parameterdatensatz.
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
	 */
	public static class Daten extends AbstractDatum {

		/** Prognosehorizont in Tagen. */
		private long prognoseHorizont;

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
			prognoseHorizont = 0;
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
		public Daten(final Daten daten) {
			this();
			if (daten != null) {
				prognoseHorizont = daten.prognoseHorizont;
				aktualisierungsIntervall = daten.aktualisierungsIntervall;
				iterationsSchrittweite = daten.iterationsSchrittweite;
				faktorQ0 = daten.faktorQ0;
				laengePkw = daten.laengePkw;
				stauBeginn = daten.stauBeginn;
				stauAufhebung = daten.stauAufhebung;
				datenStatus = daten.datenStatus;
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
		public Daten(final ResultData result) {
			this();
			setZeitstempel(result.getDataTime());
			final Data daten = result.getData();

			if (daten != null) {
				prognoseHorizont = daten.getUnscaledValue(Att.PROGNOSE_HORIZONT)
						.longValue();
				aktualisierungsIntervall = daten
						.getUnscaledValue(Att.AKTUALISIERUNGS_INTERVALL)
						.longValue();
				iterationsSchrittweite = daten
						.getTimeValue(Att.ITERATIONS_SCHRITTWEITE).getMillis();
				faktorQ0 = daten.getScaledValue(Att.FAKTOR_Q0).doubleValue();
				laengePkw = daten.getUnscaledValue(Att.LAENGE_PKW).longValue();
				stauBeginn = daten.getUnscaledValue(Att.LAENGE_STAUBEGINN)
						.longValue();
				stauAufhebung = daten.getUnscaledValue(Att.LAENGE_STAUAUFHEBUNG)
						.longValue();
			}

			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		@Override
		public Daten clone() {
			return new Daten(this);
		}

		/**
		 * liefert das Aktualisierungsintervall in Tagen, mit dem die Simulation
		 * des Verhaltens der Baustellen innerhalb des parametrierten Netzes
		 * erfolgend soll.
		 *
		 * @return das Aktualisierungsintervall in Tagen
		 */
		public long getAktualisierungsIntervall() {
			return aktualisierungsIntervall;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert den Faktor mit dem der Q0-Wert des Fundamentaldiagramms eines
		 * MQ multipliziert wird, wenn dieses als Basis zur Berechnung der
		 * Kapazität einer Baustelle im Bereich, der Ungültigkeit innerhalb des
		 * Prognosezeitraums verwendet wird.
		 *
		 * @return der Faktor
		 */
		public double getFaktorQ0() {
			return faktorQ0;
		}

		/**
		 * liefert die Schrittweite, mit der innerhalb des Prognosezeitraums
		 * eine Simulation des Stauverlaufs erfolgen soll in Millisekunden.
		 *
		 * @return die Schrittweite
		 */
		public long getIterationsSchrittweite() {
			return iterationsSchrittweite;
		}

		/**
		 * liefert die normierte Länge eines Fahrzeiges, die dieses im Stau
		 * beansprucht in Metern.
		 *
		 * @return die Länge
		 */
		public long getLaengePkw() {
			return laengePkw;
		}

		/**
		 * liefert den Prognosehorizont der Baustellensimulation in Tagen.
		 *
		 * @return den Horizont
		 */
		public long getProgoseHorizont() {
			return prognoseHorizont;
		}

		/**
		 * liefert die Länge, ab der eine Aufhebung eines Stauobjekts innerhalb
		 * der Simulation einer Baustelle angenommen wird, in Metern.
		 *
		 * @return die Länge
		 */
		public long getStauAufhebung() {
			return stauAufhebung;
		}

		/**
		 * liefert die Länge, ab der ein Stauobjekts innerhalb der Simulation
		 * einer Baustelle gebildet wird, in Metern.
		 *
		 * @return die Länge
		 */
		public long getStauBeginn() {
			return stauBeginn;
		}

		/**
		 * setzt das Aktualisierungsintervall, mit dem die zyklische Simulation
		 * der innerhalb des parametrierten Netzes angelegten Baustellen
		 * erfolgen soll, in Tagen.
		 *
		 * @param aktualisierungsIntervall
		 *            das Intervall in Tagen
		 */
		public void setAktualisierungsIntervall(
				final long aktualisierungsIntervall) {
			this.aktualisierungsIntervall = aktualisierungsIntervall;
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
		 * setzt den Faktor für die Verwendung des Q0-Wertes aus einem
		 * Fundamentaldiagramm.
		 *
		 * @param faktorQ0
		 *            der Faktor
		 */
		public void setFaktorQ0(final double faktorQ0) {
			this.faktorQ0 = faktorQ0;
		}

		/**
		 * setzt die Schrittweite, mit innerhalb der Baustellensimulation das
		 * Verhalten von Stauobjekten analysiert wird, in Millisekunden.
		 *
		 * @param iterationsSchrittweite
		 *            die Schrittweite in ms
		 */
		public void setIterationsSchrittweite(
				final long iterationsSchrittweite) {
			this.iterationsSchrittweite = iterationsSchrittweite;
		}

		/**
		 * setzt die Länge, die von einen Pkw innerhalb eines Staus belegt
		 * werden würde, in Metern.
		 *
		 * @param laengePkw
		 *            die Länge
		 */
		public void setLaengePkw(final long laengePkw) {
			this.laengePkw = laengePkw;
		}

		/**
		 * setzt den Prognosehorizont für die Ausführung einer
		 * Baustellensimulation in Tagen.
		 *
		 * @param horizont
		 *            der Horizont in Tagen
		 */
		public void setProgoseHorizont(final long horizont) {
			prognoseHorizont = horizont;
		}

		/**
		 * setzt die Länge, bei der ein Stauobjekt innerhalb der Simulation
		 * aufgelöst werden soll, in Metern.
		 *
		 * @param stauAufhebung
		 *            die Länge
		 */
		public void setStauAufhebung(final long stauAufhebung) {
			this.stauAufhebung = stauAufhebung;
		}

		/**
		 * setzt die Länge, bei der ein Stauobjekt innerhalb der Simulation
		 * gebildet werden soll, in Metern.
		 *
		 * @param stauBeginn
		 *            die Länge
		 */
		public void setStauBeginn(final long stauBeginn) {
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
	public PdBaustellenSimulationModell(final SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenSimulationModell");
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

		daten.getUnscaledValue(Att.PROGNOSE_HORIZONT)
		.set(datum.getProgoseHorizont());
		daten.getUnscaledValue(Att.AKTUALISIERUNGS_INTERVALL)
		.set(datum.getAktualisierungsIntervall());
		daten.getTimeValue(Att.ITERATIONS_SCHRITTWEITE)
		.setMillis(datum.getIterationsSchrittweite());
		daten.getScaledValue(Att.FAKTOR_Q0).set(datum.getFaktorQ0());
		daten.getUnscaledValue(Att.LAENGE_PKW).set(datum.getLaengePkw());
		daten.getUnscaledValue(Att.LAENGE_STAUBEGINN)
		.set(datum.getStauBeginn());
		daten.getUnscaledValue(Att.LAENGE_STAUAUFHEBUNG)
		.set(datum.getStauAufhebung());

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
