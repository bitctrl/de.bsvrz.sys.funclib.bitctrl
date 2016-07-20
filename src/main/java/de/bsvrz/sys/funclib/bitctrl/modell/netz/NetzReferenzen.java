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

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.AsbStationierung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenTeilSegment.BetriebsKilometer;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.VerkehrModellNetz;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.Verkehrsrichtung;

/**
 * (Singleton-)Klasse zur Umrechnung zwischen den Referenzierungsarten.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public class NetzReferenzen {

	/**
	 * Repr&auml;sentiert eine ASB-Stationierung auf einem
	 * Stra&szlig;enteilsegment.
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
		TeilSegmentASB(final StrassenTeilSegment teilSegment,
				final AsbStationierung asbStationierung) {
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
	private double berechneAsbSkalierung(
			final StrassenTeilSegment teilsegment) {
		final List<AsbStationierung> asbliste = teilsegment
				.getAsbStationierung();

		if (asbliste.size() == 0) {
			return 1;
		}

		double asbLaenge = 0;

		for (final AsbStationierung asb : asbliste) {
			asbLaenge += asb.getEnde() - asb.getAnfang();
		}

		if (asbLaenge == 0) {
			return 1;
		}

		final double segmentLaenge = teilsegment.getLaenge();

		return segmentLaenge / asbLaenge;
	}

	/**
	 * Initialisiert die Klasse.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param netz
	 *            Netz
	 * @throws NetzReferenzException
	 *             wenn das Netz nicht initialisiert werden kann
	 */
	public void init(final ClientDavInterface dav, final String netz)
			throws NetzReferenzException {

		if (dav == null) {
			throw new IllegalArgumentException(
					"Parameter 'dav' darf nicht null sein!");
		}

		if (netz == null) {
			throw new IllegalArgumentException(
					"Parameter 'netz' darf nicht null sein!");
		}

		// initialisiere das Netzmodell
		final SystemObject netzObjekt = dav.getDataModel().getObject(netz);
		if (netzObjekt == null) {
			throw new NetzReferenzException(
					"Das Netz '" + netz + "' kann nicht initialisiert werden");
		}
		ObjektFactory.getInstanz().setVerbindung(dav);
		ObjektFactory.getInstanz().registerStandardFactories();

		netzmodell = (VerkehrModellNetz) ObjektFactory.getInstanz()
				.getModellobjekt(netzObjekt);

		// initialisiere das Referenznetz
		NetzReferenzen.getInstanz().setNetzmodell(netzmodell);
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
			final StrassenSegmentUndOffsetOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			final StrassenTeilSegment teilsegment = findeTeilSegmentAmOffset(
					referenz);

			final StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
					.getModelSegment();

			final double offsetImTeilsegment = referenz.getStartOffset()
					- segment.getTeilSegmentOffset(teilsegment);

			final AsbStationierung asb = findeAsbStationierungAmOffset(
					teilsegment, offsetImTeilsegment);

			final long stationierung;
			final ASBStationierungsRichtung richtung;
			// if
			// (teilsegment.getAsbStationierung().get(0).getVerkehrsRichtung()
			// == Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG) {
			// richtung =
			// ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG;
			// stationierung = (asb.getAnfang() + offsetKorrigieren(
			// teilsegment, asb, offsetImTeilsegment - asb.getOffset()));
			//
			// } else {
			// richtung = ASBStationierungsRichtung.IN_STATIONIERUNGSRICHTUNG;
			// stationierung = (asb.getEnde() - offsetKorrigieren(
			// teilsegment, asb, teilsegment.getLaenge() - (offsetImTeilsegment
			// - asb.getOffset())));
			// }

			if (teilsegment.getAsbStationierung().get(0)
					.getVerkehrsRichtung() == Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG) {
				richtung = ASBStationierungsRichtung.IN_STATIONIERUNGSRICHTUNG;
				stationierung = (asb.getAnfang() + offsetKorrigieren(
						teilsegment, asb, offsetImTeilsegment));

			} else {
				richtung = ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG;
				stationierung = (asb.getEnde() - offsetKorrigieren(teilsegment,
						asb, teilsegment.getLaenge() - offsetImTeilsegment));
			}

			return new AsbStationierungOrtsReferenz(asb.getAnfangsKnoten(),
					asb.getEndKnoten(), richtung, stationierung);
		} catch (final Exception e) {
			throw new NetzReferenzException(
					"Keine Abbildung möglich: " + e.getMessage());
		}
	}

	/**
	 * Rechnet Ortsreferenz mit Stra&szlig;e und Betriebskilometer in Ortsangabe
	 * &uuml;ber das ASB-Stationierungssystem um.
	 *
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlig;e und
	 *            den Betriebskilometer dargestellt wird.
	 *
	 * @return Ortsreferenz nach dem ASB-Stationierungssystem.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public AsbStationierungOrtsReferenzInterface ermittleOrtsReferenzAsbStationierung(
			final StrasseUndBetriebsKilometerOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			final ClientDavInterface dav = ObjektFactory.getInstanz()
					.getVerbindung();

			final SystemObject systemObjekt = dav.getDataModel()
					.getObject(referenz.getStrasse().getPid());

			if (systemObjekt == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			final de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse modellstrasse = (de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse) ObjektFactory
					.getInstanz().getModellobjekt(systemObjekt);

			if (modellstrasse == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			// entsprechend Absprache ist die Strasse in positiver TMC-Richtung
			// definiert
			final TmcRichtung tmcrichtung = referenz.getFahrtRichtung()
					.equals(NetzInterface.FahrtRichtung.IN_RICHTUNG)
					? TmcRichtung.POSITIV : TmcRichtung.NEGATIV;

			for (final AeusseresStrassenSegment segment : modellstrasse
					.getAuessereStrassensegmente()) {
				if (!segment.getTmcRichtung().equals(tmcrichtung)) {
					continue;
				}

				for (final StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (final BetriebsKilometer bk : teilsegment
							.getBetriebsKilometer()) {
						if ((Integer.parseInt(bk.getBlockNummer()) == referenz
								.getBlockNummer())
								&& (bk.getWert() == referenz
								.getBetriebsKilometer())) {
							final double offsetImTeilsegment = bk.getOffset();

							final AsbStationierung asb = findeAsbStationierungAmOffset(
									teilsegment, offsetImTeilsegment);

							final String anfangsKnoten = asb.getAnfangsKnoten();
							final String endKnoten = asb.getEndKnoten();
							final long stationierung = (long) (asb.getAnfang()
									+ (referenz.getBetriebsKilometer()
											- segment.getTeilSegmentOffset(
													teilsegment)));

							final ASBStationierungsRichtung richtung;
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
		} catch (final Exception e) {
			throw new NetzReferenzException(
					"Keine Abbildung möglich: " + e.getMessage());
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
			final AsbStationierungOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			final TeilSegmentASB asbsegment = findeTeilSegmentAsbStationierung(
					referenz);

			final List<StrassenSegmentUndOffsetOrtsReferenzInterface> refliste = new ArrayList<>();

			for (final StrassenSegment asegment : asbsegment.getTeilSegment()
					.getStrassenSegment()) {
				// long offset = 0;
				long offset = (long) (asegment
						.getTeilSegmentOffset(asbsegment.getTeilSegment())
						+ asbsegment.getAsbStationierung().getOffset());

				if (asbsegment.asbStationierung.getVerkehrsRichtung()
						.equals(Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG)) {
					// offset = (long) (asegment.getTeilSegmentOffset(asbsegment
					// .getTeilSegment()) + stationierungKorrigieren(
					// asbsegment.getTeilSegment(), referenz
					// .getStationierung()
					// - asbsegment.getAsbStationierung().getAnfang()));
					offset += stationierungKorrigieren(
							asbsegment.getTeilSegment(),
							referenz.getStationierung() - asbsegment
							.getAsbStationierung().getAnfang());

				} else {
					// offset = (long) (asegment.getTeilSegmentOffset(asbsegment
					// .getTeilSegment()) + stationierungKorrigieren(
					// asbsegment.getTeilSegment(),
					// asbsegment.getAsbStationierung().getEnde() - referenz
					// .getStationierung()));

					offset += stationierungKorrigieren(
							asbsegment.getTeilSegment(),
							asbsegment.getAsbStationierung().getEnde()
							- referenz.getStationierung());

				}
				final StrassenSegmentUndOffsetOrtsReferenz ref = new StrassenSegmentUndOffsetOrtsReferenz(
						asegment.getPid(), offset);
				refliste.add(ref);
			}

			return refliste;
		} catch (final NetzReferenzAsbKnotenUnbekanntException e) {
			throw e;
		} catch (final NetzReferenzAsbStationierungUnbekanntException e) {
			throw e;
		} catch (final Exception e) {
			throw new NetzReferenzException(
					"Keine Abbildung möglich: " + e.getMessage());
		}
	}

	/**
	 * Rechnet Ortsreferenz mit Stra&szlig;e und Betriebskilometer in Angaben
	 * mit Stra&szlig;enSegment und den Offset vom Anfang des
	 * Stra&szlig;enSegments um.
	 *
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlig;e und
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
			final StrasseUndBetriebsKilometerOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			final ClientDavInterface dav = ObjektFactory.getInstanz()
					.getVerbindung();

			final SystemObject systemObjekt = dav.getDataModel()
					.getObject(referenz.getStrasse().getPid());

			if (systemObjekt == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			final de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse modellstrasse = (de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse) ObjektFactory
					.getInstanz().getModellobjekt(systemObjekt);

			if (modellstrasse == null) {
				throw new NetzReferenzException("Die Strasse '"
						+ referenz.getStrasse().getName()
						+ "' konnte in der Konfiguration nicht gefunden werden");
			}

			// entsprechend Absprache ist die Strasse in positiver TMC-Richtung
			// definiert
			final TmcRichtung tmcrichtung = referenz.getFahrtRichtung()
					.equals(NetzInterface.FahrtRichtung.IN_RICHTUNG)
					? TmcRichtung.POSITIV : TmcRichtung.NEGATIV;

			for (final AeusseresStrassenSegment segment : modellstrasse
					.getAuessereStrassensegmente()) {
				// System.out.println("ASS: " + segment.getName());
				if (!segment.getTmcRichtung().equals(tmcrichtung)) {
					continue;
				}

				for (final StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (final BetriebsKilometer bk : teilsegment
							.getBetriebsKilometer()) {
						if ((Integer.parseInt(bk.getBlockNummer()) == referenz
								.getBlockNummer())
								&& (bk.getWert() == referenz
								.getBetriebsKilometer())) {
							final long offset = (long) (segment
									.getTeilSegmentOffset(teilsegment)
									+ bk.getOffset());
							return new StrassenSegmentUndOffsetOrtsReferenz(
									segment.getPid(), offset);
						}
					}
				}
			}
		} catch (final Exception e) {
			throw new NetzReferenzException(
					"Keine Abbildung möglich: " + e.getMessage());
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
	 * @return Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlig;e und
	 *         den Betriebskilometer dargestellt wird.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public List<StrasseUndBetriebsKilometerOrtsReferenzInterface> ermittleOrtsReferenzStrasseUndBetriebsKilometer(
			final AsbStationierungOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (referenz == null) {
			throw new IllegalArgumentException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}
		try {
			final List<StrassenSegmentUndOffsetOrtsReferenzInterface> ssorefs = ermittleOrtsReferenzStrassenSegmentUndOffset(
					referenz);
			if ((ssorefs == null) || (ssorefs.size() == 0)) {
				throw new NetzReferenzException("keine Abbildung möglich");
			}
			final List<StrasseUndBetriebsKilometerOrtsReferenzInterface> reflist = new ArrayList<>();
			for (final StrassenSegmentUndOffsetOrtsReferenzInterface ssoref : ssorefs) {
				final StrasseUndBetriebsKilometerOrtsReferenzInterface sbkref = ssoref
						.ermittleOrtsReferenzStrasseUndBetriebsKilometer();
				if (sbkref != null) {
					reflist.add(sbkref);
				}
			}

			if (reflist.size() == 0) {
				throw new NetzReferenzException("Keine Abbildung möglich");
			}

			return reflist;
		} catch (final Exception e) {
			throw new NetzReferenzException(
					"Keine Abbildung möglich: " + e.getMessage());
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
	 * @return Ortsreferenz, bei dem die Ortsangabe über eine Stra&szlig;e und
	 *         den Betriebskilometer dargestellt wird.
	 *
	 * @throws NetzReferenzException
	 *             wenn keine Abbildung m&ouml;glich ist.
	 */
	public StrasseUndBetriebsKilometerOrtsReferenzInterface ermittleOrtsReferenzStrasseUndBetriebsKilometer(
			final StrassenSegmentUndOffsetOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (referenz == null) {
			throw new NetzReferenzException(
					"Das Argument 'referenz' darf nicht 'null' sein");
		}

		try {
			final StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
					.getModelSegment();
			// if (segment instanceof InneresStrassenSegment) {
			// throw new NetzReferenzException(
			// "Keine Abbildung möglich da InneresStraßenSegment");
			// }

			// bestimme die Straße
			final Strasse strasse = new Strasse(
					((StrassenSegmentUndOffsetOrtsReferenz) referenz)
					.getModelSegment().getStrasse().getSystemObject());

			// bestimme die Richtung
			final FahrtRichtung fahrtRichtung;
			final TmcRichtung segmentTmcRictung = ((AeusseresStrassenSegment) segment)
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

			final SegmentBetriebsKilometer sbk = new SegmentBetriebsKilometer(
					strasse, fahrtRichtung, segment);

			return sbk
					.findeBetriebsKilometerAmOffset(referenz.getStartOffset());
		} catch (final Exception e) {
			throw new NetzReferenzException(
					"Keine Abbildung möglich: " + e.getMessage());
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
		for (final AsbStationierung asb : teilSegment.getAsbStationierung()) {
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
		final StrassenSegment segment = ((StrassenSegmentUndOffsetOrtsReferenz) referenz)
				.getModelSegment();

		final StrassenTeilSegment teilsegmentoffset = segment
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
	@SuppressWarnings("unused")
	private TeilSegmentASB findeTeilSegmentAsbStationierungAlt(
			final AsbStationierungOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		boolean knotenGefunden = false;

		try {
			final Verkehrsrichtung richtung = referenz
					.getAsbStationierungsRichtung() == NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG
					? Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG
							: Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG;
			for (final StrassenSegment segment : netzmodell
					.getNetzSegmentListe()) {
				for (final StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (final AsbStationierung asb : teilsegment
							.getAsbStationierung()) {
						if ((asb.getVerkehrsRichtung() == richtung
								// && asb.getAnfangsKnoten().equals(
								// referenz.getAnfangsKnoten())
								// && asb.getEndKnoten().equals(
								// referenz.getEndKnoten())
								// neu testweise: es wird auch der Nullpunkt
								// '<Netzknoten>O' akzeptiert
								) && (asb.getAnfangsKnoten()
										.equals(referenz.getAnfangsKnoten())
										|| asb.getAnfangsKnoten().equals(
												referenz.getAnfangsKnoten() + "O"))

								&& (asb.getEndKnoten().equals(referenz.getEndKnoten())
										|| asb.getEndKnoten().equals(
												referenz.getEndKnoten() + "O"))) {

							knotenGefunden = true;

							if ((asb.getAnfang() <= referenz.getStationierung())
									&& (asb.getEnde() >= referenz
									.getStationierung())) {
								return new TeilSegmentASB(teilsegment, asb);
							}
						}
					}
				}
			}
		} catch (final Exception e) {
			throw new NetzReferenzException(e.getMessage());
		}

		String fehler = "Es konnte kein Teilsegment mit passender ASB-Stationierung gefunden werden (";

		if (knotenGefunden) {
			fehler += "die Stationierung existiert nicht";
		} else {
			fehler += "die Kombination Anfangsknoten/Endknoten existiert nicht";
		}
		fehler += ")";

		throw new NetzReferenzException(fehler);
	}

	private final Map<Verkehrsrichtung, Map<String, List<StrassenTeilSegment>>> richtungAnfangKnotenMap = new HashMap<>();
	private final Set<String> anfangKnotenMap = new HashSet<>();
	private final Set<String> endKnotenMap = new HashSet<>();
	private boolean anfangKnotenMapInitialisiert;

	public String getAsbKnotenKey(final String knoten) {
		return knoten.substring(0, 7);
	}

	private void initKnotenMap() {
		if (!anfangKnotenMapInitialisiert) {
			for (final StrassenSegment segment : netzmodell
					.getNetzSegmentListe()) {
				for (final StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (final AsbStationierung asb : teilsegment
							.getAsbStationierung()) {
						final Verkehrsrichtung verkehrsRichtung = asb
								.getVerkehrsRichtung();
						Map<String, List<StrassenTeilSegment>> richtungMap = richtungAnfangKnotenMap
								.get(verkehrsRichtung);
						if (richtungMap == null) {
							richtungMap = new HashMap<>();
							richtungAnfangKnotenMap.put(verkehrsRichtung,
									richtungMap);
						}
						List<StrassenTeilSegment> knotenList = richtungMap
								.get(getAsbKnotenKey(asb.getAnfangsKnoten()));
						if (knotenList == null) {
							knotenList = new ArrayList<>();
							richtungMap.put(
									getAsbKnotenKey(asb.getAnfangsKnoten()),
									knotenList);
						}
						if (!knotenList.contains(teilsegment)) {
							knotenList.add(teilsegment);
						}

						String knoten = asb.getAnfangsKnoten();
						anfangKnotenMap.add(knoten);
						anfangKnotenMap.add(getAsbKnotenKey(knoten));

						knoten = asb.getEndKnoten();
						endKnotenMap.add(knoten);
						endKnotenMap.add(getAsbKnotenKey(knoten));
					}
				}
				anfangKnotenMapInitialisiert = true;
			}
		}
	}

	/**
	 * Findet eine Liste von Teilsegmenten, deren ASB-Stationierung den
	 * Anfangsknoten der Referenz enth&auml;lt.
	 *
	 * @param referenz
	 *            Referenz
	 * @return Liste von Teilsegmenten, deren ASB-Stationierung den
	 *         Anfangsknoten der Referenz enth&auml;lt. Wenn keine Teilsegmente
	 *         gefunden werden, wird eine Exception geworfen.
	 * @throws NetzReferenzException
	 *             bei unbekannter Stationierungsrichtung
	 * @throws NetzReferenzAsbKnotenUnbekanntException
	 *             wenn einer der beiden Netzknoten der Referenz nicht bekannt
	 *             ist.
	 */
	private List<StrassenTeilSegment> findeKnotenListe(
			final AsbStationierungOrtsReferenzInterface referenz)
					throws NetzReferenzException {
		if (!anfangKnotenMapInitialisiert) {
			initKnotenMap();
		}

		final Verkehrsrichtung richtung = referenz
				.getAsbStationierungsRichtung() == NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG
				? Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG
						: Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG;

		final Map<String, List<StrassenTeilSegment>> richtungMap = richtungAnfangKnotenMap
				.get(richtung);

		if (richtungMap == null) {
			throw new NetzReferenzException("unbekannte Verkehrsrichtung");
		}

		if (!anfangKnotenMap.contains(referenz.getAnfangsKnoten())
				&& !anfangKnotenMap.contains(
						getAsbKnotenKey(referenz.getAnfangsKnoten()))) {
			throw new NetzReferenzAsbKnotenUnbekanntException(
					referenz.getAnfangsKnoten());
		}

		if (!endKnotenMap.contains(referenz.getEndKnoten()) && !endKnotenMap
				.contains(getAsbKnotenKey(referenz.getEndKnoten()))) {
			throw new NetzReferenzAsbKnotenUnbekanntException(
					referenz.getEndKnoten());
		}

		final List<StrassenTeilSegment> knotenList = richtungMap
				.get(getAsbKnotenKey(referenz.getAnfangsKnoten()));
		if (knotenList == null) {
			throw new NetzReferenzAsbKnotenUnbekanntException(
					referenz.getAnfangsKnoten());
		}

		return knotenList;
	}

	/**
	 * Test, ob 2 Netzknoten &uuml;berenstimmen. Ntzknoten ohne Kennung
	 * (Buchstaben am Ende) werden gleich der 'Ohne-Kennung' 'O' behandelt.
	 *
	 * @param knoten1
	 *            Netzknoten
	 * @param knoten2
	 *            Netzknoten
	 * @return true, wenn gleich
	 */
	public boolean netzKnotenMatch(final String knoten1, final String knoten2) {
		return knoten1.equals(knoten2) || (isOhneKennung(knoten1)
				&& isOhneKennung(knoten2)
				&& getAsbKnotenKey(knoten1).equals(getAsbKnotenKey(knoten2)));
	}

	private boolean isOhneKennung(final String knoten) {
		if (knoten.length() == 7) {
			return true;
		}

		final String kennung = knoten.substring(7, 8);

		return kennung.equalsIgnoreCase("O");
	}

	private TeilSegmentASB findeTeilSegmentAsbStationierung(
			final AsbStationierungOrtsReferenzInterface referenz)
					throws NetzReferenzException {

		final List<StrassenTeilSegment> knotenList = findeKnotenListe(referenz);

		final Verkehrsrichtung richtung = referenz
				.getAsbStationierungsRichtung() == NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG
				? Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG
						: Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG;

		boolean knotenGefunden = false;

		for (final StrassenTeilSegment teilsegment : knotenList) {
			for (final AsbStationierung asb : teilsegment
					.getAsbStationierung()) {
				if ((asb.getVerkehrsRichtung() == richtung)
						&& netzKnotenMatch(asb.getAnfangsKnoten(),
								referenz.getAnfangsKnoten())
						&& netzKnotenMatch(asb.getEndKnoten(),
								referenz.getEndKnoten())) {
					knotenGefunden = true;

					if ((asb.getAnfang() <= referenz.getStationierung())
							&& (asb.getEnde() >= referenz.getStationierung())) {
						return new TeilSegmentASB(teilsegment, asb);
					}
				}
			}
		}

		if (knotenGefunden) {
			// fehler += "Die Stationierung existiert nicht";
			throw new NetzReferenzAsbStationierungUnbekanntException(
					referenz.getStationierung());
		} else {
			throw new NetzReferenzException(
					"Die Kombination Anfangsknoten/Endknoten existiert nicht");
		}
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
	private long offsetKorrigieren(final StrassenTeilSegment teilsegment,
			final AsbStationierung asb, final double offset) {
		long stationierung = (long) (offset
				/ berechneAsbSkalierung(teilsegment));

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
	public void setNetzmodell(final VerkehrModellNetz netzmodell) {
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
	private long stationierungKorrigieren(final StrassenTeilSegment teilsegment,
			final double stationierung) {
		long offset = (long) (stationierung
				* berechneAsbSkalierung(teilsegment));

		// korrigiere numerische Fehler
		if (offset < 0) {
			offset = 0;
		}

		if (offset > teilsegment.getLaenge()) {
			offset = (long) teilsegment.getLaenge();
		}

		return offset;
	}

	/**
	 * Versucht, eine Stra&szlig;e zu einer ASB-Stationierung zu finden.<br>
	 * Dazu wird ein Stra&szlig;enteilsegment gesucht, dessen Anfangs- oder
	 * Endknoten mit Anfangs- oder Endknoten der gesuchten ASB-Stationierung
	 * &uuml;bereinstimmt. Falls ein derartiges Stra&szlig;enteilsegment
	 * gefunden wird, wird die Stra&szlig;e, zu der das Teilsegment
	 * geh&ouml;hrt, zur&uuml;ckgeliefert.
	 *
	 * @param referenz
	 *            Ortsreferenz nach dem ASB-Stationierungssystem.
	 * @return Strasse, auf der die Ortsreferenz liegt
	 * @throws NetzReferenzException Fehler beim Ermitteln der Stra&szlig;e
	 * @deprecated Ersetzt durch
	 *             {@link #findeStrassen(AsbStationierungOrtsReferenz)}.
	 */
	@Deprecated
	public Strasse findeStrasse(final AsbStationierungOrtsReferenz referenz)
			throws NetzReferenzException {
		try {
			for (final StrassenSegment segment : netzmodell
					.getNetzSegmentListe()) {
				for (final StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (final AsbStationierung asb : teilsegment
							.getAsbStationierung()) {
						if (asb.getAnfangsKnoten()
								.equals(referenz.getAnfangsKnoten())
								|| asb.getAnfangsKnoten()
								.equals(referenz.getAnfangsKnoten()
										+ "O")
								|| asb.getEndKnoten()
								.equals(referenz.getEndKnoten())
								|| asb.getEndKnoten().equals(
										referenz.getEndKnoten() + "O")) {
							if (teilsegment.getStrassenSegment().get(0)
									.getStrasse() != null) {
								return teilsegment.getStrassenSegment().get(0)
										.getStrasse();
							}
						}
					}
				}
			}
		} catch (final Exception e) {
			throw new NetzReferenzException(e.getMessage());
		}

		return null;
	}

	/**
	 * Versucht, alle Stra&szlig;en zu einer ASB-Stationierung zu finden.<br>
	 * Dazu werden alle Stra&szlig;enteilsegmente gesucht, dessen Anfangs- oder
	 * Endknoten mit Anfangs- oder Endknoten der gesuchten ASB-Stationierung
	 * &uuml;bereinstimmt. F&uuml;r alle gefundenen Stra&szlig;enteilsegmente
	 * werden die Stra&szlig;en aller Stra&szlig;ensegmente, zu denen das
	 * Teilsegment geh&ouml;rt, zur&uuml;ckgeliefert.
	 *
	 * @param referenz
	 *            Ortsreferenz nach dem ASB-Stationierungssystem.
	 * @return Strasse, Liste von gefundenen Stra&szlig;en
	 * @throws NetzReferenzException Fehler beim Ermitteln der Stra&szlig;e
	 */
	public List<Strasse> findeStrassen(
			final AsbStationierungOrtsReferenz referenz)
					throws NetzReferenzException {
		final List<Strasse> result = new ArrayList<>();
		try {
			for (final StrassenSegment segment : netzmodell
					.getNetzSegmentListe()) {
				for (final StrassenTeilSegment teilsegment : segment
						.getStrassenTeilSegmente()) {
					for (final AsbStationierung asb : teilsegment
							.getAsbStationierung()) {
						if (netzKnotenMatch(asb.getAnfangsKnoten(),
								referenz.getAnfangsKnoten())
								&& netzKnotenMatch(asb.getEndKnoten(),
										referenz.getEndKnoten())) {
							for (final StrassenSegment seg : teilsegment
									.getStrassenSegment()) {
								if ((seg.getStrasse() != null)
										&& !result.contains(seg.getStrasse())) {
									result.add(seg.getStrasse());
								}
							}
						}
					}
				}
			}
		} catch (final Exception e) {
			throw new NetzReferenzException(e.getMessage());
		}

		return result;
	}

	/**
	 * Findet den gr&ouml;ssten Stationierungswert zu einem ASB-Abschnitt/Ast.
	 *
	 * @param referenz
	 *            Ortsreferenz, bei dem die Ortsangabe &uuml;ber ein
	 *            Stra&szlig;enSegment und den Offset vom Anfang des
	 *            Stra&szlig;enSegments dargestellt wird.
	 * @return gr&ouml;sster Stationierungswert
	 * @throws NetzReferenzException
	 *             bei Fehlern
	 */
	public AsbStationierungBereich findeAsbStationierungsBereich(
			final AsbStationierungOrtsReferenz referenz)
					throws NetzReferenzException {

		final List<AsbStationierung> bereiche = new ArrayList<>();

		if (!anfangKnotenMapInitialisiert) {
			initKnotenMap();
		}

		final Verkehrsrichtung richtung = referenz
				.getAsbStationierungsRichtung() == NetzInterface.ASBStationierungsRichtung.GEGEN_STATIONIERUNGSRICHTUNG
				? Verkehrsrichtung.GEGEN_STATIONIERUNGSRICHTUNG
						: Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG;

		final List<StrassenTeilSegment> knotenList = findeKnotenListe(referenz);

		for (final StrassenTeilSegment teilsegment : knotenList) {
			for (final AsbStationierung asb : teilsegment
					.getAsbStationierung()) {
				if (netzKnotenMatch(asb.getAnfangsKnoten(),
						referenz.getAnfangsKnoten())
						&& netzKnotenMatch(asb.getEndKnoten(),
								referenz.getEndKnoten())) {
					bereiche.add(asb);
				}
			}
		}

		if (bereiche.size() == 0) {
			return null;
		}

		// Liste sortieren
		Collections.sort(bereiche, new Comparator<AsbStationierung>() {
			@Override
			public int compare(final AsbStationierung asb1,
					final AsbStationierung asb2) {
				return new Long(asb1.getAnfang())
						.compareTo(new Long(asb2.getAnfang()));
			}
		});

		final List<AsbStationierungBereich> zusammengefassteBereiche = new ArrayList<>();
		final AsbStationierungBereich anfang = new AsbStationierungBereich(
				bereiche.get(0));
		zusammengefassteBereiche.add(anfang);

		// zusammenhängende Bereiche bilden
		for (int i = 1; i < bereiche.size(); i++) {
			final AsbStationierung asb = bereiche.get(i);
			if ((asb.getAnfang() <= anfang.getMaxStationierung())
					&& (asb.getEnde() > anfang.getMaxStationierung())) {
				anfang.setMaxStationierung(asb.getEnde());
			} else {
				zusammengefassteBereiche.add(new AsbStationierungBereich(asb));
			}
		}

		if (richtung == Verkehrsrichtung.IN_STATIONIERUNGSRICHTUNG) {
			return zusammengefassteBereiche
					.get(zusammengefassteBereiche.size() - 1);
		} else {
			return anfang;
		}
	}
}
