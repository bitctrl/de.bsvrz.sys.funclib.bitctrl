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

package de.bsvrz.sys.funclib.bitctrl.test.datagen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.pat.onlprot.standardProtocolModule.ClientProtocollerInterface;
import de.bsvrz.pat.onlprot.standardProtocolModule.StandardProtocoller;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Das Modul zum Erzeugen der XML-Datei für den Datengenerator der Kernsoftware.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
class GeneriereAusgabeDateiModul extends DatenGeneratorModul {

	/**
	 * die Liste der zu verwendeten Datenquellen.
	 */
	private final SortedMap<Long, Set<DatenQuelle>> quellListe = new TreeMap<Long, Set<DatenQuelle>>();

	/**
	 * die Startzeit für die Datenausgabe.
	 */
	private long startZeit;

	/**
	 * das verwendete Protokollmodul.
	 */
	private ClientProtocollerInterface protokoller;

	/**
	 * erzeugt eine Instanz des Moduls.
	 * 
	 * @param connection
	 *            die Datenverteilerverbindung
	 * @param argumente
	 *            die Argumente des Moduls
	 */
	GeneriereAusgabeDateiModul(final ClientDavInterface connection,
			final Map<String, String> argumente) {
		super(connection, argumente);
	}

	/**
	 * {@inheritDoc}.
	 * 
	 * @see de.bsvrz.sys.funclib.bitctrl.test.datagen.DatenGeneratorModul#ausfuehren()
	 */
	@Override
	protected int ausfuehren() {
		startZeit = System.currentTimeMillis();
		String startZeitStr = getArgumente().get("-start");
		if (startZeitStr != null) {
			try {
				startZeit = DateFormat.getDateTimeInstance()
						.parse(startZeitStr).getTime();
			} catch (ParseException e) {
				System.err
						.println("Startzeit: \""
								+ startZeitStr
								+ "\" konnte nicht interpretiert werden, verwende aktuelle Zeit");
			}
		}

		String quellenNamen = getArgumente().get("-input");
		String zielName = getArgumente().get("-output");
		StringTokenizer tokenizer = new StringTokenizer(quellenNamen, ",");
		while (tokenizer.hasMoreElements()) {
			DatenQuelle quelle = new DatenQuelle(getConnection(), tokenizer
					.nextToken().trim());
			long nextStart = quelle.getNextStart(-1L);
			if (nextStart >= 0) {
				Set<DatenQuelle> set = quellListe.get(nextStart);
				if (set == null) {
					set = new HashSet<DatenQuelle>();
					quellListe.put(nextStart, set);
				}
				set.add(quelle);
			}
		}

		protokoller = erzeugeProtokoller(zielName);

		Collection<ResultData> daten = new ArrayList<ResultData>();
		while (quellListe.size() > 0) {
			long offset = quellListe.firstKey();

			Set<DatenQuelle> set = quellListe.get(offset);
			daten.clear();
			for (DatenQuelle quelle : set) {

				daten.addAll(quelle.getAusgabeDaten(startZeit, offset));
				long nextStart = quelle.getNextStart(offset);
				if (nextStart >= 0) {
					Set<DatenQuelle> newSet = quellListe.get(nextStart);
					if (newSet == null) {
						newSet = new HashSet<DatenQuelle>();
						quellListe.put(nextStart, newSet);
					}
					newSet.add(quelle);
				}
			}

			protokoller.update(daten.toArray(new ResultData[daten.size()]));
			quellListe.remove(offset);
		}

		protokoller.writeFooter();

		return 0;
	}

	/**
	 * erzeugt das Ausgabemodul zur Formatierung der Daten.
	 * 
	 * @param zielname
	 *            der Name der Zieldatei
	 * @return der Protokollierer
	 */
	private ClientProtocollerInterface erzeugeProtokoller(final String zielname) {
		PrintWriter writer = null;
		try {
			if (zielname == null) {
				writer = new PrintWriter(System.err, true);
			} else {
				writer = new PrintWriter(new FileOutputStream(
						new File(zielname)), true);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Collection<String> genArgs = new ArrayList<String>();

		for (Set<DatenQuelle> quellSet : quellListe.values()) {
			for (DatenQuelle quelle : quellSet) {
				String rolle = quelle.getRolle();
				if (rolle == null) {
					rolle = "quelle";
				}
				genArgs.add("-rolle=" + rolle);

				StringBuffer objArg = new StringBuffer();
				objArg.append("-objekte=");
				for (SystemObject obj : quelle.getObjekte()) {
					if (!objArg.toString().equals("-objekte=")) {
						objArg.append(",");
					}
					objArg.append(obj.getId());
				}
				genArgs.add(objArg.toString());
				genArgs.add("-daten=" + quelle.getDatenBeschreibung());
			}
		}

		return (ClientProtocollerInterface) new StandardProtocoller()
				.initProtocol(
						new ArgumentList(new String[] { "-ausgabe=xml" }),
						writer, genArgs.toArray(new String[genArgs.size()]));

	}
}
