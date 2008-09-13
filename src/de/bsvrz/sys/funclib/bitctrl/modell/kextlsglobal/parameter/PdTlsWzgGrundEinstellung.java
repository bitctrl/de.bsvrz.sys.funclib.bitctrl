/**
 * 
 */
package de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.parameter;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
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
		private final List<TlsWzgWvzStellCode> stellCode = new ArrayList<TlsWzgWvzStellCode>();

		/**
		 * @return the urlasser
		 */
		public Urlasser getUrlasser() {
			return urlasser;
		}

		/**
		 * @param urlasser
		 *            the urlasser to set
		 */
		public void setUrlasser(final Urlasser urlasser) {
			this.urlasser = urlasser;
		}

		/**
		 * @return the anzeigePrinzip
		 */
		public TlsWzgAnzeigePrinzip getAnzeigePrinzip() {
			return anzeigePrinzip;
		}

		/**
		 * @param anzeigePrinzip
		 *            the anzeigePrinzip to set
		 */
		public void setAnzeigePrinzip(final TlsWzgAnzeigePrinzip anzeigePrinzip) {
			this.anzeigePrinzip = anzeigePrinzip;
		}

		/**
		 * @return the stellCode
		 */
		public List<TlsWzgWvzStellCode> getStellCode() {
			return stellCode;
		}

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
			klon.anzeigePrinzip = anzeigePrinzip;
			klon.stellCode.addAll(stellCode);

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
		final Data daten = erzeugeSendeCache();

		if (datum.getUrlasser() != null) {
			datum.getUrlasser().bean2Atl(daten.getItem("Urlasser"));
		}
		daten.getUnscaledValue("AnzeigePrinzip").set(
				datum.getAnzeigePrinzip().getCode());

		final Array stellCode = daten.getArray("StellCode");
		stellCode.setLength(datum.getStellCode().size());
		for (int i = 0; i < stellCode.getLength(); ++i) {
			datum.getStellCode().get(i).bean2Atl(stellCode.getItem(i));
		}

		return daten;
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
	public void setDaten(final ResultData result) {
		check(result);

		final Daten datum = new Daten();
		if (result.hasData()) {
			final Data daten = result.getData();

			final Urlasser urlasser = new Urlasser();
			urlasser.atl2Bean(daten.getItem("Urlasser"));
			datum.setUrlasser(urlasser);

			datum.setAnzeigePrinzip(TlsWzgAnzeigePrinzip.valueOf(daten
					.getUnscaledValue("AnzeigePrinzip").byteValue()));

			final Array stellCodeArray = daten.getArray("StellCode");
			for (int i = 0; i < stellCodeArray.getLength(); ++i) {
				final TlsWzgWvzStellCode stellCode = new TlsWzgWvzStellCode();
				stellCode.atl2Bean(daten.getItem("StellCode"));
				datum.getStellCode().add(stellCode);
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
