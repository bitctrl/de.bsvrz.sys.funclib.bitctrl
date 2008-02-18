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

package de.bsvrz.sys.funclib.bitctrl.test.zufallsdaten;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.app.AbstractStandardApplication;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class Datengenerator extends AbstractStandardApplication {

	/** Applikationsname. */
	public static final String APP_NAME = "Testdatengenerator";

	/** Copyright. */
	public static final String APP_COPYRIGHT = "Copyright (C) 2007 BitCtrl Systems GmbH";

	/** Versionsangabe. */
	public static final String APP_VERSION = "1.0.0";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StandardApplicationRunner.run(new Datengenerator(), args);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(ClientDavInterface connection) throws Exception {
		ObjektFactory factory;

		factory = ObjektFactory.getInstanz();
		factory.setVerbindung(connection);
		factory.registerStandardFactories();

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#parseArguments(de.bsvrz.sys.funclib.commandLineArgs.ArgumentList)
	 */
	public void parseArguments(ArgumentList argumentList) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getApplikationName() {
		return APP_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getCopyright() {
		return APP_COPYRIGHT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getVersion() {
		return APP_VERSION;
	}

}
