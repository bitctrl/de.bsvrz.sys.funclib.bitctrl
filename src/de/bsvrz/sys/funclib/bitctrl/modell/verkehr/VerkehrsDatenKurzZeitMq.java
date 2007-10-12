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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;

/**
 * Kapselt die Attriburgruppe {@code atg.verkehrsDatenKurzZeitMq}.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class VerkehrsDatenKurzZeitMq extends AbstractOnlineDatensatz {

	/** Die PID der Attributgruppe. */
	public static final String ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ = "atg.verkehrsDatenKurzZeitMq";

	/** Die PID des Aspekts. */
	public static final String ASP_ANALYSE = "asp.analyse";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/** Der Aspekt kann von allen Instanzen gemeinsam genutzt werden. */
	private static Aspect aspAnalyse;

	/** Lkw-Anteil in Prozent. */
	private int aLkw;

	/** Bemessungsdichte KB in Fahrzeuge/km. */
	private int kb;

	/** Verkehrsst‰rke QKfz (alle Fahrzeuge) in Anzahl pro Messabschnittsdauer. */
	private int qKfz;

	/** Verkehrsst‰rke QPkw in Anzahl pro Messabschnittsdauer. */
	private int qPkw;

	/** Verkehrsst‰rke QLkw in Anzahl pro Messabschnittsdauer. */
	private int qLkw;

	/** Geschwindigkeit VKfz (Alle Fahrzeuge) in km/h. */
	private int vKfz;

	/** Geschwindigkeit VPkw in km/h. */
	private int vPkw;

	/** Geschwindigkeit VLkw in km/h. */
	private int vLkw;

	/** Standardabweichung der Kfz-Geschwindigkeiten SKfz in km/h. */
	private int sKfz;

	/**
	 * Ruft den Superkonstruktor auf.
	 * 
	 * @param mq
	 *            der Messquerschnitt dessen Kurzzeitdaten hier betrachtet
	 *            werden.
	 */
	public VerkehrsDatenKurzZeitMq(MessQuerschnittAllgemein mq) {
		super(mq);

		if (atg == null || aspAnalyse == null) {
			DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_VERKEHRS_DATEN_KURZ_ZEIT_MQ);
			aspAnalyse = modell.getAspect(ASP_ANALYSE);
		}
	}

	/**
	 * Gibt den Wert der Eigenschaft aLkw wieder.
	 * 
	 * @return the aLkw
	 */
	public int getALkw() {
		return aLkw;
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
		return aspAnalyse;
	}

	/**
	 * Gibt den Wert der Eigenschaft kb wieder.
	 * 
	 * @return the kb
	 */
	public int getKb() {
		return kb;
	}

	/**
	 * Gibt den Wert der Eigenschaft qKfz wieder.
	 * 
	 * @return the qKfz
	 */
	public int getQKfz() {
		return qKfz;
	}

	/**
	 * Gibt den Wert der Eigenschaft qLkw wieder.
	 * 
	 * @return the qLkw
	 */
	public int getQLkw() {
		return qLkw;
	}

	/**
	 * Gibt den Wert der Eigenschaft qPkw wieder.
	 * 
	 * @return the qPkw
	 */
	public int getQPkw() {
		return qPkw;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Aspect getSendeAspekt() {
		return aspAnalyse;
	}

	/**
	 * Gibt den Wert der Eigenschaft sKfz wieder.
	 * 
	 * @return the sKfz
	 */
	public int getSKfz() {
		return sKfz;
	}

	/**
	 * Gibt den Wert der Eigenschaft vKfz wieder.
	 * 
	 * @return the vKfz
	 */
	public int getVKfz() {
		return vKfz;
	}

	/**
	 * Gibt den Wert der Eigenschaft vLkw wieder.
	 * 
	 * @return the vLkw
	 */
	public int getVLkw() {
		return vLkw;
	}

	/**
	 * Gibt den Wert der Eigenschaft vPkw wieder.
	 * 
	 * @return the vPkw
	 */
	public int getVPkw() {
		return vPkw;
	}

	/**
	 * Wirft immmer eine {@code UnsupportedOperationException}.
	 * 
	 * {@inheritDoc}
	 */
	public void setDaten(Data daten) {
		throw new UnsupportedOperationException();
	}

}
