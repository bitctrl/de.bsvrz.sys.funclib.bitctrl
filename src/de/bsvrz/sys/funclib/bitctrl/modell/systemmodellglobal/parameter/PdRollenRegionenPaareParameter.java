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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Region;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Rolle;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.parameter.PdRollenRegionenPaareParameter.Daten.RolleRegionPaar;

/**
 * Kapselt die Attributgruppe
 * {@value PdBenutzerParameter#ATG_BENUTZER_PARAMETER}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class PdRollenRegionenPaareParameter extends
		AbstractParameterDatensatz<PdRollenRegionenPaareParameter.Daten> {

	/**
	 * Repräsentation der Daten des Parameters.
	 */
	public static class Daten extends AbstractDatum implements
			List<Daten.RolleRegionPaar> {

		/**
		 * Ein zusammengehöriges Paar aus Rolle und Region.
		 */
		public static class RolleRegionPaar {

			/** Die Rolle. */
			private Rolle rolle;

			/** Die Region. */
			private Region region;

			/**
			 * Initialisiert das Paar.
			 * 
			 * @param rolle
			 *            die Rolle.
			 * @param region
			 *            die Region.
			 */
			public RolleRegionPaar(final Rolle rolle, final Region region) {
				this.rolle = rolle;
				this.region = region;
			}

			/**
			 * Gibt die Rolle des Paares zurück.
			 * 
			 * @return die Rolle.
			 */
			public Rolle getRolle() {
				return rolle;
			}

			/**
			 * Legt die Rolle des Paares fest.
			 * 
			 * @param rolle
			 *            die Rolle.
			 */
			public void setRolle(final Rolle rolle) {
				this.rolle = rolle;
			}

			/**
			 * Gibt die Region des Paares zurück.
			 * 
			 * @return die Region.
			 */
			public Region getRegion() {
				return region;
			}

			/**
			 * Legt die Region des Paares fest.
			 * 
			 * @param region
			 *            die Region.
			 */
			public void setRegion(final Region region) {
				this.region = region;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public boolean equals(final Object obj) {
				if (this == obj) {
					return true;
				} else if (obj instanceof RolleRegionPaar) {
					final RolleRegionPaar a = (RolleRegionPaar) obj;

					return getRegion().equals(a.getRegion())
							&& getRolle().equals(a.getRolle());
				}
				return false;
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public int hashCode() {
				return rolle.hashCode() | region.hashCode();
			}

			/**
			 * {@inheritDoc}
			 */
			@Override
			public String toString() {
				String s;

				s = getClass() + "[";
				s += "rolle" + rolle;
				s += "region" + region;
				s += "]";

				return s;
			}

		}

		/** Der Initialzustand des Datenstatus ist {@value}. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/** Die Liste der Paare. */
		private final List<RolleRegionPaar> paare = new ArrayList<RolleRegionPaar>();

		/**
		 * {@inheritDoc}
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Legt den aktuellen Datensatzstatus fest.
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
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;

			return klon;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = getClass().getSimpleName() + "[";

			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;

			return s + "]";
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean add(final RolleRegionPaar o) {
			return paare.add(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public void add(final int index, final RolleRegionPaar element) {
			paare.add(index, element);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(final Collection<? extends RolleRegionPaar> c) {
			return paare.addAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean addAll(final int index,
				final Collection<? extends RolleRegionPaar> c) {
			return paare.addAll(index, c);
		}

		/**
		 * {@inheritDoc}
		 */
		public void clear() {
			paare.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean contains(final Object o) {
			return paare.contains(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean containsAll(final Collection<?> c) {
			return paare.containsAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public RolleRegionPaar get(final int index) {
			return paare.get(index);
		}

		/**
		 * {@inheritDoc}
		 */
		public int indexOf(final Object o) {
			return paare.indexOf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean isEmpty() {
			return paare.isEmpty();
		}

		/**
		 * {@inheritDoc}
		 */
		public Iterator<RolleRegionPaar> iterator() {
			return paare.iterator();
		}

		/**
		 * {@inheritDoc}
		 */
		public int lastIndexOf(final Object o) {
			return paare.lastIndexOf(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public ListIterator<RolleRegionPaar> listIterator() {
			return paare.listIterator();
		}

		/**
		 * {@inheritDoc}
		 */
		public ListIterator<RolleRegionPaar> listIterator(final int index) {
			return paare.listIterator(index);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean remove(final Object o) {
			return paare.remove(o);
		}

		/**
		 * {@inheritDoc}
		 */
		public RolleRegionPaar remove(final int index) {
			return paare.remove(index);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean removeAll(final Collection<?> c) {
			return paare.removeAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean retainAll(final Collection<?> c) {
			return paare.retainAll(c);
		}

		/**
		 * {@inheritDoc}
		 */
		public RolleRegionPaar set(final int index,
				final RolleRegionPaar element) {
			return paare.set(index, element);
		}

		/**
		 * {@inheritDoc}
		 */
		public int size() {
			return paare.size();
		}

		/**
		 * {@inheritDoc}
		 */
		public List<RolleRegionPaar> subList(final int fromIndex,
				final int toIndex) {
			return paare.subList(fromIndex, toIndex);
		}

		/**
		 * {@inheritDoc}
		 */
		public Object[] toArray() {
			return paare.toArray();
		}

		/**
		 * {@inheritDoc}
		 */
		public <T> T[] toArray(final T[] a) {
			return paare.toArray(a);
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_ROLLEN_REGIONEN_PAARE_PARAMETER = "atg.rollenRegionenPaareParameter";

	/** Die Attributgruppe. */
	private static AttributeGroup atg;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            ein Systemobjekt, welches eine Berechtigungsklasse darstellt.
	 */
	public PdRollenRegionenPaareParameter(final SystemObjekt objekt) {
		super(objekt);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_ROLLEN_REGIONEN_PAARE_PARAMETER);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();
		final Array feld = daten.getArray("rollenRegionenPaare");

		feld.setLength(datum.size());
		for (int i = 0; i < datum.size(); ++i) {
			final RolleRegionPaar paar = datum.get(i);
			final Data paarDav = feld.getItem(i);

			paarDav.getReferenceValue("region").setSystemObject(
					paar.getRegion().getSystemObject());
			paarDav.getReferenceValue("rolle").setSystemObject(
					paar.getRolle().getSystemObject());
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
	public void setDaten(final ResultData datensatz) {
		check(datensatz);

		final Daten datum = new Daten();
		if (datensatz.hasData()) {
			ObjektFactory factory;
			Data daten;

			factory = ObjektFactory.getInstanz();
			daten = datensatz.getData();

			final Array feld = daten.getArray("rollenRegionenPaare");

			for (int i = 0; i < feld.getLength(); ++i) {
				final Data paarDav = feld.getItem(i);
				final Region region = (Region) factory.getModellobjekt(paarDav
						.getReferenceValue("region").getSystemObject());
				final Rolle rolle = (Rolle) factory.getModellobjekt(paarDav
						.getReferenceValue("rolle").getSystemObject());

				datum.add(new RolleRegionPaar(rolle, region));
			}
		}

		datum.setZeitstempel(datensatz.getDataTime());
		datum.setDatenStatus(Datum.Status.getStatus(datensatz.getDataState()));
		setDatum(datensatz.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(datensatz.getDataDescription().getAspect(),
				datum.clone());
	}

}
