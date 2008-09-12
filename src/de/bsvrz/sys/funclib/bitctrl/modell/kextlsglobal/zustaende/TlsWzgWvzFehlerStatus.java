/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum TlsWzgWvzFehlerStatus implements Zustand<Byte> {

	ok("ok", (byte) 0),

	Fehler("Fehler", (byte) 1);

	private final String name;
	private final byte code;

	/**
	 * Liefert zu einem Code den dazugeh�rigen Zustand.
	 * 
	 * @param code
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return der ermittelte Zustand, wenn ein ung�ltiger Code �bergeben wurde,
	 *         wird {@code null} zur�ckgegeben.
	 */
	public static TlsWzgWvzFehlerStatus valueOf(final byte code) {
		for (final TlsWzgWvzFehlerStatus zustand : values()) {
			if (zustand.getCode() == code) {
				return zustand;
			}
		}

		return null;
	}

	private TlsWzgWvzFehlerStatus(final String name, final byte code) {
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
