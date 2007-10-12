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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Implementierung der gemeinsamen Methoden der Systemobjektschnittstelle.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractSystemObjekt implements SystemObjekt {

	/** Das gekapselte Systemobjekt des Datenverteilers. */
	protected final SystemObject objekt;

	/**
	 * Menge der Parameterdatensa‰tze, deren Daten innerhalb des Objekts
	 * verwaltet werden.
	 */
	private final Map<Class<? extends ParameterDatensatz>, ParameterDatensatz> parameter = new HashMap<Class<? extends ParameterDatensatz>, ParameterDatensatz>();

	/**
	 * Menge der Onlinedatens‰tze, deren Daten innerhalb des Objekts verwaltet
	 * werden.
	 */
	private final Map<Class<? extends OnlineDatensatz>, OnlineDatensatz> onlineDaten = new HashMap<Class<? extends OnlineDatensatz>, OnlineDatensatz>();

	/**
	 * Weist lediglich das Systemobjekt zu.
	 * 
	 * @param obj
	 *            Das zu kapselnde Systemobjekt
	 */
	protected AbstractSystemObjekt(SystemObject obj) {
		objekt = obj;
	}

	/**
	 * &Uuml;bernimmt die Methode von {@link SystemObject}.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SystemObjekt) {
			SystemObjekt obj = (SystemObjekt) o;
			return getSystemObject().equals(obj.getSystemObject());
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public long getId() {
		return objekt.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return objekt.getNameOrPidOrId();
	}

	/**
	 * {@inheritDoc}
	 */
	public OnlineDatensatz getOnlineDatensatz(
			Class<? extends OnlineDatensatz> typ) {
		if (!onlineDaten.containsKey(typ)) {
			try {
				onlineDaten.put(typ, typ.getConstructor(SystemObjekt.class)
						.newInstance(this));
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			}
		}
		return onlineDaten.get(typ);
	}

	/**
	 * {@inheritDoc}
	 */
	public ParameterDatensatz getParameterDatensatz(
			Class<? extends ParameterDatensatz> typ) {
		if (!parameter.containsKey(typ)) {
			try {
				parameter.put(typ, typ.getConstructor(SystemObjekt.class)
						.newInstance(this));
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ "kann nicht instantiiert werden:" + e.getMessage());
			}
		}
		return parameter.get(typ);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPid() {
		return objekt.getPid();
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObject getSystemObject() {
		return objekt;
	}

	/**
	 * &UUml;bernimmt die Methode von {@link SystemObject}.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return objekt.toString();
	}
}
