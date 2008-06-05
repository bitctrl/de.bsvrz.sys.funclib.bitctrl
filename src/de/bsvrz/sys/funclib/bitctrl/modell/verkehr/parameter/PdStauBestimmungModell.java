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
 * Wei�enfelser Stra�e 67
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
 * Parameter f�r die Stauobjektbestimmung enth�lt. Der Datensatz repr�sentiert
 * die Daten einer Attributgruppe "atg.stauBestimmmungModell".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdStauBestimmungModell extends
		AbstractParameterDatensatz<PdStauBestimmungModell.Daten> {

	/**
	 * Definition der Attributnamen f�r den Zugriff auf den Parameterdatensatz.
	 * 
	 * @author BitCtrl Systems GmbH, Uwe Peuker
	 * @version $Id: PdStauBestimmungModell.java 8112 2008-04-11 14:44:57Z
	 *          peuker $
	 */
	private static final class Att {
		/** Attributname f�r die Zykluszeit. */
		public static final String ZYKLUSZEIT = "Zykluszeit";
		/** Attributname f�r die Liste der St�rfallverfahren. */
		public static final String SIVERFAHREN = "SIVerfahren";

		/** Attributname f�r die Anfangsl�nge. */
		public static final String ANFANGSLAENGE = "Anfangslaenge";

		/** Attributname f�r die Stronabverl�ngerung. */
		public static final String STROMABVERLAENGERUNG = "StromabVerlaengerung";

		/** Attributname f�r den maximalen SI-Abstand. */
		public static final String MAXSIABSTAND = "MaxSIAbstand";

		/** Attributname f�r die Teilungsl�nge. */
		public static final String TEILUNGSLAENGE = "Teilungslaenge";

		/** Attributname f�r die Teilungsverz�gerung. */
		public static final String TEILUNGSVERZOEGERUNG = "Teilungsverzoegerung";

		/** Attributname f�r den A1-Wert der Formeln der Prognoseberechnung. */
		public static final String PROGNOSELAENGE_A1 = "PrognoselaengeA1";

		/** Attributname f�r den A2-Wert der Formeln der Prognoseberechnung. */
		public static final String PROGNOSELAENGE_A2 = "PrognoselaengeA2";

		/** Attributname f�r den A3-Wert der Formeln der Prognoseberechnung. */
		public static final String PROGNOSELAENGE_A3 = "PrognoselaengeA3";

		/** Attributname f�r die minimlae SI-G�te. */
		public static final String MINSIGUETE = "MinSIGuete";

		/** privater Konstruktor. */
		private Att() {
			super();
		}
	}

	/**
	 * Repr�sentation der Daten des Baustelleneigenschaften-Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Peuker
	 * @version $Id: PdBaustellenEigenschaften.java 4508 2007-10-18 05:30:18Z
	 *          peuker $
	 */
	public static class Daten extends AbstractDatum {

		/** Zykluszeit der Stauobjektbestimmung in Sekunden ("Zykluszeit"). */
		private long zyklusZeit;

		/**
		 * Liste der Verfahren deren St�rfallindikatoren ber�cksichtigt werden
		 * sollen ("SIVerfahren").
		 */
		private Collection<Aspect> siVerfahren = new ArrayList<Aspect>();

		/**
		 * Angenommene Anfangsl�nge des gestauten Bereichs an einem lokalen
		 * St�rfallindikator pro Minute des jeweiligen Erfassungszyklus.
		 * ("Anfangslaenge")
		 */
		private long anfangsLaenge;

		/**
		 * Angenommene Verl�ngerung der Anfangsl�nge bei gestautem
		 * Infrastrukturobjekt i an einem lokalen St�rfallindikator.
		 * ("StromabVerlaengerung")
		 */
		private long stromAbVerlaengerung;

		/**
		 * Maximalabstand f�r die Zusammenfassung von gestauten Bereichen
		 * ("MaxSIAbstand").
		 */
		private long maxSiAbstand;

		/**
		 * Mindestl�nge eines ungestauten Bereiches innerhalb eines Stauobjektes
		 * f�r die sofortige Aufteilung in zwei Stauobjekte ("Teilungslaenge").
		 */
		private long teilungsLaenge;

		/**
		 * Zeitverz�gerung f�r die Aufteilung eines Stauobjekts in dem die L�nge
		 * eines inneren ungestauten Bereiches den Wert "Teilungsl�nge" noch
		 * nicht �berschreitet ("Teilungsverzoegerung").
		 */
		private long teilungsverzoegerung;

		/**
		 * Parameter a1 aus der Gleichung f�r die Berechnung der maximalen
		 * Verl�ngerung eines Stauobjektes durch die Prognose
		 * ("PrognoselaengeA1").
		 */
		private long prognoselaengeA1;

		/**
		 * Parameter a2 aus der Gleichung f�r die Berechnung der maximalen
		 * Verl�ngerung eines Stauobjektes durch die Prognose
		 * ("PrognoselaengeA2").
		 */
		private long prognoselaengeA2;

		/**
		 * Parameter a3 aus der Gleichung f�r die Berechnung der maximalen
		 * Verl�ngerung eines Stauobjektes durch die Prognose
		 * ("PrognoselaengeA3").
		 */
		private long prognoselaengeA3;

		/**
		 * Minimale G�te von zu ber�cksichtigenden St�rfallindikatoren
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
		 * Die Funktion erzeugt ein Datum als Kopie des �bergebenen Datums.
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
		 * und f�llt die Daten entsprechend.
		 * 
		 * @param result
		 *            der �bergebene Datensatz
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
				anfangsLaenge = daten.getUnscaledValue(Att.ANFANGSLAENGE).longValue();
				stromAbVerlaengerung = daten.getUnscaledValue(
						Att.STROMABVERLAENGERUNG).longValue();
				maxSiAbstand = daten.getUnscaledValue(Att.MAXSIABSTAND).longValue();
				teilungsLaenge = daten.getUnscaledValue(Att.TEILUNGSLAENGE).longValue();
				teilungsverzoegerung = daten.getUnscaledValue(
						Att.TEILUNGSVERZOEGERUNG).longValue();
				prognoselaengeA1 = daten.getUnscaledValue(Att.PROGNOSELAENGE_A1).longValue();
				prognoselaengeA2 = daten.getUnscaledValue(Att.PROGNOSELAENGE_A2).longValue();
				prognoselaengeA3 = daten.getUnscaledValue(Att.PROGNOSELAENGE_A3).longValue();
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

		/**
		 * liefert die angenommene L�nge eines Stauobjekts an einem lokalen
		 * St�rfallindikators pro Zeiteinheit.
		 * 
		 * @return die L�nge pro Zeiteinheit (m/min)
		 */
		public long getAnfangsLaenge() {
			return anfangsLaenge;
		}

		/** {@inheritDoc} */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert den maximalen Abstand von zwei St�rfallindikatoren, die zu
		 * einem Stauobjekt zusammengefasst werden sollen (in Metern).
		 * 
		 * @return der Abstand in Metern
		 */
		public long getMaxSiAbstand() {
			return maxSiAbstand;
		}

		/**
		 * liefert die minimale G�te, mit der der Zustand eines
		 * St�rfallindikators f�r die Stauobjektbestimmung verwendet werden
		 * soll.
		 * 
		 * @return die G�te
		 */
		public double getMinSIGuete() {
			return minSIGuete;
		}

		/**
		 * liefert den Parameter a1 f�r die Berechnung der maximalen
		 * Stauverl�ngerung durch eine Prognose (siehe Anwenderanforderungen).
		 * 
		 * @return der Parameterwert
		 */
		public long getPrognoselaengeA1() {
			return prognoselaengeA1;
		}

		/**
		 * liefert den Parameter a2 f�r die Berechnung der maximalen
		 * Stauverl�ngerung durch eine Prognose (siehe Anwenderanforderungen).
		 * 
		 * @return der Parameterwert
		 */
		public long getPrognoselaengeA2() {
			return prognoselaengeA2;
		}

		/**
		 * liefert den Parameter a3 f�r die Berechnung der maximalen
		 * Stauverl�ngerung durch eine Prognose (siehe Anwenderanforderungen).
		 * 
		 * @return der Parameterwert
		 */
		public long getPrognoselaengeA3() {
			return prognoselaengeA3;
		}

		/**
		 * liefert eine Liste der St�rfallverfahren, f�r die die ermittelten
		 * St�rfallzust�nde f�r die Stauobjektbestimmung verwendet werden
		 * sollen. Die Liste enth�lt die Aspekte, die die jeweiligen Verfahren
		 * spezifizieren.
		 * 
		 * @return die Liste
		 */
		public Collection<Aspect> getSiVerfahren() {
			return siVerfahren;
		}

		/**
		 * liefert den Wert, um den ein Stauobjekt stromabw�rts verl�ngert wird
		 * (in Metern).
		 * 
		 * @return die L�nge
		 */
		public long getStromAbVerlaengerung() {
			return stromAbVerlaengerung;
		}

		/**
		 * liefert den Abstand von gest�rten St�rfallindikatoren, bei dem die
		 * Trennung eines Staupbjektes erfolgen soll (in Metern).
		 * 
		 * @return den Abstand
		 */
		public long getTeilungsLaenge() {
			return teilungsLaenge;
		}

		/**
		 * liefert die Verz�gerungszeit f�r die Trennung eines Stauobjekts, wenn
		 * der erforderliche Trennungsbastand nicht �berschritten wurde.
		 * 
		 * @return die Verz�gerungszeit in Millisekunden
		 */
		public long getTeilungsverzoegerung() {
			return teilungsverzoegerung;
		}

		/**
		 * liefert die Zykluszeit, mit der die Stauobjektbestimmung arbeitet.
		 * 
		 * @return die Zeit in Millisekunden
		 */
		public long getZyklusZeit() {
			return zyklusZeit;
		}

		/**
		 * setzt die angenommene L�nge eines Stauobjekts an einem lokalen
		 * St�rfallindikators pro Zeiteinheit.
		 * 
		 * @param anfangsLaenge
		 *            die L�nge pro Zeiteinheit (m/min)
		 */
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

		/**
		 * setzt den maximalen Abstand von zwei St�rfallindikatoren, die zu
		 * einem Stauobjekt zusammengefasst werden sollen (in Metern).
		 * 
		 * @param maxSiAbstand
		 *            der Abstand in Metern
		 */
		public void setMaxSiAbstand(long maxSiAbstand) {
			this.maxSiAbstand = maxSiAbstand;
		}

		/**
		 * setzt die minimale G�te, mit der der Zustand eines St�rfallindikators
		 * f�r die Stauobjektbestimmung verwendet werden soll.
		 * 
		 * @param minSIGuete
		 *            die G�te
		 */
		public void setMinSIGuete(double minSIGuete) {
			this.minSIGuete = minSIGuete;
		}

		/**
		 * setzt den Parameter a1 f�r die Berechnung der maximalen
		 * Stauverl�ngerung durch eine Prognose (siehe Anwenderanforderungen).
		 * 
		 * @param prognoselaengeA1
		 *            der Parameterwert
		 */
		public void setPrognoselaengeA1(long prognoselaengeA1) {
			this.prognoselaengeA1 = prognoselaengeA1;
		}

		/**
		 * setzt den Parameter a2 f�r die Berechnung der maximalen
		 * Stauverl�ngerung durch eine Prognose (siehe Anwenderanforderungen).
		 * 
		 * @param prognoselaengeA2
		 *            der Parameterwert
		 */
		public void setPrognoselaengeA2(long prognoselaengeA2) {
			this.prognoselaengeA2 = prognoselaengeA2;
		}

		/**
		 * setzt den Parameter a3 f�r die Berechnung der maximalen
		 * Stauverl�ngerung durch eine Prognose (siehe Anwenderanforderungen).
		 * 
		 * @param prognoselaengeA3
		 *            der Parameterwert
		 */
		public void setPrognoselaengeA3(long prognoselaengeA3) {
			this.prognoselaengeA3 = prognoselaengeA3;
		}

		/**
		 * setzt die Liste der St�rfallverfahren, f�r die die ermittelten
		 * St�rfallzust�nde f�r die Stauobjektbestimmung verwendet werden
		 * sollen. Die Liste enth�lt die Aspekte, die die jeweiligen Verfahren
		 * spezifizieren.
		 * 
		 * @param siVerfahren
		 *            die Aspekte der Verfahren
		 */
		public void setSiVerfahren(Aspect... siVerfahren) {
			this.siVerfahren.clear();
			if (siVerfahren != null) {
				for (Aspect asp : siVerfahren) {
					this.siVerfahren.add(asp);
				}
			}
		}

		/**
		 * setzt den Wert, um den ein Stauobjekt stromabw�rts verl�ngert wird
		 * (in Metern).
		 * 
		 * @param stromAbVerlaengerung
		 *            der Wert in Metern
		 */
		public void setStromAbVerlaengerung(long stromAbVerlaengerung) {
			this.stromAbVerlaengerung = stromAbVerlaengerung;
		}

		/**
		 * setzt den Abstand von gest�rten St�rfallindikatoren, bei dem die
		 * Trennung eines Staupbjektes erfolgen soll (in Metern).
		 * 
		 * @param teilungsLaenge
		 *            der Abstand
		 */
		public void setTeilungsLaenge(long teilungsLaenge) {
			this.teilungsLaenge = teilungsLaenge;
		}

		/**
		 * setzt die Verz�gerungszeit f�r die Trennung eines Stauobjekts, wenn
		 * der erforderliche Trennungsbastand nicht �berschritten wurde.
		 * 
		 * @param teilungsverzoegerung
		 *            die Verz�gerungszeit in Millisekunden
		 */
		public void setTeilungsverzoegerung(long teilungsverzoegerung) {
			this.teilungsverzoegerung = teilungsverzoegerung;
		}

		/**
		 * setzt die Zykluszeit, mit der die Stauobjektbestimmung arbeitet.
		 * 
		 * @param zyklusZeit
		 *            die Zeit in Millisekunden
		 */
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
			attributGruppe = objekt.getSystemObject().getDataModel().getAttributeGroup(
					"atg.stauBestimmungModell");
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
