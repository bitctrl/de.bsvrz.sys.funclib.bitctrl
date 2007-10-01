package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.NumberValue;

/**
 * Implementation der allgemeinen Funktionen von {@link MesswertObjekt}. Die
 * Klasse kann als Basis zur Implementierung der Schnittstelle
 * {@link MesswertObjekt} dienen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class AbstractMesswertObjekt implements MesswertObjekt {

	/** Die Messwerte am Messquerschnitt. */
	private final Map<Messwert, Number> messwerte;

	/** Zeitstempel der Messwerte. */
	private long zeitstempel;

	/**
	 * Sichert die Referenz auf das erweiterte Systemobjekt.
	 */
	public AbstractMesswertObjekt() {
		messwerte = new HashMap<Messwert, Number>();
	}

	/**
	 * {@inheritDoc}
	 */
	public Messwert getMesswert(String name) {
		for (Messwert mw : getMesswerte()) {
			if (name.equals(mw.getName())) {
				return mw;
			}
		}

		throw new NoSuchElementException();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getZeitstempel() {
		return zeitstempel;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setZeitstempel(long zeitstempel) {
		this.zeitstempel = zeitstempel;
	}

	/**
	 * {@inheritDoc}
	 */
	public Number getWert(Messwert wert) {
		return messwerte.get(wert);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWert(Messwert wert, Integer zahl) {
		messwerte.put(wert, zahl);
	}

	/**
	 * Extrahiert aus einem Datensatz einen bestimmten Wert. Der Wert wird im
	 * angegebenen Datensatz in der Attributliste
	 * {@link de.bsvrz.sys.funclib.bitctrl.modell.MesswertObjekt.Messwert#getName()}
	 * im Attribut {@code Wert} erwartet.
	 * 
	 * @param datensatz
	 *            Ein Datensatz
	 * @param wert
	 *            Der zu extrahierende Wert
	 * @return Der extrahierte Messwert; {@code null}, wenn der Wert ein Status
	 *         ist
	 */
	protected Integer getWert(ResultData datensatz, Messwert wert) {
		Data daten = datensatz.getData().getItem(wert.getName());
		NumberValue w = daten.getUnscaledValue("Wert");

		if (w.isState()) {
			return null;
		}

		return w.intValue();
	}

}
