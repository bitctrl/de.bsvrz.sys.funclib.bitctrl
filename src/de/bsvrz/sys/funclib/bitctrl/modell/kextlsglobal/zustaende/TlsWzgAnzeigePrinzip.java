package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

/**
 * Die Zustände des Attributes {@code att.tlsWzgAnzeigePrinzip}.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public enum TlsWzgAnzeigePrinzip implements Zustand<Byte> {

	a("a", (byte) 0),

	b("b", (byte) 1),

	c("c", (byte) 2),

	d("d", (byte) 3),

	e("e", (byte) 4),

	Cluster("Cluster", (byte) 8);

	private final String name;
	private final byte anzeigePrinzip;

	private TlsWzgAnzeigePrinzip(final String name, final byte anzeigePrinzip) {
		this.name = name;
		this.anzeigePrinzip = anzeigePrinzip;
	}

	/**
	 * Liefert zu einem Code dem dazugehörigen Anzeigeprinzip.
	 * 
	 * @param code
	 *            der Code für den ein Zustand gesucht wird.
	 * @return das ermittelte Anzeigeprinzip, wenn ein ungültiger Code übergeben
	 *         wurde, wird {@code null} zurückgegeben.
	 */
	public static TlsWzgAnzeigePrinzip valueOf(final byte code) {
		for (final TlsWzgAnzeigePrinzip anzeigePrinzip : values()) {
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
