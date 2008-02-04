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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Baustelle;

/**
 * Kapselt die Attributgruppe {@code atg.st&ouml;rfallZustand}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdBaustellenSimulation extends
		AbstractOnlineDatensatz<OdBaustellenSimulation.Daten> {

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
		private Aspekte(String pid) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Aspekt#getAspekt()
		 */
		public Aspect getAspekt() {
			return aspekt;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Aspekt#getName()
		 */
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
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id: OdBaustellenSimulation.java 6188 2008-02-04 10:27:08Z
		 *          peuker $
		 */
		public static class StauEintrag implements Cloneable {
			/** Startzeit des Staus. */
			long startZeit;
			/** Dauer. */
			long dauer;
			/** maximale Länge in Metern. */
			long maxLaenge;
			/** Zeitpunkt der maximalen Länge. */
			long maxLaengeZeit;
			/** Verlustzeit in Sekunden. */
			long verlustZeit;

			/**
			 * {@inheritDoc}.<br>
			 * 
			 * @see java.lang.Object#clone()
			 */
			@Override
			public StauEintrag clone() {

				StauEintrag neuerSchritt = new StauEintrag();

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
			public void setDauer(long dauer) {
				this.dauer = dauer;
			}

			/**
			 * setzt die maximale Dauer des Staus.
			 * 
			 * @param maxLaenge
			 *            die Länge
			 */
			public void setMaxLaenge(long maxLaenge) {
				this.maxLaenge = maxLaenge;
			}

			/**
			 * setz den Zeitpunkt der maximalen Staulänge.
			 * 
			 * @param maxLaengeZeit
			 *            der Zeitpunkt
			 */
			public void setMaxLaengeZeit(long maxLaengeZeit) {
				this.maxLaengeZeit = maxLaengeZeit;
			}

			/**
			 * setzt den Anfangszeitpunkt des Staus.
			 * 
			 * @param startZeit
			 *            der Zeitpunkt
			 */
			public void setStartZeit(long startZeit) {
				this.startZeit = startZeit;
			}

			/**
			 * setzt die maximale Verlustzeit des Staus.
			 * 
			 * @param verlustZeit
			 *            die Zeit
			 */
			public void setVerlustZeit(long verlustZeit) {
				this.verlustZeit = verlustZeit;
			}
		}

		/**
		 * die Liste der Staueinträge.
		 */
		private List<StauEintrag> staus = new ArrayList<StauEintrag>();

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * fügt diue übergebenen Staueinträge hinzu.
		 * 
		 * @param neueStaus
		 *            die Staueinträge
		 */
		public void addSchritte(StauEintrag... neueStaus) {
			for (StauEintrag schritt : neueStaus) {
				this.staus.add(schritt);
			}
		}

		/**
		 * entfernt alle Staueinträge.
		 */
		public void clearStaus() {
			this.staus.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			for (StauEintrag schritt : staus) {
				klon.staus.add(schritt.clone());
			}

			klon.datenStatus = datenStatus;
			return klon;
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
		 * liefert den an der übergebenen Position befindlichen Stau aus der
		 * Liste der Staueinträge.
		 * 
		 * @param idx
		 *            der Index
		 * @return den ermittelten Staueintrag
		 */
		public StauEintrag getStau(int idx) {
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
		 * setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(Status neuerStatus) {
			this.datenStatus = neuerStatus;
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
	public OdBaustellenSimulation(Baustelle baustelle) {
		super(baustelle);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_BAUSTELLEN_SIMULATION);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#erzeugeDatum()
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getAspekte()
	 */
	@Override
	public Collection<Aspect> getAspekte() {
		Set<Aspect> aspekte = new HashSet<Aspect>();
		for (Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getAttributGruppe()
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten datum) {
		Data daten = erzeugeSendeCache();

		Data.Array array = daten.getArray("BaustellenSimulation");
		array.setLength(datum.getStaus().size());

		for (int idx = 0; idx < datum.getStaus().size(); idx++) {
			Daten.StauEintrag schritt = datum.getStau(idx);
			array.getItem(idx).getTimeValue("StartZeit").setMillis(
					schritt.getStartZeit());
			array.getItem(idx).getTimeValue("Dauer").setMillis(
					schritt.getDauer());
			array.getItem(idx).getUnscaledValue("MaxLänge").set(
					schritt.getMaxLaenge());
			array.getItem(idx).getTimeValue("MaxLängeZeit").setMillis(
					schritt.getMaxLaengeZeit());
			array.getItem(idx).getTimeValue("VerlustZeit").setMillis(
					schritt.getVerlustZeit());
		}

		return daten;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.ResultData)
	 */
	public void setDaten(ResultData result) {
		check(result);

		Daten datum = new Daten();
		if (result.hasData()) {
			Data daten = result.getData();

			Data.Array array = daten.getArray("BaustellenSimulation");

			for (int idx = 0; idx < array.getLength(); idx++) {
				Daten.StauEintrag schritt = new Daten.StauEintrag();
				schritt.setStartZeit(array.getItem(idx).getTimeValue(
						"StartZeit").getMillis());
				schritt.setDauer(array.getItem(idx).getTimeValue("Dauer")
						.getMillis());
				schritt.setMaxLaenge(array.getItem(idx).getUnscaledValue(
						"MaxLänge").longValue());
				schritt.setMaxLaengeZeit(array.getItem(idx).getTimeValue(
						"MaxLängeZeit").getMillis());
				schritt.setVerlustZeit(array.getItem(idx).getTimeValue(
						"VerlustZeit").getMillis());
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()
				.getCode()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
