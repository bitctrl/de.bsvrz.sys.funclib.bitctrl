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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.event.EventListenerList;
import javax.swing.text.Segment;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter.PdBaustellenEigenschaften;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter.PdSituationsEigenschaften;

/**
 * Repr&auml;sentiert eine Baustelle.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class Baustelle extends Situation {

	/**
	 * die Menge der Netze in denen die die Baustelle referenziert wird.
	 */
	private final Set<VerkehrModellNetz> netze = new HashSet<VerkehrModellNetz>();

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz einer Baustelle auf der Basis des
	 * übergebenen Systemobjekts.
	 * 
	 * @param obj
	 *            das Systemobjekt, das die Baustelle definiert
	 */
	protected Baustelle(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist keine Baustelle.");
		}
	}

	// public void addBaustellenUpdateListener(BaustellenUpdateListener
	// listener) {
	// boolean registerListeners = listeners
	// .getListenerCount(BaustellenUpdateListener.class) == 0;
	//
	// listeners.add(BaustellenUpdateListener.class, listener);
	//
	// if (registerListeners) {
	// getSituationsEigenschaften().update();
	// getSituationsEigenschaften().addUpdateListener(this);
	// getSituationsEigenschaften().setAutoUpdate(true);
	//
	// getBaustellenEigenschaften().update();
	// getBaustellenEigenschaften().addUpdateListener(this);
	// getBaustellenEigenschaften().setAutoUpdate(true);
	// }
	// }

	/**
	 * fügt der Baustelle eine Netzreferenz hinzu.
	 * 
	 * @param netz
	 *            das Netz für das eine Referenz hinzugefügt wird.
	 */
	public void addNetzReferenz(VerkehrModellNetz netz) {
		netze.add(netz);
	}

	public PdBaustellenEigenschaften getBaustellenEigenschaften() {
		return (PdBaustellenEigenschaften) getParameterDatensatz(PdBaustellenEigenschaften.class);
	}

	public String getFolgeKnotenName() {
		String result = "unbekannt";
		PdSituationsEigenschaften daten = getSituationsEigenschaften();
		List<StrassenSegment> segmente = daten.getSegmente();
		if (segmente.size() > 0) {
			StrassenSegment segment = segmente.get(0);
			if (segment instanceof InneresStrassenSegment) {
				segment = ((InneresStrassenSegment) segment).getNachSegment();
			}
			if (segment instanceof AeusseresStrassenSegment) {
				StrassenKnoten knoten = ((AeusseresStrassenSegment) segment)
						.getNachKnoten();
				if (knoten != null) {
					result = knoten.getName();
				}
			}

		}
		return result;
	}

	public double getLaenge() {
		double result = 0;
		PdSituationsEigenschaften daten = getSituationsEigenschaften();
		for (StrassenSegment segment : daten.getSegmente()) {
			result += segment.getLaenge();
		}

		result -= (daten.getStartOffset() + daten.getEndOffset());
		return result;
	}

	/**
	 * liefert die Menge der Netze in denen die Baustelle refernziert wird.
	 * 
	 * @return die Menge der Netze
	 */
	public Set<VerkehrModellNetz> getNetze() {
		return netze;
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
		List<StrassenSegment> segmente = getSituationsEigenschaften()
				.getSegmente();
		if (segmente.size() > 0) {
			StrassenSegment segment = segmente.get(0);
			result = segment.getStrasse();
		}
		return result;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.BAUSTELLE;
	}

	/**
	 * entfernt eine Netzreferenz von der Baustelle.
	 * 
	 * @param netz
	 *            das Netz auf das die Referenz entfernt wird.
	 */
	public void removeNetzReferenz(VerkehrModellNetz netz) {
		netze.remove(netz);
	}
}
