/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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

package de.bsvrz.sys.funclib.bitctrl.interpreter;

import java.util.HashMap;
import java.util.Set;

import com.bitctrl.i18n.Messages;

/**
 * Kontext eines Ausdrucks. Speichert alle Variablen in einer Menge.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class Kontext {

	/** Pr&uuml;fklasse die Symbolnamen auf ihre G&uuml;ltigkeit testet. */
	public static Namenspruefer pruefer = new StandardNamenspruefer();

	/**
	 * Prüft ob der String ein gültiger Variablenname ist. Es wird nicht
	 * geprüft, ob die Variable existiert, nur ob der Name den Regeln
	 * entspricht.
	 *
	 * @param name
	 *            Zu prüfender Variablenname
	 * @throws InterpreterException
	 *             Wenn der Name {@code null} ist oder nur aus Leerzeichen
	 *             besteht
	 */
	public static void pruefeName(final String name) {
		if (!pruefer.pruefe(name)) {
			throw new InterpreterException(Messages.get(
					InterpreterMessages.BadVariableName, name));
		}
	}

	/** Die Menge der im Kontext enthaltenen Name/Wert-Paare. */
	private final HashMap<String, Object> kontext;

	/**
	 * Der Standardkonstruktor initialisiert die interne Streuspeicherabbildung.
	 */
	public Kontext() {
		kontext = new HashMap<>();
	}

	/**
	 * Kopierkonstruktor.
	 *
	 * @param kontext
	 *            Ein anderer Kontext, dessen Inhalt in den neuen kopiert wird
	 */
	public Kontext(final Kontext kontext) {
		this();
		this.kontext.putAll(kontext.kontext);
	}

	/**
	 * F&uuml;gt den Inhalt eines Kontextes hinzu.
	 *
	 * @param neu
	 *            Zu kopierender Kontext
	 */
	public void addKontext(final Kontext neu) {
		kontext.putAll(neu.kontext);
	}

	/**
	 * Schaut nach, ob im Kontext eine bestimmte Variable existiert.
	 *
	 * @param name
	 *            Name der Variable
	 * @return {@code true}, wenn die Variable existiert, sonst {@code false}
	 */
	public boolean enthaelt(final String name) {
		return kontext.containsKey(name.trim());
	}

	/**
	 * Liefert den Wert einer Variable.
	 *
	 * @param name
	 *            Name der Variablen
	 * @return Wert der Variable
	 * @throws InterpreterException
	 *             Wenn der Name {@code null} ist oder nur aus Whitespaces
	 *             besteht
	 * @throws SymbolUndefiniertException
	 *             Wenn zu dem Namen keine Variable im Kontext existiert
	 */
	public Object get(final String name) {
		pruefeName(name);

		final Object wert = kontext.get(name.trim());
		if (wert == null) {
			throw new SymbolUndefiniertException(Messages.get(
					InterpreterMessages.NoVariableWithName, name));
		}

		return wert;
	}

	/**
	 * Liefert den Wert einer Variable und pr&uuml;ft gleichzeitig deren Typ.
	 *
	 * @param name
	 *            Name der Variablen
	 * @param typ
	 *            Typ bzw. Klasse, den die Variable haben soll
	 * @return Wert der Variable
	 * @throws InterpreterException
	 *             Wenn der Variablenname oder der Typ {@code null} ist, der
	 *             Variablenname nur aus Whitespaces betsteht oder der Typ der
	 *             Variable nicht korrekt ist
	 */
	public Object get(final String name, final Class<?>... typ) {
		if (typ == null) {
			throw new InterpreterException(
					Messages.get(InterpreterMessages.BadTypNull));
		}

		final Object wert = get(name);
		boolean ok = false;

		for (final Class<? extends Object> c : typ) {
			if (c.isInstance(wert)) {
				ok = true;
			}
		}
		if (!ok) {
			throw new SymbolUndefiniertException(Messages.get(
					InterpreterMessages.NoVariableWithNameAndTyp, name, typ));
		}

		return wert;
	}

	/**
	 * Gibt alle im Kontext enthaltenen Variablenname zur&uuml;ck.
	 *
	 * @return kontext Menge von Name/Wert-Paaren
	 */
	public Set<String> getVariablen() {
		return kontext.keySet();
	}

	/**
	 * Setzt den Wert einer Variable.
	 *
	 * @param name
	 *            Name der Variable
	 * @param wert
	 *            Wert der Variable
	 * @throws InterpreterException
	 *             Wenn der Variablenname oder der Wert {@code null} ist oder
	 *             der Variablenname nur aus Whitespaces besteht
	 */
	public synchronized void set(final String name, final Object wert) {
		pruefeName(name);

		if (wert == null) {
			throw new InterpreterException(
					Messages.get(InterpreterMessages.BadValueNull));
		}

		kontext.put(name.trim(), wert);
	}

	@Override
	public String toString() {
		return kontext.toString();
	}

}
