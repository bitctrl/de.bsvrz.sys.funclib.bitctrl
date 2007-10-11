package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

import java.util.List;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractParameterDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

public class SituationsEigenschaften extends AbstractParameterDatensatz {

	/**
	 * die Attributgruppe f¸r den Zugriff auf die Parameter.
	 */
	private static AttributeGroup attributGruppe;
	/**
	 * Startzeitpunkt der Situation (Staubeginn, Baustellenbeginn, // etc.).
	 * ("StartZeit")
	 */
	private long startZeit;

	/**
	 * Dauer des Situation (sofern bekannt). Eintrag von 0 ms bedeutet //
	 * unbekannte (unendliche) Dauer. ("Dauer")
	 */
	private long dauer;

	/**
	 * Referenzen auf alle Straﬂensegmente, ¸ber die sich die Situation
	 * ausbreitet. ("StraﬂenSegment")
	 */
	private List<StrassenSegment> segmente;

	/**
	 * Position des Situationsanfangs im ersten Straﬂensegment. ("StartOffset")
	 */
	long startOffset;

	/**
	 * Position des Situationsendes im letzten Straﬂensegment. ("EndOffset")
	 */
	long endOffset;

	public SituationsEigenschaften(SystemObjekt objekt) {
		super(objekt);
		if (attributGruppe == null) {
			attributGruppe = objekt.getSystemObject().getDataModel()
					.getAttributeGroup("atg.situationsEigenschaften");
		}
	}

	public void abmeldenSender() {
		// TODO Auto-generated method stub

	}

	public AttributeGroup getAttributGruppe() {
		return attributGruppe;
	}

	public long getDauer() {
		return dauer;
	}

	public long getEndOffset() {
		return endOffset;
	}

	public List<StrassenSegment> getSegmente() {
		return segmente;
	}

	public long getStartOffset() {
		return startOffset;
	}

	public long getStartZeit() {
		return startZeit;
	}

	public void sendeDaten() {
		// TODO Auto-generated method stub

	}

	public void setDaten(Data daten) {
		if (daten != null) {
			startZeit = daten.getTimeValue("StartZeit").getMillis();
			dauer = daten.getUnscaledValue("Dauer").longValue();
			segmente.clear();
			Data.Array segmentArray = daten.getArray("StraﬂenSegment");
			for (int idx = 0; idx < segmentArray.getLength(); idx++) {
				segmente.add((StrassenSegment) ObjektFactory.getInstanz()
						.getModellobjekt(
								segmentArray.getReferenceValue(idx)
										.getSystemObject()));
			}
			startOffset = daten.getUnscaledValue("StartOffset").longValue();
			endOffset = daten.getUnscaledValue("EndOffset").longValue();
		}
	}
}
