/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.attributlisten;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.bitctrl.modell.Attributliste;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende.TlsWzgWvzBlinkenStatus;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class TlsWzgWvzBlinken implements Attributliste {

	/** Konstante für "aus" bei der Dauer. */
	public static final byte DAUER_AUS = 0;

	private TlsWzgWvzBlinkenStatus status;
	private byte dauer;

	/**
	 * @return the status
	 */
	public TlsWzgWvzBlinkenStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final TlsWzgWvzBlinkenStatus status) {
		this.status = status;
	}

	/**
	 * @return the dauer
	 */
	public byte getDauer() {
		return dauer;
	}

	/**
	 * @param dauer
	 *            the dauer to set
	 */
	public void setDauer(final byte dauer) {
		this.dauer = dauer;
	}

	/**
	 * {@inheritDoc}
	 */
	public void atl2Bean(final Data daten) {
		setStatus(TlsWzgWvzBlinkenStatus.valueOf(daten.getUnscaledValue(
				"Status").byteValue()));
		setDauer(daten.getUnscaledValue("Dauer").byteValue());
	}

	/**
	 * {@inheritDoc}
	 */
	public void bean2Atl(final Data daten) {
		daten.getUnscaledValue("Status").set(getStatus().getCode());
		daten.getUnscaledValue("Dauer").set(getDauer());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TlsWzgWvzBlinken clone() {
		final TlsWzgWvzBlinken clone = new TlsWzgWvzBlinken();
		clone.dauer = dauer;
		clone.status = status;
		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s;

		s = getClass().getName() + "[";
		s += "status=" + status;
		s += ", dauer=" + dauer;
		s += "]";

		return s;
	}

}
