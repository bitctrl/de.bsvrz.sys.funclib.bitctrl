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

package de.bsvrz.sys.funclib.bitctrl.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator, der &uuml;ber Feld l&auml;ft.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @param <T>
 * @deprecated Wurde nach {@link com.bitctrl.util.FeldIterator} ausgelagert.
 */
@Deprecated
public class FeldIterator<T> implements Iterator<T> {

	/** Das Feld. */
	private final T[] data;

	/** Die aktuelle Position im Feld. */
	private int cursor = 0;

	/**
	 * Initialisiert das Objekt, indem die Referenz gesichert wird.
	 * 
	 * @param data
	 *            das Feld.
	 */
	public FeldIterator(T... data) {
		this.data = data;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return data != null ? cursor < data.length : false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.util.Iterator#next()
	 */
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		return data[cursor++];
	}

	/**
	 * Wirft immer eine {@link UnsupportedOperationException}.
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (hasNext()) {
			return getClass().getName() + "[cursor=" + cursor + ", next="
					+ data[cursor] + "]";
		}
		return getClass().getName() + "[cursor=" + cursor + ", next=]";
	}

}
