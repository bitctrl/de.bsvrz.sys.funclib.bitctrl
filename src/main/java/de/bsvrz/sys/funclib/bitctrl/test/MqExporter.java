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

package de.bsvrz.sys.funclib.bitctrl.test;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.config.AttributeGroup;
import de.bsvrz.dav.daf.main.config.DataModel;
import de.bsvrz.dav.daf.main.config.SystemObject;
import de.bsvrz.sys.funclib.application.StandardApplication;
import de.bsvrz.sys.funclib.application.StandardApplicationRunner;
import de.bsvrz.sys.funclib.commandLineArgs.ArgumentList;

/**
 * Exportiert die Koordinaten der definierten MQ.
 *
 * @author BitCtrl Systems GmbH, Peuker
 */
public class MqExporter implements StandardApplication {

	/**
	 * Hauptfunktion der Anwendung.
	 *
	 * @param args
	 *            die übergebenen Argumente
	 */
	public static void main(final String[] args) {
		StandardApplicationRunner.run(new MqExporter(), args);
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

	@Override
	public void initialize(final ClientDavInterface connection)
			throws Exception {

		System.out.println("PidMQ" + ";" + "x" + ";" + "y");

		final DataModel model = connection.getDataModel();
		final AttributeGroup atg = model
				.getAttributeGroup("atg.punktKoordinaten");
		for (final SystemObject obj : model
				.getType("typ.messQuerschnittAllgemein").getElements()) {
			final Data datum = obj.getConfigurationData(atg);
			double x = 0;
			double y = 0;
			if (datum != null) {
				x = datum.getScaledValue("x").doubleValue();
				y = datum.getScaledValue("y").doubleValue();
			}

			System.out.println(
					obj.getPid() + ";" + excelZahl(x) + ";" + excelZahl(y));
		}
		System.exit(0);
	}

	@Override
	public void parseArguments(final ArgumentList argumentList)
			throws Exception {
		// keine zusätzlichen Argumente erwartet
	}
}
