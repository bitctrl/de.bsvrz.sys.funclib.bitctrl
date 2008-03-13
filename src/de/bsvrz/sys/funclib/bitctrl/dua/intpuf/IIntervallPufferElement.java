package de.bsvrz.sys.funclib.bitctrl.dua.intpuf;

public interface IIntervallPufferElement<T extends IIntervallDatum<T>>{

	public long getIntervallStart();
	
	public long getIntervallEnde();
	
	public T getInhalt();
	
}
