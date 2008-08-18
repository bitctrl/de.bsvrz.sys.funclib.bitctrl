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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.onlinedaten;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.bitctrl.util.Timestamp;

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
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Datenverteiler;

/**
 * Kapselt die Attributgruppe {@code atg.angemeldeteApplikationen}.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class AngemeldeteApplikationen extends
		AbstractOnlineDatensatz<AngemeldeteApplikationen.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.standard}. */
		Standard("asp.standard");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 * 
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		private Aspekte(final String pid) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getAspekt() {
			return aspekt;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/** Der aktuelle Status des Datensatzes. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/** Der angemeldete Benutzer. */
		private Benutzer benutzer;

		/** Die Applikation an der der Benutzer angemeldet ist. */
		private Applikation applikation;

		/** Der Zeitpunkt der Anmeldung laut Datenverteiler. */
		private Timestamp seit;

		/**
		 * Gibt den angemeldeten Benutzer zurück.
		 * 
		 * @return der angemeldete Benutzer.
		 */
		public Benutzer getBenutzer() {
			return benutzer;
		}

		/**
		 * Legt den angemeldeten Benutzer fest.
		 * 
		 * @param benutzer
		 *            der angemeldete Benutzer.
		 */
		public void setBenutzer(final Benutzer benutzer) {
			this.benutzer = benutzer;
		}

		/**
		 * Gibt die Applikation an der der Benutzer angemeldet ist zurück.
		 * 
		 * @return die Applikation.
		 */
		public Applikation getApplikation() {
			return applikation;
		}

		/**
		 * Legt die Applikation an der der Benutzer angemeldet ist fest.
		 * 
		 * @param applikation
		 *            die Applikation.
		 */
		public void setApplikation(final Applikation applikation) {
			this.applikation = applikation;
		}

		/**
		 * Gibt den Zeitpunkt der Anmeldung laut Datenverteiler zurück.
		 * 
		 * @return der Anmeldezeitpunkt.
		 */
		public Timestamp getSeit() {
			return seit;
		}

		/**
		 * Legt den Zeitpunkt der Anmeldung laut Datenverteiler fest.
		 * 
		 * @param seit
		 *            der Anmeldezeitpunkt.
		 */
		public void setSeit(final Timestamp seit) {
			this.seit = seit;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.applikation = applikation;
			klon.benutzer = benutzer;
			klon.seit = seit;
			klon.datenStatus = datenStatus;

			return klon;
		}

		/**
		 * {@inheritDoc}
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s;

			s = getClass().getName() + "[";
			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;
			s += ", benutzer=" + benutzer;
			s += ", applikation=" + applikation;
			s += ", seit=" + seit;
			s += "]";

			return s;
		}

		/**
		 * Setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_ANGEMELDETE_APPLIKATIONEN = "atg.angemeldeteApplikationen";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param dav
	 *            der Datenverteiler, dessen Onlinedaten verwalten werden
	 *            sollen.
	 */
	public AngemeldeteApplikationen(final Datenverteiler dav) {
		super(dav);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_ANGEMELDETE_APPLIKATIONEN);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<Aspect>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		daten.getReferenceValue("applikation").setSystemObject(
				datum.getApplikation().getSystemObject());
		daten.getReferenceValue("benutzer").setSystemObject(
				datum.getBenutzer().getSystemObject());
		daten.getTimeValue("seit").setMillis(datum.getSeit().getTime());

		return daten;
	}

	/**
	 * {@inheritDoc}
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final Data daten = result.getData();

			datum.setApplikation((Applikation) factory.getModellobjekt(daten
					.getReferenceValue("applikation").getSystemObject()));
			datum.setBenutzer((Benutzer) factory.getModellobjekt(daten
					.getReferenceValue("benutzer").getSystemObject()));
			datum
					.setSeit(new Timestamp(daten.getTimeValue("seit")
							.getMillis()));
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
