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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.LinieXY;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte.LinieXYImpl;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.MessQuerschnittAllgemein.MessQuerschnittComparator;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.zustaende.Verkehrsrichtung;

/**
 * Repr&auml;sentiert ein Stra&szlig;enteilsegment. Als Schl&uuml;ssel f&uuml;r
 * die Mess- und Fuzzy-Werte, wird der selbe wie am {@link MessQuerschnitt}
 * benutzt.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class StrassenTeilSegment extends StoerfallIndikator implements LinieXY {

	/**
	 * Eintrag für die Konfiguration der AsbStationierung (eines
	 * Straßenteilsegments).
	 */
	public class AsbStationierung {

		/**
		 * Abstand des ASB-Stationierungs-Punktes vom Anfang des Linienobjekts.
		 */
		private final long offset;

		/**
		 * Anfangsknoten mit eindeutiger Kennung (Anfangsnullpunkt) des
		 * Teilabschnittes oder Astes. Die Nullpunktbezeichung hat die Form
		 * TTTTnnnB, wobei TTTT die vierstellige TK25-Blattnummer und nnn die
		 * dreistellige laufende Nummer ist, die zusammen die bundesweit
		 * eindeutige Netzknotennummer darstellen. Durch die Kennung B (ein
		 * Zeichen) wird zusätzlich der Nullpunkt des Abschnitts oder Astes
		 * eindeutig festgelegt. Eine nicht vorhandene Kennung wird als o(hne)
		 * eingetragen.
		 */
		private final String anfangsKnoten;

		/**
		 * Endknoten mit eindeutiger Kennung (Endnullpunkt) des Teilabschnittes
		 * oder Astes. Die Nullpunktbezeichung hat die Form TTTTnnnB, wobei TTTT
		 * die vierstellige TK25-Blattnummer und nnn die dreistellige laufende
		 * Nummer ist, die zusammen die bundesweit eindeutige Netzknotennummer
		 * darstellen. Durch die Kennung B (ein Zeichen) wird zusätzlich der
		 * Nullpunkt des Abschnitts oder Astes eindeutig festgelegt. Eine nicht
		 * vorhandene Kennung wird als o(hne) eingetragen.
		 */
		private final String endKnoten;
		/**
		 * Anfangsstationierung. Stationierungsangabe in Metern relativ zum
		 * Abschnitt oder Ast.
		 */
		private final long anfang;
		/**
		 * Endstationierung. Stationierungsangabe in Metern relativ zum
		 * Abschnitt oder Ast.
		 */
		private final long ende;

		/**
		 * Angabe des Richtungsbezugs der ASB-Stationierung relativ zur Richtung
		 * des Straßensegments.
		 */
		private final Verkehrsrichtung verkehrsRichtung;

		/**
		 * Konstruktor, erzeugt einen leeren Datensatz und füllt ihn mit den
		 * Informationen aus den übergebenen Datenverteilerinformnationen.
		 *
		 * @param daten
		 *            die Daten für die Initialisierung
		 */
		public AsbStationierung(final Data daten) {
			if (daten != null) {
				anfang = daten.getUnscaledValue("Anfang").longValue();
				anfangsKnoten = daten.getTextValue("AnfangsKnoten").getText();
				ende = daten.getUnscaledValue("Ende").longValue();
				endKnoten = daten.getTextValue("EndKnoten").getText();
				offset = daten.getUnscaledValue("Offset").longValue();
				verkehrsRichtung = Verkehrsrichtung
						.getVerkehrsRichtung(daten.getUnscaledValue("VerkehrsRichtung").shortValue());
			} else {
				anfang = 0;
				anfangsKnoten = "";
				ende = 0;
				endKnoten = "";
				offset = 0;
				verkehrsRichtung = Verkehrsrichtung.UNBEKANNT;
			}
		}

		/**
		 * liefert den Anfangswert.
		 *
		 * @return den Wert
		 */
		public long getAnfang() {
			return anfang;
		}

		/**
		 * liefert den Anfangsknoten.
		 *
		 * @return den Knoten
		 */
		public String getAnfangsKnoten() {
			return anfangsKnoten;
		}

		/**
		 * liefert den Endwert.
		 *
		 * @return den Wert
		 */
		public long getEnde() {
			return ende;
		}

		/**
		 * liefert den Endknoten.
		 *
		 * @return den Knoten
		 */
		public String getEndKnoten() {
			return endKnoten;
		}

		/**
		 * liefert den Offset.
		 *
		 * @return den Offset
		 */
		public long getOffset() {
			return offset;
		}

		/**
		 * liefert die Verkehrsrichtung.
		 *
		 * @return die Richtung
		 */
		public Verkehrsrichtung getVerkehrsRichtung() {
			return verkehrsRichtung;
		}
	}

	/**
	 *
	 * Eintrag für die Konfiguration der Betriebskilometer (eines
	 * Straßenteilsegments).
	 */
	public class BetriebsKilometer {

		/**
		 * Abstand des Betriebskilometer-Punktes vom Anfang des Linienobjekts.
		 */
		private final long offset;
		/**
		 * Der Wert des Betriebskilometers.
		 */
		private final long wert;
		/**
		 * Blocknummer für diesen Betriebskilometer. Für eine Straße ist die
		 * Kombinaiton aus Betriebskilometer und Blocknummer eindeutig.
		 */
		private final String blockNummer;

		/**
		 * Konstruktor, erzeugt einen leeren Datensatz und füllt ihn mit den
		 * Informationen aus den übergebenen Datenverteilerinformnationen.
		 *
		 * @param daten
		 *            die Daten für die Initialisierung
		 */
		public BetriebsKilometer(final Data daten) {
			if (daten != null) {
				offset = daten.getUnscaledValue("Offset").longValue();
				wert = daten.getUnscaledValue("Wert").longValue();
				blockNummer = daten.getTextValue("BlockNummer").getText();
			} else {
				offset = 0;
				wert = 0;
				blockNummer = "";
			}
		}

		/**
		 * liefert die Blocknummer.
		 *
		 * @return die Nummer
		 */
		public String getBlockNummer() {
			return blockNummer;
		}

		/**
		 * liefert den Offset.
		 *
		 * @return den Offset
		 */
		public long getOffset() {
			return offset;
		}

		/**
		 * liefert den Wert des betriebskilometers.
		 *
		 * @return den Wert
		 */
		public long getWert() {
			return wert;
		}
	}

	/**
	 * markiert, ob die zugeordneten Straßensegmente bereits ermittelt wurden.
	 */
	private static boolean segmenteInitialisiert;

	/**
	 * markiert, ob die zugeordneten Messquerschnitte bereits ermittelt wurden.
	 */
	private static boolean messQuerschnitteZugeordnet;

	/**
	 * das Objekt, mit dem die Linieneigenschaften des Straßenteilsegments
	 * repräsentiert werden.
	 */
	private final LinieXY linie;

	/** Die L&auml;nge des Stra&szlig;enteilsegments. */
	private float laenge;

	/** Die Anzahl der Fahrstreifen des Stra&szlig;enteilsegments. */
	private int anzahlFahrStreifen = -1;

	/** Steigung (positiv) oder Gef&auml;lle (negativ) des Segments. */
	private int steigungGefaelle = -200; // undefiniert

	/** Liste der ASB-Stationierungs-Einträge. */
	private List<AsbStationierung> asbStationierung;

	/** Liste der Betriebskilometer-Einträge. */
	private List<BetriebsKilometer> betriebsKilometer;

	/**
	 * Nach Offset sortierte Liste der Messquerschnitt auf dem Teilsegement. Die
	 * Liste besteht zwar aus konfigurierenden Daten, diese m&uuml;ssen aber
	 * aufwendig zusammengesucht werden, weswegen die Liste nur bei Bedarf
	 * erstellt wird.
	 */
	private List<MessQuerschnittAllgemein> messQuerschnitte;

	/**
	 * Die Stra&szlig;ensegmente auf denen das Stra&szlig;enteilsegment liegt.
	 */
	private List<StrassenSegment> strassenSegmente;

	/**
	 * Erzeugt ein Stra&szlig;enteilsegment aus einem Systemobjekt.
	 *
	 * @param obj
	 *            Ein Systemobjekt, welches ein Stra&szlig;enteilsegment sein
	 *            muss
	 * @throws IllegalArgumentException
	 *             das übergebene Objekt hat den falschen Typ
	 */
	public StrassenTeilSegment(final SystemObject obj) {
		super(obj);

		linie = new LinieXYImpl(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException("Systemobjekt ist kein Straßenteilsegment.");
		}
	}

	/**
	 * liefert die Anzahl der Fahrstreifen des Strassenteilsegments.
	 *
	 * @return die Anzahl
	 */
	public int getAnzahlFahrstreifen() {
		leseKonfigDaten();
		return anzahlFahrStreifen;
	}

	/**
	 * liefert die konfigurierte Liste der AsbStationierungen. Es wird auf jeden
	 * Fall eine Liste geliefert, die gegebenenfalls leer ist, auch wenn der
	 * Datensatz nicht konfiguriert ist.
	 *
	 * @return die Liste der Stationierungen
	 */
	public List<AsbStationierung> getAsbStationierung() {
		if (asbStationierung == null) {
			asbStationierung = new ArrayList<>();
			final AttributeGroup atg = getSystemObject().getDataModel().getAttributeGroup("atg.asbStationierung");

			DataCache.cacheData(getSystemObject().getType(), atg);
			final Data datum = getSystemObject().getConfigurationData(atg);

			if (datum != null) {
				final Data.Array array = datum.getArray("AsbStationierung");
				for (int idx = 0; idx < array.getLength(); idx++) {
					asbStationierung.add(new AsbStationierung(array.getItem(idx)));
				}
			}
		}
		return asbStationierung;
	}

	/**
	 * liefert die konfigurierte Liste der Betriebskilometer. Es wird auf jeden
	 * Fall eine Liste geliefert, die gegebenenfalls leer ist, auch wenn der
	 * Datensatz nicht konfiguriert ist.
	 *
	 * @return die Liste der Betriebskilometer
	 */
	public List<BetriebsKilometer> getBetriebsKilometer() {
		if (betriebsKilometer == null) {
			betriebsKilometer = new ArrayList<>();
			final AttributeGroup atg = getSystemObject().getDataModel().getAttributeGroup("atg.betriebsKilometer");

			DataCache.cacheData(getSystemObject().getType(), atg);
			final Data datum = getSystemObject().getConfigurationData(atg);

			if (datum != null) {
				final Data.Array array = datum.getArray("BetriebsKilometer");
				for (int idx = 0; idx < array.getLength(); idx++) {
					betriebsKilometer.add(new BetriebsKilometer(array.getItem(idx)));
				}
			}
		}
		return betriebsKilometer;
	}

	/**
	 * liefert die konfigurierten Koordinaten des Straßenteilsegments.
	 *
	 * @return die Koordinaten
	 */
	@Override
	public List<Punkt> getKoordinaten() {
		return linie.getKoordinaten();
	}

	/**
	 * Gibt die L&auml;nge des Stra&szlig;enteilsegments zur&uuml;ck.
	 *
	 * @return Die L&auml;nge
	 */
	public float getLaenge() {
		leseKonfigDaten();
		return laenge;
	}

	/**
	 * Gibt die Menge der Messquerschnitte dieses Stra&szlig;enteilsegment
	 * zur&uuml;ck.
	 *
	 * @return Menge von Messquerschnitten
	 */
	public List<MessQuerschnittAllgemein> getMessQuerschnitte() {
		if (messQuerschnitte == null) {
			messQuerschnitte = new ArrayList<>();
		}

		if (!messQuerschnitteZugeordnet) {
			final List<SystemObjekt> listeSO;

			listeSO = ObjektFactory.getInstanz()
					.bestimmeModellobjekte(VerkehrsModellTypen.MESSQUERSCHNITTALLGEMEIN.getPid());

			for (final SystemObjekt so : listeSO) {
				final MessQuerschnittAllgemein mq = (MessQuerschnittAllgemein) so;
				final StrassenTeilSegment sts = mq.getStrassenTeilSegment();
				if (sts != null) {
					if (sts.messQuerschnitte == null) {
						sts.messQuerschnitte = new ArrayList<>();
					}
					sts.messQuerschnitte.add(mq);
				}
			}
			messQuerschnitteZugeordnet = true;
		}

		Collections.sort(messQuerschnitte, new MessQuerschnittComparator());
		return messQuerschnitte;
	}

	/**
	 * Gibt den Offset auf dem Stra&szlig;ensegment zur&uuml;ck, bei dem das
	 * Stra&szlig;enteilsegment beginnt.
	 *
	 * @deprecated Es wird nur das erste Straßensegment zur Berechnung des
	 *             Offsets verwendet. Stattdessen sollte die Funktion
	 *             {@link StrassenSegment#getTeilSegmentOffset(StrassenTeilSegment)}
	 *             verwendet werden
	 *
	 * @return der Offset des zugeh&ouml;rigen Stra&szlig;ensegmentes, bei dem
	 *         das Teilsegment beginnt
	 */
	@Deprecated
	public double getSegmentOffsetAnfang() {
		if (strassenSegmente == null) {
			getStrassenSegment();
		}

		double offset = -1;

		if (strassenSegmente != null) {
			offset = 0;
			for (final StrassenTeilSegment teilsegment : strassenSegmente.get(0).getStrassenTeilSegmente()) {
				if (teilsegment == this) {
					break;
				}

				offset += teilsegment.getLaenge();
			}
		}

		return offset;
	}

	/**
	 * liefert die Steigung/Gef&auml;lle des Strassenteilsegments.
	 *
	 * @return Steigung (positiv) oder Gef&auml;lle (negativ) des Segments.
	 */
	public int getSteigungGefaelle() {
		leseKonfigDaten();
		return steigungGefaelle;
	}

	/**
	 * Gibt die Stra&szlig;ensegmente zur&uuml;ck, auf denen das
	 * Stra&szlig;enteilsegment liegt.
	 *
	 * @return die Liste der Stra&szlig;ensegmente.
	 */
	public List<StrassenSegment> getStrassenSegment() {
		if (strassenSegmente == null) {
			strassenSegmente = new ArrayList<>();
		}

		if (!segmenteInitialisiert) {
			final List<SystemObjekt> listeSO;
			listeSO = ObjektFactory.getInstanz().bestimmeModellobjekte(VerkehrsModellTypen.STRASSENSEGMENT.getPid());

			for (final SystemObjekt so : listeSO) {
				final StrassenSegment ss = (StrassenSegment) so;
				for (final StrassenTeilSegment sts : ss.getStrassenTeilSegmente()) {
					if (sts.strassenSegmente == null) {
						sts.strassenSegmente = new ArrayList<>();
					}
					sts.strassenSegmente.add(ss);
				}
			}
			segmenteInitialisiert = true;
		}
		return strassenSegmente;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSENTEILSEGMENT;
	}

	/**
	 * Ruft konfigurierende Daten vom Datenverteiler ab.
	 */
	private void leseKonfigDaten() {
		if (anzahlFahrStreifen == -1) {
			final DataModel modell;
			final AttributeGroup atg;
			final Data datum;

			modell = getSystemObject().getDataModel();
			atg = modell.getAttributeGroup("atg.straßenTeilSegment");

			DataCache.cacheData(getSystemObject().getType(), atg);

			datum = getSystemObject().getConfigurationData(atg);
			if (datum != null) {
				laenge = datum.getScaledValue("Länge").floatValue();
				anzahlFahrStreifen = datum.getUnscaledValue("AnzahlFahrStreifen").intValue();
				steigungGefaelle = datum.getUnscaledValue("SteigungGefälle").intValue();
			} else {
				laenge = 0;
				anzahlFahrStreifen = 0;
				steigungGefaelle = 0;
			}
		}
	}
}
