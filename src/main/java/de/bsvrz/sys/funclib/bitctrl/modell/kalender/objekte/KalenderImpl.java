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
 */
public class KalenderImpl extends AbstractSystemObjekt implements Kalender {

	/**
	 * Kapselt die Datenverteilerlogik.
	 */
	private class MengenObserver implements MutableSetChangeListener {

		@Override
		public void update(final MutableSet set,
				final SystemObject[] addedObjects,
				final SystemObject[] removedObjects) {
			final ObjektFactory factory = ObjektFactory.getInstanz();

			if (set.equals(ereignisse)) {
				final Set<Ereignis> hinzu, entfernt;

				hinzu = new HashSet<>();
				for (final SystemObject so : addedObjects) {
					hinzu.add((Ereignis) factory.getModellobjekt(so));
				}

				entfernt = new HashSet<>();
				for (final SystemObject so : removedObjects) {
					entfernt.add((Ereignis) factory.getModellobjekt(so));
				}

				fireEreignisseAktualisiert(hinzu, entfernt);
			} else if (set.equals(ereignisTypen)) {
				final Set<EreignisTyp> hinzu, entfernt;

				hinzu = new HashSet<>();
				for (final SystemObject so : addedObjects) {
					hinzu.add((EreignisTyp) factory.getModellobjekt(so));
				}

				entfernt = new HashSet<>();
				for (final SystemObject so : removedObjects) {
					entfernt.add((EreignisTyp) factory.getModellobjekt(so));
				}

				fireEreignisTypenAktualisiert(hinzu, entfernt);
			} else if (set.equals(systemKalenderEintraege)) {
				final Set<SystemKalenderEintrag> hinzu, entfernt;

				hinzu = new HashSet<>();
				for (final SystemObject so : addedObjects) {
					hinzu.add((SystemKalenderEintrag) factory
							.getModellobjekt(so));
				}

				entfernt = new HashSet<>();
				for (final SystemObject so : removedObjects) {
					entfernt.add((SystemKalenderEintrag) factory
							.getModellobjekt(so));
				}

				fireSystemKalenderEintragAktualisiert(hinzu, entfernt);
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

	/** Die Eigenschaft {@code systemKalenderEintraege}. */
	private final MutableSet systemKalenderEintraege;

	/**
	 * Initialisiert das Objekt.
	 *
	 * @param obj
	 *            ein Systemobjekt, welches ein Kalender sein muss.
	 */
	public KalenderImpl(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Kalender.");
		}

		final ConfigurationObject co;

		co = (ConfigurationObject) getSystemObject();
		ereignisTypen = co.getMutableSet("EreignisTypen");
		ereignisse = co.getMutableSet("Ereignisse");
		systemKalenderEintraege = co.getMutableSet("SystemKalenderEinträge");
	}

	@Override
	public void add(final Ereignis ereignis)
			throws ConfigurationChangeException {
		ereignisse.add(ereignis.getSystemObject());
	}

	@Override
	public void add(final EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		ereignisTypen.add(ereignisTyp.getSystemObject());
	}

	@Override
	public void add(final SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException {
		systemKalenderEintraege.add(eintrag.getSystemObject());
	}

	@Override
	public void addEreignisListener(final EreignisListener l) {
		boolean anmelden = false;

		if (listener.getListenerCount(EreignisListener.class) == 0) {
			anmelden = true;
		}

		listener.add(EreignisListener.class, l);

		if (anmelden) {
			ereignisse.addChangeListener(mengenObserver);
		}
	}

	@Override
	public void addEreignisTypListener(final EreignisTypListener l) {
		boolean anmelden = false;

		if (listener.getListenerCount(EreignisTypListener.class) == 0) {
			anmelden = true;
		}

		listener.add(EreignisTypListener.class, l);

		if (anmelden) {
			ereignisTypen.addChangeListener(mengenObserver);
		}
	}

	@Override
	public void addSystemKalenderEintragListener(
			final SystemKalenderEintragListener l) {
		boolean anmelden = false;

		if (listener
				.getListenerCount(SystemKalenderEintragListener.class) == 0) {
			anmelden = true;
		}

		listener.add(SystemKalenderEintragListener.class, l);

		if (anmelden) {
			systemKalenderEintraege.addChangeListener(mengenObserver);
		}
	}

	@Override
	public Set<Ereignis> getEreignisse() {
		final Set<Ereignis> result;

		result = new HashSet<>();
		for (final SystemObject so : ereignisse.getElements()) {
			result.add(
					(Ereignis) ObjektFactory.getInstanz().getModellobjekt(so));
		}

		return result;
	}

	@Override
	public Set<EreignisTyp> getEreignisTypen() {
		final Set<EreignisTyp> result;

		result = new HashSet<>();
		for (final SystemObject so : ereignisTypen.getElements()) {
			result.add((EreignisTyp) ObjektFactory.getInstanz()
					.getModellobjekt(so));
		}

		return result;
	}

	@Override
	public Set<SystemKalenderEintrag> getSystemKalenderEintraege() {
		final Set<SystemKalenderEintrag> result;

		result = new HashSet<>();
		for (final SystemObject so : systemKalenderEintraege.getElements()) {
			result.add((SystemKalenderEintrag) ObjektFactory.getInstanz()
					.getModellobjekt(so));
		}

		return result;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.KALENDER;
	}

	@Override
	public void remove(final Ereignis ereignis)
			throws ConfigurationChangeException {
		ereignisse.remove(ereignis.getSystemObject());
	}

	@Override
	public void remove(final EreignisTyp ereignisTyp)
			throws ConfigurationChangeException {
		ereignisTypen.remove(ereignisTyp.getSystemObject());
	}

	@Override
	public void remove(final SystemKalenderEintrag eintrag)
			throws ConfigurationChangeException {
		systemKalenderEintraege.remove(eintrag.getSystemObject());
	}

	@Override
	public void removeEreignisListener(final EreignisListener l) {
		listener.remove(EreignisListener.class, l);

		if (listener.getListenerCount(EreignisListener.class) == 0) {
			ereignisse.removeChangeListener(mengenObserver);
		}
	}

	@Override
	public void removeEreignisTypListener(final EreignisTypListener l) {
		listener.remove(EreignisTypListener.class, l);

		if (listener.getListenerCount(EreignisTypListener.class) == 0) {
			ereignisTypen.removeChangeListener(mengenObserver);
		}
	}

	@Override
	public void removeSystemKalenderEintragListener(
			final SystemKalenderEintragListener l) {
		listener.remove(SystemKalenderEintragListener.class, l);

		if (listener
				.getListenerCount(SystemKalenderEintragListener.class) == 0) {
			systemKalenderEintraege.removeChangeListener(mengenObserver);
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
	protected void fireEreignisseAktualisiert(final Set<Ereignis> hinzu,
			final Set<Ereignis> entfernt) {
		final EreignisseAktualisiertEvent e;

		e = new EreignisseAktualisiertEvent(this, hinzu, entfernt);
		for (final EreignisListener l : listener
				.getListeners(EreignisListener.class)) {
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
	protected void fireEreignisTypenAktualisiert(final Set<EreignisTyp> hinzu,
			final Set<EreignisTyp> entfernt) {
		final EreignisTypenAktualisiertEvent e;

		e = new EreignisTypenAktualisiertEvent(this, hinzu, entfernt);
		for (final EreignisTypListener l : listener
				.getListeners(EreignisTypListener.class)) {
			l.ereignisTypenAktualisiert(e);
		}
	}

	/**
	 * Teilt den angemeldeten Listener die Änderung der Menge der
	 * Systemkalendereinträge mit.
	 *
	 * @param hinzu
	 *            die Menge der hinzugefügten Systemkalendereinträge.
	 * @param entfernt
	 *            die Menge der entfernten Systemkalendereinträge.
	 */
	protected void fireSystemKalenderEintragAktualisiert(
			final Set<SystemKalenderEintrag> hinzu,
			final Set<SystemKalenderEintrag> entfernt) {
		final SystemKalenderEintraegeAktualisiertEvent e;

		e = new SystemKalenderEintraegeAktualisiertEvent(this, hinzu, entfernt);
		for (final SystemKalenderEintragListener l : listener
				.getListeners(SystemKalenderEintragListener.class)) {
			l.systemKalenderEintraegeAktualisiert(e);
		}
	}

}
