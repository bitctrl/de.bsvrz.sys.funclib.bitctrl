/*
 * Segment 5 Intelligente Analyseverfahren, SWE 5.5 Funktionen Ganglinie
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

package de.bsvrz.sys.funclib.bitctrl.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bitctrl.util.Interval;

/**
 * Testet relevante Funktionen der Klasse.
 * 
 * @author BitCtrl Systems GmbH, Schumann
 * @version $Id$
 */
public class TestIntervall {

	/**
	 * Testet pro forma die Getter-Methoden.
	 */
	@Test
	public void testGetter() {
		Interval a;

		a = new Interval(3, 6);
		assertEquals(3L, a.getStart());
		assertEquals(6L, a.getEnd());
	}

	/**
	 * Wenn Start und Ende vertauscht sind, also Start gr&ouml;&szlig;er als
	 * Ende, dann muss eine Exception geworfen werden.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testKonstruktor() {
		new Interval(8, 2);
	}

}
