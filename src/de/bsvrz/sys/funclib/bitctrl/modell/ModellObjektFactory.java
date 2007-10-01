/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.2 Straﬂensubsegmentanalyse
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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.modell;

import java.util.List;

import de.bsvrz.dav.daf.main.config.SystemObject;

/**
 * Schnittstelle f&uuml;r alle Fabriken die aus Systemobjekten vom
 * Datenverteiler die entsprechenden Objekte eines bestimmten Modells bauen.
 *
 * @author BitCtrl, Schumann
 * @version $Id$
 */
public interface ModellObjektFactory {

	/**
	 * Baut aus dem Systemobjekt des Datenverteilers das entsprechende Objekt
	 * des Datenmodells.
	 *
	 * @param obj
	 *            Ein Systemobjekt vom Datenverteiler
	 * @return Das passende Objekt aus dem Modell oder {@code null}, wenn
	 *         keines fabriziert werden kann
	 */
	SystemObjekt getInstanz(SystemObject obj);

	/**
	 * Gibt eine Liste aller gecachten Systemobjekte zur&uuml;ck.
	 *
	 * @return Liste von Systemobjekten
	 */
	List<SystemObjekt> getInstanzen();

	/**
	 * Gibt die Liste der Typen zur&uuml;ck, die von der Fabrik erzeugt werden
	 * k&ouml;nnen.
	 *
	 * @return Liste von Typen
	 */
	SystemObjektTyp[] getTypen();

}
