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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Implementiert gemeinsame Funktionen von Onlinedatens&auml;tzen.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public abstract class AbstractOnlineDatensatz extends AbstractDatensatz
		implements OnlineDatensatz {

	/**
	 * TODO Richtig implementieren.
	 * 
	 * @author BitCtrl Systems GmbH, Falko Schumann
	 * @version $Id: AbstractOnlineDatensatz.java 4310 2007-10-11 16:11:09Z
	 *          Schumann $
	 */
	private class AsynchronerSender implements ClientSenderInterface {

		private DataDescription sendeDatenBeschreibung;

		public void dataRequest(SystemObject object,
				DataDescription dataDescription, byte state) {
			ClientDavInterface dav;
			DataModel modell;
			DataDescription dbs;
			SenderRole rolle;
			ResultData datensatz;

			if (isQuelle()) {
				rolle = SenderRole.source();
			} else {
				rolle = SenderRole.sender();
			}

			dav = ObjektFactory.getInstanz().getVerbindung();
			modell = dav.getDataModel();
			dbs = new DataDescription(getAttributGruppe(), getSendeAspekt());

			datensatz = new ResultData(getObjekt().getSystemObject(), dbs, dav
					.getTime(), getSendeCache());

			try {
				dav.subscribeSender(this, getObjekt().getSystemObject(), dbs,
						rolle);
			} catch (OneSubscriptionPerSendData e) {
				// TODO Auto-generated catch block
				// Die Ausnahme kann doch eigentlich ignoriert werden?
				e.printStackTrace();
			}
			try {
				dav.sendData(datensatz);
			} catch (DataNotSubscribedException e) {
				// TODO Auto-generated catch block
				// Die Anmeldung steht ein paar Zeilen drüber !!
				e.printStackTrace();
			} catch (SendSubscriptionNotConfirmed e) {
				// TODO Auto-generated catch block
				// Kann nicht eintreten, weil die Sendesteuerung verwendet wird
				e.printStackTrace();
			}

		}

		private DataDescription getSendeDatenBeschreibung() {
			if (sendeDatenBeschreibung == null) {
				sendeDatenBeschreibung = new DataDescription(
						getAttributGruppe(), getSendeAspekt());
			}
			return sendeDatenBeschreibung;
		}

		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
			if (object.equals(getObjekt().getSystemObject())
					&& dataDescription.equals(getSendeDatenBeschreibung())) {
				return true;
			}
			return false;
		}

	}

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            das Objekt dem der Datensatz zugeordnet ist.
	 */
	public AbstractOnlineDatensatz(SystemObjekt objekt) {
		super(objekt);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireAutoUpdate() {
		ClientDavInterface dav;
		DataDescription dbs;

		dav = ObjektFactory.getInstanz().getVerbindung();
		dbs = new DataDescription(getAttributGruppe(), getEmpfangsAspekt());

		if (isAutoUpdate()) {
			ReceiverRole rolle;

			if (isSenke()) {
				rolle = ReceiverRole.drain();
			} else {
				rolle = ReceiverRole.receiver();
			}
			dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
					ReceiveOptions.normal(), rolle);
		} else {
			dav.unsubscribeReceiver(this, getObjekt().getSystemObject(), dbs);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendeDaten() {
		// TODO
	}

}
