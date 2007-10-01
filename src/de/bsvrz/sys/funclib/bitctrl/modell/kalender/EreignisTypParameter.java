package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.dav.daf.main.Data;

/**
 * Ereignistypparameter, der die Priorit&auml;t des Ereignistyps angibt.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface EreignisTypParameter {

	/**
	 * Getter der Eigenschaft "Priorit&auml;t".
	 * 
	 * @return die Priorit&auml;t als ganze Zahl gr&ouml;&szlig;er oder gleich
	 *         Null.
	 */
	long getPrioritaet();

	/**
	 * Setter der Eigenschaft "Priorit&auml;t".
	 * 
	 * @param prioritaet
	 *            die Priorit&auml;t als ganze Zahl gr&ouml;&szlig;er oder
	 *            gleich Null.
	 */
	void setPrioritaet(long prioritaet);

	/**
	 * Setzt den inneren Zustand anhand des angegebenen Datums.
	 * 
	 * @param daten
	 *            ein g&uuml;ltiges Datum.
	 */
	void setDaten(Data daten);

}
