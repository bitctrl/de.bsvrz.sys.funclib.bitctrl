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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.intpuf;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Temporaerer Puffer fuer grosse Datenmengen, deren einzelne Bestandteile
 * (Datensaetze) chronologisch aufeinanderfolgen und deren Eigenschaften sich
 * relativ selten aendern<br>
 * Achtung: Puffer funktioniert nur fuer chronologisch einlaufende Daten
 *
 * TODO Mal optimieren und nicht immer ueber den ganzen Puffer iterieren!!!
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @param <T>
 *            die Objektart, ueber die sich die Gleichheit bzw. Ungleichheit
 */
public class IntervallPuffer<T extends IIntervallDatum<T>> {

	/**
	 * Speichert die Daten.
	 */
	private final SortedMap<Long, Intervall<T>> puffer = new TreeMap<Long, Intervall<T>>();

	/**
	 * Loescht alle Daten aus dem Puffer, die aelter als der uebergebene
	 * Zeitstempel sind.
	 *
	 * @param startIntervall
	 *            der neue Intervallbegin dieses Puffers
	 * @throws IntervallPufferException
	 *             wenn das obere Ende des Intervalls vor dem neuen unteren Ende
	 *             liegt
	 */
	public final void loescheAllesUnterhalbVon(final long startIntervall) throws IntervallPufferException {
		final List<Long> loeschListe = new ArrayList<Long>();
		for (final long start : this.puffer.keySet()) {
			final Intervall<T> intervall = this.puffer.get(start);
			if ((intervall.getIntervallStart() < startIntervall) && (intervall.getIntervallEnde() <= startIntervall)) {
				loeschListe.add(start);
			} else if ((intervall.getIntervallStart() < startIntervall)
					&& (intervall.getIntervallEnde() > startIntervall)) {
				final long restImIntervall = intervall.getIntervallEnde() - startIntervall;
				final long restIntervallTeile = restImIntervall / intervall.getGranularitaet();
				if (restIntervallTeile > 0) {
					intervall.setStart(
							intervall.getIntervallEnde() - (restIntervallTeile * intervall.getGranularitaet()));
				} else {
					loeschListe.add(start);
				}
			}
		}

		for (final Long zuLoeschenderSchluessel : loeschListe) {
			this.puffer.remove(zuLoeschenderSchluessel);
		}
	}

	/**
	 * Fuegt diesem Puffer ein neues Element hinzu.
	 *
	 * @param element
	 *            eine neues Pufferelement
	 * @throws IntervallPufferException
	 *             wenn das obere Ende des Intervalls vor dem unteren Ende liegt
	 */
	public final void add(final IIntervallPufferElement<T> element) throws IntervallPufferException {
		Intervall<T> letztesIntervall = null;

		if (!this.puffer.keySet().isEmpty()) {
			letztesIntervall = puffer.get(puffer.lastKey());
		}

		if (letztesIntervall == null) {
			letztesIntervall = new Intervall<T>(element);
			this.puffer.put(element.getIntervallStart(), letztesIntervall);
		} else {
			if (letztesIntervall.isKompatibel(element)) {
				letztesIntervall.add(element);
			} else {
				final Intervall<T> neuesIntervall = new Intervall<T>(element);
				this.puffer.put(element.getIntervallStart(), neuesIntervall);
			}
		}

	}

	/**
	 * Erfragt die Anzahl der im Puffer gespeicherten Elemente.
	 *
	 * @return die Anzahl der im Puffer gespeicherten Elemente
	 */
	public final long getSpeicherAuslastung() {
		return this.puffer.keySet().size();
	}

	@Override
	public String toString() {
		long start = Long.MAX_VALUE;
		long ende = Long.MIN_VALUE;

		for (final Intervall<T> intervall : this.puffer.values()) {
			if (intervall.getIntervallStart() < start) {
				start = intervall.getIntervallStart();
			}
			if (intervall.getIntervallEnde() > ende) {
				ende = intervall.getIntervallEnde();
			}
		}

		return this.puffer.isEmpty() ? "leer" : "[" + start + //$NON-NLS-1$ //$NON-NLS-2$
				", " + ende + "] Mem: " + this.puffer.size(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Speichert jeweils kompatible Daten innerhalb eines Intervalls..
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 * @param <T1>
	 *            der Typ des Intervall
	 */
	protected class Intervall<T1 extends IIntervallDatum<T1>> extends IntervallPufferElementAdapter<T1> {

		/**
		 * die Granularitaet dieses Intervalls.
		 */
		private final long granularitaet;

		/**
		 * Standardkonstruktor.
		 *
		 * @param element
		 *            ein erstes Element dieses Intervalls
		 * @throws IntervallPufferException
		 *             wenn das obere Ende des Intervalls vor dem unteren Ende
		 *             liegt
		 */
		protected Intervall(final IIntervallPufferElement<T1> element) throws IntervallPufferException {
			super(element.getIntervallStart(), element.getIntervallEnde());
			inhalt = element.getInhalt();
			this.granularitaet = getIntervallEnde() - getIntervallStart();
			if (this.granularitaet < 0) {
				throw new IntervallPufferException("Intervallende (" + getIntervallEnde() + //$NON-NLS-1$
						") liegt vor Intervallanfang (" + getIntervallStart() + "):\n" + inhalt); //$NON-NLS-2$
			}
		}

		/**
		 * Setzt das untere Ende des Intervalls fest.
		 *
		 * @param start
		 *            das neue untere Ende des Intervalls
		 * @throws IntervallPufferException
		 *             wenn das obere Ende des Intervalls vor dem neuen unteren
		 *             Ende liegt
		 */
		protected final void setStart(final long start) throws IntervallPufferException {
			if ((getIntervallEnde() - start) < 0) {
				throw new IntervallPufferException("Intervallende (" + getIntervallEnde() + //$NON-NLS-1$
						") liegt vor Intervallanfang (" + getIntervallStart() + "):\n" + inhalt); //$NON-NLS-2$
			}
			intervallStart = start;
		}

		/**
		 * Ueberprueft, ob das uebergebene Datum mit den bisher gespeicherten
		 * Daten im Puffer kompatibel ist.
		 *
		 * @param element
		 *            ein in den Puffer zu integrierendes Element
		 * @return ob das uebergebene Datum mit den bisher gespeicherten Daten
		 *         im Puffer kompatibel ist
		 */
		protected final boolean isKompatibel(final IIntervallPufferElement<T1> element) {
			return element.getInhalt().istGleich(inhalt)
					&& ((element.getIntervallEnde() - element.getIntervallStart()) == this.granularitaet)
					&& (intervallEnde == element.getIntervallStart());
		}

		/**
		 * Fuegt diesem Intervall ein neues Element hinzu.
		 *
		 * @param element
		 *            ein neues, logisch zu diesem Intervall passendes Element
		 * @throws IntervallPufferException
		 *             wenn das einzufuegende Datum nicht mit den bisher
		 *             gespeicherten Daten kompatibel ist
		 */
		protected final void add(final IIntervallPufferElement<T1> element) throws IntervallPufferException {
			if (isKompatibel(element)) {
				intervallEnde += granularitaet;
			} else {
				throw new IntervallPufferException("Versuch inkompatibles Datum\n" + element + //$NON-NLS-1$
						"einzufuegen in Puffer:\n" + this); //$NON-NLS-1$
			}
		}

		/**
		 * Erfragt die Granularitaet dieses Intervalls.
		 *
		 * @return die Granularitaet dieses Intervalls
		 */
		protected final long getGranularitaet() {
			return this.granularitaet;
		}

		@Override
		public String toString() {
			return "[" + intervallStart + ", " + intervallEnde + //$NON-NLS-1$//$NON-NLS-2$
					"] Granularitaet: " + this.granularitaet + "\n" + inhalt; //$NON-NLS-2$
		}

	}

	/**
	 * liefert eine sortierte Liste der Daten des Puffers.
	 *
	 * @return die Daten des Puffers.
	 */
	public List<Intervall<T>> getPuffer() {
		return new ArrayList<>(puffer.values());
	}
}