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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.ArrayList;
import java.util.Collection;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.DataCache;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr�senation einer Stra�e in der Modellabbildung.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class Strasse extends AbstractSystemObjekt {

	/**
	 * die Nummer der Stra�e.
	 */
	private final long nummer;

	/**
	 * der Typ der Stra�e.
	 */
	private final StrassenTyp strassenTyp;

	/**
	 * Zusatzbezeichnung der Stra�e.
	 */
	private final String zusatz;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt eine Instanz einer Stra�e auf Basis des �bergebenen
	 * Systemobjekts.
	 * 
	 * @param obj
	 *            das Systemobjekt
	 */
	public Strasse(SystemObject obj) {
		super(obj);
		if (!obj.isOfType(getTyp().getPid())) {
			throw new IllegalArgumentException("Systemobjekt ist keine Stra�e.");
		}

		DataModel modell = objekt.getDataModel();
		AttributeGroup atg = modell.getAttributeGroup("atg.stra�e");
		DataCache.cacheData(getSystemObject().getType(), atg);
		Data daten = obj.getConfigurationData(atg);
		if (daten != null) {
			strassenTyp = StrassenTyp.getTyp(daten.getUnscaledValue("Typ")
					.intValue());
			nummer = daten.getUnscaledValue("Nummer").longValue();
			zusatz = daten.getTextValue("Zusatz").getText();
		} else {
			strassenTyp = null;
			nummer = Long.MAX_VALUE;
			zusatz = null;
		}
	}

	/**
	 * liefert die Liste der �u�eren Stra�ensegmente, die zu dieser Stra�e
	 * geh�ren.
	 * 
	 * @return die Liste der ermittelten Stra�ensegmente
	 */
	public Collection<AeusseresStrassenSegment> getAuessereStrassensegmente() {
		Collection<AeusseresStrassenSegment> result = new ArrayList<AeusseresStrassenSegment>();
		for (AeusseresStrassenSegment segment : AeusseresStrassenSegment
				.getSegmentListe(getSystemObject().getDataModel())) {
			if (this.equals(segment.getStrasse())) {
				result.add(segment);
			}
		}
		return result;
	}

	/**
	 * liefert die Stra�ennummer.
	 * 
	 * @return die Nummer
	 */
	public long getNummer() {
		return nummer;
	}

	/**
	 * liefert den Typ der Stra�e.
	 * 
	 * @return den Typ
	 */
	public StrassenTyp getStrassenTyp() {
		return strassenTyp;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt#getTyp()
	 */
	public SystemObjektTyp getTyp() {
		return VerkehrsModellTypen.STRASSE;
	}

	/**
	 * liefert die Zusatzbezeichnung der Stra�e.
	 * 
	 * @return de Bezeichnung
	 */
	public String getZusatz() {
		return zusatz;
	}
}
