/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.NumberValue;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractMesswertObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.MesswertObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjektTyp;

/**
 * Eine Umfelddatenmessstelle erweitert um den Messwert.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class UmfelddatenMessstelleMesswert extends elddatenMessstelle
		implements MesswertObjekt {

	/** Messwert der die Gl&auml;tte beschreibt. */
	public static final Messwert GLAETTE = new Messwert("Glätte", "Glätte",
			"atg.ufdmsGlätte", "asp.prognose");

	/**
	 * Erweitert die Abstrakte Klasse um die notwendige Logik.
	 * 
	 * @author BitCtrl Systems GmbH, Schumann
	 * @version $Id$
	 */
	private class MessstelleMitMesswert extends AbstractMesswertObjekt {

		/**
		 * Tut nichts.
		 */
		public MessstelleMitMesswert() {
			// Nur zur Performance
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
		public Collection<Messwert> getMesswerte() {
			Set<Messwert> messwerte = new HashSet<Messwert>(5);
			messwerte.add(GLAETTE);
			return messwerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public Set<Messwert> setWerte(ResultData datensatz) {
			Set<Messwert> neueWerte;
			NumberValue wert;

			neueWerte = new HashSet<Messwert>();
			wert = datensatz.getData().getUnscaledValue("AktuellerZustand");

			setZeitstempel(datensatz.getDataTime());
			
			// wert < 0 sind Zustände, 0 ist nicht definiert
			if (wert.intValue() > 0) {
				setWert(GLAETTE, wert.intValue());
				neueWerte.add(GLAETTE);
			} else {
				setWert(GLAETTE, null);
			}

			return neueWerte;
		}

		/**
		 * {@inheritDoc}
		 */
		public long getId() {
			return UmfelddatenMessstelleMesswert.this.getId();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return UmfelddatenMessstelleMesswert.this.getName();
		}

		/**
		 * {@inheritDoc}
		 */
		public String getPid() {
			return UmfelddatenMessstelleMesswert.this.getPid();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObject getSystemObject() {
			return UmfelddatenMessstelleMesswert.this.getSystemObject();
		}

		/**
		 * {@inheritDoc}
		 */
		public SystemObjektTyp getTyp() {
			return UmfelddatenMessstelleMesswert.this.getTyp();
		}

	}

	/** Sichert die Messwertlogik. */
	private MessstelleMitMesswert messstelle;

	/**
	 * @param so
	 */
	public UmfelddatenMessstelleMesswert(SystemObject so) {
		super(so);
		messstelle = new MessstelleMitMesswert();
	}

	/**
	 * {@inheritDoc}
	 */
	public DataDescription getDbs(Messwert mw) {
		return messstelle.getDbs(mw);
	}

	/**
	 * {@inheritDoc}
	 */
	public Messwert getMesswert(String name) {
		return messstelle.getMesswert(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<Messwert> getMesswerte() {
		return messstelle.getMesswerte();
	}

	/**
	 * {@inheritDoc}
	 */
	public Number getWert(Messwert wert) {
		return messstelle.getWert(wert);
	}

	/**
	 * {@inheritDoc}
	 */
	public long getZeitstempel() {
		return messstelle.getZeitstempel();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setWert(Messwert wert, Integer zahl) {
		messstelle.setWert(wert, zahl);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<Messwert> setWerte(ResultData datensatz) {
		return messstelle.setWerte(datensatz);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setZeitstempel(long zeitstempel) {
		messstelle.setZeitstempel(zeitstempel);
	}

}
