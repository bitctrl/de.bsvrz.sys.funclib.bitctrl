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
	private List<E> liste;

	public ObservableList() {
		listener = new EventListenerList();
		liste = Collections.synchronizedList(new LinkedList<E>());
	}

	public void addChangeListener(ChangeListener l) {
		listener.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listener.remove(ChangeListener.class, l);
	}

	protected void fireChangeEvent() {
		ChangeEvent e;

		e = new ChangeEvent(this);
		for (ChangeListener l : listener.getListeners(ChangeListener.class)) {
			l.stateChanged(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean add(E o) {
		boolean changed;

		changed = liste.add(o);
		fireChangeEvent();
		return changed;
	}

	/**
	 * {@inheritDoc}
	 */

	public void add(int index, E element) {
		liste.add(index, element);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean addAll(Collection<? extends E> c) {
		return liste.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean addAll(int index, Collection<? extends E> c) {
		return liste.addAll(index, c);
	}

	/**
	 * {@inheritDoc}
	 */

	public void clear() {
		liste.clear();
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean contains(Object o) {
		return liste.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean containsAll(Collection<?> c) {
		return liste.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */

	public E get(int index) {
		return liste.get(index);
	}

	/**
	 * {@inheritDoc}
	 */

	public int indexOf(Object o) {
		return liste.indexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean isEmpty() {
		return liste.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */

	public Iterator<E> iterator() {
		return liste.iterator();
	}

	/**
	 * {@inheritDoc}
	 */

	public int lastIndexOf(Object o) {
		return liste.lastIndexOf(o);
	}

	/**
	 * {@inheritDoc}
	 */

	public ListIterator<E> listIterator() {
		return liste.listIterator();
	}

	/**
	 * {@inheritDoc}
	 */

	public ListIterator<E> listIterator(int index) {
		return liste.listIterator(index);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean remove(Object o) {
		return liste.remove(o);
	}

	/**
	 * {@inheritDoc}
	 */

	public E remove(int index) {
		return liste.remove(index);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean removeAll(Collection<?> c) {
		return liste.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */

	public boolean retainAll(Collection<?> c) {
		return liste.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */

	public E set(int index, E element) {
		return liste.set(index, element);
	}

	/**
	 * {@inheritDoc}
	 */

	public int size() {
		return liste.size();
	}

	/**
	 * {@inheritDoc}
	 */

	public List<E> subList(int fromIndex, int toIndex) {
		return liste.subList(fromIndex, toIndex);
	}

	/**
	 * {@inheritDoc}
	 */

	public Object[] toArray() {
		return liste.toArray();
	}

	/**
	 * {@inheritDoc}
	 */

	public <T> T[] toArray(T[] a) {
		return liste.toArray(a);
	}

}
