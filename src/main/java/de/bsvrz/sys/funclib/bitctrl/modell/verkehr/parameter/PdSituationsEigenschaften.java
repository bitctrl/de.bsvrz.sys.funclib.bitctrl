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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.AeusseresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.InneresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.Strasse;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenKnoten;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.util.SearchCycleException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Ein Parameterdatensatz, die Eigenschaften einer Situation beinhaltet. Der
 * Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.situationsEigenschaften".
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class PdSituationsEigenschaften extends AbstractParameterDatensatz<PdSituationsEigenschaften.Daten> {

	/**
	 * Die Repräsentation der Daten des Situationseigenschaften-Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		/**
		 * Dauer des Situation (sofern bekannt). Eintrag von 0 ms bedeutet //
		 * unbekannte (unendliche) Dauer. ("Dauer")
		 */
		private long dauer;
		/**
		 * Position des Situationsendes im letzten Straßensegment. ("EndOffset")
		 */
		private long endOffset;
		/**
		 * Referenzen auf alle Straßensegmente, über die sich die Situation
		 * ausbreitet. ("StraßenSegment")
		 */
		private final List<StrassenSegment> segmente = new ArrayList<>();
		/**
		 * Position des Situationsanfangs im ersten Straßensegment.
		 * ("StartOffset")
		 */
		private long startOffset;

		/**
		 * Startzeitpunkt der Situation (Staubeginn, Baustellenbeginn, // etc.).
		 * ("StartZeit")
		 */
		private long startZeit;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * Standard-Konstruktor zum Erstellen eines leeren Datensatzes.
		 *
		 */
		Daten() {
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
		Daten(final Daten daten) {
			datenStatus = daten.datenStatus;
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
		Daten(final ResultData result) {
			setZeitstempel(result.getDataTime());
			final Data daten = result.getData();
			if (daten != null) {
				startZeit = daten.getTimeValue("StartZeit").getMillis();
				dauer = daten.getTimeValue("Dauer").getMillis();
				final Data.Array segmentArray = daten.getArray("StraßenSegment");
				for (int idx = 0; idx < segmentArray.getLength(); idx++) {
					segmente.add((StrassenSegment) ObjektFactory.getInstanz()
							.getModellobjekt(segmentArray.getReferenceValue(idx).getSystemObject()));
				}
				startOffset = daten.getUnscaledValue("StartOffset").longValue();
				endOffset = daten.getUnscaledValue("EndOffset").longValue();
			} else {
				startZeit = Long.MAX_VALUE;
				dauer = 0;
				startOffset = 0;
				endOffset = 0;
			}

			datenStatus = Datum.Status.getStatus(result.getDataState());
		}

		@Override
		public Daten clone() {
			return new Daten(this);
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
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
		 * liefert die Endzeit der Situation, wenn die Dauer bekannt ist.
		 * Anderenfalls wird der Wert Long.MAX_VALUE geliefert.
		 *
		 * @return die Endzeit
		 */
		public long getEndZeit() {
			long result = Long.MAX_VALUE;
			if (dauer > 0) {
				result = startZeit + dauer;
			}
			return result;
		}

		/**
		 * liefert den in Fahrtrichtung auf die Situation folgenden
		 * Straßenknoten. Wird kein Knoten gefunden liefert die Funktion den
		 * Wert <code>null</code> zurück.
		 *
		 * @return den Knoten oder <code>null</code>
		 */
		public StrassenKnoten getFolgeKnoten() {
			StrassenKnoten result = null;

			final List<StrassenSegment> segmentListe = getSegmente();
			if (segmentListe.size() > 0) {
				final StrassenSegment segment = segmentListe.get(segmentListe.size() - 1);
				if (segment instanceof InneresStrassenSegment) {
					final InneresStrassenSegment iss = (InneresStrassenSegment) segment;
					if (iss.getNachSegment() != null) {
						result = iss.getNachSegment().getNachKnoten();
					}
					if (result == null) {
						if (iss.getNachSegment() != null) {
							result = iss.getNachSegment().getVonKnoten();
						}
					}
				} else if (segment instanceof AeusseresStrassenSegment) {
					final StrassenKnoten knoten = ((AeusseresStrassenSegment) segment).getNachKnoten();
					if (knoten != null) {
						result = knoten;
					}
				}
			}
			return result;
		}

		/**
		 * liefert die Länge der Situation als Summe der Längen der beteiligten
		 * Straßensegemente abzüglich des Endoffsets und des Startoffsets.
		 *
		 * @return die Länge
		 */
		public double getLaenge() {
			double result = 0;
			StrassenSegment letztesSegment = null;

			for (final StrassenSegment segment : getSegmente()) {
				result += segment.getLaenge();
				letztesSegment = segment;
			}

			result -= getStartOffset();
			if (letztesSegment != null) {
				result -= (letztesSegment.getLaenge() - getEndOffset());
			}
			return result;
		}

		/**
		 * liefert den vor der Situation liegenden Messquerschnitt. Wird kein
		 * Messquerschnitt gefunden, wird der Wert <code>null</code> geliefert.
		 *
		 * @return den Messquerschnitt oder <code>null</code>
		 * @throws SearchCycleException
		 *             Zykluis bei der Suche aufgetreten
		 */
		public MessQuerschnittAllgemein getMessquerschnittDavor() throws SearchCycleException {
			final StrassenSegment segment = getSegment(0);
			StrassenSegment usedSegment = segment;
			MessQuerschnittAllgemein mqDavor = null;
			final List<StrassenSegment> visited = new ArrayList<>();

			final Debug logger = Debug.getLogger();
			logger.finer("Ermittle MQ vor der Baustelle");

			while ((mqDavor == null) && (usedSegment != null)) {

				if (visited.contains(usedSegment)) {
					throw new SearchCycleException("Schleife bei der Ermittlung des vor der Baustelle liegenden MQ",
							visited);
				}

				visited.add(usedSegment);

				Debug.getLogger().finer("Verwende Segment: " + usedSegment);
				final List<MessQuerschnittAllgemein> mqs = usedSegment.getMessquerschnitte();
				if (segment == usedSegment) {
					for (int idx = mqs.size(); idx > 0; idx--) {
						final MessQuerschnittAllgemein mq = mqs.get(idx - 1);
						final float mqSegOffset = mq.getStrassenSegmentOffset();
						if (mqSegOffset < getStartOffset()) {
							mqDavor = mq;
							break;
						} else {
							logger.finer("MQ: " + mq + "mit Offset " + mqSegOffset
									+ " liegt innerhalb der Baustelle mit Startoffset: " + getStartOffset());
						}
					}
				} else if (mqs.size() > 0) {
					mqDavor = mqs.get(mqs.size() - 1);
					break;
				}

				if (mqDavor != null) {
					continue;
				}

				if (usedSegment instanceof AeusseresStrassenSegment) {
					logger.finer("MQ nicht gefunden - Suche VonKnoten für: " + usedSegment);
					final StrassenKnoten vonKnoten = ((AeusseresStrassenSegment) usedSegment).getVonKnoten();
					StrassenSegment result = null;
					if (vonKnoten != null) {
						logger.finer("Suche passendes inneres Straßensegment in Knoten: " + vonKnoten);
						for (final InneresStrassenSegment innen : vonKnoten.getInnereSegmente()) {
							if (innen.getVonSegment() != null) {
								if (usedSegment.equals(innen.getNachSegment())) {
									final Strasse strasse = segment.getStrasse();
									if ((strasse == null) || (strasse.equals(innen.getStrasse()))) {
										result = innen;
										break;
									}
								} else {
									logger.finer(innen + " führt nicht zu " + usedSegment + ", sondern zu "
											+ innen.getNachSegment());
								}
							} else {
								logger.finer(innen + " hat kein VonSegment");
							}
						}
					} else {
						logger.finer(usedSegment + " hat keinen VonKnoten");
					}
					usedSegment = result;
				} else if (usedSegment instanceof InneresStrassenSegment) {
					logger.finer("MQ nicht gefunden - Suche weiter bei VonSegment des Segments " + usedSegment);
					usedSegment = ((InneresStrassenSegment) usedSegment).getVonSegment();
				} else {
					usedSegment = null;
				}

				logger.finer("MQ nicht gefunden - Setze Suche weiter fort mit Segment: " + usedSegment);
			}
			return mqDavor;
		}

		/**
		 * liefert das Segment mit dem übergebenen Index aus der Liste der
		 * Segmente, die die Situation bilden.
		 *
		 * @param idx
		 *            der gesuchte Index
		 * @return die Liste der Segmente
		 */
		public StrassenSegment getSegment(final int idx) {
			StrassenSegment result = null;
			if ((idx >= 0) && (segmente.size() > idx)) {
				result = segmente.get(idx);
			}
			return result;
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
		 * liefert die Strasse auf der die Situation beginnt. Kann keine Strasse
		 * ermittelt werden , wird der Wert <code>null</code> geliefert.
		 *
		 * @return die Strasse oder <code>null</code>, wenn keine ermittelt
		 *         werden konnte.
		 */
		public Strasse getStrasse() {
			Strasse result = null;
			final List<StrassenSegment> segmentListe = getSegmente();
			if (segmentListe.size() > 0) {
				final StrassenSegment segment = segmentListe.get(0);
				result = segment.getStrasse();
			}
			return result;
		}

		/**
		 * liefert den in Fahrtrichtung auf die Situation folgenden
		 * Straßenknoten. Wird kein Knoten gefunden liefert die Funktion den
		 * Wert <code>null</code> zurück.
		 *
		 * @return den Knoten oder <code>null</code>
		 */
		public StrassenKnoten getVorgaengerKnoten() {
			StrassenKnoten result = null;

			final List<StrassenSegment> segmentListe = getSegmente();
			if (segmentListe.size() > 0) {
				final StrassenSegment segment = segmentListe.get(0);
				if (segment instanceof InneresStrassenSegment) {
					final InneresStrassenSegment iss = (InneresStrassenSegment) segment;
					if (iss.getVonSegment() != null) {
						result = iss.getVonSegment().getVonKnoten();
					}
					if (result == null) {
						if (iss.getVonSegment() != null) {
							result = iss.getVonSegment().getNachKnoten();
						}
					}
				} else if (segment instanceof AeusseresStrassenSegment) {
					final StrassenKnoten knoten = ((AeusseresStrassenSegment) segment).getVonKnoten();
					if (knoten != null) {
						result = knoten;
					}
				}

			}
			return result;
		}

		/**
		 * setzt den aktuellen Status des Datensatzes.
		 *
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * setzt die Dauer der Situation.
		 *
		 * @param dauer
		 *            die Dauer in Millisekunden
		 */
		public void setDauer(final long dauer) {
			this.dauer = dauer;
		}

		/**
		 * setzt den Offset des Endes der Situation bezüglich des letzten
		 * beteiligten Segments.
		 *
		 * @param endOffset
		 *            der Offset in Metern
		 */
		public void setEndOffset(final long endOffset) {
			this.endOffset = endOffset;
		}

		/**
		 * füllt die Liste der beteiligten Straßensegmente mit den übergebenen
		 * Segmenten.
		 *
		 * @param liste
		 *            die Liste der Segmente
		 */
		public void setSegmente(final List<StrassenSegment> liste) {
			segmente.clear();
			segmente.addAll(liste);
		}

		/**
		 * setzt den Offset des Anfangs der Situation bezüglich des ersten
		 * beteiligten Segments.
		 *
		 * @param startOffset
		 *            der Offset in Metern
		 */
		public void setStartOffset(final long startOffset) {
			this.startOffset = startOffset;
		}

		/**
		 * setzt die Startzeit der Situation.
		 *
		 * @param startZeit
		 *            der Zeitpunkt in Millisekunden seit 1.1.1970 0 Uhr GMT
		 */
		public void setStartZeit(final long startZeit) {
			this.startZeit = startZeit;
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
	public PdSituationsEigenschaften(final SystemObjekt objekt) {
		super(objekt);
		if (PdSituationsEigenschaften.attributGruppe == null) {
			PdSituationsEigenschaften.attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.situationsEigenschaften");
		}
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return PdSituationsEigenschaften.attributGruppe;
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data result = erzeugeSendeCache();
		result.getTimeValue("StartZeit").setMillis(datum.getStartZeit());
		result.getTimeValue("Dauer").setMillis(datum.getDauer());
		final List<StrassenSegment> segmente = datum.getSegmente();
		result.getArray("StraßenSegment").setLength(segmente.size());
		for (int idx = 0; idx < segmente.size(); idx++) {
			result.getArray("StraßenSegment").getReferenceValue(idx)
			.setSystemObject(segmente.get(idx).getSystemObject());
		}
		result.getUnscaledValue("StartOffset").set(datum.getStartOffset());
		result.getUnscaledValue("EndOffset").set(datum.getEndOffset());
		result.getUnscaledValue("Länge").set((long) datum.getLaenge());

		return result;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);
		final Daten daten = new Daten(result);
		setDatum(daten);
		daten.setDatenStatus(Datum.Status.getStatus(result.getDataState()));

		fireDatensatzAktualisiert(daten.clone());
	}

}
