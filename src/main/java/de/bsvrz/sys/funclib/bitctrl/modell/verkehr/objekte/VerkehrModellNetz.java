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
 */
public class VerkehrModellNetz extends Netz
implements MutableSetChangeListener {

	/**
	 * Name der Menge, in der die Staus des VerkehrsmodellNetz abgelegt werden.
	 */
	public static final String MENGENNAME_STAUS = "Staus";

	/**
	 * Name der Menge, in der die Baustellen des VerkehrsmodellNetz abgelegt
	 * werden.
	 */
	public static final String MENGENNAME_BAUSTELLEN = "Baustellen";

	/** PID des Typs eines VerkehrsModellNetz. */
	public static final String PID_TYP = "typ.verkehrsModellNetz";

	/**
	 * Logger für Fehlerausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die Liste der von der Klasse verwalteten Listener.
	 */
	private final EventListenerList listeners = new EventListenerList();

	/** das Systemobjekt, das die Liste der Baustellen definiert. */
	private final MutableSet baustellenMenge;

	/** das Systemobjekt, das die Liste der Staus definiert. */
	private final MutableSet stauMenge;

	/**
	 * Konstruiert aus einem Systemobjekt ein Netz.
	 *
	 * @param obj
	 *            Ein Systemobjekt, welches ein Netz darstellt
	 * @throws IllegalArgumentException
	 */
	public VerkehrModellNetz(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(PID_TYP)) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein gültiges VerkehrsModellNetz.");
		}

		baustellenMenge = ((ConfigurationObject) obj)
				.getMutableSet(MENGENNAME_BAUSTELLEN);
		stauMenge = ((ConfigurationObject) obj).getMutableSet(MENGENNAME_STAUS);
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

		final boolean registerListener = (listeners
				.getListenerCount(BaustellenListener.class) == 0);
		listeners.add(BaustellenListener.class, listener);

		if (registerListener) {
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

		final boolean registerListener = (listeners
				.getListenerCount(StauListener.class) == 0);
		listeners.add(StauListener.class, listener);

		if (registerListener) {
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
		for (final BaustellenListener listener : listeners
				.getListeners(BaustellenListener.class)) {
			for (final SystemObject obj : removedObjects) {
				final Baustelle bst = (Baustelle) ObjektFactory.getInstanz()
						.getModellobjekt(obj);
				bst.removeNetzReferenz(this);
				listener.baustelleEntfernt(this, bst);
			}
			for (final SystemObject obj : addedObjects) {
				final Baustelle bst = (Baustelle) ObjektFactory.getInstanz()
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

		for (final StauListener listener : listeners
				.getListeners(StauListener.class)) {
			for (final SystemObject stauObj : removedObjects) {
				final Stau stau = (Stau) ObjektFactory.getInstanz()
						.getModellobjekt(stauObj);
				stau.removeNetzReferenz(this);
				listener.stauEntfernt(this, stau);
			}
			for (final SystemObject stauObj : addedObjects) {
				if (stauObj.isValid()) {
					final Stau stau = (Stau) ObjektFactory.getInstanz()
							.getModellobjekt(stauObj);
					stau.addNetzReferenz(this);
					listener.stauAngelegt(this, stau);
				}
			}
		}
	}

	/**
	 * entfernt ein Baustellenobjekt mit dem übergeben Systemobjekt vom Netz.
	 * Das Objekt wird aus der Menge der Baustellen des VerkehrsmodellNetz
	 * ausgetragen.
	 *
	 * @param obj
	 *            das zu entfernende Stauobjekt
	 */
	public void baustelleEntfernen(final SystemObject obj) {
		final ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_BAUSTELLEN);
		if (set.getElements().contains(obj)) {
			try {
				set.remove(obj);
			} catch (final ConfigurationChangeException e) {
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
	public void baustelleHinzufuegen(final SystemObject obj) {
		final ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_BAUSTELLEN);
		if (!set.getElements().contains(obj)) {
			try {
				set.add(obj);
			} catch (final ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
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
		final List<AeusseresStrassenSegment> result = new ArrayList<>();
		for (final StrassenSegment segment : getNetzSegmentListe()) {
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
		final Collection<Baustelle> result = new ArrayList<>();
		for (final SystemObject obj : baustellenMenge.getElements()) {
			final Baustelle bst = (Baustelle) ObjektFactory.getInstanz()
					.getModellobjekt(obj);
			bst.addNetzReferenz(this);
			result.add(bst);

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
		final ArrayList<Stau> result = new ArrayList<>();
		for (final SystemObject stauObject : stauMenge.getElements()) {
			final Stau stau = (Stau) ObjektFactory.getInstanz()
					.getModellobjekt(stauObject);
			stau.addNetzReferenz(this);
			result.add(stau);
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
	 * entfernt einen Baustellenlistener vom Netz.
	 *
	 * @param listener
	 *            der zu entfernende Baustellenlistener
	 */
	public void removeStauListener(final StauListener listener) {
		if (listener != null) {
			listeners.remove(StauListener.class, listener);
			if (listeners.getListenerCount(StauListener.class) == 0) {
				stauMenge.removeChangeListener(this);
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
		final ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);
		if (set.getElements().contains(obj)) {
			try {
				set.remove(obj);
			} catch (final ConfigurationChangeException e) {
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
		final ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);
		if (!set.getElements().contains(obj)) {
			try {
				set.add(obj);
			} catch (final ConfigurationChangeException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * entfernt alle Staus aus dem Netz. Wird der Parameter
	 * <code>nurUngueltige</code> auf <code>true</code> gesetzt, werden nur
	 * Objekte mit dem Status "invalid" entfernt.
	 *
	 * @param nurUngueltige
	 *            nur ungültige Objekte entfernen ?
	 */
	public void stausBereinigen(final boolean nurUngueltige) {
		final ObjectSet set = ((ConfigurationObject) getSystemObject())
				.getObjectSet(MENGENNAME_STAUS);

		LOGGER.info("Bereinige Stauliste für Netz: " + getName()
		+ ", nur ungültige: " + nurUngueltige + ", Anzahl: "
		+ set.getElements().size());

		int removed = 0;
		for (final SystemObject stauObject : set.getElements()) {
			if ((!nurUngueltige) || (!stauObject.isValid())) {
				try {
					set.remove(stauObject);
					removed++;
				} catch (final ConfigurationChangeException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}

		LOGGER.info("Stauliste bereinigt für Netz: " + getName()
		+ ", Anzahl ist jetzt: " + set.getElements().size());
	}

	@Override
	public void update(final MutableSet set, final SystemObject[] addedObjects,
			final SystemObject[] removedObjects) {
		if (set.equals(baustellenMenge)) {
			aktualisiereBaustellen(addedObjects, removedObjects);
		} else if (set.equals(stauMenge)) {
			aktualisiereStaus(addedObjects, removedObjects);
		}
	}
}
