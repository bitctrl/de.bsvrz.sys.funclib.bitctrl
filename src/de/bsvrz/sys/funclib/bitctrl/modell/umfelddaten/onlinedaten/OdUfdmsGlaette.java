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
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfeldDatenMessStelle;

/**
 * Kapselt die Attriburgruppe {@code atg.ufdmsGl&auml;tte}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdUfdmsGlaette extends
		AbstractOnlineDatensatz<OdUfdmsGlaette.Daten> {

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum implements MesswertDatum {

		/**
		 * Die bekannten Messwerte am dem Datensatz.
		 */
		public enum Werte {

			/** Die Gl‰tte als Zustand. */
			Gl‰tte;

		}

		/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
		private boolean valid;
		/** Zustandswert der Gl&auml;tte. */
		private Integer glaette;

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.glaette = glaette;
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
			case Gl‰tte:
				return glaette;
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
			case Gl‰tte:
				glaette = wert != null ? wert.intValue() : null;
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
	public static final String ATG_UFDMS_GLAETTE = "atg.ufdmsGl‰tte";

	/** Die PID des Aspekts. */
	public static final String ASP_PROGNOSE = "asp.prognose";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspPrognose;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param messstelle
	 *            die Messstelle dessen Daten hier betrachtet werden.
	 */
	public OdUfdmsGlaette(UmfeldDatenMessStelle messstelle) {
		super(messstelle);

		if (atg == null || aspPrognose == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_UFDMS_GLAETTE);
			aspPrognose = modell.getAspect(ASP_PROGNOSE);
			assert atg != null && aspPrognose != null;
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
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getEmpfangsAspekt() {
		return aspPrognose;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getSendeAspekt() {
		return aspPrognose;
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

			wert = daten.getItem("AktuellerZustand").getUnscaledValue("Wert");
			if (wert.isState()) {
				datum.setWert(Daten.Werte.Gl‰tte.name(), null);
			} else {
				datum.setWert(Daten.Werte.Gl‰tte.name(), wert.intValue());
			}

			datum.setValid(true);
		} else {
			datum.setValid(false);
		}

		datum.setZeitstempel(result.getDataTime());
		setDatum(datum);
		fireDatensatzAktualisiert(datum.clone());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten d) {
		throw new UnsupportedOperationException();
	}

}
