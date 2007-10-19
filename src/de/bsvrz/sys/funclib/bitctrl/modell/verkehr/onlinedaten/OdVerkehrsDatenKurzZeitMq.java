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
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittAllgemein;

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
			SKfz;

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

		/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
		private boolean valid;

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
			klon.valid = valid;
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
		 */
		public boolean isValid() {
			return valid;
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
	public OdVerkehrsDatenKurzZeitMq(MessQuerschnittAllgemein mq) {
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
	 */
	public synchronized void setDaten(ResultData result) {
		checkAttributgruppe(result);

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

			datum.setValid(true);
		} else {
			datum.setValid(false);
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(OdVerkehrsDatenKurzZeitMq.Daten d) {
		throw new UnsupportedOperationException();
	}

}
