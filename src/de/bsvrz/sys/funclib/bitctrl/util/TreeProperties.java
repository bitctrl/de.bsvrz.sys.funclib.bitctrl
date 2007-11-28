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

package de.bsvrz.sys.funclib.bitctrl.util;

import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

/**
 * Erweitert die Properties um die Fähigkeit mit Gruppen und Feldern umzugehen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class TreeProperties extends Properties {

	/**
	 * Repräseniert eine Gruppe oder ein Feld.
	 */
	private static class Group {

		/**
		 * Der Name der Gruppe oder des Felds.
		 */
		private final String name;

		/**
		 * Der aktuelle Index des Feldes oder {@code -1}, wenn es sich um eine
		 * Gruppe handelt.
		 */
		private int index;

		/**
		 * Die aktuelle Feldgröße oder {@code -1}, wenn die Feldgröße nicht
		 * berechnet werden soll oder es sich um eine Gruppe handelt.
		 */
		private int arraySize;

		/**
		 * Erzeugt eine Gruppe.
		 * 
		 * @param name
		 *            der Name der Gruppe
		 */
		public Group(String name) {
			this.name = name;
			this.index = -1;
			this.arraySize = -1;
		}

		/**
		 * Erzeugt ein Feld.
		 * 
		 * @param name
		 *            der Name des Feldes
		 * @param guessArraySize
		 *            {@code true}, wenn die Feldgröße mit gespeichert wird.
		 */
		public Group(String name, boolean guessArraySize) {
			this.name = name;
			this.index = 0;
			this.arraySize = guessArraySize ? 0 : -1;
		}

		/**
		 * Gibt die aktuelle Feldgröße zurück.
		 * 
		 * @return die aktuelle Größe des Feldes oder {@code -1}, wenn die
		 *         Feldgröße unbekannt ist oder es sich um eine Gruppe handelt.
		 */
		public int arraySizeGuess() {
			return arraySize;
		}

		/**
		 * Fragt, ob dies ein Feld ist.
		 * 
		 * @return {@code true}, wenn es ein Feld ist und {@code false}, wenn
		 *         es ein einfache Gruppe ist.
		 */
		public boolean isArray() {
			return index != -1;
		}

		/**
		 * Legt den aktuellen Feldindex fest.
		 * 
		 * @param index
		 *            der Index.
		 */
		public void setIndex(int index) {
			this.index = index + 1;
			if (arraySize != -1 && index > arraySize) {
				arraySize = index;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String s = name;

			if (index > 0) {
				s += "." + index + ".";
			}

			return s;
		}

	}

	/** Die Eigenschaft {@code serialVersionUID}. */
	private static final long serialVersionUID = 1L;

	/** Der Stack enthält die geöffneten Gruppen . */
	private final Queue<Group> stack = new LinkedList<Group>();

	/** Der Stack als Schlüsselstring. */
	private String trace = "";

	/**
	 * Öffnet eine neue Gruppe.
	 * 
	 * @param name
	 *            der Name der Gruppe.
	 */
	public void beginGroup(String name) {
		beginGroupOrArray(new Group(name));
	}

	/**
	 * Öffnet ein neues Feld zum Schreiben.
	 * 
	 * @param name
	 *            der Name des Felds.
	 * @param size
	 *            die Größe die das Feld haben soll.
	 */
	public void beginWriteArray(String name, int size) {
		beginGroupOrArray(new Group(name, size < 0));

		if (size < 0) {
			remove("size");
		} else {
			setProperty("size", String.valueOf(size));
		}
	}

	/**
	 * Schließt ein offenes Feld.
	 * 
	 * @param name
	 *            der Name des Felds.
	 */
	public void endArray(String name) {
		endGroupOrArray(name);
	}

	/**
	 * Schließt eine offene Gruppe.
	 * 
	 * @param name
	 *            der Name der Gruppe.
	 */
	public void endGroup(String name) {
		endGroupOrArray(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		return super.getProperty(actualKey(key));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Properties#getProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public String getProperty(String key, String defaultValue) {
		return super.getProperty(actualKey(key), defaultValue);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	@Override
	public synchronized Object remove(Object key) {
		return super.remove(actualKey(key.toString()));
	}

	/**
	 * Legt den Index des aktuellen Feldes fest.
	 * 
	 * @param index
	 *            der neue Index.
	 */
	public void setArrayIndex(int index) {
		if (stack.isEmpty() || !stack.peek().isArray()) {
			throw new IllegalStateException("no array");
		}

		Group group = stack.peek();
		int length = group.toString().length();

		group.setIndex(Math.max(index, 0));
		if (stack.size() == 1) {
			trace = group.toString();
		} else {
			trace = trace.substring(0, trace.length() - length - 1)
					+ group.toString();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public Object setProperty(String key, String value) {
		return super.setProperty(actualKey(key), value);
	}

	/**
	 * Gibt den absoluten Schlüssel zu einem relativen zurück.
	 * 
	 * @param key
	 *            der relative Schlüssel.
	 * @return der absolute Schlüssel.
	 */
	private String actualKey(String key) {
		return trace + normalizedKey(key);
	}

	/**
	 * Öffnet eine neue Gruppe oder ein neues Feld.
	 * 
	 * @param group
	 *            die Gruppe oder das Feld.
	 */
	private void beginGroupOrArray(Group group) {
		stack.offer(group);
		trace += group.name + '.';
	}

	/**
	 * Schließt eine Gruppe oder ein Feld.
	 * 
	 * @param name
	 *            der Name der Gruppe oder des Felds.
	 */
	private void endGroupOrArray(String name) {
		if (!stack.peek().name.equals(name)) {
			throw new IllegalArgumentException("no group or array \"" + name
					+ "\"");
		}

		int length = stack.peek().toString().length();
		if (stack.size() == 1) {
			trace = "";
		} else {
			trace = trace.substring(0, trace.length() - length - 1);
		}
		stack.poll();
	}

	/**
	 * Ersetzt alle mehrfachen Punkte durch einzelne und entfernt Leerzeichen
	 * und Punkte am Anfang und Ende.
	 * 
	 * @param key
	 *            ein zu normalisierender Schlüssel.
	 * @return der normalisierte Schlüssel.
	 */
	private String normalizedKey(String key) {
		String normalized = key;

		while (normalized.contains("..")) {
			normalized.replaceAll("..", ".");
		}

		if (normalized.startsWith(".")) {
			normalized = normalized.substring(1);
		}

		if (normalized.endsWith(".")) {
			normalized = normalized.substring(normalized.length() - 1);
		}

		return normalized.trim();
	}

}
