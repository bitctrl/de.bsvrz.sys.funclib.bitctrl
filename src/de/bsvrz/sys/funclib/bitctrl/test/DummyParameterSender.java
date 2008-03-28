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

import java.util.HashSet;
import java.util.Set;

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.app.Pause;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Testapplikation zur Untersuchung der Blockierung der Parameterpublizierung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class DummyParameterSender implements StandardApplication,
		ClientSenderInterface {

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new DummyParameterSender(), args);
	}

	private Set<SystemObject> registered = new HashSet<SystemObject>();

	/** die Datenverteilerverbindung. */
	private ClientDavInterface dav;

	/** Attributgruppe, die Parametriert werden soll. */
	private AttributeGroup fsParamAtg;

	private String createTestString(final String string) {
		StringBuffer buffer = new StringBuffer();
		while (buffer.length() < 60000) {
			buffer.append(string);
		}

		return buffer.toString();
	}

	/** {@inheritDoc} */
	public void dataRequest(SystemObject object,
			DataDescription dataDescription, byte state) {
		if (state == ClientSenderInterface.START_SENDING) {
			System.err.println("Datensendung für " + object + " starten");
			registered.add(object);
		} else {
			System.err.println("Datensendung für " + object + " beenden");
			registered.remove(object);
		}
	}

	private Data datenSatz(final String string) {
		Data daten = dav.createData(fsParamAtg);
		daten.getTextValue("Parameter").setText(createTestString(string));
		return daten;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		this.dav = connection;

		SystemObject fs1 = connection.getDataModel().getObject("paramtest.fs1");
		SystemObject fs2 = connection.getDataModel().getObject("paramtest.fs2");
		SystemObject fs3 = connection.getDataModel().getObject("paramtest.fs3");
		SystemObject fs4 = connection.getDataModel().getObject("paramtest.fs4");
		SystemObject fs5 = connection.getDataModel().getObject("paramtest.fs5");
		SystemObject fs6 = connection.getDataModel().getObject("paramtest.fs6");
		fsParamAtg = connection.getDataModel().getAttributeGroup(
				"paramtest.atg.fs");

		SystemObject[] objekte = new SystemObject[] { fs1, fs2, fs3, fs4, fs5,
				fs6 };
		DataDescription desc = new DataDescription(fsParamAtg, connection
				.getDataModel().getAspect("asp.parameterSoll"));

		connection.subscribeSender(this, objekte, desc, SenderRole.source());

		while (true) {
			for (SystemObject obj : objekte) {
				if (registered.contains(obj)) {
					String text = Constants.EMPTY_STRING + connection.getTime();
					System.err.println("Sende Daten für " + obj);
					connection.sendData(new ResultData(obj, desc, connection
							.getTime(), datenSatz(text)));
				} else {
					Pause.warte(10);
				}
			}
		}

	}

	public boolean isRequestSupported(SystemObject object,
			DataDescription dataDescription) {
		// TODO Auto-generated method stub
		return true;
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
