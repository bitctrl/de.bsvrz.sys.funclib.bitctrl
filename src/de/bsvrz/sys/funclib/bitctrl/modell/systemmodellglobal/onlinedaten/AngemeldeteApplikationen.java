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
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
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
 * @version $Id$
 */
public class AngemeldeteApplikationen extends
		AbstractOnlineDatensatz<AngemeldeteApplikationen.Daten> {

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
		private Aspekte(final String pid) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getAspekt() {
			return aspekt;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum implements
			List<Daten.AngemeldeteApplikation> {

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

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean equals(final Object obj) {
				if (obj == this) {
					return true;
				}
				if (obj instanceof AngemeldeteApplikation) {
					final AngemeldeteApplikation o = (AngemeldeteApplikation) obj;

					return o.applikation.equals(applikation)
							&& o.benutzer.equals(benutzer)
							&& o.seit.equals(seit);
				}
				return false;
			}

			/**
			 * Der Hash wird aus dem XOR der Hashs für {@code Applikation},
			 * {@code Benutzer} und {@code Seit} gebildet.
			 * 
			 * {@inheritDoc}
			 */
			@Override
			public int hashCode() {
				return applikation.hashCode() ^ benutzer.hashCode()
						^ seit.hashCode();
			}

			/**
			 * {@inheritDoc}
			 */
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
		private final List<AngemeldeteApplikation> angemeldeteApplikationen = new ArrayList<AngemeldeteApplikation>();

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			klon.angemeldeteApplikationen.addAll(angemeldeteApplikationen);

			return klon;
		}

		/**
		 * {@inheritDoc}
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * {@inheritDoc}
		 */
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

		/**
		 * {@inheritDoc}
		 */
		public boolean add(final AngemeldeteApplikation e) {
			return angemeldeteApplikationen.add(e);
		}

		/**
		 * {@inheritDoc}
		 */
		public void add(final int index, final AngemeldeteApplikation element) {
			angemeldeteApplikationen.add(index, element);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(
				final Collection<? extends AngemeldeteApplikation> c) {
			return angemeldeteApplikationen.addAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(final int index,
				final Collection<? extends AngemeldeteApplikation> c) {
			return angemeldeteApplikationen.addAll(index, c);
		}

		/**
		 * {@inheritDoc}
		 */
		public void clear() {
			angemeldeteApplikationen.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		/**
		 * {@inheritDoc}
		 */
		public boolean contains(final Object o) {
			return angemeldeteApplikationen.contains(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(final Collection<?> c) {
			return angemeldeteApplikationen.containsAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public AngemeldeteApplikation get(final int index) {
			return angemeldeteApplikationen.get(index);
		}

		/**
		 * {@inheritDoc}
		 */
		public int indexOf(final Object o) {
			return angemeldeteApplikationen.indexOf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEmpty() {
			return angemeldeteApplikationen.isEmpty();
		}

		/**
		 * {@inheritDoc}
		 */
		public Iterator<AngemeldeteApplikation> iterator() {
			return angemeldeteApplikationen.iterator();
		}

		/**
		 * {@inheritDoc}
		 */
		public int lastIndexOf(final Object o) {
			return angemeldeteApplikationen.lastIndexOf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public ListIterator<AngemeldeteApplikation> listIterator() {
			return angemeldeteApplikationen.listIterator();
		}

		/**
		 * {@inheritDoc}
		 */
		public ListIterator<AngemeldeteApplikation> listIterator(final int index) {
			return angemeldeteApplikationen.listIterator(index);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean remove(final Object o) {
			return angemeldeteApplikationen.remove(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public AngemeldeteApplikation remove(final int index) {
			return angemeldeteApplikationen.remove(index);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(final Collection<?> c) {
			return angemeldeteApplikationen.removeAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean retainAll(final Collection<?> c) {
			return angemeldeteApplikationen.retainAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public AngemeldeteApplikation set(final int index,
				final AngemeldeteApplikation element) {
			return angemeldeteApplikationen.set(index, element);
		}

		/**
		 * {@inheritDoc}
		 */
		public int size() {
			return angemeldeteApplikationen.size();
		}

		/**
		 * {@inheritDoc}
		 */
		public List<AngemeldeteApplikation> subList(final int fromIndex,
				final int toIndex) {
			return angemeldeteApplikationen.subList(fromIndex, toIndex);
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] toArray() {
			return angemeldeteApplikationen.toArray();
		}

		/**
		 * {@inheritDoc}
		 */
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
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_ANGEMELDETE_APPLIKATIONEN);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<Aspect>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();
		final Array feld = daten.asArray();

		feld.setLength(datum.size());
		for (int i = 0; i < datum.size(); ++i) {
			feld.getItem(i).getReferenceValue("applikation").setSystemObject(
					datum.get(i).getApplikation().getSystemObject());
			feld.getItem(i).getReferenceValue("benutzer").setSystemObject(
					datum.get(i).getBenutzer().getSystemObject());
			feld.getItem(i).getTimeValue("seit").setMillis(
					datum.get(i).getSeit().getTime());
		}

		return daten;
	}

	/**
	 * {@inheritDoc}
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final Data daten = result.getData();
			final Array feld = daten.asArray();

			for (int i = 0; i < feld.getLength(); ++i) {
				final AngemeldeteApplikation app = new AngemeldeteApplikation();
				app.setApplikation((Applikation) factory.getModellobjekt(daten
						.getReferenceValue("applikation").getSystemObject()));
				app.setBenutzer((Benutzer) factory.getModellobjekt(daten
						.getReferenceValue("benutzer").getSystemObject()));
				app.setSeit(new Timestamp(daten.getTimeValue("seit")
						.getMillis()));
				datum.add(app);
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
