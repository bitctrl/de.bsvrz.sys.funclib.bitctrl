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
import java.util.Timer;
import java.util.TimerTask;

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
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Exportiert die Koordinaten der definierten MQ.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class StsStatusSender extends TimerTask implements StandardApplication,
		ClientSenderInterface {

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new StsStatusSender(), args);
	}

	private final String[] aspekte = { "asp.störfallVerfahrenStandard",
			"asp.störfallVerfahrenMARZ", "asp.störfallVerfahrenNRW",
			"asp.störfallVerfahrenRDS", "asp.störfallVerfahrenFD",
			"asp.störfallVerfahrenVKDiffKfz",
			"asp.störfallVerfahrenConstraint", "asp.störfallVerfahrenFuzzy",
			"asp.störfallVerfahrenMOBINET" };

	int letzterStatus = 0;

	/**
	 * 
	 */
	Timer timer = new Timer();
	private Collection<SystemObject> stsObjekte;
	private ClientDavInterface dav;

	private AttributeGroup atg;

	public void dataRequest(final SystemObject object,
			final DataDescription dataDescription, final byte state) {
		// System.err.println("DataReq: " + object + ", " + dataDescription + ",
		// "
		// + state);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		this.dav = connection;
		DataModel model = connection.getDataModel();
		atg = model.getAttributeGroup("atg.störfallZustand");
		stsObjekte = model.getType("typ.straßenTeilSegment").getElements();

		System.err.println("Starte Anmeldung");
		for (String aspStr : aspekte) {
			Aspect asp = model.getAspect(aspStr);
			DataDescription desc = new DataDescription(atg, asp);
			connection.subscribeSender(this, stsObjekte, desc, SenderRole
					.source());
		}
		System.err.println("Anmeldung fertig");

		timer.schedule(this, 0L, 5000L);
	}

	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
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

	@Override
	public void run() {
		ResultData[] daten = new ResultData[stsObjekte.size()];

		for (String aspStr : aspekte) {
			Aspect asp = dav.getDataModel().getAspect(aspStr);
			DataDescription desc = new DataDescription(atg, asp);

			int idx = 0;
			for (SystemObject obj : stsObjekte) {
				Data data = dav.createData(desc.getAttributeGroup());
				data.getTimeValue("T").setMillis(60000);
				data.getUnscaledValue("Situation").set(letzterStatus % 8);
				data.getTimeValue("Horizont").setMillis(0);
				data.getItem("Güte").getScaledValue("Index").set(1);
				data.getItem("Güte").getUnscaledValue("Verfahren").set(0);
				daten[idx] = new ResultData(obj, desc, System
						.currentTimeMillis(), data);
				idx++;
			}
		}

		letzterStatus++;

		try {
			System.err.println("Sende Daten");
			dav.sendData(daten);
		} catch (DataNotSubscribedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SendSubscriptionNotConfirmed e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
