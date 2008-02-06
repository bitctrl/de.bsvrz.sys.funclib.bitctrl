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

package de.bsvrz.sys.funclib.bitctrl.modell.lms.onlinedaten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum.Status;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte.RdsMeldung;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.onlinedaten.OdRdsMeldung.Daten.RdsVerkehr;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsAuthorisierungsErgebnis;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisDauer;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisKategorie;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisKodierung;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsErinnerungsTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsGeographischeRelevanz;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationFormat;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationKategorie;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationMethode;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationTabelle;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationTabelleTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsNachrichtenKlasse;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsNachrichtenSprache;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsPrioritaet;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsTMCRichtung;

/**
 * Kapselt die Attributgruppe {@code atg.rdsMeldung}.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public class OdRdsMeldung extends AbstractOnlineDatensatz<OdRdsMeldung.Daten>
		implements ParameterDatensatz<OdRdsMeldung.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.rdsBearbeitet}. */
		Bearbeitet("asp.rdsBearbeitet"),

		/** Der Aspekt {@code asp.rdsEmpfangen}. */
		Empfangen("asp.rdsEmpfangen"),

		/** Der Aspekt {@code asp.rdsGeneriert}. */
		Generiert("asp.rdsBearbeitet"),

		/** Der Aspekt {@code asp.rdsSenden}. */
		Senden("asp.rdsSenden"),

		/** Der Aspekt {@code asp.rdsVersendet}. */
		Versendet("asp.rdsVersendet");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 * 
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		private Aspekte(String pid) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Aspekt#getAspekt()
		 */
		public Aspect getAspekt() {
			return aspekt;
		}

		/**
		 * {@inheritDoc}
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Aspekt#getName()
		 */
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum {

		public static class RdsAenderungsInfo {
			private String veranlasser;
			private long modifikationsZeitpunkt;
			private String modifikationsKommentar;

			public RdsAenderungsInfo(final Data item) {
				if (item != null) {
					veranlasser = item.getTextValue("Veranlasser").getText();
					modifikationsZeitpunkt = item.getTimeValue(
							"ModifikationsZeitpunkt").getMillis();
					modifikationsKommentar = item.getTextValue(
							"ModifikationsKommentar").getText();
				}
			}

			public String getModifikationsKommentar() {
				return modifikationsKommentar;
			}

			public long getModifikationsZeitpunkt() {
				return modifikationsZeitpunkt;
			}

			public String getVeranlasser() {
				return veranlasser;
			}
		}

		public static class RdsAttribute {
			private RdsGeographischeRelevanz geographischeRelevanz;

			private boolean vorhersage;

			public RdsAttribute(final Data item) {
				if (item != null) {
					geographischeRelevanz = RdsGeographischeRelevanz
							.getStatus(item.getUnscaledValue(
									"GeographischeRelevanz").intValue());
					vorhersage = item.getUnscaledValue("Vorhersage").intValue() == 0 ? false
							: true;
				}
			}

			public RdsGeographischeRelevanz getGeographischeRelevanz() {
				return geographischeRelevanz;
			}

			public boolean isVorhersage() {
				return vorhersage;
			}
		}

		public static class RdsEreignis {

			private final List<RdsEreignisDaten> ereignisDaten = new ArrayList<RdsEreignisDaten>();

			private boolean ereignisInBeidenRichtungen;
			private RdsEreignisKodierung ereignisKodierung;
			private int ereignisTabelleNummer;
			private String ereignisTabelleVersion;
			private List<RdsEreignisTyp> ereignisTyp;

			public RdsEreignis(final Data item) {
				if (item != null) {
					Data.Array array = item.getArray("EreignisDaten");
					for (int idx = 0; idx < array.getLength(); idx++) {
						ereignisDaten.add(new RdsEreignisDaten(array
								.getItem(idx)));
					}
				}
			}

			public List<RdsEreignisDaten> getEreignisDaten() {
				return ereignisDaten;
			}

			public RdsEreignisKodierung getEreignisKodierung() {
				return ereignisKodierung;
			}

			public int getEreignisTabelleNummer() {
				return ereignisTabelleNummer;
			}

			public String getEreignisTabelleVersion() {
				return ereignisTabelleVersion;
			}

			public List<RdsEreignisTyp> getEreignisTyp() {
				return ereignisTyp;
			}

			public boolean isEreignisInBeidenRichtungen() {
				return ereignisInBeidenRichtungen;
			}
		}

		public static class RdsEreignisDaten {
			private RdsEreignisKategorie ereignisKategorie;
			private int ereignisCode;
			private int empfehlungsCode;
			private int vorhersageCode;
			private RdsEreignisDauer ereignisDauer;
			private final List<RdsEreignisQuantitaet> ereignisQuantitaet = new ArrayList<RdsEreignisQuantitaet>();

			public RdsEreignisDaten(final Data item) {
				if (item != null) {
					ereignisKategorie = RdsEreignisKategorie.getStatus(item
							.getUnscaledValue("EreignisKategorie").intValue());
					ereignisCode = item.getUnscaledValue("EreignisCode")
							.intValue();
					empfehlungsCode = item.getUnscaledValue("EmpfehlungsCode")
							.intValue();
					vorhersageCode = item.getUnscaledValue("VorhersageCode")
							.intValue();
					ereignisDauer = RdsEreignisDauer.getStatus(item
							.getUnscaledValue("EreignisDauer").intValue());

					Data.Array array = item.getArray("EreignisQuantität");
					for (int idx = 0; idx < array.getLength(); idx++) {
						ereignisQuantitaet.add(new RdsEreignisQuantitaet(array
								.getItem(idx)));
					}
				}
			}

			public int getEmpfehlungsCode() {
				return empfehlungsCode;
			}

			public int getEreignisCode() {
				return ereignisCode;
			}

			public RdsEreignisDauer getEreignisDauer() {
				return ereignisDauer;
			}

			public RdsEreignisKategorie getEreignisKategorie() {
				return ereignisKategorie;
			}

			public List<RdsEreignisQuantitaet> getEreignisQuantitaet() {
				return ereignisQuantitaet;
			}

			public int getVorhersageCode() {
				return vorhersageCode;
			}
		}

		public static class RdsEreignisQuantitaet {
			private RdsQuantitaet quantitaetsKennung;

			private String quantitaetsWert;
			private String quantitaetsEinheit;

			public RdsEreignisQuantitaet(final Data item) {
				if (item != null) {
					quantitaetsKennung = new RdsQuantitaet(item
							.getItem("QuantitätsKennung"));
					quantitaetsWert = item.getTextValue("QuantitätsWert")
							.getText();
					quantitaetsEinheit = item.getTextValue("QuantitätsEinheit")
							.getText();
				}
			}

			public String getQuantitätsEinheit() {
				return quantitaetsEinheit;
			}

			public RdsQuantitaet getQuantitätsKennung() {
				return quantitaetsKennung;
			}

			public String getQuantitätsWert() {
				return quantitaetsWert;
			}
		}

		public static class RdsLandesKennung {
			private String landescode;

			private String land;

			public RdsLandesKennung(final SystemObject ref) {
				if (ref != null) {
					Data data = ref.getConfigurationData(ref.getDataModel()
							.getAttributeGroup("atg.rdsLandesKennung"));

					if (data != null) {
						land = data.getTextValue("Land").getText();
						landescode = data.getTextValue("Landescode").getText();
					}
				}
			}

			public String getLand() {
				return land;
			}

			public String getLandescode() {
				return landescode;
			}
		}

		public static class RdsLocation {
			private int locationCode;

			private String strassenNummer;
			private String ausfahrtNummer;
			private int tmcOrtsTyp;
			private RdsLocationKoordinaten koordinaten;
			private String locationKilometrierung;
			private int locationCodeLinienReferenz;
			private int locationCodeGebietsReferenz;
			private int locationNextNegativ;
			private int locationNextPositiv;
			private RdsNachrichten locationNachricht;

			public RdsLocation(final Data item) {
				if (item != null) {
					locationCode = item.getUnscaledValue("LocationCode")
							.intValue();

					strassenNummer = item.getTextValue("StrassenNummer")
							.getText();
					ausfahrtNummer = item.getTextValue("AusfahrtNummer")
							.getText();
					tmcOrtsTyp = item.getUnscaledValue("TmcOrtsTyp").intValue();
					koordinaten = new RdsLocationKoordinaten(item
							.getItem("Koordinaten"));
					locationKilometrierung = item.getTextValue(
							"LocationKilometrierung").getText();
					locationCodeLinienReferenz = item.getUnscaledValue(
							"LocationCodeLinienReferenz").intValue();
					locationCodeGebietsReferenz = item.getUnscaledValue(
							"LocationCodeGebietsReferenz").intValue();
					;
					locationNextNegativ = item.getUnscaledValue(
							"LocationNextNegativ").intValue();
					locationNextPositiv = item.getUnscaledValue(
							"LocationNextPositiv").intValue();
					locationNachricht = new RdsNachrichten(item
							.getItem("LocationNachricht"));
				}
			}

			public String getAusfahrtNummer() {
				return ausfahrtNummer;
			}

			public RdsLocationKoordinaten getKoordinaten() {
				return koordinaten;
			}

			public int getLocationCode() {
				return locationCode;
			}

			public int getLocationCodeGebietsReferenz() {
				return locationCodeGebietsReferenz;
			}

			public int getLocationCodeLinienReferenz() {
				return locationCodeLinienReferenz;
			}

			public String getLocationKilometrierung() {
				return locationKilometrierung;
			}

			public RdsNachrichten getLocationNachricht() {
				return locationNachricht;
			}

			public int getLocationNextNegativ() {
				return locationNextNegativ;
			}

			public int getLocationNextPositiv() {
				return locationNextPositiv;
			}

			public String getStrassenNummer() {
				return strassenNummer;
			}

			public int getTmcOrtsTyp() {
				return tmcOrtsTyp;
			}
		}

		public static class RdsLocationDaten {
			private final List<RdsLocation> rdslocation = new ArrayList<RdsLocation>();

			private RdsLocationFormat rdsLocationFormat;
			private int locationExtent;

			private RdsLocationMethode locationMethode;
			private RdsTMCRichtung tmcRichtung;

			private long locationPrimaerEntfernung;

			private long locationSekundaerEntfernung;
			private int locationVorLocation;

			private int locationNachLocation;
			private RdsLocationKategorie locationKategorie;

			private String locationRichtungTextID;

			public RdsLocationDaten(final Data item) {
				if (item != null) {
					Data.Array array = item.getArray("RDSLocation");
					for (int idx = 0; idx < array.getLength(); idx++) {
						rdslocation.add(new RdsLocation(array.getItem(idx)));
					}

					rdsLocationFormat = RdsLocationFormat.getStatus(item
							.getUnscaledValue("RDSLocationFormat").intValue());
					locationExtent = item.getUnscaledValue("LocationExtent")
							.intValue();
					locationMethode = RdsLocationMethode.getStatus(item
							.getUnscaledValue("LocationMethode").intValue());
					tmcRichtung = RdsTMCRichtung.getStatus(item
							.getUnscaledValue("TMCRichtung").intValue());
					locationPrimaerEntfernung = item.getUnscaledValue(
							"LocationPrimärEntfernung").longValue();
					locationSekundaerEntfernung = item.getUnscaledValue(
							"LocationSekundärEntfernung").longValue();
					locationVorLocation = item.getUnscaledValue(
							"LocationVorLocation").intValue();
					locationNachLocation = item.getUnscaledValue(
							"LocationNachLocation").intValue();
					locationKategorie = RdsLocationKategorie.getStatus(item
							.getUnscaledValue("LocationKategorie").intValue());
					locationRichtungTextID = item.getTextValue(
							"LocationRichtungTextID").getText();
				}

			}

			public int getLocationExtent() {
				return locationExtent;
			}

			public RdsLocationKategorie getLocationKategorie() {
				return locationKategorie;
			}

			public RdsLocationMethode getLocationMethode() {
				return locationMethode;
			}

			public int getLocationNachLocation() {
				return locationNachLocation;
			}

			public long getLocationPrimaerEntfernung() {
				return locationPrimaerEntfernung;
			}

			public String getLocationRichtungTextID() {
				return locationRichtungTextID;
			}

			public long getLocationSekundaerEntfernung() {
				return locationSekundaerEntfernung;
			}

			public int getLocationVorLocation() {
				return locationVorLocation;
			}

			public List<RdsLocation> getRdslocation() {
				return rdslocation;
			}

			public RdsLocationFormat getRdsLocationFormat() {
				return rdsLocationFormat;
			}

			public RdsTMCRichtung getTmcRichtung() {
				return tmcRichtung;
			}
		}

		public static class RdsLocationKoordinaten {
			private double laenge;

			private double breite;

			public RdsLocationKoordinaten(final Data item) {
				if (item != null) {
					laenge = item.getScaledValue("RDSLocationKoordinateX")
							.doubleValue();
					breite = item.getScaledValue("RDSLocationKoordinateY")
							.doubleValue();
				}
			}

			public double getBreite() {
				return breite;
			}

			public double getLaenge() {
				return laenge;
			}
		}

		public static class RdsLocationTabelleInfo {
			private RdsLocationTabelle locationTabelle;

			private String locationTabelleVersion;
			private RdsLocationTabelleTyp locationTabelleTyp;
			private String locationTabelleName;

			public RdsLocationTabelleInfo(final Data item) {
				if (item != null) {
					locationTabelle = RdsLocationTabelle.getStatus(item
							.getUnscaledValue("LocationTabelle").intValue());
					locationTabelleVersion = item.getTextValue(
							"LocationTabelleVersion").getText();
					locationTabelleTyp = RdsLocationTabelleTyp.getStatus(item
							.getUnscaledValue("LocationTabelleTyp").intValue());
					locationTabelleName = item.getTextValue(
							"LocationTabelleName").getText();
				}
			}

			public RdsLocationTabelle getLocationTabelle() {
				return locationTabelle;
			}

			public String getLocationTabelleName() {
				return locationTabelleName;
			}

			public RdsLocationTabelleTyp getLocationTabelleTyp() {
				return locationTabelleTyp;
			}

			public String getLocationTabelleVersion() {
				return locationTabelleVersion;
			}
		}

		public static class RdsNachrichten {
			private final List<RdsNachrichtenDaten> nachrichtenDaten = new ArrayList<RdsNachrichtenDaten>();

			private RdsNachrichtenSprache nachrichtenSprache;

			public RdsNachrichten(final Data item) {
				if (item != null) {
					Data.Array array = item.getArray("NachrichtenDaten");
					for (int idx = 0; idx < array.getLength(); idx++) {
						nachrichtenDaten.add(new RdsNachrichtenDaten(array
								.getItem(idx)));
					}
					nachrichtenSprache = RdsNachrichtenSprache.getStatus(item
							.getUnscaledValue("NachrichtenSprache").intValue());
				}
			}

			public String getNachricht(final RdsNachrichtenKlasse typ) {
				String result = Konstante.LEERSTRING;
				for (RdsNachrichtenDaten eintrag : nachrichtenDaten) {
					if (eintrag.getNachrichtenKlasse().equals(typ)) {
						result = eintrag.getNachrichtenText();
						break;
					}
				}
				return result;
			}

			public List<RdsNachrichtenDaten> getNachrichtenDaten() {
				return nachrichtenDaten;
			}

			public RdsNachrichtenSprache getNachrichtenSprache() {
				return nachrichtenSprache;
			}
		}

		public static class RdsNachrichtenDaten {
			private final RdsNachrichtenKlasse nachrichtenKlasse;

			private final String nachrichtenText;

			public RdsNachrichtenDaten(final Data item) {
				nachrichtenKlasse = RdsNachrichtenKlasse.getStatus(item
						.getUnscaledValue("NachrichtenKlasse").intValue());
				nachrichtenText = item.getTextValue("NachrichtenText")
						.getText();
			}

			public RdsNachrichtenKlasse getNachrichtenKlasse() {
				return nachrichtenKlasse;
			}

			public String getNachrichtenText() {
				return nachrichtenText;
			}
		}

		public static class RdsQuantitaet {
			private String kennung;

			private String beschreibung;
			private String einheit;

			public RdsQuantitaet(final Data item) {
				if (item != null) {
					kennung = item.getTextValue("Kennung").getText();
					beschreibung = item.getTextValue("Beschreibung").getText();
					einheit = item.getTextValue("Einheit").getText();
				}
			}

			public String getBeschreibung() {
				return beschreibung;
			}

			public String getEinheit() {
				return einheit;
			}

			public String getKennung() {
				return kennung;
			}
		}

		public static class RdsText {
			private String text;
			private RdsNachrichtenSprache textSprache;

			public RdsText(final Data item) {
				if (item != null) {
					text = item.getTextValue("Text").getText();
					textSprache = RdsNachrichtenSprache.getStatus(item
							.getUnscaledValue("TextSprache").intValue());
				}
			}

			public String getText() {
				return text;
			}

			public RdsNachrichtenSprache getTextSprache() {
				return textSprache;
			}
		}

		public static class RdsVerkehr {

			private RdsNachrichten nachrichten;

			private RdsText freierText;
			private RdsLocationTabelleInfo locationTabelleInfo;
			private RdsLocationDaten locationDaten;
			private RdsEreignis ereignis;
			private RdsAttribute zusatzAttribute;
			private String alertCCode;

			public RdsVerkehr(final Data item) {
				if (item != null) {
					nachrichten = new RdsNachrichten(item
							.getItem("Nachrichten"));
					freierText = new RdsText(item.getItem("FreierText"));
					locationTabelleInfo = new RdsLocationTabelleInfo(item
							.getItem("LocationTabelleInfo"));
					locationDaten = new RdsLocationDaten(item
							.getItem("LocationDaten"));
					ereignis = new RdsEreignis(item.getItem("Ereignis"));
					zusatzAttribute = new RdsAttribute(item
							.getItem("ZusatzAttribute"));
					alertCCode = item.getTextValue("AlertCCode").getText();
				}
			}

			public String getAlertCCode() {
				return alertCCode;
			}

			public RdsEreignis getEreignis() {
				return ereignis;
			}

			public RdsText getFreierText() {
				return freierText;
			}

			public RdsLocationDaten getLocationDaten() {
				return locationDaten;
			}

			public RdsLocationTabelleInfo getLocationTabelleInfo() {
				return locationTabelleInfo;
			}

			public RdsNachrichten getNachrichten() {
				return nachrichten;
			}

			public RdsAttribute getZusatzAttribute() {
				return zusatzAttribute;
			}
		}

		public static class RdsVerwaltung {
			private String authorisierendeOrganisationsEinheit;

			private String authorisierenderNutzer;

			private long authorisierungsZeit;
			private String authorisierungsKommentar;
			private RdsAuthorisierungsErgebnis authorisierungsErgebnis;
			private int versionsNummer;
			private long aktualisierungsZeit;
			private long aktivierungsZeit;
			private long ablaufZeit;
			private long erinnerungsZeit;
			private long erinnerungsZeitOffset;
			private RdsErinnerungsTyp erinnerungsTyp;
			private long erzeugungsZeit;
			private RdsAenderungsInfo aenderungsInformationen;
			private RdsStatus status;
			private int wichtung;
			private RdsPrioritaet prioritaet;
			private RdsLandesKennung landesKennung;

			public RdsVerwaltung(final Data item) {
				if (item != null) {
					authorisierendeOrganisationsEinheit = item.getTextValue(
							"AuthorisierendeOrganisationsEinheit").getText();
					authorisierenderNutzer = item.getTextValue(
							"AuthorisierenderNutzer").getText();
					authorisierungsZeit = item.getTimeValue(
							"AuthorisierungsZeit").getMillis();
					authorisierungsKommentar = item.getTextValue(
							"AuthorisierungsKommentar").getText();
					authorisierungsErgebnis = RdsAuthorisierungsErgebnis
							.getStatus(item.getUnscaledValue(
									"AuthorisierungsErgebnis").intValue());
					versionsNummer = item.getUnscaledValue("VersionsNummer")
							.intValue();
					aktualisierungsZeit = item.getTimeValue(
							"AktualisierungsZeit").getMillis();
					aktivierungsZeit = item.getTimeValue("AktivierungsZeit")
							.getMillis();
					ablaufZeit = item.getTimeValue("AblaufZeit").getMillis();
					erinnerungsZeit = item.getTimeValue("ErinnerungsZeit")
							.getMillis();
					erinnerungsZeitOffset = item.getTimeValue(
							"ErinnerungsZeitOffset").getMillis();
					erinnerungsTyp = RdsErinnerungsTyp.getStatus(item
							.getUnscaledValue("ErinnerungsTyp").intValue());
					erzeugungsZeit = item.getTimeValue("ErzeugungsZeit")
							.getMillis();
					aenderungsInformationen = new RdsAenderungsInfo(item
							.getItem("ÄnderungsInformationen"));
					status = RdsStatus.getStatus(item
							.getUnscaledValue("Status").intValue());
					wichtung = item.getUnscaledValue("Wichtung").intValue();
					prioritaet = RdsPrioritaet.getStatus(item.getUnscaledValue(
							"Priorität").intValue());
					landesKennung = new RdsLandesKennung(item
							.getReferenceValue("LandesKennung")
							.getSystemObject());
				}
			}

			public long getAblaufZeit() {
				return ablaufZeit;
			}

			public RdsAenderungsInfo getAenderungsInformationen() {
				return aenderungsInformationen;
			}

			public long getAktivierungsZeit() {
				return aktivierungsZeit;
			}

			public long getAktualisierungsZeit() {
				return aktualisierungsZeit;
			}

			public String getAuthorisierendeOrganisationsEinheit() {
				return authorisierendeOrganisationsEinheit;
			}

			public String getAuthorisierenderNutzer() {
				return authorisierenderNutzer;
			}

			public RdsAuthorisierungsErgebnis getAuthorisierungsErgebnis() {
				return authorisierungsErgebnis;
			}

			public String getAuthorisierungsKommentar() {
				return authorisierungsKommentar;
			}

			public long getAuthorisierungsZeit() {
				return authorisierungsZeit;
			}

			public RdsErinnerungsTyp getErinnerungsTyp() {
				return erinnerungsTyp;
			}

			public long getErinnerungsZeit() {
				return erinnerungsZeit;
			}

			public long getErinnerungsZeitOffset() {
				return erinnerungsZeitOffset;
			}

			public long getErzeugungsZeit() {
				return erzeugungsZeit;
			}

			public RdsLandesKennung getLandesKennung() {
				return landesKennung;
			}

			public RdsPrioritaet getPrioritaet() {
				return prioritaet;
			}

			public RdsStatus getStatus() {
				return status;
			}

			public int getVersionsNummer() {
				return versionsNummer;
			}

			public int getWichtung() {
				return wichtung;
			}
		}

		private String infoGuid;
		private String organisation;
		private String nummer;
		private String versionGUID;
		private RdsVerwaltung verwaltung;
		private RdsVerkehr verkehr;

		/**
		 * der aktuelle Status des Datensatzes.
		 */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/**
		 * {@inheritDoc}
		 * 
		 * @see java.lang.Object#clone()
		 */
		@Override
		public Daten clone() {
			Daten klon = new Daten();
			klon.setInfoGuid(infoGuid);
			klon.setOrganisation(organisation);
			klon.setNummer(nummer);
			klon.setVersionGUID(versionGUID);
			klon.setVerwaltung(verwaltung);
			klon.setVerkehr(verkehr);

			klon.setZeitstempel(getZeitstempel());
			return klon;
		}

		/**
		 * {@inheritDoc}.<br>
		 * 
		 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datum#getDatenStatus()
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * @return the infoGuid
		 */
		public String getInfoGuid() {
			return infoGuid;
		}

		/**
		 * @return the nummer
		 */
		public String getNummer() {
			return nummer;
		}

		/**
		 * @return the organisation
		 */
		public String getOrganisation() {
			return organisation;
		}

		/**
		 * @return the verkehr
		 */
		public RdsVerkehr getVerkehr() {
			return verkehr;
		}

		/**
		 * @return the versionGUID
		 */
		public String getVersionGUID() {
			return versionGUID;
		}

		/**
		 * @return the verwaltung
		 */
		public RdsVerwaltung getVerwaltung() {
			return verwaltung;
		}

		/**
		 * setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(Status neuerStatus) {
			this.datenStatus = neuerStatus;
		}

		/**
		 * @param infoGuid
		 *            the infoGuid to set
		 */
		public void setInfoGuid(String infoGuid) {
			this.infoGuid = infoGuid;
		}

		/**
		 * @param nummer
		 *            the nummer to set
		 */
		public void setNummer(String nummer) {
			this.nummer = nummer;
		}

		/**
		 * @param organisation
		 *            the organisation to set
		 */
		public void setOrganisation(String organisation) {
			this.organisation = organisation;
		}

		/**
		 * @param verkehr
		 *            the verkehr to set
		 */
		public void setVerkehr(RdsVerkehr verkehr) {
			this.verkehr = verkehr;
		}

		/**
		 * @param versionGUID
		 *            the versionGUID to set
		 */
		public void setVersionGUID(String versionGUID) {
			this.versionGUID = versionGUID;
		}

		/**
		 * @param verwaltung
		 *            the verwaltung to set
		 */
		public void setVerwaltung(RdsVerwaltung verwaltung) {
			this.verwaltung = verwaltung;
		}
	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_RDS_MELDUNG = "atg.rdsMeldung";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param messstelle
	 *            die Messstelle dessen Daten hier betrachtet werden.
	 */
	public OdRdsMeldung(RdsMeldung meldung) {
		super(meldung);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_RDS_MELDUNG);
			assert atg != null;
		}
	}

	public void abmeldenSender() {
		abmeldenSender(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE));

	}

	public Daten abrufenDatum() {
		return abrufenDatum(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#abrufenDatum(de.bsvrz.dav.daf.main.config.Aspect)
	 */
	@Override
	public Daten abrufenDatum(Aspect asp) {
		return super.abrufenDatum(asp);
	}

	public void addUpdateListener(DatensatzUpdateListener l) {
		addUpdateListener(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL), l);

	}

	public void anmeldenSender() throws AnmeldeException {
		anmeldenSender(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#erzeugeDatum()
	 */
	public Daten erzeugeDatum() {
		return new Daten();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#getAspekte()
	 */
	@Override
	public Collection<Aspect> getAspekte() {
		Set<Aspect> aspekte = new HashSet<Aspect>();
		for (Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		aspekte.add(ObjektFactory.getInstanz().getVerbindung().getDataModel()
				.getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
		aspekte.add(ObjektFactory.getInstanz().getVerbindung().getDataModel()
				.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE));
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	public Daten getDatum() {
		return getDatum(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
	}

	public de.bsvrz.sys.funclib.bitctrl.modell.Datensatz.Status getStatusSendesteuerung() {
		return getStatusSendesteuerung(ObjektFactory.getInstanz()
				.getVerbindung().getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_VORGABE));
	}

	public boolean isAngemeldetSender() {
		return isAngemeldetSender(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE));
	}

	public boolean isAutoUpdate() {
		return isAutoUpdate(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatensatz#konvertiere(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	@Override
	protected Data konvertiere(Daten d) {
		throw new UnsupportedOperationException();
	}

	public void removeUpdateListener(DatensatzUpdateListener l) {
		removeUpdateListener(ObjektFactory.getInstanz().getVerbindung()
				.getDataModel().getAspect(DaVKonstanten.ASP_PARAMETER_SOLL), l);

	}

	public void sendeDaten(Daten datum) throws DatensendeException {
		sendeDaten(ObjektFactory.getInstanz().getVerbindung().getDataModel()
				.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE), datum);
	}

	public void sendeDaten(Daten datum, long timeout)
			throws DatensendeException {
		sendeDaten(ObjektFactory.getInstanz().getVerbindung().getDataModel()
				.getAspect(DaVKonstanten.ASP_PARAMETER_VORGABE), datum, timeout);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setDaten(ResultData result) {
		check(result);

		Daten datum = new Daten();
		Data daten = result.getData();
		if (daten != null) {
			datum.setInfoGuid(daten.getTextValue("InfoGUID").getText());
			datum.setOrganisation(daten.getItem("ID").getTextValue(
					"IDOrganisation").getText());
			datum.setNummer(daten.getItem("ID").getTextValue("IDNummer")
					.getText());
			datum.setVersionGUID(daten.getItem("Version").getTextValue(
					"VersionGUID").getText());
			datum.setVerwaltung(new Daten.RdsVerwaltung(daten
					.getItem("Version").getItem("VerwaltungsInformationen")));
			datum.setVerkehr(new RdsVerkehr(daten.getItem("Version").getItem(
					"VerkehrsInformationen")));
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

	public void update() {
		abrufenDatum(ObjektFactory.getInstanz().getVerbindung().getDataModel()
				.getAspect(DaVKonstanten.ASP_PARAMETER_SOLL));
	}
}
