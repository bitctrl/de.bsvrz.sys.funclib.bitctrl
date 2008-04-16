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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.DavConnectionListener;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;

/**
 * Allgemeine Klasse zur Realisierung eines Datenverteiler-Senders mit
 * Sendesteuerung.
 * 
 * @author peuker
 */
public class BestaetigterDavSender implements ClientSenderInterface,
		DavConnectionListener {

	private static final Map<ClientDavInterface, BestaetigterDavSender> senderObjekte = new HashMap<ClientDavInterface, BestaetigterDavSender>();

	public static BestaetigterDavSender getSender(ClientDavInterface connection) {
		BestaetigterDavSender result = senderObjekte.get(connection);
		if (result == null) {
			result = new BestaetigterDavSender(connection);
			senderObjekte.put(connection, result);
		}
		return result;
	}

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private ClientDavInterface verbindung;

	/**
	 * die Menge der angemeldeten Verbindungen.
	 */
	private TreeMap<SenderAnmeldung, SenderAnmeldung> anmeldungen = new TreeMap<SenderAnmeldung, SenderAnmeldung>();

	/**
	 * Konstruktor.<br>
	 * Erzeugt einen Datenverteiler-Sender für die übergebene Verbindung.
	 * 
	 * @param verbindung
	 *            die Datenverteiler-Verbindung
	 */
	private BestaetigterDavSender(ClientDavInterface verbindung) {
		this.verbindung = verbindung;
	}

	/**
	 * führt eine Abmeldung für die übergebene Kombination aus Systemobjekt und
	 * Datenbschreibung aus.<br>
	 * Für die entsprechende Anmeldung wird der Anmeldungszähler inkrementiert.
	 * Wenn keine Anmeldung mehr vorhanden ist, erfolgt die Abmeldung vom
	 * Datenverteiler und die Anmeldung wird aus der Liste der verwalteten
	 * Anmeldungen entfernt.
	 * 
	 * @param objekt
	 *            das Systemobjekt
	 * @param desc
	 *            die Datenbschreibung
	 */
	public void abmelden(SystemObject objekt, DataDescription desc) {
		SenderAnmeldung anmeldung = anmeldungen.get(new SenderAnmeldung(objekt,
				desc));
		if (anmeldung != null) {
			anmeldung.remove();
			if (anmeldung.size() <= 0) {
				verbindung.unsubscribeSender(this, objekt, desc);
				anmeldungen.remove(anmeldung);
			}
		} else {
			System.err.println("Unerwartetete Sendeabmeldung: " + objekt + ", "
					+ desc);
		}
	}

	/**
	 * führt eine Anmeldung für die übergebene Kombination aus Systemobjekt und
	 * Datenbschreibung aus.<br>
	 * Wenn für die Kombination noch keine Anmeldung erfolgte, wird eine neue
	 * Anmeldung ausgeführt, ansonsten wird der Zähler für dies Anmeldung
	 * erhöht.
	 * 
	 * @param objekt
	 *            das Systemobjekt
	 * @param desc
	 *            die Datenbschreibung
	 */
	public void anmelden(SystemObject objekt, DataDescription desc) {
		SenderAnmeldung neueAnmeldung = new SenderAnmeldung(objekt, desc);
		SenderAnmeldung anmeldung = anmeldungen.get(neueAnmeldung);
		if (anmeldung == null) {
			try {
				anmeldungen.put(neueAnmeldung, neueAnmeldung);
				verbindung.subscribeSender(this, objekt, desc, SenderRole
						.sender());
			} catch (OneSubscriptionPerSendData e) {
				// sollte eigentlich nicht passieren, da die Anmeldung vorher
				// gesucht wird
			}
		} else {
			anmeldung.add();
		}
	}

	/** {@inheritDoc} */
	public void connectionClosed(ClientDavInterface connection) {
		senderObjekte.remove(connection);
	}

	/** {@inheritDoc} */
	public void dataRequest(SystemObject objekt, DataDescription desc,
			byte status) {
		synchronized (anmeldungen) {
			SenderAnmeldung neueAnmeldung = new SenderAnmeldung(objekt, desc);
			SenderAnmeldung anmeldung = anmeldungen.get(neueAnmeldung);
			if (anmeldung == null) {
				anmeldungen.put(neueAnmeldung, neueAnmeldung);
				anmeldung = neueAnmeldung;
			} else {
				anmeldung.setStatus(status);
			}
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * Die Sendesteuerung wird für alle Anmeldungen unterstützt.
	 * 
	 * @see ClientSenderInterface#isRequestSupported(SystemObject,
	 *      DataDescription)
	 */
	public boolean isRequestSupported(SystemObject objekt,
			DataDescription dataDescription) {
		return true;
	}

	/**
	 * blockiert einen Thread, bis die beschriebene Anmeldung bestätigt wurde.<br>
	 * Die einfache Implementierung pollt zyklisch den Status der Anmeldung
	 * entweer bis dieser als bestätigt gilt oder der übergebene Timeoutwert
	 * erreicht wurde.<br>
	 * Wird für den Timeoutwert ein Wert kleiner oder gleich 0 angegeben, wird
	 * unendlich lange gewartet.
	 * 
	 * @param objekt
	 *            das Systemobjekt
	 * @param desc
	 *            die Datenbeschreibung
	 * @param timeout
	 *            die maximale Wartezeit in Sekunden
	 * @throws SendSubscriptionNotConfirmed
	 *             die Sendeanmeldung konnte innerhalb der geforderten Zeit
	 *             nicht bestätigt werden
	 */
	public synchronized void warteAufBestaetigung(SystemObject objekt,
			DataDescription desc, long timeout)
			throws SendSubscriptionNotConfirmed {
		long now = System.currentTimeMillis();
		SenderAnmeldung anmeldung = anmeldungen.get(new SenderAnmeldung(objekt,
				desc));
		if (anmeldung != null) {
			while (anmeldung.getStatus() != ClientSenderInterface.START_SENDING) {
				if ((timeout > 0)
						&& ((System.currentTimeMillis() - now) > timeout)) {
					throw new SendSubscriptionNotConfirmed(
							"Sendeanmeldung innerhalb der geforderten Zeit nicht bestätigt.");
				}
				Pause.warte(100L);
			}
		} else {
			throw new DataNotSubscribedException("Keine Anmeldung für "
					+ objekt + ", " + desc + " erfolgt.");
		}
	}
}
