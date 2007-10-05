package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;sentiert ein Ereignis.
 * 
 * @author Schumann
 */
public class Ereignis extends AbstractSystemObjekt {

	/** Ein beschreibender Text des Ereignisses. */
	private String beschreibung;

	/** Der Typ des Ereignisses. */
	private EreignisTyp ereignisTyp;

	/**
	 * Konstruiert ein Ereignis aus einem {@code SystemObject}.
	 * 
	 * @param obj
	 *            ein {@code SystemObject}, welches ein Ereignis sein muss.
	 */
	public Ereignis(SystemObject obj) {
		super(obj);

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException(
					"Systemobjekt ist kein Ereignis.");
		}

		modell = objekt.getDataModel();
		atg = modell.getAttributeGroup("atg.ereignisEigenschaften");
		datum = objekt.getConfigurationData(atg);
		if (datum != null) {
			SystemObject so;

			beschreibung = datum.getTextValue("Ereignisbeschreibung").getText();
			so = datum.getReferenceValue("EreignisTypReferenz")
					.getSystemObject();
			ereignisTyp = (EreignisTyp) ObjektFactory.getInstanz()
					.getModellobjekt(so);
		} else {
			beschreibung = null;
			ereignisTyp = null;
		}
	}

	/**
	 * Getter der konfigurierten Ereignisbeschreibung.
	 * 
	 * @return der Beschreibungstext.
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * Getter des konfigurierten Ereignistyps.
	 * 
	 * @return der Typ dieses Ereignisses.
	 */
	public EreignisTyp getEreignisTyp() {
		return ereignisTyp;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return KalenderModellTypen.EREIGNIS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getName() + "[name=" + getSystemObject().getName()
				+ ", pid=" + getSystemObject().getPid() + ", beschreibung="
				+ beschreibung + ", ereignisTyp=" + ereignisTyp + "]";
	}

}
