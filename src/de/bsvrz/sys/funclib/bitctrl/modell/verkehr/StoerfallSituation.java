package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.sys.funclib.bitctrl.modell.Zustand;

public enum StoerfallSituation implements Zustand {
	STOERUNG("Störung", 0), KEINE_AUSSAGE("keine Aussage", 1), FREIER_VERKEHR(
			"freier Verkehr", 2), LEBHAFTER_VERKEHR("lebhafter Verkehr", 3), DICHTER_VERKEHR(
			"dichter Verkehr", 4), ZAEHER_VERKEHR("zähfließender Verkehr", 5), STOCKENDER_VERKEHR(
			"stockender Verkehr", 6), STAU("Stau", 7);

	private int code;

	private String name;

	/**
	 * @param name
	 * @param ordinal
	 */
	private StoerfallSituation(String name, int code) {
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

	/**
	 * liefert den Namen des Zustandes.
	 * 
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

	public static StoerfallSituation getSituation(int gesuchterCode) {
		for (StoerfallSituation situation : values()) {
			if (situation.getCode() == gesuchterCode) {
				return situation;
			}
		}

		throw new IllegalArgumentException("Ungültiger Situation mit Code: "
				+ gesuchterCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.name + " (" + this.code + ")"; //$NON-NLS-1$//$NON-NLS-2$
	}

}
