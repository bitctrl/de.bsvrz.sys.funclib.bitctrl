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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bitctrl.resource.Configuration;
import com.bitctrl.util.logging.LoggerTools;

import de.bsvrz.dav.daf.main.ClientDavConnection;
import de.bsvrz.dav.daf.main.CommunicationError;
import de.bsvrz.dav.daf.main.ConnectionException;
import de.bsvrz.dav.daf.main.InconsistentLoginException;
import de.bsvrz.dav.daf.main.MissingParameterException;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Tools zum Test.
 *
 * @author BitCtrl Systems GmbH, Gieseler
 */

public final class NetzTests {
	/**
	 * Datenverteiler-Verbindung.
	 */
	private static ClientDavConnection verbindung;

	/** Der Logger der Klassse. */
	private static Logger log = Logger.getLogger("Netztests");

	/**
	 * externe Konfiguration.
	 */
	private static Configuration konfig;

	/**
	 * .
	 *
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MissingParameterException
	 * @throws ConnectionException
	 * @throws CommunicationError
	 * @throws InconsistentLoginException
	 */
	static void init()
			throws FileNotFoundException, IOException, NetzReferenzException,
			MissingParameterException, CommunicationError, ConnectionException,
			InconsistentLoginException {
		final Level level;

		konfig = Configuration.getConfiguration();
		level = Level.parse(konfig.getString("debug.level"));
		LoggerTools.setLoggerLevel(level);
		log.setLevel(level);
		log.info("Stelle Verbindung zum Datenverteiler her ...");
		verbindung = new ClientDavConnection();
		verbindung.getClientDavParameters()
		.setDavCommunicationAddress(konfig.getString("dav.host"));
		verbindung.getClientDavParameters()
		.setDavCommunicationSubAddress(konfig.getInt("dav.port"));
		verbindung.connect();
		verbindung.login(konfig.getString("dav.benutzer"),
				konfig.getString("dav.kennwort"));

		// String[] commandArguments = {
		final ArgumentList al = new ArgumentList(
				new String[] { "-debugLevelStdErrText=" + level });

		Debug.init("Netztests", al);
	}

	private NetzTests() {
	}

	public static ClientDavConnection getVerbindung() {
		return verbindung;
	}

	protected static Configuration getKonfig() {
		return konfig;
	}
}
