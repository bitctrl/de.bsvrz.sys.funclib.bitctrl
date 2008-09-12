/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.parameter;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.attributlisten.TlsWzgWvzStellCode;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.objekte.DeWzg;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.zustaende.TlsWzgAnzeigePrinzip;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.attributlisten.Urlasser;

/**
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class PdTlsWzgGrundEinstellung extends
		AbstractParameterDatensatz<PdTlsWzgGrundEinstellung.Daten> {

	/**
	 * Repräsentation der Daten.
	 * 
	 * @todo Attribute vervollständigen.
	 */
	public static class Daten extends AbstractDatum {

		/** Der initiale Datenstatus ist <em>undefiniert</em>. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		private Urlasser urlasser;
		private TlsWzgAnzeigePrinzip anzeigePrinzip;
		private TlsWzgWvzStellCode stellCode;

		/**
		 * {@inheritDoc}
		 */
		public Status getDatenStatus() {
			return datenStatus;
		}

		/**
		 * Setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status.
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.datenStatus = datenStatus;
			klon.urlasser = urlasser.clone();
			klon.anzeigePrinzip = anzeigePrinzip.clone();
			klon.stellCode = stellCode.clone();

			return klon;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			String s;

			s = getClass().getName() + "[";
			s += "zeitpunkt=" + getZeitpunkt();
			s += ", datenStatus=" + datenStatus;
			s += ", urlasser=" + urlasser;
			s += ", anzeigePrinzip=" + anzeigePrinzip;
			s += ", stellCode=" + stellCode;
			s += "]";

			return s;
		}
	}

	/** Die PID der Attributgruppe. */
	public static final String PID_ATG = "atg.tlsWzgGrundEinstellung";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * @param deWzg
	 */
	public PdTlsWzgGrundEinstellung(final DeWzg deWzg) {
		super(deWzg);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(PID_ATG);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Data konvertiere(final Daten datum) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Daten erzeugeDatum() {
		return new Daten();
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
	public void setDaten(final ResultData daten) {
		// TODO Auto-generated method stub

	}

}
