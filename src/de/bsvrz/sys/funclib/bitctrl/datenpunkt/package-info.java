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

/**
 * Bibliothek zur Realisierung der Anforderung, dass man Daten von n Objekten
 * erwartet und weitermachen will, wenn
 * <ul>
 * <li>Alle Werte da sind</li>
 * <li>Eine maximale Wartezeit abgelaufen ist, nachdem der ERSTE Wert da ist.</li>
 * </ul>
 * für Objekte, die ihre Daten vom Datenverteiler beziehen.
 */
package de.bsvrz.sys.funclib.bitctrl.datenpunkt;

