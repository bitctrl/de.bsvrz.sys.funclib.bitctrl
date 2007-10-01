package de.bsvrz.sys.funclib.bitctrl.modell;

/**
 * Schnittstelle f&uuml;r den Umgang mit Systemobjekttypen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface SystemObjektTyp {

	/**
	 * Gibt die PID des Typs zur&uuml;ck.
	 * 
	 * @return Die PID
	 */
	String getPid();

	/**
	 * Gibt die Klasse des Objekts zu dem Typ zur&uuml;ck.
	 * 
	 * @return Die Klasse
	 */
	Class<? extends SystemObjekt> getKlasse();

}
