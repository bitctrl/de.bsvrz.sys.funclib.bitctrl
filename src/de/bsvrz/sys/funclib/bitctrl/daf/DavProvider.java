package de.bsvrz.sys.funclib.bitctrl.daf;

import java.beans.PropertyChangeListener;

import de.bsvrz.dav.daf.main.ClientDavInterface;

/**
 * Stellt eine Verbindung zum Datenverteiler zur Verf�gung.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public interface DavProvider {

	/** Name der Property, die den Namen der Verbindung h�lt. */
	String PROP_NAME = "name";

	/** Name der Property, die die Datenverteilerverbindung h�lt. */
	String PROP_DAV = "dav";

	/** Name der Property, die das Flag <em>verbunden</em> h�lt. */
	String PROP_VERBUNDEN = "verbunden";

	/**
	 * Gibt den Namen der Verbindung zur�ck.
	 * 
	 * @return der Verbindungsname.
	 */
	String getName();

	/**
	 * Gibt die Verbindung zum Datenverteiler zur�ck.
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
