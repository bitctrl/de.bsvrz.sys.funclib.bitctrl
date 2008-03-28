/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.text;

import java.text.DateFormat;
import java.util.Date;

import com.bitctrl.Constants;

/**
 * Klasse zur formatierten Ausgabe von Zeitangaben als Dauer oder Zeitstempel.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public final class ZeitAngabe {

	/** der Zeitwert, der dargestellt werden soll. */
	private final long zeitWert;

	/**
	 * Konstruktor. Die Funktion erzeugt eine Instanz einer Zeitangabe. Der
	 * �bergebene Wert repr�sentiert eine Zeitgr��e in Millisekunden.
	 * 
	 * @param zeitWert
	 *            der Wert
	 */
	public ZeitAngabe(long zeitWert) {
		this.zeitWert = zeitWert;

	}

	/**
	 * liefert den Zeitwert als Dauer. Die Ausgabe erfolgt als a Tage b Stunden
	 * c Minuten d Sekunden e Millisekunden. Alle Bestandteile mit dem Wert 0
	 * werden ausgelassen. Eine negative Dauer liefert den String "ung�ltig" ein
	 * Wert von 0 wird als "0 Millisekunden" ausgegeben. Der Wert Long.MAX_VALUE
	 * wird als "unbekannt" umgesetzt.
	 * 
	 * @return die Dauer aks Text
	 */
	public String dauerAlsText() {
		return dauerAlsText("unbekannt");
	}

	/**
	 * liefert den Zeitwert als Dauer. Die Ausgabe erfolgt als a Tage b Stunden
	 * c Minuten d Sekunden e Millisekunden. Alle Bestandteile mit dem Wert 0
	 * werden ausgelassen. Eine negative Dauer liefert den String "ung�ltig" ein
	 * Wert von 0 wird als "0 Millisekunden" ausgegeben. Der Wert Long.MAX_VALUE
	 * wird als mit dem �bergebenen Text umgesetzt.
	 * 
	 * @param maxText
	 *            der Text, der f�r den Wert Long.MAX_VALUE ausgegeben werden
	 *            soll.
	 * @return die Dauer aks Text
	 */
	public String dauerAlsText(String maxText) {
		StringBuffer result = new StringBuffer();
		if (zeitWert < 0) {
			result.append("ung�ltig");
		} else if (zeitWert == Long.MAX_VALUE) {
			result.append(maxText);
		} else {
			long restWert = zeitWert;
			restWert = teilDauerErmitteln(result, restWert,
					Constants.MILLIS_PER_DAY, "Tage");
			restWert = teilDauerErmitteln(result, restWert,
					Constants.MILLIS_PER_HOUR, "Stunden");
			restWert = teilDauerErmitteln(result, restWert,
					Constants.MILLIS_PER_MINUTE, "Minuten");
			restWert = teilDauerErmitteln(result, restWert,
					Constants.MILLIS_PER_SECOND, "Sekunden");
			teilDauerErmitteln(result, restWert, 1, "Millisekunden");

			if (result.length() <= 0) {
				result.append("0 Millisekunden");
			}
		}

		return result.toString();
	}

	/**
	 * liefert den definierten Zeitwert, der Zeitangabe in Millisekunden.
	 * 
	 * @return den Wert in ms
	 */
	public long getZeitWert() {
		return zeitWert;
	}

	/**
	 * ermittelt eine Teil des "Dauer-String".
	 * 
	 * @param result
	 *            das Ziel, in dem der Text zusammengebaut wird.
	 * @param restWert
	 *            der noch bestehende Restwert
	 * @param faktor
	 *            der Faktor f�r den einzuf�genden Abschnitt
	 * @param einheit
	 *            die Einheit als Text
	 * @return den Restwert nach der Ermittlung des betreffenden Abschnittes
	 */
	private long teilDauerErmitteln(StringBuffer result, long restWert,
			long faktor, String einheit) {
		long teil = restWert / faktor;
		if (teil > 0) {
			if (result.length() > 0) {
				result.append(' ');
			}
			result.append(teil);
			result.append(" ");
			result.append(einheit);
		}
		return restWert - (teil * faktor);
	}

	/**
	 * liefert den Zeitstempel im Standard-Datums-Zeit-Format.
	 * 
	 * @return den Zeitstempel als Text
	 */
	public String zeitStempel() {
		return zeitStempel(null, null);
	}

	/**
	 * liefert den Zeitstempel im Standardformat, wobei der Wert 0 durch den
	 * �bergebenen Text ersetzt wird. Wird als Text der Wert <code>null</code>
	 * �bergeben, erfolgt keine Sonderbehanldung f�r den Zeitwert 0.
	 * 
	 * @param nullText
	 *            der Text f�r den Wert 0
	 * @return den Zeitstempel als Text
	 */
	public String zeitStempel(String nullText) {
		return zeitStempel(nullText, null);
	}

	/**
	 * liefert den Zeitstempel im Standardformat, wobei der Wert 0 durch den
	 * �bergebenen Text <code>nullText</code> und der Wert Long.MAX_VALUE
	 * durch den �bergebenen Text <code>maxText</code> ersetzt wird. Wird f�r
	 * die Textparameter der Wert <code>null</code> �bergeben, erfolgt keine
	 * Sonderbehanldung f�r den jeweiligen Wert.
	 * 
	 * @param nullText
	 *            der Text f�r den Wert 0
	 * @param maxText
	 *            der Text f�r den Wert Long.MAX_VALUE
	 * @return den Zeitstempel als Text
	 */
	public String zeitStempel(String nullText, String maxText) {
		String result = null;
		if ((zeitWert == 0) && (nullText != null)) {
			result = nullText;
		} else if ((zeitWert == Long.MAX_VALUE) && (maxText != null)) {
			result = maxText;
		} else {
			result = DateFormat.getDateTimeInstance()
					.format(new Date(zeitWert));
		}
		return result;
	}
}