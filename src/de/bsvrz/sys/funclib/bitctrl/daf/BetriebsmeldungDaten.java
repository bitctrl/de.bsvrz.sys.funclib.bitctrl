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

package de.bsvrz.sys.funclib.bitctrl.daf;

import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Klasse zur Speicherung aller für die Erstellung einer Betriebsmeldung
 * relevanten Daten.
 * 
 * @author BitCtrl Systems GmbH, peuker
 * @version $Id$
 */
public class BetriebsmeldungDaten {

	/** die ID der Meldung. */
	private String id;

	/** der Verursacher der Meldung. */
	private MessageCauser causer;

	/** der Typ der Meldung. */
	private MessageType type = MessageType.APPLICATION_DOMAIN;

	/** ein optionales Systemobjekt, das mit der Meldung in Beziehung steht. */
	private SystemObject reference;

	/** der Status der Meldung. */
	private MessageState state = MessageState.MESSAGE;

	/** Standard-Konstruktor.	 */
	public BetriebsmeldungDaten() {
		causer = new MessageCauser(null, "", "");
	}

	/**
	 * Erzeugt eine Instanz des {@link BetriebsmeldungDaten} mit Standardwerten
	 * und der übergebenen Referenz.
	 * 
	 * @param reference
	 *            das Referenzobjekt
	 */
	public BetriebsmeldungDaten(SystemObject reference) {
		this();
		setReference(reference);
	}

	/**
	 * liefert den Verursacher der Meldung. Der Standardwert ist ein Verursacher
	 * mit einer Referenz auf den aktuellen Nutzer der zu Grunde liegenden
	 * Datenverteilerverbindung und leeren Kommentartexten. Dies entspricht dem
	 * Standardverhalten des {@link MessageSender}.
	 * 
	 * @return den Verursacher
	 */
	public MessageCauser getCauser() {
		return causer;
	}

	/**
	 * liefert die definierte ID der Meldung. Der Standardwert ist
	 * <code>null</code>.
	 * 
	 * @return die ID oder <code>null</code>
	 */
	public String getId() {
		return id;
	}

	/**
	 * liefert das mit der Meldung assoziierte Systemobjekt. Der Standardwert
	 * ist <code>null</code>.
	 * 
	 * @return das Objekt oder <code>null</code>
	 */
	public SystemObject getReference() {
		return reference;
	}

	/**
	 * liefert den Status der Meldung (Meldung, widerholte Meldung, Gutmeldung,
	 * ...). Der Standardwert ist {@link MessageState#MESSAGE}, einfach Meldung.
	 * 
	 * @return den Status
	 */
	public MessageState getState() {
		return state;
	}

	/**
	 * liefert den Typ der Meldung. Der Standardwert ist
	 * {@link MessageType#APPLICATION_DOMAIN}.
	 * 
	 * @return den Typ
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * setzt den Verursacher der Meldung. Wenn <code>null</code> übergeben wird,
	 * wird ein Standardverursacher erzeugt.
	 * 
	 * @param causer
	 *            der definierte Verursacher oder <code>null</code> für den
	 *            Standardwert
	 */
	public void setCauser(MessageCauser causer) {
		if (causer == null) {
			this.causer = new MessageCauser(null, "", "");
		} else {
			this.causer = causer;
		}
	}

	/**
	 * setzt explizit die ID der Meldung.
	 * 
	 * @param id
	 *            die ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * setzt das mit der Meldung assoziierte Systemobjekt.
	 * 
	 * @param reference
	 *            das Objekt
	 */
	public void setReference(SystemObject reference) {
		this.reference = reference;
	}

	/**
	 * setzt den Status der Meldung. Wenn <code>null</code> übergeben wird, wird
	 * der Standardwert {@link MessageState#MESSAGE} gesetzt.
	 * 
	 * @param state
	 *            der Status oder <code>null</code> für den Standardwert
	 */
	public void setState(MessageState state) {
		if (state == null) {
			this.state = MessageState.MESSAGE;
		} else {
			this.state = state;
		}
	}

	/**
	 * setzt den Typ der Meldung. Wenn <code>null</code> übergeben wird, wird
	 * der Standardwert {@link MessageType#APPLICATION_DOMAIN} gesetzt.
	 * 
	 * @param type
	 *            der Typ oder <code>null</code> für den Standardwert
	 */
	public void setType(MessageType type) {
		if (type != null) {
			this.type = MessageType.APPLICATION_DOMAIN;
		} else {
			this.type = type;
		}
	}
}
