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
 * Wei�enfelser Stra�e 67
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
import de.bsvrz.sys.funclib.bitctrl.modell.AnmeldeException;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateEvent;
import de.bsvrz.sys.funclib.bitctrl.modell.DatensatzUpdateListener;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.VeWBetriebGlobalTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.objekte.BetriebsMeldungsVerwaltung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.BetriebsMeldung;
import de.bsvrz.sys.funclib.bitctrl.modell.vewbetriebglobal.onlinedaten.BetriebsMeldung.Daten;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Hilfsklasse f�r den Umgang mit der Betriebsmeldungsverwaltung. Die Klasse
 * f�hrt eine Liste der letzten Meldungen. Andere Klassen k�nnen sich �hber
 * �nderungen an dieser Liste informieren lassen.
 * <p>
 * <em>Hinweis:</em> Die Klasse ist nur f�r das Empfangen von
 * Betriebsmeldungen zust�ndig. Zum Senden von Betriebsmeldungen steht die
 * Klasse {@link de.bsvrz.sys.funclib.operatingMessage.MessageSender} zur
 * Verf�gung.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 * @see de.bsvrz.sys.funclib.operatingMessage.MessageSender
 */
public final class Betriebsmeldungsverwaltung {

	/**
	 * Der Standardwert f�r das maximale Alter von Betriebsmeldungen, die aus
	 * dem Archiv gelesen werden: {@value}.
	 */
	public static final long DEFAULT_HISTORY = 3 * Constants.MILLIS_PER_DAY;

	/** Der Standardwert f�r die maximale Anzahl gecachter Meldungen: {@value}. */
	public static final int DEFAULT_MAX_ANZAHL = 1000;

	/** Das einzige Objekt dieser Klasse. */
	private static Betriebsmeldungsverwaltung singleton;

	/**
	 * Gibt die Betriebsmeldungsverwaltung als Singleton zur�ck.
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
	 * Empf�ngt die Betriebsmeldungen und cacht diese.
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

	/** Die Betriebsmeldungsverwaltung, die hier verwaltet wird. */
	private BetriebsMeldungsVerwaltung bmv;

	/**
	 * Die Zeit in die Vergangenheit, f�r die Meldungen initial gecacht werden
	 * sollen.
	 */
	private long maxHistory = DEFAULT_HISTORY;

	/** Die maximale Anzahl gecachter Meldungen. */
	private int maxAnzahl = DEFAULT_MAX_ANZAHL;

	/**
	 * Initialisierung.
	 */
	private Betriebsmeldungsverwaltung() {
		log = Debug.getLogger();
		listeners = new EventListenerList();
		meldungsliste = new LinkedList<BetriebsMeldung.Daten>();

		init();
	}

	/**
	 * Sucht eine Applikation <em>Betriebsmeldungsverwaltung</em> im
	 * Datenverteiler.
	 */
	private void findeBmv() {
		if (bmv == null) {
			final ObjektFactory factory = ObjektFactory.getInstanz();
			final List<SystemObjekt> bmvs = factory
					.bestimmeModellobjekte(VeWBetriebGlobalTypen.BetriebsMeldungsVerwaltung
							.getPid());
			if (bmvs.isEmpty()) {
				log
						.warning("Betriebsmeldungsverwaltung wurde nicht gestartet.");
				bmv = null;
			} else {
				if (bmvs.size() > 1) {
					log
							.warning("Es wurde mehr als eine Betriebsmeldungsverwaltung gestartet.");
				}

				bmv = (BetriebsMeldungsVerwaltung) bmvs.get(0);
				final BetriebsMeldung datensatz = bmv
						.getOnlineDatensatz(BetriebsMeldung.class);
				try {
					datensatz.addUpdateListener(
							BetriebsMeldung.Aspekte.Information.getAspekt(),
							new Meldungsempfaenger());
					datensatz
							.anmeldenSender(BetriebsMeldung.Aspekte.Information
									.getAspekt());
				} catch (final AnmeldeException ex) {
					log.error(ex.getLocalizedMessage(), ex);
				}
			}
		}
	}

	/**
	 * Entfernt nicht mehr g�ltige Meldungen aus der Liste.
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
	 * Liest initial archivierte Betriebsmeldungen in den Meldungscache. Aktuell
	 * gecachte Meldungen werden dabei �berschrieben.
	 */
	public void init() {
		final List<BetriebsMeldung.Daten> neu = new ArrayList<Daten>();
		final List<BetriebsMeldung.Daten> entfernt;

		synchronized (meldungsliste) {
			findeBmv();

			if (bmv == null) {
				return;
			}

			// Factory wird umgangen, weil dieser Datensatz nur zur
			// Konvertierung verwendet wird und anschli�end das Objekt wieder
			// zerst�rt werden kann.
			final BetriebsMeldung datensatz = new BetriebsMeldung(bmv);

			final ObjektFactory factory = ObjektFactory.getInstanz();
			final DataDescription dbs = new DataDescription(datensatz
					.getAttributGruppe(), BetriebsMeldung.Aspekte.Information
					.getAspekt());
			final long zeitstempel = factory.getVerbindung().getTime();
			final ArchivIterator iterator = new ArchivIterator(factory
					.getVerbindung(), ArchivUtilities.getAnfrage(Collections
					.singletonList(bmv.getSystemObject()), dbs, new Interval(
					zeitstempel - getMaxHistory(), zeitstempel),
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
	}

	/**
	 * Registriert einen Listener f�r Betriebsmeldungen.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void addBetriebsmeldungListener(final BetriebsmeldungListener l) {
		listeners.add(BetriebsmeldungListener.class, l);
	}

	/**
	 * Deregistriert einen Listener f�r Betriebsmeldungen.
	 * 
	 * @param l
	 *            ein Listener.
	 */
	public void removeBetriebsmeldungListener(final BetriebsmeldungListener l) {
		listeners.remove(BetriebsmeldungListener.class, l);
	}

	/**
	 * Gibt die Zeit in die Vergangenheit zur�ck, f�r die initial archivierte
	 * Meldungen gelesen und gecacht werden.
	 * 
	 * @return das maximale Alter von archivierten Betriebsmeldungen.
	 */
	public long getMaxHistory() {
		return maxHistory;
	}

	/**
	 * Legt die Zeit in die Vergangenheit fest, f�r die initial archivierte
	 * Meldungen gelesen und gecacht werden.
	 * 
	 * @param maxHistory
	 *            das maximale Alter von archivierten Betriebsmeldungen.
	 */
	public void setMaxHistory(final long maxHistory) {
		// Nur der Absolutwert wird �bernommen.
		if (maxHistory >= 0) {
			this.maxHistory = maxHistory;
		} else {
			this.maxHistory = maxHistory * -1;
		}
	}

	/**
	 * Gibt die maximale Anzahl gecachter Meldungen zur�ck.
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
					"Die maximale Anzahl gecachter Betriebsmeldungen muss gr��er 0 sein.");
		}
		this.maxAnzahl = maxAnzahl;
	}

	/**
	 * Gibt die gecachter Meldungen zur�ck.
	 * 
	 * @return eine unver�nderliche Liste der aktuellen Meldungen.
	 */
	public List<BetriebsMeldung.Daten> getMeldungsliste() {
		return Collections.unmodifiableList(meldungsliste);
	}

	/**
	 * Informiert die angemeldeten Listener �ber die �nderung der Meldungsliste.
	 * 
	 * @param neu
	 *            die Liste der neu hinzugekommenen Meldungen.
	 * @param entfernt
	 *            die Liste der entfernten Meldungen.
	 */
	protected void fireMeldungslisteChanged(
			final List<BetriebsMeldung.Daten> neu,
			final List<BetriebsMeldung.Daten> entfernt) {
		final BetriebsmeldungEvent e = new BetriebsmeldungEvent(this, neu,
				entfernt);

		for (final BetriebsmeldungListener l : listeners
				.getListeners(BetriebsmeldungListener.class)) {
			l.meldungslisteChanged(e);
		}
	}

}
