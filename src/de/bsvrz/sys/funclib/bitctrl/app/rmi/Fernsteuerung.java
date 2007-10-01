package de.bsvrz.sys.funclib.bitctrl.app.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI-Schnittstelle zum Beenden einer entfernten Applikation.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface Fernsteuerung extends Remote {

	/**
	 * Wird per RMI aufgerufen, wenn die entfernte Applikation beendet werden
	 * soll.
	 * 
	 * @throws RemoteException
	 */
	void beenden() throws RemoteException;

}
