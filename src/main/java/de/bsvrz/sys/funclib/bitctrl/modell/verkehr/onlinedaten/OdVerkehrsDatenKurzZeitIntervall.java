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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitIntervall.Daten.Werte;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.ArtMittelwertBildung;
import de.bsvrz.sys.funclib.bitctrl.util.dav.Umrechung;

/**
 * Kapselt die Attributgruppe {@code atg.verkehrsDatenKurzZeitIntervall}.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class OdVerkehrsDatenKurzZeitIntervall extends
AbstractOnlineDatensatz<OdVerkehrsDatenKurzZeitIntervall.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.externeErfassung}. */
		ExterneErfassung("asp.externeErfassung"),

		/** Der Aspekt {@code asp.externeErfassung}. */
		MessWertErsetzung("asp.messWertErsetzung"),

		/** Der Aspekt {@code asp.externeErfassung}. */
		PlausibilitaetsPruefungFormal("asp.plausibilitätsPrüfungFormal"),

		/** Der Aspekt {@code asp.externeErfassung}. */
		PlausibilitaetsPruefungLogisch("asp.plausibilitätsPrüfungLogisch");

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

		/** Benennt die Messwerte die dieser Datensatz kennt. */
		public enum Werte {
			/** Fahrzeugmenge Kfz (mit Statusinformationen). */
			qKfz,

			/** Geschwindigkeit Kfz (mit Statusinformationen). */
			vKfz,

			/** Fahrzeugmenge Lkw (mit Statusinformationen). */
			qLkw,

			/** Geschwindigkeit Lkw (mit Statusinformationen). */
			vLkw,

			/** Fahrzeugmenge Pkw (mit Statusinformationen). */
			qPkw,

			/** Geschwindigkeit Pkw (mit Statusinformationen). */
			vPkw,

			/** Belegungsgrad (mit Statusinformationen). */
			b,

			/** Mittlere Nettozeitlücke (mit Statusinformationen). */
			tNetto,

			/**
			 * Standardabweichung der Geschwindigkeit (mit Statusinformationen).
			 */
			sKfz,

			/** Geglätte mittlere Geschwindigkeit (mit Statusinformationen). */
			vgKfz;
		};

		/** Intervalldauer, mit dem die Werte erfasst wurden. */
		private long intervallDauerT;

		/** Art der Mittelwertbildung (arithmetisch oder gleitend). */
		ArtMittelwertBildung artMittelwertBildung = ArtMittelwertBildung.UNBEKANNT;

		/** Fahrzeugmenge Kfz (mit Statusinformationen). */
		private Long qKfz;

		/** Geschwindigkeit Kfz (mit Statusinformationen). */
		private Long vKfz;

		/** Fahrzeugmenge Lkw (mit Statusinformationen). */
		private Long qLkw;

		/** Geschwindigkeit Lkw (mit Statusinformationen). */
		private Long vLkw;

		/** Fahrzeugmenge Pkw (mit Statusinformationen). */
		private Long qPkw;

		/** Geschwindigkeit Pkw (mit Statusinformationen). */
		private Long vPkw;

		/** Belegungsgrad (mit Statusinformationen). */
		private Long b;

		/** Mittlere Nettozeitlücke (mit Statusinformationen). */
		private Long tNetto;

		/** Standardabweichung der Geschwindigkeit (mit Statusinformationen). */
		private Long sKfz;

		/** Geglätte mittlere Geschwindigkeit (mit Statusinformationen). */
		private Long vgKfz;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.intervallDauerT = intervallDauerT;
			klon.artMittelwertBildung = artMittelwertBildung;
			klon.qKfz = qKfz;
			klon.vKfz = vKfz;
			klon.qLkw = qLkw;
			klon.vLkw = vLkw;
			klon.qPkw = qPkw;
			klon.vPkw = vPkw;
			klon.b = b;
			klon.tNetto = tNetto;
			klon.sKfz = sKfz;
			klon.vgKfz = vgKfz;

			klon.datenStatus = datenStatus;
			return klon;
		}

		/**
		 * liefert, die innerhalb des Datensatzes definierte Art der
		 * Mittelwertbildung.
		 *
		 * @return die Art der Mittelwertbildung
		 */
		public ArtMittelwertBildung getArtMittelwertBildung() {
			return artMittelwertBildung;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * liefert die innerhalb des Datensatzes definierte Intervalldauer für
		 * die erfassten Daten.
		 *
		 * @return die Intervalldauer in Millisekunden
		 */
		public long getIntervallDauerT() {
			return intervallDauerT;
		}

		@Override
		public Long getWert(final String name) {
			final Werte wert = Werte.valueOf(name);
			switch (wert) {
			case qKfz:
				return qKfz;
			case vKfz:
				return vKfz;
			case qLkw:
				return qLkw;
			case vLkw:
				return vLkw;
			case qPkw:
				return qPkw;
			case vPkw:
				return vPkw;
			case b:
				return b;
			case tNetto:
				return tNetto;
			case sKfz:
				return sKfz;
			case vgKfz:
				return vgKfz;
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

		/**
		 * setzt die Art der Mittelwertbildung für den Datensatz.
		 *
		 * @param artMittelwertBildung
		 *            die Art
		 */
		public void setArtMittelwertBildung(
				final ArtMittelwertBildung artMittelwertBildung) {
			this.artMittelwertBildung = artMittelwertBildung;
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
		 * setzt die Intervalldauer für die Daten des Datensatzes.
		 *
		 * @param intervallDauerT
		 *            die Dauer
		 */
		public void setIntervallDauerT(final long intervallDauerT) {
			this.intervallDauerT = intervallDauerT;
		}

		@Override
		public void setWert(final String name, final Number wert) {
			final Werte w = Werte.valueOf(name);
			switch (w) {
			case qKfz:
				qKfz = wert != null ? wert.longValue() : null;
				break;
			case vKfz:
				vKfz = wert != null ? wert.longValue() : null;
				break;
			case qLkw:
				qLkw = wert != null ? wert.longValue() : null;
				break;
			case vLkw:
				vLkw = wert != null ? wert.longValue() : null;
				break;
			case qPkw:
				qPkw = wert != null ? wert.longValue() : null;
				break;
			case vPkw:
				vPkw = wert != null ? wert.longValue() : null;
				break;
			case b:
				b = wert != null ? wert.longValue() : null;
				break;
			case tNetto:
				tNetto = wert != null ? wert.longValue() : null;
				break;
			case sKfz:
				sKfz = wert != null ? wert.longValue() : null;
				break;
			case vgKfz:
				vgKfz = wert != null ? wert.longValue() : null;
				break;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
				+ " kennt keinen Wert " + wert + ".");
			}
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_VERKEHRS_DATEN_KURZ_ZEIT_INTERVALL = "atg.verkehrsDatenKurzZeitIntervall";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 *
	 * @param fs
	 *            der Fahrstreifen dessen Kurzzeitdaten hier betrachtet werden.
	 */
	public OdVerkehrsDatenKurzZeitIntervall(final FahrStreifen fs) {
		super(fs);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell
					.getAttributeGroup(ATG_VERKEHRS_DATEN_KURZ_ZEIT_INTERVALL);
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

	/**
	 * Alle Messwerte sind initial "nicht ermittelbar" und die Statuswerte
	 * besitzen alle die gleichen Standardwerte. Die Messwerte aus
	 * {@link OdVerkehrsDatenKurzZeitIntervall.Daten.Werte} werden
	 * &uuml;bernommen.
	 */
	@Override
	protected Data konvertiere(final OdVerkehrsDatenKurzZeitIntervall.Daten d) {
		final Data datum = erzeugeSendeCache();

		final Number qKfz;
		Number vKfz;
		final Number qLkw, vLkw;
		Number qPkw;
		final Number vPkw, b, tNetto, sKfz, vgKfz;

		qKfz = d.getWert(Werte.qKfz.name());
		vKfz = d.getWert(Werte.vKfz.name());
		qLkw = d.getWert(Werte.qLkw.name());
		vLkw = d.getWert(Werte.vLkw.name());
		qPkw = d.getWert(Werte.qPkw.name());
		vPkw = d.getWert(Werte.vPkw.name());
		b = d.getWert(Werte.b.name());
		tNetto = d.getWert(Werte.tNetto.name());
		sKfz = d.getWert(Werte.sKfz.name());
		vgKfz = d.getWert(Werte.vgKfz.name());

		final String[] valStrings = { "qKfz", "vKfz", "qLkw", "vLkw", "qPkw",
				"vPkw", "b", "tNetto", "sKfz", "vgKfz" };

		for (final String valString : valStrings) {
			datum.getItem(valString).getUnscaledValue("Wert")
			.setText("nicht ermittelbar");
			datum.getItem(valString).getItem("Güte").getUnscaledValue("Index")
			.set(0);
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

		if (qKfz != null) {
			datum.getItem("qKfz").getUnscaledValue("Wert").set(qKfz.intValue());
			datum.getItem("qKfz").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (vKfz != null) {
			datum.getItem("vKfz").getUnscaledValue("Wert").set(vKfz.intValue());
			datum.getItem("vKfz").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		} else {
			vKfz = Umrechung.getVKfz(qLkw.doubleValue(), qKfz.doubleValue(),
					vPkw.doubleValue(), vLkw.doubleValue());
			if (vKfz != null) {
				datum.getItem("vKfz").getUnscaledValue("Wert")
				.set(vKfz.intValue());
				datum.getItem("vKfz").getItem("Güte").getUnscaledValue("Index")
				.set(10);
				datum.getItem("vKfz").getItem("Status").getItem("Erfassung")
				.getUnscaledValue("NichtErfasst").setText("Ja");
			}

		}
		if (qLkw != null) {
			datum.getItem("qLkw").getUnscaledValue("Wert").set(qLkw.intValue());
			datum.getItem("qLkw").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (vLkw != null) {
			datum.getItem("vLkw").getUnscaledValue("Wert").set(vLkw.intValue());
			datum.getItem("vLkw").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (qPkw != null) {
			datum.getItem("qPkw").getUnscaledValue("Wert").set(qPkw.intValue());
			datum.getItem("qPkw").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		} else {
			qPkw = Umrechung.getQPkw(qKfz.doubleValue(), qLkw.doubleValue())
					.longValue();
			if (qPkw != null) {
				datum.getItem("qPkw").getUnscaledValue("Wert")
				.set(qPkw.intValue());
				datum.getItem("qPkw").getItem("Güte").getUnscaledValue("Index")
				.set(10);
				datum.getItem("qPkw").getItem("Status").getItem("Erfassung")
				.getUnscaledValue("NichtErfasst").setText("Ja");

			}
		}

		if (vPkw != null) {
			datum.getItem("vPkw").getUnscaledValue("Wert").set(vPkw.intValue());
			datum.getItem("vPkw").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (b != null) {
			datum.getItem("b").getUnscaledValue("Wert").set(b.intValue());
			datum.getItem("b").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (tNetto != null) {
			datum.getItem("tNetto").getUnscaledValue("Wert")
			.set(tNetto.longValue());
			datum.getItem("tNetto").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (sKfz != null) {
			datum.getItem("sKfz").getUnscaledValue("Wert").set(sKfz.intValue());
			datum.getItem("sKfz").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}
		if (vgKfz != null) {
			datum.getItem("vgKfz").getUnscaledValue("Wert")
			.set(sKfz.intValue());
			datum.getItem("vgKfz").getItem("Güte").getUnscaledValue("Index")
			.set(10000);
		}

		datum.getTimeValue("T").setMillis(d.getIntervallDauerT());
		datum.getUnscaledValue("ArtMittelwertbildung")
		.set(d.getArtMittelwertBildung().getCode());

		return datum;
	}

	@Override
	public synchronized void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			NumberValue wert;

			datum.setIntervallDauerT(daten.getTimeValue("T").getMillis());
			datum.setArtMittelwertBildung(ArtMittelwertBildung.getStatus(
					daten.getUnscaledValue("ArtMittelwertbildung").intValue()));

			wert = daten.getItem(Daten.Werte.qKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.qKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.qKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.vKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.qLkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.qLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.qLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vLkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.vLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.qPkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.qPkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.qPkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vPkw.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vPkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.vPkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.b.name()).getScaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.b.name(), null);
			} else {
				datum.setWert(Daten.Werte.b.name(), wert.floatValue());
			}

			wert = daten.getItem(Daten.Werte.tNetto.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.tNetto.name(), null);
			} else {
				datum.setWert(Daten.Werte.tNetto.name(), wert.longValue());
			}

			wert = daten.getItem(Daten.Werte.sKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.sKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.sKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.vgKfz.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.vgKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.vgKfz.name(), wert.intValue());
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
