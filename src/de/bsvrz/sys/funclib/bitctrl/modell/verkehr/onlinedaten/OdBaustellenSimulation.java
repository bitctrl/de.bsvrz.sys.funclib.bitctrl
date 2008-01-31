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
 * Weiﬂenfelser Straﬂe 67
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
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.fachmodellglobal.GueteVerfahren;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Baustelle;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Stau;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallIndikator;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallSituation;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdStauVerlauf.PrognoseSchritt;

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

		public static class StauEintrag implements Cloneable {
			/** Startzeit des Staus. */
			long startZeit;
			/** Dauer */
			long dauer;
			/** maximale L‰nge in Metern. */
			long maxLaenge;
			/** Zeitpunkt der maximalen L‰nge. */
			long maxLaengeZeit;
			/** Verlustzeit in Sekunden. */
			long verlustZeit;

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

			public long getDauer() {
				return dauer;
			}

			public long getMaxLaenge() {
				return maxLaenge;
			}

			public long getMaxLaengeZeit() {
				return maxLaengeZeit;
			}

			public long getStartZeit() {
				return startZeit;
			}

			public long getVerlustZeit() {
				return verlustZeit;
			}

			public void setDauer(long dauer) {
				this.dauer = dauer;
			}

			public void setMaxLaenge(long maxLaenge) {
				this.maxLaenge = maxLaenge;
			}

			public void setMaxLaengeZeit(long maxLaengeZeit) {
				this.maxLaengeZeit = maxLaengeZeit;
			}

			public void setStartZeit(long startZeit) {
				this.startZeit = startZeit;
			}

			public void setVerlustZeit(long verlustZeit) {
				this.verlustZeit = verlustZeit;
			}
		}

		boolean valid;

		private List<StauEintrag> staus = new ArrayList<StauEintrag>();

		public void addSchritte(StauEintrag... schritte) {
			for (StauEintrag schritt : schritte) {
				this.staus.add(schritt);
			}
		}

		public void clearSchritte() {
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

			klon.valid = valid;
			return klon;
		}

		public StauEintrag getStau(int idx) {
			return staus.get(idx);
		}

		public List<StauEintrag> getStaus() {
			return staus;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isValid() {
			return valid;
		}

		/**
		 * Setzt das Flag {@code valid} des Datum.
		 * 
		 * @param valid
		 *            der neue Wert des Flags.
		 */
		protected void setValid(boolean valid) {
			this.valid = valid;
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
			array.getItem(idx).getUnscaledValue("MaxL‰nge").set(
					schritt.getMaxLaenge());
			array.getItem(idx).getTimeValue("MaxL‰ngeZeit").setMillis(
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
						"MaxL‰nge").longValue());
				schritt.setMaxLaengeZeit(array.getItem(idx).getTimeValue(
						"MaxL‰ngeZeit").getMillis());
				schritt.setVerlustZeit(array.getItem(idx).getTimeValue(
						"VerlustZeit").getMillis());
			}

			datum.setValid(true);
		} else {
			datum.setValid(false);
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
