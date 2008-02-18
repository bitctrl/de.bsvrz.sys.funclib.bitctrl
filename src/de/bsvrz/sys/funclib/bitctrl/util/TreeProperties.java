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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Properties;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

/**
 * Erweitert die Properties um die Fähigkeit mit Gruppen und Feldern umzugehen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @deprecated Wurde nach {@link com.bitctrl.util.Interval} ausgelagert.
 */
@Deprecated
public class TreeProperties extends Properties {

	// TODO typed Properties (int, boolean, double, ...)

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
			String s = name + ".";

			if (index > 0) {
				s += index + ".";
			}

			return s;
		}

	}

	/** Der Standardwert für Werte vom Typ {@code boolean}. */
	public static final boolean DEFAULT_BOOLEAN = false;

	/** Der Standardwert für Werte vom Typ {@code int}. */
	public static final int DEFAULT_INT = 0;

	/** Der Standardwert für Werte vom Typ {@code double}. */
	public static final double DEFAULT_DOUBLE = 0.0;

	/** Die Eigenschaft {@code serialVersionUID}. */
	private static final long serialVersionUID = 1L;

	/**
	 * A table of hex digits. Kopiert aus {@link java.util.Properties}, weil
	 * dort {@code private}.
	 */
	private static final char[] HEXDIGIT = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Convert a nibble to a hex character. Kopiert aus
	 * {@link java.util.Properties}, weil dort {@code private}.
	 * 
	 * @param nibble
	 *            the nibble to convert.
	 * @return the hex character.
	 */
	private static char toHex(int nibble) {
		return HEXDIGIT[(nibble & 0xF)];
	}

	/**
	 * Kopiert aus {@link java.util.Properties}, weil dort {@code private}.
	 * 
	 * @param bw
	 *            ein Writer
	 * @param s
	 *            ein String
	 * @throws IOException
	 *             bei I/O-Fehlern.
	 */
	private static void writeln(BufferedWriter bw, String s) throws IOException {
		bw.write(s);
		bw.newLine();
	}

	/** Der Stack enthält die geöffneten Gruppen . */
	private final Stack<Group> stack = new Stack<Group>();

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
	 * Öffnet ein neues Feld zum Lesen.
	 * 
	 * @param name
	 *            der Name des Felds.
	 * @return die Länge des Felds.
	 */
	public int beginReadArray(String name) {
		beginGroupOrArray(new Group(name, false));
		if (getProperty("size") != null) {
			return Integer.valueOf(getProperty("size"));
		}
		return 0;
	}

	/**
	 * Öffnet ein neues Feld zum Schreiben.
	 * 
	 * @param name
	 *            der Name des Felds.
	 */
	public void beginWriteArray(String name) {
		beginWriteArray(name, -1);
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
		Group group = stack.pop();

		if (group == null || !group.name.equals(name)) {
			throw new IllegalArgumentException("no array \"" + name + "\"");
		}

		if (group.arraySizeGuess() != -1) {
			setProperty(name + ".size", String.valueOf(group.arraySizeGuess()));
		}

		int length = group.toString().length();
		if (stack.size() == 0) {
			trace = "";
		} else {
			trace = trace.substring(0, trace.length() - length);
		}
	}

	/**
	 * Schließt eine offene Gruppe.
	 * 
	 * @param name
	 *            der Name der Gruppe.
	 */
	public void endGroup(String name) {
		Group group = stack.pop();

		if (group == null || !group.name.equals(name)) {
			throw new IllegalArgumentException("no group \"" + name + "\"");
		}

		int length = group.toString().length();
		if (stack.size() == 0) {
			trace = "";
		} else {
			trace = trace.substring(0, trace.length() - length - 1);
		}
	}

	/**
	 * Entspricht {@code getBoolean(key, DEFAULT_BOOLEAN)}.
	 * 
	 * @param key
	 *            ein Schl&uuml;ssel;
	 * @return der hinterlegt Wert oder, falls nicht vorhanden
	 *         {@link #DEFAULT_BOOLEAN}.
	 * @see #getBoolean(String, boolean)
	 */
	public boolean getBoolean(String key) {
		return getBoolean(key, DEFAULT_BOOLEAN);
	}

	/**
	 * Gibt den Wert der unter dem Schl&uuml;ssel hinterlegt ist als booleschen
	 * Wert zur&uuml;ck.
	 * 
	 * @param key
	 *            der Schl&uuml;ssel.
	 * @param defaultValue
	 *            ein Wert der zur&uuml;ckgegeben wird, wenn unter dem
	 *            Schl&uuml;ssel kein Wert hinterlegt ist.
	 * @return der hinterlegt Wert oder, falls nicht vorhanden, der angegebene
	 *         Standardwert.
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		return Boolean.valueOf(getProperty(key, String.valueOf(defaultValue)));
	}

	/**
	 * Entspricht {@code getDouble(key, DEFAULT_DOUBLE)}.
	 * 
	 * @param key
	 *            ein Schl&uuml;ssel;
	 * @return der hinterlegt Wert oder, falls nicht vorhanden
	 *         {@link #DEFAULT_DOUBLE}.
	 * @see #getDouble(String, double)
	 */
	public double getDouble(String key) {
		return getDouble(key, DEFAULT_DOUBLE);
	}

	/**
	 * Gibt den Wert der unter dem Schl&uuml;ssel hinterlegt ist als
	 * Gleitkommazahl zur&uuml;ck.
	 * 
	 * @param key
	 *            der Schl&uuml;ssel.
	 * @param defaultValue
	 *            ein Wert der zur&uuml;ckgegeben wird, wenn unter dem
	 *            Schl&uuml;ssel kein Wert hinterlegt ist.
	 * @return der hinterlegt Wert oder, falls nicht vorhanden, der angegebene
	 *         Standardwert.
	 */
	public double getDouble(String key, double defaultValue) {
		return Double.valueOf(getProperty(key, String.valueOf(defaultValue)));
	}

	/**
	 * Entspricht {@code getInt(key, DEFAULT_INT)}.
	 * 
	 * @param key
	 *            ein Schl&uuml;ssel;
	 * @return der hinterlegt Wert oder, falls nicht vorhanden
	 *         {@link #DEFAULT_INT}.
	 * @see #getInt(String, int)
	 */
	public int getInt(String key) {
		return getInt(key, DEFAULT_INT);
	}

	/**
	 * Gibt den Wert der unter dem Schl&uuml;ssel hinterlegt ist als Ganzzahl
	 * zur&uuml;ck.
	 * 
	 * @param key
	 *            der Schl&uuml;ssel.
	 * @param defaultValue
	 *            ein Wert der zur&uuml;ckgegeben wird, wenn unter dem
	 *            Schl&uuml;ssel kein Wert hinterlegt ist.
	 * @return der hinterlegt Wert oder, falls nicht vorhanden, der angegebene
	 *         Standardwert.
	 */
	public int getInt(String key, int defaultValue) {
		return Integer.valueOf(getProperty(key, String.valueOf(defaultValue)));
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
		String val = getProperty(key);
		return (val == null) ? defaultValue : val;
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
		Group group = stack.peek();
		int length;

		if (stack.isEmpty() || !group.isArray()) {
			throw new IllegalStateException("no array");
		}

		length = group.toString().length();

		group.setIndex(Math.max(index, 0));
		trace = trace.substring(0, trace.length() - length) + group.toString();
	}

	/**
	 * Legt einen booleschen Wert unter dem Schl&uuml;ssel ab.
	 * 
	 * @param key
	 *            der Schl&uuml;ssel.
	 * @param value
	 *            der Wert.
	 */
	public void setProperty(String key, boolean value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Legt einen Gleitkommawert unter dem Schl&uuml;ssel ab.
	 * 
	 * @param key
	 *            der Schl&uuml;ssel.
	 * @param value
	 *            der Wert.
	 */
	public void setProperty(String key, double value) {
		setProperty(key, String.valueOf(value));
	}

	/**
	 * Legt einen Ganzzahlwert unter dem Schl&uuml;ssel ab.
	 * 
	 * @param key
	 *            der Schl&uuml;ssel.
	 * @param value
	 *            der Wert.
	 */
	public void setProperty(String key, int value) {
		setProperty(key, String.valueOf(value));
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
	 * Schreibt die Properties nach Schl&uuml;ssel sortiert in den
	 * {@code OutputStream}. Kopiert aus {@link java.util.Properties}, weil
	 * dort {@code private}.
	 * 
	 * {@inheritDoc}
	 * 
	 * @see java.util.Properties#store(java.io.OutputStream, java.lang.String)
	 */
	@Override
	public synchronized void store(OutputStream out, String comments)
			throws IOException {
		BufferedWriter awriter;
		awriter = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));
		if (comments != null) {
			writeln(awriter, "#" + comments);
		}
		writeln(awriter, "#" + new Date().toString());
		// for (Enumeration e = keys(); e.hasMoreElements();) {
		// String key = (String) e.nextElement();
		// String val = (String) get(key);
		// key = saveConvert(key, true);
		//
		// /*
		// * No need to escape embedded and trailing spaces for value, hence
		// * pass false to flag.
		// */
		// val = saveConvert(val, false);
		// writeln(awriter, key + "=" + val);
		// }

		SortedSet<String> keys = new TreeSet<String>();
		for (Object o : keySet()) {
			keys.add(o.toString());
		}

		for (String key : keys) {
			String val = (String) get(key);
			key = saveConvert(key, true);

			/*
			 * No need to escape embedded and trailing spaces for value, hence
			 * pass false to flag.
			 */
			val = saveConvert(val, false);
			writeln(awriter, key + "=" + val);
		}

		awriter.flush();
	}

	/**
	 * Gibt den absoluten Schlüssel zu einem relativen zurück.
	 * 
	 * @param key
	 *            der relative Schlüssel.
	 * @return der absolute Schlüssel.
	 */
	private String actualKey(String key) {
		return trace + normalize(key);
	}

	/**
	 * Öffnet eine neue Gruppe oder ein neues Feld.
	 * 
	 * @param group
	 *            die Gruppe oder das Feld.
	 */
	private void beginGroupOrArray(Group group) {
		stack.push(group);
		trace += group.name + '.';
	}

	/**
	 * Ersetzt alle mehrfachen Punkte durch einzelne und entfernt Leerzeichen
	 * und Punkte am Anfang und Ende.
	 * 
	 * @param key
	 *            ein zu normalisierender Schlüssel.
	 * @return der normalisierte Schlüssel.
	 */
	private String normalize(String key) {
		String normalized = key;

		// Alle Leerzeichen entfernen
		normalized = normalized.replaceAll("\\s", "");

		// Punkte in der Mitte zusammenfassen
		while (normalized.contains("..")) {
			normalized = normalized.replaceAll("\\.\\.", "\\.");
		}

		// Punkt am Anfang entfernen
		if (normalized.startsWith(".")) {
			normalized = normalized.substring(1);
		}

		// Punkt am Ende entfernen
		if (normalized.endsWith(".")) {
			normalized = normalized.substring(0, normalized.length() - 1);
		}

		return normalized;
	}

	/**
	 * Converts unicodes to encoded &#92;uxxxx and escapes special characters
	 * with a preceding slash.
	 * 
	 * @param theString
	 *            ein String.
	 * @param escapeSpace
	 *            ob Leerzeichen maskiert werden sollen.
	 * @return der konvertierte String.
	 */
	private String saveConvert(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			// Handle common case first, selecting largest block that
			// avoids the specials below
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace) {
					outBuffer.append('\\');
				}
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if ((aChar < 0x0020) || (aChar > 0x007e)) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

}
