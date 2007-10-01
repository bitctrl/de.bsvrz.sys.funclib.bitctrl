/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straßensubsegmentanalyse 
 * Copyright (C) 2007 BitCtrl Systems GmbH 
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Contact Information:
 * BitCtrl Systems GmbH
 * Weißenfelser Straße 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell.umfelddaten;

import java.util.List;

import de.bsvrz.sys.funclib.bitctrl.modell.SystemObjekt;

/**
 * Dient der einheitlichen Behandlung aller Umfelddatensensoren.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface UmfelddatenSensor extends SystemObjekt {

	/**
	 * Gibt die Umfelddatenmessstellen zur&uuml;ck, zu der der Umfelddatensensor
	 * geh&ouml;hrt. Die Umfelddatensensor und -messstelle m&uuml;ssen im selben
	 * Konfigurationsbereich liegen.
	 * 
	 * @return Liste von Umfelddatenmessstellen
	 */
	List<UmfelddatenMessstelle> getUmfelddatenMessstellen();

}
