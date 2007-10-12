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
import java.util.Set;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;sentiert eine Baustelle.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class Baustelle extends Situation implements DatensatzUpdateListener {

	EventListenerList listeners = new EventListenerList();

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

	public void addBaustellenUpdateListener(BaustellenUpdateListener listener) {
		boolean registerListeners = listeners
				.getListenerCount(BaustellenUpdateListener.class) == 0;

		listeners.add(BaustellenUpdateListener.class, listener);

		if (registerListeners) {
			getSituationsEigenschaften().update();
			getSituationsEigenschaften().setAutoUpdate(true);
			getSituationsEigenschaften().addUpdateListener(this);

			getBaustellenEigenschaften().update();
			getBaustellenEigenschaften().setAutoUpdate(true);
			getBaustellenEigenschaften().addUpdateListener(this);
		}
	}

	/**
	 * fügt der Baustelle eine Netzreferenz hinzu.
	 * 
	 * @param netz
	 *            das Netz für das eine Referenz hinzugefügt wird.
	 */
	public void addNetzReferenz(VerkehrModellNetz netz) {
		netze.add(netz);
	}

	public void datensatzAktualisiert(DatensatzUpdateEvent event) {
		BaustellenUpdateEvent bstEvent = new BaustellenUpdateEvent(this);
		for (BaustellenUpdateListener listener : listeners
				.getListeners(BaustellenUpdateListener.class)) {
			listener.baustelleAktualisiert(bstEvent);
		}
	}

	public BaustellenEigenschaften getBaustellenEigenschaften() {
		return (BaustellenEigenschaften) getParameterDatensatz(BaustellenEigenschaften.class);
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
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.BAUSTELLE;
	}

	public void removeBaustellenUpdateListener(BaustellenUpdateListener listener) {
		listeners.remove(BaustellenUpdateListener.class, listener);

		if (listeners.getListenerCount(BaustellenUpdateListener.class) <= 0) {
			getSituationsEigenschaften().removeUpdateListener(this);
			getBaustellenEigenschaften().removeUpdateListener(this);
		}
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
