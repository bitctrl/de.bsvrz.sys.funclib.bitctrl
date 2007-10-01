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
 * Ein Windgeschwindigkeitssensor erweitert um den Messwert.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UDSWindgeschwindigkeitMesswert extends UDSHelligkeit implements
		MesswertObjekt {

	/** Messwert der die Windgeschwindigkeit beschreibt. */
	public static final Messwert WINDGESCHWINDIGKEIT = new Messwert(
			"WindGeschwindigkeitMittelWert", "WindGeschwindigkeit",
			"atg.ufdsWindGeschwindigkeitMittelWert", "asp.messWertErsetzung");

	/**
	 * Erweitert die Abstrakte Klasse um die notwendige Logik.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	private class WindgeschwindigkeitMitMesswert extends AbstractMesswertObjekt {

		/**
		 * Tut nichts.
		 */
		WindgeschwindigkeitMitMesswert() {
			// Nur für Performance
		}

		/**
		 * {@inheritDoc}
		 */
		public DataDescription getDbs(Messwert mw) {
			AttributeGroup atg;
			Aspect asp;
			DataModel modell;

			modell = UDSWindgeschwindigkeitMesswert.this.getSystemObject()
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
			messwerte.add(WINDGESCHWINDIGKEIT);
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

			if (atg.getPid().equals(WINDGESCHWINDIGKEIT.getAtgPID())) {
				setZeitstempel(datensatz.getDataTime());

				setWert(WINDGESCHWINDIGKEIT, getWert(datensatz,
						WINDGESCHWINDIGKEIT));
				neueWerte.add(WINDGESCHWINDIGKEIT);
			} else {
				throw new IllegalArgumentException(
						"Datensatz ist kein Analysewert für einen Helligkeitssensor.");
			}

			return neueWerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public long getId() {
			return UDSWindgeschwindigkeitMesswert.this.getId();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return UDSWindgeschwindigkeitMesswert.this.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getPid() {
			return UDSWindgeschwindigkeitMesswert.this.getPid();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObject getSystemObject() {
			return UDSWindgeschwindigkeitMesswert.this.getSystemObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObjektTyp getTyp() {
			return UDSWindgeschwindigkeitMesswert.this.getTyp();
		}

	}

	/** Sichert die Umfelddatensensorlogik. */
	private WindgeschwindigkeitMitMesswert umfelddatensensor;

	/**
	 * @param obj
	 */
	public UDSWindgeschwindigkeitMesswert(SystemObject obj) {
		super(obj);
		umfelddatensensor = new WindgeschwindigkeitMitMesswert();
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
