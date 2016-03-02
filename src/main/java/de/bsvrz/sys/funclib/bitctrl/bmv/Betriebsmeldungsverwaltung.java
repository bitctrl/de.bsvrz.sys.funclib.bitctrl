/*
 * Allgemeine Funktionen mit und ohne Datenverteilerbezug
 * Copyright (C) 2007-2015 BitCtrl Systems GmbH
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

package de.bsvrz.sys.funclib.bitctrl.bmv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.bitctrl.util.Interval;
import com.bitctrl.util.logging.LoggerTools;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.archive.ArchiveDataKind;
import de.bsvrz.sys.funclib.bitctrl.archiv.ArchivIterator;
import de.bsvrz.sys.funclib.bitctrl.archiv.ArchivUtilities;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum.Status;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.objekte.BcBetriebsMeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.bitctrl.common.parameter.PdBcBetriebsMeldungDarstellung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.attributlisten.Urlasser;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte.BetriebsMeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.OdBetriebsMeldung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsKlasse;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsStatus;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.zustaende.MeldungsTyp;
import de.bsvrz.sys.funclib.debug.Debug;
import de.bsvrz.sys.funclib.operatingMessage.MessageCauser;
import de.bsvrz.sys.funclib.operatingMessage.MessageGrade;
import de.bsvrz.sys.funclib.operatingMessage.MessageSender;
import de.bsvrz.sys.funclib.operatingMessage.MessageState;
import de.bsvrz.sys.funclib.operatingMessage.MessageType;

/**
 * Hilfsklasse für den Umgang mit der Betriebsmeldungsverwaltung. Die Klasse
 * führt eine Liste der letzten Meldungen. Andere Klassen können sich über
 * Änderungen an dieser Liste informieren lassen.
 *
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @see de.bsvrz.sys.funclib.operatingMessage.MessageSender
 */
public final class Betriebsmeldungsverwaltung {

	/**
	 * PID der BitCtrl-Betriebsmeldungsverwaltung mit erweiterten Parametern.
	 */
	public static final String PID_BITCTRL_BMV = "bitctrl.bmv";

	/** Das einzige Objekt dieser Klasse. */
	private static Betriebsmeldungsverwaltung singleton;

	/**
	 * Gibt die Betriebsmeldungsverwaltung als Singleton zurück.
	 *
	 * @return die Betriebsmeldungsverwaltung.
	 */
	public static Betriebsmeldungsverwaltung getInstanz() {
		if (singleton == null) {
			singleton = new Betriebsmeldungsverwaltung();
		}
		return singleton;
	}

	/**
	 * Liest die letzen Betriebsmeldungen aus dem Archiv und cacht diese; meldet
	 * sich auf neue Betriebsmeldungen als Empfänger an.
	 */
	private class Meldungsempfaenger implements DatensatzUpdateListener {

		@Override
		public void datensatzAktualisiert(final DatensatzUpdateEvent event) {
			final List<OdBetriebsMeldung.Daten> neu = new ArrayList<OdBetriebsMeldung.Daten>();

			if (event.getDatum() instanceof OdBetriebsMeldung.Daten) {
				final OdBetriebsMeldung.Daten datum = (OdBetriebsMeldung.Daten) event
						.getDatum();
				synchronized (meldungsliste) {
					meldungsliste.add(datum);
					neu.add(datum);

					for (final BetriebsmeldungCommand befehl : befehlsliste) {
						if (befehl.isTrigger(datum)) {
							befehl.exec();
						}
					}
				}
			} else if (event
					.getDatum() instanceof PdBcBetriebsMeldungDarstellung.Daten) {
				darstellungsparameter = (PdBcBetriebsMeldungDarstellung.Daten) event
						.getDatum();
			}

			final List<OdBetriebsMeldung.Daten> entfernt = cleanUpMeldungen();
			fireMeldungslisteChanged(neu, entfernt);
		}

	}

	/** Der Logger der Klasse. */
	private final Debug log;

	/** Die Liste der angemeldeten Listener. */
	private final EventListenerList listeners;

	/** Die Liste der gecachten Meldungen. */
	private final List<OdBetriebsMeldung.Daten> meldungsliste;

	/** Liste er Befehle die beim Meldungsempfang verarbeitet werden. */
	private final List<BetriebsmeldungCommand> befehlsliste;

	/** Der Datensatz mit dem die Meldungen empfangen werden. */
	private OdBetriebsMeldung datensatzBetriebsMeldung;

	/** Empfänger der Betriebsmeldungen. */
	private Meldungsempfaenger empfaenger;

	/** Die Darstellungsparameter für Meldungen. */
	private PdBcBetriebsMeldungDarstellung.Daten darstellungsparameter;

	/**
	 * Liest initial die letzten Betriebsmeldungen aus dem Archiv und cacht
	 * diese.
	 */
	private Betriebsmeldungsverwaltung() {
		log = Debug.getLogger();
		listeners = new EventListenerList();
		meldungsliste = new LinkedList<OdBetriebsMeldung.Daten>();
		befehlsliste = new ArrayList<BetriebsmeldungCommand>();

		log.info("Betriebsmeldungsverwaltung bereit.");
	}

	/**
	 * Führt falls noch nicht geschehen die Empfängeranmeldung für
	 * Betriebsmeldungen und das Auslesen der letzten Meldungen aus dem Archiv
	 * aus.
	 */
	private void anmeldenMeldungsenpfaenger() {
		if (empfaenger != null) {
			// Anmeldung bereits durchgeführt.
			return;
		}

		empfaenger = new Meldungsempfaenger();
		final ObjektFactory factory = ObjektFactory.getInstanz();

		// Darstellungsparameter
		final BetriebsMeldungsVerwaltung bvBmv = (BetriebsMeldungsVerwaltung) factory
				.getModellobjekt(PID_BITCTRL_BMV);
		if (bvBmv != null) {
			final PdBcBetriebsMeldungDarstellung param = bvBmv
					.getParameterDatensatz(
							PdBcBetriebsMeldungDarstellung.class);
			darstellungsparameter = param.abrufenDatum();
		} else {
			darstellungsparameter = new PdBcBetriebsMeldungDarstellung.Daten();
		}

		// Meldungen aus dem Archiv auslesen
		synchronized (meldungsliste) {
			// Factory wird umgangen, weil dieser Datensatz nur zur
			// Konvertierung verwendet wird und anschließend das Objekt wieder
			// zerstört werden kann.
			final OdBetriebsMeldung datensatz = new OdBetriebsMeldung(null);

			final DataDescription dbs = new DataDescription(
					datensatz.getAttributGruppe(),
					OdBetriebsMeldung.Aspekte.Information.getAspekt());
			final long zeitstempel = factory.getVerbindung().getTime();
			final ArchivIterator iterator = new ArchivIterator(
					factory.getVerbindung(),
					ArchivUtilities.getAnfrage(
							Collections.singletonList(
									factory.getAOE().getSystemObject()),
							dbs,
							new Interval(zeitstempel
									- darstellungsparameter.getMaxHistory(),
									zeitstempel),
							ArchiveDataKind.ONLINE));

			while (iterator.hasNext()) {
				datensatz.setDaten(iterator.next());
				final OdBetriebsMeldung.Daten datum = datensatz.getDatum(
						OdBetriebsMeldung.Aspekte.Information.getAspekt());
				meldungsliste.add(datum);
			}

			cleanUpMeldungen();
		}

		// Als Empfänger für Betriebsmeldungen anmelden
		datensatzBetriebsMeldung = factory.getAOE()
				.getOnlineDatensatz(OdBetriebsMeldung.class);
		datensatzBetriebsMeldung.addUpdateListener(
				OdBetriebsMeldung.Aspekte.Information.getAspekt(), empfaenger);
	}

	/**
	 * Entfernt nicht mehr gültige Meldungen aus der Liste.
	 *
	 * @return die Liste der entfernten Meldungen.
	 */
	private List<OdBetriebsMeldung.Daten> cleanUpMeldungen() {
		final List<OdBetriebsMeldung.Daten> entfernt = new ArrayList<OdBetriebsMeldung.Daten>();
		synchronized (meldungsliste) {
			// Entferne Meldungenm, die zu alte sind.
			final long maxZeitstempel = System.currentTimeMillis()
					- darstellungsparameter.getMaxHistory();
			final Iterator<OdBetriebsMeldung.Daten> iterator = meldungsliste
					.iterator();
			while (iterator.hasNext()) {
				final OdBetriebsMeldung.Daten meldung = iterator.next();
				if (meldung.getZeitstempel() < maxZeitstempel) {
					entfernt.add(meldung);
					iterator.remove();
				} else if (meldung.getDatenStatus() != Datum.Status.DATEN) {
					iterator.remove();
				}
			}

			// Entferne Meldungen, die zuviel sind.
			while (meldungsliste.size() > darstellungsparameter
					.getMaxAnzahl()) {
				entfernt.add(meldungsliste.remove(0));
			}
		}

		return entfernt;
	}

	/**
	 * Registriert einen Listener für Betriebsmeldungen. Die Anmeldung für den
	 * Empfang der Betriebsmeldungen und das Auslesen der letzten Meldungen aus
	 * dem Archiv erfolgt einmalig beim ersten Aufruf der Methode.
	 *
	 * @param l
	 *            ein Listener.
	 */
	public void addBetriebsmeldungListener(final BetriebsmeldungListener l) {
		anmeldenMeldungsenpfaenger();
		listeners.add(BetriebsmeldungListener.class, l);
	}

	/**
	 * Deregistriert einen Listener für Betriebsmeldungen.
	 *
	 * @param l
	 *            ein Listener.
	 */
	public void removeBetriebsmeldungListener(final BetriebsmeldungListener l) {
		listeners.remove(BetriebsmeldungListener.class, l);
	}

	/**
	 * Informiert die angemeldeten Listener über die Änderung der Meldungsliste.
	 *
	 * @param neu
	 *            die Liste der neu hinzugekommenen Meldungen.
	 * @param entfernt
	 *            die Liste der entfernten Meldungen.
	 */
	protected synchronized void fireMeldungslisteChanged(
			final List<OdBetriebsMeldung.Daten> neu,
			final List<OdBetriebsMeldung.Daten> entfernt) {
		final BetriebsmeldungEvent e = new BetriebsmeldungEvent(this, neu,
				entfernt);

		for (final BetriebsmeldungListener l : listeners
				.getListeners(BetriebsmeldungListener.class)) {
			l.meldungslisteChanged(e);
		}
	}

	/**
	 * Fügt einen Befehl der Befehlsliste hinzu.
	 *
	 * @param befehl
	 *            ein Befehl.
	 */
	public void addBefehl(final BetriebsmeldungCommand befehl) {
		befehlsliste.add(befehl);
	}

	/**
	 * Entfernt einen Befehl aus der Befehlsliste.
	 *
	 * @param befehl
	 *            ein Befehl.
	 */
	public void removeBefehl(final BetriebsmeldungCommand befehl) {
		befehlsliste.remove(befehl);
	}

	/**
	 * Fragt, ob die Betriebsmeldungsverwaltung gestartet wurde. Dies ist nur
	 * der Fall, wenn der Datensatz nach der Anmeldung <em>Keine Daten</em> oder
	 * <em>Daten</em> liefert.
	 *
	 * @return {@code true}, wenn Meldungen empfangen werden können.
	 */
	public boolean isBereit() {
		final Status status = datensatzBetriebsMeldung
				.abrufenDatum(OdBetriebsMeldung.Aspekte.Information.getAspekt())
				.getDatenStatus();
		return (status == Status.DATEN) || (status == Status.KEINE_DATEN);
	}

	/**
	 * Gibt die gecachter Meldungen zurück. Die Anmeldung für den Empfang der
	 * Betriebsmeldungen und das Auslesen der letzten Meldungen aus dem Archiv
	 * erfolgt einmalig beim ersten Aufruf der Methode.
	 *
	 *
	 * @return eine unveränderliche Liste der aktuellen Meldungen.
	 */
	public List<OdBetriebsMeldung.Daten> getMeldungsliste() {
		anmeldenMeldungsenpfaenger();
		return Collections.unmodifiableList(meldungsliste);
	}

	/**
	 * Sendet eine Betriebsmeldung.
	 *
	 * @param typ
	 *            der Meldungstyp.
	 * @param mtz
	 *            der Meldungstypzusatz.
	 * @param klasse
	 *            die Meldungsklasse.
	 * @param text
	 *            der Meldungstext.
	 */
	public void sende(final MeldungsTyp typ, final MeldungsTypZusatz mtz,
			final MeldungsKlasse klasse, final String text) {
		getSender().sendMessage(getMessageType(typ),
				getMessageTypeAddOn(mtz.compile()),
				getMessageGrade(klasse), null, new MessageCauser(ObjektFactory
						.getInstanz().getBenutzer().getSystemObject(), "", ""),
				text);
	}

	/**
	 * Sendet eine Betriebsmeldung.
	 *
	 * @param typ
	 *            der Meldungstyp.
	 * @param mtz
	 *            der Meldungstypzusatz.
	 * @param klasse
	 *            die Meldungsklasse.
	 * @param status
	 *            der Meldungsstatus.
	 * @param text
	 *            der Meldungstext.
	 */
	public void sende(final MeldungsTyp typ, final MeldungsTypZusatz mtz,
			final MeldungsKlasse klasse, final MeldungsStatus status,
			final String text) {
		getSender().sendMessage("", getMessageType(typ),
				getMessageTypeAddOn(mtz.compile()), getMessageGrade(klasse),
				getMessageState(status), text);
	}

	/**
	 * Sendet eine Betriebsmeldung.
	 *
	 * @param typ
	 *            der Meldungstyp.
	 * @param mtz
	 *            der Meldungstypzusatz.
	 * @param klasse
	 *            die Meldungsklasse.
	 * @param status
	 *            der Meldungsstatus.
	 * @param referenz
	 *            ein Systemobjekt auf das sich die Meldung bezieht.
	 * @param text
	 *            der Meldungstext.
	 * @param urlasser
	 *            die Urlasserinformation.
	 */
	public void sende(final MeldungsTyp typ, final MeldungsTypZusatz mtz,
			final MeldungsKlasse klasse, final MeldungsStatus status,
			final SystemObjekt referenz, final String text,
			final Urlasser urlasser) {
		getSender().sendMessage("", getMessageType(typ),
				getMessageTypeAddOn(mtz.compile()), getMessageGrade(klasse),
				referenz != null ? referenz.getSystemObject() : null,
				getMessageState(status),
				new MessageCauser(urlasser.getBenutzer().getSystemObject(),
						urlasser.getUrsache(), urlasser.getVeranlasser()),
				text);
	}

	/**
	 * Sendet eine Betriebsmeldung.
	 * <p>
	 * <em>Hinweis:</em> Diese Methode ist nur für das erneute Senden
	 * (Quittieren, Kommentieren oder Wiederholen) einer empfangenen Meldung
	 * gedacht.
	 *
	 * @param meldung
	 *            eine Meldung.
	 */
	public void sende(final OdBetriebsMeldung.Daten meldung) {
		getSender().sendMessage(meldung.getId(),
				getMessageType(meldung.getMeldungsTyp()),
				getMessageTypeAddOn(meldung.getMeldungsTypZusatz()),
				getMessageGrade(meldung.getMeldungsKlasse()),
				meldung.getReferenz() != null
						? meldung.getReferenz().getSystemObject() : null,
				getMessageState(meldung.getMeldungsStatus()),
				new MessageCauser(
						meldung.getUrlasser().getBenutzer().getSystemObject(),
						meldung.getUrlasser().getUrsache(),
						meldung.getUrlasser().getVeranlasser()),
				meldung.getMeldungsText());
	}

	/**
	 * Konvertiert von einem Modellwert in einen Datenverteilerwert.
	 *
	 * @param typ
	 *            ein Meldungstyp im Modell.
	 * @return der Meldungstyp im Datenverteiler.
	 */
	private MessageType getMessageType(final MeldungsTyp typ) {
		switch (typ) {
		case Fach:
			return MessageType.APPLICATION_DOMAIN;
		case System:
			return MessageType.SYSTEM_DOMAIN;
		default:
			log.warning("Unbekannter Meldungstyp: " + typ);
			return MessageType.APPLICATION_DOMAIN;
		}
	}

	/**
	 * Konvertiert von einem Modellwert in einen Datenverteilerwert.
	 *
	 * @param klasse
	 *            eine Meldungsklasse im Modell.
	 * @return die Meldungsklasse im Datenverteiler.
	 */
	private MessageGrade getMessageGrade(final MeldungsKlasse klasse) {
		switch (klasse) {
		case Information:
			return MessageGrade.INFORMATION;
		case Warnung:
			return MessageGrade.WARNING;
		case Fehler:
			return MessageGrade.ERROR;
		case Fatal:
			return MessageGrade.FATAL;
		default:
			log.warning("Unbekannte Meldungsklasse: " + klasse);
			return MessageGrade.INFORMATION;
		}
	}

	/**
	 * Konvertiert von einem Modellwert in einen Datenverteilerwert.
	 *
	 * @param status
	 *            ein Meldungsstatus im Modell.
	 * @return der Meldungsstatus im Datenverteiler.
	 */
	private MessageState getMessageState(final MeldungsStatus status) {
		switch (status) {
		case Meldung:
			return MessageState.MESSAGE;
		case NeueMeldung:
			return MessageState.NEW_MESSAGE;
		case Wiederholungsmeldung:
			return MessageState.REPEAT_MESSAGE;
		case Aenderungsmeldung:
			return MessageState.CHANGE_MESSAGE;
		case Gutmeldung:
			return MessageState.GOOD_MESSAGE;
		default:
			log.warning("Unbekannter Meldungsstatus: " + status);
			return MessageState.MESSAGE;
		}
	}

	/**
	 * Generiert falls nötig einen Standardmeldungszusatz.
	 *
	 * @param meldungsTypZusatz
	 *            ein Zusatz, der {@code null} oder ein Leerstring sein kann.
	 * @return ein gültiger Meldungstypzusatz.
	 */
	private String getMessageTypeAddOn(final String meldungsTypZusatz) {
		if ((meldungsTypZusatz == null) || (meldungsTypZusatz.length() == 0)) {
			return LoggerTools.getCallPosition(new Throwable());
		}
		return meldungsTypZusatz;
	}

	/**
	 * Gibt den Sender von Betriebsmeldungen zurück. Die Methode ist identisch
	 * mit dem Aufruf von {@link MessageSender#getInstance()}. Diese Methode
	 * dient lediglich dazu, dass in Klassen nicht zwei
	 * Betriebsmeldungsverwaltungen verwendet werden müssen.
	 *
	 * @return der Sender.
	 */
	public MessageSender getSender() {
		return MessageSender.getInstance();
	}

	/**
	 * Gibt die aktuellen Darstellungsparameter für Betriebsmeldungen zurück.
	 *
	 * @return die Darstellungsparameter.
	 */
	public PdBcBetriebsMeldungDarstellung.Daten getDarstellungsparameter() {
		final ObjektFactory factory = ObjektFactory.getInstanz();
		final BcBetriebsMeldungsVerwaltung bvBmv = (BcBetriebsMeldungsVerwaltung) factory
				.getModellobjekt(PID_BITCTRL_BMV);
		if (bvBmv != null) {
			final PdBcBetriebsMeldungDarstellung param = bvBmv
					.getParameterDatensatz(
							PdBcBetriebsMeldungDarstellung.class);
			return param.abrufenDatum();
		} else {
			return new PdBcBetriebsMeldungDarstellung.Daten();
		}
	}

}
