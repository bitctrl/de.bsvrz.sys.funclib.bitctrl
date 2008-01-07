package de.bsvrz.sys.funclib.bitctrl.modell.geo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.geometrie.Punkt;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

public class FlaecheXYImpl extends AbstractSystemObjekt implements FlaecheXY {

	/**
	 * die Koordinaten der Fläche.
	 */
	private List<Punkt> koordinaten;

	protected FlaecheXYImpl(SystemObject obj) {
		super(obj);
		// TODO Auto-generated constructor stub
	}

	public Collection<Punkt> getKoordinaten() {
		if (koordinaten == null) {
			koordinaten = new ArrayList<Punkt>();
			AttributeGroup atg = getSystemObject().getDataModel()
					.getAttributeGroup("atg.flächenKoordinaten");

			DataCache.cacheData(getSystemObject().getType(), atg);
			Data datum = getSystemObject().getConfigurationData(atg);
			if (datum != null) {
				Data.Array xArray = datum.getArray("x");
				Data.Array yArray = datum.getArray("y");
				if ((xArray != null) && (yArray != null)) {
					int size = Math.max(xArray.getLength(), yArray.getLength());
					for (int idx = 0; idx < size; idx++) {
						koordinaten.add(new Punkt(xArray.getItem(idx)
								.asScaledValue().doubleValue(), yArray.getItem(
								idx).asScaledValue().doubleValue()));
					}
				}
			}
		}

		return koordinaten;
	}

	public SystemObjektTyp getTyp() {
		return GeoModellTypen.FLAECHE_XY;
	}
}
