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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.StrassenSegment;

/**
 * Ein Parameterdatensatz, die Eigenschaften einer Situation beinhaltet. Der
 * Datensatz repr‰sentiert die Daten einer Attributgruppe
 * "atg.situationsEigenschaften".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdSituationsEigenschaften extends AbstractParameterDatensatz {

	/**
	 * die Attributgruppe f¸r den Zugriff auf die Parameter.
	 */
	private static AttributeGroup attributGruppe;

	/**
	 * Startzeitpunkt der Situation (Staubeginn, Baustellenbeginn, // etc.).
	 * ("StartZeit")
	 */
	private long startZeit;

	/**
	 * Dauer des Situation (sofern bekannt). Eintrag von 0 ms bedeutet //
	 * unbekannte (unendliche) Dauer. ("Dauer")
	 */
	private long dauer;

	/**
	 * Referenzen auf alle Straﬂensegmente, ¸ber die sich die Situation
	 * ausbreitet. ("StraﬂenSegment")
	 */
	private final List<StrassenSegment> segmente = new ArrayList<StrassenSegment>();

	/**
	 * Position des Situationsanfangs im ersten Straﬂensegment. ("StartOffset")
	 */
	long startOffset;

	/**
	 * Position des Situationsendes im letzten Straﬂensegment. ("EndOffset")
	 */
	long endOffset;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz des Parameterdatensatzes auf der Basis
	 * des ¸bergebenen Systemobjekts.
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
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getAttributGruppe()
	 */
	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
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
	 * liefert die Position des Situationsendes im letzten Straﬂensegment.
	 * 
	 * @return der Offset
	 */
	public long getEndOffset() {
		return endOffset;
	}

	/**
	 * Referenzen auf alle Straﬂensegmente, ¸ber die sich die Situation
	 * ausbreitet.
	 * 
	 * @return die Liste der Segmente
	 */
	public List<StrassenSegment> getSegmente() {
		return segmente;
	}

	/**
	 * liefert die Position des Situationsanfangs im ersten Straﬂensegment.
	 * 
	 * @return den Offset
	 */
	public long getStartOffset() {
		return startOffset;
	}

	/**
	 * liefert den Startzeitpunkt der Situation (Staubeginn, Baustellenbeginn, //
	 * etc.).
	 * 
	 * @return den Zeitpunkt
	 */
	public long getStartZeit() {
		return startZeit;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#sendeDaten()
	 */
	@Override
	public void sendeDaten() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.Data)
	 */
	public void setDaten(Data daten) {
		if (daten != null) {
			startZeit = daten.getTimeValue("StartZeit").getMillis();
			dauer = daten.getTimeValue("Dauer").getMillis();
			segmente.clear();
			Data.Array segmentArray = daten.getArray("StraﬂenSegment");
			for (int idx = 0; idx < segmentArray.getLength(); idx++) {
				segmente.add((StrassenSegment) ObjektFactory.getInstanz()
						.getModellobjekt(
								segmentArray.getReferenceValue(idx)
										.getSystemObject()));
			}
			startOffset = daten.getUnscaledValue("StartOffset").longValue();
			endOffset = daten.getUnscaledValue("EndOffset").longValue();
		}
	}
}
