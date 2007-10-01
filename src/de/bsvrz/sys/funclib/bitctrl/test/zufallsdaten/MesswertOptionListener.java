package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.util.EventListener;

/**
 * Listener der &Auml;nderungen an Optionen von Messwerten &uuml;berwacht.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface MesswertOptionListener extends EventListener {

	/**
	 * Signalisiert eine &Auml;nderung des Minimum des Messwerts.
	 * 
	 * @param e
	 *            Das Event
	 */
	void minimumAktualisiert(MesswertOptionEvent e);

	/**
	 * Signalisiert eine &Auml;nderung des Maximum des Messwerts.
	 * 
	 * @param e
	 *            Das Event
	 */
	void maximumAktualisiert(MesswertOptionEvent e);

}
