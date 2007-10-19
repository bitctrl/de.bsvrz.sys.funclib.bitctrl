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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StrassenSegment;

/**
 * Ein Parameterdatensatz, die Eigenschaften einer Situation beinhaltet. Der
 * Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.situationsEigenschaften".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdSituationsEigenschaften extends
		AbstractParameterDatensatz<PdSituationsEigenschaften.Daten> {

	/**
	 * Die Repräsentation der Daten des Situationseigenschaften-Datensatzes.
	 * 
	 * @author BitCtrl Systems GmbH, Peuker
	 * @version $Id: PdSituationsEigenschaften.java 4508 2007-10-18 05:30:18Z
	 *          peuker $
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Dauer des Situation (sofern bekannt). Eintrag von 0 ms bedeutet //
		 * unbekannte (unendliche) Dauer. ("Dauer")
		 */
		private final long dauer;
		/**
		 * Position des Situationsendes im letzten Straßensegment. ("EndOffset")
		 */
		private final long endOffset;
		/**
		 * Referenzen auf alle Straßensegmente, über die sich die Situation
		 * ausbreitet. ("StraßenSegment")
		 */
		private final List<StrassenSegment> segmente = new ArrayList<StrassenSegment>();
		/**
		 * Position des Situationsanfangs im ersten Straßensegment.
		 * ("StartOffset")
		 */
		private final long startOffset;

		/**
		 * Startzeitpunkt der Situation (Staubeginn, Baustellenbeginn, // etc.).
		 * ("StartZeit")
		 */
		private final long startZeit;

		/**
		 * markiert die Gültigkeit des Datums. ("StartZeit")
		 */
		private boolean valid;

		/**
		 * Standard-Konstruktor zum Erstellen eines leeren Datensatzes.
		 * 
		 */
		Daten() {
			valid = false;
			setZeitstempel(0);
			startZeit = Long.MAX_VALUE;
			dauer = 0;
			startOffset = 0;
			endOffset = 0;
		}

		/**
		 * Konstruktor zu Erstellen einer Kopie des übergebenen Datums.
		 * 
		 * @param daten
		 *            das zu kopierende Datum
		 */
		Daten(Daten daten) {
			this.valid = daten.valid;
			setZeitstempel(daten.getZeitstempel());
			startZeit = daten.startZeit;
			dauer = daten.dauer;
			startOffset = daten.startOffset;
			endOffset = daten.endOffset;
			segmente.addAll(daten.segmente);
		}

		/**
		 * Konstruktor.<br>
		 * Die Funktion erzeugt eine Instnz des Datums und füllt dieses mit dem
		 * Inhalt des übergebenen Datenverteiler-Datensatzes.
		 * 
		 * @param result
		 *            die vom Datenverteiler empfangenen Dtaen
		 */
		Daten(ResultData result) {
			setZeitstempel(result.getDataTime());
			Data daten = result.getData();
			if (daten != null) {
				valid = true;
				startZeit = daten.getTimeValue("StartZeit").getMillis();
				dauer = daten.getTimeValue("Dauer").getMillis();
				Data.Array segmentArray = daten.getArray("StraßenSegment");
				for (int idx = 0; idx < segmentArray.getLength(); idx++) {
					segmente.add((StrassenSegment) ObjektFactory.getInstanz()
							.getModellobjekt(
									segmentArray.getReferenceValue(idx)
											.getSystemObject()));
				}
				startOffset = daten.getUnscaledValue("StartOffset").longValue();
				endOffset = daten.getUnscaledValue("EndOffset").longValue();
			} else {
				valid = false;
				startZeit = Long.MAX_VALUE;
				dauer = 0;
				startOffset = 0;
				endOffset = 0;
			}
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum#clone()
		 */
		@Override
		public Datum clone() {
			return new Daten(this);
		}

		/**
		 * liefert die Dauer der Situation.
		 * 
		 * @return die Dauer
		 */
		public long getDauer() {
			return dauer;
		}

		/**
		 * liefert die Position des Situationsendes im letzten Straßensegment.
		 * 
		 * @return der Offset
		 */
		public long getEndOffset() {
			return endOffset;
		}

		/**
		 * Referenzen auf alle Straßensegmente, über die sich die Situation
		 * ausbreitet.
		 * 
		 * @return die Liste der Segmente
		 */
		public List<StrassenSegment> getSegmente() {
			return segmente;
		}

		/**
		 * liefert die Position des Situationsanfangs im ersten Straßensegment.
		 * 
		 * @return den Offset
		 */
		public long getStartOffset() {
			return startOffset;
		}

		/**
		 * liefert den Startzeitpunkt der Situation (Staubeginn,
		 * Baustellenbeginn, // etc.).
		 * 
		 * @return den Zeitpunkt
		 */
		public long getStartZeit() {
			return startZeit;
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#isValid()
		 */
		public boolean isValid() {
			return valid;
		}

	}

	/**
	 * die Attributgruppe für den Zugriff auf die Parameter.
	 */
	private static AttributeGroup attributGruppe;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz des Parameterdatensatzes auf der Basis
	 * des übergebenen Systemobjekts.
	 * 
	 * @param objekt
	 *            das Systemobjekt
	 */
	public PdSituationsEigenschaften(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.situationsEigenschaften");
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#abmeldenSender()
	 */
	@Override
	public void abmeldenSender() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#erzeugeDatum()
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getAttributGruppe()
	 */
	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getDatum()
	 */
	@Override
	public Daten getDatum() {
		return super.getDatum();
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten datum) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.Data)
	 */
	public void setDaten(ResultData result) {
		checkAttributgruppe(result);
		setDatum(new Daten(result));
	}
}
