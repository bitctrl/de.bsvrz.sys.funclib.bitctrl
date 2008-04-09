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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashSet;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.MessQuerschnittTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnittAllgemein</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id$
 */
public abstract class MessQuerschnittAllgemein extends AbstractSystemObjekt {

	/**
	 * Debug-Logger.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * Menge aller allgemeinen Messquerschnitte.
	 */
	private static Collection<MessQuerschnittAllgemein> mqaMenge = null;

	/**
	 * Systemobjekt des Ersatzmessquerschnitts f�r die Messwertersetzung.
	 */
	private SystemObject ersatzQuerschnittObj = null;

	/**
	 * Typ eines MessQuerschnitts (HauptFahrbahn, NebenFahrbahn, ...).
	 */
	private MessQuerschnittTyp typ = null;

	/**
	 * Standardkonstruktor.
	 * 
	 * @param dav
	 *            Datenverteiler-Verbindung
	 * @param mqaObjekt
	 *            Systemobjekt eines allgemeinen Messquerschnittes
	 */
	protected MessQuerschnittAllgemein(final ClientDavInterface dav,
			final SystemObject mqaObjekt) {
		super(mqaObjekt);

		if (mqaObjekt == null) {
			throw new NullPointerException(
					"�bergebenes allgemeines Messquerschnittobjekt ist <<null>>"); //$NON-NLS-1$
		}

		AttributeGroup atgEigenschaften = dav.getDataModel().getAttributeGroup(
				DUAKonstanten.ATG_MQ_ALLGEMEIN);
		Data eigenschaften = mqaObjekt.getConfigurationData(atgEigenschaften);

		if (eigenschaften == null) {
			LOGGER
					.warning("Eigenschaften von allgemeinem Messquerschnittobjekt " + mqaObjekt + //$NON-NLS-1$
							" konnten nicht ausgelesen werden"); //$NON-NLS-1$
		} else {
			this.typ = MessQuerschnittTyp.getZustand(eigenschaften
					.getUnscaledValue("Typ").intValue()); //$NON-NLS-1$
			if (eigenschaften.getReferenceValue("ErsatzMessQuerschnitt") != null) { //$NON-NLS-1$
				this.ersatzQuerschnittObj = eigenschaften.getReferenceValue(
						"ErsatzMessQuerschnitt").getSystemObject(); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 * 
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessQuerschnittAllgemein> getAlleInstanzen() {
		if (mqaMenge == null) {
			mqaMenge = new HashSet<MessQuerschnittAllgemein>();
			mqaMenge.addAll(MessQuerschnitt.getInstanzen());
			mqaMenge.addAll(MessQuerschnittVirtuell.getInstanzen());
		}

		return mqaMenge;
	}

	/**
	 * Erfragt eine mit dem �bergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 * 
	 * @param mqaObjekt
	 *            ein MessQuerschnittAllgemein-Systemobjekt
	 * @return eine mit dem �bergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese
	 *         Instanz nicht ermittelt werden konnte
	 */
	public static MessQuerschnittAllgemein getInstanz(
			final SystemObject mqaObjekt) {
		MessQuerschnittAllgemein ergebnis = null;

		ergebnis = MessQuerschnitt.getInstanz(mqaObjekt);
		if (ergebnis == null) {
			ergebnis = MessQuerschnittVirtuell.getInstanz(mqaObjekt);
		}

		return ergebnis;
	}

	/**
	 * Erfragt die Menge der mittelbar oder unmittelbar an diesem Querschnitt
	 * definierten Fahrstreifen.
	 * 
	 * @return die Menge der mittelbar oder unmittelbar an diesem Querschnitt
	 *         definierten Fahrstreifen oder <code>null</code>, wenn hier
	 *         keine Fahrstreifen definiert sind
	 */
	public abstract Collection<FahrStreifen> getFahrStreifen();

	/**
	 * Erfragt den Ersatzquerschnitt dieses allgemeinen Messquerschnittes.
	 * 
	 * @return den Ersatzquerschnitt dieses allgemeinen Messquerschnittes
	 */
	public final MessQuerschnittAllgemein getErsatzMessquerSchnitt() {
		return getInstanz(this.ersatzQuerschnittObj);
	}

	/**
	 * Erfragt den Typ dieses Messquerschnittes.
	 * 
	 * @return der Typ dieses Messquerschnittes
	 */
	public final MessQuerschnittTyp getMQTyp() {
		return this.typ;
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnittAllgemein.class;
			}

			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}
}
