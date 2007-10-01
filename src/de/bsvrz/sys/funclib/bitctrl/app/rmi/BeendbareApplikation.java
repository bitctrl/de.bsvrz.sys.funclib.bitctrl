package de.bsvrz.sys.funclib.bitctrl.app.rmi;

import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

import de.bsvrz.sys.funclib.application.StandardApplication;

/**
 * Abstrakte Klasse einer Datenverteilerapplikation, die sich beenden
 * l&auml;&szlig;t.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public abstract class BeendbareApplikation implements StandardApplication,
		Fernsteuerung {

	/**
	 * Registriert die Applikation unter dem angegebenen Namen. Der Name muss in
	 * der RMI-Registry eindeutig sein.
	 * 
	 * @param name
	 *            der Name unter der die Applikation per RMI angesprochen werden
	 *            soll.
	 * @throws RemoteException
	 */
	public BeendbareApplikation(String name) throws RemoteException {
		Fernsteuerung stub;
		Registry registry;

		RemoteServer.setLog(System.out);
		stub = (Fernsteuerung) UnicastRemoteObject.exportObject(this, 0);
		registry = LocateRegistry.getRegistry();
		registry.rebind(name, stub);
	}

	/**
	 * Wird aufgerufen, um die Applikation zum Beenden aufzufordern.
	 * 
	 * @param name
	 *            der Name unter der die Applikation per RMI angesprochen werden
	 *            soll.
	 * @throws RemoteException
	 */
	public static void beendeApplikation(String name) throws RemoteException {
		Registry registry;
		Fernsteuerung f;

		registry = LocateRegistry.getRegistry();
		try {
			f = (Fernsteuerung) registry.lookup(name);
		} catch (NotBoundException e) {
			throw new IllegalStateException();
		}
		try {
			f.beenden();
		} catch (UnmarshalException ex) {
			if (ex.getCause() instanceof SocketException) {
				System.out.println("Applikation wurde beendet.");
			}
		}
	}

}
