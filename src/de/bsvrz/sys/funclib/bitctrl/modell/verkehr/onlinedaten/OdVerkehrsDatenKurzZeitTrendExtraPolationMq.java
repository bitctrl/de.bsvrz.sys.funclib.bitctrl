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
import de.bsvrz.dav.daf.main.Data.NumberValue;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitTrendExtraPolationMq.Daten.Werte;
import de.bsvrz.sys.funclib.bitctrl.util.dav.Umrechung;

/**
 * Kapselt die Attributgruppe
 * {@code atg.verkehrsDatenKurzZeitTrendExtraPolationMq}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdVerkehrsDatenKurzZeitTrendExtraPolationMq
		extends
		AbstractOnlineDatensatz<OdVerkehrsDatenKurzZeitTrendExtraPolationMq.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.prognoseNormal}. */
		PrognoseNormal("asp.prognoseNormal"),

		/** Der Aspekt {@code asp.prognoseFlink}. */
		PrognoseFlink("asp.prognoseFlink"),

		/** Der Aspekt {@code asp.prognoseNormal}. */
		PrognoseTraege("asp.prognoseTr‰ge");

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
	public static class Daten extends AbstractDatum implements MesswertDatum {

		/** Benennt die Messwerte die dieser Datensatz kennt. */
		public enum Werte {

			/** Lkw-Anteil in Prozent. */
			ALkwP,

			/** Bemessungsdichte KB in Fahrzeuge/km. */
			KBP,

			/**
			 * Verkehrsst‰rke QKfz (alle Fahrzeuge) in Anzahl pro
			 * Messabschnittsdauer.
			 */
			QKfzP,

			/** Verkehrsst‰rke QPkw in Anzahl pro Messabschnittsdauer. */
			QPkwP,

			/** Verkehrsst‰rke QLkw in Anzahl pro Messabschnittsdauer. */
			QLkwP,

			/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
			VKfzP,

			/** Geschwindigkeit VPkw in km/h. */
			VPkwP,

			/** Geschwindigkeit VLkw in km/h. */
			VLkwP;

		}

		/** Lkw-Anteil in Prozent. */
		private Integer aLkwP;

		/** Bemessungsdichte KB in Fahrzeuge/km. */
		private Integer kbP;

		/**
		 * Verkehrsst‰rke QKfz (alle Fahrzeuge) in Anzahl pro
		 * Messabschnittsdauer.
		 */
		private Integer qKfzP;

		/** Verkehrsst‰rke QPkw in Anzahl pro Messabschnittsdauer. */
		private Integer qPkwP;

		/** Verkehrsst‰rke QLkw in Anzahl pro Messabschnittsdauer. */
		private Integer qLkwP;

		/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
		private Integer vKfzP;

		/** Geschwindigkeit VPkw in km/h. */
		private Integer vPkwP;

		/** Geschwindigkeit VLkw in km/h. */
		private Integer vLkwP;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.aLkwP = aLkwP;
			klon.qKfzP = qKfzP;
			klon.qLkwP = qLkwP;
			klon.qPkwP = qPkwP;
			klon.vKfzP = vKfzP;
			klon.vLkwP = vLkwP;
			klon.vPkwP = vPkwP;
			klon.kbP = kbP;
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
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum#getWert(java.lang.String)
		 */
		public Number getWert(final String name) {
			final Werte wert = Werte.valueOf(name);
			switch (wert) {
			case ALkwP:
				return aLkwP;
			case KBP:
				return kbP;
			case QKfzP:
				return qKfzP;
			case QLkwP:
				return qLkwP;
			case QPkwP:
				return qPkwP;
			case VKfzP:
				return vKfzP;
			case VLkwP:
				return vLkwP;
			case VPkwP:
				return vPkwP;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
						+ " kennt keinen Wert " + name + ".");
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum#getWerte()
		 */
		public List<String> getWerte() {
			final List<String> werte = new ArrayList<String>();

			for (final Werte w : Werte.values()) {
				werte.add(w.name());
			}
			return werte;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum#setWert(java.lang.String,
		 *      java.lang.Number)
		 */
		public void setWert(final String name, final Number wert) {
			final Werte w = Werte.valueOf(name);
			switch (w) {
			case ALkwP:
				aLkwP = wert != null ? wert.intValue() : null;
				break;
			case KBP:
				kbP = wert != null ? wert.intValue() : null;
				break;
			case QKfzP:
				qKfzP = wert != null ? wert.intValue() : null;
				break;
			case QLkwP:
				qLkwP = wert != null ? wert.intValue() : null;
				break;
			case QPkwP:
				qPkwP = wert != null ? wert.intValue() : null;
				break;
			case VKfzP:
				vKfzP = wert != null ? wert.intValue() : null;
				break;
			case VLkwP:
				vLkwP = wert != null ? wert.intValue() : null;
				break;
			case VPkwP:
				vPkwP = wert != null ? wert.intValue() : null;
				break;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
						+ " kennt keinen Wert " + wert + ".");
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String s = "Daten[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", isValid" + isValid();
			for (final Werte w : Werte.values()) {
				s += ", " + w.name() + "=" + getWert(w.name());
			}

			return s + "]";
		}

		/**
		 * setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			this.datenStatus = neuerStatus;
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String PID_ATG = "atg.verkehrsDatenKurzZeitTrendExtraPolationMq";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param mq
	 *            der Messquerschnitt dessen Kurzzeitdaten hier betrachtet
	 *            werden.
	 */
	public OdVerkehrsDatenKurzZeitTrendExtraPolationMq(
			final MessQuerschnittAllgemein mq) {
		super(mq);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(PID_ATG);
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
		final Set<Aspect> aspekte = new HashSet<Aspect>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
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
	public synchronized void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			NumberValue wert;

			wert = daten.getItem(Daten.Werte.QKfzP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.QKfzP.name(), null);
			} else {
				datum.setWert(Daten.Werte.QKfzP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.QLkwP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.QLkwP.name(), null);
			} else {
				datum.setWert(Daten.Werte.QLkwP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.QPkwP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.QPkwP.name(), null);
			} else {
				datum.setWert(Daten.Werte.QPkwP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.VKfzP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.VKfzP.name(), null);
			} else {
				datum.setWert(Daten.Werte.VKfzP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.VLkwP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.VLkwP.name(), null);
			} else {
				datum.setWert(Daten.Werte.VLkwP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.VPkwP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.VPkwP.name(), null);
			} else {
				datum.setWert(Daten.Werte.VPkwP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.KBP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.KBP.name(), null);
			} else {
				datum.setWert(Daten.Werte.KBP.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.ALkwP.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.ALkwP.name(), null);
			} else {
				datum.setWert(Daten.Werte.ALkwP.name(), wert.intValue());
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Alle Messwerte sind initial "nicht ermittelbar" und die Statuswerte
	 * besitzen alle die gleichen Standardwerte. Die Messwerte aus
	 * {@link OdVerkehrsDatenKurzZeitTrendExtraPolationMq.Daten.Werte} werden
	 * &uuml;bernommen.
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(
			final OdVerkehrsDatenKurzZeitTrendExtraPolationMq.Daten d) {
		final Data datum = erzeugeSendeCache();
		final Integer qKfz;
		final Integer qLkw;
		final Integer vPkw;
		final Integer vLkw;
		final Integer kb;
		final Integer qPkw;
		final Integer vKfz;
		final Integer qb;
		final Integer aLkw;
		Number n;

		n = d.getWert(Werte.QKfzP.name());
		qKfz = n != null ? n.intValue() : null;

		n = d.getWert(Werte.QLkwP.name());
		qLkw = n != null ? n.intValue() : null;

		n = d.getWert(Werte.VPkwP.name());
		vPkw = n != null ? n.intValue() : null;

		n = d.getWert(Werte.VLkwP.name());
		vLkw = n != null ? n.intValue() : null;

		n = d.getWert(Werte.KBP.name());
		kb = n != null ? n.intValue() : null;

		final String[] valStrings = { "QKfz", "VKfz", "QLkw", "VLkw", "QPkw",
				"VPkw", "B", "SKfz", "BMax", "VgKfz", "ALkw", "KKfz", "KPkw",
				"KLkw", "QB", "KB", "VDelta" };

		for (int idx = 0; idx < valStrings.length; idx++) {
			datum.getItem(valStrings[idx]).getUnscaledValue("Wert").setText(
					"nicht ermittelbar");
			datum.getItem(valStrings[idx]).getItem("Status").getItem(
					"Erfassung").getUnscaledValue("NichtErfasst").setText(
					"Nein");
			datum.getItem(valStrings[idx]).getItem("Status")
					.getItem("PlFormal").getUnscaledValue("WertMax").setText(
							"Nein");
			datum.getItem(valStrings[idx]).getItem("Status")
					.getItem("PlFormal").getUnscaledValue("WertMin").setText(
							"Nein");
			datum.getItem(valStrings[idx]).getItem("Status").getItem(
					"PlLogisch").getUnscaledValue("WertMaxLogisch").setText(
					"Nein");
			datum.getItem(valStrings[idx]).getItem("Status").getItem(
					"PlLogisch").getUnscaledValue("WertMinLogisch").setText(
					"Nein");
			datum.getItem(valStrings[idx]).getItem("Status").getItem(
					"MessWertErsetzung").getUnscaledValue("Implausibel")
					.setText("Nein");
			datum.getItem(valStrings[idx]).getItem("Status").getItem(
					"MessWertErsetzung").getUnscaledValue("Interpoliert")
					.setText("Nein");
			datum.getItem(valStrings[idx]).getItem("G¸te").getUnscaledValue(
					"Index").set(-1);
			datum.getItem(valStrings[idx]).getItem("G¸te").getUnscaledValue(
					"Verfahren").set(0);
		}

		if (qKfz != null) {
			datum.getItem("QKfz").getUnscaledValue("Wert").set(qKfz);
		}
		datum.getItem("QKfz").getItem("G¸te").getUnscaledValue("Index").set(10);

		if (qLkw != null) {
			datum.getItem("QLkw").getUnscaledValue("Wert").set(qLkw);
		}
		datum.getItem("QLkw").getItem("G¸te").getUnscaledValue("Index").set(10);

		if (vPkw != null) {
			datum.getItem("VPkw").getUnscaledValue("Wert").set(vPkw);
		}
		datum.getItem("VPkw").getItem("G¸te").getUnscaledValue("Index").set(10);

		if (vLkw != null) {
			datum.getItem("VLkw").getUnscaledValue("Wert").set(vLkw);
		}
		datum.getItem("VLkw").getItem("G¸te").getUnscaledValue("Index").set(10);

		if (kb != null) {
			datum.getItem("KB").getUnscaledValue("Wert").set(kb);
		}
		datum.getItem("KB").getItem("G¸te").getUnscaledValue("Index").set(10);

		// Nicht erfasste Werte berechnen

		aLkw = Umrechung.getALkw(qLkw, qKfz);
		if (aLkw != null) {
			datum.getItem("ALkw").getUnscaledValue("Wert").set(aLkw);
		}
		datum.getItem("ALkw").getItem("G¸te").getUnscaledValue("Index").set(10);

		qPkw = Umrechung.getQPkw(qKfz, qLkw);
		if (qPkw != null) {
			datum.getItem("QPkw").getUnscaledValue("Wert").set(qPkw);
			datum.getItem("QPkw").getItem("G¸te").getUnscaledValue("Index")
					.set(10);
			datum.getItem("QPkw").getItem("Status").getItem("Erfassung")
					.getUnscaledValue("NichtErfasst").setText("Ja");

		}

		vKfz = Umrechung.getVKfz(qLkw, qKfz, vPkw, vLkw);
		if (vKfz != null) {
			datum.getItem("VKfz").getUnscaledValue("Wert").set(vKfz);
			datum.getItem("VKfz").getItem("G¸te").getUnscaledValue("Index")
					.set(10);
			datum.getItem("VKfz").getItem("Status").getItem("Erfassung")
					.getUnscaledValue("NichtErfasst").setText("Ja");
		}

		qb = Umrechung.getQB(qLkw, qKfz, vPkw, vLkw, 0.5f, 1);
		if (qb != null) {
			datum.getItem("QB").getUnscaledValue("Wert").set(qb);
			datum.getItem("QB").getItem("G¸te").getUnscaledValue("Index").set(
					10);
			datum.getItem("QB").getItem("Status").getItem("Erfassung")
					.getUnscaledValue("NichtErfasst").setText("Ja");
		}

		return datum;
	}

}
