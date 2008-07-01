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

package de.bsvrz.sys.funclib.bitctrl.dua;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import com.bitctrl.Constants;

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
import de.bsvrz.dav.daf.main.config.AttributeType;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.IntegerAttributeType;
import de.bsvrz.dav.daf.main.config.IntegerValueRange;
import de.bsvrz.dav.daf.main.config.IntegerValueState;
import de.bsvrz.dav.daf.main.config.ObjectTimeSpecification;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.config.SystemObjectType;
import de.bsvrz.dav.daf.main.impl.config.DafConfigurationAuthority;
import de.bsvrz.dav.daf.main.impl.config.DafConfigurationObject;
import de.bsvrz.dav.daf.main.impl.config.DafDynamicObject;
import de.bsvrz.sys.funclib.bitctrl.daf.DaVKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.av.DAVObjektAnmeldung;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Einige hilfreiche Methoden, die an verschiedenen Stellen innerhalb der DUA
 * Verwendung finden.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public final class DUAUtensilien {

	/**
	 * statischer Sender von Standardparametern.
	 */
	private static ClientSenderInterface parameterSender = null;

	/**
	 * Standardkonstruktor.
	 */
	private DUAUtensilien() {

	}

	/**
	 * Schablone für eine ganze positive Zahl.
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
	 * @return einen veränderten Attributpfad oder <code>null</code>, wenn
	 *         die Ersetzung nicht durchgeführt werden konnte
	 */
	public static String ersetzeLetztesElemInAttPfad(final String attPfad,
			final String ersetzung) {
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
	 * alle Anmeldungen unter der übergebenen Datenbeschreibung für das
	 * übergebene Objekt enthält.<br>
	 * <b>Achtung:</b> Das Objekt wird in seine finalen Instanzen aufgelöst.
	 * Sollte für das Objekt <code>null</code> übergeben worden sein, so wird
	 * 'Alle Objekte' angenommen. Gleiches gilt für die Elemente der
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
	public static Collection<DAVObjektAnmeldung> getAlleObjektAnmeldungen(
			final SystemObject obj, final DataDescription datenBeschreibung,
			final ClientDavInterface dav) {
		Collection<DAVObjektAnmeldung> anmeldungen = new TreeSet<DAVObjektAnmeldung>();

		Collection<SystemObject> finObjekte = getBasisInstanzen(obj, dav);

		for (SystemObject finObj : finObjekte) {
			if (datenBeschreibung == null
					|| (datenBeschreibung.getAttributeGroup() == null && datenBeschreibung
							.getAspect() == null)) {
				for (AttributeGroup atg : finObj.getType().getAttributeGroups()) {
					for (Aspect asp : atg.getAspects()) {
						anmeldungen.add(new DAVObjektAnmeldung(finObj,
								new DataDescription(atg, asp, (short) 0)));
					}
				}
			} else if (datenBeschreibung.getAttributeGroup() == null) {
				for (AttributeGroup atg : finObj.getType().getAttributeGroups()) {
					try {
						anmeldungen.add(new DAVObjektAnmeldung(finObj,
								new DataDescription(atg, datenBeschreibung
										.getAspect(), (short) 0)));
					} catch (IllegalArgumentException ex) {
						Debug.getLogger().fine(Constants.EMPTY_STRING, ex);
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
		}

		return anmeldungen;
	}

	/**
	 * Liest eine Argument aus der ArgumentListe der Kommandozeile aus.
	 * 
	 * @param schluessel
	 *            der Schlüssel
	 * @param argumentListe
	 *            alle Argumente der Kommandozeile
	 * @return das Wert des DAV-Arguments mit dem übergebenen Schlüssel oder
	 *         <code>null</code>, wenn der Schlüssel nicht gefunden wurde
	 */
	public static String getArgument(final String schluessel,
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
	 * Extrahiert aus einem übergebenen Datum ein darin enthaltenes Datum.
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
	 *         Extraktion möglich war
	 */
	public static Data getAttributDatum(final String attributPfad,
			final Data datum) {
		Data ergebnis = null;

		if (datum != null) {
			if (attributPfad != null) {
				final String[] elemente = attributPfad.split("[.]"); //$NON-NLS-1$
				ergebnis = datum;
				for (String element : elemente) {
					if (ergebnis != null) {
						if (element.length() == 0) {
							Debug.getLogger().warning(
									"Syntaxfehler in Attributpfad: \""//$NON-NLS-1$
											+ attributPfad + "\""); //$NON-NLS-1$
							return null;
						}

						if (element.matches(NATUERLICHE_ZAHL)) {
							ergebnis = ergebnis.asArray().getItem(
									Integer.parseInt(element));
						} else {
							ergebnis = ergebnis.getItem(element);
						}

					} else {
						Debug.getLogger().warning(
								"Datensatz " + datum + " kann nicht bis \"" + //$NON-NLS-1$ //$NON-NLS-2$
										attributPfad + "\" exploriert werden."); //$NON-NLS-1$
					}
				}
			} else {
				Debug.getLogger().warning(
						"Übergebener Attributpfad ist " + DUAKonstanten.NULL); //$NON-NLS-1$
			}
		} else {
			Debug.getLogger().warning(
					"Übergebenes Datum ist " + DUAKonstanten.NULL); //$NON-NLS-1$
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge aller Konfigurationsobjekte bzw. Dynamischen Objekte
	 * (finale Objekte), die unter Umständen im Parameter <code>obj</code>
	 * 'versteckt' sind. Sollte als Objekte <code>
	 * null</code> übergeben worden
	 * sein, so werden alle (finalen) Objekte zurückgegeben.
	 * 
	 * @param obj
	 *            ein Systemobjekt (finales Objekt oder Typ)
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @return eine Menge von finalen Systemobjekten
	 */
	public static Collection<SystemObject> getBasisInstanzen(
			final SystemObject obj, final ClientDavInterface dav) {
		Collection<SystemObject> finaleObjekte = new HashSet<SystemObject>();

		if (obj == null || obj.getPid().equals(DaVKonstanten.TYP_TYP)) {
			SystemObjectType typTyp = dav.getDataModel().getType(
					DaVKonstanten.TYP_TYP);
			for (SystemObject typ : typTyp.getElements()) {
				if (typ.isValid()) {
					if (typ instanceof SystemObjectType) {
						for (SystemObject elem : ((SystemObjectType) typ)
								.getElements()) {
							if (elem.isValid()) {
								if (elem.getClass().equals(
										DafConfigurationObject.class)
										|| elem.getClass().equals(
												DafDynamicObject.class)
										|| elem
												.getClass()
												.equals(
														DafConfigurationAuthority.class)) {
									finaleObjekte.add(elem);
								}
							}
						}
					}
				}
			}
		} else if (obj instanceof SystemObjectType) {
			SystemObjectType typ = (SystemObjectType) obj;
			for (SystemObject elem : typ.getElements()) {
				if (elem.isValid()) {
					finaleObjekte.addAll(getBasisInstanzen(elem, dav));
				}
			}
		} else if (obj.getClass().equals(DafConfigurationObject.class)
				|| obj.getClass().equals(DafDynamicObject.class)
				|| obj.getClass().equals(DafConfigurationAuthority.class)) {
			finaleObjekte.add(obj);
		} else {
			Debug.getLogger().fine("Das übergebene Objekt ist weder ein Typ," + //$NON-NLS-1$
					" ein Konfigurationsobjekt, ein dynamisches Objekt" + //$NON-NLS-1$
					" noch eine Konfigurationsautorität: " + obj); //$NON-NLS-1$
		}

		return finaleObjekte;
	}

	/**
	 * Erfragt die Menge aller Konfigurationsobjekte bzw. Dynamischen Objekte
	 * (finale Objekte), die unter Umständen im Argument <code>obj</code>
	 * 'versteckt' sind <b>und außerdem innerhalb der übergebenen
	 * Konfigurationsbereiche liegen</b>. Sollte als Objekte <code>
	 * null</code>
	 * übergeben worden sein, so werden alle (finalen) Objekte zurückgegeben.
	 * 
	 * @param obj
	 *            ein Systemobjekt (finales Objekt oder Typ)
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 * @param kBereichsFilter
	 *            eine Menge von Konfigurationsbereichen
	 * @return eine Menge von finalen Systemobjekten, die innerhalb der
	 *         übergebenen Konfigurationsbereiche (bzw. im
	 *         Standardkonfigurationsbereich) definiert sind.
	 */
	public static Collection<SystemObject> getBasisInstanzen(
			final SystemObject obj, final ClientDavInterface dav,
			final Collection<ConfigurationArea> kBereichsFilter) {
		Collection<SystemObject> finaleObjekte = new HashSet<SystemObject>();
		Collection<ConfigurationArea> benutzteBereiche = new HashSet<ConfigurationArea>();

		if (kBereichsFilter != null && kBereichsFilter.size() > 0) {
			benutzteBereiche = kBereichsFilter;
		} else {
			/**
			 * Es wurden keine Konfigurationsbereiche übergeben:
			 * Standardkonfigurationsbereich wird verwendet
			 */
			benutzteBereiche.add(dav.getDataModel().getConfigurationAuthority()
					.getConfigurationArea());
		}

		if (obj == null || obj.getPid().equals(DaVKonstanten.TYP_TYP)) {
			Collection<SystemObjectType> typColl = new TreeSet<SystemObjectType>();
			for (SystemObject typ : dav.getDataModel().getType(
					DaVKonstanten.TYP_TYP).getElements()) {
				if (typ.isValid() && typ instanceof SystemObjectType) {
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
			Debug.getLogger().fine("Das übergebene Objekt ist weder ein Typ," + //$NON-NLS-1$
					" ein Konfigurationsobjekt, ein dynamisches Objekt" + //$NON-NLS-1$
					" noch eine Konfigurationsautorität: " + obj); //$NON-NLS-1$
		}

		return finaleObjekte;
	}

	/**
	 * Gibt die Anhahl der Stunden zurück, die dieser Tag hat.
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

		if ((cal1.get(Calendar.DST_OFFSET) / 60L / 60L / 1000L) == 0
				&& (cal2.get(Calendar.DST_OFFSET) / 60L / 60L / 1000L) == 1) {
			stundenDesTages = 23;
		}
		if ((cal1.get(Calendar.DST_OFFSET) / 60L / 60L / 1000L) == 1
				&& (cal2.get(Calendar.DST_OFFSET) / 60L / 60L / 1000L) == 0) {
			stundenDesTages = 25;
		}

		return stundenDesTages;
	}

	/**
	 * Erfragt die textliche Entsprechung eines Messwertes, dessen Wertebereich
	 * bei 0 (inklusive) beginnt und der die Zustände <code>fehlerhaft</code>,
	 * <code>nicht ermittelbar</code> oder
	 * <code>nicht ermittelbar/fehlerhaft</code> besitzen kann.
	 * 
	 * @param messwert
	 *            ein Messwert
	 * @return die textliche Entsprechung eines Messwertes
	 */
	public static String getTextZuMesswert(final long messwert) {
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
	 * Erfragt, ob die übergebene Systemobjekt-Attributgruppen-Aspekt-
	 * Kombination gültig bzw. kompatibel (bzw. so anmeldbar) ist.
	 * 
	 * @param obj
	 *            das (finale) Systemobjekt
	 * @param datenBeschreibung
	 *            die Datenbeschreibung
	 * @return <code>null</code>, wenn die übergebene Systemobjekt-
	 *         Attributgruppen-Aspekt-Kombination gültig ist, entweder. Oder
	 *         eine die Inkombatibilität beschreibende Fehlermeldung sonst.
	 */
	public static String isKombinationOk(final SystemObject obj,
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
					" ist für Objekt " + obj + //$NON-NLS-1$
					" nicht definiert"; //$NON-NLS-1$
		} else if (!datenBeschreibung.getAttributeGroup().getAspects()
				.contains(datenBeschreibung.getAspect())) {
			result = "Aspekt " + datenBeschreibung.getAspect() + //$NON-NLS-1$
					" ist für Attributgruppe " //$NON-NLS-1$
					+ datenBeschreibung.getAttributeGroup()
					+ " nicht definiert"; //$NON-NLS-1$)
		} else if (!(obj.getClass().equals(DafConfigurationObject.class)
				|| obj.getClass().equals(DafDynamicObject.class) || obj
				.getClass().equals(DafConfigurationAuthority.class))) {
			result = "Es handelt sich weder um ein Konfigurationsobjekt, " + //$NON-NLS-1$
					"ein dynamisches Objekt noch eine Konfigurationsautorität: " //$NON-NLS-1$
					+ obj;
		}

		return result;
	}

	/**
	 * Ermittelt, ob der übergebene Wert im Wertebereich des übergebenen
	 * Attributs liegt (für skalierte Ganzzahlen).
	 * 
	 * @param attribut
	 *            das skalierte Ganzzahl-Attribut
	 * @param wertSkaliert
	 *            der skalierte Wert
	 * @return <code>false</code>, wenn das übergebene Attribut ein
	 *         skaliertes Ganzzahl-Attribut ist <b>und</b> einen Wertebereich
	 *         besitzt <b>und</b> dieser durch den übergebenen Wert verletzt
	 *         ist, sonst <code>true</code>
	 */
	public static boolean isWertInWerteBereich(Data attribut,
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
			throw new NullPointerException("Übergebenes Attribut ist <<null>>"); //$NON-NLS-1$
		}

		return ergebnis;
	}

	/**
	 * Ermittelt, ob der uebergebene Wert im Wertebereich des uebergebenen
	 * Attributs liegt.
	 * 
	 * @param attribut
	 *            das Ganzzahl-Attribut
	 * @param wert
	 *            der Wert
	 * @return <code>false</code>, wenn das uebergebene Attribut ein
	 *         Ganzzahl-Attribut ist <b>und</b> einen Wertebereich besitzt
	 *         <b>und</b> dieser durch den uebergebenen Wert verletzt ist,
	 *         sonst <code>true</code>
	 */
	public static boolean isWertInWerteBereich(Data attribut, long wert) {
		boolean ergebnis = true;

		if (attribut != null) {
			AttributeType typ = attribut.getAttributeType();

			if (typ instanceof IntegerAttributeType) {
				List<IntegerValueState> statuss = ((IntegerAttributeType) typ)
						.getStates();
				if (!statuss.isEmpty()) {
					for (IntegerValueState status : ((IntegerAttributeType) typ)
							.getStates()) {
						if (wert == status.getValue()) {
							return true;
						}
					}
				}

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
			throw new NullPointerException("Übergebenes Attribut ist <<null>>"); //$NON-NLS-1$
		}

		return ergebnis;
	}

	/**
	 * Erfragt den gerundeten Wert als Zeichenkette.
	 * 
	 * @param wert
	 *            ein Wert
	 * @param nachkommastellen
	 *            Rundungsstellen
	 * @return der gerundete Wert als Zeichenkette
	 */
	public static String runde(double wert, int nachkommastellen) {
		double nachkommaDouble = Math.pow(10, nachkommastellen);
		return new Double(Math.round(wert * nachkommaDouble) / nachkommaDouble)
				.toString();
	}

	/**
	 * Wandelt eine Zeitspanne in Millisekunden in einen Text um.
	 * 
	 * @param vergleichsIntervallInMs
	 *            zeit in Millisekunden
	 * @return die uebergebene Zeitspanne in Millisekunden als Text
	 */
	public static String getVergleichsIntervallInText(
			long vergleichsIntervallInMs) {
		StringBuffer text = new StringBuffer();

		try {
			long val = vergleichsIntervallInMs;
			int millis = (int) (val % 1000);
			val /= 1000;
			int seconds = (int) (val % 60);
			val /= 60;
			int minutes = (int) (val % 60);
			val /= 60;
			int hours = (int) (val % 24);
			val /= 24;
			long days = val;
			if (days != 0) {
				if (days == 1) {
					text.append("1 Tag "); //$NON-NLS-1$
				} else if (days == -1) {
					text.append("-1 Tag "); //$NON-NLS-1$
				} else {
					text.append(days).append(" Tage "); //$NON-NLS-1$
				}
			}
			if (hours != 0) {
				if (hours == 1) {
					text.append("1 Stunde "); //$NON-NLS-1$
				} else if (hours == -1) {
					text.append("-1 Stunde "); //$NON-NLS-1$
				} else {
					text.append(hours).append(" Stunden "); //$NON-NLS-1$
				}
			}
			if (minutes != 0) {
				if (minutes == 1) {
					text.append("1 Minute "); //$NON-NLS-1$
				} else if (minutes == -1) {
					text.append("-1 Minute "); //$NON-NLS-1$
				} else {
					text.append(minutes).append(" Minuten "); //$NON-NLS-1$
				}
			}
			if (seconds != 0
					|| (days == 0 && hours == 0 && minutes == 0 && millis == 0)) {
				if (seconds == 1) {
					text.append("1 Sekunde "); //$NON-NLS-1$
				} else if (seconds == -1) {
					text.append("-1 Sekunde "); //$NON-NLS-1$
				} else {
					text.append(seconds).append(" Sekunden "); //$NON-NLS-1$
				}
			}
			if (millis != 0) {
				if (millis == 1) {
					text.append("1 Millisekunde "); //$NON-NLS-1$
				} else if (millis == -1) {
					text.append("-1 Millisekunde "); //$NON-NLS-1$
				} else {
					text.append(millis).append(" Millisekunden "); //$NON-NLS-1$
				}
			}
			text.setLength(text.length() - 1);
		} catch (Exception e) {
			return "[" + vergleichsIntervallInMs + "ms]"; //$NON-NLS-1$//$NON-NLS-2$
		}

		return text.toString();
	}

	/**
	 * Erfragt eine Kurzinformation eines Objektarrays.
	 * 
	 * @param objekte
	 *            alle Objekte
	 * @return eine Kurzinformation eines Objektarrays
	 */
	public static String getArrayKurzInfo(Object[] objekte) {
		String kurzInfo = ""; //$NON-NLS-1$

		if (objekte != null) {
			kurzInfo = new Integer(objekte.length).toString();
			if (objekte.length > 0) {
				kurzInfo += " ["; //$NON-NLS-1$
				for (Object obj : objekte) {
					String dummy = null;
					if (obj == null) {
						dummy = "<<null>>"; //$NON-NLS-1$
					} else {
						dummy = obj.toString();
						if (dummy == null) {
							dummy = "$<<null>>"; //$NON-NLS-1$
						}
					}
					kurzInfo += dummy + ", "; //$NON-NLS-1$

					if (kurzInfo.length() > 70) {
						kurzInfo = kurzInfo.substring(0, 70) + "..., "; //$NON-NLS-1$
						break;
					}
				}
				kurzInfo = kurzInfo.substring(0, kurzInfo.length() - 2);
				kurzInfo += "]"; //$NON-NLS-1$
			}
		} else {
			kurzInfo = "<<null>>"; //$NON-NLS-1$
		}

		return kurzInfo;
	}

	/**
	 * Erfragt, ob ein Systemobjekt in einem der uebergebenen
	 * Konfigurationsbereiche enthalten ist.
	 * 
	 * @param obj
	 *            ein Systemobjekt
	 * @param kbs
	 *            eine Menge von Konfigurationsbereichen
	 * @return <code>true</code>, wenn das Systemobjekt in einem der
	 *         uebergebenen Konfigurationsbereiche enthalten ist bzw. wenn die
	 *         Menge der Konfigurationsbereiche <code>null</code> oder leer
	 *         ist. Sonst <code>false</code>
	 */
	public static boolean isObjektInKBsEnthalten(final SystemObject obj,
			final ConfigurationArea[] kbs) {
		boolean enthalten = true;

		if (kbs != null && kbs.length > 0) {
			enthalten = false;
			for (ConfigurationArea kb : kbs) {
				if (obj.getConfigurationArea().equals(kb)) {
					enthalten = true;
					break;
				}
			}
		}

		return enthalten;
	}

	/**
	 * Parametriert die Parametrierung dergestalt, dass von dieser <b>alle</b>
	 * Parameter erfasst werden.
	 * 
	 * @param dav
	 *            Verbindung zum Datenverteiler
	 */
	public static void setAlleParameter(final ClientDavInterface dav) {
		DataDescription datenBeschreibung = new DataDescription(dav
				.getDataModel().getAttributeGroup("atg.parametrierung"), dav
				.getDataModel().getAspect("asp.parameterVorgabe"));

		if (parameterSender == null) {
			parameterSender = new ClientSenderInterface() {

				/**
				 * {@inheritDoc}
				 */
				public void dataRequest(SystemObject object,
						DataDescription dataDescription, byte state) {
					//
				}

				/**
				 * {@inheritDoc}
				 */
				public boolean isRequestSupported(SystemObject object,
						DataDescription dataDescription) {
					return false;
				}

			};

			try {
				dav.subscribeSender(parameterSender, dav.getDataModel()
						.getConfigurationAuthority(), datenBeschreibung,
						SenderRole.sender());
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException ex) {
					//
				}
			} catch (OneSubscriptionPerSendData e) {
				throw new RuntimeException(e);
			}
		}

		Data atgData = dav.createData(datenBeschreibung.getAttributeGroup());

		atgData.getItem("Urlasser").getReferenceValue("BenutzerReferenz")
				.setSystemObject(null);
		atgData.getItem("Urlasser").getTextValue("Ursache").setText("");
		atgData.getItem("Urlasser").getTextValue("Veranlasser").setText("");

		atgData.getArray("ParameterSatz").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getReferenceArray(
				"Bereich").setLength(0);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"DatenSpezifikation").setLength(1);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"DatenSpezifikation").getItem(0).getReferenceArray("Objekt")
				.setLength(0);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"DatenSpezifikation").getItem(0).getReferenceArray(
				"AttributGruppe").setLength(0);
		atgData.getArray("ParameterSatz").getItem(0).getArray(
				"DatenSpezifikation").getItem(0).getUnscaledValue(
				"SimulationsVariante").set(0);
		atgData.getArray("ParameterSatz").getItem(0).getItem("Einstellungen")
				.getUnscaledValue("Parametrieren").set(DUAKonstanten.JA);

		ResultData resultat = new ResultData(dav.getDataModel()
				.getConfigurationAuthority(), datenBeschreibung, System
				.currentTimeMillis(), atgData);

		try {
			dav.sendData(resultat);
		} catch (DataNotSubscribedException e) {
			throw new RuntimeException(e);
		} catch (SendSubscriptionNotConfirmed e) {
			throw new RuntimeException(e);
		}

		try {
			Thread.sleep(5000L);
		} catch (InterruptedException ex) {
			//
		}
	}

	/**
	 * Sendet eine Betriebsmeldung.
	 * 
	 * @param dav
	 *            Verbindung zum Datenverteiler.
	 * @param grade
	 *            die Art der Meldung (<code>FATAL</code>,
	 *            <code>ERROR</code>, <code>WARNING</code>,
	 *            <code>INFORMATION</code>).
	 * @param objekt
	 *            Referenziertes Systemobjekt.
	 * @param nachricht
	 *            die Betriebsmeldung.
	 */
	public static void sendeBetriebsmeldung(ClientDavInterface dav,
			MessageGrade grade, SystemObject objekt, String nachricht) {
		MessageSender.getInstance().sendMessage(
				MessageType.APPLICATION_DOMAIN,
				null,
				grade,
				objekt,
				new MessageCauser(dav.getLocalUser(), Constants.EMPTY_STRING,
						Constants.EMPTY_STRING), nachricht);
	}

	/**
	 * Extrahiert aus einer Zeichenkette alle über Kommata getrennten
	 * Konfigurationsbereiche und gibt deren Systemobjekte zurück.
	 * @param dav
	 * 			  Verbindung zum Datenverteiler
	 * @param kbString
	 *            Zeichenkette mit den Konfigurationsbereichen
	 * @return (ggf. leere) <code>ConfigurationArea-Collection</code> mit
	 *         allen extrahierten Konfigurationsbereichen.
	 */
	public static Collection<ConfigurationArea> getKonfigurationsBereicheAlsObjekte(ClientDavInterface dav,
			final String kbString) {
		List<String> resultListe = new ArrayList<String>();

		if (kbString != null) {
			String[] s = kbString.split(","); //$NON-NLS-1$
			for (String dummy : s) {
				if (dummy != null && dummy.length() > 0) {
					resultListe.add(dummy);
				}
			}
		}
		Collection<ConfigurationArea> kbListe = new HashSet<ConfigurationArea>();

		for (String kb : resultListe) {
			ConfigurationArea area = dav.getDataModel()
					.getConfigurationArea(kb);
			if (area != null) {
				kbListe.add(area);
			}
		}

		return kbListe;
	}
	
}
