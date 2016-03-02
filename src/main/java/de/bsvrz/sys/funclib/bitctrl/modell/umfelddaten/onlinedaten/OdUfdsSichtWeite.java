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

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.onlinedaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.NumberValue;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.objekte.UfdsSichtWeite;

/**
 * Kapselt die Attriburgruppe {@code atg.ufdsSichtWeite}.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class OdUfdsSichtWeite
extends AbstractOnlineDatensatz<OdUfdsSichtWeite.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.analyse}. */
		MessWertErsetzung("asp.messWertErsetzung");

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
	public static class Daten extends AbstractDatum implements MesswertDatum {

		/**
		 * Die bekannten Messwerte am dem Datensatz.
		 */
		public enum Werte {

			/** Die Sichtweite in m. */
			SichtWeite;

		}

		/** Sichtweite in m. */
		private Integer sichtWeite;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.sichtWeite = sichtWeite;
			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		@Override
		public Number getWert(final String name) {
			final Werte wert = Werte.valueOf(name);
			switch (wert) {
			case SichtWeite:
				return sichtWeite;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
				+ " kennt keinen Wert " + name + ".");
			}
		}

		@Override
		public List<String> getWerte() {
			final List<String> werte = new ArrayList<String>();

			for (final Werte w : Werte.values()) {
				werte.add(w.name());
			}
			return werte;
		}

		@Override
		public void setWert(final String name, final Number wert) {
			final Werte w = Werte.valueOf(name);
			switch (w) {
			case SichtWeite:
				sichtWeite = wert != null ? wert.intValue() : null;
				break;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
				+ " kennt keinen Wert " + wert + ".");
			}
		}

		@Override
		public String toString() {
			String s;

			s = getClass().getName() + "[";
			s += "zeitpunkt=" + getZeitpunkt();
			s += ", isValid" + isValid();
			for (final Werte w : Werte.values()) {
				s += ", " + w.name() + "=" + getWert(w.name());
			}
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
	public static final String ATG_UFDS_SICHT_WEITE = "atg.ufdsSichtWeite";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 *
	 * @param sensor
	 *            der Umfelddatensensor dessen Daten hier betrachtet werden.
	 */
	public OdUfdsSichtWeite(final UfdsSichtWeite sensor) {
		super(sensor);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_UFDS_SICHT_WEITE);
			assert atg != null;
		}
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<Aspect>();
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
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			NumberValue wert;

			wert = daten.getItem(Daten.Werte.SichtWeite.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.SichtWeite.name(), null);
			} else {
				datum.setWert(Daten.Werte.SichtWeite.name(), wert.intValue());
			}

		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

	@Override
	protected Data konvertiere(final Daten d) {
		final Data datum = erzeugeSendeCache();
		final Integer sw;
		final String wert;
		final Number n;

		wert = OdUfdsSichtWeite.Daten.Werte.SichtWeite.name();
		n = d.getWert(wert);
		sw = n != null ? n.intValue() : null;

		datum.getTimeValue("T").setMillis(Constants.MILLIS_PER_MINUTE);
		datum.getItem(wert).getUnscaledValue("Wert")
		.setText("nicht ermittelbar");
		datum.getItem(wert).getItem("Status").getItem("Erfassung")
		.getUnscaledValue("NichtErfasst").setText("Nein");
		datum.getItem(wert).getItem("Status").getItem("PlFormal")
		.getUnscaledValue("WertMax").setText("Nein");
		datum.getItem(wert).getItem("Status").getItem("PlFormal")
		.getUnscaledValue("WertMin").setText("Nein");
		datum.getItem(wert).getItem("Status").getItem("MessWertErsetzung")
		.getUnscaledValue("Implausibel").setText("Nein");
		datum.getItem(wert).getItem("Status").getItem("MessWertErsetzung")
		.getUnscaledValue("Interpoliert").setText("Nein");
		datum.getItem(wert).getItem("G�te").getUnscaledValue("Index").set(-1);
		datum.getItem(wert).getItem("G�te").getUnscaledValue("Verfahren")
		.set(0);

		if (sw != null) {
			datum.getItem(wert).getUnscaledValue("Wert").set(sw);
		}
		datum.getItem(wert).getItem("G�te").getScaledValue("Index").set(1);

		return datum;
	}

}
