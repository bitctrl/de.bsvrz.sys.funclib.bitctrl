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

package de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kalender.KalenderModellTypen;

/**
 * Implementiert den Ereigniskalender. Anstelle dieser Klasse, sollte immer die
 * Schnittstelle {@link Kalender} verwendet werden.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class KalenderImpl extends AbstractSystemObjekt implements Kalender {

	/**
	 * Kapselt die Datenverteilerlogik.
	 */
	private class MengenObserver implements MutableSetChangeListener {

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.config.MutableSetChangeListener#update(de.bsvrz.dav.daf.main.config.MutableSet,
		 *      de.bsvrz.dav.daf.main.config.SystemObject[],
		 *      de.bsvrz.dav.daf.main.config.SystemObject[])
		 */
		public void update(MutableSet set, SystemObject[] addedObjects,
				SystemObject[] removedObjects) {
			ObjektFactory factory = ObjektFactory.getInstanz();

			if (set.equals(ereignisse)) {
				Set<Ereignis> hinzu, entfernt;

				hinzu = new HashSet<Ereignis>();
				for (SystemObject so : addedObjects) {
					hinzu.add((Ereignis) factory.getModellobjekt(so));
				}

				entfernt = new HashSet<Ereignis>();
				for (SystemObject so : removedObjects) {
					entfernt.add((Ereignis) factory.getModellobjekt(so));
				}

				fireEreignisseAktualisiert(hinzu, entfernt);
			} else if (set.equals(ereignisTypen)) {
				Set<EreignisTyp> hinzu, entfernt;

				hinzu = new HashSet<EreignisTyp>();
				for (SystemObject so : addedObjects) {
					hinzu.add((EreignisTyp) factory.getModellobjekt(so));
				}

				entfernt = new HashSet<EreignisTyp>();
				for (SystemObject so : removedObjects) {
					entfernt.add((EreignisTyp) factory.getModellobjekt(so));
				}

				fireEreignisTypenAktualisiert(hinzu, entfernt);
			}
		}
	}

	/** Die Eigenschaft {@code listener}. */
	private final EventListenerList listener = new EventListenerList();

	/** Die Eigenschaft {@code mengenObserver}. */
	private final MengenObserver mengenObserver = new MengenObserver();

	/** Die Eigenschaft {@code ereignisTypen}. */
	private final MutableSet ereignisTypen;

	/** Die Eigenschaft {@code ereignisse}. */
	private final MutableSet ereignisse;

	/**
	 * Initialisiert das Objekt.
	 * 
	 * @param obj
	 *            ein Systemobjekt, welches ein Kalender sein muss.
	 */
	public KalenderImpl(SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Kalender.");
		}

		ConfigurationObject co;

		co = (ConfigurationObject) getSystemObject();
		ereignisTypen = co.getMutableSet("EreignisTypen");
		ereignisse = co.getMutableSet("Ereignisse");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#add(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis)
	 */
	public void add(Ereignis ereignis) throws ConfigurationChangeException {
		ereignisse.add(ereignis.getSystemObject());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#add(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp)
	 */
	public void add(EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		ereignisTypen.add(ereignisTyp.getSystemObject());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#addEreignisListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisListener)
	 */
	public void addEreignisListener(EreignisListener l) {
		boolean anmelden = false;

		if (listener.getListenerCount(EreignisListener.class) == 0) {
			anmelden = true;
		}

		listener.add(EreignisListener.class, l);

		if (anmelden) {
			ereignisse.addChangeListener(mengenObserver);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#addEreignisTypListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTypListener)
	 */
	public void addEreignisTypListener(EreignisTypListener l) {
		boolean anmelden = false;

		if (listener.getListenerCount(EreignisTypListener.class) == 0) {
			anmelden = true;
		}

		listener.add(EreignisTypListener.class, l);

		if (anmelden) {
			ereignisTypen.addChangeListener(mengenObserver);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#getEreignisse()
	 */
	public Set<Ereignis> getEreignisse() {
		Set<Ereignis> result;

		result = new HashSet<Ereignis>();
		for (SystemObject so : ereignisse.getElements()) {
			result.add((Ereignis) ObjektFactory.getInstanz()
					.getModellobjekt(so));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#getEreignisTypen()
	 */
	public Set<EreignisTyp> getEreignisTypen() {
		Set<EreignisTyp> result;

		result = new HashSet<EreignisTyp>();
		for (SystemObject so : ereignisTypen.getElements()) {
			result.add((EreignisTyp) ObjektFactory.getInstanz()
					.getModellobjekt(so));
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.KALENDER;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#remove(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Ereignis)
	 */
	public void remove(Ereignis ereignis) throws ConfigurationChangeException {
		ereignisse.remove(ereignis.getSystemObject());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#remove(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTyp)
	 */
	public void remove(EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		ereignisTypen.remove(ereignisTyp.getSystemObject());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#removeEreignisListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisListener)
	 */
	public void removeEreignisListener(EreignisListener l) {
		listener.remove(EreignisListener.class, l);

		if (listener.getListenerCount(EreignisListener.class) == 0) {
			ereignisse.removeChangeListener(mengenObserver);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.Kalender#removeEreignisTypListener(de.bsvrz.sys.funclib.bitctrl.modell.kalender.objekte.EreignisTypListener)
	 */
	public void removeEreignisTypListener(EreignisTypListener l) {
		listener.remove(EreignisTypListener.class, l);

		if (listener.getListenerCount(EreignisTypListener.class) == 0) {
			ereignisTypen.removeChangeListener(mengenObserver);
		}
	}

	/**
	 * Teilt den angemeldeten Listener die Änderung der Ereignismenge mit.
	 * 
	 * @param hinzu
	 *            die Menge der hinzugefügten Ereignisse.
	 * @param entfernt
	 *            die Menge der entfernten Ereignisse.
	 */
	protected void fireEreignisseAktualisiert(Set<Ereignis> hinzu,
			Set<Ereignis> entfernt) {
		EreignisseAktualisiertEvent e;

		e = new EreignisseAktualisiertEvent(this, hinzu, entfernt);
		for (EreignisListener l : listener.getListeners(EreignisListener.class)) {
			l.ereignisseAktualisiert(e);
		}
	}

	/**
	 * Teilt den angemeldeten Listener die Änderung der Ereignistypmenge mit.
	 * 
	 * @param hinzu
	 *            die Menge der hinzugefügten Ereignistypen.
	 * @param entfernt
	 *            die Menge der entfernten Ereignistypen.
	 */
	protected void fireEreignisTypenAktualisiert(Set<EreignisTyp> hinzu,
			Set<EreignisTyp> entfernt) {
		EreignisTypenAktualisiertEvent e;

		e = new EreignisTypenAktualisiertEvent(this, hinzu, entfernt);
		for (EreignisTypListener l : listener
				.getListeners(EreignisTypListener.class)) {
			l.ereignisTypenAktualisiert(e);
		}
	}

}
