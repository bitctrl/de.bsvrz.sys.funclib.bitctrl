package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.util.Timer;
import java.util.TimerTask;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Implementiert gemeinsame Funktionen aller Sender von Testdaten.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractMesswertSender implements ClientSenderInterface,
		MesswertSender {

	/**
	 * Eine Zeitschaltuhr, die die Datengenerierung zyklisch startet.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	protected class Zeitschaltuhr extends TimerTask {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			for (SystemObject so : getObjekte()) {
				try {
					verbindung.sendData(generiereDaten(so));
				} catch (DataNotSubscribedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1);
				} catch (SendSubscriptionNotConfirmed e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1);
				}
				logger.info(so + " / Daten gesendet.");
			}

		}

	}

	/** Debug-Logger. */
	protected final Debug logger;

	/** Datenverteilerverbindung. */
	protected ClientDavInterface verbindung;

	/** Timer der die zyklische Testdatengenerierung steuert. */
	private Timer timer;

	/** Aktuelles Intervall in Sekunden. */
	private int intervall;

	/**
	 * Sichert die Verbindung zum Datenverteiler und initialisiert den
	 * Debug-Logger.
	 * 
	 * @param verbindung
	 *            Datenverteilerverbindung
	 */
	public AbstractMesswertSender(ClientDavInterface verbindung) {
		this.verbindung = verbindung;
		logger = Debug.getLogger();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
	public void dataRequest(SystemObject object,
			DataDescription dataDescription, byte state) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTimer(int intervall) {
		// Alten Timer stoppen, wenn bereits einer läuft
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}

		this.intervall = intervall;
		timer = new Timer(false);
		timer.scheduleAtFixedRate(new Zeitschaltuhr(), 0, intervall * 1000);

		logger.info("Neues Intervall eingestellt:" + intervall);
	}

	/**
	 * {@inheritDoc}
	 */
	public int getIntervall() {
		return intervall;
	}

}
