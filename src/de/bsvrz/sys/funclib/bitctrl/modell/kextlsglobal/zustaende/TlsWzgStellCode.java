/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum TlsWzgStellCode implements Zustand<Short> {

	FreieDefiniton("freie Definiton", (short) 0),

	LetzteSchaltungBeibehalten("letzte Schaltung beibehalten", (short) 255);

	private final String name;
	private final short code;

	/**
	 * Liefert zu einem Code dem dazugeh�rigen Anzeigeprinzip.
	 * 
	 * @param code
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return das ermittelte Anzeigeprinzip, wenn ein ung�ltiger Code �bergeben
	 *         wurde, wird {@code null} zur�ckgegeben.
	 */
	public static TlsWzgStellCode valueOf(final short code) {
		for (final TlsWzgStellCode stellCode : values()) {
			if (stellCode.getCode() == code) {
				return stellCode;
			}
		}

		return null;
	}

	private TlsWzgStellCode(final String name, final short code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * {@inheritDoc}
	 */
	public Short getCode() {
		return code;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

}
