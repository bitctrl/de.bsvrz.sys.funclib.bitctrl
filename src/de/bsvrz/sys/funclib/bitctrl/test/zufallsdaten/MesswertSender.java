package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import java.util.Collection;
import java.util.List;

import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Schnittstelle f&uuml;r alle Sender von Testdaten.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface MesswertSender {

	/**
	 * Legt das Intervall fest, in dem Messwerte generiert werden sollen.
	 * 
	 * @param intervall
	 *            Intervalldauer in Sekunden
	 */
	void setTimer(int intervall);

	/**
	 * Gibt das aktuelle verwendete Intervall zur&uuml;ck in dem Messwerte
	 * generiert werden.
	 * 
	 * @return Intervall in Sekunden
	 */
	int getIntervall();

	/**
	 * Gibt die Messwerte zur&uuml;ck, die generiert werden.
	 * 
	 * @return Liste von Messwertoptionen
	 */
	List<MesswertOption> getMesswerte();

	/**
	 * Gibt eine Liste von {@link Anzeige} zur&uuml;ck, welche die generierten
	 * Messwerte anzeigen.
	 * 
	 * @return Liste von Anzeigeelementen
	 */
	List<Anzeige> getAnzeigen();

	/**
	 * Generiert einen Datensatz f&uuml;r das angegebene Systemobjekt.
	 * 
	 * @param so
	 *            Ein Systemobjekt
	 * @return Der generierte Datensatz
	 */
	ResultData generiereDaten(SystemObject so);

	/**
	 * Gibt die Liste der Systemobjekte zur&uuml;ck, f&uuml;r die Messwerte
	 * generiert werden.
	 * 
	 * @return Liste von Systemobjekten
	 */
	Collection<SystemObject> getObjekte();

}
