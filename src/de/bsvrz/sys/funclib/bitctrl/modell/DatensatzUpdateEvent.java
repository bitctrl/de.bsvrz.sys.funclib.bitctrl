package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.EventObject;

public class DatensatzUpdateEvent extends EventObject {

	private final Datensatz datensatz;

	public DatensatzUpdateEvent(Object source, Datensatz datensatz) {
		super(source);
		this.datensatz = datensatz;
	}

	public Datensatz getDatensatz() {
		return datensatz;
	}
}
