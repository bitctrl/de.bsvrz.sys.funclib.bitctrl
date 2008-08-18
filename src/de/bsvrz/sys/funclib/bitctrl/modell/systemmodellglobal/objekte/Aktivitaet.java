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

package de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.ConfigurationObject;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractSystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.SystemModellGlobalTypen;

/**
 * Repräsentiert eine Aktivität des Datenverteilers im Sinne der Nutzerrechte.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class Aktivitaet extends AbstractSystemObjekt {

	/** Flag ob der Lesezugriff gestattet ist. */
	private boolean lesen;

	/** Flag ob der Schreibzugriff gestattet ist. */
	private boolean schreiben;

	/** Flag ob die Anmeldung als Quelle und Senke erlaubt ist. */
	private boolean quelleSenke;

	/** Die Liste der betroffenen Aspekte. */
	private List<Aspect> aspekte;

	/** Die Liste der betroffenen Attributgruppen. */
	private List<Aspect> attributgruppen;

	/** Flag, ob die Konfigurationsdaten bereits gelesen wurden. */
	private boolean konfigDatenGelesen = false;

	/**
	 * Konstruktor zum Anlegen eines Systemobjekt, das ein Applikationsobjekt
	 * {@link SystemModellGlobalTypen#AKTIVITAET} in der
	 * Datenverteiler-Konfiguration repräsentiert.
	 * 
	 * @param obj
	 *            das Objekt in der Konfiguration des Datenverteilers
	 */
	public Aktivitaet(final SystemObject obj) {
		super(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public SystemObjektTyp getTyp() {
		return SystemModellGlobalTypen.Aktivitaet;
	}

	/**
	 * Gibt das Flag zurück, ob der Lesezugriff gestattet ist.
	 * 
	 * @return das Flag ob der Lesezugriff gestattet ist.
	 */
	public boolean isLesen() {
		leseKonfigEigenschaften();
		return lesen;
	}

	/**
	 * Gibt das Flag zurück, ob der Schreibzugriff gestattet ist.
	 * 
	 * @return das Flag ob der Schreibzugriff gestattet ist.
	 */
	public boolean isSchreiben() {
		leseKonfigEigenschaften();
		return schreiben;
	}

	/**
	 * Gibt das Flag zurück, ob die Anmeldung als Quelle und Senke erlaubt ist.
	 * 
	 * @return das Flag ob die Anmeldung als Quelle und Senke erlaubt ist.
	 */
	public boolean isQuelleSenke() {
		leseKonfigEigenschaften();
		return quelleSenke;
	}

	/**
	 * Gibt eine Read-Only-Liste der Aspekte zurück, auf die sich die Aktivität
	 * bezieht.
	 * 
	 * @return die Liste der betroffenen Aspekte.
	 */
	public List<Aspect> getAspekte() {
		if (aspekte == null) {
			aspekte = new ArrayList<Aspect>();
			final ConfigurationObject co = (ConfigurationObject) getSystemObject();
			for (final SystemObject so : co.getObjectSet("Aspekte")
					.getElements()) {
				aspekte.add((Aspect) so);
			}
		}

		return Collections.unmodifiableList(aspekte);
	}

	/**
	 * Gibt eine Read-Only-Liste der Attributgruppen zurück, auf die sich die
	 * Aktivität bezieht.
	 * 
	 * @return die Liste der betroffenen Aspekte.
	 */
	public List<Aspect> getAttributgruppen() {
		if (attributgruppen == null) {
			attributgruppen = new ArrayList<Aspect>();
			final ConfigurationObject co = (ConfigurationObject) getSystemObject();
			for (final SystemObject so : co.getObjectSet("Attributgruppen")
					.getElements()) {
				attributgruppen.add((Aspect) so);
			}
		}

		return Collections.unmodifiableList(attributgruppen);
	}

	/**
	 * Liest die konfigurierenden Daten des Objekts bei Bedarf.
	 */
	private void leseKonfigEigenschaften() {
		if (!konfigDatenGelesen) {
			return;
		}

		DataModel modell;
		AttributeGroup atg;
		Data datum;

		modell = objekt.getDataModel();
		atg = modell.getAttributeGroup("atg.benutzerEigenschaften");
		datum = objekt.getConfigurationData(atg);

		if (datum != null) {
			lesen = datum.getUnscaledValue("lesen").intValue() == 1;
			schreiben = datum.getUnscaledValue("schreiben").intValue() == 1;
			quelleSenke = datum.getUnscaledValue("quelleSenke").intValue() == 1;
		} else {
			lesen = false;
			schreiben = false;
			quelleSenke = false;
		}

		konfigDatenGelesen = true;
	}

}
