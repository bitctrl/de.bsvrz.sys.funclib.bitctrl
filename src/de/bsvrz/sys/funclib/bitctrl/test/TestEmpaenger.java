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

package de.bsvrz.sys.funclib.bitctrl.test;

import java.util.Timer;

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
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Exportiert die Koordinaten der definierten MQ.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class TestEmpaenger implements StandardApplication,
		ClientReceiverInterface {

	/**
	 * Logger f�r Debug-Ausgaben.
	 */
	private static Debug logger = Debug.getLogger();

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die �bergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new TestEmpaenger(), args);
	}

	/**
	 * Liste der PIDs f�r die Aspekte, f�r die eine Anmeldung erfolgen soll.
	 */
	private final String[] aspekte = {
			// "asp.st�rfallVerfahrenFuzzy",
			"asp.st�rfallVerfahrenStandard", "asp.st�rfallVerfahrenMARZ",
			"asp.st�rfallVerfahrenNRW", "asp.st�rfallVerfahrenRDS",
			"asp.st�rfallVerfahrenFD", "asp.st�rfallVerfahrenVKDiffKfz",
			"asp.st�rfallVerfahrenConstraint", "asp.st�rfallVerfahrenMOBINET" };

	/**
	 * der letzte Status, der als St�rzustand versendet werden sollte.
	 */
	Long letzterWert = null;

	/**
	 * Timer f�r zyklische Datenversendung.
	 */
	Timer timer = new Timer();

	/**
	 * die Liste der Stra�enteilsegmente f�r die der St�rfallZustand versendet
	 * werden soll.
	 */
	private SystemObject objekt;

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private ClientDavInterface dav;

	/**
	 * die Attributgruppe zum Versand des St�rfallzustandes.
	 */
	private AttributeGroup atg;

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		logger = Debug.getLogger();

		this.dav = connection;
		DataModel model = connection.getDataModel();
		atg = model.getAttributeGroup("atg.st�rfallZustand");
		objekt = model.getObject("mq.MQ5");

		logger.info("Starte Anmeldung");
		Aspect asp = model.getAspect("asp.st�rfallVerfahrenStandard");
		DataDescription desc = new DataDescription(atg, asp);

		connection.subscribeReceiver(this, objekt, desc, ReceiveOptions
				.normal(), ReceiverRole.drain());
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#parseArguments(de.bsvrz.sys.funclib.commandLineArgs.ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		// keine zus�tzlichen Argumente erwartet
	}

	public void update(ResultData[] results) {
		for (ResultData result : results) {
			Data daten = result.getData();
			if (daten != null) {
				long neuerWert = daten.getTimeValue("Horizont").getMillis();
				if (letzterWert != null) {
					if ((neuerWert - letzterWert) != 1) {
						System.err.println("Fehler: NeuerWert == " + neuerWert
								+ " LetzterWert == " + letzterWert);
					}
				}
				letzterWert = neuerWert;
				if ((letzterWert % 1000) == 0) {
					System.err.println("Empfange Daten: LetzterWert == "
							+ letzterWert);
				}
			}
		}
	}
}
