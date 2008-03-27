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

package de.bsvrz.sys.funclib.bitctrl.test;

import java.util.List;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.bitctrl.modell.ObjektFactory;
import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.VerkehrsModellTypen;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.InneresStrassenSegment;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.MessQuerschnittAllgemein;
import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Exportiert die Koordinaten der definierten MQ.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 * 
 */
public class MqConfigTest implements StandardApplication {

	/**
	 * Hauptfunktion der Anwendung.
	 * 
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new MqConfigTest(), args);
	}

	/**
	 * Ausgabe einer Zahl im Excelformat, d.h. mit Komma als Dezimalzeichen.
	 * 
	 * @param x
	 *            die Zahl
	 * @return der Ausgabestring
	 */
	private String excelZahl(final double x) {
		String result = Double.toString(x);
		result = result.replace('.', ',');
		return result;
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#initialize(de.bsvrz.dav.daf.main.ClientDavInterface)
	 */
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		ObjektFactory.getInstanz().setVerbindung(connection);
		ObjektFactory.getInstanz().registerStandardFactories();

		long start = System.currentTimeMillis();

		List<SystemObjekt> mqs = ObjektFactory.getInstanz()
				.bestimmeModellobjekte(
						VerkehrsModellTypen.MESSQUERSCHNITTALLGEMEIN.getPid());

		List<SystemObjekt> strSeg = ObjektFactory.getInstanz()
				.bestimmeModellobjekte(
						VerkehrsModellTypen.INNERES_STRASSENSEGMENT.getPid());

		for (SystemObjekt obj : mqs) {
			MessQuerschnittAllgemein mq = (MessQuerschnittAllgemein) obj;
			if (mq.getLinie() instanceof InneresStrassenSegment) {
				InneresStrassenSegment segment = (InneresStrassenSegment) mq
						.getLinie();
				if (segment != null) {
					if (mq.getPid().contains("HFB")) {
						if (segment.getPid().contains("00000")) {
							System.err.println(mq.getPid() + ": "
									+ mq.getLinie().getPid() + ", Offset = "
									+ excelZahl(mq.getOffset()));

							if (segment.getVonSegment() != null) {
								System.err
										.println("\tPotentielle Verknüpfungen mit gleichen Vorgängersegment:");
								for (SystemObjekt segObj : strSeg) {
									InneresStrassenSegment seg = (InneresStrassenSegment) segObj;
									if (segment.getVonSegment().equals(
											seg.getVonSegment())) {
										if (seg.getNachSegment() != null) {
											System.err.println("\t" + seg);
										}
									}
								}
							}

							if (segment.getNachSegment() != null) {
								System.err
										.println("\tPotentielle Verknüpfungen mit gleichen Nachfolgersegment:");
								for (SystemObjekt segObj : strSeg) {
									InneresStrassenSegment seg = (InneresStrassenSegment) segObj;
									if (segment.getNachSegment().equals(
											seg.getNachSegment())) {
										if (seg.getVonSegment() != null) {
											System.err.println("\t" + seg);
										}
									}
								}
							}

						}
					}
				}
			}
		}

		for (SystemObjekt obj : mqs) {
			MessQuerschnittAllgemein mq = (MessQuerschnittAllgemein) obj;
			System.err.println(mq.getPid() + ";"
					+ excelZahl(mq.getKoordinate().getY()) + ";"
					+ excelZahl(mq.getKoordinate().getX()));
		}

		System.err.println(mqs.size() + " MQ einlesen in "
				+ (System.currentTimeMillis() - start) + " ms");

		System.exit(0);
	}

	/**
	 * {@inheritDoc}.<br>
	 * 
	 * @see de.bsvrz.sys.funclib.application.StandardApplication#parseArguments(de.bsvrz.sys.funclib.commandLineArgs.ArgumentList)
	 */
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		// keine zusätzlichen Argumente erwartet
	}
}
