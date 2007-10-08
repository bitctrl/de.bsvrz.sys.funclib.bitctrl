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
 * Wei�enfelser Stra�e 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.verkehr;

/**
 * Repr�sentation eines Koordinatenpunktes f�r geografisch bezogene Objekte
 * innerhalb der Datenverteilerkonfiguration.
 * 
 * @author BitCtrl Systems GmbH, Peuker
 * @version $Id$
 */
public class Punkt {
	/**
	 * die X-Koordinate.
	 */
	private final double x;

	/**
	 * die Y-Koordinate.
	 */
	private final double y;

	/**
	 * Konstruktor.<br>
	 * Die Funktion erzeugt einen Punkt mit den Koordinaten x und y.
	 * 
	 * @param x
	 *            die X-Koordinate
	 * @param y
	 *            die Y-Koordinate
	 */
	public Punkt(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * liefert die X-Koordinate des Punkts.
	 * 
	 * @return die Koordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * liefert die Y-Koordinate des Punkts.
	 * 
	 * @return die Koordinate
	 */
	public double getY() {
		return y;
	}
}
