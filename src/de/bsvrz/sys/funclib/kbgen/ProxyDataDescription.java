/*
 * Copyright 2006 by inovat, Dipl.-Ing. H. C. Kniß
 * ALL RIGHTS RESERVED.
 *
 * THIS SOFTWARE IS  PROVIDED  "AS IS"  AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF  MERCHANTABILITY  AND  FITNESS  FOR  A PARTICULAR  PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL inovat OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES  (INCL., BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES;  LOSS OF USE, DATA, OR  PROFITS;
 * OR BUSINESS INTERRUPTION)  HOWEVER  CAUSED  AND  ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  OR TORT (INCL.
 * NEGLIGENCE OR OTHERWISE)  ARISING  IN  ANY  WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.bsvrz.sys.funclib.kbgen;

import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Objekt, über den die (konfigurierenden) Daten einer Attributgruppe für ein Objekt mit den Objektreferenzen innerhalb
 * der Attributgruppe verknüpft werden.<p> Erläuterung: Die Klasse {@link ConfigAreaCreator} erzeugt aus den ihr
 * übergebenen Objekten vom Typ {@link ProxyConfigurationObject} die Objektdefinitionen für einen Konfigurationsbereich
 * im Datenkatalog. Dazu enthält ein {@link ProxyConfigurationObject} alle Informationen zu dem zu erzeugenden
 * Konfigurationsobjekt. Dies sind neben Typ, Name und Pid insbesondere die Informationen zu den konfigurierenden
 * Attributgruppen. Damit die Klasse {@link ConfigAreaCreator} die Informationen zu den konfigurierenden Attributgruppen
 * genenerisch ermitteln kann, halt ein {@link ProxyConfigurationObject} zu jeder konfigurierenden Attributgruppe die
 * Daten in Form einer {@link stauma.dav.clientside.Data} Struktur, über die generisch iteriert werden kann. Das Problem
 * besteht nun darin, dass Objektreferenzen in einer solchen {@link stauma.dav.clientside.Data} Struktur bei der
 * Erstellung des {@link ProxyConfigurationObject} nicht eingetragen werden können, da dass referenzierte Objekt i. d.
 * R. noch gar nicht in der Konfiguration verfügbar ist. Der Versuch eine solche Referenz anzulegen führt deshalb zu
 * einem Fehler. Aus diesem Grund wird zu einem {@link stauma.dav.clientside.Data} Objekt zusätzlich eine Liste mit den
 * Pid der zu referenzierenden Objekte mitgeführt, wobei die Reihenfolge der Einträge in der Liste der Reihenfolge der
 * Objektreferenzen im {@link stauma.dav.clientside.Data} Objekt entspricht. Bei der Auswertung der Informationen durch
 * die Klasse {@link ConfigAreaCreator} ermittelt diese dann den Wert einer Referenz nicht aus dem {@link
 * stauma.dav.clientside.Data} Objekt, sondern aus dem entsprechenden Eintrag der Liste.
 *
 * @author inovat, innovative systeme - verkehr - tunnel - technik
 * @author Dipl.-Ing. Hans Christian Kniß (HCK)
 * @version $Revision: 113 $ / $Date: 2007-03-25 10:49:50 +0200 (So, 25 Mrz 2007) $ / ($Author: HCK $)
 */

public class ProxyDataDescription {
	// ------------------------------------------------------------------------
	// VARIABLENDEKLARATIONEN
	// ------------------------------------------------------------------------

	/** DebugLogger für Debug-Ausgaben. */
	private static final Debug debug = Debug.getLogger();
	Data _data;
	List<String> _referenceValues;

	// ------------------------------------------------------------------------
	// KONSTRUKTOREN  (und vom Konstruktor verwende Methoden)
	// ------------------------------------------------------------------------

	/**
	 * Erzeugt eine Datenbeschreibung aus den Daten und der Liste der Pids von Objektreferenzen.
	 *
	 * @param data            Konfigurierende Daten.
	 * @param referenceValues Liste der Pids mit eventuell in data benötigten Objektreferenzen.
	 */
	public ProxyDataDescription(Data data, List<String> referenceValues) {
		_data = data.createModifiableCopy();
		_referenceValues = referenceValues;
	}

	// ------------------------------------------------------------------------
	// GETTER / SETTER METHODEN
	// ------------------------------------------------------------------------

	/**
	 * Liefert das {@link stauma.dav.clientside.Data} Objekt.
	 *
	 * @return {@link stauma.dav.clientside.Data} Objekt mit nicht gesetzten Objektreferenzen.
	 */
	public Data getData() {
		return _data;
	}

	/**
	 * Liefert die Liste der Pids mit den Objektreferenzen.
	 *
	 * @return Liste der Pids mit den im {@link stauma.dav.clientside.Data} Objekt fehlenden Objektreferenzen.
	 */
	public List<String> getReferenceValues() {
		return _referenceValues;
	}
}


