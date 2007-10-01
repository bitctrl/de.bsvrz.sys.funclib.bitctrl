package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum StrassenKnotenTyp implements Zustand {
	SONSTIG("SonstigerKnoten", 0),
	AUTOBAHNKREUZ("AutobahnKreuz", 1),
	AUTOBAHNDREIECK("AutobahnDreieck", 2),
	AUTOBAHNANSCHLUSS("AutobahnAnschlussStelle", 3),
	AUTOBAHNENDE("AutobahnEnde", 4);

	private int code;

	private String name;

	/**
	 * @param name
	 * @param ordinal
	 */
	private StrassenKnotenTyp(String name, int code) {
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

	public static StrassenKnotenTyp getTyp(int gesuchterCode) {
		for (StrassenKnotenTyp typ : values()) {
			if ( typ.getCode() == gesuchterCode) {
				return typ;
			}
		}

		throw new IllegalArgumentException("Ungültiger Typ mit Code: " + gesuchterCode);
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
