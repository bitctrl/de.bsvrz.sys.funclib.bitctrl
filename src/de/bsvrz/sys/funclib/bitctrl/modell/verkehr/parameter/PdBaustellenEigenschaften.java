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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.BaustellenStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.BaustellenVeranlasser;

/**
 * Ein Parameterdatensatz, die Eigenschaften einer Baustelle beinhaltet. Der
 * Datensatz repräsentiert die Daten einer Attributgruppe
 * "atg.baustellenEigenschaften".
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class PdBaustellenEigenschaften extends AbstractParameterDatensatz {

	/**
	 * die Attributgruppe, in der die Eigenschaften enthaöten sind.
	 */
	private static AttributeGroup attributGruppe;

	/**
	 * Zustand der Baustelle. ("Status")
	 */
	private BaustellenStatus status = BaustellenStatus.ENTWORFEN;

	/**
	 * Restkapazität während der Gültigkeitsdauer der Baustelle.
	 * ("RestKapazität")
	 */
	private long restKapazitaet;

	/**
	 * Veranlasser der Baustelle (BIS-System oder VRZ). ("Veranlasser")
	 */
	private BaustellenVeranlasser veranlasser = BaustellenVeranlasser.UNDEFINIERT;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeigt eine Instanz eines
	 * Baustellen-Eigenschaften-Parameterdatensatzes.
	 * 
	 * @param objekt
	 *            das der Baustelle zugrundliegende Systemobjekt.
	 */
	public PdBaustellenEigenschaften(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.baustellenEigenschaften");
		}
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#abmeldenSender()
	 */
	@Override
	public void abmeldenSender() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#getAttributGruppe()
	 */
	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
	}

	/**
	 * liefert die Restkapazität der Baustelle.
	 * 
	 * @return die Restkapazität
	 */
	public long getRestKapazitaet() {
		return restKapazitaet;
	}

	/**
	 * liefert den Status der Baustelle.
	 * 
	 * @return der Status
	 */
	public BaustellenStatus getStatus() {
		return status;
	}

	/**
	 * liefert den Veranlassser der Baustelle.
	 * 
	 * @return den Veranlasser
	 */
	public BaustellenVeranlasser getVeranlasser() {
		return veranlasser;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#sendeDaten()
	 */
	@Override
	public void sendeDaten() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.Datensatz#setDaten(de.bsvrz.dav.daf.main.Data)
	 */
	public void setDaten(Data daten) {
		if (daten != null) {
			status = BaustellenStatus.getStatus(daten
					.getUnscaledValue("Status").intValue());
			restKapazitaet = daten.getScaledValue("RestKapazität").longValue();
			veranlasser = BaustellenVeranlasser.getVeranlasser(daten
					.getUnscaledValue("Veranlasser").intValue());
		}
	}
}
