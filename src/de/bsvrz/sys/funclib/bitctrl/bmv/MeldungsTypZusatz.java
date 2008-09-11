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

package de.bsvrz.sys.funclib.bitctrl.bmv;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * Hilfsklasse zum Generieren und parsen von Meldungstypzusätzen.
 * 
 * @author BitCtrl Systems GmbH, Falko
 * @version $Id$
 */
public class MeldungsTypZusatz {

	public static final String SYM_PARAMETER = "::";

	public static final String SYM_ASSIGN = ":=";

	public static final String SYM_DELIMITER = ";;";

	private final Map<String, String> parameter = new HashMap<String, String>();
	private final String id;

	public static MeldungsTypZusatz parse(final String meldungsTypZusatz)
			throws ParseException {
		final MeldungsTypZusatz mtz;
		String[] rest = meldungsTypZusatz.split(SYM_PARAMETER);

		if (rest.length == 1) {
			mtz = new MeldungsTypZusatz(rest[0]);
		} else if (rest.length == 2) {
			mtz = new MeldungsTypZusatz(rest[0]);

			rest = rest[1].split(SYM_DELIMITER);
			for (final String param : rest) {
				final String[] s = param.split(SYM_ASSIGN);
				if (s.length == 1) {
					mtz.set(s[0], "");
				} else if (s.length == 2) {
					mtz.set(s[0], s[1]);
				} else {
					throw new ParseException("Das Trennzeichen \"" + SYM_ASSIGN
							+ "\" kommt mehr als einmal im Parameter \"" + s[0]
							+ "\" vor.", 0);
				}
			}
		} else {
			throw new ParseException("Das Trennzeichen \"" + SYM_PARAMETER
					+ "\" kommt mehr als einmal vor.", 0);
		}

		return mtz;
	}

	public MeldungsTypZusatz(final String id) {
		this.id = id;
	}

	public String compile() {
		String s = id;

		if (!parameter.isEmpty()) {
			s += SYM_PARAMETER;
			final Iterator<Entry<String, String>> iterator = parameter
					.entrySet().iterator();
			while (iterator.hasNext()) {
				final Entry<String, String> e = iterator.next();
				s += e.getKey() + SYM_ASSIGN + e.getValue();
				if (iterator.hasNext()) {
					s += SYM_DELIMITER;
				}
			}
		}

		return s;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gibt eine unveränderliche Kopie der Paramter zurück.
	 * 
	 * @return die Kopie der Parameter.
	 */
	public Map<String, String> getParameter() {
		return Collections.unmodifiableMap(parameter);
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean containsKey(final String name) {
		return parameter.containsKey(name);
	}

	/**
	 * @param name
	 * @return
	 */
	public String getString(final String name) {
		return parameter.get(name);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void set(final String name, final String value) {
		parameter.put(name, value);
	}

	/**
	 * Gibt {@code null} zurück, wenn unter dem Namen kein Parameter angelegt
	 * ist oder dieser ein Leerstring ist.
	 * 
	 * @param name
	 * @return
	 */
	public SystemObjekt getObjekt(final String name) {
		if (parameter.get(name) != null && !parameter.get(name).equals("")) {
			return ObjektFactory.getInstanz().getModellobjekt(
					parameter.get(name));
		}

		return null;
	}

	/**
	 * Legt einen Leerstring ab, wenn {@code value == null}.
	 * 
	 * @param name
	 * @param value
	 */
	public void set(final String name, final SystemObjekt value) {
		if (value != null) {
			parameter.put(name, value.getPid());
		} else {
			parameter.put(name, "");
		}
	}

	/**
	 * @param name
	 * @return
	 */
	public int getInteger(final String name) {
		return Integer.parseInt(parameter.get(name));
	}

	/**
	 * @param name
	 * @param value
	 */
	public void set(final String name, final int value) {
		parameter.put(name, String.valueOf(value));
	}

	/**
	 * @param name
	 * @return
	 */
	public long getLong(final String name) {
		return Long.parseLong(parameter.get(name));
	}

	/**
	 * @param name
	 * @param value
	 */
	public void set(final String name, final long value) {
		parameter.put(name, String.valueOf(value));
	}

	/**
	 * @param name
	 * @return
	 */
	public double getDouble(final String name) {
		return Double.parseDouble(parameter.get(name));
	}

	/**
	 * @param name
	 * @param value
	 */
	public void set(final String name, final double value) {
		parameter.put(name, String.valueOf(value));
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean getBoolean(final String name) {
		return Boolean.parseBoolean(parameter.get(name));
	}

	/**
	 * @param name
	 * @param value
	 */
	public void set(final String name, final boolean value) {
		parameter.put(name, String.valueOf(value));
	}

	/**
	 * Der Meldungstypzusatz ist gleich, wenn Id und Parameter übereinstimmen.
	 * Die Reihenfolge der Parameter spielt keine Rolle.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		} else if (obj instanceof MeldungsTypZusatz) {
			final MeldungsTypZusatz o = (MeldungsTypZusatz) obj;

			return id.equals(o.id) && parameter.equals(o.parameter);
		}
		return false;
	}

	/**
	 * Gibt den Hash-Wert der Id zurück.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see #getId()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s;

		s = getClass().getName() + "[";
		s += "id=" + id;
		s += ", paramater=" + parameter;
		s += "]";

		return s;
	}

}
