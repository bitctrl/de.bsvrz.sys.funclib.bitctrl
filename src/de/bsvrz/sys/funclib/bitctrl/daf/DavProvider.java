package de.bsvrz.sys.funclib.bitctrl.daf;

import java.beans.PropertyChangeListener;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Stellt eine Verbindung zum Datenverteiler zur Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface DavProvider {

	/** Name der Property, die den Namen der Verbindung hält. */
	String PROP_NAME = "name";

	/** Name der Property, die die Datenverteilerverbindung hält. */
	String PROP_DAV = "dav";

	/** Name der Property, die das Flag <em>verbunden</em> hält. */
	String PROP_VERBUNDEN = "verbunden";

	/**
	 * Gibt den Namen der Verbindung zurück.
	 * 
	 * @return der Verbindungsname.
	 */
	String getName();

	/**
	 * Gibt die Verbindung zum Datenverteiler zurück.
	 * 
	 * @return die Verbindung.
	 */
	ClientDavInterface getDav();

	/**
	 * Flag ob eine Datenverteilerverbindung besteht.
	 * 
	 * @return <code>true</code>, wenn eine aktuell eine Verbindung besteht,
	 *         sonst <code>false</code>.
	 */
	boolean isVerbunden();

	/**
	 * Registriert einen Listener auf eine Property der Klasse.
	 * 
	 * @param listener
	 *            der Listener.
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Registriert einen Listener auf eine Property der Klasse.
	 * 
	 * @param propertyName
	 *            der Name der zu beobachtenden Propertery.
	 * @param listener
	 *            der Listener.
	 */
	void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

	/**
	 * Meldet einen Listener auf eine Property der Klasse wieder ab.
	 * 
	 * @param listener
	 *            der Listener.
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);

	/**
	 * Meldet einen Listener auf eine Property der Klasse wieder ab.
	 * 
	 * @param propertyName
	 *            der Name der Property die nicht mehr beobachtet werden soll.
	 * @param listener
	 *            der Listener.
	 */
	void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

}
