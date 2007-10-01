/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

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
 * Ein Niederschlagssensor erweitert um den Messwert.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UDSNiederschlagsintensitaetMesswert extends UDSHelligkeit
		implements MesswertObjekt {

	/** Messwert der die Niederschlagsintensit&auml;t beschreibt. */
	public static final Messwert NIEDERSCHLAGSINTENSITAET = new Messwert(
			"NiederschlagsIntensität", "Niederschlagsintensität",
			"atg.ufdsNiederschlagsIntensität", "asp.messWertErsetzung");

	/**
	 * Erweitert die Abstrakte Klasse um die notwendige Logik.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	private class NiederschlagsintensitaetMitMesswert extends
			AbstractMesswertObjekt {

		/**
		 * Tut nichts.
		 */
		NiederschlagsintensitaetMitMesswert() {
			// Nur für Performance
		}

		/**
		 * {@inheritDoc}
		 */
		public DataDescription getDbs(Messwert mw) {
			AttributeGroup atg;
			Aspect asp;
			DataModel modell;

			modell = UDSNiederschlagsintensitaetMesswert.this.getSystemObject()
					.getDataModel();
			atg = modell.getAttributeGroup(mw.getAtgPID());
			asp = modell.getAspect(mw.getAspPID());
			return new DataDescription(atg, asp);
		}

		/**
		 * {@inheritDoc}
		 */
		public Collection<Messwert> getMesswerte() {
			Set<Messwert> messwerte = new HashSet<Messwert>(5);
			messwerte.add(NIEDERSCHLAGSINTENSITAET);
			return messwerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public Set<Messwert> setWerte(ResultData datensatz) {
			Set<Messwert> neueWerte;
			AttributeGroup atg;

			atg = datensatz.getDataDescription().getAttributeGroup();
			neueWerte = new HashSet<Messwert>();

			if (atg.getPid().equals(NIEDERSCHLAGSINTENSITAET.getAtgPID())) {
				setZeitstempel(datensatz.getDataTime());

				setWert(NIEDERSCHLAGSINTENSITAET, getWert(datensatz,
						NIEDERSCHLAGSINTENSITAET));
				neueWerte.add(NIEDERSCHLAGSINTENSITAET);
			} else {
				throw new IllegalArgumentException(
						"Datensatz ist kein Analysewert für einen Niederschlagssensor.");
			}

			return neueWerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public long getId() {
			return UDSNiederschlagsintensitaetMesswert.this.getId();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return UDSNiederschlagsintensitaetMesswert.this.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getPid() {
			return UDSNiederschlagsintensitaetMesswert.this.getPid();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObject getSystemObject() {
			return UDSNiederschlagsintensitaetMesswert.this.getSystemObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObjektTyp getTyp() {
			return UDSNiederschlagsintensitaetMesswert.this.getTyp();
		}

	}

	/** Sichert die Umfelddatensensorlogik. */
	private NiederschlagsintensitaetMitMesswert umfelddatensensor;

	/**
	 * @param obj
	 */
	public UDSNiederschlagsintensitaetMesswert(SystemObject obj) {
		super(obj);
		umfelddatensensor = new NiederschlagsintensitaetMitMesswert();
	}

	/**
	 * {@inheritDoc}
	 */
	public DataDescription getDbs(Messwert mw) {
		return umfelddatensensor.getDbs(mw);
	}

	/**
	 * {@inheritDoc}
	 */
	public Messwert getMesswert(String name) {
		return umfelddatensensor.getMesswert(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Messwert> getMesswerte() {
		return umfelddatensensor.getMesswerte();
	}

	/**
	 * {@inheritDoc}
	 */
	public Number getWert(Messwert wert) {
		return umfelddatensensor.getWert(wert);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getZeitstempel() {
		return umfelddatensensor.getZeitstempel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWert(Messwert wert, Integer zahl) {
		umfelddatensensor.setWert(wert, zahl);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Messwert> setWerte(ResultData datensatz) {
		return umfelddatensensor.setWerte(datensatz);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setZeitstempel(long zeitstempel) {
		umfelddatensensor.setZeitstempel(zeitstempel);
	}

}
