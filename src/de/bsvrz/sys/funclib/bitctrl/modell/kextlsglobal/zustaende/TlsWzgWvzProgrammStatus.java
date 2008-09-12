/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum TlsWzgWvzProgrammStatus implements Zustand<Byte> {

	abgeschlossen("abgeschlossen", (byte) 0),

	InArbeit("in Arbeit", (byte) 1);

	private final String name;
	private final byte code;

	/**
	 * Liefert zu einem Code den dazugehörigen Zustand.
	 * 
	 * @param code
	 *            der Code für den ein Zustand gesucht wird.
	 * @return der ermittelte Zustand, wenn ein ungültiger Code übergeben wurde,
	 *         wird {@code null} zurückgegeben.
	 */
	public static TlsWzgWvzProgrammStatus valueOf(final byte code) {
		for (final TlsWzgWvzProgrammStatus zustand : values()) {
			if (zustand.getCode() == code) {
				return zustand;
			}
		}

		return null;
	}

	private TlsWzgWvzProgrammStatus(final String name, final byte code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * {@inheritDoc}
	 */
	public Byte getCode() {
		return code;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

}
