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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;

/**
 * Implementiert gemeinsame Funktionen von Parametern.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id: AbstractParameterDatensatz.java 4336 2007-10-12 13:46:53Z
 *          Schumann $
 */
public abstract class AbstractParameterDatensatz extends AbstractDatensatz
		implements ParameterDatensatz {

	/**
	 * Der Empf&auml;nger wird in einer internen Klasse vor dem Anwender
	 * versteckt.
	 */
	private class AsynchronerReceiver implements ClientReceiverInterface {

		/** Die Datenverteilerverbindung. */
		private final ClientDavInterface dav;

		/** Die aktuell verwendete Datenbeschreibung. */
		private DataDescription dbs;

		/**
		 * Konstruiert den Sender.
		 */
		public AsynchronerReceiver() {
			dav = ObjektFactory.getInstanz().getVerbindung();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 */
		public void abmelden() {
			if (dbs != null) {
				dav.unsubscribeReceiver(this, getObjekt().getSystemObject(),
						dbs);
				dbs = null;
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 */
		public void anmelden() {
			abmelden();
			dbs = new DataDescription(getAttributGruppe(), ObjektFactory
					.getInstanz().getVerbindung().getDataModel().getAspect(
							DaVKonstanten.ASP_PARAMETER_SOLL));
			dav.subscribeReceiver(this, getObjekt().getSystemObject(), dbs,
					ReceiveOptions.normal(), ReceiverRole.receiver());
		}

		/**
		 * {@inheritDoc}
		 */
		public void update(ResultData[] results) {
			for (ResultData result : results) {
				if (result.hasData()) {
					setDaten(result.getData());
					valid = true;
				} else {
					valid = false;
				}
				letzterZeitstempel = result.getDataTime();
			}
		}
	}

	/**
	 * Versteckt die Sendelogik des Datenverteilers vor dem Anwender.
	 */
	private class SynchronerSender implements ClientSenderInterface {

		/** Die Datenverteilerverbindung. */
		private final ClientDavInterface dav;

		/** Die aktuell verwendete Datenbeschreibung. */
		private DataDescription dbs;

		/** Der Zustand der Sendesteuerung. */
		private boolean sendenErlaubt;

		/**
		 * Konstruiert den Sender.
		 */
		public SynchronerSender() {
			dav = ObjektFactory.getInstanz().getVerbindung();
		}

		/**
		 * Meldet eine vorhandene Sendeanmeldung wieder ab. Existiert keine
		 * Anmeldung, passiert nichts.
		 */
		public void abmelden() {
			if (dbs != null) {
				dav.unsubscribeSender(this, getObjekt().getSystemObject(), dbs);
				dbs = null;
			}
		}

		/**
		 * Meldet eine neue Sendeanmeldung an. Eine eventuell existierende
		 * Anmeldung wird vorher abgemeldet.
		 * 
		 * @throws AnmeldeException
		 *             wenn die Anmeldung schief ging.
		 */
		public void anmelden() throws AnmeldeException {
			abmelden();
			dbs = new DataDescription(getAttributGruppe(), ObjektFactory
					.getInstanz().getVerbindung().getDataModel().getAspect(
							DaVKonstanten.ASP_PARAMETER_VORGABE));
			try {
				dav.subscribeSender(this, getObjekt().getSystemObject(), dbs,
						SenderRole.sender());
			} catch (OneSubscriptionPerSendData ex) {
				throw new AnmeldeException(ex);
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.ClientSenderInterface#dataRequest(de.bsvrz.dav.daf.main.config.SystemObject,
		 *      de.bsvrz.dav.daf.main.DataDescription, byte)
		 */
		public void dataRequest(SystemObject object,
				DataDescription dataDescription, byte state) {
			if (isRequestSupported(object, dataDescription)
					&& state == ClientSenderInterface.START_SENDING) {
				sendenErlaubt = true;
			} else {
				sendenErlaubt = false;
			}
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.dav.daf.main.ClientSenderInterface#isRequestSupported(de.bsvrz.dav.daf.main.config.SystemObject,
		 *      de.bsvrz.dav.daf.main.DataDescription)
		 */
		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
			if (object.equals(getObjekt().getSystemObject())
					&& dataDescription.equals(dbs)) {
				return true;
			}
			return false;
		}

		/**
		 * F&uuml;gt ein Datum der Warteschlange des Senders hinzu.
		 * 
		 * @param daten
		 *            ein zu sendentes Datum.
		 * @throws DatensendeException
		 *             wenn die Daten nicht gesendet werden konnten.
		 */
		public void sende(Data daten) throws DatensendeException {
			if (sendenErlaubt) {
				ResultData datensatz = new ResultData(getObjekt()
						.getSystemObject(), dbs, dav.getTime(), daten);
				try {
					dav.sendData(datensatz);
				} catch (DataNotSubscribedException ex) {
					throw new DatensendeException(ex);
				} catch (SendSubscriptionNotConfirmed ex) {
					throw new DatensendeException(ex);
				}
			} else {
				throw new DatensendeException(
						"Die Sendesteuerung hat das Senden verboten.");
			}
		}
	}

	/** Das Flag f&uuml;r die G&uuml;ltigkeit des Datensatzes. */
	private boolean valid;

	/** Der Empf&auml;nger dieses Datensatzes. */
	private final AsynchronerReceiver receiver = new AsynchronerReceiver();

	/** Der Sender dieses Datensatzes. */
	private final SynchronerSender sender = new SynchronerSender();

	/** Der Zeitstempel der letzten Aktualisierung des Datensatzes. */
	private long letzterZeitstempel;

	/**
	 * Konstruktor.
	 * 
	 * @param objekt
	 *            ds Objekt, dem der Datensatz zugeordnet ist.
	 */
	public AbstractParameterDatensatz(SystemObjekt objekt) {
		super(objekt);
	}

	/**
	 * {@inheritDoc}
	 */
	public void abmeldenSender() {
		sender.abmelden();
	}

	/**
	 * {@inheritDoc}
	 */
	public void anmeldenSender() throws AnmeldeException {
		sender.anmelden();
	}

	/**
	 * {@inheritDoc}
	 */
	public long getLetzterZeitstempel() {
		return letzterZeitstempel;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendeDaten() throws DatensendeException {
		sender.sende(getSendeCache());
		clearSendeCache();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireAutoUpdate() {
		if (isAutoUpdate()) {
			receiver.anmelden();
		} else {
			receiver.abmelden();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void fireUpdate() {
		if (!isAutoUpdate()) {
			ClientDavInterface dav;
			DataDescription dbs;
			ResultData datensatz;

			dav = ObjektFactory.getInstanz().getVerbindung();
			dbs = new DataDescription(getAttributGruppe(), ObjektFactory
					.getInstanz().getVerbindung().getDataModel().getAspect(
							DaVKonstanten.ASP_PARAMETER_SOLL));
			datensatz = dav.getData(getObjekt().getSystemObject(), dbs, 0);
			letzterZeitstempel = datensatz.getDataTime();
			setDaten(datensatz.getData());
		}
	}
}
