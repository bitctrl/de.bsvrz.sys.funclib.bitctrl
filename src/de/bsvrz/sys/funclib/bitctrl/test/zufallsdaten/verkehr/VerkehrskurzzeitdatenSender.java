package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.verkehr;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.MessQuerschnittMesswert;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.AbstractMesswertSender;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.Anzeige;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOption;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOptionEvent;
import de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten.MesswertOptionListener;
import de.bsvrz.sys.funclib.bitctrl.util.dav.Umrechung;

/**
 * Generiert Kurzzeitdaten f&uuml;r Messquerschnitte. Zur Zeit werden folgende
 * Daten generiert und versandt:
 * <ul>
 * <li>QKfz</li>
 * <li>QLkw</li>
 * <li>VPkw</li>
 * <li>VLkw</li>
 * <li>SKfz</li>
 * <li>KB</li>
 * <ul>
 * Daraus werden folgende berechnet und ebenfalls versandt:
 * <ul>
 * <li>QPkw</li>
 * <li>VKfz</li>
 * <li>QB</li>
 * <li>ALkw</li>
 * <ul>
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class VerkehrskurzzeitdatenSender extends AbstractMesswertSender
		implements MesswertOptionListener {

	/**
	 * Aufz&auml;hlung der Messwerte, die per Zufall generiert werden.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	public enum Wert {
		/** Verkehrsst&auml;rke aller Kfz. */
		QKfz,

		/** Verkehrsst&auml;rke der Lkw. */
		QLkw,

		/** Geschwindigkeit der Pkw. */
		VPkw,

		/** Geschwindigkeit der Lkw. */
		VLkw,

		/** Standardabweichung der Kfz-Geschwindigkeit. */
		SKfz,

		/** Bemessungsdichte. */
		KB;
	}

	/** Liste der Systemobjekte, f&uuml;r die Messwerte generiert werden. */
	private final List<SystemObject> objekte;

	/** Datenbeschreibung des zu sendenden Datensatzes. */
	private DataDescription dbs;

	/** Der Zufallszahlengenerator. */
	private final Random datenquelle = new Random();

	/** Sichert eingestellte Minimas. */
	private final int min[];

	/** Sichert eingestellte Maximas. */
	private final int max[];

	/** Liste der betrachteten Messwertoptionen. */
	private final MesswertOption messwerte[];

	/** Liste der generierten Messwerte. */
	private final Anzeige anzeigen[];

	/**
	 * Konstruiert den Datengenerator f&uuml;r Verkehrsdaten.
	 * 
	 * @param verbindung
	 *            Datenverteilerverbindung
	 * @param objekte
	 *            Liste von Systemobjekte; es werden nur darin enthaltende
	 *            Messquerschnitte verwendet
	 */
	public VerkehrskurzzeitdatenSender(ClientDavInterface verbindung,
			Collection<SystemObject> objekte) {
		super(verbindung);

		MesswertOption qKfz, qLkw, vPkw, vLkw, sKfz, kb;
		Anzeige anzQKfz, anzQPkw, anzQLkw;
		Anzeige anzVKfz, anzVPkw, anzVLkw;
		Anzeige anzSKfz, anzKB, anzQB, anzALkw;
		DataModel modell;
		AttributeGroup atg;
		Aspect asp;

		modell = verbindung.getDataModel();

		this.objekte = new ArrayList<SystemObject>();
		for (SystemObject so : objekte) {
			if (so.isOfType(VerkehrsModellTypen.MESSQUERSCHNITT.getPid())) {
				this.objekte.add(so);
			}
		}

		anzQKfz = new Anzeige("QKfz", "Fz/h");
		anzQPkw = new Anzeige("QPkw", "Fz/h");
		anzQLkw = new Anzeige("QLkw", "Fz/h");
		anzVKfz = new Anzeige("VKfz", "km/h");
		anzVPkw = new Anzeige("VPkw", "km/h");
		anzVLkw = new Anzeige("VLkw", "km/h");
		anzSKfz = new Anzeige("SKfz", "km/h");
		anzKB = new Anzeige("KB", "Fz/km");
		anzALkw = new Anzeige("ALkw", "%");
		anzQB = new Anzeige("QB", "PkwE/h");
		anzeigen = new Anzeige[] { anzQKfz, anzQPkw, anzQLkw, anzVKfz, anzVPkw,
				anzVLkw, anzSKfz, anzALkw, anzKB, anzQB };

		qKfz = new MesswertOption("QKfz", 0, 10000);
		qKfz.addMesswertOptionListener(this);
		qLkw = new MesswertOption("QLkw", 0, 10000);
		qLkw.addMesswertOptionListener(this);
		vPkw = new MesswertOption("VPkw", 0, 254);
		vPkw.addMesswertOptionListener(this);
		vLkw = new MesswertOption("VLkw", 0, 254);
		vLkw.addMesswertOptionListener(this);
		sKfz = new MesswertOption("SKfz", 0, 254);
		sKfz.addMesswertOptionListener(this);
		kb = new MesswertOption("KB", 0, 1000);
		kb.addMesswertOptionListener(this);
		messwerte = new MesswertOption[] { qKfz, qLkw, vPkw, vLkw, sKfz, kb };

		min = new int[messwerte.length];
		max = new int[messwerte.length];
		for (Wert mw : Wert.values()) {
			min[mw.ordinal()] = messwerte[mw.ordinal()].getMin();
			max[mw.ordinal()] = messwerte[mw.ordinal()].getMax();
		}

		atg = modell
				.getAttributeGroup(MessQuerschnittMesswert.ALKW.getAtgPID());
		asp = modell.getAspect(MessQuerschnittMesswert.ALKW.getAspPID());
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
				+ " Messquerschnitte durchgeführt.");
	}

	/**
	 * {@inheritDoc}
	 */
	public List<MesswertOption> getMesswerte() {
		return Arrays.asList(messwerte);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Anzeige> getAnzeigen() {
		return Arrays.asList(anzeigen);
	}

	/**
	 * {@inheritDoc}
	 */
	public void maximumAktualisiert(MesswertOptionEvent e) {
		Wert wert;

		wert = Wert.valueOf(e.getMesswert());
		max[wert.ordinal()] = e.getMaximum();
		logger.info("Maximum für " + e.getMesswert() + " eingestellt", e
				.getMaximum());
	}

	/**
	 * {@inheritDoc}
	 */
	public void minimumAktualisiert(MesswertOptionEvent e) {
		Wert wert;

		wert = Wert.valueOf(e.getMesswert());
		min[wert.ordinal()] = e.getMinimum();
		logger.info("Minimum für " + e.getMesswert() + " eingestellt", e
				.getMinimum());
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
		int qKfz, qLkw, vPkw, vLkw, sKfz, kb;
		int bandbreite;
		Data daten;
		long zeitstempel;

		zeitstempel = System.currentTimeMillis();

		bandbreite = max[Wert.QKfz.ordinal()] - min[Wert.QKfz.ordinal()];
		qKfz = min[Wert.QKfz.ordinal()]
				+ (int) (datenquelle.nextDouble() * bandbreite);

		bandbreite = Math.min(qKfz, max[Wert.QLkw.ordinal()])
				- min[Wert.QLkw.ordinal()];
		qLkw = min[Wert.QLkw.ordinal()]
				+ (int) (datenquelle.nextDouble() * bandbreite);

		bandbreite = max[Wert.VPkw.ordinal()] - min[Wert.VPkw.ordinal()];
		vPkw = min[Wert.VPkw.ordinal()]
				+ (int) (datenquelle.nextDouble() * bandbreite);

		bandbreite = max[Wert.VLkw.ordinal()] - min[Wert.VLkw.ordinal()];
		vLkw = min[Wert.VLkw.ordinal()]
				+ (int) (datenquelle.nextDouble() * bandbreite);

		bandbreite = max[Wert.SKfz.ordinal()] - min[Wert.SKfz.ordinal()];
		sKfz = min[Wert.SKfz.ordinal()]
				+ (int) (datenquelle.nextDouble() * bandbreite);

		bandbreite = max[Wert.KB.ordinal()] - min[Wert.KB.ordinal()];
		kb = min[Wert.KB.ordinal()]
				+ (int) (datenquelle.nextDouble() * bandbreite);

		daten = buildData(so, qKfz, qLkw, vPkw, vLkw, sKfz, kb);
		return new ResultData(so, dbs, zeitstempel, daten);
	}

	/**
	 * Generiert den Datensatz und berechnet abh&auml;ngige Werte.
	 * 
	 * @param so
	 *            Ein Systemobjekt
	 * @param qKfz
	 *            Zufallswert von QKfz
	 * @param qLkw
	 *            Zufallswert von QLkw
	 * @param vPkw
	 *            Zufallswert von VPkw
	 * @param vLkw
	 *            Zufallswert von VLkw
	 * @param sKfz
	 *            Zufallswert von SKfz
	 * @param kb
	 *            Zufallswert von KB
	 * @return Der generierte Datensatz
	 */
	private Data buildData(SystemObject so, int qKfz, int qLkw, int vPkw,
			int vLkw, int sKfz, int kb) {
		Data data;
		Integer qPkw, vKfz, qb, aLkw;

		logger.fine(so + " / Eingangswerte: QKfz=" + qKfz + ", QLkw=" + qLkw
				+ ", VPkw=" + vPkw + ", VLkw=" + vLkw + ", SKfz=" + sKfz
				+ ", KB=" + kb);

		data = verbindung.createData(dbs.getAttributeGroup());

		final String[] valStrings = { "QKfz", "VKfz", "QLkw", "VLkw", "QPkw",
				"VPkw", "B", "SKfz", "BMax", "VgKfz", "ALkw", "KKfz", "KPkw",
				"KLkw", "QB", "KB", "VDelta" };

		for (int idx = 0; idx < valStrings.length; idx++) {
			data.getItem(valStrings[idx]).getUnscaledValue("Wert").setText(
					"nicht ermittelbar");
			data.getItem(valStrings[idx]).getItem("Status")
					.getItem("Erfassung").getUnscaledValue("NichtErfasst")
					.setText("Nein");
			data.getItem(valStrings[idx]).getItem("Status").getItem("PlFormal")
					.getUnscaledValue("WertMax").setText("Nein");
			data.getItem(valStrings[idx]).getItem("Status").getItem("PlFormal")
					.getUnscaledValue("WertMin").setText("Nein");
			data.getItem(valStrings[idx]).getItem("Status")
					.getItem("PlLogisch").getUnscaledValue("WertMaxLogisch")
					.setText("Nein");
			data.getItem(valStrings[idx]).getItem("Status")
					.getItem("PlLogisch").getUnscaledValue("WertMinLogisch")
					.setText("Nein");
			data.getItem(valStrings[idx]).getItem("Status").getItem(
					"MessWertErsetzung").getUnscaledValue("Implausibel")
					.setText("Nein");
			data.getItem(valStrings[idx]).getItem("Status").getItem(
					"MessWertErsetzung").getUnscaledValue("Interpoliert")
					.setText("Nein");
			data.getItem(valStrings[idx]).getItem("Güte").getUnscaledValue(
					"Index").set(-1);
			data.getItem(valStrings[idx]).getItem("Güte").getUnscaledValue(
					"Verfahren").set(0);
		}

		data.getItem("QKfz").getUnscaledValue("Wert").set(qKfz);
		data.getItem("QKfz").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[0].setWert(qKfz);
		
		data.getItem("QLkw").getUnscaledValue("Wert").set(qLkw);
		data.getItem("QLkw").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[2].setWert(qLkw);
		
		data.getItem("VPkw").getUnscaledValue("Wert").set(vPkw);
		data.getItem("VPkw").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[4].setWert(vPkw);
		
		data.getItem("VLkw").getUnscaledValue("Wert").set(vLkw);
		data.getItem("VLkw").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[5].setWert(vLkw);
		
		data.getItem("SKfz").getUnscaledValue("Wert").set(sKfz);
		data.getItem("SKfz").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[6].setWert(sKfz);
		
		data.getItem("KB").getUnscaledValue("Wert").set(kb);
		data.getItem("KB").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[8].setWert(kb);
		
		// Nicht erfasste Werte berechnen

		aLkw = Umrechung.getALkw(qLkw, qKfz);
		data.getItem("ALkw").getUnscaledValue("Wert").set(aLkw);
		data.getItem("ALkw").getItem("Güte").getUnscaledValue("Index").set(10);
		anzeigen[7].setWert(aLkw);
		
		qPkw = Umrechung.getQPkw(qKfz, qLkw);
		if (qPkw != null) {
			data.getItem("QPkw").getUnscaledValue("Wert").set(qPkw);
			data.getItem("QPkw").getItem("Güte").getUnscaledValue("Index").set(
					10);
			data.getItem("QPkw").getItem("Status").getItem("Erfassung")
					.getUnscaledValue("NichtErfasst").setText("Ja");

		}
		anzeigen[1].setWert(qPkw);

		vKfz = Umrechung.getVKfz(qLkw, qKfz, vPkw, vLkw);
		if (vKfz != null) {
			data.getItem("VKfz").getUnscaledValue("Wert").set(vKfz);
			data.getItem("VKfz").getItem("Güte").getUnscaledValue("Index").set(
					10);
			data.getItem("VKfz").getItem("Status").getItem("Erfassung")
					.getUnscaledValue("NichtErfasst").setText("Ja");
		}
		anzeigen[3].setWert(vKfz);

		qb = Umrechung.getQB(qLkw, qKfz, vPkw, vLkw, 0.5f, 1);
		if (qb != null) {
			data.getItem("QB").getUnscaledValue("Wert").set(qb);
			data.getItem("QB").getItem("Güte").getUnscaledValue("Index")
					.set(10);
			data.getItem("QB").getItem("Status").getItem("Erfassung")
					.getUnscaledValue("NichtErfasst").setText("Ja");
		}
		anzeigen[9].setWert(qb);

		logger.fine(so + " / Berechnete Werte: QPkw=" + qPkw + ", VKfz=" + vKfz
				+ ", QB=" + qb + ", ALkw=" + aLkw);

		return data;
	}

}
