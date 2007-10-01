package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten;

import java.util.ArrayList;
import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UDSWindgeschwindigkeitMesswert;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.Anzeige;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOption;

/**
 * Generiert Messwerte f&uuml;r Windgeschwindigkeitssensoren.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UDSWindgeschwindigkeitSender extends
		AbstractUmfelddatensensorSender {

	/** Einziger Wert der hier generiert wird: die Windgeschwindig. */
	public static final String WERT = "WindGeschwindigkeitMittelWert";

	/**
	 * Konstruiert den Datengenerator f&uuml;r Umfelddatensensoren.
	 * 
	 * @param verbindung
	 *            Datenverteilerverbindung
	 * @param objekte
	 *            Liste von Systemobjekte; es werden nur darin enthaltende
	 *            Windgeschwindigkeitssensoren verwendet
	 */
	public UDSWindgeschwindigkeitSender(ClientDavInterface verbindung,
			Collection<SystemObject> objekte) {
		super(verbindung);

		DataModel modell;
		AttributeGroup atg;
		Aspect asp;

		modell = verbindung.getDataModel();

		this.objekte = new ArrayList<SystemObject>();
		for (SystemObject so : objekte) {
			if (so.isOfType(UmfelddatenModelTypen.UDS_WINDGESCHWINDIGKEIT
					.getPid())) {
				this.objekte.add(so);
			}
		}

		option = new MesswertOption("Windgeschwindigkeit", 0, 1000);
		option.addMesswertOptionListener(this);

		anzeige = new Anzeige("Windgeschwindigkeit", "m/s");
		
		min = option.getMin();
		max = option.getMax();

		atg = modell
				.getAttributeGroup(UDSWindgeschwindigkeitMesswert.WINDGESCHWINDIGKEIT
						.getAtgPID());
		asp = modell
				.getAspect(UDSWindgeschwindigkeitMesswert.WINDGESCHWINDIGKEIT
						.getAspPID());
		dbs = new DataDescription(atg, asp);

		try {
			verbindung.subscribeSender(this, this.objekte, dbs, SenderRole
					.source());
		} catch (OneSubscriptionPerSendData e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		logger.config("Anmeldung für " + this.objekte.size()
				+ " Umfelddatensensoren durchgeführt.");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return WERT;
	}

}
