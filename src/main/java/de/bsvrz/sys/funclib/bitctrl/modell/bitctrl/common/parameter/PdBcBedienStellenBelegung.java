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

package de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.parameter;

import java.util.ArrayList;

import com.bitctrl.util.Timestamp;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.objekte.BcBedienStelle;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;

/**
 * Dieser Parameter legt fest, welcher Benutzer auf eine Bedienstelle zugreifen
 * darfs.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class PdBcBedienStellenBelegung
		extends AbstractParameterDatensatz<PdBcBedienStellenBelegung.Daten> {

	/**
	 * Repräsentation der Daten der Onlinedaten.
	 */
	public static class Daten extends ArrayList<Benutzer>implements Datum {

		/** Der Zeitstempel des Datums. */
		private long zeitstempel;

		/** Der aktuelle Status des Datensatzes. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Erzeugt eine flache Kopie.
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(zeitstempel);
			klon.datenStatus = datenStatus;

			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
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

		@Override
		public String toString() {
			String s;

			s = getClass().getName() + "[";
			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;
			s += "]";

			return s;
		}

		@Override
		public String getZeitpunkt() {
			return Timestamp.absoluteTime(getZeitstempel());
		}

		@Override
		public long getZeitstempel() {
			return zeitstempel;
		}

		@Override
		public boolean isValid() {
			return getDatenStatus() == Datum.Status.DATEN;
		}

		@Override
		public void setZeitstempel(final long zeitstempel) {
			this.zeitstempel = zeitstempel;
		}
	}

	/** Die PID der Attributgruppe. */
	public static final String PID_ATG = "atg.bcBedienStellenBelegung";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * Konstruktor.
	 *
	 * @param objekt
	 *            ein Ereignis.
	 */
	public PdBcBedienStellenBelegung(final BcBedienStelle objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(PID_ATG);
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
		final Array mitglieder;

		mitglieder = daten.getArray("Mitglieder");
		mitglieder.setLength(datum.size());
		for (int i = 0; i < mitglieder.getLength(); ++i) {
			mitglieder.getItem(i).asReferenceValue()
					.setSystemObject(datum.get(i).getSystemObject());
		}

		return daten;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();
			final Array mitglieder;
			final ObjektFactory factory = ObjektFactory.getInstanz();

			mitglieder = daten.getArray("Mitglieder");
			for (int i = 0; i < mitglieder.getLength(); ++i) {
				datum.add((Benutzer) factory.getModellobjekt(mitglieder
						.getItem(i).asReferenceValue().getSystemObject()));
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
