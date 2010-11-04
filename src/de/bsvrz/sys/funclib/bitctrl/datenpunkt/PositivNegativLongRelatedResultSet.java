/*
 * BitCtrl- Datenverteiler- Funktionsbibliothek
 * Copyright (C) 2007-2010 BitCtrl Systems GmbH 
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
import de.bsvrz.dav.daf.main.Data;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;

/**
 * Zusammenhängende Menge von Long-Ergebnissen einer atl.positivNegativListe.
 * 
 * @author BitCtrl Systems GmbH, Albrecht Uhlmann
 * @version $Id$
 */
public class PositivNegativLongRelatedResultSet extends RelatedResultSet<Long> {

	/**
	 * Konstruktor wertet beide Listen aus und lädt die Objekte beider in die
	 * Menge.
	 * 
	 * @param container
	 *            übergeordneter Container, der bei Vollständigkeit aller
	 *            Ergebnisse oder Timeout benachrichtigt wird.
	 * @param timeoutMs
	 *            Timeout in Millisekunden
	 * @param connection
	 *            Verbindung zum Datenverteiler
	 * @param atlPositivNegativListe
	 *            die Liste. Darf nicht <code>null</code> sein.
	 * @throws InvalidArgumentException
	 *             Falls bei der Anmeldung etwas fehlschlägt
	 * @see Datenpunkt
	 */
	public PositivNegativLongRelatedResultSet(
			final IRelatedResultSetContainer container, final int timeoutMs,
			final ClientDavInterface connection,
			final Data atlPositivNegativListe) throws InvalidArgumentException {
		super(container, timeoutMs);
		final Data.Array atlPositivArray = atlPositivNegativListe
				.getArray("Positivliste");
		final Data.Array atlNegativArray = atlPositivNegativListe
				.getArray("Negativliste");
		int loop;
		for (loop = 0; loop < atlPositivArray.getLength(); ++loop) {
			final DavUnscaledValueIndividualResult r = new DavUnscaledValueIndividualResult(
					this);
			r.setConnection(connection);
			r.setAtlDatenpunkt(atlPositivArray.getItem(loop));
		}
		for (loop = 0; loop < atlNegativArray.getLength(); ++loop) {
			final DavUnscaledValueIndividualResult r = new DavUnscaledValueIndividualResult(
					this);
			r.setConnection(connection);
			r.setAtlDatenpunkt(atlNegativArray.getItem(loop));
		}
	}

}
