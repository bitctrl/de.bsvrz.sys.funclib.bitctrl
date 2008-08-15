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

package de.bsvrz.sys.funclib.bitctrl.archive;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.bitctrl.util.FieldIterator;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.archive.ArchiveData;
import de.bsvrz.dav.daf.main.archive.ArchiveDataKind;
import de.bsvrz.dav.daf.main.archive.ArchiveDataQueryResult;
import de.bsvrz.dav.daf.main.archive.ArchiveDataSpecification;
import de.bsvrz.dav.daf.main.archive.ArchiveDataStream;
import de.bsvrz.dav.daf.main.archive.ArchiveQueryPriority;
import de.bsvrz.dav.daf.main.archive.ArchiveRequestManager;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Der Archiviterator erleichtert die Iteration �ber die Ergebnisse einer
 * Archivabfrage.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class ArchivIterator implements Iterator<ResultData> {

	/** Der Logger der Klasse. */
	private final Debug log = Debug.getLogger();

	/** Die Archivdatenstr�me. */
	private final Iterator<ArchiveDataStream> stroeme;

	/** Der aktuelle Archivdatenstrom. */
	private ArchiveDataStream strom;

	/** Der aktuelle Archivdatensatz. */
	private ArchiveData archivdaten;

	/**
	 * F�hrt die Archivanfrage durch und initialisiert den Iterator.
	 * 
	 * @param dav
	 *            eine Datenverteilerverbindung.
	 * @param specs
	 *            die Spezifikation der Archivanfragen.
	 */
	public ArchivIterator(final ClientDavInterface dav,
			final List<ArchiveDataSpecification> specs) {
		final ArchiveRequestManager archiv = dav.getArchive();

		if (!archiv.isArchiveAvailable()) {
			stroeme = null;
			log.warning("Das Archiv steht nicht zur Verf�gung.");
		} else {
			final ArchiveDataQueryResult antwort = archiv.request(
					ArchiveQueryPriority.MEDIUM, specs);

			// Alle Str�me abrufen
			try {
				stroeme = new FieldIterator<ArchiveDataStream>(antwort
						.getStreams());
			} catch (final IllegalStateException ex) {
				throw new ArchivException(
						"Fehler beim Empfang des Archivdatenstroms", ex);
			} catch (final InterruptedException ex) {
				throw new ArchivException(
						"Fehler beim Empfang des Archivdatenstroms", ex);
			}

			// Den ersten Archivdatensatz abrufen
			if (stroeme.hasNext()) {
				strom = stroeme.next();
			}
			fetch();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		return archivdaten != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ResultData next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		final boolean delayed = archivdaten.getDataKind().equals(
				ArchiveDataKind.ONLINE_DELAYED)
				|| archivdaten.getDataKind().equals(
						ArchiveDataKind.REQUESTED_DELAYED);
		final Data daten = archivdaten.getData();
		final ResultData datensatz = new ResultData(archivdaten.getObject(),
				archivdaten.getDataDescription(), delayed, archivdaten
						.getDataIndex(), archivdaten.getDataTime(),
				(byte) (archivdaten.getDataType().getCode() - 1), daten);

		// Den n�chsten Archivdatensatz abrufen
		fetch();

		return datensatz;
	}

	/**
	 * Wird nicht unterst�tzt.
	 * 
	 * {@inheritDoc}
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ruft den n�chsten Archivdatensatz ab.
	 */
	private void fetch() {
		if (strom == null) {
			return;
		}

		while (true) {
			try {
				archivdaten = strom.take();
			} catch (final IllegalStateException ex) {
				throw new ArchivException(
						"Fehler beim Empfang eines Archivdatensatzes", ex);
			} catch (final InterruptedException ex) {
				throw new ArchivException(
						"Fehler beim Empfang eines Archivdatensatzes", ex);
			} catch (final IOException ex) {
				throw new ArchivException(
						"Fehler beim Empfang eines Archivdatensatzes", ex);
			}

			if (archivdaten != null) {
				// N�chsten Archivdatensatz empfangen
				break;
			} else if (stroeme.hasNext()) {
				// Pr�fe den n�chsten Strom auf Daten
				strom = stroeme.next();
			} else {
				// Kein weiterer Archivdatensatz verf�gbar
				break;
			}
		}
	}

}
