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
 * @version $Id: OrtsReferenzStrassenSegmentUndOffset.java 7485 2008-03-17
 *          14:49:20Z gieseler $
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
	 * @see de.bsvrz.kex.isis.isis.OrtsReferenzStrassenSegmentUndOffsetInterface#ermittleOrtsReferenzAsbStationierung()
	 */
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzAsbStationierung(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.OrtsReferenzStrassenSegmentUndOffsetInterface#ermittleOrtsReferenzStrasseUndBetriebsKilometer()
	 */
	public StrasseUndBetriebsKilometerOrtsReferenzInterface ermittleOrtsReferenzStrasseUndBetriebsKilometer()
			throws NetzReferenzException {
		return NetzReferenzen.getInstanz()
				.ermittleOrtsReferenzStrasseUndBetriebsKilometer(this);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.OrtsReferenzStrassenSegmentUndOffsetInterface#getLaengsNeigung()
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
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.StrassenSegmentInterface#getName()
	 */
	public String getName() {
		return modelSegment.getName();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.StrassenSegmentInterface#getPid()
	 */
	public String getPid() {
		return modelSegment.getPid();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.OrtsReferenzStrassenSegmentUndOffsetInterface#getStartOffset()
	 */
	public long getStartOffset() {
		return startOffset;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.OrtsReferenzStrassenSegmentUndOffsetInterface#getStrassenSegment()
	 */
	public StrassenSegment getStrassenSegment() {
		return modelSegment;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.kex.isis.isis.OrtsReferenzStrassenSegmentUndOffsetInterface#getZuflussMessQuerschnitt()
	 */
//	public MessQuerschnittInterface getZuflussMessQuerschnitt()
//			throws NetzReferenzException {
//		List<MessQuerschnittAllgemein> mqs = modelSegment.getMessquerschnitte();
//		List<MessQuerschnittAllgemein> aufsegment = new ArrayList<MessQuerschnittAllgemein>();
//
//		MessQuerschnittAllgemein foundmq = null;
//
//		// bestimme alle MQ auf dem Segment
//		for (MessQuerschnittAllgemein mq : mqs) {
//			if (mq.getLinie().equals(modelSegment)) {
//				aufsegment.add(mq);
//			}
//		}
//
//		if (aufsegment.size() > 0) {
//			// sortieren nach Offset
//			Collections.sort(aufsegment,
//					new Comparator<MessQuerschnittAllgemein>() {
//						public int compare(final MessQuerschnittAllgemein o1,
//								final MessQuerschnittAllgemein o2) {
//							return new Float(o1.getOffset())
//									.compareTo(new Float(o2.getOffset()));
//						}
//					});
//
//			// passenden suchen
//			MessQuerschnittAllgemein vor = null;
//			for (MessQuerschnittAllgemein mq : aufsegment) {
//				if (mq.getOffset() < startOffset) {
//					vor = mq;
//				}
//
//				if (foundmq == null || mq.getOffset() > foundmq.getOffset()) {
//					foundmq = mq;
//				}
//
//				if (foundmq != null && mq.getOffset() > startOffset) {
//					break;
//				}
//			}
//			if (vor != null) {
//				foundmq = vor;
//			}
//		}
//
//		if (foundmq != null) {
//			return new MessQuerschnitt(foundmq);
//		}
//
//		return null;
//	}

}
