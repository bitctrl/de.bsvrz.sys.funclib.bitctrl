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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.LmsModellTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.BaustellenListener;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Repr&auml;sentiert eine Landesmeldestelle.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @version $Id$
 */
public class LandesMeldeStelle extends AbstractSystemObjekt implements
		MutableSetChangeListener {

	/**
	 * Name der Menge, in der die Staus des VerkehrsmodellNetz abgelegt werden.
	 */
	public static final String MENGENNAME_MELDUNGEN = "RDSMeldungen"; //$NON-NLS-1$ 

	/**
	 * Logger für Fehlerausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die Liste der von der Klasse verwalteten Listener.
	 */
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * das Systemobjekt, das die Liste der Baustellen definiert.
	 */
	private final MutableSet meldungsMenge;

	/**
	 * Konstruiert aus einem Systemobjekt ein Netz.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Netz darstellt
	 * @throws IllegalArgumentException
	 */
	public LandesMeldeStelle(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(LmsModellTypen.LANDESMELDESTELLE.getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein gültiges VerkehrsModellNetz."); //$NON-NLS-1$
		}

		meldungsMenge = ((ConfigurationObject) obj)
				.getMutableSet(MENGENNAME_MELDUNGEN);
	}

	/**
	 * fügt dem Netz einen MeldungsListener hinzu.
	 * 
	 * @param listener
	 *            der hinzuzufügende Listener
	 */
	public void addBaustellenListener(final MeldungsListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException(
					"null beim registrieren eines Listeners ist nicht erlaubt");
		}

		boolean registerListener = (listeners
				.getListenerCount(MeldungsListener.class) == 0);
		listeners.add(MeldungsListener.class, listener);

		if (registerListener) {
			System.err.println("Anmeldung für Baustellen == "
					+ meldungsMenge.getElements().size());
			meldungsMenge.addChangeListener(this);
		}
	}

	/**
	 * benachrichtigt alle BaustellenListener über hinzugefügte oder entfernte
	 * Baustellen.
	 * 
	 * @param addedObjects
	 *            die Systemobjekte, die die hinzugefügten Baustellen definieren
	 * @param removedObjects
	 *            die Systemobjekte, die die entfernten Baustellen definieren
	 */
	private void aktualisiereMeldungen(final SystemObject[] addedObjects,
			final SystemObject[] removedObjects) {
		for (MeldungsListener listener : listeners
				.getListeners(MeldungsListener.class)) {
			for (SystemObject obj : removedObjects) {
				RdsMeldung meldung = (RdsMeldung) ObjektFactory.getInstanz()
						.getModellobjekt(obj);
				meldung.removeLmsReferenz(this);
				listener.meldungEntfernt(this, meldung);
			}
			for (SystemObject obj : addedObjects) {
				RdsMeldung meldung = (RdsMeldung) ObjektFactory.getInstanz()
						.getModellobjekt(obj);
				meldung.addLmsReferenz(this);
				listener.meldungAngelegt(this, meldung);
			}
		}
	}

	/**
	 * liefert eine Liste der aktuell innerhalb des VerkehrsmodellNetzes
	 * eingetragenen Baustellen.
	 * 
	 * @return die Liste der Baustellen
	 */
	public Collection<RdsMeldung> getMeldungen() {
		Collection<RdsMeldung> result = new ArrayList<RdsMeldung>();
		for (SystemObject obj : meldungsMenge.getElements()) {
			RdsMeldung meldung = (RdsMeldung) ObjektFactory.getInstanz()
					.getModellobjekt(obj);
			meldung.addLmsReferenz(this);
			result.add(meldung);

		}
		return result;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return LmsModellTypen.LANDESMELDESTELLE;
	}

	/**
	 * entfernt ein Baustellenobjekt mit dem übergeben Systemobjekt vom Netz.
	 * Das Objekt wird aus der Menge der Baustellen des VerkehrsmodellNetz
	 * ausgetragen.
	 * 
	 * @param obj
	 *            das zu entfernende Stauobjekt
	 */
	public void meldungEntfernen(final SystemObject obj) {
		ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_MELDUNGEN);
		if (set.getElements().contains(obj)) {
			try {
				set.remove(obj);
			} catch (ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * fügt den Netz eine Baustelle mit dem übergeben Systemobjekt hinzu. Das
	 * Objekt wird in die Menge der Baustellen des VerkehrsmodellNetz
	 * eingetragen.
	 * 
	 * @param obj
	 *            das neue Baustellen
	 */
	public void meldungHinzufuegen(final SystemObject obj) {
		ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_MELDUNGEN);
		if (!set.getElements().contains(obj)) {
			try {
				set.add(obj);
			} catch (ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * entfernt einen Baustellenlistener vom Netz.
	 * 
	 * @param listener
	 *            der zu entfernende Baustellenlistener
	 */
	public void removeMeldungsListener(final MeldungsListener listener) {
		if (listener != null) {
			listeners.remove(MeldungsListener.class, listener);
			if (listeners.getListenerCount(BaustellenListener.class) == 0) {
				meldungsMenge.removeChangeListener(this);
			}
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.dav.daf.main.config.MutableSetChangeListener#update(de.bsvrz.dav.daf.main.config.MutableSet,
	 *      de.bsvrz.dav.daf.main.config.SystemObject[],
	 *      de.bsvrz.dav.daf.main.config.SystemObject[])
	 */
	public void update(final MutableSet set, final SystemObject[] addedObjects,
			final SystemObject[] removedObjects) {
		if (set.equals(meldungsMenge)) {
			System.err.println("Anzahl der Baustellen == "
					+ set.getElements().size());
			aktualisiereMeldungen(addedObjects, removedObjects);
		}
	}
}
