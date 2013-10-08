package de.bsvrz.sys.funclib.bitctrl.daf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;

import de.bsvrz.dav.daf.main.ClientDavConnection;
import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.DavConnectionListener;

/**
 * Basisimplementierung der Schnittstelle {@link DavProvider}. Kann als
 * Grundlage für eigene Implementierungen dienen oder wenn nur eine Instanz
 * (Singleton) benötigt wird.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class DefaultDavProvider implements DavProvider, DavConnectionListener {

	private static DefaultDavProvider singleton;

	/**
	 * Gibt eine Defaultverbindung als Singleton zurück. Nützlich für
	 * Applikationen, die nur mit einer Verbindung umgehen müssen.
	 * 
	 * @return eine Singletonverbindung.
	 * @see #init(ClientDavInterface)
	 */
	public static DavProvider getInstanz() {
		if (singleton == null) {
			singleton = new DefaultDavProvider("default", null);
		}
		return singleton;
	}

	/** Kann zum feuern von {@link PropertyChangeEvent}s verwendet werden. */
	protected final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	private String name;
	private ClientDavInterface dav;
	private boolean verbunden;

	/**
	 * Klasse darf nicht direkt instanziiert werden, wegen dem
	 * Singleton-Entwurfsmuster.
	 * 
	 * @param name
	 *            der Name der Verbindung.
	 * @param dav
	 *            die Verbindung.
	 */
	protected DefaultDavProvider(final String name, final ClientDavInterface dav) {
		this.name = name;
		setDav(dav);
	}

	/**
	 * Initialisert die Datenverteilerverbindung. Die Methode geht davon aus,
	 * dass die übergebene Verbindung mit dem Datenverteiler verbunden ist.
	 * 
	 * <p>
	 * <em>Hinweis:</em> Diese Methode wird nur bei der Verwendung als Singleton
	 * benötigt.<br>
	 * <em>Hinweis:</em> Diese Methode muss aufgerufen werden, bevor auf die
	 * Datenverteilerverbindung zugegriffen werden kann.
	 * 
	 * @param verbindung
	 *            die Datenverteilerverbindung.
	 * @see #getInstanz()
	 */
	public void init(final ClientDavInterface verbindung) {
		if (dav != null) {
			throw new IllegalStateException(
					"Die Datenverteilerverbindung wurde bereits initialisiert.");
		}

		setDav(verbindung);
	}

	public String getName() {
		return name;
	}

	/**
	 * Legt den Namen der Verbindung fest.
	 * 
	 * <p>
	 * Der Name darf weder <code>null</code> noch ein leerer String sein.
	 * 
	 * @param name
	 *            der neue Name der Verbindung.
	 */
	protected void setName(final String name) {
		if (name == null) {
			throw new NullPointerException("Der Name darf nicht null sein.");
		}
		if (name.length() == 0) {
			throw new IllegalArgumentException(
					"Der Name darf kein leerer String sein.");
		}

		final String oldValue = this.name;
		if (oldValue == name) {
			return;
		}

		this.name = name;
		propertyChangeSupport.firePropertyChange(PROP_NAME, oldValue, name);
	}

	public ClientDavInterface getDav() {
		return dav;
	}

	/**
	 * Legt die neue Verbindung zum Datenverteiler fest.
	 * 
	 * @param dav
	 *            die neue Datenverteilerverbindung.
	 */
	protected void setDav(final ClientDavInterface dav) {
		final ClientDavInterface oldValue = this.dav;
		if (oldValue == dav) {
			return;
		}

		if (this.dav != null) {
			this.dav.removeConnectionListener(this);
		}

		this.dav = dav;
		if (dav == null) {
			setVerbunden(false);
		} else {
			// FIXME Workaround für fehlendes ClientDavConnection.isConnected()
			if (dav instanceof ClientDavConnection) {
				final ClientDavConnection conn = (ClientDavConnection) dav;
				try {
					final Field field = ClientDavConnection.class
							.getDeclaredField("connected");
					field.setAccessible(true);
					setVerbunden((Boolean) field.get(conn));
				} catch (final SecurityException ex) {
					// Wir sind optimistisch und hoffen die Verbindung ist da
					setVerbunden(true);
				} catch (final NoSuchFieldException ex) {
					// Wir sind optimistisch und hoffen die Verbindung ist da
					setVerbunden(true);
				} catch (final IllegalArgumentException ex) {
					// Wir sind optimistisch und hoffen die Verbindung ist da
					setVerbunden(true);
				} catch (final IllegalAccessException ex) {
					// Wir sind optimistisch und hoffen die Verbindung ist da
					setVerbunden(true);
				}
			} else {
				setVerbunden(true);
			}
		}

		if (dav != null) {
			dav.addConnectionListener(this);
		}
		propertyChangeSupport.firePropertyChange(PROP_DAV, oldValue, dav);
	}

	public boolean isVerbunden() {
		return verbunden;
	}

	/**
	 * Setzt das Flag für den Verbindungszustand.
	 * 
	 * @param verbunden
	 *            der neue Wert.
	 */
	protected void setVerbunden(final boolean verbunden) {
		final boolean oldValue = this.verbunden;
		if (oldValue == verbunden) {
			return;
		}

		this.verbunden = verbunden;
		propertyChangeSupport.firePropertyChange(PROP_VERBUNDEN, oldValue,
				verbunden);
	}

	public void connectionClosed(final ClientDavInterface connection) {
		setVerbunden(false);
	}

	public void addPropertyChangeListener(final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(final String propertyName,
			final PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(
			final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(final String propertyName,
			final PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	@Override
	public String toString() {
		if (dav != null) {
			final String hostname = dav.getClientDavParameters()
					.getDavCommunicationAddress();
			final int port = dav.getClientDavParameters()
					.getDavCommunicationSubAddress();
			final String zustand = verbunden ? "verbunden" : "nicht verbunden";
			return name + " (" + hostname + ":" + port + ", " + zustand + ")";
		}

		return name;
	}

}
