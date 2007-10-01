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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeType;
import de.bsvrz.dav.daf.main.config.ReferenceAttributeType;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Erzeugt aus einer Liste von Objekten vom Typ {@link ProxyConfigurationObject} die Objektdefinitionen für einen
 * Konfigurationsbereich im Datenkatalog. Dazu enthält ein {@link ProxyConfigurationObject} alle Informationen zu dem zu
 * erzeugenden Konfigurationsobjekt. Dies sind neben Typ, Name und Pid insbesondere die Informationen zu den
 * konfigurierenden Attributgruppen. Damit die Klasse {@link ConfigAreaCreator} die Informationen zu den
 * konfigurierenden Attributgruppen genenerisch ermitteln kann, halt ein {@link ProxyConfigurationObject} zu jeder
 * konfigurierenden Attributgruppe die Daten in Form einer {@link stauma.dav.clientside.Data} Struktur, über die
 * generisch iteriert werden kann. Das Problem besteht nun darin, dass Objektreferenzen in einer solchen {@link
 * stauma.dav.clientside.Data} Struktur bei der Erstellung des {@link ProxyConfigurationObject} nicht eingetragen werden
 * können, da dass referenzierte Objekt i. d. R. noch gar nicht in der Konfiguration verfügbar ist. Der Versuch eine
 * solche Referenz anzulegen führt deshalb zu einem Fehler. Aus diesem Grund wird zu einem {@link
 * stauma.dav.clientside.Data} Objekt zusätzlich eine Liste mit den Pid der zu referenzierenden Objekte mitgeführt,
 * wobei die Reihenfolge der Einträge in der Liste der Reihenfolge der Objektreferenzen im {@link
 * stauma.dav.clientside.Data} Objekt entspricht. Bei der Auswertung der Informationen durch die Klasse {@link
 * ConfigAreaCreator} ermittelt diese dann den Wert einer Referenz nicht aus dem {@link stauma.dav.clientside.Data}
 * Objekt, sondern aus dem entsprechenden Eintrag der Liste.
 *
 * @author inovat, innovative systeme - verkehr - tunnel - technik
 * @author Dipl.-Ing. Hans Christian Kniß (HCK)
 * @version $Revision: 134 $ / $Date: 2007-04-22 20:41:36 +0200 (So, 22 Apr 2007) $ / ($Author: HCK $)
 */

public class ConfigAreaCreator {
	// ------------------------------------------------------------------------
	// VARIABLENDEKLARATIONEN
	// ------------------------------------------------------------------------

	/** DebugLogger für Debug-Ausgaben. */
	private static final Debug debug = Debug.getLogger();

	private List<ProxyConfigurationObject> _configAreaData;
	private File _configAreaFile;
	private String _configAreaPid;
	private String _configAreaName;
	private String _configAreaInfo;
	private String _configResponsibilityName;

	private BufferedOutputStream _bos;

	// ------------------------------------------------------------------------
	// KONSTRUKTOREN  (und vom Konstruktor verwende Methoden)
	// ------------------------------------------------------------------------

	/**
	 * Erzeugt ein Objekt vom Typ ConfigAreaCreator
	 *
	 * @param configAreaFile           File, in das der erzeugte Konfiguraitonsbereich geschrieben wird.
	 * @param configAreaPid            PID für den Konfigurationsbereich.
	 * @param configAreaName           Name des Konfigurationsbereichs.
	 * @param configAreaInfo           Infotext für diesen Konfigurationsbereich.
	 * @param configResponsibilityName Konfigurationsverantwortlicher.
	 * @param configAreaData           Die Konfigurationsdaten, die in den Konfigurationsbereich umgesetzt werden sollen.
	 */
	public ConfigAreaCreator(File configAreaFile,
	                         String configAreaPid,
	                         String configAreaName,
	                         String configAreaInfo,
	                         String configResponsibilityName,
	                         List<ProxyConfigurationObject> configAreaData) {
		_configAreaFile = configAreaFile;
		_configAreaData = configAreaData;
		_configAreaPid = configAreaPid;
		_configAreaName = configAreaName;
		_configAreaInfo = configAreaInfo;
		_configResponsibilityName = configResponsibilityName;

		try {
			_bos = new BufferedOutputStream(new FileOutputStream(_configAreaFile));
		}
		catch (IOException e) {
			debug.error("Fehler beim anlegen der Datei: " + _configAreaFile.getAbsoluteFile());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// ------------------------------------------------------------------------
	// SONSTIGE METHODEN
	// ------------------------------------------------------------------------

	/** Konvertiert die Daten in eine Konfigurationsdatei. */
	public void run() {
		StringBuffer sb = new StringBuffer();

		// Konfigurationsbereichskopf anlegen

		sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
		sb.append("<!DOCTYPE konfigurationsBereich PUBLIC \"-//K2S//DTD Dokument//DE\" \"K2S.dtd\">\n");
		sb.append("<konfigurationsBereich pid=\"");
		sb.append(_configAreaPid);
		sb.append("\" name=\"");
		sb.append(_configAreaName);
		sb.append("\" verantwortlich=\"");
		sb.append(_configResponsibilityName);
		sb.append("\">\n" + "\t<info>\n" + "\t\t<kurzinfo>");
		sb.append(_configAreaInfo);
		sb.append("</kurzinfo>\n" + "\t</info>\n" + "\t<objekte>\n");

		writeConfigData(sb);

		int level = 2;

		for (ProxyConfigurationObject pco : _configAreaData) {
			sb = new StringBuffer();

			// Öffnendes Tag für Konfigurationsobjekt schreiben
			sb.append(indent(level));
			sb.append("<konfigurationsObjekt typ=\"");
			sb.append(pco.getType().getPid());
			sb.append("\"" + " pid=\"");
			sb.append(pco.getPid());
			sb.append("\"" + " name=\"");
			sb.append(pco.getName());
			sb.append("\">" + "\n");

			level++;

			// Konfigurierende Atg des Konfigurationsobjekts schreiben
			for (ProxyDataDescription proxyDataDescription : pco.getProxyConfigurationDataList()) {
				Data data = proxyDataDescription.getData();
				List<String> referenceValues = proxyDataDescription.getReferenceValues();

				// Datensatz rekursiv ausgeben
				printData(data, referenceValues, level, sb);
			}

			// Mengen des Konfigurationsobjekts schreiben
			for (ProxySetDescription proxySetDescription : pco.getProxySetList()) {
				printSet(proxySetDescription, level, sb);
			}

			level--;
			// Schließendes Tag für Konfigurationsobjekt schreiben
			sb.append(indent(level)).append("</konfigurationsObjekt>\n");

			writeConfigData(sb);
		}

		sb = new StringBuffer();
		sb.append("\t</objekte>\n" +
		          "</konfigurationsBereich>");

		writeConfigData(sb);

		try {
			_bos.close();
		}
		catch (IOException e) {
			debug.error("Fehler beim Schließen der Datei.");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Erzeugt abhängig vom aktuellen Einrückungslevel eintsprechende Tabulatoreinrückungen.
	 *
	 * @param level Aktuelle Einrücktiefe.
	 *
	 * @return Ein StringBuffer mit Tabstopps entspechend level.
	 */
	private StringBuffer indent(int level) {
		StringBuffer sb = new StringBuffer(level);
		for (int i = 0; i < level; ++i) {
			sb.append("\t");
		}
		return sb;
	}

	/**
	 * Einen Datensatz ausgeben. Grundsätzlich wird unterschieden zwischen "einfachen" Daten (Eigenschaft
	 * <code>isPlain</code>) und im Gegensatz dazu Feldern und Listen. Zur Ausgabe der letzteren beiden ruft sich diese
	 * Methode rekursiv wieder auf.
	 *
	 * @param data            Daten der Konfigurierenden Attributgruppe.
	 * @param referenceValues Liste mit den Referenzen der Attributgruppe.
	 * @param level           Aktuelle Einrücktiefe.
	 * @param sb              Ausgabebuffer.
	 */
	private void printData(Data data, List<String> referenceValues, int level, StringBuffer sb) {
		if (data.isPlain()) {
			/* Name dieses Datums */
			String name = data.getName();
			AttributeType attributeType = data.getAttributeType();
			String value;

			if (attributeType instanceof ReferenceAttributeType) {
				value = referenceValues.get(0);
				referenceValues.remove(0);
			}
			else {
				/* Der konkrete Wert dieses Datums */
				value = data.asTextValue().getValueText();
			}
			// <datum name="name" wert="value"/>
			sb.append(indent(level));
			sb.append("<datum name=\"");
			sb.append(name);
			sb.append("\"" + " wert=\"");
			sb.append(value);
			sb.append("\"/>\n");
		}
		else if (data.isArray()) {
			/* Iterator zum Durchlaufen des Feldes */
			Iterator i = data.iterator();

			/* Name dieses Datums */
			String name = data.getName();

			// <datenfeld name="name">
			sb.append(indent(level)).append("<datenfeld name=\"").append(name).append("\">\n");

			level++;
			while (i.hasNext()) {
				printData((Data) i.next(), referenceValues, level, sb);
			}
			level--;
			// </datenfeld>
			sb.append(indent(level)).append("</datenfeld>\n");
		}
		else if (data.isList()) {
			/* Iterator zum Durchlaufen der Liste */
			Iterator i = data.iterator();

			/* Name dieses Datums */
			String name = data.getName();

			if (level == 3) {
				// Öffnendes Tag für Datensatz schreiben
				sb.append(indent(level)).append("<datensatz pid=\"").append(name).append("\">\n");
			}
			else {
				// <datenliste name="name">
				sb.append(indent(level)).append("<datenliste name=\"").append(name).append("\">\n");
			}

			level++;
			while (i.hasNext()) {
				printData((Data) i.next(), referenceValues, level, sb);
			}
			level--;

			if (level == 3) {
				// Schließendes Tag für Datensatz schreiben
				sb.append(indent(level)).append("</datensatz>\n");
			}
			else {
				// </datenliste>
				sb.append(indent(level)).append("</datenliste>\n");
			}
		}
	}

	/**
	 * Gibt eine Menge eines Konfigurationsobjekts aus.
	 *
	 * @param proxySetDescription Menge, die ausgegeben werden soll.
	 * @param level               Aktuelle Einrücktiefe.
	 * @param sb                  Ausgabebuffer.
	 */
	private void printSet(ProxySetDescription proxySetDescription, int level, StringBuffer sb) {
		String name = proxySetDescription.getName();
		// <objektMenge name="name">
		sb.append(indent(level)).append("<objektMenge name=\"").append(name).append("\">\n ");

		level++;

		for (String pid : proxySetDescription.getElements()) {
			// <element pid="name"/>
			sb.append(indent(level)).append("<element pid=\"").append(pid).append("\"/>\n");
		}

		level--;

		// </objektMenge>
		sb.append(indent(level)).append("</objektMenge>\n");
	}

	/**
	 * Schreibt einen Stringbuffer in Datei.
	 *
	 * @param sb Zu schreibender StringBuffer.
	 */
	private void writeConfigData(StringBuffer sb) {
		try {
			_bos.write(sb.toString().getBytes());
		}
		catch (IOException e) {
			debug.error("Fehler beim Schreiben auf Datei: " + _configAreaFile.getAbsoluteFile());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}