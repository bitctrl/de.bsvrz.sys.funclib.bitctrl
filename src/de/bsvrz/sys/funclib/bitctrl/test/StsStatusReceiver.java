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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
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

/**
 * Exportiert die Koordinaten der definierten MQ.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class StsStatusReceiver implements StandardApplication,
		ClientReceiverInterface {

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new StsStatusReceiver(), args);
	}

	private final String[] aspekte = { "asp.störfallVerfahrenStandard",
			"asp.störfallVerfahrenMARZ", "asp.störfallVerfahrenNRW",
			"asp.störfallVerfahrenRDS", "asp.störfallVerfahrenFD",
			"asp.störfallVerfahrenVKDiffKfz",
			"asp.störfallVerfahrenConstraint", "asp.störfallVerfahrenFuzzy",
			"asp.störfallVerfahrenMOBINET" };

	private long start;

	/**
	 * 
	 */
	private final Map<Aspect, Collection<SystemObject>> stsMap = new HashMap<Aspect, Collection<SystemObject>>();
	private ClientDavInterface dav;
	private DataDescription desc;

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		start = System.currentTimeMillis();

		this.dav = connection;
		DataModel model = connection.getDataModel();
		AttributeGroup atg = model.getAttributeGroup("atg.störfallZustand");

		System.err.println("Starte Empfänger-Anmeldung");
		for (String aspStr : aspekte) {
			Aspect asp = model.getAspect(aspStr);
			DataDescription desc = new DataDescription(atg, asp);

			Collection<SystemObject> stsObjekte = new HashSet<SystemObject>();
			stsObjekte.addAll(model.getType("typ.straßenTeilSegment")
					.getElements());

			stsMap.put(asp, stsObjekte);

			// for (SystemObject obj : model.getType("typ.straßenTeilSegment")
			// .getElements()) {
			// connection.subscribeReceiver(this, obj, desc, ReceiveOptions
			// .normal(), ReceiverRole.receiver());
			// }
			System.err.println("Anmeldung für Aspekt: " + asp);
			connection.subscribeReceiver(this, stsObjekte, desc, ReceiveOptions
					.normal(), ReceiverRole.receiver(), 0L);
		}
		System.err.println("Empfänger-Anmeldung fertig");
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

	public void update(final ResultData[] results) {
		for (ResultData result : results) {
			if (result.hasData()) {
				Collection<SystemObject> stsObjekte = stsMap.get(result
						.getDataDescription().getAspect());
				stsObjekte.remove(result.getObject());
			}
		}

		int count = 0;
		for (Collection<SystemObject> stsObjekte : stsMap.values()) {
			count += stsObjekte.size();
		}
		if (count <= 0) {
			System.err.println("Alle Daten empfangen nach "
					+ (System.currentTimeMillis() - start) + " ms");
			System.exit(0);
		}
	}

	// @Override
	// public void run() {
	// ResultData[] daten = new ResultData[stsObjekte.size()];
	//
	// int idx = 0;
	// for (SystemObject obj : stsObjekte) {
	// Data data = dav.createData(desc.getAttributeGroup());
	// data.getTimeValue("T").setMillis(60000);
	// data.getUnscaledValue("Situation").set(letzterStatus % 8);
	// data.getTimeValue("Horizont").setMillis(0);
	// data.getItem("Güte").getScaledValue("Index").set(1);
	// data.getItem("Güte").getUnscaledValue("Verfahren").set(0);
	// daten[idx] = new ResultData(obj, desc, System.currentTimeMillis(),
	// data);
	// idx++;
	// }
	//
	// letzterStatus++;
	//
	// try {
	// System.err.println("Sende Daten");
	// dav.sendData(daten);
	// } catch (DataNotSubscribedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SendSubscriptionNotConfirmed e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
