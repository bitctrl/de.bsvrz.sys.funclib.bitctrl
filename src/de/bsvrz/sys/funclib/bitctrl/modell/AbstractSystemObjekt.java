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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bsvrz.dav.daf.main.config.AttributeGroup;
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
	private final Map<Class<? extends ParameterDatensatz<? extends Datum>>, ParameterDatensatz<? extends Datum>> parameter;

	/**
	 * Menge der Onlinedatensätze, deren Daten innerhalb des Objekts verwaltet
	 * werden.
	 */
	private final Map<Class<? extends OnlineDatensatz<? extends Datum>>, OnlineDatensatz<? extends Datum>> onlineDaten;

	/**
	 * Weist lediglich das Systemobjekt zu.
	 * 
	 * @param obj
	 *            Das zu kapselnde Systemobjekt
	 */
	protected AbstractSystemObjekt(final SystemObject obj) {
		objekt = obj;
		parameter = Collections.synchronizedMap(new HashMap<Class<? extends ParameterDatensatz<? extends Datum>>, ParameterDatensatz<? extends Datum>>());
		onlineDaten = Collections.synchronizedMap(new HashMap<Class<? extends OnlineDatensatz<? extends Datum>>, OnlineDatensatz<? extends Datum>>());
	}

	/**
	 * &Uuml;bernimmt die Methode von {@link SystemObject}.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object o) {
		if (o instanceof SystemObjekt) {
			final SystemObjekt obj = (SystemObjekt) o;
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
	public Collection<? extends OnlineDatensatz<? extends Datum>> getOnlineDatensatz() {
		return onlineDaten.values();
	}

	/**
	 * {@inheritDoc}
	 */
	public <O extends OnlineDatensatz<? extends Datum>> O getOnlineDatensatz(
			final Class<O> typ) {
		if (!onlineDaten.containsKey(typ)) {
			OnlineDatensatz<? extends Datum> od;
			final List<AttributeGroup> atgListe;

			od = getDatensatz(typ);
			if (od == null) {
				throw new IllegalArgumentException(
						"Datensatz "
								+ typ
								+ " kann nicht instantiiert werden, da der öffentlicher "
								+ "Konstruktor mit einem Parameter vom Typ SystemObjekt fehlt.");
			}

			atgListe = getSystemObject().getType().getAttributeGroups();
			if (atgListe.contains(od.getAttributGruppe())) {
				onlineDaten.put(typ, od);
			} else {
				throw new IllegalArgumentException("Datensatz "
						+ od.getAttributGruppe() + " kann nicht mit Objekt "
						+ getSystemObject().getType()
						+ " verwendet werden. Verfügbare Attributgruppen: "
						+ atgListe);
			}
		}
		return (O) onlineDaten.get(typ);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getParameterDatensatz()
	 */
	public Collection<? extends ParameterDatensatz<? extends Datum>> getParameterDatensatz() {
		return parameter.values();
	}

	/**
	 * {@inheritDoc}
	 */
	public <P extends ParameterDatensatz<? extends Datum>> P getParameterDatensatz(
			final Class<P> typ) {
		if (!parameter.containsKey(typ)) {
			ParameterDatensatz<? extends Datum> pd;
			final List<AttributeGroup> atgListe;

			pd = getDatensatz(typ);
			if (pd == null) {
				throw new IllegalArgumentException(
						"Datensatz "
								+ typ
								+ " kann nicht instantiiert werden, da der öffentlicher "
								+ "Konstruktor mit einem Parameter vom Typ SystemObjekt fehlt.");
			}

			atgListe = getSystemObject().getType().getAttributeGroups();
			if (atgListe.contains(pd.getAttributGruppe())) {
				parameter.put(typ, pd);
			} else {
				throw new IllegalArgumentException("Datensatz "
						+ pd.getAttributGruppe() + " kann nicht mit Objekt "
						+ getSystemObject().getType()
						+ " verwendet werden. Verfügbare Attributgruppen: "
						+ atgListe);
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
	public boolean hasOnlineDatensatz(
			final Class<? extends OnlineDatensatz<? extends Datum>> typ) {
		return getDatensatz(typ) != null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#hasParameterDatensatz(java.lang.Class)
	 */
	public boolean hasParameterDatensatz(
			final Class<? extends ParameterDatensatz<? extends Datum>> typ) {
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
	 * @param <D>
	 *            Der Typ des Datensatzes.
	 * @return ein Objekt der Klasse oder {@code null}, wenn der Datensatz am
	 *         Systemobjekt nicht unterst&uuml;tzt wird..
	 */
	private <D extends Datensatz<? extends Datum>> D getDatensatz(
			final Class<D> typ) {
		if (Modifier.isAbstract(typ.getModifiers())
				|| Modifier.isInterface(typ.getModifiers())) {
			throw new IllegalArgumentException("Datensatz " + typ.getName()
					+ " kann nicht instantiiert werden, da es sich um eine "
					+ "Schnittstelle oder abstrakte Klasse handelt.");
		}

		for (final Constructor<?> c : typ.getConstructors()) {
			final Class<?>[] parameterTypes = c.getParameterTypes();

			if (Modifier.isPublic(c.getModifiers())
					&& parameterTypes.length == 1
					&& parameterTypes[0].isAssignableFrom(getClass())) {
				try {
					return (D) c.newInstance(this);
				} catch (final IllegalArgumentException ex) {
					// Darf nicht mehr eintreten, weil geprüpft
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				} catch (final InstantiationException ex) {
					// Darf nicht mehr eintreten, weil geprüpft
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				} catch (final IllegalAccessException ex) {
					// Darf nicht mehr eintreten, weil geprüpft
					throw new IllegalArgumentException("Datensatz "
							+ typ.getName()
							+ " kann nicht instantiiert werden:"
							+ ex.getMessage());
				} catch (final InvocationTargetException ex) {
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
