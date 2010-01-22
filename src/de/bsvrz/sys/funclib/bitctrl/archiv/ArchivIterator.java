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

package de.bsvrz.sys.funclib.bitctrl.archiv;

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

/**
 * Der Archiviterator erleichtert die Iteration über die Ergebnisse einer
 * Archivabfrage.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class ArchivIterator implements Iterator<ResultData> {

	private final Iterator<ArchiveDataStream> streams;

	private ArchiveDataStream currentStream;

	private ArchiveData currentData;

	/**
	 * Führt die Archivanfrage durch und initialisiert den Iterator.
	 * 
	 * @param dav
	 *            eine Datenverteilerverbindung.
	 * @param specs
	 *            die Spezifikation der Archivanfragen.
	 * @throws ArchivException
	 *             wenn die ein Fehler bei der Archivanfrage passiert erst.
	 */
	public ArchivIterator(final ClientDavInterface dav,
			final List<ArchiveDataSpecification> specs) {
		final ArchiveRequestManager archiv = dav.getArchive();

		if (!archiv.isArchiveAvailable()) {
			streams = null;
			throw new ArchivException("Das Archiv steht nicht zur Verfügung.");
		}

		final ArchiveDataQueryResult antwort = archiv.request(
				ArchiveQueryPriority.MEDIUM, specs);

		// Alle Ströme abrufen
		try {
			streams = new FieldIterator<ArchiveDataStream>(antwort.getStreams());
		} catch (final IllegalStateException ex) {
			throw new ArchivException(
					"Fehler beim Empfang des Archivdatenstroms", ex);
		} catch (final InterruptedException ex) {
			throw new ArchivException(
					"Fehler beim Empfang des Archivdatenstroms", ex);
		}

		// Den ersten Archivdatensatz abrufen
		if (streams.hasNext()) {
			currentStream = streams.next();
		}
		fetchNextData();
	}

	public boolean hasNext() {
		return currentData != null;
	}

	/**
	 * @throws ArchivException
	 *             wenn die ein Fehler bei der Archivanfrage passiert erst.
	 */
	public ResultData next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}

		final boolean delayed = currentData.getDataKind().equals(
				ArchiveDataKind.ONLINE_DELAYED)
				|| currentData.getDataKind().equals(
						ArchiveDataKind.REQUESTED_DELAYED);
		final Data daten = currentData.getData();
		final ResultData datensatz = new ResultData(currentData.getObject(),
				currentData.getDataDescription(), delayed, currentData
						.getDataIndex(), currentData.getDataTime(),
				(byte) (currentData.getDataType().getCode() - 1), daten);

		// Den nächsten Archivdatensatz abrufen
		fetchNextData();

		return datensatz;
	}

	/**
	 * Wird nicht unterstützt.
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	private void fetchNextData() {
		if (currentStream == null) {
			return;
		}

		while (true) {
			try {
				currentData = currentStream.take();
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

			if (currentData != null) {
				// Nächsten Archivdatensatz empfangen
				break;
			} else if (streams.hasNext()) {
				// Prüfe den nächsten Strom auf Daten
				currentStream = streams.next();
			} else {
				// Kein weiterer Archivdatensatz verfügbar
				break;
			}
		}
	}

}
