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
import de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten.UmfeldDatenMessStelle;

/**
 * Kapselt die Attriburgruppe {@code atg.ufdmsGl&auml;tte}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class OdUfdmsGlaette extends AbstractOnlineDatensatz {

	/** Die PID der Attributgruppe. */
	public static final String ATG_UFDMS_GLAETTE = "atg.ufdmsGl‰tte";

	/** Die PID des Aspekts. */
	public static final String ASP_PROGNOSE = "asp.prognose";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspPrognose;

	/** Zustandswert der Gl&auml;tte. */
	private Integer glaette;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param messstelle
	 *            die Messstelle dessen Daten hier betrachtet werden.
	 */
	public OdUfdmsGlaette(UmfeldDatenMessStelle messstelle) {
		super(messstelle);

		if (atg == null || aspPrognose == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_UFDMS_GLAETTE);
			aspPrognose = modell.getAspect(ASP_PROGNOSE);
			assert atg != null && aspPrognose != null;
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
		return aspPrognose;
	}

	/**
	 * Gibt den Wert der Eigenschaft {@code Glaette} wieder.
	 * 
	 * @return {@code Glaette}
	 */
	public Integer getGlaette() {
		return glaette;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getSendeAspekt() {
		return aspPrognose;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setDaten(Data daten) {
		NumberValue wert;

		wert = daten.getItem("Helligkeit").getUnscaledValue("Wert");
		if (wert.isState()) {
			glaette = null;
		} else {
			glaette = wert.intValue();
		}
	}

}
