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

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messStellenGruppe</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class MessStellenGruppe extends AbstractSystemObjekt {

	/**
	 * Mapt alle MessStelleGruppe-Systemobjekte auf Objekte dieser Klasse.
	 */
	protected static Map<SystemObject, MessStellenGruppe> sysObjMsgObjMap = new HashMap<SystemObject, MessStellenGruppe>();

	/**
	 * Datenverteiler-Verbindung..
	 */
	protected static ClientDavInterface sDav;

	/**
	 * Messstellen dieser Gruppe (sortiert wie in Konfiguration).
	 */
	private MessStelle[] messStellen = new MessStelle[0];

	/**
	 * Legt fest, ob die Ermittlung systematischer Detektorfehler f�r diese
	 * MessStellenGruppe durchgef�hrt werden soll.
	 */
	private boolean systematischeDetektorfehler;

	/**
	 * Standardkontruktor .
	 *
	 * @param msgObjekt
	 *            ein Systemobjekt vom Typ <code>typ.messStellenGruppe</code>
	 * @throws DUAInitialisierungsException
	 *             wenn die Messstellengruppe nicht initialisiert werden konnte
	 */
	@SuppressWarnings("unused")
	protected MessStellenGruppe(final SystemObject msgObjekt)
			throws DUAInitialisierungsException {
		super(msgObjekt);

		final ConfigurationObject konfigObjekt = (ConfigurationObject) msgObjekt;

		final AttributeGroup atgEigenschaften = sDav.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_MESS_STELLEN_GRUPPE);
		final Data eigenschaften = msgObjekt
				.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			Debug.getLogger()
			.warning("\"atg.messStellenGruppe\" von Messstellengruppe " //$NON-NLS-1$
					+ msgObjekt + " konnten nicht ausgelesen werden");
		} else {
			if (eigenschaften.getReferenceArray("MessStellen") != null) { //$NON-NLS-1$
				final MessStelle[] dummy = new MessStelle[eigenschaften
				                                          .getReferenceArray("MessStellen").getLength()]; //$NON-NLS-1$
				int c = 0;
				for (int i = 0; i < eigenschaften
						.getReferenceArray("MessStellen").getLength(); i++) { //$NON-NLS-1$
					if ((eigenschaften.getReferenceArray("MessStellen") //$NON-NLS-1$
							.getReferenceValue(i) != null)
							&& (eigenschaften.getReferenceArray("MessStellen") //$NON-NLS-1$
									.getReferenceValue(i)
									.getSystemObject() != null)) {
						dummy[c++] = MessStelle.getInstanz(eigenschaften
								.getReferenceArray("MessStellen")
								.getReferenceValue(i).getSystemObject());
					}
				}

				if (c > 0) {
					messStellen = new MessStelle[c];
					for (int i = 0; i < c; i++) {
						messStellen[i] = dummy[i];
					}
				}
			}

			systematischeDetektorfehler = eigenschaften
					.getUnscaledValue("SystematischeDetektorfehler") //$NON-NLS-1$
					.intValue() == DUAKonstanten.JA;
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessStellenGruppe> getInstanzen() {
		return sysObjMsgObjMap.values();
	}

	/**
	 * Initialisiert diese Klasse, indem f�r alle Systemobjekte vom Typ
	 * <code>typ.messStelleGruppe</code> statische Instanzen dieser Klasse
	 * angelegt werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException
	 *             wenn eines der Objekte nicht initialisiert werden konnte
	 */
	protected static void initialisiere(final ClientDavInterface dav1)
			throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException(
					"Datenverteiler-Verbindung ist <<null>>"); //$NON-NLS-1$
		}

		if (sDav != null) {
			throw new RuntimeException(
					"Objekt darf nur einmal initialisiert werden"); //$NON-NLS-1$
		}
		sDav = dav1;

		for (final SystemObject msObj : sDav.getDataModel()
				.getType(DUAKonstanten.TYP_MESS_STELLEN_GRUPPE).getElements()) {
			if (msObj.isValid()) {
				sysObjMsgObjMap.put(msObj, new MessStellenGruppe(msObj));
			}
		}
	}

	/**
	 * Initialisiert diese Klasse, indem f�r alle Systemobjekte vom Typ
	 * <code>typ.messStelleGruppe</code> statische Instanzen dieser Klasse
	 * angelegt werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 *
	 * @param kbs
	 *            Menge der zu betrachtenden Konfigurationsbereiche
	 * @throws DUAInitialisierungsException
	 *             wenn eines der Objekte nicht initialisiert werden konnte
	 */
	protected static void initialisiere(final ClientDavInterface dav1,
			final ConfigurationArea[] kbs) throws DUAInitialisierungsException {
		if (dav1 == null) {
			throw new NullPointerException(
					"Datenverteiler-Verbindung ist <<null>>"); //$NON-NLS-1$
		}

		if (sDav != null) {
			throw new RuntimeException(
					"Objekt darf nur einmal initialisiert werden"); //$NON-NLS-1$
		}
		sDav = dav1;

		for (final SystemObject msObj : sDav.getDataModel()
				.getType(DUAKonstanten.TYP_MESS_STELLEN_GRUPPE).getElements()) {
			if (msObj.isValid()
					&& DUAUtensilien.isObjektInKBsEnthalten(msObj, kbs)) {
				sysObjMsgObjMap.put(msObj, new MessStellenGruppe(msObj));
			}
		}
	}

	/**
	 * Erfragt eine mit dem �bergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param msgObjekt
	 *            ein MessStellenGruppe-Systemobjekt
	 * @return eine mit dem �bergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 */
	public static MessStellenGruppe getInstanz(final SystemObject msgObjekt) {
		if (sDav == null) {
			throw new RuntimeException(
					"MessStellen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		MessStellenGruppe ergebnis = null;

		if (msgObjekt != null) {
			ergebnis = sysObjMsgObjMap.get(msgObjekt);
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge der Messstellen dieser Messstellengruppe.
	 *
	 * @return ggf. leere Menge der Messstellen dieser Messstellengruppe
	 *         (sortiert wie in Konfiguration)
	 */
	public final MessStelle[] getMessStellen() {
		return messStellen;
	}

	/**
	 * Erfragt, ob die Ermittlung systematischer Detektorfehler f�r diese
	 * MessStellenGruppe durchgef�hrt werden soll.
	 *
	 * @return die Ermittlung systematischer Detektorfehler f�r diese
	 *         MessStellenGruppe durchgef�hrt werden soll
	 */
	public final boolean isSystematischeDetektorfehler() {
		return systematischeDetektorfehler;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessStellenGruppe.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}

}