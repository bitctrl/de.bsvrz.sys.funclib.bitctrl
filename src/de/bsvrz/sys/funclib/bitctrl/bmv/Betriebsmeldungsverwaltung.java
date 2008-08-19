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

package de.bsvrz.sys.funclib.bitctrl.bmv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;

import com.bitctrl.Constants;
import com.bitctrl.util.Interval;

import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.archive.ArchiveDataKind;
import de.bsvrz.sys.funclib.bitctrl.archiv.ArchivIterator;
import de.bsvrz.sys.funclib.bitctrl.archiv.ArchivUtilities;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.Datum.Status;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.BetriebsMeldung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.BetriebsMeldung.Daten;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Hilfsklasse für den Umgang mit der Betriebsmeldungsverwaltung. Die Klasse
 * führt eine Liste der letzten Meldungen. Andere Klassen können sich ühber
 * Änderungen an dieser Liste informieren lassen.
 * <p>
 * <em>Hinweis:</em> Die Klasse ist nur für das Empfangen von
 * Betriebsmeldungen zuständig. Zum Senden von Betriebsmeldungen steht die
 * Klasse {@link de.bsvrz.sys.funclib.operatingMessage.MessageSender} zur
 * Verfügung.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 * @see de.bsvrz.sys.funclib.operatingMessage.MessageSender
 */
public final class Betriebsmeldungsverwaltung {

	/**
	 * Der Standardwert für das maximale Alter von Betriebsmeldungen, die aus
	 * dem Archiv gelesen werden: {@value}.
	 */
	public static final long DEFAULT_HISTORY = 3 * Constants.MILLIS_PER_DAY;

	/** Der Standardwert für die maximale Anzahl gecachter Meldungen: {@value}. */
	public static final int DEFAULT_MAX_ANZAHL = 1000;

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

		/**
		 * {@inheritDoc}
		 */
		public void datensatzAktualisiert(final DatensatzUpdateEvent event) {
			final List<BetriebsMeldung.Daten> neu = new ArrayList<Daten>();

			if (event.getDatum() instanceof BetriebsMeldung.Daten) {
				final BetriebsMeldung.Daten datum = (Daten) event.getDatum();
				synchronized (meldungsliste) {
					meldungsliste.add(datum);
					neu.add(datum);

					for (final BetriebsmeldungCommand befehl : befehlsliste) {
						if (befehl.isTrigger(datum)) {
							befehl.exec();
						}
					}
				}
			}

			final List<BetriebsMeldung.Daten> entfernt = cleanUpMeldungen();
			fireMeldungslisteChanged(neu, entfernt);
		}

	}

	/** Der Logger der Klasse. */
	private final Debug log;

	/** Die Liste der angemeldeten Listener. */
	private final EventListenerList listeners;

	/** Die Liste der gecachten Meldungen. */
	private final List<BetriebsMeldung.Daten> meldungsliste;

	/** Liste er Befehle die beim Meldungsempfang verarbeitet werden. */
	private final List<BetriebsmeldungCommand> befehlsliste;

	/**
	 * Die Zeit in die Vergangenheit, für die Meldungen initial gecacht werden
	 * sollen.
	 */
	private long maxHistory = DEFAULT_HISTORY;

	/** Die maximale Anzahl gecachter Meldungen. */
	private int maxAnzahl = DEFAULT_MAX_ANZAHL;

	/** Der Datensatz mit dem die Meldungen empfangen werden. */
	private final BetriebsMeldung datensatzBetriebsMeldung;

	/**
	 * Liest initial die letzten Betriebsmeldungen aus dem Archiv und cacht
	 * diese.
	 */
	private Betriebsmeldungsverwaltung() {
		log = Debug.getLogger();
		listeners = new EventListenerList();
		meldungsliste = new LinkedList<BetriebsMeldung.Daten>();
		befehlsliste = new ArrayList<BetriebsmeldungCommand>();

		final List<BetriebsMeldung.Daten> neu = new ArrayList<Daten>();
		final List<BetriebsMeldung.Daten> entfernt;
		final ObjektFactory factory = ObjektFactory.getInstanz();

		synchronized (meldungsliste) {

			// Factory wird umgangen, weil dieser Datensatz nur zur
			// Konvertierung verwendet wird und anschlißend das Objekt wieder
			// zerstört werden kann.
			final BetriebsMeldung datensatz = new BetriebsMeldung(factory
					.getAOE());

			final DataDescription dbs = new DataDescription(datensatz
					.getAttributGruppe(), BetriebsMeldung.Aspekte.Information
					.getAspekt());
			final long zeitstempel = factory.getVerbindung().getTime();
			final ArchivIterator iterator = new ArchivIterator(factory
					.getVerbindung(), ArchivUtilities.getAnfrage(Collections
					.singletonList(factory.getAOE().getSystemObject()), dbs,
					new Interval(zeitstempel - getMaxHistory(), zeitstempel),
					ArchiveDataKind.ONLINE));

			meldungsliste.clear();
			while (iterator.hasNext()) {
				datensatz.setDaten(iterator.next());
				final Daten datum = datensatz
						.getDatum(BetriebsMeldung.Aspekte.Information
								.getAspekt());
				meldungsliste.add(datum);
				neu.add(datum);
			}
			entfernt = cleanUpMeldungen();
		}

		fireMeldungslisteChanged(neu, entfernt);

		datensatzBetriebsMeldung = factory.getAOE().getOnlineDatensatz(
				BetriebsMeldung.class);
		datensatzBetriebsMeldung.addUpdateListener(
				BetriebsMeldung.Aspekte.Information.getAspekt(),
				new Meldungsempfaenger());

		log.info("Betriebsmeldungsverwaltung bereit.");
	}

	/**
	 * Entfernt nicht mehr gültige Meldungen aus der Liste.
	 * 
	 * @return die Liste der entfernten Meldungen.
	 * @see #getMaxHistory()
	 * @see #getMaxAnzahl()
	 */
	private List<BetriebsMeldung.Daten> cleanUpMeldungen() {
		final List<BetriebsMeldung.Daten> entfernt = new ArrayList<Daten>();
		synchronized (meldungsliste) {
			// Entferne Meldungenm, die zu alte sind.
			final long maxZeitstempel = System.currentTimeMillis()
					- getMaxHistory();
			final Iterator<BetriebsMeldung.Daten> iterator = meldungsliste
					.iterator();
			while (iterator.hasNext()) {
				final BetriebsMeldung.Daten meldung = iterator.next();
				if (meldung.getZeitstempel() < maxZeitstempel) {
					entfernt.add(meldung);
					iterator.remove();
				}
			}

			// Entferne Meldungen, die zuviel sind.
			while (meldungsliste.size() > getMaxAnzahl()) {
				entfernt.add(meldungsliste.remove(0));
			}
		}
		return entfernt;
	}

	/**
	 * Registriert einen Listener für Betriebsmeldungen.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void addBetriebsmeldungListener(final BetriebsmeldungListener l) {
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
			final List<BetriebsMeldung.Daten> neu,
			final List<BetriebsMeldung.Daten> entfernt) {
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
	 * der Fall, wenn der Datensatz nach der Anmeldung <em>Keine Daten</em>
	 * oder <em>Daten</em> liefert.
	 * 
	 * @return {@code true}, wenn Meldungen empfangen werden können.
	 */
	public boolean isBereit() {
		final Status status = datensatzBetriebsMeldung.abrufenDatum(
				BetriebsMeldung.Aspekte.Information.getAspekt())
				.getDatenStatus();
		return status == Status.DATEN || status == Status.KEINE_DATEN;
	}

	/**
	 * Gibt die Zeit in die Vergangenheit zurück, für die initial archivierte
	 * Meldungen gelesen und gecacht werden.
	 * 
	 * @return das maximale Alter von archivierten Betriebsmeldungen.
	 */
	public long getMaxHistory() {
		return maxHistory;
	}

	/**
	 * Legt die Zeit in die Vergangenheit fest, für die initial archivierte
	 * Meldungen gelesen und gecacht werden.
	 * 
	 * @param maxHistory
	 *            das maximale Alter von archivierten Betriebsmeldungen.
	 */
	public void setMaxHistory(final long maxHistory) {
		// Nur der Absolutwert wird übernommen.
		if (maxHistory >= 0) {
			this.maxHistory = maxHistory;
		} else {
			this.maxHistory = maxHistory * -1;
		}
	}

	/**
	 * Gibt die maximale Anzahl gecachter Meldungen zurück.
	 * 
	 * @return die Maximalanzahl gecachter Meldungen.
	 */
	public int getMaxAnzahl() {
		return maxAnzahl;
	}

	/**
	 * Legt die maximale Anzahl gecachter Meldungen fest.
	 * 
	 * @param maxAnzahl
	 *            die Maximalanzahl gecachter Meldungen.
	 */
	public void setMaxAnzahl(final int maxAnzahl) {
		if (maxAnzahl <= 0) {
			throw new IllegalArgumentException(
					"Die maximale Anzahl gecachter Betriebsmeldungen muss größer 0 sein.");
		}
		this.maxAnzahl = maxAnzahl;
	}

	/**
	 * Gibt die gecachter Meldungen zurück.
	 * 
	 * @return eine unveränderliche Liste der aktuellen Meldungen.
	 */
	public List<BetriebsMeldung.Daten> getMeldungsliste() {
		return Collections.unmodifiableList(meldungsliste);
	}

}
