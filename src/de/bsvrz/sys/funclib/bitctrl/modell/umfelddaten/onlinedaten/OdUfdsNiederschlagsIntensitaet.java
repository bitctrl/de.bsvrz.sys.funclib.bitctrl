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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.onlinedaten;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.Data.NumberValue;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UfdsNiederschlagsIntensitaet;

/**
 * Kapselt die Attriburgruppe {@code atg.ufdsNiederschlagsIntensit&auml;t}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdUfdsNiederschlagsIntensitaet extends AbstractOnlineDatensatz {

	/** Die PID der Attributgruppe. */
	public static final String ATG_UFDS_NIEDERSCHLAGS_INTENSITAET = "atg.ufdsNiederschlagsIntensitaet";

	/** Die PID des Aspekts. */
	public static final String ASP_MESS_WERT_ERSETZUNG = "asp.messWertErsetzung";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspMessWertErsetzung;

	/** Niederschlagsintensit&auml;t in mm/h. */
	private Integer niederschlagsIntensitaet;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param sensor
	 *            der Umfelddatensensor dessen Daten hier betrachtet werden.
	 */
	public OdUfdsNiederschlagsIntensitaet(UfdsNiederschlagsIntensitaet sensor) {
		super(sensor);

		if (atg == null || aspMessWertErsetzung == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_UFDS_NIEDERSCHLAGS_INTENSITAET);
			aspMessWertErsetzung = modell.getAspect(ASP_MESS_WERT_ERSETZUNG);
			assert atg != null && aspMessWertErsetzung != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public AttributeGroup getAttributGruppe() {
		return atg;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getEmpfangsAspekt() {
		return aspMessWertErsetzung;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code NiederschlagsIntensitaet} wieder.
	 * 
	 * @return {@code NiederschlagsIntensitaet}.
	 */
	public Integer getNiederschlagsIntensitaet() {
		return niederschlagsIntensitaet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getSendeAspekt() {
		return aspMessWertErsetzung;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(Data daten) {
		NumberValue wert;

		wert = daten.getItem("NiederschlagsIntensitaet").getUnscaledValue(
				"Wert");
		if (wert.isState()) {
			niederschlagsIntensitaet = null;
		} else {
			niederschlagsIntensitaet = wert.intValue();
		}
	}

}
