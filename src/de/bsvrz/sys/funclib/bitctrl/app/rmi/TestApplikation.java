package de.bsvrz.sys.funclib.bitctrl.app.rmi;

import java.rmi.RemoteException;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Beispiel f&uuml;r eine Applikation, die sich definiert beenden
 * l&auml;&szlig;t.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class TestApplikation extends BeendbareApplikation {

	/** Der innerhalb von RMI eindeutige Name der Applikation. */
	public static final String APP_NAME = "TestApp";

	/**
	 * Ruft den Superkonstruktor auf und registriert damit die Applikation bei
	 * der RMI
	 * 
	 * @throws RemoteException
	 */
	public TestApplikation() throws RemoteException {
		super(APP_NAME);
	}

	/** Sichert die Verbindung, damit sie vorm Beenden geschlossen wird. */
	private ClientDavInterface verbindung;

	/**
	 * {@inheritDoc}
	 */
	public void initialize(ClientDavInterface connection) throws Exception {
		verbindung = connection; // Verbindung merken
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unused")
	public void parseArguments(ArgumentList argumentList) throws Exception {
		// nichts
	}

	/**
	 * Schlie&szlig;t die Verbindung zum Datenverteiler und beendet dann die
	 * Applikation.
	 * <p>
	 * {@inheritDoc}
	 */
	public void beenden() {
		verbindung.disconnect(false, "Programmende.");
		System.exit(0);
	}

	/**
	 * Startet die Applikation, wenn die Datenverteilerparameter angegeben wurde
	 * oder beendet sie, wenn ein einzelener Parameter "Stop" angegeben wird.
	 * 
	 * @param args
	 *            entweder die Datenverteilerparameter oder "Stop"
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 1 && args[0].equals("Stop")) {
				beendeApplikation(APP_NAME);
			} else {
				StandardApplicationRunner.run(new TestApplikation(), args);
			}
		} catch (RemoteException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}

}
