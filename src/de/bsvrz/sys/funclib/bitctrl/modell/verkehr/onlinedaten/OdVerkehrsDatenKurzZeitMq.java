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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.onlinedaten.OdVerkehrsDatenKurzZeitMq.Daten.Werte;
import de.bsvrz.sys.funclib.bitctrl.util.dav.Umrechung;

/**
 * Kapselt die Attributgruppe {@code atg.verkehrsDatenKurzZeitMq}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdVerkehrsDatenKurzZeitMq extends
		AbstractOnlineDatensatz<OdVerkehrsDatenKurzZeitMq.Daten> {

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
		private Aspekte(final String pid) {
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
	public static class Daten extends AbstractDatum implements MesswertDatum {

		/** Benennt die Messwerte die dieser Datensatz kennt. */
		public enum Werte {

			/** Lkw-Anteil in Prozent. */
			ALkw,

			/** Bemessungsdichte KB in Fahrzeuge/km. */
			KB,

			/**
			 * Verkehrsst‰rke QKfz (alle Fahrzeuge) in Anzahl pro
			 * Messabschnittsdauer.
			 */
			QKfz,

			/** Verkehrsst‰rke QPkw in Anzahl pro Messabschnittsdauer. */
			QPkw,

			/** Verkehrsst‰rke QLkw in Anzahl pro Messabschnittsdauer. */
			QLkw,

			/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
			VKfz,

			/** Geschwindigkeit VPkw in km/h. */
			VPkw,

			/** Geschwindigkeit VLkw in km/h. */
			VLkw,

			/** Standardabweichung der Kfz-Geschwindigkeiten SKfz in km/h. */
			SKfz,

			/** Belegungsgrad (mit Statusinformationen) in Prozent. */
			B;

		}

		/** Lkw-Anteil in Prozent. */
		private Integer aLkw;

		/** Bemessungsdichte KB in Fahrzeuge/km. */
		private Integer kb;

		/**
		 * Verkehrsst‰rke QKfz (alle Fahrzeuge) in Anzahl pro
		 * Messabschnittsdauer.
		 */
		private Integer qKfz;

		/** Verkehrsst‰rke QPkw in Anzahl pro Messabschnittsdauer. */
		private Integer qPkw;

		/** Verkehrsst‰rke QLkw in Anzahl pro Messabschnittsdauer. */
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

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.aLkw = aLkw;
			klon.qKfz = qKfz;
			klon.qLkw = qLkw;
			klon.qPkw = qPkw;
			klon.vKfz = vKfz;
			klon.vLkw = vLkw;
			klon.vPkw = vPkw;
			klon.kb = kb;
			klon.sKfz = sKfz;
			klon.b = b;
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
			Werte wert = Werte.valueOf(name);
			switch (wert) {
			case ALkw:
				return aLkw;
			case KB:
				return kb;
			case QKfz:
				return qKfz;
			case QLkw:
				return qLkw;
			case QPkw:
				return qPkw;
			case VKfz:
				return vKfz;
			case VLkw:
				return vLkw;
			case VPkw:
				return vPkw;
			case SKfz:
				return sKfz;
			case B:
				return b;
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
			List<String> werte = new ArrayList<String>();

			for (Werte w : Werte.values()) {
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
		protected void setDatenStatus(Status neuerStatus) {
			this.datenStatus = neuerStatus;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum#setWert(java.lang.String,
		 *      java.lang.Number)
		 */
		public void setWert(final String name, final Number wert) {
			Werte w = Werte.valueOf(name);
			switch (w) {
			case ALkw:
				aLkw = wert != null ? wert.intValue() : null;
				break;
			case KB:
				kb = wert != null ? wert.intValue() : null;
				break;
			case QKfz:
				qKfz = wert != null ? wert.intValue() : null;
				break;
			case QLkw:
				qLkw = wert != null ? wert.intValue() : null;
				break;
			case QPkw:
				qPkw = wert != null ? wert.intValue() : null;
				break;
			case VKfz:
				vKfz = wert != null ? wert.intValue() : null;
				break;
			case VLkw:
				vLkw = wert != null ? wert.intValue() : null;
				break;
			case VPkw:
				vPkw = wert != null ? wert.intValue() : null;
				break;
			case SKfz:
				sKfz = wert != null ? wert.intValue() : null;
				break;
			case B:
				b = wert != null ? wert.floatValue() : null;
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
			for (Werte w : Werte.values()) {
				s += ", " + w.name() + "=" + getWert(w.name());
			}

			return s + "]";
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ = "atg.verkehrsDatenKurzZeitMq";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param mq
	 *            der Messquerschnitt dessen Kurzzeitdaten hier betrachtet
	 *            werden.
	 */
	public OdVerkehrsDatenKurzZeitMq(final MessQuerschnittAllgemein mq) {
		super(mq);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ);
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
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Alle Messwerte sind initial "nicht ermittelbar" und die Statuswerte
	 * besitzen alle die gleichen Standardwerte. Die Messwerte aus
	 * {@link OdVerkehrsDatenKurzZeitMq.Daten.Werte} werden &uuml;bernommen.
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(final OdVerkehrsDatenKurzZeitMq.Daten d) {
		final Data datum = erzeugeSendeCache();
		final Integer qKfz;
		final Integer qLkw;
		final Integer vPkw;
		final Integer vLkw;
		final Integer sKfz;
		final Integer kb;
		final Integer b;
		final Integer qPkw;
		final Integer vKfz;
		final Integer qb;
		final Integer aLkw;
		Number n;

		n = d.getWert(Werte.QKfz.name());
		qKfz = n != null ? n.intValue() : null;

		n = d.getWert(Werte.QLkw.name());
		qLkw = n != null ? n.intValue() : null;

		n = d.getWert(Werte.VPkw.name());
		vPkw = n != null ? n.intValue() : null;

		n = d.getWert(Werte.VLkw.name());
		vLkw = n != null ? n.intValue() : null;

		n = d.getWert(Werte.SKfz.name());
		sKfz = n != null ? n.intValue() : null;

		n = d.getWert(Werte.KB.name());
		kb = n != null ? n.intValue() : null;

		n = d.getWert(Werte.B.name());
		b = n != null ? n.intValue() : null;

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

		if (sKfz != null) {
			datum.getItem("SKfz").getUnscaledValue("Wert").set(sKfz);
		}
		datum.getItem("SKfz").getItem("G¸te").getUnscaledValue("Index").set(10);

		if (kb != null) {
			datum.getItem("KB").getUnscaledValue("Wert").set(kb);
		}
		datum.getItem("KB").getItem("G¸te").getUnscaledValue("Index").set(10);

		if (b != null) {
			datum.getItem("B").getUnscaledValue("Wert").set(b);
		}
		datum.getItem("B").getItem("G¸te").getUnscaledValue("Index").set(10);

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

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setDaten(final ResultData result) {
		check(result);

		Daten datum = new Daten();
		if (result.hasData()) {
			Data daten = result.getData();
			NumberValue wert;

			wert = daten.getItem(Daten.Werte.QKfz.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.QKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.QKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.QLkw.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.QLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.QLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.QPkw.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.QPkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.QPkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.VKfz.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.VKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.VKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.VLkw.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.VLkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.VLkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.VPkw.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.VPkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.VPkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.SKfz.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.SKfz.name(), null);
			} else {
				datum.setWert(Daten.Werte.SKfz.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.KB.name())
					.getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.KB.name(), null);
			} else {
				datum.setWert(Daten.Werte.KB.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.ALkw.name()).getUnscaledValue(
					"Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.ALkw.name(), null);
			} else {
				datum.setWert(Daten.Werte.ALkw.name(), wert.intValue());
			}

			wert = daten.getItem(Daten.Werte.B.name()).getScaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.B.name(), null);
			} else {
				datum.setWert(Daten.Werte.B.name(), wert.floatValue());
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
