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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.Comparator;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktLiegtAufLinienObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktLiegtAufLinienObjektmpl;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXY;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;

/**
 * Repr&auml;ssentiert einen allgemeinen Messquerschnitt.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann, Peuker
 * @version $Id$
 */
public abstract class MessQuerschnittAllgemein extends StoerfallIndikator
		implements PunktXY, PunktLiegtAufLinienObjekt {

	/**
	 * Definiert eine Ordung auf Messquerschnitte nach deren Offset. Die Ordnung
	 * ist nur innerhalb des selben Stra&szlig;ensegments korrekt.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id: MessQuerschnittAllgemein.java 4123 2007-10-05 14:03:15Z
	 *          Schumann $
	 */
	static class MessQuerschnittComparator implements
			Comparator<MessQuerschnittAllgemein> {

		/**
		 * Vergleicht die beiden Messquerschnitte nach ihrem Offset. Der
		 * vergleich ist nur dann sinnvoll, wenn sich beide Messquerschnitte auf
		 * dem gleichen Stra&szlig;ensegment befinde.
		 * <p>
		 * {@inheritDoc}
		 */
		public int compare(final MessQuerschnittAllgemein mq1,
				final MessQuerschnittAllgemein mq2) {
			return Float.compare(mq1.getStrassenSegmentOffset(), mq2
					.getStrassenSegmentOffset());
		}

	}

	/**
	 * Das Stra&szlig;enteilsegment auf dem der Messquerschnitt liegt. Die
	 * Information ist konfigurierend, muss aber aufwendig zusammengesucht
	 * werden, weswegen die Liste nur bei Bedarf erstellt wird.
	 */
	private StrassenTeilSegment strassenTeilSegment;

	/**
	 * das Objekt, mit dem die Punkteigenschaften dess MQ repr�sentiert werden.
	 */
	private PunktXY punkt;

	/**
	 * das Objekt, mit dem die PunktLiegtAufLinie-Eigenschaften dess MQ
	 * repr�sentiert werden.
	 */
	private PunktLiegtAufLinienObjekt punktLiegtAufLinie;

	/**
	 * Erzeugt einen allgemeinen Messquerschnitt aus einem Systemobjekt.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein MessQuerschnittAllgemein sein
	 *            muss
	 */
	public MessQuerschnittAllgemein(final SystemObject obj) {
		super(obj);

		punkt = new PunktXYImpl(obj);
		punktLiegtAufLinie = new PunktLiegtAufLinienObjektmpl(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein MessquerschnittAllgemein.");
		}
	}

	/**
	 * liefert die konfigurierten Koordinaten, an denen sich der Me�querschnitt
	 * befindet.
	 * 
	 * @return die Position oder <code>null</code>, wenn keine konfiguriert
	 *         wurde
	 */
	public Punkt getKoordinate() {
		return punkt.getKoordinate();
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktLiegtAufLinienObjekt#getLinie()
	 */
	public StrassenSegment getLinie() {
		return (StrassenSegment) punktLiegtAufLinie.getLinie();
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.PunktLiegtAufLinienObjekt#getOffset()
	 */
	public float getOffset() {
		return punktLiegtAufLinie.getOffset();
	}

	/**
	 * Gibt das Stra&szlig;ensegment zur&uuml;ck, auf dem der Messquerschnitt
	 * liegt.
	 * 
	 * @return Ein Stra&szlig;ensegment
	 */
	public StrassenSegment getStrassenSegment() {
		return getLinie();
	}

	/**
	 * Gibt die Position des Messquerschnitts auf dem Stra&szlig;ensegment als
	 * Offset zur L&auml;nge des Stra&szlig;ensegments zur&uuml;ck.
	 * 
	 * @return Der Offset
	 */
	public float getStrassenSegmentOffset() {
		return getOffset();
	}

	/**
	 * Gibt das Stra&szlig;enteilsegment zur&uuml;ck auf dem sich der
	 * Messquerschnitt befindet.
	 * 
	 * @return Das Stra&szlig;enteilsegment
	 */
	public StrassenTeilSegment getStrassenTeilSegment() {
		if (strassenTeilSegment != null) {
			return strassenTeilSegment;
		}

		float offsetSS = 0;
		StrassenTeilSegment sts = null;

		if (getStrassenSegment() != null) {
			// Das richtige STS ist letzte f�r das gilt offset(MQ) < offsetSS,
			// wenn keines gefunden wird wird das letzte geliefert
			for (StrassenTeilSegment s : getStrassenSegment()
					.getStrassenTeilSegmente()) {
				sts = s;
				if (offsetSS < getOffset()
						&& getOffset() < offsetSS + s.getLaenge()) {
					break;
				}
				offsetSS += s.getLaenge();
			}
			strassenTeilSegment = sts;
		}

		return strassenTeilSegment;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.MESSQUERSCHNITTALLGEMEIN;
	}
}