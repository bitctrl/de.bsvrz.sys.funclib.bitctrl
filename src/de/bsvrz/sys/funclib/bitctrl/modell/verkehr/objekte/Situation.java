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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.tmc.zustaende.TmcRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter.PdSituationsEigenschaften;

/**
 * Repr&auml;sentiert eine Situation.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public abstract class Situation extends AbstractSystemObjekt {

	/**
	 * die Menge der Netze in denen die Situation referenziert wird.
	 */
	private final Set<VerkehrModellNetz> netze = new HashSet<VerkehrModellNetz>();

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz eines Situation auf der Basis des
	 * übergebenen Systemobjekts.
	 * 
	 * @param obj
	 *            das Systemobjekt, das die Situation definiert
	 */
	public Situation(final SystemObject obj) {
		super(obj);
	}

	/**
	 * fügt der Situation eine Netzreferenz hinzu.
	 * 
	 * @param netz
	 *            das Netz für das eine Referenz hinzugefügt wird.
	 */
	public void addNetzReferenz(final VerkehrModellNetz netz) {
		netze.add(netz);
	}

	/**
	 * liefert den in Fahrtrichtung auf die Baustelle folgenden Straßenknoten.
	 * Wird kein Knoten gefunden liefert die Funktion den Wert <code>null</code>
	 * zurück.
	 * 
	 * @return den Knoten oder <code>null</code>
	 */
	public StrassenKnoten getFolgeKnoten() {
		StrassenKnoten result = null;
		PdSituationsEigenschaften daten = getSituationsEigenschaften();
		List<StrassenSegment> segmente = daten.getDatum().getSegmente();
		if (segmente.size() > 0) {
			StrassenSegment segment = segmente.get(0);
			if (segment instanceof InneresStrassenSegment) {
				segment = ((InneresStrassenSegment) segment).getNachSegment();
			}
			if (segment instanceof AeusseresStrassenSegment) {
				StrassenKnoten knoten = ((AeusseresStrassenSegment) segment)
						.getNachKnoten();
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
		PdSituationsEigenschaften daten = getSituationsEigenschaften();
		for (StrassenSegment segment : daten.getDatum().getSegmente()) {
			result += segment.getLaenge();
			letztesSegment = segment;
		}

		result -= daten.getDatum().getStartOffset();
		if (letztesSegment != null) {
			result -= (letztesSegment.getLaenge() - daten.getDatum()
					.getEndOffset());
		}
		return result;
	}

	/**
	 * liefert die Menge der Netze in denen die Situation referenziert wird.
	 * 
	 * @return die Menge der Netze
	 */
	public Set<VerkehrModellNetz> getNetze() {
		return netze;
	}

	/**
	 * liefert den Datensatz zum Speichern der Sitautionseigenschaften.
	 * 
	 * @return den Datensatz
	 */
	public PdSituationsEigenschaften getSituationsEigenschaften() {
		return getParameterDatensatz(PdSituationsEigenschaften.class);
	}

	/**
	 * liefert die Strasse auf der die Baustelle beginnt. Kann keine Strasse
	 * ermittelt werden , wird der Wert <code>null</code> geliefert.
	 * 
	 * @return die Strasse oder <code>null</code>, wenn keine ermittelt
	 *         werden konnte.
	 */
	public Strasse getStrasse() {
		Strasse result = null;
		PdSituationsEigenschaften.Daten daten = getSituationsEigenschaften()
				.getDatum();
		if ((daten != null) && daten.isValid()) {
			List<StrassenSegment> segmente = getSituationsEigenschaften()
					.getDatum().getSegmente();
			for (StrassenSegment seg : segmente) {
				result = seg.getStrasse();
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}

	/**
	 * @param anpassen
	 * @return
	 */
	/**
	 * ermittelt den Name der Strasse. Wenn die TMC-Richtung nicht NEGATIV ist
	 * wird der Name der zugeordneten Strasse geliefert. Wenn keine Strasse
	 * zugeordnet wurde, der String "undefiniert". Im Fall der negativen
	 * Richtung wird versucht einen Name der Form "&lt;strasse&gt; von
	 * &lt;anfang&gt; nach &lt;ende&gt;" entsprechend anzupassen.
	 * 
	 * @param anpassen
	 *            definiert, ob eine richtungsbezogene Anpassung des Namnes
	 *            versucht werden soll
	 * @return der Name
	 */
	public String getStrassenName(boolean anpassen) {
		Strasse strasse = getStrasse();
		String name = null;
		if (strasse != null) {
			name = strasse.getName();
		}

		if ((name != null) && anpassen) {
			TmcRichtung richtung = getTmcRichtung();
			if (richtung == TmcRichtung.NEGATIV) {
				StringTokenizer tokenizer = new StringTokenizer(name, " ");
				if (tokenizer.countTokens() == 5) {
					String strName = tokenizer.nextToken().trim();
					String von = tokenizer.nextToken().trim();
					String anfang = tokenizer.nextToken().trim();
					String nach = tokenizer.nextToken().trim();
					String ende = tokenizer.nextToken().trim();

					if ("von".equals(von) && "nach".equals(nach)) {
						name = strName + " von " + ende + " nach " + anfang;
					}
				}
			}
		}

		if (name == null) {
			name = "undefiniert";
		}

		return name;
	}

	/**
	 * liefert die Richtung in der der Stau liegt. Kann keine Rcihtung ermittelt
	 * werden , wird der Wert {@value TmcRichtung#UNDEFINIERT} geliefert.
	 * 
	 * @return die Strasse oder <code>null</code>, wenn keine ermittelt
	 *         werden konnte.
	 */
	public TmcRichtung getTmcRichtung() {
		TmcRichtung result = null;
		List<StrassenSegment> segmente = getSituationsEigenschaften()
				.getDatum().getSegmente();
		for (StrassenSegment seg : segmente) {
			if (seg instanceof AeusseresStrassenSegment) {
				result = ((AeusseresStrassenSegment) seg).getTmcRichtung();
				if (result != null) {
					switch (result) {
					case NEGATIV:
					case POSITIV:
						break;
					case OHNE:
					case UNDEFINIERT:
						result = null;
					}
				}
			}
			if (result != null) {
				break;
			}
		}
		return result;
	}

	/**
	 * liefert den in Fahrtrichtung vor der Baustelle liegenden Straßenknoten.
	 * Wird kein Knoten gefunden liefert die Funktion den Wert <code>null</code>
	 * zurück.
	 * 
	 * @return den Knoten oder <code>null</code>
	 */
	public StrassenKnoten getVonKnoten() {
		StrassenKnoten result = null;
		PdSituationsEigenschaften daten = getSituationsEigenschaften();
		List<StrassenSegment> segmente = daten.getDatum().getSegmente();
		if (segmente.size() > 0) {
			StrassenSegment segment = segmente.get(0);
			if (segment instanceof AeusseresStrassenSegment) {
				result = ((AeusseresStrassenSegment) segment).getVonKnoten();
			} else if (segment instanceof InneresStrassenSegment) {
				result = ((InneresStrassenSegment) segment).getStrassenKnoten();
			}
		}
		return result;
	}

	/**
	 * entfernt eine Netzreferenz von der Situation.
	 * 
	 * @param netz
	 *            das Netz auf das die Referenz entfernt wird.
	 */
	public void removeNetzReferenz(final VerkehrModellNetz netz) {
		netze.remove(netz);
	}

}
