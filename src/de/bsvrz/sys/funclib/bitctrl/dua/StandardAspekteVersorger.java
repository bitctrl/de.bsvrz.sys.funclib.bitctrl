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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IStandardAspekte;
import de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen.IVerwaltung;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Ableitungen dieser Klasse stellen die Standard-Publikationsinformationen f�r
 * <b>einen</b> Modul-Typ und <b>eine</b> SWE innerhalb der DUA zur Verf�gung.<br>
 * Diese Informationen k�nnen im Konstruktor dieser Klasse f�r je eine
 * SWE-Modultyp-Kombination als Instanz der Klasse
 * <code>StandardAspekteAdapter</code> angelegt werden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public abstract class StandardAspekteVersorger {

	/**
	 * Debug-Logger.
	 */
	protected static final Debug LOGGER = Debug.getLogger();

	/**
	 * Verbindung zum Verwaltungsmodul.
	 */
	protected IVerwaltung verwaltung = null;

	/**
	 * Die Informationen �ber die Standardaspekte f�r die Publikation einer
	 * bestimmten SWE-Modultyp-Kombinationen (hier mit leerem Objekt
	 * initialisiert, das zur�ckgegeben wird, wenn die Standardaspekte nicht zur
	 * Verf�gung stehen).
	 */
	protected IStandardAspekte standardAspekte = new IStandardAspekte() {

		/**
		 * {@inheritDoc}
		 */
		public final Collection<DAVObjektAnmeldung> getStandardAnmeldungen(
				final SystemObject[] objektFilter) {
			return new ArrayList<DAVObjektAnmeldung>();
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getStandardAspekt(ResultData originalDatum) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Collection<AttributeGroup> getAlleAttributGruppen() {
			return new ArrayList<AttributeGroup>();
		}

	};

	/**
	 * Standardkonstruktor.
	 * 
	 * @param verwaltung
	 *            Verbindung zum Verwaltungsmodul
	 * @throws DUAInitialisierungsException wird weitergereicht
	 */
	public StandardAspekteVersorger(final IVerwaltung verwaltung)
			throws DUAInitialisierungsException {
		this.verwaltung = verwaltung;
		init();
	}

	/**
	 * Initialisiert die Standard-Publikationsinformationen f�r einen kompletten
	 * Modul-Typ und eine SWE.
	 * 
	 * @throws DUAInitialisierungsException
	 *             wenn es Probleme beim Initialisieren der
	 *             Standard-Publikationsinformationen gab
	 */
	protected abstract void init() throws DUAInitialisierungsException;

	/**
	 * Erfragt die Standardpublikations-Informationen.
	 * 
	 * @return eine <code>IStandardAspekte</code>-Schnittstelle zu den
	 *         Standardpublikations-Informationen
	 */
	public final IStandardAspekte getStandardPubInfos() {
		return this.standardAspekte;
	}

	/**
	 * In diesen Objekten werden alle Informationen �ber das standardm��ige
	 * Publikationsverhalten von SWE-Modul-Typ- Kombinationen festgehalten.
	 * Diese Objekte werden innerhalb von <code>StandardAspekteVersorger</code>
	 * statisch erstellt und k�nnen �ber die statische Methode
	 * <code>getStandardPubInfos(..)</code> ausgelesen werden.
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 * 
	 */
	protected class StandardAspekteAdapter implements IStandardAspekte {

		/**
		 * Notwendige Datenanmeldungen.
		 */
		private Collection<DAVObjektAnmeldung> anmeldungenGlobal = new TreeSet<DAVObjektAnmeldung>();

		/**
		 * Mapping von Systemobjekt-Attributgruppe-Aspekt-Kombination auf einen
		 * Standardpublikationsaspekt.
		 */
		private Map<DAVObjektAnmeldung, Aspect> publikationsMap = new TreeMap<DAVObjektAnmeldung, Aspect>();

		/**
		 * Standardkonstruktor.
		 * 
		 * @param zuordnungen
		 *            Liste mit Standardpublikationszurodnungen
		 * @throws DUAInitialisierungsException
		 *             wenn Standard- Publikaionsinformationen nicht angelegt
		 *             werden konnten
		 */
		public StandardAspekteAdapter(
				final StandardPublikationsZuordnung[] zuordnungen)
				throws DUAInitialisierungsException {
			if (zuordnungen != null) {
				for (StandardPublikationsZuordnung zuordnung : zuordnungen) {
					try {
						for (SystemObject finObj : DUAUtensilien
								.getBasisInstanzen(
										zuordnung.typ,
										verwaltung.getVerbindung(),
										StandardAspekteVersorger.this.verwaltung
												.getKonfigurationsBereiche())) {
							anmeldungenGlobal.add(new DAVObjektAnmeldung(
									finObj, new DataDescription(zuordnung.atg,
											zuordnung.aspAusgang, (short) 0)));
						}

						DataDescription originalDesc = new DataDescription(
								zuordnung.atg, zuordnung.aspEingang, (short) 0);
						for (SystemObject obj : zuordnung.typ.getElements()) {
							if (obj.isValid()) {
								DAVObjektAnmeldung objektAnmeldung = new DAVObjektAnmeldung(
										obj, originalDesc);
								this.publikationsMap.put(objektAnmeldung,
										zuordnung.aspAusgang);
							}
						}
					} catch (Exception e) {
						throw new DUAInitialisierungsException("Standard-" + //$NON-NLS-1$
								"Publikaionsinformationen konnten nicht angelegt werden"); //$NON-NLS-1$
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getStandardAspekt(ResultData originalDatum) {
			Aspect ergebnis = null;

			if (originalDatum != null) {
				try {
					DAVObjektAnmeldung objektAnmeldung = new DAVObjektAnmeldung(
							originalDatum.getObject(), originalDatum
									.getDataDescription());

					ergebnis = this.publikationsMap.get(objektAnmeldung);
				} catch (IllegalArgumentException e) {
					LOGGER.fine("Der Standard-Publikationsaspekt konnte" + //$NON-NLS-1$
							"nicht ermittelt werden: " + originalDatum, e); //$NON-NLS-1$
				}
			}

			return ergebnis;
		}

		/**
		 * {@inheritDoc}
		 */
		public final Collection<DAVObjektAnmeldung> getStandardAnmeldungen(
				final SystemObject[] objektFilter) {
			Collection<DAVObjektAnmeldung> anmeldungen = new TreeSet<DAVObjektAnmeldung>();

			if (objektFilter == null || objektFilter.length == 0) {
				anmeldungen = anmeldungenGlobal;
			} else {
				HashSet<SystemObject> objekte = new HashSet<SystemObject>();
				for (SystemObject obj : objektFilter) {
					objekte.add(obj);
				}

				for (DAVObjektAnmeldung anmeldung : anmeldungenGlobal) {
					if (objekte.contains(anmeldung.getObjekt())) {
						anmeldungen.add(anmeldung);
					}
				}
			}

			return anmeldungen;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s = "Objekt-Anmeldungen:\n"; //$NON-NLS-1$

			for (DAVObjektAnmeldung anmeldung : anmeldungenGlobal) {
				s += anmeldung;
			}

			return s;
		}

		/**
		 * {@inheritDoc}
		 */
		public Collection<AttributeGroup> getAlleAttributGruppen() {
			Collection<AttributeGroup> attributGruppen = new TreeSet<AttributeGroup>();

			for (DAVObjektAnmeldung anmeldung : this.anmeldungenGlobal) {
				attributGruppen.add(anmeldung.getDatenBeschreibung()
						.getAttributeGroup());
			}

			return attributGruppen;
		}

	}

	/**
	 * Zuordnung einer bestimmten
	 * <code>SystemObjectType-AttributeGroup-Aspect</code>-Kombination zu
	 * einem Standardpublikationsaspekt.
	 * 
	 * @author BitCtrl Systems GmbH, Thierfelder
	 * 
	 */
	protected class StandardPublikationsZuordnung {

		/**
		 * Objekttyp eines Originaldatums.
		 */
		protected SystemObjectType typ = null;

		/**
		 * Attributgruppe eines Originadatums.
		 */
		protected AttributeGroup atg = null;

		/**
		 * Aspekt eines Originaldatums.
		 */
		protected Aspect aspEingang = null;

		/**
		 * Standardpublikationsaspekt.
		 */
		protected Aspect aspAusgang = null;

		/**
		 * Standardkonstruktor.
		 * 
		 * @param typ
		 *            Objekttyp des Originaldatums
		 * @param atg
		 *            Attributgruppe des Originadatums
		 * @param aspEingang
		 *            Aspekt des Originaldatums
		 * @param aspAusgang
		 *            Standardpublikationsaspekt f�r die <code>SystemObjectType-
		 *            AttributeGroup-Aspect</code>-Kombination
		 * @throws DUAInitialisierungsException
		 *             falls eines der �bergebenen DAV-Elemente nicht ausgelesen
		 *             werden konnte
		 */
		public StandardPublikationsZuordnung(String typ, String atg,
				String aspEingang, String aspAusgang)
				throws DUAInitialisierungsException {
			try {
				DataModel dataModel = StandardAspekteVersorger.this.verwaltung
						.getVerbindung().getDataModel();
				this.typ = dataModel.getType(typ);
				this.atg = dataModel.getAttributeGroup(atg);
				this.aspEingang = dataModel.getAspect(aspEingang);
				this.aspAusgang = dataModel.getAspect(aspAusgang);
			} catch (Exception ex) {
				throw new DUAInitialisierungsException(
						"Standardpublikationsaspekt" + //$NON-NLS-1$
								" konnte" + //$NON-NLS-1$
								" nicht eingerichtet werden ", ex); //$NON-NLS-1$
			}
		}
	}
}
