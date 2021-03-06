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

import java.util.Collection;
import java.util.HashSet;
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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.MessQuerschnitt;

/**
 * Kapselt die Attributgruppe {@code atg.baustellenSimulationStartenMitMq}. Über
 * diese Attributgruppe kann die Simulation einer Baustelle durch die
 * Baustellensimulation manuell aktiviert werden.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class OdBaustellenSimulationStartenMitMq extends
AbstractOnlineDatensatz<OdBaustellenSimulationStartenMitMq.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.senden}. */
		Senden("asp.senden");

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

		/**
		 * der Name des Auftragsgebers.
		 */
		private String name = "";

		/**
		 * eine Bemerkung zum Versand des Auftrags.
		 */
		private String bemerkung = "";

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Referenz auf den MQ, der zur Berechnung des Simulation benutzt werden
		 * soll.
		 */
		private MessQuerschnitt mq;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();
			klon.name = name;
			klon.bemerkung = bemerkung;
			klon.datenStatus = datenStatus;
			klon.mq = mq;
			return klon;
		}

		/**
		 * liefert die Bemerkung.
		 *
		 * @return den Text
		 */
		public String getBemerkung() {
			return bemerkung;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert den Mq.
		 *
		 * @return der Text
		 */
		public MessQuerschnitt getMq() {
			return mq;
		}

		/**
		 * Setzt den Mq.
		 *
		 * @param mq
		 *            Messquerschnitt
		 */
		public void setMq(final MessQuerschnitt mq) {
			this.mq = mq;
		}

		/**
		 * liefert den Namen.
		 *
		 * @return der Text
		 */
		public String getName() {
			return name;
		}

		/**
		 * setzt die Bemerkung.
		 *
		 * @param bemerkung
		 *            der Bemerkungstext
		 */
		public void setBemerkung(final String bemerkung) {
			if (bemerkung != null) {
				this.bemerkung = bemerkung;
			} else {
				this.bemerkung = "";
			}
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
		 * setzt den Name.
		 *
		 * @param name
		 *            der Namenstext
		 */
		public void setName(final String name) {
			if (name != null) {
				this.name = name;
			} else {
				this.name = "";
			}
		}
	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_BAUSTELLEN_SIMULATION_STARTEN_MIT_MQ = "atg.baustellenSimulationStartenMitMq";
	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Datensatz.
	 *
	 * @param baustelle
	 *            die Baustelle.
	 */
	public OdBaustellenSimulationStartenMitMq(final Baustelle baustelle) {
		super(baustelle);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(
					ATG_BAUSTELLEN_SIMULATION_STARTEN_MIT_MQ);
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

		daten.getTextValue("Name").setText(datum.getName());
		daten.getTextValue("Bemerkung").setText(datum.getBemerkung());
		if (datum.getMq() != null) {
			daten.getReferenceValue("BerechnungsMQ")
			.setSystemObject(datum.getMq().getSystemObject());
		}

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			final ObjektFactory factory = ObjektFactory.getInstanz();

			datum.setName(daten.getTextValue("Name").getText());
			datum.setBemerkung(daten.getTextValue("Bemerkung").getText());
			datum.setMq((MessQuerschnitt) factory.getModellobjekt(daten
					.getReferenceValue("BerechnungsMQ").getSystemObject()));
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
