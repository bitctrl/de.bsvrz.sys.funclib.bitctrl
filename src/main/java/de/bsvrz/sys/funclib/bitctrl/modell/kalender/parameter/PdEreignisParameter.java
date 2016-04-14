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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter;

import java.util.ArrayList;
import java.util.List;

import com.bitctrl.util.Interval;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.SystemKalenderEintrag;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.parameter.PdEreignisParameter.Daten.VerkehrlicheGueltigkeit;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.NetzBestandTeil;

/**
 * Kapselt die Attributgruppe "atg.ereignisParameter".
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class PdEreignisParameter
extends AbstractParameterDatensatz<PdEreignisParameter.Daten> {

	/**
	 * Repräsentation der Daten des Ereignisparameters.
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Repräsentiert die verkehrliche Gültigkeit.
		 */
		public static class VerkehrlicheGueltigkeit {

			/** Die Eigenschaft {@code VOR_ANFANG}. */
			public static final int VOR_ANFANG = 0;

			/** Die Eigenschaft {@code NACH_ANFANG}. */
			public static final int NACH_ANFANG = 1;

			/** Die Eigenschaft {@code VOR_ENDE}. */
			public static final int VOR_ENDE = 2;

			/** Die Eigenschaft {@code NACH_ENDE}. */
			public static final int NACH_ENDE = 3;

			/** Die Eigenschaft {@code dauerAnfang}. */
			private long dauerAnfang;

			/** Die Eigenschaft {@code bezugAnfang}. */
			private int bezugAnfang;

			/** Die Eigenschaft {@code dauerEnde}. */
			private long dauerEnde;

			/** Die Eigenschaft {@code bezugEnde}. */
			private int bezugEnde;

			/**
			 * Initialisiert das Objekt.
			 */
			public VerkehrlicheGueltigkeit() {
				dauerAnfang = 0;
				bezugAnfang = VOR_ANFANG;
				dauerEnde = 0;
				bezugEnde = NACH_ENDE;
			}

			/**
			 * Gibt den Wert der Eigenschaft {@code bezugAnfang} wieder.
			 *
			 * @return {@code bezugAnfang}.
			 */
			public int getBezugAnfang() {
				return bezugAnfang;
			}

			/**
			 * Gibt den Wert der Eigenschaft {@code bezugEnde} wieder.
			 *
			 * @return {@code bezugEnde}.
			 */
			public int getBezugEnde() {
				return bezugEnde;
			}

			/**
			 * Gibt den Wert der Eigenschaft {@code dauerAnfang} wieder.
			 *
			 * @return {@code dauerAnfang}.
			 */
			public long getDauerAnfang() {
				return dauerAnfang;
			}

			/**
			 * Gibt den Wert der Eigenschaft {@code dauerEnde} wieder.
			 *
			 * @return {@code dauerEnde}.
			 */
			public long getDauerEnde() {
				return dauerEnde;
			}

			/**
			 * Legt den Wert der Eigenschaft {@code bezugAnfang} fest.
			 *
			 * @param bezugAnfang
			 *            der neue Wert von {@code bezugAnfang}.
			 */
			public void setBezugAnfang(final int bezugAnfang) {
				this.bezugAnfang = bezugAnfang;
			}

			/**
			 * Legt den Wert der Eigenschaft {@code bezugEnde} fest.
			 *
			 * @param bezugEnde
			 *            der neue Wert von {@code bezugEnde}.
			 */
			public void setBezugEnde(final int bezugEnde) {
				this.bezugEnde = bezugEnde;
			}

			/**
			 * Legt den Wert der Eigenschaft {@code dauerAnfang} fest.
			 *
			 * @param dauerAnfang
			 *            der neue Wert von {@code dauerAnfang}.
			 */
			public void setDauerAnfang(final long dauerAnfang) {
				this.dauerAnfang = dauerAnfang;
			}

			/**
			 * Legt den Wert der Eigenschaft {@code dauerEnde} fest.
			 *
			 * @param dauerEnde
			 *            der neue Wert von {@code dauerEnde}.
			 */
			public void setDauerEnde(final long dauerEnde) {
				this.dauerEnde = dauerEnde;
			}

		}

		/** Die Eigenschaft {@code raeumlicheGueltigkeit}. */
		private final List<NetzBestandTeil> raeumlicheGueltigkeit = new ArrayList<>();

		/** Die Eigenschaft {@code zeitlicheGueltigkeit}. */
		private Interval zeitlicheGueltigkeit;

		/** Die Eigenschaft {@code systemKalenderEintrag}. */
		private SystemKalenderEintrag systemKalenderEintrag;

		/** Die Eigenschaft {@code verkehrlicheGueltigkeit}. */
		private final List<VerkehrlicheGueltigkeit> verkehrlicheGueltigkeit = new ArrayList<>();

		/** Die Eigenschaft {@code quelle}. */
		private String quelle;

		/** Der aktuelle Status des Datensatzes. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Erzeugt eine flache Kopie.
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			klon.quelle = quelle;
			klon.raeumlicheGueltigkeit.addAll(raeumlicheGueltigkeit);
			klon.systemKalenderEintrag = systemKalenderEintrag;
			klon.zeitlicheGueltigkeit = zeitlicheGueltigkeit;
			klon.verkehrlicheGueltigkeit.addAll(verkehrlicheGueltigkeit);

			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code quelle} wieder.
		 *
		 * @return {@code quelle}.
		 */
		public String getQuelle() {
			return quelle;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code raeumlicheGueltigkeit} wieder.
		 *
		 * @return {@code raeumlicheGueltigkeit}.
		 */
		public List<NetzBestandTeil> getRaeumlicheGueltigkeit() {
			return raeumlicheGueltigkeit;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code systemKalenderEintrag} wieder.
		 *
		 * @return {@code systemKalenderEintrag} oder {@code null}, wenn das
		 *         Ereignis über die zeitliche Gültigkeit definiert ist.
		 */
		public SystemKalenderEintrag getSystemKalenderEintrag() {
			return systemKalenderEintrag;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code verkehrlicheGueltigkeit} wieder.
		 *
		 * @return {@code verkehrlicheGueltigkeit}.
		 */
		public List<VerkehrlicheGueltigkeit> getVerkehrlicheGueltigkeit() {
			return verkehrlicheGueltigkeit;
		}

		/**
		 * Gibt den Wert der Eigenschaft {@code zeitlicheGueltigkeit} wieder.
		 *
		 * @return {@code zeitlicheGueltigkeit} oder {@code null}, wenn das
		 *         Intervall ungültig ist oder das Ereignis über den
		 *         Systemkalendereintrag definiert ist.
		 */
		public Interval getZeitlicheGueltigkeit() {
			return zeitlicheGueltigkeit;
		}

		/**
		 * setzt den aktuellen Datensatzstatus.
		 *
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code quelle} fest.
		 *
		 * @param quelle
		 *            der neue Wert von {@code quelle}.
		 */
		public void setQuelle(final String quelle) {
			this.quelle = quelle;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code systemKalenderEintrag} fest.
		 *
		 * @param systemKalenderEintrag
		 *            der neue Wert von {@code systemKalenderEintrag}.
		 */
		public void setSystemKalenderEintrag(
				final SystemKalenderEintrag systemKalenderEintrag) {
			this.systemKalenderEintrag = systemKalenderEintrag;
		}

		/**
		 * Legt den Wert der Eigenschaft {@code zeitlicheGueltigkeit} fest.
		 *
		 * @param zeitlicheGueltigkeit
		 *            der neue Wert von {@code zeitlicheGueltigkeit}.
		 */
		public void setZeitlicheGueltigkeit(
				final Interval zeitlicheGueltigkeit) {
			this.zeitlicheGueltigkeit = zeitlicheGueltigkeit;
		}

		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;

			return s + "]";
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_EREIGNIS_PARAMETER = "atg.ereignisParameter";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Konstruktor.
	 *
	 * @param objekt
	 *            ein Ereignis.
	 */
	public PdEreignisParameter(final SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_EREIGNIS_PARAMETER);
			assert atg != null;
		}
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		Array feld;

		feld = daten.getArray("RäumlicheGültigkeit");
		feld.setLength(datum.getRaeumlicheGueltigkeit().size());
		for (int i = 0; i < feld.getLength(); i++) {
			feld.getItem(i).asReferenceValue().setSystemObject(
					datum.getRaeumlicheGueltigkeit().get(i).getSystemObject());
		}

		if (datum.getZeitlicheGueltigkeit() == null) {
			daten.getTimeValue("BeginnZeitlicheGültigkeit").setMillis(1);
			daten.getTimeValue("EndeZeitlicheGültigkeit").setMillis(1);
		} else {
			daten.getTimeValue("BeginnZeitlicheGültigkeit")
			.setMillis(datum.getZeitlicheGueltigkeit().getStart());
			daten.getTimeValue("EndeZeitlicheGültigkeit")
			.setMillis(datum.getZeitlicheGueltigkeit().getEnd());
		}

		if (datum.getSystemKalenderEintrag() != null) {
			daten.getReferenceValue("SystemKalenderEintragReferenz")
			.setSystemObject(
					datum.getSystemKalenderEintrag().getSystemObject());
		}

		feld = daten.getArray("VerkehrlicheGültigkeit");
		feld.setLength(datum.getVerkehrlicheGueltigkeit().size());
		for (int i = 0; i < feld.getLength(); i++) {
			final VerkehrlicheGueltigkeit vg;

			vg = datum.getVerkehrlicheGueltigkeit().get(i);

			feld.getItem(i).getTimeValue("ZeitDauerAnfangIntervall")
			.setMillis(vg.getDauerAnfang());
			feld.getItem(i).getUnscaledValue("ZeitBezugAnfangIntervall")
			.set(vg.getBezugAnfang());
			feld.getItem(i).getTimeValue("ZeitDauerEndeIntervall")
			.setMillis(vg.getDauerEnde());
			feld.getItem(i).getUnscaledValue("ZeitBezugEndeIntervall")
			.set(vg.getBezugEnde());
		}

		daten.getTextValue("Quelle").setText(datum.getQuelle());

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final ObjektFactory factory;
			final Data daten;
			Array feld;
			final long start, ende;

			factory = ObjektFactory.getInstanz();
			daten = result.getData();

			feld = daten.getArray("RäumlicheGültigkeit");
			for (int i = 0; i < feld.getLength(); i++) {
				datum.getRaeumlicheGueltigkeit()
				.add((NetzBestandTeil) factory
						.getModellobjekt(feld.getItem(i)
								.asReferenceValue().getSystemObject()));
			}

			start = daten.getTimeValue("BeginnZeitlicheGültigkeit").getMillis();
			ende = daten.getTimeValue("EndeZeitlicheGültigkeit").getMillis();
			if (start == ende) {
				// Definition über Systemkalendereintrag
				datum.setSystemKalenderEintrag(
						(SystemKalenderEintrag) factory.getModellobjekt(daten
								.getReferenceValue(
										"SystemKalenderEintragReferenz")
								.getSystemObject()));
			} else if (start < ende) {
				// Definition über zeitliche Gültigkeit
				datum.setZeitlicheGueltigkeit(new Interval(start, ende));
			} else {
				// Intervall ist ungültig, weil Start größer als Ende
				datum.setZeitlicheGueltigkeit(null);
			}

			feld = daten.getArray("VerkehrlicheGültigkeit");
			for (int i = 0; i < feld.getLength(); i++) {
				final VerkehrlicheGueltigkeit vg;

				vg = new VerkehrlicheGueltigkeit();
				vg.setDauerAnfang(feld.getItem(i)
						.getTimeValue("ZeitDauerAnfangIntervall").getMillis());
				vg.setBezugAnfang(feld.getItem(i)
						.getUnscaledValue("ZeitBezugAnfangIntervall")
						.intValue());
				vg.setDauerEnde(feld.getItem(i)
						.getTimeValue("ZeitDauerEndeIntervall").getMillis());
				vg.setBezugEnde(feld.getItem(i)
						.getUnscaledValue("ZeitBezugEndeIntervall").intValue());

				datum.getVerkehrlicheGueltigkeit().add(vg);
			}

			datum.setQuelle(daten.getTextValue("Quelle").getText());
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
