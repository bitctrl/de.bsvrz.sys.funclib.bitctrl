/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.1 Ganglinienprognose
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

package de.bsvrz.sys.funclib.bitctrl.kalender;

import java.util.EventListener;

/**
 * Eine Applikation kann diese Schnittstelle implementieren, wenn sie &uuml;ber
 * Kalenderereignisse informiert werden m&ouml;chte.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public interface KalenderListener extends EventListener {

	/**
	 * Wird aufgerufen, wenn die Antwort auf eine Kalenderanfrage eingetroffen
	 * ist.
	 * 
	 * @param e
	 *            das Kalenderevent, welches die Antwort beinhaltet.
	 */
	void antwortEingetroffen(KalenderEvent e);

}
