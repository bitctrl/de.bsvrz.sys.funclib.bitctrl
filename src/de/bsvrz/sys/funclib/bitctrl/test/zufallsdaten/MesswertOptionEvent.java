package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.util.EventObject;

/**
 * Event welches die &Auml;nderung einer Option eines Messwerts signalisiert.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
@SuppressWarnings("serial")
public class MesswertOptionEvent extends EventObject {

	/**
	 * Ruft den Superkonstruktor auf.
	 * 
	 * @param source
	 *            Quelle des Events
	 */
	public MesswertOptionEvent(MesswertOption source) {
		super(source);
	}

	/**
	 * Gibt den Namen des Messwerts zur&uuml;ck, dessen Optionen sich
	 * ge&auml;ndert haben.
	 * 
	 * @return Name des Messwert
	 */
	public String getMesswert() {
		return ((MesswertOption) source).getName();
	}

	/**
	 * Gibt das aktuelle Minimum zur&uuml;ck.
	 * 
	 * @return Minimum
	 */
	public Integer getMinimum() {
		return ((MesswertOption) source).getMin();
	}

	/**
	 * Gibt das aktuelle Maximum zur&uuml;ck.
	 * 
	 * @return Maximum
	 */

	public Integer getMaximum() {
		return ((MesswertOption) source).getMax();
	}

}
