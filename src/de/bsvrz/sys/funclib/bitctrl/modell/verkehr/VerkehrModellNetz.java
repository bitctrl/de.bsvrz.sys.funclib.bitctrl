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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Repr&auml;sentiert ein Stra&szlig;ensegment.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @version $Id$
 */
public class VerkehrModellNetz extends Netz implements MutableSetChangeListener {

	/**
	 * Name der Menge, in der die Staus des VerkehrsmodellNetz abgelegt werden.
	 */
	public static final String MENGENNAME_STAUS = "Staus"; //$NON-NLS-1$ 

	/** PID des Typs eines VerkehrsModellNetz. */
	@SuppressWarnings("hiding")
	public static final String PID_TYP = "typ.verkehrsModellNetz"; //$NON-NLS-1$

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
	private final MutableSet baustellenMenge;

	/**
	 * das Systemobjekt, das die Liste der Staus definiert.
	 */
	private final MutableSet stauMenge;

	/**
	 * Konstruiert aus einem Systemobjekt ein Netz.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, welches ein Netz darstellt
	 * @throws IllegalArgumentException
	 */
	VerkehrModellNetz(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(PID_TYP)) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein gültiges VerkehrsModellNetz."); //$NON-NLS-1$
		}

		baustellenMenge = ((ConfigurationObject) obj)
				.getMutableSet("Baustellen");
		stauMenge = ((ConfigurationObject) obj).getMutableSet("Staus");

	}

	/**
	 * fügt dem Netz einen BaustellenListener hinzu.
	 * 
	 * @param listener
	 *            der hinzuzufügende Listener
	 */
	public void addBaustellenListener(final BaustellenListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException(
					"null beim registrieren eines Listeners ist nicht erlaubt");
		}

		boolean registerListener = (listeners
				.getListenerCount(BaustellenListener.class) == 0);
		listeners.add(BaustellenListener.class, listener);

		if (registerListener) {
			System.err.println("Anmeldung für Baustellen == "
					+ baustellenMenge.getElements().size());
			baustellenMenge.addChangeListener(this);
		}
	}

	/**
	 * fügt dem Netz einen BaustellenListener hinzu.
	 * 
	 * @param listener
	 *            der hinzuzufügende Listener
	 */
	public void addStauListener(final StauListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException(
					"null beim registrieren eines Listeners ist nicht erlaubt");
		}

		boolean registerListener = (listeners
				.getListenerCount(StauListener.class) == 0);
		listeners.add(StauListener.class, listener);

		if (registerListener) {
			System.err.println("Anmeldung für Staus == "
					+ stauMenge.getElements().size());
			stauMenge.addChangeListener(this);
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
	private void aktualisiereBaustellen(final SystemObject[] addedObjects,
			final SystemObject[] removedObjects) {
		for (BaustellenListener listener : listeners
				.getListeners(BaustellenListener.class)) {
			for (SystemObject obj : removedObjects) {
				Baustelle bst = (Baustelle) ObjektFactory.getInstanz()
						.getModellobjekt(obj);
				bst.removeNetzReferenz(this);
				listener.baustelleEntfernt(this, bst);
			}
			for (SystemObject obj : addedObjects) {
				Baustelle bst = (Baustelle) ObjektFactory.getInstanz()
						.getModellobjekt(obj);
				bst.addNetzReferenz(this);
				listener.baustelleAngelegt(this, bst);
			}
		}
	}

	/**
	 * benachrichtigt alle StauListener über hinzugefügte oder entfernte Staus.
	 * 
	 * @param addedObjects
	 *            die Systemobjekte, die die hinzugefügten Staus definieren
	 * @param removedObjects
	 *            die Systemobjekte, die die entfernten Staus definieren
	 */
	private void aktualisiereStaus(final SystemObject[] addedObjects,
			final SystemObject[] removedObjects) {
		for (StauListener listener : listeners.getListeners(StauListener.class)) {
			for (SystemObject obj : removedObjects) {
				Stau stau = (Stau) ObjektFactory.getInstanz().getModellobjekt(
						obj);
				stau.removeNetzReferenz(this);
				listener.stauEntfernt(this, stau);
			}
			for (SystemObject obj : addedObjects) {
				Stau stau = (Stau) ObjektFactory.getInstanz().getModellobjekt(
						obj);
				stau.addNetzReferenz(this);
				listener.stauAngelegt(this, stau);
			}
		}
	}

	/**
	 * liefert die Liste der äußeren Straßensegmente, die das Netz bilden und
	 * zur übergebenen Straße gehören. Die Liste enthält alle äußeren
	 * Straßensegmente, die innerhalb des Netzes selbst konfiguriert sind und
	 * zusätzlich die Segmente aus den Listen der Unternetze.
	 * 
	 * @param strasse
	 *            die Straße, für die die äußeren Straßensegemente gesucht
	 *            werden
	 * @return die Liste der ermittelten Straßensegmente
	 */
	public List<AeusseresStrassenSegment> getAssListe(final Strasse strasse) {
		List<AeusseresStrassenSegment> result = new ArrayList<AeusseresStrassenSegment>();
		for (StrassenSegment segment : getNetzSegmentListe()) {
			if (segment instanceof AeusseresStrassenSegment) {
				if ((strasse == null) || strasse.equals(segment.getStrasse())) {
					result.add((AeusseresStrassenSegment) segment);
				}
			}
		}
		return result;
	}

	/**
	 * liefert eine Liste der aktuell innerhalb des VerkehrsmodellNetzes
	 * eingetragenen Baustellen.
	 * 
	 * @return die Liste der Baustellen
	 */
	public Collection<Baustelle> getBaustellen() {
		Collection<Baustelle> result = new ArrayList<Baustelle>();
		for (SystemObject obj : baustellenMenge.getElements()) {
			result.add((Baustelle) ObjektFactory.getInstanz().getModellobjekt(
					obj));
		}
		return result;
	}

	/**
	 * liefert eine Liste der aktuell innerhalb des VerkehrsmodellNetzes
	 * eingetragenen Staus.
	 * 
	 * @return die Liste der Staus
	 */
	public Collection<Stau> getStaus() {
		Collection<Stau> result = new ArrayList<Stau>();
		for (SystemObject obj : stauMenge.getElements()) {
			result.add((Stau) ObjektFactory.getInstanz().getModellobjekt(obj));
		}
		return result;
	}

	/**
	 * entfernt einen Baustellenlistener vom Netz.
	 * 
	 * @param listener
	 *            der zu entfernende Baustellenlistener
	 */
	public void removeBaustellenListener(final BaustellenListener listener) {
		if (listener != null) {
			listeners.remove(BaustellenListener.class, listener);
			if (listeners.getListenerCount(BaustellenListener.class) == 0) {
				baustellenMenge.removeChangeListener(this);
			}
		}
	}

	/**
	 * entfernt ein Stauobjekt mit dem übergeben Systemobjekt vom Netz. Das
	 * Objekt wird in die Menge der Staus des VerkehrsmodellNetz ausgetragen.
	 * 
	 * @param obj
	 *            das zu entfernende Stauobjekt
	 */
	public void stauEntfernen(final SystemObject obj) {
		ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);
		if (set.getElements().contains(obj)) {
			try {
				set.remove(obj);
			} catch (ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * fügt den Netz ein Stauobjekt mit dem übergeben Systemobjekt hinzu. Das
	 * Objekt wird in die Menge der Staus des VerkehrsmodellNetz eingetragen.
	 * 
	 * @param obj
	 *            das neue Stauobjekt
	 */
	public void stauHinzufuegen(final SystemObject obj) {
		ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);
		if (!set.getElements().contains(obj)) {
			try {
				set.add(obj);
			} catch (ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
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
		System.err.println("Anzahl der Baustellen == "
				+ set.getElements().size());
		if (set.equals(baustellenMenge)) {
			aktualisiereBaustellen(addedObjects, removedObjects);
		} else if (set.equals(stauMenge)) {
			aktualisiereStaus(addedObjects, removedObjects);
		}
	}
}
