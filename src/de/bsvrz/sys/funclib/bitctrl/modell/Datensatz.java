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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.NoSuchElementException;

import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;

/**
 * Schnittstelle f&uum;r den Inhalt einer Attributgruppen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @param <T>
 *            Der Typ des Datums den der Datensatz sichert.
 */
public interface Datensatz<T extends Datum> {

	/**
	 * Die Statuscodes der Sendesteuerung des Datenverteilers als {@code Enum}.
	 */
	public enum Status {

		/** Der Versand von Daten kann gestartet werden. */
		START(ClientSenderInterface.START_SENDING,
				"Der Versand von Daten kann gestartet werden."),

		/**
		 * Der Versand von Daten soll angehalten werden, weil momentan kein
		 * Abnehmer sich für die Daten interessiert.
		 */
		STOP(
				ClientSenderInterface.STOP_SENDING,
				"Der Versand von Daten soll angehalten werden, weil momentan kein Abnehmer sich für die Daten interessiert."),

		/**
		 * Der Versand von Daten soll angehalten werden, weil momentan keine
		 * Rechte für den Versand vorliegen.
		 */
		KEINE_RECHTE(
				ClientSenderInterface.STOP_SENDING_NO_RIGHTS,
				"Der Versand von Daten soll angehalten werden, weil momentan keine Rechte für den Versand vorliegen."),

		/**
		 * Der Versand von Daten soll angehalten werden, weil die entsprechende
		 * Anmeldung momentan nicht gültig ist (z.B. wegen doppelter Quelle).
		 */
		ANMELDUNG_UNGUELTIG(
				ClientSenderInterface.STOP_SENDING_NOT_A_VALID_SUBSCRIPTION,
				"Der Versand von Daten soll angehalten werden, weil die entsprechende Anmeldung momentan nicht gültig ist (z.B. wegen doppelter Quelle).");

		/**
		 * Bestimmt den Status zu einem Code.
		 * 
		 * @param code
		 *            ein Code.
		 * @return der gesuchte Status.
		 */
		public static Status getStatus(int code) {
			for (Status s : values()) {
				if (s.getCode() == code) {
					return s;
				}
			}

			throw new NoSuchElementException("Ungültiger Code");
		}

		/** Die Eigenschaft {@code beschreibung}. */
		private final String beschreibung;

		/** Die Eigenschaft {@code code}. */
		private final int code;

		/**
		 * Initialisiert das Objekt.
		 * 
		 * @param code
		 *            der Statuscode in den
		 *            Datenverteilerapplikationsfunktionen.
		 * @param beschreibung
		 *            die Beschreibung des Status.
		 */
		private Status(int code, String beschreibung) {
			this.code = code;
			this.beschreibung = beschreibung;
		}

		/**
		 * Gibt die Beschreibung des Status wieder.
		 * 
		 * @return {@code beschreibung}.
		 */
		public String getBeschreibung() {
			return beschreibung;
		}

		/**
		 * Gibt den passenden Statuscode in den
		 * Datenverteilerapplikationsfunktionen wieder.
		 * 
		 * @return {@code code}.
		 */
		public int getCode() {
			return code;
		}

	}

	/**
	 * Erzeugt ein leeres oder mit Standardwerten ausgef&uuml;lltes Datum des
	 * Datensatzes. Dieses Datum kann nach dem Ausf&uuml;llen an den
	 * Datenverteiler versandt werden.
	 * 
	 * @return das Datum.
	 */
	T erzeugeDatum();

	/**
	 * Gibt die Attributgruppe zur&uuml;ck die diesem Datensatz entpricht.
	 * 
	 * @return die Attributgruppe die dem Datensatz entspricht.
	 */
	AttributeGroup getAttributGruppe();

	/**
	 * Gibt das Systemobjekt zur&uuml;ck, zu dem der Datensatz geh&ouml;rt.
	 * 
	 * @return das Objekt, zu dem der Datensatz geh&ouml;rt.
	 */
	SystemObjekt getObjekt();

	/**
	 * Liest das Datum aus und setzt dessen Inhalt als internen Zustand.
	 * 
	 * @param daten
	 *            ein passender Datenverteilerdatensatz.
	 */
	void setDaten(ResultData daten);

}
