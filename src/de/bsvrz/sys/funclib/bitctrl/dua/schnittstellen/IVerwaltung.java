/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.schnittstellen;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.bitctrl.dua.dfs.typen.SWETyp;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Abstrakte Implementation einer Schnittstelle zu einem Verwaltungsmodul. Ein
 * Verwaltungsmodul stellt immer den Eintrittspunkt in eine SWE 4.x dar und 
 * implementiert daher das Interface <code>StandardApplication</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public interface IVerwaltung
extends StandardApplication, ClientReceiverInterface{

	/**
	 * Erfragt die Verbindung zum Datenverteiler.
	 * 
	 * @return die Verbindung zum Datenverteiler
	 */
	public ClientDavInterface getVerbindung();
	
	/**
	 * Sendet eine Betriebsmeldung an die Betriebsmeldungsverwaltung
	 * 
	 * @param id ID der Meldung. Dieses Attribut kann von der Applikation
	 * gesetzt werden, um einen Bezug zu einer vorherigen Meldung herzustellen.
	 * @param typ Typ der Betriebsmeldung (Diese Klasse stellt die beiden Zust�nde
	 * "System" und "Fach" f�r Meldungen, die sich auf systemtechnische oder
	 * fachliche Zust�nde beziehen, bereit)
	 * @param nachrichtenTypErweiterung Erweiterung
	 * @param klasse Klasse der Betriebsmeldung
	 * @param status Gibt den Zustand einer Meldung an
	 * @param nachricht Nachrichtentext der Betriebsmeldung
	 */
	public void sendeBetriebsMeldung(final String id,
									 final MessageType typ,
									 final String nachrichtenTypErweiterung,
									 final MessageGrade klasse,
									 final MessageState status,
									 final String nachricht);

	/**
	 * �ber diese Methode soll ein Modul Verwaltung anderen Modulen
	 * die Menge aller zu bearbeitenden Objekte zur Verf�gung stellen.
	 * Sollte an dieser Stelle <code>null</code> �bergeben werden, so
	 * sollten vom fragenden Modul alle inhaltlich passenden Systemobjekte
	 * des Standardkonfigurationsbereichs zur Bearbeitung angenommen werden.
	 * 
	 * @return alle zu bearbeitenden Objekte
	 */
	public SystemObject[] getSystemObjekte();
	
	/**
	 * Erfragt die dem Verwaltungsmodul �bergebenen
	 * Konfigurationsbereiche
	 * 
	 * @return alle Konfigurationsbereiche, die diesem
	 * Verwaltungsmodul �bergeben wurden.
	 */
	public Collection<ConfigurationArea> getKonfigurationsBereiche();
	
	/**
	 * Erfragt die SWE, f�r die die dieses Interface
	 * implementierende Klasse die Verwaltung darstellt
	 * 
	 * @return die SWE, f�r die die dieses Interface
	 * implementierende Klasse die Verwaltung darstellt
	 */
	public SWETyp getSWETyp();
	
	
	/**
	 * Erfragt ein Kommandozeilenargument der Applikation
	 * 
	 * @param schluessel der Name des Arguments
	 * @return das Kommandozeilenargument des Schluessels oder <code>null</code>,
	 * wenn das Argument nicht uebergeben wurde
	 */
	public String getArgument(final String schluessel);
	
}
