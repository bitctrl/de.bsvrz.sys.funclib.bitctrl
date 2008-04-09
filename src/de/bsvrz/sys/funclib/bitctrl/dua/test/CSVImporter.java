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

package de.bsvrz.sys.funclib.bitctrl.dua.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.bitctrl.Constants;

import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Allgemeine Klasse zum Lesen aus CSV-Dateien.
 * 
 * @author BitCtrl Systems GmbH, Thierfelder
 *
 * @version $Id$
 */
public class CSVImporter {

	/**
	 * Debug-Logger.
	 */
	private static final Debug LOGGER = Debug.getLogger();

	/**
	 * CSV-Datei.
	 */
	private File csvDatei = null;

	/**
	 * der Reader für die CSV-Datei.
	 */
	private BufferedReader leser = null;

	/**
	 * die Zeile, auf der im Moment der Datei-Zeier steht.
	 */
	private int zeilenNummer = -1;

	/**
	 * Standardkonstruktor Nr.1.
	 * 
	 * @param csvDatei CSV-Datei
	 * @throws Exception wenn die Datei nicht geöffnet werden kann
	 */
	public CSVImporter(final File csvDatei) throws Exception {
		this.csvDatei = csvDatei;
		this.leser = new BufferedReader(new FileReader(csvDatei));
	}

	/**
	 * Standardkonstruktor Nr.2.
	 * 
	 * @param csvDateiName Name der CSV-Datei (mit oder ohne Suffix)
	 * @throws Exception wenn die Datei nicht geöffnet werden kann
	 */
	public CSVImporter(final String csvDateiName) throws Exception {
		String name = csvDateiName;
		if (!name.toLowerCase().endsWith(".csv")) { //$NON-NLS-1$
			name += ".csv"; //$NON-NLS-1$
		}
		this.csvDatei = new File(name);
		this.leser = new BufferedReader(new FileReader(csvDatei));
	}

	/**
	 * Gibt alle Spalten einer Zeile der Tabelle als
	 * String-Array zurück.
	 * 
	 * @return ein String-Array mit den Spalten einer Zeile
	 * oder <code>null</code>, wenn das Dateiende erreicht ist
	 */
	public final String[] getNaechsteZeile() {
		String[] result = null;

		try {
			String red = leser.readLine();
			this.zeilenNummer++;

			if (red != null) {
				result = red.split(";"); //$NON-NLS-1$
			}
		} catch (IOException ex) {
			LOGGER.error("Fehler beim Lesen aus " + this, ex); //$NON-NLS-1$
		}

		return result;
	}

	/**
	 * Erfragt die Zeilennummer, auf der im Moment der Datei-Zeiger steht.
	 * 
	 * @return die Zeilennummer, auf der im Moment der Datei-Zeiger steht
	 * <code>-1</code> indiziert, dass noch nicht aus der Datei gelesen wurde
	 */
	public final int getZeilenNummer() {
		return this.zeilenNummer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = csvDatei.toString();

		try {
			s = csvDatei.getCanonicalPath();
		} catch (IOException ex) {
			LOGGER.error(Constants.EMPTY_STRING, ex);
		}

		return s;
	}

	/**
	 * Setzt den Dateizeiger wieder auf Anfang.
	 */
	public final void reset() {
		try {
			this.leser.close();
			this.leser = new BufferedReader(new FileReader(this.csvDatei));
		} catch (IOException ex) {
			LOGGER.error(Constants.EMPTY_STRING, ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void finalize() throws Throwable {
		if (this.leser != null) {
			this.leser.close();
		}
	}

}
