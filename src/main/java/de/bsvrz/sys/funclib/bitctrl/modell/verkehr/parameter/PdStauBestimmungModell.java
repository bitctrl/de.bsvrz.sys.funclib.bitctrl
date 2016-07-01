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
 */
public class PdStauBestimmungModell
extends AbstractParameterDatensatz<PdStauBestimmungModell.Daten> {

	/**
	 * Definition der Attributnamen für den Zugriff auf den Parameterdatensatz.
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
	 */
	public static class Daten extends AbstractDatum {

		/** Zykluszeit der Stauobjektbestimmung in Sekunden ("Zykluszeit"). */
		private long zyklusZeit;

		/**
		 * Liste der Verfahren deren Störfallindikatoren berücksichtigt werden
		 * sollen ("SIVerfahren").
		 */
		private final Collection<Aspect> siVerfahren = new ArrayList<>();

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
		public Daten(final Daten daten) {
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
				zyklusZeit = daten.getUnscaledValue(Att.ZYKLUSZEIT).longValue();
				final Data.Array verfahrensArray = daten
						.getArray(Att.SIVERFAHREN);
				for (int idx = 0; idx < verfahrensArray.getLength(); idx++) {
					siVerfahren.add((Aspect) verfahrensArray
							.getReferenceValue(idx).getSystemObject());
				}
				anfangsLaenge = daten.getUnscaledValue(Att.ANFANGSLAENGE)
						.longValue();
				stromAbVerlaengerung = daten
						.getUnscaledValue(Att.STROMABVERLAENGERUNG).longValue();
				maxSiAbstand = daten.getUnscaledValue(Att.MAXSIABSTAND)
						.longValue();
				teilungsLaenge = daten.getUnscaledValue(Att.TEILUNGSLAENGE)
						.longValue();
				teilungsverzoegerung = daten
						.getUnscaledValue(Att.TEILUNGSVERZOEGERUNG).longValue();
				prognoselaengeA1 = daten.getUnscaledValue(Att.PROGNOSELAENGE_A1)
						.longValue();
				prognoselaengeA2 = daten.getUnscaledValue(Att.PROGNOSELAENGE_A2)
						.longValue();
				prognoselaengeA3 = daten.getUnscaledValue(Att.PROGNOSELAENGE_A3)
						.longValue();
				minSIGuete = daten.getScaledValue(Att.MINSIGUETE).doubleValue();
			}

			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		@Override
		public Daten clone() {
			return new Daten(this);
		}

		/**
		 * liefert die angenommene Länge eines Stauobjekts an einem lokalen
		 * Störfallindikators pro Zeiteinheit.
		 *
		 * @return die Länge pro Zeiteinheit (m/min)
		 */
		public long getAnfangsLaenge() {
			return anfangsLaenge;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert den maximalen Abstand von zwei Störfallindikatoren, die zu
		 * einem Stauobjekt zusammengefasst werden sollen (in Metern).
		 *
		 * @return der Abstand in Metern
		 */
		public long getMaxSiAbstand() {
			return maxSiAbstand;
		}

		/**
		 * liefert die minimale Güte, mit der der Zustand eines
		 * Störfallindikators für die Stauobjektbestimmung verwendet werden
		 * soll.
		 *
		 * @return die Güte
		 */
		public double getMinSIGuete() {
			return minSIGuete;
		}

		/**
		 * liefert den Parameter a1 für die Berechnung der maximalen
		 * Stauverlängerung durch eine Prognose (siehe Anwenderanforderungen).
		 *
		 * @return der Parameterwert
		 */
		public long getPrognoselaengeA1() {
			return prognoselaengeA1;
		}

		/**
		 * liefert den Parameter a2 für die Berechnung der maximalen
		 * Stauverlängerung durch eine Prognose (siehe Anwenderanforderungen).
		 *
		 * @return der Parameterwert
		 */
		public long getPrognoselaengeA2() {
			return prognoselaengeA2;
		}

		/**
		 * liefert den Parameter a3 für die Berechnung der maximalen
		 * Stauverlängerung durch eine Prognose (siehe Anwenderanforderungen).
		 *
		 * @return der Parameterwert
		 */
		public long getPrognoselaengeA3() {
			return prognoselaengeA3;
		}

		/**
		 * liefert eine Liste der Störfallverfahren, für die die ermittelten
		 * Störfallzustände für die Stauobjektbestimmung verwendet werden
		 * sollen. Die Liste enthält die Aspekte, die die jeweiligen Verfahren
		 * spezifizieren.
		 *
		 * @return die Liste
		 */
		public Collection<Aspect> getSiVerfahren() {
			return siVerfahren;
		}

		/**
		 * liefert den Wert, um den ein Stauobjekt stromabwärts verlängert wird
		 * (in Metern).
		 *
		 * @return die Länge
		 */
		public long getStromAbVerlaengerung() {
			return stromAbVerlaengerung;
		}

		/**
		 * liefert den Abstand von gestörten Störfallindikatoren, bei dem die
		 * Trennung eines Staupbjektes erfolgen soll (in Metern).
		 *
		 * @return den Abstand
		 */
		public long getTeilungsLaenge() {
			return teilungsLaenge;
		}

		/**
		 * liefert die Verzögerungszeit für die Trennung eines Stauobjekts, wenn
		 * der erforderliche Trennungsbastand nicht überschritten wurde.
		 *
		 * @return die Verzögerungszeit in Millisekunden
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
		 * setzt die angenommene Länge eines Stauobjekts an einem lokalen
		 * Störfallindikators pro Zeiteinheit.
		 *
		 * @param anfangsLaenge
		 *            die Länge pro Zeiteinheit (m/min)
		 */
		public void setAnfangsLaenge(final long anfangsLaenge) {
			this.anfangsLaenge = anfangsLaenge;
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
		 * setzt den maximalen Abstand von zwei Störfallindikatoren, die zu
		 * einem Stauobjekt zusammengefasst werden sollen (in Metern).
		 *
		 * @param maxSiAbstand
		 *            der Abstand in Metern
		 */
		public void setMaxSiAbstand(final long maxSiAbstand) {
			this.maxSiAbstand = maxSiAbstand;
		}

		/**
		 * setzt die minimale Güte, mit der der Zustand eines Störfallindikators
		 * für die Stauobjektbestimmung verwendet werden soll.
		 *
		 * @param minSIGuete
		 *            die Güte
		 */
		public void setMinSIGuete(final double minSIGuete) {
			this.minSIGuete = minSIGuete;
		}

		/**
		 * setzt den Parameter a1 für die Berechnung der maximalen
		 * Stauverlängerung durch eine Prognose (siehe Anwenderanforderungen).
		 *
		 * @param prognoselaengeA1
		 *            der Parameterwert
		 */
		public void setPrognoselaengeA1(final long prognoselaengeA1) {
			this.prognoselaengeA1 = prognoselaengeA1;
		}

		/**
		 * setzt den Parameter a2 für die Berechnung der maximalen
		 * Stauverlängerung durch eine Prognose (siehe Anwenderanforderungen).
		 *
		 * @param prognoselaengeA2
		 *            der Parameterwert
		 */
		public void setPrognoselaengeA2(final long prognoselaengeA2) {
			this.prognoselaengeA2 = prognoselaengeA2;
		}

		/**
		 * setzt den Parameter a3 für die Berechnung der maximalen
		 * Stauverlängerung durch eine Prognose (siehe Anwenderanforderungen).
		 *
		 * @param prognoselaengeA3
		 *            der Parameterwert
		 */
		public void setPrognoselaengeA3(final long prognoselaengeA3) {
			this.prognoselaengeA3 = prognoselaengeA3;
		}

		/**
		 * setzt die Liste der Störfallverfahren, für die die ermittelten
		 * Störfallzustände für die Stauobjektbestimmung verwendet werden
		 * sollen. Die Liste enthält die Aspekte, die die jeweiligen Verfahren
		 * spezifizieren.
		 *
		 * @param siVerfahren
		 *            die Aspekte der Verfahren
		 */
		public void setSiVerfahren(final Aspect... siVerfahren) {
			this.siVerfahren.clear();
			if (siVerfahren != null) {
				for (final Aspect asp : siVerfahren) {
					this.siVerfahren.add(asp);
				}
			}
		}

		/**
		 * setzt den Wert, um den ein Stauobjekt stromabwärts verlängert wird
		 * (in Metern).
		 *
		 * @param stromAbVerlaengerung
		 *            der Wert in Metern
		 */
		public void setStromAbVerlaengerung(final long stromAbVerlaengerung) {
			this.stromAbVerlaengerung = stromAbVerlaengerung;
		}

		/**
		 * setzt den Abstand von gestörten Störfallindikatoren, bei dem die
		 * Trennung eines Staupbjektes erfolgen soll (in Metern).
		 *
		 * @param teilungsLaenge
		 *            der Abstand
		 */
		public void setTeilungsLaenge(final long teilungsLaenge) {
			this.teilungsLaenge = teilungsLaenge;
		}

		/**
		 * setzt die Verzögerungszeit für die Trennung eines Stauobjekts, wenn
		 * der erforderliche Trennungsbastand nicht überschritten wurde.
		 *
		 * @param teilungsverzoegerung
		 *            die Verzögerungszeit in Millisekunden
		 */
		public void setTeilungsverzoegerung(final long teilungsverzoegerung) {
			this.teilungsverzoegerung = teilungsverzoegerung;
		}

		/**
		 * setzt die Zykluszeit, mit der die Stauobjektbestimmung arbeitet.
		 *
		 * @param zyklusZeit
		 *            die Zeit in Millisekunden
		 */
		public void setZyklusZeit(final long zyklusZeit) {
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
	public PdStauBestimmungModell(final SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.stauBestimmungModell");
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

		daten.getUnscaledValue(Att.ZYKLUSZEIT).set(datum.getZyklusZeit());

		final Data.Array verfahrensArray = daten.getArray(Att.SIVERFAHREN);
		verfahrensArray.setLength(datum.getSiVerfahren().size());
		int idx = 0;
		for (final Aspect asp : datum.getSiVerfahren()) {
			verfahrensArray.getReferenceValue(idx++).setSystemObject(asp);
		}
		daten.getUnscaledValue(Att.ANFANGSLAENGE).set(datum.getAnfangsLaenge());
		daten.getUnscaledValue(Att.STROMABVERLAENGERUNG)
		.set(datum.getStromAbVerlaengerung());
		daten.getUnscaledValue(Att.MAXSIABSTAND).set(datum.getMaxSiAbstand());
		daten.getUnscaledValue(Att.TEILUNGSLAENGE)
		.set(datum.getTeilungsLaenge());
		daten.getUnscaledValue(Att.TEILUNGSVERZOEGERUNG)
		.set(datum.getTeilungsverzoegerung());
		daten.getUnscaledValue(Att.PROGNOSELAENGE_A1)
		.set(datum.getPrognoselaengeA1());
		daten.getUnscaledValue(Att.PROGNOSELAENGE_A2)
		.set(datum.getPrognoselaengeA2());
		daten.getUnscaledValue(Att.PROGNOSELAENGE_A3)
		.set(datum.getPrognoselaengeA3());
		daten.getScaledValue(Att.MINSIGUETE).set(datum.getMinSIGuete());

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
