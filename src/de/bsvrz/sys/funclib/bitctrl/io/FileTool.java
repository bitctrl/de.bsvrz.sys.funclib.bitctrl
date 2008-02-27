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

package de.bsvrz.sys.funclib.bitctrl.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

/**
 * Allgemeine Funktionen zur Arbeit mit Dateien.
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public final class FileTool {

	/**
	 * kopiert die angegebene Datei.
	 * 
	 * @param datei
	 *            die Datei, die kopiert werden soll
	 * @param ziel
	 *            die Zieldatei
	 * @return <i>true</i>, wenn der Kopiervorgang erfolgreich war.
	 */
	private static boolean kopiereFile(File datei, File ziel) {
		boolean result = false;

		if (datei.isFile()) {
			try {
				FileReader input = new FileReader(datei);
				try {
					FileWriter output = new FileWriter(ziel);
					do {
						int ch = input.read();
						if (ch < 0) {
							break;
						}
						output.write(ch);
					} while (true);
					output.flush();
					output.close();
					result = true;
				} catch (IOException e) {
					loescheVerzeichnis(ziel);
				}
				try {
					input.close();
				} catch (IOException e) {
					// Fehler beim Schliessen der Quelle wird vernachlässigt
				}
			} catch (FileNotFoundException e) {
				// Quelle konnte nicht gelesen werden
			}
		}

		return result;
	}

	/**
	 * kopiert das angegebene Verzeichnis/Datei rekursiv.
	 * 
	 * @param verzeichnis
	 *            das Verzeichnis
	 * @param ziel
	 *            das Zielverzeichnis
	 * @return <i>true</i>, wenn der Kopiervorgang erfolgreich war.
	 */
	public static boolean kopiereVerzeichnis(final File verzeichnis,
			final File ziel) {

		boolean result = false;

		if (ziel.exists()) {
			if (!loescheVerzeichnis(ziel)) {
				result = false;
			} else {
				result = true;
			}
		} else {
			result = true;
		}

		if (!verzeichnis.isDirectory()) {
			return kopiereFile(verzeichnis, ziel);
		}

		if (result) {
			if (ziel.mkdirs()) {
				for (File file : verzeichnis.listFiles()) {
					if (!kopiereVerzeichnis(new File(verzeichnis, file
							.getName()), new File(ziel, file.getName()))) {
						result = false;
						break;
					}
				}
			} else {
				result = false;
			}
		}

		if (!result) {
			loescheVerzeichnis(ziel);
		}

		return result;
	}

	/**
	 * loescht die angegebene Datei/Verzeichnis rekursiv.
	 * 
	 * @param verzeichnis
	 *            das Verzeichnis
	 * @return <i>true</i>, wenn der Löschvorgang erfolgreich ausgeführt werden
	 *         konnte
	 */
	public static boolean loescheVerzeichnis(final File verzeichnis) {

		boolean result = true;

		if (verzeichnis.isDirectory()) {
			for (File file : verzeichnis.listFiles()) {
				if (file.isDirectory()) {
					if (!loescheVerzeichnis(file)) {
						result = false;
						break;
					}
				} else {
					result = file.delete();
				}
			}
			if (result) {
				result = verzeichnis.delete();
			}
		} else {
			result = verzeichnis.delete();
		}

		return result;
	}

	/**
	 * Testappliation.
	 * 
	 * @param args
	 *            die Argumente
	 */
	public static void main(String[] args) {
		ThreadInfo[] infos = ManagementFactory.getThreadMXBean()
				.dumpAllThreads(true, true);
		for (ThreadInfo info : infos) {
			System.err.println(info.getThreadName()
					+ ": "
					+ ManagementFactory.getThreadMXBean().getThreadCpuTime(
							info.getThreadId()) / 1000000 + " ms");
		}
		FileTool.kopiereVerzeichnis(new File("H:/TEMP/Neuer Ordner"), new File(
				"H:/TEMP/TESTKOPIE"));

		for (ThreadInfo info : infos) {
			System.err.println(info.getThreadName()
					+ ": "
					+ ManagementFactory.getThreadMXBean().getThreadCpuTime(
							info.getThreadId()) / 1000000 + " ms");
		}

	}

	/**
	 * Provater Konstruktor.
	 */
	private FileTool() {
		// Konstruktor wird versteckt
	}

}
