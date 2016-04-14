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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bitctrl.Constants;

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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Baustelle;

/**
 * Kapselt die Attributgruppe {@code atg.st&ouml;rfallZustand}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class OdBaustellenSimulation extends AbstractOnlineDatensatz<OdBaustellenSimulation.Daten> {

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
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung().getDataModel();
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

		/**
		 * ein Staueintrag in der Liste der für eine Baustelle prognostizierten
		 * Staus.
		 */
		public static class StauEintrag implements Cloneable {
			/** Startzeit des Staus. */
			private long startZeit;
			/** Dauer. */
			private long dauer;
			/** maximale Länge in Metern. */
			private long maxLaenge;
			/** Zeitpunkt der maximalen Länge. */
			private long maxLaengeZeit;
			/** Verlustzeit in Sekunden. */
			private long verlustZeit;

			@Override
			public StauEintrag clone() {

				final StauEintrag neuerSchritt = new StauEintrag();

				neuerSchritt.setStartZeit(startZeit);
				neuerSchritt.setDauer(dauer);
				neuerSchritt.setMaxLaenge(maxLaenge);
				neuerSchritt.setMaxLaengeZeit(maxLaengeZeit);
				neuerSchritt.setVerlustZeit(verlustZeit);

				return neuerSchritt;
			}

			/**
			 * liefert die Dauer des Staus.
			 *
			 * @return die Dauer
			 */
			public long getDauer() {
				return dauer;
			}

			/**
			 * liefert die maximale Länge des Staus.
			 *
			 * @return die Länge
			 */
			public long getMaxLaenge() {
				return maxLaenge;
			}

			/**
			 * liefert den Zeitpunkt für den die maximale Länge des Staus
			 * ermittelt wurde.
			 *
			 * @return den Zeitpunkt
			 */
			public long getMaxLaengeZeit() {
				return maxLaengeZeit;
			}

			/**
			 * liefert den Anfangszeitpunkt des Staus.
			 *
			 * @return den Zeitpunkt
			 */
			public long getStartZeit() {
				return startZeit;
			}

			/**
			 * liefert die maximale Verlustzeit des Staus.
			 *
			 * @return die Zeit
			 */
			public long getVerlustZeit() {
				return verlustZeit;
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
			 * setzt die maximale Dauer des Staus.
			 *
			 * @param maxLaenge
			 *            die Länge
			 */
			public void setMaxLaenge(final long maxLaenge) {
				this.maxLaenge = maxLaenge;
			}

			/**
			 * setz den Zeitpunkt der maximalen Staulänge.
			 *
			 * @param maxLaengeZeit
			 *            der Zeitpunkt
			 */
			public void setMaxLaengeZeit(final long maxLaengeZeit) {
				this.maxLaengeZeit = maxLaengeZeit;
			}

			/**
			 * setzt den Anfangszeitpunkt des Staus.
			 *
			 * @param startZeit
			 *            der Zeitpunkt
			 */
			public void setStartZeit(final long startZeit) {
				this.startZeit = startZeit;
			}

			/**
			 * setzt die maximale Verlustzeit des Staus.
			 *
			 * @param verlustZeit
			 *            die Zeit
			 */
			public void setVerlustZeit(final long verlustZeit) {
				this.verlustZeit = verlustZeit;
			}
		}

		/**
		 * Markierung, ob mit dem Datensatz die Ergebnisse einer erfolgreichen
		 * Simulation versendet werden.
		 */
		private boolean simulationErfolgreich;

		/** der Name des Auftraggebers. */
		private String auftragGeber;

		/** eine Bemerkung zum Simulationsauftrag. */
		private String bemerkung;

		/** eine potentielle Fehlermeldung. */
		private String fehlerMeldung;

		/**
		 * die Liste der Staueinträge.
		 */
		private final List<StauEintrag> staus = new ArrayList<>();

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * fügt die übergebenen Staueinträge hinzu.
		 *
		 * @param neueStaus
		 *            die Staueinträge
		 */
		public void addSchritte(final StauEintrag... neueStaus) {
			for (final StauEintrag schritt : neueStaus) {
				staus.add(schritt);
			}
		}

		/**
		 * entfernt alle Staueinträge.
		 */
		public void clearStaus() {
			staus.clear();
		}

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			for (final StauEintrag schritt : staus) {
				klon.staus.add(schritt.clone());
			}

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			return klon;
		}

		/**
		 * liefert den Auftraggeber. Wenn keiner gesetzt ist, wird der String
		 * "Baustellensimulation" geliefert.
		 *
		 * @return den Auftraggeber
		 */
		public String getAuftragGeber() {
			String result = auftragGeber;
			if (result == null) {
				result = "Baustellensimulation";
			}
			return result;
		}

		/**
		 * liefert die Bemerkung die bei der Beauftragung einer Simulation
		 * angegebenen wurde. Wenn keine gesetzt wurde, wird ein Leerstring
		 * geliefert.
		 *
		 * @return die Bemerkung
		 */
		public String getBemerkung() {
			String result = bemerkung;
			if (result == null) {
				result = Constants.EMPTY_STRING;
			}
			return result;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert eine potentielle Fehlermeldung, die die Ausführung der
		 * Simulation verhindert hat. Wenn keine gesetzt wurde, wird ein
		 * Leerstring geliefert.
		 *
		 * @return die Meldung
		 */
		public String getFehlerMeldung() {
			String result = fehlerMeldung;
			if (result == null) {
				result = Constants.EMPTY_STRING;
			}
			return result;
		}

		/**
		 * liefert den an der übergebenen Position befindlichen Stau aus der
		 * Liste der Staueinträge.
		 *
		 * @param idx
		 *            der Index
		 * @return den ermittelten Staueintrag
		 */
		public StauEintrag getStau(final int idx) {
			return staus.get(idx);
		}

		/**
		 * liefert die Liste der Staueinträge.
		 *
		 * @return die Liste
		 */
		public List<StauEintrag> getStaus() {
			return staus;
		}

		/**
		 * liefert den Marker, über den festgelegt ist, ob die Simulation
		 * erfolgreich durchgeführt werden konnte.
		 *
		 * @return <code>true</code>, wenn die Daten das Ergebnis einer
		 *         erfolgreichen Simulation beschreiben.
		 */
		public boolean isSimulationErfolgreich() {
			return simulationErfolgreich;
		}

		/**
		 * sertr den Name des Auftraggebers der Simulation.
		 *
		 * @param auftragGeber
		 *            der Name
		 */
		public void setAuftragGeber(final String auftragGeber) {
			this.auftragGeber = auftragGeber;
		}

		/**
		 * setzt den Bemerkungstext, der mit dem Auftrag zur SImulation
		 * geliefert wurde.
		 *
		 * @param bemerkung
		 *            der Text
		 */
		public void setBemerkung(final String bemerkung) {
			this.bemerkung = bemerkung;
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
		 * setzt einen Fehlertext, wenn die Simulation nicht erfolgreich
		 * ausgeführt werden konnte.
		 *
		 * @param fehlerMeldung
		 *            der Text
		 */
		public void setFehlerMeldung(final String fehlerMeldung) {
			this.fehlerMeldung = fehlerMeldung;
		}

		/**
		 * setzt den Marker, der eine erfolgreiche Simulation markiert.
		 *
		 * @param simulationErfolgreich
		 *            <code>true</code>, wenn die Daten einer erfolgreichen
		 *            Simulation entstammen
		 */
		public void setSimulationErfolgreich(final boolean simulationErfolgreich) {
			this.simulationErfolgreich = simulationErfolgreich;
		}
	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_BAUSTELLEN_SIMULATION = "atg.baustellenSimulation";
	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Datensatz.
	 *
	 * @param baustelle
	 *            die Baustelle.
	 */
	public OdBaustellenSimulation(final Baustelle baustelle) {
		super(baustelle);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung().getDataModel();
			atg = modell.getAttributeGroup(ATG_BAUSTELLEN_SIMULATION);
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

		if (datum.isSimulationErfolgreich()) {
			daten.getUnscaledValue("SimulationErfolgreich").set(1);
		} else {
			daten.getUnscaledValue("SimulationErfolgreich").set(0);
		}

		daten.getTextValue("Auftraggeber").setText(datum.getAuftragGeber());
		daten.getTextValue("Bemerkung").setText(datum.getBemerkung());
		daten.getTextValue("Fehlermeldung").setText(datum.getFehlerMeldung());

		final Data.Array array = daten.getArray("BaustellenSimulation");
		array.setLength(datum.getStaus().size());

		for (int idx = 0; idx < datum.getStaus().size(); idx++) {
			final Daten.StauEintrag schritt = datum.getStau(idx);
			array.getItem(idx).getTimeValue("StartZeit").setMillis(schritt.getStartZeit());
			array.getItem(idx).getTimeValue("Dauer").setMillis(schritt.getDauer());
			array.getItem(idx).getUnscaledValue("MaxLänge").set(schritt.getMaxLaenge());
			array.getItem(idx).getTimeValue("MaxLängeZeit").setMillis(schritt.getMaxLaengeZeit());
			array.getItem(idx).getTimeValue("VerlustZeit").setMillis(schritt.getVerlustZeit());
		}

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();

		if (result.hasData()) {
			final Data daten = result.getData();

			if (daten.getUnscaledValue("SimulationErfolgreich").intValue() != 0) {
				datum.setSimulationErfolgreich(true);
			}

			datum.setAuftragGeber(daten.getTextValue("Auftraggeber").getText());
			datum.setBemerkung(daten.getTextValue("Bemerkung").getText());
			datum.setFehlerMeldung(daten.getTextValue("Fehlermeldung").getText());

			final Data.Array array = daten.getArray("BaustellenSimulation");

			for (int idx = 0; idx < array.getLength(); idx++) {
				final Daten.StauEintrag schritt = new Daten.StauEintrag();
				schritt.setStartZeit(array.getItem(idx).getTimeValue("StartZeit").getMillis());
				schritt.setDauer(array.getItem(idx).getTimeValue("Dauer").getMillis());
				schritt.setMaxLaenge(array.getItem(idx).getUnscaledValue("MaxLänge").longValue());
				schritt.setMaxLaengeZeit(array.getItem(idx).getTimeValue("MaxLängeZeit").getMillis());
				schritt.setVerlustZeit(array.getItem(idx).getTimeValue("VerlustZeit").getMillis());
				datum.addSchritte(schritt);
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(), datum.clone());
	}
}
