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

package de.bsvrz.sys.funclib.bitctrl.test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.concurrent.UnboundedQueue;

/**
 * Testapplikation zur Untersuchung der Blockierung der Parameterpublizierung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class DummyParametrierung implements StandardApplication,
		ClientReceiverInterface {

	public class Sender extends Thread implements ClientSenderInterface {

		private ClientDavInterface connection;

		private UnboundedQueue<ResultData> queue = new UnboundedQueue<ResultData>();

		public Sender(ClientDavInterface connection) {
			this.connection = connection;
		}

		public void dataRequest(SystemObject object,
				DataDescription dataDescription, byte state) {
			// TODO Auto-generated method stub

		}

		public boolean isRequestSupported(SystemObject object,
				DataDescription dataDescription) {
			// TODO Auto-generated method stub
			return false;
		}

		public void put(ResultData result) {
			synchronized (queue) {
				queue.put(result);
				System.err.println("Puffergrˆﬂe: " + queue.size());
				Pause.warte(30);
				synchronized (this) {
					this.notify();
				}
			}
		}

		@Override
		public void run() {
			while (true) {
				ResultData data = null;
				synchronized (queue) {
					if (queue.size() > 0) {
						try {
							data = queue.take();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (data != null) {
					try {
						connection.sendData(new ResultData(data.getObject(),
								new DataDescription(fsParamAtg, dav
										.getDataModel().getAspect(
												"asp.parameterSoll")), dav
										.getTime(), data.getData()));
					} catch (DataNotSubscribedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SendSubscriptionNotConfirmed e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if (queue.size() <= 0) {
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die ¸bergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new DummyParametrierung(), args);
	}

	/** die Datenverteilerverbindung. */
	private ClientDavInterface dav;

	private Sender sender;

	/** Attributgruppe, die Parametriert werden soll. */
	private AttributeGroup fsParamAtg;

	/** {@inheritDoc} */
	public void dataRequest(SystemObject object,
			DataDescription dataDescription, byte state) {
		// TODO Auto-generated method stub
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		this.dav = connection;
		sender = new Sender(connection);

		SystemObject fs1 = connection.getDataModel().getObject("paramtest.fs1");
		SystemObject fs2 = connection.getDataModel().getObject("paramtest.fs2");
		SystemObject fs3 = connection.getDataModel().getObject("paramtest.fs3");
		SystemObject fs4 = connection.getDataModel().getObject("paramtest.fs4");
		SystemObject fs5 = connection.getDataModel().getObject("paramtest.fs5");
		SystemObject fs6 = connection.getDataModel().getObject("paramtest.fs6");
		fsParamAtg = connection.getDataModel().getAttributeGroup(
				"paramtest.atg.fs");
		connection.subscribeReceiver(this, new SystemObject[] { fs1, fs2, fs3,
				fs4, fs5, fs6 }, new DataDescription(fsParamAtg, connection
				.getDataModel().getAspect("asp.parameterVorgabe")),
				ReceiveOptions.normal(), ReceiverRole.drain());
		connection.subscribeSender(sender, new SystemObject[] { fs1, fs2, fs3,
				fs4, fs5, fs6 }, new DataDescription(fsParamAtg, connection
				.getDataModel().getAspect("asp.parameterSoll")), SenderRole
				.source());

		sender.start();

	}

	/** {@inheritDoc} */
	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#parseArguments(de.bsvrz.sys.funclib.commandLineArgs.ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		// keine zus‰tzlichen Argumente erwartet
	}

	/** {@inheritDoc} */
	public void update(ResultData[] results) {
		for (ResultData result : results) {
			if (result.hasData()) {
				try {
					sender.put(result);
					// Pause.warte(70);
				} catch (DataNotSubscribedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
