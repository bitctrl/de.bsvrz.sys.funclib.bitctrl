package de.bsvrz.sys.funclib.bitctrl.interpreter;

import de.bsvrz.sys.funclib.bitctrl.interpreter.logik.LogikHandler;
import de.bsvrz.sys.funclib.bitctrl.interpreter.logik.LogischerWert;

public class TestInterpreter {

	public static void main(String args[]) {
		Handler h = new LogikHandler();
		
		h.validiereHandler(Operator.getOperator("nicht"), LogischerWert.WAHR);
	}
	
}
