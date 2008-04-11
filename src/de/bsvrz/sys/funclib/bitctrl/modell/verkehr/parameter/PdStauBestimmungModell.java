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
 * Parameter für die Stauobjektbestimmung enthält. Der Datensatz repräsentiert
 * die Daten einer Attributgruppe "atg.stauBestimmmungModell".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdStauBestimmungModell extends
		AbstractParameterDatensatz<PdStauBestimmungModell.Daten> {

	/**
	 * Definition der Attributnamen für den Zugriff auf den Parameterdatensatz.
	 * 
	 * @author BitCtrl Systems GmbH, Uwe Peuker
	 * @version $Id$
	 */
	private static final class Att {
		/** Attributname für die Zykluszeit. */
		public static final String ZYKLUSZEIT = "Zykluszeit";
		/** Attributname für die Liste der Störfallverfahren. */
		public static final String SIVERFAHREN = "SIVerfahren";

		/** Attributname für die Anfangslänge. */
		public static final String ANFANGSLAENGE = "Anfangslaenge";

		/** Attributname für die Stronabverlängerung. */
		public static final String STROMABVERLAENGERUNG = "StromabVerlaengerung";

		/** Attributname für den maximalen SI-Abstand. */
		public static final String MAXSIABSTAND = "MaxSIAbstand";

		/** Attributname für die Teilungslänge. */
		public static final String TEILUNGSLAENGE = "Teilungslaenge";

		/** Attributname für die Teilungsverzögerung. */
		public static final String TEILUNGSVERZOEGERUNG = "Teilungsverzoegerung";

		/** Attributname für den A1-Wert der Formeln der Prognoseberechnung. */
		public static final String PROGNOSELAENGE_A1 = "PrognoselaengeA1";

		/** Attributname für den A2-Wert der Formeln der Prognoseberechnung. */
		public static final String PROGNOSELAENGE_A2 = "PrognoselaengeA2";

		/** Attributname für den A3-Wert der Formeln der Prognoseberechnung. */
		public static final String PROGNOSELAENGE_A3 = "PrognoselaengeA3";

		/** Attributname für die minimlae SI-Güte. */
		public static final String MINSIGUETE = "MinSIGuete";

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

		/** Zykluszeit der Stauobjektbestimmung in Sekunden ("Zykluszeit"). */
		private long zyklusZeit;

		/**
		 * Liste der Verfahren deren Störfallindikatoren berücksichtigt werden
		 * sollen ("SIVerfahren").
		 */
		private Collection<Aspect> siVerfahren = new ArrayList<Aspect>();

		/**
		 * Angenommene Anfangslänge des gestauten Bereichs an einem lokalen
		 * Störfallindikator pro Minute des jeweiligen Erfassungszyklus.
		 * ("Anfangslaenge")
		 */
		private long anfangsLaenge;

		/**
		 * Angenommene Verlängerung der Anfangslänge bei gestautem
		 * Infrastrukturobjekt i an einem lokalen Störfallindikator.
		 * ("StromabVerlaengerung")
		 */
		private long stromAbVerlaengerung;

		/**
		 * Maximalabstand für die Zusammenfassung von gestauten Bereichen
		 * ("MaxSIAbstand").
		 */
		private long maxSiAbstand;

		/**
		 * Mindestlänge eines ungestauten Bereiches innerhalb eines Stauobjektes
		 * für die sofortige Aufteilung in zwei Stauobjekte ("Teilungslaenge").
		 */
		private long teilungsLaenge;

		/**
		 * Zeitverzögerung für die Aufteilung eines Stauobjekts in dem die Länge
		 * eines inneren ungestauten Bereiches den Wert "Teilungslänge" noch
		 * nicht überschreitet ("Teilungsverzoegerung").
		 */
		private long teilungsverzoegerung;

		/**
		 * Parameter a1 aus der Gleichung für die Berechnung der maximalen
		 * Verlängerung eines Stauobjektes durch die Prognose
		 * ("PrognoselaengeA1").
		 */
		private long prognoselaengeA1;

		/**
		 * Parameter a2 aus der Gleichung für die Berechnung der maximalen
		 * Verlängerung eines Stauobjektes durch die Prognose
		 * ("PrognoselaengeA2").
		 */
		private long prognoselaengeA2;

		/**
		 * Parameter a3 aus der Gleichung für die Berechnung der maximalen
		 * Verlängerung eines Stauobjektes durch die Prognose
		 * ("PrognoselaengeA3").
		 */
		private long prognoselaengeA3;

		/**
		 * Minimale Güte von zu berücksichtigenden Störfallindikatoren
		 * ("MinSIGuete").
		 */
		private double minSIGuete;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standardkonstruktor.<br>
		 * Die Funktion erzeugt ein en leeres Datum.
		 */
		public Daten() {
			zyklusZeit = 0;
			anfangsLaenge = 0;
			stromAbVerlaengerung = 0;
			maxSiAbstand = 0;
			teilungsLaenge = 0;
			teilungsverzoegerung = 0;
			prognoselaengeA1 = 0;
			prognoselaengeA2 = 0;
			prognoselaengeA3 = 0;
			minSIGuete = 0.0;
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
				zyklusZeit = daten.zyklusZeit;
				siVerfahren.addAll(daten.siVerfahren);
				anfangsLaenge = daten.anfangsLaenge;
				stromAbVerlaengerung = daten.stromAbVerlaengerung;
				maxSiAbstand = daten.maxSiAbstand;
				teilungsLaenge = daten.teilungsLaenge;
				teilungsverzoegerung = daten.teilungsverzoegerung;
				prognoselaengeA1 = daten.prognoselaengeA1;
				prognoselaengeA2 = daten.prognoselaengeA2;
				prognoselaengeA3 = daten.prognoselaengeA3;
				minSIGuete = daten.minSIGuete;
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
				zyklusZeit = daten.getUnscaledValue(Att.ZYKLUSZEIT).longValue();
				Data.Array verfahrensArray = daten.getArray(Att.SIVERFAHREN);
				for (int idx = 0; idx < verfahrensArray.getLength(); idx++) {
					siVerfahren.add((Aspect) verfahrensArray.getReferenceValue(
							idx).getSystemObject());
				}
				anfangsLaenge = daten.getUnscaledValue(Att.ANFANGSLAENGE)
						.longValue();
				stromAbVerlaengerung = daten.getUnscaledValue(
						Att.STROMABVERLAENGERUNG).longValue();
				maxSiAbstand = daten.getUnscaledValue(Att.MAXSIABSTAND)
						.longValue();
				teilungsLaenge = daten.getUnscaledValue(Att.TEILUNGSLAENGE)
						.longValue();
				teilungsverzoegerung = daten.getUnscaledValue(
						Att.TEILUNGSVERZOEGERUNG).longValue();
				prognoselaengeA1 = daten
						.getUnscaledValue(Att.PROGNOSELAENGE_A1).longValue();
				prognoselaengeA2 = daten
						.getUnscaledValue(Att.PROGNOSELAENGE_A2).longValue();
				prognoselaengeA3 = daten
						.getUnscaledValue(Att.PROGNOSELAENGE_A3).longValue();
				minSIGuete = daten.getScaledValue(Att.MINSIGUETE).doubleValue();
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

		public long getAnfangsLaenge() {
			return anfangsLaenge;
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#getDatenStatus()
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		public long getMaxSiAbstand() {
			return maxSiAbstand;
		}

		public double getMinSIGuete() {
			return minSIGuete;
		}

		public long getPrognoselaengeA1() {
			return prognoselaengeA1;
		}

		public long getPrognoselaengeA2() {
			return prognoselaengeA2;
		}

		public long getPrognoselaengeA3() {
			return prognoselaengeA3;
		}

		public Collection<Aspect> getSiVerfahren() {
			return siVerfahren;
		}

		public long getStromAbVerlaengerung() {
			return stromAbVerlaengerung;
		}

		public long getTeilungsLaenge() {
			return teilungsLaenge;
		}

		public long getTeilungsverzoegerung() {
			return teilungsverzoegerung;
		}

		public long getZyklusZeit() {
			return zyklusZeit;
		}

		public void setAnfangsLaenge(long anfangsLaenge) {
			this.anfangsLaenge = anfangsLaenge;
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

		public void setMaxSiAbstand(long maxSiAbstand) {
			this.maxSiAbstand = maxSiAbstand;
		}

		public void setMinSIGuete(double minSIGuete) {
			this.minSIGuete = minSIGuete;
		}

		public void setPrognoselaengeA1(long prognoselaengeA1) {
			this.prognoselaengeA1 = prognoselaengeA1;
		}

		public void setPrognoselaengeA2(long prognoselaengeA2) {
			this.prognoselaengeA2 = prognoselaengeA2;
		}

		public void setPrognoselaengeA3(long prognoselaengeA3) {
			this.prognoselaengeA3 = prognoselaengeA3;
		}

		public void setSiVerfahren(Aspect... siVerfahren) {
			this.siVerfahren.clear();
			if (siVerfahren != null) {
				for (Aspect asp : siVerfahren) {
					this.siVerfahren.add(asp);
				}
			}
		}

		public void setStromAbVerlaengerung(long stromAbVerlaengerung) {
			this.stromAbVerlaengerung = stromAbVerlaengerung;
		}

		public void setTeilungsLaenge(long teilungsLaenge) {
			this.teilungsLaenge = teilungsLaenge;
		}

		public void setTeilungsverzoegerung(long teilungsverzoegerung) {
			this.teilungsverzoegerung = teilungsverzoegerung;
		}

		public void setZyklusZeit(long zyklusZeit) {
			this.zyklusZeit = zyklusZeit;
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
	public PdStauBestimmungModell(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.stauBestimmungModell");
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

		daten.getUnscaledValue(Att.ZYKLUSZEIT).set(datum.getZyklusZeit());

		Data.Array verfahrensArray = daten.getArray(Att.SIVERFAHREN);
		verfahrensArray.setLength(datum.getSiVerfahren().size());
		int idx = 0;
		for (Aspect asp : datum.getSiVerfahren()) {
			verfahrensArray.getReferenceValue(idx++).setSystemObject(asp);
		}
		daten.getUnscaledValue(Att.ANFANGSLAENGE).set(datum.getAnfangsLaenge());
		daten.getUnscaledValue(Att.STROMABVERLAENGERUNG).set(
				datum.getStromAbVerlaengerung());
		daten.getUnscaledValue(Att.MAXSIABSTAND).set(datum.getMaxSiAbstand());
		daten.getUnscaledValue(Att.TEILUNGSLAENGE).set(
				datum.getTeilungsLaenge());
		daten.getUnscaledValue(Att.TEILUNGSVERZOEGERUNG).set(
				datum.getTeilungsverzoegerung());
		daten.getUnscaledValue(Att.PROGNOSELAENGE_A1).set(
				datum.getPrognoselaengeA1());
		daten.getUnscaledValue(Att.PROGNOSELAENGE_A2).set(
				datum.getPrognoselaengeA2());
		daten.getUnscaledValue(Att.PROGNOSELAENGE_A3).set(
				datum.getPrognoselaengeA3());
		daten.getScaledValue(Att.MINSIGUETE).set(datum.getMinSIGuete());

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
