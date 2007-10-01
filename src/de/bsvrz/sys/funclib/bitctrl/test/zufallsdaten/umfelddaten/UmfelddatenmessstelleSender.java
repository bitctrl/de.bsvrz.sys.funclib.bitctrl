package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenMessstelleMesswert;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfelddatenModelTypen;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.AbstractMesswertSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.Anzeige;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOption;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOptionEvent;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOptionListener;

/**
 * Generiert Messwerte f&uuml;r Umfelddatenmessstellen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UmfelddatenmessstelleSender extends AbstractMesswertSender
		implements MesswertOptionListener {

	/**
	 * Einziger Wert der hier generiert wird: die Gl&auml;tte.
	 */
	public static final String WERT = "Glätte";

	/**
	 * Liste der betrachteten Messwertoptionen.
	 */
	private MesswertOption option;

	/** Anzeige f&uuml;r den letzten Wert. */
	private Anzeige anzeige;

	/** Liste der Systemobjekte, f&uuml;r die Messwerte generiert werden. */
	private List<SystemObject> objekte;

	/** Sichert eingestellte Minimas. */
	private int min;

	/** Sichert eingestellte Maximas. */
	private int max;

	/** Datenbeschreibung des zu sendenden Datensatzes. */
	private DataDescription dbs;

	/** Der Zufallszahlengenerator. */
	private final Random datenquelle = new Random();

	/**
	 * Konstruiert den Datengenerator f&uuml;r Umfelddatenmessstellen.
	 * 
	 * @param verbindung
	 *            Datenverteilerverbindung
	 * @param objekte
	 *            Liste von Systemobjekte; es werden nur darin enthaltende
	 *            Umfelddatenmessstellen verwendet
	 */
	public UmfelddatenmessstelleSender(ClientDavInterface verbindung,
			Collection<SystemObject> objekte) {
		super(verbindung);

		DataModel modell;
		AttributeGroup atg;
		Aspect asp;

		modell = verbindung.getDataModel();

		this.objekte = new ArrayList<SystemObject>();
		for (SystemObject so : objekte) {
			if (so.isOfType(UmfelddatenModelTypen.UMFELDDATENMESSSTELLE
					.getPid())) {
				this.objekte.add(so);
			}
		}

		option = new MesswertOption("Glätte", 1, 11);
		option.addMesswertOptionListener(this);

		anzeige = new Anzeige("Glätte", "(Zustand)");

		min = option.getMin();
		max = option.getMax();

		atg = modell.getAttributeGroup(UmfelddatenMessstelleMesswert.GLAETTE
				.getAtgPID());
		asp = modell.getAspect(UmfelddatenMessstelleMesswert.GLAETTE
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
				+ " Umfelddatenmessstellen durchgeführt.");
	}

	/**
	 * {@inheritDoc}
	 */
	public List<MesswertOption> getMesswerte() {
		List<MesswertOption> liste;

		liste = new ArrayList<MesswertOption>();
		liste.add(option);
		return liste;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Anzeige> getAnzeigen() {
		List<Anzeige> liste;

		liste = new ArrayList<Anzeige>();
		liste.add(anzeige);

		return liste;
	}

	/**
	 * {@inheritDoc}
	 */
	public void maximumAktualisiert(MesswertOptionEvent e) {
		max = e.getMaximum();
		logger.info("Maximum für " + e.getMesswert() + " eingestellt", max);
	}

	/**
	 * {@inheritDoc}
	 */
	public void minimumAktualisiert(MesswertOptionEvent e) {
		min = e.getMinimum();
		logger.info("Minimum für " + e.getMesswert() + " eingestellt", min);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<SystemObject> getObjekte() {
		return new ArrayList<SystemObject>(objekte);
	}

	/**
	 * {@inheritDoc}
	 */
	public ResultData generiereDaten(SystemObject so) {
		int wert;
		int bandbreite;
		Data daten;
		long zeitstempel;

		zeitstempel = System.currentTimeMillis();

		bandbreite = max - min;
		wert = min + (int) (datenquelle.nextDouble() * bandbreite);

		daten = buildData(wert);
		anzeige.setWert(wert);

		logger.fine(so + " / Generierter Wert: " + option.getName() + "="
				+ wert);

		return new ResultData(so, dbs, zeitstempel, daten);
	}

	/**
	 * Generiert den Datensatz
	 * 
	 * @param wert
	 *            Wert der Gl&auml;tte
	 * @return Fetriger Datensatz
	 */
	private Data buildData(int wert) {
		Data data;

		data = verbindung.createData(dbs.getAttributeGroup());
		data.getUnscaledValue("AktuellerZustand").set(wert);
		data.getUnscaledValue("PrognoseZustandIn5Minuten").set(-1);
		data.getUnscaledValue("PrognoseZustandIn15Minuten").set(-1);
		data.getUnscaledValue("PrognoseZustandIn30Minuten").set(-1);
		data.getUnscaledValue("PrognoseZustandIn60Minuten").set(-1);
		data.getUnscaledValue("PrognoseZustandIn90Minuten").set(-1);

		return data;
	}

}
