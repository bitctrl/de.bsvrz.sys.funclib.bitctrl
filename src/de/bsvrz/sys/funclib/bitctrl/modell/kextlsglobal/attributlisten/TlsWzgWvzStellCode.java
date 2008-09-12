/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.attributlisten;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.bitctrl.modell.Attributliste;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende.TlsWzgStellCode;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class TlsWzgWvzStellCode implements Attributliste {

	private TlsWzgStellCode Code;
	private TlsWzgWvzBlinken blinken;
	private TlsWzgWvzStatus status;

	/**
	 * @return
	 */
	public TlsWzgStellCode getCode() {
		return Code;
	}

	/**
	 * @param code
	 */
	public void setCode(final TlsWzgStellCode code) {
		Code = code;
	}

	/**
	 * @return the blinken
	 */
	public TlsWzgWvzBlinken getBlinken() {
		return blinken;
	}

	/**
	 * @param blinken
	 *            the blinken to set
	 */
	public void setBlinken(final TlsWzgWvzBlinken blinken) {
		this.blinken = blinken;
	}

	/**
	 * @return the status
	 */
	public TlsWzgWvzStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final TlsWzgWvzStatus status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	public void atl2Bean(final Data daten) {
		setCode(TlsWzgStellCode.valueOf(daten.getUnscaledValue("Code")
				.shortValue()));

		final TlsWzgWvzBlinken atlBlinken = new TlsWzgWvzBlinken();
		atlBlinken.atl2Bean(daten.getItem("Blinken"));
		setBlinken(atlBlinken);

		final TlsWzgWvzStatus atlStatus = new TlsWzgWvzStatus();
		atlStatus.atl2Bean(daten.getItem("Status"));
		setStatus(atlStatus);
	}

	/**
	 * {@inheritDoc}
	 */
	public void bean2Atl(final Data daten) {
		daten.getUnscaledValue("Code").set(getCode().getCode());
		getBlinken().bean2Atl(daten.getItem("Blinken"));
		getStatus().bean2Atl(daten.getItem("Status"));
	}

}
