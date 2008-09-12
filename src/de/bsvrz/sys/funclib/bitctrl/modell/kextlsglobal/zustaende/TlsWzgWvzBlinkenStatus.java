package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Die Zust�nde des Attributes {@code att.tlsWzgWvzBlinkStatus}.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum TlsWzgWvzBlinkenStatus implements Zustand<Byte> {

	Aus("aus", (byte) 0),

	Ein("ein", (byte) 1);

	private final String name;
	private final byte anzeigePrinzip;

	private TlsWzgWvzBlinkenStatus(final String name, final byte anzeigePrinzip) {
		this.name = name;
		this.anzeigePrinzip = anzeigePrinzip;
	}

	/**
	 * Liefert zu einem Code dem dazugeh�rigen Anzeigeprinzip.
	 * 
	 * @param code
	 *            der Code f�r den ein Zustand gesucht wird.
	 * @return das ermittelte Anzeigeprinzip, wenn ein ung�ltiger Code �bergeben
	 *         wurde, wird {@code null} zur�ckgegeben.
	 */
	public static TlsWzgWvzBlinkenStatus valueOf(final byte code) {
		for (final TlsWzgWvzBlinkenStatus anzeigePrinzip : values()) {
			if (anzeigePrinzip.getCode() == code) {
				return anzeigePrinzip;
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Byte getCode() {
		return anzeigePrinzip;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}

}
