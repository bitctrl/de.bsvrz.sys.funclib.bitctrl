/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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
/* Copyright by BitCtrl Systems Leipzig */
/* BitCtrl Systems Leipzig */
/* Weisenfelser Str. 67 */
/* 04229 Leipzig */
/* Tel.: +49 341 49067 - 0 */
/* Fax.: +49 341 49067 - 15 */
/* mailto:info@bitctrl.de */
/* http://www.bitctrl.de */
/*---------------------------------------------------------------*/
package de.bsvrz.sys.funclib.bitctrl.testdaten;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientSenderInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.DataNotSubscribedException;
import de.bsvrz.dav.daf.main.OneSubscriptionPerSendData;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.SendSubscriptionNotConfirmed;
import de.bsvrz.dav.daf.main.SenderRole;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Test-Applikation zur Erzeugung einigermaßen realistischer Kurzzeitdaten.
 *
 * @author BitCtrl Systems GmbH, anonymous
 */
public final class UfdTestSender extends TimerTask implements
		StandardApplication, ClientSenderInterface {

	/**
	 * der Logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(KzdTestSender.class
			.getName());

	/**
	 * Liste aller Umfelddatensensoren mit ihrem aktuellen Verbindungszustand.
	 */
	private final Collection<SystemObject> umfeldDatenSensorenListe = new ArrayList<>();

	private final Timer checkTimer = new Timer("UfdCheckTimer", true);

	private long startZeit;

	/**
	 * intervall, in dem neue Daten erzeugt werden.
	 */
	private final long delay = 60000L;

	private ClientDavInterface con;

	private boolean aktiv;

	private final Map<SystemObject, ResultData> latestResultsKzd = new LinkedHashMap<>();

	/**
	 * Standardkonstruktor.
	 */
	private UfdTestSender() {
		super();
	}

	/**
	 * Prüft die Verfügbarkeit von Kurzzeitdaten aller Fahrstreifen<br>
	 * und sendet für die Stationen, die länger als X Minuten keine KZD
	 * empfangen haben eine Warnung an die BMV.
	 */
	@Override
	public void run() {
		final long now = System.currentTimeMillis();
		if (startZeit == 0) {
			startZeit = (now / 60000L) * 60000L;
		}

		LOGGER.fine("Sende Daten für " + umfeldDatenSensorenListe.size() //$NON-NLS-1$
				+ " Umfelddatensensoren");

		int loop = 0;
		while ((loop < 20) && (startZeit < now)) {
			for (final SystemObject sensor : umfeldDatenSensorenListe) {
				try {
					Data data;
					data = getUmfelddatenDaten(sensor);
					final SystemObjectType typ = sensor.getType();
					final Aspect asp = con.getDataModel().getAspect(
							"asp.externeErfassung");
					final AttributeGroup atg = con.getDataModel()
							.getAttributeGroup(
									"atg."
											+ typ.getPidOrNameOrId().replace(
													"typ.", ""));
					final DataDescription desc = new DataDescription(atg, asp);
					final ResultData result = new ResultData(sensor, desc,
							startZeit, data);
					latestResultsKzd.put(sensor, result);
					if (aktiv) {
						con.sendData(result);
					}
				} catch (final DataNotSubscribedException e) {
					LOGGER.warning(e.getLocalizedMessage());
				} catch (final SendSubscriptionNotConfirmed e) {
					LOGGER.warning(e.getLocalizedMessage());
				} catch (final Exception e) {
					LOGGER.severe(e.getLocalizedMessage());
				}
			}
			startZeit += (1L * delay);
			loop++;
		}
	}

	@Override
	public void initialize(final ClientDavInterface dav) throws Exception {
		con = dav;

		final Aspect asp = dav.getDataModel().getAspect("asp.externeErfassung"); //$NON-NLS-1$

		umfeldDatenSensorenListe.addAll(dav.getDataModel()
				.getType("typ.umfeldDatenSensor").getElements());

		synchronized (umfeldDatenSensorenListe) {
			try {
				for (final SystemObject obj : umfeldDatenSensorenListe) {
					final ResultData initialData = latestResultsKzd.get(obj);
					if (initialData != null) {
						con.subscribeSource(this, initialData);
					} else {
						final SystemObjectType typ = obj.getType();
						final AttributeGroup atg = con.getDataModel()
								.getAttributeGroup(
										"atg."
												+ typ.getPidOrNameOrId()
														.replace("typ.", ""));
						con.subscribeSender(this, obj, new DataDescription(atg,
								asp), SenderRole.source());
					}
				}
			} catch (final OneSubscriptionPerSendData e) {
				LOGGER.severe(e.getLocalizedMessage());
			} catch (final Exception e) {
				LOGGER.severe(e.getLocalizedMessage());
			}
			aktiv = true;

			checkTimer.scheduleAtFixedRate(this, 1000L, delay);
		}
	}

	@Override
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		final String startStr = argumentList.fetchArgument("-startDatum=") //$NON-NLS-1$
				.asString();
		if ((startStr != null) && (startStr.length() > 0)) {
			startZeit = DateFormat.getDateInstance().parse(startStr).getTime();
		}

	}

	/**
	 * Die Hauptfunktion des Observers.
	 *
	 * @param args
	 *            Programm-Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new UfdTestSender(), args);
	}

	@Override
	public void dataRequest(final SystemObject object,
			final DataDescription dataDescription, final byte state) {
		if (state == ClientSenderInterface.START_SENDING) {
			System.out.println("los");
		}

	}

	@Override
	public boolean isRequestSupported(final SystemObject object,
			final DataDescription dataDescription) {
		boolean result = false;
		if (umfeldDatenSensorenListe.contains(object)) {
			result = true;
		} else {
			LOGGER.warning("Unerwarteter Request: Attributgruppe "
					+ dataDescription.getAttributeGroup().getName()
					+ " Aspekt " + dataDescription.getAspect().getName()
					+ " Objekt " + object.getPid());
		}
		return result;
	}

	/**
	 * @return die Daten
	 * @throws Exception
	 */
	private Data getUmfelddatenDaten(final SystemObject sensor)
			throws Exception {

		final SystemObjectType typ = sensor.getType();
		final AttributeGroup atg = con.getDataModel().getAttributeGroup(
				"atg." + typ.getPidOrNameOrId().replace("typ.", ""));

		final Data result = con.createData(atg);
		result.setToDefault();
		result.getTimeValue("T").setSeconds(60);
		final Data embbededItem = result.getItem(typ.getPidOrNameOrId()
				.replace("typ.ufds", ""));

		embbededItem.getScaledValue("Wert").set(new Random().nextDouble());
		final Data statusItem = embbededItem.getItem("Status");
		statusItem.getItem("Erfassung").getUnscaledValue("NichtErfasst")
				.setText("Nein");

		statusItem.getItem("PlFormal").getUnscaledValue("WertMax")
				.setText("Nein");
		statusItem.getItem("PlFormal").getUnscaledValue("WertMin")
				.setText("Nein");
		statusItem.getItem("MessWertErsetzung").getUnscaledValue("Implausibel")
				.setText("Nein");
		statusItem.getItem("MessWertErsetzung")
				.getUnscaledValue("Interpoliert").setText("Nein");

		final Data gueteItem = embbededItem.getItem("Güte");
		gueteItem.getUnscaledValue("Index").set(-1); //$NON-NLS-1$
		gueteItem.getUnscaledValue("Verfahren").set(0);

		return result;
	}

}
