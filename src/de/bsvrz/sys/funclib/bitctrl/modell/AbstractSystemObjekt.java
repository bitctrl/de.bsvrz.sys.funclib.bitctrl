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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
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
	 * Menge der Parameterdatensaätze, deren Daten innerhalb des Objekts
	 * verwaltet werden.
	 */
	private final Map<Class<? extends ParameterDatensatz<?>>, ParameterDatensatz<?>> parameter = new HashMap<Class<? extends ParameterDatensatz<?>>, ParameterDatensatz<?>>();

	/**
	 * Menge der Onlinedatensätze, deren Daten innerhalb des Objekts verwaltet
	 * werden.
	 */
	private final Map<Class<? extends OnlineDatensatz<?>>, OnlineDatensatz<?>> onlineDaten = new HashMap<Class<? extends OnlineDatensatz<?>>, OnlineDatensatz<?>>();

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
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getOnlineDatensatz()
	 */
	public Collection<? extends OnlineDatensatz<?>> getOnlineDatensatz() {
		return onlineDaten.values();
	}

	/**
	 * {@inheritDoc}
	 */
	public <O extends OnlineDatensatz<?>> O getOnlineDatensatz(Class<O> typ) {
		if (!onlineDaten.containsKey(typ)) {
			OnlineDatensatz<?> od;

			od = (OnlineDatensatz<?>) getDatensatz(typ);
			if (od == null) {
				throw new IllegalArgumentException(
						"Datensatz "
								+ typ
								+ " kann nicht instantiiert werden, da der öffentlicher "
								+ "Konstruktor mit einem Parameter vom Typ SystemObjekt fehlt.");
			}
			if (getSystemObject().getType().getAttributeGroups().contains(
					od.getAttributGruppe())) {
				onlineDaten.put(typ, od);
			} else {
				throw new IllegalArgumentException("Datensatz " + typ
						+ " kann nicht mit Objekt "
						+ getSystemObject().getType().getPid()
						+ " verwendet werden.");
			}
		}
		return (O) onlineDaten.get(typ);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getParameterDatensatz()
	 */
	public Collection<? extends ParameterDatensatz<?>> getParameterDatensatz() {
		return parameter.values();
	}

	/**
	 * {@inheritDoc}
	 */
	public <P extends ParameterDatensatz<?>> P getParameterDatensatz(
			Class<P> typ) {
		if (!parameter.containsKey(typ)) {
			ParameterDatensatz<?> pd;

			pd = (ParameterDatensatz<?>) getDatensatz(typ);
			if (pd == null) {
				throw new IllegalArgumentException(
						"Datensatz "
								+ typ
								+ " kann nicht instantiiert werden, da der öffentlicher "
								+ "Konstruktor mit einem Parameter vom Typ SystemObjekt fehlt.");
			}
			if (getSystemObject().getType().getAttributeGroups().contains(
					pd.getAttributGruppe())) {
				parameter.put(typ, pd);
			} else {
				throw new IllegalArgumentException("Datensatz " + typ.getName()
						+ " kann nicht mit Objekt "
						+ getSystemObject().getType().getPid()
						+ " verwendet werden.");
			}
		}
		return (P) parameter.get(typ);
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
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#hasOnlineDatensatz(java.lang.Class)
	 */
	public boolean hasOnlineDatensatz(Class<? extends OnlineDatensatz<?>> typ) {
		return getDatensatz(typ) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#hasParameterDatensatz(java.lang.Class)
	 */
	public boolean hasParameterDatensatz(
			Class<? extends ParameterDatensatz<?>> typ) {
		return getDatensatz(typ) != null;
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

	/**
	 * Generiert aus der Datensatzklasse ein Objekt. Dazu muss ein
	 * &ouml;ffentlicher Konstruktor existieren, der als einzigen Parameter ein
	 * SystemObjekt entgegennimmt.
	 * 
	 * @param typ
	 *            die Klasse eines Datensatzes.
	 * @return ein Objekt der Klasse oder {@code null}, wenn der Datensatz am
	 *         Systemobjekt nicht unterst&uuml;tzt wird..
	 */
	private Datensatz<?> getDatensatz(Class<? extends Datensatz<?>> typ) {
		if (Modifier.isAbstract(typ.getModifiers())
				|| Modifier.isInterface(typ.getModifiers())) {
			throw new IllegalArgumentException("Datensatz " + typ.getName()
					+ " kann nicht instantiiert werden, da es sich um eine "
					+ "Schnittstelle oder abstrakte Klasse handelt.");
		}

		for (Constructor<? extends Datensatz<?>> c : typ.getConstructors()) {
			Class<?>[] parameterTypes = c.getParameterTypes();

			if (Modifier.isPublic(c.getModifiers())
					&& parameterTypes.length == 1
					&& parameterTypes[0].isAssignableFrom(getClass())) {
				try {
					return c.newInstance(this);
				} catch (IllegalArgumentException ex) {
					// Darf nicht mehr eintreten, weil geprüpft
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				} catch (InstantiationException ex) {
					// Darf nicht mehr eintreten, weil geprüpft
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				} catch (IllegalAccessException ex) {
					// Darf nicht mehr eintreten, weil geprüpft
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				} catch (InvocationTargetException ex) {
					// Tritt ein, wenn der aufgerufene Konstruktor eine
					// Exception geworfen hat
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				}
			}
		}

		return null;
	}

}
