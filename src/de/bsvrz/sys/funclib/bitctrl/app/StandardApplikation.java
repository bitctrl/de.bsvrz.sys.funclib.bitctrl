package de.bsvrz.sys.funclib.bitctrl.app;

import de.bsvrz.sys.funclib.application.StandardApplication;

/**
 * Erweitert die Schnittstelle von Datenverteilerapplikationen um die
 * Möglichkeit sie kontrolliert zu beenden
 * 
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public interface StandardApplikation extends StandardApplication {

	/**
	 * Sollte aufgerufen werden, bevor die Applikation beendet wird
	 */
	void exit();

}
