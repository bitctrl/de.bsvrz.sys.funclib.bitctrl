package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.umfelddaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.AbstractMesswertSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.Anzeige;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOption;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOptionEvent;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOptionListener;

/**
 * Implementiert die Gemeinsamkeiten aller Umfeldatensensoren.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractUmfelddatensensorSender extends
		AbstractMesswertSender implements MesswertOptionListener {

	/**
	 * Liste der betrachteten Messwertoptionen.
	 */
	protected MesswertOption option;
	
	/** Anzeige f&uuml;r den letzten Wert. */
	protected Anzeige anzeige;

	/** Liste der Systemobjekte, f&uuml;r die Messwerte generiert werden. */
	protected List<SystemObject> objekte;

	/** Sichert eingestellte Minimas. */
	protected int min;

	/** Sichert eingestellte Maximas. */
	protected int max;

	/** Datenbeschreibung des zu sendenden Datensatzes. */
	protected DataDescription dbs;

	/** Der Zufallszahlengenerator. */
	private final Random datenquelle = new Random();

	/**
	 * Ruft lediglich den Superkonstruktor auf.
	 * 
	 * @param verbindung
	 *            Datenverteilerverbindung
	 */
	public AbstractUmfelddatensensorSender(ClientDavInterface verbindung) {
		super(verbindung);
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
		
		logger.fine(so + " / Generierter Wert: " + option.getName() + "=" + wert);		
		
		return new ResultData(so, dbs, zeitstempel, daten);
	}

	/**
	 * Generiert den Datensatz
	 * 
	 * @param wert
	 *            Wert der Helligkeit
	 * @return Fetriger Datensatz
	 */
	protected Data buildData(int wert) {
		Data data;

		data = verbindung.createData(dbs.getAttributeGroup());
		data.getTimeValue("T").setSeconds(getIntervall());
		data.getItem(getName()).getUnscaledValue("Wert").set(wert);
		data.getItem(getName()).getItem("Status").getItem("Erfassung")
				.getUnscaledValue("NichtErfasst").setText("Nein");
		data.getItem(getName()).getItem("Status").getItem("PlFormal")
				.getUnscaledValue("WertMax").setText("Nein");
		data.getItem(getName()).getItem("Status").getItem("PlFormal")
				.getUnscaledValue("WertMin").setText("Nein");
		data.getItem(getName()).getItem("Status").getItem("MessWertErsetzung")
				.getUnscaledValue("Implausibel").setText("Nein");
		data.getItem(getName()).getItem("Status").getItem("MessWertErsetzung")
				.getUnscaledValue("Interpoliert").setText("Nein");
		data.getItem(getName()).getItem("Güte").getUnscaledValue("Index").set(
				10);
		data.getItem(getName()).getItem("Güte").getUnscaledValue("Verfahren")
				.set(0);

		return data;
	}

	/**
	 * Gibt den Namen des Messwerts zur&uuml;ck. Muss in abgeleiteten Klassen
	 * zwingend &uuml;berschrieben werden.
	 * 
	 * @return Name des Messwerts, dessen Daten generiert werden
	 */
	protected String getName() {
		return null;
	}

}
