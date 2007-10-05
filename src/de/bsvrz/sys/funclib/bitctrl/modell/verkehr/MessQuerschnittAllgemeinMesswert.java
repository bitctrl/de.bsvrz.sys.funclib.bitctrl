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

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractMesswertObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Repr&auml;sentiert einen Messquerschnitt und seine Messwerte. Modelliert
 * werden die Werte ALkw, KB, QKfz, SKfz und VKfz.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class MessQuerschnittAllgemeinMesswert extends MessQuerschnittAllgemein implements
		MesswertObjekt {

	/**
	 * Erweitert die Abstrakte Klasse um die notwendige Logik.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id: MessQuerschnittMesswert.java 4107 2007-10-05 10:11:54Z
	 *          Schumann $
	 */
	private class MQmitMesswert extends AbstractMesswertObjekt {

		/**
		 * Tut nichts.
		 */
		MQmitMesswert() {
			// Nur für Performance
		}

		/**
		 * {@inheritDoc}
		 */
		public DataDescription getDbs(Messwert mw) {
			DataModel modell;
			AttributeGroup atg;
			Aspect asp;

			modell = getSystemObject().getDataModel();
			atg = modell.getAttributeGroup(mw.getAtgPID());
			asp = modell.getAspect(mw.getAspPID());

			return new DataDescription(atg, asp);
		}

		/**
		 * {@inheritDoc}
		 */
		public long getId() {
			return MessQuerschnittAllgemeinMesswert.this.getId();
		}

		/**
		 * {@inheritDoc}
		 */
		public Collection<Messwert> getMesswerte() {
			Set<Messwert> messwerte = new HashSet<Messwert>(5);
			messwerte.add(ALKW);
			messwerte.add(KB);
			messwerte.add(QKFZ);
			messwerte.add(SKFZ);
			messwerte.add(VKFZ);
			return messwerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return MessQuerschnittAllgemeinMesswert.this.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getPid() {
			return MessQuerschnittAllgemeinMesswert.this.getPid();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObject getSystemObject() {
			return MessQuerschnittAllgemeinMesswert.this.getSystemObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObjektTyp getTyp() {
			return MessQuerschnittAllgemeinMesswert.this.getTyp();
		}

		/**
		 * {@inheritDoc}
		 */
		public Set<Messwert> setWerte(ResultData datensatz) {
			Set<Messwert> neueWerte;
			AttributeGroup atg;

			atg = datensatz.getDataDescription().getAttributeGroup();
			neueWerte = new HashSet<Messwert>();

			// Test auf eine ATG reicht, da alle Messwerte die selbe ATG
			// besitzen
			if (atg.getPid().equals(ALKW.getAtgPID())) {
				setZeitstempel(datensatz.getDataTime());

				setWert(ALKW, getWert(datensatz, ALKW));
				neueWerte.add(ALKW);

				setWert(KB, getWert(datensatz, KB));
				neueWerte.add(KB);

				setWert(QKFZ, getWert(datensatz, QKFZ));
				neueWerte.add(QKFZ);

				setWert(SKFZ, getWert(datensatz, SKFZ));
				neueWerte.add(SKFZ);

				setWert(VKFZ, getWert(datensatz, VKFZ));
				neueWerte.add(VKFZ);
			} else {
				throw new IllegalArgumentException(
						"Datensatz ist kein Analysewert für einen Messquerschnitt.");
			}

			return neueWerte;
		}

	}

	/** Messwert der den Lkw-Anteil beschreibt. */
	public static final Messwert ALKW = new Messwert("ALkw", "Lkw-Anteil",
			"atg.verkehrsDatenKurzZeitMq", "asp.analyse");

	/** Messwert der die Bemessungsdichte beschreibt. */
	public static final Messwert KB = new Messwert("KB", "Bemessungsdichte",
			"atg.verkehrsDatenKurzZeitMq", "asp.analyse");

	/** Messwert der die Verkehrsst&auml;rke beschreibt. */
	public static final Messwert QKFZ = new Messwert("QKfz", "Verkehrsstärke",
			"atg.verkehrsDatenKurzZeitMq", "asp.analyse");

	/** Messwert der die Standardabweichung der Geschwindigkeit beschreibt. */
	public static final Messwert SKFZ = new Messwert("SKfz",
			"Standardabweichung der Geschwindigkeit",
			"atg.verkehrsDatenKurzZeitMq", "asp.analyse");

	/** Messwert der die Geschwindigkeit beschreibt. */
	public static final Messwert VKFZ = new Messwert("VKfz", "Geschwindigkeit",
			"atg.verkehrsDatenKurzZeitMq", "asp.analyse");

	/** Sichert die Messwertlogik. */
	private final MQmitMesswert mq;

	/**
	 * Konstruiert einen Messquerschnitt mit Messwert.
	 * 
	 * @param so
	 *            Ein Systemobjekt, welches ein Messquerschnitt sein muss.
	 */
	public MessQuerschnittAllgemeinMesswert(SystemObject so) {
		super(so);
		mq = new MQmitMesswert();
	}

	/**
	 * {@inheritDoc}
	 */
	public DataDescription getDbs(Messwert mw) {
		return mq.getDbs(mw);
	}

	/**
	 * {@inheritDoc}
	 */
	public Messwert getMesswert(String name) {
		return mq.getMesswert(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Messwert> getMesswerte() {
		return mq.getMesswerte();
	}

	/**
	 * {@inheritDoc}
	 */
	public Number getWert(Messwert wert) {
		return mq.getWert(wert);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getZeitstempel() {
		return mq.getZeitstempel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWert(Messwert wert, Integer zahl) {
		mq.setWert(wert, zahl);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Messwert> setWerte(ResultData datensatz) {
		return mq.setWerte(datensatz);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setZeitstempel(long zeitstempel) {
		mq.setZeitstempel(zeitstempel);
	}

}
