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

package de.bsvrz.sys.funclib.bitctrl.modell.geo.objekte;

import java.util.List;

/**
 * Schnittstelle für die Repräsentation eines Komplex-Objektes
 * ("typ.komplexXY").
 * 
 * @author BitCtrl Systems GmbH, Uwe Peuker
 * @version $Id$
 */
public interface KomplexXY extends Komplex {

	/**
	 * liefert die Liste der Flächen aus denen das komplexe Element
	 * zusammengesetzt ist.
	 * 
	 * @return die Liste der Flächen
	 */
	List<Flaeche> getFlaechen();

	/**
	 * liefert die Liste der komplexen Element aus denen das komplexe Element
	 * zusammengesetzt ist.
	 * 
	 * @return die Liste der komplexen Elemente
	 */
	List<Komplex> getKomplexe();

	/**
	 * liefert die Liste der Linien aus denen das komplexe Element
	 * zusammengesetzt ist.
	 * 
	 * @return die Liste der Linien
	 */
	List<Linie> getLinien();

	/**
	 * liefert die Liste der Punkte aus denen das komplexe Element
	 * zusammengesetzt ist.
	 * 
	 * @return die Liste der Punkte
	 */
	List<Punkt> getPunkte();
}
