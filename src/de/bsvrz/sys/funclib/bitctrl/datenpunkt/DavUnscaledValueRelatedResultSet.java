/*
 * BitCtrl-Funktionsbibliothek
 * Copyright (C) 2015 BitCtrl Systems GmbH
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
package de.bsvrz.sys.funclib.bitctrl.datenpunkt;

import com.bitctrl.util.resultset.IRelatedResultSetContainer;
import com.bitctrl.util.resultset.RelatedResultSet;

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.ResultData;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;

/**
 * Beispielimplementierung von einer zusammenhängenden Menge von
 * Long-Ergebnissen.
 *
 * @author BitCtrl Systems GmbH, Albrecht Uhlmann
 */
public class DavUnscaledValueRelatedResultSet extends
		RelatedResultSet<Long, ResultData> {

	/**
	 * Konstruktor erzeugt für jedes Feldelement aus der atl.datenpunkt ein
	 * individuelles Objekt und registriert es mit uns.
	 *
	 * @param container
	 *            übergeordneter Container, der bei Vollständigkeit aller
	 *            Ergebnisse oder Timeout benachrichtigt wird.
	 * @param timeoutMs
	 *            Timeout in Millisekunden
	 * @param connection
	 *            Verbindung zum Datenverteiler
	 * @param atlDatenpunktArray
	 *            eine variabel lange Liste von Datenpunkten
	 * @throws InvalidArgumentException
	 *             Falls bei der Anmeldung etwas fehlschlägt
	 * @see Datenpunkt
	 */
	public DavUnscaledValueRelatedResultSet(
			final IRelatedResultSetContainer container, final int timeoutMs,
			final ClientDavInterface connection, final Array atlDatenpunktArray)
			throws InvalidArgumentException {
		super(container, timeoutMs);
		int loop;
		for (loop = 0; loop < atlDatenpunktArray.getLength(); ++loop) {
			final DavUnscaledValueIndividualResult r = new DavUnscaledValueIndividualResult(
					this);
			r.setConnection(connection);
			r.setAtlDatenpunkt(atlDatenpunktArray.getItem(loop));
		}
	}

}
