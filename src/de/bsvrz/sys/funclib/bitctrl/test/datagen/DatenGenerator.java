/**
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Wei&szlig;enfelser Stra&szlig;e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.test.datagen;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList.Argument;

/**
 * Werkzeug zum generieren von Testdaten für beliebige Datensätze.<br>
 * Das Programm generiert eine XML-Datei, die mit dem Standard-Datengenerator
 * der Datenverteiler-Software eingesetzt werden kann.<br>
 * Für jeden zu verwendenden Datensatztyp wird eine Datei erstellt, die in drei
 * Teilen die zu sendenden Daten beschreibt:
 * <ol>
 * <li>Der Konfigurationsbereich enthält die Informationen zu den Objekten, für
 * die Daten versendet werden sollen, die Attributgruppe und der Aspekt für die
 * Daten und die Art der Anmeldung der Datenquelle (quelle, sender)</li>
 * <li>Der Standardwertbereich enthält die Werte, die beim Datenversand
 * konstant sind.</li>
 * <li>Der Datenbereich enthält im CSV-Format die Daten für die Attribute, die
 * abweichend zu den Standardwerten im jeweiligen Datensatz gesetzt werden
 * sollen. Die erste Spalte enthält immer die Zeit des Datensatzes. Der
 * Zeitstempel wird relativ in Sekunden angegeben.</li>
 * </ol>
 * <br>
 * Das Programm kann in zwei Betriebsarten ausgeführt werden, die durch den
 * Aufrufparameter <i>-modus</i> festgelegt werden:
 * <ul>
 * <li>CONFIG: erzeugt eine Vorlagedatei für alle verfügbaren Attribugruppen,
 * Aspekt-Kombinationen. Die Ausgabe der Vorlagedateien erfolgt in das
 * Verzeichnis "dataTemplates". Der Name des Ausgabeverzeichnisses kann über den
 * Aufrufparameter "-verzeichnis" verändert werden.</li>
 * <li>CREATE: erzeugt aus einer Liste von Konfigurationsdateien die XML-Datei
 * für den Datengenerator der Kernsoftware. Mit dem Parameter <b>-input</b>
 * wird eine kommagetrennte Liste von Eingabedateien angegeben. Die Ausgabe
 * erfolgt in die mit <b>-output</b> angegebene Datei oder auf die Konsole. Die
 * Angabe der Startzeit kann mit der Option <b><i>-start</i></b> erfolgen</li>
 * </ul>
 * 
 * @author peuker
 * 
 */
public class DatenGenerator implements StandardApplication {

	/**
	 * die Argumente für die Datengeneratormodule.
	 */
	private final Map<String, String> argumente = new HashMap<String, String>();

	/**
	 * der String zur Beschreibung des Ausführungsmodus.
	 */
	private String modStr;

	/**
	 * {@inheritDoc}.
	 * 
	 * @see StandardApplication#initialize(ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {
		DatenGeneratorModul modul = DatenGeneratorFactory.getModul(connection,
				modStr, argumente);
		System.exit(modul.ausfuehren());
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see StandardApplication#parseArguments(ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		if (!argumentList.hasArgument("-modus")) {
			throw new IllegalArgumentException();
		}

		modStr = argumentList.fetchArgument("-modus=").asNonEmptyString();
		while (argumentList.hasUnusedArguments()) {
			Argument argument = argumentList.fetchNextArgument();
			argumente.put(argument.getName(), argument.getValue());
		}

	}

	/**
	 * die Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente.
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new DatenGenerator(), args);
	}
}
