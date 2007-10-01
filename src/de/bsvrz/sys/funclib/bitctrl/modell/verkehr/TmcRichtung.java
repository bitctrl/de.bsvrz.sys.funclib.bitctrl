package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum TmcRichtung implements Zustand {
	UNDEFINIERT("Undefiniert", Integer.MAX_VALUE), POSITIV("positiv", 1), OHNE(
			"ohne Richtung", 0), NEGATIV("negativ", -1);

	private int code;

	private String name;

	/**
	 * @param name
	 * @param ordinal
	 */
	private TmcRichtung(String name, int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * liefert den Code des Zustandes.
	 * 
	 * @return den Code.
	 */
	public int getCode() {
		return code;
	}

	public static TmcRichtung getTyp(int gesuchterCode) {
		for (TmcRichtung typ : values()) {
			if (typ.getCode() == gesuchterCode) {
				return typ;
			}
		}

		throw new IllegalArgumentException("Ungültiger Typ mit Code: "
				+ gesuchterCode);
	}

	/**
	 * 
	 * {@inheritDoc}
	 *
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Zustand#getName()
	 */
	public String getName() {
		return name;
	}
	
}
