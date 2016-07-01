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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.FahrStreifen;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitFs.Daten.Werte;
import de.bsvrz.sys.funclib.bitctrl.util.dav.Umrechung;

/**
 * Kapselt die Attributgruppe {@code atg.verkehrsDatenKurzZeitFs}.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class OdVerkehrsDatenKurzZeitFs
extends AbstractOnlineDatensatz<OdVerkehrsDatenKurzZeitFs.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.analyse}. */
		Analyse("asp.analyse");

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
	public static class Daten extends AbstractDatum implements MesswertDatum {

		/** Benennt die Messwerte die dieser Datensatz kennt. */
		public enum Werte {

			/** Lkw-Anteil in Prozent. */
			aLkw,

			/** Bemessungsdichte KB in Fahrzeuge/km. */
			kB,

			/**
			 * Verkehrsstärke QKfz (alle Fahrzeuge) in Anzahl pro
			 * Messabschnittsdauer.
			 */
			qKfz,

			/** Verkehrsstärke QPkw in Anzahl pro Messabschnittsdauer. */
			qPkw,

			/** Verkehrsstärke QLkw in Anzahl pro Messabschnittsdauer. */
			qLkw,

			/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
			vKfz,

			/** Geschwindigkeit VPkw in km/h. */
			vPkw,

			/** Geschwindigkeit VLkw in km/h. */
			vLkw,

			/** Standardabweichung der Kfz-Geschwindigkeiten SKfz in km/h. */
			sKfz,

			/** Belegungsgrad (mit Statusinformationen) in Prozent. */
			b;

		}

		/** Lkw-Anteil in Prozent. */
		private Integer aLkw;

		/** Bemessungsdichte KB in Fahrzeuge/km. */
		private Integer kb;

		/**
		 * Verkehrsstärke QKfz (alle Fahrzeuge) in Anzahl pro
		 * Messabschnittsdauer.
		 */
		private Integer qKfz;

		/** Verkehrsstärke QPkw in Anzahl pro Messabschnittsdauer. */
		private Integer qPkw;

		/** Verkehrsstärke QLkw in Anzahl pro Messabschnittsdauer. */
		private Integer qLkw;

		/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
		private Integer vKfz;

		/** Geschwindigkeit VPkw in km/h. */
		private Integer vPkw;

		/** Geschwindigkeit VLkw in km/h. */
		private Integer vLkw;

		/** Standardabweichung der Kfz-Geschwindigkeiten SKfz in km/h. */
		private Integer sKfz;

		/** Belegungsgrad (mit Statusinformationen) in Prozent. */
		private Float b;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.aLkw = aLkw;
			klon.qKfz = qKfz;
			klon.qLkw = qLkw;
			klon.qPkw = qPkw;
			klon.vKfz = vKfz;
			klon.vLkw = vLkw;
			klon.vPkw = vPkw;
			klon.kb = kb;
			klon.b = b;
			klon.sKfz = sKfz;
			klon.datenStatus = datenStatus;
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
			case aLkw:
				return aLkw;
			case kB:
				return kb;
			case qKfz:
				return qKfz;
			case qLkw:
				return qLkw;
			case qPkw:
				return qPkw;
			case vKfz:
				return vKfz;
			case vLkw:
				return vLkw;
			case vPkw:
				return vPkw;
			case sKfz:
				return sKfz;
			case b:
				return b;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
				+ " kennt keinen Wert " + name + ".");
			}
		}

		@Override
		public List<String> getWerte() {
			final List<String> werte = new ArrayList<>();

			for (final Werte w : Werte.values()) {
				werte.add(w.name());
			}
			return werte;
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

		@Override
		public void setWert(final String name, final Number wert) {
			final Werte w = Werte.valueOf(name);
			switch (w) {
			case aLkw:
				aLkw = wert != null ? wert.intValue() : null;
				break;
			case kB:
				kb = wert != null ? wert.intValue() : null;
				break;
			case qKfz:
				qKfz = wert != null ? wert.intValue() : null;
				break;
			case qLkw:
				qLkw = wert != null ? wert.intValue() : null;
				break;
			case qPkw:
				qPkw = wert != null ? wert.intValue() : null;
				break;
			case vKfz:
				vKfz = wert != null ? wert.intValue() : null;
				break;
			case vLkw:
				vLkw = wert != null ? wert.intValue() : null;
				break;
			case vPkw:
				vPkw = wert != null ? wert.intValue() : null;
				break;
			case sKfz:
				sKfz = wert != null ? wert.intValue() : null;
				break;
			case b:
				b = wert != null ? wert.floatValue() : null;
				break;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
				+ " kennt keinen Wert " + wert + ".");
			}
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_VERKEHRS_DATEN_KURZ_ZEIT_FS = "atg.verkehrsDatenKurzZeitFs";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 *
	 * @param fs
	 *            der Fahrstreifen dessen Kurzzeitdaten hier betrachtet werden.
	 */
	public OdVerkehrsDatenKurzZeitFs(final FahrStreifen fs) {
		super(fs);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_VERKEHRS_DATEN_KURZ_ZEIT_FS);
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

	/**
	 * Alle Messwerte sind initial "nicht ermittelbar" und die Statuswerte
	 * besitzen alle die gleichen Standardwerte. Die Messwerte aus
	 * {@link OdVerkehrsDatenKurzZeitFs.Daten.Werte} werden &uuml;bernommen.
	 */
	@Override
	protected Data konvertiere(final OdVerkehrsDatenKurzZeitFs.Daten d) {
		final Data datum = erzeugeSendeCache();
		final int qKfz, qLkw, vPkw, vLkw, sKfz, kb, b;
		final Integer qPkw, vKfz, qb, aLkw;

		qKfz = d.getWert(Werte.qKfz.name()).intValue();
		qLkw = d.getWert(Werte.qLkw.name()).intValue();
		vPkw = d.getWert(Werte.vPkw.name()).intValue();
		vLkw = d.getWert(Werte.vLkw.name()).intValue();
		sKfz = d.getWert(Werte.sKfz.name()).intValue();
		kb = d.getWert(Werte.kB.name()).intValue();
		b = d.getWert(Werte.b.name()).intValue();

		final String[] valStrings = { "qKfz", "vKfz", "qLkw", "vLkw", "qPkw",
				"vPkw", "b", "sKfz", "bMax", "vgKfz", "aLkw", "kKfz", "kPkw",
				"kLkw", "qB", "kB", "vDelta" };

		for (final String valString : valStrings) {
			datum.getItem(valString).getUnscaledValue("Wert")
			.setText("nicht ermittelbar");
			datum.getItem(valString).getItem("Status").getItem("Erfassung")
			.getUnscaledValue("NichtErfasst").setText("Nein");
			datum.getItem(valString).getItem("Status").getItem("PlFormal")
			.getUnscaledValue("WertMax").setText("Nein");
			datum.getItem(valString).getItem("Status").getItem("PlFormal")
			.getUnscaledValue("WertMin").setText("Nein");
			datum.getItem(valString).getItem("Status").getItem("PlLogisch")
			.getUnscaledValue("WertMaxLogisch").setText("Nein");
			datum.getItem(valString).getItem("Status").getItem("PlLogisch")
			.getUnscaledValue("WertMinLogisch").setText("Nein");
			datum.getItem(valString).getItem("Status")
			.getItem("MessWertErsetzung")
			.getUnscaledValue("Implausibel").setText("Nein");
			datum.getItem(valString).getItem("Status")
			.getItem("MessWertErsetzung")
			.getUnscaledValue("Interpoliert").setText("Nein");
			datum.getItem(valString).getItem("Güte").getUnscaledValue("Index")
			.set(-1);
			datum.getItem(valString).getItem("Güte")
			.getUnscaledValue("Verfahren").set(0);
		}

		datum.getItem("qKfz").getUnscaledValue("Wert").set(qKfz);
		datum.getItem("qKfz").getItem("Güte").getUnscaledValue("Index").set(10);

		datum.getItem("qLkw").getUnscaledValue("Wert").set(qLkw);
		datum.getItem("qLkw").getItem("Güte").getUnscaledValue("Index").set(10);

		datum.getItem("vPkw").getUnscaledValue("Wert").set(vPkw);
		datum.getItem("vPkw").getItem("Güte").getUnscaledValue("Index").set(10);

		datum.getItem("vLkw").getUnscaledValue("Wert").set(vLkw);
		datum.getItem("vLkw").getItem("Güte").getUnscaledValue("Index").set(10);

		datum.getItem("sKfz").getUnscaledValue("Wert").set(sKfz);
		datum.getItem("sKfz").getItem("Güte").getUnscaledValue("Index").set(10);

		datum.getItem("kB").getUnscaledValue("Wert").set(kb);
		datum.getItem("kB").getItem("Güte").getUnscaledValue("Index").set(10);

		datum.getItem("b").getUnscaledValue("Wert").set(b);
		datum.getItem("b").getItem("Güte").getUnscaledValue("Index").set(10);

		// Nicht erfasste Werte berechnen

		aLkw = Umrechung.getALkw(qLkw, qKfz);
		datum.getItem("aLkw").getUnscaledValue("Wert").set(aLkw);
		datum.getItem("aLkw").getItem("Güte").getUnscaledValue("Index").set(10);

		qPkw = Umrechung.getQPkw(qKfz, qLkw);
		if (qPkw != null) {
			datum.getItem("qPkw").getUnscaledValue("Wert").set(qPkw);
			datum.getItem("qPkw").getItem("Güte").getUnscaledValue("Index")
			.set(10);
			datum.getItem("qPkw").getItem("Status").getItem("Erfassung")
			.getUnscaledValue("NichtErfasst").setText("Ja");

		}

		vKfz = Umrechung.getVKfz(qLkw, qKfz, vPkw, vLkw);
		if (vKfz != null) {
			datum.getItem("vKfz").getUnscaledValue("Wert").set(vKfz);
			datum.getItem("vKfz").getItem("Güte").getUnscaledValue("Index")
			.set(10);
			datum.getItem("vKfz").getItem("Status").getItem("Erfassung")
			.getUnscaledValue("NichtErfasst").setText("Ja");
		}

		qb = Umrechung.getQB(qLkw, qKfz, vPkw, vLkw, 0.5f, 1);
		if (qb != null) {
			datum.getItem("qB").getUnscaledValue("Wert").set(qb);
			datum.getItem("qB").getItem("Güte").getUnscaledValue("Index")
			.set(10);
			datum.getItem("qB").getItem("Status").getItem("Erfassung")
			.getUnscaledValue("NichtErfasst").setText("Ja");
		}

		return datum;
	}

	@Override
	public synchronized void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			NumberValue wert;

			wert = daten.getItem(Daten.Werte.qKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.qKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.qKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.qLkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.qLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.qLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.qPkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.qPkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.qPkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.vKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vLkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.vLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vPkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vPkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.vPkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.sKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.sKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.sKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.kB.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.kB.name(), null);
			} else {
				datum.setWert(Daten.Werte.kB.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.aLkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.aLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.aLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.b.name()).getScaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.b.name(), null);
			} else {
				datum.setWert(Daten.Werte.b.name(), wert.floatValue());
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
