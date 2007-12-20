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
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
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
public class DynamischeMengenWatcher extends TimerTask implements
		StandardApplication, MutableSetChangeListener {

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
		StandardApplicationRunner.run(new DynamischeMengenWatcher(), args);
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
	int letzterStatus = 0;

	/**
	 * Timer für zyklische Datenversendung.
	 */
	Timer timer = new Timer();

	/**
	 * die Liste der Straßenteilsegmente für die der StörfallZustand versendet
	 * werden soll.
	 */
	private final Collection<SystemObject> stsObjekte = new ArrayList<SystemObject>();

	/**
	 * die verwendete Datenverteilerverbindung.
	 */
	private ClientDavInterface dav;

	/**
	 * die Attributgruppe zum Versand des Störfallzustandes.
	 */
	private AttributeGroup atg;

	private MutableSet ereignisse;

	private DynamicObjectType ereignisTyp;

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
		ereignisse = connection.getLocalConfigurationAuthority().getMutableSet(
				"Ereignisse");
		ereignisTyp = (DynamicObjectType) model.getType("typ.ereignis");
		// ereignisse = ((ConfigurationObject) connection.getDataModel()
		// .getObject("RDSNetz")).getMutableSet("Baustellen");
		// ereignisTyp = (DynamicObjectType) model.getType("typ.baustelle");
		System.err.println("Es gibt " + ereignisse.getElements().size()
				+ " Baustellen");

		ereignisse.addChangeListener(this);

		if (erzeugeObjekte) {
			timer.schedule(this, 0L, 60000L);
			ereignisse.remove(ereignisse.getElements().toArray(
					new SystemObject[0]));
		}
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

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		try {
			DynamicObject neuesEreigns = dav.getDataModel()
					.createDynamicObject(ereignisTyp, "", "");
			System.err.println("Neues Objekt mit ID: " + neuesEreigns.getId());
			ereignisse.add(neuesEreigns);
		} catch (ConfigurationChangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(final MutableSet set, final SystemObject[] addedObjects,
			final SystemObject[] removedObjects) {
		// TODO Auto-generated method stub
		System.err.println("Anzahl: " + set.getElements().size() + " NEU: "
				+ addedObjects.length + " ENTFERNT: " + removedObjects.length);
		for (SystemObject obj : addedObjects) {
			System.err.println("Hinzugefügt: " + obj.getId());
		}
		for (SystemObject obj : removedObjects) {
			System.err.println("Entfernt: " + obj.getId());
		}
	}
}
