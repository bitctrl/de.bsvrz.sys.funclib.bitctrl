/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.attributlisten;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.bitctrl.modell.Attributliste;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende.TlsWzgWvzFehlerStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende.TlsWzgWvzProgrammStatus;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class TlsWzgWvzStatus implements Attributliste {

	private TlsWzgWvzFehlerStatus fehler;
	private TlsWzgWvzProgrammStatus programm;

	/**
	 * @return the fehler
	 */
	public TlsWzgWvzFehlerStatus getFehler() {
		return fehler;
	}

	/**
	 * @param fehler
	 *            the fehler to set
	 */
	public void setFehler(final TlsWzgWvzFehlerStatus fehler) {
		this.fehler = fehler;
	}

	/**
	 * @return the programm
	 */
	public TlsWzgWvzProgrammStatus getProgramm() {
		return programm;
	}

	/**
	 * @param programm
	 *            the programm to set
	 */
	public void setProgramm(final TlsWzgWvzProgrammStatus programm) {
		this.programm = programm;
	}

	/**
	 * {@inheritDoc}
	 */
	public void atl2Bean(final Data daten) {
		setFehler(TlsWzgWvzFehlerStatus.valueOf(daten
				.getUnscaledValue("Fehler").byteValue()));
		setProgramm(TlsWzgWvzProgrammStatus.valueOf(daten.getUnscaledValue(
				"Programm").byteValue()));
	}

	/**
	 * {@inheritDoc}
	 */
	public void bean2Atl(final Data daten) {
		daten.getUnscaledValue("Fehler").set(getFehler().getCode());
		daten.getUnscaledValue("Programm").set(getProgramm().getCode());
	}

}
