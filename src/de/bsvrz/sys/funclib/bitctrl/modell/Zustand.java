package de.bsvrz.sys.funclib.bitctrl.modell;

/**
 * Schnittstelle f&uuml;r einen Zustand eines Datenverteilerattributs.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface Zustand {

	/**
	 * Liefert den Code des Zustandes.
	 * 
	 * @return Der Code
	 */
	int getCode();

	/** Liefert den Namen des Zustandes.
	 * 
	 * @return der Name des Zustandes.
	 */
	String getName();
	
}
