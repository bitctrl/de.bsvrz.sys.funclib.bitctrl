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
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.fachmodellglobal.GueteVerfahren;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.Stau;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallIndikator;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StoerfallSituation;

/**
 * Kapselt die Attributgruppe {@code atg.st&ouml;rfallZustand}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: OdStoerfallZustand.java 5226 2007-12-13 14:28:46Z Schumann $
 */
public class OdStauVerlauf extends AbstractOnlineDatensatz<OdStauVerlauf.Daten> {

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

		/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
		private boolean valid;

		/** Schrittweite der Prognose in Sekunden. */
		private long schrittweite;

		/** Dauer des Staus. */
		private long dauer;

		/** Auflösungszeit. */
		private long aufloesungsZeit;

		/** Maximale Länge. */
		private long maxLaenge;

		/** Zeitpunkt der maximalen Länge. */
		private long zeitMaxLaenge;

		private List<PrognoseSchritt> schritte = new ArrayList<PrognoseSchritt>();

		public void addSchritte(PrognoseSchritt... schritte) {
			for (PrognoseSchritt schritt : schritte) {
				this.schritte.add(schritt);
			}
		}

		public void clearSchritte() {
			this.schritte.clear();
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			klon.setSchrittweite(schrittweite);
			klon.setDauer(dauer);
			klon.setAufloesungsZeit(aufloesungsZeit);
			klon.setMaxLaenge(maxLaenge);
			klon.setZeitMaxLaenge(zeitMaxLaenge);

			for (PrognoseSchritt schritt : schritte) {
				klon.schritte.add(schritt.clone());
			}

			klon.valid = valid;
			return klon;
		}

		public long getAufloesungsZeit() {
			return aufloesungsZeit;
		}

		public long getDauer() {
			return dauer;
		}

		public long getMaxLaenge() {
			return maxLaenge;
		}

		public PrognoseSchritt getSchritt(int idx) {
			return schritte.get(idx);
		}

		public List<PrognoseSchritt> getSchritte() {
			return schritte;
		}

		public long getSchrittweite() {
			return schrittweite;
		}

		public long getZeitMaxLaenge() {
			return zeitMaxLaenge;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isValid() {
			return valid;
		}

		public void setAufloesungsZeit(long aufloesungsZeit) {
			this.aufloesungsZeit = aufloesungsZeit;
		}

		public void setDauer(long dauer) {
			this.dauer = dauer;
		}

		public void setMaxLaenge(long maxLaenge) {
			this.maxLaenge = maxLaenge;
		}

		public void setSchrittweite(long schrittweite) {
			this.schrittweite = schrittweite;
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

		public void setZeitMaxLaenge(long zeitMaxLaenge) {
			this.zeitMaxLaenge = zeitMaxLaenge;
		}
	}

	public class PrognoseSchritt implements Cloneable {
		/** Zufluß an Fahrzeugen in Fz/h. */
		long zufluss;
		/** Kapazität in Fz/h. */
		long kapazitaet;
		/** Länge in Metern. */
		long laenge;
		/** Verlustzeit in Sekunden. */
		long verlustZeit;
		/** vKfz in Km/h. */
		long vKfz;

		@Override
		public PrognoseSchritt clone() {

			PrognoseSchritt neuerSchritt = new PrognoseSchritt();

			neuerSchritt.setZufluss(zufluss);
			neuerSchritt.setKapazitaet(kapazitaet);
			neuerSchritt.setLaenge(laenge);
			neuerSchritt.setVerlustZeit(verlustZeit);
			neuerSchritt.setVKfz(vKfz);

			return neuerSchritt;
		}

		public long getKapazitaet() {
			return kapazitaet;
		}

		public long getLaenge() {
			return laenge;
		}

		public long getVerlustZeit() {
			return verlustZeit;
		}

		public long getVKfz() {
			return vKfz;
		}

		public long getZufluss() {
			return zufluss;
		}

		public void setKapazitaet(long kapazitaet) {
			this.kapazitaet = kapazitaet;
		}

		public void setLaenge(long laenge) {
			this.laenge = laenge;
		}

		public void setVerlustZeit(long verlustZeit) {
			this.verlustZeit = verlustZeit;
		}

		public void setVKfz(long vKfz) {
			this.vKfz = vKfz;
		}

		public void setZufluss(long zufluss) {
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
	 * @param si
	 *            ein St&ouml;rfallindikator.
	 */
	public OdStauVerlauf(Stau stau) {
		super(stau);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_STAU_VERLAUF);
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

		daten.getTimeValue("Schrittweite").setMillis(datum.getSchrittweite());
		daten.getTimeValue("Dauer").setMillis(datum.getDauer());
		daten.getTimeValue("AuflösungsZeit").setMillis(
				datum.getAufloesungsZeit());
		daten.getUnscaledValue("MaxLänge").set(datum.getDauer());
		daten.getTimeValue("MaxLängeZeit")
				.setMillis(datum.getAufloesungsZeit());

		Data.Array array = daten.getArray("Prognoseverlauf");
		array.setLength(datum.getSchritte().size());

		for (int idx = 0; idx < datum.getSchritte().size(); idx++) {
			PrognoseSchritt schritt = datum.getSchritt(idx);
			array.getItem(idx).getUnscaledValue("Zufluss").set(
					schritt.getZufluss());
			array.getItem(idx).getUnscaledValue("Kapazität").set(
					schritt.getKapazitaet());
			array.getItem(idx).getUnscaledValue("Länge").set(
					schritt.getLaenge());
			array.getItem(idx).getTimeValue("VerlustZeit").setMillis(
					schritt.getVerlustZeit());
			array.getItem(idx).getUnscaledValue("vKfz").set(schritt.getVKfz());
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

			datum.setSchrittweite(daten.getTimeValue("Schrittweite")
					.getMillis());
			datum.setDauer(daten.getTimeValue("Dauer").getMillis());
			datum.setAufloesungsZeit(daten.getTimeValue("AuflösungsZeit")
					.getMillis());
			datum.setMaxLaenge(daten.getUnscaledValue("MaxLänge").longValue());
			datum.setZeitMaxLaenge(daten.getTimeValue("MaxLängeZeit")
					.getMillis());

			Data.Array array = daten.getArray("Prognoseverlauf");

			for (int idx = 0; idx < array.getLength(); idx++) {
				PrognoseSchritt schritt = new PrognoseSchritt();
				schritt.setZufluss(array.getItem(idx).getUnscaledValue(
						"Zufluss").longValue());
				schritt.setKapazitaet(array.getItem(idx).getUnscaledValue(
						"Kapazität").longValue());
				schritt.setLaenge(array.getItem(idx).getUnscaledValue("Länge")
						.longValue());
				schritt.setVerlustZeit(array.getItem(idx).getTimeValue(
						"VerlustZeit").getMillis());
				schritt.setVKfz(array.getItem(idx).getUnscaledValue("vKfz")
						.longValue());
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
