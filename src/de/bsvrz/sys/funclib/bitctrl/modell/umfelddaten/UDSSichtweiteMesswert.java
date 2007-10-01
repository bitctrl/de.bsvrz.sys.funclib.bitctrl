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
 * Ein Sichtweitesensor erweitert um den Messwert.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UDSSichtweiteMesswert extends UDSHelligkeit implements
		MesswertObjekt {

	/** Messwert der die Sichtweite beschreibt. */
	public static final Messwert SICHTWEITE = new Messwert("SichtWeite",
			"Sichtweite", "atg.ufdsSichtWeite", "asp.messWertErsetzung");

	/**
	 * Erweitert die Abstrakte Klasse um die notwendige Logik.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	private class SichtweiteMitMesswert extends AbstractMesswertObjekt {

		/**
		 * Tut nichts.
		 */
		SichtweiteMitMesswert() {
			// Nur für Performance
		}

		/**
		 * {@inheritDoc}
		 */
		public DataDescription getDbs(Messwert mw) {
			AttributeGroup atg;
			Aspect asp;
			DataModel modell;

			modell = UDSSichtweiteMesswert.this.getSystemObject()
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
			messwerte.add(SICHTWEITE);
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

			if (atg.getPid().equals(SICHTWEITE.getAtgPID())) {
				setZeitstempel(datensatz.getDataTime());

				setWert(SICHTWEITE, getWert(datensatz, SICHTWEITE));
				neueWerte.add(SICHTWEITE);
			} else {
				throw new IllegalArgumentException(
						"Datensatz ist kein Analysewert für einen Sichtweitesensor.");
			}

			return neueWerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public long getId() {
			return UDSSichtweiteMesswert.this.getId();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return UDSSichtweiteMesswert.this.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getPid() {
			return UDSSichtweiteMesswert.this.getPid();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObject getSystemObject() {
			return UDSSichtweiteMesswert.this.getSystemObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObjektTyp getTyp() {
			return UDSSichtweiteMesswert.this.getTyp();
		}

	}

	/** Sichert die Umfelddatensensorlogik. */
	private SichtweiteMitMesswert umfelddatensensor;

	/**
	 * @param obj
	 */
	public UDSSichtweiteMesswert(SystemObject obj) {
		super(obj);
		umfelddatensensor = new SichtweiteMitMesswert();
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
