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
	 * Logger für Debug-Ausgaben.
	 */
	private static Debug logger = Debug.getLogger();

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new TestEmpaenger(), args);
	}

	/**
	 * Liste der PIDs für die Aspekte, für die eine Anmeldung erfolgen soll.
	 */
	private final String[] aspekte = {
			// "asp.störfallVerfahrenFuzzy",
			"asp.störfallVerfahrenStandard", "asp.störfallVerfahrenMARZ",
			"asp.störfallVerfahrenNRW", "asp.störfallVerfahrenRDS",
			"asp.störfallVerfahrenFD", "asp.störfallVerfahrenVKDiffKfz",
			"asp.störfallVerfahrenConstraint", "asp.störfallVerfahrenMOBINET" };

	/**
	 * der letzte Status, der als Störzustand versendet werden sollte.
	 */
	Long letzterWert = null;

	/**
	 * Timer für zyklische Datenversendung.
	 */
	Timer timer = new Timer();

	/**
	 * die Liste der Straßenteilsegmente für die der StörfallZustand versendet
	 * werden soll.
	 */
	private SystemObject objekt;

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private ClientDavInterface dav;

	/**
	 * die Attributgruppe zum Versand des Störfallzustandes.
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
		atg = model.getAttributeGroup("atg.störfallZustand");
		objekt = model.getObject("mq.MQ5");

		logger.info("Starte Anmeldung");
		Aspect asp = model.getAspect("asp.störfallVerfahrenStandard");
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
		// keine zusätzlichen Argumente erwartet
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
