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

package de.bsvrz.sys.funclib.bitctrl.util;

import java.util.ArrayList;
import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.modell.verkehr.objekte.StrassenSegment;

/**
 * Exception die geworfen wird, wenn eine Schleife in der Suche über
 * Strassensegmente innerhalb eines Netzes ermittelt wurde.
 *
 * @author BitCtrl Systems GmbH, Uwe Peuker
 */
public class SearchCycleException extends Exception {

	/** die Liste der bei der Sucje durchlaufenen Segmente. */
	private final List<StrassenSegment> visitedSegments = new ArrayList<>();

	/**
	 * liefert die Liste der bei der Suche durchlaufenen StrassenSegmente.
	 *
	 * @return die Liste
	 */
	public List<StrassenSegment> getVisitedSegments() {
		return visitedSegments;
	}

	/**
	 * Konstruktor.
	 *
	 * @param string
	 *            der Meldungstext
	 * @param visited
	 *            die Liste der durchlaufenen Strassensegmente
	 */
	public SearchCycleException(final String string,
			final List<StrassenSegment> visited) {
		super(string);
		visitedSegments.addAll(visited);
	}
}
