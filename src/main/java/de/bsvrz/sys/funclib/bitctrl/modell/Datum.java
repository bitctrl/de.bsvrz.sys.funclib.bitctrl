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

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.DataState;

/**
 * Kapselt die Daten eines Datensatzes.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public interface Datum {

	/**
	 * Der Status eines Datensatzes. Kapselt die Instanzen der DAF-Klasse
	 * {@link DataState} als ENUM.
	 */
	enum Status {
		/**
		 * Datensatztyp für ungültige Datensätze (Initialwert).
		 */
		UNDEFINIERT(
				null), /**
				 * Datensatztyp für Datensätze die Nutzdaten enthalten
				 * (siehe Technische Anforderungen Archivsystem).
				 */
		DATEN(DataState.DATA), /**
		 * Datensatztyp für leere Datensätze, die vom
		 * Archivsystem in den Antwort-Datensatzstrom
		 * von Teilanfragen eingefügt wird, um Bereiche
		 * zu markieren, die gelöscht (und nicht
		 * gesichert) wurden.
		 */
		GELOESCHTER_BLOCK(
				DataState.DELETED_BLOCK), /**
				 * Datensatztyp für leere
				 * Datensätze, die vom Archivsystem
				 * in jeden Datensatzstrom eingefügt
				 * werden, um das Ende eines
				 * Datensatzstroms einer Teilanfrage
				 * zu markieren.
				 */
		ARCHIV_ENDE(
				DataState.END_OF_ARCHIVE), /**
				 * Datensatztyp für leere
				 * Datensätze, die vom
				 * Datenverteiler versendet werden
				 * können, wenn eine Anmeldung von
				 * Daten im Konflikt mit anderen
				 * Anmeldungen steht (z.B. mehrere
				 * Senken für die gleichen Daten).
				 */
		UNGUELTIGE_ANMELDUNG(
				DataState.INVALID_SUBSCRIPTION), /**
				 * Datensatztyp für leere
				 * Datensätze, die von der
				 * Quelle ohne Attributwerte
				 * versendet wurden (siehe
				 * Technische Anforderungen
				 * Archivsystem).
				 */
		KEINE_DATEN(
				DataState.NO_DATA), /**
				 * Datensatztyp für leere Datensätze, die
				 * vom Datenverteiler generiert wurden, weil
				 * nicht die erforderlichen Rechte zum
				 * Empfang der Daten vorliegen.
				 */
		KEINE_RECHTE(
				DataState.NO_RIGHTS), /**
				 * Datensatztyp für leere Datensätze,
				 * die vom Datenverteiler generiert
				 * wurden, weil keine Quelle für die
				 * entsprechenden Daten existiert.
				 */
		KEINE_QUELLE(
				DataState.NO_SOURCE), /**
				 * Datensatztyp für leere Datensätze,
				 * die vom Archivsystem generiert
				 * wurden, um eine potentielle
				 * Datenlücke zu markieren.
				 */
		MOEGLICHE_LUECKE(
				DataState.POSSIBLE_GAP), /**
				 * Datensatztyp für leere
				 * Datensätze, die vom Archivsystem
				 * in den Antwort-Datensatzstrom von
				 * Teilanfragen eingefügt wird, um
				 * Bereiche zu markieren, die
				 * ausgelagert (d.h. gesichert und
				 * gelöscht) wurden.
				 */
		BLOCK_NICHT_VERFUEGBAR(DataState.UNAVAILABLE_BLOCK);

		/**
		 * Bestimmt den Status zu einem Code.
		 *
		 * @param dataState
		 *            ein Datenstatus.
		 * @return der gesuchte Status.
		 */
		public static Status getStatus(final DataState dataState) {
			Status result = UNDEFINIERT;

			for (final Status status : values()) {
				final DataState state = status.getDatenStatus();
				if ((state != null) && state.equals(dataState)) {
					result = status;
					break;
				}
			}
			return result;
		}

		/** der Datenstatus, der dem Wert zu Grunde liegt. */
		private final DataState datenStatus;

		/**
		 * Initialisiert das Objekt.
		 *
		 * @param code
		 *            der Statuscode in den
		 *            Datenverteilerapplikationsfunktionen.
		 */
		Status(final DataState code) {
			datenStatus = code;
		}

		/**
		 * Gibt den passenden Statuscode in den
		 * Datenverteilerapplikationsfunktionen wieder.
		 *
		 * @return {@code code}.
		 */
		public int getCode() {
			int result = 0;
			if (datenStatus != null) {
				result = datenStatus.getCode();
			}
			return result;
		}

		/**
		 * Gibt den Namen des Status zurück.
		 *
		 * @return der Statusname.
		 */
		public String getName() {
			final String result;
			if (datenStatus != null) {
				result = datenStatus.toString();
			} else {
				result = "Undefiniert";
			}
			return result;
		}

		/**
		 * liefert den zugeordneten Datenstatus des ENUM-Wertes. Der Wert kann
		 * <code>null</code> sein, wenn kein Status zugeordnet wurde.
		 *
		 * @return den Datenstatus
		 */
		private DataState getDatenStatus() {
			return datenStatus;
		}

	}

	/**
	 * Klont das Objekt, in dem der Zeitstempel und alle Daten hart kopiert
	 * werden.
	 *
	 * @return ein Klon des Datum.
	 */
	Datum clone();

	/**
	 * liefert den aktuellen Status des Datensatzes.
	 *
	 * @return den Status
	 */
	Status getDatenStatus();

	/**
	 * Gibt den Zeitstempel des Datums als lesbaren Text zur&uuml;ck.
	 *
	 * @return der Zeitpunkt.
	 */
	String getZeitpunkt();

	/**
	 * Gibt den Zeitstempel des Datum zur&uuml;ck.
	 *
	 * @return den Zeitstempel oder 0, wenn er nicht bekannt ist.
	 */
	long getZeitstempel();

	/**
	 * Prüft ob das Datum Daten enthält.
	 *
	 * @return {@code true}, wenn der Datensatz Daten enthält.
	 */
	boolean isValid();

	/**
	 * Legt den Zeitstempel des Datums fest.
	 *
	 * @param zeitstempel
	 *            der neue Zeitstempel.
	 */
	void setZeitstempel(long zeitstempel);

}
