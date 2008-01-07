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

public class KomplexXY extends AbstractSystemObjekt implements Komplex {

	private final static Debug LOGGER = Debug.getLogger();

	List<Punkt> punkte;
	List<Linie> linien;
	List<Flaeche> flaechen;
	List<Komplex> komplexe;

	protected KomplexXY(SystemObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	private void fuelleObjektListen() {
		flaechen = new ArrayList<Flaeche>();
		punkte = new ArrayList<Punkt>();
		linien = new ArrayList<Linie>();
		komplexe = new ArrayList<Komplex>();

		DataModel model = getSystemObject().getDataModel();
		Data daten = getSystemObject().getConfigurationData(
				model.getAttributeGroup("atg.komplexKoordinaten"));
		if (daten != null) {
			Data.Array array = daten.getArray("FlächenReferenz");
			if (array != null) {
				for (int idx = 0; idx < array.getLength(); idx++) {
					Flaeche neuesObjekt = (FlaecheXY) ObjektFactory
							.getInstanz().getModellobjekt(
									array.getReferenceValue(idx)
											.getSystemObject());
					if (neuesObjekt != null) {
						flaechen.add(neuesObjekt);
					} else {
						LOGGER.warning("Flächenobjekt: "
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
					KomplexXY neuesObjekt = (KomplexXY) ObjektFactory
							.getInstanz().getModellobjekt(
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

	public List<Flaeche> getFlaechen() {
		if (flaechen == null) {
			fuelleObjektListen();
		}
		return flaechen;
	}

	public List<Komplex> getKomplexe() {
		if (komplexe == null) {
			fuelleObjektListen();
		}
		return komplexe;
	}

	public List<Linie> getLinien() {
		if (linien == null) {
			fuelleObjektListen();
		}
		return linien;
	}

	public List<Punkt> getPunkte() {
		if (punkte == null) {
			fuelleObjektListen();
		}
		return punkte;
	}

	public SystemObjektTyp getTyp() {
		return GeoModellTypen.KOMPLEX_XY;
	}
}
