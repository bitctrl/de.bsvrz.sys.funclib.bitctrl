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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Stau;

/**
 * Kapselt die Attributgruppe {@code atg.st&ouml;rfallZustand}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class OdStauVerlauf
extends AbstractOnlineDatensatz<OdStauVerlauf.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.prognoseNormal}. */
		PrognoseNormal("asp.prognoseNormal");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 *
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		Aspekte(final String pid) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		@Override
		public Aspect getAspekt() {
			return aspekt;
		}

		@Override
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/** Schrittweite der Prognose in Sekunden. */
		private long schrittweite;

		/** Dauer des Staus. */
		private long dauer = Long.MAX_VALUE;

		/** Aufl�sungszeit. */
		private long aufloesungsZeit = Long.MAX_VALUE;

		/** Maximale L�nge. */
		private long maxLaenge;

		/** Zeitpunkt der maximalen L�nge. */
		private long zeitMaxLaenge;

		/**
		 * die Einzelschritte der Prognose.
		 */
		private final List<PrognoseSchritt> schritte = new ArrayList<>();

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * f�gt die �bergebenen Prognoseschritte hinzu.
		 *
		 * @param neueSchritte
		 *            die hinzuzuf�genden Schritte
		 */
		public void addSchritte(final PrognoseSchritt... neueSchritte) {
			for (final PrognoseSchritt schritt : neueSchritte) {
				schritte.add(schritt);
			}
		}

		/**
		 * l�scht alle Schritte des Stauverlaufs.
		 */
		public void clearSchritte() {
			schritte.clear();
		}

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setSchrittweite(schrittweite);
			klon.setDauer(dauer);
			klon.setAufloesungsZeit(aufloesungsZeit);
			klon.setMaxLaenge(maxLaenge);
			klon.setZeitMaxLaenge(zeitMaxLaenge);

			for (final PrognoseSchritt schritt : schritte) {
				klon.schritte.add(schritt.clone());
			}

			klon.datenStatus = datenStatus;
			return klon;
		}

		/**
		 * liefert die aktuelle Geschwindigkeit im Stau.
		 *
		 * @return die Geschwindigkeit in km/h
		 */
		public long getAktuelleGeschwindigkeit() {
			long result = 0;
			if (getSchritte().size() > 0) {
				final PrognoseSchritt schritt = getSchritt(0);
				if (schritt != null) {
					result = schritt.getVKfz();
				}
			}
			return result;
		}

		/**
		 * liefert die prognostizierte Aufl�sungszeit.
		 *
		 * @return die Zeit
		 */
		public long getAufloesungsZeit() {
			return aufloesungsZeit;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert die prognostizierte Dauer.
		 *
		 * @return die Dauer
		 */
		public long getDauer() {
			return dauer;
		}

		/**
		 * liefert die maximale L�nge im Prognosebereich.
		 *
		 * @return die L�nge
		 */
		public long getMaxLaenge() {
			return maxLaenge;
		}

		/**
		 * liefert die maximale prognostizierte Verlustzeit in Millisekunden.
		 *
		 * @return die Zeit
		 */
		public long getMaxVerlustzeit() {
			long result = 0;
			for (final PrognoseSchritt schritt : getSchritte()) {
				result = Math.max(result, schritt.getVerlustZeit());
			}
			return result;
		}

		/**
		 * liefert den mit dem Index spezifizierten Prognoseschritt.
		 *
		 * @param idx
		 *            der gew�nschte Index
		 * @return den Prignoseschritt
		 */
		public PrognoseSchritt getSchritt(final int idx) {
			return schritte.get(idx);
		}

		/**
		 * liefert die Liste aller Prognoseschritte.
		 *
		 * @return die Schritte
		 */
		public List<PrognoseSchritt> getSchritte() {
			return schritte;
		}

		/**
		 * liefert die f�r die Prognose verwendete Schrittweite.
		 *
		 * @return die Schrittweite
		 */
		public long getSchrittweite() {
			return schrittweite;
		}

		/**
		 * liefert den Zeitpunkt zu dem im Prognosezeitraun die gr��te L�nge des
		 * Staus auftritt.
		 *
		 * @return den Zeitpunkt
		 */
		public long getZeitMaxLaenge() {
			return zeitMaxLaenge;
		}

		/**
		 * setzt die Aufl�sungszeit des Staus.
		 *
		 * @param aufloesungsZeit
		 *            der Zeitpunkt
		 */
		public void setAufloesungsZeit(final long aufloesungsZeit) {
			this.aufloesungsZeit = aufloesungsZeit;
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
		 * setzt die Dauer des Staus.
		 *
		 * @param dauer
		 *            die Dauer
		 */
		public void setDauer(final long dauer) {
			this.dauer = dauer;
		}

		/**
		 * setzt die maximale L�nge des Staus im Prognosezeitraum.
		 *
		 * @param maxLaenge
		 *            die L�nge
		 */
		public void setMaxLaenge(final long maxLaenge) {
			this.maxLaenge = maxLaenge;
		}

		/**
		 * setzt die f�r die Prognose verwendete Schrittweite.
		 *
		 * @param schrittweite
		 *            die Schrittweite
		 */
		public void setSchrittweite(final long schrittweite) {
			this.schrittweite = schrittweite;
		}

		/**
		 * setzt den Zeitpunkt, zu dem im Prognosezeitraum die maximale L�nge
		 * des Staus aufgetreten ist.
		 *
		 * @param zeitMaxLaenge
		 *            der Zeitpunkt
		 */
		public void setZeitMaxLaenge(final long zeitMaxLaenge) {
			this.zeitMaxLaenge = zeitMaxLaenge;
		}
	}

	/**
	 * Die Repr�sentation eines Schritts innerhalb einer Stauprognose.
	 */
	public class PrognoseSchritt implements Cloneable {
		/** Zuflu� an Fahrzeugen in Fz/h. */
		private long zufluss;
		/** Kapazit�t in Fz/h. */
		private long kapazitaet;
		/** L�nge in Metern. */
		private long laenge;
		/** Verlustzeit in Sekunden. */
		private long verlustZeit;
		/** vKfz in Km/h. */
		private long vKfz;

		@Override
		public PrognoseSchritt clone() {

			final PrognoseSchritt neuerSchritt = new PrognoseSchritt();

			neuerSchritt.setZufluss(zufluss);
			neuerSchritt.setKapazitaet(kapazitaet);
			neuerSchritt.setLaenge(laenge);
			neuerSchritt.setVerlustZeit(verlustZeit);
			neuerSchritt.setVKfz(vKfz);

			return neuerSchritt;
		}

		/**
		 * liefert die freie Kapazit�t innerhalb des Prognoseschritts.
		 *
		 * @return die Kapazit�t
		 */
		public long getKapazitaet() {
			return kapazitaet;
		}

		/**
		 * liefert die im Prognoseschritt erwartetet Staul�nge.
		 *
		 * @return die L�nge
		 */
		public long getLaenge() {
			return laenge;
		}

		/**
		 * liefert die im Schritt berechnete Verlustzeit.
		 *
		 * @return die Zeit
		 */
		public long getVerlustZeit() {
			return verlustZeit;
		}

		/**
		 * liefert den VKfz-Wert f�r den Schritt.
		 *
		 * @return den Wert
		 */
		public long getVKfz() {
			return vKfz;
		}

		/**
		 * liefert den Zufluss zum Zeitpunkt des Prognoseschritts.
		 *
		 * @return den Zuflusswert
		 */
		public long getZufluss() {
			return zufluss;
		}

		/**
		 * setzt die Kapazit�t zum Zeitpunkt des Schritts.
		 *
		 * @param kapazitaet
		 *            die Kapazit�t
		 */
		public void setKapazitaet(final long kapazitaet) {
			this.kapazitaet = kapazitaet;
		}

		/**
		 * setzt die L�nge des Staus zum Zeitpunkt des Prognoseschritts.
		 *
		 * @param laenge
		 *            die L�nge
		 */
		public void setLaenge(final long laenge) {
			this.laenge = laenge;
		}

		/**
		 * setzt die Verlustzeit zum Zeitpunkt des Prognoseschritts.
		 *
		 * @param verlustZeit
		 *            die Zeit
		 */
		public void setVerlustZeit(final long verlustZeit) {
			this.verlustZeit = verlustZeit;
		}

		/**
		 * setzt den VKfz-Wert zum Zeitpunkt des prognoseschritts.
		 *
		 * @param vKfz
		 *            der Wert
		 */
		public void setVKfz(final long vKfz) {
			this.vKfz = vKfz;
		}

		/**
		 * setzt den Zuflusswert f�r den Prognoseschritt.
		 *
		 * @param zufluss
		 *            dr Wert
		 */
		public void setZufluss(final long zufluss) {
			this.zufluss = zufluss;
		}
	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_STAU_VERLAUF = "atg.stauVerlauf";
	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Datensatz.
	 *
	 * @param stau
	 *            der Stau.
	 */
	public OdStauVerlauf(final Stau stau) {
		super(stau);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_STAU_VERLAUF);
			assert atg != null;
		}
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		daten.getTimeValue("Schrittweite").setMillis(datum.getSchrittweite());
		daten.getTimeValue("Dauer").setMillis(datum.getDauer());
		daten.getTimeValue("Aufl�sungsZeit")
		.setMillis(datum.getAufloesungsZeit());
		daten.getUnscaledValue("MaxL�nge").set(datum.getDauer());
		daten.getTimeValue("MaxL�ngeZeit")
		.setMillis(datum.getAufloesungsZeit());

		final Data.Array array = daten.getArray("Prognoseverlauf");
		array.setLength(datum.getSchritte().size());

		for (int idx = 0; idx < datum.getSchritte().size(); idx++) {
			final PrognoseSchritt schritt = datum.getSchritt(idx);
			array.getItem(idx).getUnscaledValue("Zufluss")
			.set(schritt.getZufluss());
			array.getItem(idx).getUnscaledValue("Kapazit�t")
			.set(schritt.getKapazitaet());
			array.getItem(idx).getUnscaledValue("L�nge")
			.set(schritt.getLaenge());
			array.getItem(idx).getTimeValue("VerlustZeit")
			.setMillis(schritt.getVerlustZeit());
			array.getItem(idx).getUnscaledValue("vKfz").set(schritt.getVKfz());
		}

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();

			datum.setSchrittweite(
					daten.getTimeValue("Schrittweite").getMillis());
			datum.setDauer(daten.getTimeValue("Dauer").getMillis());
			datum.setAufloesungsZeit(
					daten.getTimeValue("Aufl�sungsZeit").getMillis());
			datum.setMaxLaenge(daten.getUnscaledValue("MaxL�nge").longValue());
			datum.setZeitMaxLaenge(
					daten.getTimeValue("MaxL�ngeZeit").getMillis());

			final Data.Array array = daten.getArray("Prognoseverlauf");

			for (int idx = 0; idx < array.getLength(); idx++) {
				final PrognoseSchritt schritt = new PrognoseSchritt();
				schritt.setZufluss(array.getItem(idx)
						.getUnscaledValue("Zufluss").longValue());
				schritt.setKapazitaet(array.getItem(idx)
						.getUnscaledValue("Kapazit�t").longValue());
				schritt.setLaenge(array.getItem(idx).getUnscaledValue("L�nge")
						.longValue());
				schritt.setVerlustZeit(array.getItem(idx)
						.getTimeValue("VerlustZeit").getMillis());
				schritt.setVKfz(array.getItem(idx).getUnscaledValue("vKfz")
						.longValue());
				datum.addSchritte(schritt);
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
