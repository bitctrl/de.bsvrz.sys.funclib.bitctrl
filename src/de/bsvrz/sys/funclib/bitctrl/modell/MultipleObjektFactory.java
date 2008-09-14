/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.HashMap;
import java.util.Map;

/**
 * Erlaubt die Verwendung mehrerer Objektfabriken in einer Applikation. Die
 * Fabriken werden über den ihnen zugewiesenen Namen verwaltet.
 * 
 * @author BitCtrl Systems GmbH, Falko
 * @version $Id$
 */
public class MultipleObjektFactory extends ObjektFactory {

	private static MultipleObjektFactory singleton;

	/**
	 * Gibt die einzige Instanz dieser Klasse zurück.
	 * 
	 * @return das Singleton.
	 */
	public static MultipleObjektFactory getInstanz() {
		if (singleton == null) {
			singleton = new MultipleObjektFactory();
		}
		return singleton;
	}

	private final Map<String, ObjektFactory> objektFactories;

	/**
	 * Öffentlichen Konstruktor verstecken.
	 */
	protected MultipleObjektFactory() {
		objektFactories = new HashMap<String, ObjektFactory>();
	}

	/**
	 * Gibt die Objektfabrik mit dem angegebenen Namen zurück. Existiert diese
	 * noch nicht, wird sie angelegt. Eine neu angelegt Fabrik ist noch nicht
	 * initialisiert.
	 * 
	 * @param verbindungsName
	 *            der Name der gesuchten Datenverteilerverbindung.
	 * @return die Datenverteilerverbindung.
	 * @see ObjektFactory#setVerbindung(de.bsvrz.dav.daf.main.ClientDavInterface)
	 * @see ObjektFactory#registerStandardFactories()
	 * @see ObjektFactory#registerFactory(ModellObjektFactory...)
	 */
	public synchronized ObjektFactory getDav(final String verbindungsName) {
		if (!objektFactories.containsKey(verbindungsName)) {
			objektFactories.put(verbindungsName, new ObjektFactory());
		}
		return objektFactories.get(verbindungsName);
	}

}
