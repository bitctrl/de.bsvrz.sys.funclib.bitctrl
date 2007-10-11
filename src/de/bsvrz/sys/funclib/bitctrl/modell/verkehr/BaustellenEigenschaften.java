package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

public class BaustellenEigenschaften extends AbstractParameterDatensatz {

	private static AttributeGroup attributGruppe;

	/**
	 * Zustand der Baustelle. ("Status")
	 */
	private BaustellenStatus status;

	/**
	 * Restkapazit�t w�hrend der G�ltigkeitsdauer der Baustelle.
	 * ("RestKapazit�t")
	 */
	private long restKapazitaet;

	/**
	 * Veranlasser der Baustelle (BIS-System oder VRZ). ("Veranlasser")
	 */
	private BaustellenVeranlasser veranlasser;

	public BaustellenEigenschaften(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenEigenschaften");
		}
	}

	public void abmeldenSender() {
		// TODO Auto-generated method stub

	}

	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
	}

	public long getRestKapazitaet() {
		return restKapazitaet;
	}

	public BaustellenStatus getStatus() {
		return status;
	}

	public BaustellenVeranlasser getVeranlasser() {
		return veranlasser;
	}

	public void sendeDaten() {
		// TODO Auto-generated method stub

	}

	public void setDaten(Data daten) {
		if (daten != null) {
			status = BaustellenStatus.getStatus(daten
					.getUnscaledValue("Status").intValue());
			restKapazitaet = daten.getScaledValue("RestKapazit�t").longValue();
			veranlasser = BaustellenVeranlasser.getVeranlasser(daten
					.getUnscaledValue("Veranlasser").intValue());
		}
	}
}
