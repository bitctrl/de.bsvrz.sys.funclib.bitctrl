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
 * Weiﬂenfelser Straﬂe 67
 * 04229 Leipzig
 * Phone: +49 341-490670
 * mailto: info@bitctrl.de
 */

package de.bsvrz.sys.funclib.bitctrl.daf;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Testet die Methoden der Klasse.
 * 
 * @author BitCtrl Systems GmbH, Falko Schumann
 * @version $Id$
 */
public class DavToolsTest {

	/**
	 * Testet die Methode {@link DavTools#generierePID(String, String)}.
	 */
	@Test
	public void testGenerierePID() {
		String s, name, praefix;

		name = "asf";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.asf", s);

		name = "ASF";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.aSF", s);

		name = "a s f";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.aSF", s);

		name = "A s f";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.aSF", s);

		name = "einKleinerTest";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.einKleinerTest", s);

		name = "EinKleinerTest";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.einKleinerTest", s);

		name = "Ein kleiner Test";
		praefix = "pre.";
		s = DavTools.generierePID(name, praefix);
		assertEquals("pre.einKleinerTest", s);
	}

}
