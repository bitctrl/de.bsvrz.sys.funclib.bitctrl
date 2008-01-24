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

package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Implementierung eines KomplexXY-Objektes, das durch ein SystemObjekt
 * innerhalb der Datenverteiler-Konfiguration repr�sentiert wird.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class KomplexXYImpl extends AbstractSystemObjekt implements KomplexXY {

	/**
	 * Logger f�r Debugausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die Liste der Punkte des Objekts.
	 */
	List<Punkt> punkte;

	/**
	 * die Liste der Linien des Objekts.
	 */
	List<Linie> linien;

	/**
	 * die Liste der Fl�chen des Objekts.
	 */
	List<Flaeche> flaechen;

	/**
	 * die Liste der komplexen Objekte des Objekts.
	 */
	List<Komplex> komplexe;

	/**
	 * Konstruktor. Die Funktion erstellt ein Komplexes Objekt, das durch das
	 * �bergebenen Objekt innerhalb der Datenverteilerkonfiguration
	 * repr�sentiert ist.
	 * 
	 * @param obj
	 *            das Objekt
	 */
	public KomplexXYImpl(SystemObject obj) {
		super(obj);
	}

	/**
	 * liest die Objekte von der Datenverteiler-Konfiguration aus und f�llt die
	 * internen Strukturen.
	 */
	private void fuelleObjektListen() {
		flaechen = new ArrayList<Flaeche>();
		punkte = new ArrayList<Punkt>();
		linien = new ArrayList<Linie>();
		komplexe = new ArrayList<Komplex>();

		DataModel model = getSystemObject().getDataModel();
		Data daten = getSystemObject().getConfigurationData(
				model.getAttributeGroup("atg.komplexKoordinaten"));
		if (daten != null) {
			Data.Array array = daten.getArray("Fl�chenReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					Flaeche neuesObjekt = (FlaecheXY) ObjektFactory
							.getInstanz().getModellobjekt(
									array.getReferenceValue(idx)
											.getSystemObject());
					if (neuesObjekt != null) {
						flaechen.add(neuesObjekt);
					} else {
						LOGGER.warning("Fl�chenobjekt: "
								+ array.getReferenceValue(idx)
										.getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}

			array = daten.getArray("PunktReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					Punkt neuesObjekt = (Punkt) ObjektFactory.getInstanz()
							.getModellobjekt(
									array.getReferenceValue(idx)
											.getSystemObject());
					if (neuesObjekt != null) {
						punkte.add(neuesObjekt);
					} else {
						LOGGER.warning("Punktobjekt: "
								+ array.getReferenceValue(idx)
										.getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}

			array = daten.getArray("LinienReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					Linie neuesObjekt = (Linie) ObjektFactory.getInstanz()
							.getModellobjekt(
									array.getReferenceValue(idx)
											.getSystemObject());
					if (neuesObjekt != null) {
						linien.add(neuesObjekt);
					} else {
						LOGGER.warning("Linienobjekt: "
								+ array.getReferenceValue(idx)
										.getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}

			array = daten.getArray("KomplexReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					Komplex neuesObjekt = (Komplex) ObjektFactory.getInstanz()
							.getModellobjekt(
									array.getReferenceValue(idx)
											.getSystemObject());
					if (neuesObjekt != null) {
						komplexe.add(neuesObjekt);
					} else {
						LOGGER.warning("Komplexobjekt: "
								+ array.getReferenceValue(idx)
										.getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.KomplexXY#getFlaechen()
	 */
	public List<Flaeche> getFlaechen() {
		if (flaechen == null) {
			fuelleObjektListen();
		}
		return flaechen;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.KomplexXY#getKomplexe()
	 */
	public List<Komplex> getKomplexe() {
		if (komplexe == null) {
			fuelleObjektListen();
		}
		return komplexe;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.KomplexXY#getLinien()
	 */
	public List<Linie> getLinien() {
		if (linien == null) {
			fuelleObjektListen();
		}
		return linien;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.geo.KomplexXY#getPunkte()
	 */
	public List<Punkt> getPunkte() {
		if (punkte == null) {
			fuelleObjektListen();
		}
		return punkte;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.KOMPLEX_XY;
	}
}
