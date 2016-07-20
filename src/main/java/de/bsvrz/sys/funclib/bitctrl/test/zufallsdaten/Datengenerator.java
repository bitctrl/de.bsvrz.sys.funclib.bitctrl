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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * @author BitCtrl Systems GmbH, Falko Schumann
 */
public class Datengenerator implements StandardApplication {

	/** Applikationsname. */
	public static final String APP_NAME = "Testdatengenerator";

	/** Copyright. */
	public static final String APP_COPYRIGHT = "Copyright (C) 2007 BitCtrl Systems GmbH";

	/** Versionsangabe. */
	public static final String APP_VERSION = "1.0.0";

	public static void main(final String[] args) {
		StandardApplicationRunner.run(new Datengenerator(), args);
	}

	@Override
	public void initialize(final ClientDavInterface connection)
			throws Exception {
		final ObjektFactory factory;

		factory = ObjektFactory.getInstanz();
		factory.setVerbindung(connection);
		factory.registerStandardFactories();

	}

	@Override
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
