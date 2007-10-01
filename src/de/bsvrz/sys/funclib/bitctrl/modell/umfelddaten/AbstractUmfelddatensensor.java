package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.Konfigurationsbereich;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;

/**
 * Implementtiert die gemeinsame Logik von Umfelddatensensoren. 
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractUmfelddatensensor extends AbstractSystemObjekt
		implements UmfelddatenSensor {

	/**
	 * Ruft den Superkonstruktor auf.
	 * 
	 * @param obj
	 *            Ein Systemobjekt, was ein Umfelddatensensor darstellt
	 */
	public AbstractUmfelddatensensor(SystemObject obj) {
		super(obj);
	}

	/**
	 * TODO: Ergebnis zwischenspeichern, da konfigurierende Daten 
	 * <p>
	 * {@inheritDoc}
	 */
	public List<UmfelddatenMessstelle> getUmfelddatenMessstellen() {
		List<UmfelddatenMessstelle> listeUDMS;
		List<SystemObject> listeSO;

		listeUDMS = new ArrayList<UmfelddatenMessstelle>();
		listeSO = Konfigurationsbereich.getObjekte(objekt
				.getConfigurationArea(),
				UmfelddatenModelTypen.UMFELDDATENMESSSTELLE.getPid());

		for (SystemObject so : listeSO) {
			UmfelddatenMessstelle udms = (UmfelddatenMessstelle) ObjektFactory
					.getModellobjekt(so);
			if (udms.besitzt(this)) {
				listeUDMS.add(udms);
			}
		}

		return listeUDMS;
	}

}