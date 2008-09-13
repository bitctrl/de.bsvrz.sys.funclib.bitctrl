/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.objekte;

import java.util.Collections;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.KExTlsGlobalTypen;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class De extends AbstractSystemObjekt {

	/**
	 * Das Flag wird auf {@code true} gesetzt, wenn die Konfiguratiosndaten der
	 * ATG gelesen wurden.
	 */
	private boolean atgDe = false;

	private String bezeichnung;
	private boolean cluster;
	private int eaKanal;
	private int deKanal;
	private String umsetzungsModul;

	/**
	 * @param obj
	 */
	public De(final SystemObject obj) {
		super(obj);

		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException("Systemobjekt ist keine DE.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return KExTlsGlobalTypen.De;
	}

	/**
	 * @return the bezeichnung
	 */
	public String getBezeichnung() {
		readAtgDe();
		return bezeichnung;
	}

	/**
	 * @return the cluster
	 */
	public boolean isCluster() {
		readAtgDe();
		return cluster;
	}

	/**
	 * @return the eaKanal
	 */
	public int getEaKanal() {
		readAtgDe();
		return eaKanal;
	}

	/**
	 * @return the deKanal
	 */
	public int getDeKanal() {
		readAtgDe();
		return deKanal;
	}

	/**
	 * @return the umsetzungsModul
	 */
	public String getUmsetzungsModul() {
		readAtgDe();
		return umsetzungsModul;
	}

	private void readAtgDe() {
		if (atgDe) {
			return;
		}

		final ClientDavInterface dav = ObjektFactory.getInstanz()
				.getVerbindung();
		final DataModel modell = dav.getDataModel();
		final AttributeGroup atg = modell.getAttributeGroup("atg.de");
		final Data[] daten = modell.getConfigurationData(Collections
				.singleton(getSystemObject()), atg);
		if (daten.length >= 1) {
			bezeichnung = daten[0].getTextValue("Bezeichnung").getText();
			cluster = daten[0].getUnscaledValue("Cluster").intValue() > 0;
			eaKanal = daten[0].getUnscaledValue("EAKanal").intValue();
			deKanal = daten[0].getUnscaledValue("DEKanal").intValue();
			umsetzungsModul = daten[0].getTextValue("UmsetzungsModul")
					.getText();
		}

		atgDe = true;
	}

}
