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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment;

/**
 * Implementation des OrtsReferenzStrassenSegmentUndOffsetInterface.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id:$
 * 
 */
public class StrassenSegmentUndOffsetOrtsReferenz implements
		StrassenSegmentUndOffsetOrtsReferenzInterface {

	/** Offset auf dem StraﬂenSegment in Metern. */
	private final long startOffset;

	/** das zugeh&ouml;rige ModellObjekt. */
	private final StrassenSegment modelSegment;

	/**
	 * Erzeugt eine neue Ortsreferenz auf der Basis eines Stra&szlig;ensegmentes
	 * und Offsets.
	 * 
	 * @param pidStrassenSegment
	 *            Stra&szlig;ensegment
	 * @param startOffset
	 *            Offset auf dem Stra&szlig;ensegment.
	 * @throws NetzReferenzException
	 *             wenn die Ortsreferenz nicht erzeugt werden kann.
	 */
	public StrassenSegmentUndOffsetOrtsReferenz(String pidStrassenSegment,
			long startOffset) throws NetzReferenzException {

		if ((pidStrassenSegment == null) || (pidStrassenSegment.length() == 0)) {
			throw new IllegalArgumentException(
					"Das Straﬂensegment darf nicht 'null' oder leer sein");
		}

		if (startOffset < 0) {
			throw new IllegalArgumentException(
					"Der Offset darf nicht negativ sein!");
		}

		this.startOffset = startOffset;

		SystemObject systemObjekt = ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getObject(pidStrassenSegment);

		if (systemObjekt == null) {
			throw new NetzReferenzException("Das Straﬂensegment mit der PID '"
					+ pidStrassenSegment + "' existiert nicht im Datenkatalog!");
		}

		modelSegment = (de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment) ObjektFactory
				.getInstanz().getModellobjekt(systemObjekt);

		if (modelSegment == null) {
			throw new NetzReferenzException("Das Straﬂensegment mit der PID '"
					+ pidStrassenSegment + "' existiert nicht im Datenkatalog!");
		}

		if (modelSegment.getLaenge() < startOffset) {
			throw new NetzReferenzException(
					"Der Offset ist grˆﬂer als die L‰nge des Straﬂensegmentes!");
		}
	}

	/** 
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrassenSegmentUndOffsetOrtsReferenzInterface#ermittleOrtsReferenzAsbStationierung()
	 */
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzAsbStationierung(this);
	}


	/** 
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrassenSegmentUndOffsetOrtsReferenzInterface#ermittleOrtsReferenzStrasseUndBetriebsKilometer()
	 */
	public StrasseUndBetriebsKilometerOrtsReferenzInterface ermittleOrtsReferenzStrasseUndBetriebsKilometer()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzStrasseUndBetriebsKilometer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrassenSegmentUndOffsetOrtsReferenzInterface#getLaengsNeigung()
	 */
	public Integer getLaengsNeigung() throws NetzReferenzException {
		float offsetSTS = 0;

		for (StrassenTeilSegment teilseg : modelSegment
				.getStrassenTeilSegmente()) {
			if (offsetSTS < startOffset
					&& startOffset < offsetSTS + teilseg.getLaenge()) {
				return new Integer(teilseg.getSteigungGefaelle());
			}
			offsetSTS += teilseg.getLaenge();
		}

		throw new NetzReferenzException(
				"Die L‰ngsneigung konnte nicht bestimmt werden");
	}

	/**
	 * Gibt das zugeordnete StrassenSegment der Verkehrsmodells zur&uuml;ck.
	 * 
	 * @return StrassenSegment des Verkehrsmodells
	 */
	public StrassenSegment getModelSegment() {
		return modelSegment;
	}


	/**
	 * Gibt den Namen des Segmentes zur&uuml;ck.
	 * 
	 * @return Name des Segmentes
	 */
	public String getName() {
		return modelSegment.getName();
	}

	/**
	 * Gibt die PID des Segmentes zur&uuml;ck.
	 * 
	 * @return PID des Segmentes
	 */
	public String getPid() {
		return modelSegment.getPid();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrassenSegmentUndOffsetOrtsReferenzInterface#getStartOffset()
	 */
	public long getStartOffset() {
		return startOffset;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.netz.StrassenSegmentUndOffsetOrtsReferenzInterface#getStrassenSegment()
	 */
	public StrassenSegment getStrassenSegment() {
		return modelSegment;
	}

}
