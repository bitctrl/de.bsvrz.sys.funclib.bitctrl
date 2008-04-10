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

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.FahrStreifenLage;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.fahrStreifen</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public class FahrStreifen extends AbstractSystemObjekt {

	/**
	 * Mapt alle Fahrstreifen-Systemobjekte auf Objekte der Klasse
	 * <code>FahrStreifen</code>.
	 */
	protected static Map<SystemObject, FahrStreifen> sysObjFsObjMap = new HashMap<SystemObject, FahrStreifen>();

	/**
	 * Datenverteiler-Verbindung.
	 */
	protected static ClientDavInterface sDav = null;

	/**
	 * die Lage dieses Fahrtreifens.
	 */
	private FahrStreifenLage lage = null;

	/**
	 * Systemobjekt des Ersatzfahrstreifens dieses Fahrstreifens.
	 */
	private SystemObject ersatzFahrstreifenObj = null;

	/**
	 * Systemobjekt des Nachbarfahrstreifens dieses Fahrstreifens.
	 */
	private SystemObject nachbarFahrstreifenObj = null;

	/**
	 * Standardkontruktor.
	 * 
	 * @param fsObjekt
	 *            ein Systemobjekt vom Typ <code>typ.fahrStreifen</code>
	 * @throws DUAInitialisierungsException
	 *             wenn der Fahrstreifen nicht initialisiert werden konnte
	 */
	protected FahrStreifen(final SystemObject fsObjekt)
			throws DUAInitialisierungsException {
		super(fsObjekt);

		if (fsObjekt == null) {
			throw new NullPointerException(
					"Übergebenes Fahrstreifenobjekt ist <<null>>"); //$NON-NLS-1$
		}

		AttributeGroup atgEigenschaften = sDav.getDataModel().getAttributeGroup(
				DUAKonstanten.ATG_FAHRSTREIFEN);
		Data eigenschaften = fsObjekt.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			throw new DUAInitialisierungsException(
					"Eigenschaften von Fahrstreifenobjekt " + fsObjekt + //$NON-NLS-1$
							" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		}

		this.lage = FahrStreifenLage.getZustand(eigenschaften.getUnscaledValue(
				"Lage").intValue()); //$NON-NLS-1$
		if (eigenschaften.getReferenceValue("ErsatzFahrStreifen") != null) { //$NON-NLS-1$
			this.ersatzFahrstreifenObj = eigenschaften.getReferenceValue(
					"ErsatzFahrStreifen").getSystemObject(); //$NON-NLS-1$
		}
	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.fahrStreifen</code> statische Instanzen dieser Klasse
	 * angelegt werden.
	 * 
	 * @param dav1
	 *            Datenverteiler-Verbindung
	 * @throws DUAInitialisierungsException
	 *             wenn eines der Objekte nicht initialisiert werden konnte
	 */
	protected static final void initialisiere(final ClientDavInterface dav1)
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

		for (SystemObject fsObjekt : sDav.getDataModel().getType(
				DUAKonstanten.TYP_FAHRSTREIFEN).getElements()) {
			if (fsObjekt.isValid()) {
				sysObjFsObjMap.put(fsObjekt, new FahrStreifen(fsObjekt));
			}
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<FahrStreifen> getInstanzen() {
		if (sDav == null) {
			throw new RuntimeException(
					"FahrStreifen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		return sysObjFsObjMap.values();
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 * 
	 * @param fsObjekt
	 *            ein Fahrstreifen-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese
	 *         Instanz nicht ermittelt werden konnte
	 */
	public static final FahrStreifen getInstanz(final SystemObject fsObjekt) {
		if (sDav == null) {
			throw new RuntimeException(
					"Fahrstreifen-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		FahrStreifen ergebnis = null;

		if (fsObjekt != null) {
			ergebnis = sysObjFsObjMap.get(fsObjekt);
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Lage dieses Fahrtreifens innerhalb eines Messquerschnitts.
	 * 
	 * @return die Lage dieses Fahrtreifens innerhalb eines Messquerschnitts
	 */
	public FahrStreifenLage getLage() {
		return this.lage;
	}

	/**
	 * Erfragt den Ersatzfahrstreifen dieses Fahrstreifens.
	 * 
	 * @return den Ersatzfahrstreifen dieses Fahrstreifens oder
	 *         <code>null</code>, wenn dieser nicht ermittelt werden konnte
	 */
	public final FahrStreifen getErsatzFahrStreifen() {
		return FahrStreifen.getInstanz(this.ersatzFahrstreifenObj);
	}

	/**
	 * Setzt den Ersatzfahrstreifen dieses Fahrstreifens.
	 * 
	 * @param ersatzFahrstreifenObj1
	 *            den Ersatzfahrstreifen dieses Fahrstreifens
	 */
	protected final void setErsatzFahrStreifen(
			final SystemObject ersatzFahrstreifenObj1) {
		this.ersatzFahrstreifenObj = ersatzFahrstreifenObj1;
	}

	/**
	 * Erfragt den Nachbarfahrstreifen dieses Fahrstreifens.
	 * 
	 * @return den Nachbarfahrstreifen dieses Fahrstreifens oder
	 *         <code>null</code>, wenn dieser Fahrstreifen keinen
	 *         Nachbarfahrstreifen hat
	 */
	public final FahrStreifen getNachbarFahrStreifen() {
		return FahrStreifen.getInstanz(this.nachbarFahrstreifenObj);
	}

	/**
	 * Setzt den Nachbarfahrstreifen dieses Fahrstreifens.
	 * 
	 * @param nachbarFahrstreifenObj1
	 *            den Nachbarfahrstreifen dieses Fahrstreifens
	 */
	protected final void setNachbarFahrStreifen(
			final SystemObject nachbarFahrstreifenObj1) {
		this.nachbarFahrstreifenObj = nachbarFahrstreifenObj1;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			public Class<? extends SystemObjekt> getKlasse() {
				return FahrStreifen.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}

}
