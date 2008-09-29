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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.netz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bitctrl.resource.Configuration;
import com.bitctrl.util.logging.LoggerTools;

import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Tools zum Test.
 * 
 * @author BitCtrl Systems GmbH, Gieseler
 * @version $Id: IsisTests.java 9149 2008-05-23 16:35:57Z gieseler $
 * 
 */

public final class NetzTests {
	/** Der Logger der Klassse. */
	private static Logger log = Logger
			.getLogger("Netztests");

	/**
	 * externe Konfiguration.
	 */
	public static Configuration konfig = null;

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws BisInterfaceException
	 */
	static void init() throws FileNotFoundException, IOException,
			NetzReferenzException {
		final Level level;

		konfig = Configuration.getConfiguration();
		level = Level.parse(konfig.getString("debug.level"));
		LoggerTools.setLogggerLevel(level);
		log.setLevel(level);

		// String[] commandArguments = {
		ArgumentList al = new ArgumentList(
				new String[] { "-debugLevelStdErrText=" + level });

		Debug.init("Netztests", al);
	}



	/**
	 * Privater Konstruktor.
	 */
	private NetzTests() {
		/**
		 * 
		 */
	}
}