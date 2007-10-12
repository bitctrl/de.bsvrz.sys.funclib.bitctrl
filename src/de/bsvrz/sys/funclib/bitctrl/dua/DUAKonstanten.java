/**
 * Segment 4 Datenübernahme und Aufbereitung (DUA), SWE 4.x 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:<br>
 * BitCtrl Systems GmbH<br>
 * Weißenfelser Straße 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.text.SimpleDateFormat;

/**
 * Allgemeine Konstanten der DUA
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 */
public class DUAKonstanten {

	/**
	 * <code>null</code>-String
	 */
	public static final String NULL = "<<null>>"; //$NON-NLS-1$
		
	/**
	 * Kommandozeilenargument: KonfigurationsBereichsPid
	 */
	public static final String ARG_KONFIGURATIONS_BEREICHS_PID
							= "KonfigurationsBereichsPid"; //$NON-NLS-1$

	/**
	 * DAV-Typ-PID <code>typ.umfeldDatenSensor</code>
	 */
	public static final String TYP_UFD_SENSOR = "typ.umfeldDatenSensor"; //$NON-NLS-1$

	/**
	 * DAV-Typ-PID <code>typ.umfeldDatenMessStelle</code>
	 */
	public static final String TYP_UFD_MESSSTELLE = "typ.umfeldDatenMessStelle"; //$NON-NLS-1$
	
	/**
	 * DAV-Typ-PID <code>typ.messQuerschnittAllgemein</code>
	 */
	public static final String TYP_MQ_ALLGEMEIN = "typ.messQuerschnittAllgemein"; //$NON-NLS-1$
	
	/**
	 * DAV-Typ-PID <code>typ.messQuerschnittVirtuell</code>
	 */
	public static final String TYP_MQ_VIRTUELL = "typ.messQuerschnittVirtuell"; //$NON-NLS-1$
	
	/**
	 * DAV-Typ-PID <code>typ.messQuerschnitt</code>
	 */
	public static final String TYP_MQ = "typ.messQuerschnitt"; //$NON-NLS-1$

	/**
	 * DAV-Typ-PID <code>typ.messStelle</code>
	 */
	public static final String TYP_MESS_STELLE = "typ.messStelle"; //$NON-NLS-1$
	
	/**
	 * DAV-Typ-PID <code>typ.messStellenGruppe</code>
	 */
	public static final String TYP_MESS_STELLEN_GRUPPE = "typ.messStellenGruppe"; //$NON-NLS-1$
	
	/**
	 * DAV-Typ-PID <code>typ.fahrStreifen</code>
	 */
	public static final String TYP_FAHRSTREIFEN = "typ.fahrStreifen"; //$NON-NLS-1$	
	
	/**
	 * DAV-Typ-PID <code>typ.fahrStreifenLangZeit</code>
	 */
	public static final String TYP_FAHRSTREIFEN_LZ = "typ.fahrStreifenLangZeit"; //$NON-NLS-1$
	
	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitIntervall</code> 
	 */
	public static final String ATG_KZD = "atg.verkehrsDatenKurzZeitIntervall"; //$NON-NLS-1$
	
	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenLangZeitIntervall</code>
	 */
	public static final String ATG_LZD = "atg.verkehrsDatenLangZeitIntervall"; //$NON-NLS-1$

	/**
	 * DAV-Atg-PID <code>"atg.störfallZustand"</code>
	 */
	public static final String ATG_STOERFALL_ZUSTAND = "atg.störfallZustand"; //$NON-NLS-1$
	
	/**
	 * DAV-Asp-PID <code>asp.externeErfassung</code>
	 */
	public static final String ASP_EXTERNE_ERFASSUNG = "asp.externeErfassung"; //$NON-NLS-1$

	/**
	 * DAV-Asp-PID <code>asp.plausibilitätsPrüfungFormal</code>
	 */
	public static final String ASP_PL_PRUEFUNG_FORMAL = "asp.plausibilitätsPrüfungFormal"; //$NON-NLS-1$
	
	/**
	 * DAV-Asp-PID <code>asp.plausibilitätsPrüfungLogisch</code>
	 */
	public static final String ASP_PL_PRUEFUNG_LOGISCH = "asp.plausibilitätsPrüfungLogisch"; //$NON-NLS-1$

	/**
	 * DAV-Asp-PID <code>asp.messWertErsetzung</code>
	 */
	public static final String ASP_MESSWERTERSETZUNG = "asp.messWertErsetzung"; //$NON-NLS-1$

	/**
	 * DAV-Asp-PID <code>asp.analyse</code>
	 */
	public static final String ASP_ANALYSE = "asp.analyse"; //$NON-NLS-1$

	/**
	 * DAV-Atg-PID <code>atg.fahrStreifen</code>
	 */
	public static final String ATG_FAHRSTREIFEN = "atg.fahrStreifen"; //$NON-NLS-1$

	/**
	 * DAV-Atg-PID <code>atg.messStelle</code>
	 */
	public static final String ATG_MESS_STELLE = "atg.messStelle"; //$NON-NLS-1$
	
	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittAllgemein</code>
	 */
	public static final String ATG_MQ_ALLGEMEIN = "atg.messQuerschnittAllgemein"; //$NON-NLS-1$
	
	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittVirtuell</code>
	 */
	public static final String ATG_MQ_VIRTUELL = "atg.messQuerschnittVirtuell"; //$NON-NLS-1$
	
	/**
	 * DAV-Atg-PID <code>atg.messQuerschnittVirtuellStandard</code>
	 */
	public static final String ATG_MQ_VIRTUELL_STANDARD = "atg.messQuerschnittVirtuellStandard"; //$NON-NLS-1$	
	
	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitTrendExtraPolationFs</code>
	 */
	public static final String ATG_KURZZEIT_TRENT_FS = "atg.verkehrsDatenKurzZeitTrendExtraPolationFs"; //$NON-NLS-1$	

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitTrendExtraPolationMq</code>
	 */
	public static final String ATG_KURZZEIT_TRENT_MQ = "atg.verkehrsDatenKurzZeitTrendExtraPolationMq"; //$NON-NLS-1$	

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitGeglättetFs</code>
	 */
	public static final String ATG_KURZZEIT_GEGLAETTET_FS = "atg.verkehrsDatenKurzZeitGeglättetFs"; //$NON-NLS-1$	

	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitGeglättetMq</code>
	 */
	public static final String ATG_KURZZEIT_GEGLAETTET_MQ = "atg.verkehrsDatenKurzZeitGeglättetMq"; //$NON-NLS-1$		
	
	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitFs</code>
	 */
	public static final String ATG_KURZZEIT_FS = "atg.verkehrsDatenKurzZeitFs"; //$NON-NLS-1$		
	
	/**
	 * DAV-Atg-PID <code>atg.verkehrsDatenKurzZeitMq</code>
	 */
	public static final String ATG_KURZZEIT_MQ = "atg.verkehrsDatenKurzZeitMq"; //$NON-NLS-1$

	/**
	 * Wert <b>Ja</b> von Attribut-Typ <code>att.jaNein</code>
	 */
	public static final int JA = 1;
	
	/**
	 * Wert <b>Nein</b> von Attribut-Typ <code>att.jaNein</code>
	 */
	public static final int NEIN = 0;
	
	/**
	 * Daten sind nicht ermittelbar (ist KEIN Fehler). Wird gesetzt, wenn
	 * der entsprechende Wert nicht ermittelbar ist und kein Interpolation
	 * sinnvoll möglich ist (z.B. ist die Geschwindigkeit nicht ermittelbar,
	 * wenn kein Fahrzeug erfasst wurde)
	 */
	public static final int NICHT_ERMITTELBAR = -1;
	
	/**
	 * Daten sind fehlerhaft. 
	 * Wird gesetzt, wenn die Daten als fehlerhaft erkannt wurden.
	 */
	public static final int FEHLERHAFT = -2;
	
	/**
	 * Daten nicht ermittelbar, da bereits Basiswerte fehlerhaft. 
	 * Wird gesetzt, wenn Daten, die zur Berechnung dieses Werts notwendig sind,
	 * bereits als fehlerhaft gekennzeichnet sind, oder wenn die Berechnung aus
	 * anderen Gründen (z.B. Nenner = 0 in der Berechnungsformel) nicht möglich war.
	 */
	public static final int NICHT_ERMITTELBAR_BZW_FEHLERHAFT = -3;
	
	/**
	 * Undefinierter (nicht initialisierter Messwert)
	 */
	public static final int MESSWERT_UNBEKANNT = -4;
	
	/**
	 * Mittelwertbildung <code>unbekannt</code> 
	 */
	public static final int MWB_UNBEKANNT = -1;
	
	/**
	 * Mittelwertbildung <code>gleitende Mittelwertbildung</code>
	 */
	public static final int MWB_GLEITEND = 0;
	
	/**
	 * Mittelwertbildung <code>arithmetische Mittelwertbildung</code>
	 */
	public static final int MWB_ARITHMETISCH = 1;
	
	/**
	 * Standard-Format der Zeitangabe innerhalb der Betriebsmeldungen
	 */
	public static final SimpleDateFormat BM_ZEIT_FORMAT = 
						new SimpleDateFormat("dd.MM.yyyy HH:mm"); //$NON-NLS-1$
	
	/**
	 * Genaues Format der Zeitangabe mit Datum
	 */
	public static final SimpleDateFormat ZEIT_FORMAT_GENAU = 
						new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS"); //$NON-NLS-1$
	
	/**
	 * Genaues Format der Zeitangabe ohne Datum
	 */
	public static final SimpleDateFormat NUR_ZEIT_FORMAT_GENAU = 
						new SimpleDateFormat("HH:mm:ss.SSS"); //$NON-NLS-1$
	
	/**
	 * Feld mit allen innerhalb eines KZD-Satzes beschriebenen Attributen
	 */
	public static final String[] KZD_ATTRIBUTE = new String[]
	    {"qKfz", "vKfz", //$NON-NLS-1$ //$NON-NLS-2$
		 "qLkw", "vLkw", //$NON-NLS-1$ //$NON-NLS-2$
		 "qPkw", "vPkw", //$NON-NLS-1$ //$NON-NLS-2$
		 "b", //$NON-NLS-1$
		 "tNetto", //$NON-NLS-1$
		 "sKfz", //$NON-NLS-1$
		 "vgKfz"}; //$NON-NLS-1$
	
}
