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
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.NumberValue;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittAllgemein;

/**
 * Kapselt die Attributgruppe {@code atg.verkehrsDatenKurzZeitMq}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdVerkehrsDatenKurzZeitMq extends AbstractOnlineDatensatz {

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
			 * Verkehrsstärke QKfz (alle Fahrzeuge) in Anzahl pro
			 * Messabschnittsdauer.
			 */
			QKfz,

			/** Verkehrsstärke QPkw in Anzahl pro Messabschnittsdauer. */
			QPkw,

			/** Verkehrsstärke QLkw in Anzahl pro Messabschnittsdauer. */
			QLkw,

			/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
			VKfz,

			/** Geschwindigkeit VPkw in km/h. */
			VPkw,

			/** Geschwindigkeit VLkw in km/h. */
			VLkw,

			/** Standardabweichung der Kfz-Geschwindigkeiten SKfz in km/h. */
			SKfz;

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
			return klon;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum#getWert(java.lang.String)
		 */
		public Number getWert(String name) {
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
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum#setWert(java.lang.String,
		 *      java.lang.Number)
		 */
		public void setWert(String name, Number wert) {
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
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
						+ " kennt keinen Wert " + wert + ".");
			}
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ = "atg.verkehrsDatenKurzZeitMq";

	/** Die PID des Aspekts. */
	public static final String ASP_ANALYSE = "asp.analyse";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspAnalyse;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param mq
	 *            der Messquerschnitt dessen Kurzzeitdaten hier betrachtet
	 *            werden.
	 */
	public OdVerkehrsDatenKurzZeitMq(MessQuerschnittAllgemein mq) {
		super(mq);

		if (atg == null && aspAnalyse == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ);
			aspAnalyse = modell.getAspect(ASP_ANALYSE);
			assert atg != null && aspAnalyse != null;
		}
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
	@Override
	public Aspect getEmpfangsAspekt() {
		return aspAnalyse;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getSendeAspekt() {
		return aspAnalyse;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setDaten(ResultData result) {
		if (!result.getDataDescription().getAttributeGroup().equals(
				getAttributGruppe())) {
			throw new IllegalArgumentException(
					"Das Datum muss zur Attributgruppe " + getAttributGruppe()
							+ " gehören.");
		}

		if (!result.hasData()) {
			setValid(false);
			setDatum(null);
			fireDatensatzAktualisiert(null);
		}

		Data daten = result.getData();
		Daten datum = new Daten();
		NumberValue wert;

		wert = daten.getItem(Daten.Werte.QKfz.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.QKfz.name(), null);
		} else {
			datum.setWert(Daten.Werte.QKfz.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.QLkw.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.QLkw.name(), null);
		} else {
			datum.setWert(Daten.Werte.QLkw.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.QPkw.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.QPkw.name(), null);
		} else {
			datum.setWert(Daten.Werte.QPkw.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.VKfz.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.VKfz.name(), null);
		} else {
			datum.setWert(Daten.Werte.VKfz.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.VLkw.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.VLkw.name(), null);
		} else {
			datum.setWert(Daten.Werte.VLkw.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.VPkw.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.VPkw.name(), null);
		} else {
			datum.setWert(Daten.Werte.VPkw.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.SKfz.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.SKfz.name(), null);
		} else {
			datum.setWert(Daten.Werte.SKfz.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.KB.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.KB.name(), null);
		} else {
			datum.setWert(Daten.Werte.KB.name(), wert.intValue());
		}

		wert = daten.getItem(Daten.Werte.ALkw.name()).getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.ALkw.name(), null);
		} else {
			datum.setWert(Daten.Werte.ALkw.name(), wert.intValue());
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(datum);
		setValid(true);
		fireDatensatzAktualisiert(datum.clone());
	}

}
