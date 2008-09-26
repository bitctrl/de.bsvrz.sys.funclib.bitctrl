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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.ASBStationierungsRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.netz.NetzInterface.FahrtRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.tmc.zustaende.TmcRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.AeusseresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.VerkehrModellNetz;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.AsbStationierung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.BetriebsKilometer;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.Verkehrsrichtung;

/**
 * Klasse zur Umrechnung zwischen den Referenzierungsarten.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id: NetzReferenzen.java 9929 2008-06-26 09:53:00Z gieseler $
 * 
 */

public class NetzReferenzen {

	/**
	 * Repr&auml;sentiert eine ASB-Stationierung auf einem
	 * Stra&szlig;enteilsegment.
	 * 
	 * @author BitCtrl Systems GmbH, Gieseler
	 * @version $Id: NetzReferenzen.java 9929 2008-06-26 09:53:00Z gieseler $
	 * 
	 */
	private class TeilSegmentASB {
		/** das Stra&szlig;enteilsegment. */
		private final StrassenTeilSegment teilSegment;

		/** die ASB-Stationierungsangabe. */
		private final AsbStationierung asbStationierung;

		/**
		 * Erzeugt eine Instanz.
		 * 
		 * @param teilSegment
		 *            Stra&szlig;enteilsegment
		 * @param asbStationierung
		 *            ASB-Stationierungsangabe
		 */
		public TeilSegmentASB(StrassenTeilSegment teilSegment,
				AsbStationierung asbStationierung) {
			super();
			this.teilSegment = teilSegment;
			this.asbStationierung = asbStationierung;
		}

		/**
		 * Gibt die ASB-Stationierung zur&uuml;ck.
		 * 
		 * @return ASB-Stationierung
		 */
		protected AsbStationierung getAsbStationierung() {
			return asbStationierung;
		}

		/**
		 * Gibt das Stra&szlig;enteilsegment zur&uuml;ck.
		 * 
		 * @return Stra&szlig;enteilsegment
		 */
		protected StrassenTeilSegment getTeilSegment() {
			return teilSegment;
		}
	}

	/** Das Singleton der Klasse. */
	private static NetzReferenzen singleton;

	/**
	 * Gibt das einzige Objekt der Klasse zur&uuml;ck.
	 * 
	 * @return das Singleton der Klasse.
	 */
	public static NetzReferenzen getInstanz() {
		if (singleton == null) {
			singleton = new NetzReferenzen();
		}

		return singleton;
	}

	/** Das Netzmodell. */
	private VerkehrModellNetz netzmodell;

	/**
	 * Berechnet die Skalierung zur Korrektur der Offsets bzw.
	 * Stationierungswerte als Verh&auml;ltnis von L&auml;nge des Teilsegmentes
	 * zur Stationierungsl&auml;nge in ASB. Die Skalierung ist notwendig, da die
	 * L&auml;ngen der Teilsegmente die reale L&auml;nge des Polygonzuges
	 * darstellt, die von den importierten Stationierungsangaben abweichen kann,
	 * da die Stationierungsangaben die L&auml;nge des Abschnittes/Astes
	 * beschreiben, der nicht mit dem Polygonzug des Segmentes
	 * &uuml;bereinstimmen muss (liegt bei 2-streifigen Stra&szlig;en i.d.R. in
	 * der Mitte)
	 * 
	 * @param teilsegment
	 *            Teilsegment
	 * @return Skalierungsfaktor
	 */
	private double berechneAsbSkalierung(final StrassenTeilSegment teilsegment) {
		List<AsbStationierung> asbliste = teilsegment.getAsbStationierung();

		double asbLaenge = asbliste.get(asbliste.size() - 1).getEnde()
				- asbliste.get(0).getAnfang();
		double segmentLaenge = teilsegment.getLaenge();

		return segmentLaenge / asbLaenge;
	}

	/**
	 * Rechnet Ortsreferenzen mit Stra&szlig;enSegment und den Offset in
	 * Ortsangabe &uuml;ber das ASB-Stationierungssystem um.
	 * 
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *            Stra&szlig;enSegment und den Offset vom Anfang des
	 *            Stra&szlig;enSegments dargestellt wird.
	 * 
	 * @return Ortsreferenz nach dem ASB-Stationierungssystem.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung(
			StrassenSegmentUndOffsetOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			StrassenTeilSegment teilsegment = findeTeilSegmentAmOffset(referenz);

			StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
					.getModelSegment();

			double offsetImTeilsegment = referenz.getStartOffset()
					- segment.getTeilSegmentOffset(teilsegment);

			AsbStationierung asb = findeAsbStationierungAmOffset(teilsegment,
					offsetImTeilsegment);

			long stationierung = (asb.getAnfang() + offsetKorrigieren(
					teilsegment, asb, offsetImTeilsegment - asb.getOffset()));

			ASBStationierungsRichtung richtung;
			if (teilsegment.getAsbStationierung().get(0).getVerkehrsRichtung() == Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG) {
				richtung = ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG;
			} else {
				richtung = ASBStationierungsRichtung.IN_STATIONIERUNGSRICHTUNG;
			}

			return new AsbStationierungOrtsReferenz(asb.getAnfangsKnoten(), asb
					.getEndKnoten(), richtung, stationierung);
		} catch (Exception e) {
			throw new NetzReferenzException("Keine Abbildung möglich: "
					+ e.getMessage());
		}
	}

	/**
	 * Rechnet Ortsreferenz mit Stra&szlig;e und Betriebskilometer in Ortsangabe
	 * &uuml;ber das ASB-Stationierungssystem um.
	 * 
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlige und
	 *            den Betriebskilometer dargestellt wird.
	 * 
	 * @return Ortsreferenz nach dem ASB-Stationierungssystem.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung(
			StrasseUndBetriebsKilometerOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			ClientDavInterface dav = ObjektFactory.getInstanz().getVerbindung();

			SystemObject systemObjekt = dav.getDataModel().getObject(
					referenz.getStrasse().getPid());

			if (systemObjekt == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse modellstrasse = (de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse) ObjektFactory
					.getInstanz().getModellobjekt(systemObjekt);

			if (modellstrasse == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			// entsprechend Absprache ist die Strasse in positiver TMC-Richtung
			// definiert
			TmcRichtung tmcrichtung = referenz.getFahrtRichtung().equals(
					NetzInterface.FahrtRichtung.IN_RICHTUNG) ? TmcRichtung.POSITIV
					: TmcRichtung.NEGATIV;

			for (AeusseresStrassenSegment segment : modellstrasse
					.getAuessereStrassensegmente()) {
				if (!segment.getTmcRichtung().equals(tmcrichtung)) {
					continue;
				}

				for (StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (BetriebsKilometer bk : teilsegment
							.getBetriebsKilometer()) {
						if (Integer.parseInt(bk.getBlockNummer()) == referenz
								.getBlockNummer()
								&& bk.getWert() == referenz
										.getBetriebsKilometer()) {
							double offsetImTeilsegment = bk.getOffset();

							AsbStationierung asb = findeAsbStationierungAmOffset(
									teilsegment, offsetImTeilsegment);

							String anfangsKnoten = asb.getAnfangsKnoten();
							String endKnoten = asb.getEndKnoten();
							long stationierung = (long) (asb.getAnfang() + (referenz
									.getBetriebsKilometer() - segment
									.getTeilSegmentOffset(teilsegment)));

							ASBStationierungsRichtung richtung;
							if (asb.getVerkehrsRichtung() == Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG) {
								richtung = ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG;
							} else {
								richtung = ASBStationierungsRichtung.IN_STATIONIERUNGSRICHTUNG;
							}

							return new AsbStationierungOrtsReferenz(
									anfangsKnoten, endKnoten, richtung,
									stationierung);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new NetzReferenzException("Keine Abbildung möglich: "
					+ e.getMessage());
		}
		throw new NetzReferenzException("Keine Abbildung möglich");
	}

	/**
	 * Rechnet Ortsreferenzen vom ASB-Stationierungssystem in Angaben mit
	 * Stra&szlig;enSegment und den Offset vom Anfang des Stra&szlig;enSegments
	 * um.
	 * 
	 * @param referenz
	 *            Ortsreferenz nach dem ASB-Stationierungssystem.
	 * 
	 * @return Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *         Stra&szlig;enSegment und den Offset vom Anfang des
	 *         Stra&szlig;enSegments dargestellt wird.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public List<StrassenSegmentUndOffsetOrtsReferenzInterface> ermittleOrtsReferenzStrassenSegmentUndOffset(
			AsbStationierungOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			TeilSegmentASB asbsegment = findeTeilSegmentAsbStationierung(referenz);

			List<StrassenSegmentUndOffsetOrtsReferenzInterface> refliste = new ArrayList<StrassenSegmentUndOffsetOrtsReferenzInterface>();

			for (StrassenSegment asegment : asbsegment.getTeilSegment()
					.getStrassenSegment()) {
				long offset = (long) (asegment.getTeilSegmentOffset(asbsegment
						.getTeilSegment()) + stationierungKorrigieren(
						asbsegment.getTeilSegment(), referenz
								.getStationierung()
								- asbsegment.getAsbStationierung().getAnfang()));
				StrassenSegmentUndOffsetOrtsReferenz ref = new StrassenSegmentUndOffsetOrtsReferenz(
						asegment.getPid(), offset);
				refliste.add(ref);
			}

			return refliste;
		} catch (Exception e) {
			throw new NetzReferenzException("Keine Abbildung möglich: "
					+ e.getMessage());
		}
	}

	/**
	 * Rechnet Ortsreferenz mit Stra&szlig;e und Betriebskilometer in Angaben
	 * mit Stra&szlig;enSegment und den Offset vom Anfang des
	 * Stra&szlig;enSegments um.
	 * 
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlige und
	 *            den Betriebskilometer dargestellt wird.
	 * 
	 * @return Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *         Stra&szlig;enSegment und den Offset vom Anfang des
	 *         Stra&szlig;enSegments dargestellt wird.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public StrassenSegmentUndOffsetOrtsReferenzInterface ermittleOrtsReferenzStrassenSegmentUndOffset(
			StrasseUndBetriebsKilometerOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			ClientDavInterface dav = ObjektFactory.getInstanz().getVerbindung();

			SystemObject systemObjekt = dav.getDataModel().getObject(
					referenz.getStrasse().getPid());

			if (systemObjekt == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse modellstrasse = (de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse) ObjektFactory
					.getInstanz().getModellobjekt(systemObjekt);

			if (modellstrasse == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			// entsprechend Absprache ist die Strasse in positiver TMC-Richtung
			// definiert
			TmcRichtung tmcrichtung = referenz.getFahrtRichtung().equals(
					NetzInterface.FahrtRichtung.IN_RICHTUNG) ? TmcRichtung.POSITIV
					: TmcRichtung.NEGATIV;

			for (AeusseresStrassenSegment segment : modellstrasse
					.getAuessereStrassensegmente()) {
				// System.out.println("ASS: " + segment.getName());
				if (!segment.getTmcRichtung().equals(tmcrichtung)) {
					continue;
				}

				for (StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (BetriebsKilometer bk : teilsegment
							.getBetriebsKilometer()) {
						if (Integer.parseInt(bk.getBlockNummer()) == referenz
								.getBlockNummer()
								&& bk.getWert() == referenz
										.getBetriebsKilometer()) {
							long offset = (long) (segment
									.getTeilSegmentOffset(teilsegment) + bk
									.getOffset());
							return new StrassenSegmentUndOffsetOrtsReferenz(
									segment.getPid(), offset);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new NetzReferenzException("Keine Abbildung möglich: "
					+ e.getMessage());
		}

		throw new NetzReferenzException("Keine Abbildung möglich");
	}

	/**
	 * Rechnet Ortsreferenzen vom ASB-Stationierungssystem in Angaben &uuml;ber
	 * eine Straße und den Betriebskilometers um.
	 * 
	 * @param referenz
	 *            Ortsreferenz nach dem ASB-Stationierungssystem.
	 * 
	 * @return Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlige und
	 *         den Betriebskilometer dargestellt wird.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public List<StrasseUndBetriebsKilometerOrtsReferenzInterface> ermittleOrtsReferenzStrasseUndBetriebsKilometer(
			AsbStationierungOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		if (referenz == null) {
			throw new IllegalArgumentException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}
		try {
			List<StrassenSegmentUndOffsetOrtsReferenzInterface> ssorefs = ermittleOrtsReferenzStrassenSegmentUndOffset(referenz);
			if (ssorefs == null || ssorefs.size() == 0) {
				throw new NetzReferenzException("keine Abbildung möglich");
			}
			List<StrasseUndBetriebsKilometerOrtsReferenzInterface> reflist = new ArrayList<StrasseUndBetriebsKilometerOrtsReferenzInterface>();
			for (StrassenSegmentUndOffsetOrtsReferenzInterface ssoref : ssorefs) {
				StrasseUndBetriebsKilometerOrtsReferenzInterface sbkref = ssoref
						.ermittleOrtsReferenzStrasseUndBetriebsKilometer();
				if (sbkref != null) {
					reflist.add(sbkref);
				}
			}

			if (reflist.size() == 0) {
				throw new NetzReferenzException("Keine Abbildung möglich");
			}

			return reflist;
		} catch (Exception e) {
			throw new NetzReferenzException("Keine Abbildung möglich: "
					+ e.getMessage());
		}
	}

	/**
	 * Rechnet Ortsreferenz mit Stra&szlig;enSegment und Offset vom Anfang des
	 * Stra&szlig;enSegments in Angaben &uuml;ber eine Straße und den
	 * Betriebskilometers um.
	 * 
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *            Stra&szlig;enSegment und den Offset vom Anfang des
	 *            Stra&szlig;enSegments dargestellt wird.
	 * 
	 * @return Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlige und
	 *         den Betriebskilometer dargestellt wird.
	 * 
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public StrasseUndBetriebsKilometerOrtsReferenzInterface ermittleOrtsReferenzStrasseUndBetriebsKilometer(
			StrassenSegmentUndOffsetOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
					.getModelSegment();
			// if (segment instanceof InneresStrassenSegment) {
			// throw new NetzReferenzException(
			// "Keine Abbildung möglich da InneresStraßenSegment");
			// }

			// bestimme die Straße
			Strasse strasse = new Strasse(
					((StrassenSegmentUndOffsetOrtsReferenz) referenz)
							.getModelSegment().getStrasse().getSystemObject());

			// bestimme die Richtung
			FahrtRichtung fahrtRichtung;
			TmcRichtung segmentTmcRictung = ((AeusseresStrassenSegment) segment)
					.getTmcRichtung();
			// entsprechend Absprache ist die Strasse in positiver TMC-Richtung
			// definiert
			if (segmentTmcRictung == TmcRichtung.POSITIV) {
				fahrtRichtung = NetzInterface.FahrtRichtung.IN_RICHTUNG;
			} else if (segmentTmcRictung == TmcRichtung.NEGATIV) {
				fahrtRichtung = NetzInterface.FahrtRichtung.GEGEN_RICHTUNG;
			} else {
				throw new NetzReferenzException(
						"Die Richtung der Straße kann nicht bestimmt werden");
			}

			SegmentBetriebsKilometer sbk = new SegmentBetriebsKilometer(
					strasse, fahrtRichtung, segment);

			return sbk
					.findeBetriebsKilometerAmOffset(referenz.getStartOffset());
		} catch (Exception e) {
			throw new NetzReferenzException("Keine Abbildung möglich: "
					+ e.getMessage());
		}

	}

	/**
	 * Findet eine ASB-Stationierungsangabe f&uuml;r einen bestimmten Offset
	 * innerhalb eines Stra&szlig;enteilsegmentes.
	 * 
	 * @param teilSegment
	 *            Stra&szlig;enteilsegment
	 * @param offset
	 *            Offset auf dem Stra&szlig;enteilsegment
	 * @return ASB-Stationierungsangabe
	 * @throws NetzReferenzException
	 *             wenn keine passende ASB-Stationierungsangabe bestimmt werden
	 *             konnte
	 */
	private AsbStationierung findeAsbStationierungAmOffset(
			final StrassenTeilSegment teilSegment, final double offset)
			throws NetzReferenzException {
		AsbStationierung asbStationierung = null;

		// die ASB-Angaben sollten nach Offset innerhalb des Teilsegmentes
		// geordnet sein,
		// d.h. es wird die letzte Angabe benutzt, deren Offset innerhalb des
		// Teilsegmentes < als
		// der verbleibende Offset innerhalb des Teilsegmentes ist
		for (AsbStationierung asb : teilSegment.getAsbStationierung()) {
			if (asb.getOffset() < offset) {
				asbStationierung = asb;
			}
		}

		if (asbStationierung == null) {
			throw new NetzReferenzException(
					"Es kann keine passende ASB-Stationierung gefunden werden");
		}

		return asbStationierung;
	}

	/**
	 * Findet eine Betriebskilometerangabe f&uuml;r einen bestimmten Offset
	 * innerhalb eines Stra&szlig;enteilsegmentes.
	 * 
	 * @param teilSegment
	 *            Stra&szlig;enteilsegment
	 * @param offset
	 *            Offset auf dem Stra&szlig;enteilsegment
	 * @return Betriebskilometerangabe
	 * @throws NetzReferenzException
	 *             wenn keine passende Betriebskilometerangabe bestimmt werden
	 *             konnte
	 */
	private BetriebsKilometer findeBetriebsKilometerAmOffset(
			final StrassenTeilSegment teilSegment, final double offset)
			throws NetzReferenzException {
		BetriebsKilometer betriebsKilometer = null;

		// die Betriebskilometerangaben sollten nach Offset innerhalb des
		// Teilsegmentes geordnet sein,
		// d.h. es wird die letzte Angabe benutzt, deren Offset innerhalb des
		// Teilsegmentes < als
		// der verbleibende Offset innerhalb des Teilsegmentes ist
		for (BetriebsKilometer bk : teilSegment.getBetriebsKilometer()) {
			if (bk.getOffset() < offset) {
				betriebsKilometer = bk;
			}
		}

		if (betriebsKilometer == null) {
			throw new NetzReferenzException(
					"Es kann keine passende Betriebskilometerangabe gefunden werden");
		}

		return betriebsKilometer;
	}

	/**
	 * Findet das Teilsegment des Stra&szlig;ensegmentes, auf dass von der
	 * Ortsreferenz verwiesen wird.
	 * 
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *            Stra&szlig;enSegment und den Offset vom Anfang des
	 *            Stra&szlig;enSegments dargestellt wird.
	 * @return Stra&szlig;enteilsegment am Offset
	 * @throws NetzReferenzException
	 *             wen das Teilsegment nicht bestimmt werden kann
	 */
	private StrassenTeilSegment findeTeilSegmentAmOffset(
			final StrassenSegmentUndOffsetOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
				.getModelSegment();

		StrassenTeilSegment teilsegmentoffset = segment
				.getStrassenTeilSegment(referenz.getStartOffset());

		if (teilsegmentoffset == null) {
			throw new NetzReferenzException("Das Teilsegment des Segmentes '"
					+ referenz.getStrassenSegment().getPid() + "' zum Offset '"
					+ referenz.getStartOffset()
					+ "' konnte nicht bestimmt werden");
		}

		return teilsegmentoffset;
	}

	/**
	 * Findet das Teilsegment, auf dass von der Ortsreferenz verwiesen wird.
	 * 
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *            Stra&szlig;enSegment und den Offset vom Anfang des
	 *            Stra&szlig;enSegments dargestellt wird.
	 * @return Stra&szlig;enteilsegment, dass die entsrepchende
	 *         ASB-Stationierung aufweist
	 * @throws NetzReferenzException
	 *             wen kein Teilsegment gefunden werden konnte
	 */
	private TeilSegmentASB findeTeilSegmentAsbStationierung(
			final AsbStationierungOrtsReferenzInterface referenz)
			throws NetzReferenzException {
		try {
			Verkehrsrichtung richtung = referenz.getAsbStationierungsRichtung() == NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG ? Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG
					: Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG;
			for (StrassenSegment segment : netzmodell.getNetzSegmentListe()) {
				for (StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (AsbStationierung asb : teilsegment
							.getAsbStationierung()) {
						if (asb.getVerkehrsRichtung() == richtung
								&& asb.getAnfangsKnoten().equals(
										referenz.getAnfangsKnoten())
								&& asb.getEndKnoten().equals(
										referenz.getEndKnoten())
								&& asb.getAnfang() <= referenz
										.getStationierung()
								&& asb.getEnde() >= referenz.getStationierung()) {
							return new TeilSegmentASB(teilsegment, asb);
						}
					}
				}
			}
		} catch (Exception e) {
			throw new NetzReferenzException(e.getMessage());
		}

		throw new NetzReferenzException(
				"Es konnte kein Teilsegment mit passender ASB-Stationierung gefunden werden");
	}

	/**
	 * Korrigiert den Offset innerhalb des Teilsegmentes zum korrekten Wert
	 * f&uuml;r die ASB-Stationierung.
	 * 
	 * @param teilsegment
	 *            Teilsegment
	 * @param asb
	 *            ASB-Stationierungsangabe
	 * @param offset
	 *            Offset im Teilsegment
	 * @return korrigierter Offset als Stationierungswert
	 */
	private long offsetKorrigieren(StrassenTeilSegment teilsegment,
			AsbStationierung asb, double offset) {
		long stationierung = (long) (offset / berechneAsbSkalierung(teilsegment));

		// korrigiere numerische Fehler
		if (stationierung < asb.getAnfang()) {
			stationierung = asb.getAnfang();
		}

		if (stationierung > asb.getEnde()) {
			stationierung = asb.getEnde();
		}

		return stationierung;
	}

	/**
	 * ordnet der Klasse das Netmodell zu.
	 * 
	 * @param netzmodell
	 *            das Netzmodell
	 * 
	 */
	public void setNetzmodell(VerkehrModellNetz netzmodell) {
		this.netzmodell = netzmodell;
	}

	/**
	 * Korrigiert die Stationierung zum korrekten Wert f&uuml;r den Offset.
	 * 
	 * @param teilsegment
	 *            Teilsegment
	 * @param stationierung
	 *            Stationierung
	 * @return korrigierte Stationierungswert als Offset
	 */
	private long stationierungKorrigieren(StrassenTeilSegment teilsegment,
			double stationierung) {
		long offset = (long) (stationierung * berechneAsbSkalierung(teilsegment));

		// korrigiere numerische Fehler
		if (offset < 0) {
			offset = 0;
		}

		if (offset > teilsegment.getLaenge()) {
			offset = (long) teilsegment.getLaenge();
		}

		return offset;
	}

}
