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

package de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.config.Aspect;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractDatum;
import de.bsvrz.sys.funclib.bitctrl.modell.AbstractOnlineDatensatz;
import de.bsvrz.sys.funclib.bitctrl.modell.Aspekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.Urlasser;
import de.bsvrz.sys.funclib.bitctrl.modell.systemmodellglobal.objekte.Benutzer;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte.BetriebsMeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsKlasse;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsTyp;

/**
 * Kapselt die Attributgruppe {@code atg.betriebsMeldung}.
 * <p>
 * <em>Hinweis:</em> Dieser Datensatz ist mit bedacht zu verwenden, da er in
 * der Regel mit Hilfe von speziellen Klassen bzw. Methoden verwendet wird.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 * @see de.bsvrz.sys.funclib.operatingMessage.MessageSender
 * @see de.bsvrz.sys.funclib.bitctrl.bmv.Betriebsmeldungsverwaltung
 */
public class BetriebsMeldung extends
		AbstractOnlineDatensatz<BetriebsMeldung.Daten> {

	/**
	 * Die vorhandenen Aspekte des Datensatzes.
	 */
	public enum Aspekte implements Aspekt {

		/** Der Aspekt {@code asp.information}. */
		Information("asp.information");

		/** Der Aspekt, den das enum kapselt. */
		private final Aspect aspekt;

		/**
		 * Erzeugt aus der PID den Aspekt.
		 * 
		 * @param pid
		 *            die PID eines Aspekts.
		 */
		private Aspekte(final String pid) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			aspekt = modell.getAspect(pid);
			assert aspekt != null;
		}

		/**
		 * {@inheritDoc}
		 */
		public Aspect getAspekt() {
			return aspekt;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getName() {
			return aspekt.getNameOrPidOrId();
		}

	}

	/**
	 * Kapselt die Daten des Datensatzes.
	 */
	public static class Daten extends AbstractDatum implements
			Comparable<Daten> {

		/** Der aktuelle Status des Datensatzes. */
		private Status datenStatus = Datum.Status.UNDEFINIERT;

		/** Die Id der Applikation, die die Informationsmeldung erzeugt hat. */
		private long applikationsId;

		/**
		 * Laufende Nummerierung der durch die Applikation seit dem
		 * Applikationsstart erzeugten Informationsmeldungen.
		 */
		private long laufendeNummer;

		/** Typ der Applikation, die die Informationsmeldung erzeugt hat. */
		private SystemObjekt applikationsTyp;

		/** Wird für einen Bezug zur vorherigen Meldung benötigt. */
		private String applikationsKennung;

		/** ID der Meldung. */
		private String id;

		/** Klassifizierung der Meldungen in unterschiedliche Typen. */
		private MeldungsTyp meldungsTyp;

		/** Zur genaueren Beschreibung des MeldungsTyps. */
		private String meldungsTypZusatz;

		/** Klassifizierung einer Meldung. */
		private MeldungsKlasse meldungsKlasse;

		/** Referenz auf ein beliebiges Konfigurationsobjekt. */
		private SystemObjekt referenz;

		/** Gibt den Meldungsstatus an. */
		private MeldungsStatus meldungsStatus;

		/** Die Urlasserinformation. */
		private Urlasser urlasser;

		/** Text der Meldung. */
		private String meldungsText;

		/**
		 * Gibt die Id der Applikation zurück, die die Informationsmeldung
		 * erzeugt hat.
		 * 
		 * @return die Applikations-Id.
		 */
		public long getApplikationsId() {
			return applikationsId;
		}

		/**
		 * Legt die Id der Applikation fest, die die Informationsmeldung erzeugt
		 * hat.
		 * 
		 * @param applikationsId
		 *            die Applikations-Id.
		 */
		public void setApplikationsId(final long applikationsId) {
			this.applikationsId = applikationsId;
		}

		/**
		 * Gibt die laufende Nummerierung der durch die Applikation seit dem
		 * Applikationsstart erzeugten Informationsmeldungen zurück.
		 * 
		 * @return die laufende Nummer der Meldung.
		 */
		public long getLaufendeNummer() {
			return laufendeNummer;
		}

		/**
		 * Legt die laufende Nummerierung der durch die Applikation seit dem
		 * Applikationsstart erzeugten Informationsmeldungen fest.
		 * 
		 * @param laufendeNummer
		 *            die laufende Nummer der Meldung.
		 */
		public void setLaufendeNummer(final long laufendeNummer) {
			this.laufendeNummer = laufendeNummer;
		}

		/**
		 * Gibt den Typ der Applikation, die die Informationsmeldung erzeugt hat
		 * zurück.
		 * 
		 * @return der Applikationstyp.
		 */
		public SystemObjekt getApplikationsTyp() {
			return applikationsTyp;
		}

		/**
		 * Legt den Typ der Applikation, die die Informationsmeldung erzeugt hat
		 * fest.
		 * 
		 * @param applikationsTyp
		 *            der Applikationstyp.
		 */
		public void setApplikationsTyp(final SystemObjekt applikationsTyp) {
			this.applikationsTyp = applikationsTyp;
		}

		/**
		 * Gibt die Applikationskennung zurück.
		 * 
		 * @return die Applikationskennung.
		 */
		public String getApplikationsKennung() {
			return applikationsKennung;
		}

		/**
		 * Legt die Applikationskennung fest.
		 * 
		 * @param applikationsKennung
		 *            die Applikationskennung.
		 */
		public void setApplikationsKennung(final String applikationsKennung) {
			this.applikationsKennung = applikationsKennung;
		}

		/**
		 * Gibt die Id der Meldung zurück. Dieses Attribut kann bei jeder
		 * Meldung von der Applikation gesetzt werden, um einen Bezug zu einer
		 * vorherigen Meldung herzustellen.
		 * 
		 * @return die Meldungs-Id.
		 */
		public String getId() {
			return id;
		}

		/**
		 * Legt die Id der Meldung fest. Dieses Attribut kann bei jeder Meldung
		 * von der Applikation gesetzt werden, um einen Bezug zu einer
		 * vorherigen Meldung herzustellen.
		 * 
		 * @param id
		 *            die Meldungs-Id.
		 */
		public void setId(final String id) {
			this.id = id;
		}

		/**
		 * Gibt den Typ der Meldung zurück. Klassifizierung der Meldungen in
		 * unterschiedliche Typen, die eine inhaltliche Unterscheidung der
		 * Meldungen bewirken. Z.B. System und Fach für Meldungen, die sich auf
		 * programmtechnische oder fachliche Zustände beziehen.
		 * 
		 * @return der Meldungstyp.
		 */
		public MeldungsTyp getMeldungsTyp() {
			return meldungsTyp;
		}

		/**
		 * Legt den Typ der Meldung fest. Klassifizierung der Meldungen in
		 * unterschiedliche Typen, die eine inhaltliche Unterscheidung der
		 * Meldungen bewirken. Z.B. System und Fach für Meldungen, die sich auf
		 * programmtechnische oder fachliche Zustände beziehen.
		 * 
		 * @param meldungsTyp
		 *            der Meldungstyp.
		 */
		public void setMeldungsTyp(final MeldungsTyp meldungsTyp) {
			this.meldungsTyp = meldungsTyp;
		}

		/**
		 * Gibt die nähere Beschreibung des Meldungstyps zurück.
		 * 
		 * @return der Meldungstypzusatz.
		 */
		public String getMeldungsTypZusatz() {
			return meldungsTypZusatz;
		}

		/**
		 * Legt die nähere Beschreibung des Meldungstyps fest.
		 * 
		 * @param meldungsTypZusatz
		 *            der Meldungstypzusatz.
		 */
		public void setMeldungsTypZusatz(final String meldungsTypZusatz) {
			this.meldungsTypZusatz = meldungsTypZusatz;
		}

		/**
		 * Gibt die Klasse der Meldung zurück.
		 * 
		 * @return die Meldungsklasse.
		 */
		public MeldungsKlasse getMeldungsKlasse() {
			return meldungsKlasse;
		}

		/**
		 * Legt die Klasse der Meldung fest.
		 * 
		 * @param meldungsKlasse
		 *            die Meldungsklasse.
		 */
		public void setMeldungsKlasse(final MeldungsKlasse meldungsKlasse) {
			this.meldungsKlasse = meldungsKlasse;
		}

		/**
		 * Gibt die Referez auf ein Systemobjekt zurück, auf das sich die
		 * Meldung bezieht.
		 * 
		 * @return ein Systemobjekt oder {@code null}.
		 */
		public SystemObjekt getReferenz() {
			return referenz;
		}

		/**
		 * Legt die Referez auf ein Systemobjekt fest, auf das sich die Meldung
		 * bezieht.
		 * 
		 * @param referenz
		 *            ein Systemobjekt oder {@code null}.
		 */
		public void setReferenz(final SystemObjekt referenz) {
			this.referenz = referenz;
		}

		/**
		 * Gibt den Status der Meldung zurück.
		 * 
		 * @return der Meldungsstatus.
		 */
		public MeldungsStatus getMeldungsStatus() {
			return meldungsStatus;
		}

		/**
		 * Legt den Status der Meldung fest.
		 * 
		 * @param meldungsStatus
		 *            der Meldungsstatus.
		 */
		public void setMeldungsStatus(final MeldungsStatus meldungsStatus) {
			this.meldungsStatus = meldungsStatus;
		}

		/**
		 * Gibt den Text der Meldung zurück.
		 * 
		 * @return der Meldungstext.
		 */
		public String getMeldungsText() {
			return meldungsText;
		}

		/**
		 * Legt den Text der Meldung fest.
		 * 
		 * @param meldungsText
		 *            der Meldungstext.
		 */
		public void setMeldungsText(final String meldungsText) {
			this.meldungsText = meldungsText;
		}

		/**
		 * Gibt die Urlasserinformation Benutzer zurück.
		 * 
		 * @return die Urlasserinformation.
		 */
		public Urlasser getUrlasser() {
			return urlasser;
		}

		/**
		 * Legt die Urlasserinformation fest.
		 * 
		 * @param urlasser
		 *            die Urlasserinformation.
		 */
		public void setUrlasser(final Urlasser urlasser) {
			this.urlasser = urlasser;
		}

		/**
		 * Zwei Betriebsmeldungen werden als gleich angesehen, wenn sie in
		 * Meldungstyp, Meldungstypzusatz, Meldungsklasse, Referenz,
		 * Meldungstext, Urlasserbenutzer und Urlasserursache übereinstimmen.
		 * 
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof Daten) {
				final Daten datum = (Daten) obj;

				return datum.meldungsTyp.equals(meldungsTyp)
						&& datum.meldungsTypZusatz.equals(meldungsTypZusatz)
						&& datum.meldungsKlasse.equals(meldungsKlasse)
						&& datum.referenz.equals(referenz)
						&& datum.meldungsText.equals(meldungsText)
						&& datum.urlasser.equals(urlasser);
			}

			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Daten clone() {
			final Daten klon = new Daten();

			klon.setZeitstempel(getZeitstempel());
			klon.applikationsId = applikationsId;
			klon.applikationsKennung = applikationsKennung;
			klon.applikationsTyp = applikationsTyp;
			klon.datenStatus = datenStatus;
			klon.id = id;
			klon.laufendeNummer = laufendeNummer;
			klon.meldungsKlasse = meldungsKlasse;
			klon.meldungsText = meldungsText;
			klon.meldungsTyp = meldungsTyp;
			klon.meldungsTypZusatz = meldungsTypZusatz;
			klon.referenz = referenz;
			klon.meldungsStatus = meldungsStatus;
			klon.urlasser = urlasser != null ? urlasser.clone() : null;

			return klon;
		}

		/**
		 * {@inheritDoc}
		 */
		public Status getDatenStatus() {
			return datenStatus;
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
			s += ", applikationsId=" + applikationsId;
			s += ", applikationsKennung=" + applikationsKennung;
			s += ", applikationsTyp=" + applikationsTyp;
			s += ", id=" + id;
			s += ", laufendeNummer=" + laufendeNummer;
			s += ", meldungsKlasse=" + meldungsKlasse;
			s += ", meldungsStatus=" + meldungsStatus;
			s += ", meldungsTyp=" + meldungsTyp;
			s += ", meldungsTypZusatz=" + meldungsTypZusatz;
			s += ", referenz=" + referenz;
			s += ", urlasser=" + urlasser;
			s += ", meldungsText=" + meldungsText;
			s += "]";

			return s;
		}

		/**
		 * Setzt den aktuellen Status des Datensatzes.
		 * 
		 * @param neuerStatus
		 *            der neue Status
		 */
		protected void setDatenStatus(final Status neuerStatus) {
			datenStatus = neuerStatus;
		}

		/**
		 * Sortiert werden die Betriebsmeldungen nach ihrem Zeitstempel.
		 * 
		 * {@inheritDoc}
		 */
		public int compareTo(final Daten datum) {
			if (getZeitstempel() < datum.getZeitstempel()) {
				return -1;
			} else if (getZeitstempel() > datum.getZeitstempel()) {
				return 1;
			}
			return 0;
		}

	}

	/** Die PID der Attributgruppe. */
	public static final String ATG_BETRIEBSMELDUNG = "atg.betriebsMeldung";

	/** Die Attributgruppe kann von allen Instanzen gemeinsam genutzt werden. */
	private static AttributeGroup atg;

	/**
	 * Initialisiert den Onlinedatensatz.
	 * 
	 * @param bmv
	 *            die Betriebsmeldungsverwaltung, dessen Onlinedaten verwalten
	 *            werden sollen.
	 */
	public BetriebsMeldung(final BetriebsMeldungsVerwaltung bmv) {
		super(bmv);

		if (atg == null) {
			final DataModel modell = ObjektFactory.getInstanz().getVerbindung()
					.getDataModel();
			atg = modell.getAttributeGroup(ATG_BETRIEBSMELDUNG);
			assert atg != null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Aspect> getAspekte() {
		final Set<Aspect> aspekte = new HashSet<Aspect>();
		for (final Aspekt a : Aspekte.values()) {
			aspekte.add(a.getAspekt());
		}
		return aspekte;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Data konvertiere(final Daten datum) {
		final Data daten = erzeugeSendeCache();

		daten.getUnscaledValue("ApplikationsID").set(datum.getApplikationsId());
		daten.getTextValue("ApplikationsKennung").setText(
				datum.getApplikationsKennung());
		daten.getReferenceValue("ApplikationsTyp").setSystemObject(
				datum.getApplikationsTyp().getSystemObject());
		daten.getTextValue("ID").setText(datum.getId());
		daten.getUnscaledValue("LaufendeNummer").set(datum.getLaufendeNummer());
		daten.getUnscaledValue("MeldungsKlasse").set(
				datum.getMeldungsKlasse().getCode());
		daten.getUnscaledValue("Status").set(
				datum.getMeldungsStatus().getCode());
		daten.getTextValue("MeldungsText").setText(datum.getMeldungsText());
		daten.getUnscaledValue("MeldungsTyp").set(
				datum.getMeldungsTyp().getCode());
		daten.getTextValue("MeldungsTypZusatz").setText(
				datum.getMeldungsTypZusatz());

		if (datum.getReferenz() != null) {
			Array array;

			array = daten.getArray("Referenz");
			array.setLength(1);
			array.getItem(0).asReferenceValue().setSystemObject(
					datum.getReferenz().getSystemObject());
		}

		daten.getItem("Urlasser").getReferenceValue("BenutzerReferenz")
				.setSystemObject(
						datum.getUrlasser().getBenutzer().getSystemObject());
		daten.getItem("Urlasser").getTextValue("Ursache").setText(
				datum.getUrlasser().getUrsache());
		daten.getItem("Urlasser").getTextValue("Veranlasser").setText(
				datum.getUrlasser().getVeranlasser());

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
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final Data daten = result.getData();

			datum.setApplikationsId(daten.getUnscaledValue("ApplikationsID")
					.longValue());
			datum.setApplikationsKennung(daten.getTextValue(
					"ApplikationsKennung").getText());
			datum.setApplikationsTyp(factory.getModellobjekt(daten
					.getReferenceValue("ApplikationsTyp").getSystemObject()));
			datum.setId(daten.getTextValue("ID").getText());
			datum.setLaufendeNummer(daten.getUnscaledValue("LaufendeNummer")
					.longValue());
			datum.setMeldungsKlasse(MeldungsKlasse.getMeldungsKlasse(daten
					.getUnscaledValue("MeldungsKlasse").intValue()));
			datum.setMeldungsStatus(MeldungsStatus.getMeldungsStatus(daten
					.getUnscaledValue("Status").intValue()));
			datum.setMeldungsText(daten.getTextValue("MeldungsText").getText());
			datum.setMeldungsTyp(MeldungsTyp.getMeldungsTyp(daten
					.getUnscaledValue("MeldungsTyp").intValue()));
			datum.setMeldungsTypZusatz(daten.getTextValue("MeldungsTypZusatz")
					.getText());

			final Array array = daten.getArray("Referenz");
			if (array.getLength() > 0) {
				datum.setReferenz(factory.getModellobjekt(array.getItem(0)
						.asReferenceValue().getSystemObject()));
			}

			final Urlasser urlasser = new Urlasser();
			urlasser.setBenutzer((Benutzer) factory.getModellobjekt(daten
					.getItem("Urlasser").getReferenceValue("BenutzerReferenz")
					.getSystemObject()));
			urlasser.setUrsache(daten.getItem("Urlasser").getTextValue(
					"Ursache").getText());
			urlasser.setVeranlasser(daten.getItem("Urlasser").getTextValue(
					"Veranlasser").getText());
			datum.setUrlasser(urlasser);
		}

		datum.setDatenStatus(Datum.Status.getStatus(result.getDataState()));
		datum.setZeitstempel(result.getDataTime());
		setDatum(result.getDataDescription().getAspect(), datum);
		fireDatensatzAktualisiert(result.getDataDescription().getAspect(),
				datum.clone());
	}
}
