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

package de.bsvrz.sys.funclib.bitctrl.modell;

import javax.swing.event.EventListenerList;

import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;

/**
 * Implementiert gemeinsame Funktionen der Datens&auml;tze.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public abstract class AbstractDatensatz implements Datensatz,
		ClientReceiverInterface {

	/** Das Systemobjekt. */
	private final SystemObjekt objekt;

	/** Liste der registrierten Listener. */
	private final EventListenerList listeners = new EventListenerList();

	/** Das Flag f&uuml;r autoamtische Aktualisierng des Datensatzes. */
	private boolean autoUpdate;

	/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
	private boolean valid;

	/** Der Sendecache. */
	private Data sendeCache;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            das Systemobjekt, dem der Datensatz zugeordnet ist.
	 */
	public AbstractDatensatz(SystemObjekt objekt) {
		super();
		this.objekt = objekt;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addUpdateListener(DatensatzUpdateListener listener) {
		listeners.add(DatensatzUpdateListener.class, listener);
	}

	/**
	 * Leert den Sendecache.
	 */
	protected void clearSendeCache() {
		sendeCache = null;
	}

	/**
	 * Meldet den Datensatz abh&auml;nig vom Flag {@code autoUpdate} als
	 * Empf&auml;nger an oder ab.
	 * 
	 * @see #isAutoUpdate()
	 */
	protected abstract void fireAutoUpdate();

	/**
	 * Benachricht registrierte Listener &uuml;ber &Auml;nderungen am Datensatz.
	 */
	protected void fireDatensatzAktualisiert() {
		DatensatzUpdateEvent event = new DatensatzUpdateEvent(getObjekt(), this);
		for (DatensatzUpdateListener listener : listeners
				.getListeners(DatensatzUpdateListener.class)) {
			listener.datensatzAktualisiert(event);
		}
	}

	/**
	 * ruft die aktuellen Daten ab und überträgt diee in die internen
	 * Datenspeicher.
	 */
	protected abstract void fireUpdate();

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getObjekt()
	 */
	public final SystemObjekt getObjekt() {
		return objekt;
	}

	/**
	 * Gibt den Sendecache zur&uuml;ck. Ist der Cache leer (z.&nbsp;B. nach dem
	 * Senden), wird ein neues Datum angelegt. Datensatz&auml;nderungen werden
	 * am Cache durchgef&uuml;hrt und anschlie&szlig;end mit
	 * {@link #sendeDaten()} gesammelt gesendet.
	 * 
	 * @return der Sendecache.
	 * @see #sendeDaten()
	 */
	protected Data getSendeCache() {
		if (sendeCache == null) {
			sendeCache = ObjektFactory.getInstanz().getVerbindung().createData(
					getAttributGruppe());
		}
		return sendeCache;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeUpdateListener(DatensatzUpdateListener listener) {
		listeners.remove(DatensatzUpdateListener.class, listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAutoUpdate(boolean ein) {
		if (autoUpdate != ein) {
			autoUpdate = ein;
			fireAutoUpdate();
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#update()
	 */
	public void update() {
		fireUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(ResultData[] results) {
		for (ResultData result : results) {
			if (result.hasData()) {
				setDaten(result.getData());
				valid = true;
			} else {
				valid = false;
			}
		}
	}

}
