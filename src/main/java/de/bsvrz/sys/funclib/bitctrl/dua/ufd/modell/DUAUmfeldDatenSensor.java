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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.ufd.modell;

import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ClientReceiverInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ReceiveOptions;
import de.bsvrz.dav.daf.main.ReceiverRole;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.UmfeldDatenSensorUnbekannteDatenartException;
import de.bsvrz.sys.funclib.bitctrl.dua.ufd.typen.UmfeldDatenArt;

/**
 * Korrespondiert mit einem Objekt vom Typ <code>typ.umfeldDatenSensor</code>
 * und stellt alle Konfigurationsdaten, sowie die Parameter der
 * Messwertersetzung zur Verf�gung.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class DUAUmfeldDatenSensor implements ClientReceiverInterface {

	/**
	 * statische Instanzen dieser Klasse.
	 */
	private static Map<SystemObject, DUAUmfeldDatenSensor> instanzen = new HashMap<SystemObject, DUAUmfeldDatenSensor>();

	/**
	 * das Systemobjekt.
	 */
	private final SystemObject objekt;

	/**
	 * Maximaler Zeitbereich, �ber den eine Messwertersetzung f�r diesen Sensor
	 * durchgef�hrt wird.
	 */
	private long maxZeitMessWertErsetzung = -1;

	/**
	 * Maximaler Zeitbereich, �ber den eine Messwertfortschreibung bei
	 * implausiblen Werten stattfindet.
	 */
	private long maxZeitMessWertFortschreibung = -1;

	/**
	 * Die Umfelddatenmessstelle vorher.
	 */
	private SystemObject vorgaenger;

	/**
	 * Die Umfelddatenmessstelle nachher.
	 */
	private SystemObject nachfolger;

	/**
	 * Ersatzsensor dieses Umfelddatensensors f�r die Messwertersetzung.
	 */
	private SystemObject ersatzSensor;

	/**
	 * Zeigt an, ob dieser Sensor der Hauptsensor f�r diesen Sensortyp an der
	 * Umfelddatenmessstelle, oder ein(er von mehreren) Nebensensoren f�r diesen
	 * Sensortyp an der Umfelddatenmessstelle ist.
	 */
	private boolean hauptSensor;

	/**
	 * Die Umfelddatenart dieses Sensors.
	 */
	private final UmfeldDatenArt datenArt;

	/**
	 * Erfragt die statische Instanz dieser Klasse, die mit dem uebergebenen
	 * Systemobjekt assoziiert ist (nicht vorhandene werden ggf. angelegt)
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            ein Systemobjekt eines Umfelddatensensors
	 * @return die statische Instanz dieser Klasse, die mit dem uebergebenen
	 *         Systemobjekt assoziiert ist
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	static final DUAUmfeldDatenSensor getInstanz(final ClientDavInterface dav,
			final SystemObject objekt)
					throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException(
					"Umfelddatensensor mit Systemobjekt <<null>> existiert nicht"); //$NON-NLS-1$
		}

		DUAUmfeldDatenSensor instanz = instanzen.get(objekt);

		if (instanz == null) {
			instanz = new DUAUmfeldDatenSensor(dav, objekt);
			instanzen.put(objekt, instanz);
		}

		return instanz;
	}

	/**
	 * Standardkonstruktor.
	 *
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param objekt
	 *            das Systemobjekt des Umfelddatensensors
	 * @throws UmfeldDatenSensorUnbekannteDatenartException
	 */
	protected DUAUmfeldDatenSensor(final ClientDavInterface dav,
			final SystemObject objekt)
					throws UmfeldDatenSensorUnbekannteDatenartException {
		if (objekt == null) {
			throw new NullPointerException(
					"Als Umfelddatensensor wurde <<null>> uebergeben"); //$NON-NLS-1$
		}
		this.objekt = objekt;

		datenArt = UmfeldDatenArt.getUmfeldDatenArtVon(objekt);
		if (datenArt == null) {
			throw new UmfeldDatenSensorUnbekannteDatenartException(
					"Datenart von Umfelddatensensor " + this.objekt + //$NON-NLS-1$
					" (" + objekt.getType()
					+ ") konnte nicht identifiziert werden"); //$NON-NLS-1$
		}

		final ConfigurationObject konfigObjekt = (ConfigurationObject) objekt;
		final Data konfigDaten = konfigObjekt.getConfigurationData(
				dav.getDataModel().getAttributeGroup("atg.umfeldDatenSensor")); //$NON-NLS-1$

		if (konfigDaten != null) {
			if (konfigDaten.getReferenceValue("Vorg�nger") != null) { //$NON-NLS-1$
				vorgaenger = konfigDaten.getReferenceValue("Vorg�nger") //$NON-NLS-1$
						.getSystemObject();
			}
			if (konfigDaten.getReferenceValue("Nachfolger") != null) { //$NON-NLS-1$
				nachfolger = konfigDaten.getReferenceValue("Nachfolger") //$NON-NLS-1$
						.getSystemObject();
			}

			if (konfigDaten.getReferenceValue("ErsatzSensor") != null) { //$NON-NLS-1$
				ersatzSensor = konfigDaten.getReferenceValue("ErsatzSensor") //$NON-NLS-1$
						.getSystemObject();
			}
			hauptSensor = konfigDaten.getUnscaledValue("Typ").intValue() == 0; //$NON-NLS-1$
		}

		final DataDescription parameterBeschreibung = new DataDescription(
				dav.getDataModel()
				.getAttributeGroup("atg.ufdsMessWertErsetzung"), //$NON-NLS-1$
				dav.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
		dav.subscribeReceiver(this, objekt, parameterBeschreibung,
				ReceiveOptions.normal(), ReceiverRole.receiver());
	}

	/**
	 * Erfragt die Umfelddatenart dieses Sensors.
	 *
	 * @return die Umfelddatenart dieses Sensors
	 */
	public final UmfeldDatenArt getDatenArt() {
		return datenArt;
	}

	/**
	 * Erfragt die Umfelddatenmessstelle vorher.
	 *
	 * @return die Umfelddatenmessstelle vorher oder <code>null</code>, wenn
	 *         diese nicht konfiguriert ist
	 */
	public final SystemObject getVorgaenger() {
		return vorgaenger;
	}

	/**
	 * Ergagt die Umfelddatenmessstelle nachher.
	 *
	 * @return die Umfelddatenmessstelle nachher oder <code>null</code>, wenn
	 *         diese nicht konfiguriert ist
	 */
	public final SystemObject getNachfolger() {
		return nachfolger;
	}

	/**
	 * Erfragt den Ersatzsensor dieses Umfelddatensensors f�r die
	 * Messwertersetzung.
	 *
	 * @return der Ersatzsensor dieses Umfelddatensensors f�r die
	 *         Messwertersetzung oder <code>null</code>, wenn dieser nicht
	 *         konfiguriert ist
	 */
	public final SystemObject getErsatzSensor() {
		return ersatzSensor;
	}

	/**
	 * Erfragt, ob dieser Sensor der Hauptsensor f�r diesen Sensortyp an der
	 * Umfelddatenmessstelle, oder ein(er von mehreren) Nebensensoren f�r diesen
	 * Sensortyp an der Umfelddatenmessstelle ist.
	 *
	 * @return ob dieser Sensor der Hauptsensor ist
	 */
	public final boolean isHauptSensor() {
		return hauptSensor;
	}

	@Override
	public void update(final ResultData[] resultate) {
		if (resultate != null) {
			for (final ResultData resultat : resultate) {
				if ((resultat != null) && (resultat.getData() != null)) {
					final Data ufdsMessWertErsetzungData = resultat.getData();
					maxZeitMessWertErsetzung = ufdsMessWertErsetzungData
							.getTimeValue("maxZeitMessWertErsetzung") //$NON-NLS-1$
							.getMillis();

					maxZeitMessWertFortschreibung = ufdsMessWertErsetzungData
							.getTimeValue("maxZeitMessWertFortschreibung") //$NON-NLS-1$
							.getMillis();
				}
			}
		}
	}

	/**
	 * Erfragt den maximalen Zeitbereich, �ber den eine Messwertersetzung f�r
	 * diesen Sensor durchgef�hrt wird.
	 *
	 * @return maximaler Zeitbereich, �ber den eine Messwertersetzung f�r diesen
	 *         Sensor durchgef�hrt wird
	 */
	public final long getMaxZeitMessWertErsetzung() {
		return maxZeitMessWertErsetzung;
	}

	/**
	 * Erfragt den maximalen Zeitbereich, �ber den eine Messwertfortschreibung
	 * bei implausiblen Werten stattfindet.
	 *
	 * @return maximaler Zeitbereich, �ber den eine Messwertfortschreibung bei
	 *         implausiblen Werten stattfindet
	 */
	public final long getMaxZeitMessWertFortschreibung() {
		return maxZeitMessWertFortschreibung;
	}

	/**
	 * Erfragt das Systemobjekt.
	 *
	 * @return das Systemobjekt
	 */
	public final SystemObject getObjekt() {
		return objekt;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean ergebnis = false;

		if ((obj != null) && (obj instanceof DUAUmfeldDatenSensor)) {
			final DUAUmfeldDatenSensor that = (DUAUmfeldDatenSensor) obj;
			ergebnis = objekt.equals(that.objekt);
		}

		return ergebnis;
	}

	@Override
	public String toString() {
		return objekt.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((datenArt == null) ? 0 : datenArt.hashCode());
		result = (prime * result) + ((ersatzSensor == null) ? 0 : ersatzSensor.hashCode());
		result = (prime * result) + (hauptSensor ? 1231 : 1237);
		result = (prime * result) + (int) (maxZeitMessWertErsetzung ^ (maxZeitMessWertErsetzung >>> 32));
		result = (prime * result) + (int) (maxZeitMessWertFortschreibung ^ (maxZeitMessWertFortschreibung >>> 32));
		result = (prime * result) + ((nachfolger == null) ? 0 : nachfolger.hashCode());
		result = (prime * result) + ((objekt == null) ? 0 : objekt.hashCode());
		result = (prime * result) + ((vorgaenger == null) ? 0 : vorgaenger.hashCode());
		return result;
	}

	/** entfernt die gespeicherten Instanzen f�r Testzwecke.
	 *
	 * TODO pr�fen, wozu das gut ist!
	 */
	protected static void resetCache() {
		instanzen.clear();
	}

}