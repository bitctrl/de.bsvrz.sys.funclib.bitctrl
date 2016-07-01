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

package de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.geo.GeoModellTypen;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Implementierung eines KomplexXY-Objektes, das durch ein SystemObjekt
 * innerhalb der Datenverteiler-Konfiguration repräsentiert wird.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class KomplexXYImpl extends AbstractSystemObjekt implements KomplexXY {

	/**
	 * Logger für Debugausgaben.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * die Liste der Punkte des Objekts.
	 */
	private List<Punkt> punkte;

	/**
	 * die Liste der Linien des Objekts.
	 */
	private List<Linie> linien;

	/**
	 * die Liste der Flächen des Objekts.
	 */
	private List<Flaeche> flaechen;

	/**
	 * die Liste der komplexen Objekte des Objekts.
	 */
	private List<Komplex> komplexe;

	/**
	 * Konstruktor. Die Funktion erstellt ein Komplexes Objekt, das durch das
	 * übergebenen Objekt innerhalb der Datenverteilerkonfiguration
	 * repräsentiert ist.
	 *
	 * @param obj
	 *            das Objekt
	 */
	public KomplexXYImpl(final SystemObject obj) {
		super(obj);
	}

	/**
	 * liest die Objekte von der Datenverteiler-Konfiguration aus und füllt die
	 * internen Strukturen.
	 */
	private void fuelleObjektListen() {
		flaechen = new ArrayList<>();
		punkte = new ArrayList<>();
		linien = new ArrayList<>();
		komplexe = new ArrayList<>();

		final DataModel model = getSystemObject().getDataModel();
		final Data daten = getSystemObject().getConfigurationData(
				model.getAttributeGroup("atg.komplexKoordinaten"));
		if (daten != null) {
			Data.Array array = daten.getArray("FlächenReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					final Flaeche neuesObjekt = (FlaecheXY) ObjektFactory
							.getInstanz().getModellobjekt(array
									.getReferenceValue(idx).getSystemObject());
					if (neuesObjekt != null) {
						flaechen.add(neuesObjekt);
					} else {
						LOGGER.warning("Flächenobjekt: "
								+ array.getReferenceValue(idx).getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}

			array = daten.getArray("PunktReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					final Punkt neuesObjekt = (Punkt) ObjektFactory.getInstanz()
							.getModellobjekt(array.getReferenceValue(idx)
									.getSystemObject());
					if (neuesObjekt != null) {
						punkte.add(neuesObjekt);
					} else {
						LOGGER.warning("Punktobjekt: "
								+ array.getReferenceValue(idx).getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}

			array = daten.getArray("LinienReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					final Linie neuesObjekt = (Linie) ObjektFactory.getInstanz()
							.getModellobjekt(array.getReferenceValue(idx)
									.getSystemObject());
					if (neuesObjekt != null) {
						linien.add(neuesObjekt);
					} else {
						LOGGER.warning("Linienobjekt: "
								+ array.getReferenceValue(idx).getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}

			array = daten.getArray("KomplexReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					final Komplex neuesObjekt = (Komplex) ObjektFactory
							.getInstanz().getModellobjekt(array
									.getReferenceValue(idx).getSystemObject());
					if (neuesObjekt != null) {
						komplexe.add(neuesObjekt);
					} else {
						LOGGER.warning("Komplexobjekt: "
								+ array.getReferenceValue(idx).getSystemObject()
								+ " konnte nicht angelegt werden");
					}
				}
			}
		}
	}

	@Override
	public List<Flaeche> getFlaechen() {
		if (flaechen == null) {
			fuelleObjektListen();
		}
		return flaechen;
	}

	@Override
	public List<Komplex> getKomplexe() {
		if (komplexe == null) {
			fuelleObjektListen();
		}
		return komplexe;
	}

	@Override
	public List<Linie> getLinien() {
		if (linien == null) {
			fuelleObjektListen();
		}
		return linien;
	}

	@Override
	public List<Punkt> getPunkte() {
		if (punkte == null) {
			fuelleObjektListen();
		}
		return punkte;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return GeoModellTypen.KOMPLEX_XY;
	}
}
