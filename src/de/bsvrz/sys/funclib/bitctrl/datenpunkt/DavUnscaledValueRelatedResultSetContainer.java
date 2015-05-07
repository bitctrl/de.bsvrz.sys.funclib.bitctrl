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

import de.bsvrz.dav.daf.main.ClientDavInterface;
import de.bsvrz.dav.daf.main.Data.Array;
import de.bsvrz.dav.daf.main.impl.InvalidArgumentException;
import de.bsvrz.sys.funclib.debug.Debug;

/**
 * Beispielhafte Containerimplementierung für ganzzahlige Ergebnisse.
 *
 * @author BitCtrl Systems GmbH, Albrecht Uhlmann
 */
public class DavUnscaledValueRelatedResultSetContainer implements
IRelatedResultSetContainer {

	/**
	 * Der Debug-Logger.
	 */
	private static final Debug DEBUG = Debug.getLogger();

	private final DavUnscaledValueRelatedResultSet resultSet;

	/**
	 * Konstruktor legt intern das eigentliche Rgebnis-Set an.
	 *
	 * @param connection
	 *            Verbindung zum Datenverteiler
	 * @param atlDatenpunktArray
	 *            ein Feld von atl.datenpunkt
	 * @throws InvalidArgumentException
	 *             Falls eines der Feldelemente ungültig ist
	 * @see Datenpunkt
	 */
	public DavUnscaledValueRelatedResultSetContainer(
			final ClientDavInterface connection, final Array atlDatenpunktArray)
					throws InvalidArgumentException {
		resultSet = new DavUnscaledValueRelatedResultSet(this, 6000,
				connection, atlDatenpunktArray);
	}

	@Override
	public void resultSetComplete(final boolean timeout) {
		DEBUG.warning("timeout=" + timeout);
		System.out.println("timeout=" + timeout + "\nResultset=" + resultSet);
	}

}
