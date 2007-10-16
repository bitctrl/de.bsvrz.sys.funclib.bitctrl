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

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.NumberValue;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.Wert;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittAllgemein;

/**
 * Kapselt die Attributgruppe {@code atg.verkehrsDatenKurzZeitMq}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdVerkehrsDatenKurzZeitMq extends AbstractOnlineDatensatz
		implements MesswertDatensatz {

	/** Benennt die Messwerte die dieser Datensatz kennt. */
	public enum Werte implements Wert {

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

	/** Die PID der Attributgruppe. */
	public static final String ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ = "atg.verkehrsDatenKurzZeitMq";

	/** Die PID des Aspekts. */
	public static final String ASP_ANALYSE = "asp.analyse";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspAnalyse;

	/** Lkw-Anteil in Prozent. */
	private Integer aLkw;

	/** Bemessungsdichte KB in Fahrzeuge/km. */
	private Integer kb;

	/** Verkehrsstärke QKfz (alle Fahrzeuge) in Anzahl pro Messabschnittsdauer. */
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
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatensatz#getWert(de.bsvrz.sys.funclib.bitctrl.modell.Wert)
	 */
	public Number getWert(Wert wert) {
		if (wert.equals(Werte.ALkw)) {
			return aLkw;
		} else if (wert.equals(Werte.KB)) {
			return kb;
		} else if (wert.equals(Werte.QKfz)) {
			return qKfz;
		} else if (wert.equals(Werte.QLkw)) {
			return qLkw;
		} else if (wert.equals(Werte.QPkw)) {
			return qPkw;
		} else if (wert.equals(Werte.VKfz)) {
			return vKfz;
		} else if (wert.equals(Werte.VLkw)) {
			return vLkw;
		} else if (wert.equals(Werte.VPkw)) {
			return vPkw;
		} else if (wert.equals(Werte.SKfz)) {
			return sKfz;
		}
		throw new IllegalArgumentException("Der Datensatz " + toString()
				+ " kennt keinen Wert " + wert + ".");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.MesswertDatensatz#getWerte()
	 */
	public Wert[] getWerte() {
		return Werte.values();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(Data daten) {
		if (!daten.getName().equals(ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ)) {
			throw new IllegalArgumentException(
					"Das Datum muss zur Attributgruppe "
							+ ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ + " gehören.");
		}
		NumberValue wert;

		wert = daten.getItem("QKfz").getUnscaledValue("Wert");
		if (wert.isState()) {
			qKfz = null;
		} else {
			qKfz = wert.intValue();
		}

		wert = daten.getItem("QLkw").getUnscaledValue("Wert");
		if (wert.isState()) {
			qLkw = null;
		} else {
			qLkw = wert.intValue();
		}

		wert = daten.getItem("QPkw").getUnscaledValue("Wert");
		if (wert.isState()) {
			qPkw = null;
		} else {
			qPkw = wert.intValue();
		}

		wert = daten.getItem("VKfz").getUnscaledValue("Wert");
		if (wert.isState()) {
			vKfz = null;
		} else {
			vKfz = wert.intValue();
		}

		wert = daten.getItem("VLkw").getUnscaledValue("Wert");
		if (wert.isState()) {
			vLkw = null;
		} else {
			vLkw = wert.intValue();
		}

		wert = daten.getItem("VPkw").getUnscaledValue("Wert");
		if (wert.isState()) {
			vPkw = null;
		} else {
			vPkw = wert.intValue();
		}

		wert = daten.getItem("SKfz").getUnscaledValue("Wert");
		if (wert.isState()) {
			sKfz = null;
		} else {
			sKfz = wert.intValue();
		}

		wert = daten.getItem("KB").getUnscaledValue("Wert");
		if (wert.isState()) {
			kb = null;
		} else {
			kb = wert.intValue();
		}

		wert = daten.getItem("ALkw").getUnscaledValue("Wert");
		if (wert.isState()) {
			aLkw = null;
		} else {
			aLkw = wert.intValue();
		}
	}

}
