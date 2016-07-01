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

package de.bsvrz.sys.funclib.bitctrl.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class ObservableList<E> implements List<E> {

	private final EventListenerList listener;
	private final List<E> liste;

	public ObservableList() {
		listener = new EventListenerList();
		liste = Collections.synchronizedList(new LinkedList<E>());
	}

	public void addChangeListener(final ChangeListener l) {
		listener.add(ChangeListener.class, l);
	}

	public void removeChangeListener(final ChangeListener l) {
		listener.remove(ChangeListener.class, l);
	}

	protected void fireChangeEvent() {
		final ChangeEvent e;

		e = new ChangeEvent(this);
		for (final ChangeListener l : listener.getListeners(ChangeListener.class)) {
			l.stateChanged(e);
		}
	}

	@Override
	public boolean add(final E o) {
		final boolean changed;

		changed = liste.add(o);
		fireChangeEvent();
		return changed;
	}

	@Override
	public void add(final int index, final E element) {
		liste.add(index, element);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return liste.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		return liste.addAll(index, c);
	}

	@Override
	public void clear() {
		liste.clear();
	}

	@Override
	public boolean contains(final Object o) {
		return liste.contains(o);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return liste.containsAll(c);
	}

	@Override
	public E get(final int index) {
		return liste.get(index);
	}

	@Override
	public int indexOf(final Object o) {
		return liste.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return liste.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return liste.iterator();
	}

	@Override
	public int lastIndexOf(final Object o) {
		return liste.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return liste.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(final int index) {
		return liste.listIterator(index);
	}

	@Override
	public boolean remove(final Object o) {
		return liste.remove(o);
	}

	@Override
	public E remove(final int index) {
		return liste.remove(index);
	}

	@Override
	public boolean removeAll(final Collection<?> c) {
		return liste.removeAll(c);
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return liste.retainAll(c);
	}

	@Override
	public E set(final int index, final E element) {
		return liste.set(index, element);
	}

	@Override
	public int size() {
		return liste.size();
	}

	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return liste.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return liste.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] a) {
		return liste.toArray(a);
	}

}
