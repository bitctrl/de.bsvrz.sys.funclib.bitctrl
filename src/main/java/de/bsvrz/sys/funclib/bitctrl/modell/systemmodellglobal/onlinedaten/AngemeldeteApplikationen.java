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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.onlinedaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.bitctrl.util.Timestamp;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Applikation;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Datenverteiler;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.onlinedaten.AngemeldeteApplikationen.Daten.AngemeldeteApplikation;

/**
 * Kapselt die Attributgruppe {@code atg.angemeldeteApplikationen}.
 *
 * @author BitCtrl Systems GmbH, Schumann
 */
public class AngemeldeteApplikationen extends AbstractOnlineDatensatz<AngemeldeteApplikationen.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.standard}. */
		Standard("asp.standard");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 *
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		Aspekte(final String pid) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung().getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		@Override
		public Aspect getAspekt() {
			return aspekt;
		}

		@Override
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum implements List<Daten.AngemeldeteApplikation> {

		/**
		 * Beschreibt ein Element der Attributliste.
		 */
		public static class AngemeldeteApplikation {
			/** Der angemeldete Benutzer. */
			private Benutzer benutzer;

			/** Die Applikation an der der Benutzer angemeldet ist. */
			private Applikation applikation;

			/** Der Zeitpunkt der Anmeldung laut Datenverteiler. */
			private Timestamp seit;

			/**
			 * Gibt den angemeldeten Benutzer zurück.
			 *
			 * @return der angemeldete Benutzer.
			 */
			public Benutzer getBenutzer() {
				return benutzer;
			}

			/**
			 * Legt den angemeldeten Benutzer fest.
			 *
			 * @param benutzer
			 *            der angemeldete Benutzer.
			 */
			public void setBenutzer(final Benutzer benutzer) {
				this.benutzer = benutzer;
			}

			/**
			 * Gibt die Applikation an der der Benutzer angemeldet ist zurück.
			 *
			 * @return die Applikation.
			 */
			public Applikation getApplikation() {
				return applikation;
			}

			/**
			 * Legt die Applikation an der der Benutzer angemeldet ist fest.
			 *
			 * @param applikation
			 *            die Applikation.
			 */
			public void setApplikation(final Applikation applikation) {
				this.applikation = applikation;
			}

			/**
			 * Gibt den Zeitpunkt der Anmeldung laut Datenverteiler zurück.
			 *
			 * @return der Anmeldezeitpunkt.
			 */
			public Timestamp getSeit() {
				return seit;
			}

			/**
			 * Legt den Zeitpunkt der Anmeldung laut Datenverteiler fest.
			 *
			 * @param seit
			 *            der Anmeldezeitpunkt.
			 */
			public void setSeit(final Timestamp seit) {
				this.seit = seit;
			}

			@Override
			public boolean equals(final Object obj) {
				if (obj == this) {
					return true;
				}
				if (obj instanceof AngemeldeteApplikation) {
					final AngemeldeteApplikation o = (AngemeldeteApplikation) obj;
					boolean equals = true;

					if (applikation != null) {
						equals &= applikation.equals(o.applikation);
					} else {
						equals &= applikation == o.applikation;
					}

					if (benutzer != null) {
						equals &= benutzer.equals(o.benutzer);
					} else {
						equals &= benutzer == o.benutzer;
					}

					return equals;
				}
				return false;
			}

			/**
			 * Der Hash wird aus dem XOR der Hashs für {@code Applikation},
			 * {@code Benutzer} und {@code Seit} gebildet.
			 */
			@Override
			public int hashCode() {
				return applikation.hashCode() ^ benutzer.hashCode() ^ seit.hashCode();
			}

			@Override
			public String toString() {
				String s;

				s = getClass().getName() + "[";
				s += ", benutzer=" + benutzer;
				s += ", applikation=" + applikation;
				s += ", seit=" + seit;
				s += "]";

				return s;
			}

		}

		/** Der aktuelle Status des Datensatzes. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/** Die Liste der angemeldeten Applikationen. */
		private final List<AngemeldeteApplikation> angemeldeteApplikationen = new ArrayList<>();

		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			klon.angemeldeteApplikationen.addAll(angemeldeteApplikationen);

			return klon;
		}

		@Override
		public Status getDatenStatus() {
			return datenStatus;
		}

		@Override
		public String toString() {
			String s;

			s = getClass().getName() + "[";
			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;
			s += ", angemeldeteApplikationen=" + angemeldeteApplikationen;
			s += "]";

			return s;
		}

		/**
		 * Setzt den aktuellen Status des Datensatzes.
		 *
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		@Override
		public boolean add(final AngemeldeteApplikation e) {
			return angemeldeteApplikationen.add(e);
		}

		@Override
		public void add(final int index, final AngemeldeteApplikation element) {
			angemeldeteApplikationen.add(index, element);
		}

		@Override
		public boolean addAll(final Collection<? extends AngemeldeteApplikation> c) {
			return angemeldeteApplikationen.addAll(c);
		}

		@Override
		public boolean addAll(final int index, final Collection<? extends AngemeldeteApplikation> c) {
			return angemeldeteApplikationen.addAll(index, c);
		}

		@Override
		public void clear() {
			angemeldeteApplikationen.clear();
		}

		@Override
		public boolean contains(final Object o) {
			return angemeldeteApplikationen.contains(o);
		}

		@Override
		public boolean containsAll(final Collection<?> c) {
			return angemeldeteApplikationen.containsAll(c);
		}

		@Override
		public AngemeldeteApplikation get(final int index) {
			return angemeldeteApplikationen.get(index);
		}

		@Override
		public int indexOf(final Object o) {
			return angemeldeteApplikationen.indexOf(o);
		}

		@Override
		public boolean isEmpty() {
			return angemeldeteApplikationen.isEmpty();
		}

		@Override
		public Iterator<AngemeldeteApplikation> iterator() {
			return angemeldeteApplikationen.iterator();
		}

		@Override
		public int lastIndexOf(final Object o) {
			return angemeldeteApplikationen.lastIndexOf(o);
		}

		@Override
		public ListIterator<AngemeldeteApplikation> listIterator() {
			return angemeldeteApplikationen.listIterator();
		}

		@Override
		public ListIterator<AngemeldeteApplikation> listIterator(final int index) {
			return angemeldeteApplikationen.listIterator(index);
		}

		@Override
		public boolean remove(final Object o) {
			return angemeldeteApplikationen.remove(o);
		}

		@Override
		public AngemeldeteApplikation remove(final int index) {
			return angemeldeteApplikationen.remove(index);
		}

		@Override
		public boolean removeAll(final Collection<?> c) {
			return angemeldeteApplikationen.removeAll(c);
		}

		@Override
		public boolean retainAll(final Collection<?> c) {
			return angemeldeteApplikationen.retainAll(c);
		}

		@Override
		public AngemeldeteApplikation set(final int index, final AngemeldeteApplikation element) {
			return angemeldeteApplikationen.set(index, element);
		}

		@Override
		public int size() {
			return angemeldeteApplikationen.size();
		}

		@Override
		public List<AngemeldeteApplikation> subList(final int fromIndex, final int toIndex) {
			return angemeldeteApplikationen.subList(fromIndex, toIndex);
		}

		@Override
		public Object[] toArray() {
			return angemeldeteApplikationen.toArray();
		}

		@Override
		public <T> T[] toArray(final T[] a) {
			return angemeldeteApplikationen.toArray(a);
		}

	}

	/** Die PID der Attributgruppe. */
	private static final String ATG_ANGEMELDETE_APPLIKATIONEN = "atg.angemeldeteApplikationen";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 *
	 * @param dav
	 *            der Datenverteiler, dessen Onlinedaten verwalten werden
	 *            sollen.
	 */
	public AngemeldeteApplikationen(final Datenverteiler dav) {
		super(dav);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung().getDataModel();
			atg = modell.getAttributeGroup(ATG_ANGEMELDETE_APPLIKATIONEN);
			assert atg != null;
		}
	}

	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();
		final Array feld = daten.asArray();

		feld.setLength(datum.size());
		for (int i = 0; i < datum.size(); ++i) {
			feld.getItem(i).getReferenceValue("applikation")
			.setSystemObject(datum.get(i).getApplikation().getSystemObject());
			feld.getItem(i).getReferenceValue("benutzer").setSystemObject(datum.get(i).getBenutzer().getSystemObject());
			feld.getItem(i).getTimeValue("seit").setMillis(datum.get(i).getSeit().getTime());
		}

		return daten;
	}

	@Override
	public Daten erzeugeDatum() {
		return new Daten();
	}

	@Override
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	@Override
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final Data daten = result.getData();
			final Array feld = daten.getArray("angemeldeteApplikation");

			for (int i = 0; i < feld.getLength(); ++i) {
				final AngemeldeteApplikation app = new AngemeldeteApplikation();
				SystemObject so;

				so = feld.getItem(i).getReferenceValue("applikation").getSystemObject();
				if (so != null) {
					app.setApplikation((Applikation) factory.getModellobjekt(so));
				}

				so = feld.getItem(i).getReferenceValue("benutzer").getSystemObject();
				if (so != null) {
					app.setBenutzer((Benutzer) factory.getModellobjekt(so));
				}

				app.setSeit(new Timestamp(feld.getItem(i).getTimeValue("seit").getMillis()));
				datum.add(app);
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(), datum.clone());
	}
}
