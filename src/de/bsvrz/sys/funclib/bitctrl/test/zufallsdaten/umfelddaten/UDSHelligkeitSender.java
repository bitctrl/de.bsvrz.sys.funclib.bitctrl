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
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UDSHelligkeitMesswert;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.Anzeige;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOption;

/**
 * Generiert Messwerte f&uuml;r Helligkeitsensoren.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UDSHelligkeitSender extends AbstractUmfelddatensensorSender {

	/** Einziger Wert der hier generiert wird: die Helligkeit. */
	public static final String WERT = "Helligkeit";
	
	/**
	 * Konstruiert den Datengenerator f&uuml;r Umfelddatensensoren.
	 * 
	 * @param verbindung
	 *            Datenverteilerverbindung
	 * @param objekte
	 *            Liste von Systemobjekte; es werden nur darin enthaltende
	 *            Helligkeitssensoren verwendet
	 */
	public UDSHelligkeitSender(ClientDavInterface verbindung,
			Collection<SystemObject> objekte) {
		super(verbindung);

		DataModel modell;
		AttributeGroup atg;
		Aspect asp;

		modell = verbindung.getDataModel();

		this.objekte = new ArrayList<SystemObject>();
		for (SystemObject so : objekte) {
			if (so.isOfType(UmfelddatenModelTypen.UDS_HELLIGKEIT.getPid())) {
				this.objekte.add(so);
			}
		}

		option = new MesswertOption("Helligkeit", 0, 60000);
		option.addMesswertOptionListener(this);

		anzeige = new Anzeige("Helligkeit", "Lx");
		
		min = option.getMin();
		max = option.getMax();

		atg = modell.getAttributeGroup(UDSHelligkeitMesswert.HELLIGKEIT
				.getAtgPID());
		asp = modell.getAspect(UDSHelligkeitMesswert.HELLIGKEIT.getAspPID());
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
