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
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.lve;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationArea;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAInitialisierungsException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAUtensilien;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.AtgMessQuerschnittVirtuellVLage;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.KeineDatenException;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.typen.MessQuerschnittVirtuellLage;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Korrespondiert mit dem Systemobjekt <code>typ.messQuerschnittVirtuell</code>.
 *
 * @author BitCtrl Systems GmbH, Thierfelder
 */
public class MessQuerschnittVirtuell extends MessQuerschnittAllgemein {

	/**
	 * Indiziert die Vorschrift, nach der die virtuellen MQs berechnet werden
	 * sollen.
	 *
	 * @author BitCtrl Systems GmbH, Thierfelder
	 *
	 */
	public enum BerechnungsVorschrift {

		/**
		 * <b>DUMMY.</b><br>
		 * Steht fuer die Information, dass keine der beiden moeglichen
		 * Attributgruppen versorgt sind.
		 */
		UNBEKANNT,

		/**
		 * <b>NEUE VORSCHRIFT.</b><br>
		 * Definiert abstrakt eine Menge von (MQ, Anteil)-Tupeln, die
		 * beschreiben, wie sich die Werte des virtuellen MQ aus denen der
		 * einzelen-MQs anteilig errechnen.
		 */
		AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE,

		/**
		 * <b>ALTE VORSCHRIFT.</b><br>
		 * Definiert konkret Einfahrten und Ausfahrten sowie die Streckenstuecke
		 * <code>vor</code>, <code>mitte</code> und <code>nach</code> und
		 * errechnet anhand dieser Informationen nach Regeln aus Afo 4.0 die
		 * Werte des virtuellen MQ.
		 */
		AUF_BASIS_VON_ATG_MQ_VIRTUELL_STANDARD
	};

	/**
	 * Mapt alle MessQuerschnittVirtuell-Systemobjekte auf Objekte dieser
	 * Klasse.
	 */
	protected static Map<SystemObject, MessQuerschnittVirtuell> sysObjMqvObjMap = new HashMap<SystemObject, MessQuerschnittVirtuell>();

	/**
	 * Datenverteiler-Verbindung.
	 */
	protected static ClientDavInterface sDav;

	/**
	 * Die aktuelle Vorschrift, nach der die virtuellen MQs berechnet werden
	 * sollen.
	 */
	private BerechnungsVorschrift berechnungsVorschrift = BerechnungsVorschrift.UNBEKANNT;

	/**
	 * Messquerschnitt VOR der Anschlussstelle.
	 */
	private MessQuerschnitt mqVorObj;

	/**
	 * Messquerschnitt NACH der Anschlussstelle.
	 */
	private MessQuerschnitt mqNachObj;

	/**
	 * Messquerschnitt MITTE der Anschlussstelle.
	 */
	private MessQuerschnitt mqMitteObj;

	/**
	 * Messquerschnitt AUSFAHRT der Anschlussstelle.
	 */
	private MessQuerschnitt mqAusfahrtObj;

	/**
	 * Messquerschnitt EINFAHRT der Anschlussstelle.
	 */
	private MessQuerschnitt mqEinfahrtObj;

	/**
	 * die Lage des virtuellen MQs.
	 */
	private MessQuerschnittVirtuellLage mqVirtLage;

	/**
	 * die Bestandteile dieses VMQ.
	 */
	private AtgMessQuerschnittVirtuellVLage atgMessQuerschnittVirtuellVLage;

	/**
	 * Standardkontruktor.
	 *
	 * @param mqvObjekt
	 *            ein Systemobjekt vom Typ
	 *            <code>typ.messQuerschnittVirtuell</code>
	 * @throws DUAInitialisierungsException
	 *             wenn der virtuelle Messquerschnitt nicht initialisiert werden
	 *             konnte
	 */
	protected MessQuerschnittVirtuell(final SystemObject mqvObjekt)
			throws DUAInitialisierungsException {
		super(sDav, mqvObjekt);

		if (mqvObjekt == null) {
			throw new NullPointerException(
					"Uebergebenes MessQuerschnittVirtuell-Systemobjekt ist <<null>>"); //$NON-NLS-1$
		}

		final AttributeGroup atgEigenschaftenSTD = sDav.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_MQ_VIRTUELL_STANDARD);
		final Data eigenschaftenSTD = mqvObjekt
				.getConfigurationData(atgEigenschaftenSTD);

		if (eigenschaftenSTD == null) {
			Debug.getLogger().warning(
					"\"atg.messQuerschnittVirtuellStandard\" von MessQuerschnittVirtuell-Objekt " //$NON-NLS-1$
					+ mqvObjekt + " konnten nicht ausgelesen werden");
		} else {
			mqVirtLage = MessQuerschnittVirtuellLage.getZustand(
					eigenschaftenSTD.getUnscaledValue("Lage").intValue());
			if (eigenschaftenSTD
					.getReferenceValue("MessQuerschnittVor") != null) { //$NON-NLS-1$
				mqVorObj = MessQuerschnitt.getInstanz(
						eigenschaftenSTD.getReferenceValue("MessQuerschnittVor") //$NON-NLS-1$
						.getSystemObject());
			}
			if (eigenschaftenSTD
					.getReferenceValue("MessQuerschnittNach") != null) { //$NON-NLS-1$
				mqNachObj = MessQuerschnitt.getInstanz(eigenschaftenSTD
						.getReferenceValue("MessQuerschnittNach") //$NON-NLS-1$
						.getSystemObject());
			}
			if (eigenschaftenSTD
					.getReferenceValue("MessQuerschnittMitte") != null) { //$NON-NLS-1$
				mqMitteObj = MessQuerschnitt.getInstanz(eigenschaftenSTD
						.getReferenceValue("MessQuerschnittMitte") //$NON-NLS-1$
						.getSystemObject());
			}
			if (eigenschaftenSTD
					.getReferenceValue("MessQuerschnittAusfahrt") != null) { //$NON-NLS-1$
				mqAusfahrtObj = MessQuerschnitt.getInstanz(eigenschaftenSTD
						.getReferenceValue("MessQuerschnittAusfahrt") //$NON-NLS-1$
						.getSystemObject());
			}
			if (eigenschaftenSTD
					.getReferenceValue("MessQuerschnittEinfahrt") != null) { //$NON-NLS-1$
				mqEinfahrtObj = MessQuerschnitt.getInstanz(eigenschaftenSTD
						.getReferenceValue("MessQuerschnittEinfahrt") //$NON-NLS-1$
						.getSystemObject());
			}
			berechnungsVorschrift = BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_STANDARD;
		}

		try {
			atgMessQuerschnittVirtuellVLage = new AtgMessQuerschnittVirtuellVLage(
					sDav, mqvObjekt);
			berechnungsVorschrift = BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE;
		} catch (final KeineDatenException e) {
			Debug.getLogger().warning(
					"\"atg.messQuerschnittVirtuellVLage\" von MessQuerschnittVirtuell-Objekt "
							+ mqvObjekt + " konnten nicht ausgelesen werden:\n"
							+ e.getMessage());
		}

		if (berechnungsVorschrift == BerechnungsVorschrift.UNBEKANNT) {
			throw new DUAInitialisierungsException(
					"Die Berechnungsvorschrift fuer den VMQ " + mqvObjekt
					+ " kann nicht bestimmt werden, "
					+ "da keine der notwendigen Attributgruppen versorgt ist"
					+ " (\"atg.messQuerschnittVirtuellVLage\" bzw. \"atg.messQuerschnittVirtuellStandard\").");
		}

	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.messQuerschnittVirtuell</code> statische Instanzen dieser
	 * Klasse angelegt werden.
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

		for (final SystemObject mqvObjekt : sDav.getDataModel()
				.getType(DUAKonstanten.TYP_MQ_VIRTUELL).getElements()) {
			if (mqvObjekt.isValid()) {
				sysObjMqvObjMap.put(mqvObjekt,
						new MessQuerschnittVirtuell(mqvObjekt));
			}
		}
	}

	/**
	 * Initialisiert diese Klasse, indem für alle Systemobjekte vom Typ
	 * <code>typ.messQuerschnittVirtuell</code> statische Instanzen dieser
	 * Klasse angelegt werden.
	 *
	 * @param dav1
	 *            Datenverteiler-Verbindung
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

		for (final SystemObject mqvObjekt : sDav.getDataModel()
				.getType(DUAKonstanten.TYP_MQ_VIRTUELL).getElements()) {
			if (mqvObjekt.isValid()
					&& DUAUtensilien.isObjektInKBsEnthalten(mqvObjekt, kbs)) {
				sysObjMqvObjMap.put(mqvObjekt,
						new MessQuerschnittVirtuell(mqvObjekt));
			}
		}
	}

	/**
	 * Erfragt alle statischen Instanzen dieser Klasse.
	 *
	 * @return alle statischen Instanzen dieser Klasse
	 */
	public static Collection<MessQuerschnittVirtuell> getInstanzen() {
		if (sDav == null) {
			throw new RuntimeException(
					"MessQuerschnittVirtuell-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		return sysObjMqvObjMap.values();
	}

	/**
	 * Erfragt eine mit dem übergebenen Systemobjekt assoziierte statische
	 * Instanz dieser Klasse.
	 *
	 * @param mqvObjekt
	 *            ein MessQuerschnittVirtuell-Systemobjekt
	 * @return eine mit dem übergebenen Systemobjekt assoziierte statische
	 *         Instanz dieser Klasse oder <code>null</code>, wenn diese Instanz
	 *         nicht ermittelt werden konnte
	 */
	public static MessQuerschnittVirtuell getInstanz(
			final SystemObject mqvObjekt) {
		if (sDav == null) {
			throw new RuntimeException(
					"MessQuerschnittVirtuell-Klasse wurde noch nicht initialisiert"); //$NON-NLS-1$
		}
		MessQuerschnittVirtuell ergebnis = null;

		if (mqvObjekt != null) {
			ergebnis = sysObjMqvObjMap.get(mqvObjekt);
		}

		return ergebnis;
	}

	@Override
	public Collection<FahrStreifen> getFahrStreifen() {
		final Collection<FahrStreifen> fahrStreifenMenge = new HashSet<FahrStreifen>();

		if ((getBerechnungsVorschrift() == null)
				|| (getBerechnungsVorschrift() == BerechnungsVorschrift.AUF_BASIS_VON_ATG_MQ_VIRTUELL_V_LAGE)) {
			for (final AtgMessQuerschnittVirtuellVLage.AtlMessQuerSchnittBestandTeil mqBestandteil : getAtgMessQuerschnittVirtuellVLage()
					.getMessQuerSchnittBestandTeile()) {
				final MessQuerschnittAllgemein mqa = MessQuerschnittAllgemein
						.getInstanz(mqBestandteil.getMQReferenz());
				if (mqa != null) {
					fahrStreifenMenge.addAll(mqa.getFahrStreifen());
				}
			}
		} else {
			if (getMQVor() != null) {
				fahrStreifenMenge.addAll(getMQVor().getFahrStreifen());
			}
			if (getMQNach() != null) {
				fahrStreifenMenge.addAll(getMQNach().getFahrStreifen());
			}
			if (getMQEinfahrt() != null) {
				fahrStreifenMenge.addAll(getMQEinfahrt().getFahrStreifen());
			}
			if (getMQAusfahrt() != null) {
				fahrStreifenMenge.addAll(getMQAusfahrt().getFahrStreifen());
			}
			if (getMQMitte() != null) {
				fahrStreifenMenge.addAll(getMQMitte().getFahrStreifen());
			}
		}

		return fahrStreifenMenge;
	}

	/**
	 * Erfragt Messquerschnitt VOR der Anschlussstelle.
	 *
	 * @return Messquerschnitt VOR der Anschlussstelle
	 */
	public final MessQuerschnitt getMQVor() {
		return mqVorObj;
	}

	/**
	 * Erfragt Messquerschnitt NACH der Anschlussstelle.
	 *
	 * @return Messquerschnitt NACH der Anschlussstelle
	 */
	public final MessQuerschnitt getMQNach() {
		return mqNachObj;
	}

	/**
	 * Erfragt Messquerschnitt MITTE der Anschlussstelle.
	 *
	 * @return Messquerschnitt MITTE der Anschlussstelle
	 */
	public final MessQuerschnitt getMQMitte() {
		return mqMitteObj;
	}

	/**
	 * Erfragt Messquerschnitt AUSFAHRT der Anschlussstelle.
	 *
	 * @return Messquerschnitt AUSFAHRT der Anschlussstelle
	 */
	public final MessQuerschnitt getMQAusfahrt() {
		return mqAusfahrtObj;
	}

	/**
	 * Erfragt Messquerschnitt EINFAHRT der Anschlussstelle.
	 *
	 * @return Messquerschnitt EINFAHRT der Anschlussstelle
	 */
	public final MessQuerschnitt getMQEinfahrt() {
		return mqEinfahrtObj;
	}

	/**
	 * Erfragt die Lage dieses virtuellen Messquerschnitts.
	 *
	 * @return die Lage dieses virtuellen Messquerschnitts.
	 */
	public final MessQuerschnittVirtuellLage getMQVirtuellLage() {
		return mqVirtLage;
	}

	/**
	 * Erfragt die aktuelle Vorschrift, nach der die virtuellen MQs berechnet
	 * werden sollen. Die Berechnungsvorschrift ergibt sich aus der Versorgung
	 * der beiden Attributgruppen:<br>
	 * - <code>atg.messQuerschnittVirtuellVLage</code> und<br>
	 * - <code>atg.messQuerschnittVirtuellStandard</code>.<br>
	 * Sind beide versorgt, so wird die Vorschrift auf Basis von
	 * <code>atg.messQuerschnittVirtuellVLage</code> bevorzugt.
	 *
	 * @return die aktuelle Vorschrift, nach der die virtuellen MQs berechnet
	 *         werden sollen.
	 */
	public final BerechnungsVorschrift getBerechnungsVorschrift() {
		return berechnungsVorschrift;
	}

	/**
	 * Erfragt die Konfigurationsinformationen aus der Attributgruppe
	 * <code>atg.messQuerschnittVirtuellVLage</code>.
	 *
	 * @return die Konfigurationsinformationen aus der Attributgruppe
	 *         <code>atg.messQuerschnittVirtuellVLage</code>.
	 */
	public final AtgMessQuerschnittVirtuellVLage getAtgMessQuerschnittVirtuellVLage() {
		return atgMessQuerschnittVirtuellVLage;
	}

	@Override
	public SystemObjektTyp getTyp() {
		return new SystemObjektTyp() {

			@Override
			public Class<? extends SystemObjekt> getKlasse() {
				return MessQuerschnittVirtuell.class;
			}

			@Override
			public String getPid() {
				return getSystemObject().getType().getPid();
			}

		};
	}

}
