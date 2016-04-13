/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * Copyright 2016 by Kappich Systemberatung Aachen
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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.dua.lve.daten;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;
import de.bsvrz.sys.funclib.bitctrl.dua.DUAKonstanten;
import de.bsvrz.sys.funclib.bitctrl.dua.lve.daten.AtgMessQuerschnittVirtuellVLage.AtlMessQuerSchnittBestandTeil;
import de.bsvrz.sys.funclib.debug.Debug;

import java.util.ArrayList;
import java.util.List;

/**
 * Korrespondiert mit der Attributgruppe
 * <code>atg.messQuerschnittVirtuell</code>.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 * 
 * @version $Id: AtgMessQuerschnittVirtuell.java 20781 2009-12-14 08:27:26Z tfelder $
 */
public class AtgMessQuerschnittVirtuell implements MessQuerschnittAnteile {

	/**
	 * Liste der Messquerschnitte mit Angabe der Berechnungsvorschrift, wie aus
	 * diesen Messquerschnittswerten die Werte des virtuellen Messquerschnitts
	 * ermittelt werden sollen.
	 */
	private AtlMessQuerSchnittBestandTeil[] messQuerSchnittBestandTeile = null;
	
	/**
	 * Standardkonstruktor.
	 * 
	 * @param dav
	 *            Verbindung zum Datenverteiler.
	 * @param objekt
	 *            der virtuelle Messquerschnitt, dessen Konfigurationsdaten
	 *            ausgelesen werden sollen.
	 * @throws KeineDatenException
	 *             Wird geworfen, wenn die konfigurierende Attributgruppe nicht
	 *             definiert ist.
	 */
	public AtgMessQuerschnittVirtuell(ClientDavInterface dav,
			SystemObject objekt) throws KeineDatenException {
		Data atgData = objekt.getConfigurationData(dav.getDataModel()
				.getAttributeGroup(DUAKonstanten.ATG_MQ_VIRTUELL));
		if (atgData == null) {
			throw new KeineDatenException(
					"Die Attributgruppe \"atg.messQuerschnittVirtuell\" von VMQ "
							+ objekt + " ist nicht definiert.");
		}
		
		List<AtlMessQuerSchnittBestandTeil> dummy = new ArrayList<AtlMessQuerSchnittBestandTeil>();
		for (int i = 0; i < atgData.getArray("MessQuerSchnittBestandTeile")
				.getLength(); i++) {
			try {
				AtlMessQuerSchnittBestandTeil anteil = new AtlMessQuerSchnittBestandTeil(
						atgData.getArray("MessQuerSchnittBestandTeile")
								.getItem(i));
				dummy.add(anteil);
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
				Debug.getLogger().warning(
						"Problem beim Auslesen von Konfigurationsdaten von VMQ "
								+ objekt + ":\n" + e.getMessage());
			}
		}

		this.messQuerSchnittBestandTeile = dummy
				.toArray(new AtlMessQuerSchnittBestandTeil[0]);
	}

	/**
	 * Erfragt Liste der Messquerschnitte mit Angabe der Berechnungsvorschrift,
	 * wie aus diesen Messquerschnittswerten die Werte des virtuellen
	 * Messquerschnitts ermittelt werden sollen.
	 * 
	 * @return (ggf. leere) Liste der Messquerschnitte mit Angabe der
	 *         Berechnungsvorschrift.
	 */
	public AtlMessQuerSchnittBestandTeil[] getMessQuerSchnittBestandTeile() {
		return this.messQuerSchnittBestandTeile;
	}

	@Override
	public SystemObject getMessQuerschnittGeschwindigkeit() {
		return null;
	}

}
