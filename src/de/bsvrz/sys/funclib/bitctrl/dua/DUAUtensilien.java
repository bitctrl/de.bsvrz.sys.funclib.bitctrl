/**
 * Segment 4 Daten�bernahme und Aufbereitung (DUA), SWE 4.x
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
 * Wei�enfelser Stra�e 67<br>
 * 04229 Leipzig<br>
 * Phone: +49 341-490670<br>
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.AttributeType;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueRange;
import de.bsvrz.dav.daf.main.config.ObjectTimeSpecification;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dav.daf.main.impl.config.DafConfigurationAuthority;
import de.bsvrz.dav.daf.main.impl.config.DafConfigurationObject;
import de.bsvrz.dav.daf.main.impl.config.DafDynamicObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.bitctrl.konstante.Konstante;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Einige hilfreiche Methoden, die an verschiedenen Stellen innerhalb der DUA
 * Verwendung finden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 */
public class DUAUtensilien {

	/**
	 * Debug-Logger
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Schablone f�r eine ganze positive Zahl
	 */
	private static final String NATUERLICHE_ZAHL = "\\d+"; //$NON-NLS-1$

	/**
	 * Ersetzt den letzten Teil eines Attribuspfades durch eine bestimmte
	 * Zeichenkette. Der Aufruf<br>
	 * <code>"a.b.c.Status.PlFormal.WertMax" = ersetzeLetztesElemInAttPfad(
	 * "a.b.c.Wert", "Status.PlFormal.WertMax")</code><br>
	 * bewirkt bspw., dass auf den Statuswert <code>max</code> der formalen
	 * Plausibilisierung des Elements <code>a.b.c</code> zugegriffen werden
	 * kann
	 * 
	 * @param attPfad
	 *            der orginale Attributpfad
	 * @param ersetzung
	 *            die Zeichenkette, durch die der letzte Teil des Attributpfades
	 *            ersetzt werden soll
	 * @return einen ver�nderten Attributpfad oder <code>null</code>, wenn
	 *         die Ersetzung nicht durchgef�hrt werden konnte
	 */
	public static final String ersetzeLetztesElemInAttPfad(
			final String attPfad, final String ersetzung) {
		String ergebnis = null;

		if (attPfad != null && attPfad.length() > 0 && ersetzung != null
				&& ersetzung.length() > 0) {
			int letzterPunkt = attPfad.lastIndexOf("."); //$NON-NLS-1$
			if (letzterPunkt != -1) {
				ergebnis = attPfad.substring(0, letzterPunkt + 1) + ersetzung;
			} else {
				ergebnis = ersetzung;
			}
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge von <code>DAVObjektAnmeldung</code>-Objekten, die
	 * alle Anmeldungen unter der �bergebenen Datenbeschreibung f�r das
	 * �bergebene Objekt enth�lt.<br>
	 * <b>Achtung:</b> Das Objekt wird in seine finalen Instanzen aufgel�st.
	 * Sollte f�r das Objekt <code>null</code> �bergeben worden sein, so wird
	 * 'Alle Objekte' angenommen. Gleiches gilt f�r die Elemente der
	 * Datenbeschreibung.
	 * 
	 * @param obj
	 *            ein Systemobjekt (auch Typ)
	 * @param datenBeschreibung
	 *            eine Datenbeschreibung
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @return eine Menge von <code>DAVObjektAnmeldung</code>-Objekten
	 */
	public static final Collection<DAVObjektAnmeldung> getAlleObjektAnmeldungen(
			final SystemObject obj, final DataDescription datenBeschreibung,
			final ClientDavInterface dav) {
		Collection<DAVObjektAnmeldung> anmeldungen = new TreeSet<DAVObjektAnmeldung>();

		Collection<SystemObject> finObjekte = getBasisInstanzen(obj, dav);

		for (SystemObject finObj : finObjekte) {
			try {
				if (datenBeschreibung == null
						|| (datenBeschreibung.getAttributeGroup() == null && datenBeschreibung
								.getAspect() == null)) {
					for (AttributeGroup atg : finObj.getType()
							.getAttributeGroups()) {
						for (Aspect asp : atg.getAspects()) {
							anmeldungen.add(new DAVObjektAnmeldung(finObj,
									new DataDescription(atg, asp, (short) 0)));
						}
					}
				} else if (datenBeschreibung.getAttributeGroup() == null) {
					for (AttributeGroup atg : finObj.getType()
							.getAttributeGroups()) {
						try {
							anmeldungen.add(new DAVObjektAnmeldung(finObj,
									new DataDescription(atg, datenBeschreibung
											.getAspect(), (short) 0)));
						} catch (Exception ex) {
							LOGGER.fine(Konstante.LEERSTRING, ex);
						}
					}
				} else if (datenBeschreibung.getAspect() == null) {
					for (Aspect asp : datenBeschreibung.getAttributeGroup()
							.getAspects()) {
						anmeldungen.add(new DAVObjektAnmeldung(finObj,
								new DataDescription(datenBeschreibung
										.getAttributeGroup(), asp, (short) 0)));
					}
				} else {
					anmeldungen.add(new DAVObjektAnmeldung(finObj,
							datenBeschreibung));
				}
			} catch (Exception ex) {
				LOGGER.fine(Konstante.LEERSTRING, ex);
			}
		}

		return anmeldungen;
	}

	/**
	 * Liest eine Argument aus der ArgumentListe der Kommandozeile aus
	 * 
	 * @param schluessel
	 *            der Schl�ssel
	 * @param argumentListe
	 *            alle Argumente der Kommandozeile
	 * @return das Wert des DAV-Arguments mit dem �bergebenen Schl�ssel oder
	 *         <code>null</code>, wenn der Schl�ssel nicht gefunden wurde
	 */
	public static final String getArgument(final String schluessel,
			final List<String> argumentListe) {
		String ergebnis = null;

		if (schluessel != null && argumentListe != null) {
			for (String argument : argumentListe) {
				String[] teile = argument.split("="); //$NON-NLS-1$
				if (teile != null && teile.length > 1) {
					if (teile[0].equals("-" + schluessel)) { //$NON-NLS-1$
						ergebnis = teile[1];
						break;
					}
				}
			}
		}

		return ergebnis;
	}

	/**
	 * Extrahiert aus einem �bergebenen Datum ein darin enthaltenes Datum.
	 * 
	 * @param attributPfad
	 *            gibt den kompletten Pfad zu einem Attribut innerhalb einer
	 *            Attributgruppe an. Die einzelnen Pfadbestandteile sind jeweils
	 *            durch einen Punkt '.' separiert. Um z. B. ein Attribut mit dem
	 *            Namen "maxSichtweite", welches Bestandteil einer variablen
	 *            Liste (Array) mit dem Namen "ListeDerSichtweiten" zu
	 *            spezifizieren, ist folgendes einzutragen:
	 *            "ListeDerSichtweiten.2.maxSichtweite", wobei hier das dritte
	 *            Arrayelement der Liste angesprochen wird.
	 * @param datum
	 *            das Datum, aus dem ein eingebettetes Datum extrahiert werden
	 *            soll.
	 * @return das extrahierte Datum oder <code>null</code> wenn keine
	 *         Extraktion m�glich war
	 */
	public static final Data getAttributDatum(final String attributPfad,
			final Data datum) {
		Data ergebnis = null;

		if (datum != null) {
			if (attributPfad != null) {
				final String[] elemente = attributPfad.split("[.]"); //$NON-NLS-1$
				ergebnis = datum;
				for (String element : elemente) {
					if (ergebnis != null) {
						if (element.length() == 0) {
							LOGGER.warning("Syntaxfehler in Attributpfad: \""//$NON-NLS-1$
									+ attributPfad + "\""); //$NON-NLS-1$
							return null;
						}

						try {
							if (element.matches(NATUERLICHE_ZAHL)) {
								ergebnis = ergebnis.asArray().getItem(
										Integer.parseInt(element));
							} else {
								ergebnis = ergebnis.getItem(element);
							}
						} catch (Exception ex) {
							LOGGER.warning("Fehler bei Exploration von Datum " + //$NON-NLS-1$
									datum + " mit \"" + //$NON-NLS-1$
									attributPfad + "\"", ex); //$NON-NLS-1$
							return null;
						}

					} else {
						LOGGER
								.warning("Datensatz " + datum + " kann nicht bis \"" + //$NON-NLS-1$ //$NON-NLS-2$
										attributPfad + "\" exploriert werden."); //$NON-NLS-1$
					}
				}
			} else {
				LOGGER
						.warning("�bergebener Attributpfad ist " + DUAKonstanten.NULL); //$NON-NLS-1$
			}
		} else {
			LOGGER.warning("�bergebenes Datum ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge aller Konfigurationsobjekte bzw. Dynamischen Objekte
	 * (finale Objekte), die unter Umst�nden im Parameter <code>obj</code>
	 * 'versteckt' sind. Sollte als Objekte <code>
	 * null</code> �bergeben worden
	 * sein, so werden alle (finalen) Objekte zur�ckgegeben.
	 * 
	 * @param obj
	 *            ein Systemobjekt (finales Objekt oder Typ)
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @return eine Menge von finalen Systemobjekten
	 */
	public static final Collection<SystemObject> getBasisInstanzen(
			final SystemObject obj, final ClientDavInterface dav) {
		Collection<SystemObject> finaleObjekte = new HashSet<SystemObject>();

		if (obj == null || obj.getPid().equals(DaVKonstanten.TYP_TYP)) {
			SystemObjectType typTyp = dav.getDataModel().getType(
					DaVKonstanten.TYP_TYP);
			for (SystemObject typ : typTyp.getElements()) {
				if (typ instanceof SystemObjectType) {
					for (SystemObject elem : ((SystemObjectType) typ)
							.getElements()) {
						if (elem.getClass()
								.equals(DafConfigurationObject.class)
								|| elem.getClass().equals(
										DafDynamicObject.class)
								|| elem.getClass().equals(
										DafConfigurationAuthority.class)) {
							finaleObjekte.add(elem);
						}
					}
				}
			}
		} else if (obj instanceof SystemObjectType) {
			SystemObjectType typ = (SystemObjectType) obj;
			for (SystemObject elem : typ.getElements()) {
				finaleObjekte.addAll(getBasisInstanzen(elem, dav));
			}
		} else if (obj.getClass().equals(DafConfigurationObject.class)
				|| obj.getClass().equals(DafDynamicObject.class)
				|| obj.getClass().equals(DafConfigurationAuthority.class)) {
			finaleObjekte.add(obj);
		} else {
			LOGGER.fine("Das �bergebene Objekt ist weder ein Typ," + //$NON-NLS-1$
					" ein Konfigurationsobjekt, ein dynamisches Objekt" + //$NON-NLS-1$
					" noch eine Konfigurationsautorit�t: " + obj); //$NON-NLS-1$
		}

		return finaleObjekte;
	}

	/**
	 * Erfragt die Menge aller Konfigurationsobjekte bzw. Dynamischen Objekte
	 * (finale Objekte), die unter Umst�nden im Argument <code>obj</code>
	 * 'versteckt' sind <b>und au�erdem innerhalb der �bergebenen
	 * Konfigurationsbereiche liegen</b>. Sollte als Objekte <code>
	 * null</code>
	 * �bergeben worden sein, so werden alle (finalen) Objekte zur�ckgegeben.
	 * 
	 * @param obj
	 *            ein Systemobjekt (finales Objekt oder Typ)
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @param kBereichsFilter
	 *            eine Menge von Konfigurationsbereichen
	 * @return eine Menge von finalen Systemobjekten, die innerhalb der
	 *         �bergebenen Konfigurationsbereiche (bzw. im
	 *         Standardkonfigurationsbereich) definiert sind.
	 */
	public static final Collection<SystemObject> getBasisInstanzen(
			final SystemObject obj, final ClientDavInterface dav,
			final Collection<ConfigurationArea> kBereichsFilter) {
		Collection<SystemObject> finaleObjekte = new HashSet<SystemObject>();
		Collection<ConfigurationArea> benutzteBereiche = new HashSet<ConfigurationArea>();

		if (kBereichsFilter != null && kBereichsFilter.size() > 0) {
			benutzteBereiche = kBereichsFilter;
		} else {
			/**
			 * Es wurden keine Konfigurationsbereiche �bergeben:
			 * Standardkonfigurationsbereich wird verwendet
			 */
			benutzteBereiche.add(dav.getDataModel().getConfigurationAuthority()
					.getConfigurationArea());
		}

		if (obj == null || obj.getPid().equals(DaVKonstanten.TYP_TYP)) {
			Collection<SystemObjectType> typColl = new TreeSet<SystemObjectType>();
			for (SystemObject typ : dav.getDataModel().getType(
					DaVKonstanten.TYP_TYP).getElements()) {
				if (typ instanceof SystemObjectType) {
					typColl.add((SystemObjectType) typ);
				}
			}
			for (ConfigurationArea kb : benutzteBereiche) {
				for (SystemObject elem : kb.getObjects(typColl,
						ObjectTimeSpecification.valid())) {
					if (elem.getClass().equals(DafConfigurationObject.class)
							|| elem.getClass().equals(DafDynamicObject.class)
							|| elem.getClass().equals(
									DafConfigurationAuthority.class)) {
						finaleObjekte.add(elem);
					}
				}
			}
		} else if (obj instanceof SystemObjectType) {
			Collection<SystemObjectType> typColl = new ArrayList<SystemObjectType>();
			typColl.add((SystemObjectType) obj);
			for (ConfigurationArea kb : benutzteBereiche) {
				for (SystemObject elem : kb.getObjects(typColl,
						ObjectTimeSpecification.valid())) {
					if (elem.getClass().equals(DafConfigurationObject.class)
							|| elem.getClass().equals(DafDynamicObject.class)
							|| elem.getClass().equals(
									DafConfigurationAuthority.class)) {
						finaleObjekte.add(elem);
					}
				}
			}
		} else if (obj.getClass().equals(DafConfigurationObject.class)
				|| obj.getClass().equals(DafDynamicObject.class)
				|| obj.getClass().equals(DafConfigurationAuthority.class)) {
			if (benutzteBereiche.contains(obj.getConfigurationArea())) {
				finaleObjekte.add(obj);
			}
		} else {
			LOGGER.fine("Das �bergebene Objekt ist weder ein Typ," + //$NON-NLS-1$
					" ein Konfigurationsobjekt, ein dynamisches Objekt" + //$NON-NLS-1$
					" noch eine Konfigurationsautorit�t: " + obj); //$NON-NLS-1$
		}

		return finaleObjekte;
	}

	/**
	 * Gibt die Anhahl der Stunden zur�ck, die dieser Tag hat.
	 * 
	 * @param zeitStempel
	 *            ein Zeitpunkt, der innerhalb des entsprechenden Tages liegt
	 * @return Anzahl der Stunden, die dieser Tag hat
	 */
	public static int getStundenVonTag(final long zeitStempel) {
		int stundenDesTages = 24;

		GregorianCalendar cal1 = new GregorianCalendar();
		GregorianCalendar cal2 = new GregorianCalendar();
		cal1.setTimeInMillis(zeitStempel);
		cal2.setTimeInMillis(zeitStempel);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal2.set(Calendar.HOUR_OF_DAY, 10);

		if ((cal1.get(Calendar.DST_OFFSET) / 60l / 60l / 1000l) == 0
				&& (cal2.get(Calendar.DST_OFFSET) / 60l / 60l / 1000l) == 1)
			stundenDesTages = 23;
		if ((cal1.get(Calendar.DST_OFFSET) / 60l / 60l / 1000l) == 1
				&& (cal2.get(Calendar.DST_OFFSET) / 60l / 60l / 1000l) == 0)
			stundenDesTages = 25;

		return stundenDesTages;
	}

	/**
	 * Erfragt die textliche Entsprechung eines Messwertes, dessen Wertebereich
	 * bei 0 (inklusive) beginnt und der die Zust�nde <code>fehlerhaft</code>,
	 * <code>nicht ermittelbar</code> oder
	 * <code>nicht ermittelbar/fehlerhaft</code> besitzen kann
	 * 
	 * @param messwert
	 *            ein Messwert
	 * @return die textliche Entsprechung eines Messwertes
	 */
	public static final String getTextZuMesswert(final long messwert) {
		String s = "undefiniert (" + messwert + ")"; //$NON-NLS-1$ //$NON-NLS-2$

		if (messwert >= 0) {
			s = new Long(messwert).toString();
		} else if (messwert == DUAKonstanten.NICHT_ERMITTELBAR) {
			s = "nicht ermittelbar"; //$NON-NLS-1$
		} else if (messwert == DUAKonstanten.FEHLERHAFT) {
			s = "fehlerhaft"; //$NON-NLS-1$
		} else if (messwert == DUAKonstanten.NICHT_ERMITTELBAR_BZW_FEHLERHAFT) {
			s = "nicht ermittelbar/fehlerhaft"; //$NON-NLS-1$
		}

		return s;
	}

	/**
	 * Erfragt, ob die �bergebene Systemobjekt-Attributgruppen-Aspekt-
	 * Kombination g�ltig bzw. kompatibel (bzw. so anmeldbar) ist.
	 * 
	 * @param obj
	 *            das (finale) Systemobjekt
	 * @param datenBeschreibung
	 *            die Datenbeschreibung
	 * @return <code>null</code>, wenn die �bergebene Systemobjekt-
	 *         Attributgruppen-Aspekt-Kombination g�ltig ist, entweder. Oder
	 *         eine die Inkombatibilit�t beschreibende Fehlermeldung sonst.
	 */
	public static final String isKombinationOk(final SystemObject obj,
			final DataDescription datenBeschreibung) {
		String result = null;

		if (obj == null) {
			result = "Objekt ist " + DUAKonstanten.NULL; //$NON-NLS-1$
		} else if (datenBeschreibung == null) {
			result = "Datenbeschreibung ist " + DUAKonstanten.NULL; //$NON-NLS-1$
		} else if (datenBeschreibung.getAttributeGroup() == null) {
			result = "Attributgruppe ist " + DUAKonstanten.NULL; //$NON-NLS-1$
		} else if (datenBeschreibung.getAspect() == null) {
			result = "Aspekt ist " + DUAKonstanten.NULL; //$NON-NLS-1$
		} else if (!obj.getType().getAttributeGroups().contains(
				datenBeschreibung.getAttributeGroup())) {
			result = "Attributgruppe " + datenBeschreibung.getAttributeGroup() + //$NON-NLS-1$
					" ist f�r Objekt " + obj + //$NON-NLS-1$
					" nicht definiert"; //$NON-NLS-1$
		} else if (!datenBeschreibung.getAttributeGroup().getAspects()
				.contains(datenBeschreibung.getAspect())) {
			result = "Aspekt " + datenBeschreibung.getAspect() + //$NON-NLS-1$
					" ist f�r Attributgruppe " //$NON-NLS-1$
					+ datenBeschreibung.getAttributeGroup() +
					" nicht definiert"; //$NON-NLS-1$)
		} else if (!(obj.getClass().equals(DafConfigurationObject.class)
				|| obj.getClass().equals(DafDynamicObject.class) || obj
				.getClass().equals(DafConfigurationAuthority.class))) {
			result = "Es handelt sich weder um ein Konfigurationsobjekt, " + //$NON-NLS-1$
					"ein dynamisches Objekt noch eine Konfigurationsautorit�t: " //$NON-NLS-1$
					+ obj;
		}

		return result;
	}

	/**
	 * Ermittelt, ob der �bergebene Wert im Wertebereich des �bergebenen
	 * Attributs liegt (f�r skalierte Ganzzahlen)
	 * 
	 * @param attribut
	 *            das skalierte Ganzzahl-Attribut
	 * @param wertSkaliert
	 *            der skalierte Wert
	 * @return <code>false</code>, wenn das �bergebene Attribut ein
	 *         skaliertes Ganzzahl-Attribut ist <b>und</b> einen Wertebereich
	 *         besitzt <b>und</b> dieser durch den �bergebenen Wert verletzt
	 *         ist, sonst <code>true</code>
	 */
	public static final boolean isWertInWerteBereich(Data attribut,
			double wertSkaliert) {
		boolean ergebnis = true;

		if (attribut != null) {
			AttributeType typ = attribut.getAttributeType();

			if (typ instanceof IntegerAttributeType) {
				IntegerValueRange wertebereich = ((IntegerAttributeType) typ)
						.getRange();

				long wertUnskaliert = Math.round(wertSkaliert
						/ wertebereich.getConversionFactor());
				if (wertebereich != null) {
					if (wertUnskaliert < wertebereich.getMinimum()
							|| wertUnskaliert > wertebereich.getMaximum()) {
						ergebnis = false;
					}
				}
			}
		} else {
			throw new NullPointerException("�bergebenes Attribut ist <<null>>"); //$NON-NLS-1$
		}

		return ergebnis;
	}

	/**
	 * Ermittelt, ob der �bergebene Wert im Wertebereich des �bergebenen
	 * Attributs liegt
	 * 
	 * @param attribut
	 *            das Ganzzahl-Attribut
	 * @param wert
	 *            der Wert
	 * @return <code>false</code>, wenn das �bergebene Attribut ein
	 *         Ganzzahl-Attribut ist <b>und</b> einen Wertebereich besitzt
	 *         <b>und</b> dieser durch den �bergebenen Wert verletzt ist, sonst
	 *         <code>true</code>
	 */
	public static final boolean isWertInWerteBereich(Data attribut, long wert) {
		boolean ergebnis = true;

		if (attribut != null) {
			AttributeType typ = attribut.getAttributeType();

			if (typ instanceof IntegerAttributeType) {
				IntegerValueRange wertebereich = ((IntegerAttributeType) typ)
						.getRange();
				if (wertebereich != null) {
					if (wert < wertebereich.getMinimum()
							|| wert > wertebereich.getMaximum()) {
						ergebnis = false;
					}
				}
			}
		} else {
			throw new NullPointerException("�bergebenes Attribut ist <<null>>"); //$NON-NLS-1$
		}

		return ergebnis;
	}
	
	
	/**
	 * Erfragt den gerundeten Wert als Zeichenkette
	 * 
	 * @param wert ein Wert
	 * @param nachkommastellen Rundungsstellen
	 * @return der gerundete Wert als Zeichenkette
	 */
	public static final String runde(double wert, int nachkommastellen){
		double nachkommaDouble = Math.pow(10, nachkommastellen);
		return new Double(Math.round(wert * nachkommaDouble) / nachkommaDouble).toString();
	}

	
	/**
	 * Wandelt eine Zeitspanne in Millisekunden in einen Text um
	 * 
	 * @param vergleichsIntervallInMs zeit in Millisekunden
	 * @return die uebergebene Zeitspanne in Millisekunden als Text
	 */
	public static final String getVergleichsIntervallInText(long vergleichsIntervallInMs){
		StringBuffer text = new StringBuffer();
			
		try {
			long val = vergleichsIntervallInMs;
			int millis = (int)(val % 1000);
			val /= 1000;
			int seconds = (int)(val % 60);
			val /= 60;
			int minutes = (int)(val % 60);
			val /= 60;
			int hours = (int)(val % 24);
			val /= 24;
			long days = val;
			if(days != 0) {
				if(days == 1) {
					text.append("1 Tag "); //$NON-NLS-1$
				}
				else if(days == -1) {
					text.append("-1 Tag "); //$NON-NLS-1$
				}
				else {
					text.append(days).append(" Tage "); //$NON-NLS-1$
				}
			}
			if(hours != 0) {
				if(hours == 1) {
					text.append("1 Stunde "); //$NON-NLS-1$
				}
				else if(hours == -1) {
					text.append("-1 Stunde "); //$NON-NLS-1$
				}
				else {
					text.append(hours).append(" Stunden "); //$NON-NLS-1$
				}
			}
			if(minutes != 0) {
				if(minutes == 1) {
					text.append("1 Minute "); //$NON-NLS-1$
				}
				else if(minutes == -1) {
					text.append("-1 Minute "); //$NON-NLS-1$
				}
				else {
					text.append(minutes).append(" Minuten "); //$NON-NLS-1$
				}
			}
			if(seconds != 0 || (days == 0 && hours == 0 && minutes == 0 && millis == 0)) {
				if(seconds == 1) {
					text.append("1 Sekunde "); //$NON-NLS-1$
				}
				else if(seconds == -1) {
					text.append("-1 Sekunde "); //$NON-NLS-1$
				}
				else {
					text.append(seconds).append(" Sekunden "); //$NON-NLS-1$
				}
			}
			if(millis != 0) {
				if(millis == 1) {
					text.append("1 Millisekunde "); //$NON-NLS-1$
				}
				else if(millis == -1) {
					text.append("-1 Millisekunde "); //$NON-NLS-1$
				}
				else {
					text.append(millis).append(" Millisekunden "); //$NON-NLS-1$
				}
			}
			text.setLength(text.length() - 1);
		}
		catch(Exception e) {
			return "[" + vergleichsIntervallInMs + "ms]";  //$NON-NLS-1$//$NON-NLS-2$
		}
	
		return text.toString();
	}
	
}
