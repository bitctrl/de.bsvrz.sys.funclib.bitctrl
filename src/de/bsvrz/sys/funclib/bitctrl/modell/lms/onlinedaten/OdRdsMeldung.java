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

import com.bitctrl.Constants;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensendeException;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte.RdsMeldung;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.objekte.RdsQuantitaet;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.onlinedaten.OdRdsMeldung.Daten.RdsVerkehr;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsAuthorisierungsErgebnis;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEmpfehlungsCode;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisCode;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisDauer;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisKategorie;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisKodierung;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsEreignisTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsErinnerungsTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsGeographischeRelevanz;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationFormat;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationKategorie;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationMethode;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationRichtungTextID;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationTabelle;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsLocationTabelleTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsNachrichtenKlasse;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsNachrichtenSprache;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsPrioritaet;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsTMCRichtung;
import de.bsvrz.sys.funclib.bitctrl.modell.lms.zustaende.RdsVorhersageCode;

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
			DataModel modell = ObjektFactory.getInstanz().getVerbindung().getDataModel();
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

		/**
		 * Die Struktur zur Speicherung der RDS-Änderungs-Informationen.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsAenderungsInfo {
			/** der Name des Veranlassers der Änderung. */
			private String veranlasser;

			/** der Zeitpunkt der Änderung. */
			private Long modifikationsZeitpunkt;

			/** ein Kommentar zur Änderung. */
			private String modifikationsKommentar;

			/**
			 * Konstruktor. Die Informationen werden als Datenverteiler-Daten
			 * übergeben.
			 * 
			 * @param daten
			 *            die vom Datenverteiler empfangenen Daten
			 */
			public RdsAenderungsInfo(final Data daten) {
				if (daten != null) {
					veranlasser = daten.getTextValue("Veranlasser").getText();
					modifikationsZeitpunkt = daten.getTimeValue(
							"ModifikationsZeitpunkt").getMillis();
					modifikationsKommentar = daten.getTextValue(
							"ModifikationsKommentar").getText();
				}
			}

			/**
			 * liefert den Kommentar der Änderung. Wenn kein Kommentar gesetzt
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return den Kommentar oder <code>null</code>
			 */
			public String getModifikationsKommentar() {
				return modifikationsKommentar;
			}

			/**
			 * liefert den Zeitpunkt, an dem die Änderung vorgenommen wurde.
			 * Wenn der Zeitpunkt nicht definiert wurde, wird der Wert
			 * <code>null</code> geliefert.
			 * 
			 * @return den Zeitpunkt oder <code>null</code>
			 */
			public Long getModifikationsZeitpunkt() {
				return modifikationsZeitpunkt;
			}

			/**
			 * liefert den Name des Veranlassers der Änderung. Wurde der Name
			 * nicht festgelegt, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return den Name oder <code>null</code>
			 */
			public String getVeranlasser() {
				return veranlasser;
			}
		}

		/**
		 * Repräsentatiopn eines RDS-Attributs.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsAttribute {
			/** Attribut zur Speicherung der geographischen Relevanz. */
			private RdsGeographischeRelevanz geographischeRelevanz;

			/** Kennzeichen für eine Vorhersage. */
			private boolean vorhersage;

			/**
			 * Konstruktor. Der Inhalt des Attributs wird aus den übergebenen
			 * Datenverteilerdaten ermittelt.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsAttribute(final Data daten) {
				if (daten != null) {
					geographischeRelevanz = RdsGeographischeRelevanz.getStatus(daten.getUnscaledValue(
							"GeographischeRelevanz").intValue());
					if (daten.getUnscaledValue("Vorhersage").intValue() != 0) {
						vorhersage = true;
					}
				}
			}

			/**
			 * liefert die Geografische Relevanz des Attributs. Wenn die
			 * Relevanz nicht gesetzt wurde, wird der Wert <code>null</code>
			 * geliefert. Wenn der Datenverteilerdatensatz einen unbekannten
			 * Code für die geographische Relevanz geliefert hat wird der
			 * Standardwert {@link RdsGeographischeRelevanz#NATIONAL} geliefert.
			 * 
			 * @return die Relevanz oder <code>null</code>
			 */
			public RdsGeographischeRelevanz getGeographischeRelevanz() {
				return geographischeRelevanz;
			}

			/**
			 * bestimmt, ob es sich um einen Vorhersage handelt.
			 * 
			 * @return <code>true</code>, wenn es sich um eine Vorhersage
			 *         handelt
			 */
			public boolean isVorhersage() {
				return vorhersage;
			}
		}

		/**
		 * Repräsentation eines RDS-Ereignisses innerhalb einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsEreignis {

			/** die Liste der Ereignisdaten. */
			private final List<RdsEreignisDaten> ereignisDaten = new ArrayList<RdsEreignisDaten>();

			/** markiert, ob das Ereignis für beide Richtungen gültig ist. */
			private boolean ereignisInBeidenRichtungen;

			/** die Ereigniskodierung. */
			private RdsEreignisKodierung ereignisKodierung;

			/** die Nummer der verwendeten Ereignistabelle. */
			private int ereignisTabelleNummer;

			/** die Version der Ereignistabelle. */
			private String ereignisTabelleVersion;

			/** die Liste der zugeordneten Ereignistypen. */
			private List<RdsEreignisTyp> ereignisTyp = new ArrayList<RdsEreignisTyp>();

			/**
			 * Konstruktor. Die Daten des Ereignisses werden aus dem übergebenen
			 * Datenverteiler-Datensatz ermittelt.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsEreignis(final Data daten) {
				if (daten != null) {
					Data.Array array = daten.getArray("EreignisDaten");
					for (int idx = 0; idx < array.getLength(); idx++) {
						ereignisDaten.add(new RdsEreignisDaten(
								array.getItem(idx)));
					}
					if (daten.getUnscaledValue("EreignisInBeidenRichtungen").intValue() != 0) {
						ereignisInBeidenRichtungen = true;
					}
					ereignisKodierung = RdsEreignisKodierung.getStatus(daten.getUnscaledValue(
							"EreignisKodierung").intValue());
					ereignisTabelleNummer = daten.getUnscaledValue(
							"EreignisTabelleNummer").intValue();
					ereignisTabelleVersion = daten.getTextValue(
							"EreignisTabelleVersion").getText();

					array = daten.getArray("EreignisTyp");
					for (int idx = 0; idx < array.getLength(); idx++) {
						ereignisTyp.add(RdsEreignisTyp.getStatus(array.getUnscaledValue(
								idx).intValue()));
					}
				}
			}

			/**
			 * liefert die Liste der Ereignisdaten.
			 * 
			 * @return die Liste
			 */
			public List<RdsEreignisDaten> getEreignisDaten() {
				return ereignisDaten;
			}

			/**
			 * liefert die Ereigniskodierung. Wenn die Kodierung nicht gesetzt
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Kodierung oder <code>null</code>
			 */
			public RdsEreignisKodierung getEreignisKodierung() {
				return ereignisKodierung;
			}

			/**
			 * liefert die Nummer der verwendeten Eregnistabelle. Wenn keine
			 * Nummer gesetzt wurde wird 0 geliefert.
			 * 
			 * @return die Nummer
			 */
			public int getEreignisTabelleNummer() {
				return ereignisTabelleNummer;
			}

			/**
			 * liefert die Version der Ereignistabelle. Wenn der Eintrag nicht
			 * gesetzt wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Version oder <code>null</code>
			 */
			public String getEreignisTabelleVersion() {
				return ereignisTabelleVersion;
			}

			/**
			 * liefert die Liste der zugeordneten Ereignistypen.
			 * 
			 * @return die Liste
			 */
			public List<RdsEreignisTyp> getEreignisTyp() {
				return ereignisTyp;
			}

			/**
			 * liefert eine Aussage, ob das Ereignis für beide Fahrtrichtungen
			 * relevant ist.
			 * 
			 * @return <code>true</code>, wenn das Ereignis für beide
			 *         Fahrtrichtungen git
			 */
			public boolean isEreignisInBeidenRichtungen() {
				return ereignisInBeidenRichtungen;
			}
		}

		/**
		 * Datenstruktur für die Definition der Ereignisdaten eines
		 * {@link RdsEreignis}.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsEreignisDaten {
			/** die Kategorie des Ereignisses. */
			private RdsEreignisKategorie ereignisKategorie;

			/** der Code des Ereignisses. */
			private RdsEreignisCode ereignisCode;

			/** der Empfehlungscode. */
			private RdsEmpfehlungsCode empfehlungsCode;

			/** der Vorhersagecode. */
			private RdsVorhersageCode vorhersageCode;

			/** die Dauer des Ereignisses (qualitativ). */
			private RdsEreignisDauer ereignisDauer;

			/** die Liste der zugeordneten Ereignisquantitäten. */
			private final List<RdsEreignisQuantitaet> ereignisQuantitaet = new ArrayList<RdsEreignisQuantitaet>();

			/**
			 * Konstruktor. Der Inhalt der Datnstruktur wird aus dem übergebenen
			 * Datenverteiler-Datensatz ermittelt.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsEreignisDaten(final Data daten) {
				if (daten != null) {
					ereignisKategorie = RdsEreignisKategorie.getKategorie(daten.getUnscaledValue(
							"EreignisKategorie").intValue());
					ereignisCode = RdsEreignisCode.getEreignisCode(daten.getUnscaledValue(
							"EreignisCode").intValue());
					empfehlungsCode = RdsEmpfehlungsCode.getEmpfehlungsCode(daten.getUnscaledValue(
							"EmpfehlungsCode").intValue());
					vorhersageCode = RdsVorhersageCode.getVorhersagecode(daten.getUnscaledValue(
							"VorhersageCode").intValue());
					ereignisDauer = RdsEreignisDauer.getStatus(daten.getUnscaledValue(
							"EreignisDauer").intValue());

					Data.Array array = daten.getArray("EreignisQuantität");
					for (int idx = 0; idx < array.getLength(); idx++) {
						ereignisQuantitaet.add(new RdsEreignisQuantitaet(
								array.getItem(idx)));
					}
				}
			}

			/**
			 * liefert den zugewiesenen Emnpfehlungscode. Wenn keine Empfehlung
			 * zugeordnet wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return den Code oder <code>null</code>
			 */
			public RdsEmpfehlungsCode getEmpfehlungsCode() {
				return empfehlungsCode;
			}

			/**
			 * liefert den zugewiesenen Ereigniscode. Wenn kein Code zugeordnet
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return den Code oder <code>null</code>
			 */
			public RdsEreignisCode getEreignisCode() {
				return ereignisCode;
			}

			/**
			 * liefert die qualitative Dauer des Ereignisses. Wurde das
			 * Atztribut nicht gesetzt, wird der Wert <code>null</code>
			 * geliefert. Enthält der Datenverteiler-Datensatz einen unbekannten
			 * Code für die Ereignisdauer, wird der Standardwert
			 * {@link RdsEreignisDauer#UNBEKANNT} geliefert.
			 * 
			 * @return die Dauer oder <code>null</code>
			 */
			public RdsEreignisDauer getEreignisDauer() {
				return ereignisDauer;
			}

			/**
			 * liefert die zugeordnete Ereigniskategorie. Wurde keine Kategorie
			 * definiert, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Kategorie oder <code>null</code>
			 */
			public RdsEreignisKategorie getEreignisKategorie() {
				return ereignisKategorie;
			}

			/**
			 * liefert die Liste der zugeordneten Ereignisquantitäten.
			 * 
			 * @return die Liste
			 */
			public List<RdsEreignisQuantitaet> getEreignisQuantitaet() {
				return ereignisQuantitaet;
			}

			/**
			 * liefert den zugewiesenen Vorhersagecode. Wurde keine Vorhersage
			 * definiert, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return den Code oder <code>null</code>
			 */
			public RdsVorhersageCode getVorhersageCode() {
				return vorhersageCode;
			}
		}

		/**
		 * Repräsentation einer RDS-Ereignis-Quantität innerhalb einer
		 * RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsEreignisQuantitaet {
			/** die Kennung der Quantität. */
			private RdsQuantitaet quantitaetsKennung;

			/** der Wert der Quantität. */
			private String quantitaetsWert;

			/** die Einheit der Quantität. */
			private String quantitaetsEinheit;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datenverteiler-Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsEreignisQuantitaet(final Data daten) {
				if (daten != null) {
					quantitaetsKennung = new RdsQuantitaet(
							daten.getReferenceValue("QuantitätsKennung").getSystemObject());
					quantitaetsWert = daten.getTextValue("QuantitätsWert").getText();
					quantitaetsEinheit = daten.getTextValue("QuantitätsEinheit").getText();
				}
			}

			/**
			 * liefert die Einheit der Quantität. Wenn keine Einheit definiert
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Einheit oder <code>null</code>
			 */
			public String getQuantitaetsEinheit() {
				return quantitaetsEinheit;
			}

			/**
			 * liefert die Kennung der Quantität. Wenn keine Kennung bekannt
			 * wird, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Kennung oder <code>null</code>
			 */
			public RdsQuantitaet getQuantitaetsKennung() {
				return quantitaetsKennung;
			}

			/**
			 * liefert den Wert der Quantität, wen kein Wert definiert wurde,
			 * wird <code>null</code> geliefert.
			 * 
			 * @return den Wert oder <code>null</code>
			 */
			public String getQuantitaetsWert() {
				return quantitaetsWert;
			}
		}

		/**
		 * Repräsentation der Struktur zur Definition der Landeskennung
		 * innerhalb einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsLandesKennung {

			/** der Landescode. */
			private String landescode;

			/** die Bezeichung des Landes. */
			private String land;

			/**
			 * Konstruktor. Die konfigurierenden Daten der Attributgruppe
			 * "atg.rdsLandesKennung" des übergebenen Systemobjekts werden
			 * verwendet, um die Attribute der Landeskennung zu ermitteln.
			 * 
			 * @param ref
			 *            das Systemobjekt, an dem die Daten für die
			 *            Landeskennung abgelegt sind
			 */
			public RdsLandesKennung(final SystemObject ref) {
				if (ref != null) {
					Data data = ref.getConfigurationData(ref.getDataModel().getAttributeGroup(
							"atg.rdsLandesKennung"));

					if (data != null) {
						land = data.getTextValue("Land").getText();
						landescode = data.getTextValue("Landescode").getText();
					}
				}
			}

			/**
			 * liefert die Bezeichnung des Landes. Wenn keine entsprechende
			 * Bezeichnung definiert ist, wird der Wert <code>null</code>
			 * geliefert.
			 * 
			 * @return die Bezeichnung oder <code>null</code>
			 */
			public String getLand() {
				return land;
			}

			/**
			 * liefert den Landescode. Wenn keine entsprechender Code definiert
			 * ist, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return den Code oder <code>null</code>
			 */
			public String getLandescode() {
				return landescode;
			}
		}

		/**
		 * die Repräsentation einer RDS-Location innerhalb einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsLocation {
			/** der Code der Location. */
			private int locationCode;

			/** die Nummer der Straße als Zeichenkette. */
			private String strassenNummer;

			/** die Nummer der Ausfahrt als Zeichenkette. */
			private String ausfahrtNummer;

			/** der TMC-Ortstyp. */
			private int tmcOrtsTyp;

			/** die Koordinaten. */
			private RdsLocationKoordinaten koordinaten;

			/** die Kilometrierung. */
			private String locationKilometrierung;

			/** die Nummer einer Linienreferenz. */
			private int locationCodeLinienReferenz;

			/** die Nummer einer Gebietsreferenz. */
			private int locationCodeGebietsReferenz;

			/** die Nummer der nächsten Location in negativer Richtung. */
			private int locationNextNegativ;

			/** die Nummer der nächsten Location in positiver Richtung. */
			private int locationNextPositiv;

			/** die Nachrichten der Location. */
			private RdsNachrichten locationNachricht;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informatioenen
			 * aus dem übergebenen Datenverteiler-Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsLocation(final Data daten) {
				if (daten != null) {
					locationCode = daten.getUnscaledValue("LocationCode").intValue();

					strassenNummer = daten.getTextValue("StrassenNummer").getText();
					ausfahrtNummer = daten.getTextValue("AusfahrtNummer").getText();
					tmcOrtsTyp = daten.getUnscaledValue("TmcOrtsTyp").intValue();
					koordinaten = new RdsLocationKoordinaten(
							daten.getItem("Koordinaten"));
					locationKilometrierung = daten.getTextValue(
							"LocationKilometrierung").getText();
					locationCodeLinienReferenz = daten.getUnscaledValue(
							"LocationCodeLinienReferenz").intValue();
					locationCodeGebietsReferenz = daten.getUnscaledValue(
							"LocationCodeGebietsReferenz").intValue();
					locationNextNegativ = daten.getUnscaledValue(
							"LocationNextNegativ").intValue();
					locationNextPositiv = daten.getUnscaledValue(
							"LocationNextPositiv").intValue();
					locationNachricht = new RdsNachrichten(
							daten.getItem("LocationNachricht"));
				}
			}

			/**
			 * liefert die Nummer einer zugeordneten Ausfahrt als Zeichenkette.
			 * Wenn keine Ausfahrtnummer definiert ist, wird der Wert
			 * <code>null</code> geliefert.
			 * 
			 * @return die Nummer oder <code>null</code>
			 */
			public String getAusfahrtNummer() {
				return ausfahrtNummer;
			}

			/**
			 * liefert die Koordinaten der Location. Wenn keine Koordinaten
			 * definiert sind, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Koordinaten oder <code>null</code>
			 */
			public RdsLocationKoordinaten getKoordinaten() {
				return koordinaten;
			}

			/**
			 * liefert den Code der Location. Wenn keine Definition erfolgt ist,
			 * wird der Wert 0 geliefert.
			 * 
			 * @return den Code
			 */
			public int getLocationCode() {
				return locationCode;
			}

			/**
			 * liefert den Code einer zugeordneten Gebietsreferenz. Wenn keine
			 * definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return der Code
			 */
			public int getLocationCodeGebietsReferenz() {
				return locationCodeGebietsReferenz;
			}

			/**
			 * liefert den Code einer zugeordneten Linienreferenz. Wenn keine
			 * definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return der Code
			 */
			public int getLocationCodeLinienReferenz() {
				return locationCodeLinienReferenz;
			}

			/**
			 * liefert die Kilometrierung der Location. Wenn keine definiert
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Kilometrierung oder <code>null</code>
			 */
			public String getLocationKilometrierung() {
				return locationKilometrierung;
			}

			/**
			 * liefert die der Location zugeordneten Nachrichten. Wenn keine
			 * definiert wurden, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Nachrichten oder <code>null</code>
			 */
			public RdsNachrichten getLocationNachricht() {
				return locationNachricht;
			}

			/**
			 * liefert den Code der nächsten in negativer Richtung liegenden
			 * Location. Wenn keine definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return der Code
			 */
			public int getLocationNextNegativ() {
				return locationNextNegativ;
			}

			/**
			 * liefert den Code der nächsten in positiver Richtung liegenden
			 * Location. Wenn keine definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return der Code
			 */
			public int getLocationNextPositiv() {
				return locationNextPositiv;
			}

			/**
			 * liefert die Straßennummer der Location als Zeichenkette. Wenn
			 * keine definiert wurde, wird der Wert <code>null</code>
			 * geliefert.
			 * 
			 * @return die Straßennummer oder <code>null</code>
			 */
			public String getStrassenNummer() {
				return strassenNummer;
			}

			/**
			 * liefert den TMC-Ortstyp der Location. Wenn keiner definiert
			 * wurde, wird der Wert 0 geliefert.
			 * 
			 * @return den Typ
			 */
			public int getTmcOrtsTyp() {
				return tmcOrtsTyp;
			}
		}

		/**
		 * Die Repräsentation von Location-Daten innerhalb einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsLocationDaten {
			/** die Liste der zugeordneten Locations. */
			private final List<RdsLocation> rdslocation = new ArrayList<RdsLocation>();

			/** das Format. */
			private RdsLocationFormat rdsLocationFormat;

			/** der Extent. */
			private int locationExtent;

			/** die Locationmethode. */
			private RdsLocationMethode locationMethode;

			/** die TMC-Richtung. */
			private RdsTMCRichtung tmcRichtung;

			/** die primäre Entfernung. */
			private long locationPrimaerEntfernung;

			/** die sekundäre Entfernung. */
			private long locationSekundaerEntfernung;

			/** die vorgelagerte Location. */
			private int locationVorLocation;

			/** die nachgelagerte Location. */
			private int locationNachLocation;

			/** die Kategorie. */
			private RdsLocationKategorie locationKategorie;

			/** die Richtungs-Text-ID. */
			private RdsLocationRichtungTextID locationRichtungTextID;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datenverteiler-Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsLocationDaten(final Data daten) {
				if (daten != null) {
					Data.Array array = daten.getArray("RDSLocation");
					for (int idx = 0; idx < array.getLength(); idx++) {
						rdslocation.add(new RdsLocation(array.getItem(idx)));
					}

					rdsLocationFormat = RdsLocationFormat.getStatus(daten.getUnscaledValue(
							"RDSLocationFormat").intValue());
					locationExtent = daten.getUnscaledValue("LocationExtent").intValue();
					locationMethode = RdsLocationMethode.getStatus(daten.getUnscaledValue(
							"LocationMethode").intValue());
					tmcRichtung = RdsTMCRichtung.getStatus(daten.getUnscaledValue(
							"TMCRichtung").intValue());
					locationPrimaerEntfernung = daten.getUnscaledValue(
							"LocationPrimärEntfernung").longValue();
					locationSekundaerEntfernung = daten.getUnscaledValue(
							"LocationSekundärEntfernung").longValue();
					locationVorLocation = daten.getUnscaledValue(
							"LocationVorLocation").intValue();
					locationNachLocation = daten.getUnscaledValue(
							"LocationNachLocation").intValue();
					locationKategorie = RdsLocationKategorie.getStatus(daten.getUnscaledValue(
							"LocationKategorie").intValue());
					locationRichtungTextID = RdsLocationRichtungTextID.getStatus(daten.getUnscaledValue(
							"LocationRichtungTextID").intValue());
				}

			}

			/**
			 * liefert den Extent. Wenn keine Definition erfolgt ist, wird der
			 * Wert 0 geliefert.
			 * 
			 * @return den Extent
			 */
			public int getLocationExtent() {
				return locationExtent;
			}

			/**
			 * liefert die Kategorie. Wenn keine Definition erfolgt ist, wird
			 * der Wert <code>null</code> geliefert. Wenn der Datensatz keinen
			 * gültigen Code für die Kategorie enthalten hat, wird der
			 * Standardwert {@link RdsLocationKategorie#PUNKT} geliefert.
			 * 
			 * @return die Kategorie oder <code>null</code>
			 */
			public RdsLocationKategorie getLocationKategorie() {
				return locationKategorie;
			}

			/**
			 * liefert die Location-Methode. Wenn keine Definition erfolgt ist,
			 * wird der Wert <code>null</code> geliefert. Wenn der Datensatz
			 * keinen gültigen Code für die Methode enthälten hat, wird der
			 * Standardwert {@link RdsLocationMethode#METHODE0} geliefert.
			 * 
			 * @return die Methode oder <code>null</code>
			 */
			public RdsLocationMethode getLocationMethode() {
				return locationMethode;
			}

			/**
			 * liefert den Code der nachgelagerten Location. Wenn keine
			 * Definition erfolgt ist, wird der Wert 0 geliefert.
			 * 
			 * @return den Code
			 */
			public int getLocationNachLocation() {
				return locationNachLocation;
			}

			/**
			 * liefert die primäre Entfernung der Location. Wenn keine
			 * Definition erfolgt ist, wird der Wert 0 geliefert.
			 * 
			 * @return die Entfernung
			 */
			public long getLocationPrimaerEntfernung() {
				return locationPrimaerEntfernung;
			}

			/**
			 * liefert die Richtungs-Text-ID der Location. Wenn keine Definition
			 * erfolgt ist, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die ID oder <code>null</code>
			 */
			public RdsLocationRichtungTextID getLocationRichtungTextID() {
				return locationRichtungTextID;
			}

			/**
			 * liefert die sekundäre Entfernung der Location. Wenn keine
			 * Definition erfolgt ist, wird der Wert 0 geliefert.
			 * 
			 * @return die Entfernung
			 */
			public long getLocationSekundaerEntfernung() {
				return locationSekundaerEntfernung;
			}

			/**
			 * liefert den Code der vorgelagerten Location. Wenn keine
			 * Definition erfolgt ist, wird der Wert 0 geliefert.
			 * 
			 * @return den Code
			 */
			public int getLocationVorLocation() {
				return locationVorLocation;
			}

			/**
			 * liefert die Liste der zugeordneten RDS-Locations.
			 * 
			 * @return die Liste
			 */
			public List<RdsLocation> getRdslocation() {
				return rdslocation;
			}

			/**
			 * liefert das Location-Format der Location. Wenn keine Definition
			 * erfolgt ist, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return das Format oder <code>null</code>
			 */
			public RdsLocationFormat getRdsLocationFormat() {
				return rdsLocationFormat;
			}

			/**
			 * liefert die TMC-Richtung. Wenn keine Definition erfolgt ist, wird
			 * der Wert <code>null</code> geliefert. Wenn der Datensatz keinen
			 * gültigen Code für die TMC-Richtung enthät, wird der Standardwert
			 * {@link RdsTMCRichtung#POSITIV} geliefert.
			 * 
			 * @return die Richtung oder <code>null</code>
			 */
			public RdsTMCRichtung getTmcRichtung() {
				return tmcRichtung;
			}
		}

		/**
		 * Repräsentation der Koordinaten innerhal einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsLocationKoordinaten {
			/** die definierte Länge. */
			private double laenge;

			/** die definierte Breite. */
			private double breite;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsLocationKoordinaten(final Data daten) {
				if (daten != null) {
					laenge = daten.getScaledValue("RDSLocationKoordinateX").doubleValue();
					breite = daten.getScaledValue("RDSLocationKoordinateY").doubleValue();
				}
			}

			/**
			 * liefert die definierte Breite. Wenn keine Breite definiert wurde,
			 * wird der Wert 0 geliefert.
			 * 
			 * @return die Breite
			 */
			public double getBreite() {
				return breite;
			}

			/**
			 * liefert die definierte Länge. Wenn keine Länge definiert wurde,
			 * wird der Wert 0 geliefert.
			 * 
			 * @return die Länge
			 */
			public double getLaenge() {
				return laenge;
			}
		}

		/**
		 * Repräsentation der Informationen einer Location-Tabelle innerhalb
		 * einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsLocationTabelleInfo {
			/** die verwendete Tabelle. */
			private RdsLocationTabelle locationTabelle;

			/** die Version der Tabelle. */
			private String locationTabelleVersion;

			/** die Typ der Tabelle. */
			private RdsLocationTabelleTyp locationTabelleTyp;

			/** der Name der Tabelle. */
			private String locationTabelleName;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsLocationTabelleInfo(final Data daten) {
				if (daten != null) {
					locationTabelle = RdsLocationTabelle.getLocationTabelle(daten.getUnscaledValue(
							"LocationTabelle").intValue());
					locationTabelleVersion = daten.getTextValue(
							"LocationTabelleVersion").getText();
					locationTabelleTyp = RdsLocationTabelleTyp.getStatus(daten.getUnscaledValue(
							"LocationTabelleTyp").intValue());
					locationTabelleName = daten.getTextValue(
							"LocationTabelleName").getText();
				}
			}

			/**
			 * liefert die zugeordnete Tabelle. Wenn keine Tabelle zugeordnet
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Tabelle oder <code>null</code>
			 */
			public RdsLocationTabelle getLocationTabelle() {
				return locationTabelle;
			}

			/**
			 * liefert den Tabellennamen. Wenn kein Name definiert wurde, wird
			 * der Wert <code>null</code> geliefert.
			 * 
			 * @return der Name oder <code>null</code>
			 */
			public String getLocationTabelleName() {
				return locationTabelleName;
			}

			/**
			 * liefert den Tabellentyp. Wenn kein Typ definiert wurde, wird der
			 * Wert <code>null</code> geliefert. Wenn der Datensatz keinen
			 * gültigen Code für den Tabellentyp enthält, wird der Standardwert
			 * {@link RdsLocationTabelleTyp#TMC} geliefert.
			 * 
			 * @return den Typ oder <code>null</code>
			 */
			public RdsLocationTabelleTyp getLocationTabelleTyp() {
				return locationTabelleTyp;
			}

			/**
			 * liefert die Tabellenversion. Wenn keine Version definiert wurde,
			 * wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Version oder <code>null</code>
			 */
			public String getLocationTabelleVersion() {
				return locationTabelleVersion;
			}
		}

		/**
		 * Repräsentation der Nachrichten innerhalb einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsNachrichten {

			/** die Daten der Nachrichten. */
			private final List<RdsNachrichtenDaten> nachrichtenDaten = new ArrayList<RdsNachrichtenDaten>();

			/** die verwendete Sprache der Nachrichten. */
			private RdsNachrichtenSprache nachrichtenSprache;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datenverteiler-Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsNachrichten(final Data daten) {
				if (daten != null) {
					Data.Array array = daten.getArray("NachrichtenDaten");
					for (int idx = 0; idx < array.getLength(); idx++) {
						nachrichtenDaten.add(new RdsNachrichtenDaten(
								array.getItem(idx)));
					}
					nachrichtenSprache = RdsNachrichtenSprache.getNachrichtenSprache(daten.getUnscaledValue(
							"NachrichtenSprache").intValue());
				}
			}

			/**
			 * liefert die Nachricht mit dem übergebenen Typ. Wenn kein
			 * entsprechender Eintrag existiert wird eine leere Zeichenkette
			 * geliefert.
			 * 
			 * @param typ
			 *            der Typ für den der Nachrichteneintrag ermittelt
			 *            werden soll
			 * @return der ermittelte Text
			 */
			public String getNachricht(final RdsNachrichtenKlasse typ) {
				String result = Constants.EMPTY_STRING;
				for (RdsNachrichtenDaten eintrag : nachrichtenDaten) {
					if (eintrag.getNachrichtenKlasse().equals(typ)) {
						result = eintrag.getNachrichtenText();
						break;
					}
				}
				return result;
			}

			/**
			 * liefert die Liste aller definierten Nachrichtendaten.
			 * 
			 * @return die Liste
			 */
			public List<RdsNachrichtenDaten> getNachrichtenDaten() {
				return nachrichtenDaten;
			}

			/**
			 * liefert die Nachrichtensprache. Wenn keine Sprache definiert
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Sprache oder <code>null</code>
			 */
			public RdsNachrichtenSprache getNachrichtenSprache() {
				return nachrichtenSprache;
			}
		}

		/**
		 * Repräsentiert einen Eintrag innerhalb einer Nachricht einer
		 * RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsNachrichtenDaten {

			/** die Klasse des Eintrags. */
			private RdsNachrichtenKlasse nachrichtenKlasse;

			/** der Text des Eintrags. */
			private String nachrichtenText;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datenverteiler-Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsNachrichtenDaten(final Data daten) {
				if (daten != null) {
					nachrichtenKlasse = RdsNachrichtenKlasse.getStatus(daten.getUnscaledValue(
							"NachrichtenKlasse").intValue());
					nachrichtenText = daten.getTextValue("NachrichtenText").getText();
				}
			}

			/**
			 * liefert die Nachrichtenklasse des Eintrags. Wenn keine definiert
			 * wurde, wird der Wert <code>null</code> geliefert. Wenn der
			 * Datenverteiler-Datensatz keinen gültugen Code für die
			 * Nachrichtenklasse enthalten hat, wird der Standardwert
			 * {@link RdsNachrichtenKlasse#FREIER_TEXT} geliefert.
			 * 
			 * @return die nachrichtenklasse oder <code>null</code>
			 */
			public RdsNachrichtenKlasse getNachrichtenKlasse() {
				return nachrichtenKlasse;
			}

			/**
			 * liefert den zugeordneten Nachrichtentext oder <code>null</code>,
			 * wenn keiner definiert wurde.
			 * 
			 * @return den Text oder <code>null</code>
			 */
			public String getNachrichtenText() {
				return nachrichtenText;
			}
		}

		/**
		 * Die Repräsentation eines Textes innerhalb einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsText {

			/** der Text. */
			private String text;

			/** die verwendete Sprache. */
			private RdsNachrichtenSprache textSprache;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datenverteiler-Datensatzes initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsText(final Data daten) {
				if (daten != null) {
					text = daten.getTextValue("Text").getText();
					textSprache = RdsNachrichtenSprache.getNachrichtenSprache(daten.getUnscaledValue(
							"TextSprache").intValue());
				}
			}

			/**
			 * liefert den definierten Text. Wurde kein Text definiert, wird der
			 * Wert <code>null</code> geliefert.
			 * 
			 * @return den Text oder <code>null</code>
			 */
			public String getText() {
				return text;
			}

			/**
			 * liefert die verwendete Sprache. Wurde keine Sprache definiert,
			 * wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Sprache oder <code>null</code>
			 */
			public RdsNachrichtenSprache getTextSprache() {
				return textSprache;
			}
		}

		/**
		 * Repräsenation des verkehrlichen Teils einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsVerkehr {

			/** die Nachrichten der Meldung. */
			private RdsNachrichten nachrichten;

			/** innerhalb der Meldung gelieferter freier Text. */
			private RdsText freierText;

			/** Informationen zur verwendeten Locationtabelle. */
			private RdsLocationTabelleInfo locationTabelleInfo;

			/** die der Meldung zugeordneten Location-Daten. */
			private RdsLocationDaten locationDaten;

			/** ein der Meldung zugeordnetes Ereignis. */
			private RdsEreignis ereignis;

			/** zusätzliche Attribute der Meldung. */
			private RdsAttribute zusatzAttribute;

			/** definierter Alarmierungscode. */
			private String alertCCode;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergeben Datenverteiler-Datensatz initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsVerkehr(final Data daten) {
				if (daten != null) {
					nachrichten = new RdsNachrichten(
							daten.getItem("Nachrichten"));
					freierText = new RdsText(daten.getItem("FreierText"));
					locationTabelleInfo = new RdsLocationTabelleInfo(
							daten.getItem("LocationTabelleInfo"));
					locationDaten = new RdsLocationDaten(
							daten.getItem("LocationDaten"));
					ereignis = new RdsEreignis(daten.getItem("Ereignis"));
					zusatzAttribute = new RdsAttribute(
							daten.getItem("ZusatzAttribute"));
					alertCCode = daten.getTextValue("AlertCCode").getText();
				}
			}

			/**
			 * liefert den Alarmcode. Wenn keiner definiert wurde, wird der Wert
			 * <code>null</code> geliefert.
			 * 
			 * @return der Code oder <code>null</code>
			 */
			public String getAlertCCode() {
				return alertCCode;
			}

			/**
			 * liefert das der Meldung zugeordnete Ereignis. Wurde kein Ereignis
			 * definiert, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return das Ereignis oder <code>null</code>
			 */
			public RdsEreignis getEreignis() {
				return ereignis;
			}

			/**
			 * liefert den innerhalb der Meldung definierten freien Text. Wurden
			 * keine entsprechenden Daten definiert, wird der Wert
			 * <code>null</code> geliefert.
			 * 
			 * @return den Text oder <code>null</code>
			 */
			public RdsText getFreierText() {
				return freierText;
			}

			/**
			 * liefert die Daten der Location, der die Meldung zugeordnet ist.
			 * Wurden keine Location-Daten definiert, wird der Wert
			 * <code>null</code> geliefert.
			 * 
			 * @return die Daten oder <code>null</code>
			 */
			public RdsLocationDaten getLocationDaten() {
				return locationDaten;
			}

			/**
			 * liefert die Informationen zur verwendeten Location-Tabelle.
			 * Wurden keine entsprechenden Informationen definiert, wird der
			 * Wert <code>null</code> geliefert.
			 * 
			 * @return die Informationen oder <code>null</code>
			 */
			public RdsLocationTabelleInfo getLocationTabelleInfo() {
				return locationTabelleInfo;
			}

			/**
			 * liefert die Nachrichten der Meldung. Wurden keine nachrichten
			 * definiert, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Nachrichten oder <code>null</code>
			 */
			public RdsNachrichten getNachrichten() {
				return nachrichten;
			}

			/**
			 * liefert die Zusatzattribute. Wenn keine Zusatzattribute definiert
			 * wurden, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Attribute oder <code>null</code>
			 */
			public RdsAttribute getZusatzAttribute() {
				return zusatzAttribute;
			}
		}

		/**
		 * Repräsentation der Verwaltungsdaten einer RDS-Meldung.
		 * 
		 * @author BitCtrl Systems GmbH, Uwe Peuker
		 * @version $Id$
		 */
		public static class RdsVerwaltung {
			/** der Name der authorisiernden Organisiationseinheit. */
			private String authorisierendeOrganisationsEinheit;

			/** der Name des authorisiernden Nutzers. */
			private String authorisierenderNutzer;

			/** der Zeitpunkt der Authorisierung. */
			private long authorisierungsZeit;

			/** ein Kommentar zur Authorisierung. */
			private String authorisierungsKommentar;

			/** das Ergebnis der Authorisierung. */
			private RdsAuthorisierungsErgebnis authorisierungsErgebnis;

			/** das Versionsnummer. */
			private int versionsNummer;

			/** der Zeitpunkt der letzten Aktualisierung der Meldung. */
			private long aktualisierungsZeit;

			/** der Zeitpunkt der Aktivierung der Meldung. */
			private long aktivierungsZeit;

			/** der Ablaufzeitpunkt der Meldung. */
			private long ablaufZeit;

			/** der Zeitpunkt der nächsten Erinnerung für die Meldung. */
			private long erinnerungsZeit;

			/** der Offset des Erinnnerungszeitpunkts. */
			private long erinnerungsZeitOffset;

			/** der Typ der Erinnerung. */
			private RdsErinnerungsTyp erinnerungsTyp;

			/** der Zeitpunkt der Erzeugung der Meldung. */
			private long erzeugungsZeit;

			/** die Änderungsinfromationen der Meldung. */
			private RdsAenderungsInfo aenderungsInformationen;

			/** der Status der Meldung. */
			private RdsStatus status;

			/** die Wichtung der Meldung. */
			private int wichtung;

			/** die Priorität der Meldung. */
			private RdsPrioritaet prioritaet;

			/** die Landeskennung der Meldung. */
			private RdsLandesKennung landesKennung;

			/**
			 * Konstruktor. Die Daten des Objekts werden mit den Informationen
			 * aus dem übergebenen Datenverteiler-Datensatzes initialisiert.
			 * 
			 * @param daten
			 *            die Daten
			 */
			public RdsVerwaltung(final Data daten) {
				if (daten != null) {
					authorisierendeOrganisationsEinheit = daten.getTextValue(
							"AuthorisierendeOrganisationsEinheit").getText();
					authorisierenderNutzer = daten.getTextValue(
							"AuthorisierenderNutzer").getText();
					authorisierungsZeit = daten.getTimeValue(
							"AuthorisierungsZeit").getMillis();
					authorisierungsKommentar = daten.getTextValue(
							"AuthorisierungsKommentar").getText();
					authorisierungsErgebnis = RdsAuthorisierungsErgebnis.getStatus(daten.getUnscaledValue(
							"AuthorisierungsErgebnis").intValue());
					versionsNummer = daten.getUnscaledValue("VersionsNummer").intValue();
					aktualisierungsZeit = daten.getTimeValue(
							"AktualisierungsZeit").getMillis();
					aktivierungsZeit = daten.getTimeValue("AktivierungsZeit").getMillis();
					ablaufZeit = daten.getTimeValue("AblaufZeit").getMillis();
					erinnerungsZeit = daten.getTimeValue("ErinnerungsZeit").getMillis();
					erinnerungsZeitOffset = daten.getTimeValue(
							"ErinnerungsZeitOffset").getMillis();
					erinnerungsTyp = RdsErinnerungsTyp.getStatus(daten.getUnscaledValue(
							"ErinnerungsTyp").intValue());
					erzeugungsZeit = daten.getTimeValue("ErzeugungsZeit").getMillis();
					aenderungsInformationen = new RdsAenderungsInfo(
							daten.getItem("ÄnderungsInformationen"));
					status = RdsStatus.getStatus(daten.getUnscaledValue(
							"Status").intValue());
					wichtung = daten.getUnscaledValue("Wichtung").intValue();
					prioritaet = RdsPrioritaet.getStatus(daten.getUnscaledValue(
							"Priorität").intValue());
					landesKennung = new RdsLandesKennung(
							daten.getReferenceValue("LandesKennung").getSystemObject());
				}
			}

			/**
			 * liefert die Ablaufzeit der Meldung. Wenn keine Ablaufzeit
			 * definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return die Zeit
			 */
			public long getAblaufZeit() {
				return ablaufZeit;
			}

			/**
			 * liefert die Änderungsinformatioenen. Wurden keine Informationen
			 * definiert, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Informationen oder <code>null</code>
			 */
			public RdsAenderungsInfo getAenderungsInformationen() {
				return aenderungsInformationen;
			}

			/**
			 * liefert die Aktivierungszeit der Meldung. Wenn keine
			 * Aktivierungszeit definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return die Zeit
			 */
			public long getAktivierungsZeit() {
				return aktivierungsZeit;
			}

			/**
			 * liefert die Aktualisierungzeit der Meldung. Wenn keine
			 * Aktualisierungszeit definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return die Zeit
			 */
			public long getAktualisierungsZeit() {
				return aktualisierungsZeit;
			}

			/**
			 * liefert die Bezeichnung der authorisierenden
			 * Organisationseinheit. Wenn keine Bezeichnung definiert wurde,
			 * wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Bezeichnung oder <code>null</code>
			 */
			public String getAuthorisierendeOrganisationsEinheit() {
				return authorisierendeOrganisationsEinheit;
			}

			/**
			 * liefert die Bezeichnung des authorisierenden Nutzers. Wenn keine
			 * Bezeichnung definiert wurde, wird der Wert <code>null</code>
			 * geliefert.
			 * 
			 * @return die Bezeichnung oder <code>null</code>
			 */
			public String getAuthorisierenderNutzer() {
				return authorisierenderNutzer;
			}

			/**
			 * liefert das Authorisierungsergebnis. Wenn kein Ergebnis definiert
			 * wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return das Ergebnis oder <code>null</code>
			 */
			public RdsAuthorisierungsErgebnis getAuthorisierungsErgebnis() {
				return authorisierungsErgebnis;
			}

			/**
			 * liefert den Kommentar zur Authorisierung. Wenn kein Kommentar
			 * definiert wurde, wird der Wert <code>null</code> geliefert.
			 * 
			 * @return der Kommentartext oder <code>null</code>
			 */
			public String getAuthorisierungsKommentar() {
				return authorisierungsKommentar;
			}

			/**
			 * liefert die Authorisierungszeit der Meldung. Wenn keine
			 * Authorisierungszeit definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return die Zeit
			 */
			public long getAuthorisierungsZeit() {
				return authorisierungsZeit;
			}

			/**
			 * liefert den Erinnerungstyp. Wenn kein Typ definiert wurde, wird
			 * der Wert <code>null</code> geliefert. Wenn der Datensatz zur
			 * Initialisierung keinen gültigen Code für den Erinnerungstp
			 * enthält, wird der Standardwert {@link RdsErinnerungsTyp#KEINE}
			 * geliefert.
			 * 
			 * @return den Typ oder <code>null</code>
			 */
			public RdsErinnerungsTyp getErinnerungsTyp() {
				return erinnerungsTyp;
			}

			/**
			 * liefert die Erinnerungszeit der Meldung. Wenn keine
			 * Erinnerungszeit definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return die Zeit
			 */
			public long getErinnerungsZeit() {
				return erinnerungsZeit;
			}

			/**
			 * liefert den Offset der Erinnerungszeit. Wenn kein Offset
			 * definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return den Offset
			 */
			public long getErinnerungsZeitOffset() {
				return erinnerungsZeitOffset;
			}

			/**
			 * liefert die Erzeugungszeit der Meldung. Wenn keine Erzeugungszeit
			 * definiert wurde, wird der Wert 0 geliefert.
			 * 
			 * @return die Zeit
			 */
			public long getErzeugungsZeit() {
				return erzeugungsZeit;
			}

			/**
			 * liefert die Landeskennung. Wurde keine Landeskennung definiert,
			 * wird der Wert <code>null</code> geliefert.
			 * 
			 * @return die Kennung oder <code>null</code>
			 */
			public RdsLandesKennung getLandesKennung() {
				return landesKennung;
			}

			/**
			 * liefert die Priorität. Wurde keine Priorotät definiert, wird der
			 * Wert <code>null</code> geliefert. Enthält der initialisiernde
			 * Datensatz keinen gültigen Code für die Priorität, wird der
			 * Standardwert {@link RdsPrioritaet#NORMAL} geliefert.
			 * 
			 * @return die Priorität oder <code>null</code>
			 */
			public RdsPrioritaet getPrioritaet() {
				return prioritaet;
			}

			/**
			 * liefert den Status. Wurde kein Status definiert, wird der Wert
			 * <code>null</code> geliefert. Enthält der initialisiernde
			 * Datensatz keinen gültigen Code für die Priorität, wird der
			 * Standardwert {@link RdsPrioritaet#NORMAL} geliefert.
			 * 
			 * @return die Priorität oder <code>null</code>
			 */
			public RdsStatus getStatus() {
				return status;
			}

			/**
			 * liefert die Versionsnummer der Meldung. Wenn keine definiert
			 * wurde wird der Wert 0 geliefert.
			 * 
			 * @return die Nummer
			 */
			public int getVersionsNummer() {
				return versionsNummer;
			}

			/**
			 * liefert die Wichtung der Meldung. Wenn keine definiert wurde,
			 * wird der Wert 0 geliefert.
			 * 
			 * @return der Wert
			 */
			public int getWichtung() {
				return wichtung;
			}
		}

		/** die Info GUID. */
		private String infoGuid;

		/** der Name der Organisation. */
		private String organisation;

		/** der Nummer der Meldung. */
		private String nummer;

		/** der Versions GUID. */
		private String versionGUID;

		/** die Verwaltungsdaten der Meldung. */
		private RdsVerwaltung verwaltung;

		/** die Verkehrsdaten der Meldung. */
		private RdsVerkehr verkehr;

		/** der aktuelle Status des Datensatzes. */
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

			klon.setDatenStatus(datenStatus);
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
		 * liefert die Info GUID. Wurde keine ID definiert, wird der Wert
		 * <code>null</code> geliefert.
		 * 
		 * @return die ID oder <code>null</code>
		 */
		public String getInfoGuid() {
			return infoGuid;
		}

		/**
		 * liefert die Nummer der Meldung. Wurde keine Nummer definiert, wird
		 * der Wert <code>null</code> geliefert.
		 * 
		 * @return die Nummer oder <code>null</code>
		 */
		public String getNummer() {
			return nummer;
		}

		/**
		 * liefert die Bezeichnung der Organisation. Wenn keine Bezeichnung
		 * definiert wurde, wird der Wert <code>null</code> geliefert.
		 * 
		 * @return die Bezeichnung oder <code>null</code>
		 */
		public String getOrganisation() {
			return organisation;
		}

		/**
		 * liefert die verkehrstechnischen daten der Meldung. Wenn keine
		 * entsprechenden Daten definiert sind wird der Wert <code>null</code>
		 * geliefert.
		 * 
		 * @return die Daten oder <code>null</code>
		 */
		public RdsVerkehr getVerkehr() {
			return verkehr;
		}

		/**
		 * liefert die Versions GUID. Ist keine ID definiert, wird der Wert
		 * <code>null</code> geliefert.
		 * 
		 * @return die ID oder <code>null</code>
		 */
		public String getVersionGUID() {
			return versionGUID;
		}

		/**
		 * liefert die Verwaltungsdaten der Meldung. Sind keine Verwaltungsdaten
		 * definiert, wird der Wert <code>null</code> geliefert.
		 * 
		 * @return die Daten oder <code>null</code>
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
		 * setzt die Info GUID der Meldung auf den übergebenen Wert.
		 * 
		 * @param id
		 *            die zu setzende ID
		 */
		public void setInfoGuid(String id) {
			this.infoGuid = id;
		}

		/**
		 * setzt die Nummer der Meldung auf den übergebenen Wert.
		 * 
		 * @param nummer
		 *            die zu setzende Nummer
		 */
		public void setNummer(String nummer) {
			this.nummer = nummer;
		}

		/**
		 * setzt den Name der Organisation der Meldung auf den übergebenen Wert.
		 * 
		 * @param organisation
		 *            der zu setzende Name
		 */
		public void setOrganisation(String organisation) {
			this.organisation = organisation;
		}

		/**
		 * setzt die verkehrstechnischen Daten der Meldung auf den übergebenen
		 * Wert.
		 * 
		 * @param verkehr
		 *            die zu setzenden Daten
		 */
		public void setVerkehr(RdsVerkehr verkehr) {
			this.verkehr = verkehr;
		}

		/**
		 * setzt die Versions GUID der Meldung auf den übergebenen Wert.
		 * 
		 * @param id
		 *            die zu setzende ID
		 */
		public void setVersionGUID(String id) {
			this.versionGUID = id;
		}

		/**
		 * setzt die Verwaltungsdaten der Meldung auf den übergebenen Wert.
		 * 
		 * @param verwaltung
		 *            die zu setzenden Daten
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
	 * initialisiert den Onlinedatensatz.
	 * 
	 * @param meldung
	 *            die Meldung, für deren Daten ein Datensatz initialisiert
	 *            werden soll
	 */
	public OdRdsMeldung(RdsMeldung meldung) {
		super(meldung);

		if (atg == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung().getDataModel();
			atg = modell.getAttributeGroup(ATG_RDS_MELDUNG);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#abmeldenSender()
	 */
	public void abmeldenSender() {
		abmeldenSender(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_VORGABE));

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#abrufenDatum()
	 */
	public Daten abrufenDatum() {
		return abrufenDatum(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_SOLL));
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

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#addUpdateListener(de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener)
	 */
	public void addUpdateListener(DatensatzUpdateListener l) {
		addUpdateListener(
				ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_SOLL), l);

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#anmeldenSender()
	 */
	public void anmeldenSender() throws AnmeldeException {
		anmeldenSender(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_VORGABE));
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
		aspekte.add(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_SOLL));
		aspekte.add(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_VORGABE));
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#getDatum()
	 */
	public Daten getDatum() {
		return getDatum(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_SOLL));
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#getStatusSendesteuerung()
	 */
	public de.bsvrz.sys.funclib.bitctrl.modell.Datensatz.Status getStatusSendesteuerung() {
		return getStatusSendesteuerung(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_VORGABE));
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#isAngemeldetSender()
	 */
	public boolean isAngemeldetSender() {
		return isAngemeldetSender(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_VORGABE));
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#isAutoUpdate()
	 */
	public boolean isAutoUpdate() {
		return isAutoUpdate(ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
				DaVKonstanten.ASP_PARAMETER_SOLL));
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

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#removeUpdateListener(de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener)
	 */
	public void removeUpdateListener(DatensatzUpdateListener l) {
		removeUpdateListener(
				ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_SOLL), l);

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#sendeDaten(de.bsvrz.sys.funclib.bitctrl.modell.Datum)
	 */
	public void sendeDaten(Daten datum) throws DatensendeException {
		sendeDaten(
				ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_VORGABE), datum);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.ParameterDatensatz#sendeDaten(de.bsvrz.sys.funclib.bitctrl.modell.Datum,
	 *      long)
	 */
	public void sendeDaten(Daten datum, long timeout)
			throws DatensendeException {
		sendeDaten(
				ObjektFactory.getInstanz().getVerbindung().getDataModel().getAspect(
						DaVKonstanten.ASP_PARAMETER_VORGABE), datum, timeout);
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
			datum.setNummer(daten.getItem("ID").getTextValue("IDNummer").getText());
			datum.setVersionGUID(daten.getItem("Version").getTextValue(
					"VersionGUID").getText());
			datum.setVerwaltung(new Daten.RdsVerwaltung(
					daten.getItem("Version").getItem("VerwaltungsInformationen")));
			datum.setVerkehr(new RdsVerkehr(daten.getItem("Version").getItem(
					"VerkehrsInformationen")));
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
