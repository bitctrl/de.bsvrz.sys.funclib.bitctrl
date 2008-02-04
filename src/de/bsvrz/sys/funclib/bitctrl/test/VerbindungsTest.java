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

package de.bsvrz.sys.funclib.bitctrl.test;

import java.util.Collection;

import de.bsvrz.dav.daf.main.ClientDavConnection;
import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Exportiert die Koordinaten der definierten MQ.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class VerbindungsTest implements StandardApplication {

	public class TestReceiver implements ClientReceiverInterface {
		private final String name;

		public TestReceiver(final String string) {
			this.name = string;
		}

		public void update(final ResultData[] results) {
			for (ResultData result : results) {
				System.err.println(name + ": " + result);
				System.err.println("No valid subscription: "
						+ result.isNoValidSubscription());
			}

		}

	}

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new VerbindungsTest(), args);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		SystemObject aoe = connection.getLocalConfigurationAuthority();
		AttributeGroup atg = connection.getDataModel().getAttributeGroup(
				"atg.prognoseGanglinienAnfrage");
		Aspect asp = connection.getDataModel().getAspect("asp.anfrage");

		System.err.println("Erste Anmeldung");

		connection.subscribeReceiver(new TestReceiver("ERSTER"), aoe,
				new DataDescription(atg, asp), ReceiveOptions.normal(),
				ReceiverRole.drain());

		System.err.println("V1: " + connection.getLocalApplicationObject());

		Pause.warte(10000);

		// connection.disconnect(false, "Einfach mal Disconnecten");

		// Pause.warte(10000);

		// connection.connect();

		System.err.println("Neue Verbindung");
		ClientDavInterface neueVerbindung = new ClientDavConnection(connection
				.getClientDavParameters());
		System.err.println("Connect");
		neueVerbindung.connect();
		System.err.println("Login");
		neueVerbindung.login();

		System.err.println("V2: " + neueVerbindung.getLocalApplicationObject());

		System.err.println("Zweitanmeldung");
		neueVerbindung.subscribeReceiver(new TestReceiver("ZWEITER"), aoe,
				new DataDescription(atg, asp), ReceiveOptions.normal(),
				ReceiverRole.drain());

		// System.exit(0);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#parseArguments(de.bsvrz.sys.funclib.commandLineArgs.ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		// keine zusätzlichen Argumente erwartet
	}

}
