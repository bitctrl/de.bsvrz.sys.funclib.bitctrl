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
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.attributlisten.ProtokollEinstellung;
import de.bsvrz.sys.funclib.bitctrl.modell.kextlsglobal.objekte.AnschlussPunkt;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.attributlisten.Urlasser;

/**
 * Parametersatz mit Standard-Einstellungen für OSI-2-Protokolle (Primary und
 * Secondary), die über den zugeordneten Anschlusspunkt kommunizieren.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class PdProtokollEinstellungenStandard extends
		AbstractParameterDatensatz<PdProtokollEinstellungenStandard.Daten> {

	/**
	 * Repräsentation der Daten.
	 * 
	 * @todo Attribute vervollständigen.
	 */
	public static class Daten extends AbstractDatum {

		/** Der initiale Datenstatus ist <em>undefiniert</em>. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		private Urlasser urlasser;
		private final List<ProtokollEinstellung> protokollEinstellung = new ArrayList<ProtokollEinstellung>();

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
		 * @return
		 */
		public List<ProtokollEinstellung> getProtokollEinstellung() {
			return protokollEinstellung;
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
			klon.protokollEinstellung.addAll(protokollEinstellung);

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
			s += ", protokollEinstellung=" + protokollEinstellung;
			s += "]";

			return s;
		}
	}

	/** Die PID der Attributgruppe. */
	public static final String PID_ATG = "atg.protokollEinstellungenStandard";

	/** Die Attributgruppe, in der die Eigenschaften enthalten sind. */
	private static AttributeGroup atg;

	/**
	 * @param anschlussPunkt
	 */
	public PdProtokollEinstellungenStandard(final AnschlussPunkt anschlussPunkt) {
		super(anschlussPunkt);

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

		final Array protokollEinstellung = daten
				.getArray("ProtokollEinstellung");
		protokollEinstellung.setLength(datum.getProtokollEinstellung().size());
		for (int i = 0; i < protokollEinstellung.getLength(); ++i) {
			datum.getProtokollEinstellung().get(i).bean2Atl(
					protokollEinstellung.getItem(i));
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

			final Array stellCodeArray = daten.getArray("ProtokollEinstellung");
			for (int i = 0; i < stellCodeArray.getLength(); ++i) {
				final ProtokollEinstellung protokollEinstellung = new ProtokollEinstellung();
				protokollEinstellung.atl2Bean(daten
						.getItem("ProtokollEinstellung"));
				datum.getProtokollEinstellung().add(protokollEinstellung);
			}
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}

}
