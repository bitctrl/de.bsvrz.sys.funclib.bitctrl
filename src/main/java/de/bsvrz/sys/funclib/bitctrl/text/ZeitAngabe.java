/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.text;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Klasse zur formatierten Ausgabe von Zeitangaben als Dauer oder Zeitstempel.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public final class ZeitAngabe {

	/** der Zeitwert, der dargestellt werden soll. */
	private final long zeitWert;

	/**
	 * Konstruktor. Die Funktion erzeugt eine Instanz einer Zeitangabe. Der
	 * übergebene Wert repräsentiert eine Zeitgröße in Millisekunden.
	 *
	 * @param zeitWert
	 *            der Wert
	 */
	public ZeitAngabe(final long zeitWert) {
		this.zeitWert = zeitWert;

	}

	/**
	 * liefert den Zeitwert als Dauer. Die Ausgabe erfolgt als a Tage b Stunden
	 * c Minuten d Sekunden e Millisekunden. Alle Bestandteile mit dem Wert 0
	 * werden ausgelassen. Eine negative Dauer liefert den String "ungültig" ein
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
	 * werden ausgelassen. Eine negative Dauer liefert den String "ungültig" ein
	 * Wert von 0 wird als "0 Millisekunden" ausgegeben. Der Wert Long.MAX_VALUE
	 * wird als mit dem übergebenen Text umgesetzt.
	 *
	 * @param maxText
	 *            der Text, der für den Wert Long.MAX_VALUE ausgegeben werden
	 *            soll.
	 * @return die Dauer aks Text
	 */
	public String dauerAlsText(final String maxText) {
		final StringBuffer result = new StringBuffer();
		if (zeitWert < 0) {
			result.append("ungültig");
		} else if (zeitWert == Long.MAX_VALUE) {
			result.append(maxText);
		} else {
			long restWert = zeitWert;
			restWert = teilDauerErmitteln(result, restWert,
					TimeUnit.DAYS.toMillis(1), "Tage");
			restWert = teilDauerErmitteln(result, restWert,
					TimeUnit.HOURS.toMillis(1), "Stunden");
			restWert = teilDauerErmitteln(result, restWert,
					TimeUnit.MINUTES.toMillis(1), "Minuten");
			restWert = teilDauerErmitteln(result, restWert,
					TimeUnit.SECONDS.toMillis(1), "Sekunden");
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
	 *            der Faktor für den einzufügenden Abschnitt
	 * @param einheit
	 *            die Einheit als Text
	 * @return den Restwert nach der Ermittlung des betreffenden Abschnittes
	 */
	private long teilDauerErmitteln(final StringBuffer result,
			final long restWert, final long faktor, final String einheit) {
		final long teil = restWert / faktor;
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
	 * übergebenen Text ersetzt wird. Wird als Text der Wert <code>null</code>
	 * übergeben, erfolgt keine Sonderbehanldung für den Zeitwert 0.
	 *
	 * @param nullText
	 *            der Text für den Wert 0
	 * @return den Zeitstempel als Text
	 */
	public String zeitStempel(final String nullText) {
		return zeitStempel(nullText, null);
	}

	/**
	 * liefert den Zeitstempel im Standardformat, wobei der Wert 0 durch den
	 * übergebenen Text <code>nullText</code> und der Wert Long.MAX_VALUE durch
	 * den übergebenen Text <code>maxText</code> ersetzt wird. Wird für die
	 * Textparameter der Wert <code>null</code> übergeben, erfolgt keine
	 * Sonderbehanldung für den jeweiligen Wert.
	 *
	 * @param nullText
	 *            der Text für den Wert 0
	 * @param maxText
	 *            der Text für den Wert Long.MAX_VALUE
	 * @return den Zeitstempel als Text
	 */
	public String zeitStempel(final String nullText, final String maxText) {
		String result = null;
		if ((zeitWert == 0) && (nullText != null)) {
			result = nullText;
		} else if ((zeitWert == Long.MAX_VALUE) && (maxText != null)) {
			result = maxText;
		} else {
			final LocalDateTime localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(zeitWert), ZoneId.systemDefault());
			result = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
		}
		return result;
	}
}
