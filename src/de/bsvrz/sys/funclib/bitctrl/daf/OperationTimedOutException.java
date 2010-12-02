package de.bsvrz.sys.funclib.bitctrl.daf;

/**
 * eine Operation ist nicht in einer vorgegebenen Zeit abgeschlossen worden.
 */
@SuppressWarnings("serial")
public class OperationTimedOutException extends Exception {

	/**
	 * Standardkonstruktor.
	 */
	public OperationTimedOutException() {
		super(OperationTimedOutException.class.getName());
	}

}
