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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataAndATGUsageInformation;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationChangeException;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.DynamicObject;
import de.bsvrz.dav.daf.main.config.DynamicObjectType;
import de.bsvrz.dav.daf.main.config.MutableSet;
import de.bsvrz.dav.daf.main.config.MutableSetChangeListener;
import de.bsvrz.dav.daf.main.config.ObjectSet;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList.Argument;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Exportiert die Koordinaten der definierten MQ.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class SimulationsTester implements StandardApplication {

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
		StandardApplicationRunner.run(new SimulationsTester(), args);
	}

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private ClientDavInterface dav;

	/**
	 * die Attributgruppe zum Versand des Störfallzustandes.
	 */
	private AttributeGroup atg;

	private MutableSet baustellen;

	private DynamicObjectType baustellenTyp;

	private boolean erzeugeObjekte;

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
		DynamicObjectType streckenTyp = (DynamicObjectType) model
				.getType("typ.simulationsStrecke");
		AttributeGroup atg = model
				.getAttributeGroup("atg.simulationsStreckenBeschreibung");
		Data daten = dav.createData(atg);

		if (model.getObject("styp.meineStrecke") == null) {
			dav.getLocalConfigurationAuthority().getConfigurationArea()
					.createDynamicObject(streckenTyp, "styp.meineStrecke",
							"Meine Strecke");
		}

		System.err.println("Daten: " + daten);

		DynamicObjectType simulationsTyp = (DynamicObjectType) model
				.getType("typ.onlineSimulation");
		atg = model.getAttributeGroup("atg.simulationsEigenschaften");

		daten = dav.createData(atg);
		daten.getUnscaledValue("SimulationsVariante").set(5);
		daten.getReferenceValue("SimulationsStreckenReferenz").setSystemObject(
				model.getObject("styp.meineStrecke"));
		System.err.println("Daten: " + daten);

		if (model.getObject("simulation.5") == null) {
			DataAndATGUsageInformation info = new DataAndATGUsageInformation(
					atg.getAttributeGroupUsage(model
							.getAspect("asp.eigenschaften")), daten);
			dav.getLocalConfigurationAuthority().getConfigurationArea()
					.createDynamicObject(simulationsTyp, "simulation.5",
							"Simulation 5", Collections.singletonList(info));
		}
		// else {
		// model.getObject("simulation.5").invalidate();
		// }

		dav.getLocalConfigurationAuthority().getMutableSet("Simulationen").add(
				model.getObject("simulation.5"));

		System.exit(0);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#parseArguments(de.bsvrz.sys.funclib.commandLineArgs.ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		Argument arg = null;
		while (argumentList.hasUnusedArguments()) {
			arg = argumentList.fetchNextArgument();
			if ("SENDER".equals(arg.getName())) {
				erzeugeObjekte = true;
			}
		}
	}

	// /**
	// * {@inheritDoc}.<br>
	// *
	// * @see java.util.TimerTask#run()
	// */
	// @Override
	// public void run() {
	// try {
	// // DynamicObject neuesEreigns = dav.getDataModel()
	// // .createDynamicObject(baustellenTyp, "", "");
	// // System.err.println("Neues Objekt mit ID: " +
	// // neuesEreigns.getId());
	// // baustellen.add(neuesEreigns);
	// } catch (ConfigurationChangeException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
