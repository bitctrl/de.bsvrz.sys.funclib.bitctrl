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

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.onlinedaten;

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
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UfdsWindGeschwindigkeitMittelWert;

/**
 * Kapselt die Attriburgruppe {@code atg.ufdsWindGeschwindleitMittelWert}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdUfdsWindGeschwindigkeitMittelWert extends
		AbstractOnlineDatensatz {

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum implements MesswertDatum {

		/**
		 * Die bekannten Messwerte am dem Datensatz.
		 */
		public enum Werte {

			/** Die mittlere Windgeschwindigkeit in m/s. */
			WindGeschwindigkeitMittelWert;

		}

		/** Helligkeit in m/s. */
		private Double windGeschwindigkeitMittelWert;

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.windGeschwindigkeitMittelWert = windGeschwindigkeitMittelWert;
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
			case WindGeschwindigkeitMittelWert:
				return windGeschwindigkeitMittelWert;
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
			case WindGeschwindigkeitMittelWert:
				windGeschwindigkeitMittelWert = wert != null ? wert
						.doubleValue() : null;
				break;
			default:
				throw new IllegalArgumentException("Das Datum " + getClass()
						+ " kennt keinen Wert " + wert + ".");
			}
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_UFDS_HELLIGKEIT = "atg.ufdsWindGeschwindigkeitMittelWert";

	/** Die PID des Aspekts. */
	public static final String ASP_MESS_WERT_ERSETZUNG = "asp.messWertErsetzung";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspMessWertErsetzung;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param sensor
	 *            der Umfelddatensensor dessen Daten hier betrachtet werden.
	 */
	public OdUfdsWindGeschwindigkeitMittelWert(
			UfdsWindGeschwindigkeitMittelWert sensor) {
		super(sensor);

		if (atg == null && aspMessWertErsetzung == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_UFDS_HELLIGKEIT);
			aspMessWertErsetzung = modell.getAspect(ASP_MESS_WERT_ERSETZUNG);
			assert atg != null && aspMessWertErsetzung != null;
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
		return aspMessWertErsetzung;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getSendeAspekt() {
		return aspMessWertErsetzung;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(ResultData result) {
		if (!result.getDataDescription().getAttributeGroup().equals(
				getAttributGruppe())) {
			throw new IllegalArgumentException(
					"Das Datum muss zur Attributgruppe " + getAttributGruppe()
							+ " gehören.");
		}

		Data daten = result.getData();
		NumberValue wert;
		Daten datum = new Daten();

		wert = daten.getItem(Daten.Werte.WindGeschwindigkeitMittelWert.name())
				.getUnscaledValue("Wert");
		if (wert.isState()) {
			datum.setWert(Daten.Werte.WindGeschwindigkeitMittelWert.name(),
					null);
		} else {
			datum.setWert(Daten.Werte.WindGeschwindigkeitMittelWert.name(),
					wert.doubleValue());
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(datum);
		setValid(true);
		fireDatensatzAktualisiert(datum.clone());
	}

}
