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

package de.bsvrz.sys.funclib.bitctrl.daf;

import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Repräsentation der Daten einer Datenverteiler-Sendeanmeldung.<br>
 * Instanzen dieser Klasse dienen lediglich der Verwaltung der Anmeldedaten. Die
 * eigentliche Anmeldung beim Datenverteiler wird nicht ausgeführt.
 *
 * @author peuker
 */
public class SenderAnmeldung implements Comparable<SenderAnmeldung> {

	/**
	 * das Systemobjekt, für das die Anmeldung erfolgt ist.
	 */
	private SystemObject objekt;

	/**
	 * die Datenbeschreibung, für die die Anmeldung erfolgt.
	 */
	private DataDescription desc;

	/**
	 * der Status der Sendesteuerung.
	 */
	private byte status = ClientSenderInterface.STOP_SENDING;

	/**
	 * die Anzahl der Anmeldungen.
	 */
	private int anzahl;

	/**
	 * Konstruktor.<br>
	 * Erzeugt die Datenrepräsentation einer Sendeanmeldung für die gegebene
	 * Kombination aus Systemobjekt und Datenbeschreibung.
	 *
	 * @param objekt
	 * @param desc
	 */
	public SenderAnmeldung(SystemObject objekt, DataDescription desc) {
		this.objekt = objekt;
		this.desc = desc;
		anzahl = 1;
	}

	/**
	 * vergleicht zwei Sendeanmeldungen miteinander.<br>
	 * Die Funktion implementiert die entsprechende Funktion der Schnittstelle
	 * Comparable. {@inheritDoc}.
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(SenderAnmeldung anmeldung) {
		int result = 0;
		result = objekt.compareTo(anmeldung.objekt);
		if (result == 0) {
			result = desc.getAttributeGroup().compareTo(
					anmeldung.desc.getAttributeGroup());
			if (result == 0) {
				result = desc.getAspect().compareTo(anmeldung.desc.getAspect());
			}
		}
		return result;
	}

	/**
	 * setzt den Status der Sendesteuerung der Anmeldung.
	 *
	 * @param status
	 *            der Status
	 */
	public void setStatus(byte status) {
		this.status = status;
	}

	/**
	 * liefert den Status der Sendesteuerung für die Anmeldung.
	 *
	 * @return den Status
	 */
	public byte getStatus() {
		return status;
	}

	/**
	 * inkrementiert die Anzahl der Anmeldung für die entsprechende Kombination
	 * aus Systemobjekt und Datenbeschreibung.
	 */
	public void add() {
		anzahl++;
	}

	/**
	 * dekrementiert die Anzahl der Anmeldung für die entsprechende Kombination
	 * aus Systemobjekt und Datenbeschreibung.
	 */
	public void remove() {
		anzahl--;

	}

	/**
	 * liefert die Anzahl der registrierten Anmeldungen für die entsprechende
	 * Kombination aus Systemobjekt und Datenbeschreibung.
	 *
	 * @return die Anzahl
	 */
	public int size() {
		return anzahl;
	}
}
