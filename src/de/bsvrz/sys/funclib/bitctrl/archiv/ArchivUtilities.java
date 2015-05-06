/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2009 BitCtrl Systems GmbH 
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.bitctrl.util.Interval;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.DataDescription;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.archive.ArchiveDataKind;
import de.bsvrz.dav.daf.main.archive.ArchiveDataKindCombination;
import de.bsvrz.dav.daf.main.archive.ArchiveDataSpecification;
import de.bsvrz.dav.daf.main.archive.ArchiveOrder;
import de.bsvrz.dav.daf.main.archive.ArchiveRequestOption;
import de.bsvrz.dav.daf.main.archive.ArchiveTimeSpecification;
import de.bsvrz.dav.daf.main.archive.TimingType;
import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Diverse Hilfsmethoden für Archivanfragen.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public final class ArchivUtilities {

	/**
	 * Ruft Archivdaten in einen Rutsch ab. Diese Methode sollte nur verwendet
	 * werden, wenn die zu erwartenden Liste der Archivdaten nicht zu groß ist.
	 * 
	 * <p>
	 * <em>Hinweis:</em> Diese Methode sollte nur für Anfragen benutzt werden,
	 * die relativ kleine Datenmengen abfragen, da die Abfrage sonst sehr lange
	 * dauern oder gar fehlschlagen kann. Besser ist es den
	 * {@link ArchivIterator} zu verwenden.
	 * 
	 * @param dav
	 *            eine Datenverteilerverbindung.
	 * @param objekte
	 *            die Objekte, dessen Archivdaten abgefragt werden sollen.
	 * @param dbs
	 *            die Datenbeschreibung der Archivdaten.
	 * @param intervall
	 *            das Zeitintervall der Archivanfrage.
	 * @param nurAenderungen
	 *            <code>true</code>, wenn nur geänderten Datensätze zurückgeben
	 *            werden sollen. Aufeinanderfolgende identische Datensätze
	 *            werden hierbei zu einem Datensatz zusammengefasst.
	 * @param dataKinds
	 *            die gewünschten Datensatzarten. Wenn nicht angegeben, werden
	 *            nur Onlinedaten abgefragt.
	 * @return die Liste der Archivdaten.
	 * @see ArchivIterator
	 * @see #getAnfrage(Collection, DataDescription, Interval, boolean,
	 *      ArchiveDataKind...)
	 */
	public static List<ResultData> getArchivdaten(final ClientDavInterface dav,
			final Collection<? extends SystemObject> objekte,
			final DataDescription dbs, final Interval intervall,
			final boolean nurAenderungen, final ArchiveDataKind... dataKinds) {
		final Iterator<ResultData> iterator = new ArchivIterator(dav,
				getAnfrage(objekte, dbs, intervall, nurAenderungen, dataKinds));
		final List<ResultData> liste = new ArrayList<ResultData>();

		while (iterator.hasNext()) {
			liste.add(iterator.next());
		}

		return liste;
	}

	/**
	 * Liefert eine beliebige Anzahl an Archivdatensätzen vor einem definierten
	 * Zeitpunkt.
	 * 
	 * <p>
	 * <em>Hinweis:</em> Diese Methode sollte nur für Anfragen benutzt werden,
	 * die relativ kleine Datenmengen abfragen, da die Abfrage sonst sehr lange
	 * dauern oder gar fehlschlagen kann. Besser ist es den
	 * {@link ArchivIterator} zu verwenden.
	 * 
	 * @param dav
	 *            eine Datenverteilerverbindung.
	 * @param objekte
	 *            die Objekte, dessen Archivdaten abgefragt werden sollen.
	 * @param dbs
	 *            die Datenbeschreibung der Archivdaten.
	 * @param zeitstempel
	 *            der Zeitpunkt vor dem die Datensätze liegen sollen.
	 * @param anzahlDatensaetze
	 *            die Anzahl der gewünschten Datensätze.
	 * @param nurAenderungen
	 *            <code>true</code>, wenn nur geänderten Datensätze zurückgeben
	 *            werden sollen. Aufeinanderfolgende identische Datensätze
	 *            werden hierbei zu einem Datensatz zusammengefasst.
	 * @param dataKinds
	 *            die gewünschten Datensatzarten. Wenn nicht angegeben, werden
	 *            nur Onlinedaten abgefragt.
	 * @return die Liste der Archivdaten.
	 * @see ArchivIterator
	 * @see #getAnfrage(Collection, DataDescription, long, int, boolean,
	 *      ArchiveDataKind...)
	 */
	public static List<ResultData> getArchivdaten(final ClientDavInterface dav,
			final Collection<? extends SystemObject> objekte,
			final DataDescription dbs, final long zeitstempel,
			final int anzahlDatensaetze, final boolean nurAenderungen,
			final ArchiveDataKind... dataKinds) {
		final Iterator<ResultData> iterator = new ArchivIterator(dav,
				getAnfrage(objekte, dbs, zeitstempel, anzahlDatensaetze,
						nurAenderungen, dataKinds));
		final List<ResultData> liste = new ArrayList<ResultData>();

		while (iterator.hasNext()) {
			liste.add(iterator.next());
		}

		return liste;
	}

	/**
	 * Erzeugt aus den Parametern eine äquivalente Archivanfrage für einen
	 * Zeitraum.
	 * 
	 * @param objekte
	 *            die Objekte, dessen Archivdaten abgefragt werden sollen.
	 * @param dbs
	 *            die Datenbeschreibung der Archivdaten.
	 * @param intervall
	 *            das Zeitintervall der Archivanfrage.
	 * @param nurAenderungen
	 *            <code>true</code>, wenn nur geänderten Datensätze zurückgeben
	 *            werden sollen. Aufeinanderfolgende identische Datensätze
	 *            werden hierbei zu einem Datensatz zusammengefasst.
	 * @param dataKinds
	 *            die gewünschten Datensatzarten. Wenn nicht angegeben, werden
	 *            nur Onlinedaten abgefragt.
	 * @return die Liste der Archivanfragen.
	 * @see ArchivIterator
	 */
	public static List<ArchiveDataSpecification> getAnfrage(
			final Collection<? extends SystemObject> objekte,
			final DataDescription dbs, final Interval intervall,
			final boolean nurAenderungen, final ArchiveDataKind... dataKinds) {
		final ArchiveTimeSpecification timeSpec = new ArchiveTimeSpecification(
				TimingType.DATA_TIME, false, intervall.getStart(), intervall
						.getEnd());
		final List<ArchiveDataSpecification> specs = new ArrayList<ArchiveDataSpecification>();

		for (final SystemObject so : objekte) {
			specs.add(new ArchiveDataSpecification(timeSpec,
					createArchiveDataKindCombination(dataKinds),
					ArchiveOrder.BY_DATA_TIME,
					nurAenderungen ? ArchiveRequestOption.DELTA
							: ArchiveRequestOption.NORMAL, dbs, so));
		}

		return specs;
	}

	/**
	 * Erzeugt aus den Parametern eine äquivalente Archivanfrage für eine
	 * bestimmte Anzahl Datensätze vor einem Endzeitpunkt.
	 * 
	 * @param objekte
	 *            die Objekte, dessen Archivdaten abgefragt werden sollen.
	 * @param dbs
	 *            die Datenbeschreibung der Archivdaten.
	 * @param zeitstempel
	 *            der Zeitpunkt vor dem die Datensätze liegen sollen.
	 * @param anzahlDatensaetze
	 *            die Anzahl der gewünschten Datensätze.
	 * @param nurAenderungen
	 *            <code>true</code>, wenn nur geänderten Datensätze zurückgeben
	 *            werden sollen. Aufeinanderfolgende identische Datensätze
	 *            werden hierbei zu einem Datensatz zusammengefasst.
	 * @param dataKinds
	 *            die gewünschten Datensatzarten. Wenn nicht angegeben, werden
	 *            nur Onlinedaten abgefragt.
	 * @return die Liste der Archivanfragen.
	 * @see ArchivIterator
	 */
	public static List<ArchiveDataSpecification> getAnfrage(
			final Collection<? extends SystemObject> objekte,
			final DataDescription dbs, final long zeitstempel,
			final int anzahlDatensaetze, final boolean nurAenderungen,
			final ArchiveDataKind... dataKinds) {
		final ArchiveTimeSpecification timeSpec = new ArchiveTimeSpecification(
				TimingType.DATA_TIME, true, anzahlDatensaetze, zeitstempel);
		final List<ArchiveDataSpecification> specs = new ArrayList<ArchiveDataSpecification>();

		for (final SystemObject so : objekte) {
			specs.add(new ArchiveDataSpecification(timeSpec,
					createArchiveDataKindCombination(dataKinds),
					ArchiveOrder.BY_DATA_TIME,
					nurAenderungen ? ArchiveRequestOption.DELTA
							: ArchiveRequestOption.NORMAL, dbs, so));
		}

		return specs;

	}

	private static ArchiveDataKindCombination createArchiveDataKindCombination(
			final ArchiveDataKind... dataKinds) {
		final ArchiveDataKindCombination dataKindComb;

		switch (dataKinds.length) {
		case 1:
			dataKindComb = new ArchiveDataKindCombination(dataKinds[0]);
			break;
		case 2:
			dataKindComb = new ArchiveDataKindCombination(dataKinds[0],
					dataKinds[1]);
			break;
		case 3:
			dataKindComb = new ArchiveDataKindCombination(dataKinds[0],
					dataKinds[1], dataKinds[2]);
			break;
		case 4:
			dataKindComb = new ArchiveDataKindCombination(dataKinds[0],
					dataKinds[1], dataKinds[2], dataKinds[3]);
			break;
		default:
			dataKindComb = new ArchiveDataKindCombination(
					ArchiveDataKind.ONLINE);
		}
		return dataKindComb;
	}

	private ArchivUtilities() {
		// Keine Instanzen von Utility-Klassen erlaubt.
	}

}
