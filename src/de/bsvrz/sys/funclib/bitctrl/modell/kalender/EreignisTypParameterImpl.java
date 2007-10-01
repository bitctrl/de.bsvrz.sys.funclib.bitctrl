package de.bsvrz.sys.funclib.bitctrl.modell.kalender;

import de.bsvrz.dav.daf.main.Data;

/**
 * Implementiert die Schnittstelle.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class EreignisTypParameterImpl implements EreignisTypParameter {

	/** Die Priori&auml;t des Ereignistyps. */
	private long prioritaet;

	/**
	 * {@inheritDoc}
	 */
	public long getPrioritaet() {
		return prioritaet;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPrioritaet(long prioritaet) {
		this.prioritaet = prioritaet;
	}

	/**
	 * Setzt den inneren Zustand anhand des angegebenen Datums.
	 * 
	 * @param daten
	 *            ein g&uuml;ltiges Datum.
	 */
	public void setDaten(Data daten) {
		if (daten != null) {
			prioritaet = daten.getUnscaledValue("EreignisTypPriorität")
					.longValue();
		}
	}

}
